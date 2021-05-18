package com.example.lms.service;

import com.example.lms.model.BorrowedBook;
import com.example.lms.model.ReturnedBook;
import com.example.lms.model.User;

public interface UserService {

    User login(User user);

    User register(User user);

    User updateInfo(User user);

    int borrowBook(BorrowedBook borrowedBook);

    int returnBook(ReturnedBook returnedBook);

}
