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
import com.erp.sheelafoam.adapter.UpcomingHoliday_Adapter;
import com.erp.sheelafoam.asynctask.NetworkTask;
import com.erp.sheelafoam.constant.Constant;
import com.erp.sheelafoam.model.UpcomingHolidayModel;
import com.erp.sheelafoam.utils.AlertDialogManager;
import com.erp.sheelafoam.utils.ConnectionDetector;
import com.erp.sheelafoam.utils.Util;

import org.json.JSONObject;

/**
 * Created by priyanka.sharma on 12/1/2016.
 */

public class UpcomingHolidaysFragment extends Fragment implements NetworkTask.Result {
    LinearLayout main_holiday_layout;
    RecyclerView holiday_listing;
    RelativeLayout no_data_layout;
    LinearLayoutManager layoutManager;
    ConnectionDetector cDetector;
    AlertDialogManager alert;
    String GrtPlusUserID, OP_USER_ROLENAME, UserID, UserToken, UserAuthType;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.side_upcoming_holiday
                , container, false);
        new UserLogAPI("Upcoming Holiday Page", getActivity());
        init(view);

        return view;
    }

    private void init(View view) {
        cDetector = new ConnectionDetector(getActivity());
        alert = new AlertDialogManager();
        getSharedPreferenceValues();

        ((HomeScreen) getActivity()).txt_title.setVisibility(View.VISIBLE);
        ((HomeScreen) getActivity()).profile_image.setVisibility(View.GONE);
        ((HomeScreen) getActivity()).txt_title.setText("Upcoming Holidays");

        holiday_listing = (RecyclerView) view.findViewById(R.id.holiday_listing);
        no_data_layout = (RelativeLayout) view.findViewById(R.id.no_data_layout);
        main_holiday_layout = (LinearLayout) view.findViewById(R.id.main_holiday_layout);
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        holiday_listing.setHasFixedSize(true);
        holiday_listing.setLayoutManager(layoutManager);

        callWSAppointment();

    }

    private void getSharedPreferenceValues() {
        GrtPlusUserID = Util.getSharedPrefrenceValue(getActivity(), Constant.Sp_GrtPlusUserID);
        OP_USER_ROLENAME = Util.getSharedPrefrenceValue(getActivity(), Constant.Sp_op_user_role_name);
        UserID = Util.getSharedPrefrenceValue(getActivity(), Constant.Sp_UserID);
        UserToken = Util.getSharedPrefrenceValue(getActivity(), Constant.Sp_UserToken);
        UserAuthType = Util.getSharedPrefrenceValue(getActivity(), Constant.Sp_AuthType);
    }

    private void callWSAppointment() {
        if (!cDetector.isConnectingToInternet())
            alert.showAlertDialog(getActivity(), "Network Connection", "Enable Your Internet Connection", false);
        else {
            try {
                JSONObject obj = new JSONObject();

                obj.put("op_greatplus_user_id", GrtPlusUserID);
                obj.put("mode", "holiday_list");
                obj.put("op_user_zone", "NORTH");
                obj.put("uid", UserID);
                obj.put("auth_type", UserAuthType);
                obj.put("token", UserToken);
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
        UpcomingHolidayModel upcomingHolidayModel = gson.fromJson(object, UpcomingHolidayModel.class);

        if (upcomingHolidayModel.getInfo().size() > 0) {
            main_holiday_layout.setVisibility(View.VISIBLE);
            no_data_layout.setVisibility(View.GONE);

            UpcomingHoliday_Adapter upcomingHoliday_adapter = new UpcomingHoliday_Adapter(getActivity(), upcomingHolidayModel.getInfo());
            holiday_listing.setAdapter(upcomingHoliday_adapter);
        } else {
            main_holiday_layout.setVisibility(View.GONE);
            no_data_layout.setVisibility(View.VISIBLE);
        }
    }
}
