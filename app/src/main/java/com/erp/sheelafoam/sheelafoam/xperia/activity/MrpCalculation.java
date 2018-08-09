package com.erp.sheelafoam.sheelafoam.xperia.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.erp.sheelafoam.R;
import com.erp.sheelafoam.constant.Constant;
import com.erp.sheelafoam.sheelafoam.utility.GlobalVariables;
import com.erp.sheelafoam.sheelafoam.xperia.dataholders.LBHolder;
import com.erp.sheelafoam.sheelafoam.xperia.dataholders.LBHolderInch;
import com.erp.sheelafoam.sheelafoam.xperia.dataholders.ThiknessHolder;
import com.erp.sheelafoam.sheelafoam.xperia.dataholders.ThiknessHolderInch;
import com.erp.sheelafoam.sheelafoam.xperia.function.DecimalDigitsInputFilter;
import com.erp.sheelafoam.sheelafoam.xperia.function.XperiaFunctions;
import com.erp.sheelafoam.utils.Util;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MrpCalculation extends AppCompatActivity implements OnClickListener {


	String SizeType="INCH",CategoryPrev="ALL",CategoryCurr="";
	int FirstTimeFlag=0,ProductChange=0;

	int InchMmFlag=1;

	int ShowDialogOrNot=0;


	AlertDialog alert;
	Button show;
	TextView dispname,drpState,drpCategory, defsize1, defsize2, defsize3, totalRs, Warranty, MrpHeading,TvSwitch,TvMm,TvInch,tvSize;


	ArrayList<String> stateName = new ArrayList<String>();
	ArrayList<String> categoryName = new ArrayList<String>();
	ArrayList<String> ZoneName = new ArrayList<String>();
	ArrayList<String> dispName = new ArrayList<String>();
	ArrayList<String> spthikness = new ArrayList<String>();
	ArrayList<String> spsize = new ArrayList<String>();

	ArrayList<LBHolder> lbHolders = new ArrayList<LBHolder>();
	ArrayList<ThiknessHolder> thiknessHolders = new ArrayList<ThiknessHolder>();
	ArrayList<LBHolderInch> lbHoldersInch = new ArrayList<LBHolderInch>();
	ArrayList<ThiknessHolderInch> thiknessHoldersInch = new ArrayList<ThiknessHolderInch>();


	ArrayAdapter stateNm, dispNm,thikness,size,category;
	EditText sizeL, sizeB, sizeThikness,tvThickness;

	String Zone = "",Category="";
	String Mobile = "", Lenght = "", Bredth = "", Thikness = "", Colour = "", ProductName = "";
	ProgressDialog progressDialog;

	private SharedPreferences mPrefs;
	String mobileNum = "";

	TextView textview_back, textview_user_id, tvTitle;
	ImageButton btn_logout;
	private String user_role = "";

	String getDefaultState = "", getAllState = "", getDealerDetails = "", getAllproduct = "";
	String LBURL = "", COLURL = "", THIKURL = "", DTLURL = "",CATURL="",PRODURL="";
	String Mob="", mobile="";
	Toolbar toolbar;
	private TextView toolbarTitle;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mrp_calculations);
		TvSwitch=(TextView)findViewById(R.id.tvswitch);
		TvSwitch.setOnClickListener(this);
		TvMm=(TextView)findViewById(R.id.tvmm);
		TvMm.setVisibility(View.INVISIBLE);
		TvMm.setOnClickListener(this);
		TvInch=(TextView)findViewById(R.id.tvin);
		TvInch.setOnClickListener(this);

		tvSize=(TextView)findViewById(R.id.tvSizes);
		tvSize.setOnClickListener(this);
		tvThickness=(EditText)findViewById(R.id.tvThikness);
		tvThickness.setOnClickListener(this);


		sizeL = (EditText) findViewById(R.id.mat_width);
		sizeB = (EditText) findViewById(R.id.mat_height);

		sizeL.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(10, 1)});
		sizeB.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(10, 1)});



		initialize();

		if(XperiaFunctions.getMob(MrpCalculation.this).toString().trim().length()<=0)
		{
			Mob=XperiaFunctions.getExpMod(MrpCalculation.this);
		}
		else
		{
			Mob=XperiaFunctions.getMob(MrpCalculation.this).toString().trim();
		}
		mobile = Util.getSharedPrefrenceValue(this, Constant.Sp_User_Mobile);
		getDefaultState = "http://125.19.46.252/ws/get_default_state.php?MOBILE="+mobile;
		getAllState = "http://125.19.46.252/ws/get_all_state.php?MOBILE=" + mobile;
		getDealerDetails = "http://125.19.46.252/ws/get_dealer_v1.php?MOBILE=" + mobile + "&DEALER=DEALER&DEALER_ID=" + Util.getSharedPrefrenceValue(this, Constant.Sp_DealerID);
		getAllproduct = "http://125.19.46.252/ws/get_product.php?MOBILE=" + mobile + "&ZONE=" + Zone;

		//String Mob=XperiaFunctions.getExpMod(MrpCalculation.this);
		//Toast.makeText(getApplicationContext(), Mob, 500).show();
		new getallState().execute();
		Log.e("Dealer details", Util.getSharedPrefrenceValue(this, Constant.Sp_DealerID)+"---"+Util.getSharedPrefrenceValue(this, Constant.Sp_User_Mobile));
		// Toast.makeText(getApplicationContext(),
		// XperiaFunctions.getdealerId(MrpCalculation.this) +
		// XperiaFunctions.getMob(MrpCalculation.this), 500).show();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) // Press Back Icon
		{
			finish();
		}
		return super.onOptionsItemSelected(item);
	}

	void switchInchToMmViseVersa()
	{
		if(InchMmFlag==1)
		{
			TvMm.setVisibility(View.VISIBLE);
			TvInch.setVisibility(View.INVISIBLE);
			InchMmFlag=2;
			SizeType="MM";
			if(ProductName.length()>0)
			{
				getLbANDThikness();
			}
			else
			{
				//Toast.makeText(getApplicationContext(), "First Select Your Product", 500).show();
			}

			if(FirstTimeFlag==1)
			{
				try{
					Double l= Double.parseDouble(sizeL.getText().toString())*25.4;
					sizeL.setText(String.valueOf(Math.round(l)).toString());
				}catch(Exception e){sizeL.setText("");}

				try{
					Double b= Double.parseDouble(sizeB.getText().toString())*25.4;
					sizeB.setText(String.valueOf(Math.round(b)).toString());
				}catch(Exception e){sizeB.setText("");}
			}

		}
		else
		{
			TvMm.setVisibility(View.INVISIBLE);
			TvInch.setVisibility(View.VISIBLE);
			InchMmFlag=1;
			SizeType="INCH";
			if(ProductName.length()>0)
			{
				getLbANDThikness();
			}
			else
			{
				//Toast.makeText(getApplicationContext(), "First Select Your Product", 500).show();
			}

			if(FirstTimeFlag==1)
			{
				try{
					Double l= Double.parseDouble(sizeL.getText().toString())/25.4;
					//sizeL.setText(String.valueOf(Math.round(l)).toString());
					//Log.e("Inch Value ",String.valueOf(l).toString());
					sizeL.setText(String.format("%.1f", l));
					//Toast.makeText(getApplicationContext(), String.valueOf(l), Toast.LENGTH_LONG).show();
				}catch(Exception e){
					Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();}

				try{
					Double b= Double.parseDouble(sizeB.getText().toString())/25.4;
					//sizeB.setText(String.valueOf(Math.round(b)).toString());
					sizeB.setText(String.format("%.1f", b));
				}catch(Exception e){sizeB.setText("");}
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
				if(spsize.size()>0)
				{
					size = new ArrayAdapter(MrpCalculation.this, R.layout.spinner_items_center_white, spsize);
					dialogLb();
				}
				else
				{
					if(ProductName.length()>0)
					{
						Toast.makeText(getApplicationContext(), "Product size not available", Toast.LENGTH_SHORT).show();
					}
					else
					{
						Toast.makeText(getApplicationContext(), "Product not selected", Toast.LENGTH_SHORT).show();
					}
				}
				//Toast.makeText(getApplicationContext(), "Size",Toast.LENGTH_LONG).show();
				break;

			case R.id.tvThikness:
				if(spthikness.size()>0)
				{
					tvThickness.setFocusableInTouchMode(false);
					tvThickness.setFocusable(false);
					tvThickness.clearFocus();
				}
				else
				{
					tvThickness.setFocusableInTouchMode(true);
					tvThickness.setFocusable(true);
				}
				if(spthikness.size()>0)
				{
					thikness = new ArrayAdapter(MrpCalculation.this, R.layout.spinner_items_center, spthikness);
					dialogThikness();
				}
				else
				{
					if(ProductName.length()>0)
					{
						//Toast.makeText(getApplicationContext(), "Product Thickness not available",Toast.LENGTH_SHORT).show();
					}
					else
					{
						//Toast.makeText(getApplicationContext(), "Product not selected",Toast.LENGTH_SHORT).show();
					}
				}
				break;

			case R.id.btn_show:

				//Toast.makeText(getApplicationContext(), state.getSelectedItemPosition()+"",Toast.LENGTH_LONG).show();

				if (validate()) {
					if(InchMmFlag==1)
					{
						Double l= Double.parseDouble(sizeL.getText().toString())*25.4;
						Double b= Double.parseDouble(sizeB.getText().toString())*25.4;
						String tDurl = "http://125.19.46.252/ws/get_product_mrp.php?MOBILE=" + Mob+ "&p_state=" + drpState.getText().toString().trim() + "&p_product_name=" + ProductName.toString().trim() + "&p_length=" + String.valueOf(Math.round(l)).toString().trim() + "&p_bredth=" + String.valueOf(Math.round(b)).toString().trim() + "&p_thick=" + tvThickness.getText().toString().trim() + "&p_color=";
						System.out.println("Show1"+tDurl);
						DTLURL=tDurl.replace(" ", "%20");
					}

					else
					{
						String tDurl = "http://125.19.46.252/ws/get_product_mrp.php?MOBILE=" + Mob+ "&p_state=" + drpState.getText().toString().trim() + "&p_product_name=" + ProductName.toString().trim() + "&p_length=" + sizeL.getText().toString().trim() + "&p_bredth=" + sizeB.getText().toString().trim() + "&p_thick=" + tvThickness.getText().toString().trim() + "&p_color=";
						DTLURL=tDurl.replace(" ", "%20");
						System.out.println("Show2"+tDurl);
					}
					InputMethodManager inputManager = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
					inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
					//Toast.makeText(getApplicationContext(),InchMmFlag + "  " + DTLURL, Toast.LENGTH_LONG).show();
					new getProductMrp().execute();
				}

				break;

			case R.id.btn_logout:
				GlobalVariables.logout(this);
				break;

			case R.id.drpstate:
				if(stateName.size()>0)
				{
					stateNm = new ArrayAdapter(MrpCalculation.this, R.layout.spinner_items, stateName);
					stateNm.notifyDataSetChanged();
					dialogState();
				}
				else
				{
					new getallState().execute();
				}
				break;

			case R.id.drpproductdispname:
				if(CategoryPrev.equalsIgnoreCase(CategoryCurr))
				{

					dispNm = new ArrayAdapter(MrpCalculation.this, R.layout.spinner_items, dispName);
					dispNm.notifyDataSetChanged();
					dialog();
				}
				else
				{
					ShowDialogOrNot=0;
					String Cat="";
					if(drpCategory.getText().toString().trim().equalsIgnoreCase("ALL"))
					{
						Cat="";
					}else
					{
						Cat=drpCategory.getText().toString().trim();
					}

					String ProdUrl="http://125.19.46.252/ws/get_product_v1.php?MOBILE="+Mob+"&ZONE="+Zone+"&PRODUCT_CATEGORY="+Cat;
					PRODURL=ProdUrl.replace(" ", "%20");
					new getallproducts().execute();
				}

				break;

			case R.id.drpproductdispcategory:
				if (categoryName.size() < 1)
				{

					String CatUrl="http://125.19.46.252/ws/get_product_category_v1.php?MOBILE=" + Mob + "&ZONE=" + Zone;
					CATURL=CatUrl.replace(" ", "%20");

					new getallCAtegories().execute();
				}
				else {
					category = new ArrayAdapter(MrpCalculation.this, R.layout.spinner_items, categoryName);
					category.notifyDataSetChanged();
					dialogCategory();
				}
				break;


		}
	}


	void initialize() {
		mPrefs = getSharedPreferences("Location", Context.MODE_PRIVATE);
		toolbar = (Toolbar) findViewById(R.id.app_toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		textview_back = (TextView) findViewById(R.id.app_toolbar_title);
		textview_back.setText("Mrp Calculation");
		progressDialog = new ProgressDialog(MrpCalculation.this);
		progressDialog.setMessage("Loading....");
		MrpHeading = (TextView) findViewById(R.id.mrp_heading);
		//MrpHeading.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
		totalRs = (TextView) findViewById(R.id.total_rs);
		Warranty = (TextView) findViewById(R.id.warranty);
		show = (Button) findViewById(R.id.btn_show);
		show.setOnClickListener(this);
		dispname = (TextView) findViewById(R.id.drpproductdispname);
		dispname.setOnClickListener(this);
		drpState= (TextView) findViewById(R.id.drpstate);
		drpState.setOnClickListener(this);
		drpCategory= (TextView) findViewById(R.id.drpproductdispcategory);
		drpCategory.setOnClickListener(this);
		//stateName.add("Select");


		stateNm = new ArrayAdapter(MrpCalculation.this, R.layout.spinner_items, stateName);
		category = new ArrayAdapter(MrpCalculation.this, R.layout.spinner_items, categoryName);
		dispNm = new ArrayAdapter(MrpCalculation.this, R.layout.spinner_items, dispName);
		thikness = new ArrayAdapter(MrpCalculation.this, R.layout.spinner_items_center, spthikness);
		size = new ArrayAdapter(MrpCalculation.this, R.layout.spinner_items_center_white, spsize);

		dispname.setText("Select");
		drpState.setText("Select");
		drpCategory.setText("ALL");

		if(spthikness.size()>0)
		{
			tvThickness.setFocusableInTouchMode(false);
			tvThickness.setFocusable(false);
			tvThickness.clearFocus();
		}
		else
		{
			tvThickness.setFocusableInTouchMode(true);
			tvThickness.setFocusable(true);
		}

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
				Log.e("Response", s + "\n" + getDefaultState);
				JSONObject obj = new JSONObject(s);
				String ds = obj.getString("op_state");
				drpState.setText(ds);
				Zone=ZoneName.get(stateName.indexOf(ds)).toString().trim();
				//Toast.makeText(getApplicationContext(), ds, 500).show();
				if (progressDialog.isShowing())
					progressDialog.dismiss();
			} catch (Exception e) {
				if(stateName.size()>0){
					drpState.setText(stateName.get(0));
					Zone=ZoneName.get(0);}
				if (progressDialog.isShowing())
					progressDialog.dismiss();
				//Toast.makeText(MrpCalculation.this, "Network Error Occurred...", Toast.LENGTH_SHORT).show();
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
				stateName.clear();
				ZoneName.clear();
				JSONObject obj = new JSONObject(s);
				JSONArray arr = obj.getJSONArray("state");
				for (int i = 0; i < arr.length(); i++) {
					JSONObject o = arr.getJSONObject(i);
					stateName.add(o.getString("STATE"));
					ZoneName.add(o.getString("ZONE"));
				}
				// stateNm=new
				// ArrayAdapter(MrpCalculation.this,android.R.layout.simple_list_item_1,stateName);
				stateNm.notifyDataSetChanged();

				if (progressDialog.isShowing()) {
					progressDialog.dismiss();
				}
				new getdefaultState().execute();

			} catch (Exception e) {if (progressDialog.isShowing())
				progressDialog.dismiss();
				Toast.makeText(MrpCalculation.this, "Network Error Occurred...", Toast.LENGTH_SHORT).show();
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
				Log.e("Responsee", s);
				JSONObject obj = new JSONObject(s);
				Zone = obj.getString("DEALER_ZONE");
				if (progressDialog.isShowing()) {
					progressDialog.dismiss();
				}

				new getallproducts().execute();

			} catch (Exception e) {if (progressDialog.isShowing())
				progressDialog.dismiss();
				Toast.makeText(MrpCalculation.this, "Network Error Occurred...", Toast.LENGTH_SHORT).show();
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
				URL url = new URL(PRODURL);
				Log.e("PRODUCT URL", url.toString());
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
				dispName.clear();
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
				dispNm = new ArrayAdapter(MrpCalculation.this, R.layout.spinner_items, dispName);
				dispNm.notifyDataSetChanged();
				CategoryCurr=drpCategory.getText().toString();
				if(ShowDialogOrNot==0)
				{
					dialog();
				}
				else
				{
					dispname.setText("Select");
					ProductName="";
					sizeL.setText("");
					sizeB.setText("");
					tvThickness.setText("");
					totalRs.setText("");
					Warranty.setText("");
				}
				if (progressDialog.isShowing())
					progressDialog.dismiss();
			} catch (Exception e) {if (progressDialog.isShowing())
				progressDialog.dismiss();
				Toast.makeText(MrpCalculation.this, "Network Error Occurred...", Toast.LENGTH_SHORT).show();
			}
		}
	}


	public class getallCAtegories extends AsyncTask<String, String, String> {

		StringBuilder stringBuilder = new StringBuilder();

		@Override
		protected void onPreExecute() {
			if (!progressDialog.isShowing())
				progressDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			try {
				URL url = new URL(CATURL);
				Log.e("PRODUCT URL", url.toString());
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
				categoryName.add("ALL");
				JSONObject obj = new JSONObject(s);
				JSONArray arr = obj.getJSONArray("All_Product");
				for (int i = 0; i < arr.length(); i++) {
					JSONObject o = arr.getJSONObject(i);
					categoryName.add(o.getString("SUB_SUB_PRODUCT_SEGMENT_2016"));// SUB_PRODUCT
					// PRODUCT_SPECIFICATION
					// COLOR_APPLICABLE_YN
				}

				category = new ArrayAdapter(MrpCalculation.this, R.layout.spinner_items, categoryName);
				category.notifyDataSetChanged();
				dialogCategory();

				if (progressDialog.isShowing())
					progressDialog.dismiss();
			} catch (Exception e) {if (progressDialog.isShowing())
				progressDialog.dismiss();
				Toast.makeText(MrpCalculation.this, "Network Error Occurred...", Toast.LENGTH_SHORT).show();
			}
		}
	}


	ListView products;
	public void dialog() {
		final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MrpCalculation.this);
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
				dispname.setError(null);
				alert.dismiss();
				FirstTimeFlag=0;
				ProductChange=0;
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
				MrpCalculation.this.dispNm.getFilter().filter(txtSearch.getText().toString());
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

			String TempUrl = "http://125.19.46.252/ws/get_product_l_b.php?MOBILE="+Mob+"&product_name="+ProductName.toString().trim()+"&length_type="+SizeType.toString().trim();
			//Log.e("TEMPLB URL", TempUrl);

			LBURL=TempUrl.replace(" ","%20");

			String TempUrl2 = "http://125.19.46.252/ws/get_product_thick.php?MOBILE="+Mob+"&product_name="+ProductName.toString().trim()+"&length_type="+SizeType.toString().trim();
			//Log.e("TEMPLB URL", TempUrl2);

			THIKURL=TempUrl2.replace(" ","%20");

			//Log.e("LB URL", LBURL);
			//Log.e("THIK URL", THIKURL);
			new getProductLB().execute();
			new getProductThik().execute();
			totalRs.setText("");
			Warranty.setText("");
		} catch (Exception e) {
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
				Lenght = "";
				Bredth = "";
				spsize.clear();
				for (int i = 0; i < arr.length(); i++)
				{
					if(SizeType.equalsIgnoreCase("MM"))
					{
						LBHolder lbh = new LBHolder();
						lbh.setLength(arr.getJSONObject(i).getString("LENGTH"));
						lbh.setBredth(arr.getJSONObject(i).getString("BREDTH"));
						lbHolders.add(lbh);
						spsize.add(arr.getJSONObject(i).getString("LENGTH") + " x " + arr.getJSONObject(i).getString("BREDTH"));
					}

					if(SizeType.equalsIgnoreCase("INCH"))
					{
						LBHolderInch lbh = new LBHolderInch();
						lbh.setLengthInch(arr.getJSONObject(i).getString("LENGTH"));
						lbh.setBredthInch(arr.getJSONObject(i).getString("BREDTH"));
						lbHoldersInch.add(lbh);
						spsize.add(arr.getJSONObject(i).getString("LENGTH") + " x " + arr.getJSONObject(i).getString("BREDTH"));
					}

				}

				//sizeL.setText("");
				//sizeB.setText("");

				if(SizeType.equalsIgnoreCase("MM"))
				{

					Lenght=lbHolders.get(0).getLength();
					Bredth=lbHolders.get(0).getBredth();
					if(FirstTimeFlag==0 && ProductChange==0)
					{
						sizeL.setText(Lenght);
						sizeB.setText(Bredth);
						FirstTimeFlag=1;
						ProductChange=1;
					}

					sizeL.setError(null);
					sizeB.setError(null);
					size.notifyDataSetChanged();
				}

				if(SizeType.equalsIgnoreCase("INCH"))
				{

					Lenght=lbHoldersInch.get(0).getLengthInch();
					Bredth=lbHoldersInch.get(0).getBredthInch();
					if(FirstTimeFlag==0 && ProductChange==0)
					{
						sizeL.setText(Lenght);
						sizeB.setText(Bredth);
						FirstTimeFlag=1;
						ProductChange=1;
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


	public class getProductThik extends AsyncTask<String, String, String>
    {

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
				spthikness.clear();
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
				Thikness=spthikness.get(0);
				tvThickness.setText(Thikness);
				thikness.notifyDataSetChanged();

				if(spthikness.size()>0)
				{
					tvThickness.setFocusableInTouchMode(false);
					tvThickness.setFocusable(false);
					tvThickness.clearFocus();
				}
				else
				{
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
			//Toast.makeText(getApplicationContext(), s.toString(), Toast.LENGTH_LONG).show();
			try {
				Log.e("URL ", DTLURL);
				Log.e("Response", s);
				//Toast.makeText(getApplicationContext(), drpState.getText().toString()+ " " + Zone+ " " +ProductName+ "\n" + s, Toast.LENGTH_LONG).show();
				JSONObject obj = new JSONObject(s);
				String mrp = obj.getString("op_mrp");
				String msg = obj.getString("op_message");
				Warranty.setText(obj.getString("op_mrp_gurranty"));

				if(mrp.length()>0)
				{
					if (mrp.length() == 5) {
						totalRs.setText("\u20B9" + " " + mrp.substring(0, 2) + "," + mrp.substring(2, mrp.length()));
					} else if (mrp.length() == 4) {
						totalRs.setText("\u20B9" + " " + mrp.substring(0, 1) + "," + mrp.substring(1, mrp.length()));
					} else {
						totalRs.setText("\u20B9" + " " + mrp);
					}
				}
				else
				{
					totalRs.setText("");
				}

				if(msg.length()>1)
				{
					showMessageDialog(msg);
				}

				// Toast.makeText(MrpCalculation.this, mrp.length()+"",
				// Toast.LENGTH_SHORT).show();

				if (progressDialog.isShowing())
					progressDialog.dismiss();

			} catch (Exception e) {
				if (progressDialog.isShowing())
					progressDialog.dismiss();
				totalRs.setText("");
				Warranty.setText("");
				Toast.makeText(MrpCalculation.this, "Product Details Not Found Or Network Error Occurred...",
						Toast.LENGTH_SHORT).show();
			}
		}
	}


	void showMessageDialog(final String msg)
	{
		final Dialog dialog = new Dialog(MrpCalculation.this, R.style.MyDialog2);
		View view = LayoutInflater.from(MrpCalculation.this).inflate(R.layout.message_dialog, null);
		WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
		params.width = WindowManager.LayoutParams.MATCH_PARENT;
		params.height = WindowManager.LayoutParams.WRAP_CONTENT;
		dialog.setContentView(view);
		dialog.getWindow().setGravity(Gravity.BOTTOM);
		dialog.getWindow().setWindowAnimations(R.style.DialogAnimations_SmileWindow);
		dialog.setCancelable(true);
		TextView tvmsg=(TextView)view.findViewById(R.id.message);
		tvmsg.setText(msg);
		view.findViewById(R.id.ok).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});

		dialog.show();

	}

	public void updateMatSize(int position) {
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
			sizeL.setError("Enter\nLength");
		}else{sizeL.setError(null);}

		if(sizeB.length()<=0)
		{
			valid=false;
			sizeB.setError("Enter\nBredth");
		}else{sizeB.setError(null);}

		if(ProductName.length()<=0)
		{
			valid=false;
			dispname.setError(" ");
			//Toast.makeText(MrpCalculation.this, "Select Product Name",
			//Toast.LENGTH_SHORT).show();
		}else{dispname.setError(null);}


		if(drpState.getText().toString().length()<=0 || drpState.getText().toString().equalsIgnoreCase("Select"))
		{
			valid=false;
			drpState.setError(" ");
			//Toast.makeText(MrpCalculation.this, "Select Product Name",
			//Toast.LENGTH_SHORT).show();
		}else{drpState.setError(null);}

		return valid;
	}


	ListView Lb;
	public void dialogLb() {
		final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MrpCalculation.this);
		LayoutInflater inflater = getLayoutInflater();
		final View dialogView = inflater.inflate(R.layout.product_lb_dialog, null);
		final EditText txtSearch = (EditText) dialogView.findViewById(R.id.txtSearch);
		ImageView close = (ImageView) dialogView.findViewById(R.id.closeDialog);
		Lb = (ListView) dialogView.findViewById(R.id.lbsize);
		Lb.setAdapter(size);

		Lb.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				int pos=spsize.indexOf(parent.getAdapter().getItem(position).toString());
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
				MrpCalculation.this.size.getFilter().filter(txtSearch.getText().toString());
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


	ListView ThicknessList;
	public void dialogThikness() {
		final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MrpCalculation.this);
		LayoutInflater inflater = getLayoutInflater();
		final View dialogView = inflater.inflate(R.layout.product_thikness_dialog, null);
		final EditText txtSearch = (EditText) dialogView.findViewById(R.id.txtSearch);
		ImageView close = (ImageView) dialogView.findViewById(R.id.closeDialog);
		ThicknessList = (ListView) dialogView.findViewById(R.id.thikness);
		ThicknessList.setAdapter(thikness);

		ThicknessList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				Thikness=parent.getAdapter().getItem(position).toString();
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
				MrpCalculation.this.thikness.getFilter().filter(txtSearch.getText().toString());
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

