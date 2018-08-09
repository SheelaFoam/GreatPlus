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
import com.erp.sheelafoam.adapter.TaskAdapterSideMenu;
import com.erp.sheelafoam.asynctask.NetworkTask;
import com.erp.sheelafoam.constant.Constant;
import com.erp.sheelafoam.model.TaskModel;
import com.erp.sheelafoam.utils.AlertDialogManager;
import com.erp.sheelafoam.utils.ConnectionDetector;
import com.erp.sheelafoam.utils.Util;

import org.json.JSONObject;

/**
 * Created by priyanka.sharma on 12/1/2016.
 */

public class MyTask extends Fragment implements NetworkTask.Result {
    LinearLayout main_task_layout;
    RecyclerView task_listing;
    RelativeLayout no_data_layout;
    LinearLayoutManager layoutManager;
    ConnectionDetector cDetector;
    AlertDialogManager alert;
    private String GreatPlusUserID, OP_USER_ROLENAME, UserID, UserToken, UserDisplayName, UserAuthType;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sidemenu_mytask
                , container, false);
        new UserLogAPI("My Tasks Page", getActivity());
        init(view);

        return view;
    }

    private void init(View view) {
        cDetector = new ConnectionDetector(getActivity());
        alert = new AlertDialogManager();
        getSharedPreferenceValues();

        ((HomeScreen) getActivity()).txt_title.setText("My Task");
        ((HomeScreen) getActivity()).profile_image.setVisibility(View.GONE);
        ((HomeScreen) getActivity()).txt_title.setVisibility(View.VISIBLE);

        task_listing = (RecyclerView) view.findViewById(R.id.task_listing);
        no_data_layout = (RelativeLayout) view.findViewById(R.id.no_data_layout);
        main_task_layout = (LinearLayout) view.findViewById(R.id.main_task_layout);
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        task_listing.setHasFixedSize(true);
        task_listing.setLayoutManager(layoutManager);

        callWSTask();
    }

    private void getSharedPreferenceValues() {
        GreatPlusUserID = Util.getSharedPrefrenceValue(getActivity(), Constant.Sp_GrtPlusUserID);
        OP_USER_ROLENAME = Util.getSharedPrefrenceValue(getActivity(), Constant.Sp_op_user_role_name);
        UserID = Util.getSharedPrefrenceValue(getActivity(), Constant.Sp_UserID);
        UserToken = Util.getSharedPrefrenceValue(getActivity(), Constant.Sp_UserToken);
        UserDisplayName = Util.getSharedPrefrenceValue(getActivity(), Constant.Sp_DisplayName);
        UserAuthType = Util.getSharedPrefrenceValue(getActivity(), Constant.Sp_AuthType);
    }

    private void callWSTask() {
        if (!cDetector.isConnectingToInternet())
            alert.showAlertDialog(getActivity(), "Network Connection", "Enable Your Internet Connection", false);
        else {
            try {
                JSONObject obj = new JSONObject();

                obj.put("op_greatplus_user_id", GreatPlusUserID);
                obj.put("mode", "task");
                obj.put("op_user_role_name", OP_USER_ROLENAME);
                obj.put("uid", UserID);
                obj.put("auth_type", UserAuthType);
                obj.put("token", UserToken);
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
        TaskModel taskModel = gson.fromJson(object, TaskModel.class);

        if (taskModel.getInfo().size() > 0) {
            main_task_layout.setVisibility(View.VISIBLE);
            no_data_layout.setVisibility(View.GONE);

            TaskAdapterSideMenu taskAdapter = new TaskAdapterSideMenu(getActivity(), taskModel.getInfo());
            task_listing.setAdapter(taskAdapter);
        } else {
            main_task_layout.setVisibility(View.GONE);
            no_data_layout.setVisibility(View.VISIBLE);
        }
    }
}
