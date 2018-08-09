package com.erp.sheelafoam.sheelafoam.xperia.activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.erp.sheelafoam.R;
import com.erp.sheelafoam.sheelafoam.xperia.dataholders.ComplaintNumberHolder;

public class AddDetailsnew extends Activity {

	GridView gridview;
	Button addImageBtn, addDocbutton;
	Button submitBtn, btnSave;
	ImageButton btn_logout;
	EditText edittext_remark;
	String imgName, mobileNum, user_role,complainNum;
	TextView textview_complaintID, textview_app_title;
	RelativeLayout rlayout_remark, rlayout_bottom;

	private SharedPreferences mPrefs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_complain_details);
		mPrefs = getSharedPreferences("Location", Context.MODE_PRIVATE);
		user_role = mPrefs.getString("op_user_type", "");
		ComplaintNumberHolder.setIsResume(2);
		
		initializeCntrols();

	}

	void initializeCntrols() {
		gridview = (GridView) findViewById(R.id.listView1);
		textview_complaintID = (TextView) findViewById(R.id.textview_complaintID);
		textview_app_title = (TextView) findViewById(R.id.textview_back);
		edittext_remark = (EditText) findViewById(R.id.edittext_remark);
		rlayout_bottom = (RelativeLayout) findViewById(R.id.rlayout_bottom);
		complainNum = getIntent().getStringExtra("complainid");
		mobileNum = getIntent().getStringExtra("mobile");

		if (user_role.equals("CUSTOMER")) {
			edittext_remark.setVisibility(View.GONE);
			rlayout_bottom.setVisibility(View.GONE);
		} else {
			// Agent or Sales person
			edittext_remark.setVisibility(View.VISIBLE);
			rlayout_bottom.setVisibility(View.VISIBLE);
		}

		textview_app_title.setText("greatplus" + " " + user_role + " " + "app");
		SpannableString spannable_string = new SpannableString(textview_app_title.getText().toString().trim());
		spannable_string.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.color_great)), 0, 5,
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		spannable_string.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.color_plus)), 5, 10,
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

		textview_app_title.setText(spannable_string);
	}

}
