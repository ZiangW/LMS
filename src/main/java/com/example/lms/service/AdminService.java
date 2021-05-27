package com.example.lms.service;

import com.example.lms.model.Admin;
import com.example.lms.model.Book;
import com.example.lms.model.BookCategory;
import com.example.lms.model.User;
import com.example.lms.vo.AdminVo;
import com.example.lms.vo.BookCategoryVo;
import com.example.lms.vo.BookVo;
import com.example.lms.vo.OperationVo;

import java.util.List;

/**
 * @author 王子昂
 * @date 5 /21/21
 * @description 管理员服务接口
 */
public interface AdminService {

    /**
     * Login admin vo.
     *
     * @param admin the admin
     * @return the admin vo
     */
    AdminVo login(Admin admin);

    /**
     * Register admin vo.
     *
     * @param admin the admin
     * @return the admin vo
     */
    AdminVo register(Admin admin);

    /**
     * Delete user list.
     *
     * @param list the list
     * @return the list
     */
    List<OperationVo> deleteUser(List<User> list);

    /**
     * Gets users.
     *
     * @param user the user
     * @return the users
     */
    List<User> getUsers(User user);

    /**
     * Add books book vo.
     *
     * @param book the book
     * @return the book vo
     */
    BookVo addBooks(Book book);

    /**
     * Delete books list.
     *
     * @param list the list
     * @return the list
     */
    List<OperationVo> deleteBooks(List<Book> list);

    /**
     * Update books book vo.
     *
     * @param book the book
     * @return the book vo
     */
    BookVo updateBooks(Book book);

    /**
     * Gets books.
     *
     * @param book the book
     * @return the books
     */
    List<Book> getBooks(Book book);

    /**
     * Gets book category.
     *
     * @param bookCategory the book category
     * @return the book category
     */
    List<BookCategory> getBookCategory(BookCategory bookCategory);

    /**
     * Add book category book category vo.
     *
     * @param bookCategory the book category
     * @return the book category vo
     */
    BookCategoryVo addBookCategory(BookCategory bookCategory);

    /**
     * Delete book category list.
     *
     * @param list the list
     * @return the list
     */
    List<OperationVo> deleteBookCategory(List<BookCategory> list);

}
