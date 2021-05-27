package com.example.lms.service.impl;

import java.sql.Timestamp;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.lms.exception.BusinessException;
import com.example.lms.model.BorrowedBook;
import com.example.lms.model.ReturnedBook;
import com.example.lms.model.User;
import com.example.lms.service.*;
import com.example.lms.vo.OperationVo;
import com.example.lms.vo.UserVo;

import static com.example.lms.constants.GlobalConstants.*;
import static com.example.lms.enums.RespEnum.PARAM_ERROR;
import static com.example.lms.enums.RespEnum.SERVICE_ERROR;

import lombok.extern.slf4j.Slf4j;

/**
 * @author 王子昂
 * @date 5/21/21
 * @description The type User service.
 *
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserManager         userManager;
    @Autowired
    private BorrowedBookManager borrowedBookManager;
    @Autowired
    private ReturnedBookManager returnedBookManager;
    @Autowired
    private BookManager         bookManager;
    @Autowired
    private RedissonClient      redissonClient;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OperationVo borrowBook(BorrowedBook borrowedBook) {
        if (this.filterBorrow(borrowedBook)) {
            throw new BusinessException("借阅记录信息不合法", PARAM_ERROR.getErrno());
        }

        RLock borrowReturnLock = redissonClient.getFairLock("bookCountLock::" + borrowedBook.getLendBookId());

        try {
            borrowReturnLock.lock(1, TimeUnit.SECONDS);

            // 更新余量
            int res = bookManager.subBookCount(BOOK_COUNT, borrowedBook.getLendBookId());

            // 增加借书记录
            if (res == 1) {
                borrowedBook.setLendDate(new Timestamp(System.currentTimeMillis()));

                if (borrowedBookManager.addRecord(borrowedBook) == 1) {
                    return OperationVo.builder().operationCode(OPERATION_SUCCESS).build();
                } else {
                    throw new BusinessException("借阅记录添加失败，借阅流程中止", SERVICE_ERROR.getErrno());
                }
            } else {
                throw new BusinessException("图书余量不支持借阅，借阅流程中止", SERVICE_ERROR.getErrno());
            }
        } finally {
            borrowReturnLock.unlock();
        }
    }

    private boolean filterBorrow(BorrowedBook borrowedBook) {
        return (borrowedBook.getLendId() != null)
               || (borrowedBook.getLendUserId() == null)
               || (borrowedBook.getLendBookId() == null)
               || (borrowedBook.getLendDate() != null)
               || (borrowedBook.getLendStatus() != null);
    }

    private boolean filterLogin(User user) {
        return (user.getUserName() == null)
               || ((user.getUserPwd() == null) && (user.getUserId() != null))
               || ((user.getUserStatus() != null) && (user.getUserType() != null))
               || ((user.getUserEmail() != null) && (user.getAdminId() != null));
    }

    private boolean filterReturn(ReturnedBook returnedBook) {
        return (returnedBook.getReturnId() != null)
               || (returnedBook.getReturnUserId() == null)
               || (returnedBook.getReturnBookId() == null)
               || (returnedBook.getReturnDate() != null)
               || (returnedBook.getReturnStatus() != null);
    }

    @Override
    public UserVo login(User user) {
        if (this.filterLogin(user)) {
            throw new BusinessException("请求中存在多余参数，请注意请求参数格式", PARAM_ERROR.getErrno());
        }

        List<User> users = userManager.getUsers(user);

        // 用户信息匹配单个用户
        if (users.isEmpty()) {
            log.error("未查询到相关用户，用户名称为{}", user.getUserName());
        } else if (users.size() == 1) {
            log.info("查询到相关用户，用户名称为{}", user.getUserName());

            return UserVo.builder().userId(users.get(0).getUserId()).build();
        } else {
            log.error("查询到多个用户名称相关结果，用户名称为{}", user.getUserName());
        }

        return UserVo.builder().userId(NULL_USER_ID).build();
    }

    @Override
    public UserVo register(User user) {
        User res = userManager.addUser(user);

        if (res.getUserId() == null) {
            throw new BusinessException("注册用户失败", SERVICE_ERROR.getErrno());
        }

        return UserVo.builder().userId(res.getUserId()).build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OperationVo returnBook(ReturnedBook returnedBook) {
        if (this.filterReturn(returnedBook)) {
            throw new BusinessException("归还记录信息不合法", PARAM_ERROR.getErrno());
        }

        RLock borrowReturnLock = redissonClient.getFairLock("bookCountLock::" + returnedBook.getReturnBookId());

        try {
            borrowReturnLock.lock(1, TimeUnit.SECONDS);

            BorrowedBook borrowedBook = new BorrowedBook();

            borrowedBook.setLendBookId(returnedBook.getReturnBookId());
            borrowedBook.setLendUserId(returnedBook.getReturnUserId());

            List<BorrowedBook> borrowList = borrowedBookManager.getRecords(borrowedBook);

            if (borrowList.isEmpty()) {
                throw new BusinessException("借阅记录不存在，还书记录非法存在，还书流程中止", PARAM_ERROR.getErrno());
            }

            borrowedBook = new BorrowedBook();
            borrowedBook.setLendId(borrowList.get(0).getLendId());

            if (borrowedBookManager.deleteRecord(borrowedBook) != 1) {
                throw new BusinessException("借阅记录更新状态失败，还书记录非法存在，还书流程中止", PARAM_ERROR.getErrno());
            }

            // 更新余量
            int res = bookManager.addBookCount(BOOK_COUNT, returnedBook.getReturnBookId());

            // 增加还书记录
            if (res == 1) {
                returnedBook.setReturnDate(new Timestamp(System.currentTimeMillis()));

                if (returnedBookManager.addRecord(returnedBook) == 1) {
                    return OperationVo.builder().operationCode(OPERATION_SUCCESS).build();
                } else {
                    throw new BusinessException("还书记录添加失败，还书流程中止", SERVICE_ERROR.getErrno());
                }
            } else {
                throw new BusinessException("图书余量更新失败，还书失败，还书流程中止", SERVICE_ERROR.getErrno());
            }
        } finally {
            borrowReturnLock.unlock();
        }
    }

    @Override
    public UserVo updateInfo(User user) {
        User res = userManager.updateUser(user);

        if (res.getUserId() == null) {
            throw new BusinessException("更新用户失败", SERVICE_ERROR.getErrno());
        }

        return UserVo.builder().userId(res.getUserId()).build();
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
