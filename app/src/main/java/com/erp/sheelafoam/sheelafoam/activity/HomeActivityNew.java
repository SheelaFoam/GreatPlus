/**
 * Short description for file: Used for show dashboard data
 * <p>
 * <p>
 * Used Android  minSdkVersion 11
 * Used Android  targetSdkVersion 19
 *
 * @package com.sheela.employeeportal.erp.sheelafoam
 * @author Vinay Kumar Gupta
 * Class name  HomeActivity
 * Short description for class:  class used for show dashboard data
 * Creation Date  02-02-2015
 * Last Modified  02-02-2015
 * Modified By    Vinay Kumar Gupta
 */

package com.erp.sheelafoam.sheelafoam.activity;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentManager.OnBackStackChangedListener;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.erp.sheelafoam.R;
import com.erp.sheelafoam.sheelafoam.categories.CategoryFragment;
import com.erp.sheelafoam.sheelafoam.utility.AppConstant;
import com.erp.sheelafoam.sheelafoam.utility.AsyncTaskListner;
import com.erp.sheelafoam.sheelafoam.utility.ConnectionDetector;
import com.erp.sheelafoam.sheelafoam.utility.GlobalVariables;
import com.erp.sheelafoam.sheelafoam.utility.MyAsyncTask;
import com.erp.sheelafoam.sheelafoam.utility.MyCustomAsyncTask;
import com.erp.sheelafoam.sheelafoam.utility.SharePref;
import com.erp.sheelafoam.sheelafoam.xperia.function.XperiaFunctions;
import com.splunk.mint.Mint;

import org.json.JSONException;
import org.json.JSONObject;

import javax.sql.DataSource;

public class HomeActivityNew extends ActionBarActivity implements OnClickListener, AsyncTaskListner {


    private static final int CROP_FROM_CAMERA = 4;
    private static int LOAD_IMAGE_RESULTS = 2;
    /*capture image from camera*/
    private static int CAPTURE_IMAGE_RESULTS = 3;
    Context ctx;
    FrameLayout flayout;
    ImageButton btn_logout;
    TextView textview_user_id, textview_back;
    TextView textview_app_title;
    String op_user_name = "", op_email_id = "", op_mobile_number = "", op_landline_number = "", op_address = "";
    String op_city = "", op_state = "", op_pincode = "", op_country = "", op_photo = "", op_user_type = "", op_message = "";
    String op_user_dob = "", op_territory = "", op_start_off_timing = "", op_end_off_timing = "",
            op_start_or_timing = "", op_end_or_timing = "";
    String imageName, user_type = "", requset_no = "";
    Fragment deletedFragment;
    ConnectionDetector con;
    int width;
    int height;
    JSONObject jObject_response;
    String user_id = "", password = "";
    String sim_two;
    int flag_dashboard = 0;
    int flag_profile = 0;
    int flag_addresses = 0;
    int flag_contact = 0;
    int flag_change_password = 0;
    int flag_edit_profile = 0;
    int flage_tour_trip = 0;
    DataSource dataSource;
    FragmentTransaction transaction;
    MyCustomAsyncTask customAsyncTask;
    boolean isLogin = false;
    private BroadcastReceiver reMyreceive;
    private IntentFilter filter;

    /*crop image*/
    private SharedPreferences mPrefs;
    private SharedPreferences loacalData;
	
	/*public class Myreceiver extends BroadcastReceiver{
	   	 
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
             
            Log.e("test", "MyReceiver: broadcast received");
            
      
            
        }
         
    }*/

    /**
     * Metho : This method is used for create alert dialog
     * if user is not login in app.
     * **/

