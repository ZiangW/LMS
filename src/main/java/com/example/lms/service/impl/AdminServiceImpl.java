package com.example.lms.service.impl;

import com.example.lms.model.Admin;
import com.example.lms.model.Book;
import com.example.lms.model.BookCategory;
import com.example.lms.model.User;
import com.example.lms.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
public class AdminServiceImpl implements AdminService {

    @Autowired
    private UserManager userManager;
    @Autowired
    private AdminManager adminManager;
    @Autowired
    private BookManager bookManager;
    @Autowired
    private BookCategoryManager bookCategoryManager;

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
    public List<User> getUsers(User user) {
        return userManager.getUsers(user);
    }

    @Override
    public Book addBooks(Book book) {
        return bookManager.addBooks(book);
    }

    @Override
    public List<Integer> deleteBooks(List<Book> list) {
        return bookManager.deleteBooks(list);
    }

    @Override
    public Book updateBooks(Book book) {
        return bookManager.updateBooks(book);
    }

    @Override
    public List<Book> getBooks(Book book) {
        return bookManager.getBooks(book);
    }

    @Override
    public List<BookCategory> getBookCategory(BookCategory bookCategory) {
        return bookCategoryManager.getBookCategory(bookCategory);
    }

    @Override
    public BookCategory addBookCategory(BookCategory bookCategory) {
        return bookCategoryManager.addBookCategory(bookCategory);
    }

    @Override
    public BookCategory updateBookCategory(BookCategory bookCategory) {
        return bookCategoryManager.updateBookCategory(bookCategory);
    }

    @Override
    public List<Integer> deleteBookCategory(List<BookCategory> list) {
        return bookCategoryManager.deleteBookCategory(list);
    }

    @Override
    public Admin login(Admin admin) {
        List<Admin> admins = adminManager.getAdmins(admin);
        // 用户信息匹配单个单个管理员
        if (admins.size() < 1) {
            log.info("未查询到相关管理员");
        } else if (admins.size() == 1) {
            if (admins.get(0).getAdminPwd().equals(admin.getAdminPwd())) {
                return admins.get(0);
            } else {
                log.info("管理员信息不匹配");
            }
        } else {
            log.info("相关管理员信息不唯一");
        }
        // 不匹配返回空用户
        return new Admin();
    }

}
