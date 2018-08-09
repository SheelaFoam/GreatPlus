package com.erp.sheelafoam.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.erp.sheelafoam.BE.HeaderMenuBE;
import com.erp.sheelafoam.Picasso;
import com.erp.sheelafoam.R;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SecondLevelAdapter extends BaseExpandableListAdapter {
    private final Context mContext;
    private final List<String> mListDataHeader;
    private final Map<String, List<String>> mListDataChild;
    ArrayList<HeaderMenuBE> base;

    public SecondLevelAdapter(Context mContext, List<String> mListDataHeader, Map<String, List<String>> mListDataChild, ArrayList<HeaderMenuBE> base) {
        this.mContext = mContext;
        this.mListDataHeader = mListDataHeader;
        this.mListDataChild = mListDataChild;
        this.base = base;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.mListDataChild.get(this.mListDataHeader.get(groupPosition))
                .get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.navigationview_bottom_child_third_row, parent, false);
        }
        TextView txtListChild = (TextView) convertView
                .findViewById(R.id.txt_child_third_bottom);
        txtListChild.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
        txtListChild.setText(childText);

        ImageView img_child_third_bottom = (ImageView) convertView.findViewById(R.id.img_child_third_bottom);

        if (base.get(0).getSUB_MENU().get(childPosition).getICON().isEmpty()) {
            Picasso.with(mContext).load(R.drawable.ic_right_arrow).into(img_child_third_bottom);
        } else {
            String url = base.get(0).getSUB_MENU().get(groupPosition).getSUB_MENU().get(childPosition).getICON();
            Picasso.with(mContext).load(base.get(0).getSUB_MENU().get(groupPosition).getSUB_MENU().get(childPosition).getICON()).into(img_child_third_bottom);
        }

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        try {
            return this.mListDataChild.get(this.mListDataHeader.get(groupPosition)).size();
        } catch (Exception e) {
            return 0;
        }
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.mListDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        if (mListDataHeader == null)
            return 0;
        return this.mListDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.navigationview_bottom_child_row, parent, false);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.txt_child_bottom);
        ImageView img_child_bottom = (ImageView) convertView.findViewById(R.id.img_child_bottom);
        if (base.get(0).getSUB_MENU().get(groupPosition).getICON().isEmpty()) {
            Picasso.with(mContext).load(R.drawable.ic_right_arrow).into(img_child_bottom);
        } else {
            Picasso.with(mContext).load(base.get(0).getSUB_MENU().get(groupPosition).getICON()).into(img_child_bottom);
        }

        ImageView image_indicator_third = (ImageView) convertView.findViewById(R.id.image_indicator_third);

        if (base.get(0).getSUB_MENU().get(groupPosition).getCHILD_MENU().equals("Option 3")) {
            image_indicator_third.setVisibility(View.VISIBLE);

            lblListHeader.setTypeface(null, Typeface.BOLD);
            lblListHeader.setTextColor(this.mContext.getResources().getColor(R.color.black));
            lblListHeader.setText(headerTitle);
        } else {
            image_indicator_third.setVisibility(View.GONE);
            lblListHeader.setTypeface(null, Typeface.NORMAL);
            lblListHeader.setTextColor(this.mContext.getResources().getColor(R.color.drawerBottomTextcolor));
            lblListHeader.setText(headerTitle);
        }

    /*    lblListHeader.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
        lblListHeader.setTextColor(Color.YELLOW);*/
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
