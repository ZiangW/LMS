package com.example.lms.service.impl;

import com.example.lms.dao.ReturnedBookDao;
import com.example.lms.model.Book;
import com.example.lms.model.ReturnedBook;
import com.example.lms.service.RedisService;
import com.example.lms.service.ReturnedBookManager;
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
@CacheConfig(cacheNames = "returnedBooks")
public class ReturnedBookManagerImpl implements ReturnedBookManager {

    @Autowired
    private ReturnedBookDao returnedBookDao;
    @Autowired
    private RedisService<ReturnedBook> redisService;

    @Override
    public int addRecord(ReturnedBook returnedBook) {
        ReturnedBook res = new ReturnedBook();

        // 过滤非法记录
        if (!this.filterAddRecord(returnedBook)) {
            log.info("记录不完整");
            return 0;
        }
        List<ReturnedBook> list = returnedBookDao.selectByExample(Example.builder(ReturnedBook.class)
                .where(selectWithConditions(returnedBook, false)).build());
        if (list.size() > 0) {
            if (list.get(0).getReturnStatus() == 0) {
                log.info("存在失效记录");
                returnedBook.setReturnId(list.get(0).getReturnId());
                if (returnedBookDao.updateByPrimaryKey(returnedBook) == 1) {
                    res = returnedBook;
                } else {
                    log.info("更新记录失败");
                }
            } else {
                log.info("记录已存在");
            }
        } else if (returnedBookDao.insertSelective(returnedBook) == 1) {
            log.info("不存在失效记录");
            list = returnedBookDao.selectByExample(Example.builder(Book.class)
                    .where(selectWithConditions(returnedBook, true)).build());
            if (list.size() == 1) {
                res = list.get(0);
            }
        } else {
            log.info("不存在失效记录");
            log.info("添加记录失败");
        }
        // CachePut
        if (res.getReturnId() != null) {
            redisService.addObject("returnedBook::set::*", "returnId::" + res.getReturnId(), res);
            return 1;
        }
        return 0;
    }

    @Override
    public List<Integer> deleteRecords(List<ReturnedBook> list) {
        List<Integer> res = new ArrayList<>();
        for (ReturnedBook returnedBook : list) {
            returnedBook.setReturnStatus(0);
            int flag = returnedBookDao.updateByPrimaryKeySelective(returnedBook);
            res.add(flag);
            if (flag == 1) {
                redisService.deleteObject("returnId::" + returnedBook.getReturnId());
            }
        }
        return res;
    }

    @Override
    public int updateRecord(ReturnedBook returnedBook) {
        if (this.filterUpdateRecord(returnedBook)) {
            log.info("非法记录");
            return 0;
        }
        if (returnedBookDao.updateByPrimaryKeySelective(returnedBook) == 1) {
            List<ReturnedBook> list = returnedBookDao.selectByExample(Example.builder(ReturnedBook.class)
                    .where(this.selectWithConditions(returnedBook, true)).build());
            if (list.size() == 1) {
                ReturnedBook res = list.get(0);
                // CachePut
                redisService.updateObject("returnId::" + res.getReturnId(), res);
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
    public List<ReturnedBook> getRecords(ReturnedBook returnedBook) {
        // Cacheable
        String key = "returnedBook::set::" + "returnId::" + returnedBook.getReturnId();
        List<ReturnedBook> list = redisService.selectObjects(key);
        if (list.size() < 1) {
            log.info("缓存不存在");
            list = returnedBookDao.selectByExample(Example.builder(ReturnedBook.class)
                    .where(this.selectWithConditions(returnedBook, true)).build());
            List<String> keys = new ArrayList<>();
            for (ReturnedBook b : list) {
                keys.add("returnId::" + b.getReturnId());
            }
            redisService.addObjects(key, keys, list);
        }
        return list;
    }

    @Override
    public List<ReturnedBook> getAllRecords() {
        return returnedBookDao.selectAll();
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

    private boolean filterUpdateRecord(ReturnedBook returnedBook) {
        if (returnedBook.getReturnId() == null) {
            log.info("记录id为空");
            return true;
        }
        return returnedBook.getReturnBookId() == null && returnedBook.getReturnUserId() == null
                && returnedBook.getReturnDate() == null && returnedBook.getReturnStatus() == null;
    }

    private boolean filterAddRecord(ReturnedBook returnedBook) {
        if (returnedBook.getReturnId() != null) {
            log.info("记录id不为空");
            return false;
        }
        return returnedBook.getReturnBookId() != null && returnedBook.getReturnUserId() != null
                && returnedBook.getReturnDate() != null && returnedBook.getReturnStatus() != null;
    }


}
