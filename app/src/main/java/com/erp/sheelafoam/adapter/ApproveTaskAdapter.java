package com.erp.sheelafoam.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.erp.sheelafoam.BE.ApproveTaskBE;
import com.erp.sheelafoam.R;
import com.erp.sheelafoam.constant.Constant;
import com.erp.sheelafoam.utils.AlertDialogManager;
import com.erp.sheelafoam.utils.ConnectionDetector;
import com.erp.sheelafoam.utils.Util;

import java.util.ArrayList;

/**
 * Created by priyanka.sharma on 11/18/2016.
 */

public class ApproveTaskAdapter extends RecyclerView.Adapter<ApproveTaskAdapter.EmployerJobsListedHolder> {

    Context context;
    ArrayList<ApproveTaskBE> base;
    public ConnectionDetector cDetector;
    AlertDialogManager alert;
    String GreatPlusUserID, OP_USER_ROLENAME;

    public ApproveTaskAdapter(Context context, ArrayList<ApproveTaskBE> base) {
        this.context = context;
        this.base = base;
    }

    @Override
    public EmployerJobsListedHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.approvetask_row, parent, false);
        EmployerJobsListedHolder viewHolder = new EmployerJobsListedHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(EmployerJobsListedHolder holder, int position) {
        String DisplayNAme = Util.getSharedPrefrenceValue(context, Constant.Sp_DisplayName);
        holder.task_name.setText(DisplayNAme);
        holder.task_matter.setText(base.get(position).getTASK_DESC());
        holder.task_subject.setText(base.get(position).getTASK_TITLE());
        holder.approve_task_time.setText(base.get(position).getTASK_DATETIME());

        if (base.size() - 1 == position) {
            holder.task_divider.setVisibility(View.GONE);
        } else {
            holder.task_divider.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return base.size();
    }

    public class EmployerJobsListedHolder extends RecyclerView.ViewHolder {
        TextView task_matter, task_subject, task_name, task_divider, approve_task_time;

        public EmployerJobsListedHolder(View itemView) {
            super(itemView);

            task_subject = (TextView) itemView.findViewById(R.id.task_subject);
            task_matter = (TextView) itemView.findViewById(R.id.task_matter);
            task_name = (TextView) itemView.findViewById(R.id.task_name);
            approve_task_time = (TextView) itemView.findViewById(R.id.approve_task_time);
            task_divider = (TextView) itemView.findViewById(R.id.task_divider);

        }
    }
}