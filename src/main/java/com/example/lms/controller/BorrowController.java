package com.example.lms.controller;

import com.example.lms.model.BorrowedBook;
import com.example.lms.service.BorrowedBookManager;
import com.example.lms.vo.ResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 王子昂
 * @date 5/21/21
 * @description Borrow controller
 */
@RestController
@RequestMapping(value = "/Borrow")
public class BorrowController {
    @Autowired
    private BorrowedBookManager borrowedBookManager;

    @PostMapping(value = "/addRecord")
    @ResponseBody
    public ResponseVo<Integer> addRecord(@RequestBody BorrowedBook borrowedBook) throws Exception {
        return ResponseVo.getSuccessResp(borrowedBookManager.addRecord(borrowedBook));
    }

    @PostMapping(value = "/deleteRecords")
    @ResponseBody
    public ResponseVo<Integer> deleteRecords(@RequestBody BorrowedBook borrowedBook) {
        return ResponseVo.getSuccessResp(borrowedBookManager.deleteRecord(borrowedBook));
    }

    @PostMapping(value = "/updateRecord")
    @ResponseBody
    public ResponseVo<Integer> updateRecord(@RequestBody BorrowedBook borrowedBook) throws Exception {
        return ResponseVo.getSuccessResp(borrowedBookManager.updateRecord(borrowedBook));
    }

    @PostMapping(value = "/getRecords")
    @ResponseBody
    public ResponseVo<List<BorrowedBook>> getRecords(@RequestBody BorrowedBook borrowedBook) throws Exception {
        return ResponseVo.getSuccessResp(borrowedBookManager.getRecords(borrowedBook));
    }

    @GetMapping(value = "/getAllRecords")
    @ResponseBody
    public ResponseVo<List<BorrowedBook>> getAllRecords() {
        return ResponseVo.getSuccessResp(borrowedBookManager.getAllRecords());
    }
}
