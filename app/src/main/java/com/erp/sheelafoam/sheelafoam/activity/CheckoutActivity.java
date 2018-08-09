package com.erp.sheelafoam.sheelafoam.activity;

import android.app.ActionBar.LayoutParams;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.erp.sheelafoam.R;
import com.erp.sheelafoam.erp.utils.NetworkTask;
import com.erp.sheelafoam.erp.utils.NetworkTask.Result;
import com.erp.sheelafoam.sheelafoam.utility.ConnectionDetector;
import com.erp.sheelafoam.sheelafoam.utility.GlobalVariables;
import com.erp.sheelafoam.sheelafoamutility.AvenuesParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckoutActivity extends AppCompatActivity implements OnClickListener,
		Result {

	ConnectionDetector con;
	private SharedPreferences mPrefs;
	String UserID, DealerID, SerialNo;
	RecordHolder holder = null;
	RecordHolder_NEW holder_new = null;
	ListView product_listing, amount_listing;
	ImageButton btnAddProduct;
	private static final String NUMBER_GCARD = "GCARD_NUMBER";
	private static final String SUB_PRODUCT_SUB = "SUB_PRODUCT";
	private static final String RATE_MRP = "MRP_RATE";
	private static final String AMOUNT_TRAN = "TRAN_AMOUNT";
	private static final String INVOICE_ID = "INVOICE_DTL_ID";// INVOICE_DTL_ID
	private static final String DESCRIPTION_TRAN = "TRAN_DESCRIPTION";
	EditText etSerialNo;
	TextView pay_cash, pay_card, tv_no_data, pay_link;
	EditText ed_full_name, ed_email, ed_contact_no, ed_pin_code, ed_address,
			payment_pay, et_serial_no;
	String strFullName, strEmail, strContactNo, strCity, strState, strPinCode,
			strAddress, strSerialNumber = "";
	Spinner ed_city, ed_state;

	String serialNo = null;

	String invoice_id;
	ArrayList<HashMap<String, String>> menuItems = new ArrayList<HashMap<String, String>>();
	ArrayList<HashMap<String, String>> menuItems1 = new ArrayList<HashMap<String, String>>();

	HashMap<String, ArrayList<String>> listCity = new HashMap<String, ArrayList<String>>();
	ArrayList<String> listState = new ArrayList<String>();

	String op_transaction_id, op_service_provider_name, op_merchant_id;
	TextView tv_invoice_id;

	TextView textview_back, textview_user_id;
	ImageButton btn_logout;
	private String user_role = "";

	AlertDialog alertDialog;
	Toolbar toolbar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_checkout);

		mPrefs = getSharedPreferences("Location", Context.MODE_PRIVATE);
		UserID = mPrefs.getString("user_id", "");// DEALER_ID

		/*
		 * Toast.makeText(getApplicationContext(), UserID, Toast.LENGTH_LONG)
		 * .show();
		 */

		DealerID = mPrefs.getString("DEALER_ID", "");
		btnAddProduct = (ImageButton) findViewById(R.id.btn_add_product);
		etSerialNo = (EditText) findViewById(R.id.et_serial_no);
		product_listing = (ListView) findViewById(R.id.product_listing);
		product_listing.setOnTouchListener(new OnTouchListener() {
			// Setting on Touch Listener for handling the touch inside
			// ScrollView
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// Disallow the touch request for parent scroll on touch of
				// child view
				v.getParent().requestDisallowInterceptTouchEvent(true);
				return false;
			}
		});
		pay_card = (TextView) findViewById(R.id.pay_card);
		pay_cash = (TextView) findViewById(R.id.pay_cash);
		pay_link = (TextView) findViewById(R.id.pay_LINK);
		payment_pay = (EditText) findViewById(R.id.payment_pay);
		et_serial_no = (EditText) findViewById(R.id.et_serial_no);
		amount_listing = (ListView) findViewById(R.id.amount_listing);
		tv_invoice_id = (TextView) findViewById(R.id.invoice_id);
		tv_no_data = (TextView) findViewById(R.id.tv_no_data);
		amount_listing.setOnTouchListener(new OnTouchListener() {
			// Setting on Touch Listener for handling the touch inside
			// ScrollView
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// Disallow the touch request for parent scroll on touch of
				// child view
				v.getParent().requestDisallowInterceptTouchEvent(true);
				return false;
			}
		});
		ed_full_name = (EditText) findViewById(R.id.ed_full_name);
		ed_email = (EditText) findViewById(R.id.ed_email);
		ed_contact_no = (EditText) findViewById(R.id.ed_contact_no);
		ed_city = (Spinner) findViewById(R.id.ed_city);
		ed_state = (Spinner) findViewById(R.id.ed_state);
		ed_pin_code = (EditText) findViewById(R.id.ed_pin_code);
		ed_address = (EditText) findViewById(R.id.ed_address);
		btnAddProduct.setOnClickListener(this);
		pay_cash.setOnClickListener(this);
		pay_card.setOnClickListener(this);
		pay_link.setOnClickListener(this);
		// callWSDetail();

		initialize();

		callWSCityState();

		ed_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parentView,
                                       View selectedItemView, int position, long id) {

				if (position != 0) {
					ArrayList city = new ArrayList();
					city.add("Select City*");
					city.addAll(listCity.get(ed_state.getSelectedItem()
							.toString()));

					ArrayAdapter<String> adapter = new ArrayAdapter<String>(
							getApplicationContext(), R.layout.spinner_text,
							city);

					ed_city.setAdapter(adapter);
				} else {
					String city[] = { "Select City*" };
					ArrayAdapter<String> adapter = new ArrayAdapter<String>(
							getApplicationContext(), R.layout.spinner_text,
							city);

					ed_city.setAdapter(adapter);
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> parentView) {
				// your code here
			}

		});

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) // Press Back Icon
		{
			finish();
		}
		return super.onOptionsItemSelected(item);
	}

	void initialize() {

		mPrefs = getSharedPreferences("Location", Context.MODE_PRIVATE);

		toolbar = (Toolbar) findViewById(R.id.app_toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);

		textview_back = (TextView) findViewById(R.id.app_toolbar_title);
		textview_back.setText("Digital Payment");
//		if (mPrefs.getString("op_user_type", null).equalsIgnoreCase(
//				"DISTRIBUTOR")) {
//			textview_back.setText("greatplus distributor app");
//		} else if (mPrefs.getString("op_user_type", null).equalsIgnoreCase(
//				"DEALER")) {
//			textview_back.setText("greatplus dealer app");
//		} else {
//			textview_back.setText("greatplus employee app");
//		}

		//textview_user_id = (TextView) findViewById(R.id.textview_user_id);
		//btn_logout = (ImageButton) findViewById(R.id.btn_logout);
		//btn_logout.setOnClickListener(this);
		//textview_user_id.setText(mPrefs.getString("user_id", ""));

		/*SpannableString spannable_string = new SpannableString(textview_back
				.getText().toString().trim());
		spannable_string.setSpan(new ForegroundColorSpan(getResources()
				.getColor(R.color.color_great)), 0, 5,
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		spannable_string.setSpan(new ForegroundColorSpan(getResources()
				.getColor(R.color.color_plus)), 5, 10,
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

		textview_back.setText(spannable_string);*/
	}

	public static void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null)
			return;

		int desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(),
				MeasureSpec.UNSPECIFIED);
		int totalHeight = 0;
		View view = null;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			view = listAdapter.getView(i, view, listView);
			if (i == 0)
				view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth,
						LayoutParams.WRAP_CONTENT));

			view.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
			totalHeight += view.getMeasuredHeight();
		}
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}

	private void callWSDetail() {
		try {
			JSONObject obj = new JSONObject();
			obj.put("p_greatplus_user_id", UserID);
			obj.put("p_dealer_id", DealerID);
			obj.put("mode", "list_item");

			String url = "http://125.19.46.252/ws/prc_get_product_gcard.php";

			NetworkTask networkTask = new NetworkTask(CheckoutActivity.this, 1,
					obj.toString());
			networkTask.setDialogMessage("Please wait..");
			networkTask.exposePostExecute(this);
			networkTask.execute(url);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void callWSCityState() {
		try {
			JSONObject obj = new JSONObject();

			String url = "http://sleepwellproducts.com/service/get_state_city.php";

			NetworkTask networkTask = new NetworkTask(CheckoutActivity.this, 6,
					obj.toString());
			networkTask.setDialogMessage("Please wait..");
			networkTask.exposePostExecute(this);
			networkTask.execute(url);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/*@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.checkout, menu);
		return true;
	}*/

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.btn_add_product:
			strSerialNumber = et_serial_no.getText().toString();
			strContactNo = ed_contact_no.getText().toString();
			if (!strSerialNumber.isEmpty())
				callWSADD();
			else {
				Toast.makeText(getApplicationContext(),
						"Please enter serial number", Toast.LENGTH_LONG).show();
			}

			break;
		case R.id.pay_cash:
			if (validate()) {
				if (serialNo != null) {
					showDialogCash("Transaction successful. Guarantee against serial "
							+ serialNo + " has been registered successfully.");
				} else {
					showDialogCash("Transaction successful.");
				}

			}
			break;
		case R.id.pay_card:
			if (validate()) {

				showDialog();

			}
			break;
		case R.id.pay_LINK:
			if (validate()) {
				callWSPAYLINKCASH();
				callWSPAYLINK();

			}
			break;
		case R.id.btn_logout:
			GlobalVariables.logout(this);
			break;
		default:
			break;
		}

	}

	private boolean validate() {
		boolean flag = true;
		boolean flagPopup = true;

		String payment_pay1 = payment_pay.getText().toString();

		strContactNo = ed_contact_no.getText().toString();
		strFullName = ed_full_name.getText().toString();

		strState = ed_state.getSelectedItem().toString();
		strCity = ed_city.getSelectedItem().toString();

		strPinCode = ed_pin_code.getText().toString();
		strAddress = ed_address.getText().toString();
		strEmail = ed_email.getText().toString();

		// strFullName, strEmail, strContactNo, strCity, strState, strPinCode,
		// strAddress

		if (strFullName.trim().length() == 0) {
			// ed_full_name.setError("Please provide name");
			flag = false;
			ed_full_name.setFocusable(true);
			// ed_full_name.setError("");

			ed_full_name.setBackgroundResource(R.drawable.btn_red_curve);
		} else {
			ed_full_name.setBackgroundResource(R.drawable.btn_gry_curve);
		}
		if (strEmail.trim().length() == 0) {
			// ed_email.setError("Please provide email id");
			flag = false;
			ed_email.setBackgroundResource(R.drawable.btn_red_curve);
			// showDialogValidate("Payment can only be initiated after completing mandatory fields, highlighted in Red");

		} else if (!isEmailValid(strEmail)) {
			// ed_email.setError("Please enter correct email id");
			flag = false;
			flagPopup = false;
			ed_email.setFocusable(true);
			ed_email.setBackgroundResource(R.drawable.btn_red_curve);
			showDialogValidate("Please enter correct email id");
		} else {
			ed_email.setBackgroundResource(R.drawable.btn_gry_curve);
		}

		if (strContactNo.trim().length() == 0) {
			// ed_contact_no.setError("Please provide contact number");
			flag = false;
			ed_contact_no.setFocusable(true);

			ed_contact_no.setBackgroundResource(R.drawable.btn_red_curve);
			// showDialogValidate("Payment can only be initiated after completing mandatory fields, highlighted in Red");

		} else if (strContactNo.trim().length() != 10) {
			// ed_contact_no.setError("Please provide valid contact number");
			flag = false;
			flagPopup = false;
			ed_contact_no.setFocusable(true);
			ed_contact_no.setBackgroundResource(R.drawable.btn_red_curve);
			showDialogValidate("Please provide valid contact number");
		} else {
			ed_contact_no.setBackgroundResource(R.drawable.btn_gry_curve);
		}

		if (strState.equals("Select State*")) {
			ed_state.setFocusable(true);
			// Toast.makeText(getApplicationContext(), "Please Select State",
			// Toast.LENGTH_SHORT).show();
			flag = false;
			ed_state.setBackgroundResource(R.drawable.spinner_checkout_red);
			// showDialogValidate("Payment can only be initiated after completing mandatory fields, highlighted in Red");
		} else {
			ed_state.setBackgroundResource(R.drawable.spinner_checkout);
		}

		if (strCity.equals("Select City*")) {
			ed_city.setFocusable(true);
			// Toast.makeText(getApplicationContext(), "Please Select City",
			// Toast.LENGTH_SHORT).show();
			flag = false;
			ed_city.setBackgroundResource(R.drawable.spinner_checkout_red);
			// showDialogValidate("Payment can only be initiated after completing mandatory fields, highlighted in Red");
		} else {
			ed_city.setBackgroundResource(R.drawable.spinner_checkout);
		}

		if (strPinCode.trim().length() == 0) {
			// ed_pin_code.setError("Please provide pincode");
			flag = false;
			ed_pin_code.setBackgroundResource(R.drawable.btn_red_curve);
			// showDialogValidate("Payment can only be initiated after completing mandatory fields, highlighted in Red");
			ed_pin_code.setFocusable(true);
		} else if (strPinCode.trim().length() != 6) {
			// ed_pin_code.setError("Please provide valid pincode");
			flag = false;
			flagPopup = false;
			ed_pin_code.setFocusable(true);
			ed_pin_code.setBackgroundResource(R.drawable.btn_red_curve);
			showDialogValidate("Please provide valid pincode");
		} else {
			ed_pin_code.setBackgroundResource(R.drawable.btn_gry_curve);
		}

		if (strAddress.trim().length() == 0) {
			// ed_address.setError("Please provide address");
			flag = false;
			ed_address.setFocusable(true);
			ed_address.setBackgroundResource(R.drawable.btn_red_curve);
			// showDialogValidate("Payment can only be initiated after completing mandatory fields, highlighted in Red");
		} else {
			ed_address.setBackgroundResource(R.drawable.btn_gry_curve);
		}

		if (!flag)
			if (flagPopup)
				showDialogValidate("Payment can only be initiated after completing mandatory fields, highlighted in Red");

		if (flag) {
			if (payment_pay1.length() == 0) {
				payment_pay.setFocusable(true);
				flag = false;
				// Toast.makeText(getApplicationContext(),
				// "Please Enter Payment Pay Amount",
				// Toast.LENGTH_SHORT).show();
				showDialogValidate("Kindly enter the product you wish to purchase or first enter the INR to be paid");
			} else if (payment_pay1.trim().equals("0")) {
				payment_pay.setFocusable(true);
				flag = false;
				// Toast.makeText(getApplicationContext(),
				// "Please Enter Payment Pay Amount",
				// Toast.LENGTH_SHORT).show();
				showDialogValidate("Kindly enter the product you wish to purchase or first enter the INR to be paid");

			}
		}

		return flag;
	}

	private void callWSPAYLINK() {
		try {
			JSONObject obj = new JSONObject();

			obj.put("p_customer_name", strFullName);
			obj.put("p_payment_amount", payment_pay.getText().toString());
			obj.put("p_dealer_id", DealerID);
			obj.put("p_email", strEmail);
			obj.put("p_contact_number", strContactNo);

			/*
			 * obj.put("p_greatplus_user_id", UserID);
			 * 
			 * obj.put("p_transaction_date", ""); obj.put("p_invoice_id", "");
			 * obj.put("p_transaction_id", op_transaction_id);
			 * 
			 * 
			 * 
			 * obj.put("p_city", strCity); obj.put("p_state", strState);
			 * obj.put("p_pin_code", strPinCode); obj.put("p_address",
			 * strAddress);
			 * 
			 * obj.put("p_payment_mode", "Cash");
			 */
			String url = "";
			if (op_service_provider_name.equals("CCAVENUE"))
				url = "http://sleepwellproducts.com/ccav/web/NON_SEAMLESS_KIT/pay_link_to_customer.php";
			else if (op_service_provider_name.equals("HDFC"))
				url = "http://sleepwellproducts.com//hdfc/NON_SEAMLESS_KIT/pay_link_to_customer.php";

			NetworkTask networkTask = new NetworkTask(CheckoutActivity.this, 7,
					obj.toString());
			networkTask.setDialogMessage("Please wait..");
			networkTask.exposePostExecute(this);
			networkTask.execute(url);

		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	private void callWSPAYCASH() {
		try {
			JSONObject obj = new JSONObject();
			obj.put("p_greatplus_user_id", UserID);
			obj.put("p_dealer_id", DealerID);
			obj.put("p_transaction_date", "");
			obj.put("p_invoice_id", "");
			obj.put("p_transaction_id", op_transaction_id);
			obj.put("p_customer_name", strFullName);
			obj.put("p_email", strEmail);
			obj.put("p_contact_number", strContactNo);
			obj.put("p_city", strCity);
			obj.put("p_state", strState);
			obj.put("p_pin_code", strPinCode);
			obj.put("p_address", strAddress);
			obj.put("p_payment_amount", payment_pay.getText().toString());
			obj.put("p_payment_mode", "Cash");

			String url = "http://125.19.46.252/ws/prc_get_payment_gcard.php";

			NetworkTask networkTask = new NetworkTask(CheckoutActivity.this, 4,
					obj.toString());
			networkTask.setDialogMessage("Please wait..");
			networkTask.exposePostExecute(this);
			networkTask.execute(url);

		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	private void callWSPAYLINKCASH() {
		try {
			JSONObject obj = new JSONObject();
			obj.put("p_greatplus_user_id", UserID);
			obj.put("p_dealer_id", DealerID);
			obj.put("p_transaction_date", "");
			obj.put("p_invoice_id", "");
			obj.put("p_transaction_id", op_transaction_id);
			obj.put("p_customer_name", strFullName);
			obj.put("p_email", strEmail);
			obj.put("p_contact_number", strContactNo);
			obj.put("p_city", strCity);
			obj.put("p_state", strState);
			obj.put("p_pin_code", strPinCode);
			obj.put("p_address", strAddress);
			obj.put("p_payment_amount", payment_pay.getText().toString());
			if (op_service_provider_name.equals("CCAVENUE"))
				obj.put("p_payment_mode", "ccavenue-Pay Link To Customer");
			else if (op_service_provider_name.equals("HDFC"))
				obj.put("p_payment_mode", "hdfc-Pay Link To Customer");

			String url = "http://125.19.46.252/ws/prc_get_payment_gcard.php";

			NetworkTask networkTask = new NetworkTask(CheckoutActivity.this, 8,
					obj.toString());
			networkTask.setDialogMessage("Please wait..");
			networkTask.exposePostExecute(this);
			networkTask.execute(url);

		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	private void callWSPAYCARD() {
		try {
			JSONObject obj = new JSONObject();
			obj.put("p_greatplus_user_id", UserID); //ROSHNIS
			obj.put("p_dealer_id", DealerID); //S1DLH912032

			String url = "http://125.19.46.252/ws/get_transaction.php";

			NetworkTask networkTask = new NetworkTask(CheckoutActivity.this, 5,
					obj.toString());
			networkTask.setDialogMessage("Please wait..");
			networkTask.exposePostExecute(this);
			networkTask.execute(url);

		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	private void callWSADD() {
		// TODO Auto-generated method stub
		try {
			JSONObject obj = new JSONObject();
			// showToast(op_transaction_id);

			obj.put("p_greatplus_user_id", UserID);
			obj.put("p_dealer_id", DealerID);
			obj.put("p_non_eo", "");
			obj.put("p_gcard_number", strSerialNumber);
			obj.put("p_customer_mobile", UserID);
			obj.put("p_transaction_id", op_transaction_id);
			obj.put("p_transaction_date", "");
			obj.put("mode", "add_item");

			String url = "http://125.19.46.252/ws/prc_get_product_gcard.php";

			NetworkTask networkTask = new NetworkTask(CheckoutActivity.this, 2,
					obj.toString());
			networkTask.setDialogMessage("Please wait..");
			networkTask.exposePostExecute(this);
			networkTask.execute(url);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void resultfromNetwork(String object, int id, int arg1, String arg2) {
		// TODO Auto-generated method stub
		switch (id) {
		case 1:
			try {
				Log.e("Detail RESPONSE", object);
				// showToast(object);
				JSONObject Obj = new JSONObject(object);
				JSONArray jsonArray = Obj.getJSONArray("data");

				int status = Obj.getInt("status");

				JSONArray invoice_summary = Obj.getJSONArray("invoice_summary");
				if (jsonArray.length() > 0) {
					if (invoice_summary.length() != 0) {
						menuItems1.clear();
						for (int i = 0; i < invoice_summary.length(); i++) {
							JSONObject c = invoice_summary.getJSONObject(i);
							// String category_id = c.getString("category_id");
							String TRAN_DESCRIPTION = c
									.getString("TRAN_DESCRIPTION");
							String TRAN_AMOUNT = c.getString("TRAN_AMOUNT");

							HashMap<String, String> contact1 = new HashMap<String, String>();

							contact1.put(DESCRIPTION_TRAN, TRAN_DESCRIPTION);
							contact1.put(AMOUNT_TRAN, TRAN_AMOUNT);

							menuItems1.add(contact1);

							if (TRAN_DESCRIPTION.equals("TOTAL")) {
								payment_pay.setText(TRAN_AMOUNT);
							}

							amount_listing.setAdapter(new ListViewAdapter_NEW(
									CheckoutActivity.this));
							setListViewHeightBasedOnChildren(amount_listing);

						}
					}
				} else {

				}

				// looping through All Contacts
				if (jsonArray.length() != 0) {
					menuItems.clear();
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject c = jsonArray.getJSONObject(i);
						// String category_id = c.getString("category_id");
						if (i == 0) {
							serialNo = c.getString("GCARD_NUMBER");
						} else {
							serialNo = serialNo + ","
									+ c.getString("GCARD_NUMBER");
						}

						String GCARD_NUMBER = c.getString("GCARD_NUMBER");
						String SUB_PRODUCT = c.getString("SUB_PRODUCT");
						String MRP_RATE = c.getString("MRP_RATE");
						String INVOICE_DTL_ID = c.getString("INVOICE_DTL_ID");

						HashMap<String, String> contact = new HashMap<String, String>();

						contact.put(NUMBER_GCARD, GCARD_NUMBER);
						contact.put(SUB_PRODUCT_SUB, SUB_PRODUCT);
						contact.put(RATE_MRP, MRP_RATE);
						contact.put(INVOICE_ID, INVOICE_DTL_ID);

						menuItems.add(contact);

						product_listing.setAdapter(new ListViewAdapter(
								CheckoutActivity.this));
						setListViewHeightBasedOnChildren(product_listing);
					}
				} else {
					Toast.makeText(getApplicationContext(), "No Data!!",
							Toast.LENGTH_SHORT).show();
				}

			} catch (Exception e) {
				// TODO: handle exception
			}
			break;
		case 2:
			try {

				JSONObject Obj = new JSONObject(object);

				Log.e("ADD RESPONSE", object);
				// showToast(object);

				int status = Obj.getInt("status");
				String op_message = Obj.getString("op_message");
				Toast.makeText(getApplicationContext(), op_message,
						Toast.LENGTH_SHORT).show();
				JSONArray jsonArray = Obj.getJSONArray("data");
				JSONArray invoice_summary = Obj.getJSONArray("invoice_summary");

				if (jsonArray.length() > 0) {
					if (invoice_summary.length() != 0) {
						menuItems1.clear();
						for (int i = 0; i < invoice_summary.length(); i++) {
							JSONObject c = invoice_summary.getJSONObject(i);
							// String category_id = c.getString("category_id");
							String TRAN_DESCRIPTION = c
									.getString("TRAN_DESCRIPTION");
							String TRAN_AMOUNT = c.getString("TRAN_AMOUNT");

							HashMap<String, String> contact1 = new HashMap<String, String>();

							contact1.put(DESCRIPTION_TRAN, TRAN_DESCRIPTION);
							contact1.put(AMOUNT_TRAN, TRAN_AMOUNT);

							menuItems1.add(contact1);

							if (TRAN_DESCRIPTION.equals("TOTAL")) {
								payment_pay.setText(TRAN_AMOUNT);
							}
							amount_listing.setAdapter(new ListViewAdapter_NEW(
									CheckoutActivity.this));
							setListViewHeightBasedOnChildren(amount_listing);
							etSerialNo.setText("");

						}

					}
				} else {
					payment_pay.setText("");
					menuItems1.clear();
					ListViewAdapter_NEW listViewAdapter_NEW = new ListViewAdapter_NEW(
							getApplicationContext());
					amount_listing.setAdapter(new ListViewAdapter_NEW(
							CheckoutActivity.this));
					listViewAdapter_NEW.notifyDataSetChanged();
					setListViewHeightBasedOnChildren(amount_listing);

				}

				// looping through All Contacts
				if (jsonArray.length() != 0) {
					menuItems.clear();
					tv_no_data.setVisibility(View.GONE);
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject c = jsonArray.getJSONObject(i);
						// String category_id = c.getString("category_id");
						if (i == 0) {
							serialNo = c.getString("GCARD_NUMBER");
						} else {
							serialNo = serialNo + ","
									+ c.getString("GCARD_NUMBER");
						}
						String GCARD_NUMBER = c.getString("GCARD_NUMBER");
						String SUB_PRODUCT = c.getString("SUB_PRODUCT");
						String MRP_RATE = c.getString("MRP_RATE");
						String INVOICE_DTL_ID = c.getString("INVOICE_DTL_ID");

						/*
						 * showToast(object); Log.e("ADD RESPONSE", object);
						 */

						HashMap<String, String> contact = new HashMap<String, String>();

						contact.put(NUMBER_GCARD, GCARD_NUMBER);
						contact.put(SUB_PRODUCT_SUB, SUB_PRODUCT);
						contact.put(RATE_MRP, MRP_RATE);
						contact.put(INVOICE_ID, INVOICE_DTL_ID);

						menuItems.add(contact);

						product_listing.setAdapter(new ListViewAdapter(
								CheckoutActivity.this));
						setListViewHeightBasedOnChildren(product_listing);
					}
				} else {
					serialNo = null;
					tv_no_data.setVisibility(View.VISIBLE);
					menuItems.clear();
					product_listing.setAdapter(new ListViewAdapter(
							CheckoutActivity.this));
					setListViewHeightBasedOnChildren(product_listing);
					et_serial_no.setText("");

					/*
					 * Toast.makeText(getApplicationContext(), "No Data!!",
					 * Toast.LENGTH_LONG).show();
					 */
				}

			} catch (Exception e) {
				// TODO: handle exception
				// showToast(e.getMessage() + "");
			}
			break;
		case 3:
			try {
				JSONObject Obj = new JSONObject(object);

				Log.e("DELETE RESPONSE", "" + Obj);

				int status = Obj.getInt("status");
				String msg = Obj.getString("msg");
				JSONArray jsonArray = Obj.getJSONArray("data");
				JSONArray invoice_summary = Obj.getJSONArray("invoice_summary");

				if (jsonArray.length() > 0) {
					if (invoice_summary.length() != 0) {
						menuItems1.clear();
						for (int i = 0; i < invoice_summary.length(); i++) {
							JSONObject c = invoice_summary.getJSONObject(i);
							// String category_id = c.getString("category_id");
							String TRAN_DESCRIPTION = c
									.getString("TRAN_DESCRIPTION");
							String TRAN_AMOUNT = c.getString("TRAN_AMOUNT");

							HashMap<String, String> contact1 = new HashMap<String, String>();

							contact1.put(DESCRIPTION_TRAN, TRAN_DESCRIPTION);
							contact1.put(AMOUNT_TRAN, TRAN_AMOUNT);

							menuItems1.add(contact1);

							if (TRAN_DESCRIPTION.equals("TOTAL")) {
								payment_pay.setText(TRAN_AMOUNT);
							}

							ListViewAdapter_NEW listViewAdapter_NEW = new ListViewAdapter_NEW(
									getApplicationContext());
							amount_listing.setAdapter(new ListViewAdapter_NEW(
									CheckoutActivity.this));
							listViewAdapter_NEW.notifyDataSetChanged();
							setListViewHeightBasedOnChildren(amount_listing);
						}
					}
				} else {
					payment_pay.setText("");
					menuItems1.clear();
					ListViewAdapter_NEW listViewAdapter_NEW = new ListViewAdapter_NEW(
							getApplicationContext());
					amount_listing.setAdapter(new ListViewAdapter_NEW(
							CheckoutActivity.this));
					listViewAdapter_NEW.notifyDataSetChanged();
					setListViewHeightBasedOnChildren(amount_listing);

				}

				// looping through All Contacts
				if (jsonArray.length() != 0) {
					menuItems.clear();
					tv_no_data.setVisibility(View.GONE);
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject c = jsonArray.getJSONObject(i);

						if (i == 0) {
							serialNo = c.getString("GCARD_NUMBER");
						} else {
							serialNo = serialNo + ","
									+ c.getString("GCARD_NUMBER");
						}
						String GCARD_NUMBER = c.getString("GCARD_NUMBER");
						String SUB_PRODUCT = c.getString("SUB_PRODUCT");
						String MRP_RATE = c.getString("MRP_RATE");
						String INVOICE_DTL_ID = c.getString("INVOICE_DTL_ID");

						HashMap<String, String> contact = new HashMap<String, String>();

						contact.put(NUMBER_GCARD, GCARD_NUMBER);
						contact.put(SUB_PRODUCT_SUB, SUB_PRODUCT);
						contact.put(RATE_MRP, MRP_RATE);
						contact.put(INVOICE_ID, INVOICE_DTL_ID);

						Toast.makeText(getApplicationContext(), msg,
								Toast.LENGTH_SHORT).show();

						menuItems.add(contact);
						Log.e("Menu Items Data", "" + menuItems);

						product_listing.setAdapter(new ListViewAdapter(
								CheckoutActivity.this));
						setListViewHeightBasedOnChildren(product_listing);
					}
				} else {

					serialNo = null;
					tv_no_data.setVisibility(View.VISIBLE);
					menuItems.clear();
					product_listing.setAdapter(new ListViewAdapter(
							CheckoutActivity.this));
					setListViewHeightBasedOnChildren(product_listing);
					et_serial_no.setText("");

					/*
					 * Toast.makeText(getApplicationContext(), "No Data!!",
					 * Toast.LENGTH_LONG).show();
					 */
				}

			} catch (Exception e) {
				// TODO: handle exception
			}
			break;
		case 4:
			try {
				// showToast(object);

				JSONObject Obj = new JSONObject(object);

				int status = Obj.getInt("status");
				String msg = Obj.getString("msg");

				if (status == 1) {
					/*
					 * Toast.makeText(getApplicationContext(),
					 * "Transaction has been saved", Toast.LENGTH_SHORT)
					 * .show();
					 */
					menuItems1.clear();
					ListViewAdapter_NEW listViewAdapter_NEW = new ListViewAdapter_NEW(
							getApplicationContext());
					amount_listing.setAdapter(new ListViewAdapter_NEW(
							CheckoutActivity.this));
					listViewAdapter_NEW.notifyDataSetChanged();
					setListViewHeightBasedOnChildren(amount_listing);

					menuItems.clear();
					product_listing.setAdapter(new ListViewAdapter(
							CheckoutActivity.this));
					setListViewHeightBasedOnChildren(product_listing);

					tv_no_data.setVisibility(View.VISIBLE);
					serialNo = null;

					ed_full_name.setText("");
					ed_email.setText("");
					ed_contact_no.setText("");
					ed_pin_code.setText("");
					ed_address.setText("");
					payment_pay.setText("");
					et_serial_no.setText("");

					ed_state.setSelection(0);
					callWSPAYCARD();
				}

			} catch (Exception e) {
				// TODO: handle exception
			}

			break;
		case 5:
			try {
				// showToast(object);
				JSONObject Obj = new JSONObject(object);

				int status = Obj.getInt("status");
				String data = Obj.getString("data");
				Obj = new JSONObject(data);
				// showToast(object);
				op_transaction_id = Obj.getString("op_transaction_id");
				op_service_provider_name = Obj
						.getString("op_service_provider_name");
				op_merchant_id = Obj.getString("op_merchant_id");
				// showToast(op_service_provider_name);

				tv_invoice_id.setText("Invoice Id: " + op_transaction_id);

				ed_state.setSelection(0);

				menuItems1.clear();
				ListViewAdapter_NEW listViewAdapter_NEW = new ListViewAdapter_NEW(
						getApplicationContext());
				amount_listing.setAdapter(new ListViewAdapter_NEW(
						CheckoutActivity.this));
				listViewAdapter_NEW.notifyDataSetChanged();
				setListViewHeightBasedOnChildren(amount_listing);

				tv_no_data.setVisibility(View.VISIBLE);
				menuItems.clear();
				product_listing.setAdapter(new ListViewAdapter(
						CheckoutActivity.this));
				setListViewHeightBasedOnChildren(product_listing);
				et_serial_no.setText("");

			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			break;
		case 6:
			try {
				JSONObject Obj = new JSONObject(object);

				// /Log.e("DELETE RESPONSE", "" + Obj);

				JSONArray jsonArray = Obj.getJSONArray("data");

				String arrState[] = new String[jsonArray.length()];
				listState.add("Select State*");

				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject c = jsonArray.getJSONObject(i);
					String state = c.getString("state");
					arrState[i] = state;

					JSONArray jsonArrayCity = c.getJSONArray("city");
					if (jsonArrayCity.length() > 0) {
						ArrayList<String> arrList = new ArrayList<String>();

						for (int j = 0; j < jsonArrayCity.length(); j++) {

							JSONObject jsonObject = jsonArrayCity
									.getJSONObject(j);

							String city_name = jsonObject
									.getString("city_name");
							// showToast(city_name);
							arrList.add(city_name);
						}

						listState.add(state);
						listCity.put(state, arrList);
					}
					// showToast(state);

				}

				ArrayAdapter<String> adapter = new ArrayAdapter<String>(
						getApplicationContext(), R.layout.spinner_text,
						listState);

				ed_state.setAdapter(adapter);
				// ed_state.setAdapter(adapter);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				showToast(e.getLocalizedMessage());
			}
			// System.out.print(object);
			break;
		case 7:
			try {
				JSONObject Obj = new JSONObject(object);

				Log.e("ADD RESPONSE", object);
				// showToast(object);

				int status = Obj.getInt("status");

				String op_message = Obj.getString("message");

				if (status == 1) {
					/*
					 * Toast.makeText(getApplicationContext(),
					 * "Transaction has been saved", Toast.LENGTH_SHORT)
					 * .show();
					 */

					showDialogLink(op_message);
					menuItems1.clear();
					ListViewAdapter_NEW listViewAdapter_NEW = new ListViewAdapter_NEW(
							getApplicationContext());
					amount_listing.setAdapter(new ListViewAdapter_NEW(
							CheckoutActivity.this));
					listViewAdapter_NEW.notifyDataSetChanged();
					setListViewHeightBasedOnChildren(amount_listing);

					menuItems.clear();
					product_listing.setAdapter(new ListViewAdapter(
							CheckoutActivity.this));
					setListViewHeightBasedOnChildren(product_listing);

					tv_no_data.setVisibility(View.VISIBLE);
					serialNo = null;

					ed_full_name.setText("");
					ed_email.setText("");
					ed_contact_no.setText("");
					ed_pin_code.setText("");
					ed_address.setText("");
					payment_pay.setText("");
					et_serial_no.setText("");

					ed_state.setSelection(0);
					callWSPAYCARD();
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
			break;
		}
	}

	public class ListViewAdapter extends BaseAdapter {
		Context mcontext;
		LayoutInflater inflater;
		ArrayList<HashMap<String, String>> data;
		HashMap<String, String> resultp = new HashMap<String, String>();

		public ListViewAdapter(Context context) {

			this.mcontext = context;

		}

		@Override
		public int getCount() {

			return menuItems.size();

		}

		@Override
		public Object getItem(int position) {

			return menuItems.get(position);
		}

		@Override
		public long getItemId(int position) {

			return position;

		}

		@Override
		public View getView(final int position, View convertView,
                            ViewGroup parent) {

			View row = convertView;
			try {

				// resultp = data.get(position);
				if (row == null) {
					inflater = (LayoutInflater) mcontext
							.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					row = inflater.inflate(R.layout.checkout_row, null);

					holder = new RecordHolder();
					holder.gcard_no = (TextView) row
							.findViewById(R.id.gcard_no);
					holder.mrp = (TextView) row.findViewById(R.id.mrp);
					holder.product_name = (TextView) row
							.findViewById(R.id.product_name);

					holder.delete_product = (LinearLayout) row
							.findViewById(R.id.delete_product);
					row.setTag(holder);
				} else {
					holder = (RecordHolder) row.getTag();
				}

				try {
					/*
					 * holder.gcard_no.setText("skdjgdhgsvgzs");
					 * holder.mrp.setText
					 * (menuItems.get(position).get(RATE_MRP));
					 * holder.product_name.setText(menuItems.get(position).get(
					 * SUB_PRODUCT_SUB));
					 */

					if (!menuItems.get(position).get(NUMBER_GCARD)
							.equals("null")) {
						holder.gcard_no.setText(menuItems.get(position).get(
								NUMBER_GCARD));

					} else {
						// showToast(menuItems.get(position).get(NUMBER_GCARD));
					}

					// showToast(menuItems.get(position).get(NUMBER_GCARD));
					if (!menuItems.get(position).get(RATE_MRP).equals("null"))
						holder.mrp.setText(menuItems.get(position)
								.get(RATE_MRP));
					if (menuItems.get(position).get(SUB_PRODUCT_SUB) != null)
						holder.product_name.setText(menuItems.get(position)
								.get(SUB_PRODUCT_SUB));

					invoice_id = menuItems.get(position).get(INVOICE_ID);

					holder.delete_product.setTag(position);
					holder.delete_product
							.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									String val = menuItems.get(position).get(
											INVOICE_ID);
									String Gcard_no = menuItems.get(position)
											.get(NUMBER_GCARD);

									calWSDelete(val, Gcard_no);
								}
							});
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}

			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}

			return row;
		}
	}

	private void calWSDelete(String Invoice_ID, String GCArd) {
		try {
			JSONObject obj = new JSONObject();
			obj.put("p_greatplus_user_id", UserID);
			obj.put("p_dealer_id", DealerID);
			obj.put("p_gcard_number", GCArd);
			obj.put("p_invoice_dtl_id", Invoice_ID);
			obj.put("p_transaction_id", op_transaction_id);

			String url = "http://125.19.46.252/ws/prc_del_product_gcard.php";

			NetworkTask networkTask = new NetworkTask(CheckoutActivity.this, 3,
					obj.toString());
			networkTask.setDialogMessage("Please wait..");
			networkTask.exposePostExecute(this);
			networkTask.execute(url);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	class RecordHolder {
		TextView mrp, product_name, gcard_no;
		LinearLayout delete_product;
	}

	public class ListViewAdapter_NEW extends BaseAdapter {
		Context mcontext;
		LayoutInflater inflater;
		ArrayList<HashMap<String, String>> data;
		HashMap<String, String> resultp = new HashMap<String, String>();

		public ListViewAdapter_NEW(Context context) {

			this.mcontext = context;

		}

		@Override
		public int getCount() {

			return menuItems1.size();

		}

		@Override
		public Object getItem(int position) {

			return menuItems1.get(position);
		}

		@Override
		public long getItemId(int position) {

			return position;

		}

		@Override
		public View getView(final int position, View convertView,
                            ViewGroup parent) {

			View row = convertView;
			try {

				if (row == null) {
					inflater = (LayoutInflater) mcontext
							.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					row = inflater.inflate(R.layout.amount_row, null);

					holder_new = new RecordHolder_NEW();
					holder_new.tax = (TextView) row.findViewById(R.id.tax);
					holder_new.text = (TextView) row.findViewById(R.id.text);
					holder_new.divider = (TextView) row
							.findViewById(R.id.divider);

					row.setTag(holder_new);
				} else {
					holder_new = (RecordHolder_NEW) row.getTag();
				}

				try {
					if (!menuItems1.get(position).get(AMOUNT_TRAN)
							.equals("null"))
						holder_new.tax.setText(menuItems1.get(position).get(
								AMOUNT_TRAN));

					if (!menuItems1.get(position).get(DESCRIPTION_TRAN)
							.equals("null"))
						holder_new.text.setText(menuItems1.get(position).get(
								DESCRIPTION_TRAN));

					if (menuItems1.size() - 2 == position) {
						holder_new.divider.setVisibility(View.VISIBLE);
					} else {
						holder_new.divider.setVisibility(View.GONE);
					}

				} catch (Exception e) {
					// TODO: handle exception
				}

			} catch (Exception e) {
				// TODO: handle exception
			}

			return row;
		}
	}

	class RecordHolder_NEW {
		TextView divider, text, tax;
	}

	private void ccAvenueWork(String merchantID, String amount, String order_id) {
		String vAccessCode = "AVYD68EA58AD56DYDA";
		String vMerchantId = "119962"; // merchantID;
		String vCurrency = "INR";
		String vAmount = amount;
		Double vAmount_double = Double.valueOf(amount);
		// showToast(vAmount_double+"");
		if (!vAccessCode.equals("") && !vMerchantId.equals("")
				&& !vCurrency.equals("") && !vAmount.equals("")) {

			Intent intent = new Intent(this, WebViewActivity.class);
			intent.putExtra(AvenuesParams.ACCESS_CODE, vAccessCode);
			intent.putExtra(AvenuesParams.MERCHANT_ID, vMerchantId);
			intent.putExtra(AvenuesParams.ORDER_ID, order_id);
			intent.putExtra(AvenuesParams.CURRENCY, vCurrency);
			// intent.putExtra(AvenuesParams.AMOUNT,"1.00");
			intent.putExtra(AvenuesParams.AMOUNT, vAmount_double + "");

			intent.putExtra(AvenuesParams.REDIRECT_URL,
					"http://sleepwellproducts.com/ccav/ccavResponseHandler.php");
			intent.putExtra(AvenuesParams.CANCEL_URL,
					"http://sleepwellproducts.com/ccav/ccavResponseHandler.php");
			intent.putExtra(AvenuesParams.RSA_KEY_URL,
					"http://sleepwellproducts.com/ccav/GetRSA.php");

			intent.putExtra("p_customer_name", ed_full_name.getText()
					.toString());
			intent.putExtra("p_email", ed_email.getText().toString());
			intent.putExtra("p_contact_number", ed_contact_no.getText()
					.toString());

			intent.putExtra("p_city", strCity);
			intent.putExtra("p_state", strState);

			intent.putExtra("p_pin_code", ed_pin_code.getText().toString());
			intent.putExtra("p_address", ed_address.getText().toString());
			intent.putExtra("p_payment_mode", op_service_provider_name);

			startActivity(intent);

		} else {
			showToast("All parameters are mandatory.");
		}
	}

	private void hdfcWork(String merchantID, String amount, String order_id) {
		/*
		 * String vAccessCode = "AVBC68EA99CL63CBLC"; String vMerchantId =
		 * "122732"; // merchantID;
		 */
		String vAccessCode = "AVNC68EA99CL70CNLC";
		String vMerchantId = "122737";
		String vCurrency = "INR";
		String vAmount = amount;
		Double vAmount_double = Double.valueOf(amount);
		// showToast(vAmount_double+"");
		if (!vAccessCode.equals("") && !vMerchantId.equals("")
				&& !vCurrency.equals("") && !vAmount.equals("")) {

			Intent intent = new Intent(this, WebViewActivity.class);
			intent.putExtra(AvenuesParams.ACCESS_CODE, vAccessCode);
			intent.putExtra(AvenuesParams.MERCHANT_ID, vMerchantId);
			intent.putExtra(AvenuesParams.ORDER_ID, order_id);
			intent.putExtra(AvenuesParams.CURRENCY, vCurrency);
			// intent.putExtra(AvenuesParams.AMOUNT,"1.00");
			intent.putExtra(AvenuesParams.AMOUNT, vAmount_double + "");

			intent.putExtra(AvenuesParams.REDIRECT_URL,
					"http://54.254.131.151/hdfc/ccavResponseHandler1.php");
			intent.putExtra(AvenuesParams.CANCEL_URL,
					"http://54.254.131.151/hdfc/ccavResponseHandler1.php");
			intent.putExtra(AvenuesParams.RSA_KEY_URL,
					"http://54.254.131.151/hdfc/GetRSA.php");

			intent.putExtra("p_customer_name", ed_full_name.getText()
					.toString());
			intent.putExtra("p_email", ed_email.getText().toString());
			intent.putExtra("p_contact_number", ed_contact_no.getText()
					.toString());

			intent.putExtra("p_city", strCity);
			intent.putExtra("p_state", strState);

			intent.putExtra("p_pin_code", ed_pin_code.getText().toString());
			intent.putExtra("p_address", ed_address.getText().toString());
			intent.putExtra("p_payment_mode", op_service_provider_name);

			startActivity(intent);

		} else {
			showToast("All parameters are mandatory.");
		}
	}

	public void showToast(String msg) {
		Toast.makeText(this, "Toast: " + msg, Toast.LENGTH_SHORT).show();
	}

	private void showDialog() {

		String textHTML = "<font color=\"#FF0000\">"
				+ "\"Please do not refresh or close the window.\"" + "</font>";
		new AlertDialog.Builder(CheckoutActivity.this)
				.setTitle("DIGITAL PAYMENT")
				.setMessage(
						Html.fromHtml("Transaction saved. Redirecting for online payment."
								+ textHTML))
				.setPositiveButton(android.R.string.ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								// continue with delete
								String amount = payment_pay.getText()
										.toString();
								if (op_service_provider_name.equals("CCAVENUE")) {

									ccAvenueWork(op_merchant_id, amount,
											op_transaction_id);
								} else if (op_service_provider_name
										.equals("HDFC")) {// HDFC
									hdfcWork(op_merchant_id, amount,
											op_transaction_id);
								}
							}
						})
				.setNegativeButton(android.R.string.no,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								// do nothing

							}
						}).show();
	}

	private void showDialogCash(String msg) {
		new AlertDialog.Builder(CheckoutActivity.this)
				.setTitle("PAY BY CASH")
				.setMessage(msg)
				.setPositiveButton(android.R.string.ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								// continue with delete
								callWSPAYCASH();
							}
						})
				.setNegativeButton(android.R.string.no,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								// do nothing

							}
						}).show();
	}

	private void showDialogLink(String msg) {
		new AlertDialog.Builder(CheckoutActivity.this)
				.setTitle("PAY LINK TO CUSTOMER")
				.setMessage(msg)
				.setPositiveButton(android.R.string.ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								// continue with delete
								// callWSPAYCASH();
							}
						}).show();
	}

	private void showDialogValidate(String title) {
		new AlertDialog.Builder(CheckoutActivity.this)
				.setTitle("")
				.setMessage(title)
				.setPositiveButton(android.R.string.ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								// continue with delete
								// callWSPAYCASH();
							}
						}).show();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		ed_full_name.setText("");
		ed_email.setText("");
		ed_contact_no.setText("");
		ed_pin_code.setText("");
		ed_address.setText("");
		payment_pay.setText("");
		et_serial_no.setText("");
		callWSPAYCARD();
	}

	public static boolean isEmailValid(String email) {
		boolean isValid = false;

		String STREMAILADDREGEX = "^[_A-Za-z0-9]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,4})$"; // EMAIL
																															// REGEX
		Pattern pattern = Pattern.compile(STREMAILADDREGEX,
				Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(email);
		if (matcher.matches()) {
			isValid = true;
		}
		return isValid;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
	}
}
