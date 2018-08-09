package com.erp.sheelafoam.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.erp.sheelafoam.R;

import java.util.List;

/**
 * Created by admin on 26-02-2018.
 */

public class StoreAdapter extends BaseAdapter {
    private List<String> list;
    private LayoutInflater inflater;

    public StoreAdapter(Context context, List<String> list) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public String getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null)
            view = inflater.inflate(R.layout.item_store, viewGroup, false);
        TextView tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        tvTitle.setText(getItem(i));
        return view;
    }
}
