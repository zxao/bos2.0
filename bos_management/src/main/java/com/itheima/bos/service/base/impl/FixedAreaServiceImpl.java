package com.itheima.bos.service.base.impl;

import com.itheima.bos.dao.base.IFixedAreaRepository;
import com.itheima.bos.domain.base.FixedArea;
import com.itheima.bos.service.base.IFixedAreaService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class FixedAreaServiceImpl implements IFixedAreaService {

    @Autowired
    private IFixedAreaRepository fixedAreaRepository;

    @Override
    public void save(FixedArea model) {
        fixedAreaRepository.save(model);
    }

    /**
     * 定区的分页条件查询操作
     * @param page
     * @param rows
     * @param model
     * @return
     */
    @Override
    public Page<FixedArea> fixedAreaQueryPage(Integer page, Integer rows, final FixedArea model) {
        //分页对象
        Pageable pageable = new PageRequest(page-1,rows);
        //分页条件对象
        Specification specification = new Specification() {
            List<Predicate> list = new ArrayList<Predicate>();
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder cb) {
                //定区编号id
                if(StringUtils.isNotBlank(model.getId())){
                    Predicate id = cb.equal(root.get("id").as(String.class), model.getId());
                    list.add(id);
                }

                //所属单位company
                if(StringUtils.isNotBlank(model.getCompany())){
                    Predicate company = cb.like(root.get("company").as(String.class), "%"+model.getCompany()+"%");
                    list.add(company);
                }

                //分区名称




                Predicate and = cb.and(list.toArray(new Predicate[0]));
                return and;
            }
        };

        Page<FixedArea> pageData = fixedAreaRepository.findAll(specification, pageable);

        return pageData;
    }

    @Override
    public List<FixedArea> fixedAreaFindAll() {
        return fixedAreaRepository.findAll();
    }
}
