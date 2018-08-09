package com.erp.sheelafoam.sheelafoam.utility;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.Settings.Secure;
import android.support.v4.content.IntentCompat;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.erp.sheelafoam.R;
import com.erp.sheelafoam.sheelafoam.activity.SplashActivity;

import org.apache.http.conn.util.InetAddressUtils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressLint("SimpleDateFormat")
public class GlobalVariables extends Application {
	
	
	
//	public static  final String CREATE_NEW_DTP="No DTP available on this date.You want to create new DTP?";

//	public static  final String NETWORK_ERROR="Network error";
	public static  final String DEVICE_ID_KEY="DeviceId";
	public static String DEVICE_ID_VALUE="";
	public static final String API_STATIC_KEY="ApiKey";
	public static final String API_STATIC_VALUE="aa97805dd5ff7027ac9e21d88049b787";
	public static  final String IP_ADDRESS_KEY="IpAddress";
	public static String IP_ADDRESS_VALUE="";
	public static String DEVICE_ACCESS_TOKEN="";
	
	public static final String AUTHENTICATION_TOKEN_KEY="AccessToken";
	public static String AUTHENTICATION_TOKEN_VALUE="";
	
	
	public static String DEVICE_MODEL="";
	public static String DEVICE_OS="";
	
	static Typeface opensan_regular;
	static Typeface roboto_regular;
	public  static String imsiSIM2;
	public static String MASTER_DEALER_CATEGORY = "";
	public static String DEALER_CATEGORY = "";
 
	// Flag for open o not a new foam when place order.
	
	
	// App Version code
	
	public static int APP_VERSION_CODE;
	
	/**
	 * App massages
	 * **/
	public static final String BLANK_REMARK="Please enter your remark";
	public static final String SELECT_IMAGES="Please select at least one image";
	public static final String NO_RECORD="No record found";
	public static final String CONNECTION_ERROR="Connection error";
	public static final String BLANK_SERIAL_NO_ERROR="Please Enter Serial Number";
	public static final String BLANK_DEALER_CODE="Please Enter Purchase Dealer Code";
	public static final String DATE_DIFFERENCE_ERROR="FROM Date must be less than TO Date";
	
	public static final String CURRENT_DATE_ERROR="Your current date is wrong";
	public static final String OTP_SENDING="OTP is sending on registerd mobile number";
	
	
	
	public static final String INSERT_KEY="INSERT";
	public static final String UPDATE_KEY="UPDATE";
	public static final String DELETE_KEY="DELETE";
	//public static final String URL_SECOND="http://52.0.133.114/greatPlus/api.php";
	
	
	public static final String URL_SECOND="http://125.19.46.245/greatPlus/api.php";
	public static final String URL_FIRST="http://125.19.46.252/sheelafoams/apis.php";
	public static final String IMAGE_BASE_URL="http://125.19.46.252/sheelafoams/uploads/";
	int day;
	int month;
	int year;
	//public static boolean isFixedSize;
	/**
	 * flag for call category Api at once when come on this UI.
	 * **/
	
	
	
	
	public static void SetFont(Activity mActivity) {
		// TODO Auto-generated constructor stub
	
	 try {
	    
	    	
	    	opensan_regular = Typeface.createFromAsset(mActivity.getAssets(),
		            "fonts/opensans_regular.ttf");
			roboto_regular= Typeface.createFromAsset(mActivity.getAssets(),
		            "fonts/opensans_regular.ttf");
			
	      final Field StaticField = Typeface.class
	                .getDeclaredField( "MONOSPACE");
	      
	        StaticField.setAccessible(true);
	        StaticField.set(null, roboto_regular);
	        
	        final Field StaticField2 = Typeface.class
	                .getDeclaredField( "SANS_SERIF");
	        StaticField2.setAccessible(true);
	        
	        StaticField2.set(null, opensan_regular);
	        
	 
	        
	    } catch (NoSuchFieldException e) {
	        e.printStackTrace();
	    } catch (IllegalAccessException e) {
	        e.printStackTrace();
	    }
	}
  


    
    
