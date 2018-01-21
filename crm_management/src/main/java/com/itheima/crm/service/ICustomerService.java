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

    /**
     * 客户信息的注册
     * @param customer
     */
    @Path("/regist")
    @POST
    @Consumes({"application/xml","application/json"})
    public void regist(Customer customer);

    /**
     * 根据电话号码查询客户
     */
    @Path("customer/findByTelephone/{telephone}")
    @GET
    @Consumes({"application/xml","application/json"})
    @Produces({"application/xml","application/json"})
    public Customer findByTelephone(@PathParam("telephone") String telephone);

    /**
     * 根据手机号修改激活状态
     * @param telephone
     */
    @Path("customer/updateTypeByTelephone/{telephone}")
    @PUT
    @Consumes({"application/xml","application/json"})
    public void updataTypeByTelephone(@PathParam("telephone") String telephone);

    /**
     * 客户登录
     * @param telephone
     * @param password
     * @return
     */
    @Path("/customer/customerLogin")
    @GET
    @Consumes({"application/xml","application/json"})
    @Produces({"application/xml","application/json"})
    public Customer customerLogin(@QueryParam("telephone") String telephone,@QueryParam("password") String password);

    @Path("/customer/findFixedAreaIdByAddress")
    @GET
    @Consumes({"application/xml","application/json"})
    @Produces({"application/xml","application/json"})
    public String findfixedAreaIdByAddress(@QueryParam("address") String address);



}
