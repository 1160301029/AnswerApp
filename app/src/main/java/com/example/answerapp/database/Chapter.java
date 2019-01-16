package com.example.answerapp.database;

import cn.bmob.v3.BmobObject;

/**
 * 数据库中所有的章节列表
 */
public class Chapter extends BmobObject {

    private String title;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
