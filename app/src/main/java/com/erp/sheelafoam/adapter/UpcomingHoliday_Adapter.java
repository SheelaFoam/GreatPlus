package com.erp.sheelafoam.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.erp.sheelafoam.BE.UpcomingHolidayBE;
import com.erp.sheelafoam.R;

import java.util.ArrayList;

/**
 * Created by priyanka.sharma on 11/18/2016.
 */

public class UpcomingHoliday_Adapter extends RecyclerView.Adapter<UpcomingHoliday_Adapter.EmployerJobsListedHolder> {

    Context context;
    ArrayList<UpcomingHolidayBE> base;

    public UpcomingHoliday_Adapter(Context context, ArrayList<UpcomingHolidayBE> base) {
        this.context = context;
        this.base = base;
    }

    @Override
    public EmployerJobsListedHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sidemenu_upcomingholiday_row, parent, false);
        EmployerJobsListedHolder viewHolder = new EmployerJobsListedHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(EmployerJobsListedHolder holder, int position) {
        holder.holiday_date.setText(base.get(position).getHOLIDAY_DATE());
        holder.holiday_festival.setText(base.get(position).getHOLIDAY_NAME());

        if (base.size() - 1 == position) {
            holder.holiday_divider.setVisibility(View.GONE);
        } else {
            holder.holiday_divider.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return base.size();
    }

    public class EmployerJobsListedHolder extends RecyclerView.ViewHolder {
        TextView holiday_divider, holiday_festival, holiday_date;

        public EmployerJobsListedHolder(View itemView) {
            super(itemView);

            holiday_festival = (TextView) itemView.findViewById(R.id.holiday_festival);
            holiday_divider = (TextView) itemView.findViewById(R.id.holiday_divider);
            holiday_date = (TextView) itemView.findViewById(R.id.holiday_date);
        }
    }
}