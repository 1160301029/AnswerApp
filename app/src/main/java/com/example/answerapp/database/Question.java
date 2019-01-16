package com.example.answerapp.database;

import android.text.TextUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobObject;

public class Question extends BmobObject implements Serializable {

    protected String chapter;

    protected String title; // 题目

    protected String type; // 题型: 单选/多选/判断 -> Single/Multi/Judge

    // 最多留个选项
    protected String option0;

    protected String option1;

    protected String option2;

    protected String option3;

    protected String option4;

    protected String option5;

    protected Integer answerId;  // 若是参选则为一个数，若为多选，则其为一个数字组合 如 320代表选了2个选项分别是第4，第3，第1个

    protected String answer; // 题解

    protected Boolean isFinish = false; // 是否作答

    protected Integer selectedId; // 所选序号


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOption0() {
        return option0;
    }

    public void setOption0(String option0) {
        this.option0 = option0;
    }

    public String getOption1() {
        return option1;
    }

    public void setOption1(String option1) {
        this.option1 = option1;
    }

    public String getOption2() {
        return option2;
    }

    public void setOption2(String option2) {
        this.option2 = option2;
    }

    public String getOption3() {
        return option3;
    }

    public void setOption3(String option3) {
        this.option3 = option3;
    }

    public String getOption4() {
        return option4;
    }

    public void setOption4(String option4) {
        this.option4 = option4;
    }

    public String getOption5() {
        return option5;
    }

    public void setOption5(String option5) {
        this.option5 = option5;
    }

    public int getAnswerId() {
        return answerId;
    }

    public void setAnswerId(int answerId) {
        this.answerId = answerId;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Boolean getFinish() {
        return isFinish;
    }

    public void setFinish(Boolean finish) {
        isFinish = finish;
    }

    public Integer getSelectedId() {
        return selectedId;
    }

    public void setSelectedId(Integer selectedId) {
        this.selectedId = selectedId;
    }

    public List<String> getOptions(){
        List<String> options = new ArrayList<>();

        if (option0!=null && !TextUtils.isEmpty(option0.trim())){
            options.add(option0);
        }
        if (option1!=null && !TextUtils.isEmpty(option1.trim())){
            options.add(option1);
        }
        if (option2!=null && !TextUtils.isEmpty(option2.trim())){
            options.add(option2);
        }
        if (option3!=null && !TextUtils.isEmpty(option3.trim())){
            options.add(option3);
        }
        if (option4!=null && !TextUtils.isEmpty(option4.trim())){
            options.add(option4);
        }
        if (option5!=null && !TextUtils.isEmpty(option5.trim())){
            options.add(option5);
        }
        return options;
    }

    public String getChapter() {
        return chapter;
    }

    public void setChapter(String chapter) {
        this.chapter = chapter;
    }
}
