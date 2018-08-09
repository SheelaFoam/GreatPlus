package com.erp.sheelafoam.sheelafoam.xperia.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.erp.sheelafoam.R;
import com.erp.sheelafoam.sheelafoam.xperia.activity.MrpCalculation;
import com.erp.sheelafoam.sheelafoam.xperia.dataholders.ColorHolder;

import java.util.ArrayList;

/**
 * Created by Md Farhan on 15-July-16.
 */
public class ColorSpinnerAdapter extends ArrayAdapter<String>
{

    Activity activity;
    ArrayList<ColorHolder> data;
    LayoutInflater inflater;
    SparseBooleanArray flag;
    MrpCalculation mrpCalculation;
    public ColorSpinnerAdapter(Context context, int resource, ArrayList objects, SparseBooleanArray aprs)
    {
        super(context, resource,objects);
        this.activity=(Activity)context;
        this.data=objects;
        this.mrpCalculation=(MrpCalculation)context;
        this.flag=aprs;
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
            ColorHolder colordtls=data.get(position);
           // LayoutInflater inflater=activity.getLayoutInflater();
            View row=inflater.inflate(R.layout.drp_color_layout, parent, false);
            TextView title=(TextView)row.findViewById(R.id.txtcolorname);
            if(flag.get(position))
            {
          	  title.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.mat_size_bg_active));
            }
            else
            {
          	  title.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.mat_size_bg_inactive));
            }
            
            title.setOnClickListener(new OnClickListener() {
  			
  			@Override
  			public void onClick(View v)
  			{
  				try
  				{
  					//mrpCalculation.updateMatColor(position);
  					//Toast.makeText(activity, data.get(position).getColorName()+data.get(position).getColorCode(), Toast.LENGTH_SHORT).show();
  				}catch (Exception e) {
  					// TODO: handle exception
  				}
  				
  			}
  		});
            title.setText(colordtls.getColorName());
            //title.setTextColor(Color.parseColor(data.get(position).getColorCode().toString()));
            return row;
    }

}