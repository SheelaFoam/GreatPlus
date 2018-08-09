package com.erp.sheelafoam.sheelafoam.xperia;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.erp.sheelafoam.R;

import java.util.List;

/**
 * Created by sudhirharit on 21/11/17.
 */

public class ProductAdapter extends BaseAdapter {

    Context context;
    List<Product> products;
    public ProductAdapter(Context context, List<Product> products){
        this.context = context;
        this.products = products;
    }
    @Override
    public int getCount() {
        return products.size();
    }

    @Override
    public Object getItem(int i) {
        return products.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.complaint_tye_row, viewGroup, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Product product = (Product) getItem(i);
        viewHolder.complaintName.setText(product.getProductName());

        return convertView;

    }


    public class ViewHolder{
        TextView complaintName;

        public ViewHolder(View view){
            complaintName = (TextView) view.findViewById(R.id.complaint_name);
        }
    }
}
