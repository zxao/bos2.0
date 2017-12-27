package com.itheima.bos.service.base.impl;

import com.itheima.bos.dao.base.IAreaRepository;
import com.itheima.bos.domain.base.Area;
import com.itheima.bos.service.base.IAreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AreaServiceImpl implements IAreaService {

    //注入dao
    @Autowired
    private IAreaRepository areaRepository;

    @Override
    public void areaBatchImport(Area area) {
        areaRepository.save(area);
    }
}
