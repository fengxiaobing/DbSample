package com.simple.ime.sqlite;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用来映射表明
 */
@Target(ElementType.FIELD)//用来标识这个注解可以写在什么位置
@Retention(RetentionPolicy.RUNTIME) //注解的生命周期   java----class----jvm
public @interface DbFile {
    String value();  //能够得到注解括号里的数据
}
