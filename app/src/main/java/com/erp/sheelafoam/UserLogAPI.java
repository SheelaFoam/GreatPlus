package com.erp.sheelafoam;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.util.Log;

import com.google.gson.Gson;
import com.erp.sheelafoam.asynctask.NetworkTask;
import com.erp.sheelafoam.constant.Constant;
import com.erp.sheelafoam.model.SubmitPollsModel;
import com.erp.sheelafoam.utils.Util;

import org.json.JSONObject;

/**
 * Created by priyanka.sharma on 12/6/2016.
 */

public class UserLogAPI implements NetworkTask.Result {
    private String UserID, UserEmail, UserToken, UserAuthType;
    String PageTitle, GreatPlusUserID, OP_USER_ROLENAME, ModelName, Device_id, currentVer, CurrentLocationLat_str, CurrentLocationLng_str;
    Context context;

    public UserLogAPI(String pageTitle, Context context) {
        PageTitle = pageTitle;
        this.context = context;
        getSharedPreferenceValues();
        callWSUserLog();
    }

    private void getSharedPreferenceValues() {
        ModelName = android.os.Build.MODEL;
        Device_id = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);

        SharedPreferences preferences = context.getSharedPreferences("LocationLatLng", Activity.MODE_PRIVATE);
        CurrentLocationLat_str = preferences.getString("CurrentLocationLat_str", "");
        CurrentLocationLng_str = preferences.getString("CurrentLocationLng_str", "");
        currentVer = android.os.Build.VERSION.RELEASE;
        Log.e("currentVer Number", "" + currentVer);
        UserID = Util.getSharedPrefrenceValue(context, Constant.Sp_UserID);
        UserEmail = Util.getSharedPrefrenceValue(context, Constant.Sp_USerEmail);
        GreatPlusUserID = Util.getSharedPrefrenceValue(context, Constant.Sp_GrtPlusUserID);
        OP_USER_ROLENAME = Util.getSharedPrefrenceValue(context, Constant.Sp_op_user_role_name);
        UserToken = Util.getSharedPrefrenceValue(context, Constant.Sp_UserToken);
        UserAuthType = Util.getSharedPrefrenceValue(context, Constant.Sp_AuthType);
        Log.e("currentVer Numberanand", "" + OP_USER_ROLENAME);

    }

/*    {"user_email":"roshni.shreshtha@sheelafoam.com","uid":"ROSHNI.SHRESHTHA","action":"log",
            "pagetitle" :"Home Page","device_name" :" ","mobile" :"Android",
            "op_greatplus_user_id":"ROSHNIS","device_id":"","longitude":"","latitude":"","device_os":""}*/

    private void callWSUserLog() {
        try {
            JSONObject obj = new JSONObject();
            obj.put("user_email", UserEmail);
            obj.put("uid", UserID);
            obj.put("token", UserToken);
            obj.put("action", "log");
            obj.put("auth_type", UserAuthType);
            obj.put("pagetitle", PageTitle);
            obj.put("op_user_role_name", OP_USER_ROLENAME);
            obj.put("device_name", ModelName);
            obj.put("mobile", "Android");
            obj.put("op_greatplus_user_id", GreatPlusUserID);
                obj.put("device_id", Device_id);

            if (CurrentLocationLat_str.equals(null) || CurrentLocationLng_str.equals(null)) {
                obj.put("longitude", "0.0");
                obj.put("latitude", "0.0");
            } else {
                obj.put("longitude", CurrentLocationLng_str);
                obj.put("latitude", CurrentLocationLat_str);
            }
            obj.put("device_os", currentVer);
            obj.put("op_user_role_name", OP_USER_ROLENAME);

            String url = Constant.WS_URL + Constant.WS_USER_LOG;
            NetworkTask networkTask = new NetworkTask(context, 1, obj.toString());
            networkTask.setProgressDialog(false);
            networkTask.exposePostExecute(this);
            networkTask.execute(url);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void resultfromNetwork(String object, int id, int arg1, String arg2) {
        Gson gson_ = new Gson();
        SubmitPollsModel base_ = gson_.fromJson(object, SubmitPollsModel.class);
        if (base_.getInfo().get(0).getStatus() == 1) {
        } else {
        }
    }
}
