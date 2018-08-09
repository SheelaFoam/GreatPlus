package com.erp.sheelafoam.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.erp.sheelafoam.HomeScreen;
import com.erp.sheelafoam.R;
import com.erp.sheelafoam.UserLogAPI;
import com.erp.sheelafoam.adapter.ApproveTaskAdapter;
import com.erp.sheelafoam.asynctask.NetworkTask;
import com.erp.sheelafoam.constant.Constant;
import com.erp.sheelafoam.model.ApproveTaskModel;
import com.erp.sheelafoam.utils.AlertDialogManager;
import com.erp.sheelafoam.utils.ConnectionDetector;
import com.erp.sheelafoam.utils.Util;

import org.json.JSONObject;

/**
 * Created by priyanka.sharma on 12/1/2016.
 */

public class ApproveMyTask extends Fragment implements NetworkTask.Result {
    LinearLayout main_task_layout;
    RecyclerView approve_task_listing;
    RelativeLayout no_data_layout;
    LinearLayoutManager layoutManager;
    ConnectionDetector cDetector;
    AlertDialogManager alert;
    private String GreatPlusUserID, OP_USER_ROLENAME, UserID, UserDisplayName, UserToken, UserAuthType;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.approve_mytask
                , container, false);
        new UserLogAPI("Approve Tasks Page", getActivity());
        init(view);

        return view;
    }

    private void init(View view) {
        cDetector = new ConnectionDetector(getActivity());
        alert = new AlertDialogManager();
        getSharedPreferenceValues();

        ((HomeScreen) getActivity()).txt_title.setText("Approve Tasks");
        ((HomeScreen) getActivity()).profile_image.setVisibility(View.GONE);
        ((HomeScreen) getActivity()).txt_title.setVisibility(View.VISIBLE);

        approve_task_listing = (RecyclerView) view.findViewById(R.id.approve_task_listing);
        no_data_layout = (RelativeLayout) view.findViewById(R.id.no_data_layout);
        main_task_layout = (LinearLayout) view.findViewById(R.id.main_task_layout);
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        approve_task_listing.setHasFixedSize(true);
        approve_task_listing.setLayoutManager(layoutManager);

        callWSTask();
    }

    private void getSharedPreferenceValues() {
        GreatPlusUserID = Util.getSharedPrefrenceValue(getActivity(), Constant.Sp_GrtPlusUserID);
        OP_USER_ROLENAME = Util.getSharedPrefrenceValue(getActivity(), Constant.Sp_op_user_role_name);
        UserID = Util.getSharedPrefrenceValue(getActivity(), Constant.Sp_UserID);
        UserDisplayName = Util.getSharedPrefrenceValue(getActivity(), Constant.Sp_DisplayName);
        UserToken = Util.getSharedPrefrenceValue(getActivity(), Constant.Sp_UserToken);
        UserAuthType = Util.getSharedPrefrenceValue(getActivity(), Constant.Sp_AuthType);
    }

    private void callWSTask() {
        if (!cDetector.isConnectingToInternet())
            alert.showAlertDialog(getActivity(), "Network Connection", "Enable Your Internet Connection", false);
        else {
            try {
                JSONObject obj = new JSONObject();

                obj.put("op_greatplus_user_id", GreatPlusUserID);
                obj.put("mode", "approve_task");
                obj.put("op_user_role_name", OP_USER_ROLENAME);
                obj.put("uid", UserID);
                obj.put("token", UserToken);
                obj.put("auth_type", UserAuthType);
                obj.put("displayname", UserDisplayName);

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
        ApproveTaskModel taskModel = gson.fromJson(object, ApproveTaskModel.class);

        if (taskModel.getInfo().size() > 0) {
            main_task_layout.setVisibility(View.VISIBLE);
            no_data_layout.setVisibility(View.GONE);

            ApproveTaskAdapter taskAdapter = new ApproveTaskAdapter(getActivity(), taskModel.getInfo());
            approve_task_listing.setAdapter(taskAdapter);
        } else {
            main_task_layout.setVisibility(View.GONE);
            no_data_layout.setVisibility(View.VISIBLE);
        }
    }
}
