package com.example.lms.service;

import com.example.lms.model.Book;
import com.example.lms.model.BookCategory;

import java.util.List;

public interface BookCategoryManager {

    List<BookCategory> getBookCategory(BookCategory bookCategory);

    BookCategory addBookCategory(BookCategory bookCategory);

    BookCategory updateBookCategory(BookCategory bookCategory);

    List<Integer> deleteBookCategory(List<BookCategory> list);

    List<BookCategory> getAllBookCategories();

}
