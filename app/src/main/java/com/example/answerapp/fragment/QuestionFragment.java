package com.example.answerapp.fragment;

import android.graphics.Color;
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
import android.widget.Toast;

import com.example.answerapp.R;
import com.example.answerapp.database.Question;
import com.example.answerapp.util.Util;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.fragment.app.Fragment;

public class QuestionFragment extends Fragment {

    private String TAG = getClass().getName();

    private Question question;

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
        question = (Question) getArguments().getSerializable("question");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_question, container, false);
        Log.d(TAG, "onCreateView: 重新生成布局");
        initView();
//        initData();
//        initListner();
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
            initListner();
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
        title.setText(question.getTitle());
        explainTxt.setText(question.getAnswer());

        answerId = question.getAnswerId();


        switch (question.getType()){
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

    private void initListner() {
        options.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup rd, int id) {
                for (int i = 0; i < listRadio.size(); i++) {
                    AppCompatRadioButton radioButton = listRadio.get(i);
                    //遍历查找找到当前点击的item
                    if (radioButton.getId() == id) {
                        if (i == answerId) {//判断选择是否是正确答案
                            radioButton.setTextColor(getContext().getResources().getColor(R.color.right));
                            question.setSelectedId(i);
                            setRightDrable(radioButton);//设置样式
//                            QuestionActivity.nextQuestion();
                        } else {//选择的是错误答案
                            radioButton.setTextColor(getContext().getResources().getColor(R.color.error));
                            setErrorDrable(radioButton);//设置样式

                            listRadio.get(answerId).setTextColor(getContext().getResources().getColor(R.color.right));
                            setRightDrable(listRadio.get(answerId));
                            question.setSelectedId(i);
                            setErrorDrable(radioButton);
                        }
//                        result.setRightAnswer(answer);//设置选对题目的标识
                        question.setFinish(true);//设置完成了答题
                        radioButtonClickEnable();//设置不可点击

//                        QuestionActivity.upDataRightAndError();//更新MainActivity
                        break;
                    }
                }
            }
        });

        explainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                explainTxt.setVisibility(View.VISIBLE);
            }
        });
    }

    private void initSigleSelection(){
        List<String> options = question.getOptions();

        for (String option : options){
            addRadioButtonView(option);
        }

        if (question.getFinish()) {//判断当前题目是否已经答题答过了，如果是的话，就恢复答题数据，并且设置不可点击
            radioButtonClickEnable();
            if (question.getSelectedId() != question.getAnswerId()) {//如果没有选择到正确答案的话，就要显示错误答案，否则不显示
                AppCompatRadioButton radio = listRadio.get(question.getSelectedId());
                radio.setTextColor(getContext().getResources().getColor(R.color.error));
                setErrorDrable(radio);
            }
            AppCompatRadioButton radio = listRadio.get(question.getAnswerId());
            radio.setTextColor(getContext().getResources().getColor(R.color.right));
            setRightDrable(radio);
        }
    }

    private void initMultipleSelection(){

        List<String> options = question.getOptions();

        for (String option : options){
            addCheckBoxView(option);
        }

        if (question.getFinish()) {
            Log.d(TAG, "initMultipleSelection: 问题已回答");
            checkBoxClickEnable();
            //遍历用户的选择
            List<Integer> selectedList = Util.getResultList(question.getSelectedId());
            List<Integer> answerList = Util.getResultList(question.getAnswerId());

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



        } else {
            //添加一个确定按钮
            AppCompatButton appCompatButton = new AppCompatButton(getContext());
            appCompatButton.setText("确定");
            appCompatButton.setBackgroundColor(getContext().getResources().getColor(R.color.colorPrimary));
            appCompatButton.setTextColor(Color.parseColor("#ffffff"));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(20, 30, 20, 20);
            appCompatButton.setLayoutParams(params);
            checkLayout.addView(appCompatButton);
            appCompatButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    doHandle();
                }
            });
        }
    }

    private void doHandle() {
        List<Integer> selectedList = new ArrayList<>();

        for (int i = 0; i < listCheck.size(); i++){
            if (listCheck.get(i).isChecked()){
                selectedList.add(i);
            }
        }

        if (selectedList.size() == 0){
            Toast.makeText(getContext(), "还未选择选项", Toast.LENGTH_SHORT).show();
            return;
        }
        Log.d(TAG, "doHandle: 选项有" + selectedList);
        selectedId = Util.getResultNum(selectedList);
        Log.d(TAG, "doHandle: 处理之后为" + selectedId);

        question.setSelectedId(selectedId);

        List<Integer> answerList = Util.getResultList(answerId);

        // 显示正确答案
        for (int i : answerList){
            listCheck.get(i).setTextColor(getContext().getResources().getColor(R.color.right));
        }

        // 显示所选的错误答案
        for (int i : selectedList){
            if (!answerList.contains(i)){
                listCheck.get(i).setTextColor(getContext().getResources().getColor(R.color.error));
            }
        }

        if (answerId == selectedId){
            explainTxt.setText("答案正确");
        }else {
            explainTxt.setText("答案错误");
        }

        question.setFinish(true);



    }


    private void initJudge(){

        List<String> options = question.getOptions();

        for (String option : options){
            addRadioButtonView(option);
        }

        if (question.getFinish()) {//判断当前题目是否已经答题答过了，如果是的话，就恢复答题数据，并且设置不可点击
            radioButtonClickEnable();
            if (question.getSelectedId() != question.getAnswerId()) {//如果没有选择到正确答案的话，就要显示错误答案，否则不显示
                AppCompatRadioButton radio = listRadio.get(question.getSelectedId());
                radio.setTextColor(getContext().getResources().getColor(R.color.error));
                setErrorDrable(radio);
            }
            AppCompatRadioButton radio = listRadio.get(question.getAnswerId());
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
//        for (AppCompatCheckBox checkbos : listCheck) {
//            checkbos.setClickable(false);
//        }
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
