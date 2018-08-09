package com.erp.sheelafoam.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;

import com.erp.sheelafoam.R;
import com.erp.sheelafoam.models.ShowRoomModelOne;
import com.erp.sheelafoam.utils.CustomTypefaceTextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ShowRoomListViewAdapter extends BaseAdapter {
    Typeface tf;
    ShowRoomModelOne model;
    private Context context;
    private List<ShowRoomModelOne> array;
    private LayoutInflater inflater;

    public ShowRoomListViewAdapter(Context context, List<ShowRoomModelOne> array) {
        this.context = context;
        this.array = array;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return array.size();
    }

    @Override
    public ShowRoomModelOne getItem(int i) {
        return array.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = inflater.inflate(R.layout.showroom_listview_item, parent, false);
        model = array.get(i);
        CustomTypefaceTextView tv_showroom_list_date = (CustomTypefaceTextView) convertView.findViewById(R.id.tv_shooroom_item_title1);
        //tv_showroom_list_date.setText(model.getDate());
        tv_showroom_list_date.setText( formatDateFromstring("yyyy-MM-dd", "dd-MMM-yyyy", model.getDate()));
        if (model.getImageList() != null && model.getImageList().size() > 0) {
            GridView gridview_feedbackList = (GridView) convertView.findViewById(R.id.gridview_feedbackList);
            gridview_feedbackList.setAdapter(new ShowRoomImageAdapter(context, model.getImageList()));

        }
        return convertView;
    }
    public static String formatDateFromstring(String inputFormat, String outputFormat, String inputDate) {
        Date parsed = null;
        String outputDate = "";
        SimpleDateFormat df_input = new SimpleDateFormat(inputFormat, java.util.Locale.getDefault());
        SimpleDateFormat df_output = new SimpleDateFormat(outputFormat, java.util.Locale.getDefault());
        try {
            parsed = df_input.parse(inputDate);
            outputDate = df_output.format(parsed);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return outputDate;
    }


}
