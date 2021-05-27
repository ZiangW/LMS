package com.example.lms.service.impl;

import com.alibaba.fastjson.JSON;
import com.example.lms.dao.ReturnedBookDao;
import com.example.lms.exception.BusinessException;
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

import static com.example.lms.constants.GlobalConstants.RETURN_KEY_PREFIX;
import static com.example.lms.enums.RespEnum.PARAM_ERROR;

/**
 * @author 王子昂
 * @date 5/21/21
 * @description ReturnedBookManager实现
 */
@Slf4j
@Service
@CacheConfig(cacheNames = "returnedBooks")
public class ReturnedBookManagerImpl implements ReturnedBookManager {

    @Autowired
    private ReturnedBookDao returnedBookDao;
    @Autowired
    private RedisService<ReturnedBook> redisService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int addRecord(ReturnedBook returnedBook) {
        if (this.filterAddRecord(returnedBook)) {
            throw new BusinessException("添加记录的信息不完整", PARAM_ERROR.getErrno());
        }
        ReturnedBook res = new ReturnedBook();
        List<ReturnedBook> list = returnedBookDao.selectByExample(Example.builder(ReturnedBook.class)
                .where(selectWithConditions(returnedBook, false)).build());
        if (!list.isEmpty()) {
            if (list.get(0).getReturnStatus() == 0) {
                log.info("存在失效记录");
                returnedBook.setReturnId(list.get(0).getReturnId());
                if (returnedBookDao.updateByPrimaryKey(returnedBook) == 1) {
                    res = returnedBook;
                } else {
                    log.error("（添加）更新记录失败，参数为{}", JSON.toJSON(returnedBook));
                }
            } else {
                log.error("记录已存在，不可重复添加，参数为{}", JSON.toJSON(returnedBook));
            }
        } else if (returnedBookDao.insertSelective(returnedBook) == 1) {
            log.info("不存在失效记录");
            res = returnedBook;
        } else {
            log.info("不存在失效记录");
            log.error("（添加）插入记录信息失败，参数为{}", JSON.toJSON(returnedBook));
        }
        // CachePut
        if (res.getReturnId() != null) {
            redisService.addObject("returnedBook::set::*", RETURN_KEY_PREFIX + res.getReturnId(), res);
            return 1;
        }
        return 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteRecord(ReturnedBook returnedBook) {
        if (this.filterDeleteRecord(returnedBook)) {
            throw new BusinessException("删除记录的信息不合法", PARAM_ERROR.getErrno());
        }
        returnedBook.setReturnStatus(0);
        int flag = returnedBookDao.updateByPrimaryKeySelective(returnedBook);
        if (flag == 1) {
            redisService.deleteObject(RETURN_KEY_PREFIX + returnedBook.getReturnId());
        } else {
            log.error("删除记录失败，参数为{}", JSON.toJSON(returnedBook));
        }
        return flag;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateRecord(ReturnedBook returnedBook) {
        if (this.filterUpdateRecord(returnedBook)) {
            throw new BusinessException("更新记录的信息不合法", PARAM_ERROR.getErrno());
        }
        if (returnedBookDao.updateByPrimaryKeySelective(returnedBook) == 1) {
            // CachePut
            redisService.updateObject(RETURN_KEY_PREFIX + returnedBook.getReturnId(), returnedBook);
            return 1;
        } else {
            log.error("更新借阅记录失败，参数为{}", JSON.toJSON(returnedBook));
        }
        return 0;
    }

    @Override
    public List<ReturnedBook> getRecords(ReturnedBook returnedBook) {
        // Cacheable
        String key = "returnedBook::set::" + RETURN_KEY_PREFIX + returnedBook.getReturnId();
        List<ReturnedBook> list = redisService.selectObjects(key);
        if (list.isEmpty()) {
            log.info("缓存不存在");
            list = returnedBookDao.selectByExample(Example.builder(ReturnedBook.class)
                    .where(this.selectWithConditions(returnedBook, true)).build());
            List<String> keys = new ArrayList<>();
            for (ReturnedBook b : list) {
                keys.add(RETURN_KEY_PREFIX + b.getReturnId());
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
        WeekendSqls<ReturnedBook> conditions = WeekendSqls.custom();
        if (returnedBook.getReturnId() != null) {
            conditions.andEqualTo(ReturnedBook::getReturnId, returnedBook.getReturnId());
        }
        if (returnedBook.getReturnUserId() != null) {
            conditions.andEqualTo(ReturnedBook::getReturnUserId, returnedBook.getReturnUserId());
        }
        if (returnedBook.getReturnBookId() != null) {
            conditions.andEqualTo(ReturnedBook::getReturnBookId, returnedBook.getReturnBookId());
        }
        if (selectOrUpdate) {
            conditions.andEqualTo(ReturnedBook::getReturnStatus, 1);
        }
        return conditions;
    }

    private boolean filterUpdateRecord(ReturnedBook returnedBook) {
        return returnedBook.getReturnId() == null || (returnedBook.getReturnBookId() == null && returnedBook.getReturnUserId() == null
                && returnedBook.getReturnDate() == null && returnedBook.getReturnStatus() == null);
    }

    private boolean filterAddRecord(ReturnedBook returnedBook) {
        return returnedBook.getReturnId() != null || returnedBook.getReturnBookId() == null || returnedBook.getReturnUserId() == null
                || returnedBook.getReturnDate() == null || returnedBook.getReturnStatus() != null;
    }

    private boolean filterDeleteRecord(ReturnedBook returnedBook) {
        return returnedBook.getReturnId() == null || returnedBook.getReturnBookId() != null || returnedBook.getReturnUserId() != null
                || returnedBook.getReturnDate() != null || returnedBook.getReturnStatus() != null;
    }


}
