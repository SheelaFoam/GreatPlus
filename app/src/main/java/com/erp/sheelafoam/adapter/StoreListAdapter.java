package com.erp.sheelafoam.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.erp.sheelafoam.R;
import com.erp.sheelafoam.models.StoreList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by twigz on 13/3/18.
 */

public class StoreListAdapter extends BaseAdapter {
    List<StoreList> storeLists = new ArrayList<>();
    Context context;

    public StoreListAdapter(Context context, List<StoreList> storeLists) {
        this.context = context;
        this.storeLists = storeLists;
    }

    @Override
    public int getCount() {
        return storeLists.size();
    }

    @Override
    public Object getItem(int i) {
        return storeLists.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.complaint_tye_row, viewGroup, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        StoreList store = (StoreList) getItem(i);
        viewHolder.complaintName.setText(store.getpARENT_CHANNEL_PARTNER_NAME());

        return view;
    }

    public class ViewHolder{
        TextView complaintName;

        public ViewHolder(View view){
            complaintName = (TextView) view.findViewById(R.id.complaint_name);
        }
    }
}


