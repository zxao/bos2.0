package com.itheima.fore.action;

import cn.itcast.crm.domain.Customer;
import com.itheima.bos.domain.base.Area;
import com.itheima.bos.domain.constant.Constants;
import com.itheima.bos.domain.take_delivery.Order;
import com.itheima.utils.BaseAction;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.ws.rs.core.MediaType;

@Controller
@Scope("prototype")
@ParentPackage("json-default")
@Namespace("/")
public class OrderAction extends BaseAction<Order> {

    //属性封装某些字段
    private String sendAreaInfo;
    private String recAreaInfo;

    public void setSendAreaInfo(String sendAreaInfo) {
        this.sendAreaInfo = sendAreaInfo;
    }

    public void setRecAreaInfo(String recAreaInfo) {
        this.recAreaInfo = recAreaInfo;
    }


    /**
     * 订单的保存操作
     * @return
     */

    @Action(value = "order_add",results = {@Result(name="success",type="redirect",location = "index.html")})
    public String orderSave(){
        Area sendArea = new Area();
        String[] send = sendAreaInfo.split("/");
        sendArea.setProvince(send[0]);
        sendArea.setCity(send[1]);
        sendArea.setDistrict(send[2]);

        Area recArea = new Area();
        String[] rec = sendAreaInfo.split("/");
        recArea.setProvince(send[0]);
        recArea.setCity(send[1]);
        recArea.setDistrict(send[2]);

        model.setSendArea(sendArea);
        model.setRecArea(recArea);

        //关联客户信息
        Customer customer = (Customer) ServletActionContext.getRequest().getSession().getAttribute("customer");
        model.setCustomer_id(customer.getId());


        //使用webService bos_management保存订单
        WebClient.create(Constants.BOS_MANAGEMENT_URL+"/services/orderService/order/orderSave").type(MediaType.APPLICATION_JSON_TYPE).post(model);

        return SUCCESS;
    }

}
