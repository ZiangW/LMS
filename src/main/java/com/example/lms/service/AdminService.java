package com.example.lms.service;

import com.example.lms.model.Admin;
import com.example.lms.model.Book;
import com.example.lms.model.BookCategory;
import com.example.lms.model.User;

import java.util.List;

public interface AdminService {

    Admin login(Admin admin);

    User addUser(User user);

    User updateUser(User user);

    List<Integer> deleteUser(List<User> list);

    List<User> getUsers(User user);

    Book addBooks(Book book);

    List<Integer> deleteBooks(List<Book> list);

    Book updateBooks(Book book);

    List<Book> getBooks(Book book);

    List<BookCategory> getBookCategory(BookCategory bookCategory);

    BookCategory addBookCategory(BookCategory bookCategory);

    BookCategory updateBookCategory(BookCategory bookCategory);

    List<Integer> deleteBookCategory(List<BookCategory> list);

}
