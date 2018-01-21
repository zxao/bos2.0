package com.itheima.bos.dao.base;

import com.itheima.bos.domain.base.Area;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IAreaRepository extends JpaRepository<Area,String>,JpaSpecificationExecutor<Area> {
    @Query("select distinct province from Area")
    public List<String> areaFindProvince();
    @Query("select distinct city from Area where province=?")
    List<String> areaFindCity(String province);
    @Query("select distinct district from Area where province=? and city=?")
    List<String> areaFindDistrictByProvinceAndCity(String province, String city);

    Area findByProvinceAndCityAndDistrict(String province, String city, String district);
}
