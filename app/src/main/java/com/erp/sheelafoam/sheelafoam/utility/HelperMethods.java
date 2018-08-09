package com.erp.sheelafoam.sheelafoam.utility;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.Settings.Secure;
import android.util.Base64;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class HelperMethods {
	public static boolean isNetworkAvailable(Activity act) {
        ConnectivityManager connectivityManager
          = (ConnectivityManager)act.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }
	public static void hideSoftKeyboard(Activity activity) {
		if (activity.getCurrentFocus() != null && activity.getCurrentFocus() instanceof EditText) {
			InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(activity.getWindow().getCurrentFocus() 
					.getWindowToken(), 0);
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	
	public static String getYyMmDd(String insurence_expiry_date)
	{
		try {
		String a[]=insurence_expiry_date.split("-");
		
		insurence_expiry_date=a[2]+"-"+a[1]+"-"+a[0];
		return insurence_expiry_date;
	} 
		catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

		return "";

	}
	
	
	
	
	
	
	public static String getDDmmYYYY(String date1)
	{
		try {
			//String date1="02 Feb 89";
			StringBuilder sb = new StringBuilder();
			  SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yy");
							//String dateInString = "2013-05-05";
						 
				Date myDate = formatter.parse(date1);
				
				
				String date_parse[]=new SimpleDateFormat("dd-MM-yyyy").format(myDate).split("-");
					
				sb.append(date_parse[0]+" "+date_parse[1]+" "+date_parse[2]);
				date1 = sb.toString();	
				
				System.out.println(date_parse[0]+" "+date_parse[1]+" "+date_parse[2]);
		 
		 
	} 
		catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

		return date1;

	}
	
	
	

	
	public static String getDDmmYYYYByHiphon(String date1)
	{
		
		try {
			//String date1="02 Feb 89";
			StringBuilder sb = new StringBuilder();
			  SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yy");
							//String dateInString = "2013-05-05";
						 
				Date myDate = formatter.parse(date1);
				
				
				String date_parse[]=new SimpleDateFormat("dd-MM-yyyy").format(myDate).split("-");
					
				sb.append(date_parse[0]+"-"+date_parse[1]+"-"+date_parse[2]);
				date1 = sb.toString();	
				
				System.out.println(date_parse[0]+" "+date_parse[1]+" "+date_parse[2]);
		 
		 
	} 
		catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

		return date1;

	}
	
	
	
	public static String getDDMMMyyyy(String date1)
	{
		try {
			//String date1="02-02-1989";
			StringBuilder sb = new StringBuilder();
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		 Date myDate = formatter.parse(date1);
		 String date_parse[]=new SimpleDateFormat("dd-MMM-yyyy").format(myDate).split("-");
		 sb.append(date_parse[0]+" "+date_parse[1]+" "+date_parse[2]);
		// sb.append(date_parse[0]+" "+date_parse[1]+" "+date_parse[2]);
		 date1 = sb.toString();	
		Log.e("Date in dd-MMM-yy", " "+date_parse.toString());
		System.out.println(date_parse[0]+" "+date_parse[1]+" "+date_parse[2]);
	} 
		catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

		return date1;

	}
	
	
	
	public static String getDDMMMyy(String date1)
	{
		try {
			
			//String date1="02-02-1989";
			StringBuilder sb = new StringBuilder();
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		
		 Date myDate = formatter.parse(date1);
		 
		
		 
		
		
		 String date_parse[]=new SimpleDateFormat("dd-MMM-yy").format(myDate).split("-");
		 
		 sb.append(date_parse[0]+"-"+date_parse[1]+"-"+date_parse[2]);
		 
		// sb.append(date_parse[0]+" "+date_parse[1]+" "+date_parse[2]);		
		 
		 date1 = sb.toString().toUpperCase();	
		Log.e("Date in dd-MMM-yy", " "+date_parse.toString());
		System.out.println(date_parse[0]+" "+date_parse[1]+" "+date_parse[2]);
		 
		 
	} 
		catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

		return date1;

	}
	
	public static String getCurrentTime()
	{
		
		
		Calendar c= Calendar.getInstance();
		
		String hour_str="";
		String minute_str="";
		String second_str="";
		String am_pm_str="";
		
		int hour = c.get(Calendar.HOUR);
		int minute = c.get(Calendar.MINUTE);
		int second = c.get(Calendar.SECOND);
		
		int am_pm=c.get(Calendar.AM_PM);
		if(String.valueOf(hour).length()==1){
			hour_str="0"+hour;
				
				
		}
		else{
			hour_str= String.valueOf(hour);
		}
		
		if(String.valueOf(minute).length()==1){
			minute_str="0"+minute;
				
				
		}
		else{
			minute_str= String.valueOf(minute);
		}
		
		if(String.valueOf(second).length()==1){
			second_str="0"+second;
				
				
		}
		else{
			second_str= String.valueOf(second);
		}
		
		if(am_pm==0)
		{
			am_pm_str="AM";
		}
		else
		{
			am_pm_str="PM";
		}
		
		return hour_str+":"+minute_str+":"+second+" "+am_pm_str; 
		//Calendar c=Calendar.getInstance();
		/*int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);
		if(String.valueOf(day).length()==1){
			if(String.valueOf(month).length()==1)
			return  "0"+day+"/0"+month+"/"+year+" "+time_str;
			else
				return  "0"+day+"/"+month+"/"+year+" "+time_str;
				
				
		}
		else{
			if(String.valueOf(month).length()==1)
				return  ""+day+"/0"+month+"/"+year+" "+time_str;
				else
					return  ""+day+"/"+month+"/"+year+" "+time_str;
		}*/
			
	}
	
	
	
	 public static String getCurrentDateInDisplayFormat(Context mcoContext, String date_str)
		{
		 
		 String date="";
		 
		 String[]str=date_str.split("-");
		 String year=str[0];
		 String month=str[1];
		 String day=str[2];
		 
		 date=day+"-"+month+"-"+year;
		
			 return date;
		}
	
	
	
	
	
	  
	  public static String getImageNameFromUri(Uri contentNAME, Activity activity) {
	      String result;
	      Cursor cursor = activity.getContentResolver().query(contentNAME, null, null, null, null);
	      if (cursor == null) { // Source is Dropbox or other similar local file path
	          result = contentNAME.getPath();
	      } else { 
	          cursor.moveToFirst(); 
	          int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DISPLAY_NAME);
	          result = cursor.getString(idx);
	          cursor.close();
	      }
	      
	      result=getImageName(result);
	      return result;
	  }
	  
	  public static String getImageName(String imageName)
	  {
		 String name=null;
		  String[] date=imageName.split("\\.");
		  
		
		   name=date[0];
		   name=name+".png";
		 //name=imageName;
		
		  
		  return name;
	  }
	  
	  public static String getImageExtFromUri(Uri contentNAME, Activity activity) {
	      String result;
	      Cursor cursor = activity.getContentResolver().query(contentNAME, null, null, null, null);
	      if (cursor == null) { // Source is Dropbox or other similar local file path
	          result = contentNAME.getPath();
	      } else { 
	          cursor.moveToFirst(); 
	          int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DISPLAY_NAME);
	          result = cursor.getString(idx);
	          cursor.close();
	      }
	      
	      result=getImageExt(result);
	      return result;
	  }
	  
	  
	  public static String getImageExt(String imageName)
	  {
		 String ext=null;
		  String[] date=imageName.split("\\.");
		  
		
		   ext=date[1];
		
		  
		  return ext;
	  }
	  
	  public  static String convertBitmapToBase64(Bitmap b)
		 {
			 ByteArrayOutputStream stream = new ByteArrayOutputStream();
			 b.compress(Bitmap.CompressFormat.PNG, 100, stream);
			 byte[] byteArray = stream.toByteArray();
			/* int length=byteArray.length;
			 size =length/1024;
			 Log.e("size", ""+size);*/
			// String image_size=byteArray
			 String imageEncoded = Base64.encodeToString(byteArray, Base64.NO_WRAP);

			
			    return imageEncoded;
		 }
	  
	  public static void saveBase64InSharePrefrence_profilePicture(Activity activity, Bitmap profilePicture)
	  {
		  SharedPreferences mPrefs = activity.getSharedPreferences("Location", Context.MODE_PRIVATE);
		  Editor editor = mPrefs.edit();
	      editor.putString("USER_PROFILE_PICTURE", encodeTobase64(profilePicture));
	     
	     
	      editor.commit();
	  }
	  
	// Encode bitmap in base64
	  
	  public static String encodeTobase64(Bitmap image) {
		    Bitmap immage = image;
		    ByteArrayOutputStream baos = new ByteArrayOutputStream();
		    immage.compress(Bitmap.CompressFormat.PNG, 100, baos);
		    byte[] b = baos.toByteArray();
		    String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

		    Log.d("Image Log:", imageEncoded);
		    return imageEncoded;

		}
	  
	  // get bitmap from base64
	  public static Bitmap decodeBase64(String input) {
	       byte[] decodedByte = Base64.decode(input, 0);
	       return BitmapFactory
	               .decodeByteArray(decodedByte, 0, decodedByte.length);
	   }


	public static String getDDmmYYYYByCalenderInstanse(GregorianCalendar c) {

		
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH);
			int day = c.get(Calendar.DAY_OF_MONTH);
			if(String.valueOf(day).length()==1){
				if(String.valueOf(month).length()==1)
				return  "0"+day+"-0"+month+"-"+year;
				else
					return  "0"+day+"-"+month+"-"+year;
					
					
			}
			else{
				if(String.valueOf(month).length()==1)
					return  ""+day+"-0"+month+"-"+year;
					else
						return  ""+day+"-"+month+"-"+year;
			}
				
		}
	
	
	
	
	public static String getNextDDmmYYYYByCalenderInstanse(GregorianCalendar c) {

		
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		c.add(Calendar.MONTH, 1);
		 month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);
		if(String.valueOf(day).length()==1){
			if(String.valueOf(month).length()==1)
			return  "0"+day+"-0"+month+"-"+year;
			else
				return  "0"+day+"-"+month+"-"+year;
				
				
		}
		else{
			if(String.valueOf(month).length()==1)
				return  ""+day+"-0"+month+"-"+year;
				else
					return  ""+day+"-"+month+"-"+year;
		}
	}

	public  static  void setVersionCode(Context activity, int id) {
		// TODO Auto-generated method stub
		Editor editor= activity.getSharedPreferences(SharePref.PREF_NAME, Context.MODE_PRIVATE).edit();
		editor.putInt(SharePref.VERSION_CODE, id);
		editor.commit();
		Log.e("vesrion set", ""+id);
		
	}
	
	
	
	public static int getAppVersion(Context context) {
		  try {
		      PackageInfo packageInfo = context.getPackageManager()
		              .getPackageInfo(context.getPackageName(), 0);
		      return packageInfo.versionCode;
		  } catch (NameNotFoundException e) {
		      // should never happen
		      throw new RuntimeException("Could not get package name: " + e);
		  }
		}
	
	
	
	public static String getDeviceId(Context ctx)
	  {
		   String android_id = Secure.getString(ctx.getContentResolver(), Secure.ANDROID_ID);
		   AppConstant.DEVICE_ID=android_id;
		   Log.e("DEVICE_ID_VALUE", AppConstant.DEVICE_ID);
	/*	   SharedPreferences  mPrefs = ctx.getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE);
		   Editor prefsEditor = mPrefs.edit();
		   prefsEditor.putString("DEVICE_ID", android_id);
		   prefsEditor.commit();*/
		   return android_id;
	  }
	
	
	
	public static String getCurrentDate()
	{
		Calendar calendar = Calendar.getInstance();
		Date today = calendar.getTime();
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		String todayAsString = dateFormat.format(today);
		// tomorrow_date =
		// HelperMethods.getDDmmYYYY(tomorrowAsString.toString());
		// current_date = HelperMethods.getDDmmYYYY(todayAsString.toString());
		String current_date = todayAsString.toString();
		Log.e("current_date", " = " + current_date);
				return current_date;
	}
	
	public static String getFifteenDaysOldDate()
	{
		Calendar calendar = Calendar.getInstance();
		Date today = calendar.getTime();
		calendar.add(Calendar.DAY_OF_MONTH, -15);
		Date fiffteen_days_old = calendar.getTime();
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		String fiffteen_days_oldAsString = dateFormat.format(fiffteen_days_old);
		// tomorrow_date =
		// HelperMethods.getDDmmYYYY(tomorrowAsString.toString());
		// current_date = HelperMethods.getDDmmYYYY(todayAsString.toString());
		String tomorrow_date = fiffteen_days_oldAsString.toString();
		Log.e("tomorrow_date", " = " + tomorrow_date);
		return tomorrow_date;
	}
	
	public static int getDateDifferenceInDays(String date_from_str, String date_to_str)
	{
		SimpleDateFormat dfDate  = new SimpleDateFormat("dd-MM-yyyy");
	    Date date_from = null;
	    Date date_to= null;
	    Calendar cal = Calendar.getInstance();
	    try {
	            date_from = dfDate.parse(date_from_str);
	            date_to = dfDate.parse(date_to_str);
	        } catch (java.text.ParseException e) {
	            e.printStackTrace();
	        }

	    int diffInDays = (int) ((date_to.getTime() - date_from.getTime())/ (1000 * 60 * 60 * 24));
	    //System.out.println(diffInDays);
	    return diffInDays;
	}

}
