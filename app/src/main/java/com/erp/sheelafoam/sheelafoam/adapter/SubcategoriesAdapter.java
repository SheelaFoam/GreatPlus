package com.erp.sheelafoam.sheelafoam.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.erp.sheelafoam.R;
import com.erp.sheelafoam.sheelafoam.entry.SubCategories;
import com.erp.sheelafoam.sheelafoam.loader.DownImageLoader;

import java.util.List;


public class SubcategoriesAdapter extends BaseAdapter {

	List<SubCategories> _data;
	Context _c;
	TextView textview_purpose_name;
	

	
	
	public SubcategoriesAdapter (List<SubCategories> data, Context c ){
		_data = data;
		_c = c;
	
		// addressList = new ArrayList<MessageDetails>();
		// addressList.addAll(_data);
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

	 private class ViewHolder {
	        ImageView imageview_icon;
	        TextView textview_category_name;
	        
	    }
		
	
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		View v = convertView;
		if (v == null)
		{
			LayoutInflater vi = (LayoutInflater)_c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.item_gridview,null);
			holder = new ViewHolder();
			holder.textview_category_name=(TextView)v.findViewById(R.id.textview_category_name);
			holder.imageview_icon=(ImageView)v.findViewById(R.id.imageview_icon);
			v.setTag(holder);
		}

		else
		{
			 holder = (ViewHolder) convertView.getTag();
		}
		
		
		SubCategories entry=_data.get(position);
		String url=entry.getUrl();
		
		
		holder.textview_category_name.setText(entry.getName());
	
		/*ImageLoader loader=new ImageLoader(_c);
		loader.DisplayImage(url,R.drawable.profile_default_image, imageview_icon);*/
		
		DownImageLoader loader=new DownImageLoader(_c);
		//ImageLoader loader=new ImageLoader(_context);
		//loader.DisplayImage(url,R.drawable.profile_default_image, imageview_icon);
		holder.imageview_icon.setTag(url);
		loader.DisplayImage(url, _c, holder.imageview_icon);
	
		return v;
	}
	


}