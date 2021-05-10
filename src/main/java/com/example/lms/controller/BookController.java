package com.example.lms.controller;

import com.example.lms.model.Book;
import com.example.lms.service.BookManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/books")
public class BookController {

    @Autowired
    private BookManager bookManager;

    @PostMapping(value = "/addBooks")
    @ResponseBody
    public int addBooks(@RequestBody Book book) {

        return bookManager.addBooks(book);

    }

    @PostMapping(value = "/deleteBooks")
    @ResponseBody
    public List<Integer> deleteBooks(@RequestBody List<Book> list) {

        return bookManager.deleteBooks(list);

    }

    @PostMapping(value = "/updateBooks")
    @ResponseBody
    public int updateBooks(@RequestBody Book book) {

        return bookManager.updateBooks(book);

    }

    @PostMapping(value = "/getBooks")
    @ResponseBody
    public List<Book> getBooks(@RequestBody Book book) {

        return bookManager.getBooks(book);
    }

    @GetMapping(value = "/getAllBooks")
    @ResponseBody
    public List<Book> getAllBooks() {

        return bookManager.getAllBooks();

    }

}
