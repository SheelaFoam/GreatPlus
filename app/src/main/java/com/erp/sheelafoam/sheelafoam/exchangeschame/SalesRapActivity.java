package com.erp.sheelafoam.sheelafoam.exchangeschame;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.erp.sheelafoam.HomeScreen;
import com.erp.sheelafoam.R;
import com.erp.sheelafoam.adapter.MTSReportAdapter;
import com.erp.sheelafoam.adapter.SalesReportAdapter;
import com.erp.sheelafoam.constant.Constant;
import com.erp.sheelafoam.model.RepresentativeName;
import com.erp.sheelafoam.models.MTSModel;
import com.erp.sheelafoam.models.SalesModel;
import com.erp.sheelafoam.prefrences.SheelaSharedPreference;
import com.erp.sheelafoam.sheelafoam.activity.MTSReportActivity;
import com.erp.sheelafoam.sheelafoam.activity.ShowroomImageActivity;
import com.erp.sheelafoam.sheelafoam.utility.GlobalVariables;
import com.erp.sheelafoam.sheelafoam.utility.HelperMethods;
import com.erp.sheelafoam.utils.CommonClass;
import com.erp.sheelafoam.utils.CustomTypefaceEditText;
import com.erp.sheelafoam.utils.URLS;
import com.erp.sheelafoam.utils.Util;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SalesRapActivity extends Activity implements View.OnClickListener{
    Context context;
    private ListView listview;
    private AutoCompleteTextView autocomplete_salesRepName;
    private CustomTypefaceEditText et_search;
    private ImageButton ib_backicon, ib_filter;
    private SalesReportAdapter adapter;
    private ProgressDialog progress;
    List<SalesModel> details;
    private TextView tv_logoname,tv_emptyText, textview_date_from,textview_date_to,tv_salesGo;
    private SheelaSharedPreference preference;
    private CommonClass commonClass;
    private String  date_from = "",date_to = "";
    int year,month,day;
    private LinearLayout linear_search;
    String UserID;

    private DatePickerDialog.OnDateSetListener datePickerListener_date_from = new DatePickerDialog.OnDateSetListener() {
        // when dialog box is closed, below method will be called.
        @SuppressLint("NewApi")
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            try {
                DecimalFormat mFormat = new DecimalFormat("00");
                mFormat.setRoundingMode(RoundingMode.DOWN);
                date_from = mFormat.format(Double.valueOf(year)) + "-"
                        + mFormat.format(Double.valueOf(monthOfYear + 1)) + "-"
                        + mFormat.format(Double.valueOf(dayOfMonth));
                date_from = GlobalVariables.getCurrentDateInDisplayFormat(SalesRapActivity.this, date_from);
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
                       SalesRapActivity.this, date_to);
                textview_date_to.setText(date_to);
            } catch (Exception e) {
                Log.d("exception", e.getMessage());
            }

        }

    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange_sales_repo_activity);
        preference = new SheelaSharedPreference(this);
        progress = new ProgressDialog(this);
        ib_backicon = (ImageButton) findViewById(R.id.ib_backicon);
        tv_logoname = (TextView) findViewById(R.id.tv_logoname);
        tv_logoname.setText("Sales Report");
        et_search = (CustomTypefaceEditText) findViewById(R.id.et_salesSearch);
        tv_salesGo= (TextView) findViewById(R.id.tv_salesGo);
        linear_search= (LinearLayout) findViewById(R.id.linear_search);
        tv_emptyText= (TextView) findViewById(R.id.tv_emptyText);
        listview = (ListView) findViewById(R.id.list_listViewSales);
        textview_date_from = (TextView) findViewById(R.id.textview_date_from);
        textview_date_to = (TextView) findViewById(R.id.textview_date_to);
        textview_date_from.setText(HelperMethods.getFifteenDaysOldDate());
        textview_date_to.setText(HelperMethods.getCurrentDate());
        autocomplete_salesRepName= (AutoCompleteTextView) findViewById(R.id.autocomplete_salesRepName);
        UserID = Util.getSharedPrefrenceValue(SalesRapActivity.this, Constant.Sp_UserID);
        try {
            callSalesRepresentativeService(UserID);
        } catch (Exception e) {
            e.printStackTrace();


        }
        ib_backicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goBack = new Intent(SalesRapActivity.this, HomeScreen.class);
                startActivity(goBack);
            }
        });
        textview_date_from.setOnClickListener(this);
        textview_date_to.setOnClickListener(this);
        tv_salesGo.setOnClickListener(this);
        getCurrentDate();
        autocomplete_salesRepName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                RepresentativeName representativeName = (RepresentativeName) adapterView.getItemAtPosition(i);
                autocomplete_salesRepName.setText(representativeName.getSALESREPNAME());
            }
        });

        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (details.size() > 0) {
                    String find = charSequence.toString();
                    ArrayList<SalesModel> temp = setSearchList(find);
                    adapter = new SalesReportAdapter(SalesRapActivity.this, temp);
                    listview.setAdapter(adapter);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

    }

    public ArrayList<SalesModel> setSearchList(String find) {
        ArrayList<SalesModel> list = new ArrayList<>();
        list.clear();
        String findWord = find.toLowerCase();
        for (SalesModel slrModel : details) {
            if (slrModel.getPRODUCTSPECIFICATION().toString().toLowerCase().contains(findWord) ||
                    slrModel.getBREDTH().toString().toLowerCase().contains(findWord) ||
                    slrModel.getLENGTH().toString().toLowerCase().contains(findWord) ||
                    slrModel.getLENGTH().toString().toLowerCase().contains(findWord) ||
                    slrModel.getCOUNT1().toString().toLowerCase().contains(findWord)) {
                list.add(slrModel);
            }
        }
        return list;
    }
    private void callSALESReportService() throws JSONException {
        com.android.volley.RequestQueue requestQueue1 = Volley.newRequestQueue(SalesRapActivity.this);
        final JSONObject jsonObject = new JSONObject();
        jsonObject.put("p_user_id", UserID);//9811676725 UserID
        jsonObject.put("p_from_date", textview_date_from.getText().toString());//01-04-2018
        jsonObject.put("p_to_date", textview_date_to.getText().toString());//05-07-2018
        jsonObject.put("p_dlr_sales_rep_name", autocomplete_salesRepName.getText().toString());//sales rap name manish
        System.out.println("SalesReport Req:-"+jsonObject);
        JsonArrayRequest _arrayReq = new JsonArrayRequest(Request.Method.POST, URLS.SALES_REPORT_URL, jsonObject,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        progress.dismiss();
                       // linear_search.setVisibility(View.VISIBLE);
                        System.out.println("SalesReport response is: " + jsonArray);
                        Gson gson = new Gson();
                        Type collectionType = new TypeToken<List<SalesModel>>() {
                        }.getType();
                        details = gson.fromJson(jsonArray.toString(), collectionType);
                        adapter = new SalesReportAdapter(SalesRapActivity.this,details);
                        listview.setAdapter(adapter);
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SalesRapActivity.this, "No Data Found", Toast.LENGTH_LONG).show();
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
        requestQueue1.add(_arrayReq);

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.textview_date_from:
                datePicker_date_from();
                break;
            case R.id.textview_date_to:
                datePicker_date_to();
                break;
            case R.id.tv_salesGo:
                try {
                   if (!commonClass.checkInternetConnection(SalesRapActivity.this)) {
                        Toast.makeText(SalesRapActivity.this, "Please check internet connection!", Toast.LENGTH_LONG).show();
                    } else {
                        progress = new ProgressDialog(SalesRapActivity.this);
                        progress.setMessage("Please wait....");
                        progress.setCancelable(false);
                        progress.show();
                        String delearID = Util.getSharedPrefrenceValue(SalesRapActivity.this, Constant.Sp_emp_grpCode);
                        System.out.println("MTS DelearID"+delearID);
                        callSALESReportService();
                       HelperMethods.hideSoftKeyboard(SalesRapActivity.this);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;
        }
    }
    private boolean isValid(){
        if (textview_date_from.getText().toString().length()==0){
            GlobalVariables.defaultOneButtonDialog(SalesRapActivity.this, "Please select FROM Date");
            return  false;
        }
        else if (textview_date_to.getText().toString().length()==0){
            GlobalVariables.defaultOneButtonDialog(SalesRapActivity.this, "Please select TO Date");
            return  false;
        }
        return true;
    }
    public void getFromAndToDate() {
        date_from = textview_date_from.getText().toString().trim();
        date_to = textview_date_to.getText().toString().trim();
    }
    public void datePicker_date_from() {
        DatePickerDialog _date = new DatePickerDialog(SalesRapActivity.this,
                datePickerListener_date_from, year, month, day);
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
        DatePickerDialog _date = new DatePickerDialog(SalesRapActivity.this,
                datePickerListener_date_to, year, month, day);
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
    }
    private void callSalesRepresentativeService(String userId)  throws JSONException {
        RequestQueue requestQueue1 = Volley.newRequestQueue(SalesRapActivity.this);
        JSONObject _req = new JSONObject();
        _req.put("p_user_id", userId);
        JsonArrayRequest _arrayReq = new JsonArrayRequest(Request.Method.POST, URLS.SALES_REP, _req,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        Log.d("Sales response is", jsonArray + "");
                        Type listType = new TypeToken<List<RepresentativeName>>() {
                        }.getType();
                        List<RepresentativeName> list = new Gson().fromJson(jsonArray.toString(), listType);
                        autocomplete_salesRepName.setAdapter(new SalesRapActivity.SalesRepoNameAdapter(SalesRapActivity.this, list));
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };
        requestQueue1.add(_arrayReq);
    }

    class SalesRepoNameAdapter extends BaseAdapter implements Filterable {
        private List<RepresentativeName> item;
        private List<RepresentativeName> nameList;
        private LayoutInflater inflater;

        public SalesRepoNameAdapter(Context context, List<RepresentativeName> nameList) {
            this.item = new ArrayList<>();
            this.nameList = nameList;
            inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return item.size();
        }

        @Override
        public RepresentativeName getItem(int i) {
            return item.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null)
                view = inflater.inflate(R.layout.item_auto_complete, viewGroup, false);
            TextView tvRepName = (TextView) view.findViewById(R.id.tvRepName);
            tvRepName.setText("" + getItem(i).getSALESREPNAME());
            return view;
        }

        @Override
        public Filter getFilter() {
            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence charSequence) {
                    FilterResults filterResults = new FilterResults();
                    synchronized (charSequence) {
                        if (charSequence != null && charSequence.length() > 1) {
                            List<RepresentativeName> list = getFilterList(charSequence.toString());
                            filterResults.count = list.size();
                            filterResults.values = list;
                        }
                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

                    if (filterResults.values != null) {
                        item = (ArrayList<RepresentativeName>) filterResults.values;
                    } else {
                        item = null;
                    }
                    if (item != null && item.size() > 0) {
                        notifyDataSetChanged();
                    } else {
                        notifyDataSetInvalidated();
                    }

                }
            };
        }

        private List<RepresentativeName> getFilterList(String s) {
            List<RepresentativeName> list = new ArrayList<>();
            for (RepresentativeName name : nameList) {
                String repName = name.getSALESREPNAME().toLowerCase().replace("\\s", "");
                if (repName.contains(s.toLowerCase().replace("\\s", ""))) {
                    list.add(name);
                }
            }
            return list;
        }

    }
}
