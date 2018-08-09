package com.erp.sheelafoam.fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatSpinner;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.erp.sheelafoam.HomeScreen;
import com.erp.sheelafoam.R;
import com.erp.sheelafoam.UserLogAPI;
import com.erp.sheelafoam.asynctask.NetworkTask;
import com.erp.sheelafoam.constant.Constant;
import com.erp.sheelafoam.model.ApplyLeaveModel;
import com.erp.sheelafoam.model.Leave_Detail_Model;
import com.erp.sheelafoam.model.SubmitPollsModel;
import com.erp.sheelafoam.utils.AlertDialogManager;
import com.erp.sheelafoam.utils.ConnectionDetector;
import com.erp.sheelafoam.utils.Util;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by priyanka.sharma on 12/15/2016.
 */

public class LeaveFragment extends Fragment implements NetworkTask.Result, View.OnClickListener, DatePickerDialog.OnDateSetListener {
    RelativeLayout no_data_layout;
    ConnectionDetector cDetector;
    AppCompatSpinner sp_leave_type;
    CoordinatorLayout leave_crl;
    EditText ed_no_leaves, ed_reason;
    TextView leave_frm, leave_to, txt_name;
    AlertDialogManager alert;
    Button btn_leave_submit;
    RadioButton radio_first, radio_second, radio_both;
    RadioGroup radio_layout;
    String UserID, UserDisplayName, User_empGroupCode, User_opRoleNAme, UserEmail, GreatPlusUserID, str_LeaveFrom = "",
            str_LeaveTo = "", LeaveType, strReason, strNoOfleaves, UserToken, UserAuthType;
    int selectedData = -1;
    Calendar c;
    boolean flag = true;
    TextView balance_leave_co, taken_leave_co, encash_leave_co, opening_leave_co, balance_leave_el, taken_leave_el,
            encash_leave_el, opening_leave_el, balance_leave_cl, taken_leave_cl, encash_leave_cl, opening_leave_cl;
    private String str_which_half_leave;
    boolean isFirstTime = false;
    Date date1,date2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_leave
                , container, false);
        new UserLogAPI("Leave Apply Page", getActivity());
        init(view);

        return view;
    }

    private void init(View view) {
        cDetector = new ConnectionDetector(getActivity());
        alert = new AlertDialogManager();

        ((HomeScreen) getActivity()).txt_title.setText("Leave Request");
        ((HomeScreen) getActivity()).profile_image.setVisibility(View.GONE);
        ((HomeScreen) getActivity()).txt_title.setVisibility(View.VISIBLE);

        getSharedPreferenceValues();

        c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), this, year, month, day);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

        sp_leave_type = (AppCompatSpinner) view.findViewById(R.id.sp_leave_type);

        sp_leave_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                LeaveType = parentView.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
        leave_frm = (TextView) view.findViewById(R.id.leave_frm);
        leave_crl = (CoordinatorLayout) view.findViewById(R.id.leave_crl);
        leave_to = (TextView) view.findViewById(R.id.leave_to);
        txt_name = (TextView) view.findViewById(R.id.txt_name);

        balance_leave_co = (TextView) view.findViewById(R.id.balance_leave_co);
        balance_leave_cl = (TextView) view.findViewById(R.id.balance_leave_cl);
        balance_leave_el = (TextView) view.findViewById(R.id.balance_leave_el);
        opening_leave_cl = (TextView) view.findViewById(R.id.opening_leave_cl);
        opening_leave_el = (TextView) view.findViewById(R.id.opening_leave_el);
        opening_leave_co = (TextView) view.findViewById(R.id.opening_leave_co);
        taken_leave_cl = (TextView) view.findViewById(R.id.taken_leave_cl);
        taken_leave_el = (TextView) view.findViewById(R.id.taken_leave_el);
        taken_leave_co = (TextView) view.findViewById(R.id.taken_leave_co);
        encash_leave_cl = (TextView) view.findViewById(R.id.encash_leave_cl);
        encash_leave_el = (TextView) view.findViewById(R.id.encash_leave_el);
        encash_leave_co = (TextView) view.findViewById(R.id.encash_leave_co);

        ed_no_leaves = (EditText) view.findViewById(R.id.ed_no_leaves);
        ed_reason = (EditText) view.findViewById(R.id.ed_reason);
        radio_layout = (RadioGroup) view.findViewById(R.id.radio_layout);
        radio_both = (RadioButton) view.findViewById(R.id.radio_both);
        radio_first = (RadioButton) view.findViewById(R.id.radio_first);
        radio_second = (RadioButton) view.findViewById(R.id.radio_second);
        txt_name.setText(UserDisplayName);

        btn_leave_submit = (Button) view.findViewById(R.id.btn_leave_submit);
        btn_leave_submit.setOnClickListener(this);
        leave_frm.setOnClickListener(this);
        leave_to.setOnClickListener(this);
        radio_layout.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (radio_first.isChecked() || radio_second.isChecked()) {
                    ed_no_leaves.setText("0.5");

                    if (radio_first.isChecked())
                        str_which_half_leave = "F";
                    else
                        str_which_half_leave = "S";

                    c = Calendar.getInstance();
                    int year = c.get(Calendar.YEAR);
                    int month = c.get(Calendar.MONTH);
                    int day = c.get(Calendar.DAY_OF_MONTH);

                    leave_to.setText(str_LeaveFrom);
                } else if (radio_both.isChecked()) {
                    str_which_half_leave = "B";
                    ed_no_leaves.setText("1");
                }
            }
        });
        callWSLeaveType();
        callWSLeaveDetail();
    }

    private void getSharedPreferenceValues() {
        UserID = Util.getSharedPrefrenceValue(getActivity(), Constant.Sp_UserID);
        UserDisplayName = Util.getSharedPrefrenceValue(getActivity(), Constant.Sp_DisplayName);
        User_opRoleNAme = Util.getSharedPrefrenceValue(getActivity(), Constant.Sp_RoleName);
        User_empGroupCode = Util.getSharedPrefrenceValue(getActivity(), Constant.Sp_emp_grpCode);
        UserEmail = Util.getSharedPrefrenceValue(getActivity(), Constant.Sp_USerEmail);
        GreatPlusUserID = Util.getSharedPrefrenceValue(getActivity(), Constant.Sp_GrtPlusUserID);
        UserToken = Util.getSharedPrefrenceValue(getActivity(), Constant.Sp_UserToken);
        UserAuthType = Util.getSharedPrefrenceValue(getActivity(), Constant.Sp_AuthType);
    }

    private void callWSLeaveType() {
        if (!cDetector.isConnectingToInternet())
            alert.showAlertDialog(getActivity(), "Network Connection", "Enable Your Internet Connection", false);
        else {
            try {
                JSONObject obj = new JSONObject();
                obj.put("user_email", UserDisplayName);
                obj.put("mode", "leave_list");
                obj.put("uid", UserID);
                obj.put("token", UserToken);
                obj.put("auth_type", UserAuthType);
                obj.put("op_greatplus_user_id", GreatPlusUserID);
                obj.put("op_user_role_name", User_opRoleNAme);
                obj.put("op_user_emp_group_code", User_empGroupCode);

                String url = Constant.WS_URL + Constant.WS_APPLY_LEAVE;
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.leave_frm:
                selectedData = -1;
                setDate();
                break;
            case R.id.leave_to:
                selectedData = 0;
                setDate();
                radio_both.setChecked(true);
                break;
            case R.id.btn_leave_submit:
                if (validate()) {
                    callWSLeaveSubmit();
                }
                break;
        }
    }

    public void setDate() {
        final Calendar c = Calendar.getInstance();
        final int year = c.get(Calendar.YEAR);
        final int month = c.get(Calendar.MONTH);
        final int day = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), this, year, month, day);

        final Calendar later = Calendar.getInstance();
        later.add(Calendar.MONTH, 1);
        //datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000);
        //   datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    private void callWSLeaveSubmit() {

        if (!cDetector.isConnectingToInternet())
            alert.showAlertDialog(getActivity(), "Network Connection", "Enable Your Internet Connection", false);
        else {
            try {
                JSONObject obj = new JSONObject();
                obj.put("p_leave_type", LeaveType);
                obj.put("p_leave_date_from", str_LeaveFrom);
                obj.put("p_leave_date_to", str_LeaveTo);
                obj.put("p_leave_nos", strNoOfleaves);
                obj.put("p_remark", strReason);
                obj.put("op_user_name", UserDisplayName);
                obj.put("p_half_day_leave_dt", "");
                obj.put("auth_type", UserAuthType);
                obj.put("user_email", UserEmail);
                obj.put("uid", UserID);
                obj.put("token", UserToken);
                obj.put("mode", "apply_leave");
                obj.put("op_greatplus_user_id", GreatPlusUserID);
                obj.put("op_user_role_name", User_opRoleNAme);
                obj.put("op_user_emp_group_code", User_empGroupCode);
                obj.put("p_which_half_leave", str_which_half_leave);

                String url = Constant.WS_URL + Constant.WS_APPLY_LEAVE;
                NetworkTask networkTask = new NetworkTask(getActivity(), 2, obj.toString());
                networkTask.setDialogMessage("Loading...");
                networkTask.exposePostExecute(this);
                networkTask.execute(url);

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }


    private void callWSLeaveDetail() {

        if (!cDetector.isConnectingToInternet())
            alert.showAlertDialog(getActivity(), "Network Connection", "Enable Your Internet Connection", false);
        else {
            try {
                JSONObject obj = new JSONObject();
                obj.put("user_email", UserEmail);
                obj.put("uid", UserID);
                obj.put("mode", "leave_list");
                obj.put("auth_type", UserAuthType);
                obj.put("token", UserToken);
                obj.put("op_greatplus_user_id", GreatPlusUserID);
                obj.put("op_user_role_name", User_opRoleNAme);
                obj.put("op_user_emp_group_code", User_empGroupCode);

                String url = Constant.WS_URL + Constant.WS_APPLY_LEAVE;
                NetworkTask networkTask = new NetworkTask(getActivity(), 3, obj.toString());
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
        switch (id) {
            case 1:
                Gson gson = new Gson();
                ApplyLeaveModel base = gson.fromJson(object, ApplyLeaveModel.class);

                if (base.getInfo().get(0).getStatus() == 1) {
                    String arrSS[] = new String[6];

                    arrSS[0] = base.getInfo().get(0).getLeave_type().get(0).getA1();
                    arrSS[1] = base.getInfo().get(0).getLeave_type().get(0).getA2();
                    arrSS[2] = base.getInfo().get(0).getLeave_type().get(0).getA3();
                    arrSS[3] = base.getInfo().get(0).getLeave_type().get(0).getA4();
                    arrSS[4] = base.getInfo().get(0).getLeave_type().get(0).getA5();
                    arrSS[5] = base.getInfo().get(0).getLeave_type().get(0).getA6();

                    ArrayAdapter<String> adp = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, arrSS);
                    sp_leave_type.setAdapter(adp);
                   /* String Leave_Type = base.getInfo().get(0).getLeave_type();
                    String[] leave = Leave_Type.split(",");
                    for (int i = 0; i < leave.length; i++) {
                        leave_type_array.add(leave[i]);
                    }
                    ArrayAdapter<String> adp = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, leave_type_array);
                    sp_leave_type.setAdapter(adp);*/

                } else {

                }
                break;
            case 2:
                Gson gson1 = new Gson();
                SubmitPollsModel base1 = gson1.fromJson(object, SubmitPollsModel.class);

                if (base1.getInfo().get(0).getStatus() == 1) {
                    Util.showSnackbar(getActivity(), leave_crl, base1.getInfo().get(0).getMsg());
                    sp_leave_type.setSelection(0);
                    leave_frm.setText("");
                    leave_to.setText("");
                    ed_reason.setText("");
                    radio_layout.check(radio_both.getId());
                    ed_no_leaves.setText("");
                } else {
                    Util.showSnackbar(getActivity(), leave_crl, base1.getInfo().get(0).getMsg());
                }
                break;
            case 3:
                Gson gson2 = new Gson();

                Leave_Detail_Model leave_detail_model = gson2.fromJson(object, Leave_Detail_Model.class);

                if (leave_detail_model.getLeave_detail().size() > 0) {
                    balance_leave_cl.setText(leave_detail_model.getLeave_detail().get(0).getOp_bal_cl());
                    balance_leave_co.setText(leave_detail_model.getLeave_detail().get(0).getOp_bal_co());
                    balance_leave_el.setText(leave_detail_model.getLeave_detail().get(0).getOp_bal_el());

                    opening_leave_cl.setText(leave_detail_model.getLeave_detail().get(0).getOp_open_cl());
                    opening_leave_co.setText(leave_detail_model.getLeave_detail().get(0).getOp_open_co());
                    opening_leave_el.setText(leave_detail_model.getLeave_detail().get(0).getOp_open_el());

                    encash_leave_cl.setText(leave_detail_model.getLeave_detail().get(0).getOp_receive_cl());
                    encash_leave_co.setText(leave_detail_model.getLeave_detail().get(0).getOp_receive_co());
                    encash_leave_el.setText(leave_detail_model.getLeave_detail().get(0).getOp_receive_el());

                    taken_leave_cl.setText(leave_detail_model.getLeave_detail().get(0).getOp_issue_cl());
                    taken_leave_co.setText(leave_detail_model.getLeave_detail().get(0).getOp_issue_co());
                    taken_leave_el.setText(leave_detail_model.getLeave_detail().get(0).getOp_issue_el());

                } else {

                }
                break;

        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        if (selectedData == -1)
            leave_frm.setText(zeroPrefix(dayOfMonth) + "/" + zeroPrefix(month + 1) + "/" + year);
        else
            leave_to.setText(zeroPrefix(dayOfMonth) + "/" + zeroPrefix(month + 1) + "/" + year);

        SimpleDateFormat myFormat = new SimpleDateFormat("dd/MM/yyyy");

        str_LeaveFrom = leave_frm.getText().toString();
        str_LeaveTo = leave_to.getText().toString();

        try {
             date1 = myFormat.parse(str_LeaveFrom);
             date2 = myFormat.parse(str_LeaveTo);
            if (date2.getTime() >= date1.getTime()) {

                long diff = date2.getTime() - date1.getTime();
                float dayCount = (float) diff / (24 * 60 * 60 * 1000) + 1;
                int days = (int) dayCount;
                ed_no_leaves.setText(String.valueOf(days));
                ed_no_leaves.setEnabled(false);
                System.out.println("Days: " + days);
            } else if(date1.after(date2)){
                Util.showSnackbar(getActivity(), leave_crl, "You must provide Leave To date greater than Leave From.");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public String zeroPrefix(int quantity) {
        if (quantity < 10) {
            return "0" + quantity;
        }
        return "" + quantity;
    }

    private boolean validate() {
        boolean flag = true;

        strReason = ed_reason.getText().toString();
        strNoOfleaves = ed_no_leaves.getText().toString();

        if (LeaveType.length() == 0) {
            Util.showSnackbar(getActivity(), leave_crl, "Please fill Leave Type!!");
            flag = false;
        } else if (str_LeaveFrom.length() == 0 || str_LeaveTo.length() == 0) {
            Util.showSnackbar(getActivity(), leave_crl, "Please fill Dates!!");
            flag = false;
        } else if (strReason.length() == 0) {
            Util.showSnackbar(getActivity(), leave_crl, "Please fill Leave Reason!!");
            flag = false;
        } else if(date1.after(date2)){
            Util.showSnackbar(getActivity(), leave_crl, "You must provide Leave To date greater than Leave From.");
            flag = false;
        }

        return flag;
    }

    private void strDateAfter() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), this, year, month, day);

        final Calendar later = Calendar.getInstance();
        later.add(Calendar.MONTH, 1);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();

        str_LeaveTo = leave_to.getText().toString();
        callWSLeaveSubmit();

    }
}
