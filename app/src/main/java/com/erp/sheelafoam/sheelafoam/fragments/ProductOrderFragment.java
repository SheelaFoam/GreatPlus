package com.erp.sheelafoam.sheelafoam.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.erp.sheelafoam.HomeScreen;
import com.erp.sheelafoam.OrderDashboardActivity;
import com.erp.sheelafoam.R;
import com.erp.sheelafoam.constant.Constant;
import com.erp.sheelafoam.prefrences.SheelaSharedPreference;
import com.erp.sheelafoam.sheelafoam.adapter.ColorAdapter;
import com.erp.sheelafoam.sheelafoam.adapter.DealerAdapter;
import com.erp.sheelafoam.sheelafoam.adapter.LocationListAdapter;
import com.erp.sheelafoam.sheelafoam.adapter.ProductListAdapter;
import com.erp.sheelafoam.sheelafoam.entry.ColorBean;
import com.erp.sheelafoam.sheelafoam.entry.DealerBean;
import com.erp.sheelafoam.sheelafoam.entry.LocationBean;
import com.erp.sheelafoam.sheelafoam.entry.ProductListAddEditBean;
import com.erp.sheelafoam.sheelafoam.entry.SizeBean;
import com.erp.sheelafoam.sheelafoam.helper.DecimalDigitsInputFilter;
import com.erp.sheelafoam.sheelafoam.helper.HelperMessages;
import com.erp.sheelafoam.sheelafoam.utility.ApiList;
import com.erp.sheelafoam.sheelafoam.utility.AppConstant;
import com.erp.sheelafoam.sheelafoam.utility.AsyncTaskListner;
import com.erp.sheelafoam.sheelafoam.utility.GlobalVariables;
import com.erp.sheelafoam.sheelafoam.utility.HelperMethods;
import com.erp.sheelafoam.sheelafoam.utility.MyAsyncTask;
import com.erp.sheelafoam.sheelafoam.utility.MyCustomAsyncTask;
import com.erp.sheelafoam.sheelafoam.utility.SharePref;
import com.erp.sheelafoam.utils.CustomDialog;
import com.erp.sheelafoam.utils.CustomDialog2;
import com.erp.sheelafoam.utils.Util;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.regex.Pattern;

import static android.graphics.Color.TRANSPARENT;

public class ProductOrderFragment extends Fragment implements AsyncTaskListner,
        OnClickListener, OnCheckedChangeListener {
    static CustomDialog cd;
    static CustomDialog2 cd2;
    /* String object for holding oder & delivery date & current date */
    static String order_date = "";
    private static ProductOrderFragment instance = null;
    public TextView textview_back;
    TextView textview_delivery_date_picker, textview_order_date;
    LinearLayout llayout_uom_radio_inches, llayout_uom_radio_mm,
            llayout_order_no, llayout_product_color;
    EditText edittext_product_length_text, edittext_product_width_text,
            edittext_product_thickness_text, edittext_product_quantity,
            edittext_customer_name, edittext_customer_mobile,
            edittext_product_remark;
    TextView textview_image_size1, textview_image_size2,
            textview_dealer_list, textview_dealer_name, textview_product_color,
            textview_order_no;
    TextView autocomplete_product_name, textview_location, tvRemarkText;
    String delivery_date = "", current_date = "", tomorrow_date = "", mobile_no = "", remark = "", p_encodeImage = "", customer_name = "", p_image_name = "", length = "", width = "", thickness = "";
    String dealer_id;
    String old_image_name;
    String p_image_ext = "";
    String uom = "";
    String color_name;
    String p_uom = "";
    String location_name = "";
    String location_code = "";
    String user_type = "", user_name = "", quantity = "", userRole = "",
            final_dealer_name = "", final_color_name = "", dealer_name = "", dealer_category = "",
            zone = "", product_name = "", sub_product = "",
            product_specification = "", image_url = "", old_image = "", userId;
    String requset_no = "";
    String color_applicable_yn = "";
    String order_no = "", image_length = "", image_breadth = "", current_date_time = "";
    StringBuilder ids;
    String dummyProduct[] = {"Abcd", "Acbdcc", "Bcdbcc", "EfgAbcc", "HIjcc"};
    RadioButton radio_uom_inches, radio_uom_mm, radio_uom_bdl, radio_uom_pcs;
    JSONObject jsonObjectRequest, jsonObjectResponse;
    DealerAdapter dealerAdapter;
    LocationListAdapter locationAdapter;
    ColorAdapter colorAdapter;
    ProductListAdapter productListAdapter;
    ArrayList<ColorBean> arrayList_color = new ArrayList<ColorBean>();
    ArrayList<DealerBean> arrayList_dealer = new ArrayList<DealerBean>();
    ArrayList<ProductListAddEditBean> arrayList_products = new ArrayList<ProductListAddEditBean>();
    ArrayList<ProductListAddEditBean> arrayList_products_2 = new ArrayList<ProductListAddEditBean>();
    ArrayList<LocationBean> arrayList_location = new ArrayList<LocationBean>();
    ArrayList<SizeBean> arrayList_size = new ArrayList<SizeBean>();
    SharedPreferences loacalData;
    MyCustomAsyncTask customAsyncTask;
    int flag_product, flag_color, flag_location, flag_size;
    String p_auto_size_flag = "";
    String names, phoneNo, uniqueCode, pId = "", sUniqueCode;
    private Button saveAndNewTv, SubmitTextView, btn_placeOrder;
    private Context mContext;
    private int flagEdit;
    private SharedPreferences mPrefs;
    private ImageView upload_pic;
    private int year;
    private int month;
    private int day;
    private ProgressDialog dialog;
    private Toolbar toolbar;
    private RelativeLayout relativeLayout_toolbar, back_btn;
    private String channel_partner_group = "";
    private ScrollView scrollView1;
    private Boolean order_menu = false;
    private LinearLayout rlayout_submit_buttons;
    private SheelaSharedPreference sheelaSharedPreference;
    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        @SuppressLint("NewApi")
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            try {
                DecimalFormat mFormat = new DecimalFormat("00");
                mFormat.setRoundingMode(RoundingMode.DOWN);
                delivery_date = mFormat.format(Double.valueOf(year)) + "-"
                        + mFormat.format(Double.valueOf(monthOfYear + 1)) + "-"
                        + mFormat.format(Double.valueOf(dayOfMonth));
                delivery_date = GlobalVariables.getCurrentDateInDisplayFormat(
                        getActivity(), delivery_date);
                // delivery_date = HelperMethods.getDDmmYYYY(delivery_date);
                textview_delivery_date_picker.setText(delivery_date);
                delivery_date = HelperMethods.getDDmmYYYY(delivery_date);
                Log.e("delivery_date", " = " + delivery_date);
            } catch (Exception e) {
                Log.d("exception", e.getMessage());
            }
        }

    };

    public static void defaultOneButtonDialog_serverDate(Activity activity, String msg, final TextView textview_order_date, final String date_server) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {


                    public void onClick(DialogInterface dialog, int id) {
                        textview_order_date.setText(date_server);
                        order_date = date_server;
                        dialog.cancel();
                        if (cd != null && cd.isShowing())
                            cd.dismiss();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public static ProductOrderFragment getInstance() {
        if (instance == null)
            instance = new ProductOrderFragment();
        return instance;
    }

    /*
     * ==========================================================================
     * ========== Method for getting referencing view from layout
     * ================
     * =====================================================================
     */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.fragment_order_add_new, container, false);
        //  rlayout_submit_buttons= (LinearLayout) view.findViewById(R.id.rlayout_submit_buttons);
        mContext = getActivity();
        sheelaSharedPreference = SheelaSharedPreference.getInstance(mContext);
        mPrefs = getActivity().getSharedPreferences("Location", Context.MODE_PRIVATE);
        AppConstant.IS_MM_SELECTED = false;
        AppConstant.IS_INCHES_SELECTED = false;
        getCurrentDate();
        getTomorrowDate();
        getUiObject(view);
        // randomNumber();
        if (getArguments() != null) {
            order_menu = getArguments().getBoolean("order_menu");
            System.out.println("boolean value " + order_menu);
            if (!order_menu) {
                tvRemarkText.setVisibility(View.VISIBLE);
                pId = getArguments().getString("b_id");
                names = getArguments().getString("name");
                phoneNo = getArguments().getString("mobile");
                uniqueCode = getArguments().getString("unique_code");
                sUniqueCode = getArguments().getString("short_unique_code");
                edittext_product_remark.setText(sUniqueCode);
                edittext_customer_mobile.setText(phoneNo);
                edittext_customer_name.setText(names);
                System.out.println("AnandGetBandalValue" + pId);
            }
        }

        if (order_menu) {
            relativeLayout_toolbar.setVisibility(View.VISIBLE);
            textview_back.setText("Place Order");
            back_btn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    //getActivity().onBackPressed();
                    getActivity().finish();
                }
            });
        } else if (getActivity() instanceof OrderDashboardActivity) {
            ((OrderDashboardActivity) getActivity()).textview_back.setText("Place Order");
        } else if (getActivity() instanceof HomeScreen) {
            ((HomeScreen) getActivity()).txt_title.setText("Place Order");
            ((HomeScreen) getActivity()).profile_image.setVisibility(View.GONE);
            ((HomeScreen) getActivity()).txt_title.setVisibility(View.VISIBLE);
        }


        setOnClickListner();
        current_date_time = HelperMethods.getCurrentTime();
        Log.e("current date & time", current_date_time);

        edittext_product_length_text.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub
                if (!hasFocus) {

                    length = edittext_product_length_text.getText().toString().trim();
                    convertLength(length);
                }
            }
        });

        edittext_product_width_text.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub
                if (!hasFocus) {

                    width = edittext_product_width_text.getText().toString().trim();
                    convertWidth(width);

                }
            }
        });


        // Constructor of CustomAsyncTask for dialog
        customAsyncTask = new MyCustomAsyncTask(getActivity(), ProductOrderFragment.this);

        try {
            if (arrayList_dealer.size() > 0) {
                arrayList_dealer.clear();
            }
            if (arrayList_products.size() > 0) {
                arrayList_products.clear();
            }

        } catch (Exception e) {

        }
        ids = new StringBuilder();
        loacalData = getActivity().getSharedPreferences(SharePref.MODE_TYPE,
                Context.MODE_PRIVATE);
        user_type = mPrefs.getString(AppConstant.USER_TYPE, null);
        userRole = Util.getSharedPrefrenceValue(getActivity(), Constant.Sp_User_Type);
        user_name = mPrefs.getString(AppConstant.USER_NAME, null);
        location_code = mPrefs.getString("p_location_code", "");
        location_name = mPrefs.getString("p_location_name", "");
        textview_location.setText(location_name);
        if (Util.getSharedPrefrenceValue(getActivity(), Constant.Sp_User_Type).equals("DEALER")) {
            getLocationData(Util.getSharedPrefrenceValue(getActivity(), Constant.Sp_DealerID));
        }
        Log.d("New User Types", Util.getSharedPrefrenceValue(getActivity(), Constant.Sp_User_Type) + "and " + Util.getSharedPrefrenceValue(getActivity(), Constant.Sp_DealerID));
        Log.d("user_type", mPrefs.getString("op_user_type", "") + "---" + mPrefs.getString("op_user_name", "").toString());
        //if(mPrefs.getString("op_user_type", "").equals("DEALER")){
        if (Util.getSharedPrefrenceValue(getActivity(), Constant.Sp_User_Type).equals("DEALER")) {
            //textview_dealer_list.setText(mPrefs.getString("op_user_name","").toString());
            textview_dealer_list.setText(Util.getSharedPrefrenceValue(getActivity(), Constant.Op_user_name));
            //final_dealer_name=mPrefs.getString("op_user_name","").toString();
            final_dealer_name = Util.getSharedPrefrenceValue(getActivity(), Constant.Op_user_name);
            //dealer_category = mPrefs.getString("p_dealer_category", "");
            dealer_category = mPrefs.getString("DEALER_CATEGORY", "");
            // calling Location API
            //dealer_id=mPrefs.getString("DEALER_ID", "");
            dealer_id = Util.getSharedPrefrenceValue(getActivity(), Constant.Sp_DealerID);
            dealer_name = mPrefs.getString("DEALER_NAME", "");

        } else {

            getDealers();


        }

        textview_dealer_list.setVisibility(View.VISIBLE);

        if (AppConstant.isNewFoam) {
            textview_dealer_list.setText(dealer_name);
        }

