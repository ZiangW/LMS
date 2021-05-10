package com.example.lms.dao;

import com.example.lms.model.BookCategory;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

public interface BookCategoryDao extends Mapper<BookCategory>, MySqlMapper<BookCategory> {

}
