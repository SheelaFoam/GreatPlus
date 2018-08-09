package com.erp.sheelafoam.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.erp.sheelafoam.BE.SankalpStoriesBE;
import com.erp.sheelafoam.Picasso;
import com.erp.sheelafoam.R;
import java.util.ArrayList;

/**
 * Created by priyanka.sharma on 11/18/2016.
 */

public class SankalpStoriesAdapter extends RecyclerView.Adapter<SankalpStoriesAdapter.EmployerJobsListedHolder> {

    Context context;
    ArrayList<SankalpStoriesBE> base;

    public SankalpStoriesAdapter(Context context, ArrayList<SankalpStoriesBE> base) {
        this.context = context;
        this.base = base;
    }

    @Override
    public EmployerJobsListedHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sidemenu_sankapstories_row, parent, false);
        EmployerJobsListedHolder viewHolder = new EmployerJobsListedHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(EmployerJobsListedHolder holder, int position) {
        holder.sankalp_title.setText(base.get(position).getTitle());
        holder.sankalp_date.setText(base.get(position).getDate());

        holder.sankalp_desc.setText((base.get(position).getDesc()));
        Picasso.with(context).load(base.get(position).getImage()).into(holder.sankalp_image);

        if (base.size() - 1 == position) {
            holder.sankap_divider.setVisibility(View.GONE);
        } else {
            holder.sankap_divider.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return base.size();
    }

    public class EmployerJobsListedHolder extends RecyclerView.ViewHolder {
        TextView sankalp_desc, sankalp_date, sankalp_title, sankap_divider;
        ImageView sankalp_image;

        public EmployerJobsListedHolder(View itemView) {
            super(itemView);

            sankalp_desc = (TextView) itemView.findViewById(R.id.sankalp_desc);
            sankalp_date = (TextView) itemView.findViewById(R.id.sankalp_date);
            sankalp_title = (TextView) itemView.findViewById(R.id.sankalp_title);
            sankap_divider = (TextView) itemView.findViewById(R.id.sankap_divider);
            sankalp_image = (ImageView) itemView.findViewById(R.id.sankalp_image);
        }
    }
}