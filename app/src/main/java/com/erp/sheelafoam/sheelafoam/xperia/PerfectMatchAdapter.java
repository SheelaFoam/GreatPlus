package com.erp.sheelafoam.sheelafoam.xperia;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import com.erp.sheelafoam.R;
import com.erp.sheelafoam.sheelafoam.xperia.activity.PerfectMatchOrder;

import java.util.List;

/**
 * Created by sudhirharit on 20/11/17.
 */

public class PerfectMatchAdapter extends BaseAdapter {

    Context context;
    List<PerfectMatchModel> perfectMatches;
    public PerfectMatchAdapter(Context context, List<PerfectMatchModel> perfectMatches){
        this.perfectMatches = perfectMatches;
        this.context = context;
    }

    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public Object getItem(int i) {
        return perfectMatches.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.perfect_match_row, viewGroup, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(context, PerfectMatchOrder.class);
//                context.startActivity(intent);
            }
        });

        return convertView;
    }


    public class ViewHolder{
        Button placeOrder;
        public ViewHolder(View view){
            placeOrder = (Button) view.findViewById(R.id.place_order);
        }
    }
}
