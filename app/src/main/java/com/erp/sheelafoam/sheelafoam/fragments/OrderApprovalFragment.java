package com.erp.sheelafoam.sheelafoam.fragments;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.erp.sheelafoam.R;
import com.erp.sheelafoam.sheelafoam.adapter.ApprovedOrderAdapter;
import com.erp.sheelafoam.sheelafoam.entry.ProductOrderListBean;
import com.erp.sheelafoam.sheelafoam.utility.ApiList;
import com.erp.sheelafoam.sheelafoam.utility.AsyncTaskListner;
import com.erp.sheelafoam.sheelafoam.utility.GlobalVariables;
import com.erp.sheelafoam.sheelafoam.utility.HelperMethods;
import com.erp.sheelafoam.sheelafoam.utility.MyAsyncTask;
import com.erp.sheelafoam.sheelafoam.utility.MyCustomAsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class OrderApprovalFragment extends Fragment implements AsyncTaskListner {
	
	Context mContext;
	
	SharedPreferences mPrefs;
	ImageView gender_icon,upload_pic;
	
	private int year;
	private int month;
	private int day; 
	
	public String requset_no;
	public int deleted_position;	

	Activity mActivity;
	
	JSONObject jsonObjectRequest, jsonObjectResponse;
	  
	
	
	
	ListView listView;
	TextView order_title;
	
	
	ArrayList<ProductOrderListBean> orderListBeans=new ArrayList<ProductOrderListBean>();
	
	ApprovedOrderAdapter listOrderAdapter;
	
	
	//Declare All Listing Field
	
	
	String captured_image;
	String order_number;
	
	String captured_image_url;
	
	String breadth;
	String remark;
	String customer_name;
	String customer_mobile;
	String qty;
	String order_id;
	String colour;
	String uom;
	String thick;
	String product_display_name;
	String captured_bredth;
	String delivery_date;
	String captured_length;
	
	String order_date;
	String dealer_name;
	
	
	   
	/*
	 * Singleton class for accessing methods from other activity/fragments
	 * */
	public static OrderApprovalFragment fragment;
	public static OrderApprovalFragment getInstance() {
	           if (fragment == null) {
	               fragment = new OrderApprovalFragment();
	           }
	           return fragment;
	       }

	       public OrderApprovalFragment() {
	           fragment = this;
	       }
	
	       
	       
/*
 * ====================================================================================
 * onCreateView Method for initializing all the views & objects 
 * that will be used in package
 * =====================================================================================
 */	       
	       
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		
		View view=inflater.inflate(R.layout.fragment_order_view, container,false);
		mContext=getActivity();
		
		mPrefs = getActivity().getSharedPreferences("Location", Context.MODE_PRIVATE);
		
		mActivity = getActivity();
		
		
		getUiObject(view);
		
		
		/*
		 * Get Order details 
		 */
	//	getOrderDetails();
		
		getAllOrder();
	/*	if(orderListBeans.size()==0)
		{	Toast.makeText(mActivity, "You don't have any order", Toast.LENGTH_LONG).show();}
   */ 	
		
		view.setOnClickListener(null);
	
		return view;
	}
	
	
	
private void getAllOrder() {
	// TODO Auto-generated method stub
	
	try
	{
	requset_no = "1";
	orderListBeans.clear();
	
		jsonObjectRequest=new JSONObject();
		jsonObjectRequest.put("request", ApiList.API_GET_ALL_ORDER
			);
		
		jsonObjectRequest.put("p_from_date","");
		jsonObjectRequest.put("p_to_date","");
		
		//jsonObjectRequest.put("p_from_date",HelperMethods.getDDMMMyyyy(HelperMethods.getFifteenDaysOldDate()));
		//jsonObjectRequest.put("p_to_date",HelperMethods.getDDMMMyyyy(HelperMethods.getCurrentDate()));
		Log.e("##request##", jsonObjectRequest.toString());
		new MyAsyncTask(getActivity(),OrderApprovalFragment.this, jsonObjectRequest, ApiList.URL_PLACE_ORDER).execute();
	
	
	}
	
	catch(Exception e)
	{
		
		Log.e("error", ""+e);
		
	}
	
	
	
}




/*
 * ====================================================================================
 * Method for getting referencing view from layout
 * =====================================================================================
 **/

