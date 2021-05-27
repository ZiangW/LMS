package com.example.lms.service.impl;

import com.alibaba.fastjson.JSON;
import com.example.lms.dao.UserDao;
import com.example.lms.exception.BusinessException;
import com.example.lms.model.User;
import com.example.lms.service.RedisService;
import com.example.lms.service.UserManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.weekend.WeekendSqls;

import java.util.ArrayList;
import java.util.List;

import static com.example.lms.constants.GlobalConstants.USER_KEY_PREFIX;
import static com.example.lms.enums.RespEnum.PARAM_ERROR;

/**
 * @author 王子昂
 * @date 5/21/21
 * @description UserManager实现
 */
@Slf4j
@Service
@CacheConfig(cacheNames = "users")
public class UserManagerImpl implements UserManager {

    @Autowired
    private UserDao userDao;
    @Autowired
    private RedisService<User> redisService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public User addUser(User user) {
        if (this.filterAddUser(user)) {
            throw new BusinessException("添加用户的信息不完整", PARAM_ERROR.getErrno());
        }
        // 密码加密
        String md5 = DigestUtils.md5DigestAsHex(user.getUserPwd().getBytes());
        user.setUserPwd(md5);

        User res = new User();
        List<User> list = userDao.selectByExample(Example.builder(User.class)
                .where(this.selectWithCoreConditions(user, false)).build());

        if (!list.isEmpty()) {
            if (list.get(0).getUserStatus() == 0) {
                log.info("存在失效用户，用户id为{}", list.get(0).getUserId());
                user.setUserId(list.get(0).getUserId());
                if (userDao.updateByPrimaryKey(user) == 1) {
                    log.info("（更新覆盖）创建用户成功，用户id为{}", user.getUserId());
                    res = user;
                } else {
                    log.error("添加）更新用户信息失败，用户id为{}", user.getUserId());
                }
            } else {
                log.error("用户已存在，不可重复添加，用户id为{}", user.getUserId());
            }
        } else if (userDao.insertSelective(user) == 1) {
            log.info("不存在失效用户");
            log.info("创建用户成功，用户id为{}", user.getUserId());
            res = user;
        } else {
            log.info("不存在失效用户");
            log.error("（添加）插入用户信息失败，参数为{}", JSON.toJSON(user));
        }
        // CachePut
        if (res.getUserId() != null) {
            redisService.addObject("user::set::*", USER_KEY_PREFIX + res.getUserId(), res);
        }
        return res;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<Integer> deleteUsers(List<User> list) {
        if (this.filterDeleteUser(list)) {
            throw new BusinessException("删除用户的信息存在多余参数", PARAM_ERROR.getErrno());
        }
        List<Integer> res = new ArrayList<>();
        for (User user : list) {
            user.setUserStatus(0);
            int flag = userDao.updateByPrimaryKeySelective(user);
            res.add(flag);
            // CacheEvict
            if (flag == 1) {
                log.info("删除用户成功，用户id为{}", user.getUserId());
                redisService.deleteObject(USER_KEY_PREFIX + user.getUserId());
            } else {
                log.error("删除用户失败，用户id为{}", user.getUserId());
            }
        }
        return res;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public User updateUser(User user) {
        if (this.filterUpdateUser(user)) {
            throw new BusinessException("更新用户的信息不合法", PARAM_ERROR.getErrno());
        }
        // 密码加密
        if (user.getUserPwd() != null) {
            String md5 = DigestUtils.md5DigestAsHex(user.getUserPwd().getBytes());
            user.setUserPwd(md5);
        }

        User res = new User();
        if (userDao.updateByExampleSelective(user, Example.builder(User.class)
                .where(this.updateWithConditions(user)).build()) == 1) {
            log.info("更新用户成功，用户id为{}", user.getUserId());
            // CachePut
            redisService.updateObject(USER_KEY_PREFIX + user.getUserId(), user);
        } else {
            log.error("更新用户信息失败，用户id为{}", user.getUserId());
        }
        return res;
    }

    @Override
    public List<User> getUsers(User user) {
        if (user.getUserPwd() != null) {
            String md5 = DigestUtils.md5DigestAsHex(user.getUserPwd().getBytes());
            user.setUserPwd(md5);
        }
        // Cacheable
        String key = "user::set::" + USER_KEY_PREFIX + user.getUserId()
                + "userName::" + user.getUserName();
        List<User> list = redisService.selectObjects(key);
        if (list.isEmpty()) {
            log.info("缓存不存在");
            list = userDao.selectByExample(Example.builder(User.class)
                    .where(this.selectWithConditions(user)).build());
            List<String> keys = new ArrayList<>();
            for (User u : list) {
                keys.add(USER_KEY_PREFIX + u.getUserId());
            }
            redisService.addObjects(key, keys, list);
        } else {
            log.info("缓存存在，缓存key为{}", key);
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
        if (user.getUserId() != null) {
            conditions.andEqualTo(User::getUserId, user.getUserId());
        }
        if (user.getUserName() != null) {
            conditions.andEqualTo(User::getUserName, user.getUserName());
        }
        if (selectOrUpdate) {
            conditions.andEqualTo(User::getUserStatus, 1);
        }
        return conditions;
    }

    private WeekendSqls<User> selectWithConditions(User user) {
        WeekendSqls<User> conditions = this.selectWithCoreConditions(user, true);
        if (user.getUserPwd() != null) {
            conditions.andEqualTo(User::getUserPwd, user.getUserPwd());
        }
        if (user.getUserEmail() != null) {
            conditions.andEqualTo(User::getUserEmail, user.getUserEmail());
        }
        if (user.getUserType() != null) {
            conditions.andEqualTo(User::getUserType, user.getUserType());
        }
        if (user.getAdminId() != null) {
            conditions.andEqualTo(User::getAdminId, user.getAdminId());
        }
        return conditions;
    }

    private boolean filterUpdateUser(User user) {
        return user.getUserId() == null || user.getUserName() != null || (user.getUserType() == null && user.getUserEmail() == null
                && user.getUserPwd() == null && user.getUserStatus() == null
                && user.getAdminId() == null);
    }

    private boolean filterAddUser(User user) {
        return user.getUserId() != null || user.getUserName() == null || user.getUserType() == null
                || user.getUserEmail() == null || user.getUserPwd() == null
                || user.getAdminId() == null || user.getUserStatus() != null;
    }

    private boolean filterDeleteUser(List<User> list) {
        if (list.isEmpty()) {
            return true;
        }
        for (User user : list) {
            if (user.getUserId() == null || user.getUserName() != null || user.getUserType() != null
                    || user.getUserEmail() != null || user.getUserPwd() != null
                    || user.getAdminId() != null || user.getUserStatus() != null) {
                return true;
            }
        }
        return false;
    }

}
