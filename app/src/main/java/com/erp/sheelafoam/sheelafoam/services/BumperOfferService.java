package com.erp.sheelafoam.sheelafoam.services;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.format.DateFormat;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.erp.sheelafoam.constant.Constant;
import com.erp.sheelafoam.sheelafoam.activity.BumperOfferActivity;
import com.erp.sheelafoam.utils.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class BumperOfferService extends Service {

    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Date d = new Date();
            CharSequence s  = DateFormat.format("dd-mm-yyyy ", d.getTime());
            System.out.println("date"+s);
            getData(null, null);
            handler.postDelayed(runnable, 30 * 60 * 1000);
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handler.postDelayed(runnable, 25 * 1000);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        if(handler!=null){
            handler.removeCallbacks(runnable);
        }
        stopSelf();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(handler!=null){
            handler.removeCallbacks(runnable);
        }
    }

    private void getData(String fromDate, String toDate) {
        java.text.DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        StringBuilder sb = new StringBuilder(dateFormat.format(new Date()));
        System.out.println("TodayDate " +sb);
        String delearID = Util.getSharedPrefrenceValue(getApplicationContext(), Constant.Sp_emp_grpCode);
        String UserID = Util.getSharedPrefrenceValue(getApplicationContext(), Constant.Sp_UserID);
        Map<String, String> jsonParams = new HashMap<String, String>();
        jsonParams.put("p_user_id", UserID);//-------------"9814509216"
        jsonParams.put("p_dealer_id", delearID);//------------"S1DLH230380"
        jsonParams.put("p_from", sb.toString());
        jsonParams.put("p_to", sb.toString());
        JsonObjectRequest myRequest = new JsonObjectRequest(
                Request.Method.POST,
                "https://greatplus.com/api_services/bumper_prize_api.php",
                new JSONObject(jsonParams),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("result==", response.toString());
                        try {
                            String status = response.getString("status");
                            if (status.equalsIgnoreCase("200")) {
                                JSONArray jsonArray = response.getJSONArray("bumper_prize");
                                callIntent(jsonArray.getJSONObject(0));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
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
        RequestQueue MyRequestQueue = Volley.newRequestQueue(BumperOfferService.this);
        MyRequestQueue.add(myRequest);
    }

    private void callIntent(JSONObject _object) {
        Intent intent = new Intent(BumperOfferService.this, BumperOfferActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("BumperObject", _object.toString());
        startActivity(intent);
    }

}
