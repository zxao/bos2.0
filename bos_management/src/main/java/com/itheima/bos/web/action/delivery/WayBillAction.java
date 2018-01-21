package com.itheima.bos.web.action.delivery;

import com.itheima.bos.domain.take_delivery.WayBill;
import com.itheima.bos.service.delivery.IWayBillService;
import com.itheima.bos.utils.BaseAction;
import com.opensymphony.xwork2.ActionContext;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.Map;

/**
 * 运单管理
 */
@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class WayBillAction extends BaseAction<WayBill> {

    //注入wayBillService
    @Autowired
    private IWayBillService wayBillService;

    private static final Logger LOGGER = Logger.getLogger(WayBillAction.class);


    @Action(value = "waybill_save",results = {@Result(type = "json")})
    public String waybillSave() {
        Map<String,Object> result = new HashMap<String,Object>();
        try{
            //判断是否有订单
            if(model.getOrder()!=null && (model.getOrder().getId()==null || model.getOrder().getId()==0)){
                //说明订单不存在
                model.setOrder(null);
            }

            wayBillService.save(model);
            //保存成功
            result.put("success",true);
            result.put("msg","运单保存成功");
            LOGGER.info("保存运单成功，运单号为："+model.getWayBillNum());

        }catch (Exception e){
            //保存失败
            e.printStackTrace();
            result.put("success",false);
            result.put("msg","运单保存失败");
            LOGGER.info("保存运单失败，运单号为："+model.getWayBillNum());
        }

        //将结果集压入值栈
        ActionContext.getContext().getValueStack().push(result);

        return SUCCESS;
    }

    @Action(value = "wayBill_wayBillQueryPage",results = {@Result(type = "json")})
    public String waybillPageQuery(){

        Pageable pageable = new PageRequest(page-1,rows,new Sort(new Sort.Order(Sort.Direction.DESC,"id")));
        Page<WayBill> pageData = wayBillService.findQueryPage(model,pageable);
        pushPageDataToValueStack(pageData);

        return SUCCESS;
    }
    @Action(value = "wayBill_findByWayBillNum",results = {@Result(type = "json")})
    public String findByWayBillNum(){
        Map<String,Object> result = new HashMap<String,Object>();
        try{
            WayBill wayBill = wayBillService.findByWayBillNum(model.getWayBillNum());
            //保存订单成功
            result.put("success",true);
            result.put("wayBillData",wayBill);
        }catch (Exception e){
            //保存订单失败
            result.put("success",false);
            e.printStackTrace();
        }
        //将结果集压入值栈
        ActionContext.getContext().getValueStack().push(result);
        return SUCCESS;


    }

    @Action(value = "wayBill_wayBillSave",results = {@Result(type = "json")})
    public String wayBillSave(){
        Map<String,Object> result = new HashMap<String,Object>();
        try{
            //判断订单的id是否存在

           wayBillService.save(model);
            //保存订单成功
            result.put("success",true);
            result.put("msg","保存运单成功");
        }catch (Exception e){
            result.put("success",false);
            result.put("msg","保存运单失败");
            e.printStackTrace();
        }
        ActionContext.getContext().getValueStack().push(result);

        return SUCCESS;
    }
}
