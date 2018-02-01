package com.example.wyxiang.answerapp.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import com.example.wyxiang.answerapp.R;

/**
 * Created by wyxiang on 08.07.17.
 */

public class MenuActivity extends AppCompatActivity{

    private Button btn_test, btn_history;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        btn_test = (Button) findViewById(R.id.menu_test);
        btn_history = (Button) findViewById(R.id.menu_history);

        btn_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuActivity.this,ChapterListActivity.class);
                startActivity(intent);
            }
        });
    }
}
