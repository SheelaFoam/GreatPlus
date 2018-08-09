package com.erp.sheelafoam.erp.sheelafoam;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.erp.sheelafoam.sheelafoam.activity.SplashActivity;
import com.erp.sheelafoam.sheelafoam.utility.ApiList;
import com.erp.sheelafoam.sheelafoam.utility.HelperMethods;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class RegisterGCMService extends IntentService {
	String data;
	
	String response_data;
	
	
	String url;
	
	

	public static final String EXTRA_MESSAGE = "message";
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
   
    static final String TAG = "GCMDemo";
    GoogleCloudMessaging gcm;
    String regid;
    Context context;
    String abc;
    
    String API_NAME;
	
	

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Log.d("On Create", "create Service");
	}
	
	
	
	
	public RegisterGCMService() {
	    super("name");
	  
	  }

	  // Will be called asynchronously be Android
	  @Override
	  protected void onHandleIntent(Intent intent) {
		  
		  
		  
	//	  UpDatekart(0);
		  
		 
	  }


/*	
	  private void UpDatekart(int i) {
		// TODO Auto-generated method stub
		if(i==0)
			  API_NAME=ApiList.API_GCM;
		else
			  API_NAME=ApiList.API_APP_VERSION;
			
			 data= "apiName="+API_NAME;
			
			String costomerId="";
			String device_id="";
			String key="";
			int zipcode = 201301;
			
			device_id = Secure.getString(getApplicationContext().getContentResolver(),
			            Secure.ANDROID_ID);
				
			key= 	getSharedPreferences(SharePref.PREF_NAME, Context.MODE_PRIVATE).getString(SharePref.SECRET_KEY, secretKey);
			if(key.length()==0 ||costomerId.length()==0)
					key=secretKey;
		
				
//				zipcode=getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE).getInt(SharePref.PIN_CODE,201301 );
			 
		 // http://quick2kart.com/api.php?apiName=push&secretKey=61109bcebdbe69255a3b395de4cbe552&uuid=53F86AA4-5C70-4CC3-B504-ED62A51396FC&registrationId=45344fa29343ee16b2ec94f8889d08a5031a6b740e6ebde787424ebe8a23d8b2&deviceName=ios
		  final SharedPreferences prefs = getGCMPreferences(getApplicationContext());
		  String registrationId = prefs.getString(PROPERTY_REG_ID, "");
		  
		data=data+"&uuid="+device_id+"&secretKey="+secretKey+"&zipcode="+zipcode+"&deviceName=android"+"&registrationId="+registrationId;
	//	Log.e("data", ApiList.HOST_NAME+data);
		
		new PerformBackgroundtask().execute();
			
	}
	*/

private SharedPreferences getGCMPreferences(Context context) {
  
  return getSharedPreferences(SplashActivity.class.getSimpleName(),
          Context.MODE_PRIVATE);
}

	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
		
		
	}

	@Override
	public int onStartCommand(final Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		
		return super.onStartCommand(intent, flags, startId);
	}


	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.d("Destroy ", "Destroy Services");
	}
	
	
	
	

		
	
	
class PerformBackgroundtask extends AsyncTask<Void, Void, Void> {
		
	
		protected void onPreExecute() {
			 try {
			
				   
				super.onPreExecute();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		}

		@Override
		protected Void doInBackground(Void... params) {
			
			
			getData(data, ApiList.HOST_NAME);
			

			return null;
		}

	
		@Override
		protected void onPostExecute(Void result1)  {
			
			if(API_NAME.equals(ApiList.API_APP_VERSION)){
				
				
				
						try {
							JSONObject jObject=new JSONObject(response_data);
							
							
							   String status=jObject.getString("status");
							
								
								  if(status.equalsIgnoreCase("1"))
								    {	
									  String version=jObject.getString("version");
									  Log.e("appsmnjks", version);
										HelperMethods.setVersionCode(getApplicationContext(), Integer.parseInt(version));
										
								    }
						} catch (NumberFormatException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					
				}
			else{
		
			try {
				Log.e("response GCM", response_data);
//				UpDatekart(1);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			}
		}
				
				
				

			
 			 
 			 
				
	}
	
private void checkAndroidVersion() {
	// TODO Auto-generated method stub

	  

		  data= "apiName="+ ApiList.API_APP_VERSION;
		  API_NAME= ApiList.API_APP_VERSION;
		new PerformBackgroundtask();
	
	
}
	

	private void getData(String data , String url) {
		// TODO Auto-generated method stub
		
		try {
			
			HttpClient client = new DefaultHttpClient();
			int timeout = 30; // seconds
			HttpParams httpParams = client.getParams();
			HttpConnectionParams.setConnectionTimeout(httpParams, timeout * 1000); // http.connection.timeout
			HttpPost post = new HttpPost(url);
			//post.addHeader(Constants.Authorization_KEY, Constants.Authorization_VALUE);

			
				StringEntity se = new StringEntity(data);
				//se.setContentEncoding("UTF-8");
				se.setContentType("application/x-www-form-urlencoded");
				post.setEntity(se);
			
			HttpResponse response;
			response = client.execute(post);
		
		 //  Log.e("response",response.getStatusLine().toString());
			response_data=convertStreamToString(response.getEntity().getContent());
			
			Log.e("result", response_data);
		} 
		
		catch (ConnectTimeoutException e) {
	        //Here Connection TimeOut excepion   
		
	      Log.e("time out", "time out");
	   }
		
		catch (UnsupportedEncodingException e) {
		
		} 
		catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
			e.printStackTrace();
		// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}





    public String convertStreamToString(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}


    

}
