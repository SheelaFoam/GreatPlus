package com.erp.sheelafoam.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.erp.sheelafoam.R;
import com.erp.sheelafoam.model.NavigationViewModel_EMAIL;

import java.util.ArrayList;

/**
 * Created by priyanka.sharma on 11/18/2016.
 */

public class NavigationViewTopEMAIL extends RecyclerView.Adapter<NavigationViewTopEMAIL.EmployerJobsListedHolder> {

    Context context;
    ArrayList<NavigationViewModel_EMAIL> base;

    public NavigationViewTopEMAIL(Context context, ArrayList<NavigationViewModel_EMAIL> base) {
        this.base = base;
        this.context = context;
    }

    @Override
    public EmployerJobsListedHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.navigationview_top_row, parent, false);
        EmployerJobsListedHolder viewHolder = new EmployerJobsListedHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(EmployerJobsListedHolder holder, int position) {
        holder.txt.setText(base.get(position).getDrawer_list_textfor_EMAIL());

        int CatagoryImage_Url = base.get(position).getDrawer_list_iconsfor_EMAIL();
        holder.icon.setImageResource(CatagoryImage_Url);

        if (base.size() - 1 == position) {
            holder.navigation_top_divider.setVisibility(View.GONE);
        } else {
            holder.navigation_top_divider.setVisibility(View.VISIBLE);
        }
        //Picasso.with(context).load(CatagoryImage_Url).into(holder.icon);
    }

    @Override
    public int getItemCount() {
        Log.d("Size-->", base.size() + "");
        return base.size();
    }

    public class EmployerJobsListedHolder extends RecyclerView.ViewHolder {

        TextView txt, navigation_top_divider;
        ImageView icon;

        public EmployerJobsListedHolder(View itemView) {
            super(itemView);

            txt = (TextView) itemView.findViewById(R.id.txt);
            icon = (ImageView) itemView.findViewById(R.id.icon);
            navigation_top_divider = (TextView) itemView.findViewById(R.id.navigation_top_divider);
        }
    }
}