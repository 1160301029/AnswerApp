package com.example.answerapp.util;

import android.content.Context;
import android.text.format.Time;
import android.view.WindowManager;

import com.example.answerapp.database.Question;

import java.util.ArrayList;
import java.util.List;

public class Util {

    public static List<Integer> getResultList(int result){
        List<Integer> resultList = new ArrayList<>();

        while(result != 0){
            int x = result % 10;
            result /= 10;
            resultList.add(x);
        }

        return resultList;
    }

    public static int getResultNum(List<Integer> resultList){

        int result = 0;

        for (int i = resultList.size() - 1; i >= 0; i--){
            result = result * 10 + resultList.get(i);
        }

        return result;
    }

    public static int getScreenWidth(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        return windowManager.getDefaultDisplay().getWidth();
    }

    /**
     * 自己造数据，测试用
     * @return
     */
    public static List<Question> getQuestions(){
        List<Question> questions = new ArrayList<>();

        for(int i = 0; i < 5; i++){
            Question question = new Question();
            question.setChapter("Test");
            question.setTitle("这是第" + (i + 1) + "题");
            question.setType("Multi");
//            question.setType("Judge");

            question.setOption0("A");
            question.setOption1("B");
            question.setOption2("C");
            question.setOption3("D");
            question.setOption4("E");
//            question.setAnswerId(i%4);
            question.setAnswerId(420);
            questions.add(question);
        }

        return questions;
    }

    public static String getSystemTime(){
        Time t=new Time(); // or Time t=new Time("GMT+8"); 加上Time Zone资料。
        t.setToNow(); // 取得系统时间。
        int year = t.year;
        int month = t.month+1;
        int day = t.monthDay;
        int hour = t.hour; // 0-23
        int minute = t.minute;
        int second = t.second;
        String systemTime = year+"年"+month+"月"+day+"日"+hour+":"+minute+":"+second;
        return systemTime;
    }

}
