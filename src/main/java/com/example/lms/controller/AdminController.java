package com.example.lms.controller;

import com.example.lms.model.Admin;
import com.example.lms.model.Book;
import com.example.lms.model.BookCategory;
import com.example.lms.model.User;
import com.example.lms.service.AdminManager;
import com.example.lms.service.AdminService;
import com.example.lms.service.BookCategoryManager;
import com.example.lms.service.BookManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/Admins")
public class AdminController {

    @Autowired
    private AdminManager adminManager;
    @Autowired
    private AdminService adminService;
    @Autowired
    private BookManager bookManager;
    @Autowired
    private BookCategoryManager bookCategoryManager;

    @PostMapping(value = "/login")
    @ResponseBody
    public Admin login(@RequestBody Admin admin) {
        return adminService.login(admin);
    }

    @PostMapping(value = "/addAdmins")
    @ResponseBody
    public Admin addAdmin(@RequestBody Admin admin) {
        return adminManager.addAdmin(admin);
    }

    @PostMapping(value = "/deleteAdmins")
    @ResponseBody
    public int deleteAdmins(@RequestBody Admin admin) {
        return adminManager.deleteAdmin(admin);
    }

    @PostMapping(value = "/updateAdmins")
    @ResponseBody
    public Admin updateAdmin(@RequestBody Admin admin) {
        return adminManager.updateAdmin(admin);
    }

    @PostMapping(value = "/getAdmins")
    @ResponseBody
    public List<Admin> getAdmins(@RequestBody Admin admin) {
        return adminManager.getAdmins(admin);
    }

    @GetMapping(value = "/getAllAdmins")
    @ResponseBody
    public List<Admin> getAllAdmins() {
        return adminManager.getAllAdmins();
    }

    @PostMapping(value = "/addUsers")
    @ResponseBody
    public User addUser(@RequestBody User user) {
        return adminService.addUser(user);
    }

    @PostMapping(value = "/updateUsers")
    @ResponseBody
    public User updateUser(@RequestBody User user) {
        return adminService.updateUser(user);
    }

    @PostMapping(value = "/deleteUsers")
    @ResponseBody
    public List<Integer> deleteUsers(@RequestBody List<User> list) {
        return adminService.deleteUser(list);
    }

    @PostMapping(value = "/getUsers")
    @ResponseBody
    public List<User> getUsers(@RequestBody User user) {
        return adminService.getUsersByConditions(user);
    }


    @PostMapping(value = "/addBooks")
    @ResponseBody
    public Book addBook(@RequestBody Book book) {
        return bookManager.addBooks(book);
    }

    @PostMapping(value = "/deleteBooks")
    @ResponseBody
    public List<Integer> deleteBook(@RequestBody List<Book> list) {
        return bookManager.deleteBooks(list);
    }

    @PostMapping(value = "/updateBooks")
    @ResponseBody
    public Book updateBook(@RequestBody Book book) {
        return bookManager.updateBooks(book);
    }

    @PostMapping(value = "/getBooks")
    @ResponseBody
    public List<Book> getBooks(@RequestBody Book book) {
        return bookManager.getBooks(book);
    }

    @PostMapping(value = "/getBookCategory")
    @ResponseBody
    public List<BookCategory> getBookCategory(@RequestBody BookCategory bookCategory) {
        return bookCategoryManager.getBookCategory(bookCategory);
    }

    @PostMapping(value = "/addBookCategory")
    @ResponseBody
    public BookCategory addBookCategory(@RequestBody BookCategory bookCategory) {
        return bookCategoryManager.addBookCategory(bookCategory);
    }

    @PostMapping(value = "/deleteBookCategory")
    @ResponseBody
    public List<Integer> deleteBookCategory(@RequestBody List<BookCategory> list) {
        return bookCategoryManager.deleteBookCategory(list);
    }

    @PostMapping(value = "/updateBookCategory")
    @ResponseBody
    public BookCategory updateBookCategory(@RequestBody BookCategory bookCategory) {
        return bookCategoryManager.updateBookCategory(bookCategory);
    }
}
