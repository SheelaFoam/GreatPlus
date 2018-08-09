package com.erp.sheelafoam.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.erp.sheelafoam.BE.Performance_homeBE;
import com.erp.sheelafoam.R;

import java.util.ArrayList;

/**
 * Created by priyanka.sharma on 11/18/2016.
 */

public class MyPerformance_home_Adapter extends RecyclerView.Adapter<MyPerformance_home_Adapter.EmployerJobsListedHolder> {

    Context context;
    ArrayList<Performance_homeBE> base;
    int percntage_progress_value;
    Handler handler = new Handler();

    public MyPerformance_home_Adapter(Context context, ArrayList<Performance_homeBE> base) {
        this.context = context;
        this.base = base;
    }

    @Override
    public EmployerJobsListedHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.myperformance_row, parent, false);
        EmployerJobsListedHolder viewHolder = new EmployerJobsListedHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final EmployerJobsListedHolder holder, int position) {
        holder.txt_performance_txt.setText(base.get(position).getKPI());
        percntage_progress_value = base.get(position).getACH_DETAIL();
        holder.tvWeightage.setText(base.get(position).getWEIGHTAGE());
        holder.performance_percn_value.setText(base.get(position).getACH_DETAIL() + "%");
        holder.percentage_value.setText(base.get(position).getTARGET());

        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                while (percntage_progress_value < 100) {
                    // progressBarValue++;

                    handler.post(new Runnable() {

                        @Override
                        public void run() {

                            holder.performnce_progress_value.setProgress(percntage_progress_value);
                        }
                    });
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        if (base.get(position).getACH_DETAIL() <= 25) {
            holder.performnce_progress_value.getProgressDrawable().setColorFilter(Color.parseColor("#ec6464"), PorterDuff.Mode.SRC_IN);
        } else if (base.get(position).getACH_DETAIL() > 25 && base.get(position).getACH_DETAIL() <= 50) {
            holder.performnce_progress_value.getProgressDrawable().setColorFilter(Color.parseColor("#FF7F50"), PorterDuff.Mode.SRC_IN);
        } else if (base.get(position).getACH_DETAIL() > 50) {
            holder.performnce_progress_value.getProgressDrawable().setColorFilter(Color.parseColor("#33cc66"), PorterDuff.Mode.SRC_IN);
        }

        if (base.size() - 1 == position) {
            holder.performance_divider.setVisibility(View.GONE);
        } else {
            holder.performance_divider.setVisibility(View.VISIBLE);
        }


    }

    @Override
    public int getItemCount() {
        return base.size();
    }

    public class EmployerJobsListedHolder extends RecyclerView.ViewHolder {
        TextView txt_performance_txt, performance_percn_value, performance_divider, percentage_value, tvWeightage;
        ProgressBar performnce_progress_value;

        public EmployerJobsListedHolder(View itemView) {
            super(itemView);

            txt_performance_txt = (TextView) itemView.findViewById(R.id.txt_performance_txt);
            performance_percn_value = (TextView) itemView.findViewById(R.id.performance_percn_value);
            performance_divider = (TextView) itemView.findViewById(R.id.performance_divider);
            percentage_value = (TextView) itemView.findViewById(R.id.percentage_value);
            tvWeightage = (TextView) itemView.findViewById(R.id.tv_weightage);
            performnce_progress_value = (ProgressBar) itemView.findViewById(R.id.performnce_progress_value);
        }
    }
}