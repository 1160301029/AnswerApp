package com.example.wyxiang.answerapp.UI;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.wyxiang.answerapp.Adapter.ChapterAdapter;
import com.example.wyxiang.answerapp.Database.Chapter;
import com.example.wyxiang.answerapp.Database.Question;
import com.example.wyxiang.answerapp.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by wyxiang on 18-2-1.
 */

public class ChapterListActivity extends AppCompatActivity{

    private List<Chapter> chapterList = new ArrayList<>();
    private String TAG = "ChapterListActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter_list);

        final ListView listView = (ListView) findViewById(R.id.listView);
        //加个加载进度条，使得网络不好时显示不出来也不那么突兀
        final ProgressDialog progress = new ProgressDialog(ChapterListActivity.this);
        progress.setMessage("正在加载中...");
        progress.setCanceledOnTouchOutside(false);
        progress.show();
        //TODO 离线数据库？
        BmobQuery<Chapter> query = new BmobQuery<>();
        query.findObjects(new FindListener<Chapter>() {
            @Override
            public void done(List<Chapter> list, BmobException e) {
                if (e==null && list.size() > 0){
                    progress.hide();
                    chapterList = list;
                    ChapterAdapter adapter = new ChapterAdapter(ChapterListActivity.this,R.layout.item_chapter_list,chapterList);
                    listView.setAdapter(adapter);
                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String title = chapterList.get(i).getTitle();
                Log.d(TAG, "onItemClick: " + title);
                if (!title.equals("")){
                    BmobQuery<Question> questionQuery = new BmobQuery<>();
                    questionQuery.addWhereEqualTo("chapter",title);
                    questionQuery.findObjects(new FindListener<Question>() {
                        @Override
                        public void done(List<Question> list, BmobException e) {
                            if (e==null && list.size() > 0){
                                Log.d(TAG, "done: " + list.size());
                                Intent intent = new Intent(ChapterListActivity.this,TestActivity.class);
                                intent.putExtra("list", (Serializable) list);
                                startActivity(intent);
                            }
                        }
                    });
                }
            }
        });
    }
}
