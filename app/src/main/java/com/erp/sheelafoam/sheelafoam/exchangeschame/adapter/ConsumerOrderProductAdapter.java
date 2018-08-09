package com.erp.sheelafoam.sheelafoam.exchangeschame.adapter;


import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Toast;

import com.erp.sheelafoam.R;
import com.erp.sheelafoam.sheelafoam.exchangeschame.ConsumerOrderActivity2;
import com.erp.sheelafoam.sheelafoam.exchangeschame.model.ConsumerItemModel;
import com.erp.sheelafoam.utils.CustomTypefaceTextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class ConsumerOrderProductAdapter  extends BaseAdapter {
    private Context context;
    private List<ConsumerItemModel> array;
    private LayoutInflater inflater;
    private Typeface tf;
    int count = 0;
    EditText et_valueLength1,et_valueBredth1,et_valueThikness ;
    String length="",width="";
    public ConsumerOrderProductAdapter(Context context, List<ConsumerItemModel> array) {
        this.context = context;
        this.array = array;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

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
    public View getView(final int i, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = inflater.inflate(R.layout.consumer_order_item_details, parent, false);
        CustomTypefaceTextView tv_valueProduct = (CustomTypefaceTextView) convertView.findViewById(R.id.tv_valueProduct1);
        et_valueLength1 = (EditText) convertView.findViewById(R.id.et_valueLength1);
        et_valueBredth1 = (EditText) convertView.findViewById(R.id.et_valueBredth1);
        et_valueThikness = (EditText) convertView.findViewById(R.id.et_valueThikness1);
        CustomTypefaceTextView tv_valueRemove = (CustomTypefaceTextView) convertView.findViewById(R.id.tv_valueRemove);
        final CustomTypefaceTextView tv_valueQTY = (CustomTypefaceTextView) convertView.findViewById(R.id.tv_valueQTY);
        CustomTypefaceTextView tv_ValueAddItem = (CustomTypefaceTextView) convertView.findViewById(R.id.tv_ValueAddItem);
        final CustomTypefaceTextView tv_ValuetotalAmount = (CustomTypefaceTextView) convertView.findViewById(R.id.tv_ValuetotalAmount);
        final CustomTypefaceTextView tv_valueADD = (CustomTypefaceTextView) convertView.findViewById(R.id.tv_valueADD);
        if (getItem(i).getProductNameValue() == null || getItem(i).getProductNameValue().equalsIgnoreCase("null") || getItem(i).getProductNameValue().equalsIgnoreCase(null)) {
            tv_valueProduct.setText("N/A");
        } else {
            tv_valueProduct.setText(getItem(i).getProductNameValue());
        }
        et_valueBredth1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    width = et_valueBredth1.getText().toString().trim();
                    convertWidth(width);
                }
            }
        });
        et_valueLength1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    length = et_valueLength1.getText().toString().trim();
                    convertLength(length);
                }
            }
        });
        tv_valueRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (count > 0) {
                    count--;
                }
                String temp1 = String.valueOf(count);
                tv_valueQTY.setText(temp1);
            }
        });
        tv_ValueAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count++;
                String temp = String.valueOf(count);
                tv_valueQTY.setText(temp);
            }
        });
        tv_valueADD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<ConsumerItemModel> list=new ArrayList<ConsumerItemModel>();
                ConsumerItemModel model=new ConsumerItemModel();
                model.setProductNameValue(getItem(i).getProductNameValue());
                model.setQtyValue(tv_valueQTY.getText().toString());
                model.setTotalAmountValue(tv_ValuetotalAmount.getText().toString());
                list.add(model);
                Toast.makeText(context, "Add item in list"+tv_ValuetotalAmount.getText().toString()+", "+tv_valueQTY.getText().toString()+"  '"+model.getProductNameValue(), Toast.LENGTH_SHORT).show();
              /*  Intent goToNext=new Intent(context, ConsumerOrderActivity2.class) ;
                goToNext.putExtra("A", list);
                startActivity(goToNext);*/
            }
        });
        return convertView;
    }
    public void convertLength(String length) {
        if (length != null && length.length() > 0) {
            if (Double.parseDouble(length) < 100) {
                length = roundOffLengthAndWidth(Double.parseDouble(length) * 25.4);
                et_valueLength1.setText(length);
            }
        }
    }
    public void convertWidth(String width) {
        if (width != null && width.length() > 0) {
            if (Double.parseDouble(width) < 100) {
                width = roundOffLengthAndWidth(Double.parseDouble(width) * 25.4);
                et_valueBredth1.setText(width);
            }

        }
    }

    public String roundOffLengthAndWidth(double value) {
        DecimalFormat f = new DecimalFormat("0.0");
        String formattedValue = f.format(value);
        String[] array_split = formattedValue.split(Pattern.quote("."));
        Log.e("array size", "" + array_split.length);
        String _value = array_split[0];
        if (Double.parseDouble(array_split[1]) > 5 || Double.parseDouble(array_split[1]) == 5) {
            _value = String.valueOf(Double.parseDouble(_value) + 1);
        }
        return _value;
    }
}