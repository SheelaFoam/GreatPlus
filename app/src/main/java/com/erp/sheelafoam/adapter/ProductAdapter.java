package com.erp.sheelafoam.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.erp.sheelafoam.R;
import com.erp.sheelafoam.models.SalesModel;

import java.util.List;

public class ProductAdapter extends BaseAdapter {
    Context context;
    List<SalesModel> list;
    LayoutInflater inflater;
    private int total_item_count =0, total_amount =0;
    private TextView tv_total_amount,tv_total_item;
    public ProductAdapter(Context context, List<SalesModel> list, TextView tv_total_amount, TextView tv_total_item) {
        this.list = list;
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.tv_total_amount = tv_total_amount;
        this.tv_total_item = tv_total_item;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public SalesModel getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View view1 = inflater.inflate(R.layout.item_product_list, null);
        TextView item_name = (TextView) view1.findViewById(R.id.item_name);
        TextView add_item = (TextView) view1.findViewById(R.id.add_item);
        TextView remove_item = (TextView) view1.findViewById(R.id.remove_item);
        final TextView item_price = (TextView) view1.findViewById(R.id.item_price);
        TextView item_short_desc = (TextView) view1.findViewById(R.id.item_short_desc);
        final TextView tv_item_count= (TextView) view1.findViewById(R.id.item_count);
        item_name.setText(list.get(i).getBREDTH());
        item_price.setText(list.get(i).getCOUNT1());
        item_short_desc.setText(list.get(i).getLENGTH());

        add_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                total_item_count = total_item_count+1;
                total_amount = total_amount + Integer.valueOf(item_price.getText().toString());
                tv_item_count.setText(String.valueOf(Integer.valueOf(tv_item_count.getText().toString())+1));

                tv_total_amount.setText(String.valueOf(total_amount));
                tv_total_item.setText(String.valueOf(total_item_count));
            }
        });

        remove_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Integer.valueOf(tv_item_count.getText().toString())>0) {
                    total_item_count = total_item_count - 1;
                    total_amount = total_amount - Integer.valueOf(item_price.getText().toString());
                    tv_item_count.setText(String.valueOf(Integer.valueOf(tv_item_count.getText().toString())-1));
                    tv_total_amount.setText(String.valueOf(total_amount));
                    tv_total_item.setText(String.valueOf(total_item_count));
                }
            }
        });
        return view1;
    }
}
