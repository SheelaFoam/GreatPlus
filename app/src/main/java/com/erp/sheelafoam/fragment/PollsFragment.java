package com.erp.sheelafoam.fragment;


import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.erp.sheelafoam.HomeScreen;
import com.erp.sheelafoam.R;
import com.erp.sheelafoam.UserLogAPI;
import com.erp.sheelafoam.adapter.YourOpinionAdapter;
import com.erp.sheelafoam.asynctask.NetworkTask;
import com.erp.sheelafoam.constant.Constant;
import com.erp.sheelafoam.model.PollsModel;
import com.erp.sheelafoam.model.SubmitPollsModel;
import com.erp.sheelafoam.utils.AlertDialogManager;
import com.erp.sheelafoam.utils.ConnectionDetector;
import com.erp.sheelafoam.utils.Util;

import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class PollsFragment extends Fragment implements View.OnClickListener, NetworkTask.Result {

    RecyclerView opinion_listing;
    RelativeLayout your_opinion_layout;
    Button btnSubmitVote;
    LinearLayoutManager layoutManager;
    CoordinatorLayout opinion_crl;
    ConnectionDetector cDetector;
    AlertDialogManager alert;
    String UserID, UserToken, UserEmail, GreatPlusUserID, OP_USER_ROLENAME, UserAuthType;
    TextView question;
    YourOpinionAdapter yourOpinionAdapter;

    PollsModel base_;
    RelativeLayout no_data_layout;

    public PollsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_polls, container, false);
        new UserLogAPI("Your Opinion Page", getActivity());
        init(view);
        getSharedPreferenceValues();
        callWSStepSecond();
        return view;
    }

    private void init(View view) {

        cDetector = new ConnectionDetector(getActivity());
        alert = new AlertDialogManager();

        ((HomeScreen) getActivity()).txt_title.setVisibility(View.VISIBLE);
        ((HomeScreen) getActivity()).txt_title.setText("Your Opinion");
        ((HomeScreen) getActivity()).profile_image.setVisibility(View.GONE);


        opinion_listing = (RecyclerView) view.findViewById(R.id.opinion_listing);
        opinion_crl = (CoordinatorLayout) view.findViewById(R.id.opinion_crl);
        btnSubmitVote = (Button) view.findViewById(R.id.submit_vote);
        btnSubmitVote.setOnClickListener(this);

        question = (TextView) view.findViewById(R.id.question);
        no_data_layout = (RelativeLayout) view.findViewById(R.id.no_data_layout);
        your_opinion_layout = (RelativeLayout) view.findViewById(R.id.no_data_layout);

        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        opinion_listing.setHasFixedSize(true);
        opinion_listing.setLayoutManager(layoutManager);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit_vote:
                if (yourOpinionAdapter.mSelectedItem != -1) {
                    String id = yourOpinionAdapter.optionID.get("ID");
                    callWSSubmitVote(id);
                }
        }
    }


    private void callWSSubmitVote(String id) {
        if (!cDetector.isConnectingToInternet())
            alert.showAlertDialog(getActivity(), "Network Connection", "Enable Your Internet Connection", false);
        else {
            try {
                JSONObject obj = new JSONObject();
                obj.put("user_id", UserID);
                obj.put("email", UserEmail);
                obj.put("action", "submit_poll");
                obj.put("id_polls", base_.getInfo().get(0).getId_polls());
                obj.put("id_poll_options", id);
                obj.put("auth_type", UserAuthType);
                obj.put("op_greatplus_user_id", GreatPlusUserID);
                obj.put("token", UserToken);
                obj.put("op_user_role_name", OP_USER_ROLENAME);


                String url = Constant.WS_URL + Constant.WS_OPTION_SUBMIT;
                NetworkTask networkTask = new NetworkTask(getActivity(), 3, obj.toString());
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
        UserToken = Util.getSharedPrefrenceValue(getActivity(), Constant.Sp_UserToken);
        UserEmail = Util.getSharedPrefrenceValue(getActivity(), Constant.Sp_USerEmail);
        GreatPlusUserID = Util.getSharedPrefrenceValue(getActivity(), Constant.Sp_GrtPlusUserID);
        OP_USER_ROLENAME = Util.getSharedPrefrenceValue(getActivity(), Constant.Sp_op_user_role_name);
        UserAuthType = Util.getSharedPrefrenceValue(getActivity(), Constant.Sp_AuthType);
    }


    private void callWSStepSecond() {
        if (!cDetector.isConnectingToInternet())
            alert.showAlertDialog(getActivity(), "Network Connection", "Enable Your Internet Connection", false);
        else {
            try {
                JSONObject obj = new JSONObject();
                obj.put("uid", UserID);
                obj.put("auth_type", UserAuthType);
                obj.put("mode", "polls");
                obj.put("token", UserToken);
                obj.put("user_email", UserEmail);
                obj.put("op_greatplus_user_id", GreatPlusUserID);
                obj.put("op_user_role_name", OP_USER_ROLENAME);

                String url = Constant.WS_URL + Constant.WS_SIDEMENU;
                NetworkTask networkTask = new NetworkTask(getActivity(), 2, obj.toString());
                //  networkTask.setDialogMessage("Loading...");
                networkTask.exposePostExecute(this);
                networkTask.execute(url);

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void resultfromNetwork(String object, int id, int arg1, String arg2) {

        switch (id) {
            case 2: {
                Gson gson_ = new Gson();
                base_ = gson_.fromJson(object, PollsModel.class);

                if (base_.getInfo().size() > 0) {
                    question.setText(base_.getInfo().get(0).getName());
                    yourOpinionAdapter = new YourOpinionAdapter(getActivity(), base_.getInfo());
                    opinion_listing.setAdapter(yourOpinionAdapter);

                    if (base_.getInfo().get(0).getPolls_option_answer().size() > 0) {
                        btnSubmitVote.setVisibility(View.GONE);
                    }
                }

                break;
            }
            case 3:
                Gson gson_ = new Gson();
                SubmitPollsModel base_ = gson_.fromJson(object, SubmitPollsModel.class);
                if (base_.getInfo().get(0).getStatus() == 1) {
                    Util.showSnackbar(getActivity(), opinion_crl, base_.getInfo().get(0).getMsg());
                } else {
                    Util.showSnackbar(getActivity(), opinion_crl, base_.getInfo().get(0).getMsg());
                }
                break;
        }
    }
}
