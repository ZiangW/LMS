package com.example.lms.service.impl;

import com.example.lms.dao.BookDao;
import com.example.lms.model.Book;
import com.example.lms.model.BorrowedBook;
import com.example.lms.service.BookManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.weekend.WeekendSqls;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class BookManagerImpl implements BookManager {

    @Autowired
    private BookDao bookDao;

    @Override
    public int addBooks(Book book) {
        List<Book> list = bookDao.selectByExample(this.selectWithConditions(book, false));
        if (list.size() > 0) {
            if (list.get(0).getBookStatus() == 0) {
                book.setBookId(list.get(0).getBookId());
                return bookDao.updateByPrimaryKey(book);
            }
            // 已存在
            return 2;
        }
        return bookDao.insertSelective(book);
    }

    @Override
    public List<Integer> deleteBooks(List<Book> list) {
        List<Integer> res = new ArrayList<>();
        for (Book book : list) {
            book.setBookStatus(0);
            res.add(bookDao.updateByPrimaryKeySelective(book));
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
    public int updateBooks(Book book) {
        return bookDao.updateByExampleSelective(book, Example.builder(Book.class).where(updateWithConditions(book)).build());
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
        return bookDao.selectByExample(this.selectWithConditions(book, true));
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
