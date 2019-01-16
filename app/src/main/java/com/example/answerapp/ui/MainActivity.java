package com.example.answerapp.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.answerapp.R;
import com.example.answerapp.database.Chapter;
import com.example.answerapp.database.History;
import com.example.answerapp.database.Question;
import com.example.answerapp.database.WrongBook;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import cn.bmob.v3.Bmob;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class MainActivity extends AppCompatActivity {

    private String TAG = getClass().getName();

    private ListView listView;

    private List<String> chapters;

    private Toolbar toolbar;

    private Map<String,List<Question>> questions;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bmob.initialize(this,"03ed672534583aab5914232995118da3");

        listView = findViewById(R.id.listview);
        toolbar = findViewById(R.id.toolbar);

        questions = new HashMap<>();

        setToolbar();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final String chapter = chapters.get(i);

                if (chapter.equals("模拟考试")){
                    boolean isReady = true;
                    for (int j = 0; j < chapters.size()-1; j++){
                        if (!questions.containsKey(chapters.get(j))){
                            queryChapter(chapters.get(j), false);
                            isReady = false;
                        }
                    }
                    if (!isReady)
                        return;
                    List<Question> qs = getExamQuestions(100);
                    Intent intent = new Intent(MainActivity.this, TestActivity.class);
                    intent.putExtra("question", (Serializable) qs);
                    startActivity(intent);
                    return;
                }

                if (questions.containsKey(chapter)){
                    Log.d(TAG, "onItemClick: 已加载好" + chapter);
                    Intent intent = new Intent(MainActivity.this, TestActivity.class);
                    intent.putExtra("question", (Serializable) questions.get(chapter));
                    startActivity(intent);
                }else {
                    queryChapter(chapter, true);
                }

            }

        });

        BmobQuery<Chapter> query = new BmobQuery<>();
        query.findObjects(new FindListener<Chapter>() {
            @Override
            public void done(List<Chapter> object, BmobException e) {
                if (e == null) {
                    chapters = new ArrayList<>();
                    for (Chapter chapter : object){
                        chapters.add(chapter.getTitle());
                    }
                    // 增加模拟考试
                    chapters.add("模拟考试");
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                            MainActivity.this,R.layout.item_chapter, chapters);
                    listView.setAdapter(adapter);

                    // 预加载所有数据
                    for(final String chapter : chapters){
                        // 模拟考试没有预加载数据，而是将不同章混在一起
                        if (chapter.equals("模拟考试")){
                            continue;
                        }
                        queryChapter(chapter, false);
                    }
                } else {
                    Toast.makeText(MainActivity.this, "获取数据失败，请重试", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "done: 获取数据失败");
                }
            }
        });


    }

    /**
     * 设置Toolbar，各个按钮的点击功能
     */
    private void setToolbar() {
        toolbar.setSubtitleTextColor(Color.WHITE);  //设置副标题字体颜色
        setSupportActionBar(toolbar);   //必须使用
        //添加左边图标点击事件
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //添加menu项点击事件
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.history:
//                        Toast.makeText(MainActivity.this, "暂不支持查看历史", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this, HistoryListActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.wrong_book:
                        SharedPreferences preferences = getSharedPreferences("data",MODE_PRIVATE);
                        String id = preferences.getString("userMail","");
                        BmobQuery<WrongBook> query = new BmobQuery<>();
                        query.setLimit(500);
                        query.addWhereEqualTo("usrId", id);
                        query.findObjects(new FindListener<WrongBook>() {
                            @Override
                            public void done(List<WrongBook> object, BmobException e) {
                                if (e == null) {
                                    List<History> histories = new ArrayList<>();
                                    for (WrongBook wrongBook : object){
                                        histories.add(new History(wrongBook));
                                    }

                                    Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
                                    intent.putExtra("history", (Serializable) histories);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(MainActivity.this, "获取数据失败，请重试", Toast.LENGTH_SHORT).show();
                                    Log.d(TAG, "done: 获取数据失败");
                                }
                            }
                        });
                }
                return true;    //返回为true
            }
        });
    }

    //设置menu（右边图标）
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu); //解析menu布局文件到menu
        return true;
    }


    private void queryChapter(final String chapter, final boolean jump){
        BmobQuery<Question> query = new BmobQuery<>();
        query.setLimit(500);
        query.addWhereEqualTo("chapter", chapter);
        query.findObjects(new FindListener<Question>() {
            @Override
            public void done(List<Question> object, BmobException e) {
                if (e == null) {
                    questions.put(chapter, object);
                    Log.d(TAG, "done: 下载完成" + chapter);
                    if(jump){
                        Intent intent = new Intent(MainActivity.this, TestActivity.class);
                        intent.putExtra("question", (Serializable) object);
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(MainActivity.this, "获取数据失败，请重试", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "done: 获取数据失败");
                }
            }
        });
    }


    private List<Question> getExamQuestions(int getNum) {
        List<Question> questions1 = new ArrayList<>();


        List<Integer> number = new ArrayList<>();

        int sum = 0;

        for (int i = 0; i < chapters.size()-1; i++){
            List<Question> cq = questions.get(chapters.get(i));
            number.add(cq.size());
            sum += cq.size();
        }

        int realNum = 0;
        for (int i = 0; i < chapters.size()-1; i++){
            List<Question> cq = questions.get(chapters.get(i));
            int num = number.get(i) * getNum /sum;
            realNum += num;
            questions1.addAll(createRandomList(cq, num));
        }

        // 凑齐数目
        if (realNum < getNum){
            List<Question> cq = questions.get(chapters.get(0));
            questions1.addAll(createRandomList(cq, getNum - realNum));
        }

        Log.d(TAG, "getExamQuestions: " + questions1.size());
        return questions1;
    }

    private List<Question> createRandomList(List<Question> list, int n) {
        Map<String, String> map = new HashMap<>();
        List<Question>listNew = new ArrayList<>();
        if(list.size()<=n){
            return list;
        }else{
            while(map.size()<n){
                int random = (int) (Math.random() * list.size());
                if (!map.containsKey(String.valueOf(random))) {
                    map.put(String.valueOf(random), "");
                    listNew.add(list.get(random));
                }
            }
            return listNew;
        }
    }

}
