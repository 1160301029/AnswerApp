package com.example.answerapp.fragment;

import android.graphics.drawable.VectorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.answerapp.R;
import com.example.answerapp.database.History;
import com.example.answerapp.util.Util;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.fragment.app.Fragment;

public class HistoryFragment extends Fragment {

    private String TAG = getClass().getName();

    private History history;

    private View rootView;

    private TextView title;
    private RadioGroup options;
    private LinearLayout explainLayout;
    private TextView explainTxt;
    private LinearLayout checkLayout;

    //Fragment的View加载完毕的标记
    private boolean isViewCreated = false;

    //Fragment对用户可见的标记
    private boolean isUIVisible = false;


    List<AppCompatRadioButton> listRadio;
    List<AppCompatCheckBox> listCheck;
    int selectedId;
    int answerId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        history = (History) getArguments().getSerializable("history");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_question, container, false);
        Log.d(TAG, "onCreateView: 重新生成布局");
        initView();
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isViewCreated = true;
        lazyLoad();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        //isVisibleToUser这个boolean值表示:该Fragment的UI 用户是否可见
        if (isVisibleToUser) {
            isUIVisible = true;
            lazyLoad();
        } else {
            isUIVisible = false;
        }
    }

    private void lazyLoad() {
        //这里进行双重标记判断,是因为setUserVisibleHint会多次回调,并且会在onCreateView执行前回调,必须确保onCreateView加载完毕且页面可见,才加载数据
        if (isViewCreated && isUIVisible) {
            Log.d(TAG, "lazyLoad: 真正加载布局");
            initData();
            //数据加载完毕,恢复标记,防止重复加载
            isViewCreated = false;
            isUIVisible = false;

        }
    }

    private void initView() {
        listCheck = new ArrayList<>();
        listRadio = new ArrayList<>();
        title = rootView.findViewById(R.id.title);
        options = rootView.findViewById(R.id.option_group);
        explainLayout = rootView.findViewById(R.id.explain_layout);
        explainTxt = rootView.findViewById(R.id.explain_txt);
        checkLayout = rootView.findViewById(R.id.check_layout);
    }

    private void initData() {
        title.setText(history.getTitle());
        explainTxt.setText(history.getAnswer());

        answerId = history.getAnswerId();

        switch (history.getType()){
            case "Single":
                initSigleSelection();
                break;
            case "Multi":
                initMultipleSelection();
                break;
            case "Judge":
                initJudge();
                break;
        }
    }


    private void initSigleSelection(){
        List<String> options = history.getOptions();
        for (String option : options){
            addRadioButtonView(option);
        }
        radioButtonClickEnable();
        if (history.getFinish()) {//判断当前题目是否已经答题答过了，如果是的话，就恢复答题数据，并且设置不可点击
            if (history.getSelectedId() != history.getAnswerId()) {//如果没有选择到正确答案的话，就要显示错误答案，否则不显示
                AppCompatRadioButton radio = listRadio.get(history.getSelectedId());
                radio.setTextColor(getContext().getResources().getColor(R.color.error));
                setErrorDrable(radio);
            }
            AppCompatRadioButton radio = listRadio.get(history.getAnswerId());
            radio.setTextColor(getContext().getResources().getColor(R.color.right));
            setRightDrable(radio);
        }
    }

    private void initMultipleSelection(){

        List<String> options = history.getOptions();

        for (String option : options){
            addCheckBoxView(option);
        }
        checkBoxClickEnable();
        if (history.getFinish()) {
            Log.d(TAG, "initMultipleSelection: 问题已回答");

            //遍历用户的选择
            List<Integer> selectedList = Util.getResultList(history.getSelectedId());
            List<Integer> answerList = Util.getResultList(history.getAnswerId());

            for (int i = 0; i < answerList.size(); i++) {
                int a = answerList.get(i);//拿到答题的标号
                listCheck.get(a).setTextColor(getContext().getResources().getColor(R.color.right));
            }
            Log.d(TAG, "initMultipleSelection: 选择的题号有" + selectedList);
            for (int i = 0; i < selectedList.size(); i++) {
                int a = selectedList.get(i);//拿到答题的标号
                if (answerList.contains(a)){
                    listCheck.get(a).setTextColor(getContext().getResources().getColor(R.color.right));
                    listCheck.get(a).setChecked(true);
                }else {
                    listCheck.get(a).setTextColor(getContext().getResources().getColor(R.color.error));
                    listCheck.get(a).setChecked(true);
                }
            }
        }
    }

    private void initJudge(){

        List<String> options = history.getOptions();

        for (String option : options){
            addRadioButtonView(option);
        }
        radioButtonClickEnable();
        if (history.getFinish()) {//判断当前题目是否已经答题答过了，如果是的话，就恢复答题数据，并且设置不可点击

            if (history.getSelectedId() != history.getAnswerId()) {//如果没有选择到正确答案的话，就要显示错误答案，否则不显示
                AppCompatRadioButton radio = listRadio.get(history.getSelectedId());
                radio.setTextColor(getContext().getResources().getColor(R.color.error));
                setErrorDrable(radio);
            }
            AppCompatRadioButton radio = listRadio.get(history.getAnswerId());
            radio.setTextColor(getContext().getResources().getColor(R.color.right));
            setRightDrable(radio);
        }
    }

    private void addCheckBoxView(String question) {
        AppCompatCheckBox checkBox = new AppCompatCheckBox(getContext());
        checkBox.setText(question);
        checkBox.setTextSize(20);
//        checkBox.setTextColor(getContext().getResources().getColor(R.color.black333));
        RadioGroup.LayoutParams param = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.WRAP_CONTENT);
        param.setMargins(10, 10, 10, 10);
        checkBox.setLayoutParams(param);
        checkLayout.addView(checkBox);
        listCheck.add(checkBox);
    }


    private void addRadioButtonView(String question) {
        AppCompatRadioButton appCompatRadioButton = new AppCompatRadioButton(getContext());
        appCompatRadioButton.setText(question);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            VectorDrawable bitmapDrawable = (VectorDrawable) getResources().getDrawable(R.drawable.ic_default);
            appCompatRadioButton.setButtonDrawable(bitmapDrawable);
        }
        appCompatRadioButton.setTextSize(20);
        appCompatRadioButton.setTextColor(getContext().getResources().getColor(R.color.black333));
        RadioGroup.LayoutParams param = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.WRAP_CONTENT);
        param.setMargins(10, 10, 10, 10);
        appCompatRadioButton.setLayoutParams(param);
        options.addView(appCompatRadioButton);
        listRadio.add(appCompatRadioButton);
    }

    private void radioButtonClickEnable() {
        for (AppCompatRadioButton radioButton : listRadio) {
            radioButton.setClickable(false);
        }
    }

    private void checkBoxClickEnable() {
        for (AppCompatCheckBox checkbos : listCheck) {
            checkbos.setClickable(false);
        }
        checkLayout.setClickable(false);
    }

    private void setErrorDrable(AppCompatRadioButton radioButton) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            VectorDrawable error = (VectorDrawable) getResources().getDrawable(R.drawable.ic_error);
            radioButton.setButtonDrawable(error);
        }
    }


    private void setRightDrable(AppCompatRadioButton radioButton) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            VectorDrawable right = (VectorDrawable) getResources().getDrawable(R.drawable.ic_right);
            radioButton.setButtonDrawable(right);
        }
    }
}

