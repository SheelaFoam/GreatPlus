package com.erp.sheelafoam.sheelafoam.activity;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.erp.sheelafoam.R;
import com.erp.sheelafoam.sheelafoam.qrcode.ScanQrCodeActivity;
import com.erp.sheelafoam.sheelafoam.utility.AsyncTaskListner;
import com.erp.sheelafoam.sheelafoam.utility.ConnectionDetector;
import com.erp.sheelafoam.sheelafoam.utility.GlobalVariables;
import com.erp.sheelafoam.sheelafoam.utility.MyAsyncTask;
import com.splunk.mint.Mint;

import org.json.JSONObject;


public class LoginActivity extends Activity implements OnClickListener,AsyncTaskListner {
	
	
	int action=1;
	 private BroadcastReceiver reMyreceive;
	 private IntentFilter filter;
	 
	 Handler handler_location = new Handler();
	 
	
	 
	 double lat,lng;
	 String time="";
	
	

	Context ctx;
	
	Button btn_ok;
	EditText edittext_user_id,edittext_password;
	TextView showhideTextview;
	ConnectionDetector con;
	int width;
	int height;
	
	JSONObject jObject_response;
	
	String user_id="",password="",auth_token="",p_role_name="";
	String sim_two;
	
	/*String variables for hold Api response data*/
	String op_authorised_yn="",op_message="",op_user_name="",op_user_type="",op_menu_id="",op_dealer_id="",op_dealer_name="";
	String op_kpi_name_1="",op_kpi_value_1="",op_kpi_growth_yn_1="",op_kpi_name_2="";
	String op_kpi_value_2="",op_kpi_growth_yn_2="",op_kpi_name_3="",op_kpi_value_3="";
	String op_kpi_growth_yn_3="",op_kpi_tag_line_3="",op_kpi_name_4="",op_kpi_value_4="";
	String op_kpi_growth_yn_4="",op_kpi_name_5="",op_kpi_value_5="",op_kpi_growth_yn_5="";
	String op_kpi_name_6="",op_kpi_value_6="",op_kpi_growth_yn_6="",op_kpi_tag_line_6="";
	String op_notice_date1="",op_notice1="",op_notice_date2="",op_notice2="";
	String op_photo="",op_email_id="",op_dealer_category="",user_role="";
	private SharedPreferences mPrefs;
	public static Intent i;
	
	/**
	 * Device registration id return by GCM at splash screen.
	 * **/
	String reg_id;
	
	/**
	 * device id.
	 * **/
	
