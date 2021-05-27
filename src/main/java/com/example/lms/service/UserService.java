package com.example.lms.service;

import com.example.lms.model.BorrowedBook;
import com.example.lms.model.ReturnedBook;
import com.example.lms.model.User;
import com.example.lms.vo.OperationVo;
import com.example.lms.vo.UserVo;

/**
 * @author 王子昂
 * @date 5 /19/21
 * @description 用户服务接口
 */
public interface UserService {

    /**
     * Login user vo.
     *
     * @param user the user
     * @return the user vo
     */
    UserVo login(User user);

    /**
     * Register user vo.
     *
     * @param user the user
     * @return the user vo
     */
    UserVo register(User user);

    /**
     * Update info user vo.
     *
     * @param user the user
     * @return the user vo
     */
    UserVo updateInfo(User user);

    /**
     * Borrow book operation vo.
     *
     * @param borrowedBook the borrowed book
     * @return the operation vo
     */
    OperationVo borrowBook(BorrowedBook borrowedBook);

    /**
     * Return book operation vo.
     *
     * @param returnedBook the returned book
     * @return the operation vo
     */
    OperationVo returnBook(ReturnedBook returnedBook);

}
