package com.itheima.bos.web.action.system;

import com.itheima.bos.domain.system.Menu;
import com.itheima.bos.domain.system.User;
import com.itheima.bos.service.system.IMenuService;
import com.itheima.bos.utils.BaseAction;
import com.opensymphony.xwork2.ActionContext;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import java.util.List;


@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class MenuAction extends BaseAction<Menu> {

    //注入menuService
    @Autowired
    private IMenuService menuService;

    /**
     * 菜单列表的查询
     * @return
     */
    @Action(value = "menu_list",results = {@Result(type = "json")})
    public String menuList() {
        //查询所有的菜单列表
        List<Menu> menus = menuService.findAll();
        //将menus压入值栈
        ActionContext.getContext().getValueStack().push(menus);
        return SUCCESS;
    }

    @Action(value = "menu_save",results = {@Result(name = "success",type = "redirect",location = "./pages/system/menu.html")})
    public String menuSave() {
        //保存menu信息
        menuService.save(model);
        return SUCCESS;
    }

    @Action(value = "menu_showMenu",results = {@Result(type = "json")})
    public String showMenu() {
        //获取当前用户
        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getPrincipal();
        List<Menu> menus = menuService.showMenu(user);
        ActionContext.getContext().getValueStack().push(menus);
        return  SUCCESS;
    }

}
