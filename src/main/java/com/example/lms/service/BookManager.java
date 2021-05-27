package com.example.lms.service;

import com.example.lms.model.Book;

import java.util.List;

/**
 * @author 王子昂
 * @date 5 /21/21
 * @description 图书Manager接口
 */
public interface BookManager {

    /**
     * Add books book.
     *
     * @param book the book
     * @return the book
     */
    Book addBooks(Book book);

    /**
     * Delete books list.
     *
     * @param list the list
     * @return the list
     */
    List<Integer> deleteBooks(List<Book> list);

    /**
     * Update books book.
     *
     * @param book the book
     * @return the book
     */
    Book updateBooks(Book book);

    /**
     * Gets books.
     *
     * @param book the book
     * @return the books
     */
    List<Book> getBooks(Book book);

    /**
     * Gets all books.
     *
     * @return the all books
     */
    List<Book> getAllBooks();

    /**
     * Sub book count int.
     *
     * @param borrowCount the borrow count
     * @param bookId      the book id
     * @return the int
     */
    int subBookCount(int borrowCount, int bookId);

    /**
     * Add book count int.
     *
     * @param returnCount the return count
     * @param bookId      the book id
     * @return the int
     */
    int addBookCount(int returnCount, int bookId);

}