    public static void defaultOneButtonDialog(final Activity activity, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do things

                        Intent intent = new Intent(activity, SplashActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        activity.startActivity(intent);
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Fabric.with(this, new Crashlytics());


        setContentView(R.layout.activity_home);
        Log.i("TAG", "I am in Home activity New");
        //	BugSenseHandler.setup(this, "YOUR_API_KEY");
        //	Mint.initAndStartSession(this, "a38f5d66");

        Mint.initAndStartSession(HomeActivityNew.this, "155d0310");

        con = new ConnectionDetector(getApplicationContext());
        mPrefs = getSharedPreferences("Location", Context.MODE_PRIVATE);

        loacalData = getSharedPreferences(SharePref.MODE_TYPE, Context.MODE_PRIVATE);

        ctx = HomeActivityNew.this;
        //dataSource=new DataSource(this);

        isLogin = mPrefs.getBoolean("isLogin", false);

        /* Hiding tour trip tab for Distributor & Dealer */
        user_type = mPrefs.getString("op_user_type", null);


        /*calling service for loaction*/
        //filter=new IntentFilter(TestService.BROADCAST_ACTION);
        /*adding action bar in header*/
        addActionBar();


        /*initialize xml controls*/

        initialization();

        /*add onclick Listner on controls*/

        setOnclickListner();


        if (isLogin) {

            if (user_type.equalsIgnoreCase("DISTRIBUTOR") || user_type.equalsIgnoreCase("DEALER")) {


            }
            /*adding fragment to activity*/
            addHomeFragment();
            flag_dashboard = 1;
            // btn_dashboard.setBackgroundResource(R.drawable.ic_tab_dashboard_pressed);

            backAction();
            flag_dashboard = 1;

            /*
             * getting profile data
             * changes made on 21/7/15
             *
             */

            getProfileData();


            //Constructor of CustomAsyncTask for dialog
            customAsyncTask = new MyCustomAsyncTask(HomeActivityNew.this);

            //	throw new RuntimeException("This is a crash");
        } else {
            defaultOneButtonDialog(this, "You are not login in app. Please login first for access app.");
        }
    }

    /*	 * getProfileData() end */

    /*
     * getProfileData() used to get user profile data
     * @changed on 21/7/15
     * @Last Modified on 22/7/15
     *
     * getProfileData() start
     */
    private void getProfileData() {
        // TODO Auto-generated method stub
        try {
            requset_no = "1";
            JSONObject jObject_request = new JSONObject();
            jObject_request.put("request", "getProfile");

            Log.e("##request##", jObject_request.toString());

            new MyAsyncTask(HomeActivityNew.this, jObject_request).execute();
        } catch (Exception e) {

        }

    }

    /*fragment lifecycle method onPause start*/
    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();

        Log.e("onPause", "onPause is working");

    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        Log.e("onResume", "onResume is working");

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

        switch (v.getId()) {

            case R.id.btn_logout:

                GlobalVariables.logout(this);
                break;


            default:
                break;
        }

    }





    /*callback method for track API response end*/

