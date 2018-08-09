package com.erp.sheelafoam.sheelafoam.qrcode;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.erp.sheelafoam.R;
import com.journeyapps.barcodescanner.CaptureActivity;
import com.erp.sheelafoam.constant.Constant;
import com.erp.sheelafoam.sheelafoam.activity.SupportActivity;
import com.erp.sheelafoam.sheelafoam.adapter.DealerAdapter;
import com.erp.sheelafoam.sheelafoam.entry.DealerBean;
import com.erp.sheelafoam.sheelafoam.utility.AsyncTaskListner;
import com.erp.sheelafoam.sheelafoam.utility.ConnectionDetector;
import com.erp.sheelafoam.sheelafoam.utility.GlobalVariables;
import com.erp.sheelafoam.sheelafoam.utility.HelperMethods;
import com.erp.sheelafoam.sheelafoam.utility.MyAsyncTask;
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

public class ScanQrCodeActivity extends AppCompatActivity implements OnClickListener, AsyncTaskListner {

	// http://stackoverflow.com/questions/2050263/using-zxing-to-create-an-android-barcode-scanning-app
	// http://stackoverflow.com/questions/4782543/integration-zxing-library-directly-into-my-android-application/30572168#30572168
	// ArrayList for store Dealer object.
	ArrayList<DealerBean> arrayList_dealer = new ArrayList<DealerBean>();
	RelativeLayout rlayout_bottom, rlayout_saathi_name, rlayout_scanned_qrcode;
	LinearLayout llayout_tbl_header, llayout_tbl_body, rlayout_qrcode;

	TextView textview_scan_qrcode, textview_close_point, textview_app_title, textview_go, textview_saathi_name;
	TextView textview_scaned_qrcode, textview_opening_point, textview_earned_point, textview_report,
			textview_dealer_name;
	EditText edittext_qrcode, edittext_dealer_code;
	ImageButton btn_logout, btn_info;

	// String variables for hold values.

	String scaned_qrcode = ""; // it hold scaned result which called serial no.

	String role = "",MobForPoint="";

	String openingPoints = "", closingPoints = "", earnedPoints = "", msg = "", mobileNum = "", dealer_name = "",
			purchased_dealer_name = "", final_dealer_name = "", dealer_id = "", final_dealer_id = "";

	// variable of sharedPrefrence type.
	private SharedPreferences mPrefs, sharedPreferences;
	Context mContext;

	// json object for request and response.

	JSONObject json_request;
	JSONObject json_response;
	ConnectionDetector con;
	// Dealer Adapter type variable.

	DealerAdapter dealerAdapter;

    Toolbar toolbar;

    TextView toolbarTitle;

	String userId = "";

	private static final int RequestPermissionCode = 1001;

	// String obj for hold data.

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scan_qr_code);
		mPrefs = getSharedPreferences("Location", Context.MODE_PRIVATE);
		con = new ConnectionDetector(ScanQrCodeActivity.this);
		mContext = ScanQrCodeActivity.this;

		initialiseUiControls();
		addOnclickListner();

		role = mPrefs.getString("op_user_type", "");
		mobileNum = getIntent().getStringExtra("op_user_name");
		
