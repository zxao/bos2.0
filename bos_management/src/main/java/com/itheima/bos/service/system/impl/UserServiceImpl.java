package com.itheima.bos.service.system.impl;

import com.itheima.bos.dao.system.IUserRepository;
import com.itheima.bos.domain.system.Role;
import com.itheima.bos.domain.system.User;
import com.itheima.bos.service.system.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements IUserService {

    //注入userRepository
    @Autowired
    private IUserRepository userRepository;

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public void save(String[] roleIds, User user) {
        userRepository.save(user);

        for (String roleid : roleIds) {
            Role role = new Role();
            role.setId(Integer.parseInt(roleid));
            user.getRoles().add(role);
        }
    }
}
