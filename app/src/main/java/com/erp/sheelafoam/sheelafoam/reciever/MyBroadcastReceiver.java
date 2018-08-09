
/**
 * Short description for file: Used for start and broadcast background service response when after device booting.

 *
 * Used Android  minSdkVersion 14
 * Used Android  targetSdkVersion 19 

 * @package    com.example.heartbeatnew
 * @author     Vinay Kumar Gupta
 * Class name  MyBroadcastReceiver
 * Short description for class:  Used for start and broadcast background service response when after device booting.
 * Creation Date  15-01-2015
 * Last Modified  02-02-2015
 * Modified By    Vinay Kumar Gupta

 */


package com.erp.sheelafoam.sheelafoam.reciever;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;

public class MyBroadcastReceiver extends BroadcastReceiver {
	  Handler handler = new Handler();
	  
	  Context context;
	
	  /******Override method of BroadcastReceiver *******/
	  
    @Override
    public void onReceive(Context context, Intent intent) {
        /*Intent startServiceIntent = new Intent(context, MyService.class);
        context.startService(startServiceIntent);*/
    	
    	/*this.context=context;
    	
    	Intent i=new Intent(context,TestService.class);
		
		context.startService(i);*/
    
	

    }
    
   
}
