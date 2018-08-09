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

import com.erp.sheelafoam.BE.HeaderMenuBE;
import com.erp.sheelafoam.R;

import java.util.ArrayList;

/**
 * Created by pranav.mittal on 6/6/2016.
 */

public class NavigationView_NewBottom extends BaseExpandableListAdapter {

    Context _context;
    ArrayList<HeaderMenuBE> base;
    ExpandableListView et;


    public NavigationView_NewBottom(Context context, ArrayList<HeaderMenuBE> base, ExpandableListView et) {
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
        Log.d("Child size", groupPosition + "," + base.get(groupPosition).getSUB_MENU().size() + "");
        return base.get(groupPosition).getSUB_MENU().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return base.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return base.get(groupPosition).getSUB_MENU().get(childPosition);
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
            convertView = infalInflater.inflate(R.layout.navigationview_bottom_row, null);
            et.setDividerHeight(0);
        }

        TextView txt_bottom = (TextView) convertView
                .findViewById(R.id.txt_bottom);
        ImageView image_indicator = (ImageView) convertView.findViewById(R.id.image_indicator);

        txt_bottom.setText(base.get(groupPosition).getCHILD_MENU());

        if (base.get(groupPosition).getSUB_MENU().size() > 0) {
            image_indicator.setVisibility(View.VISIBLE);
        } else {
            image_indicator.setVisibility(View.GONE);
        }

        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.navigationview_bottom_child_row, null);

            et.setDividerHeight(0);
        }

        TextView txt_child_bottom = (TextView) convertView.findViewById(R.id.txt_child_bottom);

        txt_child_bottom.setText(base.get(groupPosition).getSUB_MENU().get(childPosition).getCHILD_MENU());

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
