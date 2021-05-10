package com.example.lms.service.impl;

import com.example.lms.dao.ReturnedBookDao;
import com.example.lms.model.ReturnedBook;
import com.example.lms.service.ReturnedBookManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.weekend.WeekendSqls;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ReturnedBookManagerImpl implements ReturnedBookManager {

    @Autowired
    private ReturnedBookDao returnedBookDao;

    @Override
    public List<ReturnedBook> getAllRecords() {
        return returnedBookDao.selectAll();
    }

    @Override
    public List<ReturnedBook> getRecords(ReturnedBook returnedBook) {
        return returnedBookDao.selectByExample(Example.builder(ReturnedBook.class)
                .where(selectWithConditions(returnedBook, true)).build());

    }



    @Override
    public int addRecord(ReturnedBook returnedBook) {
        List<ReturnedBook> list = returnedBookDao.selectByExample(Example.builder(ReturnedBook.class)
                .where(selectWithConditions(returnedBook, false)).build());
        if (list.size() > 0) {
            if (list.get(0).getReturnStatus() == 0) {
                returnedBook.setReturnId(list.get(0).getReturnId());
                return returnedBookDao.updateByPrimaryKey(returnedBook);
            }
            // 已存在
            return 2;
        }
        return returnedBookDao.insertSelective(returnedBook);
    }

    private WeekendSqls<ReturnedBook> selectWithConditions(ReturnedBook returnedBook, boolean selectOrUpdate) {
        WeekendSqls<ReturnedBook> sqls = WeekendSqls.custom();
        if (returnedBook.getReturnId() != null && returnedBook.getReturnId() > 0) {
            sqls.andEqualTo(ReturnedBook::getReturnId, returnedBook.getReturnId());
        }
        if (returnedBook.getReturnUserId() != null && returnedBook.getReturnUserId() > 0) {
            sqls.andEqualTo(ReturnedBook::getReturnUserId, returnedBook.getReturnUserId());
        }
        if (returnedBook.getReturnBookId() != null && returnedBook.getReturnBookId() > 0) {
            sqls.andEqualTo(ReturnedBook::getReturnBookId, returnedBook.getReturnBookId());
        }
        if (returnedBook.getReturnDate() != null) {
            sqls.andEqualTo(ReturnedBook::getReturnDate, returnedBook.getReturnDate());
        }
        if (selectOrUpdate) {
            sqls.andEqualTo(ReturnedBook::getReturnStatus, 1);
        }
        return sqls;
    }


    @Override
    public int updateRecord(ReturnedBook returnedBook) {
        return returnedBookDao.updateByPrimaryKeySelective(returnedBook);
    }

    @Override
    public List<Integer> deleteRecords(List<ReturnedBook> list) {
        List<Integer> res = new ArrayList<>();
        for (ReturnedBook returnedBook : list) {
            returnedBook.setReturnStatus(0);
            res.add(returnedBookDao.updateByPrimaryKeySelective(returnedBook));
        }
        return res;
    }

}
