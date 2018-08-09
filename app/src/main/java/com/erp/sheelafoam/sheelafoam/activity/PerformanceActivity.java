package com.erp.sheelafoam.sheelafoam.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.erp.sheelafoam.HomeScreen;
import com.erp.sheelafoam.NewWebViewActivity;
import com.erp.sheelafoam.R;
import com.erp.sheelafoam.adapter.PerformanceNewAdpter;
import com.erp.sheelafoam.models.PerformanceNewModels;
import com.erp.sheelafoam.utils.CustomDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class PerformanceActivity extends Activity {
    private ListView listview_perfomance,listview_perfomance_d;
    private ImageButton ib_backicon_new;
    private TextView tv_logoname;
    private Context context;
    private CustomDialog dialog;
    private ProgressDialog progress;
    private PerformanceNewAdpter performanceNewAdpter;
    List<PerformanceNewModels> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.perfomance_activity);
        progress = new ProgressDialog(this);
        ib_backicon_new = (ImageButton) findViewById(R.id.ib_backicon_new);
        tv_logoname = (TextView) findViewById(R.id.tv_logoname_new);
        tv_logoname.setText("Performance Dashboard");
        listview_perfomance = (ListView) findViewById(R.id.listview_perfomance);
        listview_perfomance.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent goToNext=new Intent(PerformanceActivity.this,PerformanceActivityTwo.class);
                startActivity(goToNext);
            }
        });
        try {
            progress = new ProgressDialog(this);
            progress.setMessage("Loading...");
            progress.setCancelable(false);
            progress.show();
            callWebservice();
        } catch (Exception e) {
e.printStackTrace();
        }
        ib_backicon_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goBack = new Intent(PerformanceActivity.this, HomeScreen.class);
                startActivity(goBack);
            }
        });
    }
    private void callWebservice(){
        String url = "https://greatplus.com/warranty_log_api/performance_dashboard_api.php";
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                System.out.println("res" + jsonObject);
                progress.dismiss();
                try {
                    if (jsonObject.getString("status").equalsIgnoreCase("true")) {
                        list = new ArrayList<>();
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                      /*  for (int i = 0; i < jsonArray.length(); i++) {*/
                      PerformanceNewModels model=new PerformanceNewModels();
                            JSONObject object = (JSONObject) jsonArray.get(0);
                            JSONObject object2 = (JSONObject) jsonArray.get(1);
                            String name=object.getString("name");
                            model.setName(name);
                            String imgURL= object.getString("image");
                            model.setImage(imgURL);
                            list.add(model);
                        Log.d("resimg", object.getString("image")+","+object2.getString("name"));
                           /* JSONArray array = object.getJSONArray("storeList");
                            for (int j = 0; j < array.length(); j++) {
                                JSONObject _object = (JSONObject) array.get(j);
                                _object.getString("pARENT_CHANNEL_PARTNER_NAME");
                                preference.setUserNameFootFall(_object.getString("pARENT_CHANNEL_PARTNER_NAME"));
                                tv_selectStoreFeedbackSpinner.setText(preference.getUserNameFootFall());
                                Log.d("getresponse", _object.getString("pARENT_CHANNEL_PARTNER_NAME").toString());
                            }*/
                       // }
                       performanceNewAdpter = new PerformanceNewAdpter(PerformanceActivity.this, list);
                        listview_perfomance.setAdapter(performanceNewAdpter);
                        //customDialog();
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
        RequestQueue requestQueue = Volley.newRequestQueue(PerformanceActivity.this);
        requestQueue.add(objectRequest);
    }
}
