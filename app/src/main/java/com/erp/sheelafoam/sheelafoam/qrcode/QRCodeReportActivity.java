package com.erp.sheelafoam.sheelafoam.qrcode;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.erp.sheelafoam.R;
import com.erp.sheelafoam.sheelafoam.utility.ApiList;
import com.erp.sheelafoam.sheelafoam.utility.AsyncTaskListner;
import com.erp.sheelafoam.sheelafoam.utility.ConnectionDetector;
import com.erp.sheelafoam.sheelafoam.utility.GlobalVariables;
import com.erp.sheelafoam.sheelafoam.utility.HelperMethods;
import com.erp.sheelafoam.sheelafoam.utility.MyAsyncTask;

import org.json.JSONObject;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Calendar;

public class QRCodeReportActivity extends AppCompatActivity implements OnClickListener,AsyncTaskListner {
	
	Context mContext;
	
	SharedPreferences mPrefs;
	
	ConnectionDetector con;
	
	Activity mActivity;
	
	private int year;
	private int month;
	private int day;
	
	
	/**UI controls**/
	
	ListView listView;
	TableLayout table_layout;
	TextView textview_date_from;
	TextView textview_date_to;
	TextView textview_filter_qr;
	TextView textview_app_title;
	ImageButton btn_logout;
	
	/**String variables for hold string types**/
	String role="";
	String mobileNum="";
	String date_from="";
	String date_to="";

	Toolbar toolbar;

	TextView toolbarTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_qrcode_report);
		
		mContext=QRCodeReportActivity.this;
		con=new ConnectionDetector(this);
		mPrefs = getSharedPreferences("Location", Context.MODE_PRIVATE);
		
		mActivity = QRCodeReportActivity.this;
		 role=mPrefs.getString("op_user_type", "");
		 mobileNum = getIntent().getStringExtra("op_user_name");
		
		getUiObject();
		getCurrentDate();
		
		
		setOnClickListner();
		
		//getSppanable();
	
		if(con.isConnectingToInternet())
		{
			getReportData();
		}
		else
		{
			GlobalVariables.defaultOneButtonDialog(QRCodeReportActivity.this, GlobalVariables.CONNECTION_ERROR);
		}
		
		addReportTable();
	}
	
	/**
	 * Method: This method is used for get current date.
	 * **/
	
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
	
	
	/*
	 * ==========================================================================
	 * ========== Method for populating date Picker
	 * 
	 * ==========================================================================
	 * ===========
	 */

	public void datePicker_date_from() {

		DatePickerDialog _date = new DatePickerDialog(QRCodeReportActivity.this,
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
						QRCodeReportActivity.this, date_from);

				// delivery_date = HelperMethods.getDDmmYYYY(delivery_date);

				textview_date_from.setText(date_from);

				
			}

			catch (Exception e) {
				Log.d("exception", e.getMessage());
			}

		}

	};
	
	public void datePicker_date_to() {

		DatePickerDialog _date = new DatePickerDialog(QRCodeReportActivity.this,
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
						QRCodeReportActivity.this, date_to);

				// delivery_date = HelperMethods.getDDmmYYYY(delivery_date);

				textview_date_to.setText(date_to);

				
			}

			catch (Exception e) {
				Log.d("exception", e.getMessage());
			}

		}

	};
	
	/*
	 * ====================================================================================
	 * Method for getting referencing view from layout
	 * =====================================================================================
	 **/

	private void getUiObject() {
			// TODO Auto-generated method stub

		toolbar = (Toolbar) findViewById(R.id.app_toolbar);
		toolbarTitle = (TextView) findViewById(R.id.app_toolbar_title);
		toolbarTitle.setText("Report");
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		

		listView = (ListView)findViewById(R.id.listView1);
		  table_layout = (TableLayout)findViewById(R.id.table);
		textview_date_from=(TextView)findViewById(R.id.textview_date_from);
		textview_date_to=(TextView)findViewById(R.id.textview_date_to);
		textview_filter_qr=(TextView)findViewById(R.id.textview_filter_qr);

		textview_date_from.setText(HelperMethods.getFifteenDaysOldDate());
		textview_date_to.setText(HelperMethods.getCurrentDate());
		

		
		}
	
	public void setOnClickListner()
	{
		textview_date_from.setOnClickListener(this);
		textview_date_to.setOnClickListener(this);
		textview_filter_qr.setOnClickListener(this);

	}
	
	
	/**Json request for get QR Code reports**/
	
	
