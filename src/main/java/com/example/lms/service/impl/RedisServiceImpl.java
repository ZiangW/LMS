package com.example.lms.service.impl;

import com.example.lms.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * The interface Admin manager.
 *
 * @author 王子昂
 * @date 5 /21/21
 * @description RedisService实现
 */
@Slf4j
@Service
public class RedisServiceImpl<E> implements RedisService<E> {

    @Resource
    StringRedisTemplate stringTemplate;
    @Resource
    RedisTemplate<String, Object> redisTemplate;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int addObjects(String key, List<String> keyList, List<E> valueList) {
        for (int i = 0; i < keyList.size(); i++) {
            if (keyList.get(i).isEmpty()) {
                log.error("第{}个key不存在，跳过添加", i);
                continue;
            }
            redisTemplate.opsForValue().set(keyList.get(i), valueList.get(i));
            stringTemplate.opsForSet().add(key, keyList.get(i));
        }
        return 1;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<E> selectObjects(String key) {
        Set<String> resultSet = stringTemplate.opsForSet().members(key);
        if (resultSet == null) {
            log.error("缓存失败，set的key不存在{}", key);
            return new ArrayList<>();
        } else {
            log.info("set的key存在{}", key);
        }
        List<String> keyList = new ArrayList<>(resultSet);
        List<E> valueList = new ArrayList<>();
        for (String k : keyList) {
            if (redisTemplate.opsForValue().get(k) == null) {
                log.info("{}的value不存在，删除key", k);
                stringTemplate.opsForSet().remove(key, k);
                continue;
            }
            log.info("{}的value存在，添加到result", k);
            valueList.add((E) redisTemplate.opsForValue().get(k));
        }
        return valueList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateObject(String key, E e) {
        if (redisTemplate.opsForValue().get(key) != null) {
            log.info("key存在，缓存key为：{}", key);
        }
        redisTemplate.opsForValue().set(key, e);
        return 1;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteObject(String key) {
        // 删除本条记录
        if (redisTemplate.opsForValue().get(key) != null) {
            if (Boolean.FALSE.equals(redisTemplate.delete(key))) {
                log.error("缓存删除失败，缓存key为：{}", key);
                return 0;
            }
            log.info("缓存删除成功，，缓存key为：{}", key);
            return 1;
        }
        log.error("缓存删除失败，key不存在{}", key);
        return 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int addObject(String pattern, String key, E e) {
        redisTemplate.opsForValue().set(key, e);
        // 删除所有范围查询缓存
        Set<String> keys = stringTemplate.keys(pattern);
        for (String resultKey : keys) {
            if (resultKey != null) {
                if (Boolean.FALSE.equals(stringTemplate.delete(resultKey))) {
                    log.error("缓存删除失败，缓存key为：{}", resultKey);
                    return 0;
                }
                log.info("缓存删除成功,缓存key为：{}", resultKey);
                return 1;
            }
        }
        log.error("相关缓存删除失败（key set不存在）");
        return 0;
    }

}
