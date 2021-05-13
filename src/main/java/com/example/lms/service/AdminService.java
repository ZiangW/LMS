package com.example.lms.service;

import com.example.lms.model.Admin;
import com.example.lms.model.User;

import java.util.List;

public interface AdminService {

    User updateUser(User user);

    List<Integer> deleteUser(List<User> list);

    User addUser(User user);

    List<User> getUsersByConditions(User user);

    Admin login(Admin admin);

}
