package com.erp.sheelafoam.sheelafoam.exchangeschame;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.erp.sheelafoam.HomeScreen;
import com.erp.sheelafoam.R;
import com.erp.sheelafoam.adapter.PendingAdapter;
import com.erp.sheelafoam.constant.Constant;
import com.erp.sheelafoam.models.PendingGCOrderModel;
import com.erp.sheelafoam.models.SpinnerTitle;
import com.erp.sheelafoam.prefrences.SheelaSharedPreference;
import com.erp.sheelafoam.sheelafoam.adapter.ColorAdapter;
import com.erp.sheelafoam.sheelafoam.adapter.ProductListAdapter;
import com.erp.sheelafoam.sheelafoam.entry.ColorBean;
import com.erp.sheelafoam.sheelafoam.entry.DealerBean;
import com.erp.sheelafoam.sheelafoam.entry.LocationBean;
import com.erp.sheelafoam.sheelafoam.entry.ProductListAddEditBean;
import com.erp.sheelafoam.sheelafoam.exchangeschame.model.ConsumerItemModel;
import com.erp.sheelafoam.sheelafoam.interfaces.OnItemButtonClick;
import com.erp.sheelafoam.sheelafoam.utility.ApiList;
import com.erp.sheelafoam.sheelafoam.utility.AppConstant;
import com.erp.sheelafoam.sheelafoam.utility.AsyncTaskListner;
import com.erp.sheelafoam.sheelafoam.utility.GlobalVariables;
import com.erp.sheelafoam.sheelafoam.utility.HelperMethods;
import com.erp.sheelafoam.sheelafoam.utility.MyCustomAsyncTask;
import com.erp.sheelafoam.sheelafoam.utility.SharePref;
import com.erp.sheelafoam.sheelafoam.xperia.dataholders.LBHolder;
import com.erp.sheelafoam.sheelafoam.xperia.dataholders.LBHolderInch;
import com.erp.sheelafoam.sheelafoam.xperia.dataholders.ThiknessHolder;
import com.erp.sheelafoam.sheelafoam.xperia.dataholders.ThiknessHolderInch;
import com.erp.sheelafoam.sheelafoam.xperia.function.DecimalDigitsInputFilter;
import com.erp.sheelafoam.utils.CustomDialog;
import com.erp.sheelafoam.utils.CustomDialog2;
import com.erp.sheelafoam.utils.CustomTypefaceEditText;
import com.erp.sheelafoam.utils.Util;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Pattern;

import static android.graphics.Color.TRANSPARENT;

