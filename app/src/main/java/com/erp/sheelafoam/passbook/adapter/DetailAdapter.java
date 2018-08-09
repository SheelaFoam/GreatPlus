package com.erp.sheelafoam.passbook.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.erp.sheelafoam.R;
import com.erp.sheelafoam.passbook.model.DetailModel;

import java.util.List;

public class DetailAdapter extends RecyclerView.Adapter<DetailAdapter.FreqTwoHolder> {
    private List<DetailModel> list;
    private Context context;

    public DetailAdapter(Context context, List<DetailModel> list) {
        this.context = context;
        this.list = list;

    }
    @Override
    public DetailAdapter.FreqTwoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_card_small, parent, false);
        return new DetailAdapter.FreqTwoHolder(view);
    }

    @Override
    public void onBindViewHolder(final DetailAdapter.FreqTwoHolder holder, final int position) {

        holder.tv_Dcredit.setText(list.get(position).getCredit());
        holder.tv_Ddebit.setText(list.get(position).getDebit());
        holder.tv_DtransDate.setText(list.get(position).getTransDate());
        holder.tv_DtransDetail.setText(list.get(position).getTransDetail());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class FreqTwoHolder extends RecyclerView.ViewHolder {
        private TextView tv_opening, tv_closing, tv_credit, tv_debit,tv_DtransDate,tv_DtransDetail,tv_Dcredit,tv_Ddebit;


        FreqTwoHolder(View itemView) {
            super(itemView);
            tv_DtransDate = (TextView) itemView.findViewById(R.id.tv_DtransDate);
            tv_DtransDetail = (TextView) itemView.findViewById(R.id.tv_DtransDetail);
            tv_Dcredit = (TextView) itemView.findViewById(R.id.tv_Dcredit);
            tv_Ddebit = (TextView) itemView.findViewById(R.id.tv_Ddebit);
        }
    }


}
