package com.erp.sheelafoam.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.erp.sheelafoam.R;

import java.util.ArrayList;

/**
 * Created by E5956 on 4/11/2018.
 */

public class SpinAdapter extends BaseAdapter {

    Context context;
    ArrayList<String> list;
    LayoutInflater inflater;

    public SpinAdapter(Context context, ArrayList<String> list) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public String getItem(int position) {
        // TODO Auto-generated method stub
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = inflater.inflate(R.layout.spinner_lyt, null);
        TextView tv_spinner = (TextView) convertView.findViewById(R.id.spinner_text);
        tv_spinner.setText(getItem(position));
        return convertView;
    }
}