public class ConsumerOredrActivity extends Activity implements View.OnClickListener, AsyncTaskListner,OnItemButtonClick {
    CustomDialog cd1;
    PendingAdapter adapter;
    private int qtyCounter = 1;
    static CustomDialog2 cd2;
    static CustomDialog cd;
    boolean buttonclick = false;
    List<SpinnerTitle> list;
    int BAR_CODE_RESULT = 0;
    int count = 1;
    int count2 = 1;
    int Total, Reward;
    String  giftMSG, ToBePaid;
    Button btn_Add;
    String product = "";
    ArrayList<ConsumerItemModel> list1 = new ArrayList<>();
    ProgressDialog progressDialog;
    //String getDefaultState = "", getAllState = "", getDealerDetails = "", getAllproduct = "";
    String LBURL = "", COLURL = "", THIKURL = "", DTLURL = "", CATURL = "", PRODURL = "";
    String Mob = "", mobile = "";
    MyCustomAsyncTask customAsyncTask;
    String requset_no = "";
    JSONObject jsonObjectRequest, jsonObjectResponse;
    SharedPreferences loacalData;
    String location_name = "";
    String location_code = "";
    String color_name, op_r_id="";
    String dealer_id;
    boolean statndardClick = false;
    int flag_product, flag_color, flag_location, flag_size;
    String color_applicable_yn = "";
    String user_type = "", user_name = "", quantity = "", userRole = "",
            final_dealer_name = "", final_color_name = "", dealer_name = "", dealer_category = "",
            zone = "", product_name = "", sub_product = "",
            product_specification = "", image_url = "", old_image = "", userId;
    ArrayList<LocationBean> arrayList_location = new ArrayList<LocationBean>();
    ArrayList<ColorBean> arrayList_color = new ArrayList<ColorBean>();
    ArrayList<DealerBean> arrayList_dealer = new ArrayList<DealerBean>();
    ArrayList<ProductListAddEditBean> arrayList_products = new ArrayList<ProductListAddEditBean>();
    ProductListAdapter productListAdapter;
    TextView autocomplete_product_name, tv_totalAmountOrder, tv_totalPrizeAmount, tv_totalPaidAmount;
    ColorAdapter colorAdapter;
    String dealer_state = "";
    //--------------------------------------------------
    ArrayAdapter stateNm, dispNm, thikness, size, category;
    String SizeType = "INCH", CategoryPrev = "ALL", CategoryCurr = "";
    int FirstTimeFlag = 0, ProductChange = 0;
    int InchMmFlag = 1;
    EditText sizeL, sizeB, sizeThikness, tvThickness;
    TextView dispname, drpState, drpCategory, defsize1, defsize2, defsize3, totalRs, Warranty, MrpHeading, TvSwitch, TvMm, TvInch, tvSize;
    ArrayList<String> spthikness = new ArrayList<String>();
    ArrayList<String> spsize = new ArrayList<String>();
    ArrayList<LBHolder> lbHolders = new ArrayList<LBHolder>();
    ArrayList<ThiknessHolder> thiknessHolders = new ArrayList<ThiknessHolder>();
    ArrayList<LBHolderInch> lbHoldersInch = new ArrayList<LBHolderInch>();
    ArrayList<ThiknessHolderInch> thiknessHoldersInch = new ArrayList<ThiknessHolderInch>();
    String Mobile = "", Lenght = "", Bredth = "", Thikness = "", Colour = "", ProductName = "";
    AlertDialog alert;
    RadioGroup radiogroupScheme;
    ListView Lb;
    ListView ThicknessList;
    private EditText etMobileNo, etConsumerName, etAdvance;
    private Button btnRedeemLater, btnRedeemNow;
    private TextView add_item1,tv_consumerDelearName, tv_removeItem, tv_additem, tv_logoname, tv_totalQTY, tv_productColorConsumer, tv_productNameConsumer;
    private ImageButton ib_backicon, ib_filter, ivSearch;
    private CustomTypefaceEditText et_thiknes, et_width, et_length;
    private String length = "", width = "", orderNumber="";
    //------------------------------------------------------------------------------------------------------------------------
    private SharedPreferences mPrefs;
    private ProgressDialog progress;
    private SheelaSharedPreference sheelaSharedPreference;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consumer_order_details);
        sheelaSharedPreference = SheelaSharedPreference.getInstance(this);
        mPrefs = getSharedPreferences("Location", Context.MODE_PRIVATE);
        loacalData = getSharedPreferences(SharePref.MODE_TYPE, Context.MODE_PRIVATE);
        progressDialog = new ProgressDialog(ConsumerOredrActivity.this);
        progressDialog.setMessage("Loading....");
        ib_backicon = (ImageButton) findViewById(R.id.ib_backicon);
        tv_logoname = (TextView) findViewById(R.id.tv_logoname);
        tv_logoname.setText("Pre Order");
        ivSearch = (ImageButton) findViewById(R.id.ivSearch);
        etMobileNo = (EditText) findViewById(R.id.etMobileNo);
        etConsumerName = (EditText) findViewById(R.id.etConsumerName);
        tv_totalQTY = (TextView) findViewById(R.id.tv_totalQTY);
        tv_removeItem = (TextView) findViewById(R.id.tv_removeItem);
        tv_additem = (TextView) findViewById(R.id.tv_additem);
        btnRedeemLater = (Button) findViewById(R.id.btnRedeemLater);
        btnRedeemNow = (Button) findViewById(R.id.btnRedeemNow);
        autocomplete_product_name = (TextView) findViewById(R.id.autocomplete_product_name);
        tv_consumerDelearName= (TextView) findViewById(R.id.tv_consumerDelearName);
        tv_consumerDelearName.setText(  Util.getSharedPrefrenceValue(ConsumerOredrActivity.this, Constant.Sp_DisplayName));
        ivSearch.setOnClickListener(this);
        etMobileNo.setOnClickListener(this);
        btnRedeemNow.setOnClickListener(this);
        btnRedeemLater.setOnClickListener(this);
        tv_additem.setOnClickListener(this);
        tv_removeItem.setOnClickListener(this);
        autocomplete_product_name.setOnClickListener(this);
        ib_backicon.setOnClickListener(this);
        customAsyncTask = new MyCustomAsyncTask(ConsumerOredrActivity.this);

        TvSwitch=(TextView)findViewById(R.id.tvswitch);
        TvSwitch.setOnClickListener(this);
        TvMm=(TextView)findViewById(R.id.tvmm);
        TvMm.setVisibility(View.INVISIBLE);
        TvMm.setOnClickListener(this);
        TvInch=(TextView)findViewById(R.id.tvin);
        TvInch.setOnClickListener(this);

        tvSize=(TextView)findViewById(R.id.tvSizes);
        tvSize.setOnClickListener(this);
        tvThickness = (EditText) findViewById(R.id.tvThikness);
        tvThickness.setOnClickListener(this);
        sizeL = (EditText) findViewById(R.id.mat_width);
        sizeL.setOnClickListener(this);
        sizeB = (EditText) findViewById(R.id.mat_height);
        sizeB.setOnClickListener(this);
        sizeL.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(10, 1)});
        sizeB.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(10, 1)});
        thikness = new ArrayAdapter(ConsumerOredrActivity.this, R.layout.spinner_items_center, spthikness);
        size = new ArrayAdapter(ConsumerOredrActivity.this, R.layout.spinner_items_center_white, spsize);

        sizeL.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
           public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    length = sizeL.getText().toString().trim();
                    System.out.println("SizeType.equalsIgnoreCase(MM)" + SizeType.toString());
                    if(SizeType.equalsIgnoreCase("MM")){
                        System.out.println("INSIDE MM" + SizeType.toString());
                        convertLength(length);
                    }else{
                        System.out.println("INSIDE INCH" + SizeType.toString());
                        convertMMtoInchLength(length);
                    }

               }
           }
      });
        sizeB.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() != 0){
                    tv_totalQTY.setText("1");
                    qtyCounter = 1;
                }

            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start,int before, int count) {
                if(s.length() != 0){
                    tv_totalQTY.setText("1");
                    qtyCounter = 1;
                }
            }

        });

        sizeL.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() != 0){
                    tv_totalQTY.setText("1");
                    qtyCounter = 1;
                }

            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start,int before, int count) {
                if(s.length() != 0){
                    tv_totalQTY.setText("1");
                    qtyCounter = 1;
                }
            }

        });

        tvThickness.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() != 0){
                    tv_totalQTY.setText("1");
                    qtyCounter = 1;
                }

            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start,int before, int count) {
                if(s.length() != 0){
                    tv_totalQTY.setText("1");
                    qtyCounter = 1;
                }
            }

        });

    sizeB.setOnFocusChangeListener(new View.OnFocusChangeListener() {
          @Override
            public void onFocusChange(View v, boolean hasFocus) {
               if (!hasFocus) {
                   width = sizeB.getText().toString().trim();
                   if(SizeType.equalsIgnoreCase("MM")){
                       convertWidth(width);
                   }else{
                       convertMMtoInchBreadth(width);
                   }

                   if(SizeType.equalsIgnoreCase("MM")){
                       if (sizeB.getText().length()>0){
                           if(Double.parseDouble(sizeB.getText().toString())>1067){
                               tv_totalQTY.setText("1");
                               qtyCounter = 1;
                           }
                       }

                   }else{
                       if (sizeB.getText().length()>0){
                           if(Double.parseDouble(sizeB.getText().toString())>40){
                               tv_totalQTY.setText("1");
                               qtyCounter = 1;
                           }
                       }
                   }
               }
            }
       });

        getDealers();
       // pendingListDialog();
        getLocationData(Util.getSharedPrefrenceValue(ConsumerOredrActivity.this, Constant.Sp_DealerID));
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == BAR_CODE_RESULT) {
            if (resultCode == RESULT_OK) {
                String contents = data.getStringExtra("SCAN_RESULT");
                System.out.println("scanData" + contents);
            } else if (resultCode == RESULT_CANCELED) {
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
           case R.id.tvswitch:
                switchInchToMmViseVersa();
                break;

            case R.id.tvin:
                switchInchToMmViseVersa();
                break;

            case R.id.tvmm:
                switchInchToMmViseVersa();
                break;
            case R.id.tvSizes:
                if(autocomplete_product_name.getText().length()<=0 || product_name.equalsIgnoreCase("Select Product") ){
                    Toast.makeText(getApplicationContext(), "Product not selected", Toast.LENGTH_SHORT).show();
                }else{

                        new getProductLB().execute();
                        new getProductThik().execute();
                        statndardClick = true;
                    if(spsize.size()>0)
                    {
                        System.out.println("spsize INSIDE IF<><><><> " + spsize);

                        size = new ArrayAdapter(ConsumerOredrActivity.this, R.layout.spinner_items_center_white, spsize);
                        dialogLb();
                    }
//                    else {
//                        System.out.println("spsize INSIDE ELSE<><><><> " + spsize);
//                        Toast.makeText(getApplicationContext(), "Product size not available", Toast.LENGTH_SHORT).show();
//                    }
                }

                break;

            case R.id.tvThikness:
                if (spthikness.size() > 0) {
                    tvThickness.setFocusableInTouchMode(false);
                    tvThickness.setFocusable(false);
                    tvThickness.clearFocus();
                } else {
                    tvThickness.setFocusableInTouchMode(true);
                    tvThickness.setFocusable(true);
                }
                if (spthikness.size() > 0) {
                    if(statndardClick == true){
                        thikness = new ArrayAdapter(ConsumerOredrActivity.this, R.layout.spinner_items_center, spthikness);
                        dialogThikness();
                    }

                } else {
                    if (ProductName.length() > 0) {
                        //Toast.makeText(getApplicationContext(), "Product Thickness not available",Toast.LENGTH_SHORT).show();
                    } else {
                        //Toast.makeText(getApplicationContext(), "Product not selected",Toast.LENGTH_SHORT).show();
                    }
                }
                break;

            case R.id.ib_backicon:
                Intent goBack = new Intent(ConsumerOredrActivity.this, HomeScreen.class);
                startActivity(goBack);
                break;
            case R.id.etMobileNo:
                break;
            case R.id.autocomplete_product_name:
                dialogs_Product();
                break;
            case R.id.btnRedeemNow:
                if (isValid()) {
                    progressDialog = new ProgressDialog(ConsumerOredrActivity.this);
                    progressDialog.setMessage("Please wait....");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    buttonclick = true;
                    callMRPService();
                }
                break;
            case R.id.btnRedeemLater:
                if (isValid()) {
                    progressDialog = new ProgressDialog(ConsumerOredrActivity.this);
                    progressDialog.setMessage("Please wait....");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    buttonclick = false;
                   // callMRPService();
                    callMRPServiceLater();
                }
                break;
            case R.id.tv_additem:
                if (isValid()) {

                    if (SizeType.equalsIgnoreCase("MM")) {
                        if (Double.valueOf(sizeB.getText().toString()) > 1067) {
                            tv_totalQTY.setText("1");
                        }else{

                            if(qtyCounter ==2){

                            }else {
                                qtyCounter++;
                            }
                            tv_totalQTY.setText(Integer.toString(qtyCounter));
                        }

                    }else{
                        if (Double.valueOf(sizeB.getText().toString()) > 40) {
                            tv_totalQTY.setText("1");
                        }else{
                            if(qtyCounter ==2){

                            }else {
                                qtyCounter++;
                            }
                            tv_totalQTY.setText(Integer.toString(qtyCounter));
                        }
                    }
                }

                break;
            case R.id.tv_removeItem:
                if(qtyCounter >= 2){
                    qtyCounter--;
                    tv_totalQTY.setText(Integer.toString(qtyCounter));
                }

                break;
            case R.id.ivSearch:
                progressDialog = new ProgressDialog(ConsumerOredrActivity.this);
                progressDialog.setMessage("Please wait....");
                progressDialog.setCancelable(false);
                progressDialog.show();
                searchNumber();
                break;

        }

    }

    public void dialogLb() {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ConsumerOredrActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.product_lb_dialog, null);
        final EditText txtSearch = (EditText) dialogView.findViewById(R.id.txtSearch);
        ImageView close = (ImageView) dialogView.findViewById(R.id.closeDialog);
        Lb = (ListView) dialogView.findViewById(R.id.lbsize);
        Lb.setAdapter(size);

        Lb.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int pos = spsize.indexOf(parent.getAdapter().getItem(position).toString());
                updateMatSize(pos);
                alert.dismiss();

            }

        });

        txtSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                ConsumerOredrActivity.this.size.getFilter().filter(txtSearch.getText().toString());
            }
        });

        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(false);

        alert = dialogBuilder.create();
        alert.setCanceledOnTouchOutside(true);
        alert.setCancelable(true);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });
        alert.show();
    }

    public void dialogThikness() {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ConsumerOredrActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.product_thikness_dialog, null);
        final EditText txtSearch = (EditText) dialogView.findViewById(R.id.txtSearch);
        ImageView close = (ImageView) dialogView.findViewById(R.id.closeDialog);
        ThicknessList = (ListView) dialogView.findViewById(R.id.thikness);
        ThicknessList.setAdapter(thikness);

        ThicknessList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Thikness = parent.getAdapter().getItem(position).toString();
                tvThickness.setText(Thikness);

                //int pos=spthikness.indexOf(parent.getAdapter().getItem(position).toString());
                //Toast.makeText(getApplicationContext(), pos+"", 500).show();
                alert.dismiss();
            }

        });

        txtSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                ConsumerOredrActivity.this.thikness.getFilter().filter(txtSearch.getText().toString());
            }
        });

        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(false);

        alert = dialogBuilder.create();
        alert.setCanceledOnTouchOutside(true);
        alert.setCancelable(true);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });
        alert.show();
    }

    public void updateMatSize(int position) {
        if (SizeType.equalsIgnoreCase("MM")) {
            Lenght = lbHolders.get(position).getLength();
            Bredth = lbHolders.get(position).getBredth();
        }
        if (SizeType.equalsIgnoreCase("INCH")) {
            Lenght = lbHoldersInch.get(position).getLengthInch();
            Bredth = lbHoldersInch.get(position).getBredthInch();
        }

        sizeL.setText(Lenght);
        sizeB.setText(Bredth);
    }

    public String roundOffLengthAndWidth(double value) {
        DecimalFormat f = new DecimalFormat("0.0");
        String formattedValue = f.format(value);
        String[] array_split = formattedValue.split(Pattern.quote("."));
        Log.e("array size", "" + array_split.length);
        String _value = array_split[0];
        if (Double.parseDouble(array_split[1]) > 5 || Double.parseDouble(array_split[1]) == 5) {
            _value = String.valueOf(Double.parseDouble(_value) + 1);
        }
        return _value;
    }

    public void convertLength(String length) {
        if (length != null && length.length() > 0) {
            if (Double.parseDouble(length) < 100) {
                length = roundOffLengthAndWidth(Double.parseDouble(length) * 25.4);
                sizeL.setText(length);
            }
        }
    }

    public String convertLen(String length) {
        if (length != null && length.length() > 0) {
            if (Double.parseDouble(length) < 100) {
                length = roundOffLengthAndWidth(Double.parseDouble(length) * 25.4);
                //sizeL.setText(length);
            }
        }
        return length;
    }

    public void convertWidth(String width) {
        if (width != null && width.length() > 0) {
            if (Double.parseDouble(width) < 100) {
                width = roundOffLengthAndWidth(Double.parseDouble(width) * 25.4);
                sizeB.setText(width);
                count = 0;
                count2 = 0;
            }

        }
    }


    public void convertMMtoInchLength(String value) {
        if (value != null && value.length() > 0) {
            System.out.println("Double.parseDouble(width) ?<" + Double.parseDouble(value));
            if (Double.parseDouble(value) > 100) {
                width = roundOffLengthAndWidth(Double.parseDouble(value) / 25.4);
                sizeL.setText(width);

            }

        }
    }



    public void convertMMtoInchBreadth(String value) {
        if (value != null && value.length() > 0) {
            if (Double.parseDouble(value) > 100) {
                width = roundOffLengthAndWidth(Double.parseDouble(value) / 25.4);
                sizeB.setText(width);

            }

        }
    }

    public String convertMMtoInch(String value) {
        String retrunVal = "";
        if (value != null && value.length() > 0) {
            System.out.println("Double.parseDouble(width) ?<" + Double.parseDouble(value));
            if (Double.parseDouble(value) > 100) {
                retrunVal = roundOffLengthAndWidth(Double.parseDouble(value) / 25.4);
//                sizeL.setText(width);

            }

        }
        return retrunVal;
    }

    public void getLocationData(String p_dealer_id) {
        requset_no = "6";
        JSONObject jObject = new JSONObject();
        try {
            jObject.put("request", ApiList.API_LOCATION);
            jObject.put("p_dealer_id", p_dealer_id);
            Log.e("request6", "" + jObject.toString());
        } catch (Exception e) {
            Log.e("Exception", e.getMessage());
        }
        customAsyncTask.showDialog(ConsumerOredrActivity.this);
        new MyCustomAsyncTask(ConsumerOredrActivity.this, jObject, ApiList.URL_PRODUCTS).execute();
    }


    @Override
    public void onTaskComplete(String result) {
        try {
            if (requset_no.equals("3")) // Dealer list
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
                        flag_product = 0;
                        flag_location = 0;
                        customAsyncTask.hideDialog(ConsumerOredrActivity.this);
                        setDealerDataWhenLoginByDealer();
                        /*
                         * Calling dealer Dialog
                         */
                    } else {
                        customAsyncTask.hideDialog(ConsumerOredrActivity.this);
                        JSONObject data = jsonObjectResponse
                                .getJSONObject("data");
                        String msg = data.getString("op_message");
                        GlobalVariables.defaultOneButtonDialog(ConsumerOredrActivity.this,
                                "" + msg);
                    }
                } else {
                    Toast.makeText(ConsumerOredrActivity.this, "No Data returned",
                            Toast.LENGTH_SHORT).show();
                }
            } else if (requset_no.equals(2)) {
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
                        GlobalVariables.defaultOneButtonDialog(ConsumerOredrActivity.this,
                                "" + msg);
                    }
                } else {
                    Toast.makeText(ConsumerOredrActivity.this, "No Data returned",
                            Toast.LENGTH_SHORT).show();

                }
            } else if (requset_no.equals("4")) {
                if (result != null && result.length() > 0) {
                    jsonObjectResponse = new JSONObject(result);
                    Log.e("Response4", " " + jsonObjectResponse);
                    String status = jsonObjectResponse.getString("status");
                    if (status.equalsIgnoreCase("200")) {
                        JSONObject data = jsonObjectResponse
                                .getJSONObject("data");
                        JSONArray productArray = data.getJSONArray("product");
                        SharedPreferences.Editor editor = loacalData.edit();
                        editor.putString(SharePref.PRODUCTS,
                                productArray.toString());
                        editor.commit();
                        getProductList(productArray);
                        customAsyncTask.hideDialog(ConsumerOredrActivity.this);
                        flag_color = 0;
                        if (AppConstant.EDIT_ORDER == 1 && flag_product == 1)
                            dialogs_Product();
                    } else {
                        customAsyncTask.hideDialog(ConsumerOredrActivity.this);
                        JSONObject data = jsonObjectResponse
                                .getJSONObject("data");
                        String msg = data.getString("op_message");
                        GlobalVariables.defaultOneButtonDialog(ConsumerOredrActivity.this,
                                "" + msg);
                    }
                } else {
                    customAsyncTask.hideDialog(ConsumerOredrActivity.this);
                    Toast.makeText(ConsumerOredrActivity.this, "No Data returned",
                            Toast.LENGTH_SHORT).show();
                }
            } else if (requset_no.equals("6")) {
                if (result != null && result.length() > 0) {
                    jsonObjectResponse = new JSONObject(result);
                    Log.e("Response6", " " + jsonObjectResponse);
                    String status = jsonObjectResponse.getString("status");
                    if (status.equalsIgnoreCase("200")) {
                        JSONObject data = jsonObjectResponse.getJSONObject("data");
                        JSONArray locationArray = data.getJSONArray("location");
                        SharedPreferences.Editor editor = loacalData.edit();
                        editor.putString(SharePref.LOCATION, locationArray.toString());
                        editor.commit();
                        getLocationList(locationArray);
                        customAsyncTask.hideDialog(ConsumerOredrActivity.this);
                        flag_product = 0;
                        if (AppConstant.EDIT_ORDER == 1 && flag_location == 1)
                            // dialogs_location();
                            customAsyncTask.hideDialog(ConsumerOredrActivity.this);
                    } else {
                        customAsyncTask.hideDialog(ConsumerOredrActivity.this);
                        JSONObject data = jsonObjectResponse.getJSONObject("data");
                        String msg = data.getString("op_message");
                        GlobalVariables.defaultOneButtonDialog(ConsumerOredrActivity.this, "" + msg);
                    }
                    //Below line commented to get variable value from new login
                    //if(mPrefs.getString("op_user_type", "").equals("DEALER"))
                    if (Util.getSharedPrefrenceValue(ConsumerOredrActivity.this, Constant.Sp_User_Type).equals("DEALER")) {
                        getDealers();
                    } else {
                        if (AppConstant.EDIT_ORDER == 0 && location_name.length() > 0) {
                            autocomplete_product_name.setHint("Select Product");
                            //tv_productColorConsumer.setHint("Color");
                            if (arrayList_products.size() > 0)
                                arrayList_products.clear();
                            getProducts(mPrefs.getString("DEALER_NAME", ""), AppConstant.DEALER_ZONE);
                        }
                    }
                } else {
                    customAsyncTask.hideDialog(ConsumerOredrActivity.this);
                    Toast.makeText(ConsumerOredrActivity.this, "No Data returned",
                            Toast.LENGTH_SHORT).show();
                }
            }

        } catch (Exception e) {
            // rlayout_submit_buttons.setVisibility(View.VISIBLE);
            customAsyncTask.hideDialog(ConsumerOredrActivity.this);
            Log.e("Exception caught", " = " + e);
        } finally {
            if (mPrefs.getInt("CONNECTION_TIMEOUT", 0) == 1) {
                customAsyncTask.hideDialog(ConsumerOredrActivity.this);
                Toast.makeText(ConsumerOredrActivity.this, "Connection timeout",
                        Toast.LENGTH_LONG).show();
            }
        }

    }

    public void getLocationList(JSONArray jArray) {
        for (int i = 0; i < jArray.length(); i++) {
            try {
                JSONObject obj = jArray.getJSONObject(i);
                location_name = obj.getString("location_name");
                location_code = obj.getString("location_code");
                System.out.println("location" + location_name);
                //channel_partner_group = obj.getString("channel_partner_group");
                LocationBean locationBean = new LocationBean();
                locationBean.setLocation_code(location_code);
                locationBean.setLocation_name(location_name);
                // locationBean.setChannel_partner_group(channel_partner_group);
                arrayList_location.add(locationBean);
            } catch (Exception e) {

            }
        }
    }

    public void getDealers() {
        String delearID = Util.getSharedPrefrenceValue(ConsumerOredrActivity.this, Constant.Sp_emp_grpCode);
        if (HelperMethods.isNetworkAvailable(ConsumerOredrActivity.this)) {
            requset_no = "3";
            jsonObjectRequest = new JSONObject();
            try {
                if (Util.getSharedPrefrenceValue(ConsumerOredrActivity.this, Constant.Sp_User_Type).equals("EMPLOYEE")) {
                    jsonObjectRequest.put("old", "1");
                    jsonObjectRequest.put("op_user_role_name", Util.getSharedPrefrenceValue(ConsumerOredrActivity.this, Constant.Sp_op_user_role_name));
                    jsonObjectRequest.put("op_greatplus_user_id", Util.getSharedPrefrenceValue(ConsumerOredrActivity.this, Constant.Sp_GrtPlusUserID));
                } else {
                    jsonObjectRequest.put("request", ApiList.API_PRODUCT_DEALERS);
                    jsonObjectRequest.put("p_role_name", Util.getSharedPrefrenceValue(ConsumerOredrActivity.this, Constant.Sp_User_Type));
                    jsonObjectRequest.put("p_dealer_id", delearID);
                }
                Log.e("request3", "" + jsonObjectRequest.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            customAsyncTask.showDialog(ConsumerOredrActivity.this);
            if (Util.getSharedPrefrenceValue(ConsumerOredrActivity.this, Constant.Sp_User_Type).equals("EMPLOYEE")) {
                new MyCustomAsyncTask(ConsumerOredrActivity.this, jsonObjectRequest, Constant.WS_URL + Constant.WS_DEALER_LIST).execute();
            } else {
                new MyCustomAsyncTask(ConsumerOredrActivity.this,
                        jsonObjectRequest, ApiList.URL_PRODUCTS).execute();
            }
        } else {
            GlobalVariables.defaultOneButtonDialog(ConsumerOredrActivity.this,
                    "Network error");
        }
    }

    public void getProducts(String p_dealer_name, String p_zone) {
        if (HelperMethods.isNetworkAvailable(ConsumerOredrActivity.this)) {
            requset_no = "4";
            jsonObjectRequest = new JSONObject();
            try {
                jsonObjectRequest.put("request", ApiList.API_PRODUCTS2);
                jsonObjectRequest.put("p_dealer_name", p_dealer_name);
                jsonObjectRequest.put("p_zone", p_zone);
                if (Util.getSharedPrefrenceValue(ConsumerOredrActivity.this, Constant.Sp_User_Type).equals("DEALER")) {
                    Log.d("true", "" + mPrefs.getString("DEALER_CATEGORY", ""));
                    jsonObjectRequest.put("p_dealer_category", Util.getSharedPrefrenceValue(ConsumerOredrActivity.this, Constant.Sp_Dealer_Category));
                } else {
                    //Log.d("false", "" + dealer_category);
                    // jsonObjectRequest.put("p_dealer_category", dealer_category);
                }
                Log.e("request4", "" + jsonObjectRequest.toString());
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            customAsyncTask.showDialog(ConsumerOredrActivity.this);
            new MyCustomAsyncTask(ConsumerOredrActivity.this,
                    jsonObjectRequest, ApiList.URL_PRODUCTS).execute();

        } else {
            customAsyncTask.showDialog(ConsumerOredrActivity.this);
            GlobalVariables.defaultOneButtonDialog(ConsumerOredrActivity.this,
                    "Network error");
        }

    }

    public void getDealerList(JSONArray jArray) {
        for (int i = 0; i < jArray.length(); i++) {
            try {
                JSONObject obj = jArray.getJSONObject(i);
                String dealer_name = obj.getString("DEALER_NAME");
                String zone = obj.getString("DEALER_ZONE");
                String dealer_category = obj.getString("DEALER_CATEGORY");
                String dealer_id = obj.getString("DEALER_ID");
                String dealer_area = obj.getString("AREA");
                String dealer_city = obj.getString("CITY");
                dealer_state = obj.getString("STATE");
                DealerBean dealerBean = new DealerBean();
                dealerBean.setDealerName(dealer_name);
                dealerBean.setDealerZone(zone);
                dealerBean.setDealer_category(dealer_category);
                dealerBean.setDealerId(dealer_id);
                dealerBean.setDealer_area(dealer_area);
                dealerBean.setDealer_city(dealer_city);
                dealerBean.setDealer_state(dealer_state);
               /* if (AppConstant.EDIT_ORDER == 1 && textview_dealer_list.getText().toString().trim().equals(dealer_name))
                    AppConstant.DEALER_ZONE = zone;*/
                arrayList_dealer.add(dealerBean);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setDealerDataWhenLoginByDealer() {

        if (Util.getSharedPrefrenceValue(ConsumerOredrActivity.this, Constant.Sp_User_Type).equals("DEALER")) {
            DealerBean dealerBean = arrayList_dealer.get(0);
            dealer_name = dealerBean.getDealerName();
            dealer_id = dealerBean.getDealerId();
            zone = dealerBean.getDealerZone();
            dealer_category = dealerBean.getDealer_category();
            AppConstant.DEALER_ZONE = zone;
            GlobalVariables.DEALER_CATEGORY = dealer_category;
            SharedPreferences.Editor editor = mPrefs.edit();
            editor.putString("DEALER_NAME", dealer_name);
            editor.putString("DEALER_ID", dealer_id);
            editor.commit();
            autocomplete_product_name.setHint("Select Product");
            if (arrayList_products.size() > 0)
                arrayList_products.clear();
            getProducts(dealer_name, zone);

        }
    }

    public void getProductList(JSONArray jArray) {

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
        saveProductList(arrayList_products);
    }

    private void saveProductList(ArrayList<ProductListAddEditBean> productList) {
        SharedPreferences.Editor editor = mPrefs.edit();
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

    public void dialogs_Product() {
        final Dialog dialog = new Dialog(ConsumerOredrActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_dealer);
        dialog.setCancelable(true);
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
        if (arrayList_products.size() > 0) {
            productListAdapter = new ProductListAdapter(arrayList_products,
                    ConsumerOredrActivity.this);
        } else {
            arrayList_products = getProductListFromSharedPrefrences();
        }
        listview.setAdapter(productListAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
                HelperMethods.hideSoftKeyboard(ConsumerOredrActivity.this);
                ProductListAddEditBean dealerBean = arrayList_products.get(position);
                product_name = dealerBean.getProductName();
                color_applicable_yn = dealerBean.getColor_applicable_yn();
                count = 0;
                count2 = 0;
                tv_totalQTY.setText("1");
                sizeB.setText("");
                sizeL.setText("");
                tvThickness.setHint("--");
                statndardClick = false;
                tvThickness.setFocusableInTouchMode(true);
                tvThickness.setFocusable(true);
                if (autocomplete_product_name.getText().toString().trim().equalsIgnoreCase(product_name)) {
                    Toast.makeText(ConsumerOredrActivity.this, "Product already selected", Toast.LENGTH_SHORT).show();
                } else {
                    autocomplete_product_name.setText(product_name);
                  /*  et_length.setHint("L");
                    et_width.setHint("W");
                    et_thiknes.setHint("T");*/
                    if (arrayList_color.size() > 0)
                        arrayList_color.clear();
                    dialog.dismiss();
                }
            }
        });
        imagebutton_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        autocomplete_product_name1.addTextChangedListener(new TextWatcher() {
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
                final ArrayList<ProductListAddEditBean> selectedArrayList = getArrayList(arrayList_products, autocomplete_product_name1
                        .getText().toString().trim());
                productListAdapter = new ProductListAdapter(selectedArrayList, ConsumerOredrActivity.this);
                listview.setAdapter(productListAdapter);
                listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> arg0, View view,
                                            int position, long id) {
                        HelperMethods.hideSoftKeyboard(ConsumerOredrActivity.this);
                        ProductListAddEditBean dealerBean = selectedArrayList.get(position);
                        product_name = dealerBean.getProductName();
                        color_applicable_yn = dealerBean.getColor_applicable_yn();
                        if (autocomplete_product_name.getText().toString()
                                .trim().equalsIgnoreCase(product_name)) {
                            Toast.makeText(ConsumerOredrActivity.this,
                                    "Product already selected",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            autocomplete_product_name.setText(product_name);
                            System.out.println("Product Name" + autocomplete_product_name.getText().toString());
                            if (arrayList_color.size() > 0)
                                arrayList_color.clear();

                            dialog.dismiss();
                            if (arrayList_color.size() > 0)
                                arrayList_color.clear();
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

    public void dialogs_Color() {
        final Dialog dialog = new Dialog(ConsumerOredrActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_dealer);
        dialog.setCancelable(true);
        final EditText autocomplete_color_name = (EditText) dialog.findViewById(R.id.autocomplete_product_name);
        autocomplete_color_name.setVisibility(View.VISIBLE);
        final ListView listview = (ListView) dialog.findViewById(R.id.listview_securityquestion);
        ImageButton imagebutton_close = (ImageButton) dialog.findViewById(R.id.button_close);
        TextView textview_title = (TextView) dialog.findViewById(R.id.textview_title);
        textview_title.setText("Please select color");
        colorAdapter = new ColorAdapter(arrayList_color, ConsumerOredrActivity.this);
        listview.setAdapter(colorAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View view,
                                    int position, long id) {
                HelperMethods.hideSoftKeyboard(ConsumerOredrActivity.this);
                ColorBean colorBean = arrayList_color.get(position);
                color_name = colorBean.getColor();
                if (autocomplete_product_name.getText().toString().trim()
                        .equalsIgnoreCase(color_name)) {
                    Toast.makeText(ConsumerOredrActivity.this, "Color already selected",
                            Toast.LENGTH_SHORT).show();
                } else {
                    autocomplete_product_name.setText(color_name);
                    final_color_name = color_name;
                    dialog.dismiss();
                }
            }
        });
        imagebutton_close.setOnClickListener(new View.OnClickListener() {

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
                colorAdapter = new ColorAdapter(selectedArrayList, ConsumerOredrActivity.this);
                listview.setAdapter(colorAdapter);
                listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> arg0, View view,
                                            int position, long id) {
                        HelperMethods.hideSoftKeyboard(ConsumerOredrActivity.this);
                        ColorBean colorBean = selectedArrayList.get(position);
                        color_name = colorBean.getColor();
                        if (tv_productColorConsumer.getText().toString().trim()
                                .equalsIgnoreCase(color_name)) {
                            Toast.makeText(ConsumerOredrActivity.this,
                                    "Color already selected",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            tv_productColorConsumer.setText(color_name);
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

    public void getColor() {
        if (HelperMethods.isNetworkAvailable(ConsumerOredrActivity.this)) {
            requset_no = "2";
            jsonObjectRequest = new JSONObject();
            try {
                jsonObjectRequest.put("request", ApiList.API_PRODUCT_COLOR);
                jsonObjectRequest.put("p_product_name", tv_productNameConsumer.getText().toString());
                jsonObjectRequest.put("p_zone", AppConstant.DEALER_ZONE);
                Log.e("request2", "" + jsonObjectRequest.toString());
                customAsyncTask.showDialog(ConsumerOredrActivity.this);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            new MyCustomAsyncTask(ConsumerOredrActivity.this,
                    jsonObjectRequest, ApiList.URL_PRODUCTS).execute();
        } else {
            GlobalVariables.defaultOneButtonDialog(ConsumerOredrActivity.this,
                    "Network error");
        }
    }

    public void getColorList(JSONArray jArray) {
       /* if (arrayList_color.size() > 0) {
            arrayList_color.clear();
        }*/
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


    private void callMRPService() {
        String delearID = Util.getSharedPrefrenceValue(ConsumerOredrActivity.this, Constant.Sp_emp_grpCode);
        String UserID = Util.getSharedPrefrenceValue(ConsumerOredrActivity.this, Constant.Sp_UserID);
        Map<String, String> jsonParams = new HashMap<String, String>();
        String len, breadth;
        if(SizeType.equalsIgnoreCase("MM")){
            len =  sizeL.getText().toString();
            breadth = sizeB.getText().toString();
        }else{
            len =  convertLen(sizeL.getText().toString());
            breadth = convertLen(sizeB.getText().toString());
        }
        jsonParams.put("p_user_id", UserID);//---9219151562 UserId
        jsonParams.put("p_state", "");
        jsonParams.put("p_product_name", autocomplete_product_name.getText().toString());
        jsonParams.put("p_length", len);
        jsonParams.put("p_bredth", breadth);
        jsonParams.put("p_thick", tvThickness.getText().toString());
        jsonParams.put("p_color", "");
        jsonParams.put("p_customer_number", etMobileNo.getText().toString());
        jsonParams.put("p_quantity_nos", tv_totalQTY.getText().toString());
        System.out.println("mrpreq" + jsonParams);
        JsonObjectRequest myRequest = new JsonObjectRequest(Request.Method.POST, "https://greatplus.com/api_services/get_mrp_api.php",
                new JSONObject(jsonParams),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();
                        Log.d("MRPresult", response.toString());
                        try {
                            String status = response.getString("status");
                            if (status.equalsIgnoreCase("200")) {
                                JSONObject jsonObject = response.getJSONObject("Data");
                                String reward = jsonObject.getString("op_reward");
                             if (jsonObject.getString("op_reward").equalsIgnoreCase("0")) {
                                 String message = jsonObject.getString("op_message");
                                 if(message.length()>0){
                                     customErrorDialogNew(message);
                                 }else{
                                     message = "Unable to process pre order. Please contact support team.";
                                     customErrorDialogNew(message);
                                 }
                                   //customErrorDialogNew(jsonObject.getString("op_message"));
                               }else {
                                    String TotalAmout = jsonObject.getString("op_mrp");
                                    op_r_id=jsonObject.getString("op_r_id");
                                    Total = Integer.parseInt(jsonObject.getString("op_mrp"));
                                    Reward = Integer.parseInt(jsonObject.getString("op_reward"));
                                    giftMSG = jsonObject.getString("op_gift_message");
                                    ToBePaid = Integer.toString(Total - Reward);
                                    mrpDialog(Integer.toString(Total));
                               }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(ConsumerOredrActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };
        RequestQueue MyRequestQueue = Volley.newRequestQueue(ConsumerOredrActivity.this);
        myRequest.setRetryPolicy(new DefaultRetryPolicy(
                5*60*1000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MyRequestQueue.add(myRequest);
    }

    private void callMRPServiceLater() {
        String delearID = Util.getSharedPrefrenceValue(ConsumerOredrActivity.this, Constant.Sp_emp_grpCode);
        String UserID = Util.getSharedPrefrenceValue(ConsumerOredrActivity.this, Constant.Sp_UserID);
        Map<String, String> jsonParams = new HashMap<String, String>();
        String len, breadth;
        if(SizeType.equalsIgnoreCase("MM")){
            len =  sizeL.getText().toString();
            breadth = sizeB.getText().toString();
        }else{
            len =  convertLen(sizeL.getText().toString());
            breadth = convertLen(sizeB.getText().toString());
        }
        jsonParams.put("p_user_id", UserID);//---9219151562 UserId
        jsonParams.put("p_state", "");
        jsonParams.put("p_product_name", autocomplete_product_name.getText().toString());
        jsonParams.put("p_length", len);
        jsonParams.put("p_bredth", breadth);
        jsonParams.put("p_thick", tvThickness.getText().toString());
        jsonParams.put("p_color", "");
        jsonParams.put("p_customer_number", etMobileNo.getText().toString());
        jsonParams.put("p_quantity_nos", tv_totalQTY.getText().toString());
        System.out.println("mrpreq" + jsonParams);
        JsonObjectRequest myRequest = new JsonObjectRequest(Request.Method.POST, "https://greatplus.com/api_services/get_mrp_api.php",
                new JSONObject(jsonParams),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();
                        Log.d("MRPresult", response.toString());
                        try {
                            String status = response.getString("status");
                            if (status.equalsIgnoreCase("200")) {
                                JSONObject jsonObject = response.getJSONObject("Data");
                                if (jsonObject.getString("op_reward").equalsIgnoreCase("0")) {
                                    String messgage =  jsonObject.getString("op_message");
                                    if(messgage.length()>0){
                                        customErrorDialogNew(messgage);
                                    }else{
                                        messgage = "Unable to process pre order. Please contact support team.";
                                        customErrorDialogNew(messgage);
                                    }


                                }else {
                                    op_r_id=jsonObject.getString("op_r_id");
                                    Total = Integer.parseInt(jsonObject.getString("op_mrp"));
                                    Reward = Integer.parseInt(jsonObject.getString("op_reward"));
                                    giftMSG = jsonObject.getString("op_gift_message");
                                    ToBePaid = Integer.toString(Total - Reward);
                                    mrpDialogLater(Integer.toString(Total));
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(ConsumerOredrActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };
        RequestQueue MyRequestQueue = Volley.newRequestQueue(ConsumerOredrActivity.this);
        myRequest.setRetryPolicy(new DefaultRetryPolicy(
                5*60*1000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MyRequestQueue.add(myRequest);
    }

    private String randomNumber() {
        DateFormat dateFormat = new SimpleDateFormat("MMddyyyyHH:mm:ss");
        Random random = new Random();
        StringBuilder sb = new StringBuilder(dateFormat.format(new Date()));
        sb.append("_" + Util.getSharedPrefrenceValue(ConsumerOredrActivity.this, Constant.Sp_UserID).substring(7));
        sb.append("_" + random.nextInt(900));
        return sb.toString();
    }


    private void addItemByOrderNo(String len,String breadth){
        ArrayList<PendingGCOrderModel> list=sheelaSharedPreference.getArrayList();
        PendingGCOrderModel model=new PendingGCOrderModel();
        model.setOrderNumber(orderNumber);
        model.setPUserId(userId);
        model.setPCustomerMobile(etMobileNo.getText().toString());
        model.setPCustomerName(etConsumerName.getText().toString());
        model.setPProductDisplayName(autocomplete_product_name.getText().toString());
        model.setPLength(len);
        model.setPBredth(breadth);
        model.setPThick(tvThickness.getText().toString());
        model.setPQuantity(tv_totalQTY.getText().toString());
        model.setFlag(true);
        list.add(model);
        sheelaSharedPreference.saveArrayList(list);
    }

    private void callSubmitService(String totalAmount) {
        String delearID = Util.getSharedPrefrenceValue(ConsumerOredrActivity.this, Constant.Sp_emp_grpCode);
        String UserID = Util.getSharedPrefrenceValue(ConsumerOredrActivity.this, Constant.Sp_UserID);
        Map<String, String> jsonParams = new HashMap<String, String>();
        final String len, breadth;
        if(SizeType.equalsIgnoreCase("MM")){
            len =  sizeL.getText().toString();
            breadth = sizeB.getText().toString();
        }else{
            len =  convertLen(sizeL.getText().toString());
            breadth = convertLen(sizeB.getText().toString());
        }
        jsonParams.put("p_request_id", randomNumber());//--randomNumber()
        jsonParams.put("p_user_id", UserID);
        jsonParams.put("p_dealer_id", delearID);
        jsonParams.put("p_product_display_name", autocomplete_product_name.getText().toString());
        jsonParams.put("p_length", len);
        jsonParams.put("p_bredth", breadth);
        jsonParams.put("p_thick", tvThickness.getText().toString());
        jsonParams.put("p_customer_name", etConsumerName.getText().toString());
        jsonParams.put("p_quantity", tv_totalQTY.getText().toString());
        jsonParams.put("p_customer_mobile", etMobileNo.getText().toString());
        jsonParams.put("p_product_mrp", totalAmount);
        jsonParams.put("p_cash_reward", String.valueOf(Reward));
        jsonParams.put("p_advance_amt", "0");
        jsonParams.put("p_r_id", op_r_id);
        JsonObjectRequest myRequest = new JsonObjectRequest(Request.Method.POST, "https://greatplus.com/api_services/insert_order_api.php", new JSONObject(jsonParams),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("InsertResult", response.toString());
                        progressDialog.dismiss();
                        try {
                            String status = response.getString("status");
                            if (status.equalsIgnoreCase("200")) {
                                if (!response.optString("Order Number").isEmpty()) {
                                    orderNumber = response.optString("Order Number");
                                    customDialogRedeemNow("Order Number : " + orderNumber);
                                    addItemByOrderNo(len,breadth);
                                } else {
                                    String msg=response.optString("Message");
                                    System.out.println("orderNumber" + msg);
                                    if(msg.length()>0){
                                        customErrorDialogNew(msg);
                                    }else{
                                        msg = "Unable to process pre order. Please contact support team.";
                                        customErrorDialogNew(msg);
                                    }


                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(ConsumerOredrActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };
        RequestQueue MyRequestQueue = Volley.newRequestQueue(ConsumerOredrActivity.this);
        System.out.println("orderRequest" +myRequest);
        MyRequestQueue.add(myRequest);
    }
    private void callSubmitServiceLater(String totalAmount) {
        String delearID = Util.getSharedPrefrenceValue(ConsumerOredrActivity.this, Constant.Sp_emp_grpCode);
        String UserID = Util.getSharedPrefrenceValue(ConsumerOredrActivity.this, Constant.Sp_UserID);
        Map<String, String> jsonParams = new HashMap<String, String>();
        final String len, breadth;
        if(SizeType.equalsIgnoreCase("MM")){
            len =  sizeL.getText().toString();
            breadth = sizeB.getText().toString();
        }else{
            len =  convertLen(sizeL.getText().toString());
            breadth = convertLen(sizeB.getText().toString());
        }
        jsonParams.put("p_request_id", randomNumber());//--randomNumber()
        jsonParams.put("p_user_id", UserID);//UserID
        jsonParams.put("p_dealer_id",delearID);
        jsonParams.put("p_product_display_name", autocomplete_product_name.getText().toString());
        jsonParams.put("p_length", len);
        jsonParams.put("p_bredth", breadth);
        jsonParams.put("p_thick", tvThickness.getText().toString());
        jsonParams.put("p_customer_name", etConsumerName.getText().toString());
        jsonParams.put("p_quantity", tv_totalQTY.getText().toString());
        jsonParams.put("p_customer_mobile", etMobileNo.getText().toString());
        jsonParams.put("p_product_mrp", totalAmount);
        jsonParams.put("p_cash_reward", String.valueOf(Reward));
        jsonParams.put("p_advance_amt", "0");
        jsonParams.put("p_r_id", op_r_id);
        JsonObjectRequest myRequest = new JsonObjectRequest(Request.Method.POST, "https://greatplus.com/api_services/insert_order_api.php", new JSONObject(jsonParams),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("InsertResult", response.toString());
                        progressDialog.dismiss();
                        try {
                            String status = response.getString("status");
                            if (status.equalsIgnoreCase("200")) {
                                if (!response.optString("Order Number").isEmpty()) {
                                    orderNumber = response.optString("Order Number");
                                    customDialogRedeemNow("Order Number : " + orderNumber);
                                    etMobileNo.setText("");
                                    etConsumerName.setText("");
                                    sizeB.setText("");
                                    sizeL.setText("");
                                    tvThickness.setText("");
                                    tv_totalQTY.setText("1");
                                }
                                else {
                                    String msg=response.optString("Message");
                                    System.out.println("orderNumber" + msg);
                                    if(msg.length()>0){
                                        customErrorDialogNew(msg);
                                    }else{
                                        msg = "Unable to process pre order. Please contact support team.";
                                        customErrorDialogNew(msg);
                                    }
                                    //customErrorDialogNew(response.optString("Message"));
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(ConsumerOredrActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };
        RequestQueue MyRequestQueue = Volley.newRequestQueue(ConsumerOredrActivity.this);
        System.out.println("orderRequest" +myRequest);
        MyRequestQueue.add(myRequest);
    }

    private boolean isValid() {
         if (etMobileNo.getText().toString().isEmpty() || etMobileNo.getText().length()!=10) {
            Toast.makeText(ConsumerOredrActivity.this, "Please enter valid mobile number!", Toast.LENGTH_LONG).show();
            return false;
        }else if (etConsumerName.getText().toString().isEmpty()) {
             Toast.makeText(ConsumerOredrActivity.this, "Please enter consumer name!", Toast.LENGTH_LONG).show();
             return false;
         }
         else if (autocomplete_product_name.getText().toString().isEmpty() || autocomplete_product_name.getText().toString().equals("Select Product")) {
            Toast.makeText(ConsumerOredrActivity.this, "Please select Product!", Toast.LENGTH_LONG).show();
            return false;
        } else if (sizeL.getText().toString().isEmpty()) {
            Toast.makeText(ConsumerOredrActivity.this, "Please enter Length!", Toast.LENGTH_LONG).show();
            return false;
        } else if (sizeB.getText().toString().isEmpty()) {
            Toast.makeText(ConsumerOredrActivity.this, "Please enter Breath!", Toast.LENGTH_LONG).show();
            return false;
        }  else if(Double.parseDouble(sizeB.getText().toString())>Double.parseDouble(sizeL.getText().toString()))
         {
             Toast.makeText(ConsumerOredrActivity.this, "Breadth  value must be less than length!", Toast.LENGTH_LONG).show();
             return false;
         }
        else if (tvThickness.getText().toString().isEmpty()) {
            Toast.makeText(ConsumerOredrActivity.this, "Please enter Thickness!", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    private void searchNumber() {
        String delearID = Util.getSharedPrefrenceValue(ConsumerOredrActivity.this, Constant.Sp_emp_grpCode);
        String UserID = Util.getSharedPrefrenceValue(ConsumerOredrActivity.this, Constant.Sp_UserID);
        Map<String, String> jsonParams = new HashMap<String, String>();
        jsonParams.put("p_user_id", UserID);//---9219151562
        jsonParams.put("p_dealer_id", delearID);
        jsonParams.put("p_customer_mobile", etMobileNo.getText().toString());
        JsonObjectRequest myRequest = new JsonObjectRequest(
                Request.Method.POST,
                "https://greatplus.com/api_services/fetch_order_api.php",
                new JSONObject(jsonParams),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();
                        Log.d("searchResult", response.toString());
                        try {
                            String status = response.getString("status");
                            if (status.equalsIgnoreCase("200")) {
                                Object     object;
                                JSONArray  interventionJsonArray;
                                JSONObject interventionObject;
                                object=response.get("Data");
                                if (object instanceof JSONArray){
                                    interventionJsonArray = (JSONArray)object;
                                    String PRODUCT_DISPLAY_NAME = interventionJsonArray.getJSONObject(0).getString("PRODUCT_DISPLAY_NAME");
                                    String QUANTITY = interventionJsonArray.getJSONObject(0).getString("QUANTITY");
                                    String CASH_REWARD = interventionJsonArray.getJSONObject(0).getString("CASH_REWARD");
                                    String PRODUCT_MRP = interventionJsonArray.getJSONObject(0).getString("PRODUCT_MRP");
                                    String LENGTH = interventionJsonArray.getJSONObject(0).getString("LENGTH");
                                    String BREDTH = interventionJsonArray.getJSONObject(0).getString("BREDTH");
                                    String THICK = interventionJsonArray.getJSONObject(0).getString("THICK");
                                    String ADVANCE_AMT = interventionJsonArray.getJSONObject(0).getString("ADVANCE_AMT");
                                    String CUSTOMER_NAME = interventionJsonArray.getJSONObject(0).getString("CUSTOMER_NAME");
                                    String CUSTOMER_MOBILE = interventionJsonArray.getJSONObject(0).getString("CUSTOMER_MOBILE");
                                    etConsumerName.setText(CUSTOMER_NAME);
                                    autocomplete_product_name.setText(PRODUCT_DISPLAY_NAME);
                                    sizeL.setText(convertMMtoInch(LENGTH));
                                    sizeB.setText(convertMMtoInch(BREDTH));
                                    tvThickness.setText(THICK);
                                    tv_totalQTY.setText(QUANTITY);
                                    System.out.println("Anand Name" + CUSTOMER_NAME);
                                }
                                if (object instanceof  JSONObject) {
                                    interventionObject = (JSONObject) object;
                                    Toast.makeText(ConsumerOredrActivity.this, interventionObject.getString("Message"), Toast.LENGTH_LONG).show();
                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(ConsumerOredrActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };
        RequestQueue MyRequestQueue = Volley.newRequestQueue(ConsumerOredrActivity.this);
        MyRequestQueue.add(myRequest);

    }

    private void customErrorDialogNew(String message) {
        if (cd2 != null && cd2.isShowing()) {
            cd2.dismiss();
        }
        cd2 = new CustomDialog2(ConsumerOredrActivity.this, R.layout.consumer_order_error_massage);
        cd2.setCancelable(false);
        cd2.getWindow().setBackgroundDrawable(new ColorDrawable(TRANSPARENT));
        TextView dialog_title = (TextView) cd2.findViewById(R.id.dialog_title);
       // dialog_title.setText(message);
        TextView txt_address_value = (TextView) cd2.findViewById(R.id.tv_massageDialog);
        txt_address_value.setText(message);
        TextView btn_okDialog = (TextView) cd2.findViewById(R.id.btn_okDialog);
        TextView btn_canceDilog = (TextView) cd2.findViewById(R.id.btn_canceDilog);
        btn_okDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //getCurrentTimeFromServer();
                if (cd2 != null && cd2.isShowing())
                    cd2.dismiss();

            }
        });
        cd2.show();

    }

    private void customDialogRedeemNow(String msg) {
        if (cd != null && cd.isShowing()) {
            cd.dismiss();
        }
        cd = new CustomDialog(ConsumerOredrActivity.this, R.layout.consumer_order_confirmation_dialog);
        cd.setCancelable(false);
        cd.getWindow().setBackgroundDrawable(new ColorDrawable(TRANSPARENT));
        TextView dialog_title = (TextView) cd.findViewById(R.id.dialog_title);
        TextView txt_address_value = (TextView) cd.findViewById(R.id.tv_massageDialog);
        txt_address_value.setText(giftMSG);
        TextView tv_totalAmountOrder = (TextView) cd.findViewById(R.id.tv_totalAmountOrder);
        tv_totalAmountOrder.setText(String.valueOf(Total));
        TextView tv_totalPrizeAmount = (TextView) cd.findViewById(R.id.tv_totalPrizeAmount);
        tv_totalPrizeAmount.setText(String.valueOf(Reward));
        TextView  tv_totalPaidAmount=(TextView) cd.findViewById(R.id.tv_totalPaidAmount);
        tv_totalPaidAmount.setText(ToBePaid);
        TextView btn_okDialog = (TextView) cd.findViewById(R.id.btn_okDialog);
        TextView btn_canceDilog = (TextView) cd.findViewById(R.id.btn_canceDilog);
        btn_okDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cd != null && cd.isShowing())
                    cd.dismiss();
                if (buttonclick == true) {
                    Intent goToNext = new Intent(ConsumerOredrActivity.this, ConsumerExchangeSchemeActivity.class);
                    goToNext.putExtra("MobNumber", etMobileNo.getText().toString());
                    goToNext.putExtra("consumerName", etConsumerName.getText().toString());
                    goToNext.putExtra("OrderNumber",  orderNumber);
                    startActivity(goToNext);
                } else {
                    Intent goToNext = new Intent(ConsumerOredrActivity.this, ConsumerOredrActivity.class);
                    startActivity(goToNext);
                }


            }
        });
        cd.show();
    }
private  void mrpDialog(final String totalMRP){
    if (cd2 != null && cd2.isShowing()) {
        cd2.dismiss();
    }
    cd2 = new CustomDialog2(ConsumerOredrActivity.this, R.layout.total_mrp_dialog_for_consumer);
    cd2.setCancelable(false);
    cd2.getWindow().setBackgroundDrawable(new ColorDrawable(TRANSPARENT));
    TextView dialog_title = (TextView) cd2.findViewById(R.id.dialog_title);
    tv_totalAmountOrder = (TextView) cd2.findViewById(R.id.tv_totalAmountOrder);
    tv_totalAmountOrder.setText(totalMRP);
    TextView btn_okDialog = (TextView) cd2.findViewById(R.id.btn_okDialog);
    TextView btn_canceDilog = (TextView) cd2.findViewById(R.id.btn_canceDilog);
    btn_okDialog.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (cd2 != null && cd2.isShowing())
                cd2.dismiss();
           // callSubmitService(totalAmount, prizeAmount);
            if(isValid()) {
                callSubmitService(totalMRP);
            }
        }
    });
    btn_canceDilog.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (cd2 != null && cd2.isShowing())
                cd2.dismiss();

        }
    });
    cd2.show();

}
    private  void mrpDialogLater(final String totalMRP){
        if (cd2 != null && cd2.isShowing()) {
            cd2.dismiss();
        }
        cd2 = new CustomDialog2(ConsumerOredrActivity.this, R.layout.total_mrp_dialog_for_consumer);
        cd2.setCancelable(false);
        cd2.getWindow().setBackgroundDrawable(new ColorDrawable(TRANSPARENT));
        TextView dialog_title = (TextView) cd2.findViewById(R.id.dialog_title);
        tv_totalAmountOrder = (TextView) cd2.findViewById(R.id.tv_totalAmountOrder);
        tv_totalAmountOrder.setText(totalMRP);
        TextView btn_okDialog = (TextView) cd2.findViewById(R.id.btn_okDialog);
        TextView btn_canceDilog = (TextView) cd2.findViewById(R.id.btn_canceDilog);
        btn_okDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cd2 != null && cd2.isShowing())
                    cd2.dismiss();
                // callSubmitService(totalAmount, prizeAmount);
                if(isValid()) {
                    callSubmitServiceLater(totalMRP);
                }
            }
        });
        btn_canceDilog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cd2 != null && cd2.isShowing())
                    cd2.dismiss();

            }
        });
        cd2.show();

    }

    private void getLbANDThikness() {

        try {
            String UserID = Util.getSharedPrefrenceValue(ConsumerOredrActivity.this, Constant.Sp_UserID);
            String TempUrl = "http://125.19.46.252/ws/get_product_l_b.php?MOBILE=" + UserID + "&product_name=" + autocomplete_product_name.getText().toString().trim() + "&length_type=" + SizeType.toString().trim();
            Log.e("TEMPLB URL", TempUrl);
            LBURL = TempUrl.replace(" ", "%20");
            String TempUrl2 = "http://125.19.46.252/ws/get_product_thick.php?MOBILE=" + UserID + "&product_name=" + autocomplete_product_name.getText().toString().trim() + "&length_type=" + SizeType.toString().trim();
            Log.e("TEMPLB URL", TempUrl2);
            THIKURL = TempUrl2.replace(" ", "%20");
            new getProductLB().execute();
            new getProductThik().execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void switchInchToMmViseVersa() {
        if (InchMmFlag == 1) {
           TvMm.setVisibility(View.VISIBLE);
            TvInch.setVisibility(View.INVISIBLE);
            InchMmFlag = 2;
            SizeType = "MM";
            getLbANDThikness();
          /*  if(product_name.length()>0)
            {

            }*/
          /*  else
            {
                //Toast.makeText(getApplicationContext(), "First Select Your Product", 500).show();
            }*/

            if (FirstTimeFlag == 1) {
                try {
                    if (!sizeL.getText().toString().isEmpty()){
                        Double l = Double.parseDouble(sizeL.getText().toString()) * 25.4;
                        sizeL.setText(String.valueOf(Math.round(l)).toString());
                    }

                } catch (Exception e) {
                    sizeL.setText("");
                }

                try {

                    if (!sizeB.getText().toString().isEmpty()){
                        Double b = Double.parseDouble(sizeB.getText().toString()) * 25.4;
                        sizeB.setText(String.valueOf(Math.round(b)).toString());
                    }


                } catch (Exception e) {
                    sizeB.setText("");
                }
            }

        }
        else   {
           TvMm.setVisibility(View.INVISIBLE);
            TvInch.setVisibility(View.VISIBLE);
            InchMmFlag = 1;
            SizeType = "INCH";
            getLbANDThikness();
           /* if(product_name.length()>0)

            }
            else
            {
                //Toast.makeText(getApplicationContext(), "First Select Your Product", 500).show();
            }*/

            if (FirstTimeFlag == 1) {
                try {
                    Double l = Double.parseDouble(sizeL.getText().toString()) / 25.4;
                    //sizeL.setText(String.valueOf(Math.round(l)).toString());
                    //Log.e("Inch Value ",String.valueOf(l).toString());
                    sizeL.setText(String.format("%.1f", l));
                    //Toast.makeText(getApplicationContext(), String.valueOf(l), Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }

                try {
                    Double b = Double.parseDouble(sizeB.getText().toString()) / 25.4;
                    //sizeB.setText(String.valueOf(Math.round(b)).toString());
                    sizeB.setText(String.format("%.1f", b));
                } catch (Exception e) {
                    sizeB.setText("");
                }
            }

        }
    }

    @Override
    public void onButtonClick(PendingGCOrderModel model) {
        Intent goToNext = new Intent(ConsumerOredrActivity.this, ConsumerExchangeSchemeActivity.class);
        goToNext.putExtra("MobNumber",model.getPCustomerMobile());
        goToNext.putExtra("consumerName", model.getPCustomerName());
        goToNext.putExtra("OrderNumber",  model.getOrderNumber());
        startActivity(goToNext);
    }

    public class getProductLB extends AsyncTask<String, String, String> {
        StringBuilder stringBuilder = new StringBuilder();
        String UserID = Util.getSharedPrefrenceValue(ConsumerOredrActivity.this, Constant.Sp_UserID);
        String TempUrl = "http://125.19.46.252/ws/get_product_l_b.php?MOBILE=" + UserID + "&product_name=" + autocomplete_product_name.getText().toString().trim() + "&length_type=" + SizeType.toString().trim();
        String LBURL = TempUrl.replace(" ", "%20");
        //System.out.println("LBURL <><> " + LBURL);
        //Log.d("LBURL <><> " + LBURL);

        @Override
        protected void onPreExecute() {

            if (!progressDialog.isShowing())
                progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {

                URL url = new URL(LBURL);
                HttpURLConnection htp = (HttpURLConnection) url.openConnection();
                htp.setRequestMethod("GET");
                htp.setRequestProperty("Content-Type", "application/json");
                htp.setDoInput(true);
                htp.setDoOutput(true);
                htp.setUseCaches(false);
                htp.connect();

                InputStream inputStream = htp.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String Line;
                while ((Line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(Line);
                }
                inputStream.close();
                bufferedReader.close();
                return stringBuilder.toString();
            } catch (Exception e) {
                Log.e("Error Occured ", e.getMessage().toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                Log.e("LB Response ", s);
                JSONObject obj = new JSONObject(s);
                JSONArray arr = obj.getJSONArray("length_bredth");
                lbHolders.clear();
                lbHoldersInch.clear();
                Lenght = "";
                Bredth = "";
                spsize.clear();
               // spsize.add("Length x Breadth");
                for (int i = 0; i < arr.length(); i++) {
                    if (SizeType.equalsIgnoreCase("MM")) {
                        LBHolder lbh = new LBHolder();
                        lbh.setLength(arr.getJSONObject(i).getString("LENGTH"));
                        lbh.setBredth(arr.getJSONObject(i).getString("BREDTH"));
                        lbHolders.add(lbh);
                        spsize.add(arr.getJSONObject(i).getString("LENGTH") + " x " + arr.getJSONObject(i).getString("BREDTH"));
                    }

                    if (SizeType.equalsIgnoreCase("INCH")) {
                        LBHolderInch lbh = new LBHolderInch();
                        lbh.setLengthInch(arr.getJSONObject(i).getString("LENGTH"));
                        lbh.setBredthInch(arr.getJSONObject(i).getString("BREDTH"));
                        lbHoldersInch.add(lbh);
                        spsize.add(arr.getJSONObject(i).getString("LENGTH") + " x " + arr.getJSONObject(i).getString("BREDTH"));
                    }

                }

                //sizeL.setText("");
                //sizeB.setText("");

                if (SizeType.equalsIgnoreCase("MM")) {

                    Lenght = lbHolders.get(0).getLength();
                    Bredth = lbHolders.get(0).getBredth();
                    if (FirstTimeFlag == 0 && ProductChange == 0) {
                        sizeL.setText(Lenght);
                        sizeB.setText(Bredth);
                        FirstTimeFlag = 1;
                        ProductChange = 1;
                    }

                    sizeL.setError(null);
                    sizeB.setError(null);
                    size.notifyDataSetChanged();
                }

                if (SizeType.equalsIgnoreCase("INCH")) {

                    Lenght = lbHoldersInch.get(0).getLengthInch();
                    Bredth = lbHoldersInch.get(0).getBredthInch();
                    if (FirstTimeFlag == 0 && ProductChange == 0) {
                        sizeL.setText(Lenght);
                        sizeB.setText(Bredth);
                        FirstTimeFlag = 1;
                        ProductChange = 1;
                    }
                    sizeL.setError(null);
                    sizeB.setError(null);
                    size.notifyDataSetChanged();
                }


                if (progressDialog.isShowing())
                    progressDialog.dismiss();

            } catch (Exception e) {
                if (progressDialog.isShowing())
                    progressDialog.dismiss();

                lbHolders.clear();
                lbHoldersInch.clear();
                spsize.clear();
                Lenght = "";
                Bredth = "";
                sizeL.setText("");
                sizeB.setText("");
                sizeL.setError(null);
                sizeB.setError(null);
                size.notifyDataSetChanged();
            }
        }
    }

    public class getProductThik extends AsyncTask<String, String, String> {
        StringBuilder stringBuilder = new StringBuilder();
        String UserID = Util.getSharedPrefrenceValue(ConsumerOredrActivity.this, Constant.Sp_UserID);
        String TempUrl2 = "http://125.19.46.252/ws/get_product_thick.php?MOBILE=" + UserID + "&product_name=" + autocomplete_product_name.getText().toString().trim() + "&length_type=" + SizeType.toString().trim();
        String THIKURL = TempUrl2.replace(" ", "%20");

        @Override
        protected void onPreExecute() {

            if (!progressDialog.isShowing())
                progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                URL url = new URL(THIKURL);
                HttpURLConnection htp = (HttpURLConnection) url.openConnection();
                htp.setRequestMethod("GET");
                htp.setRequestProperty("Content-Type", "application/json");
                htp.setDoInput(true);
                htp.setDoOutput(true);
                htp.setUseCaches(false);
                htp.connect();

                InputStream inputStream = htp.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String Line;
                while ((Line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(Line);
                }
                inputStream.close();
                bufferedReader.close();
                return stringBuilder.toString();
            } catch (Exception e) {
                Log.e("Error Occured ", e.getMessage().toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                Log.e("Thikness Response ", s);
                JSONObject obj = new JSONObject(s);
                JSONArray arr = obj.getJSONArray("thick");
                thiknessHolders.clear();
                spthikness.clear();
               // spthikness.add("Select Thickness");
                Thikness = "";

                for (int i = 0; i < arr.length(); i++) {
                    ThiknessHolder th = new ThiknessHolder();
                    if (arr.getJSONObject(i).getString("THICK").equalsIgnoreCase("null")
                            || arr.getJSONObject(i).getString("THICK").equalsIgnoreCase("")) {

                    } else {
                        th.setThikness(arr.getJSONObject(i).getString("THICK"));
                        thiknessHolders.add(th);
                        spthikness.add(arr.getJSONObject(i).getString("THICK"));
                    }
                }
                Thikness = spthikness.get(0);
                tvThickness.setText(Thikness);
                thikness.notifyDataSetChanged();

                if (spthikness.size() > 0) {
                    tvThickness.setFocusableInTouchMode(false);
                    tvThickness.setFocusable(false);
                    tvThickness.clearFocus();
                } else {
                    tvThickness.setFocusableInTouchMode(true);
                    tvThickness.setFocusable(true);
                }

                if (progressDialog.isShowing())
                    progressDialog.dismiss();

            } catch (Exception e) {
                if (progressDialog.isShowing())
                    progressDialog.dismiss();
                thiknessHolders.clear();
                Thikness = "";
                tvThickness.setText(Thikness);
                spthikness.clear();
                thikness.notifyDataSetChanged();

                // Toast.makeText(MrpCalculation.this, "Thikness not found or
                // Network error occurred...", Toast.LENGTH_SHORT).show();
            }
        }

    }
    private  void pendingListDialog(){
        if (cd1 != null && cd1.isShowing()) {
            cd1.dismiss();
        }
        ArrayList<PendingGCOrderModel> list=sheelaSharedPreference.getArrayList();
        if(list!=null && !list.isEmpty()){
            cd1 = new CustomDialog(ConsumerOredrActivity.this, R.layout.pending_list_for_gc);
            cd1.setCancelable(true);
           // cd1.getWindow().setBackgroundDrawable(new ColorDrawable(TRANSPARENT));
            ListView listView= (ListView) cd1.findViewById(R.id.list_pending);
            adapter=new PendingAdapter(ConsumerOredrActivity.this,ConsumerOredrActivity.this,list);
            listView.setAdapter(adapter);
            cd1.show();
        }
    }
}