	String device_id;
	int isShowing;
	
/*	 public class Myreceiver extends BroadcastReceiver{
	   	 
	        @Override
	        public void onReceive(Context context, Intent intent) {
	            // TODO Auto-generated method stub
	             
	            Log.e("test", "MyReceiver: broadcast received");
	            
	             lat=intent.getDoubleExtra("Latitude",0);
	             lng=intent.getDoubleExtra("Longitude",0);
	             time=intent.getStringExtra("Time");
	             
	             Log.e("lat in receiver",""+lat);
	             Log.e("lng in receiver",""+lng);
	             Log.e("time in receiver",""+time);
	            
	        }
	         
	    }*/
	
	 
	  private ViewTreeObserver.OnGlobalLayoutListener keyboardLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
	        @Override
	        public void onGlobalLayout() {
	            int heightDiff = rootLayout.getRootView().getHeight() - rootLayout.getHeight();
	            int contentViewTop = getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();

	         
	            if(heightDiff <= contentViewTop){
	                onHideKeyboard();

	               
	            } else {
	                int keyboardHeight = heightDiff - contentViewTop;
	                onShowKeyboard(keyboardHeight);

	              
	            }
	        }
	    };
	    
	    

	    private boolean keyboardListenersAttached = false;
	    private ScrollView rootLayout;
	    private TextView headerTextView,footerTextView;

	    protected void onShowKeyboard(int keyboardHeight) {
	    
	    		rootLayout.smoothScrollTo(0, 0);
	    
	    	//Toast.makeText(getApplicationContext(), "Show", Toast.LENGTH_LONG).show();
	    	
	    }
	    protected void onHideKeyboard() {
	    
	    	rootLayout.smoothScrollTo(0, 0);
	    	
	    	//Toast.makeText(getApplicationContext(), "Hide", Toast.LENGTH_LONG).show();
	    	
	    
	    }

	    protected void attachKeyboardListeners() {
	        if (keyboardListenersAttached) {
	            return;
	        }

	         rootLayout = (ScrollView) findViewById(R.id.scrollView1);
	         
	         headerTextView = (TextView) findViewById(R.id.headerTextView);
	         footerTextView = (TextView) findViewById(R.id.footerTextView);
	         
	        
	        rootLayout.getViewTreeObserver().addOnGlobalLayoutListener(keyboardLayoutListener);

	        keyboardListenersAttached = true;
	    }

	    @Override
	    protected void onDestroy() {
	        super.onDestroy();

	        if (keyboardListenersAttached) {
	            rootLayout.getViewTreeObserver().removeGlobalOnLayoutListener(keyboardLayoutListener);
	        }
	    }
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login1);
		con=new ConnectionDetector(getApplicationContext());
		mPrefs = getSharedPreferences("Location", Context.MODE_PRIVATE);
		ctx=LoginActivity.this;
		GlobalVariables.getIPAddress(ctx);
		Mint.initAndStartSession(LoginActivity.this, "155d0310");
		reg_id=mPrefs.getString(SplashActivity.PROPERTY_REG_ID, "");
		user_role=getIntent().getStringExtra("op_user_type");
		user_id=getIntent().getStringExtra("user_name");
		
		if(reg_id.equals(""))
		{
			reg_id= GlobalVariables.DEVICE_ACCESS_TOKEN;
		}
		device_id=mPrefs.getString("DEVICE_ID", "");
		
		//Toast.makeText(getApplicationContext(), " Registration id "+reg_id+" "+" Device id "+device_id, Toast.LENGTH_LONG).show();
		//Toast.makeText(getApplicationContext(), " Device id "+device_id, Toast.LENGTH_LONG).show();
		
		//filter=new IntentFilter(TestService.BROADCAST_ACTION);
		// initialize xml controls
		
		initialization();
		
	   // add onclick Listner on controls
		
	   setOnclickListner();
	   attachKeyboardListeners();
	
	   
	//   sim_two=GlobalVariables.isDualSIM(ctx);
