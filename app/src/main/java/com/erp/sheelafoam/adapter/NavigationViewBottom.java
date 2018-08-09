package com.erp.sheelafoam.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.erp.sheelafoam.BE.HeaderMenuBE;
import com.erp.sheelafoam.R;

import java.util.ArrayList;

/**
 * Created by priyanka.sharma on 11/18/2016.
 */

public class NavigationViewBottom extends RecyclerView.Adapter<NavigationViewBottom.EmployerJobsListedHolder> {

    Context context;
    ArrayList<HeaderMenuBE> base;

    public NavigationViewBottom(Context context, ArrayList<HeaderMenuBE> base) {
        this.context = context;
        this.base = base;
    }

    @Override
    public NavigationViewBottom.EmployerJobsListedHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.navigationview_bottom_row, parent, false);
        NavigationViewBottom.EmployerJobsListedHolder viewHolder = new NavigationViewBottom.EmployerJobsListedHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(EmployerJobsListedHolder holder, int position) {
        holder.txt_bottom.setText(base.get(position).getCHILD_MENU());

    }

    @Override
    public int getItemCount() {
        Log.d("Size-->", base.size() + "");
        return base.size();
    }

    public class EmployerJobsListedHolder extends RecyclerView.ViewHolder {

        TextView txt_bottom;
        ImageView icon;

        public EmployerJobsListedHolder(View itemView) {
            super(itemView);

            txt_bottom = (TextView) itemView.findViewById(R.id.txt_bottom);

        }
    }
}
