package com.example.lms.service.impl;

import com.example.lms.dao.BorrowedBookDao;
import com.example.lms.model.Book;
import com.example.lms.model.BorrowedBook;
import com.example.lms.model.ReturnedBook;
import com.example.lms.service.BorrowedBookManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.weekend.WeekendSqls;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Transactional
@CacheConfig(cacheNames = "borrowedBooks")
public class BorrowedBookManagerImpl implements BorrowedBookManager {

    @Autowired
    private BorrowedBookDao borrowedBookDao;
    @Autowired
    private RedisServiceImpl<BorrowedBook> redisService;

    @Override
    public int addRecord(BorrowedBook borrowedBook) {
        BorrowedBook res = new BorrowedBook();

        // 过滤非法记录
        if (!this.filterAddRecord(borrowedBook)) {
            log.info("记录不完整");
            return 0;
        }
        List<BorrowedBook> list = borrowedBookDao.selectByExample(Example.builder(BorrowedBook.class)
                .where(selectWithConditions(borrowedBook, false)).build());
        if (list.size() > 0) {
            if (list.get(0).getLendStatus() == 0) {
                log.info("存在失效记录");
                if (borrowedBookDao.updateByPrimaryKey(borrowedBook) == 1) {
                    res = borrowedBook;
                } else {
                    log.info("更新记录失败");
                }
            } else {
                log.info("记录已存在");
            }
        } else if (borrowedBookDao.insertSelective(borrowedBook) == 1) {
            log.info("不存在失效记录");
            list = borrowedBookDao.selectByExample(Example.builder(BorrowedBook.class)
                    .where(selectWithConditions(borrowedBook, true)).build());
            if (list.size() == 1) {
                res = list.get(0);
            }
        } else {
            log.info("不存在失效记录");
            log.info("添加记录失败");
        }
        // CachePut
        if (res.getLendId() != null) {
            redisService.addObject("borrowedBook::set::*", "lendId::" + res.getLendId(), res);
            return 1;
        }
        return 0;
    }

    @Override
    public List<Integer> deleteRecords(List<BorrowedBook> list) {
        List<Integer> res = new ArrayList<>();
        for (BorrowedBook borrowedBook : list) {
            borrowedBook.setLendStatus(0);
            int flag = borrowedBookDao.updateByPrimaryKeySelective(borrowedBook);
            res.add(flag);
            if (flag == 1) {
                redisService.deleteObject("lendId::" + borrowedBook.getLendId());
            }
        }
        return res;
    }

    @Override
    public int updateRecord(BorrowedBook borrowedBook) {
        if (this.filterUpdateRecord(borrowedBook)) {
            log.info("非法记录");
            return 0;
        }
        if (borrowedBookDao.updateByPrimaryKeySelective(borrowedBook) == 1) {
            List<BorrowedBook> list = borrowedBookDao.selectByExample(Example.builder(BorrowedBook.class)
                    .where(this.selectWithConditions(borrowedBook, true)).build());
            if (list.size() == 1) {
                BorrowedBook res = list.get(0);
                // CachePut
                redisService.updateObject("lendId::" + res.getLendId(), res);
                return 1;
            } else {
                log.info("存在重复记录");
            }
        } else {
            log.info("记录更新失败");
        }
        return 0;
    }

    @Override
    public List<BorrowedBook> getRecords(BorrowedBook borrowedBook) {
        // Cacheable
        String key = "borrowedBook::set::" + "lendId::" + borrowedBook.getLendId()
                + "lendUserId::" + borrowedBook.getLendUserId() + "lendBookId::" + borrowedBook.getLendBookId();
        List<BorrowedBook> list = redisService.selectObjects(key);
        if (list.size() < 1) {
            log.info("缓存不存在");
            list = borrowedBookDao.selectByExample(Example.builder(BorrowedBook.class)
                    .where(this.selectWithConditions(borrowedBook, true)).build());
            List<String> keys = new ArrayList<>();
            for (BorrowedBook b : list) {
                keys.add("lendId::" + b.getLendId());
            }
            redisService.addObjects(key, keys, list);
        }
        return list;
    }

    @Override
    public List<BorrowedBook> getAllRecords() {
        return borrowedBookDao.selectAll();
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

    private boolean filterUpdateRecord(BorrowedBook borrowedBook) {
        if (borrowedBook.getLendId() == null) {
            log.info("记录id为空");
            return true;
        }
        return borrowedBook.getLendBookId() == null && borrowedBook.getLendUserId() == null
                && borrowedBook.getLendDate() == null && borrowedBook.getLendStatus() == null;
    }

    private boolean filterAddRecord(BorrowedBook borrowedBook) {
        if (borrowedBook.getLendId() != null) {
            log.info("记录id不为空");
            return false;
        }
        return borrowedBook.getLendBookId() != null && borrowedBook.getLendUserId() != null
                && borrowedBook.getLendDate() != null && borrowedBook.getLendStatus() != null;
    }


}
