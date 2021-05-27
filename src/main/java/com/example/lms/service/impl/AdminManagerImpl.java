package com.example.lms.service.impl;

import com.alibaba.fastjson.JSON;
import com.example.lms.dao.AdminDao;
import com.example.lms.exception.BusinessException;
import com.example.lms.model.Admin;
import com.example.lms.service.AdminManager;
import com.example.lms.service.RedisService;
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

import static com.example.lms.constants.GlobalConstants.ADMIN_KEY_PREFIX;
import static com.example.lms.enums.RespEnum.PARAM_ERROR;

/**
 * @author 王子昂
 * @date 5/21/21
 * @description AdminManager实现
 */
@Slf4j
@Service
@CacheConfig(cacheNames = "admins")
public class AdminManagerImpl implements AdminManager {
    @Autowired
    private AdminDao adminDao;
    @Autowired
    private RedisService<Admin> redisService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Admin addAdmin(Admin admin) {
        if (this.filterAddAdmin(admin)) {
            throw new BusinessException("添加管理员的信息不完整", PARAM_ERROR.getErrno());
        }
        // 密码加密
        String md5 = DigestUtils.md5DigestAsHex(admin.getAdminPwd().getBytes());
        admin.setAdminPwd(md5);

        Admin res = new Admin();
        if (!this.filterAddAdmin(admin)) {
            log.info("管理员信息不完整");
            return res;
        }
        List<Admin> list = adminDao.selectByExample(Example.builder(Admin.class)
                .where(this.selectWithCoreConditions(admin, false)).build());
        if (!list.isEmpty()) {
            if (list.get(0).getAdminStatus() == 0) {
                // 打印失效管理员的id
                log.info("存在失效管理员");
                admin.setAdminId(list.get(0).getAdminId());
                if (adminDao.updateByPrimaryKey(admin) == 1) {
                    res = admin;
                } else {
                    log.error("添加）更新管理员信息失败，参数为{}", JSON.toJSON(admin));
                }
            } else {
                log.error("管理员已存在，不可重复添加，参数为{}", JSON.toJSON(admin));
            }
        } else if (adminDao.insertSelective(admin) == 1) {
            // 新增管理员的id（对应不同case）
            log.info("不存在失效管理员");
            res = admin;
        } else {
            log.info("不存在失效管理员");
            log.error("（添加）插入管理员失败，参数为{}", JSON.toJSON(admin));
        }
        // CachePut
        if (res.getAdminId() != null) {
            redisService.addObject("admin::set::*", ADMIN_KEY_PREFIX + res.getAdminId(), res);
        }
        return res;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteAdmin(Admin admin) {
        if (this.filterDeleteAdmin(admin)) {
            throw new BusinessException("删除管理员提交了多余的字段", PARAM_ERROR.getErrno());
        }
        admin.setAdminStatus(0);
        // CacheEvict
        int flag = adminDao.updateByPrimaryKeySelective(admin);
        if (flag == 1) {
            redisService.deleteObject(ADMIN_KEY_PREFIX + admin.getAdminId());
        } else {
            log.error("删除管理员失败，参数为{}", JSON.toJSON(admin));
        }
        return flag;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Admin updateAdmin(Admin admin) {
        if (this.filterUpdateAdmin(admin)) {
            throw new BusinessException("更新管理员的信息不完整", PARAM_ERROR.getErrno());
        }
        // 密码加密
        if (admin.getAdminPwd() != null) {
            String md5 = DigestUtils.md5DigestAsHex(admin.getAdminPwd().getBytes());
            admin.setAdminPwd(md5);
        }

        Admin res = new Admin();
        if (adminDao.updateByExampleSelective(admin, Example.builder(Admin.class)
                .where(this.updateWithConditions(admin)).build()) == 1) {
            res = admin;
            // CachePut
            redisService.updateObject(ADMIN_KEY_PREFIX + res.getAdminId(), res);
        } else {
            log.error("管理员更新失败，参数为{}", JSON.toJSON(admin));
        }
        return res;
    }

    @Override
    public List<Admin> getAdmins(Admin admin) {
        if (admin.getAdminPwd() != null) {
            throw new BusinessException("查询管理员的信息不合法", PARAM_ERROR.getErrno());
        }
        // Cacheable
        String key = "admin::set::" + ADMIN_KEY_PREFIX + admin.getAdminId()
                + "adminName::" + admin.getAdminName();
        List<Admin> list = redisService.selectObjects(key);
        if (list.isEmpty()) {
            log.info("缓存不存在");
            list = adminDao.selectByExample(Example.builder(Admin.class)
                    .where(this.selectWithConditions(admin)).build());
            List<String> keys = new ArrayList<>();
            for (Admin ad : list) {
                keys.add(ADMIN_KEY_PREFIX + ad.getAdminId());
            }
            redisService.addObjects(key, keys, list);
        }
        return list;
    }

    @Override
    public List<Admin> getAllAdmins() {
        return adminDao.selectAll();
    }

    private WeekendSqls<Admin> updateWithConditions(Admin admin) {
        WeekendSqls<Admin> conditions = WeekendSqls.custom();
        conditions.andEqualTo(Admin::getAdminId, admin.getAdminId());
        conditions.andEqualTo(Admin::getAdminStatus, 1);
        return conditions;
    }

    private WeekendSqls<Admin> selectWithCoreConditions(Admin admin, boolean selectOrUpdate) {
        WeekendSqls<Admin> conditions = WeekendSqls.custom();
        if (admin.getAdminId() != null) {
            conditions.andEqualTo(Admin::getAdminId, admin.getAdminId());
        }
        if (admin.getAdminName() != null) {
            conditions.andEqualTo(Admin::getAdminName, admin.getAdminName());
        }
        if (selectOrUpdate) {
            conditions.andEqualTo(Admin::getAdminStatus, 1);
        }
        return conditions;
    }

    private WeekendSqls<Admin> selectWithConditions(Admin admin) {
        WeekendSqls<Admin> conditions = this.selectWithCoreConditions(admin, true);
        if (admin.getAdminPwd() != null) {
            conditions.andEqualTo(Admin::getAdminPwd, admin.getAdminPwd());
        }
        if (admin.getAdminEmail() != null) {
            conditions.andEqualTo(Admin::getAdminEmail, admin.getAdminEmail());
        }
        return conditions;
    }

    private boolean filterUpdateAdmin(Admin admin) {
        return admin.getAdminId() == null || admin.getAdminName() != null || (admin.getAdminPwd() == null && admin.getAdminEmail() == null
                && admin.getAdminStatus() == null);
    }

    private boolean filterAddAdmin(Admin admin) {
        return admin.getAdminId() != null || admin.getAdminName() == null || admin.getAdminPwd() == null
                || admin.getAdminEmail() == null || admin.getAdminStatus() != null;
    }

    private boolean filterDeleteAdmin(Admin admin) {
        return admin.getAdminId() == null || admin.getAdminPwd() != null
                || admin.getAdminEmail() != null || admin.getAdminName() != null
                || admin.getAdminStatus() != null;
    }

}