private void getReportData() {
	// TODO Auto-generated method stub
	
	try
	{
	
	//set_dealer_name.clear();
	    JSONObject jsonObjectRequest=new JSONObject();
		//jsonObjectRequest=new JSONObject();
		jsonObjectRequest.put("request", ApiList.API_GET_ALL_ORDER
			);
		jsonObjectRequest.put("p_from_date", HelperMethods.getDDMMMyyyy(HelperMethods.getFifteenDaysOldDate()));
		jsonObjectRequest.put("p_to_date", HelperMethods.getDDMMMyyyy(HelperMethods.getCurrentDate()));
		
		
		Log.e("##request##", jsonObjectRequest.toString());
		new MyAsyncTask(QRCodeReportActivity.this, jsonObjectRequest).execute();
	
	
	}
	
	catch(Exception e)
	{
		
		Log.e("error", ""+e);
		
	}
	
	
	
}


//http://www.prandroid.com/2014/05/creating-table-layout-dynamically-in.html

//public void CreateTableRow(ArrayList<ProductOrderListBean> orderListBeans) 
public void CreateTableRow(){
   
    TableRow tbrow0 = new TableRow(this);
    TextView tv0 = new TextView(this);
    tv0.setText(" Serial No. ");
    tv0.setEms(4);
    tv0.setMaxLines(2);
    tv0.setTextColor(Color.WHITE);
    tv0.setGravity(Gravity.CENTER_VERTICAL);
    tv0.setPadding(getResources().getDimensionPixelSize(R.dimen.report_header_padding), getResources().getDimensionPixelSize(R.dimen.report_header_padding), getResources().getDimensionPixelSize(R.dimen.report_header_padding), getResources().getDimensionPixelSize(R.dimen.report_header_padding));
    tv0.setBackgroundColor(getResources().getColor(R.color.header_background));
    tv0.setTextSize(getResources().getDimension(R.dimen.report_text_size));
    
   // tv0.setTypeface(null,Typeface.NORMAL);
    tbrow0.addView(tv0);
    TextView tv1 = new TextView(this);
    tv1.setText(" Opening ");
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
    TextView tv3 = new TextView(this);
    tv3.setText(" Earn ");
    tv3.setTextColor(Color.WHITE);
    tv3.setGravity(Gravity.CENTER_VERTICAL);
    tv3.setPadding(getResources().getDimensionPixelSize(R.dimen.report_header_padding), getResources().getDimensionPixelSize(R.dimen.report_header_padding), getResources().getDimensionPixelSize(R.dimen.report_header_padding), getResources().getDimensionPixelSize(R.dimen.report_header_padding));
    tv3.setBackgroundColor(getResources().getColor(R.color.header_background));
    tv3.setTextSize(getResources().getDimension(R.dimen.report_text_size));
   // tv3.setTypeface(null,Typeface.NORMAL);
   // tbrow0.setBackgroundColor(getResources().getColor(R.color.header_background));
    tbrow0.addView(tv3);
    //details
    
    TextView tv4 = new TextView(this);
    tv4.setText(" closing ");
    tv4.setTextColor(Color.WHITE);
    tv4.setGravity(Gravity.CENTER_VERTICAL);
    tv4.setPadding(getResources().getDimensionPixelSize(R.dimen.report_header_padding), getResources().getDimensionPixelSize(R.dimen.report_header_padding), getResources().getDimensionPixelSize(R.dimen.report_header_padding), 0);
    tv4.setBackgroundColor(getResources().getColor(R.color.header_background));
    tv4.setTextSize(getResources().getDimension(R.dimen.report_text_size));
   // tv4.setTypeface(null,Typeface.NORMAL);
    tbrow0.addView(tv4);
    
    TextView tv5 = new TextView(this);
    tv5.setText(" Remark ");
    tv5.setTextColor(Color.WHITE);
    tv5.setGravity(Gravity.CENTER_VERTICAL);
    tv5.setPadding(getResources().getDimensionPixelSize(R.dimen.report_header_padding), getResources().getDimensionPixelSize(R.dimen.report_header_padding), getResources().getDimensionPixelSize(R.dimen.report_header_padding), 0);
    tv5.setBackgroundColor(getResources().getColor(R.color.header_background));
    tv5.setTextSize(getResources().getDimension(R.dimen.report_text_size));
   // tv4.setTypeface(null,Typeface.NORMAL);
    tbrow0.addView(tv5);
    
    tbrow0.setBackgroundColor(getResources().getColor(R.color.header_background));
    
    TableRow.LayoutParams tableRowParams = new TableRow.LayoutParams();
    tableRowParams.setMargins(0, getResources().getDimensionPixelSize(R.dimen.report_header_padding), 0, getResources().getDimensionPixelSize(R.dimen.report_header_padding));
    tableRowParams.weight = 1;
    
    table_layout.addView(tbrow0,tableRowParams);
  /*  for (int i = 0; i < orderListBeans.size(); i++) {
    	
    	
        TableRow tbrow = new TableRow(this);
        TextView t1v = new TextView(this);
        t1v.setText(orderListBeans.get(i).getOrder_number());
        t1v.setTextColor(Color.BLACK);
        t1v.setGravity(Gravity.CENTER_VERTICAL);
        t1v.setPadding(getResources().getDimensionPixelSize(R.dimen.report_header_padding), getResources().getDimensionPixelSize(R.dimen.report_header_padding_extra), 0, getResources().getDimensionPixelSize(R.dimen.report_header_padding_extra));
        t1v.setTextSize(getResources().getDimension(R.dimen.report_text_size));
        //t1v.setTypeface(null,Typeface.NORMAL);
        tbrow.addView(t1v);
        TextView t2v = new TextView(this);
        t2v.setText(orderListBeans.get(i).getProduct_display_name());
        t2v.setEms(8);
        t2v.setMaxLines(3);
        t2v.setTextColor(Color.BLACK);
        t2v.setGravity(Gravity.CENTER_VERTICAL);
        t2v.setPadding(0, getResources().getDimensionPixelSize(R.dimen.report_header_padding_extra), 0, getResources().getDimensionPixelSize(R.dimen.report_header_padding_extra));
        t2v.setTextSize(getResources().getDimension(R.dimen.report_text_size));
        //t2v.setTypeface(null,Typeface.NORMAL);
        tbrow.addView(t2v);
//        TextView t3v = new TextView(getActivity());
//        
//        
//        t3v.setText(orderListBeans.get(i).getLength()+"x"+orderListBeans.get(i).getBreadth()+"x"+orderListBeans.get(i).getThick());
//        t3v.setTextColor(Color.BLACK);
//        t3v.setGravity(Gravity.CENTER);
//        tbrow.addView(t3v,tableRowParams);
        TextView t4v = new TextView(this);
        if(orderListBeans.get(i).getStatus().equals("0"))
        {
        	t4v.setText("Pending");
        }
        else if(orderListBeans.get(i).getStatus().equals("1"))
        {
        	t4v.setText("Approve");
        }
        else if(orderListBeans.get(i).getStatus().equals("2"))
        {
        	t4v.setText("Reject");
        }
        else
        {
        	t4v.setText("Dispatched");
        }
        
        t4v.setTextColor(Color.BLACK);
        t4v.setGravity(Gravity.CENTER_VERTICAL);
        t4v.setPadding(getResources().getDimensionPixelSize(R.dimen.report_header_padding), getResources().getDimensionPixelSize(R.dimen.report_header_padding_extra), getResources().getDimensionPixelSize(R.dimen.report_header_padding), getResources().getDimensionPixelSize(R.dimen.report_header_padding_extra));
       // t4v.setEms(3);
        t4v.setTextSize(getResources().getDimension(R.dimen.report_text_size));
       // t4v.setTypeface(null,Typeface.NORMAL);
        tbrow.addView(t4v,tableRowParams);
        
        final TextView t5v = new TextView(this);
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
				int position=t5v.getId();
				
				//Toast.makeText(getActivity(), ""+position, Toast.LENGTH_LONG).show();
				
				//createDetailDialog(position);
			}
		});
        tbrow.addView(t5v,tableRowParams);
        
        if(i%2==1)
        {
        	//tbrow.setBackgroundColor(getResources().getColor(R.color.report_background1));
        }
        else
        {
        	tbrow.setBackgroundColor(getResources().getColor(R.color.report_background2));
        }
        
        TableRow.LayoutParams tableRowParams_body = new TableRow.LayoutParams();
        tableRowParams_body.setMargins(0, 0, 0, 0);
       
        table_layout.addView(tbrow,tableRowParams_body);
    }*/

}

/**adding report table in Layout**/
public void addReportTable()
{
	table_layout.removeAllViews();
	CreateTableRow();
}

/**
 * add sppanable string to the header title
 * **/

public void getSppanable()
{
     
	if(role.equals("SAATHI"))
	{
		textview_app_title.setText("greatplus saathi app");
	}
	SpannableString spannable_string=new SpannableString(textview_app_title.getText().toString().trim());
	spannable_string.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.color_great)), 0, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
	spannable_string.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.color_plus)), 5, 10, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
	
	textview_app_title.setText(spannable_string);
}




	@Override
	public void onTaskComplete(String result) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		switch(v.getId())
		{
		  case R.id.btn_logout:
			  
			  GlobalVariables.logout(QRCodeReportActivity.this);
			  break;
			  
		  case R.id.textview_filter_qr:
			  
			  
			  break;
			  
		  case R.id.textview_date_from:
			   
		    	datePicker_date_from();
		    	
			   break;
			   
	        case R.id.textview_date_to:
			   
	        	datePicker_date_to();
	        	
			   break;
			   
		}
		
	}

}
