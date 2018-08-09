package com.erp.sheelafoam.sheelafoam.xperia.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.erp.sheelafoam.MoreAboutComplaint;
import com.erp.sheelafoam.R;
import com.erp.sheelafoam.constant.Constant;
import com.erp.sheelafoam.sheelafoam.complaint.ComplainDetails;
import com.erp.sheelafoam.sheelafoam.xperia.adapter.FailedCoplaintListAdapter;
import com.erp.sheelafoam.sheelafoam.xperia.dataholders.ComplaintNumberHolder;
import com.erp.sheelafoam.sheelafoam.xperia.dataholders.FailedComplaintHolder;
import com.erp.sheelafoam.sheelafoam.xperia.dataholders.GetSetComplaint;
import com.erp.sheelafoam.sheelafoam.xperia.function.ComplaintDataBase;
import com.erp.sheelafoam.sheelafoam.xperia.model.CoplainListAdapter;
import com.erp.sheelafoam.utils.Util;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class ComplaintNew extends AppCompatActivity {
    ListView CList;
    ArrayList<GetSetComplaint> list = new ArrayList<GetSetComplaint>();
    ProgressBar p;
    String mobileNum = "", greatPlusUserId = "";
    TextView textview_back, textview_user_id;
    ImageButton btn_logout;
    Toolbar toolbar;
    RelativeLayout failed_comp;
    Button preLogBtn;
    Button postLogBtn;
    Button searchBtn;
    EditText etComplaintId;
    String userType, userType_new;
    ArrayList<FailedComplaintHolder> Ids;
    TextView head;
    Dialog dialog;
    private SharedPreferences mPrefs;
    private String user_role = "";

    private static String formatDate(int year, int month, int day) {

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(0);
        cal.set(year, month, day);
        Date date = cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");

        return sdf.format(date);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.complaint_new);
        mPrefs = getSharedPreferences("Location", Context.MODE_PRIVATE);
        SharedPreferences sharedPreferences = getSharedPreferences("OTP_SESSION", MODE_PRIVATE);
        userType = sharedPreferences.getString("type_user", "");
        Log.d("complaint", userType);
        greatPlusUserId = Util.getSharedPrefrenceValue(ComplaintNew.this, Constant.Sp_GrtPlusUserID);
        //mobileNum = mPrefs.getString("op_user_mobile", "");
        //user_role = mPrefs.getString("op_user_type", "").toLowerCase();
        String stUser = "agent";
        user_role = stUser.toLowerCase();
        CList = (ListView) findViewById(R.id.complaintList);
        p = (ProgressBar) findViewById(R.id.pb1);
        initialize();
        new GetComplaint().execute();
        //new GetPoints().execute();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) // Press Back Icon
        {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void initialize() {
        etComplaintId = (EditText) findViewById(R.id.et_complaint_id);
        failed_comp = (RelativeLayout) findViewById(R.id.failed_complaint);
        failed_comp.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showFailedDialog();
            }
        });


        searchBtn = (Button) findViewById(R.id.search_btn);
        searchBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etComplaintId.getText().toString().equals("")) {
                    Toast.makeText(ComplaintNew.this, "Please enter complaint id to search", Toast.LENGTH_SHORT).show();
                } else {
                    if (list.size() < 0) {
                        Toast.makeText(ComplaintNew.this, "No record found", Toast.LENGTH_SHORT).show();
                    } else {
                        for (int i = 0; i < list.size(); i++) {
                            if (list.get(i).getNo().equals(etComplaintId.getText().toString())
                                    || list.get(i).getMobile().equals(etComplaintId.getText().toString())
                                    || list.get(i).getName().equals(etComplaintId.getText().toString())) {
                                CList.smoothScrollToPosition(i);
                            }
                        }
                    }
                }
            }
        });

        preLogBtn = (Button) findViewById(R.id.btn_log_pre);
        preLogBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ComplaintNew.this, LogComplaintActivity.class);
                intent.putExtra("log", "pre");
                startActivity(intent);
            }
        });


        postLogBtn = (Button) findViewById(R.id.btn_log_post);
        postLogBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ComplaintNew.this, LogComplaintActivity.class);
                intent.putExtra("log", "post");
                startActivity(intent);
            }
        });


        if (userType.equalsIgnoreCase("AGENT")) {
            preLogBtn.setVisibility(View.GONE);
            postLogBtn.setVisibility(View.GONE);
        }


        ComplaintNumberHolder.setIsResume(1);
        toolbar = (Toolbar) findViewById(R.id.app_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        textview_back = (TextView) findViewById(R.id.app_toolbar_title);
        textview_back.setText("Complaint");
        //textview_user_id = (TextView) findViewById(R.id.textview_user_id);

		/*btn_logout = (ImageButton) findViewById(R.id.btn_logout);
		btn_logout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				GlobalVariables.logout(ComplaintNew.this);
			}
		});

		textview_user_id.setText(mobileNum);

		textview_back.setText("greatplus" + " " + user_role + " " + "app");

		SpannableString spannable_string = new SpannableString(textview_back.getText().toString().trim());
		spannable_string.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.color_great)), 0, 5,
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		spannable_string.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.color_plus)), 5, 10,
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

		textview_back.setText(spannable_string); */

    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        // super.onBackPressed();
        //comment by Twigz to get back on home screen
//		if (user_role.equals("EMPLOYEE")) {
//			finish();
//		} else {
//			finish();
//		}
        finish();
    }

    public void showDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(ComplaintNew.this);

        builder.setTitle("SheelaFoam");

        builder.setMessage("Are you sure want to exit from app");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub

                finish();
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub

                dialog.dismiss();
            }
        });
        builder.show();
    }

    public void addetails(int pos) {
        Intent in = new Intent(ComplaintNew.this, ComplainDetails.class);
        in.putExtra("complainid", list.get(pos).getNo().toString());
        in.putExtra("mobile", greatPlusUserId);
        in.putExtra("opt", "1");
        ComplaintNumberHolder.setItemPosition(pos);
        startActivity(in);
        //Toast.makeText(this, list.get(pos).getNo().toString() + " " + mobileNum, Toast.LENGTH_SHORT).show();
    }

    public void viewMoreDetails(int i) {
        Intent in = new Intent(ComplaintNew.this, MoreAboutComplaint.class);
        in.putExtra("complaint_details", list.get(i));
        startActivity(in);
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        //Toast.makeText(getApplicationContext(), "On Paused", 500).show();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        if (ComplaintNumberHolder.getIsResume() == 2 && ComplaintNumberHolder.getIsSuccess() == 1) {
            list.remove(ComplaintNumberHolder.getItemPosition());
            CList.setAdapter(new CoplainListAdapter(ComplaintNew.this, list));
            if (dialog != null) {
                dialog.dismiss();
            }
            //Toast.makeText(getApplicationContext(), "Complaint to be Removed", 500).show();
        }
    }

    void showFailedDialog() {


//		if(FileUpload.imgList.size()>0){
//			UploadFile.upload(FileUpload.imgList, FileUpload.remark,ComplaintNew.this,null);
//			while (!UploadFile.isUploadComplete) {
//			}
//		}
        ComplaintDataBase coDataBase = new ComplaintDataBase(getApplicationContext());
        dialog = new Dialog(ComplaintNew.this, R.style.MyDialog2);
        View view = LayoutInflater.from(ComplaintNew.this).inflate(R.layout.failed_image_dialog, null);
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.setContentView(view);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.getWindow().setWindowAnimations(R.style.DialogAnimations_SmileWindow);
        dialog.setCancelable(true);
        List<String> data = new ArrayList<String>();

        Ids = coDataBase.getFailedComplaintsId1(mobileNum);
        for (int i = 0; i < Ids.size(); i++) {

            data.add(Ids.get(i).getComId() + " Failed Images: " + Ids.get(i).getTotalImages());
        }

//		new FileUpload(complainNum, imgList,remark,getApplicationContext(),ComplainDetails.this).start();
//		while (!UploadFile.isUploadComplete) {
//
//		}

        ListView list = (ListView) view.findViewById(R.id.complaints);
        head = (TextView) view.findViewById(R.id.header);
        FailedCoplaintListAdapter ad = new FailedCoplaintListAdapter(getApplicationContext(), Ids, ComplaintNew.this);
        list.setAdapter(ad);
        if (Ids.size() > 0) {
            dialog.show();
        } else {
            Toast.makeText(getApplicationContext(), "No failed complaints found", Toast.LENGTH_SHORT).show();
        }

    }

    public void gotoDetails(int pos) {
        Intent in = new Intent(ComplaintNew.this, ComplainDetails.class);
        in.putExtra("complainid", Ids.get(pos).getComId().toString());
        in.putExtra("mobile", mobileNum);
        in.putExtra("opt", "2");
        startActivity(in);
        if (dialog.isShowing()) {
            dialog.dismiss();
        }

        //Toast.makeText(getApplicationContext(), Ids.get(pos).getComId(), Toast.LENGTH_LONG).show();
    }

    public class GetComplaint extends AsyncTask<String, String, String> {
        StringBuilder stringBuilder = new StringBuilder();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                mobileNum = mobileNum.replaceAll(" ", "%20");
                greatPlusUserId = greatPlusUserId.replaceAll(" ", "%20");
                URL url = new URL("http://125.19.46.252/ws/complaint_agentAPI.php?MOBILE=" + greatPlusUserId);
                Log.d("URLNEW->", "http://125.19.46.252/ws/complaint_agentAPI.php?MOBILE=" + greatPlusUserId);
                HttpURLConnection htp = (HttpURLConnection) url.openConnection();
                htp.setRequestMethod("GET");
                htp.setRequestProperty("Content-Type", "application/json");
                htp.setDoInput(true);
                htp.setDoOutput(true);
                htp.setUseCaches(false);
                htp.connect();

                InputStream inputStream = htp.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String Line;
                while ((Line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(Line);
                }
                return stringBuilder.toString();
            } catch (Exception e) {

            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                Log.e("Complaint Response", s);
                JSONObject obj = new JSONObject(s);
                JSONArray ary = obj.getJSONArray("track");
                for (int i = 0; i < ary.length(); i++) {
                    JSONObject object = ary.getJSONObject(i);
                    // no, cmpDate, city, name, mobile, address, product,
                    // billDate, dealer, problem, pending
                    GetSetComplaint gsa = new GetSetComplaint();
                    gsa.setNo(object.getString("COMP_ID"));
                    gsa.setCmpDate((object.getString("ENTRYDATE")));
                    gsa.setCity(object.getString("CITY"));
                    gsa.setName(object.getString("CUST_NAME"));
                    gsa.setMobile(object.getString("MOBILE"));
                    gsa.setAddress(object.getString("ADDRESS1"));
                    gsa.setProduct(object.getString("PRODUCT_DISPLAY_NAME") + "(" + object.getString("LENGTH") + "X"
                            + object.getString("BREDTH") + "X" + object.getString("THICK") + ")");
                    gsa.setBillDate(object.getString("PUR_DATE"));
                    gsa.setDealer(object.getString("DEALER_NAME"));
                    gsa.setProblem(object.getString("COMPLAIN_TYPE_NAME"));
                    gsa.setPending(object.getString("PENDING_SINCE"));
                    list.add(gsa);
                }
                //CList.setAdapter(new CoplainListAdapter(ComplaintNew.this, list));
                //  p.setVisibility(View.GONE);

                Collections.sort(list, new CustomComparator());
                CoplainListAdapter adapter = new CoplainListAdapter(ComplaintNew.this, list);
                CList.setAdapter(adapter);
                p.setVisibility(View.GONE);

            } catch (Exception e) {
                e.printStackTrace();
                p.setVisibility(View.GONE);
                Toast.makeText(ComplaintNew.this, "No data found", Toast.LENGTH_LONG).show();
            }
        }
    }

    public class CustomComparator implements Comparator<GetSetComplaint> {// may be it would be Model

        @Override
        public int compare(GetSetComplaint obj1, GetSetComplaint obj2) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
            Date obj1Date = null, obj2Date = null;
            try {
                obj1Date = simpleDateFormat.parse(obj1.getCmpDate());
                obj2Date = simpleDateFormat.parse(obj2.getCmpDate());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return obj2Date.compareTo(obj1Date);// compare two objects
        }
    }

    public class GetPoints extends AsyncTask<String, String, String> {

        StringBuilder stringBuilder = new StringBuilder();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                URL url = new URL("http://125.19.46.252/ws/get_pointsAPI.php?MOBILE=9984216087");// + mobileNum);
                HttpURLConnection htp = (HttpURLConnection) url.openConnection();
                htp.setRequestMethod("GET");
                htp.setRequestProperty("Content-Type", "application/json");
                htp.setDoInput(true);
                htp.setDoOutput(true);
                htp.setUseCaches(false);
                htp.connect();

                InputStream inputStream = htp.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String Line;
                while ((Line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(Line);
                }
                return stringBuilder.toString();
            } catch (Exception e) {

            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                Log.e("Response", s);
                JSONObject obj = new JSONObject(s);
                String openingPoints = obj.getString("op_opening");
                String closingPoints = obj.getString("op_earned");
                String earnedPoints = obj.getString("op_closing");
                Toast.makeText(ComplaintNew.this, openingPoints + " " + closingPoints + " " + earnedPoints, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(ComplaintNew.this, "Network Error Occurred...", Toast.LENGTH_SHORT).show();
            }
        }
    }


}
