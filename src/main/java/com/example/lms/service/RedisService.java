package com.example.lms.service;

import java.util.List;

/**
 * @param <E> the type parameter
 * @author 王子昂
 * @date 5 /21/21
 * @description Redis服务接口
 */
public interface RedisService<E> {

    /**
     * Add objects int.
     *
     * @param key       the key
     * @param keyList   the key list
     * @param valueList the value list
     * @return the int
     */
    int addObjects(String key, List<String> keyList, List<E> valueList);

    /**
     * Select objects list.
     *
     * @param key the key
     * @return the list
     */
    List<E> selectObjects(String key);

    /**
     * Update object int.
     *
     * @param key the key
     * @param e   the e
     * @return the int
     */
    int updateObject(String key, E e);

    /**
     * Delete object int.
     *
     * @param key the key
     * @return the int
     */
    int deleteObject(String key);

    /**
     * Add object int.
     *
     * @param pattern the pattern
     * @param key     the key
     * @param e       the e
     * @return the int
     */
    int addObject(String pattern, String key, E e);
}
