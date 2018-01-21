package com.itheima.bos.service.transit.impl;

import com.itheima.bos.dao.delivery.IWayBillRepository;
import com.itheima.bos.dao.transit.ItransitInfoRepository;
import com.itheima.bos.domain.take_delivery.WayBill;
import com.itheima.bos.domain.transit.TransitInfo;
import com.itheima.bos.service.transit.ITransitInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TransitInfoServiceImpl implements ITransitInfoService{

    @Autowired
    private ItransitInfoRepository transitInfoRepository;

    @Autowired
    private IWayBillRepository wayBillRepository;

    @Override
    public void create(String wayBillIds) {
        if(StringUtils.isNotBlank(wayBillIds)){
            String[] wayBillIdArr = wayBillIds.split(",");
            for (String wayBillIdStr : wayBillIdArr){
                WayBill wayBill = wayBillRepository.findOne(Integer.parseInt(wayBillIdStr));
                //判断运单状态是否为待发货
                if (wayBill.getSignStatus()==1){
                    //待发货
                    TransitInfo transitInfo = new TransitInfo();
                    transitInfo.setWayBill(wayBill);
                    transitInfo.setStatus("出入库中转");
                    //保存运输信息
                    transitInfoRepository.save(transitInfo);
                    //更改运单的状态为 2.派送中
                    wayBill.setSignStatus(2);
                }
            }
        }
    }

    @Override
    public Page<TransitInfo> findPageData(Pageable pageable) {
        return transitInfoRepository.findAll(pageable);
    }
}
