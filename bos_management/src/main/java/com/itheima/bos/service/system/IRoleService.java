package com.itheima.bos.service.system;

import com.itheima.bos.domain.system.Role;
import com.itheima.bos.domain.system.User;

import java.util.List;

public interface IRoleService {
    List<Role> findByUser(User user);

    List<Role> findAll();

    void roleSave(Role model, String menuIds, String[] permissionIds);
}
