package com.example.wyxiang.answerapp.Adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.wyxiang.answerapp.Database.Chapter;
import com.example.wyxiang.answerapp.R;

import java.util.List;

/**
 * Created by wyxiang on 18-2-1.
 */

public class ChapterAdapter extends ArrayAdapter<Chapter>{

    private int resourceId;

    public ChapterAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Chapter> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Chapter chapter = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
        TextView tv_chapter_title = view.findViewById(R.id.chapter_title);
        tv_chapter_title.setText(chapter.getTitle());
        return view;
    }
}
