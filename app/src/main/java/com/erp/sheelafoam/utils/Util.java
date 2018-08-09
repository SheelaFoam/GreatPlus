package com.erp.sheelafoam.utils;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.erp.sheelafoam.R;
import com.erp.sheelafoam.constant.Constant;


public class Util {

    public static void setSharedPrefrenceValue(Object objCurrentClassReference, String strPrefenceFileName,
                                               String strKey, String strValue) {

        SharedPreferences sharedPreferencesForStoringData = ((ContextWrapper) objCurrentClassReference)
                .getSharedPreferences(strPrefenceFileName, 0);
        SharedPreferences.Editor sharedPrefencesEditor = sharedPreferencesForStoringData
                .edit();
        sharedPrefencesEditor.putString(strKey, strValue);
        sharedPrefencesEditor.commit();


    }

    public static void showSnackbar(Context context, CoordinatorLayout coordinatorLayout, String msg) {
        Snackbar snackbar = Snackbar.make(coordinatorLayout, msg, Snackbar.LENGTH_SHORT);
        View sbView = snackbar.getView();
        sbView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(ContextCompat.getColor(context, R.color.white));
        snackbar.show();
    }


    public static String getSharedPrefrenceValue(Object objCurrentClassReference, String key) {

        SharedPreferences settings = ((ContextWrapper) objCurrentClassReference)
                .getSharedPreferences(
                        Constant.PREFS_NAME, Context.MODE_PRIVATE);

        String value = settings.getString(key, null);

        return value;


    }


    public static boolean isInternetConnection(Context context) {
        // TODO Auto-generated method stub

        ConnectivityManager cn = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo nf = cn.getActiveNetworkInfo();
        boolean statusInternet;

        if (nf != null && nf.isConnected() == true) {
            Log.i("Info:", "Network Available.");
            statusInternet = true;
        } else {
            Log.i("Info:", "Network Not Available.");
            statusInternet = false;

        }
        return statusInternet;
    }


    public static String generatePIN() {

        //generate a 4 digit integer 1000 <10000
        int randomPIN = (int) (Math.random() * 9000) + 1000;

        return String.valueOf(randomPIN);
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    public static void rateUs(Context context) {
        Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK |
                Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            context.startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            context.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + context.getPackageName())));
        }
    }
}
