package com.erp.sheelafoam.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.erp.sheelafoam.BE.CompanyPerformanceBE;
import com.erp.sheelafoam.R;

import java.util.ArrayList;

/**
 * Created by priyanka.sharma on 11/18/2016.
 */

public class CompanyPerformanceAdapter extends RecyclerView.Adapter<CompanyPerformanceAdapter.EmployerJobsListedHolder> {

    Context context;
    ArrayList<CompanyPerformanceBE> base;

    public CompanyPerformanceAdapter(Context context, ArrayList<CompanyPerformanceBE> base) {
        this.context = context;
        this.base = base;
    }

    @Override
    public EmployerJobsListedHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.company_thisweek_row, parent, false);
        EmployerJobsListedHolder viewHolder = new EmployerJobsListedHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(EmployerJobsListedHolder holder, int position) {
        holder.week_rcv_achieved.setText(base.get(position).getRCV_WEEK_ACH());
        holder.week_tp_ach.setText(base.get(position).getTP_WEEK_ACH());
        holder.week_rcv_target.setText(base.get(position).getRCV_WEEK_TARGET());
        holder.week_tp_target.setText(base.get(position).getTP_WEEK_TARGET());
        holder.head_name.setText(base.get(position).getHEAD());

        if (base.size() - 1 == position) {
            holder.company_divider.setVisibility(View.GONE);
        } else {
            holder.company_divider.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return base.size();
    }

    public class EmployerJobsListedHolder extends RecyclerView.ViewHolder {
        TextView week_rcv_target, week_rcv_achieved, week_tp_ach, week_tp_target, company_divider, head_name;

        public EmployerJobsListedHolder(View itemView) {
            super(itemView);
            week_rcv_target = (TextView) itemView.findViewById(R.id.week_rcv_target);
            week_rcv_achieved = (TextView) itemView.findViewById(R.id.week_rcv_achieved);
            week_tp_ach = (TextView) itemView.findViewById(R.id.week_tp_ach);
            week_tp_target = (TextView) itemView.findViewById(R.id.week_tp_target);
            company_divider = (TextView) itemView.findViewById(R.id.company_divider);
            head_name = (TextView) itemView.findViewById(R.id.head_name);
        }
    }
}