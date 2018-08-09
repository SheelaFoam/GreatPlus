package com.erp.sheelafoam.sheelafoam.utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;


public class MyHttpClient {


    InputStream is = null;
    int timeout;
    Context ctx;
    SharedPreferences mPrefs;
    String userId;
    boolean isLogin = false;
    String authentication_token;


    public MyHttpClient(Context ctx) {

        this.ctx = ctx;

        mPrefs = ctx.getSharedPreferences("Location", Context.MODE_PRIVATE);
        userId = mPrefs.getString("UserID", "");
        isLogin = mPrefs.getBoolean("isLogin", false);

    }

    @SuppressWarnings("deprecation")
    public String GetData(JSONObject jobject) {

        timeout = 0;
        // url with header.
        // disable by Vinay on 31-07-2016.
        //final String uri="http://125.19.46.252/sheelafoams/apis.php"; // live app URL.old Url

        final String uri = "http://125.19.46.252/sheelafoams/api_v1.php";  // New Api V1

        Log.e("Request URL", uri);


        String responseString = null;

        // defaultHttpClient

        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(uri);

        // set connection timeout in HttpParam

        HttpParams httpParameters = new BasicHttpParams();
        int timeoutConnection = 5000;
        HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
        httpClient.setParams(httpParameters);

        try {

            setConnectionTimeOutFlag();

            String data = (jobject.toString());

            httpPost.setEntity(new StringEntity(data, "UTF-8"));

        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // Set up the header types needed to properly transfer JSON

        httpPost.setHeader("Content-Type", "application/json");
        httpPost.setHeader("Accept-Encoding", "US-ASCII");
        httpPost.setHeader("Accept-Language", "en-US");

        if (isLogin) {
            // setting headers if user  login

            httpPost.addHeader(GlobalVariables.DEVICE_ID_KEY, GlobalVariables.getDeviceId(ctx));
            Log.e("device id", GlobalVariables.getDeviceId(ctx));
            //httpPost.addHeader(GlobalVariables.IP_ADDRESS_KEY,mPrefs.getString("IP_ADDRESS_VALUE", ""));
            httpPost.addHeader(GlobalVariables.IP_ADDRESS_KEY, "10.216.203.12");
            Log.e("IP_ADDRESS_KEY", mPrefs.getString("IP_ADDRESS_VALUE", ""));

            httpPost.addHeader(GlobalVariables.API_STATIC_KEY, GlobalVariables.API_STATIC_VALUE);
            httpPost.addHeader(GlobalVariables.AUTHENTICATION_TOKEN_KEY, mPrefs.getString("AUTH_TOKEN", ""));
            Log.e("Access Token", mPrefs.getString("AUTH_TOKEN", ""));
        } else {
            // setting headers if user not login

            httpPost.addHeader(GlobalVariables.DEVICE_ID_KEY, GlobalVariables.getDeviceId(ctx));
            Log.e("Device ID ", GlobalVariables.getDeviceId(ctx));
            //httpPost.addHeader(GlobalVariables.IP_ADDRESS_KEY,mPrefs.getString("IP_ADDRESS_VALUE", ""));
            httpPost.addHeader(GlobalVariables.IP_ADDRESS_KEY, "10.216.203.12");

            Log.e("IP Address ", mPrefs.getString("IP_ADDRESS_VALUE", ""));
            httpPost.addHeader(GlobalVariables.API_STATIC_KEY, GlobalVariables.API_STATIC_VALUE);
            Log.e("API Key ", GlobalVariables.API_STATIC_VALUE + "//" + mPrefs.getString("IP_ADDRESS_VALUE", ""));
        }

        HttpResponse response = null;
        try {
            response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();

        } catch (ConnectTimeoutException e) {
            //Here Connection TimeOut excepion
            timeout = 1;


            setConnectionTimeOutFlag();

        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {

            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            responseString = sb.toString();


        } catch (Exception e) {

        }
        return responseString;

    }


    @SuppressWarnings("deprecation")
    public String GetData(JSONObject jobject, String url) {

        timeout = 0;

        // url without header.
        //final String uri="http://125.19.46.252/sheelafoams/api.php";


        // url with header.

        String uri = url;

        //final String uri="http://125.19.46.252/sheelafoams/api_mod.php";

        String responseString = null;

        // defaultHttpClient

        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(uri);


        // set connection timeout in HttpParam

        HttpParams httpParameters = new BasicHttpParams();
        int timeoutConnection = 5000;
        HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
        httpClient.setParams(httpParameters);

        try {

            setConnectionTimeOutFlag();

            String data = (jobject.toString());

            httpPost.setEntity(new StringEntity(data, "UTF-8"));

        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // Set up the header types needed to properly transfer JSON

        httpPost.setHeader("Content-Type", "application/json");
        httpPost.setHeader("Accept-Encoding", "US-ASCII");
        httpPost.setHeader("Accept-Language", "en-US");

        if (isLogin) {
            // setting headers if user  login

            httpPost.addHeader(GlobalVariables.DEVICE_ID_KEY, GlobalVariables.getDeviceId(ctx));

            Log.e("device id", GlobalVariables.getDeviceId(ctx));
            //httpPost.addHeader(GlobalVariables.IP_ADDRESS_KEY,mPrefs.getString("IP_ADDRESS_VALUE", ""));
            httpPost.addHeader(GlobalVariables.IP_ADDRESS_KEY, "10.216.203.12");
            httpPost.addHeader(GlobalVariables.API_STATIC_KEY, GlobalVariables.API_STATIC_VALUE);
            httpPost.addHeader(GlobalVariables.AUTHENTICATION_TOKEN_KEY, mPrefs.getString("AUTH_TOKEN", ""));

            Log.e("Access Token", mPrefs.getString("AUTH_TOKEN", ""));
        } else {
            // setting headers if user not login

            httpPost.addHeader(GlobalVariables.DEVICE_ID_KEY, GlobalVariables.getDeviceId(ctx));
            Log.e("device id", GlobalVariables.getDeviceId(ctx));
            //httpPost.addHeader(GlobalVariables.IP_ADDRESS_KEY,mPrefs.getString("IP_ADDRESS_VALUE", ""));
            httpPost.addHeader(GlobalVariables.IP_ADDRESS_KEY, "10.216.203.12");

            //httpPost.addHeader(GlobalVariables.AUTHENTICATION_TOKEN_KEY,mPrefs.getString("AUTH_TOKEN", ""));
            httpPost.addHeader(GlobalVariables.API_STATIC_KEY, GlobalVariables.API_STATIC_VALUE);
        }

        HttpResponse response = null;
        try {
            response = httpClient.execute(httpPost);


            HttpEntity entity = response.getEntity();
            is = entity.getContent();

        } catch (ConnectTimeoutException e) {
            //Here Connection TimeOut excepion
            timeout = 1;


            setConnectionTimeOutFlag();

        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {

            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            responseString = sb.toString();


        } catch (Exception e) {

        }
        return responseString;

    }


    public void setConnectionTimeOutFlag() {
        Log.e("time out", "time out");
        Editor editor = mPrefs.edit();
        editor.putInt("CONNECTION_TIMEOUT", timeout);

        editor.commit();

    }

}

