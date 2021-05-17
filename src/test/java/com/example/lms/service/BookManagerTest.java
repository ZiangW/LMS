package com.example.lms.service;


import com.example.lms.model.Book;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
public class BookManagerTest {

    @Autowired
    private BookManager bookManager;

    @Test
    public void addBooksWithExistedInfo() {
        Book book = new Book();
        book.setBookName("平凡的世界");
        book.setBookAuthor("路遥");
        book.setBookCategory(3);
        book.setBookCount(2);
        book.setBookStatus(1);

        Book res = bookManager.addBooks(book);

        Assertions.assertNull(res.getBookId(), "id存在");
        Assertions.assertNull(res.getBookName(), "名字存在");
        Assertions.assertNull(res.getBookAuthor(), "作者存在");
        Assertions.assertNull(res.getBookCategory(), "分类存在");
        Assertions.assertNull(res.getBookCount(), "数量存在");
        Assertions.assertNull(res.getBookStatus(), "状态存在");
    }

    @Test
    public void addBooksWithNewInfo() {
        Book book = new Book();
        book.setBookName("时间简史");
        book.setBookAuthor("霍金");
        book.setBookCategory(4);
        book.setBookCount(14);
        book.setBookStatus(1);

        Book res = bookManager.addBooks(book);

        Assertions.assertNotNull(res.getBookId(), "id不存在");
        Assertions.assertEquals(book.getBookName(), res.getBookName(), "名字不匹配");
        Assertions.assertEquals(book.getBookAuthor(), res.getBookAuthor(), "作者不匹配");
        Assertions.assertEquals(book.getBookCategory(), res.getBookCategory(), "分类不匹配");
        Assertions.assertEquals(book.getBookCount(), res.getBookCount(), "数量不匹配");
        Assertions.assertEquals(book.getBookStatus(), res.getBookStatus(), "状态不匹配");
    }

    @Test
    public void addBooksWithNullInfo() {
        Book book = new Book();

        Book res = bookManager.addBooks(book);

        Assertions.assertNull(res.getBookId(), "id存在");
        Assertions.assertNull(res.getBookName(), "名字存在");
        Assertions.assertNull(res.getBookAuthor(), "作者存在");
        Assertions.assertNull(res.getBookCategory(), "分类存在");
        Assertions.assertNull(res.getBookCount(), "数量存在");
        Assertions.assertNull(res.getBookStatus(), "状态存在");
    }

    @Test
    public void deleteBooksWithValidInfo() {
        List<Book> list = new ArrayList<>();
        for (int i = 1; i < 4; i++) {
            Book book = new Book();
            book.setBookId(i);
            list.add(book);
        }

        List<Integer> res = bookManager.deleteBooks(list);

        for (int code : res) {
            Assertions.assertEquals(code, 1, "返回码不为空");
        }
    }

    @Test
    public void deleteBooksWithSomeInvalidInfo() {
        List<Book> list = new ArrayList<>();
        for (int i = 1; i < 3; i++) {
            Book book = new Book();
            book.setBookId(i);
            list.add(book);
        }
        Book book = new Book();
        book.setBookName("asdf");
        list.add(book);

        List<Integer> res = bookManager.deleteBooks(list);

        Assertions.assertEquals(res.get(2), 0, "返回码错误");
    }

    @Test
    public void subBookCountWithValidInfo() {
        Assertions.assertEquals(bookManager
                .subBookCount(1, 3), 1, "返回码错误");
    }

    @Test
    public void subBookCountWithInValidInfo() {
        Assertions.assertEquals(bookManager
                .subBookCount(100, 1), 0, "返回码错误");
    }

    @Test
    public void addBookCountWithValidInfo() {
        Assertions.assertEquals(bookManager
                .addBookCount(1, 1), 1, "返回码错误");
    }

    @Test
    public void addBookCountWithInValidInfo() {
        Assertions.assertEquals(bookManager
                .addBookCount(1, 100), 0, "返回码错误");
    }

    @Test
    public void updateBooksWithValidInfo() {
        Book book = new Book();
        book.setBookId(1);
        book.setBookAuthor("aaa");
        book.setBookCategory(1);
        book.setBookCount(12);
        book.setBookStatus(1);

        Book res = bookManager.updateBooks(book);

        Assertions.assertEquals(book.getBookId(), res.getBookId(), "id不匹配");
        Assertions.assertNotNull(res.getBookName(), "名字不存在");
        Assertions.assertEquals(book.getBookAuthor(), res.getBookAuthor(), "作者不匹配");
        Assertions.assertEquals(book.getBookCategory(), res.getBookCategory(), "分类不匹配");
        Assertions.assertEquals(book.getBookCount(), res.getBookCount(), "数量不匹配");
        Assertions.assertEquals(book.getBookStatus(), res.getBookStatus(), "状态不匹配");

    }

    @Test
    public void updateBooksWithInValidInfo() {
        Book book = new Book();
        book.setBookId(112);
        Assertions.assertNotNull(book.getBookId(), "id不存在");
        Assertions.assertNull(book.getBookName(), "名字存在");
        Assertions.assertNull(book.getBookAuthor(), "作者存在");
        Assertions.assertNull(book.getBookCategory(), "分类存在");
        Assertions.assertNull(book.getBookCount(), "数量存在");
        Assertions.assertNull(book.getBookStatus(), "状态存在");

        Book res = bookManager.updateBooks(book);

        Assertions.assertNull(res.getBookId(), "id存在");
        Assertions.assertNull(res.getBookName(), "名字存在");
        Assertions.assertNull(res.getBookAuthor(), "作者存在");
        Assertions.assertNull(res.getBookCategory(), "分类存在");
        Assertions.assertNull(res.getBookCount(), "数量存在");
        Assertions.assertNull(res.getBookStatus(), "状态存在");
    }

    @Test
    public void getBooksWithValidInfo() {
        Book book = new Book();
        book.setBookId(1);

        List<Book> res = bookManager.getBooks(book);

        for (Book b : res) {
            Assertions.assertNotNull(b.getBookName(), "名字为空");
        }
    }

    @Test
    public void getBooksWithInValidInfo() {
        Book book = new Book();
        book.setBookId(1999);

        List<Book> res = bookManager.getBooks(book);

        for (Book b : res) {
            Assertions.assertNull(b.getBookName(), "名字不为空");
        }
    }

    @Test
    public void getAllBooks() {
        List<Book> res = bookManager.getAllBooks();

        for (Book b : res) {
            Assertions.assertNotNull(b.getBookId(), "id为空");
        }
    }
}
