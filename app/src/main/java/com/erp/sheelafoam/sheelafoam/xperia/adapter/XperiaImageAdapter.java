package com.erp.sheelafoam.sheelafoam.xperia.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.erp.sheelafoam.R;
import com.erp.sheelafoam.sheelafoam.loadernew.ImageLoader;

import java.io.File;
import java.util.ArrayList;


public class XperiaImageAdapter extends BaseAdapter {

	ArrayList<String> _data;
	Activity _c;
	
	ImageLoader loader;

	public XperiaImageAdapter (ArrayList<String> data, Activity c){
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
		File f;
		//Bitmap b;
		try {
			
			//String path=Environment.getExternalStorageDirectory()
			
			f = new File(Environment.getExternalStorageDirectory() + "/Pictures/SF/" + _data.get(position)+".jpg");
			//b = BitmapFactory.decodeFile(f.getAbsolutePath());
			//imageview.setImageBitmap(b);
			
			Glide.with(_c).load(f.getPath()).into(imageview);
			
			
			
		} catch (Exception e) {
		}		
		return v;
	}
	


}