    public static boolean isEmailValid(String email) {
    	   
        Pattern pattern;
        Matcher matcher;
     
        String regExpn = "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
          + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
          + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
          + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
          + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
          + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

        CharSequence inputStr = email;

        pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
        matcher = pattern.matcher(inputStr);

        if (matcher.matches())
         return true;
        else
         return false;
       }
    


    
  public static void setProfilePicture(Activity activity, ImageView imageview)
  {
	  SharedPreferences mPrefs = activity.getSharedPreferences("Location", Context.MODE_PRIVATE);
	  imageview.setImageBitmap(GlobalVariables.decodeBase64(mPrefs.getString("USER_PROFILE_PICTURE","")));
  }
  
  public static Bitmap getImage(String path , Context _c) {
	  AssetManager mngr = _c.getAssets();
	  InputStream in = null;
	  try {
	   in = mngr.open("flags/"+ path);  
	  } catch (Exception e) {
	   e.printStackTrace();
	  }
	  
	  Bitmap temp = BitmapFactory.decodeStream(in, null, null);
	
	  return temp;
	 }
  
  public static ArrayList<Integer> getDeviceSize(Activity activity)
	{
	  ArrayList<Integer> list=null;
		Display display = activity.getWindowManager().getDefaultDisplay();
	
		int width= display.getWidth();
		 int height = display.getHeight();
		 list=new ArrayList<Integer>();
		 list.add(width);
		 list.add(height);
		 return list;
	}
   
