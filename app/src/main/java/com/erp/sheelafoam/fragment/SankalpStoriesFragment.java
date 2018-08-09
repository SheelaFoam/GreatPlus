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
import com.erp.sheelafoam.adapter.SankalpStoriesAdapter;
import com.erp.sheelafoam.asynctask.NetworkTask;
import com.erp.sheelafoam.constant.Constant;
import com.erp.sheelafoam.model.SankalpStoriesModel;
import com.erp.sheelafoam.utils.AlertDialogManager;
import com.erp.sheelafoam.utils.ConnectionDetector;
import com.erp.sheelafoam.utils.Util;

import org.json.JSONObject;

/**
 * Created by priyanka.sharma on 12/2/2016.
 */

public class SankalpStoriesFragment extends Fragment implements NetworkTask.Result {
    RecyclerView sankalp_stories_listing;
    RelativeLayout no_data_layout;
    LinearLayout main_sankalp_layout;
    LinearLayoutManager layoutManager;
    ConnectionDetector cDetector;
    AlertDialogManager alert;
    private String UserID, UserToken, GreatPlusUserID, OP_USER_ROLENAME, UserAuthType;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.side_sankalpstories
                , container, false);
        new UserLogAPI("Sankalp Stories Page", getActivity());
        init(view);

        return view;
    }

    private void init(View view) {
        cDetector = new ConnectionDetector(getActivity());
        alert = new AlertDialogManager();

        getSharedPreferenceValues();
        ((HomeScreen) getActivity()).txt_title.setText("Sankalp Stories");
        ((HomeScreen) getActivity()).profile_image.setVisibility(View.GONE);
        ((HomeScreen) getActivity()).txt_title.setVisibility(View.VISIBLE);

        sankalp_stories_listing = (RecyclerView) view.findViewById(R.id.sankalp_stories_listing);
        no_data_layout = (RelativeLayout) view.findViewById(R.id.no_data_layout);
        main_sankalp_layout = (LinearLayout) view.findViewById(R.id.main_sankalp_layout);

        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        sankalp_stories_listing.setHasFixedSize(true);
        sankalp_stories_listing.setLayoutManager(layoutManager);

        callWSLatestEvents();
    }

    private void getSharedPreferenceValues() {
        GreatPlusUserID = Util.getSharedPrefrenceValue(getActivity(), Constant.Sp_GrtPlusUserID);
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

                obj.put("mode", "sankalp_stories");
                obj.put("auth_type", UserAuthType);
                obj.put("op_greatplus_user_id", GreatPlusUserID);
                obj.put("op_user_role_name", OP_USER_ROLENAME);
                obj.put("uid", UserID);
                obj.put("token", UserToken);

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
        SankalpStoriesModel sankalpStoriesModel = gson.fromJson(object, SankalpStoriesModel.class);

        if (sankalpStoriesModel.getInfo().size() > 0) {
            main_sankalp_layout.setVisibility(View.VISIBLE);
            no_data_layout.setVisibility(View.GONE);

            SankalpStoriesAdapter sankalpStoriesAdapter = new SankalpStoriesAdapter(getActivity(), sankalpStoriesModel.getInfo());
            sankalp_stories_listing.setAdapter(sankalpStoriesAdapter);
        } else {
            main_sankalp_layout.setVisibility(View.GONE);
            no_data_layout.setVisibility(View.VISIBLE);
        }
    }

}
