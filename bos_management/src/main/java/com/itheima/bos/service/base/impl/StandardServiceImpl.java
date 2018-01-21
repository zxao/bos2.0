package com.itheima.bos.service.base.impl;

import com.itheima.bos.dao.base.IStandardRepository;
import com.itheima.bos.domain.base.Standard;
import com.itheima.bos.service.base.IStandardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class StandardServiceImpl implements IStandardService{
    @Autowired
    private IStandardRepository standardRepository;

    @Override
    @CacheEvict(value = "standard",allEntries = true)
    public void save(Standard standard) {
        standardRepository.save(standard);
    }

    @Override
    @Cacheable(value = "standard",key = "#pageable.pageNumber+'_'+#pageable.pageSize")
    public Page<Standard> findPage(Pageable pageable) {
        Page<Standard> pageDate = standardRepository.findAll(pageable);
        return pageDate;
    }

    @Override
    @Cacheable("standard")
    public List<Standard> findAll() {
        return standardRepository.findAll();
    }


}
