package com.itheima.bos.web.action.base;

import com.itheima.bos.domain.base.Standard;
import com.itheima.bos.service.base.IStandardService;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import org.apache.struts2.convention.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;

import java.awt.print.PageFormat;

import java.awt.print.Printable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Namespace("/")
@ParentPackage("json-default")
@Actions
@Scope("prototype")
public class StandardAction extends ActionSupport implements ModelDriven<Standard>{

    private Standard standard = new Standard();
    @Override
    public Standard getModel() {
        return standard;
    }

    @Autowired
    private IStandardService standardService;

    @Action(value = "standard_save",results = {@Result(type = "redirect",location = "./pages/base/standard.html")})
    public String standardSave(){

        standardService.save(standard);
        return SUCCESS;
    }

    private int page;
    private int rows;

    public void setPage(int page) {
        this.page = page;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    @Action(value = "standard_findPage",results = {@Result(type = "json")})
    public String standardFindPage() {
        Pageable pageable = new PageRequest(page - 1,rows);
        Page<Standard> pageData = standardService.findPage(pageable);
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("total",pageData.getTotalElements());
        map.put("rows",pageData.getContent());
        ActionContext.getContext().getValueStack().push(map);
        return SUCCESS;
    }

    @Action(value = "standard_findAll",results = {@Result(type = "json")})
    public String standardFindAll() {
        List<Standard> standards = standardService.findAll();
        ActionContext.getContext().getValueStack().push(standards);
        return SUCCESS;
    }



}
