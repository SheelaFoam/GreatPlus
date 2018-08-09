package com.erp.sheelafoam.sheelafoam.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;

import com.erp.sheelafoam.R;
import com.erp.sheelafoam.sheelafoam.fragments.OrderApprovalFragment;
import com.erp.sheelafoam.sheelafoam.fragments.ProductOrderView;
import com.erp.sheelafoam.sheelafoam.utility.GlobalVariables;

public class NotificationActivity extends ActionBarActivity {
	
	boolean isLogin=false;
	SharedPreferences mPrefs;
	Context ctx;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);
		
		 ctx=NotificationActivity.this;
	        mPrefs = getSharedPreferences("Location", Context.MODE_PRIVATE);
	        isLogin=mPrefs.getBoolean("isLogin", false);
	        if(isLogin)
	        {
	        	if(mPrefs.getString("op_user_type", "").equals("EMPLOYEE") || mPrefs.getString("op_user_type", "").equals("DEALER"))
	        	{
	        		
	        		ProductOrderView fragment=new ProductOrderView();
	        		addFragment(fragment);
	        		finish();
	        	}
	        	else
	        	{
	        		OrderApprovalFragment fragment=new OrderApprovalFragment();
	        		addFragment(fragment);
	        		finish();
	        		
	        	}
	        }
	        else
	        {
	        	GlobalVariables.clearAllPendingActivityOnNotification(this);
	        }
	        
	        
	        
	}
	
	
	 /*user define method addFragments start*/
	 public void addFragment(Fragment fragment)
	    {
		 
		   getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
		  
			   getSupportFragmentManager().beginTransaction()   
		       // .replace(R.id.flayout, fragment)
			   	.add(R.id.flayout, fragment)
		        .addToBackStack(null)
		        .commit();
			  		       
	    }

}
