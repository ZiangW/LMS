package com.example.lms.controller;

import com.example.lms.model.Admin;
import com.example.lms.model.Book;
import com.example.lms.model.BookCategory;
import com.example.lms.model.User;
import com.example.lms.service.*;
import com.example.lms.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 王子昂
 * @date 5/21/21
 * @description Admin controller
 */
@RestController
@RequestMapping(value = "/Admins")
public class AdminController {

    @Autowired
    private AdminManager adminManager;
    @Autowired
    private AdminService adminService;
    @Autowired
    private UserService userService;

    /**
     * Add admin admin.
     *
     * @param admin the admin
     * @return the admin
     */
    @PostMapping(value = "/addAdmins")
    @ResponseBody
    public Admin addAdmin(@RequestBody Admin admin) {
        return adminManager.addAdmin(admin);
    }

    /**
     * Delete admins int.
     *
     * @param admin the admin
     * @return the int
     */
    @PostMapping(value = "/deleteAdmins")
    @ResponseBody
    public int deleteAdmins(@RequestBody Admin admin) {
        return adminManager.deleteAdmin(admin);
    }

    /**
     * Update admin admin.
     *
     * @param admin the admin
     * @return the admin
     */
    @PostMapping(value = "/updateAdmins")
    @ResponseBody
    public Admin updateAdmin(@RequestBody Admin admin) {
        return adminManager.updateAdmin(admin);
    }

    /**
     * Gets admins.
     *
     * @param admin the admin
     * @return the admins
     */
    @PostMapping(value = "/getAdmins")
    @ResponseBody
    public List<Admin> getAdmins(@RequestBody Admin admin) {
        return adminManager.getAdmins(admin);
    }

    /**
     * Gets all admins.
     *
     * @return the all admins
     */
    @GetMapping(value = "/getAllAdmins")
    @ResponseBody
    public List<Admin> getAllAdmins() {
        return adminManager.getAllAdmins();
    }

    /**
     * Login response vo.
     * 对外暴露
     * @param admin the admin
     * @return the response vo
     * @throws Exception the exception
     */
    @PostMapping(value = "/login")
    @ResponseBody
    public ResponseVo<AdminVo> login(@RequestBody Admin admin) throws Exception {
        return ResponseVo.getSuccessResp(adminService.login(admin));
    }

    /**
     * Register response vo.
     * 对外暴露
     * @param admin the admin
     * @return the response vo
     * @throws Exception the exception
     */
    @PostMapping(value = "/register")
    @ResponseBody
    public ResponseVo<AdminVo> register(@RequestBody Admin admin) throws Exception {
        return ResponseVo.getSuccessResp(adminService.register(admin));
    }

    /**
     * Add user response vo.
     * 对外暴露
     * @param user the user
     * @return the response vo
     * @throws Exception the exception
     */
    @PostMapping(value = "/addUsers")
    @ResponseBody
    public ResponseVo<UserVo> addUser(@RequestBody User user) throws Exception {
        return ResponseVo.getSuccessResp(userService.register(user));
    }

    /**
     * Update user response vo.
     * 对外暴露
     * @param user the user
     * @return the response vo
     * @throws Exception the exception
     */
    @PostMapping(value = "/updateUsers")
    @ResponseBody
    public ResponseVo<UserVo> updateUser(@RequestBody User user) throws Exception {
        return ResponseVo.getSuccessResp(userService.updateInfo(user));
    }

    /**
     * Delete users response vo.
     * 对外暴露
     * @param list the list
     * @return the response vo
     * @throws Exception the exception
     */
    @PostMapping(value = "/deleteUsers")
    @ResponseBody
    public ResponseVo<List<OperationVo>> deleteUsers(@RequestBody List<User> list) throws Exception {
        return ResponseVo.getSuccessResp(adminService.deleteUser(list));
    }

    /**
     * Gets users.
     * 对外暴露
     * @param user the user
     * @return the users
     * @throws Exception the exception
     */
    @PostMapping(value = "/getUsers")
    @ResponseBody
    public ResponseVo<List<User>> getUsers(@RequestBody User user) throws Exception {
        return ResponseVo.getSuccessResp(adminService.getUsers(user));
    }

    /**
     * Add book response vo.
     * 对外暴露
     * @param book the book
     * @return the response vo
     * @throws Exception the exception
     */
    @PostMapping(value = "/addBooks")
    @ResponseBody
    public ResponseVo<BookVo> addBook(@RequestBody Book book) throws Exception {
        return ResponseVo.getSuccessResp(adminService.addBooks(book));
    }

    /**
     * Delete book response vo.
     * 对外暴露
     * @param list the list
     * @return the response vo
     * @throws Exception the exception
     */
    @PostMapping(value = "/deleteBooks")
    @ResponseBody
    public ResponseVo<List<OperationVo>> deleteBook(@RequestBody List<Book> list) throws Exception {
        return ResponseVo.getSuccessResp(adminService.deleteBooks(list));
    }

    /**
     * Update book response vo.
     * 对外暴露
     * @param book the book
     * @return the response vo
     * @throws Exception the exception
     */
    @PostMapping(value = "/updateBooks")
    @ResponseBody
    public ResponseVo<BookVo> updateBook(@RequestBody Book book) throws Exception {
        return ResponseVo.getSuccessResp(adminService.updateBooks(book));
    }

    /**
     * Gets books.
     * 对外暴露
     * @param book the book
     * @return the books
     * @throws Exception the exception
     */
    @PostMapping(value = "/getBooks")
    @ResponseBody
    public ResponseVo<List<Book>> getBooks(@RequestBody Book book) throws Exception {
        return ResponseVo.getSuccessResp(adminService.getBooks(book));
    }

    /**
     * Gets book category.
     * 对外暴露
     * @param bookCategory the book category
     * @return the book category
     * @throws Exception the exception
     */
    @PostMapping(value = "/getBookCategory")
    @ResponseBody
    public ResponseVo<List<BookCategory>> getBookCategory(@RequestBody BookCategory bookCategory) throws Exception {
        return ResponseVo.getSuccessResp(adminService.getBookCategory(bookCategory));
    }

    /**
     * Add book category response vo.
     * 对外暴露
     * @param bookCategory the book category
     * @return the response vo
     * @throws Exception the exception
     */
    @PostMapping(value = "/addBookCategory")
    @ResponseBody
    public ResponseVo<BookCategoryVo> addBookCategory(@RequestBody BookCategory bookCategory) throws Exception {
        return ResponseVo.getSuccessResp(adminService.addBookCategory(bookCategory));
    }

    /**
     * Delete book category response vo.
     * 对外暴露
     * @param list the list
     * @return the response vo
     * @throws Exception the exception
     */
    @PostMapping(value = "/deleteBookCategory")
    @ResponseBody
    public ResponseVo<List<OperationVo>> deleteBookCategory(@RequestBody List<BookCategory> list) throws Exception {
        return ResponseVo.getSuccessResp(adminService.deleteBookCategory(list));
    }
}
