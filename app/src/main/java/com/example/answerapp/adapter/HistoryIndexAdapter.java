package com.example.answerapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.answerapp.R;
import com.example.answerapp.database.History;
import com.example.answerapp.util.Util;

import java.util.List;

public class HistoryIndexAdapter extends BaseAdapter {


    private List<History> histories;


    public HistoryIndexAdapter(List<History> histories){
        this.histories = histories;
    }

    @Override
    public int getCount() {
        return histories.size();
    }

    @Override
    public Object getItem(int position) {
        return histories.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        History history = histories.get(position);

        HistoryIndexAdapter.ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new HistoryIndexAdapter.ViewHolder();
            int width = Util.getScreenWidth(parent.getContext());
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_circle, parent, false);
            AbsListView.LayoutParams params = new AbsListView.LayoutParams(width / 9, width / 9);
            viewHolder.tv = convertView.findViewById(R.id.circle_img);
            viewHolder.tv.setLayoutParams(params);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (HistoryIndexAdapter.ViewHolder) convertView.getTag();
        }

        viewHolder.tv.setText((position + 1) + "");

        if (!history.getFinish()) {//判断该题是否已经作答
            viewHolder.tv.setBackgroundResource(R.drawable.circle_default);
        } else {
            if (history.getAnswerId() == history.getSelectedId()) {//判断选择的结果是否正确
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