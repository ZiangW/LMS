package com.example.lms.service.impl;

import com.alibaba.fastjson.JSON;
import com.example.lms.dao.BookDao;
import com.example.lms.exception.BusinessException;
import com.example.lms.model.Book;
import com.example.lms.service.BookManager;
import com.example.lms.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.weekend.WeekendSqls;

import java.util.ArrayList;
import java.util.List;

import static com.example.lms.constants.GlobalConstants.BOOK_KEY_PREFIX;
import static com.example.lms.enums.RespEnum.PARAM_ERROR;

/**
 * @author 王子昂
 * @date 5/21/21
 * @description BookManager实现
 */
@Slf4j
@Service
@CacheConfig(cacheNames = "books")
public class BookManagerImpl implements BookManager {

    @Autowired
    private BookDao bookDao;
    @Autowired
    private RedisService<Book> redisService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Book addBooks(Book book) {
        if (this.filterAddBook(book)) {
            throw new BusinessException("添加图书的信息不完整", PARAM_ERROR.getErrno());
        }

        Book res = new Book();
        List<Book> list = bookDao.selectByExample(Example.builder(Book.class)
                .where(this.selectWithCoreConditions(book, false)).build());
        if (!list.isEmpty()) {
            if (list.get(0).getBookStatus() == 0) {
                log.info("存在失效图书");
                book.setBookId(list.get(0).getBookId());
                book.setBookStatus(1);
                if (bookDao.updateByPrimaryKey(book) == 1) {
                    res = book;
                } else {
                    log.error("（添加）更新图书信息失败，参数为{}", JSON.toJSON(book));
                }
            } else {
                log.error("图书已存在，不可重复添加，参数为{}", JSON.toJSON(book));
            }
        } else if (bookDao.insertSelective(book) == 1) {
            log.info("不存在失效图书");
            res = book;
        } else {
            log.info("不存在失效图书");
            log.error("（添加）插入图书信息失败，参数为{}", JSON.toJSON(book));
        }
        // CachePut
        if (res.getBookId() != null) {
            redisService.addObject("book::set::*", BOOK_KEY_PREFIX + res.getBookId(), res);
        }
        return res;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<Integer> deleteBooks(List<Book> list) {
        if (this.filterDeleteBook(list)) {
            throw new BusinessException("删除图书的信息不合法", PARAM_ERROR.getErrno());
        }

        List<Integer> res = new ArrayList<>();
        for (Book book : list) {
            book.setBookStatus(0);
            int flag = bookDao.updateByPrimaryKeySelective(book);
            res.add(flag);
            // CacheEvict
            if (flag == 1) {
                redisService.deleteObject(BOOK_KEY_PREFIX + book.getBookId());
            } else {
                log.error("删除图书失败，参数为{}", JSON.toJSON(book));
            }
        }
        return res;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int subBookCount(int borrowCount, int bookId) {
        return bookDao.subBookCount(borrowCount, bookId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int addBookCount(int returnCount, int bookId) {
        return bookDao.addBookCount(returnCount, bookId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Book updateBooks(Book book) {
        if (this.filterUpdateBook(book)) {
            throw new BusinessException("更新图书的信息不合法", PARAM_ERROR.getErrno());
        }

        Book res = new Book();
        if (bookDao.updateByExampleSelective(book, Example.builder(Book.class)
                .where(this.updateWithConditions(book)).build()) == 1) {
            res = book;
            // CachePut
            redisService.updateObject(BOOK_KEY_PREFIX + res.getBookId(), res);
        } else {
            log.error("更新图书失败，参数为{}", JSON.toJSON(book));
        }
        return res;
    }

    @Override
    public List<Book> getBooks(Book book) {
        // Cacheable
        String key = "book::set::" + BOOK_KEY_PREFIX + book.getBookId()
                + "bookName::" + book.getBookName();
        List<Book> list = redisService.selectObjects(key);
        if (list.isEmpty()) {
            log.info("缓存不存在");
            list = bookDao.selectByExample(Example.builder(Book.class)
                    .where(this.selectWithConditions(book)).build());
            List<String> keys = new ArrayList<>();
            for (Book b : list) {
                keys.add(BOOK_KEY_PREFIX + b.getBookId());
            }
            redisService.addObjects(key, keys, list);
        }
        return list;
    }

    @Override
    public List<Book> getAllBooks() {
        return bookDao.selectAll();
    }

    private WeekendSqls<Book> updateWithConditions(Book book) {
        WeekendSqls<Book> conditions = WeekendSqls.custom();
        conditions.andEqualTo(Book::getBookId, book.getBookId());
        conditions.andEqualTo(Book::getBookStatus, 1);
        return conditions;
    }

    private WeekendSqls<Book> selectWithCoreConditions(Book book, boolean selectOrUpdate) {
        WeekendSqls<Book> conditions = WeekendSqls.custom();
        if (book.getBookId() != null) {
            conditions.andEqualTo(Book::getBookId, book.getBookId());
        }
        if (book.getBookName() != null) {
            conditions.andEqualTo(Book::getBookName, book.getBookName());
        }
        if (selectOrUpdate && book.getBookStatus() != null) {
            conditions.andEqualTo(Book::getBookStatus, 1);
        }
        return conditions;
    }

    private WeekendSqls<Book> selectWithConditions(Book book) {
        WeekendSqls<Book> conditions = this.selectWithCoreConditions(book, true);
        if (book.getBookAuthor() != null) {
            conditions.andEqualTo(Book::getBookAuthor, book.getBookAuthor());
        }
        if (book.getBookCategory() != null) {
            conditions.andEqualTo(Book::getBookCategory, book.getBookCategory());
        }
        return conditions;
    }

    private boolean filterUpdateBook(Book book) {
        return book.getBookId() == null || book.getBookName() != null
                || (book.getBookCount() == null && book.getBookCategory() == null
                && book.getBookAuthor() == null && book.getBookStatus() == null);
    }

    private boolean filterAddBook(Book book) {
        return book.getBookId() != null || book.getBookName() == null || book.getBookCount() == null
                || book.getBookCategory() == null || book.getBookAuthor() == null || book.getBookStatus() != null;
    }

    private boolean filterDeleteBook(List<Book> list) {
        if (list.isEmpty()) {
            return true;
        }
        for (Book book : list) {
            if (book.getBookId() == null || book.getBookCount() != null || book.getBookName() != null
                    || book.getBookAuthor() != null || book.getBookStatus() != null
                    || book.getBookCategory() != null) {
                return true;
            }
        }
        return false;
    }

}
