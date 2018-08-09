package com.erp.sheelafoam.sheelafoam.exchangeschame;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.erp.sheelafoam.R;
import com.erp.sheelafoam.adapter.ProductAdapter;
import com.erp.sheelafoam.adapter.ProductRecyclerAdapter;
import com.erp.sheelafoam.models.ProductCategoryModel;
import com.erp.sheelafoam.models.SalesModel;
import com.erp.sheelafoam.utils.CustomTypefaceTextView;

import java.util.ArrayList;
import java.util.List;

public class ProductMainActivity extends Activity implements View.OnClickListener {
    TextView tv_total_item, tv_total_amount, tv_checkout;
    CustomTypefaceTextView tv_cat1, tv_cat2, tv_cat3, tv_cat4;
    ListView lv_cat1, lv_cat2, lv_cat3, lv_cat4;
    SalesModel model;
    ProductAdapter adapter;
    RecyclerView product_recyclerView;
    TextView checkout, checkout_amount, item_count;
    int count = 0;
    int totalAmount = 0;
    private ImageButton ib_backicon_new, iv_dot_new;
    private TextView tv_logoname, tv_emptyText;
    private int total_item_count, total_amount;
    private List<SalesModel> list = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_main_activity);
        product_recyclerView = (RecyclerView) findViewById(R.id.product_recyclerView);
        ib_backicon_new = (ImageButton) findViewById(R.id.ib_backicon_new);
        tv_logoname = (TextView) findViewById(R.id.tv_logoname_new);
        tv_logoname.setText("PRODUCT");
        checkout = (TextView) findViewById(R.id.checkout);
        checkout_amount = (TextView) findViewById(R.id.checkout_amount);
        item_count = (TextView) findViewById(R.id.item_count);

        ArrayList<SalesModel> arrayList = new ArrayList<>();
        ArrayList<SalesModel> arrayList1 = new ArrayList<>();
        SalesModel model = new SalesModel();
        model.setBREDTH("ITEM NAME1");
        model.setLENGTH("ITEM DESCRIPTION vasgch ajgshd");
        model.setCOUNT1("550");
        arrayList.add(model);
        arrayList1.add(model.clone());
        SalesModel model1 = new SalesModel();
        model1.setBREDTH("ITEM NAME2");
        model1.setLENGTH("ITEM DESCRIPTION vasgch ajgshd");
        model1.setCOUNT1("590");
        arrayList.add(model1);
        arrayList1.add(model1.clone());
        SalesModel model2 = new SalesModel();
        model2.setBREDTH("ITEM NAME3");
        model2.setLENGTH("ITEM DESCRIPTION vasgch ajgshd svachj");
        model2.setCOUNT1("500");
        arrayList.add(model2);
        arrayList1.add(model2.clone());
        SalesModel model3 = new SalesModel();
        model3.setBREDTH("ITEM NAME4");
        model3.setLENGTH("ITEM DESCRIPTION vasgch ajgshd");
        model3.setCOUNT1("50");
        arrayList.add(model3);
        arrayList1.add(model3.clone());
        checkout.setOnClickListener(this);
        List<ProductCategoryModel> list = new ArrayList<>();
        ProductCategoryModel productCategoryModel1 = new ProductCategoryModel();
        productCategoryModel1.setTitle("Category1");
        productCategoryModel1.setSalesModels(arrayList);
        list.add(productCategoryModel1);
        ProductCategoryModel productCategoryModel2 = new ProductCategoryModel();
        productCategoryModel2.setTitle("Category2");
        productCategoryModel2.setSalesModels(arrayList1);
        list.add(productCategoryModel2);
        product_recyclerView.setLayoutManager(new LinearLayoutManager(this));
        product_recyclerView.setAdapter(new ProductRecyclerAdapter(this, list));
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.checkout:
                Intent goToNext = new Intent(ProductMainActivity.this, ProductActivityFirst.class);
                goToNext.putExtra("","");
                startActivity(goToNext);
                break;
        }
    }

    public void updateUI(SalesModel model, boolean isRemove) {
        if (list.size() > 0 && list.contains(model) && isRemove) {
            list.remove(model);
        } else if(!isRemove){
            list.add(model);
        }

        if(list.size()>0){
            item_count.setText("" + list.size());
            int total=0;
            for (SalesModel mmodel:
                 list) {
               total=total+Integer.valueOf(mmodel.getCOUNT1());
            }
            checkout_amount.setText("" + total);
        }else{
            checkout_amount.setText("" + 0);
            item_count.setText("" + 0);
        }

    }
}
