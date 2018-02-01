package com.example.wyxiang.answerapp.UI;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.wyxiang.answerapp.Database.History;
import com.example.wyxiang.answerapp.Database.Question;
import com.example.wyxiang.answerapp.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cn.bmob.v3.BmobBatch;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BatchResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListListener;
import io.github.kexanie.library.MathView;

/**
 * Created by wyxiang on 18-2-1.
 */

public class TestActivity extends AppCompatActivity{

    private String TAG = "TestActivity";
    private List<Question> list;
    private List<BmobObject> historyList = new ArrayList<>();

    private int count; //题目总数
    private int corrent; // 当前题目位置
    private RadioGroup mRadioGroup;
    RadioButton[] mRadioButton = new RadioButton[4];
    private Button btn_up;
    private Button btn_down;
    private MathView mv_title;
    private MathView mv_answerA;
    private MathView mv_answerB;
    private MathView mv_answerC;
    private MathView mv_answerD;
    private MathView mv_answer;
    private boolean wrongmode;

    private int question_id[] = new int[20];
    private int selectedAnswer[] = new int[20];

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        list = (List<Question>) getIntent().getSerializableExtra("list");
        Log.d(TAG, "onCreate: " + list.size());
        count = list.size();
        initView();
        show();

    }

    private void show() {

        corrent = 0;
        mv_title.setText(corrent + 1 + ". " + list.get(corrent).getTitle());
        mv_answerA.setText(list.get(corrent).getAnswerA());
        mv_answerB.setText(list.get(corrent).getAnswerB());
        mv_answerC.setText(list.get(corrent).getAnswerC());
        mv_answerD.setText(list.get(corrent).getAnswerD());
        mv_answer.setText(list.get(corrent).getAnswer());

        //点击上一题
        btn_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (corrent > 0) {
                    corrent--;
                    mv_title.setText(corrent + 1 +". " + list.get(corrent).getTitle());
                    mv_answerA.setText(list.get(corrent).getAnswerA());
                    mv_answerB.setText(list.get(corrent).getAnswerB());
                    mv_answerC.setText(list.get(corrent).getAnswerC());
                    mv_answerD.setText(list.get(corrent).getAnswerD());
                    mv_answer.setText(list.get(corrent).getAnswer());
                    //设置选中
                    for(int i=0;i<4;i++) {
                        mRadioButton[i].setChecked(false);
                        if (list.get(corrent).getSelectedAnswerId() == i){
                            mRadioButton[i].setChecked(true);
                        }
                    }
                }
            }
        });


        btn_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (corrent < count -1) {
                    corrent++;
                    mv_title.setText(corrent + 1 +". " + list.get(corrent).getTitle());
                    mv_answerA.setText(list.get(corrent).getAnswerA());
                    mv_answerB.setText(list.get(corrent).getAnswerB());
                    mv_answerC.setText(list.get(corrent).getAnswerC());
                    mv_answerD.setText(list.get(corrent).getAnswerD());
                    mv_answer.setText(list.get(corrent).getAnswer());
                    for(int i=0;i<4;i++) {
                        mRadioButton[i].setChecked(false);
                        if (list.get(corrent).getSelectedAnswerId() == i){
                            mRadioButton[i].setChecked(true);
                        }
                    }
                    if (corrent == count - 1){
                        //提示已是最后一题
                        Toast.makeText(TestActivity.this,"已经到达最后一题",Toast.LENGTH_SHORT).show();
                    }

                } else if(corrent == list.size() - 1 && wrongmode == true) {
                    new AlertDialog.Builder(TestActivity.this).setTitle("提示").setMessage("已经到达最后一道题，是否退出？")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            }).setNegativeButton("取消",null).show();

                } else if(corrent == count - 1 && wrongmode == false) {

                    final List<Integer> wrongList = checkAnswer(list);

                    if(wrongList.size() == 0) {
                        new AlertDialog.Builder(TestActivity.this).setTitle("提示").setMessage("你好厉害，答对了所有题！")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                    }
                                }).setNegativeButton("取消",null).show();
                    }

                    new AlertDialog.Builder(TestActivity.this).setTitle("恭喜，答题完成！")
                            .setMessage("答对了" + (count - wrongList.size()) + "道题" + "\n"
                                    + "答错了" + wrongList.size() + "道题" + "\n" + "是否查看错题？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //存储聊天记录
                            SharedPreferences preferences = getSharedPreferences("data",MODE_PRIVATE);
                            String id = preferences.getString("userMail","");
                            String time = getTime();
                            for (Question question : list){
                                History history = new History();
                                history.setTitle(question.getTitle());
                                history.setAnswerA(question.getAnswerA());
                                history.setAnswerB(question.getAnswerB());
                                history.setAnswerC(question.getAnswerC());
                                history.setAnswerD(question.getAnswerD());
                                history.setAnswer(question.getAnswer());
                                history.setSelectedAnswerId(question.getSelectedAnswerId());
                                history.setChapter(question.getChapter() + "   " + time);
                                history.setAnswerId(question.getAnswerId());
                                history.setId(id);
                                historyList.add(history);
                            }
                            final ProgressDialog progress = new ProgressDialog(TestActivity.this);
                            progress.setMessage("正在加载中...");
                            progress.setCanceledOnTouchOutside(false);
                            progress.show();
                            new BmobBatch().insertBatch(historyList).doBatch(new QueryListListener<BatchResult>() {
                                @Override
                                public void done(List<BatchResult> list, BmobException e) {
                                    if (e == null){
                                        progress.hide();
                                    }
                                }
                            });
                            //显示错题
                            if(wrongList.size()>0)
                            {
                                wrongmode = true;
                                List<Question> newList = new ArrayList<>();
                                for (int i = 0; i < wrongList.size(); i++) {
                                    newList.add(list.get(wrongList.get(i)));
                                }
                                list.clear();
                                for (int i = 0; i < newList.size(); i++) {
                                    list.add(newList.get(i));
                                }
                                corrent = 0;
                                mv_title.setText(corrent + 1 +". " + list.get(corrent).getTitle());
                                mv_answerA.setText(list.get(corrent).getAnswerA());
                                mv_answerB.setText(list.get(corrent).getAnswerB());
                                mv_answerC.setText(list.get(corrent).getAnswerC());
                                mv_answerD.setText(list.get(corrent).getAnswerD());
                                mv_answer.setText(list.get(corrent).getAnswer());
                                mv_answer.setVisibility(View.VISIBLE);
                            }
                        }
                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    }).show();
                }
            }
        });


        //检查选中
        mRadioButton[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i=0;i<4;i++) {
                    mRadioButton[i].setChecked(false);
                }
                mRadioButton[0].setChecked(true);
                list.get(corrent).setSelectedAnswerId(0);
            }
        });
        mRadioButton[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i=0;i<4;i++) {
                    mRadioButton[i].setChecked(false);
                }
                mRadioButton[1].setChecked(true);
                list.get(corrent).setSelectedAnswerId(1);
            }
        });
        mRadioButton[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i=0;i<4;i++) {
                    mRadioButton[i].setChecked(false);
                }
                mRadioButton[2].setChecked(true);
                list.get(corrent).setSelectedAnswerId(2);
            }
        });
        mRadioButton[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i=0;i<4;i++) {
                    mRadioButton[i].setChecked(false);
                }
                mRadioButton[3].setChecked(true);
                list.get(corrent).setSelectedAnswerId(3);
            }
        });
    }

    private void initView() {

        mv_title = (MathView) findViewById(R.id.mv_title);
        mv_answer = (MathView) findViewById(R.id.mv_answer);

        mv_answerA = (MathView) findViewById(R.id.mv_answerA);
        mv_answerB = (MathView) findViewById(R.id.mv_answerB);
        mv_answerC = (MathView) findViewById(R.id.mv_answerC);
        mv_answerD = (MathView) findViewById(R.id.mv_answerD);

        mRadioButton[0] = (RadioButton) findViewById(R.id.mRadioA);
        mRadioButton[1] = (RadioButton) findViewById(R.id.mRadioB);
        mRadioButton[2] = (RadioButton) findViewById(R.id.mRadioC);
        mRadioButton[3] = (RadioButton) findViewById(R.id.mRadioD);
        mRadioGroup = (RadioGroup) findViewById(R.id.mRadioGroup);

        btn_down = (Button) findViewById(R.id.btn_down);
        btn_up = (Button) findViewById(R.id.btn_up);

    }

    private List<Integer> checkAnswer(List<Question> list) {
        List<Integer>wrongList= new ArrayList<>();
        for(int i = 0 ; i<list.size();i++){
            //添加数据
            selectedAnswer[i] = list.get(i).getSelectedAnswerId();
            question_id[i] = list.get(i).getId();
            //判断对错
            if (list.get(i).getAnswerId() != list.get(i).getSelectedAnswerId()) {
                wrongList.add(i);
            }
        }
        return wrongList;
    }

    public String getTime(){
        Calendar calendar = Calendar.getInstance();
        String time = calendar.get(Calendar.YEAR) + "y"
                + (calendar.get(Calendar.MONTH)+1) + "m"//从0计算
                + calendar.get(Calendar.DAY_OF_MONTH) + "d"
                + calendar.get(Calendar.HOUR_OF_DAY) + "h";
        return time;
    }

}
