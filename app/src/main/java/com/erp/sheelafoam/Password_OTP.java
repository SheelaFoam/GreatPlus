package com.erp.sheelafoam;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.erp.sheelafoam.prefrences.SheelaSharedPreference;
import com.google.gson.Gson;
import com.erp.sheelafoam.asynctask.NetworkTask;
import com.erp.sheelafoam.constant.Constant;
import com.erp.sheelafoam.model.PasswordModel;
import com.erp.sheelafoam.sheelafoam.qrcode.ScanQrCodeActivity;
import com.erp.sheelafoam.sheelafoam.utility.AsyncTaskListner;
import com.erp.sheelafoam.sheelafoam.utility.GlobalVariables;
import com.erp.sheelafoam.sheelafoam.utility.MyAsyncTask;
import com.erp.sheelafoam.utils.AlertDialogManager;
import com.erp.sheelafoam.utils.ConnectionDetector;
import com.erp.sheelafoam.utils.Util;

import org.json.JSONObject;

/**
 * Created by priyanka.sharma on 11/22/2016.
 */

public class Password_OTP extends Activity implements NetworkTask.Result, View.OnClickListener, AsyncTaskListner {
    String Area, strPassword_OTP, USerNAme, user_role = "", reg_id;
    EditText ed_password, ed_otp;
    SharedPreferences otp_prefernce;
    CoordinatorLayout password_crl;
    Button btn_submit;
    ConnectionDetector cDetector;
    AlertDialogManager alert;
    String otp_Session = "OTP_SESSION";
    Toolbar toolbar;
    TextView tvTitle, txt_otp;

    //For Old App login integration

