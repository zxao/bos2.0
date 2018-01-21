package com.itheima.bos.service.delivery;

import com.itheima.bos.domain.take_delivery.Order;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

public interface IOrderService {

    @Path("/order/orderSave")
    @POST
    @Consumes({"application/xml,application/json"})
    public void orderSave(Order order);

    Order findByOrderNum(String orderNum);
}
