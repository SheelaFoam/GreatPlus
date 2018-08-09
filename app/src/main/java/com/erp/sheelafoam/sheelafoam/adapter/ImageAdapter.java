package com.erp.sheelafoam.sheelafoam.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.erp.sheelafoam.R;
import com.erp.sheelafoam.sheelafoam.entry.ImageEntry;
import com.erp.sheelafoam.sheelafoam.loadernew.ImageLoader;

import java.util.ArrayList;


public class ImageAdapter extends BaseAdapter {

	ArrayList<ImageEntry> _data;
	Activity _c;
	
	ImageLoader loader;

	public ImageAdapter (ArrayList<ImageEntry> data, Activity c){
		_data = data;
		_c = c;
		loader=new ImageLoader(_c);
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

	@SuppressLint("InflateParams")
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View v = convertView;
		if (v == null)
		{
			LayoutInflater vi = (LayoutInflater)_c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.item_list_image,null);
		}

		
		final ImageView imageview=(ImageView)v.findViewById(R.id.imageview);
		TextView textview_complaint_no=(TextView)v.findViewById(R.id.textview_complaint_no);
		TextView textview_user=(TextView)v.findViewById(R.id.textview_user);
		TextView textview_date=(TextView)v.findViewById(R.id.textview_date);
		
		
		ImageEntry entry=_data.get(position);
		String complaintNo=entry.getComplaintNo();
		String user_id=entry.getUser_id();
		//String =entry.getComplaintNo();
		textview_complaint_no.setText(complaintNo);   
		
		textview_user.setText(user_id);
		textview_date.setText("17-03-2016");
		if(entry.getImage()!=null)
		{
		imageview.setImageBitmap(entry.getImage()); 
		}
		else
		{
			String url=entry.getUrl();
			if(!url.equals(""))
			{
				Log.e("url of images", url);
				/*imageview.setTag(url);
				DownImageLoader loader=new DownImageLoader(_c); 
				
				loader.DisplayImage(url, _c, imageview);*/
				
				loader.DisplayImage(url, imageview);
			}
		}
		
		
//		imageview.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				//Toast.makeText(_c, "Hi i am working", Toast.LENGTH_LONG).show();
//				Bitmap bitmap = ((BitmapDrawable)imageview.getDrawable()).getBitmap();
//				ComplainDetails.getInstance().setBitmap(bitmap);
//				Intent i=new Intent(_c,ZoomActivity.class);
//				_c.startActivity(i);
//				
//				 _c.overridePendingTransition(R.anim.left_to_right,
//		                   R.anim.right_to_left);
//			}
//		});
		
		return v;
	}
	


}