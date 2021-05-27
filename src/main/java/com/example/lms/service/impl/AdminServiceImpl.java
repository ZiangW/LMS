package com.example.lms.service.impl;

import com.alibaba.fastjson.JSON;
import com.example.lms.exception.BusinessException;
import com.example.lms.model.Admin;
import com.example.lms.model.Book;
import com.example.lms.model.BookCategory;
import com.example.lms.model.User;
import com.example.lms.service.*;
import com.example.lms.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;

import static com.example.lms.constants.GlobalConstants.*;
import static com.example.lms.enums.RespEnum.PARAM_ERROR;

/**
 * @author 王子昂
 * @date 5 /21/21
 * @description AdminService实现 type Admin service.
 */
@Slf4j
@Service
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
    public List<OperationVo> deleteUser(List<User> list) {
        List<OperationVo> res = new ArrayList<>();
        for (int code : userManager.deleteUsers(list)) {
            res.add(OperationVo.builder().operationCode(code).build());
        }
        return res;
    }

    @Override
    public List<User> getUsers(User user) {
        return userManager.getUsers(user);
    }

    @Override
    public BookVo addBooks(Book book) {
        Book res = bookManager.addBooks(book);
        if (res.getBookId() == null) {
            return BookVo.builder().bookId(NULL_BOOK_ID).build();
        }
        return BookVo.builder().bookId(res.getBookId()).build();
    }

    @Override
    public List<OperationVo> deleteBooks(List<Book> list) {
        List<OperationVo> res = new ArrayList<>();
        for (int code : bookManager.deleteBooks(list)) {
            res.add(OperationVo.builder().operationCode(code).build());
        }
        return res;
    }

    @Override
    public BookVo updateBooks(Book book) {
        Book res = bookManager.updateBooks(book);
        if (res.getBookId() == null) {
            return BookVo.builder().bookId(NULL_BOOK_ID).build();
        }
        return BookVo.builder().bookId(res.getBookId()).build();
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
    public BookCategoryVo addBookCategory(BookCategory bookCategory) {
        BookCategory res = bookCategoryManager.addBookCategory(bookCategory);
        if (res.getCategoryId() == null) {
            return BookCategoryVo.builder().categoryId(NULL_CATEGORY_ID).build();
        }
        return BookCategoryVo.builder().categoryId(res.getCategoryId()).build();
    }

    @Override
    public List<OperationVo> deleteBookCategory(List<BookCategory> list) {
        List<OperationVo> res = new ArrayList<>();
        for (int code : bookCategoryManager.deleteBookCategory(list)) {
            res.add(OperationVo.builder().operationCode(code).build());
        }
        return res;
    }

    @Override
    public AdminVo login(Admin admin) {
        if (this.filterLogin(admin)) {
            throw new BusinessException("管理员参数信息不匹配", PARAM_ERROR.getErrno());
        }
        List<Admin> admins = adminManager.getAdmins(admin);
        // 用户信息匹配单个单个管理员
        if (!admins.isEmpty()) {
            log.error("未查询到相关管理员，管理员名为{}", admin.getAdminName());
        } else if (admins.size() == 1) {
            log.info("查询到相关管理员，管理员id为{}", admin.getAdminId());
            return AdminVo.builder().adminId(admins.get(0).getAdminId()).build();
        } else {
            log.error("相关管理员信息重复，管理员名称为{}", admin.getAdminName());
        }
        // 不匹配返回空用户
        return AdminVo.builder().adminId(NULL_USER_ID).build();
    }

    @Override
    public AdminVo register(Admin admin) {
        Admin res = adminManager.addAdmin(admin);
        if (res.getAdminId() == null) {
            return AdminVo.builder().adminId(NULL_USER_ID).build();
        }
        return AdminVo.builder().adminId(res.getAdminId()).build();
    }

    private boolean filterLogin(Admin admin) {
        return admin.getAdminName() == null || admin.getAdminPwd() == null
                && admin.getAdminStatus() != null || admin.getAdminEmail() != null
                && admin.getAdminId() != null;
    }

}
