package com.erp.sheelafoam.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.erp.sheelafoam.R;
import com.erp.sheelafoam.models.EmpDetails;
import com.erp.sheelafoam.models.StoreList;
import com.erp.sheelafoam.models.StoreListModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by twigz on 12/3/18.
 */

public class EmployeeAdapter extends BaseAdapter{
    private List<EmpDetails> empDetails = new ArrayList<>();
    private Context context;


    public EmployeeAdapter(Context context, List<EmpDetails> empDetails) {
        this.context = context;
        this.empDetails = empDetails;

    }

    @Override
    public int getCount() {
        return empDetails.size();
    }

    @Override
    public Object getItem(int i) {
        return empDetails.get(i);
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
        EmpDetails emp = (EmpDetails) getItem(i);

        viewHolder.complaintName.setText(emp.geteMP_NAME());

        return convertView;

    }


    public class ViewHolder{
        TextView complaintName;

        public ViewHolder(View view){
            complaintName = (TextView) view.findViewById(R.id.complaint_name);
        }
    }

}
