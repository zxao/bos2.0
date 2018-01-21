package com.itheima.bos.service.system;

import com.itheima.bos.domain.system.Permission;
import com.itheima.bos.domain.system.User;

import java.util.List;

public interface IPermissionService {
    List<Permission> findByUser(User user);

    List<Permission> findAll();

    void save(Permission model);
}
