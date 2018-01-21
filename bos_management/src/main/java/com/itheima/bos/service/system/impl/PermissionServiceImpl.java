package com.itheima.bos.service.system.impl;

import com.itheima.bos.dao.system.IPermissionReposity;
import com.itheima.bos.domain.system.Permission;
import com.itheima.bos.domain.system.User;
import com.itheima.bos.service.system.IPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PermissionServiceImpl implements IPermissionService{
    //注入permissionRepository
    @Autowired
    private IPermissionReposity permissionReposity;

    @Override
    public List<Permission> findByUser(User user) {
        if("admin".equals(user.getUsername())) {
            return permissionReposity.findAll();
        }else {
            return permissionReposity.findByUser(user.getId());
        }

    }

    @Override
    public List<Permission> findAll() {
        return permissionReposity.findAll();
    }

    @Override
    public void save(Permission permission) {
        permissionReposity.save(permission);
    }
}
