package com.erp.sheelafoam.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.erp.sheelafoam.R;
import com.erp.sheelafoam.model.GuaranteeLogModel;

import java.util.List;

/**
 * Created by E5956 on 4/24/2018.
 */

public class GuarenteeLogAdapter extends RecyclerView.Adapter<GuarenteeLogAdapter.ViewHolder> {

    List<GuaranteeLogModel> list;
    private Context context;

    public GuarenteeLogAdapter(Context context, List<GuaranteeLogModel> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public GuarenteeLogAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.guarantee_log_item, null);
        return new GuarenteeLogAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final GuaranteeLogModel model = list.get(position);
        if (TextUtils.isEmpty(model.getDATETIME()))
            holder.tv_date.setText("Not Found");
        else
            holder.tv_date.setText(model.getDATETIME().trim());
        if (TextUtils.isEmpty(model.getSERIALNUMBER()))
            holder.tv_serialNo.setText("Not Found");
        else
            holder.tv_serialNo.setText(model.getSERIALNUMBER().trim());
        if (TextUtils.isEmpty(model.getRECEIVEMESSAGE()))
            holder.tv_message.setText("No Message");
        else
            holder.tv_message.setText(model.getRECEIVEMESSAGE().trim());
        if (TextUtils.isEmpty(model.getGIFTMESSAGE()))
            holder.tv_giftMsg.setText("No Gift Message");
        else
            holder.tv_giftMsg.setText(model.getGIFTMESSAGE().trim());

//        if (position % 2 == 1) {
//            holder.linear_guarantee.setBackgroundColor(Color.parseColor("#FFFFFF"));
//        } else {
//            holder.linear_guarantee.setBackgroundColor(Color.parseColor("#cccccc"));
//        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_date, tv_serialNo, tv_message, tv_giftMsg;
        LinearLayout linear_guarantee;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_date = (TextView) itemView.findViewById(R.id.tv_date);
            tv_serialNo = (TextView) itemView.findViewById(R.id.tv_serialNo);
            tv_message = (TextView) itemView.findViewById(R.id.tv_message);
            tv_giftMsg = (TextView) itemView.findViewById(R.id.tv_giftMsg);
            linear_guarantee = (LinearLayout) itemView.findViewById(R.id.linear_guarantee);
        }
    }
}