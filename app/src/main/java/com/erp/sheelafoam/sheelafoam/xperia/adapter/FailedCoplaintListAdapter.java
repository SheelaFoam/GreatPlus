package com.erp.sheelafoam.sheelafoam.xperia.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.erp.sheelafoam.R;
import com.erp.sheelafoam.sheelafoam.xperia.activity.ComplaintNew;
import com.erp.sheelafoam.sheelafoam.xperia.dataholders.FailedComplaintHolder;

import java.util.ArrayList;

/**
 * Created by Md Farhan on 04-Oct-16.
 */
public class FailedCoplaintListAdapter extends BaseAdapter
{
    Context c;
    ArrayList<FailedComplaintHolder> list;
    LayoutInflater inflater;
    
    ComplaintNew complaintNew;
    
   public FailedCoplaintListAdapter(Context c, ArrayList<FailedComplaintHolder> list, ComplaintNew complaintNew)
   {
       this.c=c;
       this.list=list;
       this.complaintNew=complaintNew;
       this.inflater=(LayoutInflater.from(c));
   }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View itemView, ViewGroup parent)
    {
        itemView=inflater.inflate(R.layout.failed_images_custom_list,null);
        TextView compId,total;
        LinearLayout ll=(LinearLayout)itemView.findViewById(R.id.layout);
        compId=(TextView)itemView.findViewById(R.id.compId);
        total=(TextView)itemView.findViewById(R.id.totalFailed);
        compId.setText(list.get(position).getComId());
        total.setText(list.get(position).getTotalImages());
        ll.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				complaintNew.gotoDetails(position);			
			}
		});
        
        
        return itemView;
    }
}