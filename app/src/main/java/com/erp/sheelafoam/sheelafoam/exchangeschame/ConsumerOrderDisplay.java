package com.erp.sheelafoam.sheelafoam.exchangeschame;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.erp.sheelafoam.HomeScreen;
import com.erp.sheelafoam.R;
import com.erp.sheelafoam.constant.Constant;
import com.erp.sheelafoam.models.SpinnerTitle;
import com.erp.sheelafoam.sheelafoam.adapter.ColorAdapter;
import com.erp.sheelafoam.sheelafoam.adapter.ProductListAdapter;
import com.erp.sheelafoam.sheelafoam.entry.ColorBean;
import com.erp.sheelafoam.sheelafoam.entry.DealerBean;
import com.erp.sheelafoam.sheelafoam.entry.LocationBean;
import com.erp.sheelafoam.sheelafoam.entry.ProductListAddEditBean;
import com.erp.sheelafoam.sheelafoam.exchangeschame.model.ConsumerItemModel;
import com.erp.sheelafoam.sheelafoam.utility.ApiList;
import com.erp.sheelafoam.sheelafoam.utility.AppConstant;
import com.erp.sheelafoam.sheelafoam.utility.AsyncTaskListner;
import com.erp.sheelafoam.sheelafoam.utility.GlobalVariables;
import com.erp.sheelafoam.sheelafoam.utility.HelperMethods;
import com.erp.sheelafoam.sheelafoam.utility.MyCustomAsyncTask;
import com.erp.sheelafoam.sheelafoam.utility.SharePref;
import com.erp.sheelafoam.utils.CustomDialog;
import com.erp.sheelafoam.utils.CustomTypefaceEditText;
import com.erp.sheelafoam.utils.Util;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Pattern;

import static android.graphics.Color.TRANSPARENT;

public class ConsumerOrderDisplay extends Activity implements View.OnClickListener,AsyncTaskListner {
    List<SpinnerTitle> list;
    int BAR_CODE_RESULT = 0;
    int count = 0;
    int count2 = 0;
    private EditText etMobileNo, etConsumerName, etAdvance;
    private Button btnRedeemLater, btnRedeemNow;
    private TextView add_item1, tv_removeItem, tv_additem, tv_logoname, tv_totalQTY, tv_productColorConsumer, tv_productNameConsumer;
    private ImageButton ib_backicon, ib_filter, ivSearch;
    private CustomTypefaceEditText et_thiknes, et_width, et_length;
    private String length = "", width = "";
    private CustomDialog cd;
    Button btn_Add;
    String product = "";
    ArrayList<ConsumerItemModel> list1 = new ArrayList<>();
    ProgressDialog progressDialog;
    //String getDefaultState = "", getAllState = "", getDealerDetails = "", getAllproduct = "";
    String LBURL = "", COLURL = "", THIKURL = "", DTLURL = "", CATURL = "", PRODURL = "";
    String Mob = "", mobile = "";
    MyCustomAsyncTask customAsyncTask;
    String requset_no = "";
    JSONObject jsonObjectRequest, jsonObjectResponse;
    private SharedPreferences mPrefs;
    SharedPreferences loacalData;
    String location_name = "";
    String location_code = "";
    String color_name;
    String dealer_id;
    int flag_product, flag_color, flag_location, flag_size;
    String color_applicable_yn = "";
    String user_type = "", user_name = "", quantity = "", userRole = "",
            final_dealer_name = "", final_color_name = "", dealer_name = "", dealer_category = "",
            zone = "", product_name = "", sub_product = "",
            product_specification = "", image_url = "", old_image = "", userId;

