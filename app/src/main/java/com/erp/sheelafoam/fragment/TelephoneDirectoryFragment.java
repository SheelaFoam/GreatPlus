package com.erp.sheelafoam.fragment;


import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.erp.sheelafoam.HomeScreen;
import android.Manifest;
import com.erp.sheelafoam.R;
import com.erp.sheelafoam.UserLogAPI;
import com.erp.sheelafoam.adapter.TelephoneAdapter;
import com.erp.sheelafoam.asynctask.NetworkTask;
import com.erp.sheelafoam.constant.Constant;
import com.erp.sheelafoam.model.TelephoneModel;
import com.erp.sheelafoam.utils.AlertDialogManager;
import com.erp.sheelafoam.utils.ConnectionDetector;
import com.erp.sheelafoam.utils.Util;

import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class TelephoneDirectoryFragment extends Fragment implements NetworkTask.Result {


    RecyclerView recList;
    TelephoneAdapter objTelephoneAdapter;
    AppCompatSpinner spnRecord;
    TelephoneModel base;
    AutoCompleteTextView etSearch;
    ConnectionDetector cDetector;
    AlertDialogManager alert;
    private boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    int pageNo = 1;
    TextView search_txt;
    int listType = -1;
    String searchStr, UserID, GreatPlusUserID, OP_USER_ROLENAME, UserToken, UserAuthType;
    RelativeLayout no_data_layout;

    private final int RequestCallPermissionCode = 13;
    public TelephoneDirectoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.telephone_directory_activity, container, false);
        new UserLogAPI("Telephone Directory Page", getActivity());
        init(view);
        listType = -1;

        ((HomeScreen) getActivity()).txt_title.setText("Telephone Directory");
        ((HomeScreen) getActivity()).profile_image.setVisibility(View.GONE);
        ((HomeScreen) getActivity()).txt_title.setVisibility(View.VISIBLE);

        callWS();
        EnableRuntimeCallPermission();

       /* etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (s.toString().trim().length() > 1) {
                    listType = 0;
                    pageNo = 1;
                    searchStr = s.toString();
                    callWSSearch(s.toString());
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });*/
        return view;
    }

    private void init(View view) {
        cDetector = new ConnectionDetector(getActivity());
        alert = new AlertDialogManager();
        getSharedPreferenceValues();
        spnRecord = (AppCompatSpinner) view.findViewById(R.id.spn_records);
        no_data_layout = (RelativeLayout) view.findViewById(R.id.no_data_layout);
        recList = (RecyclerView) view.findViewById(R.id.telephone_listingss);
        search_txt = (TextView) view.findViewById(R.id.search_txt);
        etSearch = (AutoCompleteTextView) view.findViewById(R.id.directory_search);
        search_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(etSearch.getWindowToken(), 0);
                listType = 0;
                pageNo = 1;
                searchStr = etSearch.getText().toString();
                if (searchStr.isEmpty()) {
                    callWS();
                }else {
                    callWSSearch(searchStr);
                }

            }
        });
        recList.setHasFixedSize(true);

        ((HomeScreen) getActivity()).txt_title.setText("Telephone directory");

        final LinearLayoutManager llm;
        llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.records));
        spnRecord.setAdapter(adapter);

        recList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                visibleItemCount = llm.getChildCount();
                totalItemCount = llm.getItemCount();
                pastVisiblesItems = llm.findFirstVisibleItemPosition();

                if (loading) {
                    if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {

                        //mProgressBar.setVisibility(View.VISIBLE);
                        loading = false;
                        pageNo += 1;
                        Log.v("...", "Last Item Wow !");
                        try {
                            if (listType == -1)
                                callWSScroll(pageNo);
                            else
                                callWSSearchMore(searchStr);
                            // mProgressBar.setVisibility(View.GONE);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

    }


    private void callWS() {
        if (Util.isInternetConnection(getActivity())) {
            try {
                JSONObject obj = new JSONObject();
                obj.put("uid", UserID);
                obj.put("mode", "list");
                obj.put("page", "1");
                obj.put("token", UserToken);
                obj.put("auth_type", UserAuthType);
                obj.put("op_greatplus_user_id", GreatPlusUserID);
                obj.put("op_user_role_name", OP_USER_ROLENAME);

                //https://www.greatplus.com/service/telephone-directory.php
                String url = Constant.WS_URL + Constant.WS_TELEPHONE;
                NetworkTask networkTask = new NetworkTask(getActivity(), 1, obj.toString());
                networkTask.setDialogMessage("Loading...");
                networkTask.exposePostExecute(this);
                networkTask.execute(url);

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void callWSSearch(String keyword) {
        if (Util.isInternetConnection(getActivity())) {
            try {
                JSONObject obj = new JSONObject();//
                obj.put("uid", UserID);
                obj.put("mode", "search");
                obj.put("page", "1");
                obj.put("keyword", keyword);
                obj.put("auth_type", UserAuthType);
                obj.put("token", UserToken);
                obj.put("op_greatplus_user_id", GreatPlusUserID);
                obj.put("op_user_role_name", OP_USER_ROLENAME);

                String url = Constant.WS_URL + Constant.WS_TELEPHONE;
                NetworkTask networkTask = new NetworkTask(getActivity(), 1, obj.toString());
                networkTask.setDialogMessage("Loading...");
                networkTask.exposePostExecute(this);
                networkTask.execute(url);

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void callWSSearchMore(String keyword) {
        if (Util.isInternetConnection(getActivity())) {
            try {
                JSONObject obj = new JSONObject();//
                obj.put("uid", UserID);
                obj.put("mode", "search");
                obj.put("auth_type", UserAuthType);
                obj.put("page", pageNo);
                obj.put("token", UserToken);
                obj.put("keyword", keyword);
                obj.put("op_greatplus_user_id", GreatPlusUserID);
                obj.put("op_user_role_name", OP_USER_ROLENAME);

                String url = Constant.WS_URL + Constant.WS_TELEPHONE;
                NetworkTask networkTask = new NetworkTask(getActivity(), 2, obj.toString());
                networkTask.setDialogMessage("Loading...");
                networkTask.exposePostExecute(this);
                networkTask.execute(url);

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void getSharedPreferenceValues() {
        UserID = Util.getSharedPrefrenceValue(getActivity(), Constant.Sp_UserID);
        GreatPlusUserID = Util.getSharedPrefrenceValue(getActivity(), Constant.Sp_GrtPlusUserID);
        OP_USER_ROLENAME = Util.getSharedPrefrenceValue(getActivity(), Constant.Sp_op_user_role_name);
        UserToken = Util.getSharedPrefrenceValue(getActivity(), Constant.Sp_UserToken);
        UserAuthType = Util.getSharedPrefrenceValue(getActivity(), Constant.Sp_AuthType);

    }

    private void callWSScroll(int pageNo) {
        if (!cDetector.isConnectingToInternet())
            alert.showAlertDialog(getActivity(), "Network Connection", "Enable Your Internet Connection", false);
        else {
            try {
                JSONObject obj = new JSONObject();
                obj.put("uid", UserID);
                obj.put("mode", "list");
                obj.put("auth_type", UserAuthType);
                obj.put("token", UserToken);
                obj.put("page", pageNo);
                obj.put("op_greatplus_user_id", GreatPlusUserID);
                obj.put("op_user_role_name", OP_USER_ROLENAME);

                String url = Constant.WS_URL + Constant.WS_TELEPHONE;
                NetworkTask networkTask = new NetworkTask(getActivity(), 2, obj.toString());
                networkTask.setDialogMessage("Loading...");
                networkTask.exposePostExecute(this);
                networkTask.execute(url);

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void resultfromNetwork(String object, int id, int arg1, String arg2) {
        switch (id) {
            case 1: {
                Gson gson = new Gson();
                base = gson.fromJson(object, TelephoneModel.class);

                if (base.getInfo().size() > 0) {
                    recList.setVisibility(View.VISIBLE);
                    no_data_layout.setVisibility(View.GONE);
                    objTelephoneAdapter = new TelephoneAdapter(getActivity(), base.getInfo(), base.getInfo().size());
                    recList.setAdapter(objTelephoneAdapter);

                } else {
                    recList.setVisibility(View.GONE);
                    no_data_layout.setVisibility(View.VISIBLE);
                }

                spnRecord.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        if (i == 0) {
                            objTelephoneAdapter = new TelephoneAdapter(getActivity(), base.getInfo(), base.getInfo().size());
                            recList.setAdapter(objTelephoneAdapter);
                        } else if (i == 1) {
                            objTelephoneAdapter = new TelephoneAdapter(getActivity(), base.getInfo(), 5);
                            recList.setAdapter(objTelephoneAdapter);
                        } else if (i == 2) {
                            objTelephoneAdapter = new TelephoneAdapter(getActivity(), base.getInfo(), 10);
                            recList.setAdapter(objTelephoneAdapter);
                        } else if (i == 3) {
                            objTelephoneAdapter = new TelephoneAdapter(getActivity(), base.getInfo(), 25);
                            recList.setAdapter(objTelephoneAdapter);
                        } else if (i == 4) {
                            objTelephoneAdapter = new TelephoneAdapter(getActivity(), base.getInfo(), 100);
                            recList.setAdapter(objTelephoneAdapter);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
                break;
            }
            case 2: {
                Gson gson = new Gson();
                TelephoneModel base1 = gson.fromJson(object, TelephoneModel.class);

                if (base1.getInfo().size() > 0) {
                    this.base.getInfo().addAll(base1.getInfo());
                    objTelephoneAdapter = new TelephoneAdapter(getActivity(), base.getInfo(), base.getInfo().size());
                    recList.setAdapter(objTelephoneAdapter);
                    loading = true;
                }
                break;
            }
        }
    }

    public void EnableRuntimeCallPermission() {

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CALL_PHONE)) {

                // Printing toast messa
                //
                //
                // ge after enabling runtime permission.
                //Toast.makeText(MainActivity.this,"CAMERA permission allows us to Access CAMERA app", Toast.LENGTH_LONG).show();

            } else {

                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, RequestCallPermissionCode);

            }
        }
        else {

        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case RequestCallPermissionCode:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    //Toast.makeText(RequestListActivity.this,"Permission Granted, Now your application can access CAMERA.", Toast.LENGTH_LONG).show();

                } else {


                }
                break;
        }
    }
}
