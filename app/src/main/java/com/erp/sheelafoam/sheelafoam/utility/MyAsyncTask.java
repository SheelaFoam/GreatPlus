package com.erp.sheelafoam.sheelafoam.utility;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.erp.sheelafoam.interfaces.AsyncTaskListnerNew;

import org.json.JSONObject;

public class MyAsyncTask extends AsyncTask<String, Void, String> {

    JSONObject jobject;
    JSONObject jsonObject_response;
    SharedPreferences mPrefs;
    String url = "", message = "";
    private Activity activity;
    private ProgressDialog dialog;
    private AsyncTaskListner callback;
    private AsyncTaskListnerNew callbackNew;

    public MyAsyncTask(Activity act, JSONObject jsonObject, String url, String myMethod) {
        this.activity = act;
        this.jobject = jsonObject;
        this.url = url;
    }

//    public MyAsyncTask(AsyncTaskListnerNew<String> callback)
//    {
//       this.callbackNew = callback
//    }
    public MyAsyncTask(Activity act, JSONObject jobject) {
        this.activity = act;
        this.callback = (AsyncTaskListner) act;
        this.jobject = jobject;
        this.url = "";

        mPrefs = act.getSharedPreferences("Location", Context.MODE_PRIVATE);
    }

    // constructor for display appropriate message on loader. This message parameter is used for hold custom message.

    public MyAsyncTask(Activity act, JSONObject jobject, String message) {
        this.activity = act;
        this.callback = (AsyncTaskListner) act;
        this.jobject = jobject;
        this.url = "";
        this.message = message;
        mPrefs = act.getSharedPreferences("Location", Context.MODE_PRIVATE);
    }

    public MyAsyncTask(Activity act, Fragment fragment, JSONObject jobject) {
        this.activity = act;
        this.callback = (AsyncTaskListner) fragment;
        this.jobject = jobject;
        this.url = "";

        mPrefs = act.getSharedPreferences("Location", Context.MODE_PRIVATE);
    }

    public MyAsyncTask(Activity act, Fragment fragment, JSONObject jobject, String url) {
        this.activity = act;
        this.callback = (AsyncTaskListner) fragment;
        this.jobject = jobject;
        this.url = url;
        mPrefs = act.getSharedPreferences("Location", Context.MODE_PRIVATE);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        dialog = new ProgressDialog(activity);
        if (url.length() > 0) {
            dialog.setMessage(message);
             dialog.setMessage("Loading...");
             dialog.setCancelable(false);
             dialog.show();
        } else {
            //dialog.setMessage("Loading...");
        }
    }

    @Override
    protected String doInBackground(String... params) {
        MyHttpClient http = new MyHttpClient(activity);
        if (url.equals("")) {
            return http.GetData(jobject);
        } else {
            return http.GetData(jobject, url);
        }
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if (null != dialog && dialog.isShowing()) {
            dialog.cancel();
        }

        if (result != null && result.length() > 0) {
            System.out.println("My AsyncTask Result2"+result);
            callback.onTaskComplete(result);
        } else {
           // GlobalVariables.defaultOneButtonDialog(activity, GlobalVariables.CONNECTION_ERROR);
        }

    }


}