    ArrayList<LocationBean> arrayList_location = new ArrayList<LocationBean>();
    ArrayList<ColorBean> arrayList_color = new ArrayList<ColorBean>();
    ArrayList<DealerBean> arrayList_dealer = new ArrayList<DealerBean>();
    ArrayList<ProductListAddEditBean> arrayList_products = new ArrayList<ProductListAddEditBean>();
    ProductListAdapter productListAdapter;
    TextView autocomplete_product_name, tv_totalAmountOrder, tv_totalPrizeAmount, tv_totalPaidAmount;
    ColorAdapter colorAdapter;
    private ProgressDialog progress;
    String dealer_state = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consumer_order_details);
        mPrefs = getSharedPreferences("Location", Context.MODE_PRIVATE);
        loacalData = getSharedPreferences(SharePref.MODE_TYPE, Context.MODE_PRIVATE);
        progress = new ProgressDialog(this);
        ib_backicon = (ImageButton) findViewById(R.id.ib_backicon);
        tv_logoname = (TextView) findViewById(R.id.tv_logoname);
        tv_logoname.setText("Consumer Order");
        ivSearch = (ImageButton) findViewById(R.id.ivSearch);
        etMobileNo = (EditText) findViewById(R.id.etMobileNo);
        // btn_Add= (Button) findViewById(R.id.btn_Add);
        etConsumerName = (EditText) findViewById(R.id.etConsumerName);
       /* et_length = (CustomTypefaceEditText) findViewById(R.id.et_length);
        et_width = (CustomTypefaceEditText) findViewById(R.id.et_width);
        et_thiknes = (CustomTypefaceEditText) findViewById(R.id.et_thiknes);*/
        tv_totalQTY = (TextView) findViewById(R.id.tv_totalQTY);
        tv_removeItem = (TextView) findViewById(R.id.tv_removeItem);
        tv_additem = (TextView) findViewById(R.id.tv_additem);
        /* tv_productColorConsumer= (TextView) findViewById(R.id.tv_productColorConsumer);*/
        // tv_productNameConsumer= (TextView) findViewById(R.id.autocomplete_product_name);
        btnRedeemLater = (Button) findViewById(R.id.btnRedeemLater);
        btnRedeemNow = (Button) findViewById(R.id.btnRedeemNow);
        autocomplete_product_name = (TextView) findViewById(R.id.autocomplete_product_name);
       // etAdvance = (EditText) findViewById(R.id.etAdvance);
        tv_totalPaidAmount = (TextView) findViewById(R.id.tv_totalPaidAmount);
        tv_totalPrizeAmount = (TextView) findViewById(R.id.tv_totalPrizeAmount);
        tv_totalAmountOrder = (TextView) findViewById(R.id.tv_totalAmountOrder);
        ivSearch.setOnClickListener(this);
        etMobileNo.setOnClickListener(this);
        btnRedeemNow.setOnClickListener(this);
        btnRedeemLater.setOnClickListener(this);
        tv_additem.setOnClickListener(this);
        tv_removeItem.setOnClickListener(this);
        autocomplete_product_name.setOnClickListener(this);
        ib_backicon.setOnClickListener(this);
        customAsyncTask = new MyCustomAsyncTask(ConsumerOrderDisplay.this);
        et_length.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    length = et_length.getText().toString().trim();
                    convertLength(length);
                }
            }
        });
        et_width.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    width = et_width.getText().toString().trim();
                    convertWidth(width);
                }
            }
        });
        //getAllProduct("PARGAL FURNISHING","");
        getDealers();
        //callMRPService();
        getLocationData(Util.getSharedPrefrenceValue(ConsumerOrderDisplay.this, Constant.Sp_DealerID));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == BAR_CODE_RESULT) {
            if (resultCode == RESULT_OK) {
                String contents = data.getStringExtra("SCAN_RESULT");
                System.out.println("scanData" + contents);
            } else if (resultCode == RESULT_CANCELED) {
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_backicon:
                Intent goBack = new Intent(ConsumerOrderDisplay.this, HomeScreen.class);
                startActivity(goBack);
                break;
            case R.id.etMobileNo:
                break;
            case R.id.autocomplete_product_name:
                dialogs_Product();
                break;
            case R.id.btnRedeemNow:
               /* Intent intent = new Intent(ConsumerOredrActivity.this, CaptureActivity.class);
                intent.setAction("com.google.zxing.client.android.SCAN");
                intent.putExtra("SAVE_HISTORY", false);
                startActivityForResult(intent, 0);*/
                Intent goToNext = new Intent(ConsumerOrderDisplay.this, ConsumerExchangeSchemeActivity.class);
                goToNext.putExtra("MobNumber", etMobileNo.getText().toString());
                goToNext.putExtra("consumerName", etConsumerName.getText().toString());
                startActivity(goToNext);
                break;
            case R.id.btnRedeemLater:
                if (isValid()) {
                    progress = new ProgressDialog(ConsumerOrderDisplay.this);
                    progress.setMessage("Please wait....");
                    progress.setCancelable(false);
                    progress.show();
                    callSubmitService();
                }
                break;
            case R.id.tv_additem:
                if (isValid()) {
                    if (Double.valueOf(et_width.getText().toString()) > 1067 && count2 == 0) {
                        count++;
                        count2 = 3;
                        String temp = String.valueOf(count);
                        tv_totalQTY.setText(temp);
                        progress = new ProgressDialog(ConsumerOrderDisplay.this);
                        progress.setMessage("Please wait....");
                        progress.setCancelable(false);
                        progress.show();
                        callMRPService();
                    } else if (Double.valueOf(et_width.getText().toString()) <= 1067 && count2 <= 1) {
                        count++;
                        count2++;
                        String temp = String.valueOf(count);
                        tv_totalQTY.setText(temp);
                        progress = new ProgressDialog(ConsumerOrderDisplay.this);
                        progress.setMessage("Please wait....");
                        progress.setCancelable(false);
                        progress.show();
                        callMRPService();
                    } else {

                    }
                }
                break;
            case R.id.tv_removeItem:
                if (count > 0) {
                    count--;

                    if (count2 == 3) {
                        count2 = 0;
                    } else {
                        count2--;
                    }
                }
                String temp1 = String.valueOf(count);
                tv_totalQTY.setText(temp1);
                break;
            case R.id.ivSearch:
                progress = new ProgressDialog(ConsumerOrderDisplay.this);
                progress.setMessage("Please wait....");
                progress.setCancelable(false);
                progress.show();
                searchNumber();
                break;

        }

    }

    public String roundOffLengthAndWidth(double value) {
        DecimalFormat f = new DecimalFormat("0.0");
        String formattedValue = f.format(value);
        String[] array_split = formattedValue.split(Pattern.quote("."));
        Log.e("array size", "" + array_split.length);
        String _value = array_split[0];
        if (Double.parseDouble(array_split[1]) > 5 || Double.parseDouble(array_split[1]) == 5) {
            _value = String.valueOf(Double.parseDouble(_value) + 1);
        }
        return _value;
    }

    public void convertLength(String length) {
        if (length != null && length.length() > 0) {
            if (Double.parseDouble(length) < 100) {
                length = roundOffLengthAndWidth(Double.parseDouble(length) * 25.4);
                et_length.setText(length);
            }
        }
    }

    public void convertWidth(String width) {
        if (width != null && width.length() > 0) {
            if (Double.parseDouble(width) < 100) {
                width = roundOffLengthAndWidth(Double.parseDouble(width) * 25.4);
                et_width.setText(width);
            }

        }
    }

    private void customDialogAdd() {
        if (cd != null && cd.isShowing()) {
            cd.dismiss();
        }
        cd = new CustomDialog(ConsumerOrderDisplay.this, R.layout.custom_add_item_dialog);
        cd.setCancelable(false);
        cd.getWindow().setBackgroundDrawable(new ColorDrawable(TRANSPARENT));
       /* TextView dialog_title = (TextView) cd.findViewById(R.id.dialog_title);
        TextView txt_address_value = (TextView) cd.findViewById(R.id.tv_massageDialog);
        TextView btn_okDialog = (TextView) cd.findViewById(R.id.btn_okDialog);
        TextView btn_canceDilog = (TextView) cd.findViewById(R.id.btn_canceDilog);
        btn_okDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cd!=null && cd.isShowing())
                    cd.isShowing();
            }
        });
        btn_canceDilog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cd.dismiss();
            }
        });*/
        cd.show();
    }

    public void getLocationData(String p_dealer_id) {
       /* if (arrayList_location.size() > 0) {
            dialogs_location();
        } else if (HelperMethods.isNetworkAvailable(getActivity())) {
            requset_no = "6";*/
        requset_no = "6";
        JSONObject jObject = new JSONObject();
        try {
            jObject.put("request", ApiList.API_LOCATION);
            jObject.put("p_dealer_id", p_dealer_id);
            Log.e("request6", "" + jObject.toString());
        } catch (Exception e) {
            Log.e("Exception", e.getMessage());
        }
        customAsyncTask.showDialog(ConsumerOrderDisplay.this);
        new MyCustomAsyncTask(ConsumerOrderDisplay.this, jObject, ApiList.URL_PRODUCTS).execute();
    }


    @Override
    public void onTaskComplete(String result) {
        try {
            if (requset_no.equals("3")) // Dealer list
            {
                if (result != null && result.length() > 0) {
                    jsonObjectResponse = new JSONObject(result);
                    Log.e("Response3", " " + jsonObjectResponse);
                    String status = jsonObjectResponse.getString("status");
                    if (status.equalsIgnoreCase("200")) {
                        JSONObject data = jsonObjectResponse
                                .getJSONObject("data");
                        JSONArray dealerArray = data.getJSONArray("product");
                        getDealerList(dealerArray);
                        flag_product = 0;
                        flag_location = 0;
                        customAsyncTask.hideDialog(ConsumerOrderDisplay.this);
                        setDealerDataWhenLoginByDealer();
                        /*
                         * Calling dealer Dialog
                         */
                    } else {
                        customAsyncTask.hideDialog(ConsumerOrderDisplay.this);
                        JSONObject data = jsonObjectResponse
                                .getJSONObject("data");
                        String msg = data.getString("op_message");
                        GlobalVariables.defaultOneButtonDialog(ConsumerOrderDisplay.this,
                                "" + msg);
                    }
                } else {
                    Toast.makeText(ConsumerOrderDisplay.this, "No Data returned",
                            Toast.LENGTH_SHORT).show();
                }
            } else if (requset_no.equals(2)) {
                if (result != null && result.length() > 0) {
                    jsonObjectResponse = new JSONObject(result);
                    Log.e("Response2", " " + jsonObjectResponse);
                    String status = jsonObjectResponse.getString("status");
                    if (status.equalsIgnoreCase("200")) {
                        JSONObject data = jsonObjectResponse
                                .getJSONObject("data");
                        JSONArray colorArray = data.getJSONArray("product");
                        getColorList(colorArray);
                        //customAsyncTask.hideDialog(getActivity());
                        if (AppConstant.EDIT_ORDER == 1 && flag_color == 1)
                            dialogs_Color();
                    } else {
                        JSONObject data = jsonObjectResponse
                                .getJSONObject("data");
                        String msg = data.getString("op_message");
                        GlobalVariables.defaultOneButtonDialog(ConsumerOrderDisplay.this,
                                "" + msg);
                    }
                } else {
                    Toast.makeText(ConsumerOrderDisplay.this, "No Data returned",
                            Toast.LENGTH_SHORT).show();

                }
            } else if (requset_no.equals("4")) {
                if (result != null && result.length() > 0) {
                    jsonObjectResponse = new JSONObject(result);
                    Log.e("Response4", " " + jsonObjectResponse);
                    String status = jsonObjectResponse.getString("status");
                    if (status.equalsIgnoreCase("200")) {
                        JSONObject data = jsonObjectResponse
                                .getJSONObject("data");
                        JSONArray productArray = data.getJSONArray("product");
                        SharedPreferences.Editor editor = loacalData.edit();
                        editor.putString(SharePref.PRODUCTS,
                                productArray.toString());
                        editor.commit();
                        getProductList(productArray);
                        customAsyncTask.hideDialog(ConsumerOrderDisplay.this);
                        flag_color = 0;
                        if (AppConstant.EDIT_ORDER == 1 && flag_product == 1)
                            dialogs_Product();
                    } else {
                        customAsyncTask.hideDialog(ConsumerOrderDisplay.this);
                        JSONObject data = jsonObjectResponse
                                .getJSONObject("data");
                        String msg = data.getString("op_message");
                        GlobalVariables.defaultOneButtonDialog(ConsumerOrderDisplay.this,
                                "" + msg);
                    }
                } else {
                    customAsyncTask.hideDialog(ConsumerOrderDisplay.this);
                    Toast.makeText(ConsumerOrderDisplay.this, "No Data returned",
                            Toast.LENGTH_SHORT).show();
                }
            } else if (requset_no.equals("6")) {
                if (result != null && result.length() > 0) {
                    jsonObjectResponse = new JSONObject(result);
                    Log.e("Response6", " " + jsonObjectResponse);
                    String status = jsonObjectResponse.getString("status");
                    if (status.equalsIgnoreCase("200")) {
                        JSONObject data = jsonObjectResponse.getJSONObject("data");
                        JSONArray locationArray = data.getJSONArray("location");
                        SharedPreferences.Editor editor = loacalData.edit();
                        editor.putString(SharePref.LOCATION, locationArray.toString());
                        editor.commit();
                        getLocationList(locationArray);
                        customAsyncTask.hideDialog(ConsumerOrderDisplay.this);
                        flag_product = 0;
                        if (AppConstant.EDIT_ORDER == 1 && flag_location == 1)
                            // dialogs_location();
                            customAsyncTask.hideDialog(ConsumerOrderDisplay.this);
                    } else {
                        customAsyncTask.hideDialog(ConsumerOrderDisplay.this);
                        JSONObject data = jsonObjectResponse.getJSONObject("data");
                        String msg = data.getString("op_message");
                        GlobalVariables.defaultOneButtonDialog(ConsumerOrderDisplay.this, "" + msg);
                    }
                    //Below line commented to get variable value from new login
                    //if(mPrefs.getString("op_user_type", "").equals("DEALER"))
                    if (Util.getSharedPrefrenceValue(ConsumerOrderDisplay.this, Constant.Sp_User_Type).equals("DEALER")) {
                        getDealers();
                    } else {
                        if (AppConstant.EDIT_ORDER == 0 && location_name.length() > 0) {
                            autocomplete_product_name.setHint("Select Product");
                            //tv_productColorConsumer.setHint("Color");
                            if (arrayList_products.size() > 0)
                                arrayList_products.clear();
                            getProducts(mPrefs.getString("DEALER_NAME", ""), AppConstant.DEALER_ZONE);
                        }
                    }
                } else {
                    customAsyncTask.hideDialog(ConsumerOrderDisplay.this);
                    Toast.makeText(ConsumerOrderDisplay.this, "No Data returned",
                            Toast.LENGTH_SHORT).show();
                }
            }

        } catch (Exception e) {
            // rlayout_submit_buttons.setVisibility(View.VISIBLE);
            customAsyncTask.hideDialog(ConsumerOrderDisplay.this);
            Log.e("Exception caught", " = " + e);
        } finally {
            if (mPrefs.getInt("CONNECTION_TIMEOUT", 0) == 1) {
                customAsyncTask.hideDialog(ConsumerOrderDisplay.this);
                Toast.makeText(ConsumerOrderDisplay.this, "Connection timeout",
                        Toast.LENGTH_LONG).show();
            }
        }

    }

    public void getLocationList(JSONArray jArray) {
        for (int i = 0; i < jArray.length(); i++) {
            try {
                JSONObject obj = jArray.getJSONObject(i);
                location_name = obj.getString("location_name");
                location_code = obj.getString("location_code");
                System.out.println("location" + location_name);
                //channel_partner_group = obj.getString("channel_partner_group");
                LocationBean locationBean = new LocationBean();
                locationBean.setLocation_code(location_code);
                locationBean.setLocation_name(location_name);
                // locationBean.setChannel_partner_group(channel_partner_group);
                arrayList_location.add(locationBean);
            } catch (Exception e) {

            }
        }
    }

    public void getDealers() {
        String delearID = Util.getSharedPrefrenceValue(ConsumerOrderDisplay.this, Constant.Sp_emp_grpCode);
        if (HelperMethods.isNetworkAvailable(ConsumerOrderDisplay.this)) {
            requset_no = "3";
            jsonObjectRequest = new JSONObject();
            try {
                if (Util.getSharedPrefrenceValue(ConsumerOrderDisplay.this, Constant.Sp_User_Type).equals("EMPLOYEE")) {
                    jsonObjectRequest.put("old", "1");
                    jsonObjectRequest.put("op_user_role_name", Util.getSharedPrefrenceValue(ConsumerOrderDisplay.this, Constant.Sp_op_user_role_name));
                    jsonObjectRequest.put("op_greatplus_user_id", Util.getSharedPrefrenceValue(ConsumerOrderDisplay.this, Constant.Sp_GrtPlusUserID));
                } else {
                    jsonObjectRequest.put("request", ApiList.API_PRODUCT_DEALERS);
                    jsonObjectRequest.put("p_role_name", Util.getSharedPrefrenceValue(ConsumerOrderDisplay.this, Constant.Sp_User_Type));
                    jsonObjectRequest.put("p_dealer_id", delearID);
                }
                Log.e("request3", "" + jsonObjectRequest.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            customAsyncTask.showDialog(ConsumerOrderDisplay.this);
            if (Util.getSharedPrefrenceValue(ConsumerOrderDisplay.this, Constant.Sp_User_Type).equals("EMPLOYEE")) {
                new MyCustomAsyncTask(ConsumerOrderDisplay.this, jsonObjectRequest, Constant.WS_URL + Constant.WS_DEALER_LIST).execute();
            } else {
                new MyCustomAsyncTask(ConsumerOrderDisplay.this,
                        jsonObjectRequest, ApiList.URL_PRODUCTS).execute();
            }
        } else {
            GlobalVariables.defaultOneButtonDialog(ConsumerOrderDisplay.this,
                    "Network error");
        }
    }

    public void getProducts(String p_dealer_name, String p_zone) {
        /*   *//* if (arrayList_products.size() > 0) {
            Log.e("product list", "" + arrayList_products);
            dialogs_Product();
        }*//* else*/
        if (HelperMethods.isNetworkAvailable(ConsumerOrderDisplay.this)) {
            requset_no = "4";
            jsonObjectRequest = new JSONObject();
            try {
                jsonObjectRequest.put("request", ApiList.API_PRODUCTS2);
                jsonObjectRequest.put("p_dealer_name", p_dealer_name);
                jsonObjectRequest.put("p_zone", p_zone);
                if (Util.getSharedPrefrenceValue(ConsumerOrderDisplay.this, Constant.Sp_User_Type).equals("DEALER")) {
                    Log.d("true", "" + mPrefs.getString("DEALER_CATEGORY", ""));
                    jsonObjectRequest.put("p_dealer_category", Util.getSharedPrefrenceValue(ConsumerOrderDisplay.this, Constant.Sp_Dealer_Category));
                } else {
                    //Log.d("false", "" + dealer_category);
                    // jsonObjectRequest.put("p_dealer_category", dealer_category);
                }
                Log.e("request4", "" + jsonObjectRequest.toString());
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            customAsyncTask.showDialog(ConsumerOrderDisplay.this);
            new MyCustomAsyncTask(ConsumerOrderDisplay.this,
                    jsonObjectRequest, ApiList.URL_PRODUCTS).execute();

        } else {
            customAsyncTask.showDialog(ConsumerOrderDisplay.this);
            GlobalVariables.defaultOneButtonDialog(ConsumerOrderDisplay.this,
                    "Network error");
        }

    }

    public void getDealerList(JSONArray jArray) {
        for (int i = 0; i < jArray.length(); i++) {
            try {
                JSONObject obj = jArray.getJSONObject(i);
                String dealer_name = obj.getString("DEALER_NAME");
                String zone = obj.getString("DEALER_ZONE");
                String dealer_category = obj.getString("DEALER_CATEGORY");
                String dealer_id = obj.getString("DEALER_ID");
                String dealer_area = obj.getString("AREA");
                String dealer_city = obj.getString("CITY");
                dealer_state = obj.getString("STATE");
                DealerBean dealerBean = new DealerBean();
                dealerBean.setDealerName(dealer_name);
                dealerBean.setDealerZone(zone);
                dealerBean.setDealer_category(dealer_category);
                dealerBean.setDealerId(dealer_id);
                dealerBean.setDealer_area(dealer_area);
                dealerBean.setDealer_city(dealer_city);
                dealerBean.setDealer_state(dealer_state);
               /* if (AppConstant.EDIT_ORDER == 1 && textview_dealer_list.getText().toString().trim().equals(dealer_name))
                    AppConstant.DEALER_ZONE = zone;*/
                arrayList_dealer.add(dealerBean);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setDealerDataWhenLoginByDealer() {

        if (Util.getSharedPrefrenceValue(ConsumerOrderDisplay.this, Constant.Sp_User_Type).equals("DEALER")) {
            DealerBean dealerBean = arrayList_dealer.get(0);
            dealer_name = dealerBean.getDealerName();
            dealer_id = dealerBean.getDealerId();
            zone = dealerBean.getDealerZone();
            dealer_category = dealerBean.getDealer_category();

            //textview_dealer_list.setText(mPrefs.getString("op_user_name","").toString());
          /*  textview_dealer_list.setText(Util.getSharedPrefrenceValue(getActivity(), Constant.Op_user_name));
            final_dealer_name = dealer_name;
*/
            //GlobalVariables.DEALER_NAME = final_dealer_name;
            AppConstant.DEALER_ZONE = zone;
            GlobalVariables.DEALER_CATEGORY = dealer_category;
            SharedPreferences.Editor editor = mPrefs.edit();
            editor.putString("DEALER_NAME", dealer_name);
            editor.putString("DEALER_ID", dealer_id);
            editor.commit();
            autocomplete_product_name.setHint("Select Product");
            // tv_productColorConsumer.setHint("Color");
            if (arrayList_products.size() > 0)
                arrayList_products.clear();
            getProducts(dealer_name, zone);

        }
    }

    public void getProductList(JSONArray jArray) {
       /* if (arrayList_products.size() > 0) {
            arrayList_products.clear();
        }*/
        for (int i = 0; i < jArray.length(); i++) {
            try {
                JSONObject obj = jArray.getJSONObject(i);
                product_name = obj.getString("product_display_name");
                sub_product = obj.getString("sub_product");
                product_specification = obj.getString("product_specification");
                color_applicable_yn = obj.getString("color_applicable_yn");
                ProductListAddEditBean productListBean = new ProductListAddEditBean();
                productListBean.setProductName(product_name);
                productListBean.setSubProductName(sub_product);
                productListBean.setProductSpecification(product_specification);
                productListBean.setColor_applicable_yn(color_applicable_yn);
                arrayList_products.add(productListBean);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Log.e("Product_List", " = " + arrayList_products.toString());
        /*******************Shared variable for save product list********************/
        saveProductList(arrayList_products);
    }

    /***********************Method: Save product ArrayList in Shared Preferences***************************************/
    private void saveProductList(ArrayList<ProductListAddEditBean> productList) {
        SharedPreferences.Editor editor = mPrefs.edit();
        Gson gson = new Gson();

        String json = gson.toJson(productList);

        editor.putString("com.sheela.employeeportal.sheelafoam.products", json);
        editor.commit();
    }

    private ArrayList<ProductListAddEditBean> getProductListFromSharedPrefrences() {
        ArrayList<ProductListAddEditBean> productList = null;

        Gson gson = new Gson();
        String json = mPrefs.getString("com.sheela.employeeportal.sheelafoam.products", null);
        Type type = new TypeToken<ArrayList<ProductListAddEditBean>>() {
        }.getType();
        productList = gson.fromJson(json, type);

        return productList;
    }

    public void dialogs_Product() {
        final Dialog dialog = new Dialog(ConsumerOrderDisplay.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_dealer);
        dialog.setCancelable(true);
        final EditText autocomplete_product_name1 = (EditText) dialog
                .findViewById(R.id.autocomplete_product_name);
        autocomplete_product_name1.setVisibility(View.VISIBLE);
        final ListView listview = (ListView) dialog
                .findViewById(R.id.listview_securityquestion);
        ImageButton imagebutton_close = (ImageButton) dialog
                .findViewById(R.id.button_close);
        TextView textview_title = (TextView) dialog
                .findViewById(R.id.textview_title);
        textview_title.setText("Please select a Product");
        if (arrayList_products.size() > 0) {
            productListAdapter = new ProductListAdapter(arrayList_products,
                    ConsumerOrderDisplay.this);
        } else {
            arrayList_products = getProductListFromSharedPrefrences();
        }
        listview.setAdapter(productListAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
                HelperMethods.hideSoftKeyboard(ConsumerOrderDisplay.this);
                ProductListAddEditBean dealerBean = arrayList_products.get(position);
                product_name = dealerBean.getProductName();
                color_applicable_yn = dealerBean.getColor_applicable_yn();
                /*if (color_applicable_yn.equals("")) {
                    llayout_product_color.setVisibility(View.GONE);
                } else {
                    llayout_product_color.setVisibility(View.VISIBLE);
                }*/
                if (autocomplete_product_name.getText().toString().trim().equalsIgnoreCase(product_name)) {
                    Toast.makeText(ConsumerOrderDisplay.this, "Product already selected",
                            Toast.LENGTH_SHORT).show();
                } else {
                    autocomplete_product_name.setText(product_name);
                   /* tv_productColorConsumer.setText("");
                    tv_productColorConsumer.setHint("Color");*/
                    if (arrayList_color.size() > 0)
                        arrayList_color.clear();
                   /* if (arrayList_size.size() > 0)
                        arrayList_size.clear();*/
                    dialog.dismiss();
                    //getColor();
                }
            }
        });
        imagebutton_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        autocomplete_product_name1.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {// arrayList_countryRecords=GlobalVariables.ARRAYLIST_COUNTRYRECORDS;

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                final ArrayList<ProductListAddEditBean> selectedArrayList = getArrayList(arrayList_products, autocomplete_product_name1
                        .getText().toString().trim());
                productListAdapter = new ProductListAdapter(selectedArrayList, ConsumerOrderDisplay.this);
                listview.setAdapter(productListAdapter);
                listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> arg0, View view,
                                            int position, long id) {
                        HelperMethods.hideSoftKeyboard(ConsumerOrderDisplay.this);
                        ProductListAddEditBean dealerBean = selectedArrayList.get(position);
                        product_name = dealerBean.getProductName();
                        color_applicable_yn = dealerBean.getColor_applicable_yn();
                     /*   if (color_applicable_yn.equals("")) {
                            llayout_product_color.setVisibility(View.GONE);
                        } else {
                            llayout_product_color.setVisibility(View.VISIBLE);
                        }*/
                        if (autocomplete_product_name.getText().toString()
                                .trim().equalsIgnoreCase(product_name)) {
                            Toast.makeText(ConsumerOrderDisplay.this,
                                    "Product already selected",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            autocomplete_product_name.setText(product_name);
                            System.out.println("Product Name" + autocomplete_product_name.getText().toString());
                          /*  tv_productColorConsumer.setText("");
                            tv_productColorConsumer.setHint("Color");*/
                            if (arrayList_color.size() > 0)
                                arrayList_color.clear();
                           /* if (arrayList_size.size() > 0)
                                arrayList_size.clear();*/
                            dialog.dismiss();
                            if (arrayList_color.size() > 0)
                                arrayList_color.clear();
                            // getColor();
                        }
                    }
                });
            }

            private ArrayList<ProductListAddEditBean> getArrayList(
                    ArrayList<ProductListAddEditBean> arrayList_products,
                    String filter) {
                ArrayList<ProductListAddEditBean> selectedArrayList = new ArrayList<ProductListAddEditBean>();
                if (filter.length() >= 0) {
                    for (int i = 0; i < arrayList_products.size(); i++) {
                        if (arrayList_products.get(i).getProductName()
                                .toLowerCase().contains(filter.toLowerCase()))
                            selectedArrayList.add(arrayList_products.get(i));
                    }
                    return selectedArrayList;
                } else {
                    return arrayList_products;
                }
            }
        });
        dialog.show();
    }

    public void dialogs_Color() {
        final Dialog dialog = new Dialog(ConsumerOrderDisplay.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_dealer);
        dialog.setCancelable(true);
        final EditText autocomplete_color_name = (EditText) dialog.findViewById(R.id.autocomplete_product_name);
        autocomplete_color_name.setVisibility(View.VISIBLE);
        final ListView listview = (ListView) dialog.findViewById(R.id.listview_securityquestion);
        ImageButton imagebutton_close = (ImageButton) dialog.findViewById(R.id.button_close);
        TextView textview_title = (TextView) dialog.findViewById(R.id.textview_title);
        textview_title.setText("Please select color");
        colorAdapter = new ColorAdapter(arrayList_color, ConsumerOrderDisplay.this);
        listview.setAdapter(colorAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View view,
                                    int position, long id) {
                HelperMethods.hideSoftKeyboard(ConsumerOrderDisplay.this);
                ColorBean colorBean = arrayList_color.get(position);
                color_name = colorBean.getColor();
                if (autocomplete_product_name.getText().toString().trim()
                        .equalsIgnoreCase(color_name)) {
                    Toast.makeText(ConsumerOrderDisplay.this, "Color already selected",
                            Toast.LENGTH_SHORT).show();
                } else {
                    autocomplete_product_name.setText(color_name);
                    final_color_name = color_name;
                    dialog.dismiss();
                }
            }
        });
        imagebutton_close.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        autocomplete_color_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                final ArrayList<ColorBean> selectedArrayList = getArrayList(
                        arrayList_color, autocomplete_color_name.getText()
                                .toString().trim());
                colorAdapter = new ColorAdapter(selectedArrayList, ConsumerOrderDisplay.this);
                listview.setAdapter(colorAdapter);
                listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> arg0, View view,
                                            int position, long id) {
                        HelperMethods.hideSoftKeyboard(ConsumerOrderDisplay.this);
                        ColorBean colorBean = selectedArrayList.get(position);
                        color_name = colorBean.getColor();
                        if (tv_productColorConsumer.getText().toString().trim()
                                .equalsIgnoreCase(color_name)) {
                            Toast.makeText(ConsumerOrderDisplay.this,
                                    "Color already selected",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            tv_productColorConsumer.setText(color_name);
                            final_color_name = color_name;
                            dialog.dismiss();
                        }
                    }
                });
            }

            private ArrayList<ColorBean> getArrayList(
                    ArrayList<ColorBean> arrayList_color, String filter) {
                ArrayList<ColorBean> selectedArrayList = new ArrayList<ColorBean>();
                if (filter.length() >= 0) {
                    for (int i = 0; i < arrayList_color.size(); i++) {
                        if (arrayList_color.get(i).getColor().toLowerCase()
                                .contains(filter.toLowerCase()))
                            selectedArrayList.add(arrayList_color.get(i));
                    }
                    return selectedArrayList;
                } else {
                    return arrayList_color;
                }
            }
        });
        dialog.show();
    }

    public void getColor() {
        if (HelperMethods.isNetworkAvailable(ConsumerOrderDisplay.this)) {
            requset_no = "2";
            jsonObjectRequest = new JSONObject();
            try {
                jsonObjectRequest.put("request", ApiList.API_PRODUCT_COLOR);
                jsonObjectRequest.put("p_product_name", tv_productNameConsumer.getText().toString());
                jsonObjectRequest.put("p_zone", AppConstant.DEALER_ZONE);
                Log.e("request2", "" + jsonObjectRequest.toString());
                customAsyncTask.showDialog(ConsumerOrderDisplay.this);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            new MyCustomAsyncTask(ConsumerOrderDisplay.this,
                    jsonObjectRequest, ApiList.URL_PRODUCTS).execute();
        } else {
            GlobalVariables.defaultOneButtonDialog(ConsumerOrderDisplay.this,
                    "Network error");
        }
    }

    public void getColorList(JSONArray jArray) {
       /* if (arrayList_color.size() > 0) {
            arrayList_color.clear();
        }*/
        for (int i = 0; i < jArray.length(); i++) {
            try {
                JSONObject obj = jArray.getJSONObject(i);
                color_name = obj.getString("color");
                ColorBean colorBean = new ColorBean();
                colorBean.setColor(color_name);
                arrayList_color.add(colorBean);
            } catch (Exception e) {
            }
        }
    }


    private void callMRPService() {
        String delearID = Util.getSharedPrefrenceValue(ConsumerOrderDisplay.this, Constant.Sp_emp_grpCode);
        String UserID = Util.getSharedPrefrenceValue(ConsumerOrderDisplay.this, Constant.Sp_UserID);
        Map<String, String> jsonParams = new HashMap<String, String>();
        jsonParams.put("p_user_id", UserID);//---9219151562
        jsonParams.put("p_state", dealer_state);
        jsonParams.put("p_product_name", autocomplete_product_name.getText().toString());
        jsonParams.put("p_length", et_length.getText().toString());
        jsonParams.put("p_bredth", et_width.getText().toString());
        jsonParams.put("p_thick", et_thiknes.getText().toString());
        jsonParams.put("p_color", "");
        JsonObjectRequest myRequest = new JsonObjectRequest(
                Request.Method.POST,
                "https://greatplus.com/api_services/get_mrp_api.php",
                new JSONObject(jsonParams),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progress.dismiss();
                        Log.d("MRPresult", response.toString());
                        try {
                            String status = response.getString("status");
                            if (status.equalsIgnoreCase("200")) {
                                JSONObject jsonObject = response.getJSONObject("Data");
                                String reward = jsonObject.getString("op_reward");
                                String TotalAmout = jsonObject.getString("op_mrp");
                                tv_totalAmountOrder.setText(TotalAmout);
                                tv_totalPrizeAmount.setText(reward);
                                int Total = Integer.parseInt(jsonObject.getString("op_mrp"));
                                int Reward = Integer.parseInt(jsonObject.getString("op_reward"));
//                                if (etAdvance.getText().toString().isEmpty()) {
                                    tv_totalPaidAmount.setText(String.valueOf(Total - Reward));

//                                } else {
//                                    int Advance = Integer.parseInt(etAdvance.getText().toString());
//                                    tv_totalPaidAmount.setText(String.valueOf(Total - Reward - Advance));
//                                }
                                //System.out.println("reward" + reward);
                               // Toast.makeText(ConsumerOrderDisplay.this, jsonObject.getString("op_message"), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progress.dismiss();
                        Toast.makeText(ConsumerOrderDisplay.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };
        RequestQueue MyRequestQueue = Volley.newRequestQueue(ConsumerOrderDisplay.this);
        MyRequestQueue.add(myRequest);
    }

    private String randomNumber() {
        DateFormat dateFormat = new SimpleDateFormat("MMddyyyyHH:mm:ss");
        Random random = new Random();
        StringBuilder sb = new StringBuilder(dateFormat.format(new Date()));
        sb.append("_" + Util.getSharedPrefrenceValue(ConsumerOrderDisplay.this, Constant.Sp_UserID).substring(7));
        sb.append("_" + random.nextInt(900));
        return sb.toString();
    }

    private void callSubmitService() {
        String URL = "https://greatplus.com/api_services/ insert_order_api.php";
        String delearID = Util.getSharedPrefrenceValue(ConsumerOrderDisplay.this, Constant.Sp_emp_grpCode);
        String UserID = Util.getSharedPrefrenceValue(ConsumerOrderDisplay.this, Constant.Sp_UserID);
        Map<String, String> jsonParams = new HashMap<String, String>();
        jsonParams.put("p_request_id", randomNumber());//--randomNumber()
        jsonParams.put("p_user_id", UserID);
        jsonParams.put("p_dealer_id", delearID);
        jsonParams.put("p_product_display_name", autocomplete_product_name.getText().toString());
        jsonParams.put("p_length","");
        jsonParams.put("p_bredth", "");
        jsonParams.put("p_thick", "");
        jsonParams.put("p_customer_name", etConsumerName.getText().toString());
        jsonParams.put("p_quantity", tv_totalQTY.getText().toString());
        jsonParams.put("p_customer_mobile", etMobileNo.getText().toString());
        jsonParams.put("p_product_mrp", tv_totalAmountOrder.getText().toString());
        jsonParams.put("p_cash_reward", tv_totalPrizeAmount.getText().toString());
        jsonParams.put("p_advance_amt", etAdvance.getText().toString());
        JsonObjectRequest myRequest = new JsonObjectRequest(Request.Method.POST, "https://greatplus.com/api_services/insert_order_api.php", new JSONObject(jsonParams),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("InsertResult", response.toString());
                        progress.dismiss();
                        try {
                            String status = response.getString("status");
                            if (status.equalsIgnoreCase("200")) {
                                String orderNumber = response.getString("Order Number");
                                System.out.println("orderNumber" + orderNumber);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progress.dismiss();
                        Toast.makeText(ConsumerOrderDisplay.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };
        RequestQueue MyRequestQueue = Volley.newRequestQueue(ConsumerOrderDisplay.this);
        MyRequestQueue.add(myRequest);
    }

    private boolean isValid() {
        if (autocomplete_product_name.getText().toString().isEmpty() || autocomplete_product_name.getText().toString().equals("Select Product")) {
            Toast.makeText(ConsumerOrderDisplay.this, "Please select Product!", Toast.LENGTH_LONG).show();
            return false;
        } else if (et_length.getText().toString().isEmpty()) {
            Toast.makeText(ConsumerOrderDisplay.this, "Please enter Length!", Toast.LENGTH_LONG).show();
            return false;
        } else if (et_width.getText().toString().isEmpty()) {
            Toast.makeText(ConsumerOrderDisplay.this, "Please enter Breath!", Toast.LENGTH_LONG).show();
            return false;
        } else if (et_thiknes.getText().toString().isEmpty()) {
            Toast.makeText(ConsumerOrderDisplay.this, "Please enter Thickness!", Toast.LENGTH_LONG).show();
            return false;
        } else if (etConsumerName.getText().toString().isEmpty()) {
            Toast.makeText(ConsumerOrderDisplay.this, "Please enter consumer name!", Toast.LENGTH_LONG).show();
            return false;
        } else if (etMobileNo.getText().toString().isEmpty()) {
            Toast.makeText(ConsumerOrderDisplay.this, "Please enter valid mobile number!", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private void searchNumber() {
        String delearID = Util.getSharedPrefrenceValue(ConsumerOrderDisplay.this, Constant.Sp_emp_grpCode);
        String UserID = Util.getSharedPrefrenceValue(ConsumerOrderDisplay.this, Constant.Sp_UserID);
        Map<String, String> jsonParams = new HashMap<String, String>();
        jsonParams.put("p_user_id", UserID);//---9219151562
        jsonParams.put("p_dealer_id", delearID);
        jsonParams.put("p_customer_mobile", etMobileNo.getText().toString());
        JsonObjectRequest myRequest = new JsonObjectRequest(
                Request.Method.POST,
                "https://greatplus.com/api_services/fetch_order_api.php",
                new JSONObject(jsonParams),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progress.dismiss();
                        Log.d("searchResult", response.toString());
                        try {
                            String status = response.getString("status");
                            if (status.equalsIgnoreCase("200")) {
                                JSONArray jsonArray = response.getJSONArray("Data");

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progress.dismiss();
                        Toast.makeText(ConsumerOrderDisplay.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };
        RequestQueue MyRequestQueue = Volley.newRequestQueue(ConsumerOrderDisplay.this);
        MyRequestQueue.add(myRequest);

    }
}