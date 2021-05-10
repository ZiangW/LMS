package com.example.lms.dao;

import com.example.lms.model.Admin;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

public interface AdminDao extends Mapper<Admin>, MySqlMapper<Admin> {
    
}
