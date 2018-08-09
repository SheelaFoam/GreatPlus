package com.erp.sheelafoam.sheelafoam.utility;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

public class MyCustomAsyncTask extends AsyncTask<String, Void, String> {

	private Activity activity;
	private ProgressDialog dialog;
	private AsyncTaskListner callback;
	JSONObject jobject;
	JSONObject jsonObject_response;
	ConnectionDetector con;
	SharedPreferences mPrefs;
	String url="";
	
	public MyCustomAsyncTask(Activity act) {
		this.activity = act;
		this.callback = (AsyncTaskListner)act;
		this.jobject=jobject;
		this.url="";
		
		con=new ConnectionDetector(activity);
		createDialog();	
		mPrefs = act.getSharedPreferences("Location", Context.MODE_PRIVATE);
	}
	
	
	public MyCustomAsyncTask(Activity act, Fragment fragment) {
		this.activity = act;
		this.callback = (AsyncTaskListner)fragment;
		this.jobject=jobject;
		this.url="";
		
		con=new ConnectionDetector(activity);
		
		createDialog();			// Creating a dialog instance
		
		
		mPrefs = act.getSharedPreferences("Location", Context.MODE_PRIVATE);
	}
	
	
	
	public MyCustomAsyncTask(Activity act, JSONObject jobject) {
		this.activity = act;
		this.callback = (AsyncTaskListner)act;
		this.jobject=jobject;
		this.url="";
		
		mPrefs = act.getSharedPreferences("Location", Context.MODE_PRIVATE);
	}
	
	
	
	public MyCustomAsyncTask(Activity act, Fragment fragment, JSONObject jobject) {
		this.activity = act;
		this.callback = (AsyncTaskListner)fragment;
		this.jobject=jobject;
		this.url="";
		
		con=new ConnectionDetector(activity);
		
		 mPrefs = act.getSharedPreferences("Location", Context.MODE_PRIVATE);
	}
	
	
	public MyCustomAsyncTask(Activity act, Fragment fragment, JSONObject jobject, String url) {
		this.activity = act;
		this.callback = (AsyncTaskListner)fragment;
		this.jobject=jobject;
		this.url=url;
		con=new ConnectionDetector(activity);
		mPrefs = act.getSharedPreferences("Location", Context.MODE_PRIVATE);
		Log.e("page name", fragment.getClass().getSimpleName());
		Log.e("url", url);
		Log.e("data", ""+jobject);
	}
	
	
	
	public MyCustomAsyncTask(Activity act, JSONObject jobject, String url) {
		this.activity = act;
		this.callback = (AsyncTaskListner)act;
		this.jobject=jobject;
		this.url=url;
		
		con=new ConnectionDetector(activity);
		
		mPrefs = act.getSharedPreferences("Location", Context.MODE_PRIVATE);
	}
	
	
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();

		
	}

	@Override
	protected String doInBackground(String... params) {
		MyHttpClient http=new MyHttpClient(activity);

		if(url.equals(""))
		{
		 return http.GetData(jobject);
		}
		else
		{
			return http.GetData(jobject,url);
		}
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		
		
		//call method from here....
		
		System.out.println("product list"+result);
		if(mPrefs.getInt("CONNECTION_TIMEOUT", 0)==1)
		{
			hideDialog(activity);
			
			Toast.makeText(activity, "Connection timeout", Toast.LENGTH_LONG).show();
		}
		
		else
		{
			
			try
			{

					callback.onTaskComplete(result);
					
					hideDialog(activity);
				
			}
			catch(Exception e)
			{
				
			}
			
			
		}
		
	}
	
	
	
	public void createDialog( )
	{
		//this.activity = activity;
	
		Log.e("MyCustomAsyncTask", "showDialog called");
		
		dialog = new ProgressDialog(activity);
		dialog.setMessage("Loading...");
		dialog.setCancelable(false);
		
	
	}
	
	
	
	
	public void showDialog(Activity act)
	{
		//this.activity = activity;
	
		Log.e("MyCustomAsyncTask", "showDialog called");
		
		if(con.isConnectingToInternet())
		{
		
		dialog.show();
		}
		else
			Toast.makeText(activity, "Check your internet connection", Toast.LENGTH_SHORT);
	
	}
	
	public void hideDialog(Activity act){
		
		//this.activity = activity;
		
		Log.e("MyCustomAsyncTask", "hideDialog called");
		
		if (null != dialog && dialog.isShowing()) 
		{
			dialog.dismiss();
		}
		
	}
	
	
	}
