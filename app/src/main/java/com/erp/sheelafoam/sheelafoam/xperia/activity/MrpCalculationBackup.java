package com.erp.sheelafoam.sheelafoam.xperia.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.erp.sheelafoam.R;
import com.erp.sheelafoam.sheelafoam.utility.GlobalVariables;
import com.erp.sheelafoam.sheelafoam.xperia.adapter.MyWheelAdapter;
import com.erp.sheelafoam.sheelafoam.xperia.adapter.SizeSpinnerAdapter;
import com.erp.sheelafoam.sheelafoam.xperia.dataholders.LBHolder;
import com.erp.sheelafoam.sheelafoam.xperia.dataholders.LBHolderInch;
import com.erp.sheelafoam.sheelafoam.xperia.dataholders.ThiknessHolder;
import com.erp.sheelafoam.sheelafoam.xperia.dataholders.ThiknessHolderInch;
import com.erp.sheelafoam.sheelafoam.xperia.function.XperiaFunctions;
import com.wx.wheelview.widget.WheelView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.lucasr.twowayview.TwoWayView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class MrpCalculationBackup extends Activity implements OnClickListener {

	WheelView wv;
	List<String> data = new ArrayList<String>();
	List<String> dataInch = new ArrayList<String>();
	String SizeType="INCH";
	RadioButton inch,mm;

	AlertDialog alert;
	Button show, more;
	TextView dispname, defsize1, defsize2, defsize3, totalRs, Warranty, MrpHeading;
	Spinner state, drpsize, drpthikness, drpcolor;

	ArrayList<String> stateName = new ArrayList<String>();
	ArrayList<String> dispName = new ArrayList<String>();

	ArrayList<LBHolder> lbHolders = new ArrayList<LBHolder>();
	ArrayList<ThiknessHolder> thiknessHolders = new ArrayList<ThiknessHolder>();
	ArrayList<LBHolderInch> lbHoldersInch = new ArrayList<LBHolderInch>();
	ArrayList<ThiknessHolderInch> thiknessHoldersInch = new ArrayList<ThiknessHolderInch>();

	TwoWayView lvSize;
	SizeSpinnerAdapter siAdapter;

	SparseBooleanArray flagSize = new SparseBooleanArray();

	ArrayAdapter stateNm, dispNm;
	EditText sizeL, sizeB, sizeThikness;

	String Zone = "";
	String Mobile = "", Lenght = "", Bredth = "", Thikness = "", Colour = "", ProductName = "";
	ProgressDialog progressDialog;

	private SharedPreferences mPrefs;
	String mobileNum = "";

	TextView textview_back, textview_user_id;
	ImageButton btn_logout;
	private String user_role = "";

	String getDefaultState = "", getAllState = "", getDealerDetails = "", getAllproduct = "";
	String LBURL = "", COLURL = "", THIKURL = "", DTLURL = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mrp_calculations);
		
        data.add("");       
        wv=(WheelView)findViewById(R.id.wheelview);
         WheelView.WheelViewStyle style = new WheelView.WheelViewStyle();
         style.selectedTextColor = Color.parseColor("#000000");
         style.textColor = Color.GRAY;
         style.textSize=15;
         style.holoBorderColor= Color.TRANSPARENT;
         style.selectedTextSize = 16;
         wv.setStyle(style);
         wv.setWheelSize(1);
         wv.setSkin(WheelView.Skin.Common);
         wv.setWheelAdapter(new MyWheelAdapter(getApplicationContext()));
         wv.setWheelData(data);
         
         
      //wv.setSelection(select);
       /*  wv.setOnWheelItemSelectedListener(new WheelView.OnWheelItemSelectedListener() {
             @Override
             public void onItemSelected(int i, Object o) {
                Toast.makeText(getApplicationContext(), i+"", Toast.LENGTH_SHORT).show();
             }
         });*/
		
		
		lvSize = (TwoWayView) findViewById(R.id.lvSize);

		sizeL = (EditText) findViewById(R.id.mat_width);
		sizeB = (EditText) findViewById(R.id.mat_height);
		initialize();

		getDefaultState = "http://125.19.46.252/ws/get_default_state.php?MOBILE="
				+ XperiaFunctions.getMob(MrpCalculationBackup.this);
		getAllState = "http://125.19.46.252/ws/get_all_state.php?MOBILE=" + XperiaFunctions.getMob(MrpCalculationBackup.this);
		getDealerDetails = "http://125.19.46.252/ws/get_dealer_v1.php?MOBILE="
				+ XperiaFunctions.getMob(MrpCalculationBackup.this) + "&DEALER=DEALER&DEALER_ID="
				+ XperiaFunctions.getdealerId(MrpCalculationBackup.this);
		getAllproduct = "http://125.19.46.252/ws/get_product.php?MOBILE=" + XperiaFunctions.getMob(MrpCalculationBackup.this)
				+ "&ZONE=" + Zone;

		new getallState().execute();
		// Log.e("Dealer Id", XperiaFunctions.getdealerId(MrpCalculation.this));
		// Toast.makeText(getApplicationContext(),
		// XperiaFunctions.getdealerId(MrpCalculation.this) +
		// XperiaFunctions.getMob(MrpCalculation.this), 500).show();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.btn_show:
				
			//Toast.makeText(getApplicationContext(), state.getSelectedItemPosition()+"",Toast.LENGTH_LONG).show();
			
			if (validate()) {
				try {
					DTLURL = "http://125.19.46.252/ws/get_product_mrp.php?MOBILE="
							+ URLEncoder.encode(XperiaFunctions.getMob(MrpCalculationBackup.this), "UTF-8") + "&p_state="
							+ URLEncoder.encode(state.getSelectedItem().toString(), "UTF-8") + "&p_product_name="
							+ URLEncoder.encode(ProductName, "UTF-8") + "&p_length="
							+ URLEncoder.encode(Lenght, "UTF-8") + "&p_bredth=" + URLEncoder.encode(Bredth, "UTF-8")
							+ "&p_thick=" + URLEncoder.encode(Thikness, "UTF-8") + "&p_color="
							+ URLEncoder.encode("", "UTF-8");
				} catch (Exception e) {
				}
				new getProductMrp().execute();
			} 

			break;

		case R.id.btn_logout:
			GlobalVariables.logout(this);
			break;

		case R.id.btn_more:
			lvSize.setVisibility(View.VISIBLE);
			break;

		case R.id.drpproductdispname:
			if (dispName.size() < 1) {
				new getdealerdetails().execute();
			} else {
				dispNm = new ArrayAdapter(MrpCalculationBackup.this, R.layout.spinner_items, dispName);
				dispNm.notifyDataSetChanged();
				dialog();
			}
			break;
		}
	}

	void initialize() {

		mPrefs = getSharedPreferences("Location", Context.MODE_PRIVATE);

		textview_back = (TextView) findViewById(R.id.textview_back);
		if (mPrefs.getString("op_user_type", null).equalsIgnoreCase("DISTRIBUTOR")) {
			textview_back.setText("greatplus distributor app");
		} else if (mPrefs.getString("op_user_type", null).equalsIgnoreCase("DEALER")) {
			textview_back.setText("greatplus dealer app");
		} else {
			textview_back.setText("greatplus employee app");
		}

		textview_user_id = (TextView) findViewById(R.id.textview_user_id);
		btn_logout = (ImageButton) findViewById(R.id.btn_logout);
		btn_logout.setOnClickListener(this);
		textview_user_id.setText(mPrefs.getString("user_id", ""));
		
		inch=(RadioButton)findViewById(R.id.rbInch);
		inch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked)
				{
					SizeType="INCH";
					
					if(ProductName.length()>0)
					{						
					getLbANDThikness();
					}
					else
					{
						Toast.makeText(getApplicationContext(), "First Select Your Product", 500).show();
					}
				}
				
			}
		});
		mm=(RadioButton)findViewById(R.id.rbMm);
		mm.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked)
				{
					SizeType="MM";
					if(ProductName.length()>0)
					{						
					getLbANDThikness();
					}
					else
					{
						Toast.makeText(getApplicationContext(), "First Select Your Product", 500).show();
					}
				}
				
			}
		});
		
		SpannableString spannable_string = new SpannableString(textview_back.getText().toString().trim());
		spannable_string.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.color_great)), 0, 5,
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		spannable_string.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.color_plus)), 5, 10,
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

		textview_back.setText(spannable_string);

		progressDialog = new ProgressDialog(MrpCalculationBackup.this);
		progressDialog.setMessage("Loading....");

		MrpHeading = (TextView) findViewById(R.id.mrp_heading);
		MrpHeading.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);

		totalRs = (TextView) findViewById(R.id.total_rs);
		Warranty = (TextView) findViewById(R.id.warranty);

		show = (Button) findViewById(R.id.btn_show);
		show.setOnClickListener(this);

		more = (Button) findViewById(R.id.btn_more);
		more.setOnClickListener(this);

		state = (Spinner) findViewById(R.id.drpstate);

		dispname = (TextView) findViewById(R.id.drpproductdispname);
		dispname.setOnClickListener(this);

		stateName.add("Select");

		stateNm = new ArrayAdapter(MrpCalculationBackup.this, R.layout.spinner_items, stateName);
		dispNm = new ArrayAdapter(MrpCalculationBackup.this, R.layout.spinner_items, dispName);

		state.setAdapter(stateNm);
		dispname.setText("Select");
	}

	public class getdefaultState extends AsyncTask<String, String, String> {

		StringBuilder stringBuilder = new StringBuilder();

		@Override
		protected void onPreExecute() {
			if (!progressDialog.isShowing())
				progressDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			try {
				URL url = new URL(getDefaultState);
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
				return stringBuilder.toString();
			} catch (Exception e) {

			}
			return null;
		}

		@Override
		protected void onPostExecute(String s) {
			try {
				Log.e("Response", s);
				JSONObject obj = new JSONObject(s);
				String ds = obj.getString("op_state");
				state.setSelection(stateName.indexOf(ds));

				if (progressDialog.isShowing())
					progressDialog.dismiss();
			} catch (Exception e) {
				Toast.makeText(MrpCalculationBackup.this, "Network Error Occurred...", Toast.LENGTH_SHORT).show();
			}
		}
	}

	public class getallState extends AsyncTask<String, String, String> {

		StringBuilder stringBuilder = new StringBuilder();

		@Override
		protected void onPreExecute() {
			if (!progressDialog.isShowing())
				progressDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			try {
				URL url = new URL(getAllState);
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
				return stringBuilder.toString();
			} catch (Exception e) {

			}
			return null;
		}

		@Override
		protected void onPostExecute(String s) {
			try {
				Log.e("Response", s);
				JSONObject obj = new JSONObject(s);
				JSONArray arr = obj.getJSONArray("state");
				for (int i = 0; i < arr.length(); i++) {
					JSONObject o = arr.getJSONObject(i);
					stateName.add(o.getString("STATE"));
				}
				// stateNm=new
				// ArrayAdapter(MrpCalculation.this,android.R.layout.simple_list_item_1,stateName);
				stateNm.notifyDataSetChanged();
				state.setAdapter(stateNm);
				if (progressDialog.isShowing()) {
					progressDialog.dismiss();
				}
				new getdefaultState().execute();

			} catch (Exception e) {
				Toast.makeText(MrpCalculationBackup.this, "Network Error Occurred...", Toast.LENGTH_SHORT).show();
			}
		}
	}

	public class getdealerdetails extends AsyncTask<String, String, String> {

		StringBuilder stringBuilder = new StringBuilder();

		@Override
		protected void onPreExecute() {
			if (!progressDialog.isShowing())
				progressDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			try {
				URL url = new URL(getDealerDetails);
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
				return stringBuilder.toString();
			} catch (Exception e) {

			}
			return null;
		}

		@Override
		protected void onPostExecute(String s) {
			try {
				Log.e("Response", s);
				JSONObject obj = new JSONObject(s);
				Zone = obj.getString("DEALER_ZONE");
				if (progressDialog.isShowing()) {
					progressDialog.dismiss();
				}

				new getallproducts().execute();

			} catch (Exception e) {
				Toast.makeText(MrpCalculationBackup.this, "Network Error Occurred...", Toast.LENGTH_SHORT).show();
			}
		}
	}

	public class getallproducts extends AsyncTask<String, String, String> {

		StringBuilder stringBuilder = new StringBuilder();

		@Override
		protected void onPreExecute() {
			if (!progressDialog.isShowing())
				progressDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			try {
				URL url = new URL("http://125.19.46.252/ws/get_product.php?MOBILE="
						+ XperiaFunctions.getMob(MrpCalculationBackup.this) + "&ZONE=" + Zone);
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
				return stringBuilder.toString();
			} catch (Exception e) {

			}
			return null;
		}

		@Override
		protected void onPostExecute(String s) {
			try {
				Log.e("Response", s);
				JSONObject obj = new JSONObject(s);
				JSONArray arr = obj.getJSONArray("All_Product");
				for (int i = 0; i < arr.length(); i++) {
					JSONObject o = arr.getJSONObject(i);
					dispName.add(o.getString("PRODUCT_DISPLAY_NAME"));// SUB_PRODUCT
																		// PRODUCT_SPECIFICATION
																		// COLOR_APPLICABLE_YN
				}
				// dispNm=new
				// ArrayAdapter(MrpCalculation.this,android.R.layout.simple_list_item_1,dispName);
				dispNm.notifyDataSetChanged();
				dialog();

				if (progressDialog.isShowing())
					progressDialog.dismiss();
			} catch (Exception e) {
				Toast.makeText(MrpCalculationBackup.this, "Network Error Occurred...", Toast.LENGTH_SHORT).show();
			}
		}
	}

	ListView products;

	public void dialog() {
		final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MrpCalculationBackup.this);
		LayoutInflater inflater = getLayoutInflater();
		final View dialogView = inflater.inflate(R.layout.product_displayname_dialog, null);
		final EditText txtSearch = (EditText) dialogView.findViewById(R.id.txtSearch);
		ImageView close = (ImageView) dialogView.findViewById(R.id.closeDialog);
		products = (ListView) dialogView.findViewById(R.id.products);
		products.setAdapter(dispNm);

		products.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				ProductName = parent.getAdapter().getItem(position).toString();
				dispname.setText(parent.getAdapter().getItem(position).toString());				
				
				alert.dismiss();
				
                getLbANDThikness();

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
				MrpCalculationBackup.this.dispNm.getFilter().filter(txtSearch.getText().toString());
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

	
	void getLbANDThikness()
	{
	
		try {
			LBURL = "http://125.19.46.252/ws/get_product_l_b.php?MOBILE="
					+ URLEncoder.encode(XperiaFunctions.getMob(MrpCalculationBackup.this), "UTF-8") + "&product_name="
					+ URLEncoder.encode(ProductName, "UTF-8") + "&length_type="
					+ URLEncoder.encode(SizeType, "UTF-8");
			THIKURL = "http://125.19.46.252/ws/get_product_thick.php?MOBILE="
					+ URLEncoder.encode(XperiaFunctions.getMob(MrpCalculationBackup.this), "UTF-8") + "&product_name="
					+ URLEncoder.encode(ProductName, "UTF-8") + "&length_type="
							+ URLEncoder.encode(SizeType, "UTF-8");
			//Toast.makeText(getApplicationContext(), LBURL + "\n\n" + THIKURL, 500).show();
			new getProductLB().execute();
			new getProductThik().execute();
			totalRs.setText("");
			Warranty.setText("");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	public class getProductLB extends AsyncTask<String, String, String> {

		StringBuilder stringBuilder = new StringBuilder();

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
				flagSize.clear();
				Lenght = "";
				Bredth = "";
				// LBHolder lbhs=new LBHolder();
				// lbhs.setLength("Select");
				// lbhs.setBredth("");
				// lbHolders.add(lbhs);
				for (int i = 0; i < arr.length(); i++) 
				{
					if(SizeType.equalsIgnoreCase("MM"))
					{
					LBHolder lbh = new LBHolder();
					lbh.setLength(arr.getJSONObject(i).getString("LENGTH"));
					lbh.setBredth(arr.getJSONObject(i).getString("BREDTH"));
					lbHolders.add(lbh);
					}
					
					if(SizeType.equalsIgnoreCase("INCH"))
					{
					LBHolderInch lbh = new LBHolderInch();
					lbh.setLengthInch(arr.getJSONObject(i).getString("LENGTH"));
					lbh.setBredthInch(arr.getJSONObject(i).getString("BREDTH"));
					lbHoldersInch.add(lbh);
					}
					
				}

				 sizeL.setText("");
	             sizeB.setText("");
				
				if(SizeType.equalsIgnoreCase("MM"))
				{
				for (int i = 0; i < lbHolders.size(); i++) {
					flagSize.append(i, false);
				}
				flagSize.put(0, true);
				Lenght=lbHolders.get(0).getLength();
				Bredth=lbHolders.get(0).getBredth();
				sizeL.setText(Lenght);
				sizeB.setText(Bredth);
				sizeL.setError(null);
				sizeB.setError(null);
				siAdapter = new SizeSpinnerAdapter(MrpCalculationBackup.this, R.layout.drp_size_layout, lbHolders, flagSize,SizeType);
				
				}
				
				if(SizeType.equalsIgnoreCase("INCH"))
				{
				for (int i = 0; i < lbHoldersInch.size(); i++) {
					flagSize.append(i, false);
				}
				flagSize.put(0, true);
				Lenght=lbHoldersInch.get(0).getLengthInch();
				Bredth=lbHoldersInch.get(0).getBredthInch();
				sizeL.setText(Lenght);
				sizeB.setText(Bredth);
				sizeL.setError(null);
				sizeB.setError(null);
				siAdapter = new SizeSpinnerAdapter(MrpCalculationBackup.this, R.layout.drp_size_layout, lbHoldersInch, flagSize,SizeType);
				}
				
				lvSize.setAdapter(siAdapter);
                lvSize.setVisibility(View.INVISIBLE);
               
				if (progressDialog.isShowing())
					progressDialog.dismiss();

			} catch (Exception e) {
				if (progressDialog.isShowing())
					progressDialog.dismiss();

				lbHolders.clear();
				lbHoldersInch.clear();
				flagSize.clear();
				Lenght = "";
				Bredth = "";
				siAdapter = new SizeSpinnerAdapter(MrpCalculationBackup.this, R.layout.drp_size_layout, lbHolders, flagSize,SizeType);
				lvSize.setAdapter(siAdapter);
				//lvSize.setVisibility(View.INVISIBLE);
				// Toast.makeText(MrpCalculation.this, "Size not found Or
				// Network error occurred...", Toast.LENGTH_SHORT).show();
			}
		}
	}

	public class getProductThik extends AsyncTask<String, String, String> {

		StringBuilder stringBuilder = new StringBuilder();

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
				data.clear();
				Thikness = "";
				// ThiknessHolder ths=new ThiknessHolder();
				// ths.setThikness("Select");
				// thiknessHolders.add(ths);

				for (int i = 0; i < arr.length(); i++) {
					ThiknessHolder th = new ThiknessHolder();
					if (arr.getJSONObject(i).getString("THICK").equalsIgnoreCase("null")
							|| arr.getJSONObject(i).getString("THICK").equalsIgnoreCase("")) {
						data.add("");
					} else {
						th.setThikness(arr.getJSONObject(i).getString("THICK"));
						thiknessHolders.add(th);
						data.add(arr.getJSONObject(i).getString("THICK"));
					}
				}
				wv = (WheelView)findViewById(R.id.wheelview);
				WheelView.WheelViewStyle style = new WheelView.WheelViewStyle();
				style.selectedTextColor = Color.parseColor("#000000");
				style.textColor = Color.GRAY;
				style.textSize = 15;
				style.holoBorderColor = Color.TRANSPARENT;
				style.selectedTextSize = 16;
				wv.setStyle(style);
				wv.setWheelSize(1);
				wv.setSkin(WheelView.Skin.Holo);

				wv.setWheelAdapter(new MyWheelAdapter(getApplicationContext()));
				wv.setWheelData(data);
				try{
				wv.setSelection(0);Thikness=data.get(0).toString();}catch(Exception e){}
				wv.setOnWheelItemSelectedListener(new WheelView.OnWheelItemSelectedListener() {
					@Override
					public void onItemSelected(int i, Object o) {
						Thikness=data.get(i).toString();
					}
				});
				if (progressDialog.isShowing())
					progressDialog.dismiss();

			} catch (Exception e) {
				if (progressDialog.isShowing())
					progressDialog.dismiss();
				thiknessHolders.clear();
				Thikness = "";
				data.clear();
				data.add("");
				wv.setWheelAdapter(new MyWheelAdapter(getApplicationContext()));
				wv.setWheelData(data);
				// Toast.makeText(MrpCalculation.this, "Thikness not found or
				// Network error occurred...", Toast.LENGTH_SHORT).show();
			}
		}
	}

	public class getProductMrp extends AsyncTask<String, String, String> {

		StringBuilder stringBuilder = new StringBuilder();

		@Override
		protected void onPreExecute() {

			if (!progressDialog.isShowing())
				progressDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			try {
				URL url = new URL(DTLURL);
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
				Log.e("URL ", DTLURL);
				Log.e("Response", s);
				JSONObject obj = new JSONObject(s);
				String mrp = obj.getString("op_mrp");
				Warranty.setText(obj.getString("op_mrp_gurranty"));

				if (mrp.length() == 5) {
					totalRs.setText("\u20B9" + " " + mrp.substring(0, 2) + "," + mrp.substring(2, mrp.length()));
				} else if (mrp.length() == 4) {
					totalRs.setText("\u20B9" + " " + mrp.substring(0, 1) + "," + mrp.substring(1, mrp.length()));
				} else {
					totalRs.setText("\u20B9" + " " + mrp);
				}
				// Toast.makeText(MrpCalculation.this, mrp.length()+"",
				// Toast.LENGTH_SHORT).show();

				if (progressDialog.isShowing())
					progressDialog.dismiss();
			} catch (Exception e) {
				if (progressDialog.isShowing())
					progressDialog.dismiss();
				Toast.makeText(MrpCalculationBackup.this, "Product Details Not Found Or Network Error Occurred...",
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	public void updateMatSize(int position) {
		for (int i = 0; i < flagSize.size(); i++) {
			flagSize.put(i, false);
		}

		flagSize.put(position, true);
		siAdapter.notifyDataSetChanged();
		lvSize.setAdapter(siAdapter);
		lvSize.setSelection(position);
		if(SizeType.equalsIgnoreCase("MM"))
		{
			Lenght = lbHolders.get(position).getLength();
			Bredth = lbHolders.get(position).getBredth();
		}
		if(SizeType.equalsIgnoreCase("INCH"))
		{
			Lenght = lbHoldersInch.get(position).getLengthInch();
			Bredth = lbHoldersInch.get(position).getBredthInch();
		}
		
		sizeL.setText(Lenght);		
		sizeB.setText(Bredth);
	}

	boolean validate()
	{
		boolean valid=true;
		
		if(sizeL.length()<=0)
		{
			valid=false;
			sizeL.setError("Empty");
		}else{sizeL.setError(null);}
		
		if(sizeB.length()<=0)
		{
			valid=false;
			sizeB.setError("Empty");
		}else{sizeB.setError(null);}
		
		if(Thikness.equalsIgnoreCase("0") || Thikness.equalsIgnoreCase(""))
		{
			valid=false;
			Toast.makeText(MrpCalculationBackup.this, "Select Valid Product Thikness",
					Toast.LENGTH_SHORT).show();
		}
		
		if(state.getSelectedItemPosition()<=0)
		{
			valid=false;
			Toast.makeText(MrpCalculationBackup.this, "Select State",
					Toast.LENGTH_SHORT).show();
		}
		
		if(ProductName.length()<=0)
		{
			valid=false;
			Toast.makeText(MrpCalculationBackup.this, "Select Product Name",
					Toast.LENGTH_SHORT).show();
		}
		
		return valid;
	}
	
}
