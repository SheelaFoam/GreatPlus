package com.erp.sheelafoam.sheelafoam.exchangeschame;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.erp.sheelafoam.R;
import com.erp.sheelafoam.sheelafoam.exchangeschame.fragments.ConsumerDetailFragment;
import com.erp.sheelafoam.sheelafoam.exchangeschame.fragments.OrderConfirmationFragment;
import com.erp.sheelafoam.sheelafoam.exchangeschame.fragments.ProductDetailFragment;
import com.erp.sheelafoam.sheelafoam.exchangeschame.model.FragmentModel;

import java.util.ArrayList;
import java.util.List;

public class ProductInfoTabActivity extends AppCompatActivity {
    private TabLayout tabs;
    public ViewPager viewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_info);
        tabs = (TabLayout) findViewById(R.id.tabs);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setOffscreenPageLimit(2);
        List<FragmentModel> list = new ArrayList<>();
        list.add(new FragmentModel(new ConsumerDetailFragment(), "Consumer Details"));
        list.add((new FragmentModel(new ProductDetailFragment(), "Product Details")));
        list.add((new FragmentModel(new OrderConfirmationFragment(), "Order Confirmation")));
        viewPager.setAdapter(new ProductAdapter(getSupportFragmentManager(), list));
        tabs.setupWithViewPager(viewPager);

    }

    private class ProductAdapter extends FragmentPagerAdapter {
        private List<FragmentModel> list;

        public ProductAdapter(FragmentManager fm, List<FragmentModel> list) {
            super(fm);
            this.list = list;
        }

        @Override
        public Fragment getItem(int position) {
            return list.get(position).getFragment();
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return list.get(position).getTitle();
        }
    }
}
