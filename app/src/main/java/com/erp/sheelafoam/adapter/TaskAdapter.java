package com.erp.sheelafoam.adapter;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.erp.sheelafoam.BE.TaskBE;
import com.erp.sheelafoam.HomeScreen;
import com.erp.sheelafoam.R;
import com.erp.sheelafoam.asynctask.NetworkTask;
import com.erp.sheelafoam.constant.Constant;
import com.erp.sheelafoam.fragment.DashBoard;
import com.erp.sheelafoam.model.SubmitPollsModel;
import com.erp.sheelafoam.utils.AlertDialogManager;
import com.erp.sheelafoam.utils.ConnectionDetector;
import com.erp.sheelafoam.utils.Util;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by priyanka.sharma on 11/18/2016.
 */

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.EmployerJobsListedHolder> implements NetworkTask.Result {

    Context context;
    ArrayList<TaskBE> base;
    public ConnectionDetector cDetector;
    AlertDialogManager alert;
    String GreatPlusUserID, OP_USER_ROLENAME, UserID, UserToken, UserAuthType;
    CoordinatorLayout task_crl_home;

    public TaskAdapter(Context context, ArrayList<TaskBE> base) {
        this.context = context;
        this.base = base;
    }

    @Override
    public EmployerJobsListedHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.task_row, parent, false);
        EmployerJobsListedHolder viewHolder = new EmployerJobsListedHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(EmployerJobsListedHolder holder, int position) {
        String DisplayNAme = Util.getSharedPrefrenceValue(context, Constant.Sp_DisplayName);
        holder.task_name.setText(DisplayNAme);
        holder.task_matter.setText(base.get(position).getTASK_DESC());
        holder.task_date.setText(base.get(position).getTASK_DATETIME());

        if (base.size() - 1 == position) {
            holder.task_divider.setVisibility(View.GONE);
        } else {
            holder.task_divider.setVisibility(View.VISIBLE);
        }

        if (base.get(position).getAUTHORIZE_YN().equals("1")) {
            holder.approve_layout.setVisibility(View.VISIBLE);
        } else {
            holder.approve_layout.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return base.size();
    }

    public class EmployerJobsListedHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView task_matter, task_name, task_divider, txt_approve, txt_disapprove, task_date;
        LinearLayout approve_layout;

        public EmployerJobsListedHolder(View itemView) {
            super(itemView);
            cDetector = new ConnectionDetector(context);
            alert = new AlertDialogManager();
            getSharedPreferenceValues();

            task_matter = (TextView) itemView.findViewById(R.id.task_matter);
            task_name = (TextView) itemView.findViewById(R.id.task_name);
            task_date = (TextView) itemView.findViewById(R.id.task_date);
            approve_layout = (LinearLayout) itemView.findViewById(R.id.approve_layout);
            task_divider = (TextView) itemView.findViewById(R.id.task_divider);
            txt_approve = (TextView) itemView.findViewById(R.id.txt_approve);
            txt_disapprove = (TextView) itemView.findViewById(R.id.txt_disapprove);
            task_crl_home = (CoordinatorLayout) itemView.findViewById(R.id.task_crl_home);
            txt_disapprove.setOnClickListener(this);
            txt_approve.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.txt_approve:
                    callWSAprove_DIs(getAdapterPosition(), "Approve");
                    break;
                case R.id.txt_disapprove:
                    callWSAprove_DIs(getAdapterPosition(), "DisApprove");
                    break;
            }
        }
    }

    private void getSharedPreferenceValues() {
        GreatPlusUserID = Util.getSharedPrefrenceValue(context, Constant.Sp_GrtPlusUserID);
        OP_USER_ROLENAME = Util.getSharedPrefrenceValue(context, Constant.Sp_op_user_role_name);
        UserID = Util.getSharedPrefrenceValue(context, Constant.Sp_UserID);
        UserToken = Util.getSharedPrefrenceValue(context, Constant.Sp_UserToken);
        UserAuthType = Util.getSharedPrefrenceValue(context, Constant.Sp_AuthType);
    }

    private void callWSAprove_DIs(int adapterPosition, String Task_status) {
        if (!cDetector.isConnectingToInternet())
            alert.showAlertDialog(context, "Network Connection", "Enable Your Internet Connection", false);
        else {
            try {
                JSONObject obj = new JSONObject();
                if (Task_status.equals("Approve")) {
                    obj.put("mode", "approve");
                } else {
                    obj.put("mode", "disapprove");
                }
                obj.put("op_user_role_name", OP_USER_ROLENAME);
                obj.put("op_greatplus_user_id", GreatPlusUserID);
                obj.put("auth_type", UserAuthType);
                obj.put("task_status", base.get(adapterPosition).getAUTHORIZE_YN());
                obj.put("task_id", base.get(adapterPosition).getTASK_ID());
                obj.put("uid", UserID);
                obj.put("token", UserToken);

                String url = Constant.WS_URL + Constant.WS_APPROVE_DISAPPROVE;
                NetworkTask networkTask = new NetworkTask(context, 1, obj.toString());
                networkTask.setDialogMessage("Please wait..");
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
        SubmitPollsModel base = gson.fromJson(object, SubmitPollsModel.class);

        if (base.getInfo().get(0).getStatus() == 1) {
            Toast.makeText(context, base.getInfo().get(0).getMsg(), Toast.LENGTH_LONG).show();
            ((HomeScreen) context).commonFragmentMethod(new DashBoard(), null, null);
        } else {
            Toast.makeText(context, base.getInfo().get(0).getMsg(), Toast.LENGTH_LONG).show();
        }
    }
}