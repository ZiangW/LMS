package com.example.lms.service;

import com.example.lms.model.User;

public interface UserService {

    User login(User user);

    User register(User user);

    User updateInfo(User user);

}
