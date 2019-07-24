package com.simple.ime.sqlite;

import android.database.sqlite.SQLiteDatabase;

public class BaseDaoFactory {

    private SQLiteDatabase sqLiteDatabase;
    //定义数据库存放的路径 这个内容可以写在配置文件中
    private String sqliteDatePath;


    private static class holder{
        private static BaseDaoFactory instance = new BaseDaoFactory();
    }
    public static BaseDaoFactory getInstance(){
        return holder.instance;
    }
    private BaseDaoFactory(){
        sqliteDatePath = "data/data/com.simple.ime/bing.db";
        sqLiteDatabase = SQLiteDatabase.openOrCreateDatabase(sqliteDatePath,null);
    }
    //提供一个对外的API
    public <T>BaseDao<T> getBaseDao(Class<T> entityClass){
        BaseDao baseDao = null;
        try {
            baseDao = BaseDao.class.newInstance();
            baseDao.init(sqLiteDatabase,entityClass);
        }catch (Exception e){
            e.printStackTrace();
        }
        return baseDao;
    }
}
