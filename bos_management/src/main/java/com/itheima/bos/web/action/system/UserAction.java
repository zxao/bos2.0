package com.itheima.bos.web.action.system;

import com.itheima.bos.domain.system.User;
import com.itheima.bos.service.system.IUserService;
import com.itheima.bos.utils.BaseAction;
import com.opensymphony.xwork2.ActionContext;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class UserAction extends BaseAction<User> {

    @Autowired
    private IUserService userService;

    @Action(value = "user_login", results = {@Result(name = "success", type = "json"), @Result(name = "login", type = "json")})
    public String userLogin() {
        //将用户名与密码存入shiro的subject中
        //获得subject
        Subject subject = SecurityUtils.getSubject();
        //将用户名和密码封装到token中
        AuthenticationToken token = new UsernamePasswordToken(model.getUsername(),model.getPassword());
        Map<String,Object> result = new HashMap<String,Object>();
        try {
            subject.login(token);
            //登录成功
            result.put("success",true);
            result.put("msg","登录成功！");
            ActionContext.getContext().getValueStack().push(result);
            return SUCCESS;
        } catch (Exception e) {
            String msg = "";
            if(e instanceof UnknownAccountException){
                //用户名错误
                msg = "用户名不存在";
            }
            if(e instanceof IncorrectCredentialsException){
                //密码错误
                msg = "用户名与密码不一致！";
            }
            result.put("success",false);
            result.put("msg",msg);
            ActionContext.getContext().getValueStack().push(result);
            //登录失败
            return LOGIN;
        }
    }

    @Action(value = "user_logout",results = {@Result(name = "success",type = "redirect",location = "/login.action")})
    public String userLogout() {
        //获得subject
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return SUCCESS;
    }

    @Action(value = "user_list",results = {@Result(name = "success",type = "json")})
    public String userList() {
        List<User> users = userService.findAll();
        ActionContext.getContext().getValueStack().push(users);
        return SUCCESS;
    }

    private String[] roleIds;

    public void setRoleIds(String[] roleIds) {
        this.roleIds = roleIds;
    }

    @Action(value = "user_save",results = {@Result(name = "success",type = "redirect",location = "./pages/system/userlist.html")})
    public String userSave() {
        userService.save(roleIds,model);
        return SUCCESS;
    }

}
