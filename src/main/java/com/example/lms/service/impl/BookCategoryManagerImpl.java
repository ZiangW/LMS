package com.example.lms.service.impl;

import com.alibaba.fastjson.JSON;
import com.example.lms.dao.BookCategoryDao;
import com.example.lms.exception.BusinessException;
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

import static com.example.lms.constants.GlobalConstants.BOOK_CATEGORY_KEY_PREFIX;
import static com.example.lms.enums.RespEnum.PARAM_ERROR;

/**
 * @author 王子昂
 * @date 5/21/21
 * @description BookCategoryManager实现
 */
@Slf4j
@Service
@CacheConfig(cacheNames = "categories")
public class BookCategoryManagerImpl implements BookCategoryManager {

    @Autowired
    private BookCategoryDao bookCategoryDao;
    @Autowired
    private RedisService<BookCategory> redisService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BookCategory addBookCategory(BookCategory bookCategory) {
        if (this.filterAddBookCategory(bookCategory)) {
            throw new BusinessException("添加图书分类的信息不完整", PARAM_ERROR.getErrno());
        }

        BookCategory res = new BookCategory();
        List<BookCategory> list = bookCategoryDao.selectByExample(Example.builder(BookCategory.class)
                .where(this.selectWithConditions(bookCategory, false)).build());
        if (!list.isEmpty()) {
            if (list.get(0).getCategoryStatus() == 0) {
                log.info("存在失效图书类别");
                bookCategory.setCategoryId(list.get(0).getCategoryId());
                if (bookCategoryDao.updateByPrimaryKey(bookCategory)== 1) {
                    res = bookCategory;
                } else {
                    log.error("（添加）更新图书类别失败，参数为{}", JSON.toJSON(bookCategory));
                }
            } else {
                log.error("存在重复图书类别，参数为{}", JSON.toJSON(bookCategory));
            }
        } else if (bookCategoryDao.insertSelective(bookCategory) == 1) {
            log.info("不存在失效图书类别");
            res = bookCategory;
        } else {
            log.info("不存在失效图书类别");
            log.error("（添加）插入图书类别失败，参数为{}", JSON.toJSON(bookCategory));
        }
        // CachePut
        if (res.getCategoryId() != null) {
            redisService.addObject("category::set::*", BOOK_CATEGORY_KEY_PREFIX + res.getCategoryId(), res);
        }
        return res;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<Integer> deleteBookCategory(List<BookCategory> list) {
        if (this.filterDeleteBookCategory(list)) {
            throw new BusinessException("删除图书分类的信息不合法", PARAM_ERROR.getErrno());
        }

        List<Integer> res = new ArrayList<>();
        for (BookCategory bookCategory : list) {
            bookCategory.setCategoryStatus(0);
            int flag = bookCategoryDao.updateByPrimaryKeySelective(bookCategory);
            res.add(flag);
            // CacheEvict
            if (flag == 1) {
                redisService.deleteObject(BOOK_CATEGORY_KEY_PREFIX + bookCategory.getCategoryId());
            } else {
                log.error("删除图书分类失败，参数为{}", JSON.toJSON(bookCategory));
            }
        }
        return res;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BookCategory updateBookCategory(BookCategory bookCategory) {
        if (this.filterUpdateBookCategory(bookCategory)) {
            throw new BusinessException("更新图书分类的信息不合法", PARAM_ERROR.getErrno());
        }

        BookCategory res = new BookCategory();
        if (bookCategoryDao.updateByExampleSelective(bookCategory, Example.builder(BookCategory.class)
                .where(this.updateWithConditions(bookCategory)).build()) == 1) {
            res = bookCategory;
            // CachePut
            redisService.updateObject(BOOK_CATEGORY_KEY_PREFIX + res.getCategoryId(), res);
        } else {
            log.error("更新图书分类失败，参数为{}", JSON.toJSON(bookCategory));
        }
        return res;
    }

    @Override
    public List<BookCategory> getBookCategory(BookCategory bookCategory) {
        String key = "category::set::" + BOOK_CATEGORY_KEY_PREFIX + bookCategory.getCategoryId()
                + "bookCategoryName::" + bookCategory.getCategoryName();
        List<BookCategory> list = redisService.selectObjects(key);
        list.sort(Comparator.comparingInt(BookCategory::getCategoryId));
        if (list.isEmpty()) {
            log.info("缓存不存在");
            list = bookCategoryDao.selectByExample(Example.builder(BookCategory.class)
                    .where(this.selectWithConditions(bookCategory, true)).build());
            List<String> keys = new ArrayList<>();
            for (BookCategory bc : list) {
                keys.add(BOOK_CATEGORY_KEY_PREFIX + bc.getCategoryId());
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
        if (bookCategory.getCategoryId() != null) {
            conditions.andEqualTo(BookCategory::getCategoryId, bookCategory.getCategoryId());
        }
        if (bookCategory.getCategoryName() != null) {
            conditions.andEqualTo(BookCategory::getCategoryName, bookCategory.getCategoryName());
        }
        if (selectOrUpdate) {
            conditions.andEqualTo(BookCategory::getCategoryStatus, 1);
        }
        return conditions;
    }

    private boolean filterUpdateBookCategory(BookCategory bookCategory) {
        return bookCategory.getCategoryId() == null || bookCategory.getCategoryName() != null
                || bookCategory.getCategoryStatus() == null;
    }

    private boolean filterAddBookCategory(BookCategory bookCategory) {
        return bookCategory.getCategoryId() != null || bookCategory.getCategoryName() == null || bookCategory.getCategoryStatus() != null;
    }

    private boolean filterDeleteBookCategory(List<BookCategory> list) {
        if (list.isEmpty()) {
            return true;
        }
        for (BookCategory bookCategory : list) {
            if (bookCategory.getCategoryId() == null || bookCategory.getCategoryName() != null || bookCategory.getCategoryStatus() != null) {
                return true;
            }
        }
        return false;
    }

}
