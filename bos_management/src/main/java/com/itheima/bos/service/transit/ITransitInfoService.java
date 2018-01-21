package com.itheima.bos.service.transit;

import com.itheima.bos.domain.transit.TransitInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ITransitInfoService {
    /**
     * 开启运单中转配送
     * @param wayBillIds
     */
    void create(String wayBillIds);

    /**
     * 分页查询
     * @param pageable
     * @return
     */
    Page<TransitInfo> findPageData(Pageable pageable);
}
