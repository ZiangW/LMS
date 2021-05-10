package com.example.lms.service.impl;

import com.example.lms.model.*;
import com.example.lms.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserBookServiceImpl implements UserBookService {

    @Autowired
    private BorrowedBookManager borrowedBookManager;
    @Autowired
    private ReturnedBookManager returnedBookManager;
    @Autowired
    private BookManager bookManager;

    @Override
    public int borrowBook(BorrowedBook borrowedBook) {
        // 更新余量
        int res = bookManager.subBookCount(1, borrowedBook.getLendBookId());

        // 增加借书记录
        if (res == 1) {
            return borrowedBookManager.addRecord(borrowedBook);
        }
        return 2;

    }

    @Override
    public int returnBook(ReturnedBook returnedBook) {

        bookManager.addBookCount(1, returnedBook.getReturnBookId());

        return returnedBookManager.addRecord(returnedBook);

    }


}