//-----------------------------------------------------------------------------------------
	ListView AllState;
	public void dialogState() {
		final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MrpCalculation.this);
		LayoutInflater inflater = getLayoutInflater();
		final View dialogView = inflater.inflate(R.layout.product_state_dialog, null);
		final EditText txtSearch = (EditText) dialogView.findViewById(R.id.txtSearch);
		ImageView close = (ImageView) dialogView.findViewById(R.id.closeDialog);
		AllState = (ListView) dialogView.findViewById(R.id.allState);
		AllState.setAdapter(stateNm);

		AllState.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				int pos=stateName.indexOf(parent.getAdapter().getItem(position).toString());
				drpState.setText(parent.getAdapter().getItem(position).toString());
				Zone=ZoneName.get(pos);
				//Toast.makeText(getApplicationContext(), pos+" " + Zone, 500).show();

				ShowDialogOrNot=1;
				String Cat="";
				if(drpCategory.getText().toString().trim().equalsIgnoreCase("ALL"))
				{
					Cat="";
				}else
				{
					Cat=drpCategory.getText().toString().trim();
				}

				String ProdUrl="http://125.19.46.252/ws/get_product_v1.php?MOBILE="+Mob+"&ZONE="+Zone+"&PRODUCT_CATEGORY="+Cat;
				PRODURL=ProdUrl.replace(" ", "%20");
				new getallproducts().execute();


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
				MrpCalculation.this.stateNm.getFilter().filter(txtSearch.getText().toString());
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


	ListView AllCategory;
	public void dialogCategory() {
		final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MrpCalculation.this);
		LayoutInflater inflater = getLayoutInflater();
		final View dialogView = inflater.inflate(R.layout.product_category_dialog, null);
		final EditText txtSearch = (EditText) dialogView.findViewById(R.id.txtSearch);
		ImageView close = (ImageView) dialogView.findViewById(R.id.closeDialog);
		AllCategory = (ListView) dialogView.findViewById(R.id.allCategory);
		AllCategory.setAdapter(category);

		AllCategory.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				//int pos=stateName.indexOf(parent.getAdapter().getItem(position).toString());
				drpCategory.setText(parent.getAdapter().getItem(position).toString());
				CategoryPrev=parent.getAdapter().getItem(position).toString();
				ProductName="";
				dispname.setText("Select");
				sizeL.setText("");
				sizeB.setText("");
				tvThickness.setText("");
				totalRs.setText("");
				Warranty.setText("");
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
				MrpCalculation.this.category.getFilter().filter(txtSearch.getText().toString());
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



}
