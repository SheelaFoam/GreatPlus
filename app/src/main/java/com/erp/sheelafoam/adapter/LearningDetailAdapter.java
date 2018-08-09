package com.erp.sheelafoam.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.erp.sheelafoam.BE.LearningRefDetailBE;
import com.erp.sheelafoam.NewWebViewActivity;
import com.erp.sheelafoam.R;

import java.util.ArrayList;

/**
 * Created by priyanka.sharma on 11/18/2016.
 */

public class LearningDetailAdapter extends RecyclerView.Adapter<LearningDetailAdapter.EmployerJobsListedHolder> {

    Context context;
    ArrayList<LearningRefDetailBE> base;

    public LearningDetailAdapter(Context context, ArrayList<LearningRefDetailBE> base) {
        this.context = context;
        this.base = base;
    }

    @Override
    public EmployerJobsListedHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.learning_ref_deatil_row, parent, false);
        EmployerJobsListedHolder viewHolder = new EmployerJobsListedHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(EmployerJobsListedHolder holder, final int position) {
        holder.learning_topic.setText(base.get(position).getName());
        holder.learning_detail.setText(Html.fromHtml(base.get(position).getMessage()));
        if (base.get(position).getLink() != null) {
            holder.learning_detail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("Posstion",""+position);
                    Intent in = new Intent(context, NewWebViewActivity.class);
                    in.putExtra("WebUrl", base.get(position).getLink());
                    context.startActivity(in);
                }
            });
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return base.size();
    }

    public class EmployerJobsListedHolder extends RecyclerView.ViewHolder {
        TextView learning_topic, learning_detail;

        public EmployerJobsListedHolder(View itemView) {
            super(itemView);

            learning_topic = (TextView) itemView.findViewById(R.id.learning_topic);
            learning_detail = (TextView) itemView.findViewById(R.id.learning_detail);
        }
    }
}