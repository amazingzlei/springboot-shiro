package com.fh.dao;

import com.fh.pojo.User;

public interface UserDao {
    User getUserByUserName(String username);
}
