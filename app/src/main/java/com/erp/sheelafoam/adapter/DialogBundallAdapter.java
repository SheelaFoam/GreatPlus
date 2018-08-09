package com.erp.sheelafoam.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.erp.sheelafoam.R;
import com.erp.sheelafoam.model.SerialNumModel;
import com.erp.sheelafoam.utils.CustomTypefaceCheckBox;

import java.util.List;

/**
 * Created by ${Bhringesh} on ${18-Jan-18}.
 */

public class DialogBundallAdapter extends BaseAdapter {
    int count = 0;
    private Context context;
    private List<SerialNumModel> arr;
    private LayoutInflater inflater;

    public DialogBundallAdapter(Context context,
                                List<SerialNumModel> arr) {
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
    public SerialNumModel getItem(int position) {
        return arr.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = inflater.inflate(R.layout.dialog_bundal_item, parent,
                    false);

        final CustomTypefaceCheckBox checkBox = (CustomTypefaceCheckBox) convertView.findViewById(R.id.checkbox_bundle);
        checkBox.setText(getItem(position).getsNo());

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (!getItem(position).isChecked()) {
                    if (count >= 2) {
                        Toast.makeText(context, "You can't select more than Two serial numbers!", Toast.LENGTH_SHORT).show();
                        checkBox.setChecked(false);
                    } else {
                        count = count + 1;
                        getItem(position).setChecked(true);
                        notifyDataSetChanged();
                    }
                } else {
                    count--;
                    getItem(position).setChecked(false);
                    notifyDataSetChanged();
                }
            }
        });

        return convertView;
    }

}
