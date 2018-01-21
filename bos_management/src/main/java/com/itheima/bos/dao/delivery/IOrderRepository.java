package com.itheima.bos.dao.delivery;

import com.itheima.bos.domain.take_delivery.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IOrderRepository extends JpaRepository<Order,Integer> {
    Order findByOrderNum(String orderNum);
}
