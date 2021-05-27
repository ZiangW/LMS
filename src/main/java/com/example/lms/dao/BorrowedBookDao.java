package com.example.lms.dao;

import com.example.lms.model.BorrowedBook;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * @author 王子昂
 * @date 5/21/21
 * @description BorrowedBook Dao
 */
public interface BorrowedBookDao extends Mapper<BorrowedBook>, MySqlMapper<BorrowedBook> {

}
