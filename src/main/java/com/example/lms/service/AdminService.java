package com.example.lms.service;

import com.example.lms.model.User;

import java.util.List;

public interface AdminService {

    int updateUser(User user);

    List<Integer> deleteUser(List<User> list);

    int addUser(User user);

    List<User> getUsersByConditions(User user);

}
