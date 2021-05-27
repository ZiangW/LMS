package com.example.lms.service;

import com.example.lms.model.Admin;

import java.util.List;

/**
 * @author 王子昂
 * @date 5 /21/21
 * @description 管理员Manager接口
 */
public interface AdminManager {

    /**
     * Add admin admin.
     *
     * @param admin the admin
     * @return the admin
     */
    Admin addAdmin(Admin admin);

    /**
     * Delete admin int.
     *
     * @param admin the admin
     * @return the int
     */
    int deleteAdmin(Admin admin);

    /**
     * Update admin admin.
     *
     * @param admin the admin
     * @return the admin
     */
    Admin updateAdmin(Admin admin);

    /**
     * Gets all admins.
     *
     * @return the all admins
     */
    List<Admin> getAllAdmins();

    /**
     * Gets admins.
     *
     * @param admin the admin
     * @return the admins
     */
    List<Admin> getAdmins(Admin admin);
}
