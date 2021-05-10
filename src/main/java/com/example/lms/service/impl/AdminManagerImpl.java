package com.example.lms.service.impl;

import com.example.lms.dao.AdminDao;
import com.example.lms.dao.UserDao;
import com.example.lms.model.Admin;
import com.example.lms.service.AdminManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
@Transactional
public class AdminManagerImpl implements AdminManager {
    @Autowired
    private AdminDao adminDao;

    @Override
    public int addAdmin(Admin admin) {
        return adminDao.insertSelective(admin);
    }

    @Override
    public int deleteAdmin(Admin admin) {
        return adminDao.deleteByPrimaryKey(admin);
    }

    @Override
    public int updateAdmin(Admin admin) {
        return adminDao.updateByPrimaryKeySelective(admin);
    }

    @Override
    public List<Admin> getAllAdmins() {
        return adminDao.selectAll();
    }

    @Override
    public List<Admin> getAdmins(Admin admin) {
        return adminDao.selectByExample(this.selectWithConditions(admin));
    }

    private Example selectWithConditions(Admin admin) {
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
        return example;
    }
}
