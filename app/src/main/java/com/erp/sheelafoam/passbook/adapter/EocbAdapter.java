package com.erp.sheelafoam.passbook.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.erp.sheelafoam.passbook.PassbookMainActivity;
import com.erp.sheelafoam.passbook.fragment.Eocb;
import com.erp.sheelafoam.passbook.model.DetailModel;
import com.erp.sheelafoam.passbook.util.Appconstants;
import com.erp.sheelafoam.utils.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EocbAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    static final int TYPE_HEADER = 0;
    static final int TYPE_CELL = 1;
    List<DetailModel> contents;
    Context context;
    private CardView card_view;
    private RecyclerView recyclerView;
    private DetailAdapter eocbAdapter;
    private TextView tv_opening, tv_closing, tv_credit, tv_debit, tv_DtransDate, tv_DtransDetail, tv_Dcredit, tv_Ddebit;

    public EocbAdapter(ArrayList<DetailModel> contents, Context context) {
        this.contents = contents;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case 0:
                return TYPE_HEADER;
            default:
                return TYPE_CELL;
        }
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;

        switch (viewType) {
            case TYPE_HEADER: {
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_item_card_big, parent, false);
                card_view = (CardView) view.findViewById(R.id.card_view);
                tv_opening = (TextView) view.findViewById(R.id.tv_opening);
                tv_closing = (TextView) view.findViewById(R.id.tv_closing);
                tv_credit = (TextView) view.findViewById(R.id.tv_credit);
                tv_debit = (TextView) view.findViewById(R.id.tv_debit);
                recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
                recyclerView.setHasFixedSize(true);
                return new RecyclerView.ViewHolder(view) {
                };
            }
            case TYPE_CELL: {
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_item_card_small, parent, false);
                card_view = (CardView) view.findViewById(R.id.card_view);
                tv_DtransDate = (TextView) view.findViewById(R.id.tv_DtransDate);
                tv_DtransDetail = (TextView) view.findViewById(R.id.tv_DtransDetail);
                tv_Dcredit = (TextView) view.findViewById(R.id.tv_Dcredit);
                tv_Ddebit = (TextView) view.findViewById(R.id.tv_Ddebit);
                return new RecyclerView.ViewHolder(view) {
                };
            }
        }
        return null;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case TYPE_HEADER:
                getDetailData(PassbookMainActivity.tv_fromDate.getText().toString().substring(5), PassbookMainActivity.tv_toDate.getText().toString().substring(3));
                try {
                    if(!Eocb.jsonObject.isNull("OPENING"))
                        tv_opening.setText(Eocb.jsonObject.getString("OPENING"));
                    if(!Eocb.jsonObject.isNull("CLOSING"))
                        tv_closing.setText(Eocb.jsonObject.getString("CLOSING"));
                    if(!Eocb.jsonObject.isNull("CR"))
                        tv_credit.setText(Eocb.jsonObject.getString("CR"));
                    if(!Eocb.jsonObject.isNull("DR"))
                        tv_debit.setText(Eocb.jsonObject.getString("DR"));
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {

                }
                break;
            case TYPE_CELL:
                tv_Dcredit.setText(contents.get(position).getCredit());
                tv_Ddebit.setText(contents.get(position).getDebit());
                tv_DtransDate.setText(contents.get(position).getTransDate());
                tv_DtransDetail.setText(contents.get(position).getTransDetail());


                break;
        }
    }

    private void getDetailData(String fromDate, String toDate) {
       String delearID = Util.getSharedPrefrenceValue(context, Constant.Sp_emp_grpCode);
       String UserID = Util.getSharedPrefrenceValue(context, Constant.Sp_UserID);
        Map<String, String> jsonParams = new HashMap<String, String>();
        jsonParams.put("p_user_id", UserID);
        jsonParams.put("p_dealer_id", delearID);
        jsonParams.put("p_from", fromDate);
        jsonParams.put("p_to", toDate);
        jsonParams.put("p_scheme_type", "EOCB");
        jsonParams.put("p_report_type", "DETAIL");
        JsonObjectRequest myRequest = new JsonObjectRequest(
                Request.Method.POST,
                Appconstants.Base_Url,
                new JSONObject(jsonParams),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        contents.clear();

                        //   Toast.makeText(getActivity(), response.toString(), Toast.LENGTH_LONG).show();
                        Log.d("result==", response.toString());
                        try {
                            String status = response.getString("status");
                            if (status.equalsIgnoreCase("200")) {
                                JSONArray jsonArray = response.getJSONArray("DETAIL");
                                DetailModel detailModel = new DetailModel();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    detailModel.setCredit(jsonArray.getJSONObject(i).getString("CREDIT"));
                                    detailModel.setDebit(jsonArray.getJSONObject(i).getString("DEBIT"));
                                    detailModel.setTransDate(jsonArray.getJSONObject(i).getString("TRANSACTION_DATE"));
                                    detailModel.setTransDetail(jsonArray.getJSONObject(i).getString("TRANSACTION_DESC"));
                                    contents.add(detailModel);
                                }
                                eocbAdapter = new DetailAdapter(context, contents);
                                recyclerView.setAdapter(eocbAdapter);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };
        RequestQueue MyRequestQueue = Volley.newRequestQueue(context);
        MyRequestQueue.add(myRequest);
    }
}
