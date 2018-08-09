package com.erp.sheelafoam;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import com.erp.sheelafoam.BE.OrderMenuItems;
import com.erp.sheelafoam.adapter.OrderMenuAdapter;

import java.util.ArrayList;

public class OrderDashboardActivity extends AppCompatActivity {

    public OrderMenuAdapter orderMenuAdapter;
    public Toolbar toolbar;
    public TextView textview_back;
    ArrayList<OrderMenuItems> orderMenuItems;
    RecyclerView menu_grid;
    LinearLayoutManager menuLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_dashboard);
        toolbar = (Toolbar) findViewById(R.id.app_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        textview_back = (TextView) findViewById(R.id.app_toolbar_title);
        textview_back.setText("Order");
        menu_grid = (RecyclerView) findViewById(R.id.menu_listing);
        menuLayout = new GridLayoutManager(this, 3);
        menu_grid.setNestedScrollingEnabled(true);
        menu_grid.setHasFixedSize(true);
        menu_grid.setLayoutManager(menuLayout);
        orderMenuItems = (ArrayList<OrderMenuItems>) getIntent().getSerializableExtra("OrderMenuList");
        orderMenuAdapter = new OrderMenuAdapter(this, orderMenuItems, this.getSupportFragmentManager());
        menu_grid.setAdapter(orderMenuAdapter);
        orderMenuAdapter.notifyDataSetChanged();
        for (int i = 0; i < orderMenuItems.size(); i++) {
            Log.d("Order List Item ODA", orderMenuItems.get(i).getTxt_menu());
        }


    }
}
