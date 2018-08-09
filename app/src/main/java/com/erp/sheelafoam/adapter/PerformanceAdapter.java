package com.erp.sheelafoam.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.erp.sheelafoam.BE.MyPerformanceBE;
import com.erp.sheelafoam.R;

import java.util.ArrayList;

/**
 * Created by pranav.mittal on 6/6/2016.
 */

public class PerformanceAdapter extends BaseExpandableListAdapter {

    Context _context;
    ArrayList<MyPerformanceBE> base;
    ExpandableListView et;


    public PerformanceAdapter(Context context, ArrayList<MyPerformanceBE> base, ExpandableListView et) {
        _context = context;
        this.base = base;
        this.et = et;
    }

    @Override
    public int getGroupCount() {
        return base.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        Log.d("Child size", groupPosition + "," + base.get(groupPosition).getPerformace_detail().size() + "");
        return base.get(groupPosition).getPerformace_detail().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return base.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return base.get(groupPosition).getPerformace_detail().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.sidemenu_myperformance_groupitem, null);
            et.setDividerHeight(0);
        }

        ExpandableListView mExpandableListView = (ExpandableListView) parent;
        mExpandableListView.expandGroup(groupPosition);

        TextView kpi_value = (TextView) convertView
                .findViewById(R.id.kpi_value);

        TextView weightage = (TextView) convertView.findViewById(R.id.weightage);
        TextView ach_detail = (TextView) convertView.findViewById(R.id.ach_detail);
        ImageView iv_percentage = (ImageView) convertView.findViewById(R.id.iv_percentage);
        TextView target = (TextView) convertView.findViewById(R.id.target);

        if (Integer.valueOf(base.get(groupPosition).getACH_DETAIL()) <= 25) {
            iv_percentage.setBackgroundResource(R.drawable.red);
        } else if (Integer.valueOf(base.get(groupPosition).getACH_DETAIL()) > 25 && Integer.valueOf(base.get(groupPosition).getACH_DETAIL()) <= 50) {
            iv_percentage.setBackgroundResource(R.drawable.orange);
        } else if (Integer.valueOf(base.get(groupPosition).getACH_DETAIL()) > 50) {
            iv_percentage.setBackgroundResource(R.drawable.green);
        }

        weightage.setText(base.get(groupPosition).getWEIGHTAGE());
        target.setText(base.get(groupPosition).getTARGET());
        kpi_value.setText(base.get(groupPosition).getKPI());
        ach_detail.setText(base.get(groupPosition).getACH_DETAIL() + "%");

        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.sidemenu_myperformance_childitem, null);

            et.setDividerHeight(0);
        }

        TextView tvName = (TextView) convertView.findViewById(R.id.child_name);
        TextView tvPercentage = (TextView) convertView.findViewById(R.id.child_percentage);
        ImageView ivImage = (ImageView) convertView.findViewById(R.id.iv_percentage);

        tvName.setText(base.get(groupPosition).getPerformace_detail().get(childPosition).getPRODUCT_SEGMENT());
        tvPercentage.setText(base.get(groupPosition).getPerformace_detail().get(childPosition).getGROWTH() + "%");

        if (base.get(groupPosition).getPerformace_detail().get(childPosition).getLAST_YEAR_SALE() <= 25) {
            ivImage.setBackgroundResource(R.drawable.red);
        } else if (base.get(groupPosition).getPerformace_detail().get(childPosition).getLAST_YEAR_SALE() > 25 && base.get(groupPosition).getPerformace_detail().get(childPosition).getLAST_YEAR_SALE() <= 50) {
            ivImage.setBackgroundResource(R.drawable.orange);
        } else if (base.get(groupPosition).getPerformace_detail().get(childPosition).getLAST_YEAR_SALE() > 50) {
            ivImage.setBackgroundResource(R.drawable.green);
        }

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
