package com.example.answerapp.ui;

import android.content.pm.PackageManager;
import android.os.Bundle;

import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.example.answerapp.R;

import com.example.answerapp.adapter.HistoryAdapter;
import com.example.answerapp.database.History;

import com.example.answerapp.fragment.HistoryFragment;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;


public class HistoryActivity extends AppCompatActivity {

    private String TAG = getClass().getName();

    private ViewPager viewPager;
    private LinearLayout mainBar;
    private TextView rightTxt, errorTxt, totalTxt;
    private LinearLayout timerLayout;
    private TextView submit;

    private List<History> histories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);


        viewPager = findViewById(R.id.main_viewpager);
        mainBar = findViewById(R.id.main_bar);
        rightTxt = findViewById(R.id.main_right_tx);
        errorTxt = findViewById(R.id.main_error_tx);
        totalTxt = findViewById(R.id.main_total_tx);
        timerLayout = findViewById(R.id.question_timer);
        submit = findViewById(R.id.question_submit);

        histories = (List<History>) getIntent().getSerializableExtra("history");

        int right = 0, wrong = 0;
        for (History history : histories){
            if (history.getFinish()){
                if(history.getAnswerId() == history.getSelectedId()){
                    right++;
                }else {
                    wrong++;
                }
            }
        }
        rightTxt.setText("" + right);
        errorTxt.setText("" + wrong);

        mainBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openBottomSheetDialog();
            }
        });

        viewPager.setAdapter(new HistoryActivity.MyPagerAdapter(getSupportFragmentManager()));
        viewPager.setOnPageChangeListener(new HistoryActivity.MyPagerChangeListner());
        timerLayout.setVisibility(View.GONE);
        submit.setVisibility(View.GONE);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }


    BottomSheetDialog bottomSheetDialog;

    public void openBottomSheetDialog() {
        bottomSheetDialog = new BottomSheetDialog(this);
        GridView gridView = new GridView(this);
        gridView.setNumColumns(6);
        gridView.setVerticalSpacing(10);
        gridView.setHorizontalSpacing(10);
        gridView.setBackgroundColor(0xffffffff);
        gridView.setAdapter(new HistoryAdapter(histories));
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
            totalTxt.setText((position + 1) + "/" + histories.size());
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
            HistoryFragment historyFragment = new HistoryFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("history", histories.get(position));
            historyFragment.setArguments(bundle);
            return historyFragment;
        }

        @Override
        public int getCount() {
            return histories.size();
        }
    }



}

