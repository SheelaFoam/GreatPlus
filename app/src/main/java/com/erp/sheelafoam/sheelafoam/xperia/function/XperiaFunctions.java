package com.erp.sheelafoam.sheelafoam.xperia.function;

import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;

public class XperiaFunctions 
{
	    private static SharedPreferences Pref;
	    private static SharedPreferences.Editor editor;
	    
	    public static void setDealerDetails(Activity activity, String DealerId, String Mobile, String Ut)
	    {
	        Pref=activity.getSharedPreferences("UserSession", activity.MODE_PRIVATE);
	        editor=Pref.edit();
	        try{editor.clear();editor.commit();}catch(Exception e){}
	        editor.putString("DID", DealerId);
	        editor.putString("MOB", Mobile);
	        editor.putString("UT", Ut);
	        editor.commit();
	    }
	    
	    public static String getMob(Activity activity)
	    {
	        String mob="";
	        Pref=activity.getSharedPreferences("UserSession", activity.MODE_PRIVATE);
	        try
	        {
	            mob=Pref.getString("MOB","null");
				Log.e("Mobile: ", mob);
	            return mob;
	        }
	        catch (Exception e)
	        {
	            return "null";
	        }
	    }
	    
	    public static void setMob(Activity activity, String Mobile)
	    {
	        Pref=activity.getSharedPreferences("UserSession1", activity.MODE_PRIVATE);
	        editor=Pref.edit();
	        try{editor.clear();editor.commit();}catch(Exception e){}
	        editor.putString("MOB", Mobile);
	        editor.commit();
	    }
	    
	    public static String getExpMod(Activity activity)
	    {
	        String mob="";
	        Pref=activity.getSharedPreferences("UserSession1", activity.MODE_PRIVATE);
	        try
	        {
	            mob=Pref.getString("MOB","null");
	            return mob;
	        }
	        catch (Exception e)
	        {
	            return "null";
	        }
	    }
	    
	    
	    
	    public static String getUserType(Activity activity)
	    {
	        String mob="";
	        Pref=activity.getSharedPreferences("UserSession", activity.MODE_PRIVATE);
	        try
	        {
	            mob=Pref.getString("UT","null");
	            return mob;
	        }
	        catch (Exception e)
	        {
	            return "null";
	        }
	    }
	    
	    public static String getdealerId(Activity activity)
	    {
	        String mob="";
	        Pref=activity.getSharedPreferences("UserSession", activity.MODE_PRIVATE);
	        try
	        {
	            mob=Pref.getString("DID","null");
	            return mob;
	        }
	        catch (Exception e)
	        {
	            return "null";
	        }
	    }
	    
	    public static void logout(Activity activity)
	    {
	        Pref=activity.getSharedPreferences("UserSession", activity.MODE_PRIVATE);
	        editor=Pref.edit();
	        try
	        {
	        	editor.clear();
	            editor.commit();
	        }
	        catch (Exception e)
	        {

	        }
	    }
	    
}
