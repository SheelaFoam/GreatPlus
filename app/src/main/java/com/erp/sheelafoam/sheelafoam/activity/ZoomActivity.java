package com.erp.sheelafoam.sheelafoam.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.content.IntentCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.erp.sheelafoam.R;
import com.erp.sheelafoam.touch.TouchImageView;
import com.erp.sheelafoam.sheelafoam.complaint.ComplainDetails;
import com.erp.sheelafoam.sheelafoam.utility.DBHelper;

public class ZoomActivity extends Activity implements OnClickListener {
	
	TouchImageView imageview;
	RelativeLayout rlayout_holder;
	 DBHelper dbConn= null;
	 
	 //UI controls
	 
	 ImageButton btn_logout;
	 TextView textview_back;
	 
	 private SharedPreferences mPrefs;
	 String user_role="";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub  
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_zoom);
		 mPrefs = getSharedPreferences("Location", Context.MODE_PRIVATE);
	        user_role=mPrefs.getString("op_user_type", "").toLowerCase();
		dbConn =  DBHelper.getDBHelper(this);
		rlayout_holder=(RelativeLayout)findViewById(R.id.rlayout_holder);
		 btn_logout=(ImageButton)findViewById(R.id.btn_logout);
		 textview_back=(TextView)findViewById(R.id.textview_back);
		TouchImageView imageview=new TouchImageView(ZoomActivity.this);
		Bitmap bitmap_in_constructor= ComplainDetails.getInstance().getBitMap();
		
		btn_logout.setOnClickListener(this);
		
		int width=bitmap_in_constructor.getWidth();
		 int height=bitmap_in_constructor.getHeight();
		 
		 //set user type to the header.
		 
	     textview_back.setText("greatplus"+" "+user_role+" "+"app");

	   		SpannableString spannable_string=new SpannableString(textview_back.getText().toString().trim());
	   		spannable_string.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.color_great)), 0, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
	   		spannable_string.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.color_plus)), 5, 10, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
	   		
	   		textview_back.setText(spannable_string);
			 		
	@SuppressWarnings({ "static-access", "unused" })
    Bitmap bitmap_in_constructor1=bitmap_in_constructor.createScaledBitmap(bitmap_in_constructor, 650, 450, false);
	imageview.setImageBitmap(bitmap_in_constructor1); 
		
		
		
		rlayout_holder.addView((View)imageview);
		
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId())
		{
		   case R.id.btn_logout:
			   logout(this);
				break;
		

			default:
				break;
		}
		
	}
	
    /**
     * Method: This method used for logout from current session. Database also removed when get logout from device.
     * **/
    
public  void logout(Activity activity) {
		
		
		SharedPreferences prefs = activity.getSharedPreferences("Location",
				Context.MODE_PRIVATE);
		Editor editor = prefs.edit();

		editor.clear();
		editor.commit();
		
		removeAll();
		
		Intent intent = new Intent(activity, SplashActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
		activity.startActivity(intent);
	}

/**
 * Remove all users and groups from database.
 */
public void removeAll()
{
    // db.delete(String tableName, String whereClause, String[] whereArgs);
    // If whereClause is null, it will delete all rows.
    SQLiteDatabase db = dbConn.getWritableDatabase(); // helper is object extends SQLiteOpenHelper
    db.delete(DBHelper.TABLE_COMPLAINT, null, null);
    //db.execSQL ("drop table "+DBHelper.TABLE_COMPLAINT);
    
}

}
