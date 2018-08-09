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

public class CompanyPerformanceYTD_Adapter extends RecyclerView.Adapter<CompanyPerformanceYTD_Adapter.EmployerJobsListedHolder> {

    Context context;
    ArrayList<CompanyPerformanceBE> base;

    public CompanyPerformanceYTD_Adapter(Context context, ArrayList<CompanyPerformanceBE> base) {
        this.context = context;
        this.base = base;
    }

    @Override
    public EmployerJobsListedHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.company_ytd_row, parent, false);
        EmployerJobsListedHolder viewHolder = new EmployerJobsListedHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(EmployerJobsListedHolder holder, int position) {
        try {
            holder.ytd_rcv_achieved.setText(base.get(position).getRCV_YTD_ACH());
            holder.ytd_rcv_target.setText(base.get(position).getRCV_YTD_TARGET());
            holder.ytd_tp_ach.setText(base.get(position).getTP_YTD_ACH());
            holder.ytd_tp_target.setText(base.get(position).getTP_YTD_TARGET());
            holder.head_name.setText(base.get(position).getHEAD());

            if (base.size() - 1 == position) {
                holder.company_ytd_divider.setVisibility(View.GONE);
            } else {
                holder.company_ytd_divider.setVisibility(View.VISIBLE);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return base.size();
    }

    public class EmployerJobsListedHolder extends RecyclerView.ViewHolder {
        TextView ytd_tp_ach, ytd_rcv_target, ytd_tp_target, ytd_rcv_achieved, company_ytd_divider, head_name;

        public EmployerJobsListedHolder(View itemView) {
            super(itemView);
            ytd_tp_ach = (TextView) itemView.findViewById(R.id.ytd_tp_ach);
            ytd_rcv_target = (TextView) itemView.findViewById(R.id.ytd_rcv_target);
            ytd_tp_target = (TextView) itemView.findViewById(R.id.ytd_tp_target);
            ytd_rcv_achieved = (TextView) itemView.findViewById(R.id.ytd_rcv_achieved);
            head_name = (TextView) itemView.findViewById(R.id.head_name);
            company_ytd_divider = (TextView) itemView.findViewById(R.id.company_ytd_divider);
        }
    }
}