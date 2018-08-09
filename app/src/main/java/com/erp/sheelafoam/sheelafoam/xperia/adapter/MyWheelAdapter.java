package com.erp.sheelafoam.sheelafoam.xperia.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.erp.sheelafoam.R;
import com.wx.wheelview.adapter.BaseWheelAdapter;

public class MyWheelAdapter extends BaseWheelAdapter<String>
{
	 private Context mContext;

	public MyWheelAdapter(Context mContext) {
		this.mContext = mContext;
	}

	@Override
	protected View bindView(int position, View convertView, ViewGroup arg2) {
		ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.wheel_items, null);
            viewHolder.textView = (TextView) convertView.findViewById(R.id.title);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.textView.setText(mList.get(position));
        return convertView;
	}
	
	 static class ViewHolder {
	        TextView textView;
	    }
	
}