    @Override
    public void onTaskComplete(String result) {
        // TODO Auto-generated method stub
        Log.e("#response#", "" + result);
        try {
            if (requset_no.equals("1"))   //user profile
            {
                if (result != null && result.length() > 0) {

                    jObject_response = new JSONObject(result);
                    String status = jObject_response.getString("status");
                    if (status.equals("200")) {

                        JSONObject data = jObject_response.getJSONObject("data");
                        op_user_name = data.getString("op_user_name");
                        op_email_id = data.getString("op_email_id");
                        op_mobile_number = data.getString("op_mobile_number");
                        op_landline_number = data.getString("op_landline_number");
                        op_address = data.getString("op_address");
                        op_city = data.getString("op_city");
                        op_state = data.getString("op_state");
                        op_pincode = data.getString("op_pincode");
                        op_country = data.getString("op_country");

                        op_territory = data.getString("op_territory_allowed");

                        op_photo = data.getString("op_photo");

                        op_start_off_timing = data.getString("op_start_off_timing");
                        op_end_off_timing = data.getString("op_end_off_timing");
                        op_start_or_timing = data.getString("op_start_or_timing");
                        op_end_or_timing = data.getString("op_end_or_timing");

                        Log.e("Tracking time", op_start_off_timing + " ," + op_end_off_timing);
                        Log.e("Tracking @Night", op_start_or_timing + " ," + op_end_or_timing);

                        Editor editor = loacalData.edit();
                        editor.putString(SharePref.OFFICIAL_TRACKING_START_TIME, op_start_off_timing);
                        editor.putString(SharePref.OFFICIAL_TRACKING_END_TIME, op_end_off_timing);
                        editor.putString(SharePref.HOME_TRACKING_START_TIME, op_start_or_timing);
                        editor.putString(SharePref.HOME_TRACKING_END_TIME, op_end_or_timing);
                        editor.commit();

                        op_user_dob = data.getString("op_user_dob");
                        op_user_type = data.getString("op_user_type");
                        op_message = data.getString("op_message");

                        /*
                         * Storing territory in AppConstant.USER_TERITORY global variable
                         */
                        AppConstant.USER_TERRITORY = op_territory;
                        Log.e("USER_TERRITORY", " = " + op_territory.replace(",", ""));

                        //	setData();


                    } else {
                        GlobalVariables.defaultOneButtonDialog(this, getResources().getString(R.string.default_no_data_found_alert_msg));
                    }

                } else {
                    GlobalVariables.defaultOneButtonDialog(this, getResources().getString(R.string.default_no_data_found_alert_msg));
                }

            }

        } catch (JSONException e) {

        }


    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        //super.onBackPressed();

        //Removing current fragment & display dialog on dashBoard
        removeFragmentOnBackAction();

    }
    /*user define method initialization end*/

    /************************User Define methods**********************************/



    /*user define method initialization start*/
    public void initialization() {

        flayout = (FrameLayout) findViewById(R.id.flayout);


        //Toast.makeText(getApplicationContext(), mPrefs.getString("user_id", ""), Toast.LENGTH_LONG).show();
        //Log.e("user_id", mPrefs.getString("user_id", ""));

    }

    /*User define method setOnclickListner end*/




    /*user define methos add action bar start*/

    /*User define method setOnclickListner start*/
    private void setOnclickListner() {
        // TODO Auto-generated method stub


        //	btn_profile.setOnClickListener(this);

        //	btn_contact.setOnClickListener(this);

        btn_logout.setOnClickListener(this);


    }
    /*user define methos add action bar end*/


    /*user define method for add home fragment start*/

