package com.example.lms.service.impl;

import com.example.lms.model.Admin;
import com.example.lms.model.User;
import com.example.lms.service.AdminManager;
import com.example.lms.service.AdminService;
import com.example.lms.service.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AdminServiceImpl implements AdminService {

    @Autowired
    private UserManager userManager;
    @Autowired
    private AdminManager adminManager;

    @Override
    public User updateUser(User user) {
        return userManager.updateUser(user);
    }

    @Override
    public List<Integer> deleteUser(List<User> list) {
        return userManager.deleteUsers(list);
    }

    @Override
    public User addUser(User user) {
        return userManager.addUser(user);
    }

    @Override
    public List<User> getUsersByConditions(User user) {
        return userManager.getUsers(user);
    }

    @Override
    public Admin login(Admin admin) {
        List<Admin> admins = adminManager.getAdmins(admin);
        // 用户信息匹配单个单个管理员
        if (admins.size() == 1 && admins.get(0).getAdminPwd().equals(admin.getAdminPwd())) {
            return admins.get(0);
        }
        // 不匹配返回空用户
        return new Admin();
    }

}
