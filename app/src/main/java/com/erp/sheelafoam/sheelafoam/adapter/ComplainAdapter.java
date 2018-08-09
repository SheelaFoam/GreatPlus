package com.erp.sheelafoam.sheelafoam.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.erp.sheelafoam.R;
import com.erp.sheelafoam.sheelafoam.entry.DealerBean;

import java.util.ArrayList;


public class ComplainAdapter extends BaseAdapter {

	ArrayList<DealerBean> _data;
	Context _c;
	TextView textview_purpose_name;
//	int flag;

	public ComplainAdapter (ArrayList<DealerBean> data, Context c){
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
			v = vi.inflate(R.layout.custom_complaint_layout,null);
		}

		
		TextView no, cmpDate, city, name, mobile, address, product, billDate, dealer, problem, pending;
		Button btnAddDetails;
		no=(TextView)v.findViewById(R.id.txtNo);
		cmpDate=(TextView)v.findViewById(R.id.txtDate);
		city=(TextView)v.findViewById(R.id.txtCity);
		name=(TextView)v.findViewById(R.id.txtName);
		mobile=(TextView)v.findViewById(R.id.txtMobile);
		address=(TextView)v.findViewById(R.id.txtAddress);
		product=(TextView)v.findViewById(R.id.txtProduct);
		billDate=(TextView)v.findViewById(R.id.txtBillDate);
		dealer=(TextView)v.findViewById(R.id.txtDealer);
		problem=(TextView)v.findViewById(R.id.txtProblem);
		pending=(TextView)v.findViewById(R.id.txtPending);
		btnAddDetails=(Button)v.findViewById(R.id.btnAddDetails);
		
		DealerBean entry=_data.get(position);
		
		/*if(flag==1)
		{
			textview_purpose_name.setTextColor(_c.getResources().getColor(R.color.text_color_black));
		}*/
		
	
		textview_purpose_name.setText(entry.getDealerName());
	
		
	
		return v;
	}
	


}