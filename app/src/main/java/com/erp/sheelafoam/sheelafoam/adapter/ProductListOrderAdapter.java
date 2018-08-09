
/**
 * Short description for file: Used for display address book data

 *
 * Used Android  minSdkVersion 11
 * Used Android  targetSdkVersion 19 

 * @package    com.sheela.employeeportal.erp.sheelafoam
 * @author     Vinay Kumar Gupta
 * Class name  AddressBookAdapter
 * Short description for class:  Adapter class used for set address book  data in list
 * Creation Date  06-02-2015
 * Last Modified  06-02-2015
 * Modified By    Vinay Kumar Gupta

 */

package com.erp.sheelafoam.sheelafoam.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.erp.sheelafoam.R;
import com.erp.sheelafoam.sheelafoam.entry.ProductOrderListBean;
import com.erp.sheelafoam.sheelafoam.fragments.ProductOrderFragment;
import com.erp.sheelafoam.sheelafoam.utility.AppConstant;

import java.util.ArrayList;


public class ProductListOrderAdapter extends BaseAdapter {

	
	SharedPreferences mPrefs;
	ArrayList<ProductOrderListBean> _data;
	Activity mActivity;
	
	private String user_role;
	Fragment fragment;
	FragmentManager mFragmentManager;
	
	/*constructor*/
	public ProductListOrderAdapter (ArrayList<ProductOrderListBean> data, Activity c, Fragment fragment, FragmentManager fragmentManager){
		_data = data;
		mActivity = c;
		
		this.fragment=fragment;
		this.mFragmentManager =  fragmentManager;
	
		mPrefs = mActivity.getSharedPreferences("Location", Context.MODE_PRIVATE);
	
	}

	public int getCount() {
		// TODO Auto-generated method stub
		return _data.size();
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return _data.get(position);
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	/*override method for inflate view start*/
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View v = convertView;
		
			LayoutInflater vi = (LayoutInflater)mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.fragment_order_view_item,null);
		

			user_role=mPrefs.getString("op_user_type", "");
		
		final ProductOrderListBean entry=_data.get(position);
		
		
		
	    TextView textview_product_name = (TextView) v.findViewById(R.id.textview_product_name);
	       TextView textview_order_no = (TextView) v.findViewById(R.id.textview_order_no);
	 //      TextView textview_delivery_date = (TextView) v.findViewById(R.id.textview_delivery_date);
	       TextView textview_delivery_date = (TextView) v.findViewById(R.id.textview_delivery_date);
	       TextView textview_product_l_w_t_text = (TextView) v.findViewById(R.id.textview_product_l_w_t_text);
	         
	       TextView textview_product_quantity = (TextView) v.findViewById(R.id.textview_product_quantity);
	       TextView textview_dealer_name=(TextView)v.findViewById(R.id.textview_dealer_name);
	       TextView textview_order_status=(TextView)v.findViewById(R.id.textview_order_status);
	       TextView textview_remark=(TextView)v.findViewById(R.id.textview_remark);
	       TextView textview_cancel_order=(TextView)v.findViewById(R.id.textview_cancel_order);
	       TextView textview_edit_order=(TextView)v.findViewById(R.id.textview_edit_order);
	        
	     /*  LinearLayout llayout_edit_order = (LinearLayout) v.findViewById(R.id.llayout_edit_order);
	       LinearLayout llayout_cancel_order = (LinearLayout) v.findViewById(R.id.llayout_cancel_order);
	  */
	       
	       
	       /**
	        *This adapter and ProductOrderView fragment calling from two places
	        *first from order managment module and second is from reporting mudule.
	        * **/
	    
	       String length = (entry.getLength().length()>0)?entry.getLength():"0";
			String breadth = (entry.getBreadth().length()>0)?entry.getBreadth():"0";
			String thick = (entry.getThick().length()>0)?entry.getThick():"0";
			   
	       
	       
	   	textview_product_name.setText(entry.getProduct_display_name());
		textview_order_no.setText("Order No: "+entry.getOrder_number());
		textview_delivery_date.setText("Date: "+entry.getOrder_date());
//		textview_product_l_w_t_text.setText("LxWxT: "+entry.getLength()+"x"+entry.getBreadth()+"x"+entry.getThick());
		textview_product_l_w_t_text.setText("LxWxT: "+length+"x"+breadth+"x"+thick);
		textview_product_quantity.setText("Qty: "+entry.getQty()+" "+entry.getUom());
		textview_dealer_name.setText("Dealer Name: "+entry.getDealer_name());
		textview_remark.setText("Remark: "+entry.getRemark());
		if(entry.getStatus().equals("0"))
		{
			textview_order_status.setText("Pending");
		}
		else if(entry.getStatus().equals("1"))
		{
			textview_order_status.setText("Approved");
		}
		else if(entry.getStatus().equals("2"))
		{
			textview_order_status.setText("Rejected");
		}
		else if(entry.getStatus().equals("3"))
		{
			textview_order_status.setText("Dispatched");
		}
		else
		{
			textview_order_status.setText("");
		}
		
		
				
