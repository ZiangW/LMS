package com.example.lms.service.impl;

import com.example.lms.dao.BookCategoryDao;
import com.example.lms.model.BookCategory;
import com.example.lms.service.BookCategoryManager;
import com.example.lms.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.weekend.WeekendSqls;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
@Transactional
@CacheConfig(cacheNames = "categories")
public class BookCategoryManagerImpl implements BookCategoryManager {

    @Autowired
    private BookCategoryDao bookCategoryDao;
    @Autowired
    private RedisService<BookCategory> redisService;

    @Override
    public BookCategory addBookCategory(BookCategory bookCategory) {
        BookCategory res = new BookCategory();
        // 过滤非法图书分类
        if (!this.filterAddBookCategory(bookCategory)) {
            log.info("图书类别信息不完整");
            return res;
        }
        List<BookCategory> list = bookCategoryDao.selectByExample(Example.builder(BookCategory.class)
                .where(this.selectWithConditions(bookCategory, false)).build());
        if (list.size() > 0) {
            if (list.get(0).getCategoryStatus() == 0) {
                log.info("存在失效图书类别");
                bookCategory.setCategoryId(list.get(0).getCategoryId());
                if (bookCategoryDao.updateByPrimaryKey(bookCategory)== 1) {
                    res = bookCategory;
                } else {
                    log.info("更新图书类别信息失败");
                }
            } else {
                log.info("图书类别已存在");
            }
        } else if (bookCategoryDao.insertSelective(bookCategory) == 1) {
            log.info("不存在失效图书类别");
            list = bookCategoryDao.selectByExample(Example.builder(BookCategory.class)
                    .where(this.selectWithConditions(bookCategory, true)).build());
            if (list.size() == 1) {
                res = list.get(0);
            }
        } else {
            log.info("不存在失效图书类别");
            log.info("添加图书失败类别");
        }
        // CachePut
        if (res.getCategoryId() != null) {
            redisService.addObject("category::set::*", "bookCategoryId::" + res.getCategoryId(), res);
        }
        return res;
    }

    @Override
    public List<Integer> deleteBookCategory(List<BookCategory> list) {
        List<Integer> res = new ArrayList<>();
        for (BookCategory bookCategory : list) {
            bookCategory.setCategoryStatus(0);
            int flag = bookCategoryDao.updateByPrimaryKeySelective(bookCategory);
            res.add(flag);
            // CacheEvict
            if (flag == 1) {
                redisService.deleteObject("bookCategoryId::" + bookCategory.getCategoryId());
            }
        }
        return res;
    }

    @Override
    public BookCategory updateBookCategory(BookCategory bookCategory) {
        BookCategory res = new BookCategory();

        // 过滤非法图书分类
        if (this.filterUpdateBookCategory(bookCategory)) {
            log.info("非法图书类别信息");
            return res;
        }
        if (bookCategoryDao.updateByExampleSelective(bookCategory, Example.builder(BookCategory.class)
                .where(this.updateWithConditions(bookCategory)).build()) == 1) {
            List<BookCategory> list = bookCategoryDao.selectByExample(Example.builder(BookCategory.class)
                    .where(this.selectWithConditions(bookCategory, true)).build());
            if (list.size() == 1) {
                res = list.get(0);
                // CachePut
                redisService.updateObject("bookCategoryId::" + res.getCategoryId(), res);
            } else {
                log.info("存在重复图书类别信息");
            }
        } else {
            log.info("图书类别更新失败");
        }
        return res;
    }

    @Override
    public List<BookCategory> getBookCategory(BookCategory bookCategory) {
        String key = "category::set::" + "bookCategoryId::" + bookCategory.getCategoryId()
                + "bookCategoryName::" + bookCategory.getCategoryName();
        List<BookCategory> list = redisService.selectObjects(key);
        list.sort(Comparator.comparingInt(BookCategory::getCategoryId));
        if (list.size() < 1) {
            log.info("缓存不存在");
            list = bookCategoryDao.selectByExample(Example.builder(BookCategory.class)
                    .where(this.selectWithConditions(bookCategory, true)).build());
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

    private WeekendSqls<BookCategory> updateWithConditions(BookCategory bookCategory) {
        WeekendSqls<BookCategory> conditions = WeekendSqls.custom();
        conditions.andEqualTo(BookCategory::getCategoryId, bookCategory.getCategoryId());
        conditions.andEqualTo(BookCategory::getCategoryStatus, 1);
        return conditions;
    }

    private WeekendSqls<BookCategory> selectWithConditions(BookCategory bookCategory, boolean selectOrUpdate) {
        WeekendSqls<BookCategory> conditions = WeekendSqls.custom();
        if (bookCategory.getCategoryId() != null && bookCategory.getCategoryId() > 0) {
            conditions.andEqualTo(BookCategory::getCategoryId, bookCategory.getCategoryId());
        }
        if (bookCategory.getCategoryName() != null && bookCategory.getCategoryName().length() > 0) {
            conditions.andEqualTo(BookCategory::getCategoryName, bookCategory.getCategoryName());
        }
        if (selectOrUpdate) {
            conditions.andEqualTo(BookCategory::getCategoryStatus, 1);
        }
        return conditions;
    }

    private boolean filterUpdateBookCategory(BookCategory bookCategory) {
        if (bookCategory.getCategoryId() == null) {
            log.info("图书类别id为空");
            return true;
        }
        if (bookCategory.getCategoryName() != null) {
            log.info("图书类别名不为空");
            return true;
        }
        return bookCategory.getCategoryStatus() == null;
    }

    private boolean filterAddBookCategory(BookCategory bookCategory) {
        if (bookCategory.getCategoryId() != null) {
            log.info("图书类别id不为空");
            return false;
        }
        return bookCategory.getCategoryName() != null && bookCategory.getCategoryStatus() != null;
    }

}
