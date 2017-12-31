package com.itheima.bos.service.base.impl;

import com.itheima.bos.dao.base.IAreaRepository;
import com.itheima.bos.domain.base.Area;
import com.itheima.bos.service.base.IAreaService;
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
import javax.swing.text.html.HTMLDocument;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Transactional
public class AreaServiceImpl implements IAreaService{

    //注入dao
    @Autowired
    private IAreaRepository areaRepository;

    @Override
    public void areaBatchImport(List<Area> areas) {
        areaRepository.save(areas);
    }

    @Override
    public Page<Area> queryPage(Integer page, Integer rows, final Area area) {
        //创建pageable对象
        Pageable pageable = new PageRequest(page - 1, rows);

        //创建查询条件对象
        Specification specification = new Specification() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder cb) {
                //使用List集合存储查询条件
                List<Predicate> list = new ArrayList<Predicate>();

                //province
                if(StringUtils.isNotBlank(area.getProvince())){//如果不为空
                    Predicate province = cb.like(root.get("province").as(String.class), "%"+area.getProvince()+"%");
                    list.add(province);
                }

                //city
                if(StringUtils.isNotBlank(area.getCity())){
                    Predicate city = cb.like(root.get("city").as(String.class), "%" + area.getCity() + "%");
                    list.add(city);
                }

                //district
                if(StringUtils.isNotBlank(area.getDistrict())){
                    Predicate district = cb.like(root.get("district").as(String.class), "%" + area.getDistrict() + "%");
                    list.add(district);
                }

                //将参数使用and拼起来
                Predicate and = cb.and(list.toArray(new Predicate[0]));

                return and;
            }
        };
        //调用areaRespository的findAll(specification,pageable)
        Page<Area> pageData = areaRepository.findAll(specification, pageable);
        return pageData;
    }

    /**
     * 区域数据的添加
     * @param area
     */
    @Override
    public void save(Area area) {
        areaRepository.save(area);
    }

    @Override
    public void areaDel(String ids) {
        //遍历ids
        String[] idArr = ids.split(",");
        for (String id : idArr) {
            areaRepository.delete(id);
        }

    }

    @Override
    public List<Area> areaFindAll() {
        return areaRepository.findAll();
    }

    @Override
    public List<String> areaFindProvince() {
        return areaRepository.areaFindProvince();
    }

    @Override
    public List<String> areaFindCity(String province) {
        return areaRepository.areaFindCity(province);
    }

    @Override
    public List<String> areaFindDistrictByProvinceAndCity(String province, String city) {
        return areaRepository.areaFindDistrictByProvinceAndCity(province,city);
    }


}
