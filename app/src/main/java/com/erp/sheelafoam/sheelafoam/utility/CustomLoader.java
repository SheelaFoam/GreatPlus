package com.erp.sheelafoam.sheelafoam.utility;

import android.app.Activity;
import android.app.ProgressDialog;
import android.util.Log;

public class CustomLoader {

	ProgressDialog dialog;
	Activity activity;
	
	public CustomLoader(Activity act)
	{
           activity=act;
		   dialog = new ProgressDialog(act);
		   dialog.setMessage("Loading..");
		   dialog.setCancelable(false);
		
	}
	
	public void startLoader()
	{	
		dialog.show();
		/*activity.runOnUiThread(new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			dialog.show();
			
			Log.e("show loader is calling", "show loader is calling");
		  
		}
	});*/
		
	}
	
	public void closeLoader()
	{
		dialog.hide();
		Log.e("hide loader is calling", "hide loader is calling");
	}
	
	public boolean isShowing()
	{
		return dialog.isShowing();
		
	}

}
