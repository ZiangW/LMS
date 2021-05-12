package com.example.lms.service.impl;

import com.example.lms.dao.UserDao;
import com.example.lms.model.User;
import com.example.lms.service.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.weekend.WeekendSqls;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class UserManagerImpl implements UserManager {

    @Autowired
    private UserDao userDao;

    @Override
    public int addUser(User user) {
        List<User> list = userDao.selectByExample(this.checkWithConditions(user, false));
        if (list.size() > 0) {
            if (list.get(0).getUserStatus() == 0) {
                user.setUserId(list.get(0).getUserId());
                return userDao.updateByPrimaryKey(user);
            }
            // 已存在
            return 2;
        }
        return userDao.insertSelective(user);
    }

    @Override
    public List<Integer> deleteUsers(List<User> list) {
        List<Integer> res = new ArrayList<>();
        for (User user : list) {
            user.setUserStatus(0);
            res.add(userDao.updateByPrimaryKeySelective(user));
        }
        return res;
    }

    @Override
    public int updateUser(User user) {
        return userDao.updateByExampleSelective(user, Example.builder(User.class).where(updateWithConditions(user)).build());
    }

    private WeekendSqls<User> updateWithConditions(User user) {
        WeekendSqls<User> sqls = WeekendSqls.custom();
        sqls.andEqualTo(User::getUserId, user.getUserId());
        sqls.andEqualTo(User::getUserStatus, 1);
        return sqls;
    }

    @Override
    public List<User> getAllUsers() {
        return userDao.selectAll();
    }

    @Override
    public List<User> getUsers(User user) {
        return userDao.selectByExample(this.selectWithConditions(user, true));
    }

    @Override
    public List<User> checkUserInfo(User user) {
        return userDao.selectByExample(this.checkWithConditions(user, false));
    }

    private Example checkWithConditions(User user, boolean selectOrUpdate) {
        Example example = new Example(User.class);
        Example.Criteria criteria = example.createCriteria();
        if (user.getUserId() != null && user.getUserId() > 0) {
            criteria.andEqualTo("userId", user.getUserId());
        }
        if (user.getUserName() != null && user.getUserName().length() > 0) {
            criteria.andEqualTo("userName", user.getUserName());
        }
        if (selectOrUpdate) {
            criteria.andEqualTo("userStatus", 1);
        }
        return example;
    }

    private Example selectWithConditions(User user, boolean selectOrUpdate) {
        Example example = new Example(User.class);
        Example.Criteria criteria = example.createCriteria();
        if (user.getUserId() != null && user.getUserId() > 0) {
            criteria.andEqualTo("userId", user.getUserId());
        }
        if (user.getUserName() != null && user.getUserName().length() > 0) {
            criteria.andEqualTo("userName", user.getUserName());
        }
        if (user.getUserPwd() != null && user.getUserPwd().length() > 0) {
            criteria.andEqualTo("userPwd", user.getUserPwd());
        }
        if (user.getUserEmail() != null && user.getUserEmail().length() > 0) {
            criteria.andEqualTo("userEmail", user.getUserEmail());
        }
        if (user.getUserType() != null && user.getUserType().length() > 0) {
            criteria.andEqualTo("userType", user.getUserType());
        }
        if (user.getAdminId() != null && user.getAdminId() > 0) {
            criteria.andEqualTo("adminId", user.getAdminId());
        }
        if (selectOrUpdate) {
            criteria.andEqualTo("userStatus", 1);
        }
        return example;
    }

}
