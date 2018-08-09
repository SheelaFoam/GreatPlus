package com.erp.sheelafoam.fragment;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.erp.sheelafoam.HomeScreen;
import com.erp.sheelafoam.R;
import com.erp.sheelafoam.UserLogAPI;
import com.erp.sheelafoam.asynctask.NetworkTask;
import com.erp.sheelafoam.constant.Constant;
import com.erp.sheelafoam.model.ChangePasswordModel;
import com.erp.sheelafoam.utils.AlertDialogManager;
import com.erp.sheelafoam.utils.ConnectionDetector;
import com.erp.sheelafoam.utils.Util;

import org.json.JSONObject;

/**
 * Created by priyanka.sharma on 12/13/2016.
 */

public class ChangePassword extends Fragment implements NetworkTask.Result, View.OnClickListener {
    ConnectionDetector cDetector;
    AlertDialogManager alert;
    private String GreatPlusUserID, OP_USER_ROLENAME, strOldPassword, strNewPassword, strCnfrmPassword, UserID, UserToken,
            UserAuthType;
    EditText ed_old_password, ed_new_password, ed_cnfrm_password;
    Button btn_submit;
    CoordinatorLayout chnge_password_crl;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.change_password_activity
                , container, false);
        new UserLogAPI("Change Password Page", getActivity());
        init(view);

        return view;
    }

    private void init(View view) {
        cDetector = new ConnectionDetector(getActivity());
        alert = new AlertDialogManager();

        ((HomeScreen) getActivity()).txt_title.setText("Change Password");
        ((HomeScreen) getActivity()).profile_image.setVisibility(View.GONE);
        ((HomeScreen) getActivity()).txt_title.setVisibility(View.VISIBLE);

        getSharedPreferenceValues();

        chnge_password_crl = (CoordinatorLayout) view.findViewById(R.id.chnge_password_crl);
        ed_old_password = (EditText) view.findViewById(R.id.ed_old_password);
        ed_new_password = (EditText) view.findViewById(R.id.ed_new_password);
        ed_cnfrm_password = (EditText) view.findViewById(R.id.ed_cnfrm_password);
        btn_submit = (Button) view.findViewById(R.id.btn_submit);

        btn_submit.setOnClickListener(this);
    }

    private void getSharedPreferenceValues() {
        GreatPlusUserID = Util.getSharedPrefrenceValue(getActivity(), Constant.Sp_GrtPlusUserID);
        OP_USER_ROLENAME = Util.getSharedPrefrenceValue(getActivity(), Constant.Sp_op_user_role_name);
        UserID = Util.getSharedPrefrenceValue(getActivity(), Constant.Sp_UserID);
        UserToken = Util.getSharedPrefrenceValue(getActivity(), Constant.Sp_UserToken);
        UserAuthType = Util.getSharedPrefrenceValue(getActivity(), Constant.Sp_AuthType);
    }

    @Override
    public void resultfromNetwork(String object, int id, int arg1, String arg2) {
        Gson gson = new Gson();
        ChangePasswordModel base = gson.fromJson(object, ChangePasswordModel.class);

        if (base.getInfo().get(0).getStatus() == 1) {
            Util.showSnackbar(getActivity(), chnge_password_crl, base.getInfo().get(0).getMsg());//Sp_userProfileImage

            Util.setSharedPrefrenceValue(getActivity(), Constant.PREFS_NAME, Constant.Sp_UserToken, base.getInfo().get(0).getToken());

        } else {
            Util.showSnackbar(getActivity(), chnge_password_crl, base.getInfo().get(0).getMsg());
        }
    }

    @Override
    public void onClick(View v) {
        if (validate()) {
            callWSChangePassword();
        }
    }

    private void callWSChangePassword() {
        if (!cDetector.isConnectingToInternet())
            alert.showAlertDialog(getActivity(), "Network Connection", "Enable Your Internet Connection", false);
        else {
            try {
                JSONObject obj = new JSONObject();
                obj.put("op_greatplus_user_id", GreatPlusUserID);
                obj.put("old_password", strOldPassword);
                obj.put("new_password", strNewPassword);
                obj.put("confirm_password", strCnfrmPassword);
                obj.put("uid", UserID);
                obj.put("token", UserToken);
                obj.put("auth_type", UserAuthType);
                obj.put("mode", "change_password");
                obj.put("op_user_role_name", OP_USER_ROLENAME);

                String url = Constant.WS_URL + Constant.WS_CHANGE_PASSWORD;
                NetworkTask networkTask = new NetworkTask(getActivity(), 1, obj.toString());
                networkTask.setDialogMessage("Please Wait...");
                networkTask.exposePostExecute(this);
                networkTask.execute(url);

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private boolean validate() {
        boolean flag = true;

        strCnfrmPassword = ed_cnfrm_password.getText().toString();
        strNewPassword = ed_new_password.getText().toString();
        strOldPassword = ed_old_password.getText().toString();

        if (strOldPassword.trim().length() == 0 || strNewPassword.trim().length() == 0 || strCnfrmPassword.trim().length() == 0) {
            Util.showSnackbar(getActivity(), chnge_password_crl, "Please fill all values!!");
            flag = false;
        } else if (!strNewPassword.equals(strCnfrmPassword)) {
            Util.showSnackbar(getActivity(), chnge_password_crl, "Password mismatch!!");
            flag = false;
        }

        return flag;
    }
}