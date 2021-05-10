package com.example.lms.service;

import com.example.lms.model.Admin;

import java.util.List;

public interface AdminManager {

    int addAdmin(Admin admin);

    int deleteAdmin(Admin admin);

    int updateAdmin(Admin admin);

    List<Admin> getAllAdmins();

    List<Admin> getAdmins(Admin admin);
}
