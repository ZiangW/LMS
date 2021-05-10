package com.example.lms.dao;

import com.example.lms.model.User;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

public interface UserDao extends Mapper<User>, MySqlMapper<User> {

}
