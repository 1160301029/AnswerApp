package com.example.wyxiang.answerapp.Database;

import cn.bmob.v3.BmobObject;

/**
 * Created by wyxiang on 18-2-1.
 */

public class History extends BmobObject{

    private String id;
    private String title;
    private String answerA;
    private String answerB;
    private String answerC;
    private String answerD;
    private int answerId;
    private String answer;
    private int selectedAnswerId;
    private String chapter; //该题属于哪个章节的

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAnswerA() {
        return answerA;
    }

    public void setAnswerA(String answerA) {
        this.answerA = answerA;
    }

    public String getAnswerB() {
        return answerB;
    }

    public void setAnswerB(String answerB) {
        this.answerB = answerB;
    }

    public String getAnswerC() {
        return answerC;
    }

    public void setAnswerC(String answerC) {
        this.answerC = answerC;
    }

    public String getAnswerD() {
        return answerD;
    }

    public void setAnswerD(String answerD) {
        this.answerD = answerD;
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

    public int getSelectedAnswerId() {
        return selectedAnswerId;
    }

    public void setSelectedAnswerId(int selectedAnswerId) {
        this.selectedAnswerId = selectedAnswerId;
    }

    public String getChapter() {
        return chapter;
    }

    public void setChapter(String chapter) {
        this.chapter = chapter;
    }
}
