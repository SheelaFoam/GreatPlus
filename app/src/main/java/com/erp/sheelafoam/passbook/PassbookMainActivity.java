package com.erp.sheelafoam.passbook;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.erp.sheelafoam.R;
import com.erp.sheelafoam.passbook.fragment.BaseFragment;
import com.erp.sheelafoam.passbook.fragment.CashAmount;
import com.erp.sheelafoam.passbook.fragment.Eocb;
import com.erp.sheelafoam.passbook.fragment.ParivartanStar;
import com.erp.sheelafoam.passbook.fragment.Prize;
import com.erp.sheelafoam.passbook.model.FragmentWithTitle;
import com.github.florent37.materialviewpager.MaterialViewPager;
import com.github.florent37.materialviewpager.header.HeaderDesign;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PassbookMainActivity extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.materialViewPager)
    MaterialViewPager mViewPager;
    private List<FragmentWithTitle> list = new ArrayList<>();
    private ImageView iv_filter;
    private Button filter_btn;
    private EditText et_ToDate, et_fromDate;
    public static TextView tv_toDate, tv_fromDate;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private int fromDay, ToDay, FromMonth, ToMonth, FromYear, ToYear;
    private MainPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.passbook_main_activity);
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        ToYear = mYear;
        FromYear = mYear;

        mMonth = c.get(Calendar.MONTH);
        FromMonth = mMonth;
        ToMonth = mMonth;
        mDay = c.get(Calendar.DAY_OF_MONTH);
        fromDay = mDay - 1;
        ToDay = mDay;
        iv_filter = (ImageView) findViewById(R.id.iv_filter);
        tv_toDate = (TextView) findViewById(R.id.tv_toDate);
        tv_fromDate = (TextView) findViewById(R.id.tv_fromDate);
        tv_toDate.setText("TO " + ToDay + "-" + (ToMonth+1) + "-" + ToYear);
        tv_fromDate.setText("From " + fromDay + "-" + (FromMonth+1) + "-" + FromYear);
        iv_filter.setOnClickListener(this);
        setTitle("");
        ButterKnife.bind(this);
        list.add(new FragmentWithTitle(new Eocb(), "Eocb"));
        list.add(new FragmentWithTitle(new CashAmount(), "CASH AMOUNT"));
        list.add(new FragmentWithTitle(new ParivartanStar(), "PARIVARTAN STAR"));
        list.add(new FragmentWithTitle(new Prize(), "PRIZES"));
        final Toolbar toolbar = mViewPager.getToolbar();
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
        adapter = new MainPagerAdapter(getSupportFragmentManager());
        mViewPager.getViewPager().setAdapter(adapter);


        mViewPager.setMaterialViewPagerListener(new MaterialViewPager.Listener() {
            @Override
            public HeaderDesign getHeaderDesign(int page) {
                switch (page) {
                    case 0:
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.sleepwell4,
                                "");
                    case 1:
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.sleepwell7,
                                "");
                    case 2:
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.sleepwell3,
                                "");
                    case 3:
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.sleepwellcolor5,
                                "");
                }

                return null;
            }
        });

        mViewPager.getViewPager().setOffscreenPageLimit(mViewPager.getViewPager().getAdapter().getCount());
        mViewPager.getPagerTitleStrip().setViewPager(mViewPager.getViewPager());
        mViewPager.getPagerTitleStrip().setOnPageChangeListener(new PagerAdapterListener());
        mViewPager.post(new Runnable() {
            @Override
            public void run() {
                ((Eocb) adapter.getItem(0)).onVisible();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_filter:


                final Dialog dialog = new Dialog(PassbookMainActivity.this);
                dialog.setContentView(R.layout.filter_dialog);
                filter_btn = (Button) dialog.findViewById(R.id.filter_btn);
                et_fromDate = (EditText) dialog.findViewById(R.id.et_fromDate);
                et_ToDate = (EditText) dialog.findViewById(R.id.et_ToDate);
                et_ToDate.setText(tv_toDate.getText().toString().substring(3));
                et_fromDate.setText(tv_fromDate.getText().toString().substring(5));
                et_ToDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        DatePickerDialog datePickerDialog = new DatePickerDialog(PassbookMainActivity.this,R.style.DialogTheme,
                                new DatePickerDialog.OnDateSetListener() {

                                    @Override
                                    public void onDateSet(DatePicker view, int year,
                                                          int monthOfYear, int dayOfMonth) {

                                        et_ToDate.setText(dayOfMonth + "-" +(monthOfYear+1)  + "-" + year);
                                        ToDay = dayOfMonth;
                                        ToMonth = monthOfYear+1;
                                        ToYear = year;
                                    }
                                }, mYear, mMonth, mDay);
                        datePickerDialog.show();
                    }
                });
                et_fromDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Calendar c = Calendar.getInstance();
                        mYear = c.get(Calendar.YEAR);
                        mMonth = c.get(Calendar.MONTH);
                        mDay = c.get(Calendar.DAY_OF_MONTH) - 1;
                        DatePickerDialog datePickerDialog = new DatePickerDialog(PassbookMainActivity.this,R.style.DialogTheme,
                                new DatePickerDialog.OnDateSetListener() {

                                    @Override
                                    public void onDateSet(DatePicker view, int year,
                                                          int monthOfYear, int dayOfMonth) {


                                        et_fromDate.setText(dayOfMonth + "-" +(monthOfYear+1)  + "-" + year);
                                        fromDay = dayOfMonth;
                                        FromMonth = monthOfYear+1;
                                        FromYear = year;


                                    }
                                }, mYear, mMonth, mDay);
                        datePickerDialog.show();
                    }
                });

                filter_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String toDate = ToDay + "-" + ToMonth + "-" + ToYear;
                        String fromDate = fromDay + "-" + FromMonth + "-" + FromYear;
                        tv_toDate.setText("TO " + et_ToDate.getText().toString());
                        tv_fromDate.setText("From " + et_fromDate.getText().toString());
                        dialog.dismiss();
                        if (adapter != null) {
                            BaseFragment baseFragment = (BaseFragment) adapter.getItem(mViewPager.getViewPager().getCurrentItem());
                            baseFragment.filterData(fromDate, toDate);
                        }

                    }
                });
                dialog.show();

                break;
        }
    }



    private class MainPagerAdapter extends FragmentStatePagerAdapter {

        public MainPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return list.get(position).fragment;
        }

        @Override
        public int getCount() {
            return list.size();
        }


        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return list.get(position).title;
        }
    }

    private class PagerAdapterListener extends ViewPager.SimpleOnPageChangeListener {

        @Override
        public void onPageSelected(int position) {
            super.onPageSelected(position);
            BaseFragment fragment = list.get(position).fragment;
            fragment.onVisible();
        }
    }
}