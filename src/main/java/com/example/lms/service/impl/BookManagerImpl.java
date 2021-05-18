package com.example.lms.service.impl;

import com.example.lms.dao.BookDao;
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

@Slf4j
@Service
@Transactional
@CacheConfig(cacheNames = "books")
public class BookManagerImpl implements BookManager {

    @Autowired
    private BookDao bookDao;
    @Autowired
    private RedisService<Book> redisService;

    @Override
    public Book addBooks(Book book) {
        Book res = new Book();
        // 过滤非法book
        if (!this.filterAddBook(book)) {
            log.info("图书信息不完整");
            return res;
        }
        List<Book> list = bookDao.selectByExample(Example.builder(Book.class)
                .where(this.selectWithCoreConditions(book, false)).build());
        if (list.size() > 0) {
            if (list.get(0).getBookStatus() == 0) {
                log.info("存在失效图书");
                book.setBookId(list.get(0).getBookId());
                if (bookDao.updateByPrimaryKey(book) == 1) {
                    res = book;
                } else {
                    log.info("更新图书信息失败");
                }
            } else {
                log.info("图书已存在");
            }
        } else if (bookDao.insertSelective(book) == 1) {
            log.info("不存在失效图书");
            list = bookDao.selectByExample(Example.builder(Book.class)
                    .where(selectWithConditions(book)).build());
            if (list.size() == 1) {
                res = list.get(0);
            }
        } else {
            log.info("不存在失效图书");
            log.info("添加图书失败");
        }
        // CachePut
        if (res.getBookId() != null) {
            redisService.addObject("book::set::*", "bookId::" + res.getBookId(), res);
        }
        return res;
    }

    @Override
    public List<Integer> deleteBooks(List<Book> list) {
        List<Integer> res = new ArrayList<>();
        for (Book book : list) {
            book.setBookStatus(0);
            int flag = bookDao.updateByPrimaryKeySelective(book);
            res.add(flag);
            // CacheEvict
            if (flag == 1) {
                redisService.deleteObject("bookId::" + book.getBookId());
            }
        }
        return res;
    }

    @Override
    public int subBookCount(int borrowCount, int bookId) {
        return bookDao.subBookCount(borrowCount, bookId);
    }

    @Override
    public int addBookCount(int returnCount, int bookId) {
        return bookDao.addBookCount(returnCount, bookId);
    }

    @Override
    public Book updateBooks(Book book) {
        Book res = new Book();
        // 过滤非法book
        if (this.filterUpdateBook(book)) {
            log.info("非法图书信息");
            return res;
        }
        if (bookDao.updateByExampleSelective(book, Example.builder(Book.class)
                .where(this.updateWithConditions(book)).build()) == 1) {
            List<Book> list = bookDao.selectByExample(Example.builder(Book.class)
                    .where(this.selectWithConditions(book)).build());
            if (list.size() == 1) {
                res = list.get(0);
                // CachePut
                redisService.updateObject("bookId::" + res.getBookId(), res);
            } else {
                log.info("存在重复图书信息");
            }
        } else {
            log.info("图书更新失败");
        }
        return res;
    }

    @Override
    public List<Book> getBooks(Book book) {
        // Cacheable
        String key = "book::set::" + "bookId::" + book.getBookId()
                + "bookName::" + book.getBookName();
        List<Book> list = redisService.selectObjects(key);
        if (list.size() < 1) {
            log.info("缓存不存在");
            list = bookDao.selectByExample(Example.builder(Book.class)
                    .where(this.selectWithConditions(book)).build());
            List<String> keys = new ArrayList<>();
            for (Book b : list) {
                keys.add("bookId::" + b.getBookId());
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
        if (book.getBookId() != null && book.getBookId() > 0) {
            conditions.andEqualTo(Book::getBookId, book.getBookId());
        }
        if (book.getBookName() != null && book.getBookName().length() > 0) {
            conditions.andEqualTo(Book::getBookName, book.getBookName());
        }
        if (selectOrUpdate) {
            conditions.andEqualTo(Book::getBookStatus, 1);
        }
        return conditions;
    }

    private WeekendSqls<Book> selectWithConditions(Book book) {
        WeekendSqls<Book> conditions = this.selectWithCoreConditions(book, true);
        if (book.getBookAuthor() != null && book.getBookAuthor().length() > 0) {
            conditions.andEqualTo(Book::getBookAuthor, book.getBookAuthor());
        }
        if (book.getBookCategory() != null && book.getBookCategory() > 0) {
            conditions.andEqualTo(Book::getBookCategory, book.getBookCategory());
        }
        return conditions;
    }

    private boolean filterUpdateBook(Book book) {
        if (book.getBookId() == null) {
            log.info("图书id为空");
            return true;
        }
        if (book.getBookName() != null) {
            log.info("图书名不为空");
            return true;
        }
        return book.getBookCount() == null && book.getBookCategory() == null
                && book.getBookAuthor() == null && book.getBookStatus() == null;
    }

    private boolean filterAddBook(Book book) {
        if (book.getBookId() != null) {
            log.info("图书id不为空");
            return false;
        }
        return book.getBookName() != null && book.getBookCount() != null
                && book.getBookCategory() != null && book.getBookAuthor() != null
                && book.getBookStatus() != null;
    }

}
