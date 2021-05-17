package com.example.lms.service;

import com.example.lms.model.Book;
import com.example.lms.model.BookCategory;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.AttributeOverride;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
public class BookCategoryManagerTest {
    @Autowired
    private BookCategoryManager bookCategoryManager;

    @Test
    public void getBookCategoryWithValidInfo() {
        BookCategory bookCategory = new BookCategory();
        bookCategory.setCategoryId(1);

        List<BookCategory> res = bookCategoryManager.getBookCategory(bookCategory);

        for (BookCategory bc : res) {
            Assertions.assertNotNull(bc.getCategoryName(), "名字为空");
        }
    }

    @Test
    public void getBookCategoryWithInValidInfo() {
        BookCategory bookCategory = new BookCategory();
        bookCategory.setCategoryId(100);

        List<BookCategory> res = bookCategoryManager.getBookCategory(bookCategory);

        for (BookCategory bc : res) {
            Assertions.assertNull(bc.getCategoryName(), "名字不为空");
        }
    }

    @Test
    public void getAllBookCategories() {
        List<BookCategory> res = bookCategoryManager.getAllBookCategories();
        for (BookCategory bc : res) {
            Assertions.assertNotNull(bc.getCategoryId(), "id为空");
        }
    }

    @Test
    public void addBookCategoryWithExistedInfo() {
        BookCategory bookCategory = new BookCategory();
        bookCategory.setCategoryName("化学");
        bookCategory.setCategoryStatus(1);

        BookCategory res = bookCategoryManager.addBookCategory(bookCategory);

        Assertions.assertNull(res.getCategoryId(), "id存在");
        Assertions.assertNull(res.getCategoryName(), "名字存在");
        Assertions.assertNull(res.getCategoryStatus(), "状态存在");
    }

    @Test
    public void addBookCategoryWithNewInfo() {
        BookCategory bookCategory = new BookCategory();
        bookCategory.setCategoryName("aaaa");
        bookCategory.setCategoryStatus(1);

        BookCategory res = bookCategoryManager.addBookCategory(bookCategory);

        Assertions.assertNotNull(res.getCategoryId(), "id存在");
        Assertions.assertEquals(bookCategory.getCategoryName(), res.getCategoryName(), "名字不匹配");
        Assertions.assertEquals(bookCategory.getCategoryStatus(), res.getCategoryStatus(), "状态不匹配");
    }

    @Test
    public void addBookCategoryWithNullInfo() {
        BookCategory bookCategory = new BookCategory();

        BookCategory res = bookCategoryManager.addBookCategory(bookCategory);

        Assertions.assertNull(res.getCategoryId(), "id存在");
        Assertions.assertNull(res.getCategoryName(), "名字存在");
        Assertions.assertNull(res.getCategoryStatus(), "状态存在");
    }

    @Test
    public void updateBookCategoryWithValidInfo() {
        BookCategory bookCategory = new BookCategory();
        bookCategory.setCategoryId(5);
        bookCategory.setCategoryStatus(1);

        BookCategory res = bookCategoryManager.updateBookCategory(bookCategory);

        Assertions.assertEquals(bookCategory.getCategoryId(), res.getCategoryId(), "id不匹配");
        Assertions.assertNotNull(res.getCategoryName(), "名字不存在");
        Assertions.assertEquals(bookCategory.getCategoryStatus(), res.getCategoryStatus(), "状态不匹配");
    }

    @Test
    public void updateBookCategoryWithInValidInfo() {
        BookCategory bookCategory = new BookCategory();
        bookCategory.setCategoryId(7);

        BookCategory res = bookCategoryManager.updateBookCategory(bookCategory);

        Assertions.assertNull(res.getCategoryId(), "id存在");
        Assertions.assertNull(res.getCategoryName(), "名字存在");
        Assertions.assertNull(res.getCategoryStatus(), "状态存在");
    }

    @Test
    public void deleteBookCategoryWithValidInfo() {
        List<BookCategory> list = new ArrayList<>();
        for (int i = 1; i < 4; i++) {
            BookCategory bookCategory = new BookCategory();
            bookCategory.setCategoryId(i);
            list.add(bookCategory);
        }

        List<Integer> res = bookCategoryManager.deleteBookCategory(list);

        for (int code : res) {
            Assertions.assertEquals(code, 1, "返回码不为空");
        }
    }

    @Test
    public void deleteBookCategoryWithinValidInfo() {
        List<BookCategory> list = new ArrayList<>();
        for (int i = 1; i < 3; i++) {
            BookCategory bookCategory = new BookCategory();
            bookCategory.setCategoryId(i);
            list.add(bookCategory);
        }
        BookCategory bookCategory = new BookCategory();
        bookCategory.setCategoryName("abc");
        list.add(bookCategory);

        List<Integer> res = bookCategoryManager.deleteBookCategory(list);

        Assertions.assertEquals(res.get(2), 0, "返回码错误");
    }
}
