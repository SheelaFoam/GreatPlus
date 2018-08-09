package com.erp.sheelafoam.sheelafoam.activity;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import com.erp.sheelafoam.R;
import com.erp.sheelafoam.erp.sheelafoam.RegisterGCMService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.erp.sheelafoam.sheelafoam.qrcode.ScanQrCodeActivity;
import com.erp.sheelafoam.sheelafoam.utility.AsyncTaskListner;
import com.erp.sheelafoam.sheelafoam.utility.GlobalVariables;
import com.erp.sheelafoam.sheelafoam.utility.HelperMethods;
import com.erp.sheelafoam.sheelafoam.utility.MyAsyncTask;
import com.erp.sheelafoam.sheelafoam.xperia.activity.ComplaintNew;
import com.splunk.mint.Mint;

import org.json.JSONObject;

public class SplashActivity extends Activity implements AsyncTaskListner {
	
	
	static final String TAG = "GCMDemo";
    GoogleCloudMessaging gcm;
    Context context;
    private int verCode;
    
	public static final String EXTRA_MESSAGE = "message";
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
   // please enter your sender id
    
   // project number on Google API console is Sender_id. 
    //String SENDER_ID = "158609295269";
    
    // sender id for the pack com.sheela.employeeportal.erp.sheelafoam.
    String SENDER_ID = "955458836415";
    
	boolean isLogin=false;
	String user_role="",user_name="";
	SharedPreferences mPrefs;
	Context ctx;
	String path= Environment.getExternalStorageDirectory().getAbsolutePath()+ "/Android/data/com.sheela.employeeportal.erp.sheelafoam";
	String DB_NAME="test_sheelafoam.db";
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		//Fabric.with(this, new Crashlytics());
		
        setContentView(R.layout.activity_splash);
        ctx=SplashActivity.this;
        mPrefs = getSharedPreferences("Location", Context.MODE_PRIVATE);
        isLogin=mPrefs.getBoolean("isLogin", false);
        user_role=mPrefs.getString("op_user_type", "");
        user_name=mPrefs.getString("op_user_name", "");
        GlobalVariables.getIPAddress(ctx);
        GlobalVariables.getDeviceId(ctx);
        Mint.initAndStartSession(SplashActivity.this, "155d0310");
        context = getApplicationContext();
		if(checkPlayServices()){
			gcm = GoogleCloudMessaging.getInstance(this);
			
			registerGCM();
		}
        getAppVersionAndVersionCode();
         
// METHOD 1    
         
         /****** Create Thread that will sleep for 5 seconds *************/  
       /* File direct=new File(path);
        
        if(!direct.exists())
        {

		       if(direct.mkdirs())   
		         {
		          //directory is created;
		         }
		       //to copy db from assets to app db
		      	try {
		      		
		      		
					copyDataBase();
		      		
		      		
		      		
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		   
        	
        }
        
        else
        {
        	
        }*/
        Thread background = new Thread() {
            public void run() {
                 
            	
                try {
                    // Thread will sleep for 5 seconds  
                	sleep(3*1000);
                	
                	/*if(isLogin)
                	{
	                    // After 5 seconds redirect to another intent
	                   Intent i=new Intent(getBaseContext(),HomeActivityNew.class);
	                    startActivity(i);
	                     finish();
	                     redirect(user_role);
	                     
	                  
                	}
                	else
                	{
                		
                		Intent i=new Intent(getBaseContext(),PreLoginNew.class);
 	                    startActivity(i);
 	                   
	                    finish();
                	}
                	*/
                	
                } catch (Exception e) {
                 
                }
            }
        };
         
