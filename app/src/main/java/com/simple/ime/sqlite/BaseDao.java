package com.simple.ime.sqlite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import java.io.FileDescriptor;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class BaseDao<T> implements IBaseDao<T> {
    //持有数据库操作的引用
    private SQLiteDatabase sqLiteDatabase;
    //需要知道用户输入发是什么样的对象，获取用户的java类型
    private Class<T> entityClass;
    //表名
    private String tableName;

    //定义一个缓存  key---字段名     value----成员变量（filed）
    private HashMap<String, Field> cacheMap;
    //标记
    private boolean isInit = false;


//不带构造方法，调用层不可能直接使用我们这个类
    protected boolean init(SQLiteDatabase sqLiteDatabase, Class<T> entityClass) {
        this.sqLiteDatabase = sqLiteDatabase;
        this.entityClass = entityClass;
        if(!isInit) {
            //自动建表
            //1、得到表名  得到User上的User_bing字符串
            tableName = entityClass.getAnnotation(DbTable.class).value();
            //2、开始建表
            if (!sqLiteDatabase.isOpen()) {
                return false;
            }
            //create table if not exists User_bing
            //(_id integer,name varchar(20),pwd varchar(20))
            String createTableSql = getCreateTableSql();
            sqLiteDatabase.execSQL(createTableSql);
            cacheMap = new HashMap<>();
            initCacheMap();
            isInit = true;
        }
        return isInit;
    }

    private void initCacheMap() {
        //定义一个缓存  key---字段名     value----成员变量（filed）
        //1、得到所有的字段名
        String sql = "select * from "+tableName+" limit 1,0";
        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
        String[] columnNames = cursor.getColumnNames();
        //2、得到所有的成员变量
        Field[] columFileds = entityClass.getDeclaredFields();
        //3、保存映射关系
        for (String columnName : columnNames) {
            Field resultField = null;
            for (Field columFiled : columFileds) {
                String fieldAnnotation = columFiled.getAnnotation(DbFile.class).value();
                if(fieldAnnotation.equals(columnName)){
                    resultField = columFiled;
                    break;
                }
            }
            if(resultField != null){
                cacheMap.put(columnName,resultField);
            }
        }

    }

    private String getCreateTableSql() {
        //create table if not exists User_bing
        //(_id integer,name varchar(20),pwd varchar(20))

        StringBuilder sb = new StringBuilder();
        sb.append("create table if not exists ");
        sb.append(tableName+"(");
        //获取调用层传入的对象的所有成员变量
        Field[] fields = entityClass.getDeclaredFields();
        for (Field field : fields) {
            Class type = field.getType();
            if(type == String.class){
                sb.append(field.getAnnotation(DbFile.class).value()).append(" TEXT,");
            }else if(type == Integer.class){
                sb.append(field.getAnnotation(DbFile.class).value()+" INTEGER,");
            }else if(type == Double.class){
                sb.append(field.getAnnotation(DbFile.class).value()+" DOUBLE,");
            }else if(type == Long.class){
                sb.append(field.getAnnotation(DbFile.class).value()+" LONG,");
            }else if(type == byte[].class){
                sb.append(field.getAnnotation(DbFile.class).value()+" BLOB,");
            }else {
                //不支持的
                continue;
            }
        }

        if(sb.charAt(sb.length()-1)==','){
            sb.deleteCharAt(sb.length()-1);
        }
        sb.append(")");
        return sb.toString();
    }

    @Override
    public long insert(T entity) {

//        ContentValues values = new ContentValues();
//        values.put("","");
//        values.put("","");
//        values.put("","");
//        long result = sqLiteDatabase.insert(tableName, null, values);

        Map<String,String> map =getValues(entity);
        //把map中的数据存放到ContentValues中
        ContentValues values = getContentValues(map);
        long result = sqLiteDatabase.insert(tableName, null, values);
        return result;
    }

    private ContentValues getContentValues(Map<String, String> map) {
        ContentValues values = new ContentValues();
        Set keys = map.keySet();
        Iterator iterator = keys.iterator();
        while (iterator.hasNext()){
            String key = (String) iterator.next();
            String value = map.get(key);
            if(value!=null) {
                values.put(key, value);
            }
        }
        return values;
    }

    private Map<String, String> getValues(T entity) {

        HashMap<String,String> map = new HashMap<>();
        //values取到的是成员变量
        Iterator<Field> iterator = cacheMap.values().iterator();
        while (iterator.hasNext()){
            Field field = iterator.next();
            field.setAccessible(true);
            try {
                Object object = field.get(entity);
                if(object == null){
                    continue;
                }
                String value = object.toString();
                //获取列明
                String key = field.getAnnotation(DbFile.class).value();
                if(!TextUtils.isEmpty(key) && !TextUtils.isEmpty(value)){
                    map.put(key,value);
                }
            }catch (Exception e){

            }
        }
        return map;
    }
}
