package com.example.lms.service;

import com.example.lms.model.BorrowedBook;

import java.util.List;

/**
 * @author 王子昂
 * @date 5 /21/21
 * @description 借书记录Manager接口
 */
public interface BorrowedBookManager {

    /**
     * Gets all records.
     *
     * @return the all records
     */
    List<BorrowedBook> getAllRecords();

    /**
     * Gets records.
     *
     * @param borrowedBook the borrowed book
     * @return the records
     */
    List<BorrowedBook> getRecords(BorrowedBook borrowedBook);

    /**
     * Add record int.
     *
     * @param borrowedBook the borrowed book
     * @return the int
     */
    int addRecord(BorrowedBook borrowedBook);

    /**
     * Update record int.
     *
     * @param borrowedBook the borrowed book
     * @return the int
     */
    int updateRecord(BorrowedBook borrowedBook);

    /**
     * Delete records list.
     *
     * @param borrowedBook
     * @return the list
     */
    int deleteRecord(BorrowedBook borrowedBook);

}
