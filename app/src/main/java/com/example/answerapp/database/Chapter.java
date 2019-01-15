package com.example.answerapp.database;

import cn.bmob.v3.BmobObject;

public class Chapter extends BmobObject {

    private String title;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
