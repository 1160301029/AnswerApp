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
import com.example.answerapp.database.HistoryList;
import com.example.answerapp.database.Question;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class HistoryListActivity extends AppCompatActivity {

    private String TAG = getClass().getName();

    private ListView listView;

    private HashSet<String> historySet;

    private List<String> histories;

    private String usrId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_list);

        Bmob.initialize(this,"03ed672534583aab5914232995118da3");

        listView = findViewById(R.id.listview);
        SharedPreferences preferences = getSharedPreferences("data",MODE_PRIVATE);
        usrId = preferences.getString("userMail","");

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String time = histories.get(i);
                BmobQuery<History> query = new BmobQuery<>();
                query.setLimit(500);
                query.addWhereEqualTo("finishTime", time);
                query.findObjects(new FindListener<History>() {
                    @Override
                    public void done(List<History> object, BmobException e) {
                        if (e == null) {
                            Intent intent = new Intent(HistoryListActivity.this, HistoryActivity.class);
                            intent.putExtra("history", (Serializable) object);
                            startActivity(intent);
                        } else {
                            Toast.makeText(HistoryListActivity.this, "获取数据失败，请重试", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "done: 获取数据失败");
                        }
                    }
                });
            }
        });

        BmobQuery<HistoryList> query = new BmobQuery<>();
        query.setLimit(500);
        query.addWhereEqualTo("usrId", usrId);
        query.findObjects(new FindListener<HistoryList>() {
            @Override
            public void done(List<HistoryList> objects, BmobException e) {
                if (e == null) {
                    historySet = new HashSet<>();
                    for (HistoryList history : objects){
                        historySet.add(history.getFinishTime());
                    }

                    histories = new ArrayList<>(historySet);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                            HistoryListActivity.this,R.layout.item_chapter, histories);
                    listView.setAdapter(adapter);
                } else {
                    Toast.makeText(HistoryListActivity.this, "获取数据失败，请重试", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "done: 获取数据失败");
                }
            }
        });


    }


}

