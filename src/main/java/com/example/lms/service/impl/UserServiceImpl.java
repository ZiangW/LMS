package com.example.lms.service.impl;

import com.example.lms.model.BorrowedBook;
import com.example.lms.model.ReturnedBook;
import com.example.lms.model.User;
import com.example.lms.service.*;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserManager userManager;
    @Autowired
    private BorrowedBookManager borrowedBookManager;
    @Autowired
    private ReturnedBookManager returnedBookManager;
    @Autowired
    private BookManager bookManager;
    @Autowired
    private RedissonClient redissonClient;

    @Override
    public User login(User user) {
        List<User> users = userManager.getUsers(user);
        // 用户信息匹配单个用户
        if (users.size() < 1) {
            log.info("未查询到相关用户");
        } else if (users.size() == 1) {
            if (users.get(0).getUserPwd().equals(user.getUserPwd())) {
//                log.info("查询到相关用户");
                return users.get(0);
            }
            log.info("用户信息不匹配");
        } else {
            // 不匹配返回空用户
            log.info("相关用户信息不唯一");
        }
        return new User();
    }

    @Override
    public User register(User user) {
        return userManager.addUser(user);
    }

    @Override
    public User updateInfo(User user) {
        return userManager.updateUser(user);
    }

    @Override
    public int borrowBook(BorrowedBook borrowedBook) {
        RLock borrowReturnLock = redissonClient.getFairLock("bookCountLock::" + borrowedBook.getLendBookId());
        int res = 0;
        try {
            borrowReturnLock.lock(1, TimeUnit.SECONDS);
            if (borrowedBookManager.getRecords(borrowedBook).size() > 0) {
                log.info("记录已存在");
                return 2;
            }
            // 更新余量
            res = bookManager.subBookCount(1, borrowedBook.getLendBookId());
            // 增加借书记录
            if (res == 1) {
                borrowedBook.setLendDate(new Date());
                return borrowedBookManager.addRecord(borrowedBook);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return 2;
        } finally {
            borrowReturnLock.unlock();
        }
        log.info("借阅记录添加失败");
        return 2;
    }

    @Override
    public int returnBook(ReturnedBook returnedBook) {
        RLock borrowReturnLock = redissonClient.getFairLock("bookCountLock::" + returnedBook.getReturnBookId());
        int res = 0;
        try {
            borrowReturnLock.lock(1, TimeUnit.SECONDS);
            if (returnedBookManager.getRecords(returnedBook).size() > 0) {
                log.info("记录已存在");
                return 2;
            }
            // 更新余量
            res = bookManager.addBookCount(1, returnedBook.getReturnBookId());
            // 增加还书记录
            if (res == 1) {
                returnedBook.setReturnDate(new Date());
                return returnedBookManager.addRecord(returnedBook);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return 2;
        } finally {
            borrowReturnLock.unlock();
        }
        log.info("归还记录添加失败");
        return 2;
    }
}
