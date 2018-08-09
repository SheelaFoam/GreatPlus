package com.erp.sheelafoam;

/**
 * Created by twigz on 28/2/18.
 */

import android.content.Intent;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.erp.sheelafoam.adapter.EmployeeAdapter;
import com.erp.sheelafoam.adapter.StoreAdapter;
import com.erp.sheelafoam.adapter.StoreListAdapter;
import com.erp.sheelafoam.constant.Constant;
import com.erp.sheelafoam.models.EmpDetails;
import com.erp.sheelafoam.models.ImageResponseBody;
import com.erp.sheelafoam.models.StoreList;
import com.erp.sheelafoam.models.StoreListModel;
import com.erp.sheelafoam.sheelafoam.xperia.DealerAdapter;
import com.erp.sheelafoam.sheelafoam.xperia.activity.LogComplaintActivity;
import com.erp.sheelafoam.utils.CommonClass;
import com.erp.sheelafoam.utils.Util;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by admin on 19-02-2018.
 */

public class UploadActivity extends BaseActivity implements View.OnClickListener {
    private ImageView ivImage;
    private Button btnUpload;
    private String filePath = "";
    private CommonClass commonClass;
    private Spinner spnStores;
    private TextView emp_name, storeName;

    private RelativeLayout progressBar;
    private File imgFile;
    String uID, token;
    List<String> list = new ArrayList<>();

    EmployeeAdapter employeeAdapter;
    StoreListAdapter storeListAdapter;

    List<EmpDetails> empDetails;
    List<StoreList> storeLists = new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        commonClass = new CommonClass(this);
        progressBar = (RelativeLayout) findViewById(R.id.progress_bar);
        storeName = (TextView) findViewById(R.id.store_name);

        getEmployee();
        Intent intent = getIntent();
        filePath = intent.getStringExtra("LoadURL");
        System.out.println("File path "+filePath);
        //spnStores = (Spinner) findViewById(R.id.spnStores);
        ivImage = (ImageView) findViewById(R.id.ivImage);
        btnUpload = (Button) findViewById(R.id.btnUpload);
        emp_name = (TextView) findViewById(R.id.emp_name);


        emp_name.setOnClickListener(this);
        storeName.setOnClickListener(this);

        btnUpload.setOnClickListener(this);

        //spnStores.setAdapter(new StoreAdapter(this, list));

//        list.add("Select Store");
//        list.add("Lajpat Nagar");
//        list.add("Nehru Place");
//        list.add("Saket");
//        list.add("Noida Sector 15");

        imgFile = new File(filePath);
//        if(imgFile.exists()){
//            Bitmap bitmap  = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
//            ivImage.setImageBitmap(bitmap);
//
//        }
        Picasso.with(this).load(imgFile).into(ivImage);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.emp_name:
                showEmployeeDialog();
                break;

            case R.id.btnUpload:
                if (isValid()) {
                    RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), imgFile);
                    System.out.println("file name " + imgFile.getName());
                    MultipartBody.Part body = MultipartBody.Part.createFormData("file", imgFile.getName(), requestFile);
                    RequestBody description = RequestBody.create(MediaType.parse("multipart/form-data"), uID);
                    requestBackground(API.uploadImage(uID, token, storeName.getText().toString(), body, description), "Uploading...");
                }
                break;
            case R.id.store_name:
                if (storeLists.isEmpty()) {
                    commonClass.showToast("Selected employee has no store");
                }else {
                    showStoreListDialog();
                }

                break;
                default:
                    break;

        }




    }


    public void requestBackground(Call request, String msg) {
        commonClass.showProgress(msg, false);
        request.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                commonClass.disMissProgress();
                ImageResponseBody body = (ImageResponseBody) response.body();
                commonClass.showToast("" + body.getMessage());
                if (body.getSuccess()) {
                    UploadActivity.this.finish();
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                commonClass.disMissProgress();
                commonClass.showToast("Unable to upload Image. Please try again");
            }
        });
    }

    private boolean isValid() {
        if (emp_name.getText().toString().equalsIgnoreCase("Employee Name") || emp_name.getText().toString().equalsIgnoreCase("")) {
            commonClass.showToast("Please select the employee");
            return false;
        }
        if(storeName.getText().toString().equalsIgnoreCase("Store") || storeName.getText().toString().equalsIgnoreCase("")) {
            commonClass.showToast("Please select the store");
            return false;
        }
        if (!CommonClass.checkInternetConnection(this)) {
            commonClass.showToast("Please check your internet connectivity.");
            return false;
        }
        return true;
    }

    private void getEmployee() {
        progressBar.setVisibility(View.VISIBLE);
        uID = Util.getSharedPrefrenceValue(this, Constant.Sp_UserID);
        token = Util.getSharedPrefrenceValue(this, Constant.Sp_UserToken);
        Log.i("url->>", Constant.EMPLOYEE_API+uID+"/"+token);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constant.EMPLOYEE_API + uID + "/" + token,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressBar.setVisibility(View.GONE);
                        if(response != "") {
                            try {
                                Log.d("RESPONSE EMP", response);
                                Gson gson = new Gson();
                                StoreListModel storeListModel = gson.fromJson(response, StoreListModel.class);
                                empDetails = storeListModel.getData();

                            }catch (Exception e) {
                                e.printStackTrace();
                            }
                        }


                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    AlertDialog empAlertDialog;
    private void showEmployeeDialog() {

        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(UploadActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.complaint_type_alert_dilaog, null);

        ListView empListView = (ListView) dialogView.findViewById(R.id.complaint_list);
        TextView heading = (TextView) dialogView.findViewById(R.id.heading);
        heading.setText("Employee Name");

        employeeAdapter = new EmployeeAdapter(this, empDetails);
        empListView.setAdapter(employeeAdapter);
        employeeAdapter.notifyDataSetChanged();
        empListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                emp_name.setText(empDetails.get(i).geteMP_NAME());
                storeName.setText("Store");
                storeLists = empDetails.get(i).getStoreList();




                empAlertDialog.dismiss();

            }
        });


        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(true);

        empAlertDialog = dialogBuilder.create();
        empAlertDialog.show();

    }
    AlertDialog storeAlertDialog;
    private void showStoreListDialog() {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(UploadActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.complaint_type_alert_dilaog, null);

        ListView empListView = (ListView) dialogView.findViewById(R.id.complaint_list);
        TextView heading = (TextView) dialogView.findViewById(R.id.heading);
        heading.setText("Store");

        storeListAdapter = new StoreListAdapter(this, storeLists);
        empListView.setAdapter(storeListAdapter);
        storeListAdapter.notifyDataSetChanged();
        empListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                storeName.setText(storeLists.get(i).getpARENT_CHANNEL_PARTNER_NAME());

                storeAlertDialog.dismiss();

            }
        });


        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(true);

        storeAlertDialog = dialogBuilder.create();
        storeAlertDialog.show();
    }


}
