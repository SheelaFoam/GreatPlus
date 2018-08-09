package com.erp.sheelafoam.sheelafoam.xperia.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.erp.sheelafoam.R;
import com.erp.sheelafoam.adapter.SpinAdapter;
import com.erp.sheelafoam.constant.Constant;
import com.erp.sheelafoam.sheelafoam.utility.AsyncTaskListner;
import com.erp.sheelafoam.sheelafoam.utility.ConnectionDetector;
import com.erp.sheelafoam.sheelafoam.utility.GlobalVariables;
import com.erp.sheelafoam.sheelafoam.utility.MyAsyncTask;
import com.erp.sheelafoam.utils.Util;

import org.json.JSONObject;

import java.util.ArrayList;

public class UnregisterGcard extends AppCompatActivity implements AsyncTaskListner {
    android.support.v7.widget.Toolbar toolbar;
    EditText etBundleNumber, etMobileNumber, etSerialNumberOne;
    Spinner spn_reason;
    TextView bundleBtn;
    Button unregisterBtn;
    String serialOne = "";
    int action;
    JSONObject json_request;
    JSONObject json_response;
    ConnectionDetector con;
    String serialNoOne = "", serialNoTwo = "", customerMobileNo = "", customerName = "", customerEmail = "", invoiceNumber = "", invoiceDate = "";
    private ProgressDialog dialog;
    private String alertMessage = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unregister_gcard);

        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.app_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        bundleBtn = (TextView) findViewById(R.id.get);
        unregisterBtn = (Button) findViewById(R.id.btn_unregister);
        bundleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etBundleNumber.getText().toString().isEmpty()) {
                    GlobalVariables.defaultOneButtonDialog(UnregisterGcard.this, "Bundle no is required");
                } else {
                    getSerialNo();
                }
            }
        });

        etBundleNumber = (EditText) findViewById(R.id.etBundleNo);
        etMobileNumber = (EditText) findViewById(R.id.etMobileNo);
        etSerialNumberOne = (EditText) findViewById(R.id.etSerialNo);
        spn_reason = (Spinner) findViewById(R.id.spn_reason);
        con = new ConnectionDetector(UnregisterGcard.this);

        ArrayList<String> unregisterReasons = new ArrayList<>();
        unregisterReasons.add("Select Reason");
        unregisterReasons.add("Size mismatch");
        unregisterReasons.add("Comfort Issue");
        unregisterReasons.add("Incorrect Dispatch");
        unregisterReasons.add("Short Payment");
        spn_reason.setAdapter(new SpinAdapter(UnregisterGcard.this, unregisterReasons));

        serialOne = etSerialNumberOne.getText().toString();

        unregisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GlobalVariables.hideSoftKeyboard(UnregisterGcard.this);

                if (con.isConnectingToInternet()) {
                    if (etSerialNumberOne.getText().toString().length() > 0) {
                        if (etMobileNumber.getText().toString().length() > 0) {
                            if (etMobileNumber.getText().toString().length() == 10) {
                                if (spn_reason.getSelectedItemPosition() != 0) {
                                    submitExchangeSchameDataRequest();
                                } else {
                                    GlobalVariables.defaultOneButtonDialog(UnregisterGcard.this, "Please select reason.");
                                }
                            } else {
                                GlobalVariables.defaultOneButtonDialog(UnregisterGcard.this, "Please enter 10 digit mobile number");
                            }
                        } else {
                            GlobalVariables.defaultOneButtonDialog(UnregisterGcard.this, "Please enter customer mobile number");
                        }
                    } else {
                        GlobalVariables.defaultOneButtonDialog(UnregisterGcard.this, "Please enter serial number");
                    }
                } else {
                    GlobalVariables.TwoButtonAlertDialog(UnregisterGcard.this, GlobalVariables.CONNECTION_ERROR + " " + "You want to send sms");
                }
            }
        });
    }

    private void submitExchangeSchameDataRequest() {

        action = 2;
        try {
            json_request = new JSONObject();
//            json_request.put("request", "unregister_guarantee");
            json_request.put("request", "unregister_guarantee");
            json_request.put("p_user_id", Util.getSharedPrefrenceValue(UnregisterGcard.this, Constant.Sp_GrtPlusUserID));

            if (serialNoOne != "") {
                json_request.put("p_serial_no1", serialNoOne);
            } else {
                Log.i("SrNo", etSerialNumberOne.getText().toString() + "/" + serialOne);
                json_request.put("p_serial_no1", etSerialNumberOne.getText().toString());
            }

            if (serialNoTwo != "null")
                json_request.put("p_serial_no2", serialNoTwo);
            else
                json_request.put("p_serial_no2", "");

            json_request.put("p_dealer_mobile", Util.getSharedPrefrenceValue(UnregisterGcard.this, Constant.Sp_User_Mobile));

            if (customerMobileNo != "")
                json_request.put("p_customer_mobile", customerMobileNo);
            else
                json_request.put("p_customer_mobile", etMobileNumber.getText().toString());

            if (customerEmail != "null")
                json_request.put("p_customer_email", customerEmail);
            else
                json_request.put("p_customer_email", "");

            if (customerName != "null")
                json_request.put("p_customer_name", customerName);
            else
                json_request.put("p_customer_name", customerName);

            json_request.put("p_non_eo", "0");

            if (invoiceNumber != "null")
                json_request.put("p_invoice_number", invoiceNumber);
            else
                json_request.put("p_invoice_number", "");

            if (invoiceDate != "null")
                json_request.put("p_invoice_date", invoiceDate);
            else
                json_request.put("p_invoice_date", "");

            json_request.put("p_reason_unregister'", spn_reason.getSelectedItem().toString());

            Log.e("#Requests#", json_request.toString());
            new MyAsyncTask(UnregisterGcard.this, json_request).execute();
            dialog = new ProgressDialog(this);
            dialog.setMessage("Loading...");
            dialog.setCancelable(false);
            dialog.show();
        } catch (Exception e) {
        }
    }

    private void getSerialNo() {
        // TODO Auto-generated method stub
        action = 3;
        try {
            json_request = new JSONObject();
            json_request.put("request", "gcard_detail");
            json_request.put("p_bundle_number", etBundleNumber.getText().toString());
//            json_request.put("p_user_id", Util.getSharedPrefrenceValue(UnregisterGcard.this, Constant.Sp_GrtPlusUserID));
            json_request.put("p_user_id", Util.getSharedPrefrenceValue(UnregisterGcard.this, Constant.Sp_GrtPlusUserID));

            Log.e("SerialRequest", json_request.toString());
            new MyAsyncTask(UnregisterGcard.this, json_request).execute();
        } catch (Exception e) {
        }
    }

    @Override
    public void onTaskComplete(String result) {
        if (dialog != null && dialog.isShowing()) {
            dialog.cancel();
        }

        Log.e("#response#", result);
        if (result != null && result.length() > 0) {
            Log.e("#response#", result);
            try {
                json_response = new JSONObject(result);
                String status = json_response.getString("status");
                if (action == 3) {
                    if (status.equals("200")) {
                        JSONObject jObj = json_response.getJSONObject("data");
                        serialNoOne = jObj.getString("op_serial_no1");
                        serialNoTwo = jObj.getString("op_serial_no2");

                        if (serialNoOne != null) {
                            etSerialNumberOne.setText(serialNoOne);
                            customerMobileNo = jObj.getString("op_customer_mobile");
                            customerName = jObj.getString("op_customer_name");
                            customerEmail = jObj.getString("op_customer_email");
                            invoiceNumber = jObj.getString("op_invoice_number");
                            invoiceDate = jObj.getString("op_invoice_date");
                            etMobileNumber.setText(customerMobileNo);

                        } else if (serialNoTwo != null) {
                            etSerialNumberOne.setText(serialNoTwo);
                            customerMobileNo = jObj.getString("op_customer_mobile");
                            customerName = jObj.getString("op_customer_name");
                            customerEmail = jObj.getString("op_customer_email");
                            invoiceNumber = jObj.getString("op_invoice_number");
                            invoiceDate = jObj.getString("op_invoice_date");
                            etMobileNumber.setText(customerMobileNo);
                        } else {
                            alertMessage = jObj.getString("op_message");
                            GlobalVariables.defaultOneButtonDialog(UnregisterGcard.this, alertMessage);
                        }
                    } else {
                        JSONObject jObj = json_response.getJSONObject("data");
                        alertMessage = jObj.getString("op_message");
                        GlobalVariables.defaultOneButtonDialog(UnregisterGcard.this, alertMessage);
                    }
                } else {
                    if (status.equals("200")) {
                        JSONObject jObj = json_response.getJSONObject("data");
                        alertMessage = jObj.getString("op_message");
                        GlobalVariables.defaultOneButtonDialog(UnregisterGcard.this, alertMessage);
                        etMobileNumber.setText("");
                        etSerialNumberOne.setText("");
                        etBundleNumber.setText("");
                        spn_reason.setSelection(0);
                    } else {
                        JSONObject jObj = json_response.getJSONObject("data");
                        alertMessage = jObj.getString("op_message");
                        GlobalVariables.defaultOneButtonDialog(UnregisterGcard.this, alertMessage);
                    }
                }
            } catch (Exception e) {
            }
        }
    }
}