    private void addActionBar() {
        getSupportActionBar().setDisplayUseLogoEnabled(false);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        LayoutInflater inflator = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflator.inflate(R.layout.header, null);
        getSupportActionBar().setCustomView(v);

        textview_back = (TextView) v.findViewById(R.id.textview_back);
        if (user_type.equalsIgnoreCase("DISTRIBUTOR")) {
            textview_back.setText("greatplus distributor app");
        } else if (user_type.equalsIgnoreCase("DEALER")) {
            textview_back.setText("greatplus dealer app");
        } else {
            textview_back.setText("greatplus employee app");
        }


        textview_user_id = (TextView) v.findViewById(R.id.textview_user_id);
        btn_logout = (ImageButton) v.findViewById(R.id.btn_logout);
        textview_user_id.setText(mPrefs.getString("user_id", ""));
        textview_app_title = (TextView) v.findViewById(R.id.textview_back);

        String d = mPrefs.getString("DEALER_ID", "");

        XperiaFunctions.setDealerDetails(HomeActivityNew.this, d, textview_user_id.getText().toString(), user_type);

        //Toast.makeText(getApplicationContext(), d + textview_user_id.getText(), 500).show();

        SpannableString spannable_string = new SpannableString(textview_app_title.getText().toString().trim());
        spannable_string.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.color_great)), 0, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannable_string.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.color_plus)), 5, 10, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        textview_app_title.setText(spannable_string);


    }
    /*user define method for add home fragment end*/

    public void addHomeFragment() {

        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);


			/*   DashBoardFragment fragment=new DashBoardFragment();

			   getSupportFragmentManager().beginTransaction()
		    //    .replace(R.id.flayout, fragment)
			   	.add(R.id.flayout, fragment, "DashBoard" )
		        .addToBackStack(null)
		        .commit();*/


        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);


        CategoryFragment fragment = new CategoryFragment();

        getSupportFragmentManager().beginTransaction()
                //    .replace(R.id.flayout, fragment)
                .add(R.id.flayout, fragment, "CATEGORY")
                .addToBackStack(null)
                .commit();


    }

    /*user define method addFragments start*/

    /*user define method addFragments start*/
    public void addFragment(Fragment fragment) {

        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        getSupportFragmentManager().beginTransaction()
                // .replace(R.id.flayout, fragment)
                .add(R.id.flayout, fragment)
                .addToBackStack(null)
                .commit();

    }


    /*user define method for get fragment or activity name on back button click end*/


    /*user define removeFragmentOnBackAction method for perform action on back button click start*/

    /*user define backAction method for get fragment or activity name on back button click start*/
    public void backAction() {

        getSupportFragmentManager().addOnBackStackChangedListener(new OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                deletedFragment = getSupportFragmentManager().findFragmentById(R.id.flayout);
                // leftHeaderImageView.setVisibility(View.GONE);

            }
        });
    }
    /*user define removeFragmentOnBackAction method for perform action on back button click end*/

    public void removeFragmentOnBackAction() {
        if (deletedFragment != null) {
            String FragmentName = deletedFragment.getClass().getSimpleName();
            Log.e("fragment=", deletedFragment.getClass().getSimpleName());

            if (FragmentName.equals("ProductOrderView")
                    || FragmentName.equals("ProductOrderFragment")
                    || FragmentName.equals("ReportOrderStatusFragment")
                    || FragmentName.equals("OrderApprovalFragment")
                    || FragmentName.equals("ContactFragment")
                    || FragmentName.equals("ChangePasswordFragment")) {


                getSupportFragmentManager().popBackStack();

            } else if (FragmentName.equals("CategoryFragment")) {
                showDialog();
            } else {

                if (op_user_type.equals("DEALER")) {
                    finish();
                } else {
                    showDialog();
                }


            }


        }
    }

    /*User define method for clear all table records*/

    public void showDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(
                HomeActivityNew.this);

        builder.setTitle("SheelaFoam");

        builder.setMessage("Are you sure want to exit from app");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub

                finish();
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub

                        dialog.dismiss();
                    }
                });
        builder.show();
    }

    public void deleteAllRecordsFromDatabase() {

    }

    public void initialization_dialog(Dialog dialog) {
    }

    public void setData() {


    }
		
		 
	
		 
		 
		/* public Intent getServiceObject()
		 {
			 Gson gson = new Gson();
			    String json = mPrefs.getString("MY_SERVICE", "");
			    Intent i = gson.fromJson(json, Intent.class);
			    return i;
		 }*/

    public void setImageNameInSharePrefrenshed() {
        Editor editor = mPrefs.edit();

        editor.putString("imageName", imageName);
        //editor.putString("EMAIL_ID", op_email_id);

        editor.commit();
    }




    /*
     * @see dispatchTouchEvent(android.view.MotionEvent)
     * It'll apply to all EditTexts including those within fragments within that activity.
     */

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        if (requestCode == LOAD_IMAGE_RESULTS || requestCode == CAPTURE_IMAGE_RESULTS) {

				/*	if(getSupportFragmentManager().findFragmentById(R.id.flayout).getClass().getSimpleName().equals("ProductOrderFragment"))
				ProductOrderFragment.getInstance().uploadProfile.onActivityResult(requestCode, resultCode, data);	*/

        }

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }

        return super.dispatchTouchEvent(event);
    }
}
