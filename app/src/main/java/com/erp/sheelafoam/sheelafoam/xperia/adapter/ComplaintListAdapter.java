package com.erp.sheelafoam.sheelafoam.xperia.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.erp.sheelafoam.R;
import com.erp.sheelafoam.sheelafoam.xperia.Complaint;
import com.erp.sheelafoam.sheelafoam.xperia.activity.ComplaintNew;
import com.erp.sheelafoam.sheelafoam.xperia.dataholders.GetSetComplaint;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Md Farhan on 04-Oct-16.
 */
public class ComplaintListAdapter extends BaseAdapter {
    List<Complaint> complaints;
    Context context;

    public ComplaintListAdapter(Context context, List<Complaint> complaints) {
        this.context = context;
        this.complaints = complaints;
    }
    @Override
    public int getCount() {
        return complaints.size();
    }

    @Override
    public Object getItem(int position) {
        return complaints.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.filter_complaint_adapter, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Complaint complaint = (Complaint) getItem(position);
        viewHolder.no.setText(complaint.getCOMP_ID());
        viewHolder.name.setText(complaint.getCUST_NAME());
        viewHolder.address.setText(complaint.getADDRESS1());
        viewHolder.city.setText(complaint.getCITY());
        viewHolder.problem.setText(complaint.getCOMPLAIN_TYPE_NAME());
        viewHolder.product.setText(complaint.getPRODUCT_SPECIFICATION() + "(" + complaint.getLENGTH() +"X" + complaint.getBREDTH() + "X" + complaint.getTHICK() + ")");
        viewHolder.billDate.setText(complaint.getPUR_DATE());
        viewHolder.mobile.setText(complaint.getMOBILE());
        viewHolder.dealer.setText(complaint.getDEALER_NAME());
        viewHolder.status.setText(complaint.getSTATUS_DESC());
        viewHolder.cmpDate.setText(complaint.getENTRYDATE());

        return convertView;
    }

    public class ViewHolder {
        TextView no, cmpDate, name, mobile,address,product, billDate, dealer, problem, status, city;
        public ViewHolder(View view) {

            no=(TextView) view.findViewById(R.id.txtNo);

            cmpDate=(TextView) view.findViewById(R.id.txtDate);
            city=(TextView) view.findViewById(R.id.txtCity);
            name=(TextView) view.findViewById(R.id.txtName);
            mobile=(TextView) view.findViewById(R.id.txtMobile);
            address=(TextView) view.findViewById(R.id.txtAddress);
            product=(TextView) view.findViewById(R.id.txtProduct);
            billDate=(TextView) view.findViewById(R.id.txtBillDate);
            dealer=(TextView) view.findViewById(R.id.txtDealer);
            problem=(TextView) view.findViewById(R.id.txtProblem);
            status=(TextView) view.findViewById(R.id.txtStatus);
        }

    }
}
