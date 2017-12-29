package com.itheima.bos.service.base.impl;

import com.itheima.bos.dao.base.ISubAreaRepository;
import com.itheima.bos.domain.base.SubArea;
import com.itheima.bos.service.base.ISubAreaService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
@Transactional
public class SubAreaServiceImpl implements ISubAreaService{
    @Autowired
    private ISubAreaRepository subAreaRepository;
    @Override
    public void subAreaImport(List<SubArea> list) {
        subAreaRepository.save(list);
    }

    @Override
    public Page<SubArea> subAreaQueryPage(Integer page, Integer rows, final SubArea model) {
        Pageable pageable = new PageRequest(page-1,rows);
        Specification<SubArea> specification = new Specification<SubArea>() {
            @Override
            public Predicate toPredicate(Root<SubArea> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> list = new ArrayList<Predicate>();
                //将subArea与area关联
                Join<Object, Object> areaRoot = root.join("area",JoinType.INNER);
                //province
                if(model.getArea()!=null && StringUtils.isNotBlank(model.getArea().getProvince())){
                    Predicate province = cb.like(areaRoot.get("province").as(String.class), "%" + model.getArea().getProvince() + "%");
                    list.add(province);
                }
                if(model.getArea()!=null && StringUtils.isNotBlank(model.getArea().getCity())){
                    Predicate province = cb.like(areaRoot.get("city").as(String.class), "%" + model.getArea().getCity() + "%");
                    list.add(province);
                }

                if(model.getArea()!=null && StringUtils.isNotBlank(model.getArea().getDistrict())){
                    Predicate province = cb.like(areaRoot.get("district").as(String.class), "%" + model.getArea().getDistrict() + "%");
                    list.add(province);
                }

                //与定区进行关联
                Join<Object, Object> fixedAreaRoot = root.join("fixedArea", JoinType.INNER);
                if(model.getFixedArea()!=null && StringUtils.isNotBlank(model.getFixedArea().getId())){
                    Predicate fixedAreaId = cb.equal(fixedAreaRoot.get("id").as(String.class), model.getFixedArea().getId());
                    list.add(fixedAreaId);
                }

                if(StringUtils.isNotBlank(model.getKeyWords())){
                    Predicate keyWords = cb.like(root.get("keyWords").as(String.class), "%" + model.getKeyWords() + "%");
                    list.add(keyWords);
                }
                Predicate and = cb.and(list.toArray(new Predicate[0]));

                return and;
            }
        };
        Page<SubArea> pageData = subAreaRepository.findAll(specification,pageable);

        return pageData;
    }

    @Override
    public void save(SubArea model) {
        subAreaRepository.save(model);
    }

    @Override
    public List<SubArea> findAll() {
        return subAreaRepository.findAll();
    }

    @Override
    public void subAreaBatchDel(String ids) {
        String[] idArr = ids.split(",");
        for (String id : idArr) {
            subAreaRepository.delete(id);
        }
    }
}
