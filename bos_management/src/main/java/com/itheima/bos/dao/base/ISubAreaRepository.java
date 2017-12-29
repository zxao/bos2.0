package com.itheima.bos.dao.base;

import com.itheima.bos.domain.base.SubArea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ISubAreaRepository extends JpaRepository<SubArea,String>,JpaSpecificationExecutor<SubArea> {
}
