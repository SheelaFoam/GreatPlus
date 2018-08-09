package com.erp.sheelafoam.sheelafoam.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.erp.sheelafoam.R;
import com.erp.sheelafoam.sheelafoam.utility.AsyncTaskListner;
import com.erp.sheelafoam.sheelafoam.utility.GlobalVariables;
import com.erp.sheelafoam.sheelafoam.utility.MyAsyncTask;

import org.json.JSONObject;

public class ChangeRoleActivity extends Activity implements OnClickListener,AsyncTaskListner {
	
	TextView textview_skip,textview_saathi,textview_customer,textview_dealer,textview_employee,textview_agent,textview_distributor;
	EditText edittext_userMobileNumber;
	// String variable
	
	private String user_role="",mobile_number="";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_change_role);     
		initialiseUiControls();
		addOnClickListner();
	}
	
	
	/**
	 * Method: Method for initialize UI controls.
	 * **/
	public void initialiseUiControls()
	{
		textview_skip=(TextView)findViewById(R.id.textview_skip);
		textview_saathi=(TextView)findViewById(R.id.textview_saathi);
		textview_customer=(TextView)findViewById(R.id.textview_customer);
		textview_dealer=(TextView)findViewById(R.id.textview_dealer);
		textview_employee=(TextView)findViewById(R.id.textview_employee);
		textview_agent=(TextView)findViewById(R.id.textview_agent);
		textview_distributor=(TextView)findViewById(R.id.textview_distributor);
		edittext_userMobileNumber=(EditText)findViewById(R.id.edittext_userMobileNumber);
		
	}

	/**
	 * Method: Method for add on click listener on  UI controls.
	 * **/

	public void addOnClickListner()
	{
		textview_skip.setOnClickListener(this);
		textview_saathi.setOnClickListener(this);
		textview_customer.setOnClickListener(this);
		textview_dealer.setOnClickListener(this);
		textview_employee.setOnClickListener(this);
		textview_agent.setOnClickListener(this);
		textview_distributor.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		mobile_number=edittext_userMobileNumber.getText().toString().trim();
		
		switch(v.getId())
		{
		  case R.id.textview_skip:
			  
			  Intent i=new Intent(ChangeRoleActivity.this,SplashActivity.class);
			  startActivity(i);
			  finish();
			  
			  break;
			  
		case R.id.textview_saathi:
				  
			
			user_role="SAATHI";
			setUserRole(user_role);
				  break;
		 case R.id.textview_employee:
			 user_role="EMPLOYEE";
			 setUserRole(user_role);
					  break;
		 case R.id.textview_agent:
			 user_role="AGENT";
			 setUserRole(user_role);
			  break;
		 case R.id.textview_dealer:
			 user_role="DEALER";
			 setUserRole(user_role);
			  break;
		 case R.id.textview_customer:
			 user_role="CUSTOMER";
			 setUserRole(user_role);
			  break;
		 case R.id.textview_distributor:
			 user_role="DISTRIBUTOR";
			 setUserRole(user_role);
			  break;
			  
			  
		}
	}
	
	private void setUserRole(String user_role)
	{
		if(mobile_number!=null && !mobile_number.equals("") && mobile_number.length()>0)
		{
			try{
				JSONObject jObject_request=new JSONObject();
				jObject_request.put("request","changeUserType");
				
				jObject_request.put("deviceModel", GlobalVariables.getDeviceModel());
				jObject_request.put("deviceOs", GlobalVariables.getDeviceOs());
				jObject_request.put("accessType","A");
				jObject_request.put("mobile1",mobile_number);
				jObject_request.put("mobile2","");
				
				jObject_request.put("p_device_token","");
				jObject_request.put("p_role_name",user_role);
				
				Log.e("##request##", jObject_request.toString());
				new MyAsyncTask(this, jObject_request).execute();
			}catch(Exception e){
				
			}
			
			
		}
		
		else{
			 GlobalVariables.defaultOneButtonDialog(ChangeRoleActivity.this, "Please enter valid mobile number");
		}
		
	}


	@Override
	public void onTaskComplete(String result) {
		// TODO Auto-generated method stub
		
		Log.e("#####Response####", result);
		if(result!=null && result.length()>0){
			
			try{
			 JSONObject jObject_response=new JSONObject(result);
			  
				  String status=jObject_response.getString("status");
				  if(status.equals("200")){
					 JSONObject data= jObject_response.getJSONObject("data");
					 String op_authorised_yn=data.getString("op_authorised_yn");
					 if(op_authorised_yn.equals("1")){
						
						user_role =data.getString("op_role_name");
						finish();
						 
					 }else{
						 String op_message=data.getString("op_message");
						 
						 GlobalVariables.defaultOneButtonDialog(ChangeRoleActivity.this, op_message);
					 }
				  }else{
					  JSONObject data= jObject_response.getJSONObject("data");
					  String msg=data.getString("message");
					  GlobalVariables.defaultOneButtonDialog(ChangeRoleActivity.this, msg);
				  }
			 
			  }catch(Exception e){
			
		      }
		
	    }else{
		GlobalVariables.defaultOneButtonDialog(ChangeRoleActivity.this, GlobalVariables.NO_RECORD);
	    }
		
	}

}
