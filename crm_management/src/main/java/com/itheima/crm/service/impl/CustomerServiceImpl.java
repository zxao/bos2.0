package com.itheima.crm.service.impl;

import cn.itcast.crm.domain.Customer;
import com.itheima.crm.dao.ICustomerRepository;
import com.itheima.crm.service.ICustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@Transactional
public class CustomerServiceImpl implements ICustomerService {
    //注入customerRepository
    @Autowired
    private ICustomerRepository customerRepository;

    //查询没有关联定区的用户列表
    @Override
    public List<Customer> findNoAssociationCustomers() {
        return customerRepository.findByFixedAreaIdIsNull();
    }

    //查询已经关联了定区的客户列表
    @Override
    public List<Customer> findHasAssociationCustomers(String fixedAreaId) {
        return customerRepository.findByFixedAreaId(fixedAreaId);
    }

    //将客户与定区进行关联
    @Override
    public void associationCustomerToFixedArea(String fixedAreaId, String customerIdStr) {

        customerRepository.clearAssociationCustomerToFixedArea(fixedAreaId);
        if(!customerIdStr.equals("null")){
            //遍历客户的id
            String[] customerIds = customerIdStr.split(",");

                for (String customerId : customerIds){
                    //将字符串转数字
                    Integer id = Integer.parseInt(customerId);
                    //调用customerRepository的方法
                    customerRepository.associationCustomerToFixedArea(fixedAreaId,id);
                }


        }

    }

    @Override
    public void regist(Customer customer) {
        customerRepository.save(customer);
    }

    @Override
    public Customer findByTelephone(String telephone) {
        return customerRepository.findByTelephone(telephone);
    }

    @Override
    public void updataTypeByTelephone(String telephone) {
        customerRepository.updataTypeByTelephone(telephone);
    }
}
