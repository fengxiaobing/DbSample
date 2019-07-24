package com.simple.ime.bean;

import com.simple.ime.sqlite.DbFile;
import com.simple.ime.sqlite.DbTable;

@DbTable("User_bing")
public class User {
    @DbFile("_id")
    private Integer id;
    @DbFile("name")
    private String userName;
    @DbFile("pwd")
    private String userPassword;

    public User(Integer id, String userName, String userPassword) {
        this.id = id;
        this.userName = userName;
        this.userPassword = userPassword;
    }


}
