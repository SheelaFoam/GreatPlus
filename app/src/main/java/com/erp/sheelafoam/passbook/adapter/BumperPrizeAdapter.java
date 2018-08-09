package com.erp.sheelafoam.passbook.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.erp.sheelafoam.R;
import com.erp.sheelafoam.passbook.model.PrizeModel;

import java.util.List;

public class BumperPrizeAdapter  extends RecyclerView.Adapter<BumperPrizeAdapter.FreqTwoHolder> {
    private List<PrizeModel> list;
    private Context context;

    public BumperPrizeAdapter(Context context, List<PrizeModel> list) {
        this.context = context;
        this.list = list;

    }
    @Override
    public BumperPrizeAdapter.FreqTwoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_prizelayout, parent, false);
        return new BumperPrizeAdapter.FreqTwoHolder(view);
    }

    @Override
    public void onBindViewHolder(final BumperPrizeAdapter.FreqTwoHolder holder, final int position) {
        holder.tv_transaction.setText(list.get(position).getTransactionDate());
        holder.tv_transactiondetails.setText(list.get(position).getTransactionDetails());


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class FreqTwoHolder extends RecyclerView.ViewHolder {
        private TextView tv_transactiondetails,tv_transaction;


        FreqTwoHolder(View itemView) {
            super(itemView);
            tv_transactiondetails = (TextView) itemView.findViewById(R.id.tv_transactiondetails);
            tv_transaction = (TextView) itemView.findViewById(R.id.tv_transaction);
        }
    }


}

