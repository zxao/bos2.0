package com.itheima.bos.web.action.base;

import com.itheima.bos.domain.base.FixedArea;
import com.itheima.bos.service.base.IFixedAreaService;
import com.itheima.bos.utils.BaseAction;
import com.opensymphony.xwork2.ActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;

import java.util.List;

@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class fixedAreaAction extends BaseAction<FixedArea> {

    @Autowired
    private IFixedAreaService fixedAreaService;

    /**
     * 定区的添加操作
     * @return
     */
    @Action(value = "fixedArea_save",results = {@Result(type="redirect",location = "./pages/base/fixed_area.html")})
    public String fixedAreaSave() {
        fixedAreaService.save(model);
        return SUCCESS;
    }

    /**
     * 定区的分页条件查询
     * @return
     */
    @Action(value = "fixedArea_queryPage",results = {@Result(type = "json")})
    public String fixedAreaQueryPage() {
        Page<FixedArea> pageDate = fixedAreaService.fixedAreaQueryPage(page,rows,model);
        //将页面数据压入值栈中
        this.pushPageDataToValueStack(pageDate);
        return SUCCESS;
    }

    @Action(value = "fixedArea_findAll",results = {@Result(type = "json")})
    public String fixedAreaFindAll() {
        List<FixedArea> fixedAreaList = fixedAreaService.fixedAreaFindAll();
        ActionContext.getContext().getValueStack().push(fixedAreaList);
        return SUCCESS;
    }



}
