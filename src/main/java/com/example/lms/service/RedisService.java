package com.example.lms.service;

import java.util.List;

public interface RedisService<E> {

    int addObjects(String key, List<String> keyList, List<E> valueList);

    List<E> selectObjects(String key);

    int updateObject(String key, E e);

    int deleteObject(String key);

    int addObject(String pattern, String key, E e);
}
