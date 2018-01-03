package com.itheima.crm.dao;

import cn.itcast.crm.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ICustomerRepository extends JpaRepository<Customer,Integer>{


    @Query(value = "update Customer set fixedAreaId=? where id=?")
    @Modifying
    void associationCustomerToFixedArea(String fixedAreaId,Integer id);

    List<Customer> findByFixedAreaIdIsNull();

    List<Customer> findByFixedAreaId(String fixedAreaId);
    @Query(value ="update Customer set fixedAreaId=null where fixedAreaId=?")
    @Modifying
    void clearAssociationCustomerToFixedArea(String fixedAreaId);
}
