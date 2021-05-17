package com.example.lms.service;

import com.example.lms.model.User;

import java.util.List;

public interface UserManager {

    User addUser(User user);

    List<Integer> deleteUsers(List<User> list);

    User updateUser(User user);

    List<User> getAllUsers();

    List<User> getUsers(User user);

}
