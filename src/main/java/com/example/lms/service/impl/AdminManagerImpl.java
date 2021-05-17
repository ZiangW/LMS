package com.example.lms.service.impl;

import com.example.lms.dao.AdminDao;
import com.example.lms.model.Admin;
import com.example.lms.service.AdminManager;
import com.example.lms.service.RedisService;
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
@CacheConfig(cacheNames = "admins")
public class AdminManagerImpl implements AdminManager {
    @Autowired
    private AdminDao adminDao;
    @Autowired
    private RedisService<Admin> redisService;

    @Override
    public Admin addAdmin(Admin admin) {
        Admin res = new Admin();
        if (!this.filterAddAdmin(admin)) {
            return res;
        }
        List<Admin> list = adminDao.selectByExample(Example.builder(Admin.class)
                .where(this.selectWithCoreConditions(admin, false)).build());
        if (list.size() > 0) {
            if (list.get(0).getAdminStatus() == 0) {
                admin.setAdminId(list.get(0).getAdminId());
                if (adminDao.updateByPrimaryKey(admin) == 1) {
                    res = admin;
                }
            }
        } else if (adminDao.insertSelective(admin) == 1) {
            list = adminDao.selectByExample(Example.builder(Admin.class)
                    .where(this.selectWithConditions(admin)).build());
            if (list.size() == 1) {
                res = list.get(0);
            }
        }
        // CachePut
        if (res.getAdminId() != null) {
            redisService.addObject("admin::set::*", "adminId::" + res.getAdminId(), res);
        }
        return res;
    }

    @Override
    public int deleteAdmin(Admin admin) {
        admin.setAdminStatus(0);
        // CacheEvict
        int flag = adminDao.updateByPrimaryKeySelective(admin);
        if (flag == 1) {
            redisService.deleteObject("adminId::" + admin.getAdminId());
        }
        return flag;
    }

    @Override
    public Admin updateAdmin(Admin admin) {
        Admin res = new Admin();
        if (this.filterUpdateAdmin(admin)) {
            return res;
        }
        if (adminDao.updateByExampleSelective(admin, Example.builder(Admin.class)
                .where(this.updateWithConditions(admin)).build()) == 1) {
            List<Admin> list = adminDao.selectByExample(Example.builder(Admin.class)
                    .where(this.selectWithConditions(admin)).build());
            if (list.size() == 1) {
                res = list.get(0);
                // CachePut
                redisService.updateObject("adminId::" + res.getAdminId(), res);
            }
        }
        return res;
    }

    @Override
    public List<Admin> getAdmins(Admin admin) {
        // Cacheable
        String key = "admin::set::" + "adminId::" + admin.getAdminId()
                + "adminName::" + admin.getAdminName();
        List<Admin> list = redisService.selectObjects(key);
        if (list.size() < 1) {
            list = adminDao.selectByExample(Example.builder(Admin.class)
            .where(this.selectWithConditions(admin)).build());
            List<String> keys = new ArrayList<>();
            for (Admin ad : list) {
                keys.add("adminId::" + ad.getAdminId());
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
        if (admin.getAdminId() != null && admin.getAdminId() > 0) {
            conditions.andEqualTo(Admin::getAdminId, admin.getAdminId());
        }
        if (admin.getAdminName() != null && admin.getAdminName().length() > 0) {
            conditions.andEqualTo(Admin::getAdminName, admin.getAdminName());
        }
        if (selectOrUpdate) {
            conditions.andEqualTo(Admin::getAdminStatus, 1);
        }
        return conditions;
    }

    private WeekendSqls<Admin> selectWithConditions(Admin admin) {
        WeekendSqls<Admin> conditions = this.selectWithCoreConditions(admin, true);
        if (admin.getAdminPwd() != null && admin.getAdminPwd().length() > 0) {
            conditions.andEqualTo(Admin::getAdminPwd, admin.getAdminPwd());
        }
        if (admin.getAdminEmail() != null && admin.getAdminEmail().length() > 0) {
            conditions.andEqualTo(Admin::getAdminEmail, admin.getAdminEmail());
        }
        return conditions;
    }

    private boolean filterUpdateAdmin(Admin admin) {
        if (admin.getAdminId() == null) {
            return true;
        }
        if (admin.getAdminName() != null) {
            return true;
        }
        return admin.getAdminPwd() == null && admin.getAdminEmail() == null
                && admin.getAdminStatus() == null;
    }

    private boolean filterAddAdmin(Admin admin) {
        if (admin.getAdminId() != null) {
            return false;
        }
        return admin.getAdminName() != null && admin.getAdminPwd() != null
                && admin.getAdminEmail() != null && admin.getAdminStatus() != null;
    }
}
