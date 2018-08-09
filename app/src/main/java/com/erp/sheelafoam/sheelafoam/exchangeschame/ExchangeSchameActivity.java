package com.erp.sheelafoam.sheelafoam.exchangeschame;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.erp.sheelafoam.R;
import com.erp.sheelafoam.adapter.DialogBundallAdapter;
import com.erp.sheelafoam.constant.Constant;
import com.erp.sheelafoam.model.RepresentativeName;
import com.erp.sheelafoam.model.SerialNumModel;
import com.erp.sheelafoam.model.UserModel;
import com.erp.sheelafoam.passbook.PassbookMainActivity;
import com.erp.sheelafoam.sheelafoam.activity.GuaranteeLog;
import com.erp.sheelafoam.sheelafoam.activity.SplashActivity;
import com.erp.sheelafoam.sheelafoam.utility.AsyncTaskListner;
import com.erp.sheelafoam.sheelafoam.utility.ConnectionDetector;
import com.erp.sheelafoam.sheelafoam.utility.GlobalVariables;
import com.erp.sheelafoam.sheelafoam.utility.MyAsyncTask;
import com.erp.sheelafoam.sheelafoam.xperia.activity.UnregisterGcard;
import com.erp.sheelafoam.utils.CustomDialog;
import com.erp.sheelafoam.utils.CustomTypefaceTextView;
import com.erp.sheelafoam.utils.URLS;
import com.erp.sheelafoam.utils.Util;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.journeyapps.barcodescanner.CaptureActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import static android.graphics.Color.TRANSPARENT;

