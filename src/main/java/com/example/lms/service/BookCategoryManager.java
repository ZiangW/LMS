package com.example.lms.service;

import com.example.lms.model.BookCategory;

import java.util.List;

/**
 * @author 王子昂
 * @date 5 /21/21
 * @description 图书分类Manager接口
 */
public interface BookCategoryManager {

    /**
     * Gets book category.
     *
     * @param bookCategory the book category
     * @return the book category
     */
    List<BookCategory> getBookCategory(BookCategory bookCategory);

    /**
     * Add book category book category.
     *
     * @param bookCategory the book category
     * @return the book category
     */
    BookCategory addBookCategory(BookCategory bookCategory);

    /**
     * Update book category book category.
     *
     * @param bookCategory the book category
     * @return the book category
     */
    BookCategory updateBookCategory(BookCategory bookCategory);

    /**
     * Delete book category list.
     *
     * @param list the list
     * @return the list
     */
    List<Integer> deleteBookCategory(List<BookCategory> list);

    /**
     * Gets all book categories.
     *
     * @return the all book categories
     */
    List<BookCategory> getAllBookCategories();

}
