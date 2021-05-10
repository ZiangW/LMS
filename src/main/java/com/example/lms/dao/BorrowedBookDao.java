package com.example.lms.dao;

import com.example.lms.model.BorrowedBook;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

public interface BorrowedBookDao extends Mapper<BorrowedBook>, MySqlMapper<BorrowedBook> {

}
