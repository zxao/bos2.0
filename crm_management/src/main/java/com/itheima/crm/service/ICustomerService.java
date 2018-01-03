package com.itheima.crm.service;

import cn.itcast.crm.domain.Customer;

import javax.ws.rs.*;
import java.util.List;

/**
 * 编写webservice服务接口
 */
public interface ICustomerService {

    /**
     * 查询所有未关联定区的客户列表
     * @return
     */
    @Path("/noAssociationCustomers")
    @GET
    @Produces({"application/xml","application/json"})
    public List<Customer> findNoAssociationCustomers();

    /**
     * 已经关联到指定定区的客户列表
     */
    @Path("/hasAssocationCustomers/{fixedAreaId}")
    @GET
    @Produces({"application/xml","application/json"})
    public List<Customer> findHasAssociationCustomers(@PathParam("fixedAreaId") String fixedAreaId);

    /**
     * 将客户关联到定区上，所有的客户id拼接成以逗号分隔的字符串
     */
    @Path("/associationCustomerToFixedArea")
    @PUT
    @Consumes({"application/xml","application/json"})
    public void associationCustomerToFixedArea(@QueryParam("fixedAreaId") String fixedAreaId,@QueryParam("customerIdStr") String customerIdStr);
}
