package com.example.lms.service;

import com.example.lms.model.BookCategory;

import java.util.List;

public interface BookCategoryManager {

    List<BookCategory> getBookCategory(BookCategory bookCategory);

    int addBookCategory(BookCategory bookCategory);

    int updateBookCategory(BookCategory bookCategory);

    List<Integer> deleteBookCategory(List<BookCategory> list);

    List<BookCategory> getAllBookCategories();

}
