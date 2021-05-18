package com.example.lms.service;

import com.example.lms.model.Book;

import java.util.List;

public interface BookManager {

    Book addBooks(Book book);

    List<Integer> deleteBooks(List<Book> list);

    Book updateBooks(Book book);

    List<Book> getBooks(Book book);

    List<Book> getAllBooks();

    int subBookCount(int borrowCount, int bookId);

    int addBookCount(int returnCount, int bookId);

}
