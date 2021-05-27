package com.example.lms.dao;

import com.example.lms.model.User;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * @author 王子昂
 * @date 5/21/21
 * @description User Dao
 */
public interface UserDao extends Mapper<User>, MySqlMapper<User> {

}
