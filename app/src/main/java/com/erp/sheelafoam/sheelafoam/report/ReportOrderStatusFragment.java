package com.erp.sheelafoam.sheelafoam.report;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.erp.sheelafoam.HomeScreen;
import com.erp.sheelafoam.R;
import com.erp.sheelafoam.sheelafoam.adapter.ProductListOrderAdapter;
import com.erp.sheelafoam.sheelafoam.adapter.ReportDealerAdapter;
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
import java.util.HashSet;


@SuppressLint("ResourceAsColor")
public class ReportOrderStatusFragment extends Fragment implements AsyncTaskListner, OnClickListener {

    /*
     * Singleton class for accessing methods from other activity/fragments
     * */
    public static ReportOrderStatusFragment fragment;
    public String requset_no;
    public int deleted_position;
    /**
     * flag for check getOrder request is processing very first or no.
     * by default this flag will be false;
     **/

    public boolean isRequest = false;
    Context mContext;
    ReportDealerAdapter reportDealerAdapter;
    SharedPreferences mPrefs;
    ImageView gender_icon, upload_pic;
    TextView textview_date_from;
    TextView textview_date_to;
    TextView textview_filter_order;
    TextView textview_dealer;
    TextView textview_status;
    Activity mActivity;
    JSONObject jsonObjectRequest, jsonObjectResponse;
    ListView listView;
    TableLayout table_layout;
    ArrayList<ProductOrderListBean> orderListBeans = new ArrayList<ProductOrderListBean>();
    HashSet<String> set_dealer_name = new HashSet<String>();
    ProductListOrderAdapter listOrderAdapter;
    //Declare All Listing Field
    ConnectionDetector con;
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
    //String  delivery_date;
    String captured_length;
    String order_date;
    String dealer_name;
    View view;
    String date_from = "";
    String date_to = "";
    /**
     * String  for  filter status.
     **/

    String status_filter = "";
    String dealer_filter = "";
    String status_filter_id = "";
    Boolean order_menu = false;
    RelativeLayout relativeLayoutToolbar;
    RelativeLayout relativeLayoutBackBtn;
    TextView title;
    private int year;
    private int month;
    private int day;
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



    /*
     * ====================================================================================
     * onCreateView Method for initializing all the views & objects
     * that will be used in package
     * =====================================================================================
     */

    public ReportOrderStatusFragment() {
        fragment = this;
    }

    public static ReportOrderStatusFragment getInstance() {
        if (fragment == null) {
            fragment = new ReportOrderStatusFragment();
        }
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub


        view = inflater.inflate(R.layout.fragment_report_order_status, container, false);
        mContext = getActivity();
        con = new ConnectionDetector(getActivity());
        mPrefs = getActivity().getSharedPreferences("Location", Context.MODE_PRIVATE);

        mActivity = getActivity();

        getCurrentDate();
        getUiObject(view);

        if (getArguments() != null) {
            order_menu = getArguments().getBoolean("order_menu");
        }
        if (order_menu) {
            relativeLayoutToolbar.setVisibility(View.VISIBLE);
            title.setText("Order Report");
            relativeLayoutBackBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    getActivity().onBackPressed();
                }
            });
        } else {
            ((HomeScreen) getActivity()).txt_title.setText("Order Report");
            ((HomeScreen) getActivity()).profile_image.setVisibility(View.GONE);
            ((HomeScreen) getActivity()).txt_title.setVisibility(View.VISIBLE);
        }

        setOnClickListner();
        /*
         * Get Order details
         */
        //	getOrderDetails();
        if (con.isConnectingToInternet()) {
            getAllOrder();
        } else {
            GlobalVariables.defaultOneButtonDialog(getActivity(), GlobalVariables.CONNECTION_ERROR);
        }
	/*	if(orderListBeans.size()==0)
		{	Toast.makeText(mActivity, "You don't have any order", Toast.LENGTH_LONG).show();}
   */
        float scale = getResources().getConfiguration().fontScale;

        Log.e("scale", "" + scale);

        //getResources().getConfiguration().fontScale=1f;


