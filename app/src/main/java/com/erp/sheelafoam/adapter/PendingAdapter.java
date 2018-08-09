package com.erp.sheelafoam.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import com.erp.sheelafoam.R;
import com.erp.sheelafoam.models.PendingGCOrderModel;
import com.erp.sheelafoam.sheelafoam.interfaces.OnItemButtonClick;
import com.erp.sheelafoam.utils.CustomTypefaceTextView;

import java.util.List;

public class PendingAdapter extends BaseAdapter {
    Typeface tf;
    private Context context;
    private List<PendingGCOrderModel> array;
    private LayoutInflater inflater;
    private OnItemButtonClick click;
    public PendingAdapter(Context context, OnItemButtonClick click, List<PendingGCOrderModel> array) {
        this.context = context;
        this.click=click;
        this.array = array;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return array.size();
    }

    @Override
    public PendingGCOrderModel getItem(int i) {
        return array.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = inflater.inflate(R.layout.pending_item_for_gc, parent, false);
        //CustomTypefaceTextView tv_modeName = (CustomTypefaceTextView) convertView.findViewById(R.id.tv_modeName);
        CustomTypefaceTextView tv_orderNoValue = (CustomTypefaceTextView) convertView.findViewById(R.id.tv_orderNoValue);
        CustomTypefaceTextView tv_orderCustomerNameValue = (CustomTypefaceTextView) convertView.findViewById(R.id.tv_orderCustomerNameValue);
        CustomTypefaceTextView tv_orderConsumerMobValue = (CustomTypefaceTextView) convertView.findViewById(R.id.tv_orderConsumerMobValue);
        CustomTypefaceTextView tv_qtyValue = (CustomTypefaceTextView) convertView.findViewById(R.id.tv_qtyValue);
        CustomTypefaceTextView tv_orderProductValue = (CustomTypefaceTextView) convertView.findViewById(R.id.tv_orderProductValue);
        CustomTypefaceTextView tv_orderLenghtValue = (CustomTypefaceTextView) convertView.findViewById(R.id.tv_orderLenghtValue);
        CustomTypefaceTextView tv_orderBredthValue = (CustomTypefaceTextView) convertView.findViewById(R.id.tv_orderBredthValue);
        CustomTypefaceTextView tv_orderThiknessValue = (CustomTypefaceTextView) convertView.findViewById(R.id.tv_orderThiknessValue);
        Button btn_gc = (Button) convertView.findViewById(R.id.btn_gc);

        final PendingGCOrderModel model=getItem(i);
        btn_gc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                click.onButtonClick(model);
            }
        });
        if (model.getOrderNumber() == null || model.getOrderNumber().equalsIgnoreCase("null")) {
            tv_orderNoValue.setText("N/A");
        } else {
            tv_orderNoValue.setText(getItem(i).getOrderNumber());
        }
        if (model.getPCustomerMobile()== null || model.getPCustomerMobile().equalsIgnoreCase("null")) {
            tv_orderConsumerMobValue.setText("N/A");
        } else {
            tv_orderConsumerMobValue.setText(getItem(i).getPCustomerMobile());
        }
        if (model.getPCustomerName()== null || model.getPCustomerName().equalsIgnoreCase("null")) {
            tv_orderCustomerNameValue.setText("N/A");
        } else {
            tv_orderCustomerNameValue.setText(model.getPCustomerName());
        }

        if (model.getPLength()== null || model.getPLength().equalsIgnoreCase("null")) {
            tv_orderLenghtValue.setText("N/A");
        } else {
            tv_orderLenghtValue.setText(model.getPLength());
        }
        if (model.getPBredth()== null || model.getPBredth().equalsIgnoreCase("null")) {
            tv_orderBredthValue.setText("N/A");
        } else {
            tv_orderBredthValue.setText(model.getPBredth());
        }
        if (model.getPThick()== null || model.getPThick().equalsIgnoreCase("null")) {
            tv_orderThiknessValue.setText("N/A");
        } else {
            tv_orderThiknessValue.setText(model.getPThick());
        }
        if (model.getPProductDisplayName()== null || model.getPProductDisplayName().equalsIgnoreCase("null")) {
            tv_orderProductValue.setText("N/A");
        } else {
            tv_orderProductValue.setText(model.getPProductDisplayName());
        }
        if (model.getPQuantity()== null || model.getPQuantity().equalsIgnoreCase("null")) {
            tv_qtyValue.setText("N/A");
        } else {
            tv_qtyValue.setText(model.getPQuantity());
        }
        return convertView;
    }
}
