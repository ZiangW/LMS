package com.example.lms.service;

import com.example.lms.model.User;

import java.util.List;

public interface UserManager {

    int addUser(User book);

    List<Integer> deleteUsers(List<User> list);

    int updateUser(User user);

    List<User> getAllUsers();

    List<User> getUsers(User user);

    List<User> checkUserInfo(User user);

}
