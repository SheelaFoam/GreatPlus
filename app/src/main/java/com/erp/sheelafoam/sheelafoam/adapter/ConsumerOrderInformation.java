package com.erp.sheelafoam.sheelafoam.adapter;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.erp.sheelafoam.R;
import com.erp.sheelafoam.prefrences.SheelaSharedPreference;
import com.erp.sheelafoam.utils.CommonClass;

public class ConsumerOrderInformation extends Activity {
    private TextView tv_logoname,tv_emptyText;
    private SheelaSharedPreference preference;
    private CommonClass commonClass;
    private ImageButton ib_backicon, ib_barcode;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_product_info);
        ib_backicon = (ImageButton) findViewById(R.id.ib_backicon);
        tv_logoname = (TextView) findViewById(R.id.tv_logoname);
        //tv_logoname.setText("MTS Report");
        ib_barcode = (ImageButton) findViewById(R.id.ib_barcode);
        ib_barcode.setVisibility(View.VISIBLE);
    }
}
