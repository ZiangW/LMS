package com.example.lms.service;

import com.example.lms.model.ReturnedBook;
import com.example.lms.model.User;

import java.util.List;

public interface ReturnedBookManager {

    List<ReturnedBook> getAllRecords();

    List<ReturnedBook> getRecords(ReturnedBook returnedBook);

    int addRecord(ReturnedBook borrowedBook);

    int updateRecord(ReturnedBook borrowedBook);

    List<Integer> deleteRecords(List<ReturnedBook> list);

}
