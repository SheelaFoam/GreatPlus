package com.erp.sheelafoam.sheelafoam.exchangeschame.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.erp.sheelafoam.R;
import com.erp.sheelafoam.sheelafoam.exchangeschame.ConsumerOredrActivity;
import com.erp.sheelafoam.sheelafoam.exchangeschame.model.ConsumerItemModel;
import com.erp.sheelafoam.utils.CustomTypefaceTextView;

import java.util.List;

public class ConsumerOrderAdapter extends BaseAdapter {
    private Context context;
    private List<ConsumerItemModel> array;
    private LayoutInflater inflater;
    Typeface tf;
    int count = 0;
ConsumerOredrActivity activity;
    public ConsumerOrderAdapter(Context context, List<ConsumerItemModel> array) {
        this.context = context;
        this.array = array;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        activity=activity;

    }

    @Override
    public int getCount() {
        return array.size();
    }

    @Override
    public ConsumerItemModel getItem(int i) {
        return array.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final  int i, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = inflater.inflate(R.layout.consumer_item_order_details, parent, false);
        CustomTypefaceTextView tv_valueProduct = (CustomTypefaceTextView) convertView.findViewById(R.id.tv_valueProduct);

        CustomTypefaceTextView remove_item = (CustomTypefaceTextView) convertView.findViewById(R.id.remove_item);
        final CustomTypefaceTextView tv_qtyValue = (CustomTypefaceTextView) convertView.findViewById(R.id.tv_qtyValue);
        CustomTypefaceTextView add_item = (CustomTypefaceTextView) convertView.findViewById(R.id.add_item);
        CustomTypefaceTextView tv_valueSize = (CustomTypefaceTextView) convertView.findViewById(R.id.tv_valueSize);
        CustomTypefaceTextView tv_totalAmountValue = (CustomTypefaceTextView) convertView.findViewById(R.id.tv_totalAmountValue);
        if(getItem(i).getProductNameValue()==null||getItem(i).getProductNameValue().equalsIgnoreCase("null")||getItem(i).getProductNameValue().equalsIgnoreCase(null)){
            tv_valueProduct.setText("N/A");
        }else {
            tv_valueProduct.setText(getItem(i).getProductNameValue());
        }
        if(getItem(i).getQtyValue()==null||getItem(i).getQtyValue().equalsIgnoreCase("null")||getItem(i).getQtyValue().equalsIgnoreCase(null)){
            tv_qtyValue.setText("N/A");
        }else {
        tv_qtyValue.setText(getItem(i).getQtyValue());
    }
        if(getItem(i).getLengthValue()==null||getItem(i).getLengthValue().equalsIgnoreCase("null")||getItem(i).getLengthValue().equalsIgnoreCase(null)){
            tv_valueSize.setText("N/A");
        }else {
            tv_valueSize.setText(getItem(i).getLengthValue()+"(L)"+" "+getItem(i).getBreathValue()+" "+"(B)"+getItem(i).getThickensValue()+" "+"(T)");
        }
        if(getItem(i).getTotalAmountValue()==null||getItem(i).getTotalAmountValue().equalsIgnoreCase("null")||getItem(i).getTotalAmountValue().equalsIgnoreCase(null)){
            tv_totalAmountValue.setText("N/A");
        }else {
            tv_totalAmountValue.setText(getItem(i).getTotalAmountValue());
        }
        remove_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (count > 0) {
                    count--;
                }
                String temp1 = String.valueOf(count);

            }
        });
        add_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count++;
                String temp = String.valueOf(count);
               tv_qtyValue.setText(temp);
            }
        });

        return convertView;
    }
    /*public void removeView(Object position) {
        // lv and the adapter must be public-static in their Activity Class
        activity.listView.removeViewAt(Integer.parteInt(position).toString());
        activity.adapter.notifyDataSetChanged();
    }*/
}