//        edittext_customer_name.setText(names);
//        edittext_customer_mobile.setText(phoneNo);

        view.setOnClickListener(null);
        try {
            if (!TextUtils.isEmpty(sheelaSharedPreference.getLastOrderStatus()) &&
                    !sheelaSharedPreference.getLastProduct().isNull("p_product_display_name")) {
                JSONObject jsonObject= sheelaSharedPreference.getLastProduct();
                showMessageAlert("Your order for Product" + jsonObject.getString("p_product_display_name")
                        +"Quantity "+jsonObject.getString("p_qty") +" was incomplete. Please connect with customer" +
                        " care or check Order " +
                        "history to avoid duplicate order.",false);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return view;

    }


    private void setImageNameInSharePrefrenshed(String name, String url) {
        // TODO Auto-generated method stub

        Editor editor = mPrefs.edit();

        editor.putString("imageName", name);

        editor.putString("PROFILE_PHOTO", url);

        editor.commit();

    }

    @SuppressLint("SimpleDateFormat")
    private void getTomorrowDate() {

        String newdate = DateFormat.getDateTimeInstance().format(new Date());
        Log.d("newdate", newdate);
        // TODO Auto-generated method stub

        Calendar calendar = Calendar.getInstance();
        Date today = calendar.getTime();

        calendar.add(Calendar.DAY_OF_MONTH, 1);
        Date tomorrow = calendar.getTime();

        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        String todayAsString = dateFormat.format(today);
        String tomorrowAsString = dateFormat.format(tomorrow);

        // tomorrow_date =
        // HelperMethods.getDDmmYYYY(tomorrowAsString.toString());
        // current_date = HelperMethods.getDDmmYYYY(todayAsString.toString());

        tomorrow_date = tomorrowAsString.toString();
        current_date = todayAsString.toString();

        Log.e("current_date", " = " + current_date);
        Log.e("tomorrow_date", " = " + tomorrow_date);

        delivery_date = tomorrow_date;
        order_date = current_date;

        System.out.println(tomorrowAsString);

    }

    private void getUiObject(View view) {
        try {
            // current_date=mPrefs.getString("CURRENT_DATE", "");
            Log.e("mprefs", " = " + current_date);
            btn_placeOrder = (Button) view.findViewById(R.id.btn_placeOrder);
            //saveAndNewTv = (Button) view.findViewById(R.id.SheelaFoam_ID_PlaceOrder_SaveAndNewTV);
            scrollView1 = (ScrollView) view.findViewById(R.id.scrollView1);
            textview_order_date = (TextView) view.findViewById(R.id.textview_order_date);
            // textview_order_date.setText(HelperMethods.getYyMmDd(current_date));
            textview_order_date.setText(current_date);
            textview_delivery_date_picker = (TextView) view.findViewById(R.id.textview_delivery_date_picker);
            // textview_delivery_date_picker.setText(HelperMethods.getYyMmDd(tomorrow_date));
            textview_delivery_date_picker.setText(tomorrow_date);
            Log.e("mprefs", " = " + tomorrow_date);
            upload_pic = (ImageView) view.findViewById(R.id.upload_pic);
            // SubmitTextView = (Button) view.findViewById(R.id.SubmitTextView);
            textview_product_color = (TextView) view.findViewById(R.id.textview_product_color);
            llayout_product_color = (LinearLayout) view.findViewById(R.id.llayout_product_color);
            textview_dealer_list = (TextView) view.findViewById(R.id.textview_dealer_list);
            textview_dealer_name = (TextView) view.findViewById(R.id.textview_dealer_name);
            textview_location = (TextView) view.findViewById(R.id.textview_location);
            radio_uom_mm = (RadioButton) view.findViewById(R.id.radio_uom_mm);
            radio_uom_inches = (RadioButton) view.findViewById(R.id.radio_uom_inches);
            radio_uom_bdl = (RadioButton) view.findViewById(R.id.radio_uom_bdl);
            radio_uom_pcs = (RadioButton) view.findViewById(R.id.radio_uom_pcs);
            p_uom = "Pcs";
            edittext_product_length_text = (EditText) view.findViewById(R.id.edittext_product_length_text);
            edittext_product_width_text = (EditText) view.findViewById(R.id.edittext_product_width_text);
            edittext_product_thickness_text = (EditText) view.findViewById(R.id.edittext_product_thickness_text);
            edittext_product_length_text.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(4, 2)});
            edittext_product_width_text.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(4, 2)});
            edittext_product_thickness_text.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(4, 2)});
            edittext_product_length_text.setRawInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            edittext_product_width_text.setRawInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            edittext_product_quantity = (EditText) view.findViewById(R.id.edittext_product_quantity);
            edittext_customer_name = (EditText) view.findViewById(R.id.edittext_customer_name);
            edittext_customer_mobile = (EditText) view.findViewById(R.id.edittext_customer_mobile);
            edittext_product_remark = (EditText) view.findViewById(R.id.edittext_product_remark);
            textview_image_size1 = (TextView) view.findViewById(R.id.textview_image_size1);
            textview_image_size2 = (TextView) view.findViewById(R.id.textview_image_size2);
            autocomplete_product_name = (TextView) view.findViewById(R.id.autocomplete_product_name);
            textview_order_no = (TextView) view.findViewById(R.id.textview_order_no);
            llayout_order_no = (LinearLayout) view.findViewById(R.id.llayout_order_no);
            tvRemarkText = (TextView) view.findViewById(R.id.tv_remark_text);
            // order_view = (TextView)view.findViewById(R.id.order_view);
            relativeLayout_toolbar = (RelativeLayout) view.findViewById(R.id.rlheader);
            textview_back = (TextView) view.findViewById(R.id.title_text);
            back_btn = (RelativeLayout) view.findViewById(R.id.back_btn);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /*
     * ==========================================================================
     * ========== Method for getting current date that will be used in order
     * date & delivery date
     *
     * ==========================================================================
     * ===========
     */
    public void getCurrentDate() {

        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        /*
         * hours = c.get(Calendar.HOUR_OF_DAY); min = c.get(Calendar.MINUTE);
         *
         * current_hour= c.get(Calendar.HOUR); current_min =
         * c.get(Calendar.MINUTE);
         */
        Log.e("Date", " = " + day);

    }

    private void setOnClickListner() {
        textview_delivery_date_picker.setOnClickListener(this);
        upload_pic.setOnClickListener(this);
        radio_uom_inches.setOnCheckedChangeListener(this);
        radio_uom_mm.setOnCheckedChangeListener(this);
        radio_uom_bdl.setOnCheckedChangeListener(this);
        radio_uom_pcs.setOnCheckedChangeListener(this);
        //  SubmitTextView.setOnClickListener(this);

        textview_dealer_list.setOnClickListener(this);
        textview_product_color.setOnClickListener(this);
        autocomplete_product_name.setOnClickListener(this);
        textview_location.setOnClickListener(this);
        //  saveAndNewTv.setOnClickListener(this);
        // order_view.setOnClickListener(this);
        btn_placeOrder.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_placeOrder:
                try {
                    if (!TextUtils.isEmpty(sheelaSharedPreference.getLastOrderStatus()) &&
                            !sheelaSharedPreference.getLastProduct().isNull("p_product_display_name")) {
                        JSONObject jsonObject= sheelaSharedPreference.getLastProduct();
                        showMessageAlert("Your order for Product" + jsonObject.getString("p_product_display_name")
                                +"Quantity "+jsonObject.getString("p_qty") +" was incomplete. Please connect with customer" +
                                " care or check Order " +
                                "history to avoid duplicate order.",false);
                    } else {
                        getData();
                        if (validateData()) {
                            customDialogOld();
                            System.out.println("call");
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.upload_pic:
                // for submit
                // uploadProfile.encodeImage
                break;
            case R.id.textview_dealer_list:
                flag_location = 0;
                // dialogs_Dealer();
                //Commented below line to get variable value from new login
                //if(mPrefs.getString("op_user_type", "").equals("DEALER"))
                if (Util.getSharedPrefrenceValue(getActivity(), Constant.Sp_User_Type).equals("DEALER")) {
                } else {
                    dialog_Dealer_New();
                }
                break;
            case R.id.textview_location:
                HelperMethods.hideSoftKeyboard(getActivity());
                flag_location = 1;
                // getProducts();	Log.e("Product_Array_Size", " =" + arrayList_products.size());
                if (!textview_location.getText().toString().trim()
                        .equals("Please Select")
                        && AppConstant.EDIT_ORDER == 0) {
                    //Toast.makeText(getActivity(), "if block one", Toast.LENGTH_LONG).show();
                    if (textview_dealer_list.getText().toString().equalsIgnoreCase("Dealer Name")) {
                        Toast.makeText(getActivity(),
                                "Please Select Dealer Name First", Toast.LENGTH_LONG)
                                .show();
                    } else {
                        dialogs_location();
                    }
                } else if (!textview_location.getText().toString().trim()
                        .equals("Please Select")
                        && AppConstant.EDIT_ORDER == 1) {
				/*getProducts(textview_dealer_list.getText().toString().trim(),
						AppConstant.DEALER_ZONE);*/
                    getLocationData(dealer_id);
                } else if (arrayList_location.size() > 0) {
                    Log.e("location list", "" + arrayList_location);
                    //Toast.makeText(getActivity(), "if block two", Toast.LENGTH_LONG).show();
                    dialogs_location();
                } else if (arrayList_location.size() == 0
                        && AppConstant.EDIT_ORDER == 1) {
				/*getProducts(textview_dealer_list.getText().toString().trim(),
						AppConstant.DEALER_ZONE);
				*/
                    getLocationData(dealer_id);
                    Log.e("product list", "" + arrayList_products);
                } else {
                    Toast.makeText(getActivity(),
                            "Please Select Dealer Name First", Toast.LENGTH_LONG)
                            .show();
                }
                break;
            case R.id.textview_delivery_date_picker:
                HelperMethods.hideSoftKeyboard(getActivity());
                datePicker();
                break;
            case R.id.textview_product_color:
                HelperMethods.hideSoftKeyboard(getActivity());
                flag_color = 1;
                if (!autocomplete_product_name.getText().toString().trim()
                        .equals("Please Select")
                        && AppConstant.EDIT_ORDER == 0) {
                    //Toast.makeText(getActivity(), "if block one", Toast.LENGTH_LONG).show();
                    if (arrayList_color.size() == 0) {
                        Toast.makeText(getActivity(),
                                "Please Select Product Name First",
                                Toast.LENGTH_LONG).show();
                    } else {
                        dialogs_Color();
                    }
                } else if (arrayList_color.size() > 0
                        && AppConstant.EDIT_ORDER == 1
                        && !autocomplete_product_name.getText().toString().trim()
                        .equals("Please Select")) {
                    //Toast.makeText(getActivity(), "if block two", Toast.LENGTH_LONG).show();
                    dialogs_Color();
                } else if (arrayList_color.size() == 0 && AppConstant.EDIT_ORDER == 1) {
                    product_name = autocomplete_product_name.getText().toString()
                            .trim();
                    getColor();
                } else if (!textview_product_color.getText().toString().trim()
                        .equals("Please Select")) {
                    product_name = autocomplete_product_name.getText().toString()
                            .trim();
                    getColor();
                } else {
                    if (textview_dealer_list.getText().toString().trim()
                            .equals("Please Select")) {
                        Toast.makeText(getActivity(),
                                "Please Select Dealer Name First",
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getActivity(),
                                "Please Select Product Name First",
                                Toast.LENGTH_LONG).show();
                    }
                }
                break;
            case R.id.autocomplete_product_name:
                HelperMethods.hideSoftKeyboard(getActivity());
                flag_product = 1;
                flag_size = 1;
                // getProducts();	Log.e("Product_Array_Size", " =" + arrayList_products.size());
                if (order_menu.equals(true)) {
                    // hardProductDialog();
                    if (arrayList_products.size() == 0) {
                        if (!textview_dealer_list.getText().toString().trim().equals("Please Select") && AppConstant.EDIT_ORDER == 0) {
                            if (final_dealer_name.length() == 0) {
                                Toast.makeText(getActivity(), "Please Select Dealer Name First", Toast.LENGTH_LONG).show();
                            } else {
                                if (textview_location.getText().toString().trim().length() > 0) {
                                    dialogs_Product();
                                } else {
                                    Toast.makeText(getActivity(),
                                            "Please Select Supply From First", Toast.LENGTH_LONG)
                                            .show();
                                }
                            }
                        } else {
                            dialogs_Product();
                        }
                    } else if (!textview_dealer_list.getText().toString().trim()
                            .equals("Please Select")
                            && AppConstant.EDIT_ORDER == 1) {
                        getProducts(textview_dealer_list.getText().toString().trim(),
                                AppConstant.DEALER_ZONE);
                    } else if (arrayList_products.size() > 0) {
                        Log.e("product list", "" + arrayList_products);
                        //Toast.makeText(getActivity(), "if block two", Toast.LENGTH_LONG).show();
                        dialogs_Product();
                    } else if (arrayList_products.size() == 0
                            && AppConstant.EDIT_ORDER == 1) {
                        getProducts(textview_dealer_list.getText().toString().trim(),
                                AppConstant.DEALER_ZONE);
                        Log.e("product list", "" + arrayList_products);
                    } else {
                        Toast.makeText(getActivity(),
                                "Please Select Dealer Name First", Toast.LENGTH_LONG)
                                .show();
                    }

                } else if (getArguments() != null) {
                    hardProductDialog();
                } else if (!textview_dealer_list.getText().toString().trim().equals("Please Select") && AppConstant.EDIT_ORDER == 0) {
                    //Toast.makeText(getActivity(), "if block one", Toast.LENGTH_LONG).show();
                    if (arrayList_products.size() == 0) {
                        if (final_dealer_name.length() == 0) {
                            Toast.makeText(getActivity(), "Please Select Dealer Name First", Toast.LENGTH_LONG).show();
                        } else {
                            if (textview_location.getText().toString().trim().length() > 0) {
                                dialogs_Product();
                            } else {
                                Toast.makeText(getActivity(),
                                        "Please Select Supply From First", Toast.LENGTH_LONG)
                                        .show();
                            }
                        }
                    } else {
                        dialogs_Product();
                    }

                } else if (!textview_dealer_list.getText().toString().trim()
                        .equals("Please Select")
                        && AppConstant.EDIT_ORDER == 1) {
                    getProducts(textview_dealer_list.getText().toString().trim(),
                            AppConstant.DEALER_ZONE);
                } else if (arrayList_products.size() > 0) {
                    Log.e("product list", "" + arrayList_products);
                    //Toast.makeText(getActivity(), "if block two", Toast.LENGTH_LONG).show();
                    dialogs_Product();
                } else if (arrayList_products.size() == 0
                        && AppConstant.EDIT_ORDER == 1) {
                    getProducts(textview_dealer_list.getText().toString().trim(),
                            AppConstant.DEALER_ZONE);
                    Log.e("product list", "" + arrayList_products);
                } else {
                    Toast.makeText(getActivity(),
                            "Please Select Dealer Name First", Toast.LENGTH_LONG)
                            .show();
                }

                break;
            default:
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            switch (buttonView.getId()) {
                case R.id.radio_uom_bdl:
                    radio_uom_pcs.setChecked(false);
                    p_uom = "Bdl";
                    Log.e("p_uom", " =" + p_uom);
                    break;
                case R.id.radio_uom_pcs:
                    radio_uom_bdl.setChecked(false);
                    p_uom = "Pcs";
                    Log.e("p_uom", " =" + p_uom);
                    break;
                case R.id.radio_uom_mm:
                    radio_uom_inches.setChecked(false);
                    if (!AppConstant.IS_MM_SELECTED) {
                        convertInchesToMm();
                    }
                    AppConstant.IS_MM_SELECTED = true;
                    AppConstant.IS_INCHES_SELECTED = false;
                    uom = "mm";
                    Log.e("uom", " =" + uom);
                    /**
                     * Conversion of inches to mm
                     * **/
                    break;
                case R.id.radio_uom_inches:
                    radio_uom_mm.setChecked(false);
                    if (!AppConstant.IS_INCHES_SELECTED) {
                        convertMmToInches();
                    }
                    AppConstant.IS_MM_SELECTED = false;
                    AppConstant.IS_INCHES_SELECTED = true;
                    uom = "inches";
                    Log.e("uom", " =" + uom);
                    /**
                     * Conversion of mm to inches
                     * **/
                    break;
            }
        }
    }

    public void datePicker() {
        DatePickerDialog _date = new DatePickerDialog(getActivity(),
                datePickerListener, year, month, day);
        _date.getDatePicker().setMinDate(
                System.currentTimeMillis() + 1000 * 24 * 60 * 60);
        _date.getDatePicker().setMaxDate(
                System.currentTimeMillis() + 20 * 1000 * 24 * 60 * 60);
        _date.show();
        _date.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == DialogInterface.BUTTON_NEGATIVE) {

                        }
                    }
                });
    }

    public void getColor() {
        if (HelperMethods.isNetworkAvailable(getActivity())) {
            requset_no = "2";
            jsonObjectRequest = new JSONObject();
            try {
                jsonObjectRequest.put("request", ApiList.API_PRODUCT_COLOR);
                jsonObjectRequest.put("p_product_name", product_name);
                jsonObjectRequest.put("p_zone", AppConstant.DEALER_ZONE);
                Log.e("request2", "" + jsonObjectRequest.toString());
                customAsyncTask.showDialog(getActivity());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            new MyCustomAsyncTask(getActivity(), ProductOrderFragment.this,
                    jsonObjectRequest, ApiList.URL_PRODUCTS).execute();
        } else {
            GlobalVariables.defaultOneButtonDialog(getActivity(),
                    "Network error");
        }
    }

    public void getDealers() {
        if (HelperMethods.isNetworkAvailable(getActivity())) {
            requset_no = "3";
            jsonObjectRequest = new JSONObject();
            try {
                //jsonObjectRequest.put("p_territory", AppConstant.USER_TERRITORY);
                //jsonObjectRequest.put("p_role_name", mPrefs.getString("op_user_type", ""));
                if (Util.getSharedPrefrenceValue(getActivity(), Constant.Sp_User_Type).equals("EMPLOYEE")) {
                    jsonObjectRequest.put("old", "1");
                    jsonObjectRequest.put("op_user_role_name", Util.getSharedPrefrenceValue(getActivity(), Constant.Sp_op_user_role_name));
                    jsonObjectRequest.put("op_greatplus_user_id", Util.getSharedPrefrenceValue(getActivity(), Constant.Sp_GrtPlusUserID));
                } else {
                    jsonObjectRequest.put("request", ApiList.API_PRODUCT_DEALERS);
                    jsonObjectRequest.put("p_role_name", Util.getSharedPrefrenceValue(getActivity(), Constant.Sp_User_Type));
                    jsonObjectRequest.put("p_dealer_id", dealer_id);
                }
                Log.e("request3", "" + jsonObjectRequest.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            customAsyncTask.showDialog(getActivity());
            if (Util.getSharedPrefrenceValue(getActivity(), Constant.Sp_User_Type).equals("EMPLOYEE")) {
                new MyCustomAsyncTask(getActivity(), ProductOrderFragment.this, jsonObjectRequest, Constant.WS_URL + Constant.WS_DEALER_LIST).execute();
            } else {
                new MyCustomAsyncTask(getActivity(), ProductOrderFragment.this,
                        jsonObjectRequest, ApiList.URL_PRODUCTS).execute();
            }
        } else {
            GlobalVariables.defaultOneButtonDialog(getActivity(),
                    "Network error");
        }
    }

    public void getProducts(String p_dealer_name, String p_zone) {
        if (arrayList_products.size() > 0) {
            Log.e("product list", "" + arrayList_products);
            dialogs_Product();
        } else if (HelperMethods.isNetworkAvailable(getActivity())) {
            requset_no = "4";
            jsonObjectRequest = new JSONObject();
            try {
                jsonObjectRequest.put("request", ApiList.API_PRODUCTS);
                jsonObjectRequest.put("p_dealer_name", p_dealer_name);
                jsonObjectRequest.put("p_zone", p_zone);
                if (Util.getSharedPrefrenceValue(getActivity(), Constant.Sp_User_Type).equals("DEALER")) {
                    Log.d("true", "" + mPrefs.getString("DEALER_CATEGORY", ""));
                    jsonObjectRequest.put("p_dealer_category", Util.getSharedPrefrenceValue(getActivity(), Constant.Sp_Dealer_Category));
                } else {
                    Log.d("false", "" + dealer_category);
                    jsonObjectRequest.put("p_dealer_category", dealer_category);
                }
                Log.e("request4", "" + jsonObjectRequest.toString());
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            customAsyncTask.showDialog(getActivity());
            new MyCustomAsyncTask(getActivity(), ProductOrderFragment.this,
                    jsonObjectRequest, ApiList.URL_PRODUCTS).execute();

        } else {
            customAsyncTask.showDialog(getActivity());
            GlobalVariables.defaultOneButtonDialog(getActivity(),
                    "Network error");
        }

    }


    public void placeOrder_2() {
        if (HelperMethods.isNetworkAvailable(getActivity())) {
            requset_no = "1";
            jsonObjectRequest = new JSONObject();
            try {
                jsonObjectRequest.put("request", ApiList.API_PRODUCT_ORDER_2);
                jsonObjectRequest.put("p_dealer_name", mPrefs.getString("DEALER_NAME", ""));
                jsonObjectRequest.put("p_dealer_id", mPrefs.getString("DEALER_ID", ""));
                jsonObjectRequest.put("p_channel_partner_group", channel_partner_group);
                jsonObjectRequest.put("p_dealer_category", GlobalVariables.DEALER_CATEGORY);
                jsonObjectRequest.put("p_location_code", location_code);
                jsonObjectRequest.put("p_location_name", location_name);
                jsonObjectRequest.put("p_product_display_name", product_name);
                jsonObjectRequest.put("p_color_applicable_yn", color_applicable_yn);
                jsonObjectRequest.put("p_length", length);
                jsonObjectRequest.put("p_breadth", width);
                jsonObjectRequest.put("p_thick", thickness);
                jsonObjectRequest.put("p_colour", final_color_name);
                jsonObjectRequest.put("p_uom", p_uom);
                jsonObjectRequest.put("p_auto_size_flag", p_auto_size_flag);
                jsonObjectRequest.put("p_qty", quantity);
                jsonObjectRequest.put("p_remark", remark);
                jsonObjectRequest.put("p_customer_name", customer_name);
                jsonObjectRequest.put("p_customer_mobile", mobile_no);
                jsonObjectRequest.put("p_delivery_date", HelperMethods.getDDMMMyyyy(delivery_date));
                jsonObjectRequest.put("p_captured_image", p_image_name);
                jsonObjectRequest.put("p_captured_length", "");
                jsonObjectRequest.put("p_captured_bredth", "");
                String randomNumber = TextUtils.isEmpty(sheelaSharedPreference.getLastOrderStatus()) ? randomNumber() : sheelaSharedPreference.getLastOrderStatus();
                sheelaSharedPreference.setLastOrderStatus(randomNumber);
                jsonObjectRequest.put("p_request_id", randomNumber);//---------------
                if (p_encodeImage.length() > 0)
                    jsonObjectRequest.put("p_ext", "png");
                jsonObjectRequest.put("p_order_date", HelperMethods.getDDMMMyyyy(order_date));
                jsonObjectRequest.put("p_captured_image_binary", p_encodeImage);
                Log.e("#Date format#", "" + HelperMethods.getDDMMMyyyy(delivery_date) + " "
                        + HelperMethods.getDDMMMyyyy(order_date));
                Log.e("Anand", "" + jsonObjectRequest.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            MyAsyncTask asyncTask = new MyAsyncTask(getActivity(), ProductOrderFragment.this, jsonObjectRequest, ApiList.URL_PLACE_ORDER);
            asyncTask.execute();
        } else {
            GlobalVariables.defaultOneButtonDialog(getActivity(),
                    "Network error");
        }

    }


    private String randomNumber() {
        DateFormat dateFormat = new SimpleDateFormat("MMddyyyyHH:mm:ss");
        Random random = new Random();
        StringBuilder sb = new StringBuilder(dateFormat.format(new Date()));
        sb.append("_" + Util.getSharedPrefrenceValue(getActivity(), Constant.Sp_UserID).substring(7));
        sb.append("_" + random.nextInt(900));
        return sb.toString();
    }

    public synchronized void placeOrder() {
        if (HelperMethods.isNetworkAvailable(getActivity())) {
            // getData();
            requset_no = "1";
            jsonObjectRequest = new JSONObject();
            try {
                jsonObjectRequest.put("request", ApiList.API_PRODUCT_ORDER);
                jsonObjectRequest.put("p_dealer_name", mPrefs.getString("DEALER_NAME", ""));
                jsonObjectRequest.put("p_dealer_id", mPrefs.getString("DEALER_ID", ""));
                jsonObjectRequest.put("p_channel_partner_group", channel_partner_group);
                jsonObjectRequest.put("p_dealer_category", GlobalVariables.DEALER_CATEGORY);
                jsonObjectRequest.put("p_location_code", location_code);
                jsonObjectRequest.put("p_location_name", location_name);
                jsonObjectRequest.put("p_product_display_name", product_name);
                jsonObjectRequest.put("p_color_applicable_yn", color_applicable_yn);
                jsonObjectRequest.put("p_length", length);
                jsonObjectRequest.put("p_breadth", width);
                jsonObjectRequest.put("p_thick", thickness);
                jsonObjectRequest.put("p_colour", final_color_name);
                jsonObjectRequest.put("p_uom", p_uom);
                jsonObjectRequest.put("p_auto_size_flag", p_auto_size_flag);
                jsonObjectRequest.put("p_qty", quantity);
                jsonObjectRequest.put("p_remark", remark);
                jsonObjectRequest.put("p_customer_name", customer_name);
                jsonObjectRequest.put("p_customer_mobile", mobile_no);
                jsonObjectRequest.put("p_delivery_date", HelperMethods.getDDMMMyyyy(delivery_date));
                jsonObjectRequest.put("p_captured_image", p_image_name);
                jsonObjectRequest.put("p_captured_length", "");
                jsonObjectRequest.put("p_captured_bredth", "");
                if (p_encodeImage.length() > 0)
                    jsonObjectRequest.put("p_ext", "png");
                jsonObjectRequest.put("p_order_date",
                        HelperMethods.getDDMMMyyyy(order_date));
				/*jsonObjectRequest.put("p_order_date",
						"01/18/2016 05:08:08 PM");*/
                jsonObjectRequest.put("p_captured_image_binary", p_encodeImage);
                Log.e("#Date format#",
                        "" + HelperMethods.getDDMMMyyyy(delivery_date) + " "
                                + HelperMethods.getDDMMMyyyy(order_date));
                Log.e("jitin", "" + jsonObjectRequest.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            MyAsyncTask asyncTask = new MyAsyncTask(getActivity(), ProductOrderFragment.this, jsonObjectRequest, ApiList.URL_PLACE_ORDER);
            asyncTask.execute();
        } else {
            GlobalVariables.defaultOneButtonDialog(getActivity(),
                    "Network error");
        }
    }
    private void getData() {
        order_date = textview_order_date.getText().toString();
        delivery_date = textview_delivery_date_picker.getText().toString();
        mobile_no = edittext_customer_mobile.getText().toString();
        remark = edittext_product_remark.getText().toString();
        customer_name = edittext_customer_name.getText().toString();
        length = edittext_product_length_text.getText().toString();
        width = edittext_product_width_text.getText().toString();
        thickness = edittext_product_thickness_text.getText().toString();
        product_name = autocomplete_product_name.getText().toString();
        quantity = edittext_product_quantity.getText().toString();
        final_color_name = textview_product_color.getText().toString();
    }

    public boolean validateData() {
        if (order_date.length() == 0
                || textview_order_date.getText().toString().trim().length() == 0) {
            GlobalVariables.defaultOneButtonDialog(getActivity(),
                    "Order date can't be empty");
            // cd.dismiss();
            return false;
        }
        if (final_dealer_name.length() == 0
                || textview_dealer_list.getText().toString().trim()
                .equals("Please Select")) {
            //cd.dismiss();
            GlobalVariables.defaultOneButtonDialog(getActivity(),
                    HelperMessages.MESSAGE_DEALER_NAME);
            // textview_dealer_list.setFocusableInTouchMode(true);
            // textview_dealer_list.requestFocus();
            return false;
        } else if (product_name.length() == 0
                || autocomplete_product_name.getText().toString().trim()
                .equals("Please Select")) {
            // cd.dismiss();
            GlobalVariables.defaultOneButtonDialog(getActivity(),
                    HelperMessages.MESSAGE_PRODUCT_NAME);
            return false;
        } else if (!check_remark_upmm()) {
            return false;
        } else if (textview_location.getText().toString().trim()
                .equals("Please Select") || textview_location.getText().toString().trim().length() == 0) {
            GlobalVariables.defaultOneButtonDialog(getActivity(),
                    HelperMessages.MESSAGE_LOCATION_NAME);
            // cd.dismiss();
            return false;
        } else if (color_applicable_yn.equals("1") && textview_product_color.getText().toString().trim().length() == 0) {
            GlobalVariables.defaultOneButtonDialog(getActivity(),
                    HelperMessages.MESSAGE_PRODUCT_COLOR);
            //cd.dismiss();
            return false;
        } else if (length.length() == 0) {
            GlobalVariables.defaultOneButtonDialog(getActivity(),
                    HelperMessages.MESSAGE_PRODUCT_LENGTH);
            edittext_product_length_text.requestFocus();
            int smoothY = (int) getActivity().getResources().getDimension(
                    R.dimen.kpi_percent_text_size);
            scrollView1.smoothScrollBy(0, -smoothY);
            // cd.dismiss();
            return false;
        } else if (edittext_product_length_text.getText().toString().trim()
                .equals("0")
                || edittext_product_length_text.getText().toString().trim()
                .equals("00")
                || edittext_product_length_text.getText().toString().trim()
                .equals("000")
                || edittext_product_length_text.getText().toString().trim()
                .equals("0000")) {
            GlobalVariables.defaultOneButtonDialog(getActivity(),
                    HelperMessages.MESSAGE_LENGTH_GREATER_ZERO);
            edittext_product_length_text.requestFocus();
            int smoothY = (int) getActivity().getResources().getDimension(
                    R.dimen.kpi_percent_text_size);
            scrollView1.smoothScrollBy(0, -smoothY);
            //cd.dismiss();
            return false;
        } else if (width.length() == 0) {
            GlobalVariables.defaultOneButtonDialog(getActivity(),
                    HelperMessages.MESSAGE_PRODUCT_WIDTH);
            edittext_product_width_text.requestFocus();
            int smoothY = (int) getActivity().getResources().getDimension(
                    R.dimen.kpi_percent_text_size);
            scrollView1.smoothScrollBy(0, -smoothY);
            //cd.dismiss();
            return false;
        } else if (edittext_product_width_text.getText().toString().trim()
                .equals("0")
                || edittext_product_width_text.getText().toString().trim()
                .equals("00")
                || edittext_product_width_text.getText().toString().trim()
                .equals("000")
                || edittext_product_width_text.getText().toString().trim()
                .equals("0000")) {
            GlobalVariables.defaultOneButtonDialog(getActivity(),
                    HelperMessages.MESSAGE_BREADTH_GREATER_ZERO);
            edittext_product_width_text.requestFocus();
            int smoothY = (int) getActivity().getResources().getDimension(
                    R.dimen.kpi_percent_text_size);
            scrollView1.smoothScrollBy(0, -smoothY);
            //cd.dismiss();
            return false;
        } else if (Double.parseDouble(width) > Double.parseDouble(length) && p_auto_size_flag.equals("0")) {
            GlobalVariables.defaultOneButtonDialog(getActivity(),
                    HelperMessages.MESSAGE_LENGTH_GREATER_WIDTH);
            edittext_product_length_text.requestFocus();
            int smoothY = (int) getActivity().getResources().getDimension(
                    R.dimen.kpi_percent_text_size);
            scrollView1.smoothScrollBy(0, -smoothY);
            //cd.dismiss();
            return false;
        }
        /*
         * else if (final_color_name.length()==0 ||
         * textview_product_color.getText
         * ().toString().trim().equals("Please Select")){
         *
         * GlobalVariables.defaultOneButtonDialog(getActivity(),
         * "Pls select color"); return false; }
         */
        else if (p_uom.length() == 0) {
            GlobalVariables.defaultOneButtonDialog(getActivity(),
                    "Pls select quantity type");
            //cd.dismiss();
            return false;
        } else if (quantity.length() == 0) {
            GlobalVariables.defaultOneButtonDialog(getActivity(),
                    HelperMessages.MESSAGE_QUANTITY);
            edittext_product_quantity.requestFocus();
            int smoothY = (int) getActivity().getResources().getDimension(
                    R.dimen.kpi_percent_text_size);
            scrollView1.smoothScrollBy(0, -smoothY);
            //cd.dismiss();
            return false;
        } else if (edittext_product_quantity.getText().toString().trim()
                .equals("0")
                || edittext_product_quantity.getText().toString().trim()
                .equals("00")
                || edittext_product_quantity.getText().toString().trim()
                .equals("000")
                || edittext_product_quantity.getText().toString().trim()
                .equals("0000")) {
            GlobalVariables.defaultOneButtonDialog(getActivity(),
                    HelperMessages.MESSAGE_QUANTITY_GREATER_ZERO);
            edittext_product_quantity.requestFocus();
            int smoothY = (int) getActivity().getResources().getDimension(
                    R.dimen.kpi_percent_text_size);
            scrollView1.smoothScrollBy(0, -smoothY);
            //cd.dismiss();
            return false;
        }
		/*else if (delivery_date.length() == 0) {
			GlobalVariables.defaultOneButtonDialog(getActivity(),
					"Pls enter delivery date");
			return false;
		}*/
        else if (mobile_no.length() != 0 && mobile_no.length() != 10) {
            GlobalVariables.defaultOneButtonDialog(getActivity(),
                    HelperMessages.MESSAGE_MOBILE_NO);
            edittext_customer_mobile.requestFocus();
            int smoothY = (int) getActivity().getResources().getDimension(
                    R.dimen.kpi_percent_text_size);
            scrollView1.smoothScrollBy(0, -smoothY);
            //cd.dismiss();
            return false;
        }
		/*else if (thickness.length() == 0 && p_auto_size_flag.equals("0")) {
				GlobalVariables.defaultOneButtonDialog(getActivity(),
						HelperMessages.MESSAGE_PRODUCT_THICKNESS);
				edittext_product_thickness_text.requestFocus();
				int smoothY = (int) getActivity().getResources().getDimension(
						R.dimen.kpi_percent_text_size);
				scrollView1.smoothScrollBy(0, -smoothY);
				return false;
		}
		else if ((edittext_product_thickness_text.getText().toString().trim()
				.equals("0")
				|| edittext_product_thickness_text.getText().toString().trim()
						.equals("00")
				|| edittext_product_thickness_text.getText().toString().trim()
						.equals("000")
				|| edittext_product_thickness_text.getText().toString().trim()
						.equals("0000")) && p_auto_size_flag.equals("0")) {
			GlobalVariables.defaultOneButtonDialog(getActivity(),
					HelperMessages.MESSAGE_THICKNESS_GREATER_ZERO);
			edittext_product_thickness_text.requestFocus();
			int smoothY = (int) getActivity().getResources().getDimension(
					R.dimen.kpi_percent_text_size);
			scrollView1.smoothScrollBy(0, -smoothY);
			return false;
		}*/
		/*else if(color_applicable_yn.equals("1") && textview_product_color.getText().toString().trim().length()==0){
				GlobalVariables.defaultOneButtonDialog(getActivity(),
						HelperMessages.MESSAGE_PRODUCT_COLOR);
			return false;
		}*/
        /*
         * else if (p_encodeImage.length()==0 && AppConstant.EDIT_ORDER==0){
         *
         * GlobalVariables.defaultOneButtonDialog(getActivity(),
         * "Pls select image"); return false; }
         */

        else
            return true;

    }


    @Override
    public void onTaskComplete(String result) {
        try {



            if (requset_no.equals("1")) // Place order
            {
                //  rlayout_submit_buttons.setVisibility(View.VISIBLE);
                customAsyncTask.hideDialog(getActivity());
                Log.e("Response1", " = " + result);
                jsonObjectResponse = new JSONObject(result);
                String status = jsonObjectResponse.getString("status");
                if (status.equalsIgnoreCase("200")) {
                    if (cd != null && cd.isShowing())
                        cd.dismiss();
                    order_date = "";
                    current_date = "";
                    tomorrow_date = "";
                    mobile_no = "";
                    remark = "";
                    p_encodeImage = "";
                    customer_name = "";
                    p_image_name = "";
                    length = "";
                    width = "";
                    thickness = "";
                    sheelaSharedPreference.setLastOrderStatus("");
                    sheelaSharedPreference.setLastProduct(new JSONObject());
                    JSONObject data = jsonObjectResponse.getJSONObject("data");
                    String op_result = data.getString("op_result");
                    if (op_result.equalsIgnoreCase("1")) {
                        String op_message = data.getString("op_message");
                        saveSupplyFrom(location_code, location_name, channel_partner_group);
                        if (pId != "") {
                            remove_placed_order(pId, op_message);
                        }
                        customDialogNew(op_message);
                        //--------------change--dialog-------------------------------------------
                        // defaultOneButtonDialog(getActivity(), op_message);
                    } else {
                        String op_message = data.getString("op_message");
                        GlobalVariables.defaultOneButtonDialog(getActivity(), op_message);
                    }
                } else {

                    JSONObject data = jsonObjectResponse.getJSONObject("data");
                    String msg = data.getString("op_message");
                    GlobalVariables.defaultOneButtonDialog(getActivity(), "" + msg);
                }
            } else if (requset_no.equals("2")) // color list
            {
                if (result != null && result.length() > 0) {
                    jsonObjectResponse = new JSONObject(result);
                    Log.e("Response2", " " + jsonObjectResponse);
                    String status = jsonObjectResponse.getString("status");
                    if (status.equalsIgnoreCase("200")) {
                        JSONObject data = jsonObjectResponse
                                .getJSONObject("data");
                        JSONArray colorArray = data.getJSONArray("product");
                        getColorList(colorArray);
                        //customAsyncTask.hideDialog(getActivity());
                        if (AppConstant.EDIT_ORDER == 1 && flag_color == 1)
                            dialogs_Color();
                    } else {
                        JSONObject data = jsonObjectResponse
                                .getJSONObject("data");
                        String msg = data.getString("op_message");
                        GlobalVariables.defaultOneButtonDialog(getActivity(),
                                "" + msg);
                    }
                } else {
                    Toast.makeText(getActivity(), "No Data returned",
                            Toast.LENGTH_SHORT).show();

                }
                getSize(dealer_name, zone, product_name);
            } else if (requset_no.equals("7")) // size list
            {
                if (result != null && result.length() > 0) {
                    jsonObjectResponse = new JSONObject(result);
                    Log.e("Response7", " " + jsonObjectResponse);
                    String status = jsonObjectResponse.getString("status");
                    if (status.equalsIgnoreCase("200")) {
                        JSONObject data = jsonObjectResponse
                                .getJSONObject("data");
                        JSONArray sizeArray = data.getJSONArray("ProductSize");
                        getSizeList(sizeArray);
                        customAsyncTask.hideDialog(getActivity());
                        if (AppConstant.EDIT_ORDER == 1 && flag_size == 1) {
                            //dialogs_Color();
                            if (length.length() > 0) {
                                edittext_product_length_text.setText(length);
                                edittext_product_length_text.setEnabled(false);
                            }
                            if (width.length() > 0) {
                                edittext_product_width_text.setText(width);
                                edittext_product_width_text.setEnabled(false);
                            }
                            if (length.length() > 0 && width.length() > 0) {
                                edittext_product_thickness_text.setText(thickness);
                                edittext_product_width_text.setEnabled(false);
                            }
                        }
                    } else {
                        JSONObject data = jsonObjectResponse
                                .getJSONObject("data");
                        String msg = data.getString("op_message");
                        GlobalVariables.defaultOneButtonDialog(getActivity(),
                                "" + msg);
                    }
                } else {
                    Toast.makeText(getActivity(), "No Data returned",
                            Toast.LENGTH_SHORT).show();
                }
            } else if (requset_no.equals("3")) // Dealer list
            {
                if (result != null && result.length() > 0) {
                    jsonObjectResponse = new JSONObject(result);
                    Log.e("Response3", " " + jsonObjectResponse);
                    String status = jsonObjectResponse.getString("status");
                    if (status.equalsIgnoreCase("200")) {
                        JSONObject data = jsonObjectResponse
                                .getJSONObject("data");
                        JSONArray dealerArray = data.getJSONArray("product");
                        getDealerList(dealerArray);
                        //flag_product = 0;
                        flag_location = 0;
                        customAsyncTask.hideDialog(getActivity());
                        setDealerDataWhenLoginByDealer();
                        /*
                         * Calling dealer Dialog
                         */
                    } else {
                        customAsyncTask.hideDialog(getActivity());
                        JSONObject data = jsonObjectResponse
                                .getJSONObject("data");
                        String msg = data.getString("op_message");
                        GlobalVariables.defaultOneButtonDialog(getActivity(),
                                "" + msg);
                    }
                } else {
                    Toast.makeText(getActivity(), "No Data returned",
                            Toast.LENGTH_SHORT).show();
                }
            } else if (requset_no.equals("4")) // product list
            {
                if (result != null && result.length() > 0) {
                    jsonObjectResponse = new JSONObject(result);
                    Log.e("Response4", " " + jsonObjectResponse);
                    String status = jsonObjectResponse.getString("status");
                    if (status.equalsIgnoreCase("200")) {
                        JSONObject data = jsonObjectResponse
                                .getJSONObject("data");
                        JSONArray productArray = data.getJSONArray("product");
                        Editor editor = loacalData.edit();
                        editor.putString(SharePref.PRODUCTS,
                                productArray.toString());
                        editor.commit();
                        getProductList(productArray);
                        customAsyncTask.hideDialog(getActivity());
                        flag_color = 0;
                        if (AppConstant.EDIT_ORDER == 1 && flag_product == 1)
                            dialogs_Product();
                    } else {
                        customAsyncTask.hideDialog(getActivity());
                        JSONObject data = jsonObjectResponse
                                .getJSONObject("data");
                        String msg = data.getString("op_message");
                        GlobalVariables.defaultOneButtonDialog(getActivity(),
                                "" + msg);
                    }
                } else {
                    customAsyncTask.hideDialog(getActivity());
                    Toast.makeText(getActivity(), "No Data returned",
                            Toast.LENGTH_SHORT).show();
                }
            } else if (requset_no.equals("5")) // update/edit order
            {
                rlayout_submit_buttons.setVisibility(View.VISIBLE);
                Log.e("Response5", " = " + result);
                jsonObjectResponse = new JSONObject(result);
                String status = jsonObjectResponse.getString("status");
                if (status.equalsIgnoreCase("200")) {
                    JSONObject data = jsonObjectResponse.getJSONObject("data");
                    String op_result = data.getString("op_result");
                    if (op_result.equalsIgnoreCase("1")) {
                        String op_message = data.getString("op_message");
                        /*
                         * //refresh screen Intent intent =
                         * getActivity().getIntent();
                         * getActivity().overridePendingTransition(0, 0);
                         * intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                         * // getActivity().finish();
                         * getActivity().overridePendingTransition(0, 0);
                         * startActivity(intent);
                         *
                         * //
                         * GlobalVariables.defaultOneButtonDialog(getActivity(),
                         * op_message);
                         */
                        customAsyncTask.hideDialog(getActivity());
                        saveSupplyFrom(location_code, location_name, channel_partner_group);
                        defaultOneButtonDialog(getActivity(), op_message);
                        AppConstant.EDIT_ORDER = 0;
                    } else {
                        System.out.println("anand");
                        customAsyncTask.hideDialog(getActivity());
                        String op_message = data.getString("op_message");
                        GlobalVariables.defaultOneButtonDialog(getActivity(),
                                op_message);
                    }
                } else {
                    customAsyncTask.hideDialog(getActivity());
                    JSONObject data = jsonObjectResponse.getJSONObject("data");
                    String msg = data.getString("op_message");
                    GlobalVariables.defaultOneButtonDialog(getActivity(), ""
                            + msg);
                }
            } else if (requset_no.equals("6")) // location list
            {
                if (result != null && result.length() > 0) {
                    jsonObjectResponse = new JSONObject(result);
                    Log.e("Response6", " " + jsonObjectResponse);
                    String status = jsonObjectResponse.getString("status");
                    if (status.equalsIgnoreCase("200")) {
                        JSONObject data = jsonObjectResponse.getJSONObject("data");
                        JSONArray locationArray = data.getJSONArray("location");
                        Editor editor = loacalData.edit();
                        editor.putString(SharePref.LOCATION, locationArray.toString());
                        editor.commit();
                        //getProductList(productArray);
                        getLocationList(locationArray);
                        customAsyncTask.hideDialog(getActivity());
                        flag_product = 0;
                        if (AppConstant.EDIT_ORDER == 1 && flag_location == 1)
                            dialogs_location();
                        customAsyncTask.hideDialog(getActivity());
                    } else {
                        customAsyncTask.hideDialog(getActivity());
                        JSONObject data = jsonObjectResponse.getJSONObject("data");
                        String msg = data.getString("op_message");
                        GlobalVariables.defaultOneButtonDialog(getActivity(), "" + msg);
                    }
                    //Below line commented to get variable value from new login
                    //if(mPrefs.getString("op_user_type", "").equals("DEALER"))
                    if (Util.getSharedPrefrenceValue(getActivity(), Constant.Sp_User_Type).equals("DEALER")) {
                        getDealers();
                    } else {
                        if (AppConstant.EDIT_ORDER == 0 && location_name.length() > 0) {
                            autocomplete_product_name.setHint("Product Name");
                            textview_product_color.setHint("Color");
                            if (arrayList_products.size() > 0)
                                arrayList_products.clear();
                            getProducts(mPrefs.getString("DEALER_NAME", ""), AppConstant.DEALER_ZONE);
                        }
                    }
					/*if(AppConstant.EDIT_ORDER == 0 && location_name.length()>0)
					{
						AppConstant.DEALER_NAME = final_dealer_name;
						AppConstant.DEALER_ZONE = zone;
						autocomplete_product_name.setHint("Product Name");
						textview_product_color.setHint("Color");
						if(arrayList_products.size()>0)
							arrayList_products.clear();
						getProducts(final_dealer_name, zone);
					}*/
                } else {
                    customAsyncTask.hideDialog(getActivity());
                    Toast.makeText(getActivity(), "No Data returned",
                            Toast.LENGTH_SHORT).show();
                }
            } else if (requset_no.equals("8")) {
                if (result != null && result.length() > 0) {
                    jsonObjectResponse = new JSONObject(result);
                    Log.e("Response8", " " + jsonObjectResponse);
                    String status = jsonObjectResponse.getString("status");
                    if (status.equalsIgnoreCase("200")) {
                        JSONObject data = jsonObjectResponse.getJSONObject("data");
                        String date_server = data.getString("Date");
                        Log.e("server date", date_server);
                        int date_difference = HelperMethods.getDateDifferenceInDays(date_server, textview_order_date.getText().toString().trim());
                        //int date_difference=HelperMethods.getDateDifferenceInDays(date_server,date_server);
                        //Toast.makeText(getActivity(), ""+date_difference, Toast.LENGTH_LONG).show();
                        if (AppConstant.EDIT_ORDER == 1) {
                            // rlayout_submit_buttons.setVisibility(View.GONE);
                            customAsyncTask.showDialog(getActivity());
                            editOrder();
                            System.out.println("call_1");
                        } else {
                            if (date_difference == 0) {
                                //  rlayout_submit_buttons.setVisibility(View.GONE);
                                customAsyncTask.showDialog(getActivity());
                                // placeOrder();
                                placeOrder_2();
                                sheelaSharedPreference.setLastProduct(jsonObjectRequest);
                                sheelaSharedPreference.setLastOrderStatus(jsonObjectRequest.getString("p_request_id"));
                                System.out.println("call_2");
                            } else {
                                defaultOneButtonDialog_serverDate(getActivity(), GlobalVariables.CURRENT_DATE_ERROR, textview_order_date, date_server);
                            }
                            //placeOrder();
                        }
                    }
                }
            }

        } catch (Exception e) {
            // rlayout_submit_buttons.setVisibility(View.VISIBLE);
            customAsyncTask.hideDialog(getActivity());
            Log.e("Exception caught", " = " + e);
        } finally {

            if (mPrefs.getInt("CONNECTION_TIMEOUT", 0) == 1) {
                customAsyncTask.hideDialog(getActivity());
                Toast.makeText(getActivity(), "Connection timeout",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    public void dialogs_Color() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_dealer);
        dialog.setCancelable(true);
        final EditText autocomplete_color_name = (EditText) dialog.findViewById(R.id.autocomplete_product_name);
        autocomplete_color_name.setVisibility(View.VISIBLE);
        // set the custom dialog components - text, image and button
        final ListView listview = (ListView) dialog.findViewById(R.id.listview_securityquestion);
        ImageButton imagebutton_close = (ImageButton) dialog.findViewById(R.id.button_close);
        TextView textview_title = (TextView) dialog.findViewById(R.id.textview_title);
        textview_title.setText("Please select color");
        colorAdapter = new ColorAdapter(arrayList_color, mContext);
        listview.setAdapter(colorAdapter);
        listview.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View view,
                                    int position, long id) {
                HelperMethods.hideSoftKeyboard(getActivity());
                ColorBean colorBean = arrayList_color.get(position);
                color_name = colorBean.getColor();
                if (textview_product_color.getText().toString().trim()
                        .equalsIgnoreCase(color_name)) {
                    Toast.makeText(getActivity(), "Color already selected",
                            Toast.LENGTH_SHORT).show();
                } else {
                    textview_product_color.setText(color_name);
                    final_color_name = color_name;
                    dialog.dismiss();
                }
            }
        });
        imagebutton_close.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        autocomplete_color_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                final ArrayList<ColorBean> selectedArrayList = getArrayList(
                        arrayList_color, autocomplete_color_name.getText()
                                .toString().trim());
                colorAdapter = new ColorAdapter(selectedArrayList, mContext);
                listview.setAdapter(colorAdapter);
                listview.setOnItemClickListener(new OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> arg0, View view,
                                            int position, long id) {
                        HelperMethods.hideSoftKeyboard(getActivity());
                        ColorBean colorBean = selectedArrayList.get(position);
                        color_name = colorBean.getColor();
                        if (textview_product_color.getText().toString().trim()
                                .equalsIgnoreCase(color_name)) {
                            Toast.makeText(getActivity(),
                                    "Color already selected",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            textview_product_color.setText(color_name);
                            final_color_name = color_name;
                            dialog.dismiss();
                        }
                    }
                });
            }

            private ArrayList<ColorBean> getArrayList(
                    ArrayList<ColorBean> arrayList_color, String filter) {
                ArrayList<ColorBean> selectedArrayList = new ArrayList<ColorBean>();
                if (filter.length() >= 0) {
                    for (int i = 0; i < arrayList_color.size(); i++) {
                        if (arrayList_color.get(i).getColor().toLowerCase()
                                .contains(filter.toLowerCase()))
                            selectedArrayList.add(arrayList_color.get(i));
                    }
                    return selectedArrayList;
                } else {
                    return arrayList_color;
                }
            }
        });
        dialog.show();
    }

    public void getColorList(JSONArray jArray) {
        if (arrayList_color.size() > 0) {
            arrayList_color.clear();
        }
        for (int i = 0; i < jArray.length(); i++) {
            try {
                JSONObject obj = jArray.getJSONObject(i);
                color_name = obj.getString("color");
                ColorBean colorBean = new ColorBean();
                colorBean.setColor(color_name);
                arrayList_color.add(colorBean);
            } catch (Exception e) {
            }
        }
    }

    public void getLocationList(JSONArray jArray) {
        if (arrayList_location.size() > 0) {
            arrayList_location.clear();
        }
        for (int i = 0; i < jArray.length(); i++) {
            try {
                JSONObject obj = jArray.getJSONObject(i);
                location_name = obj.getString("location_name");
                location_code = obj.getString("location_code");
                channel_partner_group = obj.getString("channel_partner_group");
                //location_code
                LocationBean locationBean = new LocationBean();
                locationBean.setLocation_code(location_code);
                locationBean.setLocation_name(location_name);
                locationBean.setChannel_partner_group(channel_partner_group);
                arrayList_location.add(locationBean);
            } catch (Exception e) {

            }
        }
    }

    public void getSizeList(JSONArray jArray) {
        if (arrayList_size.size() > 0) {
            arrayList_size.clear();
        }
        for (int i = 0; i < jArray.length(); i++) {
            try {
                JSONObject obj = jArray.getJSONObject(i);
                length = obj.getString("base_length");
                width = obj.getString("base_bredth");
                thickness = obj.getString("base_thick");
                SizeBean sizeBean = new SizeBean();
                sizeBean.setLength(length);
                sizeBean.setWidth(width);
                sizeBean.setThikness(thickness);
                arrayList_size.add(sizeBean);
            } catch (Exception e) {
            }
        }
        if (length.length() > 0 && width.length() > 0) {
            p_auto_size_flag = "1";
        } else {
            p_auto_size_flag = "0";
        }
        /**
         * this length and width is autofill so p_auto_size_flag is auto set.
         * **/
        if (length.length() > 0)// i am getting length value which is coming from server when call size api. It means p_auto_size_flag is 1
        // and edittext_product_length_text will be non editable
        {
            edittext_product_length_text.setText(length);
            edittext_product_length_text.setEnabled(false);
        } else {
            edittext_product_length_text.setText(length);
            edittext_product_length_text.setHint("L");
            edittext_product_length_text.setEnabled(true);
        }
        if (width.length() > 0)// i am getting width value which is coming from server when call size api. It means p_auto_size_flag is 1
        // and edittext_product_width_text will be non editable
        {
            edittext_product_width_text.setText(width);
            edittext_product_width_text.setEnabled(false);
        } else {
            edittext_product_width_text.setText(width);
            edittext_product_width_text.setHint("W");
            edittext_product_width_text.setEnabled(true);
        }
        if (length.length() > 0 && width.length() > 0) // i am getting thickness value which is coming from server when call size api. It means p_auto_size_flag is 1
        // and edittext_product_thickness_text will be non editable
        {
            edittext_product_thickness_text.setText(thickness);
            edittext_product_thickness_text.setEnabled(false);
        } else {
            edittext_product_thickness_text.setText(thickness);
            edittext_product_thickness_text.setHint("T");
            edittext_product_thickness_text.setEnabled(true);
        }
    }

    public void getProductList(JSONArray jArray) {
        if (arrayList_products.size() > 0) {
            arrayList_products.clear();
        }
        for (int i = 0; i < jArray.length(); i++) {
            try {
                JSONObject obj = jArray.getJSONObject(i);
                product_name = obj.getString("product_display_name");
                sub_product = obj.getString("sub_product");
                product_specification = obj.getString("product_specification");
                color_applicable_yn = obj.getString("color_applicable_yn");
                ProductListAddEditBean productListBean = new ProductListAddEditBean();
                productListBean.setProductName(product_name);
                productListBean.setSubProductName(sub_product);
                productListBean.setProductSpecification(product_specification);
                productListBean.setColor_applicable_yn(color_applicable_yn);
                arrayList_products.add(productListBean);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Log.e("Product_List", " = " + arrayList_products.toString());
        /*******************Shared variable for save product list********************/
        saveProductList(arrayList_products);
    }

    public void getDealerList(JSONArray jArray) {
        if (arrayList_dealer.size() > 0) {
            arrayList_dealer.clear();
        }
        for (int i = 0; i < jArray.length(); i++) {
            try {
                JSONObject obj = jArray.getJSONObject(i);
                String dealer_name = obj.getString("DEALER_NAME");
                String zone = obj.getString("DEALER_ZONE");
                String dealer_category = obj.getString("DEALER_CATEGORY");
                String dealer_id = obj.getString("DEALER_ID");
                String dealer_area = obj.getString("AREA");
                String dealer_city = obj.getString("CITY");
                String dealer_state = obj.getString("STATE");
                DealerBean dealerBean = new DealerBean();
                dealerBean.setDealerName(dealer_name);
                dealerBean.setDealerZone(zone);
                dealerBean.setDealer_category(dealer_category);
                dealerBean.setDealerId(dealer_id);
                dealerBean.setDealer_area(dealer_area);
                dealerBean.setDealer_city(dealer_city);
                dealerBean.setDealer_state(dealer_state);
                if (AppConstant.EDIT_ORDER == 1 && textview_dealer_list.getText().toString().trim().equals(dealer_name))
                    AppConstant.DEALER_ZONE = zone;
                arrayList_dealer.add(dealerBean);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void hardProductDialog() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_dealer);
        dialog.setCancelable(true);
        arrayList_products_2 = new ArrayList<ProductListAddEditBean>();
        ProductListAddEditBean bean1 = new ProductListAddEditBean();
        bean1.setProductName("COMFORT CELL-SERIES PM 1.0");
        bean1.setColor_applicable_yn("");
        arrayList_products_2.add(bean1);
        ProductListAddEditBean bean2 = new ProductListAddEditBean();
        bean2.setProductName("COMFORT CELL-SERIES PM 2.0");
        bean2.setColor_applicable_yn("");
        arrayList_products_2.add(bean2);
        ProductListAddEditBean bean3 = new ProductListAddEditBean();
        bean3.setProductName("COMFORT CELL-SERIES PM 3.0");
        bean3.setColor_applicable_yn("");
        arrayList_products_2.add(bean3);
        ProductListAddEditBean bean4 = new ProductListAddEditBean();
        bean4.setProductName("COMFORT CELL-SERIES PM 4.0");
        bean4.setColor_applicable_yn("");
        arrayList_products_2.add(bean4);
        final EditText autocomplete_product_name1 = (EditText) dialog.findViewById(R.id.autocomplete_product_name);
        final ListView listview = (ListView) dialog.findViewById(R.id.listview_securityquestion);
        ImageButton imagebutton_close = (ImageButton) dialog.findViewById(R.id.button_close);
        TextView textview_title = (TextView) dialog.findViewById(R.id.textview_title);
        textview_title.setText("Please select a Product");
        productListAdapter = new ProductListAdapter(arrayList_products_2, mContext);
        listview.setAdapter(productListAdapter);
        listview.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
                // pId="";
                HelperMethods.hideSoftKeyboard(getActivity());
                ProductListAddEditBean dealerBean = arrayList_products_2.get(position);
                product_name = dealerBean.getProductName();
                color_applicable_yn = dealerBean.getColor_applicable_yn();
                if (color_applicable_yn.equals("")) {
                    llayout_product_color.setVisibility(View.GONE);
                } else {
                    llayout_product_color.setVisibility(View.VISIBLE);
                }
                if (autocomplete_product_name.getText().toString().trim().equalsIgnoreCase(product_name)) {
                    Toast.makeText(getActivity(), "Product already selected", Toast.LENGTH_SHORT).show();
                } else {
                    autocomplete_product_name.setText(product_name);
                    textview_product_color.setText("");
                    textview_product_color.setHint("Color");
                    if (arrayList_color.size() > 0)
                        arrayList_color.clear();
                    if (arrayList_size.size() > 0)
                        arrayList_size.clear();
                    dialog.dismiss();
                    getColor();
                }
            }
        });
        imagebutton_close.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        autocomplete_product_name1.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {// arrayList_countryRecords=GlobalVariables.ARRAYLIST_COUNTRYRECORDS;

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                final ArrayList<ProductListAddEditBean> selectedArrayList = getArrayList(
                        arrayList_products_2, autocomplete_product_name1
                                .getText().toString().trim());
                productListAdapter = new ProductListAdapter(selectedArrayList,
                        mContext);
                listview.setAdapter(productListAdapter);
                listview.setOnItemClickListener(new OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> arg0, View view,
                                            int position, long id) {
                        HelperMethods.hideSoftKeyboard(getActivity());
                        ProductListAddEditBean dealerBean = selectedArrayList.get(position);
                        product_name = dealerBean.getProductName();
                        color_applicable_yn = dealerBean.getColor_applicable_yn();
                        if (color_applicable_yn.equals("")) {
                            llayout_product_color.setVisibility(View.GONE);
                        } else {
                            llayout_product_color.setVisibility(View.VISIBLE);
                        }
                        if (autocomplete_product_name.getText().toString()
                                .trim().equalsIgnoreCase(product_name)) {
                            Toast.makeText(getActivity(),
                                    "Product already selected",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            autocomplete_product_name.setText(product_name);
                            textview_product_color.setText("");
                            textview_product_color.setHint("Color");
                            if (arrayList_color.size() > 0)
                                arrayList_color.clear();
                            if (arrayList_size.size() > 0)
                                arrayList_size.clear();
                            dialog.dismiss();
                            if (arrayList_color.size() > 0)
                                arrayList_color.clear();
                            getColor();
                        }
                    }
                });
            }

            private ArrayList<ProductListAddEditBean> getArrayList(ArrayList<ProductListAddEditBean> arrayList_products_2, String filter) {
                ArrayList<ProductListAddEditBean> selectedArrayList = new ArrayList<ProductListAddEditBean>();
                if (filter.length() >= 0) {
                    for (int i = 0; i < arrayList_products_2.size(); i++) {
                        if (arrayList_products_2.get(i).getProductName().toLowerCase().contains(filter.toLowerCase()))
                            selectedArrayList.add(arrayList_products_2.get(i));
                    }
                    return selectedArrayList;
                } else {
                    return arrayList_products;
                }
            }
        });
        dialog.show();
    }

    public void dialogs_Product() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_dealer);
        dialog.setCancelable(true);
        // set the custom dialog components - text, image and button
        final EditText autocomplete_product_name1 = (EditText) dialog
                .findViewById(R.id.autocomplete_product_name);
        autocomplete_product_name1.setVisibility(View.VISIBLE);
        final ListView listview = (ListView) dialog
                .findViewById(R.id.listview_securityquestion);
        ImageButton imagebutton_close = (ImageButton) dialog
                .findViewById(R.id.button_close);
        TextView textview_title = (TextView) dialog
                .findViewById(R.id.textview_title);
        textview_title.setText("Please select a Product");
        // arrayList_countryRecords=GlobalVariables.ARRAYLIST_COUNTRYRECORDS;
        if (arrayList_products.size() > 0) {
            productListAdapter = new ProductListAdapter(arrayList_products,
                    mContext);
        } else {
            arrayList_products = getProductListFromSharedPrefrences();
        }
        listview.setAdapter(productListAdapter);
        listview.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View view,
                                    int position, long id) {
                HelperMethods.hideSoftKeyboard(getActivity());
                ProductListAddEditBean dealerBean = arrayList_products
                        .get(position);
                product_name = dealerBean.getProductName();
                color_applicable_yn = dealerBean.getColor_applicable_yn();
                if (color_applicable_yn.equals("")) {
                    llayout_product_color.setVisibility(View.GONE);
                } else {
                    llayout_product_color.setVisibility(View.VISIBLE);
                }
                if (autocomplete_product_name.getText().toString().trim()
                        .equalsIgnoreCase(product_name)) {
                    Toast.makeText(getActivity(), "Product already selected",
                            Toast.LENGTH_SHORT).show();
                } else {
                    autocomplete_product_name.setText(product_name);
                    textview_product_color.setText("");
                    textview_product_color.setHint("Color");
                    if (arrayList_color.size() > 0)
                        arrayList_color.clear();
                    if (arrayList_size.size() > 0)
                        arrayList_size.clear();
                    dialog.dismiss();
                    getColor();
                }
            }
        });
        imagebutton_close.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        autocomplete_product_name1.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {// arrayList_countryRecords=GlobalVariables.ARRAYLIST_COUNTRYRECORDS;

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                final ArrayList<ProductListAddEditBean> selectedArrayList = getArrayList(
                        arrayList_products, autocomplete_product_name1
                                .getText().toString().trim());
                productListAdapter = new ProductListAdapter(selectedArrayList,
                        mContext);
                listview.setAdapter(productListAdapter);
                listview.setOnItemClickListener(new OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> arg0, View view,
                                            int position, long id) {
                        HelperMethods.hideSoftKeyboard(getActivity());
                        ProductListAddEditBean dealerBean = selectedArrayList
                                .get(position);
                        product_name = dealerBean.getProductName();
                        color_applicable_yn = dealerBean.getColor_applicable_yn();
                        if (color_applicable_yn.equals("")) {
                            llayout_product_color.setVisibility(View.GONE);
                        } else {
                            llayout_product_color.setVisibility(View.VISIBLE);
                        }
                        if (autocomplete_product_name.getText().toString()
                                .trim().equalsIgnoreCase(product_name)) {
                            Toast.makeText(getActivity(),
                                    "Product already selected",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            autocomplete_product_name.setText(product_name);
                            textview_product_color.setText("");
                            textview_product_color.setHint("Color");
                            if (arrayList_color.size() > 0)
                                arrayList_color.clear();
                            if (arrayList_size.size() > 0)
                                arrayList_size.clear();
                            dialog.dismiss();
                            if (arrayList_color.size() > 0)
                                arrayList_color.clear();
                            getColor();
                        }
                    }
                });
            }

            private ArrayList<ProductListAddEditBean> getArrayList(
                    ArrayList<ProductListAddEditBean> arrayList_products,
                    String filter) {
                ArrayList<ProductListAddEditBean> selectedArrayList = new ArrayList<ProductListAddEditBean>();
                if (filter.length() >= 0) {
                    for (int i = 0; i < arrayList_products.size(); i++) {
                        if (arrayList_products.get(i).getProductName()
                                .toLowerCase().contains(filter.toLowerCase()))
                            selectedArrayList.add(arrayList_products.get(i));
                    }
                    return selectedArrayList;
                } else {
                    return arrayList_products;
                }
            }
        });
        dialog.show();
    }

    public void dialog_Dealer_New() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_dealer);
        dialog.setCancelable(true);
        final EditText autocomplete_dealer_name = (EditText) dialog
                .findViewById(R.id.autocomplete_product_name);
        autocomplete_dealer_name.setVisibility(View.VISIBLE);
        final ListView listview = (ListView) dialog
                .findViewById(R.id.listview_securityquestion);
        ImageButton imagebutton_close = (ImageButton) dialog
                .findViewById(R.id.button_close);
        TextView textview_title = (TextView) dialog
                .findViewById(R.id.textview_title);
        textview_title.setText("Please select a Dealer");
        dealerAdapter = new DealerAdapter(arrayList_dealer, mContext);
        listview.setAdapter(dealerAdapter);
        listview.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View view,
                                    int position, long id) {
                HelperMethods.hideSoftKeyboard(getActivity());
                DealerBean dealerBean = arrayList_dealer.get(position);
                dealer_name = dealerBean.getDealerName();
                dealer_id = dealerBean.getDealerId();
                zone = dealerBean.getDealerZone();
                dealer_category = dealerBean.getDealer_category();
                Log.e("Dealer Name", "Current:"
                        + textview_dealer_list.getText().toString().trim()
                        + ",  Selected" + dealer_name);
                if (textview_dealer_list.getText().toString().trim()
                        .equalsIgnoreCase(dealer_name)) {
                    Toast.makeText(getActivity(), "Dealer already selected",
                            Toast.LENGTH_SHORT).show();
                } else {
                    textview_dealer_list.setText(dealer_name);
                    final_dealer_name = dealer_name;
                    //GlobalVariables.DEALER_NAME = ;
                    AppConstant.DEALER_ZONE = zone;
                    GlobalVariables.DEALER_CATEGORY = dealer_category;
                    //GlobalVariables.DEALER_ID=;
                    Editor editor = mPrefs.edit();
                    editor.putString("DEALER_NAME", final_dealer_name);
                    editor.putString("DEALER_ID", dealer_id);
                    editor.commit();
                    dialog.dismiss();
                    textview_location.setHint("Supply From");
                    autocomplete_product_name.setHint("Product Name");
                    textview_product_color.setHint("Color");
                    if (arrayList_location.size() > 0) {
                        arrayList_location.clear();
                    }
                    getLocationData(mPrefs.getString("DEALER_ID", ""));
                }
            }
        });

        imagebutton_close.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });
        autocomplete_dealer_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                final ArrayList<DealerBean> selectedArrayList = getArrayList(
                        arrayList_dealer, autocomplete_dealer_name.getText()
                                .toString().trim());
                dealerAdapter = new DealerAdapter(selectedArrayList, mContext);
                listview.setAdapter(dealerAdapter);
                listview.setOnItemClickListener(new OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> arg0, View view,
                                            int position, long id) {
                        HelperMethods.hideSoftKeyboard(getActivity());
                        DealerBean dealerBean = selectedArrayList.get(position);
                        dealer_name = dealerBean.getDealerName();
                        dealer_id = dealerBean.getDealerId();
                        zone = dealerBean.getDealerZone();
                        dealer_category = dealerBean.getDealer_category();
                        if (textview_dealer_list.getText().toString().trim()
                                .equalsIgnoreCase(dealer_name)) {
                            Toast.makeText(getActivity(),
                                    "Dealer already selected",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            final_dealer_name = dealer_name;
                            //GlobalVariables.DEALER_NAME = final_dealer_name;
                            AppConstant.DEALER_ZONE = zone;
                            GlobalVariables.DEALER_CATEGORY = dealer_category;
                            //GlobalVariables.DEALER_ID=dealer_id;
                            Editor editor = mPrefs.edit();
                            editor.putString("DEALER_NAME", final_dealer_name);
                            editor.putString("DEALER_ID", dealer_id);
                            editor.commit();
                            textview_dealer_list.setText(dealer_name);
                            dialog.dismiss();
                            /** Calling product api **/
                            //autocomplete_product_name.setText("Please Select");
                            //textview_product_color.setText("Please Select");
                            textview_location.setText("Supply From");
                            autocomplete_product_name.setText("Product Name");
                            textview_product_color.setText("Color");
                            if (arrayList_location.size() > 0)
                                arrayList_location.clear();
                            // disable by Vinay.Using update PRC.
                            //getLocationData(AppConstant.USER_TERRITORY, final_dealer_name);

                            getLocationData(mPrefs.getString("DEALER_ID", ""));

                        }
                    }
                });
            }

            private ArrayList<DealerBean> getArrayList(
                    ArrayList<DealerBean> arrayList_dealer, String filter) {
                ArrayList<DealerBean> selectedArrayList = new ArrayList<DealerBean>();
                if (filter.length() >= 0) {
                    for (int i = 0; i < arrayList_dealer.size(); i++) {
                        if (arrayList_dealer.get(i).getDealerName()
                                .toLowerCase().contains(filter.toLowerCase()))
                            selectedArrayList.add(arrayList_dealer.get(i));
                    }
                    return selectedArrayList;
                } else {
                    return arrayList_dealer;
                }
            }
        });
        dialog.show();
    }

    public void defaultOneButtonDialog(final Activity activity, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(msg).setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        if (cd != null && cd.isShowing())
                            cd.dismiss();
                        if (AppConstant.isNewFoam) {
                            ProductOrderFragment productOrderFragment = new ProductOrderFragment();
                            if (activity instanceof HomeScreen) {
                                ((HomeScreen) getActivity()).commonFragmentMethod(productOrderFragment, null, null);
                            } else {
                                Bundle bundle = new Bundle();
                                bundle.putBoolean("order_menu", true);
                                productOrderFragment.setArguments(bundle);
                                ((OrderDashboardActivity) getActivity()).orderMenuAdapter.commonFragmentMethod(productOrderFragment, null, null);
                            }

                            // callFragment();
                        } else {
                            ProductOrderView productOrderView = new ProductOrderView();
                            if (activity instanceof HomeScreen) {
                                ((HomeScreen) getActivity()).commonFragmentMethod(productOrderView, null, null);
                            } else {
                                Bundle bundle = new Bundle();
                                bundle.putBoolean("order_menu", true);
                                productOrderView.setArguments(bundle);
                                ((OrderDashboardActivity) getActivity()).orderMenuAdapter.commonFragmentMethod(productOrderView, null, null);
                            }

                            //callFragment();
                        }
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void customDialogNew(String message) {
        if (cd2 != null && cd2.isShowing()) {
            cd2.dismiss();
        }
        cd2 = new CustomDialog2(getActivity(), R.layout.custom_dialog_newtwo);
        cd2.setCancelable(false);
        cd2.getWindow().setBackgroundDrawable(new ColorDrawable(TRANSPARENT));
        TextView dialog_title = (TextView) cd2.findViewById(R.id.dialog_title);
        dialog_title.setText(message);
        TextView txt_address_value = (TextView) cd2.findViewById(R.id.tv_massageDialog);
        txt_address_value.setText("Do You Want to Place Another Order?");
        TextView btn_okDialog = (TextView) cd2.findViewById(R.id.btn_okDialog);
        TextView btn_canceDilog = (TextView) cd2.findViewById(R.id.btn_canceDilog);
        btn_okDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //getCurrentTimeFromServer();
                if (cd2 != null && cd2.isShowing())
                    cd2.dismiss();
                ProductOrderFragment productOrderFragment = new ProductOrderFragment();
                if (getActivity() instanceof HomeScreen) {
                    ((HomeScreen) getActivity()).commonFragmentMethod(productOrderFragment, null, null);
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("order_menu", true);
                    productOrderFragment.setArguments(bundle);
                    ((OrderDashboardActivity) getActivity()).orderMenuAdapter.commonFragmentMethod(productOrderFragment, null, null);
                }
            }
        });
        btn_canceDilog.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                // cd.dismiss();
                if (cd2 != null && cd2.isShowing())
                    cd2.dismiss();
                ProductOrderView productOrderView = new ProductOrderView();
                if (getActivity() instanceof HomeScreen) {
                    ((HomeScreen) getActivity()).commonFragmentMethod(productOrderView, null, null);
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("order_menu", true);
                    productOrderView.setArguments(bundle);
                    ((OrderDashboardActivity) getActivity()).orderMenuAdapter.commonFragmentMethod(productOrderView, null, null);
                }
            }
        });
        cd2.show();

    }

    protected void callFragment(Fragment fragment) {
        if (getActivity().getSupportFragmentManager().getBackStackEntryCount() > 0)
            getActivity().getSupportFragmentManager().popBackStack();
        getActivity().getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, fragment).addToBackStack(null).commit();
    }

    private synchronized void editOrder() {
        requset_no = "5";
        getData();
        if (HelperMethods.isNetworkAvailable(getActivity())) {
            try {
                ProductOrderView.getInstance().requset_no = "3";
                JSONObject jsonObjectRequest = new JSONObject();
                jsonObjectRequest
                        .put("request", ApiList.API_PRODUCT_EDIT_ORDER);
                jsonObjectRequest.put("p_order_number", order_no);
                jsonObjectRequest.put("p_dealer_name", final_dealer_name);
                jsonObjectRequest.put("p_dealer_id", mPrefs.getString("DEALER_ID", ""));
                jsonObjectRequest.put("p_channel_partner_group", channel_partner_group);
                jsonObjectRequest.put("p_dealer_category", dealer_category);
                jsonObjectRequest.put("p_location_code", location_code);
                jsonObjectRequest.put("p_location_name", location_name);
                jsonObjectRequest.put("p_product_display_name", product_name);
                jsonObjectRequest.put("p_color_applicable_yn", color_applicable_yn);
                jsonObjectRequest.put("p_length", length);
                jsonObjectRequest.put("p_breadth", width);
                jsonObjectRequest.put("p_thick", thickness);
                jsonObjectRequest.put("p_auto_size_flag", p_auto_size_flag);
                jsonObjectRequest.put("p_colour", color_name);
                jsonObjectRequest.put("p_uom", p_uom);
                jsonObjectRequest.put("p_qty", quantity);
                jsonObjectRequest.put("p_remark", remark);
                jsonObjectRequest.put("p_customer_name", customer_name);
                jsonObjectRequest.put("p_customer_mobile", mobile_no);
                jsonObjectRequest.put("p_delivery_date",
                        HelperMethods.getDDMMMyyyy(delivery_date));
                if (p_encodeImage.length() == 0) {
                    jsonObjectRequest.put("p_captured_image",
                            "" + mPrefs.getString("p_captured_image", ""));
                    Log.e("Image name",
                            "" + mPrefs.getString("p_captured_image", ""));
                } else
                    jsonObjectRequest.put("p_captured_image",
                            p_image_name.trim());
                jsonObjectRequest.put("p_captured_length", image_length);
                jsonObjectRequest.put("p_captured_bredth", image_breadth);
                jsonObjectRequest.put("p_order_date",
                        HelperMethods.getDDMMMyyyy(order_date));
                if (p_encodeImage.length() > 0)
                    p_image_ext = "png";
                jsonObjectRequest.put("p_ext", p_image_ext);
                jsonObjectRequest.put("p_captured_image_binary", p_encodeImage);
                if (p_encodeImage.length() == 0) {
                    old_image_name = "";
                }
                jsonObjectRequest.put("p_old_image", old_image_name);
                Log.e("request5", "" + jsonObjectRequest);
                // Resetting edit flag
                AppConstant.EDIT_ORDER = 0;
                new MyAsyncTask(getActivity(), this, jsonObjectRequest,
                        ApiList.URL_PLACE_ORDER).execute();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getActivity(), "Network Error", Toast.LENGTH_LONG)
                    .show();
        }
    }

    public void convertInchesToMm() {
        String inches_length_str = edittext_product_length_text.getText().toString().trim();
        String inches_width_str = edittext_product_width_text.getText().toString().trim();
        if (inches_length_str != null && inches_length_str.length() > 0) {
            //int inches_int=Integer.parseInt(inches_length_str);
            double inches_double = Double.parseDouble(inches_length_str);
            if (inches_double < 100) {
                double mm_double = (inches_double * 25.4);
                Toast.makeText(getActivity(), "" + mm_double, Toast.LENGTH_LONG).show();
                edittext_product_length_text.setText(roundOffLengthAndWidth(mm_double));
            } else {
                Toast.makeText(getActivity(), "Length must be less than 100 for convert inches to mm", Toast.LENGTH_LONG).show();
                edittext_product_length_text.setText("");
            }
        }
        if (inches_width_str != null && inches_width_str.length() > 0) {
            //int inches_int=Integer.parseInt(inches_width_str);
            double inches_double = Double.parseDouble(inches_width_str);
            if (inches_double < 100) {
                double mm_double = (inches_double * 25.4);
                edittext_product_width_text.setText(roundOffLengthAndWidth(mm_double));
            } else {
                Toast.makeText(getActivity(), "Widthength must be less than 100 for convert inches to mm", Toast.LENGTH_LONG).show();
                edittext_product_width_text.setText("");
            }
        }
    }

    public void convertMmToInches() {


        /**
         * inches_length_str and inches_width_str are the local variables for hold value of
         * length and width.These values will used for convert into integer and than mm_int.
         * **/
        String inches_length_str = edittext_product_length_text.getText().toString().trim();
        String inches_width_str = edittext_product_width_text.getText().toString().trim();
        if (inches_length_str != null && inches_length_str.length() > 0) {
            //int inches_int=Integer.parseInt(inches_length_str);

            double inches_double = Double.parseDouble(inches_length_str);
            if (inches_double > 0) {
                double mm_double = (inches_double / 25.4);

                //Toast.makeText(getActivity(), ""+mm_double, Toast.LENGTH_LONG).show();
                edittext_product_length_text.setText(roundOffLengthAndWidth(mm_double));
            } else {
                Toast.makeText(getActivity(), "Length must be greater than 0 for convert mm to inches", Toast.LENGTH_LONG).show();
                ;
            }
        }


        if (inches_width_str != null && inches_width_str.length() > 0) {
            //int inches_int=Integer.parseInt(inches_width_str);

            double inches_double = Double.parseDouble(inches_width_str);
            if (inches_double > 0) {
                double mm_double = (inches_double / 25.4);
                edittext_product_width_text.setText(roundOffLengthAndWidth(mm_double));
            } else {
                Toast.makeText(getActivity(), "Width must be greater than 0 for convert mm to inches", Toast.LENGTH_LONG).show();
            }


        }


    }

    public String roundOffLengthAndWidth(double value) {
        DecimalFormat f = new DecimalFormat("0.0");
        String formattedValue = f.format(value);

        //String [] array_split=formattedValue.split(".");
        String[] array_split = formattedValue.split(Pattern.quote("."));

        Log.e("array size", "" + array_split.length);


        String _value = array_split[0];
        if (Double.parseDouble(array_split[1]) > 5 || Double.parseDouble(array_split[1]) == 5) {
            _value = String.valueOf(Double.parseDouble(_value) + 1);
        }

        //Toast.makeText(getActivity(), "decimal formate  "+" "+formattedValue, Toast.LENGTH_LONG).show();

		/*//double i2 = value * 2.23694;
		 double i2 = Math.round(value*10.0)/10.0;
		 int value_new=(int) i2;





		return String.valueOf(value_new);*/

        return _value;
    }

    /**
     * Mehod : This method is used for convert length if it is less than 100 at focus
     * loss on edittext_lenght
     **/
    public void convertLength(String length) {
        if (length != null && length.length() > 0) {
            if (Double.parseDouble(length) < 100) {
                length = roundOffLengthAndWidth(Double.parseDouble(length) * 25.4);
                //Toast.makeText(getActivity(), "Hi lenght is converting"+" "+length, Toast.LENGTH_LONG).show();
                edittext_product_length_text.setText(length);
            }
        }
    }

    /**
     * Mehod : This method is used for convert width if it is less than 100 at focus
     * loss on edittext_width
     **/
    public void convertWidth(String width) {
        if (width != null && width.length() > 0) {
            if (Double.parseDouble(width) < 100) {
                width = roundOffLengthAndWidth(Double.parseDouble(width) * 25.4);
                //Toast.makeText(getActivity(), "Hi width is converting"+" "+width, Toast.LENGTH_LONG).show();
                edittext_product_width_text.setText(width);
            }

        }
    }

    public void setDealerDataWhenLoginByDealer() {
        //Below line commented to get variable value from new login
        //if(mPrefs.getString("op_user_type", "").equals("DEALER"))
        if (Util.getSharedPrefrenceValue(getActivity(), Constant.Sp_User_Type).equals("DEALER")) {
            DealerBean dealerBean = arrayList_dealer.get(0);
            dealer_name = dealerBean.getDealerName();
            dealer_id = dealerBean.getDealerId();
            zone = dealerBean.getDealerZone();
            dealer_category = dealerBean.getDealer_category();

            //textview_dealer_list.setText(mPrefs.getString("op_user_name","").toString());
            textview_dealer_list.setText(Util.getSharedPrefrenceValue(getActivity(), Constant.Op_user_name));
            final_dealer_name = dealer_name;

            //GlobalVariables.DEALER_NAME = final_dealer_name;
            AppConstant.DEALER_ZONE = zone;
            GlobalVariables.DEALER_CATEGORY = dealer_category;
            Editor editor = mPrefs.edit();
            editor.putString("DEALER_NAME", final_dealer_name);
            editor.putString("DEALER_ID", dealer_id);

            editor.commit();

            autocomplete_product_name.setHint("Product Name");
            textview_product_color.setHint("Color");
            if (arrayList_products.size() > 0)
                arrayList_products.clear();
            getProducts(final_dealer_name, zone);

        }
    }

    /**
     * Method : This method is used to get location and size from server at the selection of
     * product name.
     * <p>
     * {"request":"getLocationAndSizeData","p_territory":"06","p_dealer_name":"A & Z EXPORTS",
     * "p_zone":"NORTH","p_product_display_name":"ADORE L"}
     **/
    public void getLocationData(String p_dealer_id) {
        if (arrayList_location.size() > 0) {
            dialogs_location();
        } else if (HelperMethods.isNetworkAvailable(getActivity())) {
            requset_no = "6";
            JSONObject jObject = new JSONObject();
            try {
                jObject.put("request", ApiList.API_LOCATION);
                //jObject.put("p_territory", p_territory);
                //Comment by Amit
                jObject.put("p_dealer_id", p_dealer_id);
                //jObject.put("p_dealer_id",mPrefs.getString("DEALER_ID", "") );
				/*jObject.put("p_zone",p_zone);
				jObject.put("p_product_display_name", p_product_display_name);*/
                Log.e("request6", "" + jObject.toString());
            } catch (Exception e) {
                Log.e("Exception", e.getMessage());
            }
            customAsyncTask.showDialog(getActivity());
            new MyCustomAsyncTask(getActivity(), ProductOrderFragment.this,
                    jObject, ApiList.URL_PRODUCTS).execute();
        } else {
            customAsyncTask.showDialog(getActivity());
            GlobalVariables.defaultOneButtonDialog(getActivity(),
                    "Network error");
        }

    }

    /**
     * Method : This method is used to get size from server at the selection of
     * product name.
     * <p>
     * {"request":"getProductSize","p_dealer_name":"A & Z EXPORTS","p_zone":"NORTH","p_product_display_name":"ADORE L"}
     **/
    public void getSize(String p_dealer_name, String p_zone, String p_product_display_name) {
        if (HelperMethods.isNetworkAvailable(getActivity())) {
            requset_no = "7";
            JSONObject jObject = new JSONObject();
            try {
                jObject.put("request", ApiList.API_SIZE);
                jObject.put("p_dealer_name", p_dealer_name);
                jObject.put("p_zone", p_zone);
                jObject.put("p_product_display_name", p_product_display_name);
                Log.e("request7", "" + jObject.toString());
            } catch (Exception e) {
                Log.e("Exception", e.getMessage());
            }
            customAsyncTask.showDialog(getActivity());
            new MyCustomAsyncTask(getActivity(), ProductOrderFragment.this,
                    jObject, ApiList.URL_PRODUCTS).execute();
        } else {
            customAsyncTask.showDialog(getActivity());
            GlobalVariables.defaultOneButtonDialog(getActivity(),
                    "Network error");
        }

    }

    public void dialogs_location() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_dealer);
        dialog.setCancelable(true);
        // set the custom dialog components - text, image and button
        final EditText autocomplete_product_name1 = (EditText) dialog
                .findViewById(R.id.autocomplete_product_name);
        autocomplete_product_name1.setVisibility(View.VISIBLE);
        final ListView listview = (ListView) dialog
                .findViewById(R.id.listview_securityquestion);
        ImageButton imagebutton_close = (ImageButton) dialog
                .findViewById(R.id.button_close);
        TextView textview_title = (TextView) dialog
                .findViewById(R.id.textview_title);
        textview_title.setText("Please select a location");
        // arrayList_countryRecords=GlobalVariables.ARRAYLIST_COUNTRYRECORDS;
        locationAdapter = new LocationListAdapter(arrayList_location, mContext);
        listview.setAdapter(locationAdapter);
        listview.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                HelperMethods.hideSoftKeyboard(getActivity());
                LocationBean locationBean = arrayList_location.get(position);
                location_name = locationBean.getLocation_name();
                location_code = locationBean.getLocation_code();
                channel_partner_group = locationBean.getChannel_partner_group();

                if (textview_location.getText().toString().trim()
                        .equalsIgnoreCase(location_name)) {
                    Toast.makeText(getActivity(), "Location already selected",
                            Toast.LENGTH_SHORT).show();
                } else {

                    textview_location.setText(location_name);

                    autocomplete_product_name.setHint("Product Name");
                    textview_product_color.setHint("Color");

                    if (arrayList_products.size() > 0)
                        arrayList_products.clear();
                    dialog.dismiss();
                    getProducts(final_dealer_name, zone);


                }

            }
        });

        imagebutton_close.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                dialog.dismiss();

            }
        });

        autocomplete_product_name1.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {// arrayList_countryRecords=GlobalVariables.ARRAYLIST_COUNTRYRECORDS;

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                final ArrayList<LocationBean> selectedArrayList = getArrayList(
                        arrayList_location, autocomplete_product_name1
                                .getText().toString().trim());

                locationAdapter = new LocationListAdapter(selectedArrayList,
                        mContext);

                listview.setAdapter(locationAdapter);
                listview.setOnItemClickListener(new OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> arg0, View view,
                                            int position, long id) {
                        // TODO Auto-generated method stub
                        HelperMethods.hideSoftKeyboard(getActivity());

                        LocationBean locationBean = selectedArrayList
                                .get(position);
                        location_name = locationBean.getLocation_name();
                        channel_partner_group = locationBean.getChannel_partner_group();

                        if (textview_location.getText().toString()
                                .trim().equalsIgnoreCase(location_code)) {
                            Toast.makeText(getActivity(),
                                    "Location already selected",
                                    Toast.LENGTH_SHORT).show();
                        } else {

                            textview_location.setText(location_name);

                            //textview_product_color.setText("Please Select");
                            autocomplete_product_name.setText("Product Name");
                            textview_product_color.setText("Color");

                            dialog.dismiss();
                            if (arrayList_products.size() > 0)
                                arrayList_products.clear();

                            getProducts(final_dealer_name, zone);
                        }

                    }
                });

            }


            private ArrayList<LocationBean> getArrayList(
                    ArrayList<LocationBean> arrayList_location,
                    String filter) {
                // TODO Auto-generated method stub
                ArrayList<LocationBean> selectedArrayList = new ArrayList<LocationBean>();
                if (filter.length() >= 0) {

                    for (int i = 0; i < arrayList_location.size(); i++) {
                        if (arrayList_location.get(i).getLocation_name()
                                .toLowerCase().contains(filter.toLowerCase()))
                            selectedArrayList.add(arrayList_location.get(i));
                    }
                    return selectedArrayList;
                } else {
                    return arrayList_location;
                }

            }
        });

        dialog.show();

    }

    public void saveSupplyFrom(String p_location_code, String p_location_name, String channel_partner_group) {
        Editor editor = mPrefs.edit();
        editor.putString("p_location_code", p_location_code);
        editor.putString("p_location_name", p_location_name);
        editor.putString("channel_partner_group", channel_partner_group);
        editor.commit();
    }

    public void getDateFromNetwork(Activity act) {
        try {
            LocationManager locMan = (LocationManager) act.getSystemService(Context.LOCATION_SERVICE);
            //long networkTS = locMan.getLastKnownLocation(LocationManager.NETWORK_PROVIDER).getTime();

            boolean isNetworkEnabled = locMan.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            Location networkTS = locMan.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
       /* Toast.makeText(act,""+networkTS,Toast.LENGTH_LONG ).show();
        Date date = new Date(networkTS);
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        format.setTimeZone(TimeZone.getTimeZone("GMT+5:30"));
        String formatted = format.format(date);
        Toast.makeText(act,""+formatted,Toast.LENGTH_LONG ).show();*/
    }

    public void getCurrentTimeFromServer() {
        requset_no = "8";
        jsonObjectRequest = new JSONObject();
        try {
            jsonObjectRequest.put("request", ApiList.API_SERVER_CURRENT_DATE);
            Log.e("request8", "" + jsonObjectRequest.toString());
        } catch (Exception e) {
        }
        customAsyncTask.showDialog(getActivity());
        MyAsyncTask asyncTask = new MyAsyncTask(getActivity(), ProductOrderFragment.this, jsonObjectRequest, ApiList.URL_PRODUCTS);
        asyncTask.execute();

    }

    /***********************Method: Save product ArrayList in Shared Preferences***************************************/
    private void saveProductList(ArrayList<ProductListAddEditBean> productList) {
        Editor editor = mPrefs.edit();
        Gson gson = new Gson();

        String json = gson.toJson(productList);

        editor.putString("com.sheela.employeeportal.sheelafoam.products", json);
        editor.commit();
    }

    private ArrayList<ProductListAddEditBean> getProductListFromSharedPrefrences() {
        ArrayList<ProductListAddEditBean> productList = null;

        Gson gson = new Gson();
        String json = mPrefs.getString("com.sheela.employeeportal.sheelafoam.products", null);
        Type type = new TypeToken<ArrayList<ProductListAddEditBean>>() {
        }.getType();
        productList = gson.fromJson(json, type);

        return productList;
    }

    private void remove_placed_order(final String id, final String msg) {
        final String order_no = msg.replaceAll("\\D+", "");
        Log.i("Order NO", order_no);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://greatplus.com/task_manager/perfect_match/remove_placed_order",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            Boolean status = jsonObject.getBoolean("status");
                            if (status) {
                                Log.i("Remove Response", "Successfully Remove Placed Order " + response);
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", id);
                params.put("order_no", order_no);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    public boolean check_remark_upmm() {
        boolean status = true;
        if (pId != "") {


            if (sUniqueCode.contains("S")) {
                if (!product_name.toLowerCase().contains("comfort")) {
                    GlobalVariables.defaultOneButtonDialog(getActivity(),
                            "Please Select Comfort Series product");
                    status = false;
                }
            }
        } else {

        }
        return status;
    }

    private void customDialogOld() {
        if (cd != null && cd.isShowing()) {
            cd.dismiss();
        }
        cd = new CustomDialog(getActivity(), R.layout.custome_dialog_new);
        cd.setCancelable(false);
        cd.getWindow().setBackgroundDrawable(new ColorDrawable(TRANSPARENT));
        TextView dialog_title = (TextView) cd.findViewById(R.id.dialog_title);
        TextView txt_address_value = (TextView) cd.findViewById(R.id.tv_massageDialog);
        TextView btn_okDialog = (TextView) cd.findViewById(R.id.btn_okDialog);
        TextView btn_canceDilog = (TextView) cd.findViewById(R.id.btn_canceDilog);
        btn_okDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cd != null && cd.isShowing())
                    cd.isShowing();
                getCurrentTimeFromServer();
            }
        });
        btn_canceDilog.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                cd.dismiss();
            }
        });
        cd.show();
    }

    private void buttonHandler() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SubmitTextView.setEnabled(true);
                saveAndNewTv.setEnabled(true);
            }
        }, 3000);
    }


    private void showMessageAlert(String msg, final boolean isCallService) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
        builder1.setMessage(msg);
        builder1.setCancelable(false);
        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        if(!isCallService){
                            sheelaSharedPreference.setLastOrderStatus("");
                            sheelaSharedPreference.setLastProduct(new JSONObject());
                        }else{
                            placeOrder_2();
                        }

                    }
                });
        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

}
