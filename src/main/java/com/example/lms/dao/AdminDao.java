package com.example.lms.dao;

import com.example.lms.model.Admin;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * @author 王子昂
 * @date 5/21/21
 * @description Admin Dao
 */
public interface AdminDao extends Mapper<Admin>, MySqlMapper<Admin> {
    
}