//			 Settings.System.putFloat( getActivity().getContentResolver(), Settings.System.FONT_SCALE, 1f );


        view.setOnClickListener(null);

        return view;
    }


    /*
     * ====================================================================================
     * Method for getting referencing view from layout
     * =====================================================================================
     **/

    private void getAllOrder() {
        // TODO Auto-generated method stub

        try {
            requset_no = "1";
            orderListBeans.clear();
            //set_dealer_name.clear();
            JSONObject jsonObjectRequest = new JSONObject();
            //jsonObjectRequest=new JSONObject();
            jsonObjectRequest.put("request", ApiList.API_GET_ALL_ORDER);
            jsonObjectRequest.put("p_from_date", HelperMethods.getDDMMMyyyy(HelperMethods.getFifteenDaysOldDate()));
            jsonObjectRequest.put("p_to_date", HelperMethods.getDDMMMyyyy(HelperMethods.getCurrentDate()));
            Log.e("##request##", jsonObjectRequest.toString());
            new MyAsyncTask(getActivity(), ReportOrderStatusFragment.this, jsonObjectRequest, ApiList.URL_PLACE_ORDER).execute();


        } catch (Exception e) {

            Log.e("error", "" + e);

        }


    }

    /**
     * Method : This method used for filter date json request.
     *
     * @param date_from : This is old date
     * @param date_to   : This is to_date means current date
     **/

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
            if (dealer_filter.equals("All")) {
                dealer_filter = "";
            }
            jsonObjectRequest.put("p_dealer_name", dealer_filter);
            jsonObjectRequest.put("p_status", status_filter_id);


            Log.e("##request##", jsonObjectRequest.toString());
            new MyAsyncTask(getActivity(), ReportOrderStatusFragment.this, jsonObjectRequest, ApiList.URL_PLACE_ORDER).execute();


        } catch (Exception e) {

            Log.e("error", "" + e);

        }


    }

    private void getUiObject(View view) {
        // TODO Auto-generated method stub


        listView = (ListView) view.findViewById(R.id.listView1);
        table_layout = (TableLayout) view.findViewById(R.id.table);
        textview_date_from = (TextView) view.findViewById(R.id.textview_date_from);
        textview_date_to = (TextView) view.findViewById(R.id.textview_date_to);
        textview_filter_order = (TextView) view.findViewById(R.id.textview_filter_order);
        textview_dealer = (TextView) view.findViewById(R.id.textview_dealer);
        textview_status = (TextView) view.findViewById(R.id.textview_status);

        textview_date_from.setText(HelperMethods.getFifteenDaysOldDate());
        textview_date_to.setText(HelperMethods.getCurrentDate());

        relativeLayoutToolbar = (RelativeLayout) view.findViewById(R.id.rlheader);
        relativeLayoutBackBtn = (RelativeLayout) view.findViewById(R.id.back_btn);
        title = (TextView) view.findViewById(R.id.title_text);


    }

    public void setOnClickListner() {
        textview_date_from.setOnClickListener(this);
        textview_date_to.setOnClickListener(this);
        textview_filter_order.setOnClickListener(this);
        textview_status.setOnClickListener(this);
        textview_dealer.setOnClickListener(this);
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

            case R.id.textview_dealer:
                try {
                    if (orderListBeans.size() > 0) {
                        //dialog_dealer(orderListBeans);
                        dialog_Dealer_New();
                    } else {

                    }
                } catch (Exception e) {
                    // TODO: handle exception
                }


                break;

            case R.id.textview_filter_order:
                getFromAndToDate();
                if (con.isConnectingToInternet()) {
                    if ((date_from != null && date_to != null) || dealer_filter != null || status_filter != null) {
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
                        } else if (dealer_filter.length() > 0) {
                            getDateFilterOrder();
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

            MyCustomAsyncTask asyncTask = new MyCustomAsyncTask(getActivity(), ReportOrderStatusFragment.this, jsonObjectRequest, ApiList.URL_ORDER_DETAILS);
            asyncTask.execute();

        } else {
            GlobalVariables.defaultOneButtonDialog(getActivity(), "Network error");
        }

    }


    //http://www.prandroid.com/2014/05/creating-table-layout-dynamically-in.html

    @Override
    public void onTaskComplete(String result) {
        // TODO Auto-generated method stub

        // check it

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


                            String captured_bredth = jsonObject.getString("captured_bredth");

                            String delivery_date = HelperMethods.getDDmmYYYYByHiphon(jsonObject.getString("delivery_date"));

                            String captured_length = jsonObject.getString("captured_length");

                            String order_date = HelperMethods.getDDmmYYYYByHiphon(jsonObject.getString("order_date"));

                            String dealer_name = jsonObject.getString("dealer_name");

                            String length = jsonObject.getString("length");
                            String order_status = jsonObject.getString("status");
                            String status_updation_date = jsonObject.getString("status_updation_date");
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
                            entry.setDelivery_date(delivery_date);
                            entry.setOrder_date(order_date);
                            entry.setOrder_number(order_number);
                            entry.setOrder_id(order_id);
                            entry.setProduct_display_name(product_display_name);
                            entry.setQty(qty);
                            entry.setRemark(remark);
                            entry.setThick(thick);

                            entry.setLength(length);

                            entry.setUom(uom);
                            entry.setStatus(order_status);
                            entry.setStatus_updation_date(status_updation_date);

                            orderListBeans.add(entry);
                            if (!isRequest) {
                                set_dealer_name.add(dealer_name);
                            }


                        }

                        //displayOrderList(orderListBeans);
                        table_layout.removeAllViews();
                        CreateTableRow(view, orderListBeans);
                        isRequest = true;
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


    /*
     * ==========================================================================
     * ========== Method for populating date Picker
     *
     * ==========================================================================
     * ===========
     */

    private void displayOrderList(ArrayList<ProductOrderListBean> arrayList) {
        // TODO Auto-generated method stub


        try {
            if (orderListBeans.size() == 0) {
                GlobalVariables.defaultOneButtonDialog(mActivity, "No Orders available");
            } else {
                listOrderAdapter = new ProductListOrderAdapter(orderListBeans, getActivity(),
                        ReportOrderStatusFragment.this, getActivity().getSupportFragmentManager());
                listView.setAdapter(listOrderAdapter);


            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            Toast.makeText(getActivity(), "" + e, Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    public void CreateTableRow(View v, ArrayList<ProductOrderListBean> orderListBeans) {

        TableRow tbrow0 = new TableRow(getActivity());
        TextView tv0 = new TextView(getActivity());
        tv0.setText(" Order No. ");
        tv0.setEms(4);
        tv0.setMaxLines(2);
        tv0.setTextColor(Color.WHITE);
        tv0.setGravity(Gravity.CENTER_VERTICAL);
        tv0.setPadding(getResources().getDimensionPixelSize(R.dimen.report_header_padding), getResources().getDimensionPixelSize(R.dimen.report_header_padding), getResources().getDimensionPixelSize(R.dimen.report_header_padding), getResources().getDimensionPixelSize(R.dimen.report_header_padding));
        tv0.setBackgroundColor(getResources().getColor(R.color.header_background));
        tv0.setTextSize(getResources().getDimension(R.dimen.report_text_size));

        // tv0.setTypeface(null,Typeface.NORMAL);
        tbrow0.addView(tv0);
        TextView tv1 = new TextView(getActivity());
        tv1.setText(" Product ");
        tv1.setTextColor(Color.WHITE);
        tv1.setGravity(Gravity.CENTER_VERTICAL);
        tv1.setPadding(0, getResources().getDimensionPixelSize(R.dimen.report_header_padding), getResources().getDimensionPixelSize(R.dimen.report_header_padding), 0);
        tv1.setBackgroundColor(getResources().getColor(R.color.header_background));
        tv1.setTextSize(getResources().getDimension(R.dimen.report_text_size));
        //  tv1.setTypeface(null,Typeface.NORMAL);
        tbrow0.addView(tv1);
       /* TextView tv2 = new TextView(getActivity());
        tv2.setText(" Size ");
        tv2.setTextColor(Color.WHITE);
        tv2.setBackgroundColor(getResources().getColor(R.color.header_background));
        tbrow0.addView(tv2); */
        TextView tv3 = new TextView(getActivity());
        tv3.setText(" Status ");
        tv3.setTextColor(Color.WHITE);
        tv3.setGravity(Gravity.CENTER_VERTICAL);
        tv3.setPadding(getResources().getDimensionPixelSize(R.dimen.report_header_padding), getResources().getDimensionPixelSize(R.dimen.report_header_padding), getResources().getDimensionPixelSize(R.dimen.report_header_padding), getResources().getDimensionPixelSize(R.dimen.report_header_padding));
        tv3.setBackgroundColor(getResources().getColor(R.color.header_background));
        tv3.setTextSize(getResources().getDimension(R.dimen.report_text_size));
        // tv3.setTypeface(null,Typeface.NORMAL);
        // tbrow0.setBackgroundColor(getResources().getColor(R.color.header_background));
        tbrow0.addView(tv3);
        //details

        TextView tv4 = new TextView(getActivity());
        tv4.setText(" Details ");
        tv4.setTextColor(Color.WHITE);
        tv4.setGravity(Gravity.CENTER_VERTICAL);
        tv4.setPadding(getResources().getDimensionPixelSize(R.dimen.report_header_padding), getResources().getDimensionPixelSize(R.dimen.report_header_padding), getResources().getDimensionPixelSize(R.dimen.report_header_padding), 0);
        tv4.setBackgroundColor(getResources().getColor(R.color.header_background));
        tv4.setTextSize(getResources().getDimension(R.dimen.report_text_size));
        // tv4.setTypeface(null,Typeface.NORMAL);
        tbrow0.addView(tv4);

        tbrow0.setBackgroundColor(getResources().getColor(R.color.header_background));

        TableRow.LayoutParams tableRowParams = new TableRow.LayoutParams();
        tableRowParams.setMargins(0, getResources().getDimensionPixelSize(R.dimen.report_header_padding), 0, getResources().getDimensionPixelSize(R.dimen.report_header_padding));
        tableRowParams.weight = 1;

        table_layout.addView(tbrow0, tableRowParams);
        for (int i = 0; i < orderListBeans.size(); i++) {


            TableRow tbrow = new TableRow(getActivity());
            TextView t1v = new TextView(getActivity());
            t1v.setText(orderListBeans.get(i).getOrder_number());
            t1v.setTextColor(Color.BLACK);
            t1v.setGravity(Gravity.CENTER_VERTICAL);
            t1v.setPadding(getResources().getDimensionPixelSize(R.dimen.report_header_padding), getResources().getDimensionPixelSize(R.dimen.report_header_padding_extra), 0, getResources().getDimensionPixelSize(R.dimen.report_header_padding_extra));
            t1v.setTextSize(getResources().getDimension(R.dimen.report_text_size));
            //t1v.setTypeface(null,Typeface.NORMAL);
            tbrow.addView(t1v);
            TextView t2v = new TextView(getActivity());
            t2v.setText(orderListBeans.get(i).getProduct_display_name());
            t2v.setEms(8);
            t2v.setMaxLines(3);
            t2v.setTextColor(Color.BLACK);
            t2v.setGravity(Gravity.CENTER_VERTICAL);
            t2v.setPadding(0, getResources().getDimensionPixelSize(R.dimen.report_header_padding_extra), 0, getResources().getDimensionPixelSize(R.dimen.report_header_padding_extra));
            t2v.setTextSize(getResources().getDimension(R.dimen.report_text_size));
            //t2v.setTypeface(null,Typeface.NORMAL);
            tbrow.addView(t2v);
//            TextView t3v = new TextView(getActivity());
//
//
//            t3v.setText(orderListBeans.get(i).getLength()+"x"+orderListBeans.get(i).getBreadth()+"x"+orderListBeans.get(i).getThick());
//            t3v.setTextColor(Color.BLACK);
//            t3v.setGravity(Gravity.CENTER);
//            tbrow.addView(t3v,tableRowParams);
            TextView t4v = new TextView(getActivity());
            if (orderListBeans.get(i).getStatus().equals("0")) {
                t4v.setText("Pending");
            } else if (orderListBeans.get(i).getStatus().equals("1")) {
                t4v.setText("Approve");
            } else if (orderListBeans.get(i).getStatus().equals("2")) {
                t4v.setText("Reject");
            } else if (orderListBeans.get(i).getStatus().equals("3")) {
                t4v.setText("Dispatched");
            } else {
                t4v.setText("");
            }

            t4v.setTextColor(Color.BLACK);
            t4v.setGravity(Gravity.CENTER_VERTICAL);
            t4v.setPadding(getResources().getDimensionPixelSize(R.dimen.report_header_padding), getResources().getDimensionPixelSize(R.dimen.report_header_padding_extra), getResources().getDimensionPixelSize(R.dimen.report_header_padding), getResources().getDimensionPixelSize(R.dimen.report_header_padding_extra));
            // t4v.setEms(3);
            t4v.setTextSize(getResources().getDimension(R.dimen.report_text_size));
            // t4v.setTypeface(null,Typeface.NORMAL);
            tbrow.addView(t4v, tableRowParams);

            final TextView t5v = new TextView(getActivity());
            t5v.setText("More");

            t5v.setTextColor(Color.BLACK);
            t5v.setGravity(Gravity.CENTER_VERTICAL);
            t5v.setPadding(getResources().getDimensionPixelSize(R.dimen.report_header_padding), getResources().getDimensionPixelSize(R.dimen.report_header_padding_extra), getResources().getDimensionPixelSize(R.dimen.report_header_padding), getResources().getDimensionPixelSize(R.dimen.report_header_padding_extra));
            t5v.setTextSize(getResources().getDimension(R.dimen.report_text_size));
            // t5v.setTypeface(null,Typeface.NORMAL);
            t5v.setId(i);


            tbrow.setId(i);
            tbrow.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    int position = t5v.getId();

                    //Toast.makeText(getActivity(), ""+position, Toast.LENGTH_LONG).show();

                    createDetailDialog(position);
                }
            });
            tbrow.addView(t5v, tableRowParams);

            if (i % 2 == 1) {
                //tbrow.setBackgroundColor(getResources().getColor(R.color.report_background1));
            } else {
                tbrow.setBackgroundColor(getResources().getColor(R.color.report_background2));
            }

            TableRow.LayoutParams tableRowParams_body = new TableRow.LayoutParams();
            tableRowParams_body.setMargins(0, 0, 0, 0);

            table_layout.addView(tbrow, tableRowParams_body);
        }

    }

    public void datePicker_date_from() {

        DatePickerDialog _date = new DatePickerDialog(getActivity(),
                datePickerListener_date_from, year, month, day);
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

    /**
     * Method : This method is used for get date_from and date_to from UI control.
     **/
    public void getFromAndToDate() {
        date_from = textview_date_from.getText().toString().trim();
        date_to = textview_date_to.getText().toString().trim();
        status_filter = textview_status.getText().toString().trim();
        dealer_filter = textview_dealer.getText().toString().trim();
    }

    public void createDetailDialog(int position) {
        // custom dialog
        final Dialog dialog = new Dialog(getActivity());

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
        dialog.setContentView(R.layout.report_item_details_dialog);
        dialog.setTitle("Product Detail");

        // set the custom dialog components - text, image and button
        final ProductOrderListBean entry = orderListBeans.get(position);


        TextView textview_product_name = (TextView) dialog.findViewById(R.id.textview_item_name);
        TextView textview_order_no = (TextView) dialog.findViewById(R.id.textview_order_no);
        //      TextView textview_delivery_date = (TextView) v.findViewById(R.id.textview_delivery_date);
        TextView textview_delivery_date = (TextView) dialog.findViewById(R.id.textview_delivery_date);
        TextView textview_product_l_w_t_text = (TextView) dialog.findViewById(R.id.textview_product_l_w_t_text);

        TextView textview_product_quantity = (TextView) dialog.findViewById(R.id.textview_product_quantity);

        TextView textview_customer_name = (TextView) dialog.findViewById(R.id.textview_customer_name);
        TextView textview_item_appoval_date = (TextView) dialog.findViewById(R.id.textview_item_appoval_date);
        TextView textview_dealer_name = (TextView) dialog.findViewById(R.id.textview_dealer_name);
        TextView textview_remark = (TextView) dialog.findViewById(R.id.textview_remark);
	     /*  LinearLayout llayout_edit_order = (LinearLayout) v.findViewById(R.id.llayout_edit_order);
	       LinearLayout llayout_cancel_order = (LinearLayout) v.findViewById(R.id.llayout_cancel_order);
	  */


        /**
         *This adapter and ProductOrderView fragment calling from two places
         *first from order managment module and second is from reporting mudule.
         * **/

        String length = (entry.getLength().length() > 0) ? entry.getLength() : "0";
        String breadth = (entry.getBreadth().length() > 0) ? entry.getBreadth() : "0";
        String thick = (entry.getThick().length() > 0) ? entry.getThick() : "0";


        textview_product_name.setText("Item Name: " + entry.getProduct_display_name());
        textview_order_no.setText("Order No: " + entry.getOrder_number());
        textview_delivery_date.setText("Date: " + HelperMethods.getDDMMMyy(entry.getOrder_date()));
//		textview_product_l_w_t_text.setText("LxWxT: "+entry.getLength()+"x"+entry.getBreadth()+"x"+entry.getThick());
        textview_product_l_w_t_text.setText("LxWxT: " + length + "x" + breadth + "x" + thick);
        textview_product_quantity.setText("Qty: " + entry.getQty() + " " + entry.getUom());
        textview_dealer_name.setText("Dealer Name: " + entry.getDealer_name());

        textview_customer_name.setText("Customer Name: " + entry.getCustomer_name());
        textview_remark.setText("Remark :" + entry.getRemark());
        if (entry.getStatus().equals("0")) {
            textview_item_appoval_date.setVisibility(View.GONE);
        } else if (entry.getStatus().equals("1")) {
            textview_item_appoval_date.setText("Appoved Date: " + entry.getStatus_updation_date());
        } else if (entry.getStatus().equals("2")) {
            textview_item_appoval_date.setText("Rejected Date: " + entry.getStatus_updation_date());
        } else {
            textview_item_appoval_date.setVisibility(View.GONE);
        }

        LinearLayout llayout_approved_order = (LinearLayout) dialog.findViewById(R.id.llayout_approved_order);
        llayout_approved_order.setVisibility(View.GONE);

        LinearLayout llayout_reject_order = (LinearLayout) dialog.findViewById(R.id.llayout_reject_order);
        llayout_reject_order.setBackgroundColor(R.color.header_background);
        //llayout_reject_order.setGravity(Gravity.CENTER);

        TextView textview_reject_order = (TextView) dialog.findViewById(R.id.textview_reject_order);
        textview_reject_order.setText("Ok");

        // if button is clicked, close the custom dialog
        llayout_reject_order.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void dialog_status() {
        // 0 for pending
        // 1 for Approved
        // 2 for Rejected
        // 3 for Dispatch.
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
	
	/*public void dialog_dealer(ArrayList<ProductOrderListBean> orderListBeans)
	{
		ArrayList<String> list=new ArrayList<String>(set_dealer_name);
		
		final CharSequence[] items=new CharSequence[list.size()+1];
		for(int i=0;i<list.size();i++)
		{
			items[i]=list.get(i);
			
		}
		 
		Log.e("length", ""+items.length);

		items[items.length-1]="All";
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()); 
		
		builder.setTitle("Please Select dealer");
        
		builder.setItems(items, new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int item) {
		     // Do something with the selection
						
			
			dealer_filter=items[item].toString();
			textview_dealer.setText(items[item].toString());
			if(dealer_filter.equals("All")){
				dealer_filter="";
				
			}
			
			dialog.dismiss();
		}
		});
		AlertDialog alert = builder.create();
		alert.show();
	}*/

    // display dealer dialog with search option.

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

        final ArrayList<String> list = new ArrayList<String>(set_dealer_name);
		
		/*final CharSequence[] items=new CharSequence[list.size()+1];
		for(int i=0;i<list.size();i++)
		{
			items[i]=list.get(i);
			
		}*/

        list.add("All");


        reportDealerAdapter = new ReportDealerAdapter(list, mContext);
        listview.setAdapter(reportDealerAdapter);
        listview.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                HelperMethods.hideSoftKeyboard(getActivity());

                dealer_filter = list.get(position).toString();
                // textview_dealer.setText(dealer_filter);


                Log.e("Dealer Name", "Current:"
                        + textview_dealer.getText().toString().trim()
                        + ",  Selected" + dealer_filter);

                if (textview_dealer.getText().toString().trim()
                        .equalsIgnoreCase(dealer_filter)) {
                    Toast.makeText(getActivity(), "Dealer already selected",
                            Toast.LENGTH_SHORT).show();
                } else {
                    textview_dealer.setText(dealer_filter);
                    //final_dealer_name = dealer_name;


                    dialog.dismiss();


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

        autocomplete_dealer_name.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {


                // TODO Auto-generated method stub
                final ArrayList<String> selectedArrayList = getArrayList(
                        list, autocomplete_dealer_name.getText()
                                .toString().trim());

                reportDealerAdapter = new ReportDealerAdapter(selectedArrayList, mContext);

                listview.setAdapter(reportDealerAdapter);
                listview.setOnItemClickListener(new OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> arg0, View view,
                                            int position, long id) {
                        // TODO Auto-generated method stub
                        HelperMethods.hideSoftKeyboard(getActivity());

                        String dealer_filter = selectedArrayList.get(position);


                        if (textview_dealer.getText().toString().trim()
                                .equalsIgnoreCase(dealer_filter)) {
                            Toast.makeText(getActivity(),
                                    "Dealer already selected",
                                    Toast.LENGTH_SHORT).show();
                        } else {


                            textview_dealer.setText(dealer_filter);

                            dialog.dismiss();


                        }

                    }
                });

            }


            private ArrayList<String> getArrayList(
                    ArrayList<String> list, String filter) {
                // TODO Auto-generated method stub

                ArrayList<String> selectedArrayList = new ArrayList<String>();
                if (filter.length() >= 0) {

                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i)
                                .toLowerCase().contains(filter.toLowerCase()))
                            selectedArrayList.add(list.get(i));
                    }
                    return selectedArrayList;
                } else {
                    return list;
                }
            }
        });

        dialog.show();

    }
}
