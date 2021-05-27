package com.example.lms.controller;

import com.example.lms.model.ReturnedBook;
import com.example.lms.service.ReturnedBookManager;
import com.example.lms.vo.ResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 王子昂
 * @date 5/21/21
 * @description Return controller
 */
@RestController
@RequestMapping(value = "/Return")
public class ReturnController {
    @Autowired
    private ReturnedBookManager returnedBookManager;

    @PostMapping(value = "/addRecord")
    @ResponseBody
    public ResponseVo<Integer> addRecord(@RequestBody ReturnedBook returnedBook) throws Exception{
        return ResponseVo.getSuccessResp(returnedBookManager.addRecord(returnedBook));
    }

    @PostMapping(value = "/deleteRecords")
    @ResponseBody
    public ResponseVo<Integer> deleteRecords(@RequestBody ReturnedBook returnedBook) throws Exception{
        return ResponseVo.getSuccessResp(returnedBookManager.deleteRecord(returnedBook));
    }

    @PostMapping(value = "/updateRecord")
    @ResponseBody
    public ResponseVo<Integer> updateRecord(@RequestBody ReturnedBook returnedBook) throws Exception {
        return ResponseVo.getSuccessResp(returnedBookManager.updateRecord(returnedBook));
    }

    @PostMapping(value = "/getRecords")
    @ResponseBody
    public ResponseVo<List<ReturnedBook>> getRecords(@RequestBody ReturnedBook returnedBook) throws Exception {
        return ResponseVo.getSuccessResp(returnedBookManager.getRecords(returnedBook));
    }

    @GetMapping(value = "/getAllRecords")
    @ResponseBody
    public ResponseVo<List<ReturnedBook>> getAllRecords() {
        return ResponseVo.getSuccessResp(returnedBookManager.getAllRecords());
    }

}
