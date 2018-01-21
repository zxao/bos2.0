package com.itheima.bos.dao.system;

import com.itheima.bos.domain.system.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IMenuRepository extends JpaRepository<Menu,Integer>{
    @Query("from Menu m inner join fetch m.roles r inner join fetch r.users u where u.id=? order by m.priority")
    List<Menu> findByUser(Integer id);
}
