package com.itheima.bos.dao.system;

import com.itheima.bos.domain.system.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IRoleRepository extends JpaRepository<Role,Integer>{
    @Query("from Role r inner join fetch r.users u where u.id=?")
    List<Role> findByUser(Integer id);
}
