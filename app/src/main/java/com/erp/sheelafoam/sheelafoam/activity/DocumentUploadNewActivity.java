package com.erp.sheelafoam.sheelafoam.activity;

import android.app.ActionBar.LayoutParams;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.erp.sheelafoam.R;
import com.erp.sheelafoam.erp.utils.MultipartUtility;
import com.erp.sheelafoam.erp.utils.NetworkTask;
import com.erp.sheelafoam.erp.utils.NetworkTask.Result;
import com.erp.sheelafoam.sheelafoam.utility.ApiList;
import com.erp.sheelafoam.sheelafoam.utility.ConnectionDetector;
import com.erp.sheelafoam.sheelafoam.utility.GlobalVariables;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DocumentUploadNewActivity extends AppCompatActivity implements
        OnClickListener, Result {

	ImageButton btnUploadDocument;
	ConnectionDetector con;
	private SharedPreferences mPrefs;
	String UserID, DocumentIDs, DealerID;
	private final int REQUEST_CAMERA = 200, SELECT_FILE = 2;
	ListView lvList;
	String selectedImagePath;
	TextView textview_back, textview_user_id;
	ImageButton btn_logout;
	private String user_role = "";
	public static String STREMAILADDREGEX = "^[_A-Za-z0-9]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,4})$"; // EMAIL
																																	// REGEX
	ArrayList<HashMap<String, String>> documents_items = new ArrayList<HashMap<String, String>>();
	public static final String DOCUMENT_ID = "DOCUMENT_ID";// DOCUMENT_NAME
															// //DOCUMENT_URL
															// //STATUS
	public static final String DOCUMENT_NAME = "DOCUMENT_NAME";
	public static final String DOCUMENT_URL = "DOCUMENT_URL";
	public static final String STATUS = "STATUS";
	RecordHolder holder;
	Button btnSubmitDocument;
	TextView tvTotalDocument;
	File out;
	EditText ed_contact_name, ed_mobile_no, ed_email_id, ed_pan_no;
	String strContatName, strMobileNo, strEmailID, strPanNo;

	int countDocument = 0;
	Toolbar toolbar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_document_upload_new);

		mPrefs = getSharedPreferences("Location", Context.MODE_PRIVATE);
		UserID = mPrefs.getString("user_id", "");
		DealerID = mPrefs.getString("DEALER_ID", "");
		con = new ConnectionDetector(getApplicationContext());

		lvList = (ListView) findViewById(R.id.document_listing);
		ed_contact_name = (EditText) findViewById(R.id.ed_contact_name);
		ed_email_id = (EditText) findViewById(R.id.ed_email_id);
		ed_mobile_no = (EditText) findViewById(R.id.ed_mobile_no);
		ed_pan_no = (EditText) findViewById(R.id.ed_pan_no);

		ed_contact_name.setEnabled(true);
		ed_email_id.setEnabled(true);
		ed_mobile_no.setEnabled(true);
		ed_pan_no.setEnabled(true);

		lvList.setVerticalScrollBarEnabled(false);

		tvTotalDocument = (TextView) findViewById(R.id.tv_document_total);
		String textHTML = "<font color=\"#1EAFB4\">"
				+ "0" + "</font>";
		tvTotalDocument.setText(Html.fromHtml("Total Document: "
				+ textHTML));

		btnUploadDocument = (ImageButton) findViewById(R.id.btn_upload_document);
		btnUploadDocument.setOnClickListener(this);

		btnSubmitDocument = (Button) findViewById(R.id.btn_submit_documents);
		btnSubmitDocument.setOnClickListener(this);
		initialize();
		callWS();
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) // Press Back Icon
		{
			finish();
		}
		return super.onOptionsItemSelected(item);
	}

	void initialize() {

		toolbar = (Toolbar) findViewById(R.id.app_toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);

		textview_back = (TextView) findViewById(R.id.app_toolbar_title);
		textview_back.setText("greatplus employee app");
//		if (mPrefs.getString("op_user_type", null).equalsIgnoreCase(
//				"DISTRIBUTOR")) {
//			textview_back.setText("greatplus distributor app");
//		} else if (mPrefs.getString("op_user_type", null).equalsIgnoreCase(
//				"DEALER")) {
//			textview_back.setText("greatplus dealer app");
//		} else {
//			textview_back.setText("greatplus employee app");
//		}

		//textview_user_id = (TextView) findViewById(R.id.textview_user_id);
		//btn_logout = (ImageButton) findViewById(R.id.btn_logout);
		//btn_logout.setOnClickListener(this);
		//textview_user_id.setText(mPrefs.getString("user_id", ""));

		SpannableString spannable_string = new SpannableString(textview_back
				.getText().toString().trim());
		spannable_string.setSpan(new ForegroundColorSpan(getResources()
				.getColor(R.color.color_great)), 0, 5,
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		spannable_string.setSpan(new ForegroundColorSpan(getResources()
				.getColor(R.color.color_plus)), 5, 10,
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

		textview_back.setText(spannable_string);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.document_upload_new, menu);
		return true;
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.btn_upload_document:
			selectImage();
			/*
			 * Intent intent_four = new Intent( Intent.ACTION_PICK,
			 * android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			 * intent_four.setType("image/*"); startActivityForResult(
			 * Intent.createChooser(intent_four, "Select File"), SELECT_FILE);
			 */
			break;
		case R.id.btn_logout:
			GlobalVariables.logout(this);
			break;
		case R.id.btn_submit_documents:
			if (validate()) {
				if (countDocument >= 1)
					callWSUploadDocument();
				else {
					showDialogDocument("You can not submit document, please upload at least one document.");
				}
			}
		default:
			break;
		}
	}

	private void callWS() {
		// TODO Auto-generated method stub
		try {
			JSONObject obj = new JSONObject();
			obj.put("p_greatplus_user_id", UserID);
			obj.put("p_dealer_id", DealerID);

			String url = "http://125.19.46.252/ws/get_dealer_document.php";

			NetworkTask networkTask = new NetworkTask(
					DocumentUploadNewActivity.this, 2, obj.toString());
			networkTask.setDialogMessage("Please wait..");
			networkTask.exposePostExecute(this);
			networkTask.execute(url);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void callWsDeleteDocument(String documentIDs2) {
		// TODO Auto-generated method stub
		try {
			JSONObject obj = new JSONObject();

			obj.put("p_greatplus_user_id", UserID);
			obj.put("p_document_id", documentIDs2);
			obj.put("mode", "delete_document");

			String url = "http://125.19.46.252/ws/delete_dealer_document.php";

			NetworkTask networkTask = new NetworkTask(
					DocumentUploadNewActivity.this, 1, obj.toString());
			networkTask.setDialogMessage("Please wait..");
			networkTask.exposePostExecute(this);
			networkTask.execute(url);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void callWSUploadDocument() {
		// TODO Auto-generated method stub
		try {
			JSONObject obj = new JSONObject();
			obj.put("p_greatplus_user_id", UserID);
			obj.put("p_dealer_id", DealerID);
			obj.put("p_pi_contact_person_name", strContatName);
			obj.put("p_pi_mobile_number", strMobileNo);
			obj.put("p_pi_email_id", strEmailID);
			obj.put("p_pi_pan_number", strPanNo);

			String url = "http://125.19.46.252/ws/prc_update_document_status.php";

			NetworkTask networkTask = new NetworkTask(
					DocumentUploadNewActivity.this, 2, obj.toString());
			networkTask.setDialogMessage("Please wait..");
			networkTask.exposePostExecute(this);
			networkTask.execute(url);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private boolean validate() {
		boolean flag = true;

		strContatName = ed_contact_name.getText().toString();
		strEmailID = ed_email_id.getText().toString();
		strMobileNo = ed_mobile_no.getText().toString();
		strPanNo = ed_pan_no.getText().toString();

		if (strContatName.trim().length() == 0) {
			Toast.makeText(DocumentUploadNewActivity.this,
					"Please enter Contact Person Name!", Toast.LENGTH_LONG)
					.show();
			flag = false;
		} else if (strEmailID.trim().length() == 0) {
			Toast.makeText(DocumentUploadNewActivity.this,
					"Please enter Email ID!", Toast.LENGTH_LONG).show();
			flag = false;
		} else if (strMobileNo.trim().length() == 0) {
			Toast.makeText(DocumentUploadNewActivity.this,
					"Please enter Mobile No!", Toast.LENGTH_LONG).show();
			flag = false;
		}else if (strMobileNo.trim().length() != 10) {
			Toast.makeText(DocumentUploadNewActivity.this,
					"Please enter valid Mobile No!", Toast.LENGTH_LONG).show();
			flag = false;
		} else if (strPanNo.trim().length() == 0) {
			Toast.makeText(DocumentUploadNewActivity.this,
					"Please enter Pan Number!", Toast.LENGTH_LONG).show();
			flag = false;
		}else if (strPanNo.trim().length() != 10) {
			Toast.makeText(DocumentUploadNewActivity.this,
					"Please enter valid Pan Number!", Toast.LENGTH_LONG).show();
			flag = false;
		} else if (!isEmailValid(strEmailID)) {
			Toast.makeText(DocumentUploadNewActivity.this,
					"Please enter valid email address!", Toast.LENGTH_LONG)
					.show();
			flag = false;
		}

		return flag;
	}

	public static boolean isEmailValid(String email) {
		boolean isValid = false;

		Pattern pattern = Pattern.compile(STREMAILADDREGEX,
				Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(email);
		if (matcher.matches()) {
			isValid = true;
		}
		return isValid;
	}

	@Override
	public void resultfromNetwork(String object, int id, int arg1, String arg2) {
		// TODO Auto-generated method stub
		switch (id) {
		case 1:
			/*
			 * JSONObject obj; try { obj = new JSONObject(object); if
			 * (obj.getInt("status") == 1) { ed_contact_name.setEnabled(false);
			 * ed_email_id.setEnabled(false); ed_mobile_no.setEnabled(false);
			 * ed_pan_no.setEnabled(false); } } catch (JSONException e) { //
			 * TODO Auto-generated catch block e.printStackTrace(); }
			 */

			/*
			 * Toast.makeText(getApplicationContext(), object,
			 * Toast.LENGTH_LONG).show();
			 */

			parseData(object);
			break;
		case 2:
			/*
			 * JSONObject obj; try { obj = new JSONObject(object); if
			 * (obj.getInt("status") == 1) { ed_contact_name.setEnabled(false);
			 * ed_email_id.setEnabled(false); ed_mobile_no.setEnabled(false);
			 * ed_pan_no.setEnabled(false); } } catch (JSONException e) { //
			 * TODO Auto-generated catch block e.printStackTrace(); }
			 */

			/*
			 * Toast.makeText(getApplicationContext(), object,
			 * Toast.LENGTH_LONG).show();
			 */

			parseDataWithInfo(object);
			break;

		default:
			break;
		}
	}

	private void parseData(String object) {
		try {
			JSONObject obj = new JSONObject(object);

			if (obj.getInt("status") == 1) {

				JSONArray data = obj.getJSONArray("data");

				if (obj.getString("msg").trim().length() > 0)
					Toast.makeText(DocumentUploadNewActivity.this,
							obj.getString("msg"), Toast.LENGTH_LONG).show();

				if (data.length() > 0) {
					// btnUploadDocument.setVisibility(View.GONE);
					// btnSubmitDocument.setVisibility(View.GONE);
					documents_items.clear();
					btnSubmitDocument.setVisibility(View.VISIBLE);
					String textHTML = "<font color=\"#1EAFB4\">"
							+ data.length() + "</font>";
					tvTotalDocument.setText(Html.fromHtml("Total Document: "
							+ textHTML));
					boolean flag = false;
					boolean flagOne = false;

					countDocument = 0;
					for (int i = 0; i < data.length(); i++) {

						// JSONObject jsonobject = data.getJSONObject(i);

						JSONObject jsonobject = data.getJSONObject(i);

						HashMap<String, String> documents = new HashMap<String, String>();

						documents.put(DOCUMENT_ID,
								jsonobject.getString("DOCUMENT_ID"));
						documents.put(DOCUMENT_NAME,
								jsonobject.getString("DOCUMENT_NAME"));
						documents.put(DOCUMENT_URL,
								jsonobject.getString("DOCUMENT_URL"));
						documents.put(STATUS, jsonobject.getString("STATUS"));
						if (jsonobject.getString("STATUS").equals("0")) {
							countDocument = countDocument + 1;
							flag = true;
							// Toast.makeText(getApplicationContext(), flag+"",
							// Toast.LENGTH_SHORT).show();

						}
						/*
						 * if (jsonobject.getString("STATUS").equals("1")) {
						 * 
						 * flagOne = true; //
						 * Toast.makeText(getApplicationContext(), flag+"", //
						 * Toast.LENGTH_SHORT).show();
						 * 
						 * }
						 */
						documents_items.add(documents);

					}

					/*
					 * if(flagOne){ btnUploadDocument.setEnabled(false);
					 * btnSubmitDocument.setEnabled(false); }
					 */

					if (!flag)
						btnSubmitDocument.setVisibility(View.VISIBLE);

				} else {
					btnUploadDocument.setVisibility(View.VISIBLE);
					btnSubmitDocument.setVisibility(View.VISIBLE);
					documents_items.clear();
					String textHTML = "<font color=\"#1EAFB4\">"
							+ "0" + "</font>";
					tvTotalDocument.setText(Html.fromHtml("Total Document: "
							+ textHTML));
					countDocument = 0;
					btnSubmitDocument.setVisibility(View.VISIBLE);
				}

				lvList.setAdapter(new ListViewAdapter(
						DocumentUploadNewActivity.this));

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void parseDataWithInfo(String object) {
		try {
			JSONObject obj = new JSONObject(object);

			/*
			 * Toast.makeText(getApplicationContext(),
			 * obj.getString("customerinfo"), Toast.LENGTH_LONG).show();
			 */
			if (obj.getInt("status") == 1) {

				JSONObject objInfo = new JSONObject(
						obj.getString("customerinfo"));
				if (objInfo.length() > 0) {

					if (objInfo.getString("CONTACT_PERSON").trim().length() > 0) {
						ed_contact_name.setText(objInfo
								.getString("CONTACT_PERSON"));
						ed_email_id.setText(objInfo.getString("PI_EMAIL_ID"));
						ed_mobile_no.setText(objInfo
								.getString("CONTACT_MOBILE_NO"));
						ed_pan_no.setText(objInfo.getString("PI_PAN_NUMBER"));

						ed_contact_name.setEnabled(false);
						ed_email_id.setEnabled(false);
						ed_mobile_no.setEnabled(false);
						ed_pan_no.setEnabled(false);
					}
				}

				JSONArray data = obj.getJSONArray("data");

				if (obj.getString("msg").trim().length() > 0)
					Toast.makeText(DocumentUploadNewActivity.this,
							obj.getString("msg"), Toast.LENGTH_LONG).show();

				if (data.length() > 0) {

					documents_items.clear();
					// btnSubmitDocument.setVisibility(View.VISIBLE);
					String textHTML = "<font color=\"#1EAFB4\">"
							+ data.length() + "</font>";
					tvTotalDocument.setText(Html.fromHtml("Total Document: "
							+ textHTML));
					boolean flag = false;
					boolean flagOne = false;

					countDocument = 0;
					for (int i = 0; i < data.length(); i++) {

						// JSONObject jsonobject = data.getJSONObject(i);

						JSONObject jsonobject = data.getJSONObject(i);

						HashMap<String, String> documents = new HashMap<String, String>();

						documents.put(DOCUMENT_ID,
								jsonobject.getString("DOCUMENT_ID"));
						documents.put(DOCUMENT_NAME,
								jsonobject.getString("DOCUMENT_NAME"));
						documents.put(DOCUMENT_URL,
								jsonobject.getString("DOCUMENT_URL"));
						documents.put(STATUS, jsonobject.getString("STATUS"));
						if (jsonobject.getString("STATUS").equals("0")) {
							countDocument = countDocument + 1;
							flag = true;
							// Toast.makeText(getApplicationContext(), flag+"",
							// Toast.LENGTH_SHORT).show();

						}

						if (jsonobject.getString("STATUS").equals("1")) {

							flagOne = true;
							// Toast.makeText(getApplicationContext(), flag+"",
							// Toast.LENGTH_SHORT).show();

						}
						documents_items.add(documents);

					}

					if (flagOne) {
						btnUploadDocument.setVisibility(View.GONE);
						btnSubmitDocument.setVisibility(View.GONE);
					}

				} else {
					btnUploadDocument.setVisibility(View.VISIBLE);
					btnSubmitDocument.setVisibility(View.VISIBLE);

					documents_items.clear();
					String textHTML = "<font color=\"#1EAFB4\">"
							+ "0" + "</font>";
					
					tvTotalDocument.setText(Html.fromHtml("Total Document: "
							+ textHTML));
					countDocument = 0;
					btnSubmitDocument.setVisibility(View.VISIBLE);
				}

				lvList.setAdapter(new ListViewAdapter(
						DocumentUploadNewActivity.this));

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null)
			return;

		int desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(),
				MeasureSpec.UNSPECIFIED);
		int totalHeight = 0;
		View view = null;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			view = listAdapter.getView(i, view, listView);
			if (i == 0)
				view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth,
						LayoutParams.WRAP_CONTENT));

			view.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
			totalHeight += view.getMeasuredHeight();
		}
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}

	public class ListViewAdapter extends BaseAdapter {
		Context mcontext;
		LayoutInflater inflater;

		public ListViewAdapter(Context context) {

			this.mcontext = context;

		}

		@Override
		public int getCount() {

			return documents_items.size();

		}

		@Override
		public Object getItem(int position) {

			return documents_items.get(position);
		}

		@Override
		public long getItemId(int position) {

			return position;

		}

		@Override
		public View getView(final int position, View convertView,
                            ViewGroup parent) {

			View row = convertView;
			try {

				// resultp = data.get(position);
				if (row == null) {
					inflater = (LayoutInflater) mcontext
							.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					row = inflater.inflate(R.layout.document_row, null);

					holder = new RecordHolder();
					holder.btnDeleteDocument = (ImageButton) row
							.findViewById(R.id.btn_delete_document);
					holder.btnUploadDocument = (ImageButton) row
							.findViewById(R.id.btn_upload_document);
					holder.tvDocumentName = (TextView) row
							.findViewById(R.id.tv_document_name);
					holder.tv_serialno = (TextView) row
							.findViewById(R.id.tv_serialno);

					row.setTag(holder);
				} else {
					holder = (RecordHolder) row.getTag();
				}

				try {

					if (position < 9) {
						holder.tv_serialno
								.setText("  " + (position + 1) + ". ");
					} else {
						holder.tv_serialno.setText((position + 1) + ". ");
					}

					if (documents_items.get(position).get(STATUS).equals("1")) {
						holder.btnDeleteDocument
								.setBackgroundResource(R.drawable.ic_deleted);
						holder.btnDeleteDocument.setClickable(false);
						holder.btnUploadDocument.setClickable(false);

					} else {
						holder.btnDeleteDocument
								.setBackgroundResource(R.drawable.ic_delete);
						holder.btnDeleteDocument.setClickable(true);
						holder.btnUploadDocument.setClickable(true);
					}

					holder.tvDocumentName.setText(documents_items.get(position)
							.get("DOCUMENT_NAME"));
					holder.btnDeleteDocument.setTag(position);
					holder.btnDeleteDocument
							.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View arg0) {
									// TODO Auto-generated method stub

									callWsDeleteDocument(documents_items.get(
											position).get("DOCUMENT_ID"));

								}
							});

					holder.tvDocumentName
							.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View arg0) {
									// TODO Auto-generated method stub
									startActivity(new Intent(
											getApplicationContext(),
											DocumentImageActivity.class)
											.putExtra(
													"IMAGEURL",
													documents_items.get(
															position).get(
															DOCUMENT_URL)));
								}
							});

				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}

			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}

			return row;
		}

		View.OnClickListener clickListener = new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				int position;

				position = (Integer) holder.btnDeleteDocument.getTag();
				Toast.makeText(mcontext, position, Toast.LENGTH_SHORT).show();

				switch (view.getId()) {

				}
			}
		};
	}

	class RecordHolder {
		ImageButton btnUploadDocument, btnDeleteDocument;
		TextView tvDocumentName, tv_serialno;
	}

	@Override
	public void onActivityResult(int reqCode, int resultCode, Intent data) {
		super.onActivityResult(reqCode, resultCode, data);
		switch (reqCode) {
		case (REQUEST_CAMERA):
			try {
				/*
				 * Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
				 * ByteArrayOutputStream bytes = new ByteArrayOutputStream();
				 * thumbnail.compress(Bitmap.CompressFormat.PNG,100, bytes);
				 * File destination = new
				 * File(Environment.getExternalStorageDirectory(),
				 * System.currentTimeMillis() + ".png");
				 */

				selectedImagePath = out.getAbsolutePath();

				// FileOutputStream fo;
				try {
					/*
					 * destination.createNewFile(); fo = new
					 * FileOutputStream(destination);
					 * fo.write(bytes.toByteArray()); fo.close();
					 */

					Bitmap bm;
					BitmapFactory.Options options = new BitmapFactory.Options();
					options.inJustDecodeBounds = true;
					BitmapFactory.decodeFile(selectedImagePath, options);
					/*
					 * final int REQUIRED_SIZE = 200; int scale = 1; while
					 * (options.outWidth / scale / 2 >= REQUIRED_SIZE &&
					 * options.outHeigh t / scale / 2 >= REQUIRED_SIZE) scale *=
					 * 2; options.inSampleSize = scale;
					 * options.inJustDecodeBounds = false; bm =
					 * BitmapFactory.decodeFile(selectedImagePath, options);
					 */

					new UploadImgClass().execute();
				} catch (Exception e) {
					e.printStackTrace();
				}

			} catch (NullPointerException e) {
				e.printStackTrace();
			}
			break;

		case (SELECT_FILE):
			try {
				Uri selectedImageUri = data.getData();
				String[] projection = { MediaStore.MediaColumns.DATA };
				CursorLoader cursorLoader = new CursorLoader(
						getApplicationContext(), selectedImageUri, projection,
						null, null, null);
				Cursor cursor = cursorLoader.loadInBackground();
				int column_index = cursor
						.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
				cursor.moveToFirst();
				selectedImagePath = cursor.getString(column_index);

				Bitmap bm;
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inJustDecodeBounds = true;
				BitmapFactory.decodeFile(selectedImagePath, options);
				/*
				 * final int REQUIRED_SIZE = 200; int scale = 1; while
				 * (options.outWidth / scale / 2 >= REQUIRED_SIZE &&
				 * options.outHeigh t / scale / 2 >= REQUIRED_SIZE) scale *= 2;
				 * options.inSampleSize = scale; options.inJustDecodeBounds =
				 * false; bm = BitmapFactory.decodeFile(selectedImagePath,
				 * options);
				 */

				new UploadImgClass().execute();

			} catch (NullPointerException e) {
				e.printStackTrace();
			}
			break;
		}
	}

	class UploadImgClass extends AsyncTask<String, Void, String> {
		ProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog(DocumentUploadNewActivity.this);
			dialog.setMessage("Please wait!!");
			// dialog.setTitle(getString(R.string.progress_title));
			dialog.setCanceledOnTouchOutside(false);
			dialog.show();
		}

		@Override
		protected String doInBackground(String... urls) {
			String charset = "UTF-8";
			String uploadResponse = "";
			String requestURL = ApiList.Upload_Document;
			try {

				MultipartUtility multipart = new MultipartUtility(requestURL,
						charset);

				if (selectedImagePath != null && !selectedImagePath.isEmpty()) {
					multipart.addFilePart("p_document_name", new File(
							selectedImagePath));
					Log.d("p_document_name", new File(selectedImagePath) + "");
				} else {
					multipart.addFormField("p_document_name", "");
					Log.d("p_document_name", "");
				}

				// multipart.addFormField("p_greatplus_user_id",mPrefs.getString("user_id",
				// ""));9428007792
				multipart.addFormField("p_greatplus_user_id",
						mPrefs.getString("user_id", ""));
				multipart.addFormField("mode", "upload_document");

				Log.d("p_greatplus_user_id", mPrefs.getString("user_id", ""));

				List<String> response = multipart.finish();

				for (int i = 0; i < response.size(); i++) {
					uploadResponse = response.get(i).toString();
				}
				Log.e("imageUpload Response", "== " + uploadResponse);

			} catch (IOException e) {
				e.printStackTrace();
			}
			return uploadResponse;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			dialog.dismiss();
			if (result != null) {
				try {

					parseData(result);

				} catch (Exception e) {
				}
				Log.d("Result", result);
			}

		}
	}

	private void selectImage() {
		final CharSequence[] items = { "Take Photo", "Choose from Library",
				"Cancel" };
		AlertDialog.Builder builder = new AlertDialog.Builder(
				DocumentUploadNewActivity.this);
		builder.setTitle("Add Photo!");
		builder.setItems(items, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int item) {
				if (items[item].equals("Take Photo")) {
					/*
					 * Intent intent = new
					 * Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					 * startActivityForResult(intent, REQUEST_CAMERA);
					 */

					Intent i = new Intent(
							android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
					out = Environment.getExternalStorageDirectory();
					out = new File(out, "camera.jpg");
					i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(out));
					startActivityForResult(i, REQUEST_CAMERA);

				} else if (items[item].equals("Choose from Library")) {
					Intent intent = new Intent(
							Intent.ACTION_PICK,
							android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
					startActivityForResult(intent, SELECT_FILE);
				} else if (items[item].equals("Cancel")) {
					dialog.dismiss();
				}
			}
		});
		builder.show();
	}

	private void showDialogDocument(String msg) {
		new AlertDialog.Builder(DocumentUploadNewActivity.this)
				.setTitle("")
				.setMessage(msg)
				.setPositiveButton(android.R.string.ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								// continue with delete
								// callWSPAYCASH();
							}
						}).show();
	}
}
