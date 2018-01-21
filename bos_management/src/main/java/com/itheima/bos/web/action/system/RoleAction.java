package com.itheima.bos.web.action.system;

import com.itheima.bos.domain.system.Role;
import com.itheima.bos.service.system.IRoleService;
import com.itheima.bos.utils.BaseAction;
import com.opensymphony.xwork2.ActionContext;
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
public class RoleAction extends BaseAction<Role> {

    @Autowired
    private IRoleService roleService;

    @Action(value = "role_list",results = {@Result(type = "json")})
    public String roleList() {
        List<Role> roles = roleService.findAll();
        ActionContext.getContext().getValueStack().push(roles);
        return SUCCESS;
    }

    private String menuIds;
    private String[] permissionIds;

    public void setMenuIds(String menuIds) {
        this.menuIds = menuIds;
    }

    public void setPermissionIds(String[] permissionIds) {
        this.permissionIds = permissionIds;
    }

    @Action(value = "role_save",results = {@Result(name = "success",type = "redirect",location = "./pages/system/role.html")})
    public String roleSave() {
        roleService.roleSave(model,menuIds,permissionIds);
        return SUCCESS;
    }
}
