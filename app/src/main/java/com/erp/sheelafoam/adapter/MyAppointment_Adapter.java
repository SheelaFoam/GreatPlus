package com.erp.sheelafoam.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.erp.sheelafoam.BE.AppointmentBE;
import com.erp.sheelafoam.R;

import java.util.ArrayList;

/**
 * Created by priyanka.sharma on 11/18/2016.
 */

public class MyAppointment_Adapter extends RecyclerView.Adapter<MyAppointment_Adapter.EmployerJobsListedHolder> {

    Context context;
    ArrayList<AppointmentBE> base;

    public MyAppointment_Adapter(Context context, ArrayList<AppointmentBE> base) {
        this.context = context;
        this.base = base;
    }

    @Override
    public EmployerJobsListedHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.my_appointment_row, parent, false);
        EmployerJobsListedHolder viewHolder = new EmployerJobsListedHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(EmployerJobsListedHolder holder, int position) {
        holder.current_date.setText("' " + base.get(position).getDay() + " '");
        holder.current_month.setText(base.get(position).getMonth());
        holder.appointment_time.setText(base.get(position).getTime() + "-" + base.get(position).getEnd_time());
        holder.appointment_sub.setText(base.get(position).getDesc());

        if (base.size() - 1 == position) {
            holder.appointment_divider.setVisibility(View.GONE);
        } else {
            holder.appointment_divider.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return base.size();
    }

    public class EmployerJobsListedHolder extends RecyclerView.ViewHolder {
        TextView current_date, current_month, appointment_time, appointment_sub, appointment_divider;

        public EmployerJobsListedHolder(View itemView) {
            super(itemView);

            appointment_sub = (TextView) itemView.findViewById(R.id.appointment_sub);
            current_date = (TextView) itemView.findViewById(R.id.current_date);
            current_month = (TextView) itemView.findViewById(R.id.current_month);
            appointment_time = (TextView) itemView.findViewById(R.id.appointment_time);
            appointment_divider = (TextView) itemView.findViewById(R.id.appointment_divider);
        }
    }
}