package com.itheima.bos.service.system.impl;

import com.itheima.bos.dao.system.IRoleRepository;
import com.itheima.bos.domain.system.Menu;
import com.itheima.bos.domain.system.Permission;
import com.itheima.bos.domain.system.Role;
import com.itheima.bos.domain.system.User;
import com.itheima.bos.service.system.IRoleService;
import com.sun.net.httpserver.Authenticator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class RoleServiceImpl implements IRoleService {

    //注入roleRepository
    @Autowired
    private IRoleRepository roleRepository;

    @Override
    public List<Role> findByUser(User user) {
        //基于用户查询角色
        if("admin".equals(user.getUsername())) {
            return roleRepository.findAll();
        }else {

            return roleRepository.findByUser(user.getId());
        }
    }

    @Override
    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    @Override
    public void roleSave(Role model, String menuIds, String[] permissionIds) {
        roleRepository.save(model);
        if(!"null".equals(menuIds) && StringUtils.isNotBlank(menuIds)) {
            String[] menuIdArr = menuIds.split(",");
            for (String menuId : menuIdArr) {
                Menu menu = new Menu();
                menu.setId(Integer.parseInt(menuId));
                model.getMenus().add(menu);
            }
        }
        if(permissionIds != null) {

            for (String permissionId : permissionIds) {
                Permission permission = new Permission();
                permission.setId(Integer.parseInt(permissionId));
                model.getPermissions().add(permission);
            }
        }
    }
}
