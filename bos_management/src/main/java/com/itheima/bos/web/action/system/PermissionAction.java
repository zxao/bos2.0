package com.itheima.bos.web.action.system;

import com.itheima.bos.domain.system.Permission;
import com.itheima.bos.service.system.IPermissionService;
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
public class PermissionAction extends BaseAction<Permission> {

    @Autowired
    private IPermissionService permissionService;

    @Action(value = "permission_list",results = {@Result(type = "json")})
    public String permissinList() {
        List<Permission> permissions = permissionService.findAll();
        ActionContext.getContext().getValueStack().push(permissions);
        return SUCCESS;
    }

    @Action(value = "permission_save",results = {@Result(type = "redirect",location = "./pages/system/permission.html")})
    public String permissionSave() {

        permissionService.save(model);
        return SUCCESS;

    }

}
