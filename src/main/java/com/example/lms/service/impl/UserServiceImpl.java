package com.example.lms.service.impl;

import com.example.lms.model.User;
import com.example.lms.service.UserManager;
import com.example.lms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserManager userManager;

    @Override
    public User login(User user) {
        List<User> users = userManager.getUsers(user);
        // 用户信息匹配单个用户
        if (users.size() == 1) {
            return users.get(0);
        }
        // 不匹配返回空用户
        return new User();
    }

    @Override
    public int register(User user) {
        List<User> users = userManager.checkUserInfo(user);
        if (users.size() > 0) {
            return 0;
        }
        return userManager.addUser(user);
    }

    @Override
    public int updateInfo(User user) {
        return userManager.updateUser(user);
    }
}
