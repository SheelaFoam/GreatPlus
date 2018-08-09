package com.erp.sheelafoam.sheelafoam.board;

/**
 * Short description for file: Used for display addresses list of records 

 *
 * Used Android  minSdkVersion 11
 * Used Android  targetSdkVersion 19 

 * @package    com.sheela.employeeportal.erp.sheelafoam
 * @author     Vinay Kumar Gupta
 * Class name  AddressesFragment
 * Short description for class:  Fragment AddressesFragment used for display addresses list of records
 * Creation Date  04-02-2015
 * Last Modified  04-02-2015
 * Modified By    Vinay Kumar Gupta

 */


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.erp.sheelafoam.R;
import com.erp.sheelafoam.sheelafoam.utility.AsyncTaskListner;
import com.erp.sheelafoam.sheelafoam.utility.ConnectionDetector;
import com.erp.sheelafoam.sheelafoam.utility.GlobalVariables;

import org.json.JSONObject;

public class GlowSignBoarFragment extends Fragment implements OnClickListener,AsyncTaskListner {

	Context mContext;
	ConnectionDetector con;
	JSONObject jObject_response;
	
	TextView textview_dealer_list;
	Spinner spinner;
	
	String[] signboardType={"Raise letter","Frontlit","Backlit"};
	
	
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
		
		View view=inflater.inflate(R.layout.fragment_createsignboard, container,false);
		mContext=getActivity();
		con=new ConnectionDetector(getActivity());
		
		/*initializing xml controls*/
		initialization(view);
		
		/*add onclick listner*/
		addOnclickListner();
		
		return view;
	}
	
	/*fragment lifecycle method onCreateView end*/
	
	/*Interface callback method for track click event start*/

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		switch(v.getId())
		{
		   case R.id.btn_save:
			   
			   getInput();
			   
			   if(con.isConnectingToInternet())
			   {
				  
				 
			   }
			   
			   else
			   {
				   GlobalVariables.defaultOneButtonDialog(getActivity(), getResources().getString(R.string.default_network_alert_msg));
			   }
			   
			   break;
		   case R.id.textview_dealer_list:
			   
			   break;
			   
          case R.id.spinner:
			   
			   break;
			   
		}
	}
	
	/*Interface callback method for track click event end*/
	
	
	/*Interface callback method for get API hit response*/
	
	@Override
	public void onTaskComplete(String result) {
		// TODO Auto-generated method stub
		
		Log.e("#response#", result);
		try
		{
			if(result!=null && result.length()>0)
			{
				jObject_response=new JSONObject(result);
				String status=jObject_response.getString("status");
				if(status.equals("200"))
				{
					
					JSONObject data=jObject_response.getJSONObject("data");
					String op_result=data.getString("op_result");
					if(op_result.equals("1"))
					{
						String msg=data.getString("op_message");
						Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
						getActivity().getSupportFragmentManager().popBackStack();
					
//						ProfileFragment fragment=new ProfileFragment();
//						addFragment(fragment);
					}
					else
					{
						String msg=data.getString("op_message");
						Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
					}
				}
				
				else
				{
					JSONObject data=jObject_response.getJSONObject("data");
					String msg=data.getString("op_message");
					Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
				}
						
			}
			
			else
			{
				GlobalVariables.defaultOneButtonDialog(getActivity(), getResources().getString(R.string.default_network_alert_msg));
			}
		}
		catch(Exception e)
		{
			
		}
	}
	
	
	
	/***********************************User Define Methods*****************************************/
	
	
	/*user define method for initialize xml controls start*/
	
	public  void initialization(View view)
	{
		textview_dealer_list=(TextView)view.findViewById(R.id.textview_dealer_list);
		spinner=(Spinner)view.findViewById(R.id.spinner);
		
	}
	
	/*user define method for initialize xml controls end*/
	
	
	/*user define method addOnclickListner start*/
	
	public void addOnclickListner()
	{
		textview_dealer_list.setOnClickListener(this);
		spinner.setOnClickListener(this);
	}

	/*user define method addOnclickListner end*/
	
/*user define method getInput start*/
	
	public void getInput()
	{
		
	}

	/*user define method getInput end*/
	
	/*user define method setJsonRequest start*/
	
	public void setJsonRequest()
	{
		try
		{
			JSONObject jObject_request=new JSONObject();
			jObject_request.put("request","changePassword");
			//jObject_request.put("oldPassword",old_password);
			//jObject_request.put("oldPassword",old_password);
			//jObject_request.put("newPassword",new_password);
			
			Log.e("##request##", jObject_request.toString());
			//new MyAsyncTask(getActivity(), ChangePasswordFragment.this,jObject_request).execute();
		}
		catch(Exception e)
		{
			
		}
	}
	/*user define method setJsonRequest start*/


	 public void addFragment(Fragment fragment)
	    {
		 
		   getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
		 
			 //  DashBoardFragment fragment=new DashBoardFragment();
			   
		   getActivity().getSupportFragmentManager().beginTransaction()
		        .replace(R.id.flayout, fragment)
		        .addToBackStack(null)
		        .commit();
				
			   //.add(R.id.flayout, fragment,"PERSONAL_DETAILS");
		       
	    }
	 
	 /**
	  * Method: Create Spinner for select board type
	  * **/
	 
	 public void boardType_spinner()
	 {
		 
	 }

}
