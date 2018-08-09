package com.erp.sheelafoam.sheelafoam.xperia.activity;


import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.erp.sheelafoam.R;
import com.erp.sheelafoam.constant.Constant;
import com.erp.sheelafoam.sheelafoam.utility.ApiList;
import com.erp.sheelafoam.sheelafoam.utility.AsyncTaskListner;
import com.erp.sheelafoam.sheelafoam.utility.GlobalVariables;
import com.erp.sheelafoam.sheelafoam.utility.HelperMethods;
import com.erp.sheelafoam.sheelafoam.utility.MyCustomAsyncTask;
import com.erp.sheelafoam.sheelafoam.xperia.CompalintTypeAdapter;
import com.erp.sheelafoam.sheelafoam.xperia.ComplaintTypeModel;
import com.erp.sheelafoam.sheelafoam.xperia.DealerAdapter;
import com.erp.sheelafoam.sheelafoam.xperia.DealerModel;
import com.erp.sheelafoam.sheelafoam.xperia.Product;
import com.erp.sheelafoam.sheelafoam.xperia.ProductAdapter;
import com.erp.sheelafoam.utils.Util;
import com.google.gson.Gson;
import com.journeyapps.barcodescanner.CaptureActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogComplaintActivity extends AppCompatActivity implements View.OnClickListener, AsyncTaskListner {
    private static final int RequestPermissionCode = 1001;
    Toolbar toolbar;
    TextView toolbarTitle;
    EditText etState, etCity;
    ProgressDialog progressDialog;
    String stateUrl;
    ArrayList<String> stateName = new ArrayList<String>();
    ArrayList<String> cityName = new ArrayList<String>();
    ArrayList<String> ZoneName = new ArrayList<String>();
    ArrayAdapter stateListAdapter, cityListAdapter;
    String zone;
    AlertDialog alert;
    JSONObject jsonObjRequest;
    MyCustomAsyncTask myCustomAsyncTask;
    TextView complaintType;
    TextView productTv, customerDetailsTv, purchaseDateTv, searchTv, scanTv, dealerNameEt;
    Button submitBtn;
    EditText customerNameEt, customerAddressEt, customerPhnEt, customer_emailId, pinEt, serialNumberEt, searchSerialNumberEt, purchasedMrp, existingMrp, searchBundleNumberEt;
    String customerName, customerAddress, customerPhne, customerEmailID, dealerName, city, state, pin, productName;
    String logType, txtToolbar = "Log Post Usage";
    EditText lenthEt, widthEt, thickEt;
    EditText dlrState, dlrCity, dlrPin;
    String dState, dCity, dPin;
    String action, userType_new;
    AlertDialog complainAlertDialog;
    String complain_type_name;
    List<ComplaintTypeModel> complainTypeList = new ArrayList<>();
    DealerAdapter dealerAdapter;
    String dealerId;
    String dealerZone, dealerCategory;
    List<DealerModel> dealerList = new ArrayList<>();
    MyCustomAsyncTask customAsyncTask;
    ListView AllState;
    ListView stateCity;
    String subProduct;
    ProductAdapter productAdapter;
    List<Product> products = new ArrayList<>();
    private int mYear, mMonth, mDay;
    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    showDate(arg1, arg2 + 1, arg3);
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_complaint);
        logType = getIntent().getStringExtra("log");
        if (logType.equalsIgnoreCase("pre")) {
            txtToolbar = "Log Pre Usage";
        }
        Log.i("Logs", logType);
        try {
            initViews();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initViews() throws JSONException {
        toolbar = (Toolbar) findViewById(R.id.app_toolbar);
        toolbarTitle = (TextView) findViewById(R.id.app_toolbar_title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarTitle.setText(txtToolbar);
        customAsyncTask = new MyCustomAsyncTask(this);
        myCustomAsyncTask = new MyCustomAsyncTask(this);
        dealerNameEt = (TextView) findViewById(R.id.dealer_name);
        customerNameEt = (EditText) findViewById(R.id.customer_name);
        customerPhnEt = (EditText) findViewById(R.id.customer_mobile);
        customer_emailId = (EditText) findViewById(R.id.customer_emailId);
        customerAddressEt = (EditText) findViewById(R.id.customer_address);
        purchasedMrp = (EditText) findViewById(R.id.purchased_mrp);
        existingMrp = (EditText) findViewById(R.id.existing_mrp);
        pinEt = (EditText) findViewById(R.id.pin);
        lenthEt = (EditText) findViewById(R.id.length);
        widthEt = (EditText) findViewById(R.id.width);
        thickEt = (EditText) findViewById(R.id.thick);
        searchBundleNumberEt = (EditText) findViewById(R.id.search_bundle_number);
        searchSerialNumberEt = (EditText) findViewById(R.id.search_serial_num);
        serialNumberEt = (EditText) findViewById(R.id.serial_number);
        productTv = (TextView) findViewById(R.id.product);
        customerDetailsTv = (TextView) findViewById(R.id.customer_details_tv);
        submitBtn = (Button) findViewById(R.id.submit);
        purchaseDateTv = (TextView) findViewById(R.id.purchase_date);
        searchTv = (TextView) findViewById(R.id.tv_search);
        scanTv = (TextView) findViewById(R.id.tv_scan);
        complaintType = (TextView) findViewById(R.id.complaint_type);
        dlrCity = (EditText) findViewById(R.id.dlrCity);
        dlrPin = (EditText) findViewById(R.id.dlrPin);
        dlrState = (EditText) findViewById(R.id.dlrState);
        dlrState.setOnClickListener(this);
        dlrCity.setOnClickListener(this);
        scanTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EnableRuntimePermissionToAccessCamera();
                callScanIntent();
            }
        });
        complaintType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showComplaintAlert();
            }
        });
        etState = (EditText) findViewById(R.id.state);
        etCity = (EditText) findViewById(R.id.city);
        etState.setOnClickListener(this);
        etCity.setOnClickListener(this);
        submitBtn.setOnClickListener(this);
        dealerNameEt.setOnClickListener(this);
        purchaseDateTv.setOnClickListener(this);
        searchTv.setOnClickListener(this);
        productTv.setOnClickListener(this);

        if (logType.equals("pre")) {
            customerDetailsTv.setVisibility(View.GONE);
            customerNameEt.setVisibility(View.GONE);
            customerAddressEt.setVisibility(View.GONE);
            customerPhnEt.setVisibility(View.GONE);
            customer_emailId.setVisibility(View.GONE);
            dlrState.setVisibility(View.VISIBLE);
            dlrPin.setVisibility(View.VISIBLE);
            dlrCity.setVisibility(View.VISIBLE);
            etCity.setVisibility(View.GONE);
            etState.setVisibility(View.GONE);
            pinEt.setVisibility(View.GONE);
        }
        progressDialog = new ProgressDialog(LogComplaintActivity.this);
        progressDialog.setMessage("Loading....");

        getComplaintType();
        String dealerId = Util.getSharedPrefrenceValue(this, Constant.Sp_DealerID);
        if (dealerId != null) {
            dialog_Dealer_New(dealerId);
        } else {
            dialog_Dealer_New("");
        }
        findProducts();
        stateUrl = "http://125.19.46.252/ws/get_all_state.php";
        new getAllState().execute();
        stateListAdapter = new ArrayAdapter(LogComplaintActivity.this, R.layout.spinner_items, stateName);
        userType_new = Util.getSharedPrefrenceValue(LogComplaintActivity.this, Constant.Sp_User_Type);
        System.out.println("AnandType" + userType_new);

    }

    public void callScanIntent() {
        Util.setSharedPrefrenceValue(LogComplaintActivity.this, Constant.PREFS_NAME, Constant.CHECK_ACTIVITY_STATUS, "2");
        // Intent intent = new Intent("com.google.zxing.client.android.SCAN");
        // intent.setPackage("com.google.zxing.client.android");
        Intent intent = new Intent(LogComplaintActivity.this, CaptureActivity.class);
        // intent.setPackage("com.google.zxing.client.android");
        intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
        startActivityForResult(intent, 0);
    }

    public void EnableRuntimePermissionToAccessCamera() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(LogComplaintActivity.this,
                Manifest.permission.CAMERA)) {

            // Printing toast message after enabling runtime permission.
            //Toast.makeText(MainActivity.this,"CAMERA permission allows us to Access CAMERA app", Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(LogComplaintActivity.this, new String[]{Manifest.permission.CAMERA}, RequestPermissionCode);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case RequestPermissionCode:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {

                }
                break;
        }
    }

    private void showComplaintAlert() {

        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(LogComplaintActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.complaint_type_alert_dilaog, null);

        ListView complaintListView = (ListView) dialogView.findViewById(R.id.complaint_list);
        CompalintTypeAdapter compalintTypeAdapter = new CompalintTypeAdapter(this, complainTypeList);
        complaintListView.setAdapter(compalintTypeAdapter);
        complaintListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                complain_type_name = complainTypeList.get(i).getComplaint_name();
                complaintType.setText(complainTypeList.get(i).getComplaint_name());
                complainAlertDialog.dismiss();
            }
        });


        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(true);

        complainAlertDialog = dialogBuilder.create();
        complainAlertDialog.show();


    }

    ;

    private void getComplaintType() {
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("method", "complaint_type");
//        JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest(Request.Method.POST, )
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, "https://greatplus.com/service/complaint", new JSONObject(hashMap),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        if (response != null) {
                            try {
                                JSONArray data = response.getJSONArray("data");
                                for (int i = 0; i < data.length(); i++) {
                                    JSONObject complainObject = data.getJSONObject(i);
                                    ComplaintTypeModel complaintTypeModel = new ComplaintTypeModel(complainObject.getString("COMPLAIN_TYPE_CODE"),
                                            complainObject.getString("COMPLAIN_TYPE_NAME"));

                                    complainTypeList.add(complaintTypeModel);
                                }
                            } catch (Exception e) {

                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(LogComplaintActivity.this, "Could not process your request, check your internet connection and try again!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });


        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.state:
                if (stateName.size() > 0) {
                    stateListAdapter = new ArrayAdapter(LogComplaintActivity.this, R.layout.spinner_items, stateName);
                    stateListAdapter.notifyDataSetChanged();
                    dialogState();
                } else {
                    //new getAllState().execute();
                }
                break;
            case R.id.dlrState:
                if (stateName.size() > 0) {
                    stateListAdapter = new ArrayAdapter(LogComplaintActivity.this, R.layout.spinner_items, stateName);
                    stateListAdapter.notifyDataSetChanged();
                    dialogState();
                } else {
                    //new getAllState().execute();
                }
                break;
            case R.id.city:
                String s = etState.getText().toString();
                if (s.equalsIgnoreCase("")) {
                    Toast.makeText(this, "Please select a state first", Toast.LENGTH_SHORT).show();
                } else {
                    cityListAdapter = new ArrayAdapter(LogComplaintActivity.this, R.layout.spinner_items, cityName);
                    cityListAdapter.notifyDataSetChanged();
                    dialogCity();
                }
                break;
            case R.id.dlrCity:
                String dS = dlrState.getText().toString();
                if (dS.equalsIgnoreCase("")) {
                    Toast.makeText(this, "Please select a state first", Toast.LENGTH_SHORT).show();
                } else {
                    cityListAdapter = new ArrayAdapter(LogComplaintActivity.this, R.layout.spinner_items, cityName);
                    cityListAdapter.notifyDataSetChanged();
                    dialogCity();
                }
                break;

            case R.id.submit:
                if (canSendRequest())
                    try {
                        logComplaint();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                break;
            case R.id.purchase_date:
                showDialog(999);
                break;

            case R.id.tv_search:
                if (!searchSerialNumberEt.getText().toString().equals("") || !searchBundleNumberEt.getText().toString().equals("")) {
                    GlobalVariables.hideSoftKeyboard(LogComplaintActivity.this);
                    customAsyncTask.showDialog(LogComplaintActivity.this);
                    searchTheProduct(searchSerialNumberEt.getText().toString(), searchBundleNumberEt.getText().toString());
                } else {
                    Toast.makeText(this, "Please enter serial number or bundle number", Toast.LENGTH_SHORT).show();
                }
                break;


            case R.id.product:
                HelperMethods.hideSoftKeyboard(this);
                String d = dealerNameEt.getText().toString();
                if (d.equals("")) {
                    Toast.makeText(this, "Please select a dealer first", Toast.LENGTH_SHORT).show();
                } else {
                    if (products.size() > 0)
                        showProductList();
                    else
                        findProducts();
                }
                break;

            case R.id.dealer_name:
                showDealerDialog();
                break;
            default:
                break;
        }
    }

    private boolean isVlid() {

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                String scaned_qrcode = data.getStringExtra("SCAN_RESULT");
                Log.d("complaint", scaned_qrcode);
                customAsyncTask.showDialog(LogComplaintActivity.this);
                searchTheProduct(scaned_qrcode, "");


//                setScannedQrcodeOnUI(mobileNum, scaned_qrcode);
                // String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
                //Toast.makeText(this, scaned_qrcode, Toast.LENGTH_LONG).show();
//                rlayout_qrcode.setVisibility(View.VISIBLE);

                // json request for get points from server.

                // jsonRequestForGetPointsFromSerevr();
//                jsonRequestForGetPointsFromSerevrWithDealerCode();

            }
            // Handle successful scan
        } else if (resultCode == RESULT_CANCELED) {
            // Handle cancel
        }
    }

    private void showDealerDialog() {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(LogComplaintActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.complaint_type_alert_dilaog, null);
        ListView dealerListView = (ListView) dialogView.findViewById(R.id.complaint_list);
        TextView heading = (TextView) dialogView.findViewById(R.id.heading);
        heading.setText("Dealers");
        dealerAdapter = new DealerAdapter(this, dealerList);
        dealerListView.setAdapter(dealerAdapter);
        dealerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                dealerNameEt.setText(dealerList.get(i).getDealerName());
                dealerId = dealerList.get(i).getDealerId();
                dealerCategory = dealerList.get(i).getDealerCateogry();
                dealerZone = dealerList.get(i).getDealerZone();
                complainAlertDialog.dismiss();
                findProducts();
            }
        });


        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(true);

        complainAlertDialog = dialogBuilder.create();
        complainAlertDialog.show();
    }

    //-------use canMethosCall--------------------------------------------------------------
    private void searchTheProduct(final String serialNumber, String bundleNumber) {
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("method", "filter_products");
        hashMap.put("serial_no", serialNumber);
        hashMap.put("bundle_no", bundleNumber);
        JSONObject jsoRequestObject = new JSONObject(hashMap);
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, "https://greatplus.com/service/complaint", jsoRequestObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        customAsyncTask.hideDialog(LogComplaintActivity.this);
                        if (response != null) {
                            try {
                                Log.d("complaint", response.toString());
                                JSONObject data = response.getJSONObject("data");
                                String cn = data.getString("op_customer_name");
                                if (!cn.equals("null"))
                                    customerNameEt.setText(data.getString("op_customer_name"));
                                if (!data.getString("op_customer_mobile").equals("null"))
                                    customerPhnEt.setText(data.getString("op_customer_mobile"));
                                if (!data.getString("op_dealer_name").equals("null"))
                                    dealerNameEt.setText(data.getString("op_dealer_name"));
                                if (!data.getString("op_existing_mrp").equals("null"))
                                    existingMrp.setText(data.getString("op_existing_mrp"));
                                lenthEt.setText(data.getString("op_length"));
                                widthEt.setText(data.getString("op_breadth"));
                                thickEt.setText(data.getString("op_thick"));
                                subProduct = data.getString("op_sub_product");
                                purchasedMrp.setText(data.getString("op_purchased_mrp"));
                                productTv.setText(data.getString("op_product_specification"));
                                purchaseDateTv.setText(data.getString("op_zd_invoice_date"));
                                serialNumberEt.setText(serialNumber);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(LogComplaintActivity.this, "Your request could not be processed, please try again!", Toast.LENGTH_SHORT).show();
                        customAsyncTask.hideDialog(LogComplaintActivity.this);

                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void dialog_Dealer_New(final String dealerId) throws JSONException {
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("method", "complaint_dealers");
        hashMap.put("user_id", Util.getSharedPrefrenceValue(this, Constant.Sp_GrtPlusUserID));
        hashMap.put("dealer_id", Util.getSharedPrefrenceValue(this, Constant.Sp_emp_grpCode));
//        JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest(Request.Method.POST, )
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, "https://greatplus.com/service/complaint", new JSONObject(hashMap),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (response != null) {
                            try {
                                JSONArray data = response.getJSONArray("data");
                                for (int i = 0; i < data.length(); i++) {
                                    JSONObject dealerData = data.getJSONObject(i);
                                    DealerModel dealerModel = new DealerModel(dealerData.getString("DEALER_NAME"), dealerData.getString("DEALER_ZONE"),
                                            dealerData.getString("DEALER_CATEGORY"), dealerData.getString("DEALER_ID"));

                                    if (userType_new.equalsIgnoreCase("DEALER")) {
                                        dealerNameEt.setText(dealerData.getString("DEALER_NAME"));
                                    }

                                    dealerList.add(dealerModel);
                                    //dealerId = dealerList.get(i).getDealerId();
                                    dealerCategory = dealerList.get(i).getDealerCateogry();
                                    dealerZone = dealerList.get(i).getDealerZone();
                                }


                            } catch (Exception e) {
                            }
                        } else {
                            Toast.makeText(LogComplaintActivity.this, "Could not process your request, check your internet connection and try again!", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(LogComplaintActivity.this, "Could not process your request, check your internet connection and try again!", Toast.LENGTH_SHORT).show();
                        finish();

                    }
                });


        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private void findProducts() {
        action = "2";
        JSONObject jsonObjectRequest = new JSONObject();
        try {
            jsonObjectRequest.put("request", "getProduct");
            jsonObjectRequest.put("p_dealer_name", dealerNameEt.getText().toString());
            jsonObjectRequest.put("p_zone", dealerZone);
            jsonObjectRequest.put("p_dealer_category", dealerCategory);
            Log.d("Anand", jsonObjectRequest.toString());
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        customAsyncTask.showDialog(this);
        new MyCustomAsyncTask(this, jsonObjectRequest, ApiList.URL_PRODUCTS).execute();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == 999) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
            int yy = calendar.get(Calendar.YEAR);
            int mm = calendar.get(Calendar.MONTH);
            int dd = calendar.get(Calendar.DAY_OF_MONTH);
//            today = calendar.getTime();
            return new DatePickerDialog(this,
                    myDateListener, yy, mm, dd);
        }
        return null;
    }

    private void showDate(int year, int month, int day) {
        purchaseDateTv.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
    }

    private void logComplaint() throws JSONException {

        customAsyncTask.showDialog(LogComplaintActivity.this);
        SharedPreferences sharedPreferences = getSharedPreferences(Constant.PREFS_NAME, MODE_PRIVATE);
        String userId = sharedPreferences.getString(Constant.Sp_GrtPlusUserID, "");

        HashMap<String, String> hashMap = new HashMap<String, String>();
        if (logType.equals("post")) {
            hashMap.put("pre_post_usage", "post usage");
            hashMap.put("city", city);
            hashMap.put("state", state);
            hashMap.put("pincode", pin);
        } else {
            hashMap.put("city", dCity);
            hashMap.put("state", dState);
            hashMap.put("pincode", dPin);
            hashMap.put("pre_post_usage", "pre usage");
        }

        hashMap.put("method", "save_new");//---save--replace to------
        hashMap.put("user_id", userId);
        hashMap.put("customer_name", customerName);
        hashMap.put("customer_mobile", customerPhne);
        hashMap.put("customer_email", customerEmailID);//------addnew field----------
        hashMap.put("customer_address", customerAddress);
        hashMap.put("dealer_name", dealerName);
        hashMap.put("dealer_address", "");
        hashMap.put("comp_type_name", complain_type_name);
        hashMap.put("sub_product", subProduct);
        hashMap.put("serial", serialNumberEt.getText().toString());
        hashMap.put("mrp", purchasedMrp.getText().toString());
        hashMap.put("purchase_date", purchaseDateTv.getText().toString());
        hashMap.put("product", productTv.getText().toString());
        hashMap.put("length", lenthEt.getText().toString());
        hashMap.put("bredth", widthEt.getText().toString());
        hashMap.put("thick", thickEt.getText().toString());
//        JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest(Request.Method.POST, )

        Log.d("complaint", new JSONObject(hashMap).toString());
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, "https://greatplus.com/service/complaint", new JSONObject(hashMap),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        customAsyncTask.hideDialog(LogComplaintActivity.this);

                        if (response != null) {
                            try {
                                Gson gson = new Gson();
                                int status = response.getInt("status");
                                if (status == 200) {
                                    Toast.makeText(LogComplaintActivity.this, "Details Saved Successfully" +"," +response.getString("complaint_no"), Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    Toast.makeText(LogComplaintActivity.this, response.getString("msg"), Toast.LENGTH_SHORT).show();
                                }


                            } catch (Exception e) {
                            }
                        } else {
                            Toast.makeText(LogComplaintActivity.this, "Request could not be processed, please try again!", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        customAsyncTask.hideDialog(LogComplaintActivity.this);
                        Toast.makeText(LogComplaintActivity.this, "Request could not be processed, please try again!", Toast.LENGTH_SHORT).show();

                    }
                });


        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private boolean canSendRequest() {
        customerName = customerNameEt.getText().toString();
        customerAddress = customerAddressEt.getText().toString();
        customerPhne = customerPhnEt.getText().toString();
        customerEmailID = customer_emailId.getText().toString();
        dealerName = dealerNameEt.getText().toString();
        state = etState.getText().toString();
        pin = pinEt.getText().toString();
        city = etCity.getText().toString();
        productName = productTv.getText().toString();
        dState = dlrState.getText().toString();
        dCity = dlrCity.getText().toString();
        dPin = dlrPin.getText().toString();
        if (logType.equalsIgnoreCase("pre")) {
            if (productTv.getText().toString().isEmpty()) {
                Toast.makeText(LogComplaintActivity.this, "Please select product!", Toast.LENGTH_LONG).show();
                return false;
            } else if (lenthEt.getText().toString().isEmpty()) {
                Toast.makeText(LogComplaintActivity.this, "Please enter length!", Toast.LENGTH_LONG).show();
                return false;
            } else if (widthEt.getText().toString().isEmpty()) {
                Toast.makeText(LogComplaintActivity.this, "Please enter width!", Toast.LENGTH_LONG).show();
                return false;
            } else if (thickEt.getText().toString().isEmpty()) {
                Toast.makeText(LogComplaintActivity.this, "Please enter thick!", Toast.LENGTH_LONG).show();
                return false;
            } else if (purchaseDateTv.getText().toString().isEmpty()) {
                Toast.makeText(LogComplaintActivity.this, "Please select purchase date!", Toast.LENGTH_LONG).show();
                return false;
            } else if (dlrState.getText().toString().isEmpty()) {
                Toast.makeText(LogComplaintActivity.this, "Please select state!", Toast.LENGTH_LONG).show();
                return false;
            } else if (dlrCity.getText().toString().isEmpty()) {
                Toast.makeText(LogComplaintActivity.this, "Please select city!", Toast.LENGTH_LONG).show();
                return false;
            } else if (dlrPin.getText().toString().isEmpty() && dlrPin.getText().toString().length()!=6) {
                Toast.makeText(LogComplaintActivity.this, "Please enter valid pin no!", Toast.LENGTH_LONG).show();
                return false;
            }else if (purchasedMrp.getText().toString().isEmpty()) {
                Toast.makeText(LogComplaintActivity.this, "Please enter purchased  mrp!", Toast.LENGTH_LONG).show();
                return false;
            } else if (complaintType.getText().toString().isEmpty()) {
                Toast.makeText(LogComplaintActivity.this, "Please select complaint type !", Toast.LENGTH_LONG).show();
                return false;
            }
        } else {
            if (customerNameEt.getText().toString().isEmpty()) {
                Toast.makeText(LogComplaintActivity.this, "Please enter customer name !", Toast.LENGTH_LONG).show();
                return false;
            } else if (customerPhnEt.getText().toString().isEmpty()&& customerPhnEt.getText().toString().length()!=10) {
                Toast.makeText(LogComplaintActivity.this, "Please enter valid mobile number !", Toast.LENGTH_LONG).show();
                return false;
            } else if (!isValidEmail(customerEmailID)) {
                Toast.makeText(LogComplaintActivity.this, "Please enter valid emailId !", Toast.LENGTH_LONG).show();
                return false;
            } else if (customerAddressEt.getText().toString().isEmpty()) {
                Toast.makeText(LogComplaintActivity.this, "Please enter customer address!", Toast.LENGTH_LONG).show();
                return false;
            } else if (etState.getText().toString().isEmpty()) {
                Toast.makeText(LogComplaintActivity.this, "Please select state!", Toast.LENGTH_LONG).show();
                return false;
            } else if (etCity.getText().toString().isEmpty()) {
                Toast.makeText(LogComplaintActivity.this, "Please select city!", Toast.LENGTH_LONG).show();
                return false;
            } else if (pinEt.getText().toString().isEmpty()&& pinEt.getText().toString().length()!=6) {
                Toast.makeText(LogComplaintActivity.this, "Please enter valid pin no!", Toast.LENGTH_LONG).show();
                return false;
            } else if (productTv.getText().toString().isEmpty()) {
                Toast.makeText(LogComplaintActivity.this, "Please select product!", Toast.LENGTH_LONG).show();
                return false;
            } else if (lenthEt.getText().toString().isEmpty()) {
                Toast.makeText(LogComplaintActivity.this, "Please enter length!", Toast.LENGTH_LONG).show();
                return false;
            } else if (widthEt.getText().toString().isEmpty()) {
                Toast.makeText(LogComplaintActivity.this, "Please enter width!", Toast.LENGTH_LONG).show();
                return false;
            } else if (thickEt.getText().toString().isEmpty()) {
                Toast.makeText(LogComplaintActivity.this, "Please enter thick!", Toast.LENGTH_LONG).show();
                return false;
            } else if (complaintType.getText().toString().isEmpty()) {
                Toast.makeText(LogComplaintActivity.this, "Please select complaint type !", Toast.LENGTH_LONG).show();
                return false;
            }
        }
        return true;

    }
    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public void dialogState() {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(LogComplaintActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.product_state_dialog, null);
        final EditText txtSearch = (EditText) dialogView.findViewById(R.id.txtSearch);
        ImageView close = (ImageView) dialogView.findViewById(R.id.closeDialog);
        AllState = (ListView) dialogView.findViewById(R.id.allState);
        AllState.setAdapter(stateListAdapter);

        AllState.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                int pos = stateName.indexOf(parent.getAdapter().getItem(position).toString());
                if (logType.equalsIgnoreCase("pre")) {
                    dlrState.setText(parent.getAdapter().getItem(position).toString());
                    getStateCity(dlrState.getText().toString().toUpperCase());
                    zone = ZoneName.get(pos);
                    dlrCity.setText("");
                } else {
                    etState.setText(parent.getAdapter().getItem(position).toString());
                    getStateCity(etState.getText().toString().toUpperCase());
                    zone = ZoneName.get(pos);
                    etCity.setText("");
                }
                //Toast.makeText(getApplicationContext(), pos+" " + Zone, 500).show()


                alert.dismiss();
            }

        });

        txtSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                LogComplaintActivity.this.stateListAdapter.getFilter().filter(txtSearch.getText().toString());
            }
        });

        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(false);

        alert = dialogBuilder.create();
        alert.setCanceledOnTouchOutside(true);
        alert.setCancelable(true);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });
        alert.show();
    }

    private void dialogCity() {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(LogComplaintActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.product_state_dialog, null);
        final EditText txtSearch = (EditText) dialogView.findViewById(R.id.txtSearch);
        ImageView close = (ImageView) dialogView.findViewById(R.id.closeDialog);

        TextView heading = (TextView) dialogView.findViewById(R.id.heading);
        heading.setText("Select City");
        stateCity = (ListView) dialogView.findViewById(R.id.allState);
        stateCity.setAdapter(cityListAdapter);

        stateCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("City", "" + parent.getAdapter().getItem(position).toString());
                if (logType.equalsIgnoreCase("pre")) {
                    dlrCity.setText(parent.getAdapter().getItem(position).toString());
                } else {
                    etCity.setText(parent.getAdapter().getItem(position).toString());
                }

                alert.dismiss();
            }

        });

        txtSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                LogComplaintActivity.this.cityListAdapter.getFilter().filter(txtSearch.getText().toString());
            }
        });

        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(false);

        alert = dialogBuilder.create();
        alert.setCanceledOnTouchOutside(true);
        alert.setCancelable(true);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });
        alert.show();
    }

    private void getStateCity(String state) {
        action = "1";
        jsonObjRequest = new JSONObject();
        try {
            jsonObjRequest.put("method", "citylist");
            jsonObjRequest.put("state", state);


            Log.d("City Req", jsonObjRequest.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        myCustomAsyncTask.showDialog(LogComplaintActivity.this);
        new MyCustomAsyncTask(LogComplaintActivity.this, jsonObjRequest, Constant.WS_URL + Constant.WS_FILTER_COMPLAINT_LIST).execute();
        //if (!progressDialog.isShowing()) {
        //progressDialog.show();
        // }

    }

    private void showProductList() {

        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(LogComplaintActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.complaint_type_alert_dilaog, null);

        ListView dealerListView = (ListView) dialogView.findViewById(R.id.complaint_list);
        TextView heading = (TextView) dialogView.findViewById(R.id.heading);
        heading.setText("PRODUCTS");

        productAdapter = new ProductAdapter(this, products);
        dealerListView.setAdapter(productAdapter);
        dealerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                productTv.setText(products.get(i).getProductName());
                subProduct = products.get(i).getSubProduct();
                complainAlertDialog.dismiss();

            }
        });


        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(true);

        complainAlertDialog = dialogBuilder.create();
        complainAlertDialog.show();

    }

    @Override
    public void onTaskComplete(String result) {

        if (action.equals("1")) {
            cityName.clear();
            myCustomAsyncTask.hideDialog(LogComplaintActivity.this);
//        if (progressDialog.isShowing()) {
//            progressDialog.dismiss();
//        }
            Log.d("City Res", " " + result);
            try {
                JSONObject jObj = new JSONObject(result);
                if (jObj.getString("status").equalsIgnoreCase("200")) {
                    JSONArray jArray = jObj.getJSONArray("data");
                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject jArrayObj = jArray.getJSONObject(i);
                        cityName.add(jArrayObj.getString("CITY"));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            customAsyncTask.hideDialog(LogComplaintActivity.this);
            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONObject data = jsonObject.getJSONObject("data");
                JSONArray jsonArray = data.getJSONArray("product");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    Product product = new Product(jsonObject1.getString("product_display_name"),
                            jsonObject1.getString("sub_product"));
                    products.add(product);
                }
            } catch (Exception jsonexception) {

            }
        }

    }

    public class getAllState extends AsyncTask<String, String, String> {
        StringBuilder stringBuilder = new StringBuilder();

        @Override
        protected void onPreExecute() {
            if (!progressDialog.isShowing()) {
                progressDialog.show();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                URL url = new URL(stateUrl);
                HttpURLConnection htp = (HttpURLConnection) url.openConnection();
                htp.setRequestMethod("GET");
                htp.setRequestProperty("Content-Type", "application/json");
                htp.setDoInput(true);
                htp.setDoOutput(true);
                htp.setUseCaches(false);
                htp.connect();
                InputStream inputStream = htp.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String Line;
                while ((Line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(Line);
                }
                return stringBuilder.toString();
            } catch (Exception e) {

            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                Log.e("Response", s);
                stateName.clear();
                ZoneName.clear();
                JSONObject obj = new JSONObject(s);
                JSONArray arr = obj.getJSONArray("state");
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject o = arr.getJSONObject(i);
                    stateName.add(o.getString("STATE"));
                    ZoneName.add(o.getString("ZONE"));
                }
                stateListAdapter.notifyDataSetChanged();

                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }


            } catch (Exception e) {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                e.printStackTrace();
                Toast.makeText(LogComplaintActivity.this, "Network Error Occurred...", Toast.LENGTH_SHORT).show();
            }
        }


    }

}
