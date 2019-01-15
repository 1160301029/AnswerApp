package com.example.answerapp.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.answerapp.R;
import com.example.answerapp.database.Question;
import com.example.answerapp.util.Util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobBatch;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BatchResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListListener;

public class MainActivity extends AppCompatActivity {

    private String TAG = getClass().getName();

    private Button btn_test, btn_history;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bmob.initialize(this,"03ed672534583aab5914232995118da3");

        btn_test = findViewById(R.id.menu_test);
        btn_history = findViewById(R.id.menu_history);

        btn_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BmobQuery<Question> query = new BmobQuery<>();
                query.addWhereEqualTo("chapter", "Test");
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
//        btn_history.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(MainActivity.this,ChapterListActivity.class);
//                intent.putExtra("model","History");
//                startActivity(intent);
//            }
//        });
    }


    private void insert(){

        List<Question> questions = Util.getQuestions();

        List<BmobObject> objects = new ArrayList<>();
        for (Question q : questions){
            objects.add(q);
        }
        new BmobBatch().insertBatch(objects).doBatch(new QueryListListener<BatchResult>() {

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
                    Toast.makeText(MainActivity.this, "存储失败，请重试", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
