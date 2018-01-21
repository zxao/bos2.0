package com.itheima.bos.service.base.impl;

import com.itheima.bos.dao.base.IPromotionRepository;
import com.itheima.bos.domain.comment.PageBean;
import com.itheima.bos.domain.take_delivery.Promotion;
import com.itheima.bos.service.base.IPromotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Transactional
public class PromotionServiceImpl implements IPromotionService{
    //注入promotionRepository
    @Autowired
    private IPromotionRepository promotionRepository;

    @Override
    public void promotionSave(Promotion model) {
        promotionRepository.save(model);
    }

    @Override
    public Page<Promotion> promotionQueryPage(Pageable pageable) {
        return promotionRepository.findAll(pageable);
    }

    @Override
    public PageBean<Promotion> promotionQueryPage(Integer page, Integer rows) {
        //分页对象
        Pageable pageable = new PageRequest(page-1,rows);
        Page<Promotion> pageData = promotionRepository.findAll(pageable);
        PageBean<Promotion> pageBean = new PageBean<Promotion>();
        pageBean.setTotalCount(pageData.getTotalElements());
        pageBean.setContent(pageData.getContent());
        return pageBean;
    }

    @Override
    public Promotion showPromotionDetail(Integer id) {
        return promotionRepository.findById(id);
    }

    @Override
    public void updateStatus(Date date) {
        promotionRepository.updateStatus(date);
    }


}
