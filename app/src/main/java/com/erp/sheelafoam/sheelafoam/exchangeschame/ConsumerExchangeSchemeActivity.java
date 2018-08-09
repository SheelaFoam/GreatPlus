package com.erp.sheelafoam.sheelafoam.exchangeschame;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
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
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.erp.sheelafoam.HomeScreen;
import com.erp.sheelafoam.R;
import com.erp.sheelafoam.adapter.DialogBundallAdapter;
import com.erp.sheelafoam.adapter.PendingAdapter;
import com.erp.sheelafoam.constant.Constant;
import com.erp.sheelafoam.model.RepresentativeName;
import com.erp.sheelafoam.model.SerialNumModel;
import com.erp.sheelafoam.model.UserModel;
import com.erp.sheelafoam.models.PendingGCOrderModel;
import com.erp.sheelafoam.prefrences.SheelaSharedPreference;
import com.erp.sheelafoam.sheelafoam.activity.SplashActivity;
import com.erp.sheelafoam.sheelafoam.utility.AsyncTaskListner;
import com.erp.sheelafoam.sheelafoam.utility.ConnectionDetector;
import com.erp.sheelafoam.sheelafoam.utility.GlobalVariables;
import com.erp.sheelafoam.sheelafoam.utility.MyAsyncTask;
import com.erp.sheelafoam.utils.CustomDialog;
import com.erp.sheelafoam.utils.CustomDialog2;
import com.erp.sheelafoam.utils.CustomTypefaceTextView;
import com.erp.sheelafoam.utils.URLS;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.journeyapps.barcodescanner.CaptureActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.graphics.Color.TRANSPARENT;

public class ConsumerExchangeSchemeActivity extends AppCompatActivity implements View.OnClickListener, AsyncTaskListner {
    CustomDialog cd2;
    PendingAdapter adapter;
    private static final int CAMERA_CONSTANT = 100;
    public final String receiverMobleNo = "9212273322";
    final String[] gender = new String[1];
    private final int SCAN_REQUEST_CODE_ONE = 1;
    private final int SCAN_REQUEST_CODE_TWO = 2;
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
    boolean dateSelected = false;
    UserModel userModel;
    private RadioGroup radiogroupScheme;
    private RadioButton radioScheme, radioNoScheme, radioReplace;
    private TextView textview_app_title, textview_send;
    private TextView scanSerialNoOneTV, scanSerialNoTwoTV;
    private EditText edittext_serialNoOne, edittext_serialNoTwo, edittext_customerNo, etName;
    private String role = "", mobileNum = "", eocb = "", smile = "", mattress = "", serialNoOne = "", serialNoTwo = "", customerMobileNo = "", customerName = "", customerEmail = "";
    private String alertMessage = "", requestMSG = "", responseSMS = "", scanedQrcodeOne, scanedQrcodeTwo;
    private boolean isChecked_exchangeSchame, isChecked_exchangeSchameNA, isChecked_exchangeSchameRE;
    private SharedPreferences mPrefs;
    private ProgressDialog dialog;
    private SharedPreferences loginPref;
    private int mYear, mMonth, mDay;
    private ImageButton ib_backicon_new;
    private TextView tv_logoname, tv_emptyText;
    private  String OrderNumber="";
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
                            defaultOneButtonDialog(ConsumerExchangeSchemeActivity.this, responseSMS);
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
        setContentView(R.layout.new_guarante_registration_consumer);
        mPrefs = getSharedPreferences("Location", Context.MODE_PRIVATE);
        con = new ConnectionDetector(ConsumerExchangeSchemeActivity.this);
        mContext = ConsumerExchangeSchemeActivity.this;
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
                        //Toast.makeText(ConsumerExchangeSchemeActivity.this, "", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.radioNoScheme:
                        gender[0] = "NA";
                        check = "NA";
                       // Toast.makeText(ConsumerExchangeSchemeActivity.this, "NA", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.radioReplace:
                        gender[0] = "R";
                        check = "R";
                       // Toast.makeText(ConsumerExchangeSchemeActivity.this, "R", Toast.LENGTH_SHORT).show();
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

