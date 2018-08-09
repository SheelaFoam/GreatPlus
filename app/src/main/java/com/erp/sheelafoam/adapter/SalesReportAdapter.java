package com.erp.sheelafoam.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.erp.sheelafoam.R;
import com.erp.sheelafoam.models.SalesModel;
import com.erp.sheelafoam.utils.CustomTypefaceEditText;
import com.erp.sheelafoam.utils.CustomTypefaceTextView;

import java.util.List;

public class SalesReportAdapter extends BaseAdapter {
    private Context context;
    private List<SalesModel> array;
    private LayoutInflater inflater;
    Typeface tf;

    public SalesReportAdapter(Context context, List<SalesModel> array) {
        this.context = context;
        this.array = array;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return array.size();
    }

    @Override
    public SalesModel getItem(int i) {
        return array.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = inflater.inflate(R.layout.sales_report_item, parent, false);
        CustomTypefaceTextView tv_salesRepName = (CustomTypefaceTextView) convertView.findViewById(R.id.tv_salesRepName);
        tv_salesRepName.setText(getItem(i).getDLRSALESREPNAME());
        CustomTypefaceTextView tv_salesPSpecification = (CustomTypefaceTextView) convertView.findViewById(R.id.tv_salesPSpecification);
        if (getItem(i).getPRODUCTSPECIFICATION() == null || getItem(i).getPRODUCTSPECIFICATION().equalsIgnoreCase("null")) {
            tv_salesPSpecification.setText("N/A");
        } else {
            tv_salesPSpecification.setText(getItem(i).getPRODUCTSPECIFICATION());
        }
        CustomTypefaceTextView tv_salesReportBredth = (CustomTypefaceTextView) convertView.findViewById(R.id.tv_salesReportBredth);
        if (getItem(i).getBREDTH() == null || getItem(i).getBREDTH().equalsIgnoreCase("null")) {
            tv_salesReportBredth.setText("N/A");
        } else {
            tv_salesReportBredth.setText(getItem(i).getBREDTH());
        }

        CustomTypefaceTextView tv_salesReportLenght = (CustomTypefaceTextView) convertView.findViewById(R.id.tv_salesReportLenght);
        if (getItem(i).getLENGTH() == null || getItem(i).getLENGTH().equalsIgnoreCase("null")) {
            tv_salesReportLenght.setText("N/A");
        } else {
            tv_salesReportLenght.setText(getItem(i).getLENGTH());
        }
        CustomTypefaceTextView tv_salesReportCount = (CustomTypefaceTextView) convertView.findViewById(R.id.tv_salesReportCount);
        if (getItem(i).getCOUNT1() == null || getItem(i).getCOUNT1().equalsIgnoreCase("null")) {
            tv_salesReportCount.setText("0");
        } else {
            tv_salesReportCount.setText(getItem(i).getCOUNT1());
        }
        CustomTypefaceTextView tv_salesReportThikness= (CustomTypefaceTextView) convertView.findViewById(R.id.tv_salesReportThikness);
        if(getItem(i).getTHICK()==null || getItem(i).getTHICK().equalsIgnoreCase("null")){
            tv_salesReportThikness.setText("0");

        }else {
            tv_salesReportThikness.setText(getItem(i).getTHICK());
        }
        return convertView;
    }
}
