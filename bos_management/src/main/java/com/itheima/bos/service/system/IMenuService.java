package com.itheima.bos.service.system;

import com.itheima.bos.domain.system.Menu;
import com.itheima.bos.domain.system.User;

import java.util.List;

public interface IMenuService {
    /**
     * 菜单列表的查询
     * @return
     */
    List<Menu> findAll();

    /**
     * 菜单信息的添加
     * @param model
     */
    void save(Menu model);

    /**
     * 根据当前用户显示对应的菜单信息
     * @param user
     * @return
     */
    List<Menu> showMenu(User user);
}
