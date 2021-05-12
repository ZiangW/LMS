package com.example.lms.service.impl;

import com.example.lms.model.User;
import com.example.lms.service.UserManager;
import com.example.lms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@CacheConfig(cacheNames = "users")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserManager userManager;

    @Override
    @Cacheable(key = "'userName::'+#user.userName", unless = "#result.userId == null")
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
    @CachePut(key = "'userName::'+#user.userName", unless = "#result.userId == null")
    public User register(User user) {
        if (userManager.addUser(user) == 1) {
            List<User> users = userManager.getUsers(user);
            if (users.size() == 1) {
                return users.get(0);
            } else {
                return new User();
            }
        }
        return new User();
    }

    @Override
    @CachePut(key = "'userName::'+#user.userName", unless = "#result.userId == null")
    public User updateInfo(User user) {
        if (userManager.updateUser(user) == 1) {
            List<User> users = userManager.getUsers(user);
            if (users.size() == 1) {
                return users.get(0);
            } else {
                return new User();
            }
        }
        return new User();
    }
}
