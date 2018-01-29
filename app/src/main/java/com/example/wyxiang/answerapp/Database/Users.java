package com.example.wyxiang.answerapp.Database;

import cn.bmob.v3.BmobUser;

/**
 * Created by wyxiang on 08.07.17.
 */

public class Users extends BmobUser{

    private String nickname;


    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