private void getUiObject(View view) {
		// TODO Auto-generated method stub
	

	listView = (ListView) view.findViewById(R.id.listView1);
	order_title=(TextView)view.findViewById(R.id.order_title);
	order_title.setText("Order Approval");


	
	}



	



	
	
	public void getOrderDetails()
	{
		
		if(HelperMethods.isNetworkAvailable(getActivity()))
		{
			jsonObjectRequest=new JSONObject();
			try 
			{	
				//change it as per need
				
				requset_no="1";
				jsonObjectRequest.put("request", ApiList.API_PRODUCT_COLOR);
				Log.d("#request#", "" + jsonObjectRequest.toString());
			} 
			
			catch (JSONException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			MyCustomAsyncTask asyncTask = new MyCustomAsyncTask(getActivity(),OrderApprovalFragment.this, jsonObjectRequest, ApiList.URL_ORDER_DETAILS);
			asyncTask.execute();
			
		}
		else
		{
			GlobalVariables.defaultOneButtonDialog(getActivity(), "Network error");
		}
		
	}
	
	
	
	
	

	@Override
	public void onTaskComplete(String result) {
		// TODO Auto-generated method stub
		
	// check it 
		
		try {
			if(requset_no.equals("1"))
			{
			
				if(result!=null && result.length()>0)
				{		

					jsonObjectResponse=new JSONObject(result);
					
					Log.e("response", " "+jsonObjectResponse);
					
					String status=jsonObjectResponse.getString("status");
					
					 if(status.equalsIgnoreCase("200"))	
					 {
					    	JSONObject data=jsonObjectResponse.getJSONObject("data");
					    						    	   
					    	JSONArray productListArray = data.getJSONArray("product");
					    	
					    	
					    	for(int i=0;i<productListArray.length();i++){
					    							    			
					    		JSONObject jsonObject=productListArray.getJSONObject(i);
					    		String captured_image=jsonObject.getString("captured_image");
					    		String order_number=jsonObject.getString("order_number");
						    	
					    		String captured_image_url=jsonObject.getString("captured_image_url");
						    	
					    		String breadth=jsonObject.getString("breadth");
						    	
					    		
					    		String customer_name=jsonObject.getString("customer_name");
						    	
					    		String customer_mobile=jsonObject.getString("customer_mobile");
						    	
					    		
					    	
						    	
					    		String order_id=jsonObject.getString("order_id");
						    	
					    		String colour=jsonObject.getString("colour");
					    		
					    		String qty=jsonObject.getString("qty");
					    		String remark=jsonObject.getString("remark");
							    
					    		
					    		String uom=jsonObject.getString("uom");
						    	
					    		String thick=jsonObject.getString("thick");
						    	
					    		String product_display_name=jsonObject.getString("product_display_name");
						    	
					    		
					    		
					    		String captured_bredth=jsonObject.getString("captured_bredth");
						    	
					    		String delivery_date= HelperMethods.getDDmmYYYYByHiphon(jsonObject.getString("delivery_date"));
						    	
					    		String captured_length=jsonObject.getString("captured_length");
						    	
					    		String order_date= HelperMethods.getDDmmYYYYByHiphon(jsonObject.getString("order_date"));
						    	
					    		String dealer_name=jsonObject.getString("dealer_name");
					    		String outstanding_balance=jsonObject.getString("outstanding_balance");
					    		
					    		String length=jsonObject.getString("length");
					    		ProductOrderListBean entry=new ProductOrderListBean();
					    		
					    		entry.setBreadth(breadth);
					    		entry.setCaptured_bredth(captured_bredth);
					    		
					    		entry.setCaptured_image(captured_image);
					    		entry.setCaptured_image_url(captured_image_url.replace(" ", "%20")); 
					    		entry.setCaptured_length(captured_length);
					    		
					    		entry.setColour(colour);
					    		entry.setCustomer_mobile(customer_mobile);
					    		
					    		entry.setCustomer_name(customer_name);
					    		entry.setDealer_name(dealer_name);
					    		entry.setOutstanding_balance(outstanding_balance);
					    		entry.setDelivery_date(delivery_date);
					    		entry.setOrder_date(order_date);
					    		entry.setOrder_number(order_number);
					    		entry.setOrder_id(order_id);
					    		entry.setProduct_display_name(product_display_name);
					    		entry.setQty(qty);
					    		entry.setRemark(remark);
					    		entry.setThick(thick);
					    		
					    		entry.setLength(length);
					    	
					    		entry.setUom(uom);
					    		
					    		
					    		orderListBeans.add(entry);
						    	
					    		
					    	}
					    	
					    	displayOrderList(orderListBeans);
					    
					    	
					}
					
					else{
					//	customAsyncTask.hideDialog(getActivity());	
						JSONObject data=jsonObjectResponse.getJSONObject("data");
						String msg=data.getString("op_message");
						GlobalVariables.defaultOneButtonDialog(getActivity(), ""+msg);
					}
					
				}
				else{
					Toast.makeText(getActivity(), "No Data returned", Toast.LENGTH_SHORT).show();
				}
					
						
			}
			
			
			else if(requset_no.equals("2"))   //deleting order
			{
			
		
				if(result!=null && result.length()>0)
				{		

					jsonObjectResponse=new JSONObject(result);
					
					Log.e("response", " "+jsonObjectResponse);
					
					String status=jsonObjectResponse.getString("status");
					
					 if(status.equalsIgnoreCase("200"))	
					 {
					    	JSONObject data=jsonObjectResponse.getJSONObject("data");
							String msg=data.getString("op_message");
							//Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
							
							orderListBeans.remove(deleted_position);
							listOrderAdapter.notifyDataSetChanged();
						
							GlobalVariables.defaultOneButtonDialog(getActivity(), ""+msg);
							/*ProductOrderView fragment=new ProductOrderView();
							 getActivity().getSupportFragmentManager().beginTransaction()   
						    	.add(R.id.flayout, fragment)
						        .addToBackStack(null)
						        .commit();*/
							
						/*	getActivity().getSupportFragmentManager().popBackStack();
							ProductOrderView fragment=new ProductOrderView();
							 getActivity().getSupportFragmentManager().beginTransaction()   
						    	.add(R.id.flayout, fragment)
						        .addToBackStack(null)
						        .commit();
							 
							getAllOrder();
							*/
					    
					}
					
					else{
					//	customAsyncTask.hideDialog(getActivity());	
						
						String msg=jsonObjectResponse.getString("msg");
						
						GlobalVariables.defaultOneButtonDialog(getActivity(), ""+msg);
					}
					
				}
				else{
					Toast.makeText(getActivity(), "No Data returned", Toast.LENGTH_SHORT).show();
				}
					
						
			}
			
			
		} catch (Exception e) {
			// TODO: handle exception
			
			Log.e("Product List View", ""+e);
		}
	
		
		
	}

	private void displayOrderList(ArrayList<ProductOrderListBean> arrayList) {
		// TODO Auto-generated method stub
		
		
		try {
			if(orderListBeans.size()==0)
			{
				GlobalVariables.defaultOneButtonDialog(mActivity, "No Orders available");
			}
			else{
				 listOrderAdapter=new ApprovedOrderAdapter(orderListBeans,getActivity(),
						OrderApprovalFragment.this, getActivity().getSupportFragmentManager());
				listView.setAdapter(listOrderAdapter);
			
			
}
		} catch (Exception e) {
			// TODO Auto-generated catch block			
			Toast.makeText(getActivity(), ""+e, Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}
	}

	
	
	
	
	
	/*private void editOrder(String order_number) {
		
		
		if(HelperMethods.isNetworkAvailable(getActivity()))
		{
			try {
				ProductOrderView.getInstance().requset_no = "3";		
				

					JSONObject jsonObjectRequest=new JSONObject();	
					jsonObjectRequest.put("request", ApiList.API_PRODUCT_EDIT_ORDER);
					jsonObjectRequest.put("p_order_number", order_number);
					
					jsonObjectRequest.put("p_dealer_name", order_number);
					jsonObjectRequest.put("p_product_display_name", order_number);
					jsonObjectRequest.put("p_length", order_number);
					jsonObjectRequest.put("p_breadth", order_number);
					jsonObjectRequest.put("p_thick", order_number);
					
					jsonObjectRequest.put("p_colour", order_number);
					jsonObjectRequest.put("p_uom", order_number);
					jsonObjectRequest.put("p_qty", order_number);
					jsonObjectRequest.put("p_remark", order_number);
					jsonObjectRequest.put("p_customer_name", order_number);
					jsonObjectRequest.put("p_customer_mobile", order_number);
					
					jsonObjectRequest.put("p_delivery_date", order_number);
					jsonObjectRequest.put("p_captured_image", order_number);
					jsonObjectRequest.put("p_captured_length", order_number);
					jsonObjectRequest.put("p_captured_bredth", order_number);
					jsonObjectRequest.put("p_order_date", order_number);
					
					jsonObjectRequest.put("p_ext", order_number);
					jsonObjectRequest.put("p_captured_image_binary", order_number);
					jsonObjectRequest.put("p_old_image", order_number);
					
							
					Log.e("###request###", ""+jsonObjectRequest);	
					

				new MyAsyncTask(getActivity(),fragment,	jsonObjectRequest,ApiList.URL_PLACE_ORDER).execute();
				
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			}
		else
		{
			Toast.makeText(getActivity(), "Network Error", Toast.LENGTH_LONG).show();
		}
		

	
	
	}
	*/
	
	
	
	
	  public static void defaultTwoButtonDialog_approved(Activity activity, String msg)
	  {
		  AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		  builder.setMessage(msg)
		         .setCancelable(false)
		         .setPositiveButton("YES", new DialogInterface.OnClickListener() {
		             public void onClick(DialogInterface dialog, int id) {
		                  //do things
		            	 
		            	 dialog.cancel();
		             }
		         });
		  builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
			}
		});
		  AlertDialog alert = builder.create();
		  alert.show();
	  }
	
	  public static void defaultTwoButtonDialog_reject(Activity activity, String msg)
	  {
		  AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		  builder.setMessage(msg)
		         .setCancelable(false)
		         .setPositiveButton("YES", new DialogInterface.OnClickListener() {
		             public void onClick(DialogInterface dialog, int id) {
		                  //do things
		            	 
		            	 dialog.cancel();
		             }
		         });
		  builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
			}
		});
		  AlertDialog alert = builder.create();
		  alert.show();
	  }

}
