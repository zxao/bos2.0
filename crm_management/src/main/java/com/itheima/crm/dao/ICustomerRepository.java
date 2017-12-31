package com.itheima.crm.dao;

import cn.itcast.crm.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ICustomerRepository extends JpaRepository<Customer,Integer>{


    @Query(value = "update Customer set fixedAreaId=?2 where id=?1")
    void associationCustomerToFixedArea(Integer id, String fixedAreaId);

    List<Customer> findByFixedAreaIdIsNull();

    List<Customer> findByFixedAreaId(String fixedAreaId);
}
