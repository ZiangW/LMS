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

    @PostMapping(value = "/addAdmins")
    @ResponseBody
    public int addAdmin(@RequestBody Admin admin) {

        return adminManager.addAdmin(admin);

    }

    @PostMapping(value = "/deleteAdmins")
    @ResponseBody
    public int deleteUser(@RequestBody Admin admin) {

        return adminManager.deleteAdmin(admin);

    }

    @PostMapping(value = "/updateAdmins")
    @ResponseBody
    public int updateAdmin(@RequestBody Admin admin) {

        return adminManager.updateAdmin(admin);

    }

    @PostMapping(value = "/getAdmins")
    @ResponseBody
    public List<Admin> getUser(@RequestBody Admin admin) {

        return adminManager.getAdmins(admin);

    }

    @GetMapping(value = "/getAllAdmins")
    @ResponseBody
    public List<Admin> getAllUser() {

        return adminManager.getAllAdmins();

    }

    @PostMapping(value = "/addUser")
    @ResponseBody
    public int addUsers(@RequestBody User user) {

        return adminService.addUser(user);

    }

    @PostMapping(value = "/updateUser")
    @ResponseBody
    public int updateUsers(@RequestBody User user) {

        return adminService.updateUser(user);

    }

    @PostMapping(value = "/deleteUser")
    @ResponseBody
    public List<Integer> deleteUser(@RequestBody List<User> list) {

        return adminService.deleteUser(list);

    }

    @PostMapping(value = "/getUser")
    @ResponseBody
    public List<User> getUser(@RequestBody User user) {

        return adminService.getUsersByConditions(user);

    }


    @PostMapping(value = "/addBook")
    @ResponseBody
    public int addBook(@RequestBody Book book) {
        int res = bookManager.addBooks(book);
        bookManager.getBooks(book);
        return res;
    }

    @PostMapping(value = "/deleteBook")
    @ResponseBody
    public List<Integer> deleteBook(@RequestBody List<Book> list) {

        List<Integer> res = new ArrayList<>();
        for (Book book : list) {
            res.add(bookManager.deleteBook(book));
        }
        return res;

    }

    @PostMapping(value = "/updateBook")
    @ResponseBody
    public int updateBook(@RequestBody Book book) {

        int res = bookManager.updateBooks(book);
        bookManager.getBooks(book);
        return res;

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
    public int addBookCategory(@RequestBody BookCategory bookCategory) {
        return bookCategoryManager.addBookCategory(bookCategory);
    }

    @PostMapping(value = "/deleteBookCategory")
    @ResponseBody
    public List<Integer> deleteBookCategory(@RequestBody List<BookCategory> list) {
        return bookCategoryManager.deleteBookCategory(list);
    }

    @PostMapping(value = "/updateBookCategory")
    @ResponseBody
    public int updateBookCategory(@RequestBody BookCategory bookCategory) {
        return bookCategoryManager.updateBookCategory(bookCategory);
    }

}
