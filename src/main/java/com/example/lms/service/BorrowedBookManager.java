package com.example.lms.service;

import com.example.lms.model.BorrowedBook;

import java.util.List;

public interface BorrowedBookManager {

    List<BorrowedBook> getAllRecords();

    List<BorrowedBook> getRecords(BorrowedBook borrowedBook);

    int addRecord(BorrowedBook borrowedBook);

    int updateRecord(BorrowedBook borrowedBook);

    List<Integer> deleteRecords(List<BorrowedBook> list);

}
