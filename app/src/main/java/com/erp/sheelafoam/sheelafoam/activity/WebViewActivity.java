package com.erp.sheelafoam.sheelafoam.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.erp.sheelafoam.R;
import com.erp.sheelafoam.erp.utils.NetworkTask;
import com.erp.sheelafoam.erp.utils.NetworkTask.Result;
import com.erp.sheelafoam.sheelafoam.utility.ConnectionDetector;
import com.erp.sheelafoam.sheelafoam.utility.GlobalVariables;
import com.erp.sheelafoam.sheelafoamutility.AvenuesParams;
import com.erp.sheelafoam.sheelafoamutility.Constants;
import com.erp.sheelafoam.sheelafoamutility.RSAUtility;
import com.erp.sheelafoam.sheelafoamutility.ServiceHandler;
import com.erp.sheelafoam.sheelafoamutility.ServiceUtility;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EncodingUtils;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class WebViewActivity extends Activity implements Result,OnClickListener
{
	private ProgressDialog dialog;
	Intent mainIntent;
	String html, encVal;
	
	String UserID, DealerID, SerialNo;
	ConnectionDetector con;
	private SharedPreferences mPrefs;
	
	String p_customer_name,p_email,p_contact_number,p_city,p_state,p_pin_code,p_address,p_payment_mode;
	
	TextView textview_back, textview_user_id;
	ImageButton btn_logout;
	private String user_role = "";
	
	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.activity_webview);
		mainIntent = getIntent();

		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		mPrefs = getSharedPreferences("Location", Context.MODE_PRIVATE);
		UserID = mPrefs.getString("user_id", "");// DEALER_ID
		DealerID = mPrefs.getString("DEALER_ID", "");
		
		p_customer_name=mainIntent.getStringExtra("p_customer_name");
		p_email=mainIntent.getStringExtra("p_email");
		p_contact_number=mainIntent.getStringExtra("p_contact_number");
		p_city=mainIntent.getStringExtra("p_city");
		p_state=mainIntent.getStringExtra("p_state");
		p_pin_code=mainIntent.getStringExtra("p_pin_code");
		p_address=mainIntent.getStringExtra("p_address");
		p_payment_mode=mainIntent.getStringExtra("p_payment_mode");
		
		p_payment_mode="HDFC";
		initialize();
		// Calling async task to get display content
		new RenderView().execute();
	}
	
	
	void initialize() {

		mPrefs = getSharedPreferences("Location", Context.MODE_PRIVATE);

		textview_back = (TextView) findViewById(R.id.textview_back);
//		if (mPrefs.getString("op_user_type", null).equalsIgnoreCase("DISTRIBUTOR")) {
//			textview_back.setText("greatplus distributor app");
//		} else if (mPrefs.getString("op_user_type", null).equalsIgnoreCase("DEALER")) {
//			textview_back.setText("greatplus dealer app");
//		} else {
//			textview_back.setText("greatplus employee app");
//		}
//
//		textview_user_id = (TextView) findViewById(R.id.textview_user_id);
//		btn_logout = (ImageButton) findViewById(R.id.btn_logout);
//		btn_logout.setOnClickListener(this);
//		textview_user_id.setText(mPrefs.getString("user_id", ""));
		
//		SpannableString spannable_string = new SpannableString(textview_back.getText().toString().trim());
//		spannable_string.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.color_great)), 0, 5,
//				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//		spannable_string.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.color_plus)), 5, 10,
//				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//
//		textview_back.setText(spannable_string);
	}
	
	/**
	 * Async task class to get json by making HTTP call
	 * */
	private class RenderView extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Showing progress dialog
			dialog = new ProgressDialog(WebViewActivity.this);
			dialog.setMessage("Please wait...");
			dialog.setCancelable(false);
			dialog.show();
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			// Creating service handler class instance
			ServiceHandler sh = new ServiceHandler();
	
			// Making a request to url and getting response
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair(AvenuesParams.ACCESS_CODE, mainIntent.getStringExtra(AvenuesParams.ACCESS_CODE)));
			params.add(new BasicNameValuePair(AvenuesParams.ORDER_ID, mainIntent.getStringExtra(AvenuesParams.ORDER_ID)));
	
			String vResponse = sh.makeServiceCall(mainIntent.getStringExtra(AvenuesParams.RSA_KEY_URL), ServiceHandler.POST, params);
			System.out.println(vResponse);
			if(!ServiceUtility.chkNull(vResponse).equals("")
					&& ServiceUtility.chkNull(vResponse).toString().indexOf("ERROR")==-1){
				StringBuffer vEncVal = new StringBuffer("");
				vEncVal.append(ServiceUtility.addToPostParams(AvenuesParams.AMOUNT, mainIntent.getStringExtra(AvenuesParams.AMOUNT)));
				vEncVal.append(ServiceUtility.addToPostParams(AvenuesParams.CURRENCY, mainIntent.getStringExtra(AvenuesParams.CURRENCY)));
				encVal = RSAUtility.encrypt(vEncVal.substring(0,vEncVal.length()-1), vResponse);
			}
			
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			// Dismiss the progress dialog
			if (dialog.isShowing())
				dialog.dismiss();
			
			@SuppressWarnings("unused")
			class MyJavaScriptInterface
			{
				@JavascriptInterface
			    public void processHTML(String html)
			    {
			        // process the html as needed by the app
			    	String status = null;
			    	//Toast.makeText(getApplicationContext(), html, Toast.LENGTH_SHORT).show();
			    	if(html.indexOf("Failure")!=-1){
			    		status = "Transaction Declined!";
			    	}else if(html.indexOf("Success")!=-1){
			    		status = "Transaction Successful!";
			    		//finish();
			    	}else if(html.indexOf("Aborted")!=-1){
			    		status = "Transaction Cancelled!";
			    	}else{
			    		status = "Status Not Known!";
			    	}
			    	//Toast.makeText(getApplicationContext(), status, Toast.LENGTH_SHORT).show();
			    	/*Intent intent = new Intent(getApplicationContext(),StatusActivity.class);
					intent.putExtra("transStatus", status);
					startActivity(intent);*/
			    }
			}
			
			final WebView webview = (WebView) findViewById(R.id.web_view);
			webview.getSettings().setJavaScriptEnabled(true);
			webview.addJavascriptInterface(new MyJavaScriptInterface(), "HTMLOUT");
			webview.setWebViewClient(new WebViewClient(){
				@Override
	    	    public void onPageFinished(WebView view, String url) {
	    	        super.onPageFinished(webview, url);
	    	        if(p_payment_mode.equals("CCAVENUE")){
	    	        	 if(url.indexOf("/ccavResponseHandler.php")!=-1){
	 	    	        	webview.loadUrl("javascript:window.HTMLOUT.processHTML('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');");
	 	    	        }
	    	        }
					else if(p_payment_mode.equals("HDFC")){
						 if(url.indexOf("/ccavResponseHandler1.php")!=-1){
			    	        	webview.loadUrl("javascript:window.HTMLOUT.processHTML('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');");
			    	        }
					}
						
	    	       
	    	    }  

	    	    @Override
	    	    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
	    	        Toast.makeText(getApplicationContext(), "Oh no! " + description, Toast.LENGTH_SHORT).show();
	    	    }
			});
			
			/* An instance of this class will be registered as a JavaScript interface */
			StringBuffer params = new StringBuffer();
			params.append(ServiceUtility.addToPostParams(AvenuesParams.ACCESS_CODE,mainIntent.getStringExtra(AvenuesParams.ACCESS_CODE)));
			params.append(ServiceUtility.addToPostParams(AvenuesParams.MERCHANT_ID,mainIntent.getStringExtra(AvenuesParams.MERCHANT_ID)));
			params.append(ServiceUtility.addToPostParams(AvenuesParams.ORDER_ID,mainIntent.getStringExtra(AvenuesParams.ORDER_ID)));
			params.append(ServiceUtility.addToPostParams(AvenuesParams.REDIRECT_URL,mainIntent.getStringExtra(AvenuesParams.REDIRECT_URL)));
			params.append(ServiceUtility.addToPostParams(AvenuesParams.CANCEL_URL,mainIntent.getStringExtra(AvenuesParams.CANCEL_URL)));
			
			params.append(ServiceUtility.addToPostParams(AvenuesParams.BILLING_NAME,p_customer_name));
			params.append(ServiceUtility.addToPostParams(AvenuesParams.BILLING_ADDRESS,p_address));
			params.append(ServiceUtility.addToPostParams(AvenuesParams.BILLING_CITY,p_city));
			params.append(ServiceUtility.addToPostParams(AvenuesParams.BILLING_COUNTRY,"India"));
			params.append(ServiceUtility.addToPostParams(AvenuesParams.BILLING_EMAIL,p_email));
			params.append(ServiceUtility.addToPostParams(AvenuesParams.BILLING_STATE,p_state));
			params.append(ServiceUtility.addToPostParams(AvenuesParams.BILLING_TEL,p_contact_number));
			params.append(ServiceUtility.addToPostParams(AvenuesParams.BILLING_ZIP,p_pin_code));
			
			params.append(ServiceUtility.addToPostParams(AvenuesParams.DELIVERY_NAME,p_customer_name));
			params.append(ServiceUtility.addToPostParams(AvenuesParams.DELIVERY_ADDRESS,p_address));
			params.append(ServiceUtility.addToPostParams(AvenuesParams.DELIVERY_CITY,p_city));
			params.append(ServiceUtility.addToPostParams(AvenuesParams.DELIVERY_COUNTRY,"India"));
			params.append(ServiceUtility.addToPostParams(AvenuesParams.DELIVERY_STATE,p_state));
			params.append(ServiceUtility.addToPostParams(AvenuesParams.DELIVERY_TEL,p_contact_number));
			params.append(ServiceUtility.addToPostParams(AvenuesParams.DELIVERY_ZIP,p_pin_code));
			
			params.append(ServiceUtility.addToPostParams(AvenuesParams.MERCHANT_PARAM1,p_payment_mode));
			params.append(ServiceUtility.addToPostParams(AvenuesParams.MERCHANT_PARAM2,UserID));
			params.append(ServiceUtility.addToPostParams(AvenuesParams.MERCHANT_PARAM3,DealerID));
			params.append(ServiceUtility.addToPostParams(AvenuesParams.MERCHANT_PARAM4,mainIntent.getStringExtra(AvenuesParams.MERCHANT_ID)));
			params.append(ServiceUtility.addToPostParams(AvenuesParams.SUB_ACCOUNT_ID, DealerID));
			
			params.append(ServiceUtility.addToPostParams(AvenuesParams.ENC_VAL, URLEncoder.encode(encVal)));
			
			String vPostParams = params.substring(0,params.length()-1);
			try {
				if(p_payment_mode.equals("CCAVENUE"))
					webview.postUrl(Constants.TRANS_URL, EncodingUtils.getBytes(vPostParams, "UTF-8"));
				else if(p_payment_mode.equals("HDFC"))
					webview.postUrl(Constants.TRANS_URL_HDFC, EncodingUtils.getBytes(vPostParams, "UTF-8"));
					
				//showToast("Payment mode"+ p_payment_mode);
				
			} catch (Exception e) {
				showToast("Exception occured while opening webview.");
			}
		}
	}
	
	public void showToast(String msg) {
		Toast.makeText(this, "Toast: " + msg, Toast.LENGTH_LONG).show();
	}
	
	
	private void calWSSuccess() {
		try {
			JSONObject obj = new JSONObject();
			obj.put("p_greatplus_user_id", UserID);
			obj.put("p_dealer_id", DealerID);
			obj.put("p_transaction_id",mainIntent.getStringExtra(AvenuesParams.ORDER_ID));
			obj.put("p_customer_name",p_customer_name);
			obj.put("p_email",p_email);
			obj.put("p_contact_number",p_contact_number);
			obj.put("p_city",p_city);
			obj.put("p_state",p_state);
			obj.put("p_pin_code",p_pin_code);
			obj.put("p_address",p_address);
			obj.put("p_payment_amount",mainIntent.getStringExtra(AvenuesParams.AMOUNT));
			obj.put("p_payment_mode",p_payment_mode);
			

			String url = "http://125.19.46.252/ws/prc_close_current_transaction.php";

			NetworkTask networkTask = new NetworkTask(WebViewActivity.this, 3,
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
		
	}


	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.btn_logout:
			GlobalVariables.logout(this);
			break;
			default:
			break;
		}
		
	}
} 