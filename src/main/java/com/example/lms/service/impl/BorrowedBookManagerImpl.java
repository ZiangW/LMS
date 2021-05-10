package com.example.lms.service.impl;

import com.example.lms.dao.BorrowedBookDao;
import com.example.lms.model.BorrowedBook;
import com.example.lms.service.BorrowedBookManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.weekend.WeekendSqls;

import java.util.ArrayList;
import java.util.List;


@Service
@Transactional
public class BorrowedBookManagerImpl implements BorrowedBookManager {

    @Autowired
    private BorrowedBookDao borrowedBookDao;

    @Override
    public List<BorrowedBook> getAllRecords() {
        return borrowedBookDao.selectAll();
    }

    @Override
    public List<BorrowedBook> getRecords(BorrowedBook borrowedBook) {
        return borrowedBookDao.selectByExample(Example.builder(BorrowedBook.class)
                .where(selectWithConditions(borrowedBook, true)).build());
    }

    @Override
    public int addRecord(BorrowedBook borrowedBook) {
        List<BorrowedBook> list = borrowedBookDao.selectByExample(Example.builder(BorrowedBook.class)
                .where(selectWithConditions(borrowedBook, false)).build());
        if (list.size() > 0) {
            if (list.get(0).getLendStatus() == 0) {
                borrowedBook.setLendId(list.get(0).getLendId());
                return borrowedBookDao.updateByPrimaryKey(borrowedBook);
            }
            // 已存在
            return 2;
        }
        return borrowedBookDao.insertSelective(borrowedBook);
    }

    private WeekendSqls<BorrowedBook> selectWithConditions(BorrowedBook borrowedBook, boolean selectOrUpdate) {
        WeekendSqls<BorrowedBook> sqls = WeekendSqls.custom();
        if (borrowedBook.getLendId() != null && borrowedBook.getLendId() > 0) {
            sqls.andEqualTo(BorrowedBook::getLendId, borrowedBook.getLendId());
        }
        if (borrowedBook.getLendUserId() != null && borrowedBook.getLendUserId() > 0) {
            sqls.andEqualTo(BorrowedBook::getLendUserId, borrowedBook.getLendUserId());
        }
        if (borrowedBook.getLendBookId() != null && borrowedBook.getLendBookId() > 0) {
            sqls.andEqualTo(BorrowedBook::getLendBookId, borrowedBook.getLendBookId());
        }
        if (borrowedBook.getLendDate() != null) {
            sqls.andEqualTo(BorrowedBook::getLendDate, borrowedBook.getLendDate());
        }
        if (selectOrUpdate) {
            sqls.andEqualTo(BorrowedBook::getLendStatus, 1);
        }
        return sqls;
    }

    @Override
    public int updateRecord(BorrowedBook borrowedBook) {
        return borrowedBookDao.updateByPrimaryKeySelective(borrowedBook);
    }

    @Override
    public List<Integer> deleteRecords(List<BorrowedBook> list) {
        List<Integer> res = new ArrayList<>();
        for (BorrowedBook borrowedBook : list) {
            borrowedBook.setLendStatus(0);
            res.add(borrowedBookDao.updateByPrimaryKeySelective(borrowedBook));
        }
        return res;
    }

}
