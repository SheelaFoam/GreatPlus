package com.erp.sheelafoam.fragment;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.erp.sheelafoam.HomeScreen;
import com.erp.sheelafoam.R;
import com.erp.sheelafoam.UserLogAPI;
import com.erp.sheelafoam.adapter.LearningListingAdapter;
import com.erp.sheelafoam.asynctask.NetworkTask;
import com.erp.sheelafoam.constant.Constant;
import com.erp.sheelafoam.model.LearningRefModel;
import com.erp.sheelafoam.utils.AlertDialogManager;
import com.erp.sheelafoam.utils.ConnectionDetector;
import com.erp.sheelafoam.utils.RecyclerItemClickListener;
import com.erp.sheelafoam.utils.Util;

import org.json.JSONObject;

/**
 * Created by priyanka.sharma on 11/30/2016.
 */

public class LearningRef_Fragment extends Fragment implements NetworkTask.Result {
    RecyclerView learning_listing;
    LinearLayoutManager layoutManager;
    ConnectionDetector cDetector;
    AlertDialogManager alert;
    String UserID, UserEmail, GrtPlusUSerID, OP_USER_ROLENAME, UserToken, UserAuthType;
    LearningRefModel base;
    RelativeLayout no_data_layout, learning_relative;
    CoordinatorLayout learning_crl;
    private String Flow_From;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.learning_reference_activity
                , container, false);

        init(view);

        learning_listing.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Bundle in = new Bundle();
                        in.putString("list_id", base.getInfo().get(position).getId());
                        in.putString("Flow_from", Flow_From);
                        ((HomeScreen) getActivity()).commonFragmentMethod(new LearningRef_DetailFragment(), in, null);
                    }
                }));

        return view;
    }

    private void init(View view) {
        Bundle bundle = getArguments();
        if (bundle != null) {
            Flow_From = getArguments().getString("Flow_from");

        }

        cDetector = new ConnectionDetector(getActivity());
        alert = new AlertDialogManager();

        getSharedPreferenceValues();

        learning_listing = (RecyclerView) view.findViewById(R.id.learning_listing);
        learning_crl = (CoordinatorLayout) view.findViewById(R.id.learning_crl);
        learning_relative = (RelativeLayout) view.findViewById(R.id.learning_relative);
        no_data_layout = (RelativeLayout) view.findViewById(R.id.no_data_layout);
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        learning_listing.setHasFixedSize(true);
        learning_listing.setLayoutManager(layoutManager);

        callWSLearningRef();
    }

    private void callWSLearningRef() {
        if (!cDetector.isConnectingToInternet())
            alert.showAlertDialog(getActivity(), "Network Connection", "Enable Your Internet Connection", false);
        else {
            try {
                JSONObject obj = new JSONObject();
                obj.put("user_email", UserEmail);
                obj.put("uid", UserID);
                obj.put("mode", "list");
                obj.put("auth_type", UserAuthType);
                obj.put("token", UserToken);
                obj.put("op_greatplus_user_id", GrtPlusUSerID);
                obj.put("op_user_role_name", OP_USER_ROLENAME);

                String url;
                if (Flow_From.equalsIgnoreCase("learning")) {
                    new UserLogAPI("Learning References Page", getActivity());
                    url = Constant.WS_URL + Constant.WS_LEARNING_REF;
                    ((HomeScreen) getActivity()).txt_title.setText("Learning References");
                    ((HomeScreen) getActivity()).profile_image.setVisibility(View.GONE);
                    ((HomeScreen) getActivity()).txt_title.setVisibility(View.VISIBLE);
                } else {
                    new UserLogAPI("Policies & Procedures Page", getActivity());
                    url = Constant.WS_URL + Constant.WS_POLICIES;
                    ((HomeScreen) getActivity()).txt_title.setText("Policies & Procedures");
                    ((HomeScreen) getActivity()).profile_image.setVisibility(View.GONE);
                    ((HomeScreen) getActivity()).txt_title.setVisibility(View.VISIBLE);
                }
                NetworkTask networkTask = new NetworkTask(getActivity(), 1, obj.toString());
                networkTask.setDialogMessage("Loading...");
                networkTask.exposePostExecute(this);
                networkTask.execute(url);

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void getSharedPreferenceValues() {
        UserID = Util.getSharedPrefrenceValue(getActivity(), Constant.Sp_UserID);
        UserEmail = Util.getSharedPrefrenceValue(getActivity(), Constant.Sp_USerEmail);
        GrtPlusUSerID = Util.getSharedPrefrenceValue(getActivity(), Constant.Sp_GrtPlusUserID);
        OP_USER_ROLENAME = Util.getSharedPrefrenceValue(getActivity(), Constant.Sp_op_user_role_name);
        UserAuthType = Util.getSharedPrefrenceValue(getActivity(), Constant.Sp_AuthType);
        UserToken = Util.getSharedPrefrenceValue(getActivity(), Constant.Sp_UserToken);
    }

    @Override
    public void resultfromNetwork(String object, int id, int arg1, String arg2) {
        Gson gson = new Gson();
        base = gson.fromJson(object, LearningRefModel.class);

        if (base.getStatus() == 1) {
            if (base.getInfo().size() > 0) {
                no_data_layout.setVisibility(View.GONE);
                learning_relative.setVisibility(View.VISIBLE);
                LearningListingAdapter learningListingAdapter = new LearningListingAdapter(getActivity(), base.getInfo());
                learning_listing.setAdapter(learningListingAdapter);
            } else {
                no_data_layout.setVisibility(View.VISIBLE);
                learning_relative.setVisibility(View.GONE);
            }
        } else {
            no_data_layout.setVisibility(View.VISIBLE);
            learning_relative.setVisibility(View.GONE);
            //   Util.showSnackbar(getActivity(), learning_crl, base.getMsg());
        }
    }
}