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

@Slf4j
@Service
@Transactional
public class RedisServiceImpl<E> implements RedisService<E> {
    @Resource
    StringRedisTemplate stringTemplate;
    @Resource
    RedisTemplate<String, Object> redisTemplate;

    @Override
    public int addObjects(String key, List<String> keyList, List<E> valueList) {
        for (int i = 0; i < keyList.size(); i++) {
            if (keyList.get(i).length() < 1) {
                continue;
            }
            if (redisTemplate.opsForValue().get(keyList.get(i)) == null) {
                redisTemplate.opsForValue().set(keyList.get(i), valueList.get(i));
            }
            stringTemplate.opsForSet().add(key, keyList.get(i));
        }
        return 1;
    }

    @Override
    public List<E> selectObjects(String key) {
        Set<String> resultSet = stringTemplate.opsForSet().members(key);
        if (resultSet == null) {
            log.info("缓存失败（set不存在）");
            return new ArrayList<>();
        }
        List<String> keyList = new ArrayList<>(resultSet);
        List<E> valueList = new ArrayList<>();
        for (String k : keyList) {
            if (redisTemplate.opsForValue().get(k) == null) {
                stringTemplate.opsForSet().remove(key, k);
                continue;
            }
            valueList.add((E) redisTemplate.opsForValue().get(k));
        }
        return valueList;
    }

    @Override
    public int updateObject(String key, E e) {
        if (redisTemplate.opsForValue().get(key) != null) {
            log.info("key存在");
        }
        redisTemplate.opsForValue().set(key, e);
        return 1;
    }

    @Override
    public int deleteObject(String key) {
        // 删除本条记录
        if (redisTemplate.opsForValue().get(key) != null) {
            if(Boolean.TRUE.equals(redisTemplate.delete(key))) {
                log.info("缓存删除失败");
                return 0;
            }
            return 1;
        }
        log.info("缓存删除失败（key不存在）");
        return 0;
    }

    @Override
    public int addObject(String pattern, String key, E e) {
        redisTemplate.opsForValue().set(key, e);
        // 删除所有范围查询缓存
        Set<String> keys = stringTemplate.keys(pattern);
        if (keys != null) {
            stringTemplate.delete(keys);
            return 1;
        }
        log.info("相关缓存删除失败（key不存在）");
        return 0;
    }

}
