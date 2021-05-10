package com.example.lms.controller;

import com.example.lms.model.ReturnedBook;
import com.example.lms.service.ReturnedBookManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/Return")
public class ReturnController {
    @Autowired
    private ReturnedBookManager returnedBookManager;

    @PostMapping(value = "/addRecord")
    @ResponseBody
    public int addRecord(@RequestBody ReturnedBook returnedBook) {

        return returnedBookManager.addRecord(returnedBook);

    }

    @PostMapping(value = "/deleteRecords")
    @ResponseBody
    public List<Integer> deleteRecords(@RequestBody List<ReturnedBook> list) {

        return returnedBookManager.deleteRecords(list);

    }

    @PostMapping(value = "/updateRecord")
    @ResponseBody
    public int updateRecord(@RequestBody ReturnedBook returnedBook) {

        return returnedBookManager.updateRecord(returnedBook);

    }

    @PostMapping(value = "/getRecords")
    @ResponseBody
    public List<ReturnedBook> getRecords(@RequestBody ReturnedBook returnedBook) {

        return returnedBookManager.getRecords(returnedBook);
    }

    @GetMapping(value = "/getAllRecords")
    @ResponseBody
    public List<ReturnedBook> getAllRecords() {

        return returnedBookManager.getAllRecords();

    }
}
