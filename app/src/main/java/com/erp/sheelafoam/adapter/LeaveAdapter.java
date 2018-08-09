package com.erp.sheelafoam.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.erp.sheelafoam.BE.LeaveDetailBE;
import com.erp.sheelafoam.R;

import java.util.ArrayList;

/**
 * Created by priyanka.sharma on 11/18/2016.
 */

public class LeaveAdapter extends RecyclerView.Adapter<LeaveAdapter.EmployerJobsListedHolder> {

    Context context;
    ArrayList<LeaveDetailBE> base;

    public LeaveAdapter(Context context, ArrayList<LeaveDetailBE> base) {
        this.context = context;
        this.base = base;
    }

    @Override
    public EmployerJobsListedHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.leave_row, parent, false);
        EmployerJobsListedHolder viewHolder = new EmployerJobsListedHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(EmployerJobsListedHolder holder, int position) {
        if (position == 0) {
            holder.head_name.setText("Casual Leave (CL)");
            holder.balance_leave.setText(base.get(position).getOp_bal_cl());
            holder.taken_leave.setText(base.get(position).getOp_receive_cl());
            holder.encash_leave.setText(base.get(position).getOp_issue_cl());
            holder.opening_leave.setText(base.get(position).getOp_open_cl());
        }

        if (position == 1) {
            holder.head_name.setText("Earned Leave (EL)");
            holder.balance_leave.setText(base.get(position).getOp_bal_el());
            holder.taken_leave.setText(base.get(position).getOp_receive_el());
            holder.encash_leave.setText(base.get(position).getOp_issue_el());
            holder.opening_leave.setText(base.get(position).getOp_open_el());
        }

        if (position == 2) {
            holder.head_name.setText("Compensatory off (CO)");
            holder.balance_leave.setText(base.get(position).getOp_bal_co());
            holder.taken_leave.setText(base.get(position).getOp_receive_co());
            holder.encash_leave.setText(base.get(position).getOp_issue_co());
            holder.opening_leave.setText(base.get(position).getOp_open_co());
        }

        if (position == 2) {
            holder.leave_divider.setVisibility(View.GONE);
        } else {
            holder.leave_divider.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    public class EmployerJobsListedHolder extends RecyclerView.ViewHolder {
        TextView balance_leave, taken_leave, encash_leave, opening_leave, leave_divider, head_name;

        public EmployerJobsListedHolder(View itemView) {
            super(itemView);

            encash_leave = (TextView) itemView.findViewById(R.id.encash_leave);
            balance_leave = (TextView) itemView.findViewById(R.id.balance_leave);
            taken_leave = (TextView) itemView.findViewById(R.id.taken_leave);
            opening_leave = (TextView) itemView.findViewById(R.id.opening_leave);
            head_name = (TextView) itemView.findViewById(R.id.head_name);
            leave_divider = (TextView) itemView.findViewById(R.id.leave_divider);
        }
    }
}