			textview_cancel_order.setVisibility(View.INVISIBLE);
			textview_edit_order.setVisibility(View.INVISIBLE);
				
				
				
			
			
			/*
			textview_edit_order.setOnClickListener(new OnClickListener() {
				
			
					@Override
					public void onClick(View arg0) {
					
						if(entry.getStatus().equals("1") || entry.getStatus().equals("2"))
						{
							// check if user is ZD than he can edit order.
							
							if(!user_role.equals("DISTRIBUTOR")){
										
							
								if(entry.getStatus().equals("1"))
								{
								GlobalVariables.defaultOneButtonDialog(mActivity, ""+"This order is approved so you can't modify this order.");
								}
								else
								{
									GlobalVariables.defaultOneButtonDialog(mActivity, ""+"This order is rejected so you can't modify this order.");
								}
						      }else{
						    	  editOrder(entry);
						      }
							
						   }
										
						else{
							
							
					}
				}
					
				
			});*/
		
		
		/*v.findViewById(R.id.llayout_edit_order).setOnClickListener(new OnClickListener()*/ 
			/*v.findViewById(R.id.llayout_edit_order).setOnClickListener(new OnClickListener(){
			
			@Override
			public void onClick(View arg0) {
			
				if(entry.getStatus().equals("1") || entry.getStatus().equals("2"))
				{
					// check if user is ZD than he can edit order.
					
					if(!user_role.equals("DISTRIBUTOR")){
								
					
						if(entry.getStatus().equals("1"))
						{
						GlobalVariables.defaultOneButtonDialog(mActivity, ""+"This order is approved so you can't modify this order.");
						}
						else
						{
							GlobalVariables.defaultOneButtonDialog(mActivity, ""+"This order is rejected so you can't modify this order.");
						}
				      }else{
				    	  editOrder(entry);
				      }
					
				   }
								
				else{
					
					
			}
		}

			
		});
		
		*/
		

		/*v.findViewById(R.id.llayout_cancel_order).setOnClickListener(new OnClickListener() {*/
			/*textview_cancel_order.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				if(entry.getStatus().equals("1") || entry.getStatus().equals("2"))
				{
					if(entry.getStatus().equals("1"))
					{
					GlobalVariables.defaultOneButtonDialog(mActivity, ""+"This order is approved so you can't delete this order.");
					}
					else
					{
						GlobalVariables.defaultOneButtonDialog(mActivity, ""+"This order is rejected so you can't delete this order.");
					}
				}
				else
				{
				//deleteOrder(entry.getOrder_number(),position);
				defaultTwoButtonDialog_delete(mActivity,"Are you sure want to delete this order",entry,position);
				
				}
				
				
				
				
			}

		
		});
		*/
	
		return v;
	}
	

/*	
	override method for inflate view start
	private void deleteOrder(String order_number, int position) {
	
			
			if(HelperMethods.isNetworkAvailable(mActivity))
			{
				try {
					ProductOrderView.getInstance().requset_no = "2";		
					
					ProductOrderView.getInstance().deleted_position=position;
						JSONObject jsonObjectRequest=new JSONObject();	
						jsonObjectRequest.put("request", ApiList.API_PRODUCT_DELETE_ORDER);
						jsonObjectRequest.put("p_order_number", order_number);
						
						ProductOrderView.getInstance().requset_no = "2";		
						
							
						Log.e("###request###", ""+jsonObjectRequest);	
						

					new MyAsyncTask(mActivity,fragment, 
							jsonObjectRequest, 
							ApiList.URL_PLACE_ORDER).execute();
					
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}			}
			else
			{
				Toast.makeText(mActivity, "Network Error", Toast.LENGTH_LONG).show();
			}
			
		
	}*/
	
	
	
	
	private void saveDatainPrefences(ProductOrderListBean entry) {
		// TODO Auto-generated method stub
		
		Log.e("Entry", ""+entry);
		
		Editor editor = mPrefs.edit();
		editor.putString("p_order_number", entry.getOrder_number());
		editor.putString("p_dealer_name", entry.getDealer_name());
		editor.putString("p_product_display_name", entry.getProduct_display_name());
		editor.putString("p_length", entry.getLength());
		editor.putString("p_breadth", entry.getBreadth());
		editor.putString("p_thick", entry.getThick());
		editor.putString("p_colour", entry.getColour());
		editor.putString("p_uom", entry.getUom());
		editor.putString("p_qty", entry.getQty());
		
		editor.putString("p_remark", entry.getRemark());
		editor.putString("p_customer_name", entry.getCustomer_name());
		editor.putString("p_customer_mobile", entry.getCustomer_mobile());
		editor.putString("p_delivery_date", entry.getDelivery_date());
		editor.putString("p_captured_image_url", entry.getCaptured_image_url());
		editor.putString("p_captured_length", entry.getCaptured_length());
		editor.putString("p_captured_bredth", entry.getCaptured_bredth());
		editor.putString("p_order_date", entry.getOrder_date());
		
		editor.putString("p_ext", entry.getExt());
		editor.putString("p_old_image", entry.getCaptured_image());
		
		editor.putString("p_channel_partner_group", entry.getDealerChnlPartnerGroup());
		editor.putString("P_channel_partner_id", entry.getDealerChnlPartnerId());
		
		editor.commit();
		
		
		
	}
	
	
	
	
	
