package com.erp.sheelafoam.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.erp.sheelafoam.HomeScreen;
import com.erp.sheelafoam.R;
import com.erp.sheelafoam.UserLogAPI;
import com.erp.sheelafoam.adapter.MyAppointment_Adapter;
import com.erp.sheelafoam.asynctask.NetworkTask;
import com.erp.sheelafoam.constant.Constant;
import com.erp.sheelafoam.model.AppointmentModel;
import com.erp.sheelafoam.utils.AlertDialogManager;
import com.erp.sheelafoam.utils.ConnectionDetector;
import com.erp.sheelafoam.utils.Util;

import org.json.JSONObject;

/**
 * Created by priyanka.sharma on 12/1/2016.
 */

public class MyAppointments extends Fragment implements NetworkTask.Result {
    LinearLayout appointment_layout, main_appointment_layout;
    RecyclerView my_appointment_listing;
    RelativeLayout no_data_layout;
    LinearLayoutManager layoutManager;
    ConnectionDetector cDetector;
    AlertDialogManager alert;
    String UserID, UserToken, GrtPlusUSerID, OP_USER_ROLENAME, UserAuthType;
    private Toolbar toolbar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sidemenu_appointment
                , container, false);
        new UserLogAPI("My Appointmnts Page", getActivity());
        init(view);

        return view;
    }

    private void init(View view) {
        cDetector = new ConnectionDetector(getActivity());
        alert = new AlertDialogManager();
        getSharedPreferenceValues();

        ((HomeScreen) getActivity()).txt_title.setText("My Appointments");
        ((HomeScreen) getActivity()).txt_title.setVisibility(View.VISIBLE);
        ((HomeScreen) getActivity()).profile_image.setVisibility(View.GONE);

        my_appointment_listing = (RecyclerView) view.findViewById(R.id.my_appointment_listing);
        no_data_layout = (RelativeLayout) view.findViewById(R.id.no_data_layout);
        main_appointment_layout = (LinearLayout) view.findViewById(R.id.main_appointment_layout);
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        my_appointment_listing.setHasFixedSize(true);
        my_appointment_listing.setLayoutManager(layoutManager);

        callWSAppointment();

    }

    private void getSharedPreferenceValues() {
        UserID = Util.getSharedPrefrenceValue(getActivity(), Constant.Sp_UserID);
        UserToken = Util.getSharedPrefrenceValue(getActivity(), Constant.Sp_UserToken);
        GrtPlusUSerID = Util.getSharedPrefrenceValue(getActivity(), Constant.Sp_GrtPlusUserID);
        OP_USER_ROLENAME = Util.getSharedPrefrenceValue(getActivity(), Constant.Sp_op_user_role_name);
        UserAuthType = Util.getSharedPrefrenceValue(getActivity(), Constant.Sp_AuthType);
    }

    private void callWSAppointment() {
        if (!cDetector.isConnectingToInternet())
            alert.showAlertDialog(getActivity(), "Network Connection", "Enable Your Internet Connection", false);
        else {
            try {
                JSONObject obj = new JSONObject();

                obj.put("uid", UserID);
                obj.put("token", UserToken);
                obj.put("mode", "appointments");
                obj.put("auth_type", UserAuthType);
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
        AppointmentModel appointmentModel = gson.fromJson(object, AppointmentModel.class);

        if (appointmentModel.getInfo().size() > 0) {
            main_appointment_layout.setVisibility(View.VISIBLE);
            no_data_layout.setVisibility(View.GONE);

            MyAppointment_Adapter myAppointment_adapter = new MyAppointment_Adapter(getActivity(), appointmentModel.getInfo());
            my_appointment_listing.setAdapter(myAppointment_adapter);
        } else {
            main_appointment_layout.setVisibility(View.GONE);
            no_data_layout.setVisibility(View.VISIBLE);
        }
    }
}
