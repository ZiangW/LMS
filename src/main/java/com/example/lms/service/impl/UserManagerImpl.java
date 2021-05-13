package com.example.lms.service.impl;

import com.example.lms.dao.UserDao;
import com.example.lms.model.BookCategory;
import com.example.lms.model.User;
import com.example.lms.service.RedisService;
import com.example.lms.service.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.weekend.WeekendSqls;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@CacheConfig(cacheNames = "users")
public class UserManagerImpl implements UserManager {

    @Autowired
    private UserDao userDao;
    @Autowired
    private RedisService<User> redisService;

    @Override
    public User addUser(User user) {
        List<User> list = userDao.selectByExample(this.checkWithConditions(user, false));
        User res = new User();
        if (list.size() > 0) {
            if (list.get(0).getUserStatus() == 0) {
                user.setUserId(list.get(0).getUserId());
                if (userDao.updateByPrimaryKey(user) == 1) {
                    res = user;
                }
            }
        } else if (userDao.insertSelective(user) == 1) {
            list = userDao.selectByExample(this.checkWithConditions(user, false));
            if (list.size() == 1) {
                res = list.get(0);
            }
        }
        // CachePut
        if (res.getUserId() != null) {
            redisService.addObject("user::set::*", "userId::" + res.getUserId(), res);
        }
        return res;
    }

    @Override
    public List<Integer> deleteUsers(List<User> list) {
        List<Integer> res = new ArrayList<>();
        for (User user : list) {
            user.setUserStatus(0);
            int flag = userDao.updateByPrimaryKeySelective(user);
            res.add(flag);
            // CacheEvict
            if (flag == 1) {
                redisService.deleteObject("userId::" + user.getUserId());
            }
        }
        return res;
    }

    @Override
    public User updateUser(User user) {
        User res = new User();
        if (userDao.updateByExampleSelective(user, Example.builder(User.class).where(updateWithConditions(user)).build()) == 1) {
            List<User> list = userDao.selectByExample(this.checkWithConditions(user, false));
            if (list.size() == 1) {
                res = list.get(0);
                // CachePut
                redisService.updateObject("userId::" + res.getUserId(), res);
            }
        }
        return res;
    }

    @Override
    public List<User> getUsers(User user) {
        // Cacheable
        String key = "user::set::" + "userId::" + user.getUserId()
                + "userName::" + user.getUserName();
        List<User> list = redisService.selectObjects(key);
        if (list.size() < 1) {
            list = userDao.selectByExample(this.selectWithConditions(user, true));
            List<String> keys = new ArrayList<>();
            for (User u : list) {
                keys.add("userId::" + u.getUserId());
            }
            redisService.addObjects(key, keys, list);
        }
        return list;
    }

    @Override
    public List<User> getAllUsers() {
        return userDao.selectAll();
    }

    private WeekendSqls<User> updateWithConditions(User user) {
        WeekendSqls<User> sqls = WeekendSqls.custom();
        sqls.andEqualTo(User::getUserId, user.getUserId());
        sqls.andEqualTo(User::getUserStatus, 1);
        return sqls;
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
