package com.example.lms.controller;

import com.example.lms.model.BorrowedBook;
import com.example.lms.service.BorrowedBookManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/Borrow")
public class BorrowController {
    @Autowired
    private BorrowedBookManager borrowedBookManager;

    @PostMapping(value = "/addRecord")
    @ResponseBody
    public int addRecord(@RequestBody BorrowedBook borrowedBook) {

        return borrowedBookManager.addRecord(borrowedBook);

    }

    @PostMapping(value = "/deleteRecords")
    @ResponseBody
    public List<Integer> deleteRecords(@RequestBody List<BorrowedBook> list) {

        return borrowedBookManager.deleteRecords(list);

    }

    @PostMapping(value = "/updateRecord")
    @ResponseBody
    public int updateRecord(@RequestBody BorrowedBook borrowedBook) {

        return borrowedBookManager.updateRecord(borrowedBook);

    }

    @PostMapping(value = "/getRecords")
    @ResponseBody
    public List<BorrowedBook> getRecords(@RequestBody BorrowedBook borrowedBook) {

        return borrowedBookManager.getRecords(borrowedBook);
    }

    @GetMapping(value = "/getAllRecords")
    @ResponseBody
    public List<BorrowedBook> getAllRecords() {

        return borrowedBookManager.getAllRecords();

    }
}
