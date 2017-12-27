package com.itheima.bos.web.action.base;

import com.itheima.bos.domain.base.Courier;
import com.itheima.bos.service.base.ICourierService;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Controller;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class CourierAction extends ActionSupport implements ModelDriven<Courier>{
    private Courier courier = new Courier();

    @Override
    public Courier getModel() {
        return courier;
    }
    @Autowired
    private ICourierService courierService;

    @Action(value = "courier_save",results = {@Result(type = "redirect",location = "./pages/base/courier.html")})
    public String courierSave() {
        courierService.save(courier);
        return SUCCESS;
    }

    //接收参数page和rows
    private int page;
    private int rows;

    public void setPage(int page) {
        this.page = page;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    @Action(value = "courier_queryPage",results = {@Result(type = "json")})
    public String courierQueryPage(){
        Specification<Courier> specification = new Specification<Courier>() {
            @Override
            public Predicate toPredicate(Root<Courier> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                
                //使用List集合存储条件
                List<Predicate> list = new ArrayList<Predicate>();
                
                //courierNum
                if(StringUtils.isNotBlank(courier.getCourierNum())){//如果courierNum不为空
                    Predicate p1 = cb.equal(root.get("courierNum").as(String.class), courier.getCourierNum());
                    list.add(p1);
                }
                //company
                if(StringUtils.isNotBlank(courier.getCompany())){
                    Predicate p2 = cb.like(root.get("company").as(String.class), "%" + courier.getCompany() + "%");
                    list.add(p2);
                }
                //type
                if(StringUtils.isNotBlank(courier.getType())){
                    Predicate p3 = cb.equal(root.get("type").as(String.class), courier.getType());
                    list.add(p3);
                }

                //standard
                //使用courier关联standard
                Join<Object, Object> standardRoot = root.join("standard", JoinType.INNER);
                if(courier.getStandard()!=null && StringUtils.isNotBlank(courier.getStandard().getName())){
                    Predicate p4 = cb.like(standardRoot.get("name").as(String.class), "%" + courier.getStandard().getName() + "%");
                    list.add(p4);
                }

                Predicate and = cb.and(list.toArray(new Predicate[0]));


                return and;
            }
        };

        //封装page和rows到pageable中
        Pageable pageable = new PageRequest(0,20);
        //调用courierService的findAll(pageable)
        Page<Courier> pageData = courierService.queryPage(specification,pageable);
        //将pageDate序列化为json格式
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("total",pageData.getTotalElements());
        map.put("rows",pageData.getContent());
        //使用struts2的json工具，将要转换为json的对象压入valuestack的栈顶
        ActionContext.getContext().getValueStack().push(map);
        return SUCCESS;
    }

    private String ids;

    public void setIds(String ids) {
        this.ids = ids;
    }

    @Action(value = "courier_deleteBatch",results = {@Result(type = "redirect",location = "./pages/base/courier.html")})
    public String courierDeleteBatch() {

        courierService.courierDeleteBatch(ids);
        return SUCCESS;
    }

    @Action(value = "courier_restore",results = {@Result(type = "redirect",location = "./pages/base/courier.html")})
    public String courierRestore() {

        courierService.courierRestore(ids);
        return SUCCESS;
    }



}
