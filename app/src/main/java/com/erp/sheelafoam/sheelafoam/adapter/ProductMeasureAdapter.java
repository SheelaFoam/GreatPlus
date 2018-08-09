package com.erp.sheelafoam.sheelafoam.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.erp.sheelafoam.R;

import java.util.ArrayList;
import java.util.List;

public class ProductMeasureAdapter extends BaseAdapter {
    List<String> list = new ArrayList<>();
    private LayoutInflater inflater;

    public ProductMeasureAdapter(Context context, String item) {
        list.add(item);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
            view = inflater.inflate(R.layout.item_spinner, viewGroup, false);
        TextView tvItem = (TextView) view.findViewById(R.id.tvItem);
        tvItem.setText(getItem(i));
        return view;
    }
}
