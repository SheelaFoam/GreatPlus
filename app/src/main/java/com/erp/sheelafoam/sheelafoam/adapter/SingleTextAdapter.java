package com.erp.sheelafoam.sheelafoam.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.erp.sheelafoam.R;

import java.util.ArrayList;

public class SingleTextAdapter extends BaseAdapter
{
	



	ViewHolder holder1;
	ArrayList<String> _data;
	Activity mActivity;
	int flag;
	
	public SingleTextAdapter(Activity activity, ArrayList<String> data)
	{
		_data=data;
		mActivity=activity;
		
	}



	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return _data.size();
	}



	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return _data.get(position);
	}



	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	
	
	
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		LayoutInflater inflator=mActivity.getLayoutInflater();
		
		convertView=inflator.inflate(R.layout.single_item, null);
		

		
		holder1=new ViewHolder();
		
		holder1.text=(TextView)convertView.findViewById(R.id.textView1);
		
		
		

		convertView.setTag(holder1);
		
				
			holder1.text.setText(_data.get(position));
			
			
		
	
		return convertView;
	}
	
	

	   private class ViewHolder {
	        TextView text;
	      //  ImageView iv;

	    }


}

