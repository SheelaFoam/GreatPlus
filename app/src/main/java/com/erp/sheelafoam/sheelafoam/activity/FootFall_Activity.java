package com.erp.sheelafoam.sheelafoam.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.erp.sheelafoam.HomeScreen;
import com.erp.sheelafoam.R;
import com.erp.sheelafoam.adapter.DialogFootFallAdapter;
import com.erp.sheelafoam.adapter.RadioFootFallAdapter;
import com.erp.sheelafoam.constant.Constant;
import com.erp.sheelafoam.fragment.DashBoard;
import com.erp.sheelafoam.model.SerialNumModel;
import com.erp.sheelafoam.model.UserModel;
import com.erp.sheelafoam.prefrences.SheelaSharedPreference;
import com.erp.sheelafoam.utils.CommonClass;
import com.erp.sheelafoam.utils.CustomDialog;
import com.erp.sheelafoam.utils.CustomTypefaceTextView;
import com.erp.sheelafoam.utils.URLS;
import com.erp.sheelafoam.utils.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FootFall_Activity extends Activity implements View.OnClickListener {
    Context context;
    CustomDialog dialog;
    UserModel userModel = new UserModel();
    ArrayList<String> arrayListS = new ArrayList();
    ArrayList<String> array = new ArrayList<>();
    private ListView list_listViewMts;
    private CustomTypefaceTextView tv_selectStoreSpinner, tv_drpproductdispname;
    private ImageButton ib_backicon_new;
    private TextView tv_logoname, tv_emptyText;
    private ProgressDialog progress;
    private TextView tv_datePickr;
    private EditText et_numberWalking, et_numberConversation;
    private SheelaSharedPreference preference;
    private CommonClass commonClass;
    private int mYear, mMonth, mDay;
    private String date_from, date_to;
    private Button btn_submitFootfall;

    private static String formatDate(int year, int month, int day) {

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(0);
        cal.set(year, month, day);
        Date date = cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");

        return sdf.format(date);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.footfall_conversion_activity);
        preference = new SheelaSharedPreference(this);
        progress=new ProgressDialog(this);
        ib_backicon_new = (ImageButton) findViewById(R.id.ib_backicon_new);
        tv_logoname = (TextView) findViewById(R.id.tv_logoname_new);
        tv_logoname.setText("Footfall & Conversion");
        et_numberWalking = (EditText) findViewById(R.id.et_numberWalking);
        et_numberConversation = (EditText) findViewById(R.id.et_numberConversation);
        tv_selectStoreSpinner = (CustomTypefaceTextView) findViewById(R.id.tv_selectStoreSpinner);
        tv_drpproductdispname = (CustomTypefaceTextView) findViewById(R.id.tv_drpproductdispname);
        btn_submitFootfall = (Button) findViewById(R.id.btn_submitFootfall);
        tv_datePickr = (TextView) findViewById(R.id.tv_datePickr);
        tv_datePickr.setOnClickListener(this);
        tv_selectStoreSpinner.setOnClickListener(this);
        tv_drpproductdispname.setOnClickListener(this);
        btn_submitFootfall.setOnClickListener(this);
        ib_backicon_new.setOnClickListener(this);
        arrayListS.add("TV");
        arrayListS.add("Newspaper");
        arrayListS.add("Radio");
        arrayListS.add("Internet");
        arrayListS.add("Others");
        userModel.setPUserId("TV1");
        userModel.setPUserId("TV2");
        userModel.setPUserId("TV3");
        userModel.setPUserId("TV4");
        userModel.setPUserId("TV5");
        userModel.setSerialNumber(arrayListS);
        ArrayList ar = userModel.getSerialNumber();
        try {
            progress = new ProgressDialog(this);
            progress.setMessage("Loading...");
            progress.setCancelable(false);
            progress.show();
            callGetService();
            testService();
        }catch (Exception e){

        }

    }

    @Override
    public void onClick(View view) {
        {
            switch (view.getId()) {
                case R.id.tv_datePickr:
                    openDateDialog();
                    break;
                case R.id.tv_selectStoreSpinner:
                    openRadioDialog(this);
                    break;
                case R.id.tv_drpproductdispname:
                    openCustomeDialog(this);
                    break;
                case R.id.ib_backicon_new:
                    Intent intent = new Intent(FootFall_Activity.this, HomeScreen.class);
                    startActivity(intent);
                    break;
                case R.id.btn_submitFootfall:
                    if (isValid()){
                        try {
                            progress = new ProgressDialog(this);
                            progress.setMessage("Loading...");
                            progress.setCancelable(false);
                            progress.show();
                            callSubmitService();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                    break;
            }
        }
    }

    private void callGetService() {
        String UserID = Util.getSharedPrefrenceValue(FootFall_Activity.this, Constant.Sp_UserID);
        String UserToken = Util.getSharedPrefrenceValue(FootFall_Activity.this, Constant.Sp_UserToken);
        System.out.println("NEWTOKEN"+UserToken +","+UserID);
       // String URL = " http://be.greatplus.com/sheelafoam/rest/employees/details/ashish.nandodariya/UHJvamVjdEAjNDU= ";
        String URL = " http://be.greatplus.com/sheelafoam/rest/employees/details/"+UserID+"/"+UserToken;
       // String URL2 = URL.replaceAll(" ", "%20");
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, URL, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                System.out.println("getresponse" + jsonObject);
                progress.dismiss();
                try {
                    if (jsonObject.getBoolean("success")) {
                        jsonObject.getString("message");
                        Log.d("getresponse", jsonObject.getString("message").toString());
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = (JSONObject) jsonArray.get(i);
                            object.getString("eMP_NAME");
                            Log.d("getresponse", object.getString("eMP_NAME").toString());
                            preference.setEMPName(object.getString("eMP_NAME"));
                            JSONArray array = object.getJSONArray("storeList");
                            for (int j = 0; j < array.length(); j++) {
                                JSONObject _object = (JSONObject) array.get(j);
                                _object.getString("pARENT_CHANNEL_PARTNER_NAME");
                                preference.setUserNameFootFall(_object.getString("pARENT_CHANNEL_PARTNER_NAME"));
                                tv_selectStoreSpinner.setText(_object.getString("pARENT_CHANNEL_PARTNER_NAME"));
                                preference.setChannelPartnerName(_object.getString("cHANNEL_PARTNER_GROUP"));
                                Log.d("getresponse", _object.getString("pARENT_CHANNEL_PARTNER_NAME").toString());
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progress.dismiss();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(FootFall_Activity.this);
        requestQueue.add(objectRequest);
    }

    private void callSubmitService() throws JSONException {
        String UserID = Util.getSharedPrefrenceValue(FootFall_Activity.this, Constant.Sp_UserID);
        String UserToken = Util.getSharedPrefrenceValue(FootFall_Activity.this, Constant.Sp_UserToken);
        System.out.println("NEWTOKEN"+UserToken +","+UserID);
       // String URL_SUBMIT_FOOTFALL = "http://be.greatplus.com/sheelafoam/rest/services/saveAdvertisementFeedback/ashish.nandodariya/UHJvamVjdEAjNDU=";
        String URL_SUBMIT_FOOTFALL = "http://be.greatplus.com/sheelafoam/rest/services/saveAdvertisementFeedback/"+UserID+"/"+UserToken;
        RequestQueue queue = Volley.newRequestQueue(FootFall_Activity.this);
        Map<String, String> postParam = new HashMap<String, String>();
        postParam.put("storeId", preference.getUserNameFootFall());
        postParam.put("numberOfWalkin", et_numberWalking.getText().toString());
        postParam.put("numberOfConversation", et_numberConversation.getText().toString());
        postParam.put("heardAbout", tv_drpproductdispname.getText().toString());
        postParam.put("cityId", "");
        postParam.put("channelPartnerGroup", preference.getChannelPartnerName());
        postParam.put("date", tv_datePickr.getText().toString());
        System.out.println("footfallreq" + postParam);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                URL_SUBMIT_FOOTFALL, new JSONObject(postParam),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("footfallresponse" + response);
                        progress.dismiss();
                        try {
                            if (response.getBoolean("success")) {
                                response.getString("message");
                                Toast.makeText(FootFall_Activity.this, response.getString("message"), Toast.LENGTH_LONG).show();
                                tv_selectStoreSpinner.setText("");
                                tv_drpproductdispname.setText("");
                                tv_datePickr.setText("");
                                et_numberWalking.setText("");
                                et_numberConversation.setText("");

                            } else {

                            }

                        } catch (Exception e) {

                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
progress.dismiss();
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

    private void openDateDialog() {
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(FootFall_Activity.this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                tv_datePickr.setText(formatDate(year, monthOfYear, dayOfMonth));
            }
        }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    public void openCustomeDialog(Context context) {
        dialog = new CustomDialog(context, R.layout.dialog_footfall);
        dialog.setCancelable(true);
        CustomTypefaceTextView tv_subTile = (CustomTypefaceTextView) dialog.findViewById(R.id.tv_subTile_footfall);
        tv_subTile.setText("Form where did you hear about Sleepwell ? *");
        ListView lv = (ListView) dialog.findViewById(R.id.recycler_footfall);
        TextView btn_footfall_cancel = (TextView) dialog.findViewById(R.id.btn_footfall_cancel);
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
        dialog.show();
        lv.setAdapter(new DialogFootFallAdapter(FootFall_Activity.this, sNoList));//--1--for--checkbox--xml
        final ArrayList<String> SNoArr = new ArrayList<>();
        final TextView btn_bundle_submit = (TextView) dialog.findViewById(R.id.btn_footfall_ok);
        btn_bundle_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (SerialNumModel m : sNoList) {
                    if (m.isChecked()) {
                        SNoArr.add(m.getsNo());
                    }
                }
                if (SNoArr.size() < 1) {
                    Toast.makeText(FootFall_Activity.this, "Please select minimum one!", Toast.LENGTH_SHORT).show();

                } else if (SNoArr.size() >= 5) {
                    tv_drpproductdispname.setText(SNoArr.get(0) + "," + SNoArr.get(1) + "," + SNoArr.get(2) + "," + SNoArr.get(3) + "," + SNoArr.get(4));
                    dialog.dismiss();
                } else if (SNoArr.size() >= 4) {
                    tv_drpproductdispname.setText(SNoArr.get(0) + "," + SNoArr.get(1) + "," + SNoArr.get(2) + "," + SNoArr.get(3));
                    dialog.dismiss();
                } else if (SNoArr.size() >= 3) {
                    tv_drpproductdispname.setText(SNoArr.get(0) + "," + SNoArr.get(1) + "," + SNoArr.get(2));
                    dialog.dismiss();
                } else if (SNoArr.size() >= 2) {
                    tv_drpproductdispname.setText(SNoArr.get(0) + "," + SNoArr.get(1));
                    dialog.dismiss();
                } else if (SNoArr.size() >= 1) {
                    tv_drpproductdispname.setText(SNoArr.get(0));
                    dialog.dismiss();
                }

            }
        });
        btn_footfall_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    private void openRadioDialog(Context context) {
        array.clear();
        array.add(preference.getUserNameFootFall());
        dialog = new CustomDialog(context, R.layout.dialog_footfall);
        dialog.setCancelable(true);
        CustomTypefaceTextView tv_subTile = (CustomTypefaceTextView) dialog.findViewById(R.id.tv_subTile_footfall);
        tv_subTile.setText("Select Store ? *");
        ListView lv = (ListView) dialog.findViewById(R.id.recycler_footfall);
        TextView btn_footfall_cancel = (TextView) dialog.findViewById(R.id.btn_footfall_cancel);
        TextView btn_footfall_ok = (TextView) dialog.findViewById(R.id.btn_footfall_ok);
        btn_footfall_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_selectStoreSpinner.setText(preference.getUserNameFootFall());
                dialog.dismiss();
            }
        });
        dialog.show();
        lv.setAdapter(new RadioFootFallAdapter(this, array));
        btn_footfall_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    private boolean isValid() {
        if (tv_selectStoreSpinner.getText().toString().isEmpty()) {
            Toast.makeText(FootFall_Activity.this, "Please select store !", Toast.LENGTH_LONG).show();
            return false;
        }else if (tv_drpproductdispname.getText().toString().isEmpty()){
            Toast.makeText(FootFall_Activity.this, "Please select Form where did you hear about sleepwell !", Toast.LENGTH_LONG).show();
            return false;
        }
        else if (tv_datePickr.getText().toString().isEmpty()){
            Toast.makeText(FootFall_Activity.this, "Please select advertisement date !", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
    }
    private void testService(){
        String URL="https://greatplus.com/api_services/bumper_prize_api.php";
        RequestQueue queue = Volley.newRequestQueue(FootFall_Activity.this);
        Map<String, String> postParam = new HashMap<String, String>();
        postParam.put("p_dealer_id", "S1DLH1276081");
        postParam.put("p_user_id", "8800525300");
        postParam.put("p_from", "05-07-2018");
        postParam.put("p_to", "25-07-2018");
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                URL, new JSONObject(postParam),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("test" + response);
                        progress.dismiss();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("test" + error);
                progress.dismiss();
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }

        };
        queue.add(jsonObjReq);
    }
}