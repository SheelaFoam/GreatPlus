package com.erp.sheelafoam.sheelafoam.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.erp.sheelafoam.R;
import com.erp.sheelafoam.models.SpinnerTitle;

import java.util.List;

public class ConsumerSpinnerAdapter extends BaseAdapter {
    Context context;
    LayoutInflater inflater;
    List<SpinnerTitle> list;
    Typeface font;

    public ConsumerSpinnerAdapter(Context context, List<SpinnerTitle> list) {
        this.context = context;
        this.list = list;
        //font = new CommonClass(context).getTextFont();
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public SpinnerTitle getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = inflater.inflate(R.layout.spinner_layout, parent, false);
        TextView tv_spinner = (TextView) convertView.findViewById(R.id.tv_spinner);
        tv_spinner.setTypeface(font);
        tv_spinner.setText(getItem(position).getPropertyName());

        return convertView;
    }
}