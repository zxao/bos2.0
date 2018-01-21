package com.itheima.bos.web.action.delivery;

import com.itheima.bos.domain.take_delivery.Order;
import com.itheima.bos.service.delivery.IOrderService;
import com.itheima.bos.utils.BaseAction;
import com.opensymphony.xwork2.ActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.Map;
@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class OrderAction extends BaseAction<Order> {

    //注入orderService
    @Autowired
    private IOrderService orderService;

    /**
     * 根据orderNum查询订单信息
     * @return
     */
    @Action(value = "order_findByOrderNum",results = {@Result(type = "json")})
    public String findByOrderNum(){
        Map<String,Object> result = new HashMap<String,Object>();
        try{
            Order order = orderService.findByOrderNum(model.getOrderNum());
            //保存订单成功
            result.put("success",true);
            result.put("orderData",order);
        }catch (Exception e){
            //保存订单失败
            result.put("success",false);
            e.printStackTrace();
        }
        //将结果集压入值栈
        ActionContext.getContext().getValueStack().push(result);
        return SUCCESS;
    }
}
