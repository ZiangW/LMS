package com.example.lms.service.impl;

import com.example.lms.dao.BookDao;
import com.example.lms.model.Book;
import com.example.lms.service.BookManager;
import com.example.lms.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.weekend.WeekendSqls;

import java.util.ArrayList;
import java.util.List;

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
        List<Book> list = bookDao.selectByExample(this.selectWithConditions(book, false));
        Book res = new Book();
        if (list.size() > 0) {
            if (list.get(0).getBookStatus() == 0) {
                book.setBookId(list.get(0).getBookId());
                if (bookDao.updateByPrimaryKey(book)== 1) {
                    res = book;
                }
            }
        } else if (bookDao.insertSelective(book) == 1) {
            list = bookDao.selectByExample(this.selectWithConditions(book, false));
            if (list.size() == 1) {
                res = list.get(0);
            }
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
        if (bookDao.updateByExampleSelective(book, Example.builder(Book.class)
                .where(updateWithConditions(book)).build()) == 1) {
            List<Book> list = bookDao.selectByExample(this.selectWithConditions(book, false));
            if (list.size() == 1) {
                res = list.get(0);
                // CachePut
                redisService.updateObject("bookId::" + res.getBookId(), res);
            }
        }
        return res;
    }

    private WeekendSqls<Book> updateWithConditions(Book book) {
        WeekendSqls<Book> sqls = WeekendSqls.custom();
        if (book.getBookId() != null && book.getBookId() > 0) {
            sqls.andEqualTo(Book::getBookId, book.getBookId());

        }
        if (book.getBookName() != null && book.getBookName().length() > 0) {
            sqls.andEqualTo(Book::getBookName, book.getBookName());
        }
        sqls.andEqualTo(Book::getBookStatus, 1);
        return sqls;
    }

    @Override
    public List<Book> getBooks(Book book) {
        // Cacheable
        String key = "book::set::" + "bookId::" + book.getBookId()
                + "bookName::" + book.getBookName();
        List<Book> list = redisService.selectObjects(key);
        if (list.size() < 1) {
            list = bookDao.selectByExample(this.selectWithConditions(book, true));
            List<String> keys = new ArrayList<>();
            for (Book b : list) {
                keys.add("bookId::" + b.getBookId());
            }
            redisService.addObjects(key, keys, list);
        }
        return list;
    }

    private Example selectWithConditions(Book book, boolean selectOrUpdate) {
        Example example = new Example(Book.class);
        Example.Criteria criteria = example.createCriteria();
        if (book.getBookId() != null && book.getBookId() > 0) {
            criteria.andEqualTo("bookId", book.getBookId());
        }
        if (book.getBookName() != null && book.getBookName().length() > 0) {
            criteria.andEqualTo("bookName", book.getBookName());
        }
        if (book.getBookAuthor() != null && book.getBookName().length() > 0) {
            criteria.andEqualTo("bookAuthor", book.getBookAuthor());
        }
        if (book.getBookCategory() != null && book.getBookCategory() > 0) {
            criteria.andEqualTo("bookCategory", book.getBookCategory());
        }
        if (selectOrUpdate) {
            criteria.andEqualTo("bookStatus", 1);
        }
        return example;
    }

    @Override
    public List<Book> getAllBooks() {
        return bookDao.selectAll();
    }

}