//	   if(sim_two!=null)
//	   {
//		   Log.e("dual sim", "dual sim");
//	   }
//	   else
//	   {
//		   Log.e("not dual sim", "not dual sim");
//	   }

	   
	}

	
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		
		Log.e("onPause", "onPause is working");
		
		
	}
	
	/*fragment lifecycle method onPause end*/
	
	
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
			
		
		
	}

	
	private void setOnclickListner() {
		// TODO Auto-generated method stub
		
		
		btn_ok.setOnClickListener(this);
		showhideTextview.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		switch (v.getId()) {
		case R.id.btn_ok:
			GlobalVariables.hideSoftKeyboard(this);
			
			
			if(con.isConnectingToInternet())
			{
				
				getInputData();
				
				if(password.length()>0)
				{
					
					
					   setJsonRequest();
						
					
				}
				else
				{

					
					Toast.makeText(getApplicationContext(), getResources().getString(R.string.default_mandotry_fields_msg), Toast.LENGTH_LONG).show();
				}
				
			 
			}
			else
			{
	            GlobalVariables.defaultOneButtonDialog(this, getResources().getString(R.string.default_network_alert_msg));
			}
			
			
			
			break;
			
		case R.id.showhideTextview:
			
			if(showhideTextview.getText().toString().equals("Show Password"))
			{
				edittext_password.setInputType(InputType.TYPE_CLASS_TEXT);
				showhideTextview.setText("Hide Password");
			}
			else
			{
				edittext_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
				showhideTextview.setText("Show Password");
			}
			break;
	

		default:
			break;
		}
		
	}
	
	
	@Override
	public void onTaskComplete(String result) {
		// TODO Auto-generated method stub
		
		Log.e("After Password ", result);
		
		if(action==1)
		{
			try
			{
			  jObject_response=new JSONObject(result);
			  try
			  {
				  String status=jObject_response.getString("status");
				  if(status.equals("200"))
				  {
					 JSONObject data= jObject_response.getJSONObject("data");
					 String op_authorised_yn=data.getString("op_authorised_yn");
					 if(op_authorised_yn.equals("1"))
					 {
					 
							
							 op_message=data.getString("op_message");
							 op_user_name=data.getString("op_user_name");
							 op_user_type=data.getString("op_user_type"); 
							 op_menu_id=data.getString("op_menu_id");
							 op_dealer_id=data.getString("op_dealer_id");
							 op_dealer_name=data.getString("op_dealer_name");
							
							    
							 
							 auth_token=data.getString("token");
							 op_dealer_category=data.getString("op_dealer_category");
							 GlobalVariables.DEALER_CATEGORY=op_dealer_category;
							 GlobalVariables.MASTER_DEALER_CATEGORY=op_dealer_category;
							
							 Log.e("Auth Token", auth_token);
								
								Editor editor = mPrefs.edit();
								editor.putBoolean("isLogin", true);
								//editor.putString("UserID", userId);
								editor.putString("AUTH_TOKEN", auth_token);
								editor.putString("DEALER_CATEGORY", op_dealer_category);
								editor.putString("DEALER_NAME", op_dealer_name);
								editor.putString("DEALER_ID", op_dealer_id);
								editor.commit();
								
								setDataInSharePrefrenshed();
								
								
								 
								 setProfileJsonRequest();
								
//								  Intent i=new Intent(getBaseContext(),HomeActivity.class);
//				                    startActivity(i);
//				                     finish();
					 }
					 else
					 {
						 op_message=data.getString("op_message");
						 Toast.makeText(this, op_message, Toast.LENGTH_LONG).show();
						 
						 
					 }
				  }
				  else
				  {
					 /* JSONObject data= jObject_response.getJSONObject("data");
					  String msg=data.getString("message");*/
					  
					  
					  jObject_response=new JSONObject(result);
					  String msg=jObject_response.getString("msg");
					  Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
					  
				  }
			  }
			  catch(Exception e)
			  {
				  
			  }

			  
			}
			catch(Exception e)
			{
				
			}
		}
		else
		{
			try
			{
				jObject_response=new JSONObject(result);
				String status=jObject_response.getString("status");
				if(status.equals("200"))
				{
					JSONObject data=jObject_response.getJSONObject("data");
					op_photo=data.getString("op_photo");
					op_email_id=data.getString("op_email_id");
					
					
						savePhotoInSharedPrefrence();
						
//						callService();
//					
//					
//					 Intent i=new Intent(getBaseContext(),HomeActivity.class);
//		             startActivity(i);
//		              finish();
				}
				
				else{
					JSONObject data=jObject_response.getJSONObject("data");
					 op_message=data.getString("msg");
					 Toast.makeText(this, op_message, Toast.LENGTH_LONG).show();
					
				}
			}
			catch(Exception e)
			{
				
			}
			
			
			
		}
		
		
		
	
		
	}
	
	
	/************************User Define methods**********************************/
	/*user define method initialization for initialize xml controls start*/
	
	public void initialization()
	{
		
		btn_ok=(Button)findViewById(R.id.btn_ok);
		edittext_user_id=(EditText)findViewById(R.id.edittext_user_id);
		edittext_password=(EditText)findViewById(R.id.edittext_password);
		showhideTextview=(TextView)findViewById(R.id.showhideTextview);
	}
	
	/*user define method initialization for initialize xml controls end*/
	
	
	/*user define method getInputData for get input values start*/
	
	public void getInputData()
	{
		//user_id=edittext_user_id.getText().toString().trim();
		password=edittext_password.getText().toString().trim();
	}
	
	/*user define method getInputData for get input values end*/
	
	
	/*user define method setJsonRequest for create  hit api start*/
	public void setJsonRequest()
	{
		action=1;
		try
		{
			JSONObject jObject_request=new JSONObject();
			jObject_request.put("request","userAuthenticationV1");
			jObject_request.put("userId",user_id);
			
			// disable by Vinay on 07-08-2016
			
			//jObject_request.put("password",password);
			
			jObject_request.put("password",password);
			
			jObject_request.put("deviceModel", GlobalVariables.getDeviceModel());
			jObject_request.put("deviceOs", GlobalVariables.getDeviceOs());
			jObject_request.put("accessType","A");
			
			// disable by Vinay on 07-08-2016
			
			/*jObject_request.put("mobile1",GlobalVariables.getPhoneNumber_one(this));
			jObject_request.put("mobile2","");*/
			
			jObject_request.put("p_mobile_no_1", GlobalVariables.getPhoneNumber_one(this));
			jObject_request.put("p_mobile_no_2","");
			
			jObject_request.put("p_role_name",user_role);
			
			jObject_request.put("p_device_token",reg_id);
			jObject_request.put("p_app_version", GlobalVariables.APP_VERSION_CODE);
			
			Log.e("Password PUT Request", jObject_request.toString());
			new MyAsyncTask(this, jObject_request).execute();
		}
		catch(Exception e)
		{
			
		}
	}
	
	/*user define method setJsonRequest for create  hit api end*/
	
	
	
	/*user define method setJsonRequest for create  hit api start*/
	public void setProfileJsonRequest()
	{
		action=2;
		try
		{
			JSONObject jObject_request=new JSONObject();
			jObject_request.put("request","getProfile");

			
			Log.e("Profile PUT Request ", jObject_request.toString());
			new MyAsyncTask(this,jObject_request).execute();
		}
		catch(Exception e)
		{
			
		}
		
	}
	
	
	/*user define method setDataInSharePrefrenshed for save dashboard data in global variable start */
	
	public void setDataInSharePrefrenshed()
	{
		Editor editor = mPrefs.edit();
		
		editor.putString("op_authorised_yn", op_authorised_yn);
		editor.putString("op_message", op_message);
		editor.putString("op_user_name", op_user_name);
		editor.putString("op_user_type", op_user_type);
		editor.putString("op_menu_id", op_menu_id);
		
		
		editor.putString("user_id", user_id);
		
		
		editor.commit();
	}
	
	
	public void savePhotoInSharedPrefrence()  
	{
		//Toast.makeText(getApplicationContext(), mPrefs.getString("op_user_type", "").toString() + " Going To Home", 500).show();
		Editor editor = mPrefs.edit();
		if(!op_photo.equals("null"))
		{
			editor.putString("PROFILE_PHOTO", op_photo);
			
		}
		
		editor.putString("EMAIL_ID", op_email_id);
		editor.commit();
		
		//callService();
		
		
		 Intent i;
		  if(mPrefs.getString("op_user_type", "").toString().trim().equalsIgnoreCase("SAATHI"))
		  {
			  i=new Intent(getBaseContext(),ScanQrCodeActivity.class);
		  }
		  else
		  {
			   i=new Intent(getBaseContext(),HomeActivityNew.class);
		  }		
		
         startActivity(i);
         finish();
	}
	
	 
	
	
	 public void saveServiceObjectInSharedPreference()
	 {
		 Editor prefsEditor = mPrefs.edit();
	        Gson gson = new Gson();
	        String json = gson.toJson(i);
	        prefsEditor.putString("MY_SERVICE", json);
	        prefsEditor.commit();
	 }
	
}
