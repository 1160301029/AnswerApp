package com.example.answerapp.ui;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.answerapp.R;
import com.example.answerapp.adapter.AnswerAdapter;
import com.example.answerapp.database.History;
import com.example.answerapp.database.Question;
import com.example.answerapp.fragment.QuestionFragment;
import com.example.answerapp.util.DefineTimer;
import com.example.answerapp.util.Util;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobBatch;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BatchResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListListener;

public class TestActivity extends AppCompatActivity {

    private String TAG = getClass().getName();

    public static final String IS_EXAM = "is_exam";
    private ViewPager viewPager;
    private LinearLayout mainBar;
    private TextView rightTxt, errorTxt, totalTxt;
    private TextView timer;
    boolean isExam;
    private DefineTimer countDownTimer;
    private LinearLayout timerLayout;
    private TextView submit;

    private List<Question> questions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        Bmob.initialize(this,"03ed672534583aab5914232995118da3");

        viewPager = findViewById(R.id.main_viewpager);
        mainBar = findViewById(R.id.main_bar);
        rightTxt = findViewById(R.id.main_right_tx);
        errorTxt = findViewById(R.id.main_error_tx);
        totalTxt = findViewById(R.id.main_total_tx);
        timer = findViewById(R.id.question_countdown);
        timerLayout = findViewById(R.id.question_timer);
        submit = findViewById(R.id.question_submit);

        questions = (List<Question>) getIntent().getSerializableExtra("question");
        if (questions == null){
            Log.d(TAG, "onCreate: question is null");
            questions = Util.getQuestions();
        }


        mainBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openBottomSheetDialog();
            }
        });

        viewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        viewPager.setOnPageChangeListener(new MyPagerChangeListner());

        isExam = getIntent().getBooleanExtra(IS_EXAM, true);

        if (!isExam) {//如果不是考试,就隐藏
            timerLayout.setVisibility(View.GONE);
            submit.setVisibility(View.GONE);
        }

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backHandler();
            }
        });
    }

    private long currentTime = 12000000L;

    public void startTimer() {
        countDownTimer = new DefineTimer(currentTime, 1000) {//2700 45分钟
            @Override
            public void onTick(long l) {
                currentTime = l;
                int allSecond = (int) l / 1000;//秒
                int minute = allSecond / 60;
                int second = allSecond - minute * 60;
                timer.setText("倒计时 " + minute + ":" + second);
            }

            @Override
            public void onFinish() {
                saveHistory();
            }
        };
        countDownTimer.start();
    }


    @Override
    protected void onPause() {
        if (isExam) {
            countDownTimer.cancel();
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        if (isExam) {
            startTimer();
        }
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        if (isExam) {
            countDownTimer.cancel();
        }
        super.onDestroy();
    }


    BottomSheetDialog bottomSheetDialog;

    public void openBottomSheetDialog() {
        bottomSheetDialog = new BottomSheetDialog(this);
        GridView gridView = new GridView(this);
        gridView.setNumColumns(6);
        gridView.setVerticalSpacing(10);
        gridView.setHorizontalSpacing(10);
        gridView.setBackgroundColor(0xffffffff);
        gridView.setAdapter(new AnswerAdapter(questions));
        gridView.setScrollBarStyle(GridView.SCROLLBARS_OUTSIDE_INSET);
        gridView.setPadding(20, 20, 20, 20);
        bottomSheetDialog.setContentView(gridView);
        bottomSheetDialog.show();
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                viewPager.setCurrentItem(position, false);
                bottomSheetDialog.dismiss();
            }
        });
    }


    class MyPagerChangeListner implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            totalTxt.setText((position + 1) + "/" + questions.size());
        }

        @Override
        public void onPageSelected(int position) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            QuestionFragment questionFragment = new QuestionFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("question", questions.get(position));
            questionFragment.setArguments(bundle);
            return questionFragment;
        }

        @Override
        public int getCount() {
            return questions.size();
        }
    }

    public void backHandler() {
        int finishAnswer = 0;
        int rightAnswer = 0;
        int unfinishAnswer = 0;
        for (Question q : questions) {
            if (q.getFinish()){
                Log.d(TAG, "backHandler: " + q.getTitle() + q.getAnswerId() + q.getSelectedId());
                finishAnswer++;
                if(q.getSelectedId() == q.getAnswerId())
                    rightAnswer++;
            }else {
                unfinishAnswer++;
            }

        }

        AlertDialog dialog = new AlertDialog.Builder(this).setTitle("温馨提示").setMessage("您已回答了" + finishAnswer + "题共(" + questions.size() + ")题,共答对" + rightAnswer +"题, 未答" + unfinishAnswer + "题,确定交卷？")
                .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        saveHistory();
                    }
                }).setPositiveButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                }).create();
        dialog.show();
    }

    //提交答案
    private void saveHistory() {
        String time = Util.getSystemTime();
        Log.d(TAG, "saveHistory: " + time);

        SharedPreferences preferences = getSharedPreferences("data",MODE_PRIVATE);
        String usrId = preferences.getString("userMail","");

        List<BmobObject> histories = new ArrayList<>();
        for(Question q : questions){
            History history = new History(q);
            history.setFinishTime(time);
            history.setUsrId(usrId);
            histories.add(history);
        }

        new BmobBatch().insertBatch(histories).doBatch(new QueryListListener<BatchResult>() {

            @Override
            public void done(List<BatchResult> results, BmobException e) {
                if (e == null) {
                    for (int i = 0; i < results.size(); i++) {
                        BatchResult result = results.get(i);
                        BmobException ex = result.getError();
                        if (ex == null) {
                            Log.d(TAG, "done: " + "第" + i + "个数据批量添加成功：" + result.getCreatedAt() + "," + result.getObjectId() + "," + result.getUpdatedAt());
                        } else {
                            Log.d(TAG, "done: " + "第" + i + "个数据批量添加失败：" + ex.getMessage() + "," + ex.getErrorCode());
                        }
                    }

                    // finish

                } else {
                    Toast.makeText(TestActivity.this, "存储失败，请重试", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


}
