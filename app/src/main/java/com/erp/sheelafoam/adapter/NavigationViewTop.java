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
import com.erp.sheelafoam.model.NavigationViewModel;

import java.util.ArrayList;

/**
 * Created by priyanka.sharma on 11/18/2016.
 */

public class NavigationViewTop extends RecyclerView.Adapter<NavigationViewTop.EmployerJobsListedHolder> {

    Context context;
    ArrayList<NavigationViewModel> base;

    public NavigationViewTop(Context context, ArrayList<NavigationViewModel> base) {
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
        holder.txt.setText(base.get(position).getDrawer_list_text());

        int CatagoryImage_Url = base.get(position).getDrawer_list_icons();
        holder.icon.setImageResource(CatagoryImage_Url);
if(base.get(position).equals(position)) {
    holder.txt.setAllCaps(true);
}
        if (base.size() - 1 == position) {
            holder.navigation_top_divider.setVisibility(View.GONE);
        } else {
            holder.navigation_top_divider.setVisibility(View.VISIBLE);
        }
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
            navigation_top_divider = (TextView) itemView.findViewById(R.id.navigation_top_divider);
            icon = (ImageView) itemView.findViewById(R.id.icon);

        }
    }
}