package com.erp.sheelafoam.passbook.fragment;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
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
import com.erp.sheelafoam.constant.Constant;
import com.erp.sheelafoam.passbook.adapter.ParivartanStarAdapter;
import com.erp.sheelafoam.passbook.model.DetailModel;
import com.erp.sheelafoam.passbook.util.Appconstants;
import com.erp.sheelafoam.utils.Util;
import com.github.florent37.materialviewpager.header.MaterialViewPagerHeaderDecorator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ParivartanStar extends BaseFragment implements View.OnClickListener {
    private static final boolean GRID_LAYOUT = false;
    private static final int ITEM_COUNT = 4;
    public static JSONObject jsonObject = null;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    ParivartanStarAdapter parivartanStarAdapter;
    private TextView tv_fromDate, tv_toDate;
    private int mYear, mMonth, mDay;
    private ImageView iv_filter;
    private ProgressDialog progressDialog;
    private String fromDate = "";
    private String toDate = "";
    private ArrayList<DetailModel> detailModelArrayList = new ArrayList<>();
    private LinearLayout calender_ll;
    private String  delearID,UserID ;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_parivartan_star, container, false);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please wait.....");
        delearID = Util.getSharedPrefrenceValue(getActivity(), Constant.Sp_emp_grpCode);
        UserID = Util.getSharedPrefrenceValue(getActivity(), Constant.Sp_UserID);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        tv_fromDate = (TextView) getActivity().findViewById(R.id.tv_fromDate);
        tv_toDate = (TextView) getActivity().findViewById(R.id.tv_toDate);
        calender_ll = (LinearLayout) getActivity().findViewById(R.id.calender_ll);

        tv_fromDate.setOnClickListener(this);
        tv_toDate.setOnClickListener(this);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        final List<Object> items = new ArrayList<>();

        for (int i = 0; i < ITEM_COUNT; ++i) {
            items.add(new Object());
        }


        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mRecyclerView.setHasFixedSize(true);

        //Use this now
        parivartanStarAdapter = new ParivartanStarAdapter(getActivity(), detailModelArrayList);
        mRecyclerView.addItemDecoration(new MaterialViewPagerHeaderDecorator());
        mRecyclerView.setAdapter(parivartanStarAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_fromDate: {

                // Get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                //   Toast.makeText(getActivity(), "" + dayOfMonth, Toast.LENGTH_SHORT).show();
                                fromDate = dayOfMonth + "-" + monthOfYear + "-" + year;

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
            break;
            case R.id.tv_toDate: {

                // Get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                //  Toast.makeText(getActivity(), "" + dayOfMonth, Toast.LENGTH_SHORT).show();
                                toDate = dayOfMonth + "-" + monthOfYear + "-" + year;
                                getData(fromDate, toDate);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();

            }
            break;
        }
    }

    private void getData(String fromDate, String toDate) {
        progressDialog.show();

        Map<String, String> jsonParams = new HashMap<String, String>();
        jsonParams.put("p_user_id", UserID);
        jsonParams.put("p_dealer_id", delearID);
        jsonParams.put("p_from", fromDate);
        jsonParams.put("p_to", toDate);
        jsonParams.put("p_scheme_type", "PARIVATAN STAR");
        jsonParams.put("p_report_type", "SUMMARY");
        JsonObjectRequest myRequest = new JsonObjectRequest(
                Request.Method.POST,
                Appconstants.Base_Url,
                new JSONObject(jsonParams),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();
                        // Toast.makeText(getActivity(), response.toString(), Toast.LENGTH_LONG).show();
                        Log.d("result==", response.toString());

                        try {
                            String status = response.getString("status");
                            if (status.equalsIgnoreCase("200")) {
                                JSONArray jsonArray = response.getJSONArray("SUMMARY");
                                jsonObject = jsonArray.getJSONObject(0);
                                parivartanStarAdapter.notifyDataSetChanged();
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
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser) {

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                }
            }, 100);

        }
    }

    @Override
    public void onVisible() {
        getData(tv_fromDate.getText().toString().substring(5), tv_toDate.getText().toString().substring(3));
        calender_ll.setBackgroundColor(getResources().getColor(R.color.sleepwell3));
    }

    @Override
    public void filterData(String fromDate, String toDate) {
        getData(fromDate, toDate);
    }
}



