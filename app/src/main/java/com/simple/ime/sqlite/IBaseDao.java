package com.simple.ime.sqlite;

/**
 * 规范所有的数据库操作
 * 增删改查
 */
public interface IBaseDao<T> {

    //插入操作
    long insert(T entity);
}
