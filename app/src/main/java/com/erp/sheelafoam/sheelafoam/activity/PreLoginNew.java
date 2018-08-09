package com.erp.sheelafoam.sheelafoam.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.erp.sheelafoam.R;
import com.erp.sheelafoam.sheelafoam.qrcode.ScanQrCodeActivity;
import com.erp.sheelafoam.sheelafoam.utility.AsyncTaskListner;
import com.erp.sheelafoam.sheelafoam.utility.ConnectionDetector;
import com.erp.sheelafoam.sheelafoam.utility.GlobalVariables;
import com.erp.sheelafoam.sheelafoam.utility.MyAsyncTask;
import com.erp.sheelafoam.sheelafoam.xperia.activity.ComplaintNew;
import com.erp.sheelafoam.sheelafoam.xperia.function.XperiaFunctions;
import com.splunk.mint.Mint;

import org.json.JSONObject;

//import com.sheela.employeeportal.sheelafoam.xperia.dataholders.DealerIdHolder;

public class PreLoginNew extends Activity implements OnClickListener, AsyncTaskListner {

	private ProgressDialog dialog;
	ConnectionDetector con;
	private SharedPreferences mPrefs;
	boolean isLogin = false;
	Context ctx;
	private String user_type = "", op_menu_id = "", auth_token = "", op_dealer_category = "", op_user_name = "",
			op_user_type = "", message = "", user_role = "", otp = "";
	private String op_message, user_id = "", op_authorised_yn = "", op_email_id = "", op_dealer_id, op_dealer_name;

	// Json object for response.

	JSONObject jObject_response;

	// UI controls.

	EditText edittext_mobile_number;
	Button btn_ok;

	// String variables for hold input and output data.

	String user_name = "";

	/**
	 * Device registration id return by GCM at splash screen.
	 **/
	String reg_id;

	/**
	 * device id.
	 **/

	String device_id;

	// int variable for differentiate request

