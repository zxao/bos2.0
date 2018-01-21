package com.itheima.bos.dao.system;

import com.itheima.bos.domain.system.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IPermissionReposity extends JpaRepository<Permission,Integer> {
    @Query("from Permission p inner join fetch p.roles r inner join fetch r.users u where u.id=?")
    List<Permission> findByUser(Integer id);
}
