package com.itheima.bos.dao.base;

import com.itheima.bos.domain.base.Area;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IAreaRepository extends JpaRepository<Area,String>,JpaSpecificationExecutor<Area> {
//    @Query(value = "select DISTINCT C_CITY FROM T_AREA",nativeQuery = true)
//    List<Area> queryByCity();
}
