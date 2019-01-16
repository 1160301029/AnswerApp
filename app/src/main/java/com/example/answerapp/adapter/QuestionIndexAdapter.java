package com.example.answerapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.answerapp.R;
import com.example.answerapp.database.Question;
import com.example.answerapp.util.Util;

import java.util.List;

public class QuestionIndexAdapter extends BaseAdapter {


    private List<Question> questions;


    public QuestionIndexAdapter(List<Question> questions){
        this.questions = questions;
    }

    @Override
    public int getCount() {
        return questions.size();
    }

    @Override
    public Object getItem(int position) {
        return questions.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Question question = questions.get(position);

        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            int width = Util.getScreenWidth(parent.getContext());
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_circle, parent, false);
            AbsListView.LayoutParams params = new AbsListView.LayoutParams(width / 9, width / 9);
            viewHolder.tv = (TextView) convertView.findViewById(R.id.circle_img);
            viewHolder.tv.setLayoutParams(params);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tv.setText((position + 1) + "");

        if (!question.getFinish()) {//判断该题是否已经作答
            viewHolder.tv.setBackgroundResource(R.drawable.circle_default);
        } else {
            if (question.getAnswerId() == question.getSelectedId()) {//判断选择的结果是否正确
                viewHolder.tv.setBackgroundResource(R.drawable.circle_right);
            } else {
                viewHolder.tv.setBackgroundResource(R.drawable.circle_error);
            }
        }


        return convertView;
    }

    class ViewHolder {
        TextView tv;
    }
}