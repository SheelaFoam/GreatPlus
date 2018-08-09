package com.erp.sheelafoam.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.erp.sheelafoam.BE.OtherRequestBE;
import com.erp.sheelafoam.Picasso;
import com.erp.sheelafoam.R;
import java.util.ArrayList;

/**
 * Created by priyanka.sharma on 11/18/2016.
 */

public class OtherRequestAdapter extends RecyclerView.Adapter<OtherRequestAdapter.EmployerJobsListedHolder> {

    Context context;
    ArrayList<OtherRequestBE> base;

    public OtherRequestAdapter(Context context, ArrayList<OtherRequestBE> base) {
        this.base = base;
        this.context = context;
    }

    @Override
    public EmployerJobsListedHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.faciliy_and_request_adapter, parent, false);
        EmployerJobsListedHolder viewHolder = new EmployerJobsListedHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final EmployerJobsListedHolder holder, final int position) {
        holder.facility_txt.setText(base.get(position).getCHILD_MENU());
        if (base.get(position).getICON().isEmpty()) {
            Picasso.with(context).load(R.drawable.ic_right_arrow).into(holder.facility_icon);

        } else {
            Picasso.with(context).load(base.get(position).getICON()).into(holder.facility_icon);
        }
    }


    @Override
    public int getItemCount() {
        return base.size();
    }

    public class EmployerJobsListedHolder extends RecyclerView.ViewHolder {

        TextView facility_txt;
        ImageView facility_icon;

        public EmployerJobsListedHolder(View itemView) {
            super(itemView);

            facility_txt = (TextView) itemView.findViewById(R.id.facility_txt);
            facility_icon = (ImageView) itemView.findViewById(R.id.facility_icon);

        }
    }
}