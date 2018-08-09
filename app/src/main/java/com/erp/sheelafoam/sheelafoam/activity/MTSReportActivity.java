package com.erp.sheelafoam.sheelafoam.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.text.TextUtilsCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.erp.sheelafoam.HomeScreen;
import com.erp.sheelafoam.NewWebViewActivity;
import com.erp.sheelafoam.R;
import com.erp.sheelafoam.adapter.MTSReportAdapter;
import com.erp.sheelafoam.constant.Constant;
import com.erp.sheelafoam.models.MTSModel;
import com.erp.sheelafoam.prefrences.SheelaSharedPreference;
import com.erp.sheelafoam.sheelafoam.exchangeschame.ExchangeSchameActivity;
import com.erp.sheelafoam.utils.CommonClass;
import com.erp.sheelafoam.utils.CustomTypefaceEditText;
import com.erp.sheelafoam.utils.URLS;
import com.erp.sheelafoam.utils.Util;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dmax.dialog.SpotsDialog;

import static com.erp.sheelafoam.R.id.contact;
import static com.erp.sheelafoam.R.id.tv_msg;

/**
 * Created by E6036 on 5/3/2018.
 */

public class MTSReportActivity extends Activity {
    Context context;
    private ListView list_listViewMts;
    private CustomTypefaceEditText et_search;
    private ImageButton ib_backicon, ib_filter;
    private MTSReportAdapter mtsListAdapter;
    private ProgressDialog progress;
    List<MTSModel> details;
    private TextView tv_logoname,tv_emptyText;
    private SheelaSharedPreference preference;
    private CommonClass commonClass;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mts_report);
        preference = new SheelaSharedPreference(this);
        progress = new ProgressDialog(this);
        ib_backicon = (ImageButton) findViewById(R.id.ib_backicon);
        tv_logoname = (TextView) findViewById(R.id.tv_logoname);
        tv_logoname.setText("MTS Report");
        ib_filter = (ImageButton) findViewById(R.id.ib_filter);
        ib_filter.setVisibility(View.VISIBLE);
        et_search = (CustomTypefaceEditText) findViewById(R.id.et_search);
        tv_emptyText= (TextView) findViewById(R.id.tv_emptyText);
        list_listViewMts = (ListView) findViewById(R.id.list_listViewMts);


        try {
            if (!commonClass.checkInternetConnection(MTSReportActivity.this)) {
                Toast.makeText(MTSReportActivity.this, "Please check internet connection!", Toast.LENGTH_LONG).show();
            } else {
                progress = new ProgressDialog(MTSReportActivity.this);
                progress.setMessage("Please wait....");
                progress.setCancelable(false);
                progress.show();
               // new SpotsDialog(MTSReportActivity.this, "Loading").show();
               String delearID = Util.getSharedPrefrenceValue(MTSReportActivity.this, Constant.Sp_emp_grpCode);
               System.out.println("MTS DelearID"+delearID);
                callMTSReportService(delearID);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ib_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFilterDialog();
            }
        });
        ib_backicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goBack = new Intent(MTSReportActivity.this, HomeScreen.class);
                startActivity(goBack);
            }
        });
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (details.size() > 0) {
                    String find = charSequence.toString();
                    ArrayList<MTSModel> temp = setsearchlist(find);
                    mtsListAdapter = new MTSReportAdapter(MTSReportActivity.this, temp);
                    list_listViewMts.setAdapter(mtsListAdapter);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    public ArrayList<MTSModel> setsearchlist(String find) {
        ArrayList<MTSModel> list = new ArrayList<>();
        list.clear();
        String findWord = find.toLowerCase();
        for (MTSModel mtsModel : details) {
            if (mtsModel.getPRODUCTDISPLAYNAME().toLowerCase().contains(findWord) ||
                    mtsModel.getBREDTH().toLowerCase().contains(findWord) ||
                    mtsModel.getTHICK().toLowerCase().contains(findWord) ||
                    mtsModel.getLENGTH().toLowerCase().contains(findWord) ||
                    mtsModel.getCURRSTOCK().toLowerCase().contains(findWord)) {
                list.add(mtsModel);
            }
        }
        return list;
    }

    public ArrayList<MTSModel> setsearchlist1(String find, int id) {
        ArrayList<MTSModel> list = new ArrayList<>();
        list.clear();
        String findWord = find.toLowerCase();
        for (MTSModel mtsModel : details) {
            if (mtsModel.getPRODUCTDISPLAYNAME().toLowerCase().contains(findWord) ||
                    mtsModel.getBREDTH().toLowerCase().contains(findWord) ||
                    mtsModel.getTHICK().toLowerCase().contains(findWord) ||
                    mtsModel.getLENGTH().toLowerCase().contains(findWord) ||
                    mtsModel.getCURRSTOCK().toLowerCase().contains(findWord)) {
                list.add(mtsModel);
            }
        }
        return list;
    }

    private void callMTSReportService(String p_dealer_id) throws JSONException {
        com.android.volley.RequestQueue requestQueue1 = Volley.newRequestQueue(MTSReportActivity.this);
        final JSONObject jsonObject = new JSONObject();
        jsonObject.put("p_dealer_id", p_dealer_id);
         System.out.println("MTS Req:-"+jsonObject);
        JsonArrayRequest _arrayReq = new JsonArrayRequest(Request.Method.POST, URLS.MTS_URL, jsonObject,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        progress.dismiss();
                        //new SpotsDialog(MTSReportActivity.this, "Loading").dismiss();
                        System.out.println("MTS response is: " + jsonArray);
                        Gson gson = new Gson();
                        Type collectionType = new TypeToken<List<MTSModel>>() {
                        }.getType();
                        details = gson.fromJson(jsonArray.toString(), collectionType);
                        mtsListAdapter = new MTSReportAdapter(MTSReportActivity.this, details);
                        list_listViewMts.setAdapter(mtsListAdapter);
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(" I am in error");
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
        requestQueue1.add(_arrayReq);

    }

    @Override
    public void onBackPressed() {
        //  setFinalResult();
        super.onBackPressed();

    }

    public void showFilterDialog() {
        final Dialog dialog = new Dialog(MTSReportActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.filter_mts);
        String names[] = {"All", "In Stock", "Out of Stock"};
        ListView list_filterList = (ListView) dialog.findViewById(R.id.list_filterList);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MTSReportActivity.this, android.R.layout.simple_list_item_1, names);
        list_filterList.setAdapter(adapter);

        list_filterList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (position == 0) {
                    //for all
                    mtsListAdapter = new MTSReportAdapter(MTSReportActivity.this, details);
                    list_listViewMts.setAdapter(mtsListAdapter);
                } else if (position == 1) {
                    // In stock
                    List<MTSModel> inStockList = new ArrayList<MTSModel>();
                    for (MTSModel m : details) {
                        if (!TextUtils.isEmpty(m.getCURRSTOCK())) {
                            //int curStock = Integer.parseInt(m.getCURRSTOCK());
                           float curStock= Float.parseFloat(m.getCURRSTOCK());
                            if (curStock > 0) {
                                inStockList.add(m);
                            }
                        }
                    }
                    mtsListAdapter = new MTSReportAdapter(MTSReportActivity.this, inStockList);
                    list_listViewMts.setAdapter(mtsListAdapter);
                } else if (position == 2) {
                    //Out of stock
                    List<MTSModel> outOfStockList = new ArrayList<MTSModel>();
                    for (MTSModel m : details) {
                        if (!TextUtils.isEmpty(m.getCURRSTOCK())) {
                            //int curStock = Integer.parseInt(m.getCURRSTOCK());
                            float curStock=Float.parseFloat(m.getCURRSTOCK());
                            if (curStock <= 0) {
                                outOfStockList.add(m);
                            }
                        }
                    }
                    mtsListAdapter = new MTSReportAdapter(MTSReportActivity.this, outOfStockList);
                    list_listViewMts.setAdapter(mtsListAdapter);
                }
                dialog.dismiss();
            }
        });

        dialog.show();
    }


}
