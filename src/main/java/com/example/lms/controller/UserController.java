package com.example.lms.controller;


import com.example.lms.model.*;
import com.example.lms.service.UserBookService;
import com.example.lms.service.UserManager;
import com.example.lms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/Users")
public class UserController {

    @Autowired
    private UserManager userManager;
    @Autowired
    private UserService userService;
    @Autowired
    private UserBookService userBookService;

    @PostMapping(value = "/addUsers")
    @ResponseBody
    public int addUser(@RequestBody User user) {

        return userManager.addUser(user);

    }

    @PostMapping(value = "/deleteUsers")
    @ResponseBody
    public List<Integer> deleteUser(@RequestBody List<User> list) {

        return userManager.deleteUsers(list);

    }

    @PostMapping(value = "/updateUsers")
    @ResponseBody
    public int updateUser(@RequestBody User user) {

        return userManager.updateUser(user);

    }

    @PostMapping(value = "/getUsers")
    @ResponseBody
    public List<User> getUser(@RequestBody User user) {

        return userManager.getUsers(user);

    }

    @GetMapping(value = "/getAllUsers")
    @ResponseBody
    public List<User> getAllUser() {

        return userManager.getAllUsers();

    }

    @PostMapping(value = "/login")
    @ResponseBody
    public User login(@RequestBody User user) {

        return userService.login(user);

    }

    @PostMapping(value = "/register")
    @ResponseBody
    public int register(@RequestBody User user) {

        return userService.register(user);

    }

    @PostMapping(value = "/updateInfo")
    @ResponseBody
    public int updateInfo(@RequestBody User user) {

        return userService.updateInfo(user);

    }

    @PostMapping(value = "/borrowBook")
    @ResponseBody
    public int borrowBook(@RequestBody BorrowedBook borrowedBook) {

        return userBookService.borrowBook(borrowedBook);

    }

    @PostMapping(value = "/returnBook")
    @ResponseBody
    public int returnBook(@RequestBody ReturnedBook returnedBook) {

        return userBookService.returnBook(returnedBook);

    }

}