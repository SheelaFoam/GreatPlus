package com.erp.sheelafoam.sheelafoam.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.erp.sheelafoam.R;
import com.erp.sheelafoam.utils.CustomTypefaceTextView;

import org.json.JSONException;
import org.json.JSONObject;

public class BumperOfferActivity extends Activity {

    private ImageView ivBumper, ivCancel;
    private CustomTypefaceTextView tvDealer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bumper_offer);
        ivBumper = (ImageView) findViewById(R.id.ivBumper);
        ivCancel = (ImageView) findViewById(R.id.ivCancel);
        tvDealer = (CustomTypefaceTextView) findViewById(R.id.tvDealer);
        try {
            String _json = getIntent().getStringExtra("BumperObject");

            JSONObject _object = new JSONObject(_json);
            String image = _object.getString("BUMBER_PRIZE");
            tvDealer.setText(_object.getString("DEALER_NAME") + " \n has won \n" + _object.getString("BUMBER_PRIZE"));
            if ("Honda Activa Scooter".equalsIgnoreCase(image)) {
                ivBumper.setImageResource(R.drawable.activa);
            } else if ("Honda Amaze Car".equalsIgnoreCase(image)) {
                ivBumper.setImageResource(R.drawable.amaze);
            } else if ("Comforter - Flora (all Season)".equalsIgnoreCase(image)) {
                ivBumper.setImageResource(R.drawable.comfert_flora);
            } else if ("Comforter - Ivory (all Season)".equalsIgnoreCase(image)) {
                ivBumper.setImageResource(R.drawable.comfort_ivory);
            } else if ("Bed Sheets - Cherish".equalsIgnoreCase(image)) {
                ivBumper.setImageResource(R.drawable.bedsheet);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ivCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BumperOfferActivity.this.finish();
            }
        });
    }

}
