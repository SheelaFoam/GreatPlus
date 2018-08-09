package com.erp.sheelafoam;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.erp.sheelafoam.constant.Constant;

import com.erp.sheelafoam.sheelafoam.utility.AsyncTaskListner;
import com.erp.sheelafoam.sheelafoam.utility.GlobalVariables;
import com.erp.sheelafoam.sheelafoam.utility.HelperMethods;
import com.erp.sheelafoam.sheelafoam.utility.MyCustomAsyncTask;
import com.erp.sheelafoam.sheelafoam.xperia.Complaint;
import com.erp.sheelafoam.sheelafoam.xperia.ComplaintListModel;
import com.erp.sheelafoam.sheelafoam.xperia.activity.LogComplaintActivity;
import com.erp.sheelafoam.sheelafoam.xperia.adapter.ComplaintListAdapter;
import com.erp.sheelafoam.sheelafoam.xperia.dataholders.GetSetComplaint;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MoreAboutComplaint extends AppCompatActivity implements AsyncTaskListner {
    TextView no, cmpDate, city, name, mobile, address, product, billDate, dealer, problem, pending, toolbarTitle;
    Toolbar toolbar;
    EditText etComplaintId, etMobileNo, etKeyword;
    TextView btnSearch;
    boolean validate = false;
    JSONObject jsonObjRequest;
    MyCustomAsyncTask myCustomAsyncTask;
    ListView complaintList;
    CardView cardDetails;
    List<Complaint> complaints = new ArrayList<>();
    ComplaintListAdapter complaintListAdapter;
    ProgressBar progressBar;
    LinearLayout layoutStaticComplaint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_about_complaint);


        myCustomAsyncTask = new MyCustomAsyncTask(this);

        initViews();
    }

    GetSetComplaint getSetComplaint;
    private void initViews() {

        if (getIntent().getExtras() != null)
            getSetComplaint = (GetSetComplaint) getIntent().getSerializableExtra("complaint_details");


        toolbar = (Toolbar) findViewById(R.id.app_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbarTitle = (TextView) findViewById(R.id.app_toolbar_title);
        toolbarTitle.setText("Complaint Detail");

        layoutStaticComplaint = (LinearLayout) findViewById(R.id.layout_complaint_static);

        etComplaintId = (EditText) findViewById(R.id.et_complaint_id);
        etMobileNo = (EditText) findViewById(R.id.et_mobile_no);
        etKeyword = (EditText) findViewById(R.id.et_keyword);
        complaintList = (ListView) findViewById(R.id.complaintList);
        cardDetails = (CardView) findViewById(R.id.card_details);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        no = (TextView) findViewById(R.id.txtNo);
        cmpDate = (TextView) findViewById(R.id.txtDate);
        city = (TextView) findViewById(R.id.txtCity);
        name = (TextView) findViewById(R.id.txtName);
        mobile = (TextView) findViewById(R.id.txtMobile);
        address = (TextView) findViewById(R.id.txtAddress);
        product = (TextView) findViewById(R.id.txtProduct);
        billDate = (TextView) findViewById(R.id.txtBillDate);
        dealer = (TextView) findViewById(R.id.txtDealer);
        problem = (TextView) findViewById(R.id.txtProblem);
        pending = (TextView) findViewById(R.id.txtPending);


        btnSearch = (TextView) findViewById(R.id.btn_search);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (HelperMethods.isNetworkAvailable(MoreAboutComplaint.this)) {
                    validate = validation();
                    if (validate) {
                        getComplaints();
                    }
                } else {
                    GlobalVariables.defaultOneButtonDialog(MoreAboutComplaint.this, "No Data Connection");

                }
            }
        });


        if(getIntent().getExtras()!=null) {
            no.setText(getSetComplaint.getNo().toString());
            cmpDate.setText(getSetComplaint.getCmpDate().toString());
            city.setText(getSetComplaint.getCity().toString());
            name.setText(getSetComplaint.getName().toString());
            mobile.setText(getSetComplaint.getMobile().toString());
            address.setText(getSetComplaint.getAddress().toString());
            product.setText(getSetComplaint.getProduct());
            billDate.setText(getSetComplaint.getBillDate().toString());
            dealer.setText(getSetComplaint.getDealer().toString());
            problem.setText(getSetComplaint.getProblem().toString());
            pending.setText(getSetComplaint.getPending().toString());


        }else {
            cardDetails.setVisibility(View.GONE);
        }
    }

    public boolean validation() {
        if (etMobileNo.getText().toString().isEmpty() && etComplaintId.getText().toString().isEmpty() && etKeyword.getText().toString().isEmpty()) {
            GlobalVariables.defaultOneButtonDialog(MoreAboutComplaint.this, "Complaint ID or Mobile no or Keyword is required");
            return false;
        } else if (!etMobileNo.getText().toString().isEmpty() && etMobileNo.getText().toString().trim().length() != 10) {
            GlobalVariables.defaultOneButtonDialog(MoreAboutComplaint.this, "Enter Correct Mobile No");
            return false;
        } else {
            return true;
        }

    }

    public void getComplaints() {
        GlobalVariables.hideSoftKeyboard(MoreAboutComplaint.this);
        String complaintId = etComplaintId.getText().toString();
        String mobileNo = etMobileNo.getText().toString();
        String keyword = etKeyword.getText().toString().toUpperCase();
        SharedPreferences sharedPreferences = getSharedPreferences(Constant.PREFS_NAME, MODE_PRIVATE);

        jsonObjRequest = new JSONObject();
        try {
            jsonObjRequest.put("method", "complaint_filter");
            jsonObjRequest.put("comp_id", complaintId);
            jsonObjRequest.put("mobile_number", mobileNo);
            jsonObjRequest.put("keyword", keyword);
            jsonObjRequest.put("user_id", sharedPreferences.getString(Constant.Sp_GrtPlusUserID, ""));


        } catch (JSONException e) {
            e.printStackTrace();
        }

        myCustomAsyncTask.showDialog(MoreAboutComplaint.this);
        new MyCustomAsyncTask(MoreAboutComplaint.this, jsonObjRequest, Constant.WS_URL + Constant.WS_FILTER_COMPLAINT_LIST).execute();
    }


    @Override
    public void onTaskComplete(String result) {
        myCustomAsyncTask.hideDialog(MoreAboutComplaint.this);
        if (result != null || result.length() > 0) {
            try {
                cardDetails.setVisibility(View.GONE);
                complaintList.setVisibility(View.VISIBLE);
                Gson gson = new Gson();
                ComplaintListModel complaintListModel = gson.fromJson(result, ComplaintListModel.class);
                if (complaintListModel.getStatus().equalsIgnoreCase("200")) {
                    GlobalVariables.hideSoftKeyboard(MoreAboutComplaint.this);
                    complaints = complaintListModel.getData();
                    complaintListAdapter = new ComplaintListAdapter(MoreAboutComplaint.this, complaints);
                    complaintList.setAdapter(complaintListAdapter);
                    complaintListAdapter.notifyDataSetChanged();

                } else {
                    GlobalVariables.defaultOneButtonDialog(MoreAboutComplaint.this, complaintListModel.getMsg());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }


}
