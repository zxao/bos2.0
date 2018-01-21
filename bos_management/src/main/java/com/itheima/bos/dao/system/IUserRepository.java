package com.itheima.bos.dao.system;

import com.itheima.bos.domain.system.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IUserRepository extends JpaRepository<User,Integer> {
    User findByUsername(String username);
}
