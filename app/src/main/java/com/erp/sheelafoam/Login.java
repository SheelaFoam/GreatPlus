package com.erp.sheelafoam;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.erp.sheelafoam.asynctask.NetworkTask;
import com.erp.sheelafoam.constant.Constant;
import com.erp.sheelafoam.model.LoginModel;
import com.erp.sheelafoam.sheelafoam.utility.AsyncTaskListner;
import com.erp.sheelafoam.sheelafoam.utility.GlobalVariables;
import com.erp.sheelafoam.sheelafoam.utility.MyAsyncTask;
import com.erp.sheelafoam.utils.AlertDialogManager;
import com.erp.sheelafoam.utils.ConnectionDetector;
import com.erp.sheelafoam.utils.CustomDialog;
import com.erp.sheelafoam.utils.URLS;
import com.erp.sheelafoam.utils.Util;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.graphics.Color.TRANSPARENT;

/**
 * Created by priyanka.sharma on 11/17/2016.
 */

public class Login extends AppCompatActivity implements View.OnClickListener, NetworkTask.Result, AsyncTaskListner {
    private final int RequestPermissionCode = 12;
    private final int RequestCallPermissionCode = 29;
    Button btn_login;
    Toolbar toolbar;
    TextView tvTitle;
    ConnectionDetector cDetector;
    AlertDialogManager alert;
    String userName, reg_id;
    SharedPreferences sharedPreference;
    CoordinatorLayout login_crl;
    EditText email_mobile;
    String login_session = "LOGIN_SESSION";
    int action;
    JSONObject jObject_response;
    private String user_type = "", op_menu_id = "", auth_token = "", op_dealer_category = "", op_user_name = "",
            op_user_type = "", message = "", user_role = "", otp = "";
    private String op_message, user_id = "", op_authorised_yn = "", op_email_id = "", op_dealer_id, op_dealer_name;
    private String os_type, update_status, version_code, version_name, ver_update, date_time, versionNo;
    int versionCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_in);
        new UserLogAPI("LogIn Page", Login.this);
        findViews();
        EnableRuntimeReadSmsPermission();


    }

    private void findViews() {
        cDetector = new ConnectionDetector(Login.this);
        alert = new AlertDialogManager();
        toolbar = (Toolbar) findViewById(R.id.common_toolbar).findViewById(R.id.toolbar);
        tvTitle = (TextView) findViewById(R.id.common_toolbar).findViewById(R.id.toolbar_title);
        email_mobile = (EditText) findViewById(R.id.email_mobile);
        login_crl = (CoordinatorLayout) findViewById(R.id.login_crl);
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_login.setOnClickListener(this);
        tvTitle.setText("GREAT PLUS");
        sharedPreference = getApplicationContext().getSharedPreferences("Location", Context.MODE_PRIVATE);
        reg_id = sharedPreference.getString(Splash.PROPERTY_REG_ID, "");
        if (reg_id.equals("")) {
            reg_id = GlobalVariables.DEVICE_ACCESS_TOKEN;
        }
        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(Login.this.getPackageName(), 0);
            versionNo = pInfo.versionName;
           versionCode= pInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        try {
            callVersionService();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View v) {
        if (validate()) {
            //setJsonRequest();
            callWSLogIN();
        }
    }

    private void callVersionService() throws JSONException {
        RequestQueue queue = Volley.newRequestQueue(Login.this);
        Map<String, String> postParam = new HashMap<String, String>();
        postParam.put("os_type", "A");
        postParam.put("version_code", String.valueOf(versionCode));
        postParam.put("version_name", versionNo);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                URLS.UPDATE_VERSION, new JSONObject(postParam),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("response" + response);
                        try {
                            JSONObject jsonObject = response;
                            if (jsonObject.getString("update_status").equals("true")) {
                                os_type = jsonObject.getString("os_type");
                                version_code = jsonObject.getString("version_code");
                                version_name = jsonObject.getString("version_name");
                                ver_update = jsonObject.getString("ver_update");
                                date_time = jsonObject.getString("date_time");
                                customDialog();
                            } else {

                            }

                        } catch (Exception e) {

                        }
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

        // Adding request to request queue
        queue.add(jsonObjReq);

    }

    private void customDialog() {
        final CustomDialog cd = new CustomDialog(Login.this, R.layout.custome_dialog);
        cd.setCancelable(true);
        cd.getWindow().setBackgroundDrawable(new ColorDrawable(TRANSPARENT));
        TextView dialog_title = (TextView) cd.findViewById(R.id.dialog_title);
        dialog_title.setText("New version available" + " -V " + "(" + version_name + ")");
        TextView txt_address_value = (TextView) cd.findViewById(R.id.txt_address_value);
        txt_address_value.setText(ver_update);
        TextView btn_download = (TextView) cd.findViewById(R.id.btn_download);
        btn_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(android.content.Intent.ACTION_VIEW);
                i.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.erp.sheelafoam&hl=en_IN"));
                startActivity(i);
            }
        });
        cd.show();
    }

    private void callWSLogIN() {
        if (!cDetector.isConnectingToInternet())
            alert.showAlertDialog(Login.this, "Network Connection", "Enable Your Internet Connection", false);
        else {
            try {
                JSONObject obj = new JSONObject();
                obj.put("username", userName);
                obj.put("mode", "step1");
                String url = Constant.WS_URL + Constant.WS_LOGIN;
                Log.d("New Req", obj.toString());
                NetworkTask networkTask = new NetworkTask(Login.this, 1, obj.toString());
                networkTask.setDialogMessage("Loading...");
                networkTask.exposePostExecute(this);
                networkTask.execute(url);

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public void setJsonRequest() {
        action = 1;
        try {
            JSONObject jObject_request = new JSONObject();
            jObject_request.put("request", "userTypeV1");
            jObject_request.put("deviceModel", GlobalVariables.getDeviceModel());
            jObject_request.put("deviceOs", GlobalVariables.getDeviceOs());
            jObject_request.put("accessType", "A");
            jObject_request.put("mobile1", userName);
            jObject_request.put("mobile2", "");
            jObject_request.put("p_device_token", reg_id);
            jObject_request.put("p_app_version", GlobalVariables.APP_VERSION_CODE);
            Log.e("Login Post RequiestOLD ", jObject_request.toString());
            new MyAsyncTask(this, jObject_request).execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onTaskComplete(String result) {
        Log.d("old API Result", result);
        try {
            jObject_response = new JSONObject(result);
            try {
                String status = jObject_response.getString("status");
                if (status.equals("200")) {
                    JSONObject data = jObject_response.getJSONObject("data");
                    String op_authorised_yn = data.getString("op_authorised_yn");
                    if (op_authorised_yn.equals("1")) {
                        otp = data.getString("op_otp");
                        user_role = data.getString("op_role_name");
                        op_message = data.getString("op_message");

                        //redirect(user_role, otp, op_message);

                    } else {
//                        Toast.makeText(getApplicationContext(), data.getString("op_message"),
//                                Toast.LENGTH_SHORT).show();
//                        String msg = data.getString("message");
//                        GlobalVariables.defaultOneButtonDialog(Login.this, msg);
                    }

                } else {

                    JSONObject data = jObject_response.getJSONObject("data");
                    String msg = data.getString("message");
                    GlobalVariables.defaultOneButtonDialog(Login.this, msg);
                }
            } catch (Exception e) {

                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void resultfromNetwork(String object, int id, int arg1, String arg2) {
        Log.d("New ApiResult", object);
        Gson gson = new Gson();
        LoginModel base = gson.fromJson(object, LoginModel.class);
        if (base.getInfo().get(0).getStatus() == 1) {
            Util.showSnackbar(Login.this, login_crl, base.getInfo().get(0).getMsg());
            sharedPreference = getApplicationContext().getSharedPreferences(login_session, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor_shared = sharedPreference.edit();
            editor_shared.putBoolean("hasLoggedIn", true);
            editor_shared.commit();
            Util.setSharedPrefrenceValue(getApplicationContext(), Constant.PREFS_NAME, Constant.Sp_UserArea, base.getInfo().get(0).getArea());
            Util.setSharedPrefrenceValue(getApplicationContext(), Constant.PREFS_NAME, Constant.Sp_USerName, base.getInfo().get(0).getEmail_login());
            Intent activity_password = new Intent(Login.this, Password_OTP.class);
            activity_password.putExtra("op_user_type", user_role);
            activity_password.putExtra("user_name", userName);
            startActivity(activity_password);
            finish();
        } else {
            Util.showSnackbar(Login.this, login_crl, base.getInfo().get(0).getMsg());
        }
    }

    private boolean validate() {
        boolean flag = true;

        userName = email_mobile.getText().toString();

        if (userName.trim().length() == 0) {
            Util.showSnackbar(Login.this, login_crl, "Please enter User Name!!");
            flag = false;
        }

        return flag;
    }

    public void EnableRuntimeReadSmsPermission() {
        if (ActivityCompat.checkSelfPermission(Login.this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(Login.this, Manifest.permission.READ_SMS)) {
                // Printing toast message after enabling runtime permission.
                //Toast.makeText(MainActivity.this,"CAMERA permission allows us to Access CAMERA app", Toast.LENGTH_LONG).show();
            } else {
                ActivityCompat.requestPermissions(Login.this, new String[]{Manifest.permission.READ_SMS}, RequestPermissionCode);
            }
        } else {
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case RequestPermissionCode:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Toast.makeText(RequestListActivity.this,"Permission Granted, Now your application can access CAMERA.", Toast.LENGTH_LONG).show();
                } else {
                }
                break;
        }
    }
}