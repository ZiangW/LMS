package com.example.lms.service;

import com.example.lms.model.User;

import java.util.List;


/**
 * @author 王子昂
 * @date 5/19/21
 * @description 用户Manager接口
 */
public interface UserManager {

    /**
     * Add user user.
     *
     * @param user the user
     * @return the user
     */
    User addUser(User user);

    /**
     * Delete users list.
     *
     * @param list the list
     * @return the list
     */
    List<Integer> deleteUsers(List<User> list);

    /**
     * Update user user.
     *
     * @param user the user
     * @return the user
     */
    User updateUser(User user);

    /**
     * Gets all users.
     *
     * @return the all users
     */
    List<User> getAllUsers();

    /**
     * Gets users.
     *
     * @param user the user
     * @return the users
     */
    List<User> getUsers(User user);

}
