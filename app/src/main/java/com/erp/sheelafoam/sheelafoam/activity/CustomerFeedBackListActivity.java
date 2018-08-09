package com.erp.sheelafoam.sheelafoam.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.erp.sheelafoam.R;
import com.erp.sheelafoam.adapter.CustomerFeedBackListAdapter;
import com.erp.sheelafoam.adapter.RadioFootFallAdapter;
import com.erp.sheelafoam.constant.Constant;
import com.erp.sheelafoam.models.CustomerListFedItemModel;
import com.erp.sheelafoam.prefrences.SheelaSharedPreference;
import com.erp.sheelafoam.utils.CustomDialog;
import com.erp.sheelafoam.utils.CustomTypefaceTextView;
import com.erp.sheelafoam.utils.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CustomerFeedBackListActivity extends Activity implements View.OnClickListener {
    ArrayList<String> array = new ArrayList<>();
    CustomDialog dialog;
    CustomerFeedBackListAdapter adapter;
    ArrayList<CustomerListFedItemModel> list;
    CustomerListFedItemModel model;
    private CustomTypefaceTextView tv_selectCustomerEMPSpinner, tv_selectCustomerStoreSpinner, tv_CFLName, tv_CFLMobile, tv_CFLDate, tv_CFLEmployee, tv_CFLStore, tv_CFLGender, tv_CFLDateOfBirth, tv_CFLEmail, tv_CFLHowIH, tv_CFLPurpose, tv_CFLInterestedIn, tv_CFLCurrentMattress, tv_CFLAgeing;
    private TextView btn_getCustomerFd, tv_logoname;
    private ImageButton ib_backicon_new;
    private SheelaSharedPreference preference;
    private ListView lv_feedbackList;
    private ProgressDialog progress;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_feedback_list);
        preference = new SheelaSharedPreference(this);
        progress = new ProgressDialog(this);
        ib_backicon_new = (ImageButton) findViewById(R.id.ib_backicon_new);
        tv_logoname = (TextView) findViewById(R.id.tv_logoname_new);
        tv_logoname.setText("Customer FeedBack");
        tv_selectCustomerEMPSpinner = (CustomTypefaceTextView) findViewById(R.id.tv_selectCustomerEMPSpinner);
        tv_selectCustomerStoreSpinner = (CustomTypefaceTextView) findViewById(R.id.tv_selectCustomerStoreSpinner);
        lv_feedbackList = (ListView) findViewById(R.id.lv_feedbackList);
        btn_getCustomerFd = (TextView) findViewById(R.id.btn_getCustomerFd);
        tv_selectCustomerEMPSpinner.setText(preference.getEMPName());
        tv_selectCustomerStoreSpinner.setText(preference.getUserNameFootFall());
        tv_selectCustomerEMPSpinner.setOnClickListener(this);
        tv_selectCustomerStoreSpinner.setOnClickListener(this);
        btn_getCustomerFd.setOnClickListener(this);
        ib_backicon_new.setOnClickListener(this);

        try {
            progress = new ProgressDialog(this);
            progress.setMessage("Loading...");
            progress.setCancelable(false);
            progress.show();
            callGetService();
        }catch (Exception e){
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_selectCustomerEMPSpinner:
                openRadioDialogForEMP(this);
                break;
            case R.id.tv_selectCustomerStoreSpinner:
                openRadioDialogForStore(this);
                break;
            case R.id.btn_getCustomerFd:
                try {
                    progress = new ProgressDialog(this);
                    progress.setMessage("Loading...");
                    progress.setCancelable(false);
                    progress.show();
                    callGetServiceList();
                } catch (Exception e) {

                }
                break;
            case R.id.ib_backicon_new:
                Intent intent = new Intent(CustomerFeedBackListActivity.this, CustomerFeedbackActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void openRadioDialogForStore(Context context) {
        array.clear();
        array.add(preference.getUserNameFootFall());
        dialog = new CustomDialog(context, R.layout.dialog_footfall);
        dialog.setCancelable(true);
        CustomTypefaceTextView tv_subTile = (CustomTypefaceTextView) dialog.findViewById(R.id.tv_subTile_footfall);
        tv_subTile.setText("Select Store*");
        ListView lv = (ListView) dialog.findViewById(R.id.recycler_footfall);
        TextView btn_footfall_cancel = (TextView) dialog.findViewById(R.id.btn_footfall_cancel);
        TextView btn_footfall_ok = (TextView) dialog.findViewById(R.id.btn_footfall_ok);
        btn_footfall_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_selectCustomerStoreSpinner.setText(preference.getUserNameFootFall());
                dialog.dismiss();
            }
        });
        dialog.show();
        lv.setAdapter(new RadioFootFallAdapter(this, array));
        btn_footfall_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    private void openRadioDialogForEMP(Context context) {
        array.clear();
        array.add(preference.getEMPName());
        dialog = new CustomDialog(context, R.layout.dialog_footfall);
        dialog.setCancelable(true);
        CustomTypefaceTextView tv_subTile = (CustomTypefaceTextView) dialog.findViewById(R.id.tv_subTile_footfall);
        tv_subTile.setText("Select Employee *");
        ListView lv = (ListView) dialog.findViewById(R.id.recycler_footfall);
        TextView btn_footfall_cancel = (TextView) dialog.findViewById(R.id.btn_footfall_cancel);
        TextView btn_footfall_ok = (TextView) dialog.findViewById(R.id.btn_footfall_ok);
        btn_footfall_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_selectCustomerEMPSpinner.setText(preference.getEMPName());
                dialog.dismiss();
            }
        });
        dialog.show();
        lv.setAdapter(new RadioFootFallAdapter(this, array));
        btn_footfall_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    private void callGetServiceList() {
        String storeName = preference.getUserNameFootFall();
        String newStoreName=storeName.replaceAll(" ", "%20");
        String UserID = Util.getSharedPrefrenceValue(CustomerFeedBackListActivity.this, Constant.Sp_UserID);
        String UserToken = Util.getSharedPrefrenceValue(CustomerFeedBackListActivity.this, Constant.Sp_UserToken);
        System.out.println("NEWTOKEN" + UserToken + "," + UserID + "," + storeName);
        //String URL = " http://be.greatplus.com/sheelafoam/rest/services/customers/ashish.nandodariya/UHJvamVjdEAjNDU=/RAJNI%20SALES%20CORPORATION";
        String URL = " http://be.greatplus.com/sheelafoam/rest/services/customers/"+UserID+"/"+UserToken+"/"+newStoreName;
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, URL, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                System.out.println("listResponse" + jsonObject);
                progress.dismiss();
                try {
                    if (jsonObject.getBoolean("success")) {
                        jsonObject.getString("message");
                        list = new ArrayList<>();
                        Log.d("getresponse", jsonObject.getString("message").toString());
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            model = new CustomerListFedItemModel();
                            JSONObject object = (JSONObject) jsonArray.get(i);
                            model.setStoreName(object.getString("storeName"));
                            model.setAgeingOfMattress(object.getString("ageingOfMattress"));
                            model.setCurrentMattressBrand(object.getString("currentMattressBrand"));
                            model.setDateOfVisit(object.getString("dateOfVisit"));
                            model.setDob(object.getString("dob"));
                            model.setEmail(object.getString("email"));
                            model.setFirstName(object.getString("firstName"));
                            model.setGender(object.getString("gender"));
                            model.setHavePurchasedItem(object.getString("havePurchasedItem"));
                            model.setHowYouHeard(object.getString("howYouHeard"));
                            model.setId(object.getString("id"));
                            model.setInterestedIn(object.getString("interestedIn"));
                            model.setLastName(object.getString("lastName"));
                            model.setMobile(object.getString("mobile"));
                            model.setPurposeOfVisit(object.getString("purposeOfVisit"));
                            model.setStoreId(object.getString("storeId"));
                            list.add(model);
                            Log.d("listname", object.getString("firstName").toString());
                        }
                        adapter = new CustomerFeedBackListAdapter(CustomerFeedBackListActivity.this, list);
                        lv_feedbackList.setAdapter(adapter);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progress.dismiss();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(CustomerFeedBackListActivity.this);
        requestQueue.add(objectRequest);
    }
    private void callGetService() {
        String UserID = Util.getSharedPrefrenceValue(CustomerFeedBackListActivity.this, Constant.Sp_UserID);
        String UserToken = Util.getSharedPrefrenceValue(CustomerFeedBackListActivity.this, Constant.Sp_UserToken);
        System.out.println("NEWTOKEN"+UserToken +","+UserID);
        // String URL = " http://be.greatplus.com/sheelafoam/rest/employees/details/ashish.nandodariya/UHJvamVjdEAjNDU= ";
        String URL = " http://be.greatplus.com/sheelafoam/rest/employees/details/"+UserID+"/"+UserToken;
        // String URL2 = URL.replaceAll(" ", "%20");
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, URL, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                System.out.println("getresponse" + jsonObject);
                progress.dismiss();
                try {
                    if (jsonObject.getBoolean("success")) {
                        jsonObject.getString("message");
                        Log.d("getresponse", jsonObject.getString("message").toString());
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = (JSONObject) jsonArray.get(i);
                            object.getString("eMP_NAME");
                            Log.d("getresponse", object.getString("eMP_NAME").toString());
                            preference.setEMPName(object.getString("eMP_NAME"));
                            tv_selectCustomerEMPSpinner.setText(object.getString("eMP_NAME"));
                            JSONArray array = object.getJSONArray("storeList");
                            for (int j = 0; j < array.length(); j++) {
                                JSONObject _object = (JSONObject) array.get(j);
                                _object.getString("pARENT_CHANNEL_PARTNER_NAME");
                                preference.setUserNameFootFall(_object.getString("pARENT_CHANNEL_PARTNER_NAME"));
                                tv_selectCustomerStoreSpinner.setText(_object.getString("pARENT_CHANNEL_PARTNER_NAME"));
                                Log.d("getresponse", _object.getString("pARENT_CHANNEL_PARTNER_NAME").toString());
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progress.dismiss();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(CustomerFeedBackListActivity.this);
        requestQueue.add(objectRequest);
    }
}
