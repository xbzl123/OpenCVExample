package com.example.opencvexample;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

class SampleAdapter extends BaseAdapter {
    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView.setOnClickListener(v -> {});
        return null;
    }
}
