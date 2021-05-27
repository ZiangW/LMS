package com.example.lms.service;

import com.example.lms.model.ReturnedBook;
import com.example.lms.model.User;

import java.util.List;

/**
 * @author 王子昂
 * @date 5 /21/21
 * @description 归还记录Manager接口
 */
public interface ReturnedBookManager {

    /**
     * Gets all records.
     *
     * @return the all records
     */
    List<ReturnedBook> getAllRecords();

    /**
     * Gets records.
     *
     * @param returnedBook the returned book
     * @return the records
     */
    List<ReturnedBook> getRecords(ReturnedBook returnedBook);

    /**
     * Add record int.
     *
     * @param borrowedBook the borrowed book
     * @return the int
     */
    int addRecord(ReturnedBook borrowedBook);

    /**
     * Update record int.
     *
     * @param borrowedBook the borrowed book
     * @return the int
     */
    int updateRecord(ReturnedBook borrowedBook);

    /**
     * Delete records list.
     *
     * @param list the list
     * @return the list
     */
    int deleteRecord(ReturnedBook returnedBook);

}
