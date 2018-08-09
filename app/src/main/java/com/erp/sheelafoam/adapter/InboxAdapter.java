package com.erp.sheelafoam.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.erp.sheelafoam.BE.InboxBE;
import com.erp.sheelafoam.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by priyanka.sharma on 11/18/2016.
 */

public class InboxAdapter extends RecyclerView.Adapter<InboxAdapter.EmployerJobsListedHolder> {

    Context context;
    String date_;
    ArrayList<InboxBE> base;

    public InboxAdapter(Context context, ArrayList<InboxBE> base) {
        this.context = context;
        this.base = base;
    }

    @Override
    public EmployerJobsListedHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.inbox_row, parent, false);
        EmployerJobsListedHolder viewHolder = new EmployerJobsListedHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(EmployerJobsListedHolder holder, int position) {

        String date = base.get(position).getD().substring(0, base.get(position).getD().length() - 3);

        if(base.get(position).getE().get(0).getP() == null)
            holder.inbox_name.setText(base.get(position).getE().get(0).getA());
        else
        holder.inbox_name.setText(base.get(position).getE().get(0).getP());
        holder.inbox_subject.setText(base.get(position).getSu());

        long timestamp = Long.valueOf(date); //Example -> in ms
        Date d = new Date(timestamp * 1000);
        date_ = new SimpleDateFormat("dd-MM-yyyy").format(d);

        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = df.format(c.getTime());
        if (date_.equals(formattedDate)) {
            date_ = new SimpleDateFormat("hh:mm a").format(d);
            holder.inbox_date.setText(date_);
        } else {
            holder.inbox_date.setText(date_);
        }

        if (base.size() - 1 == position) {
            holder.inbox_divider.setVisibility(View.GONE);
        } else {
            holder.inbox_divider.setVisibility(View.VISIBLE);
        }
    }

    private String getDate(long timeStamp) {
        try {
            DateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            Date netDate = (new Date(timeStamp));
            return sdf.format(netDate);
        } catch (Exception ex) {
            return "xx";
        }
    }

    @Override
    public int getItemCount() {
        return base.size();
    }

    public class EmployerJobsListedHolder extends RecyclerView.ViewHolder {
        TextView inbox_name, inbox_subject, inbox_divider, inbox_date;

        public EmployerJobsListedHolder(View itemView) {
            super(itemView);
            inbox_subject = (TextView) itemView.findViewById(R.id.inbox_subject);
            inbox_name = (TextView) itemView.findViewById(R.id.inbox_name);
            inbox_date = (TextView) itemView.findViewById(R.id.inbox_date);
            inbox_divider = (TextView) itemView.findViewById(R.id.inbox_divider);
        }
    }
}