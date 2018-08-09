package com.erp.sheelafoam.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.erp.sheelafoam.R;
import com.erp.sheelafoam.models.CustomerListFedItemModel;
import com.erp.sheelafoam.utils.CustomTypefaceTextView;

import java.util.List;

public class CustomerFeedBackListAdapter extends BaseAdapter {
    Typeface tf;
    private Context context;
    private List<CustomerListFedItemModel> array;
    private LayoutInflater inflater;

    public CustomerFeedBackListAdapter(Context context, List<CustomerListFedItemModel> array) {
        this.context = context;
        this.array = array;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return array.size();
    }

    @Override
    public CustomerListFedItemModel getItem(int i) {
        return array.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = inflater.inflate(R.layout.cutomer_feedback_list_item, parent, false);
        CustomerListFedItemModel model = array.get(i);
        CustomTypefaceTextView tv_CFLName = (CustomTypefaceTextView) convertView.findViewById(R.id.tv_CFLName);
        CustomTypefaceTextView tv_CFLMobile = (CustomTypefaceTextView) convertView.findViewById(R.id.tv_CFLMobile);
        CustomTypefaceTextView tv_CFLDate = (CustomTypefaceTextView) convertView.findViewById(R.id.tv_CFLDate);
        CustomTypefaceTextView tv_CFLEmployee = (CustomTypefaceTextView) convertView.findViewById(R.id.tv_CFLEmployee);
        CustomTypefaceTextView tv_CFLStore = (CustomTypefaceTextView) convertView.findViewById(R.id.tv_CFLStore);
        CustomTypefaceTextView tv_CFLGender = (CustomTypefaceTextView) convertView.findViewById(R.id.tv_CFLGender);
        CustomTypefaceTextView tv_CFLDateOfBirth = (CustomTypefaceTextView) convertView.findViewById(R.id.tv_CFLDateOfBirth);
        CustomTypefaceTextView tv_CFLEmail = (CustomTypefaceTextView) convertView.findViewById(R.id.tv_CFLEmail);
        CustomTypefaceTextView tv_CFLHowIH = (CustomTypefaceTextView) convertView.findViewById(R.id.tv_CFLHowIH);
        CustomTypefaceTextView tv_CFLPurpose = (CustomTypefaceTextView) convertView.findViewById(R.id.tv_CFLPurpose);
        CustomTypefaceTextView tv_CFLInterestedIn = (CustomTypefaceTextView) convertView.findViewById(R.id.tv_CFLInterestedIn);
        CustomTypefaceTextView tv_CFLCurrentMattress = (CustomTypefaceTextView) convertView.findViewById(R.id.tv_CFLCurrentMattress);
        CustomTypefaceTextView tv_CFLAgeing = (CustomTypefaceTextView) convertView.findViewById(R.id.tv_CFLAgeing);
        tv_CFLName.setText(model.getFirstName() + "" + model.getLastName());
        tv_CFLMobile.setText(model.getMobile());
        tv_CFLDate.setText(model.getDateOfVisit());
        tv_CFLEmployee.setText(model.getStoreName());
        tv_CFLStore.setText(model.getStoreId());
        tv_CFLGender.setText(model.getGender());
        tv_CFLDateOfBirth.setText(model.getDob());
        tv_CFLEmail.setText(model.getEmail());
        tv_CFLHowIH.setText(model.getHowYouHeard());
        tv_CFLPurpose.setText(model.getPurposeOfVisit());
        tv_CFLInterestedIn.setText(model.getInterestedIn());
        tv_CFLCurrentMattress.setText(model.getCurrentMattressBrand());
      /*  CustomTypefaceTextView tv_lenghtValue = (CustomTypefaceTextView) convertView.findViewById(R.id.tv_lenghtValue);
        if (getItem(i).getLENGTH() == null || getItem(i).getLENGTH().equalsIgnoreCase("null")) {
            tv_lenghtValue.setText("N/A");
        } else {
            tv_lenghtValue.setText(getItem(i).getLENGTH());
        }*/

        return convertView;
    }
}