	int action;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pre_login);
		ctx = PreLoginNew.this;

		Mint.initAndStartSession(PreLoginNew.this, "155d0310");

		con = new ConnectionDetector(getApplicationContext());
		mPrefs = getSharedPreferences("Location", Context.MODE_PRIVATE);
		GlobalVariables.getIPAddress(ctx);

		reg_id = mPrefs.getString(SplashActivity.PROPERTY_REG_ID, "");
		Log.d("DeviceID New:", reg_id);
		if (reg_id.equals("")) {
			reg_id = GlobalVariables.DEVICE_ACCESS_TOKEN;
		}
		device_id = mPrefs.getString("DEVICE_ID", "");

		isLogin = mPrefs.getBoolean("isLogin", false);

		/* Hiding tour trip tab for Distributor & Dealer */
		user_type = mPrefs.getString("op_user_type", null);

		initialization();
		setOnclickListner();

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub

		// unregister broadcast receiver.

		unregisterReceiver(receiver);
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub

		// Register broadcast receiver.

		IntentFilter filter = new IntentFilter();
		filter.addAction("android.provider.Telephony.SMS_RECEIVED");
		registerReceiver(receiver, filter);
		super.onResume();
	}

	public void initialization() {

		btn_ok = (Button) findViewById(R.id.btn_ok);
		edittext_mobile_number = (EditText) findViewById(R.id.edittext_mobile_number);

	}

	private void setOnclickListner() {
		// TODO Auto-generated method stub

		btn_ok.setOnClickListener(this);

	}

	public void getInputData() {
		user_name = edittext_mobile_number.getText().toString().trim();

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {
		case R.id.btn_ok:

			GlobalVariables.hideSoftKeyboard(PreLoginNew.this);
			getInputData();

			XperiaFunctions.setMob(PreLoginNew.this, edittext_mobile_number.getText().toString());

			if (user_name.length() > 0) {
				if (con.isConnectingToInternet()) {
					setJsonRequest();
				} else {
					GlobalVariables.defaultOneButtonDialog(PreLoginNew.this, GlobalVariables.CONNECTION_ERROR);
				}

			} else if (user_name.length() == 0) {
				GlobalVariables.defaultOneButtonDialog(PreLoginNew.this, "Please enter mobile number");
			} else {
				GlobalVariables.defaultOneButtonDialog(PreLoginNew.this, "Mobile Number must be ten digits");
			}

			break;

		default:

			break;
		}

	}

	@Override
	public void onTaskComplete(String result) {
		// TODO Auto-generated method stub
			
		

		if (result != null && result.length() > 0) {
			if (action == 1) {
				Log.e("Action 1 After Mobile Enter ", result);
				try {
					jObject_response = new JSONObject(result);
					try {
						String status = jObject_response.getString("status");
						if (status.equals("200")) {
							JSONObject data = jObject_response.getJSONObject("data");
							String op_authorised_yn = data.getString("op_authorised_yn");
							if (op_authorised_yn.equals("1")) {

								otp = data.getString("op_otp");
								user_role = data.getString("op_role_name");
								op_message = data.getString("op_message");

								redirect(user_role, otp, op_message);

							} else {
								Toast.makeText(getApplicationContext(), data.getString("op_message"),
										Toast.LENGTH_SHORT).show();
								String msg = data.getString("message");
								GlobalVariables.defaultOneButtonDialog(PreLoginNew.this, msg);
							}

						} else {

							JSONObject data = jObject_response.getJSONObject("data");
							String msg = data.getString("message");
							GlobalVariables.defaultOneButtonDialog(PreLoginNew.this, msg);
						}
					} catch (Exception e) {

						e.printStackTrace();
					}

				}

				catch (Exception e) {
					e.printStackTrace();
				}

			} else if (action == 2) {

				Log.e("Action 2 After Mobile Enter ", result);
			//	Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
				try {
					jObject_response = new JSONObject(result);
					try {
						String status = jObject_response.getString("status");
						if (status.equals("200")) {

							JSONObject data = jObject_response.getJSONObject("data");
							String op_authorised_yn = data.getString("op_authorised_yn");
							if (op_authorised_yn.equals("1")) {

								// op_message=data.getString("op_message");
								op_user_name = data.getString("op_user_name");
								op_user_type = data.getString("op_user_type");

								// op_menu_id=data.getString("op_menu_id");

								op_dealer_id = data.getString("op_dealer_id");
								op_dealer_name = data.getString("op_dealer_name");

								auth_token = data.getString("token");
								op_dealer_category = data.getString("op_dealer_category");

								GlobalVariables.DEALER_CATEGORY = op_dealer_category;
								GlobalVariables.MASTER_DEALER_CATEGORY = op_dealer_category;

								Log.e("auth_token", auth_token);

								Editor editor = mPrefs.edit();
								editor.putBoolean("isLogin", true);
								// editor.putString("UserID", userId);
								editor.putString("AUTH_TOKEN", auth_token);
								editor.putString("DEALER_CATEGORY", op_dealer_category);
								editor.putString("DEALER_NAME", op_dealer_name);
								editor.putString("DEALER_ID", op_dealer_id);

								editor.commit();

								setDataInSharePrefrenshed();

								setProfileJsonRequest();
							} else {
								op_message = data.getString("op_message");
								GlobalVariables.defaultOneButtonDialog(PreLoginNew.this, op_message);

							}

						} else {

							String msg = jObject_response.getString("op_message");
							GlobalVariables.defaultOneButtonDialog(PreLoginNew.this, msg);
						}

					} catch (Exception e) {
						e.printStackTrace();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			} else {

				try {
					jObject_response = new JSONObject(result);
					try {
						String status = jObject_response.getString("status");
						if (status.equals("200")) {

							JSONObject data = jObject_response.getJSONObject("data");
							// String
							// op_authorised_yn=data.getString("op_authorised_yn");

							op_email_id = data.getString("op_email_id");

							savePhotoInSharedPrefrence();

							/*
							 * if(op_authorised_yn.equals("1")){ //
							 * op_photo=data.getString("op_photo");
							 * op_email_id=data.getString("op_email_id");
							 * 
							 * 
							 * savePhotoInSharedPrefrence();
							 * 
							 * 
							 * }else{ String msg=data.getString("message");
							 * GlobalVariables.defaultOneButtonDialog(
							 * PreLoginNew.this, msg); }
							 */
						} else {
							JSONObject data = jObject_response.getJSONObject("data");
							String msg = data.getString("message");
							GlobalVariables.defaultOneButtonDialog(PreLoginNew.this, msg);
						}

					} catch (Exception e) {
						e.printStackTrace();
					}
				} catch (Exception e) {

					e.printStackTrace();
				}

			}

		} else {
			GlobalVariables.defaultOneButtonDialog(PreLoginNew.this, GlobalVariables.NO_RECORD);
		}

	}

	public void setJsonRequest() {
		action = 1;
		try {
			JSONObject jObject_request = new JSONObject();
			jObject_request.put("request", "userTypeV1");

			jObject_request.put("deviceModel", GlobalVariables.getDeviceModel());
			jObject_request.put("deviceOs", GlobalVariables.getDeviceOs());
			jObject_request.put("accessType", "A");
			jObject_request.put("mobile1", user_name);
			jObject_request.put("mobile2", "");

			jObject_request.put("p_device_token", reg_id);
			jObject_request.put("p_app_version", GlobalVariables.APP_VERSION_CODE);

			Log.e("Login Post Requiest ", jObject_request.toString());
			new MyAsyncTask(this, jObject_request).execute();
		} catch (Exception e) {

		}
	}

	public void setJsonRequestForAccessToken() {
		action = 2;
		try {
			JSONObject jObject_request = new JSONObject();
			jObject_request.put("request", "userAuthenticationV1");
			jObject_request.put("userId", user_name);

			// disable by Vinay on 07-08-2016

			// jObject_request.put("password",otp);

			jObject_request.put("password", otp);

			jObject_request.put("deviceModel", GlobalVariables.getDeviceModel());
			jObject_request.put("deviceOs", GlobalVariables.getDeviceOs());
			jObject_request.put("accessType", "A");

			// disable by Vinay on 07-08-2016

			/*
			 * jObject_request.put("mobile1",GlobalVariables.getPhoneNumber_one(
			 * this)); jObject_request.put("mobile2","");
			 */

			jObject_request.put("p_mobile_no_1", GlobalVariables.getPhoneNumber_one(this));
			jObject_request.put("p_mobile_no_2", "");

			jObject_request.put("p_role_name", user_role);

			jObject_request.put("p_device_token", reg_id);
			jObject_request.put("p_app_version", String.valueOf(GlobalVariables.APP_VERSION_CODE));

			Log.e("OTP Post ", jObject_request.toString());
			new MyAsyncTask(this, jObject_request).execute();
		} catch (Exception e) {

		}
	}

	public void saveUnRegisteredUserMobile() {
		Editor editor = mPrefs.edit();

		editor.putString("op_user_name", user_name);
		editor.putString("op_user_type", user_role);
		editor.putString("user_id", user_name);
		editor.putString("op_user_mobile", user_name);
		editor.putBoolean("isLogin", true);

		// editor.putString("op_user_mobile", user_name);

		editor.commit();
	}

	public void saveRegisteredUserMobile() {
		Editor editor = mPrefs.edit();

		editor.putString("op_user_name", user_name);
		editor.putString("op_user_type", user_role);
		editor.putString("op_user_mobile", user_name);
		// editor.putBoolean("isLogin", true);

		// editor.putString("op_user_mobile", user_name);

		editor.commit();
	}

	public void redirect(String role, String otp, String op_message) {
		if (op_message.equals("OTP BASED#MENU")) {

			Toast.makeText(PreLoginNew.this, GlobalVariables.OTP_SENDING, Toast.LENGTH_LONG).show();

		} else if (op_message.equals("OTP BASED")) {

			Toast.makeText(PreLoginNew.this, GlobalVariables.OTP_SENDING, Toast.LENGTH_LONG).show();
		} else {

			callLoginActivity(role);
		}
	}

	public void redirect(String role) {
		if (role.equals("CUSTOMER") || role.equals("AGENT")) {
			callComplainActivity(mPrefs.getString("op_user_name", ""));
		}

		else if (role.equals("SAATHI")) {
			callScanQrCodeActivity(mPrefs.getString("op_user_name", ""));
		}

		else {

		}
	}

	public void callLoginActivity(String role) {
		saveRegisteredUserMobile();

		// setDataInSharePrefrenshed();
		Intent i = new Intent(getBaseContext(), LoginActivity.class);
		// Intent i=new Intent(getBaseContext(),PreLogin.class);
		i.putExtra("op_user_type", role);
		i.putExtra("user_name", user_name);
		startActivity(i);
		// Remove activity
		finish();
	}

	public void callComplainActivity(String mobile_number) {
		Intent i = new Intent(getBaseContext(), ComplaintNew.class);
		// Intent i=new Intent(getBaseContext(),PreLogin.class);
		i.putExtra("op_user_name", mobile_number);
		i.putExtra("op_user_role", user_role);
		startActivity(i);
		// Remove activity
		finish();
	}

	public void callScanQrCodeActivity(String mobile_number) {
		Intent i = new Intent(getBaseContext(), ScanQrCodeActivity.class);
		// Intent i=new Intent(getBaseContext(),PreLogin.class);
		i.putExtra("op_user_name", mobile_number);
		startActivity(i);
		// Remove activity
		finish();
	}

	private BroadcastReceiver receiver = new BroadcastReceiver() {
		// Get the object of SmsManager
		final SmsManager sms = SmsManager.getDefault();

		public void onReceive(Context context, Intent intent) {

			// Retrieves a map of extended data from the intent.
			final Bundle bundle = intent.getExtras();

			try {

				if (bundle != null) {

					final Object[] pdusObj = (Object[]) bundle.get("pdus");

					for (int i = 0; i < pdusObj.length; i++) {

						SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
						String phoneNumber = currentMessage.getDisplayOriginatingAddress();

						String senderNum = phoneNumber;
						message = currentMessage.getDisplayMessageBody();

						Log.e("SmsReceiver", "senderNum: " + senderNum + "; message: " + message);

						if (message.contains("One Time Password is")) {
							if (message.contains("for greatplus App")) {

								if (op_message.equals("OTP BASED#MENU") || op_message.equals("OTP BASED")) {

									Log.e("I am in Activity broadcast reciever", "I am in Activity broadcast reciever");
									new OtpValidateAsyncTask().execute();
								}
							}
						}

					} // end for loop
				} // bundle is null

			} catch (Exception e) {
				Log.e("SmsReceiver", "Exception smsReceiver" + e);

			}
		}
	};

	public class OtpValidateAsyncTask extends AsyncTask<Void, Void, Void> {

		String otp_validation_result = "";

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			dialog = new ProgressDialog(PreLoginNew.this);

			dialog.setMessage("Please wait....");

			dialog.setCancelable(false);
			dialog.show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			String OTP = getOtpFromString(message);
			if (OTP.equals(otp))
				otp_validation_result = "true";
			else
				otp_validation_result = "false";
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			dialog.dismiss();
			if (otp_validation_result.equals("true")) {
				Toast.makeText(PreLoginNew.this, "OTP validate successfully", Toast.LENGTH_LONG).show();

				if (op_message.equals("OTP BASED")) {
					saveUnRegisteredUserMobile();
					redirect(user_role);
					//setJsonRequestForAccessToken();
				} else {

					// call API for password validate. OTP will be pass as password in "login" API.

					setJsonRequestForAccessToken();
				}

			} else {
				Toast.makeText(PreLoginNew.this, "OTP not validate", Toast.LENGTH_LONG).show();
			}
		}

	}

	public String getOtpFromString(String str) {
		if (str == null) {
			return null;
		}

		StringBuffer strBuff = new StringBuffer();
		char c;

		for (int i = 0; i < str.length(); i++) {
			c = str.charAt(i);

			if (Character.isDigit(c)) {
				strBuff.append(c);
			}
		}
		return strBuff.toString();
	}

	public void setDataInSharePrefrenshed() {
		Editor editor = mPrefs.edit();

		editor.putString("op_authorised_yn", op_authorised_yn);
		editor.putString("op_message", op_message);
		editor.putString("op_user_name", op_user_name);
		editor.putString("op_user_type", op_user_type);
		editor.putString("op_menu_id", op_menu_id);

		editor.putString("user_id", user_name);

		editor.commit();
	}

	public void setProfileJsonRequest() {
		action = 3;
		try {
			JSONObject jObject_request = new JSONObject();
			jObject_request.put("request", "getProfile");

			Log.e("POST Profile Requiest ", jObject_request.toString());
			new MyAsyncTask(this, jObject_request).execute();
		} catch (Exception e) {

		}

	}

	public void savePhotoInSharedPrefrence() {
		Editor editor = mPrefs.edit();
		/*
		 * if(!op_photo.equals("null")) { editor.putString("PROFILE_PHOTO",
		 * op_photo);
		 * 
		 * }
		 */

		editor.putString("EMAIL_ID", op_email_id);
		editor.commit();

		// callService();

		// Intent i=new Intent(getBaseContext(),HomeActivity.class);
		Intent i = new Intent(getBaseContext(), HomeActivityNew.class);
		startActivity(i);
		finish();
	}

}
