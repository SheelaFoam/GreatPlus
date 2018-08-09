package com.erp.sheelafoam.sheelafoam.exchangeschame;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.erp.sheelafoam.R;
import com.erp.sheelafoam.sheelafoam.activity.CustomerFeedbackActivity;

public class ProductActivityFirst extends Activity {
    private EditText et_nameProduct,et_emailProduct,et_mobileProduct;
    private ImageButton ib_backicon_new, iv_dot_new;
    private TextView tv_logoname, tv_emptyText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_activity_first);
        ib_backicon_new = (ImageButton) findViewById(R.id.ib_backicon_new);
        tv_logoname = (TextView) findViewById(R.id.tv_logoname_new);
        tv_logoname.setText("Product");
        et_mobileProduct= (EditText) findViewById(R.id.et_mobileProduct);
        et_emailProduct= (EditText) findViewById(R.id.et_emailProduct);
        et_nameProduct= (EditText) findViewById(R.id.et_nameProduct);
    }
    private boolean isValid() {

        if (et_nameProduct.getText().toString().isEmpty()) {
            Toast.makeText(ProductActivityFirst.this, "Please enter your name !", Toast.LENGTH_LONG).show();
            return false;
        } else if (et_emailProduct.getText().toString().isEmpty()) {
            Toast.makeText(ProductActivityFirst.this, "Please enter valid email ID !", Toast.LENGTH_LONG).show();
            return false;
        } else if (et_mobileProduct.getText().toString().length()==0) {
            Toast.makeText(ProductActivityFirst.this, "Please enter valid Mobile number !", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

}
