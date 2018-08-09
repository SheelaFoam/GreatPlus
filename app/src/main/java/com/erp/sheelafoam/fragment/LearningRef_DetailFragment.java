package com.erp.sheelafoam.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.erp.sheelafoam.HeaderMenuDesc_Web;
import com.erp.sheelafoam.R;
import com.erp.sheelafoam.UserLogAPI;
import com.erp.sheelafoam.adapter.LearningDetailAdapter;
import com.erp.sheelafoam.asynctask.NetworkTask;
import com.erp.sheelafoam.constant.Constant;
import com.erp.sheelafoam.model.LearningRefDetailModel;
import com.erp.sheelafoam.utils.AlertDialogManager;
import com.erp.sheelafoam.utils.ConnectionDetector;
import com.erp.sheelafoam.utils.RecyclerItemClickListener;
import com.erp.sheelafoam.utils.Util;

import org.json.JSONObject;

/**
 * Created by priyanka.sharma on 11/30/2016.
 */

public class LearningRef_DetailFragment extends Fragment implements NetworkTask.Result {
    RecyclerView learning_deatail_listing;
    LinearLayoutManager layoutManager;
    ConnectionDetector cDetector;
    AlertDialogManager alert;
    String list_ID, Flow_From;
    LearningRefDetailModel base;
    CoordinatorLayout learning_detail_crl;
    String UserID, UserEmail, GrtPlusUSerID, OP_USER_ROLENAME, UserToken, UserAuthType;
    RelativeLayout no_data, learning_layout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.learning_ref_detail_activity
                , container, false);

        init(view);

        learning_deatail_listing.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        Log.d("Hello", ""+position);
                        Intent in = new Intent(getActivity(), HeaderMenuDesc_Web.class);
                        in.putExtra("WebUrl", base.getInfo().get(position).getLink());
                        getActivity().startActivity(in);


                    }
                }));

        return view;
    }

    private void init(View view) {
        cDetector = new ConnectionDetector(getActivity());
        alert = new AlertDialogManager();
        getSharedPreferenceValues();
        Bundle bundle = getArguments();
        if (bundle != null) {
            list_ID = getArguments().getString("list_id");
            Flow_From = getArguments().getString("Flow_from");
        }

        no_data = (RelativeLayout) view.findViewById(R.id.no_data);
        learning_layout = (RelativeLayout) view.findViewById(R.id.learning_layout);
        learning_deatail_listing = (RecyclerView) view.findViewById(R.id.learning_deatail_listing);
        learning_detail_crl = (CoordinatorLayout) view.findViewById(R.id.learning_detail_crl);

        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        learning_deatail_listing.setHasFixedSize(true);
        learning_deatail_listing.setLayoutManager(layoutManager);

        callWSLearningRefDetail();
    }

    private void getSharedPreferenceValues() {
        UserID = Util.getSharedPrefrenceValue(getActivity(), Constant.Sp_UserID);
        UserEmail = Util.getSharedPrefrenceValue(getActivity(), Constant.Sp_USerEmail);
        GrtPlusUSerID = Util.getSharedPrefrenceValue(getActivity(), Constant.Sp_GrtPlusUserID);
        OP_USER_ROLENAME = Util.getSharedPrefrenceValue(getActivity(), Constant.Sp_op_user_role_name);
        UserToken = Util.getSharedPrefrenceValue(getActivity(), Constant.Sp_UserToken);
        UserAuthType = Util.getSharedPrefrenceValue(getActivity(), Constant.Sp_AuthType);
    }

    private void callWSLearningRefDetail() {
        if (!cDetector.isConnectingToInternet())
            alert.showAlertDialog(getActivity(), "Network Connection", "Enable Your Internet Connection", false);
        else {
            try {
                String url;
                JSONObject obj = new JSONObject();
                obj.put("user_email", UserEmail);
                obj.put("uid", UserID);
                obj.put("mode", "detail");
                obj.put("token", UserToken);
                obj.put("auth_type", UserAuthType);
                obj.put("id", list_ID);
                obj.put("op_greatplus_user_id", GrtPlusUSerID);
                obj.put("op_user_role_name", OP_USER_ROLENAME);

                if (Flow_From.equalsIgnoreCase("learning")) {
                    new UserLogAPI("Learning References Detail Page", getActivity());
                    url = Constant.WS_URL + Constant.WS_LEARNING_REF;
                } else {
                    new UserLogAPI("Policies & Procedures Detail Page", getActivity());
                    url = Constant.WS_URL + Constant.WS_POLICIES;
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

    @Override
    public void resultfromNetwork(String object, int id, int arg1, String arg2) {
        Gson gson = new Gson();
        base = gson.fromJson(object, LearningRefDetailModel.class);

        if (base.getStatus() == 1) {
            if (base.getInfo().size() > 0) {
                learning_layout.setVisibility(View.VISIBLE);
                no_data.setVisibility(View.GONE);
                LearningDetailAdapter learningListingAdapter = new LearningDetailAdapter(getActivity(), base.getInfo());
                learning_deatail_listing.setAdapter(learningListingAdapter);
            } else {
                no_data.setVisibility(View.VISIBLE);
                learning_layout.setVisibility(View.GONE);
            }
        } else {
            no_data.setVisibility(View.VISIBLE);
            learning_layout.setVisibility(View.GONE);
            Util.showSnackbar(getActivity(), learning_detail_crl, base.getMsg());
        }
    }
}
