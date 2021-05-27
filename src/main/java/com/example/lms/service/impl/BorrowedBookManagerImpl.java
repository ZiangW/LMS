package com.example.lms.service.impl;

import com.alibaba.fastjson.JSON;
import com.example.lms.dao.BorrowedBookDao;
import com.example.lms.exception.BusinessException;
import com.example.lms.model.BorrowedBook;
import com.example.lms.service.BorrowedBookManager;
import com.example.lms.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.weekend.WeekendSqls;

import java.util.ArrayList;
import java.util.List;

import static com.example.lms.constants.GlobalConstants.LEND_KEY_PREFIX;
import static com.example.lms.enums.RespEnum.PARAM_ERROR;

/**
 * @author 王子昂
 * @date 5/21/21
 * @description RBorrowedBookManager实现
 */
@Slf4j
@Service
@CacheConfig(cacheNames = "borrowedBooks")
public class BorrowedBookManagerImpl implements BorrowedBookManager {

    @Autowired
    private BorrowedBookDao borrowedBookDao;
    @Autowired
    private RedisService<BorrowedBook> redisService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int addRecord(BorrowedBook borrowedBook) {
        if (this.filterAddRecord(borrowedBook)) {
            throw new BusinessException("添加记录的信息不完整", PARAM_ERROR.getErrno());
        }
        BorrowedBook res = new BorrowedBook();
        List<BorrowedBook> list = borrowedBookDao.selectByExample(Example.builder(BorrowedBook.class)
                .where(selectWithConditions(borrowedBook, false)).build());
        if (!list.isEmpty()) {
            if (list.get(0).getLendStatus() == 0) {
                log.info("存在失效记录");
                if (borrowedBookDao.updateByPrimaryKey(borrowedBook) == 1) {
                    res = borrowedBook;
                } else {
                    log.error("（添加）更新记录失败，参数为{}", JSON.toJSON(borrowedBook));
                }
            } else {
                log.error("记录已存在，不可重复添加，参数为{}", JSON.toJSON(borrowedBook));
            }
        } else if (borrowedBookDao.insertSelective(borrowedBook) == 1) {
            log.info("不存在失效记录");
            res = borrowedBook;
        } else {
            log.info("不存在失效记录");
            log.error("（添加）插入记录信息失败，参数为{}", JSON.toJSON(borrowedBook));
        }
        // CachePut
        if (res.getLendId() != null) {
            redisService.addObject("borrowedBook::set::*", LEND_KEY_PREFIX + res.getLendId(), res);
            return 1;
        }
        return 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteRecord(BorrowedBook borrowedBook) {
        if (this.filterDeleteRecord(borrowedBook)) {
            throw new BusinessException("删除记录的信息不合法", PARAM_ERROR.getErrno());
        }
        borrowedBook.setLendStatus(0);
        int flag = borrowedBookDao.updateByPrimaryKeySelective(borrowedBook);
        if (flag == 1) {
            redisService.deleteObject(LEND_KEY_PREFIX + borrowedBook.getLendId());
        } else {
            log.error("删除记录失败，参数为{}", JSON.toJSON(borrowedBook));
        }
        return flag;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateRecord(BorrowedBook borrowedBook) {
        if (this.filterUpdateRecord(borrowedBook)) {
            throw new BusinessException("更新记录的信息不合法", PARAM_ERROR.getErrno());
        }
        if (borrowedBookDao.updateByPrimaryKeySelective(borrowedBook) == 1) {
            redisService.updateObject(LEND_KEY_PREFIX + borrowedBook.getLendId(), borrowedBook);
            return 1;
        } else {
            log.error("更新借阅记录失败，参数为{}", JSON.toJSON(borrowedBook));
        }
        return 0;
    }

    @Override
    public List<BorrowedBook> getRecords(BorrowedBook borrowedBook) {
        // Cacheable
        String key = "borrowedBook::set::" + LEND_KEY_PREFIX + borrowedBook.getLendId()
                + "lendUserId::" + borrowedBook.getLendUserId() + "lendBookId::" + borrowedBook.getLendBookId();
        List<BorrowedBook> list = redisService.selectObjects(key);
        if (list.isEmpty()) {
            log.info("缓存不存在");
            list = borrowedBookDao.selectByExample(Example.builder(BorrowedBook.class)
                    .where(this.selectWithConditions(borrowedBook, true)).build());
            List<String> keys = new ArrayList<>();
            for (BorrowedBook b : list) {
                keys.add(LEND_KEY_PREFIX + b.getLendId());
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
        WeekendSqls<BorrowedBook> conditions = WeekendSqls.custom();
        if (borrowedBook.getLendId() != null) {
            conditions.andEqualTo(BorrowedBook::getLendId, borrowedBook.getLendId());
        }
        if (borrowedBook.getLendUserId() != null) {
            conditions.andEqualTo(BorrowedBook::getLendUserId, borrowedBook.getLendUserId());
        }
        if (borrowedBook.getLendBookId() != null) {
            conditions.andEqualTo(BorrowedBook::getLendBookId, borrowedBook.getLendBookId());
        }
        if (selectOrUpdate) {
            conditions.andEqualTo(BorrowedBook::getLendStatus, 1);
        }
        return conditions;
    }

    private boolean filterUpdateRecord(BorrowedBook borrowedBook) {
        return borrowedBook.getLendId() == null || (borrowedBook.getLendBookId() == null
                && borrowedBook.getLendUserId() == null && borrowedBook.getLendDate() == null
                && borrowedBook.getLendStatus() == null);
    }

    private boolean filterAddRecord(BorrowedBook borrowedBook) {
        return borrowedBook.getLendId() != null || borrowedBook.getLendBookId() == null
                || borrowedBook.getLendUserId() == null || borrowedBook.getLendDate() == null
                || borrowedBook.getLendStatus() != null;
    }

    private boolean filterDeleteRecord(BorrowedBook borrowedBook) {
        return borrowedBook.getLendId() == null || borrowedBook.getLendBookId() != null
                || borrowedBook.getLendUserId() != null || borrowedBook.getLendDate() != null
                || borrowedBook.getLendStatus() != null;
    }

}
