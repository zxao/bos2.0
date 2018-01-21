package com.itheima.bos.service.system;

import com.itheima.bos.domain.system.User;

import java.util.List;

public interface IUserService {
    /**
     * 根据用户名查询用户信息
     * @param username
     * @return
     */
    User findByUsername(String username);

    List<User> findAll();

    void save(String[] roleIds, User model);
}
