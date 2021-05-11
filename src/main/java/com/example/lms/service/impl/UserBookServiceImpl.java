package com.example.lms.service.impl;

import com.example.lms.model.*;
import com.example.lms.service.*;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@Transactional
public class UserBookServiceImpl implements UserBookService {

    @Autowired
    private BorrowedBookManager borrowedBookManager;
    @Autowired
    private ReturnedBookManager returnedBookManager;
    @Autowired
    private BookManager bookManager;
    @Autowired
    private RedissonClient redissonClient;

    @Override
    public int borrowBook(BorrowedBook borrowedBook) {
        RLock borrowReturnLock = redissonClient.getFairLock("borrowLock::" + borrowedBook.getLendBookId());
        int res = 0;
        try {
            borrowReturnLock.lock(3, TimeUnit.SECONDS);
            // 更新余量
            res = bookManager.subBookCount(1, borrowedBook.getLendBookId());
        } catch (Exception e) {
            log.error(e.getMessage());
            return 2;
        } finally {
            borrowReturnLock.unlock();
        }
        // 增加借书记录
        if (res == 1) {
            return borrowedBookManager.addRecord(borrowedBook);
        }
        return 2;
    }

    @Override
    public int returnBook(ReturnedBook returnedBook) {
        RLock borrowReturnLock = redissonClient.getFairLock("returnLock::" + returnedBook.getReturnBookId());
        int res = 0;
        try {
            borrowReturnLock.lock(3, TimeUnit.SECONDS);
            // 更新余量
            res = bookManager.addBookCount(1, returnedBook.getReturnBookId());
            // 增加借书记录

        } catch (Exception e) {
            log.error(e.getMessage());

        } finally {
            borrowReturnLock.unlock();
        }
        // 增加还书记录
        if (res == 1) {
            return returnedBookManager.addRecord(returnedBook);
        }
        return 2;
    }

}
