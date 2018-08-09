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
import com.erp.sheelafoam.sheelafoam.xperia.dataholders.LBHolder;
import com.erp.sheelafoam.sheelafoam.xperia.dataholders.LBHolderInch;

import java.util.ArrayList;

/**
 * Created by Md Farhan on 15-July-16.
 */
public class SizeSpinnerAdapter extends ArrayAdapter<String>
{

    Activity activity;
    ArrayList<LBHolder> data;
    ArrayList<LBHolderInch> dataInch;
    LayoutInflater inflater;
    SparseBooleanArray flag;
    MrpCalculation mrpCalculation;
    
    String sizeType;
    
    public SizeSpinnerAdapter(Activity context, int resource, ArrayList objects, SparseBooleanArray aprs, String sizeType)
    {
        super(context, resource,objects);
        this.activity=(Activity)context;
        if(sizeType.equalsIgnoreCase("MM"))
        {      	 
        this.data=objects;
        }
        else if(sizeType.equalsIgnoreCase("INCH"))
        {
        	this.dataInch=objects;
        }
        this.mrpCalculation=(MrpCalculation)context;
        this.flag=aprs;
        this.sizeType=sizeType;
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
          View row=inflater.inflate(R.layout.drp_size_layout, parent, false);
          TextView title=(TextView)row.findViewById(R.id.txtsize);
          
          if(flag.get(position))
          {
        	  title.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.mat_size_bg_active));
          }
          else
          {
        	  title.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.mat_size_bg_inactive));
          }
          
          if(sizeType.equalsIgnoreCase("MM"))
          {
        	  title.setText(data.get(position).getLength() + " x " + data.get(position).getBredth());
          }
          else if(sizeType.equalsIgnoreCase("INCH"))
          {
        	  title.setText(dataInch.get(position).getLengthInch() + " x " + dataInch.get(position).getBredthInch());
          }
          
          title.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v)
			{
				try
				{
					mrpCalculation.updateMatSize(position);
					//Toast.makeText(activity, data.get(position).getLength(), Toast.LENGTH_SHORT).show();
				}catch (Exception e) {
					// TODO: handle exception
				}
				
			}
		});
                      	  
         /*  try
           {
        	   double L,B;
               L=Double.parseDouble(lbholder.getLength())/25.4;
               B=Double.parseDouble(lbholder.getBredth())/25.4;  
               String Ll=String.valueOf(L);
               String Bb=String.valueOf(B);
               title.setText(Ll.substring(0, Ll.lastIndexOf(".")) + " x " + Bb.substring(0, Bb.lastIndexOf(".")));
           }catch (Exception e) {}*/
          
          return row;
    }

}