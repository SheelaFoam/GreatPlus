package com.erp.sheelafoam.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.erp.sheelafoam.R;
import com.erp.sheelafoam.adapter.CompanyPerformanceAdapter;
import com.erp.sheelafoam.asynctask.NetworkTask;
import com.erp.sheelafoam.constant.Constant;
import com.erp.sheelafoam.model.CompanyPerformanceModel;
import com.erp.sheelafoam.utils.AlertDialogManager;
import com.erp.sheelafoam.utils.ConnectionDetector;
import com.erp.sheelafoam.utils.Util;

import org.json.JSONObject;

/**
 * Created by priyanka.sharma on 12/2/2016.
 */

public class CompanyPerformance_ThisWeek extends Fragment implements NetworkTask.Result {
    RecyclerView comany_this_week;
    RelativeLayout no_data_layout;
    LinearLayoutManager layoutManager;
    ConnectionDetector cDetector;
    AlertDialogManager alert;
    String GrtPlusUSerID, OP_USER_ROLENAME, UserToken, UserID,UserAuthType;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.company_thisweek_fragment
                , container, false);

        init(view);

        return view;
    }

    private void init(View view) {
        getSharedPreferenceValues();
        comany_this_week = (RecyclerView) view.findViewById(R.id.comany_this_week);

        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        comany_this_week.setHasFixedSize(true);
        comany_this_week.setLayoutManager(layoutManager);

        callWSCompanyPerformance();
    }

    private void getSharedPreferenceValues() {
        GrtPlusUSerID = Util.getSharedPrefrenceValue(getActivity(), Constant.Sp_GrtPlusUserID);
        OP_USER_ROLENAME = Util.getSharedPrefrenceValue(getActivity(), Constant.Sp_op_user_role_name);
        UserID = Util.getSharedPrefrenceValue(getActivity(), Constant.Sp_UserID);
        UserToken = Util.getSharedPrefrenceValue(getActivity(), Constant.Sp_UserToken);
        UserAuthType = Util.getSharedPrefrenceValue(getActivity(), Constant.Sp_AuthType);
    }

    private void callWSCompanyPerformance() {
        cDetector = new ConnectionDetector(getActivity());
        alert = new AlertDialogManager();
        if (!cDetector.isConnectingToInternet())
            alert.showAlertDialog(getActivity(), "Network Connection", "Enable Your Internet Connection", false);
        else {
            try {
                JSONObject obj = new JSONObject();

                obj.put("mode", "company_performance");
                obj.put("op_greatplus_user_id", GrtPlusUSerID);
                obj.put("op_user_role_name", OP_USER_ROLENAME);
                obj.put("uid", UserID);
                obj.put("auth_type", UserAuthType);
                obj.put("token", UserToken);

                String url = Constant.WS_URL + Constant.WS_SIDEMENU;
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
        Gson gson = new Gson();
        CompanyPerformanceModel companyPerformanceModel = gson.fromJson(object, CompanyPerformanceModel.class);

        if (companyPerformanceModel.getInfo().size() > 0) {
            CompanyPerformanceAdapter companyPerformanceAdapter = new CompanyPerformanceAdapter(getActivity(), companyPerformanceModel.getInfo());
            comany_this_week.setAdapter(companyPerformanceAdapter);
        }
    }
}
