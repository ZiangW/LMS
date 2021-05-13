package com.example.lms.service.impl;

import com.example.lms.dao.BookCategoryDao;
import com.example.lms.model.BookCategory;
import com.example.lms.service.BookCategoryManager;
import com.example.lms.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
@Transactional
@CacheConfig(cacheNames = "categories")
public class BookCategoryManagerImpl implements BookCategoryManager {
    @Autowired
    private BookCategoryDao bookCategoryDao;
    @Autowired
    private RedisService<BookCategory> redisService;

    @Override
    public List<BookCategory> getBookCategory(BookCategory bookCategory) {
        String key = "category::set::" + "bookCategoryId::" + bookCategory.getCategoryId()
                + "bookCategoryName::" + bookCategory.getCategoryName();
        List<BookCategory> list = redisService.selectObjects(key);
//        Collections.sort(list, Comparator.comparingInt(BookCategory::getCategoryId));
        if (list.size() < 1) {
            list = bookCategoryDao.selectByExample(this.selectWithConditions(bookCategory, true));
            List<String> keys = new ArrayList<>();
            for (BookCategory bc : list) {
                keys.add("bookCategoryId::" + bc.getCategoryId());
            }
            redisService.addObjects(key, keys, list);
        }
        return list;
    }

    @Override
    public List<BookCategory> getAllBookCategories() {
        return bookCategoryDao.selectAll();
    }

    @Override
    public int addBookCategory(BookCategory bookCategory) {
        List<BookCategory> list = bookCategoryDao.selectByExample(this.selectWithConditions(bookCategory, false));
        int res;
        if (list.size() > 0) {
            if (list.get(0).getCategoryStatus() != 0) {
                // 已存在
                return 2;
            }
            bookCategory.setCategoryId(list.get(0).getCategoryId());
            res = bookCategoryDao.updateByPrimaryKey(bookCategory);
        } else {
            res = bookCategoryDao.insertSelective(bookCategory);
        }
        list = bookCategoryDao.selectByExample(this.selectWithConditions(bookCategory, true));
        if (list.size() == 1) {
            bookCategory = list.get(0);
        }
        if (res == 1) {

            redisService.addObject("category::set::*", "bookCategoryId::" + bookCategory.getCategoryId(), bookCategory);
        }
        return res;
    }

    @Override
    public int updateBookCategory(BookCategory bookCategory) {
        if (bookCategoryDao.updateByPrimaryKeySelective(bookCategory) == 1) {
            List<BookCategory> list = bookCategoryDao.selectByExample(this.selectWithConditions(bookCategory, true));
            if (list.size() == 1) {
                BookCategory bc = list.get(0);
                redisService.updateObject("bookCategoryId::" + bc.getCategoryId(), bc);
                return 1;
            }
            return 2;
        }
        return 0;
    }

    @Override
    public List<Integer> deleteBookCategory(List<BookCategory> list) {
        List<Integer> res = new ArrayList<>();
        for (BookCategory bookCategory : list) {
            bookCategory.setCategoryStatus(0);
            int flag = bookCategoryDao.updateByPrimaryKeySelective(bookCategory);
            res.add(flag);
            if (flag == 1) {
                redisService.deleteObject("bookCategoryId::" + bookCategory.getCategoryId());
            }
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
