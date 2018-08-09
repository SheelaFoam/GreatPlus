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
import com.erp.sheelafoam.sheelafoam.xperia.dataholders.ThiknessHolder;

import java.util.ArrayList;

/**
 * Created by Md Farhan on 15-July-16.
 */
public class ThiknessSpinnerAdapter extends ArrayAdapter<String>
{

    Activity activity;
    ArrayList<ThiknessHolder> data;
    LayoutInflater inflater;
    SparseBooleanArray flag;
    MrpCalculation mrpCalculation;
    public ThiknessSpinnerAdapter(Context context, int resource, ArrayList objects, SparseBooleanArray aprs)
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
    	  ThiknessHolder thholder=data.get(position);
          View row=inflater.inflate(R.layout.drp_thikness_layout, parent, false);
          TextView title=(TextView)row.findViewById(R.id.txtthikness);
         
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
					//mrpCalculation.updateMatThikness(position);
					//Toast.makeText(activity, data.get(position).getThikness(), Toast.LENGTH_SHORT).show();
				}catch (Exception e) {
					// TODO: handle exception
				}
				
			}
		});
          
           try
           {        		  
               double T;
               T= Double.parseDouble(thholder.getThikness())/25.4;
               String Ll= String.valueOf(T);
               title.setText(Ll.substring(0, Ll.lastIndexOf(".")));
           }catch(Exception e){}
          
          return row;
    }

}