package com.example.answerapp.database;

import java.io.Serializable;
import java.util.Date;

public class History extends Question implements Serializable {

    private String usrId;

    private String finishTime;


    public History(Question question){
        this.title = question.title;
        this.type = question.type;
        this.option0 = question.option0;
        this.option1 = question.option1;
        this.option2 = question.option2;
        this.option3 = question.option3;
        this.option4 = question.option4;
        this.option5 = question.option5;
        this.answerId = question.answerId;
        this.answer = question.answer;
        this.isFinish = question.isFinish;
        this.selectedId = question.selectedId;
    }

    public String getUsrId() {
        return usrId;
    }

    public void setUsrId(String usrId) {
        this.usrId = usrId;
    }

    public String getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(String finishTime) {
        this.finishTime = finishTime;
    }
}
