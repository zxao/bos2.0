package com.itheima.bos.dao.delivery;

import com.itheima.bos.domain.take_delivery.WorkBill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IWorkBillRepository extends JpaRepository<WorkBill,Integer>{
}
