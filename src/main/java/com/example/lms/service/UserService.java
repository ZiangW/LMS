package com.example.lms.service;

import com.example.lms.model.User;

public interface UserService {

    User login(User user);

    int register(User user);

    int updateInfo(User user);

}