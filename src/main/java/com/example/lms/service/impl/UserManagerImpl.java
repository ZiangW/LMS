package com.example.lms.service.impl;

import com.example.lms.dao.UserDao;
import com.example.lms.model.User;
import com.example.lms.service.RedisService;
import com.example.lms.service.UserManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.weekend.WeekendSqls;

import java.util.ArrayList;
import java.util.List;

@Slf4j
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
            log.info("用户信息不完整");
            return res;
        }
        List<User> list = userDao.selectByExample(Example.builder(User.class)
                .where(this.selectWithCoreConditions(user, false)).build());

        if (list.size() > 0) {
            if (list.get(0).getUserStatus() == 0) {
                log.info("存在失效用户");
                user.setUserId(list.get(0).getUserId());
                if (userDao.updateByPrimaryKey(user) == 1) {
                    res = user;
                } else {
                    log.info("更新用户信息失败");
                }
            } else {
                log.info("用户已存在");
            }
        } else if (userDao.insertSelective(user) == 1) {
            log.info("不存在失效用户");
            list = userDao.selectByExample(Example.builder(User.class)
                    .where(this.selectWithConditions(user)).build());
            if (list.size() == 1) {
                res = list.get(0);
            } else {
                log.info("存在重复用户");
            }
        } else {
            log.info("不存在失效用户");
            log.info("添加用户失败");
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
            log.info("非法用户信息");
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
            } else {
                log.info("存在重复用户信息");
            }
        } else {
            log.info("用户更新失败");
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
            log.info("缓存不存在");
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
            log.info("用户id为空");
            return true;
        }
        if (user.getUserName() != null) {
            log.info("用户名不为空");
            return true;
        }
        return user.getUserType() == null && user.getUserEmail() == null
                && user.getUserPwd() == null && user.getUserStatus() == null
                && user.getAdminId() == null;
    }

    private boolean filterAddUser(User user) {
        if (user.getUserId() != null) {
            log.info("用户id不为空");
            return false;
        }
        return user.getUserName() != null && user.getUserType() != null
                && user.getUserEmail() != null && user.getUserPwd() != null
                && user.getUserStatus() != null && user.getAdminId() != null;
    }

}
