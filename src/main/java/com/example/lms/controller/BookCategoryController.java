package com.example.lms.controller;

import com.example.lms.model.BookCategory;
import com.example.lms.service.BookCategoryManager;
import com.example.lms.vo.ResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 王子昂
 * @date 5/21/21
 * @description BookCategory controller
 */
@RestController
@RequestMapping(value = "/categories")
public class BookCategoryController {

    @Autowired
    private BookCategoryManager bookCategoryManager;

    @PostMapping(value = "/getBookCategory")
    @ResponseBody
    public ResponseVo<List<BookCategory>> getBookCategory(@RequestBody BookCategory bookCategory) throws Exception {
        return ResponseVo.getSuccessResp(bookCategoryManager.getBookCategory(bookCategory));
    }

    @PostMapping(value = "/addBookCategory")
    @ResponseBody
    public ResponseVo<BookCategory> addBookCategory(@RequestBody BookCategory bookCategory) throws Exception {
        return ResponseVo.getSuccessResp(bookCategoryManager.addBookCategory(bookCategory));
    }

    @PostMapping(value = "/deleteBookCategory")
    @ResponseBody
    public ResponseVo<List<Integer>> deleteBookCategory(@RequestBody List<BookCategory> list) throws Exception {
        return ResponseVo.getSuccessResp(bookCategoryManager.deleteBookCategory(list));
    }

    @PostMapping(value = "/updateBookCategory")
    @ResponseBody
    public ResponseVo<BookCategory> updateBookCategory(@RequestBody BookCategory bookCategory) throws Exception {
        return ResponseVo.getSuccessResp(bookCategoryManager.updateBookCategory(bookCategory));
    }

    @GetMapping(value = "/getALlBookCategories")
    @ResponseBody
    public ResponseVo<List<BookCategory>> getAllBookCategories() throws Exception {
        return ResponseVo.getSuccessResp(bookCategoryManager.getAllBookCategories());
    }

}
