package com.erp.sheelafoam.sheelafoam.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.erp.sheelafoam.R;
import com.erp.sheelafoam.sheelafoam.entry.LocationBean;

import java.util.ArrayList;


public class LocationListAdapter extends BaseAdapter {

	ArrayList<LocationBean> _data;
	Context _c;
	TextView textview_purpose_name;
//	int flag;

	public LocationListAdapter (ArrayList<LocationBean> data, Context c){
		_data = data;
		_c = c;
	//	this.flag=flag;

	}

	public int getCount() {
		// TODO Auto-generated method stub
		return _data.size();
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return _data.get(position);
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View v = convertView;
		if (v == null)
		{
			LayoutInflater vi = (LayoutInflater)_c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.item_dealer,null);
		}

		
		TextView textview_purpose_name=(TextView)v.findViewById(R.id.textview_purpose_name);
		
		LocationBean entry=_data.get(position);
		
		/*if(flag==1)
		{
			textview_purpose_name.setTextColor(_c.getResources().getColor(R.color.text_color_black));
		}*/
		
		
		
		textview_purpose_name.setText(entry.getLocation_name());
	
		
	
		return v;
	}
	


}