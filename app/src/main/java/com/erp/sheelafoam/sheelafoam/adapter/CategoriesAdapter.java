package com.erp.sheelafoam.sheelafoam.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.erp.sheelafoam.R;
import com.erp.sheelafoam.sheelafoam.entry.CategoryEntry;
import com.erp.sheelafoam.sheelafoam.entry.SubCategories;
import com.erp.sheelafoam.sheelafoam.loader.DownImageLoader;

import java.util.HashMap;
import java.util.List;

public class CategoriesAdapter extends BaseAdapter {
	
	
	private Context _context;
	private List<CategoryEntry> _listDataHeader; // header titles
	// child data in format of header title, child title
	private HashMap<String, List<SubCategories>> _listDataChild;

	public CategoriesAdapter(Context context, List<CategoryEntry> listDataHeader,
                             HashMap<String, List<SubCategories>> listChildData) {
		this._context = context;
		this._listDataHeader = listDataHeader;
		this._listDataChild = listChildData;
	}


	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return _listDataHeader.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return _listDataHeader.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	/*private view holder class*/
    private class ViewHolder {
        ImageView imageview_icon;
        TextView textview_category_name;
        
    }
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		View v = convertView;
		if (v == null)
		{
			LayoutInflater vi = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.item_gridview,null);
			holder=new ViewHolder();
			 holder.textview_category_name=(TextView)v.findViewById(R.id.textview_category_name);
			holder. imageview_icon=(ImageView)v.findViewById(R.id.imageview_icon);
			v.setTag(holder);
		}

		else
		{
			 holder = (ViewHolder) convertView.getTag();
		}
		
		String category_name=_listDataHeader.get(position).getName();
		String url=_listDataHeader.get(position).getUrl();
		
		
		
		holder.textview_category_name.setText(category_name); 
		if(category_name.equals("Support"))
		{
			holder.imageview_icon.setBackgroundResource(R.drawable.support);
		}
		
	
		else if(category_name.equalsIgnoreCase("Mrp Calculator"))
		{
			holder.imageview_icon.setBackgroundResource(R.drawable.mrp_calc);
		}
		else
		{
			DownImageLoader loader=new DownImageLoader(_context);
			//ImageLoader loader=new ImageLoader(_context);
			//loader.DisplayImage(url,R.drawable.profile_default_image, imageview_icon);
			holder.imageview_icon.setTag(url);
			loader.DisplayImage(url, _context, holder.imageview_icon);
		}
		
		return v;
	}

}
