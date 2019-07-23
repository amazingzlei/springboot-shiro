package com.fh.service.impl;

import com.fh.dao.UserDao;
import com.fh.pojo.User;
import com.fh.service.IUserService;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserDao userDao;

    @Override
    public User getUserByUsername(String username) {
        return userDao.getUserByUserName(username);
    }

    /*user和admin角色都能操作*/
    @RequiresRoles(value = {"user","admin"},logical = Logical.OR)
    @Override
    public void addUser() {
        System.out.println("---------->新增用户成功!");
    }

    // 只有admin角色才能操作
    @Override
    @RequiresPermissions(value = {"admin:update"})
    public void updateUser() {
        System.out.println("---------->修改用户成功!");
    }

    @Override
    @RequiresRoles(value = {"admin"})
    public void delUser() {
        System.out.println("---------->删除用户成功!");
    }
}
