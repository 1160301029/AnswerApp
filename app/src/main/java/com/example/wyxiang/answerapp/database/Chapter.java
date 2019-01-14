package com.example.wyxiang.answerapp.Database;

import cn.bmob.v3.BmobObject;

/**
 * Created by wyxiang on 18-2-1.
 */

public class Chapter extends BmobObject{

    private String title;

    private Integer number;

    public String getTitle() {
        return title;
    }

    public Integer getNumber() {
        return number;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }
}
