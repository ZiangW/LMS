package com.example.lms.service.impl;

import com.example.lms.model.User;
import com.example.lms.service.AdminService;
import com.example.lms.service.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class AdminServiceImpl implements AdminService {

    @Autowired
    private UserManager userManager;

    @Override
    public int updateUser(User user) {
        return userManager.updateUser(user);
    }

    @Override
    public List<Integer> deleteUser(List<User> list) {
        return userManager.deleteUsers(list);
    }

    @Override
    public int addUser(User user) {
        return userManager.addUser(user);
    }

    @Override
    public List<User> getUsersByConditions(User user) {
        return userManager.getUsers(user);
    }

}
