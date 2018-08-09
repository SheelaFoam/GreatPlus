package com.erp.sheelafoam.sheelafoam.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.erp.sheelafoam.R;
import com.erp.sheelafoam.sheelafoam.utility.AsyncTaskListner;
import com.erp.sheelafoam.sheelafoam.utility.ConnectionDetector;
import com.erp.sheelafoam.sheelafoam.utility.GlobalVariables;
import com.erp.sheelafoam.sheelafoam.utility.MyAsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

public class SupportActivity extends AppCompatActivity implements OnClickListener, AsyncTaskListner {

	Context mContext;

	/*xml textview controls*/
	TextView textview_timing_one, textview_tollfree_no, textview_tollfree_no1, textview_cc_email;
	TextView textview_direct_no, textview_tollfree_no_it, textview_direct_no_it;
	RelativeLayout rlayout_cc_direct_no_lable;

	/*String variables for hold response data*/
	String op_cc_timing = "", op_cc_toll_free_no = "", op_cc_email_id = "", op_cc_direct_no = "";
	String op_it_toll_free_no = "", op_it_direct_no = "";

	ImageButton btn_logout;

	/*internet connection detector object*/
	ConnectionDetector con;

	Toolbar toolbar;
	TextView toolbarTitle;

	private final int RequestPermissionCode = 14;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_support);

		mContext = SupportActivity.this;
		
		/*initializing connection detector class*/
		con = new ConnectionDetector(this);
		
		/*initializing xml controls*/
		initialization();

		EnableRuntimeReadSmsPermission();
		
		/*add onclick listner*/
		addOnclickListner();

		if (con.isConnectingToInternet()) {
			setJsonRequest();
		} else {
			GlobalVariables.defaultOneButtonDialog(SupportActivity.this, getResources().getString(R.string.default_network_alert_msg));
		}

	}

	/***********************************User Define Methods*****************************************/
	
	
	/*user define method for initialize xml controls start*/
	public void initialization() {
		toolbar = (Toolbar) findViewById(R.id.app_toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		toolbarTitle = (TextView) findViewById(R.id.app_toolbar_title);
		toolbarTitle.setText("Support");
		textview_timing_one = (TextView) findViewById(R.id.textview_timing_one);
		textview_tollfree_no = (TextView) findViewById(R.id.textview_tollfree_no);
		textview_tollfree_no1 = (TextView) findViewById(R.id.textview_tollfree_no1);
		textview_cc_email = (TextView) findViewById(R.id.textview_cc_email);
		textview_direct_no = (TextView) findViewById(R.id.textview_direct_no);
		textview_tollfree_no_it = (TextView) findViewById(R.id.textview_tollfree_no_it);
		textview_direct_no_it = (TextView) findViewById(R.id.textview_direct_no_it);
		//btn_logout=(ImageButton)findViewById(R.id.btn_logout);

		rlayout_cc_direct_no_lable = (RelativeLayout) findViewById(R.id.rlayout_cc_direct_no_lable);

	}

	public void addOnclickListner() {
		textview_tollfree_no.setOnClickListener(this);
		textview_direct_no.setOnClickListener(this);
		textview_cc_email.setOnClickListener(this);
		textview_tollfree_no1.setOnClickListener(this);
		//btn_logout.setOnClickListener(this);
		rlayout_cc_direct_no_lable.setOnClickListener(this);
	}

	/*user define method testSetJsonRequest start*/
	public void setJsonRequest() {

		try {
			JSONObject jObject_request = new JSONObject();
			jObject_request.put("request", "getContactUs");


			Log.e("##request##", jObject_request.toString());
			new MyAsyncTask(SupportActivity.this, jObject_request).execute();
		} catch (Exception e) {

		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {
			case R.id.btn_logout:

				GlobalVariables.logout(SupportActivity.this);

				break;
			case R.id.textview_direct_no:
				Intent sIntent = new Intent(Intent.ACTION_CALL, Uri


						.parse("tel:"+textview_direct_no.getText().toString()));


				sIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);


				if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
					// TODO: Consider calling
					//    ActivityCompat#requestPermissions
					// here to request the missing permissions, and then overriding
					//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
					//                                          int[] grantResults)
					// to handle the case where the user grants the permission. See the documentation
					// for ActivityCompat#requestPermissions for more details.
					return;
				}
				startActivity(sIntent);
				break;

			case R.id.textview_tollfree_no:
				Intent intent = new Intent(Intent.ACTION_CALL, Uri


						.parse("tel:"+textview_tollfree_no.getText().toString()));


				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);


				if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
					// TODO: Consider calling
					//    ActivityCompat#requestPermissions
					// here to request the missing permissions, and then overriding
					//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
					//                                          int[] grantResults)
					// to handle the case where the user grants the permission. See the documentation
					// for ActivityCompat#requestPermissions for more details.
					return;
				}
				startActivity(intent);
				break;
			case R.id.textview_tollfree_no1:
				Intent intent_one = new Intent(Intent.ACTION_CALL, Uri


						.parse("tel:"+textview_tollfree_no1.getText().toString()));


				intent_one.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent_one);
				break;
			case R.id.textview_cc_email:
//				Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
//				emailIntent.setData(Uri.parse("mailto:")); // only email apps should handle this
//				emailIntent.putExtra(Intent.EXTRA_EMAIL, textview_cc_email.getText().toString());
//				emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Your Subject");
//				if (emailIntent.resolveActivity(getPackageManager()) != null) {
//					startActivity(emailIntent);
//				}

				Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
						"mailto",textview_cc_email.getText().toString(), null));
				emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
				emailIntent.putExtra(Intent.EXTRA_TEXT, "Body");
				startActivity(Intent.createChooser(emailIntent, "Send email..."));
				break;

		
		}
	}

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
						//GlobalVariables.defaultOneButtonDialog(SupportActivity.this, getResources().getString(R.string.default_no_data_found_alert_msg));
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


	public void EnableRuntimeReadSmsPermission() {

		if (ActivityCompat.checkSelfPermission(SupportActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

			if (ActivityCompat.shouldShowRequestPermissionRationale(SupportActivity.this, Manifest.permission.CALL_PHONE)) {

				// Printing toast messa
				//
				//
				// ge after enabling runtime permission.
				//Toast.makeText(MainActivity.this,"CAMERA permission allows us to Access CAMERA app", Toast.LENGTH_LONG).show();

			} else {

				ActivityCompat.requestPermissions(SupportActivity.this, new String[]{Manifest.permission.CALL_PHONE}, RequestPermissionCode);

			}
		}
		else {

		}


	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		switch (requestCode) {
			case RequestPermissionCode:
				if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

					//Toast.makeText(RequestListActivity.this,"Permission Granted, Now your application can access CAMERA.", Toast.LENGTH_LONG).show();

				} else {


				}
				break;

		}
	}
}
