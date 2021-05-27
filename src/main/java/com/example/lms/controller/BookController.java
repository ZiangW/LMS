package com.example.lms.controller;

import com.example.lms.model.Book;
import com.example.lms.service.BookManager;
import com.example.lms.vo.ResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 王子昂
 * @date 5/21/21
 * @description Book controller
 */
@RestController
@RequestMapping(value = "/books")
public class BookController {

    @Autowired
    private BookManager bookManager;

    @PostMapping(value = "/addBooks")
    @ResponseBody
    public ResponseVo<Book> addBooks(@RequestBody Book book) throws Exception {
        return ResponseVo.getSuccessResp(bookManager.addBooks(book));
    }

    @PostMapping(value = "/deleteBooks")
    @ResponseBody
    public ResponseVo<List<Integer>> deleteBooks(@RequestBody List<Book> list) throws Exception {
        return ResponseVo.getSuccessResp(bookManager.deleteBooks(list));
    }

    @PostMapping(value = "/updateBooks")
    @ResponseBody
    public ResponseVo<Book> updateBooks(@RequestBody Book book) throws Exception {
        return ResponseVo.getSuccessResp(bookManager.updateBooks(book));
    }

    @PostMapping(value = "/getBooks")
    @ResponseBody
    public ResponseVo<List<Book>> getBooks(@RequestBody Book book) throws Exception {
        return ResponseVo.getSuccessResp(bookManager.getBooks(book));
    }

    @GetMapping(value = "/getAllBooks")
    @ResponseBody
    public ResponseVo<List<Book>> getAllBooks() {
        return ResponseVo.getSuccessResp(bookManager.getAllBooks());
    }

}