//		if(XperiaFunctions.getMob(ScanQrCodeActivity.this).toString().trim().length()<=0)
//		{
//			MobForPoint=XperiaFunctions.getExpMod(ScanQrCodeActivity.this);
//		}
//		else
//		{
//			MobForPoint=XperiaFunctions.getMob(ScanQrCodeActivity.this).toString().trim();
//		}
		
		MobForPoint=XperiaFunctions.getExpMod(ScanQrCodeActivity.this);
		userId = Util.getSharedPrefrenceValue(ScanQrCodeActivity.this, Constant.Sp_GrtPlusUserID);

		setScannedQrcodeOnUI(mobileNum, scaned_qrcode);
		displayScannedQrCode();
		if (arrayList_dealer.size() > 0) {
			arrayList_dealer.clear();
		}
		if (con.isConnectingToInternet()) {
			if (Util.getSharedPrefrenceValue(ScanQrCodeActivity.this, Constant.CHECK_ACTIVITY_STATUS).equalsIgnoreCase("1")) {
				jsonRequestForGetPointsFromSerevr();
			}

			//new GetComplaint().execute();
		} else {
			GlobalVariables.defaultOneButtonDialog(ScanQrCodeActivity.this, GlobalVariables.CONNECTION_ERROR);
		}
		setInput();

		//Commented for getting user new name from new API
		//textview_saathi_name.setText(mPrefs.getString("op_user_name", ""));
		textview_saathi_name.setText(Util.getSharedPrefrenceValue(ScanQrCodeActivity.this, Constant.Op_user_name));
	}

	/**
	 * Method: initialise UI controls
	 **/

	public void initialiseUiControls() {
//		textview_app_title = (TextView) findViewById(R.id.textview_back);
//		btn_logout = (ImageButton) findViewById(R.id.btn_logout);
//		btn_info = (ImageButton) findViewById(R.id.btn_info);

        toolbar = (Toolbar) findViewById(R.id.app_toolbar);
        setSupportActionBar(toolbar);
        toolbarTitle = (TextView) findViewById(R.id.app_toolbar_title);
        toolbarTitle.setText("Home");
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);



		textview_scan_qrcode = (TextView) findViewById(R.id.textview_scan_qrcode);
		edittext_qrcode = (EditText) findViewById(R.id.edittext_qrcode);
		textview_go = (TextView) findViewById(R.id.textview_go);
		textview_saathi_name = (TextView) findViewById(R.id.textview_saathi_name);
		textview_scaned_qrcode = (TextView) findViewById(R.id.textview_scaned_qrcode);
		textview_opening_point = (TextView) findViewById(R.id.textview_opening_point);
		textview_earned_point = (TextView) findViewById(R.id.textview_earned_point);
		textview_report = (TextView) findViewById(R.id.textview_report);
		textview_dealer_name = (TextView) findViewById(R.id.textview_dealer_name);
		edittext_dealer_code = (EditText) findViewById(R.id.edittext_dealer_code);

		textview_close_point = (TextView) findViewById(R.id.textview_close_point);
		rlayout_qrcode = (LinearLayout) findViewById(R.id.rlayout_qrcode);

		rlayout_saathi_name = (RelativeLayout) findViewById(R.id.rlayout_saathi_name);
		rlayout_scanned_qrcode = (RelativeLayout) findViewById(R.id.rlayout_scanned_qrcode);
		llayout_tbl_header = (LinearLayout) findViewById(R.id.llayout_tbl_header);
		llayout_tbl_body = (LinearLayout) findViewById(R.id.llayout_tbl_body);

		//getSppanable();
		underLineText();

	}

	/**
	 * Method: add onClickListner
	 **/

	public void addOnclickListner() {
		textview_scan_qrcode.setOnClickListener(this);
		textview_go.setOnClickListener(this);
		textview_report.setOnClickListener(this);
		textview_dealer_name.setOnClickListener(this);

	}

	/**
	 * Method: Method for call scan intent.
	 **/

	public void callScanIntent() {
		Util.setSharedPrefrenceValue(ScanQrCodeActivity.this, Constant.PREFS_NAME, Constant.CHECK_ACTIVITY_STATUS, "2");
		// Intent intent = new Intent("com.google.zxing.client.android.SCAN");
		// intent.setPackage("com.google.zxing.client.android");
		Intent intent = new Intent(ScanQrCodeActivity.this, CaptureActivity.class);
		// intent.setPackage("com.google.zxing.client.android");
		intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
		startActivityForResult(intent, 0);
	}

	public void onActivityResult(int requestCode, int resultCode, Intent intent) {

		if (requestCode == 0) {
			if (resultCode == RESULT_OK) {
				scaned_qrcode = intent.getStringExtra("SCAN_RESULT");
				setScannedQrcodeOnUI(mobileNum, scaned_qrcode);
				// String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
				//Toast.makeText(this, scaned_qrcode, Toast.LENGTH_LONG).show();
				rlayout_qrcode.setVisibility(View.VISIBLE);

				// json request for get points from server.

				// jsonRequestForGetPointsFromSerevr();
				jsonRequestForGetPointsFromSerevrWithDealerCode();

			}
			// Handle successful scan
		} else if (resultCode == RESULT_CANCELED) {
			// Handle cancel
		}
	}

	/**
	 * Method: This method is used for set input data.
	 **/

	public void setInput() {
		openingPoints = mPrefs.getString("op_opening", "");
		earnedPoints = mPrefs.getString("op_earned", "");
		closingPoints = mPrefs.getString("op_closing", "");
		scaned_qrcode = mPrefs.getString("SCANED_QRCODE", "");

		if (openingPoints.length() > 0 || earnedPoints.length() > 0 || closingPoints.length() > 0) {
			displayScannedQrCode();
			displayPointsTable();
			textview_opening_point.setText(openingPoints);
			textview_earned_point.setText(earnedPoints);
			textview_close_point.setText(closingPoints);
			textview_saathi_name.setText(mobileNum);
			textview_scaned_qrcode.setText(scaned_qrcode);
		}

		else {
			hideScannedQrCode();
			hidePointsTable();
		}
	}

	/**
	 * Method:Method for get Input values.
	 * 
	 **/

	public void getInput() {
		scaned_qrcode = edittext_qrcode.getText().toString().trim();
		purchased_dealer_name = edittext_dealer_code.getText().toString().trim();
	}

	/**
	 * Method: Method for get points from server.
	 **/

	private void jsonRequestForGetPointsFromSerevr() {
		// TODO Auto-generated method stub
		try {
			json_request = new JSONObject();
			json_request.put("request", "getSaathiPoints");
			json_request.put("saathi_user_id", userId);
			json_request.put("p_serial_number", "");
			json_request.put("p_dealer_code", "");

			Log.e("On Load POST For Points", json_request.toString());
			new MyAsyncTask(ScanQrCodeActivity.this, json_request).execute();
		} catch (Exception e) {

		}

	}

	private void jsonRequestForGetPointsFromSerevrWithDealerCode() {
		purchased_dealer_name = edittext_dealer_code.getText().toString().trim();
		// TODO Auto-generated method stub

		try {
			json_request = new JSONObject();
			json_request.put("request", "getSaathiPoints");
			//json_request.put("saathi_user_id", mobileNum);//old
			json_request.put("saathi_user_id", userId);//xperia

			json_request.put("p_serial_number", scaned_qrcode);
			json_request.put("p_dealer_code", purchased_dealer_name);

			Log.e("Points With DealerCode", json_request.toString());
			new MyAsyncTask(ScanQrCodeActivity.this, json_request).execute();
		} catch (Exception e) {

		}

	}

	/**
	 * callback method for handle click event.
	 **/
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {

		case R.id.textview_scan_qrcode:
			EnableRuntimePermissionToAccessCamera();
			GlobalVariables.hideSoftKeyboard(ScanQrCodeActivity.this);
			purchased_dealer_name = edittext_dealer_code.getText().toString().trim();
			if (purchased_dealer_name.length() > 0) {
				callScanIntent();
			} else {
				// hideScannedQrCode();
				GlobalVariables.defaultOneButtonDialog(ScanQrCodeActivity.this, GlobalVariables.BLANK_DEALER_CODE);
			}

			break;

		case R.id.textview_go:
			GlobalVariables.hideSoftKeyboard(ScanQrCodeActivity.this);
			getInput();
			if (scaned_qrcode.length() > 0 && purchased_dealer_name.length() > 0) {
				setScannedQrcodeOnUI(mobileNum, scaned_qrcode);
				displayScannedQrCode();
				if (con.isConnectingToInternet()) {
					jsonRequestForGetPointsFromSerevrWithDealerCode();
				} else {
					GlobalVariables.defaultOneButtonDialog(ScanQrCodeActivity.this, GlobalVariables.CONNECTION_ERROR);
				}
			} else {
				if (purchased_dealer_name.length() == 0) {
					// hideScannedQrCode();
					GlobalVariables.defaultOneButtonDialog(ScanQrCodeActivity.this, GlobalVariables.BLANK_DEALER_CODE);
				} else if (scaned_qrcode.length() == 0) {
					// hideScannedQrCode();
					GlobalVariables.defaultOneButtonDialog(ScanQrCodeActivity.this,
							GlobalVariables.BLANK_SERIAL_NO_ERROR);
				}

			}

			break;

		case R.id.btn_logout:

			GlobalVariables.logout(ScanQrCodeActivity.this);

			break;

		case R.id.textview_report:
			Util.setSharedPrefrenceValue(ScanQrCodeActivity.this, Constant.PREFS_NAME, Constant.CHECK_ACTIVITY_STATUS, "1");
			Intent i = new Intent(ScanQrCodeActivity.this, QRCodeReportActivity.class);
			startActivity(i);

			break;

		case R.id.textview_dealer_name:

			if (arrayList_dealer != null && arrayList_dealer.size() > 1)
				dialog_dealer();

			break;
		case R.id.btn_info:

			Intent intent_support = new Intent(ScanQrCodeActivity.this, SupportActivity.class);
			startActivity(intent_support);

			break;
		}

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		// super.onBackPressed();
		//showDialogExitFromApp();
		//startActivity(new Intent(ScanQrCodeActivity.this, HomeScreen.class));
		Util.setSharedPrefrenceValue(ScanQrCodeActivity.this, Constant.PREFS_NAME, Constant.CHECK_ACTIVITY_STATUS, "1");
		finish();
	}

	// callback method for track click event.
	@Override
	public void onTaskComplete(String result) {
		// TODO Auto-generated method stub

		if (result != null && result.length() > 0) {
			Log.e("Opening Points Response", result);
			try {
				json_response = new JSONObject(result);
				String status = json_response.getString("status");
				if (status.equals("200")) {
					JSONObject jObj = json_response.getJSONObject("data");

					openingPoints = jObj.getString("op_opening");
					earnedPoints = jObj.getString("op_earned");
					closingPoints = jObj.getString("op_closing");
					msg = jObj.getString("op_message");
					JSONObject jObj_dealer = jObj.getJSONObject("Dealer");
					JSONArray jArray_dealer = jObj_dealer.getJSONArray("Dealer");
					for (int i = 0; i < jArray_dealer.length(); i++) {
						JSONObject obj = jArray_dealer.getJSONObject(i);
						String dealerId = obj.getString("dealerCode");
						String dealerName = obj.getString("dealerName");
						DealerBean entry = new DealerBean();
						entry.setDealerId(dealerId);
						entry.setDealerName(dealerName);
						arrayList_dealer.add(entry);
					}

					textview_close_point.setText(closingPoints);
					textview_earned_point.setText(earnedPoints);
					textview_opening_point.setText(openingPoints);

					/**
					 * setting value of dealer to the textview if dealer list
					 * size is one.If size is more than one than open dialog.
					 **/
					if (arrayList_dealer != null && arrayList_dealer.size() == 1) {
						dealer_name = arrayList_dealer.get(0).getDealerName();
						dealer_id = arrayList_dealer.get(0).getDealerId();
						textview_dealer_name.setText(dealer_name);
						// GlobalVariables.DEALER_NAME=dealer_name;
						// GlobalVariables.DEALER_ID=dealer_id;

						Editor editor = mPrefs.edit();

						editor.putString("DEALER_NAME", dealer_name);
						editor.putString("DEALER_ID", dealer_id);

						editor.commit();
					}

					savePointsInSharedPrefrences();

					displayScannedQrCode();
					displayPointsTable();
					if (!msg.equals("null")) {
						GlobalVariables.defaultOneButtonDialog(ScanQrCodeActivity.this, msg);
					}
				} else {
					hideScannedQrCode();
					hidePointsTable();
					// Toast.makeText(getApplicationContext(), "No records
					// found", Toast.LENGTH_LONG).show();
					JSONObject jObj = json_response.getJSONObject("data");
					String op_message = jObj.getString("op_message");

					GlobalVariables.defaultOneButtonDialog(ScanQrCodeActivity.this, op_message);
				}
			}

			catch (Exception e) {

			}

		}

	}

	private void savePointsInSharedPrefrences() {
		// TODO Auto-generated method stub

		Editor editor = mPrefs.edit();

		editor.putString("op_opening", openingPoints);
		editor.putString("op_earned", earnedPoints);
		editor.putString("op_closing", closingPoints);
		editor.putString("SCANED_QRCODE", scaned_qrcode);

		// editor.putBoolean("isLogin", true);

		// editor.putString("op_user_mobile", user_name);

		editor.commit();

	}

	/**
	 * Method: This medoth use to create alert dialog for exit from app when
	 * user press back button.
	 **/

	public void showDialogExitFromApp() {

		AlertDialog.Builder builder = new AlertDialog.Builder(ScanQrCodeActivity.this);

		builder.setTitle("SheelaFoam");

		builder.setMessage("Are you sure want to exit from app");
		builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub

				finish();
				dialog.dismiss();
			}
		});

		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub

				dialog.dismiss();
			}
		});
		builder.show();
	}

	/**
	 * Method: This method is used to create sppanable header app title.
	 **/

	public void getSppanable() {

		if (role.equals("SAATHI")) {
			textview_app_title.setText("greatplus saathi app");
		}
		SpannableString spannable_string = new SpannableString(textview_app_title.getText().toString().trim());
		spannable_string.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.color_great)), 0, 5,
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		spannable_string.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.color_plus)), 5, 10,
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

		textview_app_title.setText(spannable_string);
	}

	/**
	 * Method: This method is used to underline a text
	 **/
	public void underLineText() {
		String str = textview_report.getText().toString();
		SpannableString content = new SpannableString(str);
		content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
		textview_report.setText(content);
	}

	/**
	 * Method: This method is used for set scanned QR code(Serial number) for
	 * display on UI.
	 * 
	 **/

	public void setScannedQrcodeOnUI(String saathiName, String serialNumber) {
		textview_saathi_name.setText(saathiName);
		textview_scaned_qrcode.setText(serialNumber);
	}

	public void displayScannedQrCode() {
		rlayout_saathi_name.setVisibility(View.VISIBLE);
		rlayout_scanned_qrcode.setVisibility(View.VISIBLE);
	}

	public void hideScannedQrCode() {
		rlayout_saathi_name.setVisibility(View.GONE);
		rlayout_scanned_qrcode.setVisibility(View.GONE);
	}

	public void displayPointsTable() {
		llayout_tbl_header.setVisibility(View.VISIBLE);
		llayout_tbl_body.setVisibility(View.VISIBLE);
	}

	public void hidePointsTable() {
		llayout_tbl_header.setVisibility(View.GONE);
		llayout_tbl_body.setVisibility(View.GONE);
	}

	/**
	 * Method: This method is used for create dealer dialog.
	 **/
	public void dialog_dealer() {

		final Dialog dialog = new Dialog(ScanQrCodeActivity.this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

		dialog.setContentView(R.layout.dialog_dealer);
		dialog.setCancelable(true);

		final EditText autocomplete_dealer_name = (EditText) dialog.findViewById(R.id.autocomplete_product_name);
		autocomplete_dealer_name.setVisibility(View.VISIBLE);

		final ListView listview = (ListView) dialog.findViewById(R.id.listview_securityquestion);
		ImageButton imagebutton_close = (ImageButton) dialog.findViewById(R.id.button_close);
		TextView textview_title = (TextView) dialog.findViewById(R.id.textview_title);
		textview_title.setText("Please select a Dealer");

		dealerAdapter = new DealerAdapter(arrayList_dealer, mContext);
		listview.setAdapter(dealerAdapter);
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
				// TODO Auto-generated method stub
				HelperMethods.hideSoftKeyboard(ScanQrCodeActivity.this);

				DealerBean dealerBean = arrayList_dealer.get(position);

				dealer_name = dealerBean.getDealerName();
				dealer_id = dealerBean.getDealerId();

				Log.e("Dealer Name",
						"Current:" + textview_dealer_name.getText().toString().trim() + ",  Selected" + dealer_name);

				if (textview_dealer_name.getText().toString().trim().equalsIgnoreCase(dealer_name)) {
					Toast.makeText(ScanQrCodeActivity.this, "Dealer already selected", Toast.LENGTH_SHORT).show();
				} else {
					textview_dealer_name.setText(dealer_name);

					// GlobalVariables.DEALER_NAME = final_dealer_name;
					// GlobalVariables.DEALER_ID = final_dealer_id;

					Editor editor = mPrefs.edit();
					editor.putString("DEALER_NAME", final_dealer_name);
					editor.putString("DEALER_ID", final_dealer_id);
					editor.commit();

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
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable arg0) {

				// TODO Auto-generated method stub
				final ArrayList<DealerBean> selectedArrayList = getArrayList(arrayList_dealer,
						autocomplete_dealer_name.getText().toString().trim());

				dealerAdapter = new DealerAdapter(selectedArrayList, mContext);

				listview.setAdapter(dealerAdapter);
				listview.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
						// TODO Auto-generated method stub
						HelperMethods.hideSoftKeyboard(ScanQrCodeActivity.this);

						DealerBean dealerBean = selectedArrayList.get(position);
						dealer_name = dealerBean.getDealerName();
						dealer_id = dealerBean.getDealerId();

						if (textview_dealer_name.getText().toString().trim().equalsIgnoreCase(dealer_name)) {
							Toast.makeText(ScanQrCodeActivity.this, "Dealer already selected", Toast.LENGTH_SHORT)
									.show();
						} else {

							final_dealer_name = dealer_name;
							final_dealer_id = dealer_id;

							// GlobalVariables.DEALER_NAME = final_dealer_name;
							// GlobalVariables.DEALER_ID = final_dealer_id;

							Editor editor = mPrefs.edit();
							editor.putString("DEALER_NAME", final_dealer_name);
							editor.putString("DEALER_ID", final_dealer_id);
							editor.commit();

							textview_dealer_name.setText(dealer_name);

							dialog.dismiss();

						}

					}
				});

			}

			private ArrayList<DealerBean> getArrayList(ArrayList<DealerBean> arrayList_dealer, String filter) {
				// TODO Auto-generated method stub

				ArrayList<DealerBean> selectedArrayList = new ArrayList<DealerBean>();
				if (filter.length() >= 0) {

					for (int i = 0; i < arrayList_dealer.size(); i++) {
						if (arrayList_dealer.get(i).getDealerName().toLowerCase().contains(filter.toLowerCase()))
							selectedArrayList.add(arrayList_dealer.get(i));
					}
					return selectedArrayList;
				}

				else {
					return arrayList_dealer;
				}
			}
		});

		dialog.show();

	}
	
	//http://125.19.46.252/ws/get_pointsAPI.php?MOBILE=9984216087
	public class GetComplaint extends AsyncTask<String, String, String> {

	
		StringBuilder stringBuilder = new StringBuilder();

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			//Toast.makeText(getApplicationContext(), "Point Api Fired", 500).show();
		}

		@Override
		protected String doInBackground(String... params) {
			try {
				URL url = new URL("http://125.19.46.252/ws/get_pointsAPI.php?MOBILE=" + MobForPoint);
				//Log.e("URL ", url.toString());
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
				Log.e("POINTS", s);
				JSONObject obj = new JSONObject(s);
				openingPoints=obj.getString("op_opening");
				earnedPoints=obj.getString("op_earned");
				closingPoints=obj.getString("op_closing");
				savePointsInSharedPrefrences();
//				displayPointsTable();
//				textview_opening_point.setText(openingPoints);
//				textview_earned_point.setText(earnedPoints);
//				textview_close_point.setText(closingPoints);
				setInput();
			} catch (Exception e) {
				//Toast.makeText(ScanQrCodeActivity.this, "Network Error Occurred...", Toast.LENGTH_SHORT).show();
			}
		}
	}


	public void EnableRuntimePermissionToAccessCamera(){

		if (ActivityCompat.shouldShowRequestPermissionRationale(ScanQrCodeActivity.this,
				Manifest.permission.CAMERA))
		{

			// Printing toast message after enabling runtime permission.
			//Toast.makeText(MainActivity.this,"CAMERA permission allows us to Access CAMERA app", Toast.LENGTH_LONG).show();

		} else {

			ActivityCompat.requestPermissions(ScanQrCodeActivity.this,new String[]{Manifest.permission.CAMERA}, RequestPermissionCode);

		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		switch (requestCode) {
			case RequestPermissionCode:
				if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


				} else {



				}
				break;
		}
	}
}
