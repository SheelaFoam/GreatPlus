package com.erp.sheelafoam.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.erp.sheelafoam.BE.LatestEventsBE;
import com.erp.sheelafoam.Picasso;
import com.erp.sheelafoam.R;
import java.util.ArrayList;

/**
 * Created by priyanka.sharma on 11/18/2016.
 */

public class LatestEventsAdapter extends RecyclerView.Adapter<LatestEventsAdapter.EmployerJobsListedHolder> {

    Context context;
    ArrayList<LatestEventsBE> base;

    public LatestEventsAdapter(Context context, ArrayList<LatestEventsBE> base) {
        this.context = context;
        this.base = base;
    }

    @Override
    public EmployerJobsListedHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sidemenu_latestevent_row, parent, false);
        EmployerJobsListedHolder viewHolder = new EmployerJobsListedHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(EmployerJobsListedHolder holder, int position) {
        holder.events_name.setText(base.get(position).getTitle());
        holder.event_date.setText(base.get(position).getEvent_date());
        holder.event_matter.setText(base.get(position).getShort_desc());
        Picasso.with(context).load(base.get(position).getImage()).into(holder.event_image);

        if (base.size() - 1 == position) {
            holder.event_divider.setVisibility(View.GONE);
        } else {
            holder.event_divider.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return base.size();
    }

    public class EmployerJobsListedHolder extends RecyclerView.ViewHolder {
        TextView events_name, event_matter, event_date, event_divider;
        ImageView event_image;

        public EmployerJobsListedHolder(View itemView) {
            super(itemView);

            events_name = (TextView) itemView.findViewById(R.id.events_name);
            event_matter = (TextView) itemView.findViewById(R.id.event_matter);
            event_date = (TextView) itemView.findViewById(R.id.event_date);
            event_divider = (TextView) itemView.findViewById(R.id.event_divider);
            event_image = (ImageView) itemView.findViewById(R.id.event_image);
        }
    }
}