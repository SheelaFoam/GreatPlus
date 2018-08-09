package com.erp.sheelafoam.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.erp.sheelafoam.HomeScreen;
import com.erp.sheelafoam.R;
import com.erp.sheelafoam.UserLogAPI;
import com.erp.sheelafoam.constant.Constant;
import com.erp.sheelafoam.utils.AlertDialogManager;
import com.erp.sheelafoam.utils.ConnectionDetector;
import com.erp.sheelafoam.utils.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by priyanka.sharma on 12/1/2016.
 */

public class CompanyPerformanceFragmnt extends Fragment {
    LinearLayoutManager layoutManager;
    ConnectionDetector cDetector;
    AlertDialogManager alert;
    private TabLayout tabLayout;
    TextView tp_txt, rcv_txt;
    private ViewPager viewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sidemenu_company_performing
                , container, false);
        new UserLogAPI("Company Performance Page", getActivity());
        init(view);

        return view;
    }

    private void init(View view) {
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        rcv_txt = (TextView) view.findViewById(R.id.rcv_txt);
        tp_txt = (TextView) view.findViewById(R.id.tp_txt);

        tp_txt.setText(Util.getSharedPrefrenceValue(getActivity(), Constant.SP_tp_name));
        rcv_txt.setText(Util.getSharedPrefrenceValue(getActivity(), Constant.SP_rcv_name));

        createViewPager(viewPager);

        ((HomeScreen) getActivity()).txt_title.setVisibility(View.VISIBLE);
        ((HomeScreen) getActivity()).txt_title.setText("Company Performance");
        ((HomeScreen) getActivity()).profile_image.setVisibility(View.GONE);

        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        createTabIcons();
    }

    private void createTabIcons() {

        TextView tabOne = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.custom_tab, null);
        tabOne.setText("THIS WEEK");
        tabLayout.getTabAt(0).setCustomView(tabOne);

        TextView tabTwo = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.custom_tab, null);
        tabTwo.setText("YTD");
        tabLayout.getTabAt(1).setCustomView(tabTwo);
    }

    private void createViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());
        adapter.addFrag(new CompanyPerformance_ThisWeek(), "THIS WEEK");
        adapter.addFrag(new CompanyPerformance_YTD(), "YTD");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

}
