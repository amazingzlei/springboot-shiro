package com.fh.service;

import com.fh.pojo.User;

public interface IUserService {
    User getUserByUsername(String username);

    void addUser();

    void updateUser();

    void delUser();
}