    public void initialiseUiControls() {
        radiogroupScheme = (RadioGroup) findViewById(R.id.radiogroupScheme);
        radioReplace = (RadioButton) findViewById(R.id.radioReplace);
        radioNoScheme = (RadioButton) findViewById(R.id.radioNoScheme);
        radioScheme = (RadioButton) findViewById(R.id.radioScheme);
        /* textview_app_title = (TextView) findViewById(R.id.app_toolbar_title);*/
        scanSerialNoOneTV = (TextView) findViewById(R.id.SheelaFoamID_ExchangeScheme_ScanSerialNoOneTV);
        scanSerialNoTwoTV = (TextView) findViewById(R.id.SheelaFoamID_ExchangeScheme_ScanSerialNoTwoTV);
        edittext_serialNoOne = (EditText) findViewById(R.id.edittext_serialNoOne);
        edittext_serialNoTwo = (EditText) findViewById(R.id.edittext_serialNoTwo);
        edittext_customerNo = (EditText) findViewById(R.id.edittext_customerNo);
        //llayout_tbl_header = (LinearLayout) findViewById(R.id.llayout_tbl_header);
        textview_send = (TextView) findViewById(R.id.textview_send);
        autocomplete_representativeName = (AutoCompleteTextView) findViewById(R.id.autocomplete_representativeName);
        etName = (EditText) findViewById(R.id.etName);
        ib_backicon_new = (ImageButton) findViewById(R.id.ib_backicon_new);
        tv_logoname = (TextView) findViewById(R.id.tv_logoname_new);
        tv_logoname.setText("Pre Invoice GCard Registration");

       /* toolbar = (Toolbar) findViewById(R.id.app_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSppanable();*/
        Intent intent = getIntent();
        String mobNumber = intent.getStringExtra("MobNumber");
        String consumerName = intent.getStringExtra("consumerName");
        OrderNumber = intent.getStringExtra("OrderNumber");
        edittext_customerNo.setText(mobNumber);
        etName.setText(consumerName);
        System.out.println("M" + mobNumber);
        autocomplete_representativeName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                RepresentativeName representativeName = (RepresentativeName) adapterView.getItemAtPosition(i);
                autocomplete_representativeName.setText(representativeName.getSALESREPNAME());
            }
        });

    }

    public void addOnclickListner() {
        textview_send.setOnClickListener(this);
        scanSerialNoOneTV.setOnClickListener(this);
        scanSerialNoTwoTV.setOnClickListener(this);
        ib_backicon_new.setOnClickListener(this);
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
        customerName = etName.getText().toString().trim();

    }

    private void submitExchangeSchameDataRequest() {
        action = 2;
        try {
            json_request = new JSONObject();
            json_request.put("request", "exchane_offer_submit");
            json_request.put("p_sales_rep_name", autocomplete_representativeName.getText().toString().trim());
            json_request.put("mobile1", mobileNum);
            json_request.put("p_serial_no1", serialNoOne);
            json_request.put("p_serial_no2", serialNoTwo);
            json_request.put("p_customer_mobile", customerMobileNo);
            json_request.put("p_customer_email", "");
            json_request.put("p_customer_name", customerName);
            json_request.put("p_non_eo", check);
            json_request.put("p_invoice_number", "");
            json_request.put("p_invoice_date", "");
            json_request.put("p_dealer_mobile", loginPref.getString(Constant.Sp_User_Mobile, ""));
            json_request.put("p_user_id", loginPref.getString(Constant.Sp_UserID, ""));
            Log.e("#SubmitRequest#", json_request.toString());
            new MyAsyncTask(ConsumerExchangeSchemeActivity.this, json_request).execute();
            dialog = new ProgressDialog(this);
            dialog.setMessage("Loading...");
            dialog.setCancelable(false);
            dialog.show();
        } catch (Exception e) {
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_backicon_new:
                Intent goBack1=new Intent(ConsumerExchangeSchemeActivity.this,ConsumerOredrActivity.class);
                startActivity(goBack1);
                break;
            case R.id.textview_send:
                GlobalVariables.hideSoftKeyboard(ConsumerExchangeSchemeActivity.this);
                getInput();
                if (con.isConnectingToInternet()) {
                    if ((serialNoOne != null && serialNoOne.length() > 0) || (customerMobileNo != null && customerMobileNo.length() > 0)) {
                        if (customerMobileNo.length() == 10) {
                            if (!serialNoOne.equals(serialNoTwo)) {
                                submitExchangeSchameDataRequest();
                            } else {

                                GlobalVariables.defaultOneButtonDialog(ConsumerExchangeSchemeActivity.this, "Serial no one and serial no two can't be same.");
                            }
                        } else {
                            GlobalVariables.defaultOneButtonDialog(ConsumerExchangeSchemeActivity.this, "Please enter valid mobile number");
                        }
                    } else {
                        GlobalVariables.defaultOneButtonDialog(ConsumerExchangeSchemeActivity.this, "Some input data is required");
                    }
                } else {
                    TwoButtonAlertDialog(ConsumerExchangeSchemeActivity.this, GlobalVariables.CONNECTION_ERROR + " " + "You want to send sms");
                }
                break;

            case R.id.btn_logout:

                logout(ConsumerExchangeSchemeActivity.this);

                break;

            case R.id.SheelaFoamID_ExchangeScheme_ScanSerialNoOneTV:
                checkCameraPermission();

                //callScanIntent(SCAN_REQUEST_CODE_ONE);

                break;

            case R.id.SheelaFoamID_ExchangeScheme_ScanSerialNoTwoTV:
                callScanIntent(SCAN_REQUEST_CODE_TWO);
                break;
        }

    }

    public void checkCameraPermission() {
        if (ActivityCompat.checkSelfPermission(ConsumerExchangeSchemeActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(ConsumerExchangeSchemeActivity.this, Manifest.permission.CAMERA)) {
                //Show Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(ConsumerExchangeSchemeActivity.this);
                builder.setTitle("Need Camera Permission");
                builder.setMessage("This app needs Camera permission.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(ConsumerExchangeSchemeActivity.this, new String[]{Manifest.permission.CAMERA}, CAMERA_CONSTANT);
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
                ActivityCompat.requestPermissions(ConsumerExchangeSchemeActivity.this, new String[]{Manifest.permission.CAMERA}, CAMERA_CONSTANT);
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
                callScanIntent(SCAN_REQUEST_CODE_ONE);
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(ConsumerExchangeSchemeActivity.this, Manifest.permission.CAMERA)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ConsumerExchangeSchemeActivity.this);
                    builder.setTitle("Need Camera Permission");
                    builder.setMessage("This app needs Camera permission");
                    builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            ActivityCompat.requestPermissions(ConsumerExchangeSchemeActivity.this, new String[]{Manifest.permission.CAMERA}, CAMERA_CONSTANT);


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
        Intent goToHome=new Intent(ConsumerExchangeSchemeActivity.this, HomeScreen.class);
        startActivity(goToHome);
         //finish();

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
                            if(!TextUtils.isEmpty(OrderNumber)){
                                SheelaSharedPreference sheelaSharedPreference=new SheelaSharedPreference(this);
                                ArrayList<PendingGCOrderModel> list=sheelaSharedPreference.getArrayList();
                                PendingGCOrderModel modelDeletion=null;
                                for (PendingGCOrderModel model:list){
                                    if(OrderNumber.equalsIgnoreCase(model.getOrderNumber())){
                                        modelDeletion=model;
                                        break;
                                    }
                                }
                                list.remove(modelDeletion);
                                sheelaSharedPreference.saveArrayList(list);
                            }
                            GlobalVariables.defaultOneButtonDialog(ConsumerExchangeSchemeActivity.this, alertMessage);
                        } else {
                            setSerialNo();
                        }
                    } else {
                        JSONObject jObj = json_response.getJSONObject("data");
                        alertMessage = jObj.getString("op_message");
                        GlobalVariables.defaultOneButtonDialog(ConsumerExchangeSchemeActivity.this, alertMessage);
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
                        defaultOneButtonDialog(ConsumerExchangeSchemeActivity.this, alertMessage);
                    } else {
                        JSONObject jObj = json_response.getJSONObject("data");
                        alertMessage = jObj.getString("op_message");
                        GlobalVariables.defaultOneButtonDialog(ConsumerExchangeSchemeActivity.this, alertMessage);
                    }
                }
            } catch (Exception e) {
            }

        }

    }

    public void showDialogExitFromApp() {
        AlertDialog.Builder builder = new AlertDialog.Builder(
                ConsumerExchangeSchemeActivity.this);
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

                                    GlobalVariables.defaultOneButtonDialog(ConsumerExchangeSchemeActivity.this, "Serial no one and serial no two can't be same.");
                                }


                            } else {

                                GlobalVariables.defaultOneButtonDialog(ConsumerExchangeSchemeActivity.this, "Please enter valid mobile number");
                            }

                        } else {
                            GlobalVariables.defaultOneButtonDialog(ConsumerExchangeSchemeActivity.this, "Some input data is required");
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
       //edittext_customerNo.setText("");
        // invoiceDateTv.setText("Invoice Date");
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
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.commit();
        Intent intent = new Intent(activity, SplashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        activity.startActivity(intent);
    }

    public void callScanIntent(int requestCode) {
        Intent intent = new Intent(ConsumerExchangeSchemeActivity.this, CaptureActivity.class);
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

    }

    public void openCustomeDialog(Context context) {
        dialog1 = new CustomDialog(context, R.layout.dialog_bundle_list);
        dialog1.setCancelable(true);
        CustomTypefaceTextView tv_subTile = (CustomTypefaceTextView) dialog1.findViewById(R.id.tv_subTile);
        tv_subTile.setText("Please select serial no!");
        ListView lv = (ListView) dialog1.findViewById(R.id.recycler_bundle);
        final List<SerialNumModel> sNoList = new ArrayList<>();
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
        lv.setAdapter(new DialogBundallAdapter(ConsumerExchangeSchemeActivity.this, sNoList));
        final ArrayList<String> SNoArr = new ArrayList<>();

        final Button btn_bundle_submit = (Button) dialog1.findViewById(R.id.btn_bundle_submit);
        btn_bundle_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (SerialNumModel m : sNoList) {
                    if (m.isChecked()) {
                        SNoArr.add(m.getsNo());
                    }
                }
                if (SNoArr.size() < 1) {
                    Toast.makeText(ConsumerExchangeSchemeActivity.this, "Please select minimum one or max two serial numbers!", Toast.LENGTH_SHORT).show();

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
        RequestQueue requestQueue1 = Volley.newRequestQueue(ConsumerExchangeSchemeActivity.this);
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
                        autocomplete_representativeName.setAdapter(new ConsumerExchangeSchemeActivity.SalesRepoNameAdapter(ConsumerExchangeSchemeActivity.this, list));
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

    public void resultDialog(String msg, boolean isCancel) {
        final Dialog dialog = new Dialog(ConsumerExchangeSchemeActivity.this);
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

