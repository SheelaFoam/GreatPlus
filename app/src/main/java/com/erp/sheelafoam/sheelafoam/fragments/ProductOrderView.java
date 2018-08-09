package com.erp.sheelafoam.sheelafoam.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.erp.sheelafoam.HomeScreen;
import com.erp.sheelafoam.R;
import com.erp.sheelafoam.sheelafoam.adapter.ProductListOrderAdapter;
import com.erp.sheelafoam.sheelafoam.entry.ProductOrderListBean;
import com.erp.sheelafoam.sheelafoam.utility.ApiList;
import com.erp.sheelafoam.sheelafoam.utility.AsyncTaskListner;
import com.erp.sheelafoam.sheelafoam.utility.ConnectionDetector;
import com.erp.sheelafoam.sheelafoam.utility.GlobalVariables;
import com.erp.sheelafoam.sheelafoam.utility.HelperMethods;
import com.erp.sheelafoam.sheelafoam.utility.MyAsyncTask;
import com.erp.sheelafoam.sheelafoam.utility.MyCustomAsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class ProductOrderView extends Fragment implements AsyncTaskListner, OnClickListener {

    private static ProductOrderView instance = null;
    public String requset_no;
    public int deleted_position;
    Context mContext;
    SharedPreferences mPrefs;
    ConnectionDetector con;
    ImageView gender_icon, upload_pic;
    RelativeLayout llayout_date_filter, rlayout_devider_two;
    RelativeLayout llayout_status_lable;
    TextView textview_date_from;
    TextView textview_date_to, textview_status, textview_filter_order;
    String date_from = "";
    String date_to = "";
    int year;
    int month;
    int day;
    RelativeLayout relativeLayoutToolbar;
    RelativeLayout relativeLayoutBackBtn;
    TextView tvTitle;
    Boolean order_menu = false;
    Activity mActivity;
    JSONObject jsonObjectRequest, jsonObjectResponse;
    ListView listView;
    ArrayList<ProductOrderListBean> orderListBeans = new ArrayList<ProductOrderListBean>();
    //Declare All Listing Field
    ProductListOrderAdapter listOrderAdapter;
    String captured_image;
    String order_number;
    String captured_image_url;
    String breadth;
    String remark;
    String customer_name;
    String customer_mobile;
    String qty;
    String order_id;
    String colour;
    String uom;
    String thick;
    String product_display_name;
    String captured_bredth;
    String delivery_date;
    String captured_length;
    String order_date;
    String dealer_name;
    String status_filter = "";
    String status_filter_id = "";
    private DatePickerDialog.OnDateSetListener datePickerListener_date_from = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        @SuppressLint("NewApi")
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            try {
                DecimalFormat mFormat = new DecimalFormat("00");
                mFormat.setRoundingMode(RoundingMode.DOWN);

                date_from = mFormat.format(Double.valueOf(year)) + "-"
                        + mFormat.format(Double.valueOf(monthOfYear + 1)) + "-"
                        + mFormat.format(Double.valueOf(dayOfMonth));

                date_from = GlobalVariables.getCurrentDateInDisplayFormat(
                        getActivity(), date_from);
                // delivery_date = HelperMethods.getDDmmYYYY(delivery_date);
                textview_date_from.setText(date_from);
            } catch (Exception e) {
                Log.d("exception", e.getMessage());
            }

        }

    };
    private DatePickerDialog.OnDateSetListener datePickerListener_date_to = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        @SuppressLint("NewApi")
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            try {
                DecimalFormat mFormat = new DecimalFormat("00");
                mFormat.setRoundingMode(RoundingMode.DOWN);
                date_to = mFormat.format(Double.valueOf(year)) + "-"
                        + mFormat.format(Double.valueOf(monthOfYear + 1)) + "-"
                        + mFormat.format(Double.valueOf(dayOfMonth));
                date_to = GlobalVariables.getCurrentDateInDisplayFormat(
                        getActivity(), date_to);
                // delivery_date = HelperMethods.getDDmmYYYY(delivery_date);
                textview_date_to.setText(date_to);
            } catch (Exception e) {
                Log.d("exception", e.getMessage());
            }

        }

    };

    public static void defaultTwoButtonDialog_delete(Activity activity, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do things

                        dialog.cancel();
                    }
                });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub

            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public static void defaultTwoButtonDialog_edit(Activity activity, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do things

                        dialog.cancel();
                    }
                });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub

            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public static ProductOrderView getInstance() {
        if (instance == null)
            instance = new ProductOrderView();
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.fragment_order_view, container, false);
        mContext = getActivity();
        mPrefs = getActivity().getSharedPreferences("Location", Context.MODE_PRIVATE);
        mActivity = getActivity();
        con = new ConnectionDetector(getActivity());
        getUiObject(view);
        setOnClickListner();
        getCurrentDate();
        if (getArguments() != null) {
            order_menu = getArguments().getBoolean("order_menu");
        }
        if (order_menu) {
            relativeLayoutToolbar.setVisibility(View.VISIBLE);
            tvTitle.setText("View Order");
            relativeLayoutBackBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    getActivity().finish();

                }
            });
        } else {
            ((HomeScreen) getActivity()).txt_title.setText("View Orders");
            ((HomeScreen) getActivity()).profile_image.setVisibility(View.GONE);
            ((HomeScreen) getActivity()).txt_title.setVisibility(View.VISIBLE);
        }


        /*
         * Get Order details
         */
        //	getOrderDetails();

        getAllOrder();
	/*	if(orderListBeans.size()==0)
		{	Toast.makeText(mActivity, "You don't have any order", Toast.LENGTH_LONG).show();}
   */

        view.setOnClickListener(null);

        return view;

    }
    private void getAllOrder() {
        try {
            requset_no = "1";
            orderListBeans.clear();
            jsonObjectRequest = new JSONObject();
            jsonObjectRequest.put("request", ApiList.API_GET_ALL_ORDER);
            jsonObjectRequest.put("p_from_date", HelperMethods.getDDMMMyyyy(HelperMethods.getFifteenDaysOldDate()));
            jsonObjectRequest.put("p_to_date", HelperMethods.getDDMMMyyyy(HelperMethods.getCurrentDate()));
            jsonObjectRequest.put("p_dealer_name", "");
            jsonObjectRequest.put("p_status", "");
            Log.e("##request##", jsonObjectRequest.toString());
            new MyAsyncTask(getActivity(), ProductOrderView.this, jsonObjectRequest, ApiList.URL_PLACE_ORDER).execute();
        } catch (Exception e) {

            Log.e("error", "" + e);

        }


    }

    private void getUiObject(View view) {
        listView = (ListView) view.findViewById(R.id.listView1);
        llayout_date_filter = (RelativeLayout) view.findViewById(R.id.llayout_date_filter);
        llayout_status_lable = (RelativeLayout) view.findViewById(R.id.llayout_status_lable);
        rlayout_devider_two = (RelativeLayout) view.findViewById(R.id.rlayout_devider_two);
        textview_date_from = (TextView) view.findViewById(R.id.textview_date_from);
        textview_date_to = (TextView) view.findViewById(R.id.textview_date_to);
        textview_status = (TextView) view.findViewById(R.id.textview_status);
        textview_filter_order = (TextView) view.findViewById(R.id.textview_filter_order);
        textview_date_from.setText(HelperMethods.getFifteenDaysOldDate());
        textview_date_to.setText(HelperMethods.getCurrentDate());
        relativeLayoutToolbar = (RelativeLayout) view.findViewById(R.id.rlheader);
        relativeLayoutBackBtn = (RelativeLayout) view.findViewById(R.id.back_btn);
        tvTitle = (TextView) view.findViewById(R.id.title_text);


/*
	     llayout_date_filter.setVisibility(View.VISIBLE);
	     llayout_status_lable.setVisibility(View.VISIBLE);
	     rlayout_devider_two.setVisibility(View.VISIBLE);  */


    }

    /*
     * ==========================================================================
     * ========== Method for populating date Picker
     *
     * ==========================================================================
     * ===========
     */

    public void setOnClickListner() {
        textview_date_from.setOnClickListener(this);
        textview_date_to.setOnClickListener(this);
        textview_filter_order.setOnClickListener(this);
        textview_status.setOnClickListener(this);

    }

    public void getOrderDetails() {

        if (HelperMethods.isNetworkAvailable(getActivity())) {
            jsonObjectRequest = new JSONObject();
            try {
                //change it as per need

                requset_no = "1";
                jsonObjectRequest.put("request", ApiList.API_PRODUCT_COLOR);
                Log.d("#request#", "" + jsonObjectRequest.toString());
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            MyCustomAsyncTask asyncTask = new MyCustomAsyncTask(getActivity(), ProductOrderView.this, jsonObjectRequest, ApiList.URL_ORDER_DETAILS);
            asyncTask.execute();

        } else {
            GlobalVariables.defaultOneButtonDialog(getActivity(), "Network error");
        }

    }

    @Override
    public void onTaskComplete(String result) {
        try {
            if (requset_no.equals("1")) {
                if (result != null && result.length() > 0) {
                    jsonObjectResponse = new JSONObject(result);
                    Log.e("response", " " + jsonObjectResponse);
                    String status = jsonObjectResponse.getString("status");
                    if (orderListBeans.size() > 0)
                        orderListBeans.clear();
                    if (status.equalsIgnoreCase("200")) {
                        JSONObject data = jsonObjectResponse.getJSONObject("data");
                        JSONArray productListArray = data.getJSONArray("product");
                        for (int i = 0; i < productListArray.length(); i++) {
                            JSONObject jsonObject = productListArray.getJSONObject(i);
                            String captured_image = jsonObject.getString("captured_image");
                            String order_number = jsonObject.getString("order_number");
                            String captured_image_url = jsonObject.getString("captured_image_url");
                            String breadth = jsonObject.getString("breadth");
                            String customer_name = jsonObject.getString("customer_name");
                            String customer_mobile = jsonObject.getString("customer_mobile");
                            String order_id = jsonObject.getString("order_id");
                            String colour = jsonObject.getString("colour");
                            String qty = jsonObject.getString("qty");
                            String remark = jsonObject.getString("remark");
                            String uom = jsonObject.getString("uom");
                            String thick = jsonObject.getString("thick");
                            String product_display_name = jsonObject.getString("product_display_name");
                            String color_applicable_yn = jsonObject.getString("COLOR_APPLICABLE_YN");
                            String captured_bredth = jsonObject.getString("captured_bredth");
                            String delivery_date = HelperMethods.getDDmmYYYYByHiphon(jsonObject.getString("delivery_date"));
                            String captured_length = jsonObject.getString("captured_length");
                            String order_date = HelperMethods.getDDmmYYYYByHiphon(jsonObject.getString("order_date"));
                            String dealer_name = jsonObject.getString("dealer_name");
                            String dealer_category = jsonObject.getString("dealer_category");
                            //String dealerChnlPartnerGroup=jsonObject.getString("channel_partner_group");
                            //String dealerChnlPartnerId=jsonObject.getString("channel_partner_id");
                            String length = jsonObject.getString("length");
                            String order_status = jsonObject.getString("status");
                            String location_code = jsonObject.getString("location_code");
                            String location_name = jsonObject.getString("location_name");
                            String p_auto_size_flag = jsonObject.getString("auto_size_flag");
                            ProductOrderListBean entry = new ProductOrderListBean();
                            entry.setBreadth(breadth);
                            entry.setCaptured_bredth(captured_bredth);
                            entry.setCaptured_image(captured_image);
                            entry.setCaptured_image_url(captured_image_url.replace(" ", "%20"));
                            entry.setCaptured_length(captured_length);
                            entry.setColour(colour);
                            entry.setCustomer_mobile(customer_mobile);
                            entry.setCustomer_name(customer_name);
                            entry.setDealer_name(dealer_name);
                            entry.setDealer_category(dealer_category);
                            entry.setDelivery_date(delivery_date);
                            entry.setOrder_date(order_date);
                            entry.setOrder_number(order_number);
                            entry.setOrder_id(order_id);
                            entry.setProduct_display_name(product_display_name);
                            entry.setColor_applicable_yn(color_applicable_yn);
                            entry.setQty(qty);
                            entry.setRemark(remark);
                            entry.setThick(thick);
                            entry.setLength(length);
                            entry.setStatus(order_status);
                            entry.setUom(uom);
                            entry.setLocation_code(location_code);
                            entry.setLocation_name(location_name);
                            entry.setP_auto_size_flag(p_auto_size_flag);
                            entry.setP_auto_size_flag(p_auto_size_flag);
                            orderListBeans.add(entry);
                        }
                        displayOrderList(orderListBeans);
                    } else {
                        //	customAsyncTask.hideDialog(getActivity());
                        JSONObject data = jsonObjectResponse.getJSONObject("data");
                        String msg = data.getString("op_message");
                        GlobalVariables.defaultOneButtonDialog(getActivity(), "" + msg);
                    }

                } else {
                    Toast.makeText(getActivity(), "No Data returned", Toast.LENGTH_SHORT).show();
                }


            } else if (requset_no.equals("2"))   //deleting order
            {
                if (result != null && result.length() > 0) {
                    jsonObjectResponse = new JSONObject(result);
                    Log.e("response", " " + jsonObjectResponse);
                    String status = jsonObjectResponse.getString("status");
                    if (status.equalsIgnoreCase("200")) {
                        JSONObject data = jsonObjectResponse.getJSONObject("data");
                        String msg = data.getString("op_message");
                        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                        orderListBeans.remove(deleted_position);
                        listOrderAdapter.notifyDataSetChanged();
							/*ProductOrderView fragment=new ProductOrderView();
							 getActivity().getSupportFragmentManager().beginTransaction()
						    	.add(R.id.flayout, fragment)
						        .addToBackStack(null)
						        .commit();*/

						/*	getActivity().getSupportFragmentManager().popBackStack();
							ProductOrderView fragment=new ProductOrderView();
							 getActivity().getSupportFragmentManager().beginTransaction()
						    	.add(R.id.flayout, fragment)
						        .addToBackStack(null)
						        .commit();

							getAllOrder();
							*/

                    } else {
                        //	customAsyncTask.hideDialog(getActivity());
                        JSONObject data = jsonObjectResponse.getJSONObject("data");
                        String msg = data.getString("op_message");
                        GlobalVariables.defaultOneButtonDialog(getActivity(), "" + msg);
                    }

                } else {
                    Toast.makeText(getActivity(), "No Data returned", Toast.LENGTH_SHORT).show();
                }


            }


        } catch (Exception e) {
            // TODO: handle exception
            Log.e("Product List View", "" + e);
        }


    }

    private void displayOrderList(ArrayList<ProductOrderListBean> arrayList) {
        try {
            if (orderListBeans.size() == 0) {
                //hideFilter()
                GlobalVariables.defaultOneButtonDialog(mActivity, "No Orders available");
                listOrderAdapter = new ProductListOrderAdapter(orderListBeans, getActivity(),
                        ProductOrderView.this, getActivity().getSupportFragmentManager());
                listView.setAdapter(listOrderAdapter);
            } else {
               // showFilter();
                listOrderAdapter = new ProductListOrderAdapter(orderListBeans, getActivity(),
                        ProductOrderView.this, getActivity().getSupportFragmentManager());
                listView.setAdapter(listOrderAdapter);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            Toast.makeText(getActivity(), "" + e, Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    public void datePicker_date_from() {
        DatePickerDialog _date = new DatePickerDialog(getActivity(), datePickerListener_date_from, year, month, day);
			/*_date.getDatePicker().setMinDate(
					System.currentTimeMillis() + 1000 * 24 * 60 * 60);
			_date.getDatePicker().setMaxDate(
					System.currentTimeMillis() + 20 * 1000 * 24 * 60 * 60);*/
        _date.show();
        _date.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == DialogInterface.BUTTON_NEGATIVE) {

                        }
                    }
                });
    }

    public void datePicker_date_to() {

        DatePickerDialog _date = new DatePickerDialog(getActivity(),
                datePickerListener_date_to, year, month, day);
			/*_date.getDatePicker().setMinDate(
					System.currentTimeMillis() + 1000 * 24 * 60 * 60);
			_date.getDatePicker().setMaxDate(
					System.currentTimeMillis() + 20 * 1000 * 24 * 60 * 60);*/

        _date.show();

        _date.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == DialogInterface.BUTTON_NEGATIVE) {

                        }
                    }
                });
    }

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
        //Log.e("Date", " = " + day);

    }
    public void dialog_status() {

        final CharSequence[] items = {"Approved", "Reject", "All"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Please Select status");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                // Do something with the selection

                status_filter = items[item].toString();
                textview_status.setText(items[item].toString());
                status_filter_id = String.valueOf(item + 1);
                if (item == 2) {
                    status_filter_id = "";
                }

                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

        switch (v.getId()) {

            case R.id.textview_date_from:

                datePicker_date_from();

                break;

            case R.id.textview_date_to:

                datePicker_date_to();

                break;

            case R.id.textview_status:

                dialog_status();

                break;
            case R.id.textview_filter_order:
                getFromAndToDate();
                if (con.isConnectingToInternet()) {
                    if ((date_from != null && date_to != null) || status_filter != null) {
                        if (date_from.length() > 0 && date_to.length() > 0) {
                            //Toast.makeText(getActivity(), "date from "+date_from, Toast.LENGTH_LONG).show();
                            //Toast.makeText(getActivity(), "date to "+date_to, Toast.LENGTH_LONG).show();

                            int date_difference = HelperMethods.getDateDifferenceInDays(date_from, date_to);
                            //Toast.makeText(getActivity(), ""+date_difference, Toast.LENGTH_LONG).show();
                            if (date_difference == 0 || date_difference > 0) {
                                getDateFilterOrder();
                            } else {
                                GlobalVariables.defaultOneButtonDialog(getActivity(), GlobalVariables.DATE_DIFFERENCE_ERROR);
                            }
                        } else if (status_filter.length() > 0) {
                            getDateFilterOrder();
                        } else if (date_from.length() == 0) {
                            GlobalVariables.defaultOneButtonDialog(getActivity(), "Please select FROM Date");
                        } else {
                            GlobalVariables.defaultOneButtonDialog(getActivity(), "Please select TO Date");
                        }

                    } else if (date_from != null) {
                        GlobalVariables.defaultOneButtonDialog(getActivity(), "Please select from date");
                    } else {
                        GlobalVariables.defaultOneButtonDialog(getActivity(), "Please select to date");
                    }


                } else {
                    GlobalVariables.defaultOneButtonDialog(getActivity(), GlobalVariables.CONNECTION_ERROR);
                }
                break;

        }
    }

    private void getDateFilterOrder() {
        // TODO Auto-generated method stub

        try {
            requset_no = "1";
            orderListBeans.clear();

            jsonObjectRequest = new JSONObject();
            jsonObjectRequest.put("request", ApiList.API_GET_ALL_ORDER
            );
            if (date_from.equals("") && date_to.equals("")) {
                jsonObjectRequest.put("p_from_date", HelperMethods.getDDMMMyyyy(HelperMethods.getFifteenDaysOldDate()));
                jsonObjectRequest.put("p_to_date", HelperMethods.getDDMMMyyyy(HelperMethods.getCurrentDate()));
            } else {
                jsonObjectRequest.put("p_from_date", HelperMethods.getDDMMMyyyy(date_from));
                jsonObjectRequest.put("p_to_date", HelperMethods.getDDMMMyyyy(date_to));
            }
            jsonObjectRequest.put("p_dealer_name", "");
            jsonObjectRequest.put("p_status", status_filter_id);

            Log.e("###request###", jsonObjectRequest.toString());
            new MyAsyncTask(getActivity(), ProductOrderView.this, jsonObjectRequest, ApiList.URL_PLACE_ORDER).execute();


        } catch (Exception e) {

            Log.e("error", "" + e);

        }


    }

    /**
     * Method : This method is used for get date_from and date_to from UI control.
     **/
    public void getFromAndToDate() {
        date_from = textview_date_from.getText().toString().trim();
        date_to = textview_date_to.getText().toString().trim();
        status_filter = textview_status.getText().toString().trim();

    }


    // Method: used to show filter if item list is greater that 0.
    public void showFilter() {
        llayout_date_filter.setVisibility(View.VISIBLE);
        llayout_status_lable.setVisibility(View.VISIBLE);
        rlayout_devider_two.setVisibility(View.VISIBLE);

    }

    // Method: used to hide filter if item list is equal to 0.
    public void hideFilter() {
        llayout_date_filter.setVisibility(View.GONE);
        llayout_status_lable.setVisibility(View.GONE);
        rlayout_devider_two.setVisibility(View.GONE);

    }


}
