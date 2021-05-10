package com.example.lms.dao;

import com.example.lms.model.ReturnedBook;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

public interface ReturnedBookDao extends Mapper<ReturnedBook>, MySqlMapper<ReturnedBook> {

}
