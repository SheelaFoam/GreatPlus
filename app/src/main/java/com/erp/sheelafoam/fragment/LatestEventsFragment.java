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
import com.erp.sheelafoam.adapter.LatestEventsAdapter;
import com.erp.sheelafoam.asynctask.NetworkTask;
import com.erp.sheelafoam.constant.Constant;
import com.erp.sheelafoam.model.LAtestEventsModel;
import com.erp.sheelafoam.utils.AlertDialogManager;
import com.erp.sheelafoam.utils.ConnectionDetector;
import com.erp.sheelafoam.utils.Util;

import org.json.JSONObject;

/**
 * Created by priyanka.sharma on 12/2/2016.
 */

public class LatestEventsFragment extends Fragment implements NetworkTask.Result {
    RecyclerView latest_event_listing;
    RelativeLayout no_data_layout;
    LinearLayout main_latestevent_layout;
    LinearLayoutManager layoutManager;
    ConnectionDetector cDetector;
    AlertDialogManager alert;
    private String USerEmail, GrtPlusUSerID, OP_USER_ROLENAME, UserToken, UserAuthType, UserID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.side_latestevents
                , container, false);
        new UserLogAPI("Latest Event Page", getActivity());
        init(view);

        return view;
    }

    private void init(View view) {
        cDetector = new ConnectionDetector(getActivity());
        alert = new AlertDialogManager();

        ((HomeScreen) getActivity()).txt_title.setText("Latest Events");
        ((HomeScreen) getActivity()).profile_image.setVisibility(View.GONE);
        ((HomeScreen) getActivity()).txt_title.setVisibility(View.VISIBLE);

        latest_event_listing = (RecyclerView) view.findViewById(R.id.latest_event_listing);
        no_data_layout = (RelativeLayout) view.findViewById(R.id.no_data_layout);
        main_latestevent_layout = (LinearLayout) view.findViewById(R.id.main_latestevent_layout);

        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        latest_event_listing.setHasFixedSize(true);
        latest_event_listing.setLayoutManager(layoutManager);

        getSharedPreferenceValues();
        callWSLatestEvents();
    }

    private void getSharedPreferenceValues() {
        USerEmail = Util.getSharedPrefrenceValue(getActivity(), Constant.Sp_USerEmail);
        GrtPlusUSerID = Util.getSharedPrefrenceValue(getActivity(), Constant.Sp_GrtPlusUserID);
        OP_USER_ROLENAME = Util.getSharedPrefrenceValue(getActivity(), Constant.Sp_op_user_role_name);
        UserID = Util.getSharedPrefrenceValue(getActivity(), Constant.Sp_UserID);
        UserToken = Util.getSharedPrefrenceValue(getActivity(), Constant.Sp_UserToken);
        UserAuthType = Util.getSharedPrefrenceValue(getActivity(), Constant.Sp_AuthType);
    }

    private void callWSLatestEvents() {
        if (!cDetector.isConnectingToInternet())
            alert.showAlertDialog(getActivity(), "Network Connection", "Enable Your Internet Connection", false);
        else {
            try {
                JSONObject obj = new JSONObject();

                obj.put("user_email", USerEmail);
                obj.put("uid", UserID);
                obj.put("token", UserToken);
                obj.put("auth_type", UserAuthType);
                obj.put("mode", "lastes_event");
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
        LAtestEventsModel lAtestEventsModel = gson.fromJson(object, LAtestEventsModel.class);

        if (lAtestEventsModel.getInfo().size() > 0) {
            main_latestevent_layout.setVisibility(View.VISIBLE);
            no_data_layout.setVisibility(View.GONE);

            LatestEventsAdapter latestEventsAdapter = new LatestEventsAdapter(getActivity(), lAtestEventsModel.getInfo());
            latest_event_listing.setAdapter(latestEventsAdapter);
        } else {
            main_latestevent_layout.setVisibility(View.GONE);
            no_data_layout.setVisibility(View.VISIBLE);
        }
    }

}
