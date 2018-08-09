package com.erp.sheelafoam.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.google.gson.Gson;
import com.erp.sheelafoam.HomeScreen;
import com.erp.sheelafoam.R;
import com.erp.sheelafoam.UserLogAPI;
import com.erp.sheelafoam.adapter.PerformanceAdapter;
import com.erp.sheelafoam.asynctask.NetworkTask;
import com.erp.sheelafoam.constant.Constant;
import com.erp.sheelafoam.model.MyPerformanceMainModel;
import com.erp.sheelafoam.utils.AlertDialogManager;
import com.erp.sheelafoam.utils.ConnectionDetector;
import com.erp.sheelafoam.utils.Util;

import org.json.JSONObject;

/**
 * Created by priyanka.sharma on 11/24/2016.
 */

public class MyPerformanceFragment extends Fragment implements NetworkTask.Result {
    ExpandableListView performance_listing;
    ConnectionDetector cDetector;
    AlertDialogManager alert;
    String emp_grpCode, GrtPlusUSerID, OP_USER_ROLENAME, UserToken, UserID, UserAuthType;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sidemenu_myperformance_layout
                , container, false);
        new UserLogAPI("My Performance Page", getActivity());
        init(view);

        return view;
    }

    private void init(View view) {
        cDetector = new ConnectionDetector(getActivity());
        alert = new AlertDialogManager();
        getSharedPreferenceValues();

        ((HomeScreen) getActivity()).txt_title.setText("My Performance");
        ((HomeScreen) getActivity()).profile_image.setVisibility(View.GONE);
        ((HomeScreen) getActivity()).txt_title.setVisibility(View.VISIBLE);

        performance_listing = (ExpandableListView) view.findViewById(R.id.performance_listing);
        callWSPerformnace();
    }

    private void getSharedPreferenceValues() {
        emp_grpCode = Util.getSharedPrefrenceValue(getActivity(), Constant.Sp_emp_grpCode);
        GrtPlusUSerID = Util.getSharedPrefrenceValue(getActivity(), Constant.Sp_GrtPlusUserID);
        OP_USER_ROLENAME = Util.getSharedPrefrenceValue(getActivity(), Constant.Sp_op_user_role_name);
        UserID = Util.getSharedPrefrenceValue(getActivity(), Constant.Sp_UserID);
        UserToken = Util.getSharedPrefrenceValue(getActivity(), Constant.Sp_UserToken);
        UserAuthType = Util.getSharedPrefrenceValue(getActivity(), Constant.Sp_AuthType);
    }

    private void callWSPerformnace() {
        if (!cDetector.isConnectingToInternet())
            alert.showAlertDialog(getActivity(), "Network Connection", "Enable Your Internet Connection", false);
        else {
            try {
                JSONObject obj = new JSONObject();

                obj.put("op_user_emp_group_code", emp_grpCode);
                obj.put("uid", UserID);
                obj.put("token", UserToken);
                obj.put("auth_type", UserAuthType);
                obj.put("op_default_fin_year", "2016-2017");
                obj.put("mode", "my_performance");
                obj.put("op_greatplus_user_id", GrtPlusUSerID);
                obj.put("op_user_role_name", OP_USER_ROLENAME);

                String url = Constant.WS_URL + Constant.WS_SIDEMENU;
                NetworkTask networkTask = new NetworkTask(getActivity(), 1, obj.toString());
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
        Gson gson = new Gson();
        MyPerformanceMainModel base = gson.fromJson(object, MyPerformanceMainModel.class);
        PerformanceAdapter objEventsListAdapter = new PerformanceAdapter(getActivity(), base.getInfo(), performance_listing);
        performance_listing.setAdapter(objEventsListAdapter);
    }
}