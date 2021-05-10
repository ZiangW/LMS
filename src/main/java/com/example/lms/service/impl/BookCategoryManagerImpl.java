package com.example.lms.service.impl;

import com.example.lms.dao.BookCategoryDao;
import com.example.lms.model.BookCategory;
import com.example.lms.service.BookCategoryManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class BookCategoryManagerImpl implements BookCategoryManager {

    @Autowired
    private BookCategoryDao bookCategoryDao;

    @Override
    public List<BookCategory> getBookCategory(BookCategory bookCategory) {
        return bookCategoryDao.selectByExample(this.selectWithConditions(bookCategory, true));
    }

    @Override
    public List<BookCategory> getAllBookCategories() {
        return bookCategoryDao.selectAll();
    }

    @Override
    public int addBookCategory(BookCategory bookCategory) {
        List<BookCategory> list = bookCategoryDao.selectByExample(this.selectWithConditions(bookCategory, false));
        if (list.size() > 0) {
            if (list.get(0).getCategoryStatus() == 0) {
                bookCategory.setCategoryId(list.get(0).getCategoryId());
                return bookCategoryDao.updateByPrimaryKey(bookCategory);
            }
            // 已存在
            return 2;
        }
        return bookCategoryDao.insertSelective(bookCategory);
    }

    @Override
    public int updateBookCategory(BookCategory bookCategory) {
        return bookCategoryDao.updateByPrimaryKeySelective(bookCategory);
    }

    @Override
    public List<Integer> deleteBookCategory(List<BookCategory> list) {
        List<Integer> res = new ArrayList<>();
        for (BookCategory bookCategory : list) {
            bookCategory.setCategoryStatus(0);
            res.add(bookCategoryDao.updateByPrimaryKeySelective(bookCategory));
        }
        return res;
    }

    private Example selectWithConditions(BookCategory bookCategory, boolean selectOrUpdate) {
        Example example = new Example(BookCategory.class);
        Example.Criteria criteria = example.createCriteria();
        if (bookCategory.getCategoryId() != null && bookCategory.getCategoryId() > 0) {
            criteria.andEqualTo("categoryId", bookCategory.getCategoryId());
        }
        if (bookCategory.getCategoryName() != null && bookCategory.getCategoryName().length() > 0) {
            criteria.andEqualTo("categoryName", bookCategory.getCategoryName());
        }
        if (selectOrUpdate) {
            criteria.andEqualTo("categoryStatus", 1);
        }
        return example;
    }

}
