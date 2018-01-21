package com.itheima.bos.service.system.impl;

import com.itheima.bos.dao.system.IMenuRepository;
import com.itheima.bos.domain.system.Menu;
import com.itheima.bos.domain.system.User;
import com.itheima.bos.service.system.IMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MenuServiceImpl implements IMenuService {
    //注入menuRepository
    @Autowired
    private IMenuRepository menuRepository;

    @Override
    public List<Menu> findAll() {
        return menuRepository.findAll();
    }

    @Override
    public void save(Menu menu) {
        if (menu.getParentMenu() != null && menu.getParentMenu().getId() == null){
            menu.setParentMenu(null);
        }
            menuRepository.save(menu);
    }

    @Override
    public List<Menu> showMenu(User user) {
        if(user != null && "admin".equals(user.getUsername())){
            return menuRepository.findAll();
        }
        //根据用户id查询对应菜单
        return menuRepository.findByUser(user.getId());
    }
}