/*	
	private void editOrder(String order_number) {
		
		
		if(HelperMethods.isNetworkAvailable(mActivity))
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
					

				new MyAsyncTask(mActivity,fragment, 
						jsonObjectRequest, 
						ApiList.URL_PLACE_ORDER).execute();
				
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			}
		else
		{
			Toast.makeText(mActivity, "Network Error", Toast.LENGTH_LONG).show();
		}
		

	
	
	}*/
	
	

	 /* public  void defaultTwoButtonDialog_delete(Activity activity,String msg,final ProductOrderListBean entry,final int position)
	  {
		  AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		  builder.setMessage(msg)
		         .setCancelable(false)
		         .setPositiveButton("YES", new DialogInterface.OnClickListener() {
		             public void onClick(DialogInterface dialog, int id) {
		                  //do things
		            	 deleteOrder(entry.getOrder_number(),position);
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
	  }*/
	
	 
	// update by Vinay Gupta on 21-05-2016.
	
	
	private void editOrder(ProductOrderListBean entry)
	  {
			
			AppConstant.EDIT_ORDER= 1;
			
      //Saving data in shred prefrences.	
				
		Editor editor = mPrefs.edit();
		editor.putString("p_order_number", entry.getOrder_number().toString());
		Log.e("Entry Order no", ""+entry+" "+entry.getOrder_number());
		
		editor.putString("p_dealer_name", entry.getDealer_name());
		
		//editor.putString("p_channel_partner_group", entry.getDealerChnlPartnerGroup());
		//editor.putString("P_channel_partner_id", entry.getDealerChnlPartnerId());
		
		editor.putString("p_dealer_category", entry.getDealer_category());
		editor.putString("p_product_display_name", entry.getProduct_display_name());
		editor.putString("p_color_applicable_yn", entry.getColor_applicable_yn());
		
		Log.e("Product name ", " "+entry.getProduct_display_name());
		editor.putString("p_length", entry.getLength());
		editor.putString("p_breadth", entry.getBreadth());
		editor.putString("p_thick", entry.getThick());
		editor.putString("p_colour", entry.getColour());
		editor.putString("p_uom", entry.getUom());
		editor.putString("p_qty", entry.getQty());
		
		editor.putString("p_remark", entry.getRemark());
		Log.e("Remark ", " "+entry.getRemark());
		
		editor.putString("p_customer_name", entry.getCustomer_name());
		editor.putString("p_customer_mobile", entry.getCustomer_mobile());
		editor.putString("p_delivery_date", entry.getDelivery_date());
		editor.putString("p_captured_image", entry.getCaptured_image());
		editor.putString("p_captured_image_url", entry.getCaptured_image_url());
		editor.putString("p_captured_length", entry.getCaptured_length());
		editor.putString("p_captured_bredth", entry.getCaptured_bredth());
		editor.putString("p_order_date", entry.getOrder_date());
		
		editor.putString("p_ext", entry.getExt());
		editor.putString("p_old_image", entry.getCaptured_image());
		editor.putString("location_name", entry.getLocation_name());
		editor.putString("location_code", entry.getLocation_code());
		editor.putString("p_auto_size_flag", entry.getP_auto_size_flag());
		
		
		
		
		editor.commit();
		
		
		
		
		
		
		
		ProductOrderFragment viewfragment=new ProductOrderFragment();
			
			mFragmentManager.beginTransaction()
			
	          .add(R.id.flayout, viewfragment)
	          .addToBackStack(null)
	          .commit();
			
			}	
}