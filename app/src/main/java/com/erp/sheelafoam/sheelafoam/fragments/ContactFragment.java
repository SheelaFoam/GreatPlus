
/**
 * Short description for file: Used for display user contact information

 *
 * Used Android  minSdkVersion 11
 * Used Android  targetSdkVersion 19 

 * @package    com.sheela.employeeportal.erp.sheelafoam
 * @author     Vinay Kumar Gupta
 * Class name  ContactFragment
 * Short description for class:  Fragment ContactFragment used for display user contact information
 * Creation Date  04-02-2015
 * Last Modified  04-02-2015
 * Modified By    Vinay Kumar Gupta

 */

package com.erp.sheelafoam.sheelafoam.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.erp.sheelafoam.R;
import com.erp.sheelafoam.sheelafoam.utility.AsyncTaskListner;
import com.erp.sheelafoam.sheelafoam.utility.ConnectionDetector;
import com.erp.sheelafoam.sheelafoam.utility.GlobalVariables;
import com.erp.sheelafoam.sheelafoam.utility.MyAsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

public class ContactFragment extends Fragment implements AsyncTaskListner,OnClickListener {

	Context mContext;
	
	/*xml textview controls*/
	TextView textview_timing_one,textview_tollfree_no,textview_cc_email;
	TextView textview_direct_no,textview_tollfree_no_it,textview_direct_no_it;
	RelativeLayout rlayout_cc_direct_no_lable;
	
	/*String variables for hold response data*/
	String op_cc_timing="",op_cc_toll_free_no="",op_cc_email_id="",op_cc_direct_no="";
	String op_it_toll_free_no="",op_it_direct_no="";
	
	
	/*internet connection detector object*/
	ConnectionDetector con;
	
	/*fragment lifecycle method onCreate start*/
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	
	/*fragment lifecycle method onCreate end*/
	
	/*fragment lifecycle method onCreateView start*/
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		View view=inflater.inflate(R.layout.fragment_contact, container,false);
		mContext=getActivity();
		
		/*initializing connection detector class*/
		con=new ConnectionDetector(getActivity());
		
		/*initializing xml controls*/
		initialization(view);
		
		/*add onclick listner*/
		addOnclickListner();
		
		if(con.isConnectingToInternet())
		{
			setJsonRequest();
		}
		
