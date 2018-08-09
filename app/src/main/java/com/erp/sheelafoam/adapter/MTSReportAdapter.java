package com.erp.sheelafoam.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.erp.sheelafoam.R;
import com.erp.sheelafoam.models.MTSModel;
import com.erp.sheelafoam.utils.CustomTypefaceTextView;

import java.util.List;

/**
 * Created by E6036 on 5/3/2018.
 */

public class MTSReportAdapter extends BaseAdapter {
    private Context context;
    private List<MTSModel> array;
    private LayoutInflater inflater;
    Typeface tf;

    public MTSReportAdapter(Context context, List<MTSModel> array) {
        this.context = context;
        this.array = array;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return array.size();
    }

    @Override
    public MTSModel getItem(int i) {
        return array.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = inflater.inflate(R.layout.mts_report_item, parent, false);
        CustomTypefaceTextView tv_modeName = (CustomTypefaceTextView) convertView.findViewById(R.id.tv_modeName);
        if(getItem(i).getPRODUCTDISPLAYNAME()==null||getItem(i).getPRODUCTDISPLAYNAME().equalsIgnoreCase("null")||getItem(i).getPRODUCTDISPLAYNAME().equalsIgnoreCase(null)){
            tv_modeName.setText("N/A");
        }else {
            tv_modeName.setText(getItem(i).getPRODUCTDISPLAYNAME());
        }
        CustomTypefaceTextView tv_bredth = (CustomTypefaceTextView) convertView.findViewById(R.id.tv_bredth);
        if (getItem(i).getBREDTH() == null || getItem(i).getBREDTH().equalsIgnoreCase("null")) {
            tv_bredth.setText("N/A");
        } else {
            tv_bredth.setText(getItem(i).getBREDTH());
        }
        CustomTypefaceTextView tv_thick = (CustomTypefaceTextView) convertView.findViewById(R.id.tv_thick);
        if (getItem(i).getTHICK() == null || getItem(i).getTHICK().equalsIgnoreCase("null")) {
            tv_thick.setText("N/A");
        } else {
            tv_thick.setText(getItem(i).getTHICK());
        }

        CustomTypefaceTextView tv_color = (CustomTypefaceTextView) convertView.findViewById(R.id.tv_color);
        if (getItem(i).getCOLOR() == null || getItem(i).getCOLOR().equalsIgnoreCase("null")) {
            tv_color.setText("N/A");
        } else {
            tv_color.setText(getItem(i).getCOLOR());
        }
        CustomTypefaceTextView tv_pscValue = (CustomTypefaceTextView) convertView.findViewById(R.id.tv_pscValue);
        if (getItem(i).getCURRSTOCK() == null || getItem(i).getCURRSTOCK().equalsIgnoreCase("null")) {
            tv_pscValue.setText("N/A");
        } else {
            tv_pscValue.setText(getItem(i).getCURRSTOCK());
        }
        CustomTypefaceTextView tv_lenghtValue = (CustomTypefaceTextView) convertView.findViewById(R.id.tv_lenghtValue);
        if (getItem(i).getLENGTH() == null || getItem(i).getLENGTH().equalsIgnoreCase("null")) {
            tv_lenghtValue.setText("N/A");
        } else {
            tv_lenghtValue.setText(getItem(i).getLENGTH());
        }

        return convertView;
    }
}
