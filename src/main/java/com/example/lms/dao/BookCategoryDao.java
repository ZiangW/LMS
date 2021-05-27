package com.example.lms.dao;

import com.example.lms.model.BookCategory;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * @author 王子昂
 * @date 5/21/21
 * @description BookCategory Dao
 */
public interface BookCategoryDao extends Mapper<BookCategory>, MySqlMapper<BookCategory> {

}
