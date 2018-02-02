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

import com.example.wyxiang.answerapp.Database.History;
import com.example.wyxiang.answerapp.R;
import java.util.List;

import io.github.kexanie.library.MathView;

/**
 * Created by wyxiang on 18-2-2.
 */

public class HistoryActivity extends AppCompatActivity{

    private String TAG = "HistoryActivity";
    private List<History> list;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        list = (List<History>) getIntent().getSerializableExtra("list");
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

                } else if(corrent == list.size() - 1) {
                    new AlertDialog.Builder(HistoryActivity.this).setTitle("提示").setMessage("已经到达最后一道题，是否退出？")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            }).setNegativeButton("取消",null).show();

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

        mv_answer.setVisibility(View.VISIBLE);
        mRadioButton[0].setEnabled(false);
        mRadioButton[1].setEnabled(false);
        mRadioButton[2].setEnabled(false);
        mRadioButton[3].setEnabled(false);
    }

}
