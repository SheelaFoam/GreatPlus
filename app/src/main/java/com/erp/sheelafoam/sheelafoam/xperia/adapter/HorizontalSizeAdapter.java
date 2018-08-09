package com.erp.sheelafoam.sheelafoam.xperia.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.erp.sheelafoam.R;
import com.erp.sheelafoam.sheelafoam.xperia.activity.RecyclerDemo;

import java.util.ArrayList;

/**
 * Created by Md Farhan on 15-July-16.
 */
public class HorizontalSizeAdapter extends ArrayAdapter<String>
{

    Activity activity;
    ArrayList<String> data;
    LayoutInflater inflater;
    SparseBooleanArray flag;
    
    RecyclerDemo rdm;
    
    public HorizontalSizeAdapter(Activity context, int resource, ArrayList objects, SparseBooleanArray aprs)
    {
        super(context, resource,objects);
        this.activity=(Activity)context;
        this.data=objects;
        this.flag=aprs;
        this.rdm=(RecyclerDemo)context;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent)
    {
      return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(final int position, View convertView, ViewGroup parent)
    {
    	  
          View row=inflater.inflate(R.layout.recycler_items, parent, false);
          TextView title=(TextView)row.findViewById(R.id.txtName);
          title.setText(data.get(position));
          if(flag.get(position))
          {
        	  title.setTextColor(Color.GREEN);
          }
          else
          {
        	  title.setTextColor(Color.BLACK);
          }
          
          title.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v)
			{
				//rdm.update(position);
				Toast.makeText(activity, data.get(position), Toast.LENGTH_SHORT).show();
			}
		});
          
          return row;
    }

}