        // start thread
        background.start();
        
        
        
        

    }
     
    @Override
    protected void onDestroy() {
         
        super.onDestroy();
         
    }
    
   
    
    
    private void registerGCM() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                try {
                    // call register and save registration ID to preference.
                    String regId = gcm.register(SENDER_ID);
                    
                    Log.e("regId New", regId);
                    
                    storeRegistrationId(context, regId);
                
                    return null;
                } catch (Exception e) {
                    // Error Handling
                    return null;
                }
            }
            
            @Override
            protected void onPostExecute(String result) {
            	
            	//Toast.makeText(SplashActivity.this, "version api is calling", Toast.LENGTH_LONG).show();
            	getAppVersion();
            	
            };
        }.execute();
    }    
    
     
    	
        private boolean checkPlayServices() {
          int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
          if (resultCode != ConnectionResult.SUCCESS) {
              if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                  GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                          PLAY_SERVICES_RESOLUTION_REQUEST).show();
              } else {
                  Log.e(TAG, "This device is not supported.");
                 
              }
              return false;
          }
          return true;
        }    
        
        
    
    private void storeRegistrationId(Context context, String regId) {
        //final SharedPreferences prefs = getGCMPreferences(context);
        
    	
    	GlobalVariables.DEVICE_ACCESS_TOKEN=regId;
        final SharedPreferences prefs= getSharedPreferences("Location", Context.MODE_PRIVATE);
        int appVersion = HelperMethods.getAppVersion(context);
        Log.e(TAG, "Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.commit();
        Intent intent=new Intent(getApplicationContext(),RegisterGCMService.class);
        startService(intent);
    }
    
    
    /*private SharedPreferences getGCMPreferences(Context context) {
    	  
    	  return getSharedPreferences(SplashActivity.class.getSimpleName(),
    	          Context.MODE_PRIVATE);
    	}*/
    
    public void redirect(String role)
	{
		 if(role.equals("DEALER") || role.equals("DISTRIBUTOR") || role.equals("EMPLOYEE"))
		 {
			 callHomeActivity();
		 }
		 else if(role.equals("CUSTOMER") || role.equals("AGENT"))
		 {
			callComplainActivity(user_name);
		 }
		 else if(role.equals("SAATHI"))
		 {
			callScanQrCodeActivity(user_name);
		 }
		
		 else
		 {
			 
		 }
	}

	private void callHomeActivity() {
		// TODO Auto-generated method stub
		
		 Intent i=new Intent(getBaseContext(),HomeActivityNew.class);
	        startActivity(i);
	         finish();
		
	}
	
	public void callComplainActivity(String user_name)
	{
		Intent i=new Intent(getBaseContext(),ComplaintNew.class);
 		//Intent i=new Intent(getBaseContext(),PreLogin.class);
		i.putExtra("op_user_name", user_name);
          startActivity(i);
         //Remove activity
         finish();
	}
    
	
	public void callScanQrCodeActivity(String user_name)
	{
		//Toast.makeText(getApplicationContext(), user_name, 500).show();
		Intent i=new Intent(getBaseContext(),ScanQrCodeActivity.class);
 		//Intent i=new Intent(getBaseContext(),PreLogin.class);
		i.putExtra("op_user_name", user_name);
          startActivity(i);
         //Remove activity
         finish();
	}
	
	
	private void getAppVersionAndVersionCode(){
		
		PackageInfo pInfo = null;
		try {
			pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String version = pInfo.versionName;
		 verCode= pInfo.versionCode;
		GlobalVariables.APP_VERSION_CODE=verCode;
		
		Log.e("version code", ""+verCode);
	}
	
	private void getAppVersion(){
		
		try{
			
			JSONObject jObject_request=new JSONObject();
			jObject_request.put("request", "checkAppVersion");
			jObject_request.put("p_app_version", verCode);
			jObject_request.put("deviceModel", GlobalVariables.getDeviceModel());
			jObject_request.put("deviceOs", GlobalVariables.getDeviceOs());
			jObject_request.put("accessType","A");
			jObject_request.put("mobile1",user_name);
			jObject_request.put("mobile2","");
			
			jObject_request.put("p_device_token", GlobalVariables.DEVICE_ACCESS_TOKEN);
			
			Log.e("##Request##", jObject_request.toString());
			
			new MyAsyncTask(SplashActivity.this, jObject_request).execute();
			
			
		}catch(Exception e){
			
		}
		
	}

	 private  void defaultTwoButtonDialog_delete(String msg)
	  {
		  AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);
		  builder.setMessage(msg)
		         .setCancelable(false)
		         .setPositiveButton("YES", new DialogInterface.OnClickListener() {
		             public void onClick(DialogInterface dialog, int id) {
		                  //do things
		            	 //https://play.google.com/store/apps/details?id=com.erp.sheelafoam&hl=en
		            	 
		            	 Intent viewIntent =
		                         new Intent("android.intent.action.VIEW",
		                         Uri.parse("https://play.google.com/store/apps/details?id=com.sheela.employeeportal.erp.sheelafoam&hl=en"));
		                         startActivity(viewIntent);
		            	 
		            	 dialog.dismiss();
		             }
		         });
		  builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
				if(isLogin)
            	{
                    // After 5 seconds redirect to another intent
                  /* Intent i=new Intent(getBaseContext(),HomeActivityNew.class);
                    startActivity(i);
                     finish();*/
                     redirect(user_role);
                     
                  
            	}
            	else
            	{
            		
            		Intent i=new Intent(getBaseContext(),PreLoginNew.class);
	                    startActivity(i);
	                   
                    finish();
            	}
            	
				
				dialog.dismiss();
			}
		});
		  AlertDialog alert = builder.create();
		  alert.show();
	  }
	
	
	@Override
	public void onTaskComplete(String result) {
		// TODO Auto-generated method stub
		
		Log.e("Response", result);
		try{
			
			JSONObject jObjResponse=new JSONObject(result);
			if(jObjResponse.getString("status").equals("200")){
			
				JSONObject jObjData=jObjResponse.getJSONObject("data");
				if(!jObjData.getString("op_update_yn").equals("1")){
					
					String msg=jObjData.getString("op_message");
					
					defaultTwoButtonDialog_delete(msg);
					
				}else{
					
					if(isLogin)
	            	{
	                    // After 5 seconds redirect to another intent
	                  /* Intent i=new Intent(getBaseContext(),HomeActivityNew.class);
	                    startActivity(i);
	                     finish();*/
	                     redirect(user_role);
	                     
	                  
	            	}
	            	else
	            	{
	            		
	            		Intent i=new Intent(getBaseContext(),PreLoginNew.class);
		                    startActivity(i);
		                   
	                    finish();
	            	}
	            	
				}
				
			}else{
				
			}
			
		}catch(Exception e){
			
		}
		
		
		
	}
	
	
    
    
	
}
