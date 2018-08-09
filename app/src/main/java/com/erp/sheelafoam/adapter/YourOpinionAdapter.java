package com.erp.sheelafoam.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.erp.sheelafoam.BE.PollsBE;
import com.erp.sheelafoam.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by priyanka.sharma on 11/18/2016.
 */

public class YourOpinionAdapter extends RecyclerView.Adapter<YourOpinionAdapter.EmployerJobsListedHolder> {

    Context context;
    private static CompoundButton lastChecked = null;
    private static int lastCheckedPos = 0;
    ArrayList<PollsBE> polls;

    public int mSelectedItem = -1;
    public  int notAvalable=-1;

    public HashMap<String,String> optionID=new HashMap<String, String>();

    public YourOpinionAdapter(Context context, ArrayList<PollsBE> polls) {
        this.context = context;
        this.polls=polls;
    }

    @Override
    public EmployerJobsListedHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.your_opinion_row, parent, false);
        EmployerJobsListedHolder viewHolder = new EmployerJobsListedHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final EmployerJobsListedHolder holder, final int position) {
        holder.option.setText(polls.get(0).getPolls_option().get(position).getName());

        Log.d("Polls answer size",polls.get(0).getPolls_option_answer().size()+"");
        if(polls.get(0).getPolls_option_answer().size()>0){
            Log.d("Under If condition",polls.get(0).getPolls_option_answer().get(0).getId_poll_options()+" , "+polls.get(0).getPolls_option().get(position).getId());
            if(polls.get(0).getPolls_option_answer().get(0).getId_poll_options().equals(polls.get(0).getPolls_option().get(position).getId())){
                Log.d("Under If condition","");
                polls.get(0).getPolls_option().get(position).setCheckValues(true);
                holder.layout_border.setBackgroundResource(R.drawable.layout_border_selected);
                notAvalable=0;
            }

            holder.percentage.setText(polls.get(0).getPolls_option_answer().get(0).getPercent().get(position).getPercent()+"%");

        }

        if(polls.get(0).getPolls_option().get(position).isCheckValues()){
            holder.optionCheck.setChecked(true);
            holder.layout_border.setBackgroundResource(R.drawable.layout_border_selected);
        }
        else {
            holder.optionCheck.setChecked(false);
            holder.layout_border.setBackgroundResource(R.drawable.layout_border);
        }


        holder.optionCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(notAvalable==-1) {
                    if (isChecked) {

                        if (lastChecked != null) {
                            lastChecked.setChecked(false);
                        }
                        //store the clicked radiobutton
                        lastChecked = buttonView;
                        polls.get(0).getPolls_option().get(position).setCheckValues(true);
                        optionID.put("ID", polls.get(0).getPolls_option().get(position).getId());
                        mSelectedItem++;
                        holder.layout_border.setBackgroundResource(R.drawable.layout_border_selected);
                    } else {
                        lastChecked = null;
                        polls.get(0).getPolls_option().get(position).setCheckValues(false);
                        optionID.remove("ID");
                        mSelectedItem++;
                        holder.layout_border.setBackgroundResource(R.drawable.layout_border);

                    }
                }
                else {
                    holder.optionCheck.setChecked(false);
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return polls.get(0).getPolls_option().size();
    }

    public class EmployerJobsListedHolder extends RecyclerView.ViewHolder {

        TextView option,percentage;
        CheckBox optionCheck;
        LinearLayout layout_border;

        public EmployerJobsListedHolder(View itemView) {
            super(itemView);
            option= (TextView) itemView.findViewById(R.id.option);
            percentage= (TextView) itemView.findViewById(R.id.percentage);
            optionCheck= (CheckBox) itemView.findViewById(R.id.option_checked);
            layout_border= (LinearLayout) itemView.findViewById(R.id.layout_border);

        }
    }
}