		else
		{
			GlobalVariables.defaultOneButtonDialog(getActivity(), getResources().getString(R.string.default_network_alert_msg));
		}
		
		
		view.setOnClickListener(null);
		return view;
	}
	/*fragment lifecycle method onCreateView end*/
	
	/*Interface callback method for get API hit response start*/
	
	@Override
	public void onTaskComplete(String result) {
		// TODO Auto-generated method stub
		
	
		
		try
		{
			Log.d("#response#", result);

			if(result.length()>0 && result!=null)
			{
				try {
					JSONObject jObject_response=new JSONObject(result);
					
					String status=jObject_response.getString("status");
					if(status.equals("200"))
					{
						JSONObject data=jObject_response.getJSONObject("data");
						op_cc_timing=data.getString("op_cc_timing");
						op_cc_toll_free_no=data.getString("op_cc_toll_free_no");
						op_cc_email_id=data.getString("op_cc_email_id");
						op_cc_direct_no=data.getString("op_cc_direct_no");
						op_it_toll_free_no=data.getString("op_it_toll_free_no");
						op_it_direct_no=data.getString("op_it_direct_no");
						if(!op_cc_timing.equals("null"))
						{
							textview_timing_one.setText(op_cc_timing);
						}
						else
						{
							textview_timing_one.setText(R.string.default_no_value);
						}
						if(!op_cc_toll_free_no.equals("null"))
						{
							textview_tollfree_no.setText(op_cc_toll_free_no);
						}
						else
						{
							textview_tollfree_no.setText(R.string.default_no_value);
						}
						if(!op_cc_email_id.equals("null"))
						{
							textview_cc_email.setText(op_cc_email_id);
						}
						else
						{
							textview_cc_email.setText(R.string.default_no_value);
						}
						if(!op_cc_direct_no.equals("null"))
						{
							textview_direct_no.setText(op_cc_direct_no);
						}
						else
						{
							textview_direct_no.setText(R.string.default_no_value);
						}
						if(!op_it_toll_free_no.equals("null"))
						{
							textview_tollfree_no_it.setText(op_it_toll_free_no);
						}
						else
						{
							textview_tollfree_no_it.setText(R.string.default_no_value);
						}
						if(!op_it_direct_no.equals("null"))
						{
							textview_direct_no_it.setText(op_it_direct_no);
						}
						else
						{
							textview_direct_no_it.setText(R.string.default_no_value);
						}
					}
					else
					{
						GlobalVariables.defaultOneButtonDialog(getActivity(), getResources().getString(R.string.default_no_data_found_alert_msg));
					}
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		
		}
		catch(Exception e)
		{
			
		}
	}
	/*Interface callback method for get API hit response end*/
	


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		switch(v.getId())
		{
		   case R.id.textview_tollfree_no:
			 
			   checkSimAvailibility(textview_tollfree_no.getText().toString().trim());
			   
			   break;
			   
		   case R.id.textview_direct_no:
			   
			 
			   checkSimAvailibility(textview_direct_no.getText().toString().trim());
			   
			   break;
			   
		   case R.id.textview_cc_email:
			   
			   if(textview_cc_email.getText().toString().trim().length()>0)
			   {
				 sendEmail(getActivity(), textview_cc_email.getText().toString().trim());
			      
			   }
			   
			   break;
			   
		   case R.id.rlayout_cc_direct_no_lable:
			   
			   //calling change password fragment.
			   ChangePasswordFragment fragment=new ChangePasswordFragment();
				
				addFragment(fragment);
			   
			   
			   break;
			   
		}
		
	}

	
	
	/***********************************User Define Methods*****************************************/
	
	
	/*user define method for initialize xml controls start*/
	
	public  void initialization(View view)
	{	textview_timing_one=(TextView)view.findViewById(R.id.textview_timing_one);
		textview_tollfree_no=(TextView)view.findViewById(R.id.textview_tollfree_no);
		textview_cc_email=(TextView)view.findViewById(R.id.textview_cc_email);
		textview_direct_no=(TextView)view.findViewById(R.id.textview_direct_no);
		textview_tollfree_no_it=(TextView)view.findViewById(R.id.textview_tollfree_no_it);
		textview_direct_no_it=(TextView)view.findViewById(R.id.textview_direct_no_it);
		rlayout_cc_direct_no_lable=(RelativeLayout)view.findViewById(R.id.rlayout_cc_direct_no_lable);
		
	}
	
	/*user define method for initialize xml controls end*/
	
	
	/*user define method addOnclickListner start*/
	
	public void addOnclickListner()
	{
		textview_tollfree_no.setOnClickListener(this);
		textview_direct_no.setOnClickListener(this);
		textview_cc_email.setOnClickListener(this);
		rlayout_cc_direct_no_lable.setOnClickListener(this);
	}
	
	/*user define method addOnclickListner end*/
	
	/*user define method testSetJsonRequest start*/
	public void setJsonRequest()
	{
		
		try
		{
			JSONObject jObject_request=new JSONObject();
			jObject_request.put("request","getContactUs");

			
			Log.e("##request##", jObject_request.toString());
			new MyAsyncTask(getActivity(), ContactFragment.this,jObject_request).execute();
		}
		catch(Exception e)
		{
			
		}
	}


	
	/*user define method testSetJsonRequest end*/
	
	public void checkSimAvailibility(String number)
	{
		if(number.length()>0)
		{
			TelephonyManager telMgr = (TelephonyManager)getActivity().getSystemService(Context.TELEPHONY_SERVICE);
		    int simState = telMgr.getSimState();
		    if(simState== TelephonyManager.SIM_STATE_ABSENT)
		    {
		      Toast.makeText(getActivity(), "No Sim card available", Toast.LENGTH_LONG).show();
		    }
		    else
		    {
		    	if(simState== TelephonyManager.SIM_STATE_READY)
		    	{
		    		 
				  GlobalVariables.makeCall(getActivity(), number);
					   
		    	}
		    }
	   }
	}
	
	 public static void sendEmail(Activity activity, String email_id) {
	      Log.i("Send email", "");

	      String[] TO = {email_id};
	      //String[] CC = {"mcmohd@gmail.com"};
	      
	      
	      Intent emailIntent = new Intent(Intent.ACTION_SEND);
	      emailIntent.setData(Uri.parse("mailto:"));
	     // emailIntent.setType("text/plain");
	      emailIntent.setType("message/rfc822");


	      emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
	     // emailIntent.putExtra(Intent.EXTRA_CC, CC);
	      emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Your subject");
	      emailIntent.putExtra(Intent.EXTRA_TEXT, "Email message goes here");

	      try {
	         activity.startActivity(Intent.createChooser(emailIntent, "Send mail..."));
	        // activity.finish();
	         Log.i("Finished sending email...", "");
	      } catch (android.content.ActivityNotFoundException ex) {
	         Toast.makeText(activity,
	         "There is no email client installed.", Toast.LENGTH_SHORT).show();
	      }
	   }


	/*@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
		Toast.makeText(getActivity(), "Hi i am on activity result", Toast.LENGTH_LONG).show();
	}*/
	 
	 // calling fragment of change password.
	 public void addFragment(Fragment fragment){
			

			/*adding fragment on button click*/
			getActivity().getSupportFragmentManager().beginTransaction()    
		       // .replace(R.id.flayout, fragment)
			   	.add(R.id.flayout, fragment)
		        .addToBackStack(null)
		        .commit();
		}
	
}
