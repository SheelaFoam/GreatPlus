package com.erp.sheelafoam.sheelafoam.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.erp.sheelafoam.HomeScreen;
import com.erp.sheelafoam.R;
import com.erp.sheelafoam.adapter.DialogFootFallAdapter;
import com.erp.sheelafoam.adapter.RadioFootFallAdapter;
import com.erp.sheelafoam.constant.Constant;
import com.erp.sheelafoam.model.SerialNumModel;
import com.erp.sheelafoam.model.UserModel;
import com.erp.sheelafoam.prefrences.SheelaSharedPreference;
import com.erp.sheelafoam.utils.CommonClass;
import com.erp.sheelafoam.utils.CustomDialog;
import com.erp.sheelafoam.utils.CustomTypefaceTextView;
import com.erp.sheelafoam.utils.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomerFeedbackActivity extends Activity implements View.OnClickListener {
    Context context;
    CustomDialog dialog;
    UserModel userModel = new UserModel();
    UserModel userModelForProduct = new UserModel();
    ArrayList<String> arrayListS = new ArrayList();
    ArrayList<String> arrayListForProduct = new ArrayList();
    ArrayList<String> array = new ArrayList<>();
    ArrayList<String> arrayRadioGroup = new ArrayList<>();
    private ImageButton ib_backicon_new, iv_dot_new;
    private TextView tv_logoname, tv_emptyText;
    private CustomTypefaceTextView tv_selectEmployeeSpinner, tv_selectStoreFeedbackSpinner, tv_genderFeedback, tv_aboutFeedback, tv_istProductFeedback, tv_brandFeedback, tv_purchasedFeedback;
    private ProgressDialog progress;
    private TextView tv_dateOfBirthFeedback;
    private EditText et_firstNameFeedback, et_lastNameFeedback, et_emailFeedback, et_mobileFeedback, et_ageingFeedback, et_purposeFeedback;
    private SheelaSharedPreference preference;
    private CommonClass commonClass;
    private int mYear, mMonth, mDay;
    private Button btn_submit_customerFeedback;

    private static String formatDate(int year, int month, int day) {

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(0);
        cal.set(year, month, day);
        Date date = cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");

        return sdf.format(date);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_feedback_activity);
        preference = new SheelaSharedPreference(this);
        progress = new ProgressDialog(this);
        ib_backicon_new = (ImageButton) findViewById(R.id.ib_backicon_new);
        tv_logoname = (TextView) findViewById(R.id.tv_logoname_new);
        tv_logoname.setText("Customer FeedBack");
        iv_dot_new = (ImageButton) findViewById(R.id.iv_dot_new);
        iv_dot_new.setVisibility(View.VISIBLE);
        tv_selectEmployeeSpinner = (CustomTypefaceTextView) findViewById(R.id.tv_selectEmployeeSpinner);
        tv_selectStoreFeedbackSpinner = (CustomTypefaceTextView) findViewById(R.id.tv_selectStoreFeedbackSpinner);
        tv_genderFeedback = (CustomTypefaceTextView) findViewById(R.id.tv_genderFeedback);
        tv_aboutFeedback = (CustomTypefaceTextView) findViewById(R.id.tv_aboutFeedback);
        tv_istProductFeedback = (CustomTypefaceTextView) findViewById(R.id.tv_istProductFeedback);
        tv_purchasedFeedback = (CustomTypefaceTextView) findViewById(R.id.tv_purchasedFeedback);
        tv_brandFeedback = (CustomTypefaceTextView) findViewById(R.id.tv_brandFeedback);
        tv_dateOfBirthFeedback = (TextView) findViewById(R.id.tv_dateOfBirthFeedback);
        et_ageingFeedback = (EditText) findViewById(R.id.et_ageingFeedback);
        et_purposeFeedback = (EditText) findViewById(R.id.et_purposeFeedback);
        et_firstNameFeedback = (EditText) findViewById(R.id.et_firstNameFeedback);
        et_lastNameFeedback = (EditText) findViewById(R.id.et_lastNameFeedback);
        et_emailFeedback = (EditText) findViewById(R.id.et_emailFeedback);
        et_mobileFeedback = (EditText) findViewById(R.id.et_mobileFeedback);
        btn_submit_customerFeedback = (Button) findViewById(R.id.btn_submit_customerFeedback);

        tv_selectEmployeeSpinner.setOnClickListener(this);
        tv_selectStoreFeedbackSpinner.setOnClickListener(this);
        tv_genderFeedback.setOnClickListener(this);
        tv_dateOfBirthFeedback.setOnClickListener(this);
        ib_backicon_new.setOnClickListener(this);
        tv_purchasedFeedback.setOnClickListener(this);
        iv_dot_new.setOnClickListener(this);
        tv_aboutFeedback.setOnClickListener(this);
        tv_istProductFeedback.setOnClickListener(this);
        tv_brandFeedback.setOnClickListener(this);
        btn_submit_customerFeedback.setOnClickListener(this);
        arrayListS.add("Our Website");
        arrayListS.add("TV Advertisement");
        arrayListS.add("Newspaper");
        arrayListS.add("Others");
        userModel.setPUserId("TV1");
        userModel.setPUserId("TV2");
        userModel.setPUserId("TV3");
        userModel.setPUserId("TV4");
        userModel.setSerialNumber(arrayListS);
        arrayListForProduct.add("Activa");
        arrayListForProduct.add("Admire");
        arrayListForProduct.add("Dignity");
        arrayListForProduct.add("Durafirm");
        arrayListForProduct.add("Aspire");
        arrayListForProduct.add("Comfort Cell");
        arrayListForProduct.add("Ultra");
        arrayListForProduct.add("Inspiration");
        arrayListForProduct.add("Others");
        userModel.setPUserId("TV1");
        userModel.setPUserId("TV2");
        userModel.setPUserId("TV3");
        userModel.setPUserId("TV4");
        userModel.setPUserId("TV5");
        userModel.setPUserId("TV6");
        userModel.setPUserId("TV7");
        userModel.setPUserId("TV8");
        userModel.setPUserId("TV9");
        userModelForProduct.setSerialNumber(arrayListForProduct);

        arrayRadioGroup.add("Male");
        arrayRadioGroup.add("Female");
        arrayRadioGroup.add("Trans Gender");
        ArrayList ar = userModel.getSerialNumber();
        tv_selectStoreFeedbackSpinner.setText(preference.getUserNameFootFall());
        tv_selectEmployeeSpinner.setText(preference.getEMPName());
        try {
            progress = new ProgressDialog(this);
            progress.setMessage("Loading...");
            progress.setCancelable(false);
            progress.show();
            callGetService();
        } catch (Exception e) {

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_selectEmployeeSpinner:
                openRadioDialog(this);
                break;
            case R.id.tv_selectStoreFeedbackSpinner:
                openRadioDialogForStore(this);
                break;
            case R.id.tv_genderFeedback:
                openRadioGroup(this);
                break;
            case R.id.tv_dateOfBirthFeedback:
                openDateDialog();
                break;
            case R.id.ib_backicon_new:
                Intent goBack = new Intent(CustomerFeedbackActivity.this, HomeScreen.class);
                startActivity(goBack);
                break;
            case R.id.tv_istProductFeedback:
                openCustomeDialogForProduct(this);
                break;
            case R.id.tv_brandFeedback:
                openCustomeDialogForBrand(this);
                break;
            case R.id.tv_purchasedFeedback:
                openRadioGroupForPurchased(this);
                break;
            case R.id.tv_aboutFeedback:
                openCustomeDialog(this
                );
                break;
            case R.id.iv_dot_new:
                Intent goToNext = new Intent(CustomerFeedbackActivity.this, CustomerFeedBackListActivity.class);
                startActivity(goToNext);
                break;
            case R.id.btn_submit_customerFeedback:
                if (isValid()) {
                    try {
                        progress = new ProgressDialog(this);
                        progress.setMessage("Loading...");
                        progress.setCancelable(false);
                        progress.show();
                        callSubMitFeeBack();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    private void openDateDialog() {
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(CustomerFeedbackActivity.this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                tv_dateOfBirthFeedback.setText(formatDate(year, monthOfYear, dayOfMonth));
            }
        }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    public void openCustomeDialog(Context context) {
        String gender_present = tv_aboutFeedback.getText().toString();
        final String[] gender = new String[1];
        dialog = new CustomDialog(context, R.layout.cutome_radio_group);
        dialog.setCancelable(true);
        CustomTypefaceTextView tv_subTile = (CustomTypefaceTextView) dialog.findViewById(R.id.tv_subTile_footfall_radioGroup);
        tv_subTile.setText("Tell us how you hear about us ?*");
        final RadioGroup rg = (RadioGroup) dialog.findViewById(R.id.radio);
        RadioButton rb_ourWesite = (RadioButton) dialog.findViewById(R.id.rb_ourWesite);
        rb_ourWesite.setVisibility(View.VISIBLE);
        RadioButton rb_TV_Advertisement = (RadioButton) dialog.findViewById(R.id.rb_TV_Advertisement);
        rb_TV_Advertisement.setVisibility(View.VISIBLE);
        RadioButton rb_Newspaper = (RadioButton) dialog.findViewById(R.id.rb_Newspaper);
        rb_Newspaper.setVisibility(View.VISIBLE);
        RadioButton rb_others = (RadioButton) dialog.findViewById(R.id.rb_others);
        rb_others.setVisibility(View.VISIBLE);
        rg.clearCheck();
        if (gender_present.equalsIgnoreCase("Our Website")) {
            rb_ourWesite.setChecked(true);
        } else if (gender_present.equalsIgnoreCase("TV Advertisement")) {
            rb_TV_Advertisement.setChecked(true);
        } else if (gender_present.equalsIgnoreCase("Newspaper")) {
            rb_Newspaper.setChecked(true);
        } else if (gender_present.equalsIgnoreCase("Others")) {
            rb_others.setChecked(true);
        }
        TextView btn_footfall_cancel = (TextView) dialog.findViewById(R.id.btn_footfall_cancel_radioGroup);
        TextView btn_footfall_ok = (TextView) dialog.findViewById(R.id.btn_footfall_ok_radioGroup);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_ourWesite:
                        gender[0] = "Our Website";

                        break;
                    case R.id.rb_TV_Advertisement:
                        gender[0] = "TV Advertisement";

                        break;
                    case R.id.rb_Newspaper:
                        gender[0] = "Newespaper";

                        break;
                    case R.id.rb_others:
                        gender[0] = "Others";

                        break;
                }
            }
        });

        btn_footfall_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (gender[0] != null)
                    tv_aboutFeedback.setText(gender[0].toString());
                dialog.dismiss();
            }
        });
        dialog.show();
        btn_footfall_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    public void openCustomeDialogForBrand(Context context) {
        String gender_present = tv_brandFeedback.getText().toString();
        final String[] gender = new String[1];
        dialog = new CustomDialog(context, R.layout.cutome_radio_group);
        dialog.setCancelable(true);
        CustomTypefaceTextView tv_subTile = (CustomTypefaceTextView) dialog.findViewById(R.id.tv_subTile_footfall_radioGroup);
        tv_subTile.setText("Current Mattress Brand*");
        final RadioGroup rg = (RadioGroup) dialog.findViewById(R.id.radio);
        RadioButton rb_brandSheelaFoam = (RadioButton) dialog.findViewById(R.id.rb_brandSheelaFoam);
        rb_brandSheelaFoam.setVisibility(View.VISIBLE);
        RadioButton rb_brandSleepwell = (RadioButton) dialog.findViewById(R.id.rb_brandSleepwell);
        rb_brandSleepwell.setVisibility(View.VISIBLE);
        RadioButton rb_brandRubco = (RadioButton) dialog.findViewById(R.id.rb_brandRubco);
        rb_brandRubco.setVisibility(View.VISIBLE);
        RadioButton rb_brandTempurPedic = (RadioButton) dialog.findViewById(R.id.rb_brandTempurPedic);
        rb_brandTempurPedic.setVisibility(View.VISIBLE);
        RadioButton rb_brandDunlopillo = (RadioButton) dialog.findViewById(R.id.rb_brandDunlopillo);
        rb_brandDunlopillo.setVisibility(View.VISIBLE);
        RadioButton rb_brandMMFoam = (RadioButton) dialog.findViewById(R.id.rb_brandMMFoam);
        rb_brandMMFoam.setVisibility(View.VISIBLE);
        RadioButton rb_brandDuroflex = (RadioButton) dialog.findViewById(R.id.rb_brandDuroflex);
        rb_brandDuroflex.setVisibility(View.VISIBLE);
        RadioButton rb_brandSleepzone = (RadioButton) dialog.findViewById(R.id.rb_brandSleepzone);
        rb_brandSleepzone.setVisibility(View.VISIBLE);
        RadioButton rb_brandKingKoil = (RadioButton) dialog.findViewById(R.id.rb_brandKingKoil);
        rb_brandKingKoil.setVisibility(View.VISIBLE);
        RadioButton rb_brandRestonic = (RadioButton) dialog.findViewById(R.id.rb_brandRestonic);
        rb_brandRestonic.setVisibility(View.VISIBLE);
        RadioButton rb_brandNone = (RadioButton) dialog.findViewById(R.id.rb_brandNone);
        rb_brandNone.setVisibility(View.VISIBLE);
        RadioButton rb_brandOthers = (RadioButton) dialog.findViewById(R.id.rb_brandOthers);
        rb_brandOthers.setVisibility(View.VISIBLE);
        rg.clearCheck();
        if (gender_present.equalsIgnoreCase("Sheela Foam")) {
            rb_brandSheelaFoam.setChecked(true);
        } else if (gender_present.equalsIgnoreCase("Sleepwell")) {
            rb_brandSleepwell.setChecked(true);
        } else if (gender_present.equalsIgnoreCase("Rubco")) {
            rb_brandRubco.setChecked(true);
        } else if (gender_present.equalsIgnoreCase("Tempur-Pedic")) {
            rb_brandTempurPedic.setChecked(true);
        } else if (gender_present.equalsIgnoreCase("Dunlopillo")) {
            rb_brandDunlopillo.setChecked(true);
        } else if (gender_present.equalsIgnoreCase("MM Foam")) {
            rb_brandMMFoam.setChecked(true);
        } else if (gender_present.equalsIgnoreCase("Duroflex")) {
            rb_brandDuroflex.setChecked(true);

        } else if (gender_present.equalsIgnoreCase("Sleepzone")) {
            rb_brandSleepzone.setChecked(true);
        } else if (gender_present.equalsIgnoreCase("King Koil")) {
            rb_brandKingKoil.setChecked(true);
        } else if (gender_present.equalsIgnoreCase("Restonic")) {
            rb_brandRestonic.setChecked(true);
        } else if (gender_present.equalsIgnoreCase("None")) {
            rb_brandNone.setChecked(true);
        } else if (gender_present.equalsIgnoreCase("Others")) {
            rb_brandOthers.setChecked(true);
        }
        TextView btn_footfall_cancel = (TextView) dialog.findViewById(R.id.btn_footfall_cancel_radioGroup);
        TextView btn_footfall_ok = (TextView) dialog.findViewById(R.id.btn_footfall_ok_radioGroup);

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_brandSheelaFoam:
                        gender[0] = "Sheela Foam";

                        break;
                    case R.id.rb_brandSleepwell:
                        gender[0] = "Sleepwell";

                        break;
                    case R.id.rb_brandRubco:
                        gender[0] = "Rubco";

                        break;
                    case R.id.rb_brandTempurPedic:
                        gender[0] = "Tempur-Pedic";

                        break;
                    case R.id.rb_brandDunlopillo:
                        gender[0] = "Dunlopillo";

                        break;
                    case R.id.rb_brandMMFoam:
                        gender[0] = "MM Foam";

                        break;
                    case R.id.rb_brandDuroflex:
                        gender[0] = "Duroflex";

                        break;
                    case R.id.rb_brandSleepzone:
                        gender[0] = "Sleepzone";

                        break;
                    case R.id.rb_brandKingKoil:
                        gender[0] = "King Koil";

                        break;
                    case R.id.rb_brandRestonic:
                        gender[0] = "Restonic";

                        break;
                    case R.id.rb_brandNone:
                        gender[0] = "None";

                        break;
                    case R.id.rb_brandOthers:
                        gender[0] = "Others";

                        break;
                }
            }
        });

        btn_footfall_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (gender[0] != null)
                    tv_brandFeedback.setText(gender[0].toString());
                dialog.dismiss();
            }
        });
        dialog.show();
        btn_footfall_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    public void openCustomeDialogForProduct(Context context) {
        dialog = new CustomDialog(context, R.layout.dialog_footfall);
        dialog.setCancelable(true);
        CustomTypefaceTextView tv_subTile = (CustomTypefaceTextView) dialog.findViewById(R.id.tv_subTile_footfall);
        tv_subTile.setText("Interested in Product *");
        ListView lv = (ListView) dialog.findViewById(R.id.recycler_footfall);
        TextView btn_footfall_cancel = (TextView) dialog.findViewById(R.id.btn_footfall_cancel);
        final List<SerialNumModel> sNoList = new ArrayList<>();
        ArrayList<String> arrayListS = new ArrayList();
        arrayListS = userModelForProduct.getSerialNumber();
        Log.d("arrayListS in Dialoge", arrayListS.size() + "");
        for (String sNo : arrayListS) {
            if (!TextUtils.isEmpty(sNo) && !sNo.equalsIgnoreCase("null")) {
                SerialNumModel model = new SerialNumModel();
                model.setsNo(sNo);
                sNoList.add(model);
            }
        }
        dialog.show();
        lv.setAdapter(new DialogFootFallAdapter(CustomerFeedbackActivity.this, sNoList));//--1--for--checkbox--xml
        final ArrayList<String> SNoArr = new ArrayList<>();
        final TextView btn_bundle_submit = (TextView) dialog.findViewById(R.id.btn_footfall_ok);
        btn_bundle_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (SerialNumModel m : sNoList) {
                    if (m.isChecked()) {
                        SNoArr.add(m.getsNo());
                    }
                }
                if (SNoArr.size() < 1) {
                    Toast.makeText(CustomerFeedbackActivity.this, "Please select minimum one!", Toast.LENGTH_SHORT).show();

                } else if (SNoArr.size() >= 9) {
                    tv_istProductFeedback.setText(SNoArr.get(0) + "," + SNoArr.get(1) + "," + SNoArr.get(2) + "," + SNoArr.get(3) + "," + SNoArr.get(4) + "," + SNoArr.get(5) + "," + SNoArr.get(6) + "," + SNoArr.get(7));
                    dialog.dismiss();
                } else if (SNoArr.size() >= 8) {
                    tv_istProductFeedback.setText(SNoArr.get(0) + "," + SNoArr.get(1) + "," + SNoArr.get(2) + "," + SNoArr.get(3) + "," + SNoArr.get(4) + "," + SNoArr.get(5) + "," + SNoArr.get(6));
                    dialog.dismiss();
                } else if (SNoArr.size() >= 7) {
                    tv_istProductFeedback.setText(SNoArr.get(0) + "," + SNoArr.get(1) + "," + SNoArr.get(2) + "," + SNoArr.get(3) + "," + SNoArr.get(4) + "," + SNoArr.get(5));
                    dialog.dismiss();
                } else if (SNoArr.size() >= 6) {
                    tv_istProductFeedback.setText(SNoArr.get(0) + "," + SNoArr.get(1) + "," + SNoArr.get(2) + "," + SNoArr.get(3) + "," + SNoArr.get(4));
                    dialog.dismiss();
                } else if (SNoArr.size() >= 5) {
                    tv_istProductFeedback.setText(SNoArr.get(0) + "," + SNoArr.get(1) + "," + SNoArr.get(2) + "," + SNoArr.get(3));
                    dialog.dismiss();
                } else if (SNoArr.size() >= 4) {
                    tv_istProductFeedback.setText(SNoArr.get(0) + "," + SNoArr.get(1) + "," + SNoArr.get(2) + "," + SNoArr.get(3));
                    dialog.dismiss();
                } else if (SNoArr.size() >= 3) {
                    tv_istProductFeedback.setText(SNoArr.get(0) + "," + SNoArr.get(1) + "," + SNoArr.get(2));
                    dialog.dismiss();
                } else if (SNoArr.size() >= 2) {
                    tv_istProductFeedback.setText(SNoArr.get(0) + "," + SNoArr.get(1));
                    dialog.dismiss();
                } else if (SNoArr.size() >= 1) {
                    tv_istProductFeedback.setText(SNoArr.get(0));
                    dialog.dismiss();
                }

            }
        });
        btn_footfall_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    private void openRadioDialog(Context context) {
        array.clear();
        array.add(preference.getEMPName());
        dialog = new CustomDialog(context, R.layout.dialog_footfall);
        dialog.setCancelable(true);
        CustomTypefaceTextView tv_subTile = (CustomTypefaceTextView) dialog.findViewById(R.id.tv_subTile_footfall);
        tv_subTile.setText("Select Employee *");
        ListView lv = (ListView) dialog.findViewById(R.id.recycler_footfall);
        TextView btn_footfall_cancel = (TextView) dialog.findViewById(R.id.btn_footfall_cancel);
        TextView btn_footfall_ok = (TextView) dialog.findViewById(R.id.btn_footfall_ok);
        btn_footfall_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_selectEmployeeSpinner.setText(preference.getEMPName());
                dialog.dismiss();
            }
        });
        dialog.show();
        lv.setAdapter(new RadioFootFallAdapter(this, array));
        btn_footfall_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    private void openRadioDialogForStore(Context context) {
        array.clear();
        array.add(preference.getUserNameFootFall());
        dialog = new CustomDialog(context, R.layout.dialog_footfall);
        dialog.setCancelable(true);
        CustomTypefaceTextView tv_subTile = (CustomTypefaceTextView) dialog.findViewById(R.id.tv_subTile_footfall);
        tv_subTile.setText("Select Store*");
        ListView lv = (ListView) dialog.findViewById(R.id.recycler_footfall);
        TextView btn_footfall_cancel = (TextView) dialog.findViewById(R.id.btn_footfall_cancel);
        TextView btn_footfall_ok = (TextView) dialog.findViewById(R.id.btn_footfall_ok);
        btn_footfall_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_selectStoreFeedbackSpinner.setText(preference.getUserNameFootFall());
                dialog.dismiss();
            }
        });
        dialog.show();
        lv.setAdapter(new RadioFootFallAdapter(this, array));
        btn_footfall_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    private void openRadioGroup(Context context) {
        String gender_present = tv_genderFeedback.getText().toString();
        final String[] gender = new String[1];
        dialog = new CustomDialog(context, R.layout.cutome_radio_group);
        dialog.setCancelable(true);
        CustomTypefaceTextView tv_subTile = (CustomTypefaceTextView) dialog.findViewById(R.id.tv_subTile_footfall_radioGroup);
        tv_subTile.setText("Select Gender*");
        final RadioGroup rg = (RadioGroup) dialog.findViewById(R.id.radio);
        RadioButton rb_male = (RadioButton) dialog.findViewById(R.id.rb_male);
        rb_male.setVisibility(View.VISIBLE);
        RadioButton rb_female = (RadioButton) dialog.findViewById(R.id.rb_female);
        rb_female.setVisibility(View.VISIBLE);
        RadioButton rb_transGender = (RadioButton) dialog.findViewById(R.id.rb_ranasGender);
        rb_transGender.setVisibility(View.VISIBLE);
        rg.clearCheck();
        if (gender_present.equalsIgnoreCase("Male")) {
            rb_male.setChecked(true);
        } else if (gender_present.equalsIgnoreCase("Female")) {
            rb_female.setChecked(true);
        } else if (gender_present.equalsIgnoreCase("Trans Gender")) {
            rb_transGender.setChecked(true);
        }
        TextView btn_footfall_cancel = (TextView) dialog.findViewById(R.id.btn_footfall_cancel_radioGroup);
        TextView btn_footfall_ok = (TextView) dialog.findViewById(R.id.btn_footfall_ok_radioGroup);

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_male:
                        gender[0] = "Male";

                        break;
                    case R.id.rb_female:
                        gender[0] = "Female";

                        break;
                    case R.id.rb_ranasGender:
                        gender[0] = "Trans Gender";

                        break;
                }
            }
        });

        btn_footfall_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (gender[0] != null)
                    tv_genderFeedback.setText(gender[0].toString());
                dialog.dismiss();
            }
        });
        dialog.show();
        btn_footfall_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    private void openRadioGroupForPurchased(Context context) {
        String gender_present = tv_purchasedFeedback.getText().toString();
        final String[] gender = new String[1];
        dialog = new CustomDialog(context, R.layout.cutome_radio_group);
        dialog.setCancelable(true);
        CustomTypefaceTextView tv_subTile = (CustomTypefaceTextView) dialog.findViewById(R.id.tv_subTile_footfall_radioGroup);
        tv_subTile.setText("Have you ever purchased any item from us ?*");
        final RadioGroup rg = (RadioGroup) dialog.findViewById(R.id.radio);
        RadioButton rb_purchasedYes = (RadioButton) dialog.findViewById(R.id.rb_purchasedYes);
        rb_purchasedYes.setVisibility(View.VISIBLE);
        RadioButton rb_purchasedNo = (RadioButton) dialog.findViewById(R.id.rb_purchasedNo);
        rb_purchasedNo.setVisibility(View.VISIBLE);

        rg.clearCheck();
        if (gender_present.equalsIgnoreCase("Yes")) {
            rb_purchasedYes.setChecked(true);
        } else if (gender_present.equalsIgnoreCase("No")) {
            rb_purchasedNo.setChecked(true);
        }
        TextView btn_footfall_cancel = (TextView) dialog.findViewById(R.id.btn_footfall_cancel_radioGroup);
        TextView btn_footfall_ok = (TextView) dialog.findViewById(R.id.btn_footfall_ok_radioGroup);

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_purchasedYes:
                        gender[0] = "Yes";
                        break;
                    case R.id.rb_purchasedNo:
                        gender[0] = "No";
                        break;
                }
            }
        });

        btn_footfall_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (gender[0] != null)
                    tv_purchasedFeedback.setText(gender[0].toString());
                dialog.dismiss();
            }
        });
        dialog.show();
        btn_footfall_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    private boolean isValid() {
        if (tv_selectEmployeeSpinner.getText().toString().isEmpty()) {
            Toast.makeText(CustomerFeedbackActivity.this, "Please select Employee !", Toast.LENGTH_LONG).show();
            return false;
        } else if (tv_selectStoreFeedbackSpinner.getText().toString().isEmpty()) {
            Toast.makeText(CustomerFeedbackActivity.this, "Please select store !", Toast.LENGTH_LONG).show();
            return false;
        } else if (et_firstNameFeedback.getText().toString().isEmpty()) {
            Toast.makeText(CustomerFeedbackActivity.this, "Please enter first name !", Toast.LENGTH_LONG).show();
            return false;
        } else if (et_lastNameFeedback.getText().toString().isEmpty()) {
            Toast.makeText(CustomerFeedbackActivity.this, "Please enter last name !", Toast.LENGTH_LONG).show();
            return false;
        } else if (tv_genderFeedback.getText().toString().isEmpty()) {
            Toast.makeText(CustomerFeedbackActivity.this, "Please select gender!", Toast.LENGTH_LONG).show();
            return false;
        } else if (tv_dateOfBirthFeedback.getText().toString().isEmpty()) {
            Toast.makeText(CustomerFeedbackActivity.this, "Please select dateOfBirth !", Toast.LENGTH_LONG).show();
            return false;
        } else if (et_emailFeedback.getText().toString().isEmpty()) {
            Toast.makeText(CustomerFeedbackActivity.this, "Please enter email ID !", Toast.LENGTH_LONG).show();
            return false;
        } else if (et_mobileFeedback.getText().toString().isEmpty()) {
            Toast.makeText(CustomerFeedbackActivity.this, "Please enter Mobile number !", Toast.LENGTH_LONG).show();
            return false;
        } else if (tv_aboutFeedback.getText().toString().isEmpty()) {
            Toast.makeText(CustomerFeedbackActivity.this, "Please select hear about us !", Toast.LENGTH_LONG).show();
            return false;
        } else if (et_purposeFeedback.getText().toString().isEmpty()) {
            Toast.makeText(CustomerFeedbackActivity.this, "Please enter purpose of visit !", Toast.LENGTH_LONG).show();
            return false;
        } else if (tv_purchasedFeedback.getText().toString().isEmpty()) {
            Toast.makeText(CustomerFeedbackActivity.this, "Please select purchased item !", Toast.LENGTH_LONG).show();
            return false;
        } else if (tv_istProductFeedback.getText().toString().isEmpty()) {
            Toast.makeText(CustomerFeedbackActivity.this, "Please select product !", Toast.LENGTH_LONG).show();
            return false;
        } else if (tv_brandFeedback.getText().toString().isEmpty()) {
            Toast.makeText(CustomerFeedbackActivity.this, "Please select brand !", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private void callGetService() {
        String UserID = Util.getSharedPrefrenceValue(CustomerFeedbackActivity.this, Constant.Sp_UserID);
        String UserToken = Util.getSharedPrefrenceValue(CustomerFeedbackActivity.this, Constant.Sp_UserToken);
        System.out.println("NEWTOKEN" + UserToken + "," + UserID);
        //String URL = " http://be.greatplus.com/sheelafoam/rest/employees/details/ashish.nandodariya/UHJvamVjdEAjNDU= ";
        String URL = " http://be.greatplus.com/sheelafoam/rest/employees/details/"+UserID+"/"+UserToken;
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, URL, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                System.out.println("getresponse" + jsonObject);
                progress.dismiss();
                try {
                    if (jsonObject.getBoolean("success")) {
                        jsonObject.getString("message");
                        Log.d("getresponse", jsonObject.getString("message").toString());
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = (JSONObject) jsonArray.get(i);
                            object.getString("eMP_NAME");
                            Log.d("getresponse", object.getString("eMP_NAME").toString());
                            preference.setEMPName(object.getString("eMP_NAME"));
                            tv_selectEmployeeSpinner.setText(preference.getEMPName());
                            JSONArray array = object.getJSONArray("storeList");
                            for (int j = 0; j < array.length(); j++) {
                                JSONObject _object = (JSONObject) array.get(j);
                                _object.getString("pARENT_CHANNEL_PARTNER_NAME");
                                preference.setUserNameFootFall(_object.getString("pARENT_CHANNEL_PARTNER_NAME"));
                                tv_selectStoreFeedbackSpinner.setText(preference.getUserNameFootFall());
                                Log.d("getresponse", _object.getString("pARENT_CHANNEL_PARTNER_NAME").toString());
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(CustomerFeedbackActivity.this);
        requestQueue.add(objectRequest);
    }

    private void callSubMitFeeBack() throws JSONException {
        String UserID = Util.getSharedPrefrenceValue(CustomerFeedbackActivity.this, Constant.Sp_UserID);
        String UserToken = Util.getSharedPrefrenceValue(CustomerFeedbackActivity.this, Constant.Sp_UserToken);
        System.out.println("NEWTOKEN" + UserToken + "," + UserID);
        String URL_SUBMIT_FEEDBACK = "http://be.greatplus.com/sheelafoam/rest/services/insertCustomer/"+UserID+"/"+UserToken;
        RequestQueue queue = Volley.newRequestQueue(CustomerFeedbackActivity.this);
        Map<String, String> postParam = new HashMap<String, String>();
        postParam.put("storeId", tv_selectStoreFeedbackSpinner.getText().toString());
        postParam.put("storeName", tv_selectEmployeeSpinner.getText().toString());
        postParam.put("firstName", et_firstNameFeedback.getText().toString());
        postParam.put("lastName", et_lastNameFeedback.getText().toString());
        postParam.put("gender", tv_genderFeedback.getText().toString());
        postParam.put("email", et_emailFeedback.getText().toString());
        postParam.put("mobile", et_mobileFeedback.getText().toString().toString());
        postParam.put("howYouHeard", tv_aboutFeedback.getText().toString());
        postParam.put("havePurchasedItem", tv_purchasedFeedback.getText().toString());
        postParam.put("purposeOfVisit", et_purposeFeedback.getText().toString());
        postParam.put("currentMattressBrand", tv_brandFeedback.getText().toString());
        postParam.put("interestedIn", tv_istProductFeedback.getText().toString());
        postParam.put("dob", tv_dateOfBirthFeedback.getText().toString());
        postParam.put("ageingOfMattress", et_ageingFeedback.getText().toString());
        postParam.put("otherInterests", "N/A");
        postParam.put("otherHowHeardText", "N/A");
        postParam.put("otherCurrentMattress", "N/A");
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                URL_SUBMIT_FEEDBACK, new JSONObject(postParam),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("response" + response);
                        progress.dismiss();
                        try {
                            if (response.getBoolean("success")) {
                                response.getString("message");
                                Toast.makeText(CustomerFeedbackActivity.this, response.getString("message"), Toast.LENGTH_LONG).show();
                                et_firstNameFeedback.setText("");
                                et_lastNameFeedback.setText("");
                                tv_genderFeedback.setText("");
                                tv_dateOfBirthFeedback.setText("");
                                et_emailFeedback.setText("");
                                et_mobileFeedback.setText("");
                                tv_aboutFeedback.setText("");
                                et_purposeFeedback.setText("");
                                tv_purchasedFeedback.setText("");
                                tv_istProductFeedback.setText("");
                                tv_brandFeedback.setText("");
                                et_ageingFeedback.setText("");
                                tv_selectEmployeeSpinner.setText("");
                                tv_selectStoreFeedbackSpinner.setText("");

                            } else {

                            }

                        } catch (Exception e) {

                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                progress.dismiss();
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }

        };

        // Adding request to request queue
        queue.add(jsonObjReq);
    }
}
