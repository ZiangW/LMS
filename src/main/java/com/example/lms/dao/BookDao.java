package com.example.lms.dao;

import com.example.lms.model.Book;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * @author 王子昂
 * @date 5/21/21
 * @description Book Dao
 */
public interface BookDao extends Mapper<Book>, MySqlMapper<Book> {

    @Update("UPDATE `book` SET `book_count` = `book_count` - #{borrowCount} " +
            "WHERE `book_id` = #{bookId} AND `book_count` >= #{borrowCount} AND `book_status` = 1")
    int subBookCount(@Param("borrowCount")int borrowCount, @Param("bookId")int bookId);

    @Update("UPDATE `book` SET `book_count` = `book_count` + #{returnCount} " +
            "WHERE `book_id` = #{bookId} AND `book_status` = 1")
    int addBookCount(@Param("returnCount")int returnCount, @Param("bookId")int bookId);

}