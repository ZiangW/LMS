package com.example.lms.service.impl;

import com.example.lms.dao.UserDao;
import com.example.lms.model.User;
import com.example.lms.service.RedisService;
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
@CacheConfig(cacheNames = "users")
public class UserManagerImpl implements UserManager {

    @Autowired
    private UserDao userDao;
    @Autowired
    private RedisService<User> redisService;

    @Override
    public User addUser(User user) {
        User res = new User();
        if (!this.filterAddUser(user)) {
            return res;
        }
        List<User> list = userDao.selectByExample(Example.builder(User.class)
                .where(this.selectWithCoreConditions(user, false)).build());

        if (list.size() > 0) {
            if (list.get(0).getUserStatus() == 0) {
                user.setUserId(list.get(0).getUserId());
                if (userDao.updateByPrimaryKey(user) == 1) {
                    res = user;
                }
            }
        } else if (userDao.insertSelective(user) == 1) {
            list = userDao.selectByExample(Example.builder(User.class)
                    .where(this.selectWithConditions(user)).build());
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
        if (this.filterUpdateUser(user)) {
            return res;
        }
        if (userDao.updateByExampleSelective(user, Example.builder(User.class)
                .where(this.updateWithConditions(user)).build()) == 1) {
            List<User> list = userDao.selectByExample(Example.builder(User.class)
                    .where(this.selectWithConditions(user)).build());
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
            list = userDao.selectByExample(Example.builder(User.class)
                    .where(this.selectWithConditions(user)).build());
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
        WeekendSqls<User> conditions = WeekendSqls.custom();
        conditions.andEqualTo(User::getUserId, user.getUserId());
        conditions.andEqualTo(User::getUserStatus, 1);
        return conditions;
    }

    private WeekendSqls<User> selectWithCoreConditions(User user, boolean selectOrUpdate) {
        WeekendSqls<User> conditions = WeekendSqls.custom();
        if (user.getUserId() != null && user.getUserId() > 0) {
            conditions.andEqualTo(User::getUserId, user.getUserId());
        }
        if (user.getUserName() != null && user.getUserName().length() > 0) {
            conditions.andEqualTo(User::getUserName, user.getUserName());
        }
        if (selectOrUpdate) {
            conditions.andEqualTo(User::getUserStatus, 1);
        }
        return conditions;
    }


    private WeekendSqls<User> selectWithConditions(User user) {
        WeekendSqls<User> conditions = this.selectWithCoreConditions(user, true);
        if (user.getUserPwd() != null && user.getUserPwd().length() > 0) {
            conditions.andEqualTo(User::getUserPwd, user.getUserPwd());
        }
        if (user.getUserEmail() != null && user.getUserEmail().length() > 0) {
            conditions.andEqualTo(User::getUserEmail, user.getUserEmail());
        }
        if (user.getUserType() != null && user.getUserType().length() > 0) {
            conditions.andEqualTo(User::getUserType, user.getUserType());
        }
        if (user.getAdminId() != null && user.getAdminId() > 0) {
            conditions.andEqualTo(User::getAdminId, user.getAdminId());
        }
        return conditions;
    }

    private boolean filterUpdateUser(User user) {
        if (user.getUserId() == null) {
            return true;
        }
        if (user.getUserName() != null) {
            return true;
        }
        return user.getUserType() == null && user.getUserEmail() == null
                && user.getUserPwd() == null && user.getUserStatus() == null
                && user.getAdminId() == null;
    }

    private boolean filterAddUser(User user) {
        if (user.getUserId() != null) {
            return false;
        }
        return user.getUserName() != null && user.getUserType() != null
                && user.getUserEmail() != null && user.getUserPwd() != null
                && user.getUserStatus() != null && user.getAdminId() != null;
    }
}
