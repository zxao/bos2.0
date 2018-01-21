package com.itheima.bos.service.base.impl;

import com.itheima.bos.dao.base.ICourierRepository;
import com.itheima.bos.domain.base.Courier;
import com.itheima.bos.service.base.ICourierService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CourierServiceImpl implements ICourierService{

    @Autowired
    private ICourierRepository courierRepository;


    @Override
    @RequiresPermissions("courier:add")
    public void save(Courier courier) {
        courierRepository.save(courier);
    }



    @Override
    public void courierDeleteBatch(String ids) {
        //处理ids
        String[] idArr = ids.split(",");

        for (String idStr : idArr) {
            Integer id = Integer.parseInt(idStr);
            courierRepository.updateDeltag(id);
        }
    }

    @Override
    public void courierRestore(String ids) {
        String[] idArr = ids.split(",");

        for (String idStr : idArr) {
            Integer id = Integer.parseInt(idStr);
            courierRepository.updateDeltagForRestore(id);
        }
    }

    @Override
    public Page<Courier> queryPage(Specification<Courier> specification, Pageable pageable) {
        return courierRepository.findAll(specification,pageable);
    }
}
