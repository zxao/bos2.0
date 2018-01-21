package com.itheima.bos.service.delivery;

import com.itheima.bos.domain.take_delivery.WayBill;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IWayBillService {
    void save(WayBill model);

    Page<WayBill> findQueryPage(WayBill wayBill,Pageable pageable);

    WayBill findByWayBillNum(String wayBillNum);

    void syncIndex();
}
