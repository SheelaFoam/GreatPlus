package com.erp.sheelafoam.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.Toast;

import com.erp.sheelafoam.R;
import com.erp.sheelafoam.model.SerialNumModel;
import com.erp.sheelafoam.prefrences.SheelaSharedPreference;
import com.erp.sheelafoam.utils.CustomTypefaceCheckBox;
import com.erp.sheelafoam.utils.CustomTypefaceTextView;

import java.util.List;

public class RadioFootFallAdapter extends BaseAdapter {
    int count = 0;
    private Context context;
    private List<String> arr;
    private LayoutInflater inflater;
    public RadioFootFallAdapter(Context context,
                                List<String> arr) {
        this.context = context;
        this.arr = arr;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return arr.size();
    }

    @Override
    public Object getItem(int position) {
        return arr.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = inflater.inflate(R.layout.custome_redio_dialog, parent,
                    false);
        RadioButton radio_btn_custom= (RadioButton) convertView.findViewById(R.id.radio_btn_custom);

        CustomTypefaceTextView tv_radio= (CustomTypefaceTextView) convertView.findViewById(R.id.tv_radio);
        tv_radio.setText(arr.get(position).toString());
        return convertView;
    }
}