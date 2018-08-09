package com.erp.sheelafoam.sheelafoam.utility;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class SharePref {
	
	
	public static final String SECRET_KEY = "registration";

	public static String COLOR="color";
	public static String PRODUCTS="products";
	public static String DEALER="dealer";
	public static String LOCATION="location";
	
	public static String MODE_TYPE="USER_DATA";
	
	public static String OFFICIAL_TRACKING_START_TIME="office_tracking_start";
	public static String OFFICIAL_TRACKING_END_TIME="office_tracking_end";
	public static String HOME_TRACKING_START_TIME="home_tracking_start";
	public static String HOME_TRACKING_END_TIME="home_tracking_end";
	
	SharedPreferences mPreferences;
	public static final String PREF_NAME = "registration";
	public static String VERSION_CODE="version_code";
	
	
	
	public List<String> getColors(Context context)
	{
		
		List<String> coloList = null;
		mPreferences = context.getSharedPreferences(MODE_TYPE,	Context.MODE_PRIVATE);

		if (mPreferences.contains(COLOR)) 
		{
			String colors = mPreferences.getString(COLOR, null);
			
			try {
				JSONObject colorJsonObject = new JSONObject(colors);
				String colorString = colorJsonObject.getString(COLOR);
				
				coloList.add(colorString);
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		} else
			return null;

		return coloList;
	}
	
		
	
	
	

	@SuppressWarnings("null")
	public List<String> getDealers(Context context)
	{
		
		List<String> dealerList = null;
		mPreferences = context.getSharedPreferences(MODE_TYPE,	Context.MODE_PRIVATE);

		if (mPreferences.contains(DEALER)) 
		{
			String dealers = mPreferences.getString(DEALER, null);
			
			try {
				JSONObject colorJsonObject = new JSONObject(dealers);
				String colorString = colorJsonObject.getString(DEALER);
				
				dealerList.add(colorString);
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		} else
			return null;

		return dealerList;
	}
	
	
	
	
	
	public List<String> getProducts(Context context)
	{
		
		List<String> productList = null;
		mPreferences = context.getSharedPreferences(MODE_TYPE,	Context.MODE_PRIVATE);

		if (mPreferences.contains(PRODUCTS)) 
		{
			String dealers = mPreferences.getString(PRODUCTS, null);
			
			try {
				JSONObject colorJsonObject = new JSONObject(dealers);
				String colorString = colorJsonObject.getString(PRODUCTS);
				
				productList.add(colorString);
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		} else
			return null;

		return productList;
	}
	
	
	
	
	
	
	
	
	
	
	
}
