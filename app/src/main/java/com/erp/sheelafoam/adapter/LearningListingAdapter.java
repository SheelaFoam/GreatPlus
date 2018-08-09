package com.erp.sheelafoam.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.erp.sheelafoam.BE.LearningReferenceBE;
import com.erp.sheelafoam.R;

import java.util.ArrayList;

/**
 * Created by priyanka.sharma on 11/18/2016.
 */

public class LearningListingAdapter extends RecyclerView.Adapter<LearningListingAdapter.EmployerJobsListedHolder> {

    Context context;
    ArrayList<LearningReferenceBE> base;

    public LearningListingAdapter(Context context, ArrayList<LearningReferenceBE> base) {
        this.context = context;
        this.base = base;
    }

    @Override
    public EmployerJobsListedHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.learning_referenece_row, parent, false);
        EmployerJobsListedHolder viewHolder = new EmployerJobsListedHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(EmployerJobsListedHolder holder, int position) {
        holder.learning_txt.setText(base.get(position).getName());

        if (base.size() - 1 == position) {
            holder.learning_divider.setVisibility(View.GONE);
        } else {
            holder.learning_divider.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return base.size();
    }

    public class EmployerJobsListedHolder extends RecyclerView.ViewHolder {
        TextView learning_txt, learning_divider;

        public EmployerJobsListedHolder(View itemView) {
            super(itemView);

            learning_txt = (TextView) itemView.findViewById(R.id.learning_txt);
            learning_divider = (TextView) itemView.findViewById(R.id.learning_divider);
        }
    }
}