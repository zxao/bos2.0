package com.itheima.bos.realm;

import com.itheima.bos.domain.system.Permission;
import com.itheima.bos.domain.system.Role;
import com.itheima.bos.domain.system.User;
import com.itheima.bos.service.system.IRoleService;
import com.itheima.bos.service.system.IUserService;
import com.itheima.bos.service.system.IPermissionService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


//@Service
public class BosRealm extends AuthorizingRealm {

    //注入userService
    @Autowired
    private IUserService userService;

    //注入roleService
    @Autowired
    private IRoleService roleService;

    //注入permissionService
    @Autowired
    private IPermissionService permissionService;

    //授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        //根据当前登录用户 查询对应角色和权限
        //获得subject
        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getPrincipal();
        //调用业务层查询角色
        List<Role> roles = roleService.findByUser(user);
        //遍历所有roles
        //添加角色
        for (Role role : roles) {
            simpleAuthorizationInfo.addRole(role.getKeyword());
        }
        //添加对应的权限
        //根据当前用户查询权限
        List<Permission> permissions = permissionService.findByUser(user);
        for (Permission permission : permissions) {
            simpleAuthorizationInfo.addStringPermission(permission.getKeyword());
        }
        return simpleAuthorizationInfo;

    }

    //认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        //将token转换类型
        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) authenticationToken;
        //根据用户名查询用户信息
        String username = usernamePasswordToken.getUsername();
        User user = userService.findByUsername(username);
        //判断用户名是否存在
        if (user == null) {
            //用户不存在
            return null;
        } else {
            //用户存在
            return new SimpleAuthenticationInfo(user,user.getPassword(),getName());
        }

    }

//    public static void main(String[] args) {
//        System.out.println(new BosRealm().getName());
//    }
}
