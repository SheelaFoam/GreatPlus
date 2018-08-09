package com.erp.sheelafoam;

import android.content.Context;

import com.google.gson.Gson;
import com.erp.sheelafoam.asynctask.NetworkTask;
import com.erp.sheelafoam.constant.Constant;
import com.erp.sheelafoam.model.SubmitPollsModel;

import org.json.JSONObject;

/**
 * Created by priyanka.sharma on 12/6/2016.
 */

public class BaseUrlApi implements NetworkTask.Result {
    private String UserID, UserEmail, UserToken;
    String PageTitle, GreatPlusUserID, OP_USER_ROLENAME, ModelName, Device_id, currentVer, CurrentLocationLat_str, CurrentLocationLng_str;
    Context context;

    public BaseUrlApi() {
        callWSUserLog();
    }

    private void callWSUserLog() {
        try {
            JSONObject obj = new JSONObject();

            String url = Constant.WS_URL + Constant.WS_USER_LOG;

            NetworkTask networkTask = new NetworkTask(context, 1, obj.toString());
            networkTask.setProgressDialog(false);
            networkTask.exposePostExecute(this);
            networkTask.execute(url);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void resultfromNetwork(String object, int id, int arg1, String arg2) {
        Gson gson_ = new Gson();
        SubmitPollsModel base_ = gson_.fromJson(object, SubmitPollsModel.class);
        if (base_.getInfo().get(0).getStatus() == 1) {
        } else {
        }
    }
}
