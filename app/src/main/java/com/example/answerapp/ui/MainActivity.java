package com.example.answerapp.ui;

import android.content.Intent;
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
import com.example.answerapp.database.Question;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bmob.initialize(this,"03ed672534583aab5914232995118da3");

        listView = findViewById(R.id.listview);
        toolbar = findViewById(R.id.toolbar);

        setToolbar();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String chapter = chapters.get(i);
                BmobQuery<Question> query = new BmobQuery<>();
                query.setLimit(500);
                query.addWhereEqualTo("chapter", chapter);
                query.findObjects(new FindListener<Question>() {
                    @Override
                    public void done(List<Question> object, BmobException e) {
                        if (e == null) {
                            Intent intent = new Intent(MainActivity.this, TestActivity.class);
                            intent.putExtra("question", (Serializable) object);
                            startActivity(intent);
                        } else {
                            Toast.makeText(MainActivity.this, "获取数据失败，请重试", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "done: 获取数据失败");
                        }
                    }
                });
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
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                            MainActivity.this,R.layout.item_chapter, chapters);
                    listView.setAdapter(adapter);
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
}
