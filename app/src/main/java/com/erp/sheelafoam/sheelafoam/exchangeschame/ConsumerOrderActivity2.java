package com.erp.sheelafoam.sheelafoam.exchangeschame;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ListView;

import com.erp.sheelafoam.R;
import com.erp.sheelafoam.sheelafoam.exchangeschame.adapter.ConsumerOrderProductAdapter;
import com.erp.sheelafoam.sheelafoam.exchangeschame.model.ConsumerItemModel;

import java.util.ArrayList;

public class ConsumerOrderActivity2 extends Activity {
    private ConsumerOrderProductAdapter adapter;
    private ListView lv_orderProduct;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cosumer_product_activity);
        lv_orderProduct = (ListView) findViewById(R.id.lv_orderProduct);
        ArrayList<ConsumerItemModel> list1 = new ArrayList<>();
        ConsumerItemModel model = new ConsumerItemModel();
        model.setProductNameValue("Comfort Cell Series 4.0");
       /* model.setLengthValue("50.0(L)" + "*" + "25.0(B)" + "*" + "1(T)");
        model.setQtyValue("2");*/
        model.setTotalAmountValue("5050");
        list1.add(model);
        ConsumerItemModel model2 = new ConsumerItemModel();
        model2.setProductNameValue("Comfort Cell Series 3.0");
       /* model2.setLengthValue("50.0(L)" + "*" + "25.0(B)" + "*" + "1(T)");
        model2.setQtyValue("3");*/
        model2.setTotalAmountValue("5000");
        list1.add(model2);
        ConsumerItemModel model3 = new ConsumerItemModel();
        model3.setProductNameValue("Comfort Cell Series 2.0");
       /* model3.setLengthValue("50.0(L)" + "*" + "25.0(B)" + "*" + "1(T)");
        model3.setQtyValue("4");*/
        model.setTotalAmountValue("5100");
        list1.add(model3);
        ConsumerItemModel model4 = new ConsumerItemModel();
        model4.setProductNameValue("Comfort Cell Series 4.0");
      /*  model4.setLengthValue("51.0(L)" + "*" + "26.0(B)" + "*" + "5(T)");
        model4.setQtyValue("4");*/
        model4.setTotalAmountValue("5000");
        list1.add(model4);
        adapter = new ConsumerOrderProductAdapter(ConsumerOrderActivity2.this, list1);
        lv_orderProduct.setAdapter(adapter);
    }
}
