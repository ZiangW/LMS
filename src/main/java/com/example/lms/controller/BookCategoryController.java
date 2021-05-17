package com.example.lms.controller;

import com.example.lms.model.BookCategory;
import com.example.lms.service.BookCategoryManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/categories")
public class BookCategoryController {

    @Autowired
    private BookCategoryManager bookCategoryManager;

    @PostMapping(value = "/getBookCategory")
    @ResponseBody
    public List<BookCategory> getBookCategory(@RequestBody BookCategory bookCategory) {
        return bookCategoryManager.getBookCategory(bookCategory);
    }

    @PostMapping(value = "/addBookCategory")
    @ResponseBody
    public BookCategory addBookCategory(@RequestBody BookCategory bookCategory) {
        return bookCategoryManager.addBookCategory(bookCategory);
    }

    @PostMapping(value = "/deleteBookCategory")
    @ResponseBody
    public List<Integer> deleteBookCategory(@RequestBody List<BookCategory> list) {
        return bookCategoryManager.deleteBookCategory(list);
    }

    @PostMapping(value = "/updateBookCategory")
    @ResponseBody
    public BookCategory updateBookCategory(@RequestBody BookCategory bookCategory) {
        return bookCategoryManager.updateBookCategory(bookCategory);
    }

    @GetMapping(value = "/getALlBookCategories")
    @ResponseBody
    public List<BookCategory> getAllBookCategories() {
        return bookCategoryManager.getAllBookCategories();
    }

}
