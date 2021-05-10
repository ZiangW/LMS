package com.example.lms.service;

import com.example.lms.model.*;

public interface UserBookService {

    int borrowBook(BorrowedBook borrowedBook);

    int returnBook(ReturnedBook returnedBook);

}
