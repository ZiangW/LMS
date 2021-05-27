package com.example.lms.dao;

import com.example.lms.model.ReturnedBook;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * @author 王子昂
 * @date 5/21/21
 * @description ReturnedBook Dao
 */
public interface ReturnedBookDao extends Mapper<ReturnedBook>, MySqlMapper<ReturnedBook> {

}
