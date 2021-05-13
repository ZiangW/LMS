package com.example.lms.service.impl;

import com.example.lms.dao.AdminDao;
import com.example.lms.dao.UserDao;
import com.example.lms.model.Admin;
import com.example.lms.model.User;
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
        List<Admin> list = adminDao.selectByExample(this.selectWithConditions(admin, false));
        Admin res = new Admin();
        if (list.size() > 0) {
            if (list.get(0).getAdminStatus() == 0) {
                admin.setAdminId(list.get(0).getAdminId());
                if (adminDao.updateByPrimaryKey(admin) == 1) {
                    res = admin;
                }
            }
        } else if (adminDao.insertSelective(admin) == 1) {
            list = adminDao.selectByExample(this.selectWithConditions(admin, false));
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
        if (adminDao.updateByExampleSelective(admin, Example.builder(Admin.class).where(updateWithConditions(admin)).build()) == 1) {
            List<Admin> list = adminDao.selectByExample(this.selectWithConditions(admin, false));
            if (list.size() == 1) {
                res = list.get(0);
                // CachePut
                redisService.updateObject("adminId::" + res.getAdminId(), res);
            }
        }
        return res;
    }

    @Override
    public List<Admin> getAllAdmins() {
        return adminDao.selectAll();
    }

    @Override
    public List<Admin> getAdmins(Admin admin) {
        // Cacheable
        String key = "admin::set::" + "adminId::" + admin.getAdminId()
                + "adminName::" + admin.getAdminName();
        List<Admin> list = redisService.selectObjects(key);
        if (list.size() < 1) {
            list = adminDao.selectByExample(this.selectWithConditions(admin, true));
            List<String> keys = new ArrayList<>();
            for (Admin ad : list) {
                keys.add("adminId::" + ad.getAdminId());
            }
            redisService.addObjects(key, keys, list);
        }
        return list;
    }

    private WeekendSqls<Admin> updateWithConditions(Admin admin) {
        WeekendSqls<Admin> sqls = WeekendSqls.custom();
        sqls.andEqualTo(Admin::getAdminId, admin.getAdminId());
        sqls.andEqualTo(Admin::getAdminStatus, 1);
        return sqls;
    }

    private Example selectWithConditions(Admin admin, boolean selectOrUpdate) {
        Example example = new Example(Admin.class);
        Example.Criteria criteria = example.createCriteria();
        if (admin.getAdminId() != null && admin.getAdminId() > 0) {
            criteria.andEqualTo("adminId", admin.getAdminId());
        }
        if (admin.getAdminName() != null && admin.getAdminName().length() > 0) {
            criteria.andEqualTo("adminName", admin.getAdminName());
        }
        if (admin.getAdminPwd() != null && admin.getAdminPwd().length() > 0) {
            criteria.andEqualTo("adminPwd", admin.getAdminPwd());
        }
        if (admin.getAdminEmail() != null && admin.getAdminEmail().length() > 0) {
            criteria.andEqualTo("adminEmail", admin.getAdminEmail());
        }
        if (selectOrUpdate) {
            criteria.andEqualTo("adminStatus", 1);
        }
        return example;
    }
}