    JSONObject jObject_response;
    String old_user_role, old_user_name, newUserRole;
    String op_authorised_yn = "", op_message = "", op_user_name = "", op_user_type = "", op_menu_id = "", op_dealer_id = "", op_dealer_name = "";
    String op_photo = "", op_email_id = "", op_dealer_category = "", auth_token;
    private SharedPreferences mPrefs;
    private SheelaSharedPreference preference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.password_activity);
        preference=new SheelaSharedPreference(this);
        new UserLogAPI("Password Page", Password_OTP.this);
        init();
    }

    private void init() {

        cDetector = new ConnectionDetector(Password_OTP.this);
        alert = new AlertDialogManager();
        getSharedPreferenceValue();
        old_user_role = getIntent().getStringExtra("op_user_type");
        mPrefs = getSharedPreferences("Location", Context.MODE_PRIVATE);
        otp_prefernce = getSharedPreferences("Location", Context.MODE_PRIVATE);
        reg_id = otp_prefernce.getString(Splash.PROPERTY_REG_ID, "");
        if (reg_id.equals("")) {
            reg_id = GlobalVariables.DEVICE_ACCESS_TOKEN;
        }
        ed_password = (EditText) findViewById(R.id.ed_password);
        password_crl = (CoordinatorLayout) findViewById(R.id.password_crl);
        btn_submit = (Button) findViewById(R.id.btn_submit);
        toolbar = (Toolbar) findViewById(R.id.common_toolbar).findViewById(R.id.toolbar);
        tvTitle = (TextView) findViewById(R.id.common_toolbar).findViewById(R.id.toolbar_title);
        txt_otp = (TextView) findViewById(R.id.txt_otp);

        if (Area.equals("OTP")) {
            ed_password.setHint("ENTER OTP");
            btn_submit.setText("VERIFY OTP");
            txt_otp.setVisibility(View.VISIBLE);
        } else {
            ed_password.setHint("Enter Password");
            btn_submit.setText("Login");
            txt_otp.setVisibility(View.GONE);
        }

        btn_submit.setOnClickListener(this);
        tvTitle.setText("GREAT PLUS");
    }

    private void getSharedPreferenceValue() {
        Area = Util.getSharedPrefrenceValue(Password_OTP.this, Constant.Sp_UserArea);
        USerNAme = Util.getSharedPrefrenceValue(Password_OTP.this, Constant.Sp_USerName);
        user_role = Util.getSharedPrefrenceValue(this, Constant.Sp_RoleName);
        //reg_id = otp_prefernce.getString("DEVICE_ID", "");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_submit:
                if (validate()) {
                    callWSPassword();
                    //setJsonRequest();
                }
                break;
        }
    }

    public void setJsonRequest() {
        Log.e("Here", "I am");
        //action=1;
        try {
            JSONObject jObject_request = new JSONObject();
            jObject_request.put("request", "userAuthenticationV1");
            jObject_request.put("userId", USerNAme);
            // disable by Vinay on 07-08-2016
            //jObject_request.put("password",password)
            jObject_request.put("password", strPassword_OTP);
            jObject_request.put("deviceModel", GlobalVariables.getDeviceModel());
            jObject_request.put("deviceOs", GlobalVariables.getDeviceOs());
            jObject_request.put("accessType", "A");
            // disable by Vinay on 07-08-2016
			/*jObject_request.put("mobile1",GlobalVariables.getPhoneNumber_one(this));
            jObject_request.put("mobile2","");*/
            //TODO: change security
            jObject_request.put("p_mobile_no_1", "");
            jObject_request.put("p_mobile_no_2", "");
            jObject_request.put("p_role_name", newUserRole);
            jObject_request.put("p_device_token", reg_id);
            jObject_request.put("p_app_version", GlobalVariables.APP_VERSION_CODE);
            Log.e("Password PUT Request1", jObject_request.toString());
            new MyAsyncTask(this, jObject_request).execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callWSPassword() {
        if (!cDetector.isConnectingToInternet())
            alert.showAlertDialog(Password_OTP.this, "Network Connection", "Enable Your Internet Connection", false);
        else {
            try {
                JSONObject obj = new JSONObject();
                obj.put("username", USerNAme);
                obj.put("mode", "step2");
                obj.put("password", strPassword_OTP);
                obj.put("area", Area);
                Log.d("Test Post", obj.toString());
                String url = Constant.WS_URL + Constant.WS_LOGIN;
                NetworkTask networkTask = new NetworkTask(Password_OTP.this, 1, obj.toString());
                networkTask.setDialogMessage("Loading...");
                networkTask.exposePostExecute(this);
                networkTask.execute(url);

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }


    private boolean validate() {
        boolean flag = true;
        strPassword_OTP = ed_password.getText().toString();
        if (strPassword_OTP.trim().length() == 0) {
            Util.showSnackbar(Password_OTP.this, password_crl, "Please enter Password!!");
            flag = false;
        }
        return flag;
    }

    @Override
    public void onTaskComplete(String result) {
        Log.d("login Result::>", result);
        try {
            jObject_response = new JSONObject(result);
            try {
                String status = jObject_response.getString("status");
                if (status.equals("200")) {
                    JSONObject data = jObject_response.getJSONObject("data");
                    String op_authorised_yn = data.getString("op_authorised_yn");
                    if (op_authorised_yn.equals("1")) {
                        op_message = data.getString("op_message");
                        op_user_name = data.getString("op_user_name");
                        op_user_type = data.getString("op_user_type");
                        op_menu_id = data.getString("op_menu_id");
                        op_dealer_id = data.getString("op_dealer_id");
                        op_dealer_name = data.getString("op_dealer_name");
                        preference.setDelearID(op_dealer_id);
                        auth_token = data.getString("token");
                        preference.setTokenID(auth_token);
                        op_dealer_category = data.getString("op_dealer_category");
                        GlobalVariables.DEALER_CATEGORY = op_dealer_category;
                        GlobalVariables.MASTER_DEALER_CATEGORY = op_dealer_category;
                        Log.e("Auth Token", auth_token + "<------>" + op_dealer_id);
                        Editor editor = mPrefs.edit();
                        editor.putBoolean("isLogin", true);
                        //editor.putString("UserID", userId);
                        editor.putString("AUTH_TOKEN", auth_token);
                        editor.putString("DEALER_CATEGORY", op_dealer_category);
                        editor.putString("DEALER_NAME", op_dealer_name);
                        editor.putString("DEALER_ID", op_dealer_id);
                        editor.commit();
                        setDataInSharePrefrenshed();
                        //setProfileJsonRequest();
//								  Intent i=new Intent(getBaseContext(),HomeActivity.class);
//				                    startActivity(i);
//				                     finish();
                    } else if (data.getString("token") != "" && data.getString("token") != null) {
                        Log.d("Tokensss", data.getString("token"));
                        auth_token = data.getString("token");
                        Editor editor = mPrefs.edit();
                        editor.putBoolean("isLogin", true);
                        editor.putString("AUTH_TOKEN", auth_token);
                        editor.commit();
                    } else {
//                        op_message=data.getString("op_message");
//                        Toast.makeText(this, op_message+"->QBC", Toast.LENGTH_LONG).show();
                    }
                } else {
                     /* JSONObject data= jObject_response.getJSONObject("data");
					  String msg=data.getString("message");*/

                    jObject_response = new JSONObject(result);
                    String msg = jObject_response.getString("msg");
                    //Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
            }
        } catch (Exception e) {
        }
    }

    public void setDataInSharePrefrenshed() {
        Log.e("I'm Here", "----");
        Editor editor = mPrefs.edit();
        editor.putString("op_authorised_yn", op_authorised_yn);
        editor.putString("op_message", op_message);
        editor.putString("op_user_name", op_user_name);
        editor.putString("op_user_type", op_user_type);
        editor.putString("op_menu_id", op_menu_id);
        editor.putString("user_id", USerNAme);
        editor.commit();
    }

    @Override
    public void resultfromNetwork(String object, int id, int arg1, String arg2) {
        Gson gson = new Gson();
        PasswordModel base = gson.fromJson(object, PasswordModel.class);
        if (base.getInfo().get(0).getStatus() == 1) {
            newUserRole = base.getInfo().get(0).getUser_info().get(0).getOp_user_type();
            setJsonRequest();
            Util.setSharedPrefrenceValue(getApplicationContext(), Constant.PREFS_NAME, Constant.Sp_UserToken, base.getInfo().get(0).getUser_info().get(0).getToken());
            Util.setSharedPrefrenceValue(getApplicationContext(), Constant.PREFS_NAME, Constant.Sp_UserID, base.getInfo().get(0).getUser_info().get(0).getUid());
            Util.setSharedPrefrenceValue(getApplicationContext(), Constant.PREFS_NAME, Constant.Sp_DisplayName, base.getInfo().get(0).getUser_info().get(0).getDisplayname());
            Util.setSharedPrefrenceValue(getApplicationContext(), Constant.PREFS_NAME, Constant.Sp_AuthType, base.getInfo().get(0).getUser_info().get(0).getAuth_type());
            Util.setSharedPrefrenceValue(getApplicationContext(), Constant.PREFS_NAME, Constant.Sp_RoleName, base.getInfo().get(0).getUser_info().get(0).getOp_role_name());
            Util.setSharedPrefrenceValue(getApplicationContext(), Constant.PREFS_NAME, Constant.Sp_emp_grpCode, base.getInfo().get(0).getUser_info().get(0).getOp_user_emp_group_code());
            Util.setSharedPrefrenceValue(getApplicationContext(), Constant.PREFS_NAME, Constant.Sp_USerEmail, base.getInfo().get(0).getUser_info().get(0).getOp_user_email_id());
            Util.setSharedPrefrenceValue(getApplicationContext(), Constant.PREFS_NAME, Constant.Sp_GrtPlusUserID, base.getInfo().get(0).getUser_info().get(0).getOp_greatplus_user_id());
            Util.setSharedPrefrenceValue(getApplicationContext(), Constant.PREFS_NAME, Constant.Sp_op_user_role_name, base.getInfo().get(0).getUser_info().get(0).getOp_user_role_name());
            Util.setSharedPrefrenceValue(getApplicationContext(), Constant.PREFS_NAME, Constant.Sp_User_Mobile, base.getInfo().get(0).getUser_info().get(0).getMobile());
            Util.setSharedPrefrenceValue(getApplicationContext(), Constant.PREFS_NAME, Constant.Sp_User_Type, base.getInfo().get(0).getUser_info().get(0).getOp_user_type());
            Util.setSharedPrefrenceValue(getApplicationContext(), Constant.PREFS_NAME, Constant.Sp_DealerID, base.getInfo().get(0).getUser_info().get(0).getOp_dealer_id());
            Util.setSharedPrefrenceValue(getApplicationContext(), Constant.PREFS_NAME, Constant.Sp_pass, strPassword_OTP);
            Util.setSharedPrefrenceValue(getApplicationContext(), Constant.PREFS_NAME, Constant.Op_user_name, base.getInfo().get(0).getUser_info().get(0).getOp_user_name());
            Util.setSharedPrefrenceValue(getApplicationContext(), Constant.PREFS_NAME, Constant.Sp_Dealer_Category, base.getInfo().get(0).getUser_info().get(0).getOp_dealer_category());
            Util.setSharedPrefrenceValue(getApplicationContext(), Constant.PREFS_NAME, Constant.CHECK_ACTIVITY_STATUS, "1");
            if (Area.equals("OTP")) {
                Util.setSharedPrefrenceValue(getApplicationContext(), Constant.PREFS_NAME, Constant.Sp_userProfileImage, base.getInfo().get(0).getUser_info().get(0).getOp_user_profile_image().replaceAll(" ", "%20"));
            }
            otp_prefernce = getApplicationContext().getSharedPreferences(otp_Session, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor_otp = otp_prefernce.edit();
            editor_otp.putString("zone", base.getInfo().get(0).getUser_info().get(0).getOp_user_zone());
            editor_otp.putBoolean("otpsession", true);
            editor_otp.putString("type_user", base.getInfo().get(0).getUser_info().get(0).getOp_user_type());
            editor_otp.commit();
            Log.d("complaint", base.getInfo().get(0).getUser_info().get(0).getOp_user_type());
            if (base.getInfo().get(0).getUser_info().get(0).getOp_role_name().equalsIgnoreCase("SAATHI")) {
                startActivity(new Intent(Password_OTP.this, ScanQrCodeActivity.class));
                finish();
            } else {
                Intent activity_home = new Intent(Password_OTP.this, HomeScreen.class);
                startActivity(activity_home);
                finish();
            }


        } else {
            Util.showSnackbar(Password_OTP.this, password_crl, base.getInfo().get(0).getMsg());
        }


    }
    @Override
    protected void onResume() {

        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter("otp"));
        super.onResume();
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);

        super.onPause();
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase("otp")) {
                final String message = intent.getStringExtra("message");
                ed_password.setText(message.trim());

                Log.d("message", message);
                //Do whatever you want with the code here
            }
        }
    };

}
