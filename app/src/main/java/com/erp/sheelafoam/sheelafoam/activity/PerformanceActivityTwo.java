package com.erp.sheelafoam.sheelafoam.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

public class PerformanceActivityTwo extends Activity {
    private GridView gridView_perfomance_two;
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
        setContentView(R.layout.performance_activity_two);
        progress = new ProgressDialog(this);
        ib_backicon_new = (ImageButton) findViewById(R.id.ib_backicon_new);
        tv_logoname = (TextView) findViewById(R.id.tv_logoname_new);
        tv_logoname.setText("Performance Dashboard");
        gridView_perfomance_two = (GridView) findViewById(R.id.gridView_perfomance_two);
        gridView_perfomance_two.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Object listItem = gridView_perfomance_two.getItemAtPosition(position);
                PerformanceNewModels models= (PerformanceNewModels) parent.getItemAtPosition(position);
                String link=models.getImageLink();
                String name=models.getName();
                System.out.println("link"+link);
                Intent in = new Intent(PerformanceActivityTwo.this, NewWebViewActivity.class);
                in.putExtra("WebUrl", link);
                in.putExtra("title", name);
                startActivity(in);

               //openGPlus(name);
            }
        });
        try {
            progress = new ProgressDialog(this);
            progress.setMessage("Loading...");
            progress.setCancelable(false);
            progress.show();
            callWebservice();
        } catch (Exception e) {

        }
        ib_backicon_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goBack = new Intent(PerformanceActivityTwo.this, PerformanceActivity.class);
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
                         for (int i = 1; i < jsonArray.length(); i++) {
                             JSONObject object = (JSONObject) jsonArray.get(i);
                             PerformanceNewModels model=new PerformanceNewModels();
                        String name=object.getString("name");
                        model.setName(name);
                        String imgURL= object.getString("image");
                        model.setImage(imgURL);
                        String urlLink=object.getString("link");
                        model.setImageLink(urlLink);
                        list.add(model);
                        Log.d("resimg", object.getString("image")+","+object.getString("name"));
                         }
                        performanceNewAdpter = new PerformanceNewAdpter(PerformanceActivityTwo.this, list);
                        gridView_perfomance_two.setAdapter(performanceNewAdpter);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(PerformanceActivityTwo.this);
        requestQueue.add(objectRequest);
    }
    public void openGPlus(String profile) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setClassName("com.google.android.apps.plus",
                    "com.google.android.apps.plus.phone.UrlGatewayActivity");
            intent.putExtra("customAppUri", profile);
            startActivity(intent);
        } catch(ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://plus.google.com/"+profile+"/posts")));
        }
    }
}
