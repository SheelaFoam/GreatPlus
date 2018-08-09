package com.erp.sheelafoam.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.erp.sheelafoam.BE.HeaderMenuBE;
import com.erp.sheelafoam.HeaderMenuDesc_Web;
import com.erp.sheelafoam.R;
import com.erp.sheelafoam.utils.CustomExpListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ParentLevelAdapter extends BaseExpandableListAdapter {
    private final Context mContext;
    private final List<String> mListDataHeader;
    private final Map<String, List<String>> mListData_SecondLevel_Map;
    private final Map<String, List<String>> mListData_ThirdLevel_Map;
    ArrayList<HeaderMenuBE> base;
    String[] mItemHeaders, mItemHeaders_LINK, mItemChildOfChild_LINK;
    int parentCount;

    public ParentLevelAdapter(Context mContext, List<String> mListDataHeader, ArrayList<HeaderMenuBE> base) {
        this.mContext = mContext;
        this.mListDataHeader = new ArrayList<>();
        this.base = base;
        this.mListDataHeader.addAll(mListDataHeader);

        // SECOND LEVEL

        mListData_SecondLevel_Map = new HashMap<>();
        parentCount = mListDataHeader.size();
        mItemHeaders = new String[base.get(0).getSUB_MENU().size()];
        mItemHeaders_LINK = new String[base.get(0).getSUB_MENU().size()];
        for (int i = 0; i < parentCount; i++) {
            if (base.get(i).getSUB_MENU().size() > 0) {
                for (int j = 0; j < base.get(i).getSUB_MENU().size(); j++) {
                    mItemHeaders[j] = base.get(i).getSUB_MENU().get(j).getCHILD_MENU();
                    mItemHeaders_LINK[j] = base.get(i).getSUB_MENU().get(j).getLINK();
                }
                mListData_SecondLevel_Map.put(mListDataHeader.get(i), Arrays.asList(mItemHeaders));
            }
        }

        // THIRD LEVEL

        List<String> listChild;
        mListData_ThirdLevel_Map = new HashMap<>();
        for (Object o : mListData_SecondLevel_Map.entrySet()) {
            Map.Entry entry = (Map.Entry) o;
            Object object = entry.getValue();
            if (object instanceof List) {
                List<String> stringList = new ArrayList<>();
                Collections.addAll(stringList, (String[]) ((List) object).toArray());
                for (int i = 0; i < stringList.size(); i++) {
                    if (base.get(0).getSUB_MENU().get(i).getSUB_MENU().size() > 0) {
                        String[] mItemChildOfChild = new String[base.get(0).getSUB_MENU().get(i).getSUB_MENU().size()];
                        String[] mItemChildOfChild_LINK = new String[base.get(0).getSUB_MENU().get(i).getSUB_MENU().size()];
                        for (int j = 0; j < base.get(0).getSUB_MENU().get(i).getSUB_MENU().size(); j++) {
                            mItemChildOfChild[j] = base.get(0).getSUB_MENU().get(i).getSUB_MENU().get(j).getCHILD_MENU();
                            mItemChildOfChild_LINK[j] = base.get(0).getSUB_MENU().get(i).getSUB_MENU().get(j).getLINK();
                        }
                        listChild = Arrays.asList(mItemChildOfChild);
                        mListData_ThirdLevel_Map.put(stringList.get(i), listChild);
                    }
                }
            }
        }
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final CustomExpListView secondLevelExpListView = new CustomExpListView(this.mContext);
        String parentNode = (String) getGroup(groupPosition);
        secondLevelExpListView.setAdapter(new SecondLevelAdapter(this.mContext, mListData_SecondLevel_Map.get(parentNode), mListData_ThirdLevel_Map, base));
        secondLevelExpListView.setGroupIndicator(null);

        secondLevelExpListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                if (base.get(0).getSUB_MENU().get(groupPosition).getSUB_MENU().size() > 0) {
                } else {
                    Intent in = new Intent(mContext, HeaderMenuDesc_Web.class);
                    in.putExtra("WebUrl", mItemHeaders_LINK[groupPosition]);
                    mContext.startActivity(in);
                    Toast.makeText(mContext, "Position" + groupPosition, Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });

        secondLevelExpListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
            /*    if (mItemHeaders_LINK[childPosition] != null) {

                }
                Intent in = new Intent(mContext, HeaderMenuDesc_Web.class);
                in.putExtra("WebUrl", mItemChildOfChild_LINK[childPosition]);
                mContext.startActivity(in);*/

                Toast.makeText(mContext, "Position" + childPosition, Toast.LENGTH_SHORT).show();

                return false;
            }
        });


        return secondLevelExpListView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.mListDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
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
            convertView = layoutInflater.inflate(R.layout.navigationview_bottom_row, parent, false);
        }
        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.txt_bottom);
       /* lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setTextColor(Color.CYAN);*/
        lblListHeader.setText(headerTitle);

        ImageView image_indicator = (ImageView) convertView.findViewById(R.id.image_indicator);

        if (base.get(groupPosition).getSUB_MENU().size() > 0) {
            image_indicator.setVisibility(View.VISIBLE);
        } else {
            image_indicator.setVisibility(View.GONE);
        }


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