public class ExchangeSchameActivity extends AppCompatActivity implements OnClickListener, AsyncTaskListner {
    private static final int CAMERA_CONSTANT = 100;
    public final String receiverMobleNo = "9212273322";
    final String[] gender = new String[1];
    private final int SCAN_REQUEST_CODE_ONE = 1;
    private final int SCAN_REQUEST_CODE_TWO = 2;
    boolean buttonClick = false;
    CustomDialog cd;
    String check = "NA";
    AutoCompleteTextView autocomplete_representativeName;
    CustomDialog dialog1;
    ArrayList<String> arrayList = new ArrayList<>();
    LinearLayout llayout_tbl_header;
    Context mContext;
    Toolbar toolbar;
    int action;
    JSONObject json_request;
    JSONObject json_response;
    ConnectionDetector con;
    TextView invoiceDateTv;
    EditText invoiceNumber;
    TextView unregisterGcardTv;
    boolean dateSelected = false;
    UserModel userModel;
    private RadioGroup radiogroupScheme;
    private RadioButton radioScheme, radioNoScheme, radioReplace;
    private TextView textview_app_title, tv_passbook, textview_send, btn_exOrder;
    private TextView scanSerialNoOneTV, scanSerialNoTwoTV, getBundle, tv_guaranteeLog, tv_Gcard;
    private EditText edittext_serialNoOne, edittext_serialNoTwo, edittext_customerNo, etName, etEmail, etBundleNo;
    private CheckBox checkbox_schame, checkbox_No_scheme, checkbox_replacesment_scheme;
    private String role = "", mobileNum = "", eocb = "", smile = "", mattress = "", serialNoOne = "", serialNoTwo = "", customerMobileNo = "", customerName = "", customerEmail = "";
    private String alertMessage = "", requestMSG = "", responseSMS = "", scanedQrcodeOne, scanedQrcodeTwo;
    private boolean isChecked_exchangeSchame, isChecked_exchangeSchameNA, isChecked_exchangeSchameRE;
    private SharedPreferences mPrefs;
    private ProgressDialog dialog;
    private SharedPreferences loginPref;
    private int mYear, mMonth, mDay;
    private ProgressDialog progress;
    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    showDate(arg1, arg2 + 1, arg3);
                }
            };
    private BroadcastReceiver responseSMSReceiver = new BroadcastReceiver() {
        final SmsManager sms = SmsManager.getDefault();

        public void onReceive(Context context, Intent intent) {
            final Bundle bundle = intent.getExtras();
            try {
                if (bundle != null) {

                    final Object[] pdusObj = (Object[]) bundle.get("pdus");
                    for (int i = 0; i < pdusObj.length; i++) {
                        SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                        String phoneNumber = currentMessage.getDisplayOriginatingAddress();
                        String senderNum = phoneNumber;
                        responseSMS = currentMessage.getDisplayMessageBody();
                        if (senderNum.equals("VM-SHFOAM") || responseSMS.contains(serialNoOne) || responseSMS.contains(serialNoTwo)
                                || responseSMS.contains(customerMobileNo) || responseSMS.contains("Mattress")) {
                            defaultOneButtonDialog(ExchangeSchameActivity.this, responseSMS);
                        }


                    }
                }

            } catch (Exception e) {
                Log.e("SmsReceiver", "Exception smsReceiver" + e);

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange_schame);
        mPrefs = getSharedPreferences("Location", Context.MODE_PRIVATE);
        con = new ConnectionDetector(ExchangeSchameActivity.this);
        mContext = ExchangeSchameActivity.this;
        progress = new ProgressDialog(this);
        role = mPrefs.getString("op_user_type", "");
        mobileNum = mPrefs.getString("op_user_mobile", "");
        loginPref = getSharedPreferences(Constant.PREFS_NAME, Context.MODE_PRIVATE);
        initialiseUiControls();
        addOnclickListner();
        radiogroupScheme.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedID) {
                switch (checkedID) {
                    case R.id.radioScheme:
                        gender[0] = "";
                        check = "";
                        break;
                    case R.id.radioNoScheme:
                        gender[0] = "NA";
                        check = "NA";
                        break;
                    case R.id.radioReplace:
                        gender[0] = "R";
                        check = "R";
                        break;
                }

            }
        });

        try {
            callSalesRepresentativeService(loginPref.getString(Constant.Sp_UserID, ""));
        } catch (Exception e) {
            e.printStackTrace();


        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) // Press Back Icon
        {
            finish();
        }
        return super.onOptionsItemSelected(item);
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

        invoiceDateTv.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
        dateSelected = true;
    }

    public void initialiseUiControls() {
        btn_exOrder = (TextView) findViewById(R.id.btn_exOrder);
        invoiceNumber = (EditText) findViewById(R.id.invoice_number);
        invoiceDateTv = (TextView) findViewById(R.id.invoice_date);
        invoiceDateTv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(999);
            }
        });
        unregisterGcardTv = (TextView) findViewById(R.id.tv_unregister);
        unregisterGcardTv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ExchangeSchameActivity.this, UnregisterGcard.class);
                startActivity(intent);
            }
        });
        radiogroupScheme = (RadioGroup) findViewById(R.id.radiogroupScheme);
        radioReplace = (RadioButton) findViewById(R.id.radioReplace);
        radioNoScheme = (RadioButton) findViewById(R.id.radioNoScheme);
        radioScheme = (RadioButton) findViewById(R.id.radioScheme);
        textview_app_title = (TextView) findViewById(R.id.app_toolbar_title);
        //textview_user_id=(TextView)findViewById(R.id.textview_user_id);
        textview_send = (TextView) findViewById(R.id.textview_send);
        tv_passbook = (TextView) findViewById(R.id.tv_passbook);
        scanSerialNoOneTV = (TextView) findViewById(R.id.SheelaFoamID_ExchangeScheme_ScanSerialNoOneTV);
        scanSerialNoTwoTV = (TextView) findViewById(R.id.SheelaFoamID_ExchangeScheme_ScanSerialNoTwoTV);
        edittext_serialNoOne = (EditText) findViewById(R.id.edittext_serialNoOne);
        edittext_serialNoTwo = (EditText) findViewById(R.id.edittext_serialNoTwo);
        edittext_customerNo = (EditText) findViewById(R.id.edittext_customerNo);
        llayout_tbl_header = (LinearLayout) findViewById(R.id.llayout_tbl_header);
        autocomplete_representativeName = (AutoCompleteTextView) findViewById(R.id.autocomplete_representativeName);
        tv_guaranteeLog = (TextView) findViewById(R.id.tv_guaranteeLog);
        etName = (EditText) findViewById(R.id.etName);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etBundleNo = (EditText) findViewById(R.id.etBundleNo);
        getBundle = (TextView) findViewById(R.id.btnBundle);
        tv_Gcard = (TextView) findViewById(R.id.tv_Gcard);
        toolbar = (Toolbar) findViewById(R.id.app_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSppanable();
        //checkbox_schame.setBackgroundColor(Color.GRAY);
        autocomplete_representativeName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                RepresentativeName representativeName = (RepresentativeName) adapterView.getItemAtPosition(i);
                autocomplete_representativeName.setText(representativeName.getSALESREPNAME());
            }
        });

    }

    public void addOnclickListner() {
        btn_exOrder.setOnClickListener(this);
        textview_send.setOnClickListener(this);
        scanSerialNoOneTV.setOnClickListener(this);
        scanSerialNoTwoTV.setOnClickListener(this);
        getBundle.setOnClickListener(this);
        tv_guaranteeLog.setOnClickListener(this);
        tv_Gcard.setOnClickListener(this);
        tv_passbook.setOnClickListener(this);
    }

    public void setSerialNo() {
        if (!serialNoOne.equalsIgnoreCase("null")) {
            edittext_serialNoOne.setText(serialNoOne);
        }
        if (!serialNoTwo.equalsIgnoreCase("null")) {
            edittext_serialNoTwo.setText(serialNoTwo);
        }
    }

    public void getInput() {
        serialNoOne = edittext_serialNoOne.getText().toString().trim();
        serialNoTwo = edittext_serialNoTwo.getText().toString().trim();
        customerMobileNo = edittext_customerNo.getText().toString().trim();
        customerEmail = etEmail.getText().toString().trim();
        customerName = etName.getText().toString().trim();

    }

    private void submitExchangeSchameDataRequest() {
        // TODO Auto-generated method stub
        action = 2;
        try {
            json_request = new JSONObject();
            json_request.put("request", "exchane_offer_submit");
            json_request.put("p_sales_rep_name", autocomplete_representativeName.getText().toString().trim());
            json_request.put("mobile1", mobileNum);
            json_request.put("p_serial_no1", serialNoOne);
            json_request.put("p_serial_no2", serialNoTwo);
            json_request.put("p_customer_mobile", customerMobileNo);
            json_request.put("p_customer_email", customerEmail);
            json_request.put("p_customer_name", customerName);
            json_request.put("p_non_eo", check);
            json_request.put("p_invoice_number", "");
            json_request.put("p_invoice_date", "");
            json_request.put("p_dealer_mobile", loginPref.getString(Constant.Sp_User_Mobile, ""));
            json_request.put("p_user_id", loginPref.getString(Constant.Sp_UserID, ""));
            Log.e("#SubmitRequest#", json_request.toString());
            new MyAsyncTask(ExchangeSchameActivity.this, json_request).execute();
            dialog = new ProgressDialog(this);
            dialog.setMessage("Loading...");
            dialog.setCancelable(false);
            dialog.show();
        } catch (Exception e) {
        }
    }

    private void getSerialNo() {
        action = 3;
        try {
            json_request = new JSONObject();
            json_request.put("request", "exchane_offer_info_bundle");
            json_request.put("p_bundle_number", etBundleNo.getText().toString().trim());
            Log.e("SerialRequest", json_request.toString());
            new MyAsyncTask(ExchangeSchameActivity.this, json_request).execute();
        } catch (Exception e) {
        }
    }

    private String createSmsString() {
        try {
            JSONObject json_request = new JSONObject();
            json_request.put("request", "exchane_offer_submit");
            json_request.put("mobile1", mobileNum);
            json_request.put("p_serial_no1", serialNoOne);
            json_request.put("p_serial_no2", serialNoTwo);
            json_request.put("p_customer_mobile", customerMobileNo);
            json_request.put("p_non_eo", isChecked_exchangeSchame);//---check
            Log.e("#Request#", json_request.toString());

        } catch (Exception e) {

        }

        return json_request.toString();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_exOrder:
                if (edittext_customerNo.getText().toString().isEmpty() || edittext_customerNo.getText().length() != 10) {
                    Toast.makeText(ExchangeSchameActivity.this, "Please enter valid mobile number !", Toast.LENGTH_LONG).show();
                } else {
                    progress = new ProgressDialog(ExchangeSchameActivity.this);
                    progress.setMessage("Please wait....");
                    progress.setCancelable(false);
                    progress.show();
                    callPreOrderService();
                }
                break;
            case R.id.textview_send:
                GlobalVariables.hideSoftKeyboard(ExchangeSchameActivity.this);
                getInput();
                if (con.isConnectingToInternet()) {
                    if (buttonClick == false) {
                        callPreOrderServiceSEND();
                    } else {
                        if (isValid()) {
                            submitExchangeSchameDataRequest();
                        }
                    }
                 /*  if (autocomplete_representativeName.getText().toString().isEmpty()) {
                        GlobalVariables.defaultOneButtonDialog(ExchangeSchameActivity.this, "Please enter sales representative name");
                    } else*/
                  /*  if ((serialNoOne != null && serialNoOne.length() > 0) || (customerMobileNo != null && customerMobileNo.length() > 0)) {
                        if (customerMobileNo.length() == 10) {
                            if (buttonClick == false) {
                                callPreOrderServiceSEND();
                            } else if (!serialNoOne.equals(serialNoTwo)) {
                                submitExchangeSchameDataRequest();
                            } else {
                                GlobalVariables.defaultOneButtonDialog(ExchangeSchameActivity.this, "Serial no one and serial no two can't be same.");
                            }
                        } else {
                            GlobalVariables.defaultOneButtonDialog(ExchangeSchameActivity.this, "Please enter valid mobile number");
                        }
                    } else {
                        GlobalVariables.defaultOneButtonDialog(ExchangeSchameActivity.this, "Some input data is required");
                    }*/
                } else {
                    TwoButtonAlertDialog(ExchangeSchameActivity.this, GlobalVariables.CONNECTION_ERROR + " " + "You want to send sms");
                }
                break;

            case R.id.btn_logout:

                logout(ExchangeSchameActivity.this);

                break;

            case R.id.SheelaFoamID_ExchangeScheme_ScanSerialNoOneTV:
                checkCameraPermission();

                //callScanIntent(SCAN_REQUEST_CODE_ONE);

                break;

            case R.id.SheelaFoamID_ExchangeScheme_ScanSerialNoTwoTV:
                callScanIntent(SCAN_REQUEST_CODE_TWO);
                break;
            case R.id.tv_passbook:
                Intent goToPassbook = new Intent(ExchangeSchameActivity.this, PassbookMainActivity.class);
                startActivity(goToPassbook);
                break;
            case R.id.tv_guaranteeLog:
                Intent intent = new Intent(ExchangeSchameActivity.this, GuaranteeLog.class);
                startActivity(intent);

       /*Intent goToNext=new Intent(ExchangeSchameActivity.this,ConsumerOredrActivity.class);
             startActivity(goToNext);*/
                break;
            case R.id.btnBundle:
                if (etBundleNo.getText().toString().isEmpty()) {
                    //resultDialog("Please enter bundle no.", true);
                    GlobalVariables.defaultOneButtonDialog(ExchangeSchameActivity.this, "Please enter bundle no.");
                } else {
                    edittext_serialNoOne.setText("");
                    edittext_serialNoTwo.setText("");
                    dialog = new ProgressDialog(this);
                    dialog.setMessage("Loading...");
                    dialog.setCancelable(false);
                    dialog.show();
                    callBundleService();

                }
                break;
            case R.id.tv_Gcard:
                Intent goToNext1 = new Intent(ExchangeSchameActivity.this, SalesRapActivity.class);
                startActivity(goToNext1);
              /*  Intent goToNext1=new Intent(ExchangeSchameActivity.this,ProductMainActivity.class);
                startActivity(goToNext1);*/
                break;
        }

    }

    public void checkCameraPermission() {
        if (ActivityCompat.checkSelfPermission(ExchangeSchameActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(ExchangeSchameActivity.this, Manifest.permission.CAMERA)) {
                //Show Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(ExchangeSchameActivity.this);
                builder.setTitle("Need Camera Permission");
                builder.setMessage("This app needs Camera permission.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(ExchangeSchameActivity.this, new String[]{Manifest.permission.CAMERA}, CAMERA_CONSTANT);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else {
                //just request the permission
                ActivityCompat.requestPermissions(ExchangeSchameActivity.this, new String[]{Manifest.permission.CAMERA}, CAMERA_CONSTANT);
            }

        } else {
            callScanIntent(SCAN_REQUEST_CODE_ONE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_CONSTANT) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //The External Storage Write Permission is granted to you... Continue your left job...
                callScanIntent(SCAN_REQUEST_CODE_ONE);
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(ExchangeSchameActivity.this, Manifest.permission.CAMERA)) {
                    //Show Information about why you need the permission
                    AlertDialog.Builder builder = new AlertDialog.Builder(ExchangeSchameActivity.this);
                    builder.setTitle("Need Camera Permission");
                    builder.setMessage("This app needs Camera permission");
                    builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            ActivityCompat.requestPermissions(ExchangeSchameActivity.this, new String[]{Manifest.permission.CAMERA}, CAMERA_CONSTANT);


                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                } else {
                    Toast.makeText(getBaseContext(), "Unable to get Permission", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    protected void onResume() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.provider.Telephony.SMS_RECEIVED");
        registerReceiver(responseSMSReceiver, filter);
        super.onResume();
    }

    @Override
    protected void onPause() {
        unregisterReceiver(responseSMSReceiver);
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        finish();
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
                        serialNoOne = jObj.getString("op_serial1");
                        serialNoTwo = jObj.getString("op_serial2");
                        if (serialNoOne == null && serialNoTwo == null || (serialNoOne.equalsIgnoreCase("null") && serialNoTwo.equalsIgnoreCase("null"))) {
                            alertMessage = jObj.getString("op_message");
                            GlobalVariables.defaultOneButtonDialog(ExchangeSchameActivity.this, alertMessage);
                        } else {
                            setSerialNo();
                        }
                    } else {
                        JSONObject jObj = json_response.getJSONObject("data");
                        alertMessage = jObj.getString("op_message");
                        GlobalVariables.defaultOneButtonDialog(ExchangeSchameActivity.this, alertMessage);
                    }
                } else if (action == 33) {
                    Log.d("response is", json_response + "");
                    try {
                        if (json_response.getString("status").equals("true")) {

                            JSONObject object = new JSONObject(json_response.getString("user"));
                            UserModel userModel = new UserModel();
                            userModel.setPUserId(object.getString("p_user_id"));
                            userModel.setPBundleNumber(object.getString("p_bundle_number"));
                            JSONArray jsonArray = new JSONArray(object.getString("serial_number"));
                            for (int i = 0; i < jsonArray.length(); i++) {
                                //arrayList.add(i + "");
                                arrayList.add(jsonArray.get(i).toString());
                                System.out.println("in arr: " + jsonArray.get(i).toString());
                            }
                            userModel.setSerialNumber(arrayList);
                            userModel.setOpMessage(object.getString("op_message"));

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    if (status.equals("200")) {
                        JSONObject jObj = json_response.getJSONObject("data");
                        alertMessage = jObj.getString("op_message");
                        defaultOneButtonDialog(ExchangeSchameActivity.this, alertMessage);
                    } else {
                        JSONObject jObj = json_response.getJSONObject("data");
                        alertMessage = jObj.getString("op_message");
                        GlobalVariables.defaultOneButtonDialog(ExchangeSchameActivity.this, alertMessage);
                    }
                }
            } catch (Exception e) {
            }

        }

    }

    public void showDialogExitFromApp() {
        AlertDialog.Builder builder = new AlertDialog.Builder(
                ExchangeSchameActivity.this);
        builder.setTitle("SheelaFoam");
        builder.setMessage("Are you sure want to exit from app");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.show();
    }

    public void getSppanable() {
        textview_app_title.setText("GCard Registration");

        // disable on 23June2016.
    /*SpannableString spannable_string=new SpannableString(textview_app_title.getText().toString().trim());
    spannable_string.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.color_great)), 0, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
	spannable_string.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.color_plus)), 5, 10, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

	textview_app_title.setText(spannable_string);*/
    }

    /***********************One button alert dialog****************************/

    private void defaultOneButtonDialog(Activity activity, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do things
                        resetInputControls();
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    /*************Two button alert dialog***************/

    private void TwoButtonAlertDialog(Activity activity, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do things


                        if ((serialNoOne != null && serialNoOne.length() > 0) || (serialNoTwo != null && serialNoTwo.length() > 0) || (customerMobileNo != null && customerMobileNo.length() > 0)) {
                            if (customerMobileNo.length() == 10) {
                                if (!serialNoOne.equals(serialNoTwo)) {
                                    sendSMS(receiverMobleNo, isChecked_exchangeSchame);
                                } else {

                                    GlobalVariables.defaultOneButtonDialog(ExchangeSchameActivity.this, "Serial no one and serial no two can't be same.");
                                }


                            } else {

                                GlobalVariables.defaultOneButtonDialog(ExchangeSchameActivity.this, "Please enter valid mobile number");
                            }

                        } else {
                            GlobalVariables.defaultOneButtonDialog(ExchangeSchameActivity.this, "Some input data is required");
                        }


                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // TODO Auto-generated method stub
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    /***************Method: User for update Input Controls.***********************/
    private void resetInputControls() {
        edittext_serialNoOne.setText("");
        edittext_serialNoTwo.setText("");
        edittext_customerNo.setText("");
        invoiceDateTv.setText("Invoice Date");
        invoiceNumber.setText("");
        etEmail.setText("");
        etBundleNo.setText("");
        etName.setText("");
        //checkbox_schame.setChecked(false);
    }

    /**************Method: This method is used for send SMS********************************/
    public void sendSMS(String phoneNo, boolean isCheckedExchangeScheme) {
        try {

            if (isCheckedExchangeScheme) {
                requestMSG = serialNoOne + "," + serialNoTwo + "," + customerMobileNo;
            } else {
                requestMSG = "NE" + "," + serialNoOne + "," + serialNoTwo + "," + customerMobileNo;
            }

            Log.e("Request sms", requestMSG);
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, requestMSG, null, null);
            Toast.makeText(getApplicationContext(), "Message Sent",
                    Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), ex.getMessage().toString(),
                    Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    private void logout(Activity activity) {


        SharedPreferences prefs = activity.getSharedPreferences("Location",
                Context.MODE_PRIVATE);
        Editor editor = prefs.edit();

        editor.clear();
        editor.commit();

        Intent intent = new Intent(activity, SplashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        activity.startActivity(intent);
    }

    /**
     * Method: Method for call scan intent.
     **/

    public void callScanIntent(int requestCode) {
        //Intent intent = new Intent("com.google.zxing.client.android.SCAN");
        // intent.setPackage("com.google.zxing.client.android");
        Intent intent = new Intent(ExchangeSchameActivity.this, CaptureActivity.class);
        //intent.setPackage("com.google.zxing.client.android");
        intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
        startActivityForResult(intent, requestCode);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {


        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                serialNoOne = intent.getStringExtra("SCAN_RESULT");
                edittext_serialNoOne.setText(serialNoOne);
            } else {
                serialNoTwo = intent.getStringExtra("SCAN_RESULT");
                edittext_serialNoTwo.setText(serialNoTwo);
            }
        }
        // Handle successful scan

    }

    public void openCustomeDialog(Context context) {
        dialog1 = new CustomDialog(context, R.layout.dialog_bundle_list);
        dialog1.setCancelable(true);
        CustomTypefaceTextView tv_subTile = (CustomTypefaceTextView) dialog1.findViewById(R.id.tv_subTile);
        tv_subTile.setText("Please select serial no!");
        ListView lv = (ListView) dialog1.findViewById(R.id.recycler_bundle);

        final List<SerialNumModel> sNoList = new ArrayList<>();
//        UserModel uModel = new UserModel();
        ArrayList<String> arrayListS = new ArrayList();
        arrayListS = userModel.getSerialNumber();
        Log.d("arrayListS in Dialoge", arrayListS.size() + "");
        for (String sNo : arrayListS) {
            if (!TextUtils.isEmpty(sNo) && !sNo.equalsIgnoreCase("null")) {
                SerialNumModel model = new SerialNumModel();
                model.setsNo(sNo);
                sNoList.add(model);
            }
        }
        dialog1.show();
        lv.setAdapter(new DialogBundallAdapter(ExchangeSchameActivity.this, sNoList));
        final ArrayList<String> SNoArr = new ArrayList<>();

        final Button btn_bundle_submit = (Button) dialog1.findViewById(R.id.btn_bundle_submit);
        btn_bundle_submit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                for (SerialNumModel m : sNoList) {
                    if (m.isChecked()) {
                        SNoArr.add(m.getsNo());
                    }
                }
                if (SNoArr.size() < 1) {
                    Toast.makeText(ExchangeSchameActivity.this, "Please select minimum one or max two serial numbers!", Toast.LENGTH_SHORT).show();

                } else if (SNoArr.size() >= 2) {
                    edittext_serialNoOne.setText(SNoArr.get(0));
                    edittext_serialNoTwo.setText(SNoArr.get(1));
                    dialog1.dismiss();
                } else {
                    edittext_serialNoOne.setText(SNoArr.get(0));
                    dialog1.dismiss();
                }

            }
        });
    }

    private void callSalesRepresentativeService(String userId) throws JSONException {
        RequestQueue requestQueue1 = Volley.newRequestQueue(ExchangeSchameActivity.this);
        JSONObject _req = new JSONObject();
        _req.put("p_user_id", userId);
        JsonArrayRequest _arrayReq = new JsonArrayRequest(Request.Method.POST, URLS.SALES_REP, _req,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        Log.d("Sales response is", jsonArray + "");
                        Type listType = new TypeToken<List<RepresentativeName>>() {
                        }.getType();
                        List<RepresentativeName> list = new Gson().fromJson(jsonArray.toString(), listType);
                        autocomplete_representativeName.setAdapter(new SalesRepoNameAdapter(ExchangeSchameActivity.this, list));
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };
        requestQueue1.add(_arrayReq);
    }

    private void callBundleService() {
        RequestQueue requestQueue = Volley.newRequestQueue(ExchangeSchameActivity.this);
        String url = "http://greatplus.com/warranty_log_api/bundle_user.php";
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("p_user_id", loginPref.getString(Constant.Sp_UserID, ""));
        params.put("p_bundle_number", etBundleNo.getText().toString().trim());
        JsonObjectRequest req = new JsonObjectRequest(url, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Anand response is", response + "");
                        dialog.dismiss();
                        try {
                            if (response.getString("status").equals("true")) {

                                JSONObject object = new JSONObject(response.getString("user"));
                                userModel = new UserModel();
                                userModel.setPUserId(object.getString("p_user_id"));
                                userModel.setPBundleNumber(object.getString("p_bundle_number"));
                                JSONArray jsonArray = new JSONArray(object.getString("serial_number"));
                                ArrayList<String> arrayListS = new ArrayList();
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    if (jsonArray.get(i).toString().equalsIgnoreCase("null")) {

                                    } else {
                                        arrayListS.add(jsonArray.get(i).toString());
                                        System.out.println("in arr: " + jsonArray.get(i).toString());
                                    }
                                }
                                userModel.setSerialNumber(arrayListS);
                                userModel.setOpMessage(object.getString("op_message"));
                                Log.d("arrayListS Size <><> ", arrayListS.size() + "");
                                ArrayList ar = userModel.getSerialNumber();
                                Log.d("a Size <><> ", ar.size() + "");
                                if (arrayListS.size() == 0 && !object.getString("op_message").equalsIgnoreCase("null")) {
                                    resultDialog(object.getString("op_message"), true);
                                } else {
                                    openCustomeDialog(ExchangeSchameActivity.this);
                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
                dialog.dismiss();
            }
        });
        requestQueue.add(req);
        //------------------------------------------------------------------------
    }

    public void resultDialog(String msg, boolean isCancel) {
        final Dialog dialog = new Dialog(ExchangeSchameActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(isCancel);
        dialog.setContentView(R.layout.popup);
        TextView tv_msg = (TextView) dialog.findViewById(R.id.tv_msg);
        tv_msg.setText(msg);
        TextView tv_ok = (TextView) dialog.findViewById(R.id.tv_ok);
        tv_ok.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void callPreOrderService() {
        {
            String delearID = Util.getSharedPrefrenceValue(ExchangeSchameActivity.this, Constant.Sp_emp_grpCode);
            String UserID = Util.getSharedPrefrenceValue(ExchangeSchameActivity.this, Constant.Sp_UserID);
            Map<String, String> jsonParams = new HashMap<String, String>();
            jsonParams.put("p_user_id", UserID);//---9219151562
            jsonParams.put("p_dealer_id", delearID);
            jsonParams.put("p_customer_mobile", edittext_customerNo.getText().toString());
            JsonObjectRequest myRequest = new JsonObjectRequest(
                    Request.Method.POST,
                    "https://greatplus.com/api_services/fetch_order_api.php",
                    new JSONObject(jsonParams),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            buttonClick = true;
                            progress.dismiss();
                            Log.d("searchResult", response.toString());
                            try {
                                String status = response.getString("status");
                                if (status.equalsIgnoreCase("200")) {
                                    Object object;
                                    JSONArray interventionJsonArray;
                                    JSONObject interventionObject;
                                    object = response.get("Data");
                                    if (object instanceof JSONArray) {
                                        interventionJsonArray = (JSONArray) object;
                                        String PRODUCT_DISPLAY_NAME = interventionJsonArray.getJSONObject(0).getString("PRODUCT_DISPLAY_NAME");
                                        String QUANTITY = interventionJsonArray.getJSONObject(0).getString("QUANTITY");
                                        String CASH_REWARD = interventionJsonArray.getJSONObject(0).getString("CASH_REWARD");
                                        int Reward = 0;
                                        if (CASH_REWARD.length() > 0) {
                                            Reward = Integer.parseInt(CASH_REWARD);
                                        }
                                        String PRODUCT_MRP = interventionJsonArray.getJSONObject(0).getString("PRODUCT_MRP");
                                        int PRODUCT_MRP1 = 0;
                                        if (PRODUCT_MRP.length() > 0) {
                                            PRODUCT_MRP1 = Integer.parseInt(PRODUCT_MRP);
                                        }
                                        String LENGTH = interventionJsonArray.getJSONObject(0).getString("LENGTH");
                                        String BREDTH = interventionJsonArray.getJSONObject(0).getString("BREDTH");
                                        String THICK = interventionJsonArray.getJSONObject(0).getString("THICK");
                                        String ADVANCE_AMT = interventionJsonArray.getJSONObject(0).getString("ADVANCE_AMT");
                                        String CUSTOMER_NAME = interventionJsonArray.getJSONObject(0).getString("CUSTOMER_NAME");
                                        String CUSTOMER_MOBILE = interventionJsonArray.getJSONObject(0).getString("CUSTOMER_MOBILE");
                                        System.out.println("Anand Name" + CUSTOMER_NAME);
                                        String ToBePaid = Integer.toString(PRODUCT_MRP1 - Reward);
                                        searchPreOrderDialog(ToBePaid, PRODUCT_DISPLAY_NAME, QUANTITY, CASH_REWARD, PRODUCT_MRP, LENGTH, BREDTH, THICK, ADVANCE_AMT, CUSTOMER_NAME, CUSTOMER_MOBILE);
                                    }
                                    if (object instanceof JSONObject) {
                                        interventionObject = (JSONObject) object;
                                        if (interventionObject != null) {
                                            Toast.makeText(ExchangeSchameActivity.this, interventionObject.getString("Message"), Toast.LENGTH_LONG).show();
                                        }

                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                progress.dismiss();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progress.dismiss();
                            Toast.makeText(ExchangeSchameActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                        }
                    }) {

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json; charset=utf-8");
                    return headers;
                }
            };
            RequestQueue MyRequestQueue = Volley.newRequestQueue(ExchangeSchameActivity.this);
            MyRequestQueue.add(myRequest);

        }
    }

    private boolean isValid() {
        if (customerMobileNo.isEmpty()) {
            GlobalVariables.defaultOneButtonDialog(ExchangeSchameActivity.this, "Please enter mobile number ! ");
            return false;
        } else if (customerMobileNo.length() != 10) {
            GlobalVariables.defaultOneButtonDialog(ExchangeSchameActivity.this, "Please enter valid mobile number ! ");
            return false;
        } else if (etName.getText().toString().isEmpty()) {
            GlobalVariables.defaultOneButtonDialog(ExchangeSchameActivity.this, "Please enter customer name ! ");
            return false;
        } else if (serialNoOne.isEmpty() || serialNoOne.length() != 8) {
            GlobalVariables.defaultOneButtonDialog(ExchangeSchameActivity.this, "Please enter valid serial no.");
            return false;
        }
        return true;
    }

    private void callPreOrderServiceSEND() {
        {

            String delearID = Util.getSharedPrefrenceValue(ExchangeSchameActivity.this, Constant.Sp_emp_grpCode);
            String UserID = Util.getSharedPrefrenceValue(ExchangeSchameActivity.this, Constant.Sp_UserID);
            Map<String, String> jsonParams = new HashMap<String, String>();
            jsonParams.put("p_user_id", UserID);//---9219151562
            jsonParams.put("p_dealer_id", delearID);
            jsonParams.put("p_customer_mobile", edittext_customerNo.getText().toString());
            JsonObjectRequest myRequest = new JsonObjectRequest(
                    Request.Method.POST,
                    "https://greatplus.com/api_services/fetch_order_api.php",
                    new JSONObject(jsonParams),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            buttonClick = true;
                            progress.dismiss();
                            Log.d("searchResult", response.toString());
                            try {
                                String status = response.getString("status");
                                if (status.equalsIgnoreCase("200")) {
                                    Object object;
                                    JSONArray interventionJsonArray;
                                    JSONObject interventionObject;
                                    object = response.get("Data");
                                    if (object instanceof JSONArray) {
                                        interventionJsonArray = (JSONArray) object;
                                        String PRODUCT_DISPLAY_NAME = interventionJsonArray.getJSONObject(0).getString("PRODUCT_DISPLAY_NAME");
                                        String QUANTITY = interventionJsonArray.getJSONObject(0).getString("QUANTITY");
                                        String CASH_REWARD = interventionJsonArray.getJSONObject(0).getString("CASH_REWARD");
                                        int Reward = 0;
                                        if (CASH_REWARD.length() > 0) {
                                            Reward = Integer.parseInt(CASH_REWARD);
                                        }
                                        String PRODUCT_MRP = interventionJsonArray.getJSONObject(0).getString("PRODUCT_MRP");
                                        int PRODUCT_MRP1 = 0;
                                        if (PRODUCT_MRP.length() > 0) {
                                            PRODUCT_MRP1 = Integer.parseInt(PRODUCT_MRP);
                                        }
                                        String LENGTH = interventionJsonArray.getJSONObject(0).getString("LENGTH");
                                        String BREDTH = interventionJsonArray.getJSONObject(0).getString("BREDTH");
                                        String THICK = interventionJsonArray.getJSONObject(0).getString("THICK");
                                        String ADVANCE_AMT = interventionJsonArray.getJSONObject(0).getString("ADVANCE_AMT");
                                        String CUSTOMER_NAME = interventionJsonArray.getJSONObject(0).getString("CUSTOMER_NAME");
                                        String CUSTOMER_MOBILE = interventionJsonArray.getJSONObject(0).getString("CUSTOMER_MOBILE");
                                        System.out.println("Anand Name" + CUSTOMER_NAME);
                                        String ToBePaid = Integer.toString(PRODUCT_MRP1 - Reward);
                                        searchPreOrderDialog(ToBePaid, PRODUCT_DISPLAY_NAME, QUANTITY, CASH_REWARD, PRODUCT_MRP, LENGTH, BREDTH, THICK, ADVANCE_AMT, CUSTOMER_NAME, CUSTOMER_MOBILE);
                                    }
                                    if (object instanceof JSONObject) {
                                        interventionObject = (JSONObject) object;
                                        if (interventionObject != null)
                                            GlobalVariables.hideSoftKeyboard(ExchangeSchameActivity.this);
                                        getInput();
                                        if (con.isConnectingToInternet()) {
                                            if (isValid()) {
                                                submitExchangeSchameDataRequest();
                                            }
                                        } else {
                                            TwoButtonAlertDialog(ExchangeSchameActivity.this, GlobalVariables.CONNECTION_ERROR + " " + "You want to send sms");
                                        }
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                progress.dismiss();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progress.dismiss();
                            Toast.makeText(ExchangeSchameActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                        }
                    }) {

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json; charset=utf-8");
                    return headers;
                }
            };
            RequestQueue MyRequestQueue = Volley.newRequestQueue(ExchangeSchameActivity.this);
            MyRequestQueue.add(myRequest);

        }
    }

    private void searchPreOrderDialog(String ToBePaid, String PRODUCT_DISPLAY_NAME, String QUANTITY, String CASH_REWARD, String PRODUCT_MRP, String LENGTH, String BREDTH, String THICK, String ADVANCE_AMT, final String CUSTOMER_NAME, String CUSTOMER_MOBILE) {
        {
            if (cd != null && cd.isShowing()) {
                cd.dismiss();
            }
            cd = new CustomDialog(ExchangeSchameActivity.this, R.layout.search_pre_order_dialog);
            cd.setCancelable(false);
            cd.getWindow().setBackgroundDrawable(new ColorDrawable(TRANSPARENT));
            TextView dialog_title = (TextView) cd.findViewById(R.id.dialog_title);
            dialog_title.setText("The Customer having mobile no. " + "" + CUSTOMER_MOBILE + "" + " has already order with below information.");
            //TextView txt_address_value = (TextView) cd.findViewById(R.id.tv_massageDialog);
            final CustomTypefaceTextView tv_productPreOrderName = (CustomTypefaceTextView) cd.findViewById(R.id.tv_productPreOrderName);
            tv_productPreOrderName.setText(PRODUCT_DISPLAY_NAME);
            CustomTypefaceTextView tv_productPreOrderQTY = (CustomTypefaceTextView) cd.findViewById(R.id.tv_productPreOrderQTY);
            tv_productPreOrderQTY.setText(QUANTITY);
            CustomTypefaceTextView tv_productPreOrderSize = (CustomTypefaceTextView) cd.findViewById(R.id.tv_productPreOrderSize);
            tv_productPreOrderSize.setText(LENGTH + " (L)" + BREDTH + " (B)" + THICK + "(T)");
            CustomTypefaceTextView tv_productPreOrderMrp = (CustomTypefaceTextView) cd.findViewById(R.id.tv_productPreOrderMrp);
            tv_productPreOrderMrp.setText(PRODUCT_MRP);
            CustomTypefaceTextView tv_productPreOrderCashReward = (CustomTypefaceTextView) cd.findViewById(R.id.tv_productPreOrderCashReward);
            tv_productPreOrderCashReward.setText(CASH_REWARD);
          /*  CustomTypefaceTextView tv_productPreOrderAdavance= (CustomTypefaceTextView) cd.findViewById(R.id.tv_productPreOrderAdavance);
            tv_productPreOrderAdavance.setText(ADVANCE_AMT);*/
            CustomTypefaceTextView tv_productPreOrderPaid = (CustomTypefaceTextView) cd.findViewById(R.id.tv_productPreOrderPaid);
            tv_productPreOrderPaid.setText(ToBePaid);
            TextView btn_okDialog = (TextView) cd.findViewById(R.id.btn_okDialog);
            TextView btn_canceDilog = (TextView) cd.findViewById(R.id.btn_canceDilog);
            btn_okDialog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    etName.setText(CUSTOMER_NAME);
                    edittext_serialNoOne.setText("");
                    edittext_serialNoTwo.setText("");
                    invoiceDateTv.setText("Invoice Date");
                    invoiceNumber.setText("");
                    etEmail.setText("");
                    etBundleNo.setText("");
                    autocomplete_representativeName.setText("");
                    cd.dismiss();

                }
            });
            cd.show();
        }
    }

    class SalesRepoNameAdapter extends BaseAdapter implements Filterable {
        private List<RepresentativeName> item;
        private List<RepresentativeName> nameList;
        private LayoutInflater inflater;

        public SalesRepoNameAdapter(Context context, List<RepresentativeName> nameList) {
            this.item = new ArrayList<>();
            this.nameList = nameList;
            inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return item.size();
        }

        @Override
        public RepresentativeName getItem(int i) {
            return item.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null)
                view = inflater.inflate(R.layout.item_auto_complete, viewGroup, false);
            TextView tvRepName = (TextView) view.findViewById(R.id.tvRepName);
            tvRepName.setText("" + getItem(i).getSALESREPNAME());
            return view;
        }

        @Override
        public Filter getFilter() {
            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence charSequence) {
                    FilterResults filterResults = new FilterResults();
                    synchronized (charSequence) {
                        if (charSequence != null && charSequence.length() > 1) {
                            List<RepresentativeName> list = getFilterList(charSequence.toString());
                            filterResults.count = list.size();
                            filterResults.values = list;
                        }
                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

                    if (filterResults.values != null) {
                        item = (ArrayList<RepresentativeName>) filterResults.values;
                    } else {
                        item = null;
                    }
                    if (item != null && item.size() > 0) {
                        notifyDataSetChanged();
                    } else {
                        notifyDataSetInvalidated();
                    }

                }
            };
        }

        private List<RepresentativeName> getFilterList(String s) {
            List<RepresentativeName> list = new ArrayList<>();
            for (RepresentativeName name : nameList) {
                String repName = name.getSALESREPNAME().replace("\\s", "");
                if (repName.contains(s.replace("\\s", ""))) {
                    list.add(name);
                }
            }
            return list;
        }

    }
}


