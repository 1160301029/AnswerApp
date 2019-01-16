package com.example.answerapp.database;

import java.io.Serializable;
import java.util.Date;

/**
 * 历史记录项，继承自Question，加入用户名和完成时间
 */
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

    public History(WrongBook wrongBook){
        this.title = wrongBook.title;
        this.type = wrongBook.type;
        this.option0 = wrongBook.option0;
        this.option1 = wrongBook.option1;
        this.option2 = wrongBook.option2;
        this.option3 = wrongBook.option3;
        this.option4 = wrongBook.option4;
        this.option5 = wrongBook.option5;
        this.answerId = wrongBook.answerId;
        this.answer = wrongBook.answer;
        this.isFinish = wrongBook.isFinish;
        this.selectedId = wrongBook.selectedId;
        this.usrId = wrongBook.getUsrId();
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
