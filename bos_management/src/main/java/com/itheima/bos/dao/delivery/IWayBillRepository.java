package com.itheima.bos.dao.delivery;

import com.itheima.bos.domain.take_delivery.WayBill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IWayBillRepository extends JpaRepository<WayBill,Integer> {
    WayBill findByWayBillNum(String wayBillNum);
}
