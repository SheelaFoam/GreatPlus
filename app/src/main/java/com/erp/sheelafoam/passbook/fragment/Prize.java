package com.erp.sheelafoam.passbook.fragment;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import com.erp.sheelafoam.R;
import com.erp.sheelafoam.passbook.adapter.PrizeAdapter;
import com.erp.sheelafoam.passbook.fragment.BaseFragment;
import com.erp.sheelafoam.passbook.model.DetailModel;
import com.erp.sheelafoam.passbook.model.PrizeModel;
import com.erp.sheelafoam.passbook.util.Appconstants;
import com.github.florent37.materialviewpager.header.MaterialViewPagerHeaderDecorator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Prize extends BaseFragment implements View.OnClickListener {


    public static ArrayList<PrizeModel> prizeModelArrayList = new ArrayList<>();
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    private TextView tv_fromDate, tv_toDate;
    private int mYear, mMonth, mDay;
    private ProgressDialog progressDialog;
    private PrizeAdapter bumperPrizeAdapter;
    private ImageView iv_filter;
    private LinearLayout calender_ll;
    private  TextView tv_noData;
    public static JSONObject jsonObject;
    private  ArrayList<DetailModel> detailModelArrayList=new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_prize, container, false);
        tv_fromDate = (TextView) getActivity().findViewById(R.id.tv_fromDate);
        tv_toDate = (TextView) getActivity().findViewById(R.id.tv_toDate);
        iv_filter = (ImageView) getActivity().findViewById(R.id.iv_filter);
        //tv_noData = (TextView) view.findViewById(R.id.tv_noData);
        tv_fromDate.setOnClickListener(this);
        tv_toDate.setOnClickListener(this);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please wait....");
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setHasFixedSize(true);
        bumperPrizeAdapter = new PrizeAdapter(detailModelArrayList,getActivity());
        mRecyclerView.addItemDecoration(new MaterialViewPagerHeaderDecorator());
        mRecyclerView.setAdapter(bumperPrizeAdapter);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        calender_ll = (LinearLayout) getActivity().findViewById(R.id.calender_ll);

    }
    private void getData(String fromDate, String toDate) {
        progressDialog.show();
        Map<String, String> jsonParams = new HashMap<String, String>();
        jsonParams.put("p_user_id", "9814509216");
        jsonParams.put("p_dealer_id", "S1DLH230380");
        jsonParams.put("p_from", fromDate);
        jsonParams.put("p_to", toDate);
        jsonParams.put("p_scheme_type", "PRIZE");
        jsonParams.put("p_report_type", "SUMMARY");
        JsonObjectRequest myRequest = new JsonObjectRequest(
                Request.Method.POST,
                Appconstants.Base_Url,
                new JSONObject(jsonParams),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();
                        //   Toast.makeText(getActivity(), response.toString(), Toast.LENGTH_LONG).show();
                        Log.d("result==", response.toString());
                        try {
                            String status = response.getString("status");
                            if (status.equalsIgnoreCase("200")) {
                                JSONArray jsonArray = response.getJSONArray("SUMMARY");
                                jsonObject = jsonArray.getJSONObject(0);
                                bumperPrizeAdapter.notifyDataSetChanged();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };
        RequestQueue MyRequestQueue = Volley.newRequestQueue(getActivity());
        MyRequestQueue.add(myRequest);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {


        }
    }

//    private void getData(String fromDate, String toDate) {
//        progressDialog.show();
//        final JSONObject jsonObject = new JSONObject();
//
//        try {
//
//            jsonObject.put("p_dealer_id", "S1DLH1276081");
//            jsonObject.put("p_user_id", "8800525300");
//            jsonObject.put("p_from", fromDate);
//            jsonObject.put("p_to", toDate);
//            Log.d("json==", jsonObject.toString());
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        JsonObjectRequest myRequest = new JsonObjectRequest(
//                Request.Method.POST,
//                Appconstants.Bupper_Prize_Url,
//                jsonObject,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                      //  Toast.makeText(getContext(),response.toString(),Toast.LENGTH_LONG).show();
//                        progressDialog.dismiss();
//                        prizeModelArrayList.clear();
//                        mRecyclerView.setVisibility(View.GONE);
//                        tv_noData.setVisibility(View.VISIBLE);
//                        bumperPrizeAdapter.notifyDataSetChanged();
//                        try {
//                            String status = response.getString("status");
//                            if (status.equalsIgnoreCase("200")) {
//                                JSONArray jsonArray = response.getJSONArray("bumper_prize");
//                                for (int i = 0; i < jsonArray.length(); i++) {
//                                    PrizeModel prize = new PrizeModel();
//                                    prize.setTransactionDate(jsonArray.getJSONObject(i).getString("TRANSACTION_DATE"));
//                                    prize.setTransactionDetails(jsonArray.getJSONObject(i).getString("TRANSACTION_DESC"));
//                                    prizeModelArrayList.add(prize);
//                                    Log.d("data===", prizeModelArrayList.get(i).getTransactionDate());
//                                }
//                                if(prizeModelArrayList.isEmpty())
//                                {
//                                    mRecyclerView.setVisibility(View.GONE);
//                                    tv_noData.setVisibility(View.VISIBLE);
//                                }
//                                else
//                                {
//                                    tv_noData.setVisibility(View.GONE);
//                                    mRecyclerView.setVisibility(View.VISIBLE);
//                                    bumperPrizeAdapter.notifyDataSetChanged();
//
//
//                                }
//
//
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
//                        Log.d("error==", error.toString());
//                        progressDialog.dismiss();
//                    }
//                }) {
//
//
//        };
//        RequestQueue MyRequestQueue = Volley.newRequestQueue(getActivity());
//        MyRequestQueue.add(myRequest);
//    }


    @Override
    public void onVisible() {
        getData(tv_fromDate.getText().toString().substring(5), tv_toDate.getText().toString().substring(3));
        calender_ll.setBackgroundColor(getResources().getColor(R.color.sleepwellcolor5));
    }

    @Override
    public void filterData(String fromDate, String toDate) {
        getData(tv_fromDate.getText().toString().substring(5), tv_toDate.getText().toString().substring(3));

    }
}