  public static void hideSoftKeyboard(Activity activity) {
		if (activity.getCurrentFocus() != null && activity.getCurrentFocus() instanceof EditText) {
			InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(activity.getWindow().getCurrentFocus() 
					.getWindowToken(), 0);

		}
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
  
  
  public static void defaultOneButtonDialog(Activity activity, String msg)
  {
	  AlertDialog.Builder builder = new AlertDialog.Builder(activity);
	  builder.setMessage(msg)
	         .setCancelable(false)
	         .setPositiveButton("OK", new DialogInterface.OnClickListener() {
	             public void onClick(DialogInterface dialog, int id) {
	                  //do things

	            	 dialog.cancel();
	             }
	         });
	  AlertDialog alert = builder.create();
	  alert.show();
  }
  
  public static void TwoButtonAlertDialog(Activity activity, String msg)
  {
	  AlertDialog.Builder builder = new AlertDialog.Builder(activity);
	  builder.setMessage(msg)
	         .setCancelable(false)
	         .setPositiveButton("OK", new DialogInterface.OnClickListener() {
	             public void onClick(DialogInterface dialog, int id) {
	                  //do things
	            	 
	            	 dialog.cancel();
	             }
	         })
	  .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		
		@Override
		public void onClick(DialogInterface dialog, int id) {
			// TODO Auto-generated method stub
			 dialog.cancel();
		}
	});
	  AlertDialog alert = builder.create();
	  alert.show();
  }
  
  
  public static String getTimeStamp()
  {
	  String timeStamp=null;
	  
	  SimpleDateFormat dateFormatUtc = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");
	  dateFormatUtc.setTimeZone(TimeZone.getTimeZone("UTC"));
	  timeStamp=dateFormatUtc.toString();
	  
	  return timeStamp;

  }
  
  public static String timeStamp()
  {
	  DateFormat df = DateFormat.getTimeInstance();
	  df.setTimeZone(TimeZone.getTimeZone("utc"));
	  String utcTime = df.format(new Date());
	  return utcTime;

  }
  
  
  
  public static void addStar(TextView textview, String msg)
  {
  	String styledText = msg + "<font color='red'>*</font>";
  	textview.setText(Html.fromHtml(styledText), TextView.BufferType.SPANNABLE);
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
  
  public static String getImageName(String imageName)
  {
	 String name=null;
	  String[] date=imageName.split("\\.");
	  
	
	   name=date[0];
	   name=name+".png";
	 //name=imageName;
	
	  
	  return name;
  }
  
  
  
  public static String getCurrentDateDay(String date)
  {
	 String day=null;
	  String[] array_date=date.split("-");
	  
	  day=array_date[2];
	  
	
	  // ext=date[1];
	
	  
	  return day;
  }
  
  public static String getImageNameFromWebUrl(String imageName)
  {
	 String name=null;
	  String[] data=imageName.split("/");
	  
	
	   name=data[data.length-1];
	  // name=name+".png";
	 //name=imageName;
	
	  
	  return name;
  }
  
  
  
  public  static String convertBitmapToBase64(Bitmap b)
	 {
		 ByteArrayOutputStream stream = new ByteArrayOutputStream();
		 b.compress(Bitmap.CompressFormat.PNG, 100, stream);
		 byte[] byteArray = stream.toByteArray();
		 String imageEncoded = Base64.encodeToString(byteArray, Base64.DEFAULT);

		    Log.d("encoded image","encoded image"+imageEncoded);
		    return imageEncoded;
	 }
  
  public static String getDeviceId(Context ctx)
  {
	   String android_id = Secure.getString(ctx.getContentResolver(), Secure.ANDROID_ID);
	   DEVICE_ID_VALUE=android_id;
	   
	   Log.e("DEVICE_ID_VALUE", DEVICE_ID_VALUE);
	   
	   SharedPreferences mPrefs = ctx.getSharedPreferences("Location", Context.MODE_PRIVATE);
	   Editor prefsEditor = mPrefs.edit();
	   prefsEditor.putString("DEVICE_ID", android_id);
	   prefsEditor.commit();
	   return android_id;
  }
  

  
  
  public static void getIPAddress(Context ctx)
 {
	  //http://stackoverflow.com/questions/11015912/how-do-i-get-ip-address-in-ipv4-format
     
     String ipv4="";
    
     try
     {
             Enumeration<NetworkInterface> enumnet = NetworkInterface.getNetworkInterfaces();
             NetworkInterface netinterface = null;
            
         while(enumnet.hasMoreElements())
         {
             netinterface = enumnet.nextElement();
            
             for (Enumeration<InetAddress> enumip = netinterface.getInetAddresses();
                  enumip.hasMoreElements();)
             {
                 InetAddress inetAddress = enumip.nextElement();
                
                 if(!inetAddress.isLoopbackAddress() && InetAddressUtils.isIPv4Address(ipv4 = inetAddress.getHostAddress()))
                 {
                     
                     
                     IP_ADDRESS_VALUE=ipv4;
                     Log.e("IP_ADDRESS", IP_ADDRESS_VALUE);
                     
                   SharedPreferences mPrefs = ctx.getSharedPreferences("Location", Context.MODE_PRIVATE);
              	   Editor prefsEditor = mPrefs.edit();
              	   prefsEditor.putString("IP_ADDRESS_VALUE", ipv4);
              	   prefsEditor.commit();
                    
                     break;
                 }
             }
         }
     }
     catch (SocketException e)
     {
         e.printStackTrace();
     }

    
 }

  
  public static String getDeviceModel()
  {
	  String device_model="";
	  try
	  {
		  device_model=android.os.Build.MODEL;
		  DEVICE_MODEL=device_model;
		  Log.e("DEVICE_MODEL", DEVICE_MODEL);
	  }
	  catch(Exception e)
	  {
		  
	  }
	  
	  return device_model;
  }
  
  
  public static String getDeviceOs()
  {
	  
	  StringBuilder builder = new StringBuilder();
	  try
	  {
		  
		  builder.append("android : ").append(Build.VERSION.RELEASE);

		  Field[] fields = Build.VERSION_CODES.class.getFields();
		  for (Field field : fields) {
		      String fieldName = field.getName();
		      int fieldValue = -1;

		      try {
		          fieldValue = field.getInt(new Object());
		      } catch (IllegalArgumentException e) {
		          e.printStackTrace();
		      } catch (IllegalAccessException e) {
		          e.printStackTrace();
		      } catch (NullPointerException e) {
		          e.printStackTrace();
		      }

		      if (fieldValue == Build.VERSION.SDK_INT) {
		          builder.append(" : ").append(fieldName).append(" : ");
		          builder.append("sdk=").append(fieldValue);
		      }
		  
	  }
	 }
	  catch(Exception e)
	  {
		  
	  }
	  Log.e("DEVICE_OS", builder.toString());
	  return builder.toString();
  }
	  
 
  public static String getPhoneNumber_one(Activity activity)
  {
	  //http://stackoverflow.com/questions/22710330/how-to-get-phone-number-of-android-device-using-sim-card-or
	 //http://stackoverflow.com/questions/14517338/android-check-whether-the-phone-is-dual-sim
	  
	  String phone_number="";
	  
	  TelephonyManager tm = (TelephonyManager)activity.getSystemService(TELEPHONY_SERVICE);
	  int simState = tm.getSimState();
	  if(simState== TelephonyManager.SIM_STATE_ABSENT)
	    {
	     // Toast.makeText(activity, "No Sim card available", Toast.LENGTH_LONG).show();	
	    }
	  else
	  {
		  if(simState== TelephonyManager.SIM_STATE_READY)
	    	{
	    		 
			  phone_number = tm.getLine1Number();
				   
	    	}
	  }
	 
	  
	  
	  return phone_number;
  }
  
  public static String getPhoneNumber_two()
  {
	  String phone_number="";
	  
	  return phone_number;
  }
  
  public static String isDualSIM(Context context) {
      return GlobalVariables.imsiSIM2 =getDeviceIdBySlot(context, "test", 1);
  }
  
  private static String getDeviceIdBySlot(Context context, String predictedMethodName, int slotID) {

      String imsi = null;

      TelephonyManager telephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

      try{

          Class<?> telephonyClass = Class.forName(telephony.getClass().getName());

          Class<?>[] parameter = new Class[1];
          parameter[0] = int.class;
          Method getSimID = telephonyClass.getMethod(predictedMethodName, parameter);

          Object[] obParameter = new Object[1];
          obParameter[0] = slotID;
          Object ob_phone = getSimID.invoke(telephony, obParameter);

          if(ob_phone != null){
              imsi = ob_phone.toString();

          }
      } catch (Exception e) {
          e.printStackTrace();
         
      }

      return imsi;
  }
  
  public  static String roundOffUptoTwo(String val, int numberOfDigitsAfterDecimal) {
      double p = (float) Math.pow(10,numberOfDigitsAfterDecimal);
     double Rval = Double.parseDouble(val) * p;
     double tmp = Math.floor(Rval);
  
    DecimalFormat three = new DecimalFormat("###0.00");
 
 
   return  three.format((double)tmp/p);
  //return (double)tmp/p;
  }
  
	public static void logout(Activity activity) {
		
		
		SharedPreferences prefs = activity.getSharedPreferences("Location",
				Context.MODE_PRIVATE);
		Editor editor = prefs.edit();

		editor.clear();
		editor.commit();
		
		
		
		Intent intent = new Intent(activity, SplashActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
		activity.startActivity(intent);
	}
	
	public  static Bitmap getRoundedCornerBitmap(Activity activity, Bitmap bitmap, int pixels)
			   throws NullPointerException {
			  Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
			    bitmap.getHeight(), Config.ARGB_8888);
			  Canvas canvas = new Canvas(output);
			  final int color = Color.parseColor("#000000");
			  final Paint paint = new Paint();
			  final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
			  final RectF rectF = new RectF(rect);
			  final float roundPx = pixels;
			  paint.setAntiAlias(true);
			  canvas.drawARGB(0, 0, 0, 0);
			  paint.setColor(color);
			  canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
			  paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
			  canvas.drawBitmap(bitmap, rect, rect, paint);
			  
			  
			  
		
			 // paint.setColor(getResources().getColor(R.color.profile_border_color));
			  paint.setStyle(Paint.Style.STROKE);
			  //paint.setStrokeWidth(getResources().getDimension(R.dimen.default_text_size_small_very_extra_extra));
			  paint.setStrokeWidth(activity.getResources().getDimension(R.dimen.default_inner_text_padding_very_small));
			  canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
			  
			  return output;
			 }
		 
	public static void makeCall(Activity activity, String number)
	{
		
		
		
		Intent intent = new Intent(Intent.ACTION_CALL);

		intent.setData(Uri.parse("tel:" +number));
		//intent.setData(Uri.parse(number));
		activity.startActivity(intent);

	
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
	         activity.finish();
	         Log.i("Finished sending email...", "");
	      } catch (android.content.ActivityNotFoundException ex) {
	         Toast.makeText(activity,
	         "There is no email client installed.", Toast.LENGTH_SHORT).show();
	      }
	   }
	 public static void saveBase64InSharePrefrence_profilePicture(Activity activity, Bitmap profilePicture)
	  {
		  SharedPreferences mPrefs = activity.getSharedPreferences("Location", Context.MODE_PRIVATE);
		  Editor editor = mPrefs.edit();
	      editor.putString("USER_PROFILE_PICTURE", encodeTobase64(profilePicture));
	     
	     
	      editor.commit();
	  }
	 
	 public static void saveCurrentDay(String currentDay, String current_date, Activity activity)
		{
			 SharedPreferences mPrefs = activity.getSharedPreferences("Location", Context.MODE_PRIVATE);
			
			 Editor editor=mPrefs.edit();
			 
			 editor.putString("CURRENT_DAY", currentDay);
			 editor.putString("CURRENT_DATE", current_date);
			 
			 editor.commit();
		}
	 
	 public static String getCurrentDay(Activity activity)
		{
		 
		 String day="";
			 SharedPreferences mPrefs = activity.getSharedPreferences("Location", Context.MODE_PRIVATE);
			
			day=mPrefs.getString("CURRENT_DAY", "");
			 
			 return day;
		}
	 
	 
	 public static String getCurrentDate(Activity activity)
		{
		 
		 String date="";
			 SharedPreferences mPrefs = activity.getSharedPreferences("Location", Context.MODE_PRIVATE);
			
			date=mPrefs.getString("CURRENT_DATE", "");
			Log.e("CURRENT_DATE", date);
			 
			 return date;
		}

	 
	 public static String getCurrentDateInDisplayFormat(Activity activity, String date_str)
		{
		 Log.e("convert date", date_str);
		 String date="";
		 
		 String[]str=date_str.split("-");
		 String year=str[0];
		 String month=str[1];
		 String day=str[2];
		 
		 date=day+"-"+month+"-"+year;
			
//		 try
//		 {
//			 DateFormat formatter = new SimpleDateFormat("dd-mm-yyyy");
//			 
//			
//			 Date d = (Date)formatter.parse(date);
//			 
//			 Log.e("convert date", d.toString());
//			 
//			 
//			 SimpleDateFormat  format = new SimpleDateFormat("dd MM yyyy");
//			 
//			 date=format.format(d);
//			
//			 
//		 }
//		 catch(Exception e)
//		 {
//			 
//		 }
		
	
			 
			 return date;
		}
	 
	 public static String getUserEmail(Activity act)
	 {
		 SharedPreferences mPrefs = act.getSharedPreferences("Location", Context.MODE_PRIVATE);
		 
		 return mPrefs.getString("op_user_name", "");
	 }
	 
	 
	 public static int getDateDifferenceInDays(String date)
	 {
		 
		SimpleDateFormat dfDate  = new SimpleDateFormat("dd-MM-yyyy");
	     Date oldDate = null;
	     Date currentDate = null;
	    Calendar cal = Calendar.getInstance();
	    try {
	    	oldDate = dfDate.parse(date);
	    	
	         Log.e("old date", oldDate.toString());
	     	
    	     currentDate = dfDate.parse(dfDate.format(cal.getTime()));//Returns 15/10/2012
    	     Log.e("current date", currentDate.toString());
    	    	
	        } catch (java.text.ParseException e) {
	            e.printStackTrace();
	        }

	    int diffInDays = (int) ((oldDate.getTime() - currentDate.getTime())/ (1000 * 60 * 60 * 24));
	    Log.e("diffrence in days ", ""+diffInDays);
	    return diffInDays;
	 }
			 
		 
	 public static void clearAllPendingActivityOnNotification(Activity activity)
	 {

			Intent intent = new Intent(activity, SplashActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
					| IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
			activity.startActivity(intent);
	 }
	 
	
	  
}
