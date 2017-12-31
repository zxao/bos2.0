package com.itheima.bos.web.action.base;

import cn.itcast.crm.domain.Customer;
import com.itheima.bos.domain.base.FixedArea;
import com.itheima.bos.service.base.IFixedAreaService;
import com.itheima.bos.utils.BaseAction;
import com.opensymphony.xwork2.ActionContext;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;

import javax.ws.rs.core.MediaType;
import java.util.Collection;
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
     *
     * @return
     */
    @Action(value = "fixedArea_save", results = {@Result(type = "redirect", location = "./pages/base/fixed_area.html")})
    public String fixedAreaSave() {
        fixedAreaService.save(model);
        return SUCCESS;
    }

    /**
     * 定区的分页条件查询
     *
     * @return
     */
    @Action(value = "fixedArea_queryPage", results = {@Result(type = "json")})
    public String fixedAreaQueryPage() {
        Page<FixedArea> pageDate = fixedAreaService.fixedAreaQueryPage(page, rows, model);
        //将页面数据压入值栈中
        this.pushPageDataToValueStack(pageDate);
        return SUCCESS;
    }

    /**
     * 定区查询所有
     *
     * @return
     */
    @Action(value = "fixedArea_findAll", results = {@Result(type = "json")})
    public String fixedAreaFindAll() {
        List<FixedArea> fixedAreaList = fixedAreaService.fixedAreaFindAll();
        ActionContext.getContext().getValueStack().push(fixedAreaList);
        return SUCCESS;
    }

    /**
     * 查询没有关联定区的客户列表
     *
     * @return
     */
    @Action(value = "fixedArea_findNoassociationCustomers", results = {@Result(type = "json")})
    public String findNoassociationCustomers() {
        Collection<? extends Customer> customers = WebClient.create("http://localhost:9001/crm_management/services/customerService/noAssociationCustomers").accept(MediaType.APPLICATION_JSON_TYPE).getCollection(Customer.class);

        //压入值栈中
        ActionContext.getContext().getValueStack().push(customers);

        return SUCCESS;
    }

    /**
     * 查询关联指定定区的客户列表
     * @return
     */
    @Action(value = "fixedArea_findAssociationCustomers",results = {@Result(type = "json")})
    public String findAssociationCustomers() {

        Collection<? extends Customer> customers = WebClient.create("http://localhost:9001/crm_management/services/customerService/hasAssocationCustomers/" + model.getId()).accept(MediaType.APPLICATION_JSON_TYPE).type(MediaType.APPLICATION_JSON_TYPE).getCollection(Customer.class);

        ActionContext.getContext().getValueStack().push(customers);
        return SUCCESS;
    }


}
