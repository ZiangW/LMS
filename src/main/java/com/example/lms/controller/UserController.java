package com.example.lms.controller;


import com.example.lms.model.*;
import com.example.lms.service.UserManager;
import com.example.lms.service.UserService;
import com.example.lms.vo.OperationVo;
import com.example.lms.vo.ResponseVo;
import com.example.lms.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @author 王子昂
 * @date 5/21/21
 * @description The type User controller.
 */
@RestController
@RequestMapping(value = "/Users")
public class UserController {

    @Autowired
    private UserManager userManager;
    @Autowired
    private UserService userService;

    /**
     * Add user user.
     *
     * @param user the user
     * @return the user
     */
    @PostMapping(value = "/addUsers")
    @ResponseBody
    public User addUser(@RequestBody User user) {
        return userManager.addUser(user);
    }

    /**
     * Delete user list.
     *
     * @param list the list
     * @return the list
     */
    @PostMapping(value = "/deleteUsers")
    @ResponseBody
    public List<Integer> deleteUser(@RequestBody List<User> list) {
        return userManager.deleteUsers(list);
    }

    /**
     * Update user user.
     *
     * @param user the user
     * @return the user
     */
    @PostMapping(value = "/updateUsers")
    @ResponseBody
    public User updateUser(@RequestBody User user) {
        return userManager.updateUser(user);
    }

    /**
     * Gets user.
     *
     * @param user the user
     * @return the user
     */
    @PostMapping(value = "/getUsers")
    @ResponseBody
    public List<User> getUser(@RequestBody User user) {
        return userManager.getUsers(user);
    }

    /**
     * Gets all user.
     *
     * @return the all user
     */
    @GetMapping(value = "/getAllUsers")
    @ResponseBody
    public List<User> getAllUser() {
        return userManager.getAllUsers();
    }

    /**
     * User login.
     * 对外暴露
     * @param user the user
     * @return responseVo the response type
     * @throws Exception the exception
     */
    @PostMapping(value = "/login")
    @ResponseBody
    public ResponseVo<UserVo> userLogin(@RequestBody User user) throws Exception {
        return ResponseVo.getSuccessResp(userService.login(user));
    }

    /**
     * Register response vo.
     * 对外暴露
     * @param user the user
     * @return the response vo
     * @throws Exception the exception
     */
    @PostMapping(value = "/register")
    @ResponseBody
    public ResponseVo<UserVo> register(@RequestBody User user) throws Exception {
        return ResponseVo.getSuccessResp(userService.register(user));
    }

    /**
     * Update info response vo.
     * 对外暴露
     * @param user the user
     * @return the response vo
     * @throws Exception the exception
     */
    @PostMapping(value = "/updateInfo")
    @ResponseBody
    public ResponseVo<UserVo> updateInfo(@RequestBody User user) throws Exception {
        return ResponseVo.getSuccessResp(userService.updateInfo(user));
    }

    /**
     * Borrow book response vo.
     * 对外暴露
     * @param borrowedBook the borrowed book
     * @return the response vo
     * @throws Exception the exception
     */
    @PostMapping(value = "/borrowBook")
    @ResponseBody
    public ResponseVo<OperationVo> borrowBook(@RequestBody BorrowedBook borrowedBook) throws Exception {
        return ResponseVo.getSuccessResp(userService.borrowBook(borrowedBook));
    }

    /**
     * Return book response vo.
     * 对外暴露
     * @param returnedBook the returned book
     * @return the response vo
     * @throws Exception the exception
     */
    @PostMapping(value = "/returnBook")
    @ResponseBody
    public ResponseVo<OperationVo> returnBook(@RequestBody ReturnedBook returnedBook) throws Exception {
        return ResponseVo.getSuccessResp(userService.returnBook(returnedBook));
    }

}
