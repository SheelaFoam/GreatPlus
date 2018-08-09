package com.erp.sheelafoam.sheelafoam.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.erp.sheelafoam.R;
import com.erp.sheelafoam.adapter.GuarenteeLogAdapter;
import com.erp.sheelafoam.constant.Constant;
import com.erp.sheelafoam.interfaces.INotifyGuaranteeLog;
import com.erp.sheelafoam.model.GuaranteeLogModel;
import com.erp.sheelafoam.utils.CustomTypefaceEditText;
import com.erp.sheelafoam.utils.CustomTypefaceTextView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by E5956 on 4/24/2018.
 */

public class GuaranteeLog extends AppCompatActivity implements INotifyGuaranteeLog {
    private List<GuaranteeLogModel> guaranteeList;
    private RecyclerView recycler_guaranteeLog;
    private Toolbar toolbar;
    private CustomTypefaceEditText et_search;
    private CustomTypefaceTextView app_toolbar_title;
    private GuarenteeLogAdapter guarenteeLogAdapter;
    private LinearLayoutManager layoutManager;
    private SharedPreferences loginPref;
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.guarantee_log_recycler);
        super.onCreate(savedInstanceState);
        progress = new ProgressDialog(this);
        toolbar = (Toolbar) findViewById(R.id.app_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        et_search = (CustomTypefaceEditText) findViewById(R.id.et_search);
        app_toolbar_title = (CustomTypefaceTextView) findViewById(R.id.app_toolbar_title);
        app_toolbar_title.setText("Guarantee Logs");
        loginPref = getSharedPreferences(Constant.PREFS_NAME, Context.MODE_PRIVATE);

        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (guaranteeList.size() > 0) {
                    String find = charSequence.toString();
                    ArrayList<GuaranteeLogModel> temp = setsearchlist(find);
                    guarenteeLogAdapter = new GuarenteeLogAdapter(GuaranteeLog.this, temp);
                    recycler_guaranteeLog.setAdapter(guarenteeLogAdapter);
                    VerticalSpaceItemDecoration dividerItemDecoration = new VerticalSpaceItemDecoration(20);
                    recycler_guaranteeLog.addItemDecoration(dividerItemDecoration);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        recycler_guaranteeLog = (RecyclerView) findViewById(R.id.recycler_guaranteeLog);

        callWebService();
    }

    public ArrayList<GuaranteeLogModel> setsearchlist(String find) {
        ArrayList<GuaranteeLogModel> list = new ArrayList<>();
        list.clear();
        String findWord = find.toLowerCase();
        for (GuaranteeLogModel guarateeModel : guaranteeList) {
            if (guarateeModel.getSERIALNUMBER().toString().toLowerCase().contains(findWord)) {
                list.add(guarateeModel);
            }
        }
        return list;
    }

    private Response callWebService() {

        progress = new ProgressDialog(GuaranteeLog.this);

        progress.setMessage("Please wait....");

        progress.setCancelable(false);
        progress.show();
        Response response = null;
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW");
        RequestBody body = RequestBody.create(mediaType, "------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; name=\"p_user_id\"\r\n\r\n"
                + loginPref.getString(Constant.Sp_UserID, "") + "\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW--");
        Request request = new Request.Builder()
                .url("http://greatplus.com/warranty_log_api/prevSevenDays.php")
                .post(body)
                .addHeader("content-type", "multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW")
                .addHeader("cache-control", "no-cache")
                .addHeader("postman-token", "b3b4e8da-1419-c65e-5ba6-db6e79e2704d")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
            }

            @Override
            public void onResponse(Response response) throws IOException {
                progress.dismiss();
                Type typeToken = new TypeToken<List<GuaranteeLogModel>>() {
                }.getType();
                Gson gson = new Gson();
                try {
                    guaranteeList = gson.fromJson(response.body().string(), typeToken);
                    System.out.println("From do in  background: " + response.body().string());
                    System.out.println("Size of list: " + guaranteeList.size());
                    GuaranteeLog.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            onServiceResponse(guaranteeList);

                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("Web service message: " + response.message() + "  Body: " + response.body().string());
            }
        });
        return response;
    }

    @Override
    public void onServiceResponse(List<GuaranteeLogModel> guaranteeLogModelList) {
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recycler_guaranteeLog.setLayoutManager(layoutManager);
        guarenteeLogAdapter = new GuarenteeLogAdapter(GuaranteeLog.this, guaranteeLogModelList);
        recycler_guaranteeLog.setAdapter(guarenteeLogAdapter);
        VerticalSpaceItemDecoration dividerItemDecoration = new VerticalSpaceItemDecoration(20);
        recycler_guaranteeLog.addItemDecoration(dividerItemDecoration);
    }

    public class VerticalSpaceItemDecoration extends RecyclerView.ItemDecoration {

        private final int verticalSpaceHeight;

        public VerticalSpaceItemDecoration(int verticalSpaceHeight) {
            this.verticalSpaceHeight = verticalSpaceHeight;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                                   RecyclerView.State state) {
            if (parent.getChildAdapterPosition(view) != parent.getAdapter().getItemCount() - 1) {
                outRect.bottom = verticalSpaceHeight;
            }
        }
    }
}
