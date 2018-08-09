package com.erp.sheelafoam;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import com.erp.sheelafoam.sheelafoam.utility.AsyncTaskListner;
import com.erp.sheelafoam.sheelafoam.utility.MyAsyncTask;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.erp.sheelafoam.constant.Constant;
import com.erp.sheelafoam.erp.sheelafoam.RegisterGCMService;
import com.erp.sheelafoam.interfaces.LocationInterface;
import com.erp.sheelafoam.sheelafoam.utility.GlobalVariables;
import com.erp.sheelafoam.sheelafoam.utility.HelperMethods;
import com.erp.sheelafoam.utils.ForCurrentLocation;
import com.erp.sheelafoam.utils.Util;

import org.json.JSONException;
import org.json.JSONObject;

public class Splash extends AppCompatActivity implements LocationInterface, AsyncTaskListner {
    Context mContext;
    GoogleCloudMessaging gcm;
    private static int SPLASH_TIME_OUT = 2000;
    boolean logIn_session, OTP_session;
    private String LocationLatLng = "LocationLatLng", UserID;
    String user_name = "";
    ForCurrentLocation forCurrentLocation;
    static final String TAG = "GCMDemo";
    private int verCode;
    private String userRoleName = "";
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    String SENDER_ID = "955458836415";
    private CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_new);
        //GlobalVariables.getIPAddress(mContext);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);

            if (checkPlayServices()) {
                gcm = GoogleCloudMessaging.getInstance(this);
                registerGCM();
            }
            forCurrentLocation = new ForCurrentLocation(this);
            forCurrentLocation.onGoogleApiConnection();
            getAppVersionAndVersionCode();
            permissions();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mContext = (Context) getApplicationContext();
                    SharedPreferences preferences = getSharedPreferences("LOGIN_SESSION", Activity.MODE_PRIVATE);
                    logIn_session = preferences.getBoolean("hasLoggedIn", false);
                    SharedPreferences preferences_ = getSharedPreferences("OTP_SESSION", Activity.MODE_PRIVATE);
                    OTP_session = preferences_.getBoolean("otpsession", false);
                    UserID = Util.getSharedPrefrenceValue(Splash.this, Constant.Sp_UserID);
                    user_name = Util.getSharedPrefrenceValue(Splash.this, Constant.Sp_USerName);
                    userRoleName = Util.getSharedPrefrenceValue(Splash.this, Constant.Sp_RoleName);
                    if (logIn_session && OTP_session && UserID != null) {
                        Intent i = new Intent(Splash.this, HomeScreen.class);
                        startActivity(i);
                        finish();
                    } else {
                        Intent i = new Intent(Splash.this, Login.class);
                        startActivity(i);
                        finish();
                    }
                }
            }, SPLASH_TIME_OUT);
        }


    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.e(TAG, "This device is not supported.");

            }
            return false;
        }
        return true;
    }

    private void registerGCM() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                try {
                    // call register and save registration ID to preference.
                    String regId = gcm.register(SENDER_ID);

                    Log.e("regId New", regId);

                    storeRegistrationId(mContext, regId);

                    return null;
                } catch (Exception e) {
                    // Error Handling
                    return null;
                }
            }

            @Override
            protected void onPostExecute(String result) {

                //Toast.makeText(SplashActivity.this, "version api is calling", Toast.LENGTH_LONG).show();
                getAppVersion();

            }

            ;
        }.execute();
    }

    private void storeRegistrationId(Context context, String regId) {
        //final SharedPreferences prefs = getGCMPreferences(context);


        GlobalVariables.DEVICE_ACCESS_TOKEN = regId;
        final SharedPreferences prefs = getSharedPreferences("Location", Context.MODE_PRIVATE);
        int appVersion = HelperMethods.getAppVersion(context);
        Log.e(TAG, "Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.commit();
        Intent intent = new Intent(getApplicationContext(), RegisterGCMService.class);
        startService(intent);
    }

    private void getAppVersion() {

        try {

            JSONObject jObject_request = new JSONObject();
            jObject_request.put("request", "checkAppVersion");
            jObject_request.put("p_app_version", verCode);
            jObject_request.put("deviceModel", GlobalVariables.getDeviceModel());
            jObject_request.put("deviceOs", GlobalVariables.getDeviceOs());
            jObject_request.put("accessType", "A");
            jObject_request.put("mobile1", "");
            jObject_request.put("mobile2", "");

            jObject_request.put("p_device_token", GlobalVariables.DEVICE_ACCESS_TOKEN);

            Log.e("##Request11##", jObject_request.toString());

            new MyAsyncTask(Splash.this, jObject_request).execute();


        } catch (Exception e) {

        }

    }

    private void getAppVersionAndVersionCode() {

        PackageInfo pInfo = null;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String version = pInfo.versionName;
        verCode = pInfo.versionCode;
        GlobalVariables.APP_VERSION_CODE = verCode;

        Log.e("version code", "" + verCode);
    }


    @Override
    public void getCurrentLatLong(double latitude, double longitude) {
        Log.e("LocationLatitude", "" + latitude);
        Log.e("LocationLongitude", "" + longitude);

        double CurrentLocationLat_dbl = latitude;
        double CurrentLocationLng_dbl = longitude;

        String CurrentLocationLat_str = String.valueOf(latitude);
        String CurrentLocationLng_str = String.valueOf(longitude);

        Log.e("LocationLatitudeString", "" + CurrentLocationLat_str);
        Log.e("LocationLongitudeString", "" + CurrentLocationLng_str);

        SharedPreferences radius_value = getApplicationContext().getSharedPreferences(LocationLatLng, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor_shared = radius_value.edit();
        editor_shared.putString("CurrentLocationLat_str", CurrentLocationLat_str);
        editor_shared.putString("CurrentLocationLng_str", CurrentLocationLng_str);
        editor_shared.commit();

    }

    private void permissions() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(getApplicationContext(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                } else {

                    ActivityCompat.requestPermissions(this,
                            new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                }
            }
        }

    }


    @Override
    public void onTaskComplete(String result) {
        SharedPreferences sharedPreferences = getSharedPreferences(Constant.PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (result != null) {
            try {
                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.getString("status").equals("200")) {
                    JSONObject data = jsonObject.getJSONObject("data");
                    if (data.getString("op_update_yn").equals("1")) {
                        editor.putBoolean("op_update_yn", true);
                        editor.commit();
                    } else {
                        editor.putBoolean("op_update_yn", false);
                        editor.commit();
                    }
                }
            } catch (JSONException js) {
                js.printStackTrace();
            }
        }
    }

    private boolean isConnection() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "No internet connection!", Snackbar.LENGTH_LONG)
                    .setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                        }
                    });
// Changing message text color
            snackbar.setActionTextColor(Color.RED);
// Changing action button text color
            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.YELLOW);
            snackbar.show();
            return true;
        } else {
            return false;
        }

    }

}


