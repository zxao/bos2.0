package com.itheima.bos.dao.base;

import com.itheima.bos.domain.base.FixedArea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface IFixedAreaRepository extends JpaRepository<FixedArea,String>,JpaSpecificationExecutor<FixedArea>{
}
