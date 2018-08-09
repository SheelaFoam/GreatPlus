package com.erp.sheelafoam.sheelafoam.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.erp.sheelafoam.R;
import com.erp.sheelafoam.erp.utils.CustomHttpClient;
import com.erp.sheelafoam.erp.utils.MultipartUtility;
import com.erp.sheelafoam.erp.utils.NetworkTask;
import com.erp.sheelafoam.erp.utils.NetworkTask.Result;
import com.erp.sheelafoam.sheelafoam.utility.ApiList;
import com.erp.sheelafoam.sheelafoam.utility.AsyncTaskListner;
import com.erp.sheelafoam.sheelafoam.utility.ConnectionDetector;
import com.erp.sheelafoam.sheelafoam.utility.GlobalVariables;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class DocumentUploadActivity extends Activity implements
        OnClickListener, AsyncTaskListner, Result {

	ImageButton btnUploadOne, btnUploadTwo, btnUploadThree, btnUploadFour;
	ImageButton btnDeleteOne, btnDeleteTwo, btnDeleteThree, btnDeleteFour;

	TextView tv_document_one, tv_document_two, tv_document_three,
			tv_document_four;
	private ProgressDialog pDialog;
	private final int REQUEST_CAMERA = 200, SELECT_FILE = 2;
	String selectedImagePath;
	ConnectionDetector con;
	private SharedPreferences mPrefs;
	String UserID, DocumentIDs, DealerID;
	static final Integer READ_EXST = 0x4;

	String urlOne, urlTwo, urlThree, urlFour;
	Button btnUploadDocument, btn_checkout;
	
	TextView textview_back, textview_user_id;
	ImageButton btn_logout;
	private String user_role = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_document_upload);

		mPrefs = getSharedPreferences("Location", Context.MODE_PRIVATE);
		UserID = mPrefs.getString("user_id", "");
		DealerID = mPrefs.getString("DEALER_ID", "");
		con = new ConnectionDetector(getApplicationContext());

		btnUploadOne = (ImageButton) findViewById(R.id.btn_upload_one);
		btnUploadTwo = (ImageButton) findViewById(R.id.btn_upload_two);
		btnUploadThree = (ImageButton) findViewById(R.id.btn_upload_three);
		btnUploadFour = (ImageButton) findViewById(R.id.btn_upload_four);
		btn_checkout = (Button) findViewById(R.id.btn_checkout);
		btn_checkout.setOnClickListener(this);

		btnDeleteOne = (ImageButton) findViewById(R.id.btn_delete_one);
		btnDeleteTwo = (ImageButton) findViewById(R.id.btn_delete_two);
		btnDeleteThree = (ImageButton) findViewById(R.id.btn_delete_three);
		btnDeleteFour = (ImageButton) findViewById(R.id.btn_delete_four);

		tv_document_one = (TextView) findViewById(R.id.tv_document_one);
		tv_document_two = (TextView) findViewById(R.id.tv_document_two);
		tv_document_three = (TextView) findViewById(R.id.tv_document_three);
		tv_document_four = (TextView) findViewById(R.id.tv_document_four);

		btnUploadDocument = (Button) findViewById(R.id.btn_submit_documents);

		btnUploadDocument.setOnClickListener(this);

		btnUploadOne.setOnClickListener(this);
		btnUploadTwo.setOnClickListener(this);
		btnUploadThree.setOnClickListener(this);
		btnUploadFour.setOnClickListener(this);

		btnDeleteOne.setOnClickListener(this);
		btnDeleteTwo.setOnClickListener(this);
		btnDeleteThree.setOnClickListener(this);
		btnDeleteFour.setOnClickListener(this);

		tv_document_one.setOnClickListener(this);
		tv_document_two.setOnClickListener(this);
		tv_document_three.setOnClickListener(this);
		tv_document_four.setOnClickListener(this);

		initialize();
		callWS();

		// new GetDocumentDetail().execute();

		/*
		 * if (con.isConnectingToInternet()) { new
		 * GetDocumentDetail().execute(); } else {
		 * GlobalVariables.defaultOneButtonDialog(DocumentUploadActivity.this,
		 * GlobalVariables.CONNECTION_ERROR); }
		 */
	}
	
	void initialize() {

		mPrefs = getSharedPreferences("Location", Context.MODE_PRIVATE);

		textview_back = (TextView) findViewById(R.id.textview_back);
		if (mPrefs.getString("op_user_type", null).equalsIgnoreCase("DISTRIBUTOR")) {
			textview_back.setText("greatplus distributor app");
		} else if (mPrefs.getString("op_user_type", null).equalsIgnoreCase("DEALER")) {
			textview_back.setText("greatplus dealer app");
		} else {
			textview_back.setText("greatplus employee app");
		}

		textview_user_id = (TextView) findViewById(R.id.textview_user_id);
		btn_logout = (ImageButton) findViewById(R.id.btn_logout);
		btn_logout.setOnClickListener(this);
		textview_user_id.setText(mPrefs.getString("user_id", ""));
		
		SpannableString spannable_string = new SpannableString(textview_back.getText().toString().trim());
		spannable_string.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.color_great)), 0, 5,
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		spannable_string.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.color_plus)), 5, 10,
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

		textview_back.setText(spannable_string);
	}

	private void callWS() {
		// TODO Auto-generated method stub
		try {
			JSONObject obj = new JSONObject();
			obj.put("p_greatplus_user_id", UserID);

			String url = "http://125.19.46.252/ws/get_dealer_document.php";

			NetworkTask networkTask = new NetworkTask(
					DocumentUploadActivity.this, 1, obj.toString());
			networkTask.setDialogMessage("Please wait..");
			networkTask.exposePostExecute(this);
			networkTask.execute(url);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.document_upload, menu);
		return true;
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.btn_upload_one:
			Intent intent = new Intent(
					Intent.ACTION_PICK,
					android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			intent.setType("image/*");
			startActivityForResult(Intent.createChooser(intent, "Select File"),
					SELECT_FILE);
			break;
		case R.id.btn_checkout:
			startActivity(new Intent(getApplicationContext(),CheckoutActivity.class));
			break;
		case R.id.btn_upload_two:
			Intent intent_second = new Intent(
					Intent.ACTION_PICK,
					android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			intent_second.setType("image/*");
			startActivityForResult(
					Intent.createChooser(intent_second, "Select File"),
					SELECT_FILE);
			break;
		case R.id.btn_upload_three:
			Intent intent_third = new Intent(
					Intent.ACTION_PICK,
					android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			intent_third.setType("image/*");
			startActivityForResult(
					Intent.createChooser(intent_third, "Select File"),
					SELECT_FILE);
			break;
		case R.id.btn_upload_four:
			Intent intent_four = new Intent(
					Intent.ACTION_PICK,
					android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			intent_four.setType("image/*");
			startActivityForResult(
					Intent.createChooser(intent_four, "Select File"),
					SELECT_FILE);
			break;
		case R.id.btn_delete_one:
			DocumentIDs = mPrefs.getString("DOCUMENT_ID_First", "");
			callWsDeleteDocument(DocumentIDs);

			break;
		case R.id.btn_delete_two:
			DocumentIDs = mPrefs.getString("DOCUMENT_ID_Second", "");
			callWsDeleteDocument(DocumentIDs);
			break;
		case R.id.btn_delete_three:
			DocumentIDs = mPrefs.getString("DOCUMENT_ID_Third", "");
			callWsDeleteDocument(DocumentIDs);
			break;
		case R.id.btn_delete_four:
			DocumentIDs = mPrefs.getString("DOCUMENT_ID_Fourth", "");
			callWsDeleteDocument(DocumentIDs);
			break;
		case R.id.btn_submit_documents:
			callWSUploadDocument();
			break;
		case R.id.tv_document_one: {//IMAGEURL
			
			startActivity(new Intent(getApplicationContext(),DocumentImageActivity.class).putExtra("IMAGEURL", urlOne));
			/*Intent i = new Intent(Intent.ACTION_VIEW);
			i.setData(Uri.parse(urlOne));
			startActivity(i);*/
			break;
		}
		case R.id.tv_document_two: {
			startActivity(new Intent(getApplicationContext(),DocumentImageActivity.class).putExtra("IMAGEURL", urlTwo));
			/*Intent i = new Intent(Intent.ACTION_VIEW);
			i.setData(Uri.parse(urlTwo));
			startActivity(i);*/
			break;
		}
		case R.id.tv_document_three: {
			startActivity(new Intent(getApplicationContext(),DocumentImageActivity.class).putExtra("IMAGEURL", urlThree));
			/*Intent i = new Intent(Intent.ACTION_VIEW);
			i.setData(Uri.parse(urlThree));
			startActivity(i);*/
			break;
		}
		case R.id.tv_document_four: {
			startActivity(new Intent(getApplicationContext(),DocumentImageActivity.class).putExtra("IMAGEURL", urlFour));
			/*Intent i = new Intent(Intent.ACTION_VIEW);
			i.setData(Uri.parse(urlFour));
			startActivity(i);*/
		}
			break;
		case R.id.btn_logout:
			GlobalVariables.logout(this);
			break;
		}
	}

	private void callWSUploadDocument() {
		// TODO Auto-generated method stub
		try {
			JSONObject obj = new JSONObject();
			obj.put("p_greatplus_user_id", UserID);
			obj.put("p_dealer_id", DealerID);

			String url = "http://125.19.46.252/ws/prc_update_document_status.php";

			NetworkTask networkTask = new NetworkTask(
					DocumentUploadActivity.this, 3, obj.toString());
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
					DocumentUploadActivity.this, 2, obj.toString());
			networkTask.setDialogMessage("Please wait..");
			networkTask.exposePostExecute(this);
			networkTask.execute(url);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void onActivityResult(int reqCode, int resultCode, Intent data) {
		super.onActivityResult(reqCode, resultCode, data);
		switch (reqCode) {

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

	class GetDocumentDetail extends AsyncTask<String, Void, String> {
		String response = "";

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			pDialog = new ProgressDialog(DocumentUploadActivity.this);
			pDialog.setMessage("Loading..");

			pDialog.setCancelable(true);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			Toast.makeText(DocumentUploadActivity.this, "Before Resonse",
					Toast.LENGTH_LONG).show();
			try {

				response = CustomHttpClient
						.executeHttpGet("http://125.19.46.252/ws/get_dealer_document.php?p_greatplus_user_id="
								+ UserID);
				Toast.makeText(DocumentUploadActivity.this,
						"After Resonse" + response, Toast.LENGTH_LONG).show();
				Log.d("URL-->",
						"http://125.19.46.252/ws/get_dealer_document.php?p_greatplus_user_id="
								+ UserID);
				// Then we assign the execution result to HttpResponse

				System.out.println("httpResponse");

			} catch (IOException ioe) {
				System.out
						.println("Second exception generates caz of httpResponse :"
								+ ioe);
				ioe.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return response;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String response) {
			// dismiss the dialog once done
			if (response != "" || response != null) {
				try {

					JSONObject Obj = new JSONObject(response);

					int status = Obj.getInt("status");

					if (status == 1) {
						JSONArray data = Obj.getJSONArray("data");

						if (data.length() == 0) {
							btnUploadDocument.setVisibility(View.GONE);
						} else {
							btnUploadDocument.setVisibility(View.VISIBLE);
						}
						for (int i = 0; i < data.length(); i++) {
							if (i == 0) {
								JSONObject jsonobject = data.getJSONObject(0);
								String DOCUMENT_ID = jsonobject
										.getString("DOCUMENT_ID");
								tv_document_one.setText(jsonobject
										.getString("DOCUMENT_NAME"));
								urlOne = jsonobject.getString("DOCUMENT_URL");

								Editor editor = mPrefs.edit();
								editor.putString("DOCUMENT_ID_First",
										DOCUMENT_ID);
								editor.commit();

								btnDeleteOne.setVisibility(View.VISIBLE);
							} else if (i == 1) {
								JSONObject jsonobject = data.getJSONObject(1);
								String DOCUMENT_ID = jsonobject
										.getString("DOCUMENT_ID");
								tv_document_one.setText(jsonobject
										.getString("DOCUMENT_NAME"));

								urlTwo = jsonobject.getString("DOCUMENT_URL");

								Editor editor = mPrefs.edit();
								editor.putString("DOCUMENT_ID_Second",
										DOCUMENT_ID);
								editor.commit();

								btnDeleteOne.setVisibility(View.VISIBLE);
							} else if (i == 2) {
								JSONObject jsonobject = data.getJSONObject(2);
								String DOCUMENT_ID = jsonobject
										.getString("DOCUMENT_ID");
								tv_document_one.setText(jsonobject
										.getString("DOCUMENT_NAME"));

								urlThree = jsonobject.getString("DOCUMENT_URL");

								Editor editor = mPrefs.edit();
								editor.putString("DOCUMENT_ID_Third",
										DOCUMENT_ID);
								editor.commit();

								btnDeleteOne.setVisibility(View.VISIBLE);
							} else if (i == 3) {
								JSONObject jsonobject = data.getJSONObject(3);
								String DOCUMENT_ID = jsonobject
										.getString("DOCUMENT_ID");
								tv_document_one.setText(jsonobject
										.getString("DOCUMENT_NAME"));

								urlFour = jsonobject.getString("DOCUMENT_URL");

								Editor editor = mPrefs.edit();
								editor.putString("DOCUMENT_ID_Fourth",
										DOCUMENT_ID);
								editor.commit();

								btnDeleteOne.setVisibility(View.VISIBLE);
							}

						}

					} else {
						btnUploadDocument.setVisibility(View.GONE);
						Toast.makeText(
								getApplicationContext(),
								" There is an internal error. Please try again..!",
								Toast.LENGTH_SHORT).show();
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
				pDialog.dismiss();
			}

		}

	}

	class UploadImgClass extends AsyncTask<String, Void, String> {
		ProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog(DocumentUploadActivity.this);
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

					JSONObject obj = new JSONObject(result);
					if (obj.getInt("status") == 1) {

						JSONArray data = obj.getJSONArray("data");

						if (data.length() == 0) {
							btnUploadDocument.setVisibility(View.GONE);
						} else {
							btnUploadDocument.setVisibility(View.VISIBLE);
						}

						parseData(result);

						/*
						 * for (int i = 0; i < data.length(); i++) { if (i == 0)
						 * { JSONObject jsonobject = data.getJSONObject(i);
						 * String DOCUMENT_ID = jsonobject
						 * .getString("DOCUMENT_ID");
						 * tv_document_one.setText(jsonobject
						 * .getString("DOCUMENT_NAME"));
						 * urlOne=jsonobject.getString("DOCUMENT_URL");
						 * btnDeleteOne.setVisibility(View.VISIBLE); } else if
						 * (i == 1) { JSONObject jsonobject =
						 * data.getJSONObject(i); String DOCUMENT_ID =
						 * jsonobject .getString("DOCUMENT_ID");
						 * tv_document_two.setText(jsonobject
						 * .getString("DOCUMENT_NAME"));
						 * 
						 * urlTwo=jsonobject.getString("DOCUMENT_URL");
						 * btnDeleteTwo.setVisibility(View.VISIBLE); } else if
						 * (i == 2) { JSONObject jsonobject =
						 * data.getJSONObject(i); String DOCUMENT_ID =
						 * jsonobject .getString("DOCUMENT_ID");
						 * tv_document_three.setText(jsonobject
						 * .getString("DOCUMENT_NAME"));
						 * 
						 * urlThree=jsonobject.getString("DOCUMENT_URL");
						 * btnDeleteThree.setVisibility(View.VISIBLE); } else if
						 * (i == 3) { JSONObject jsonobject =
						 * data.getJSONObject(i); String DOCUMENT_ID =
						 * jsonobject .getString("DOCUMENT_ID");
						 * tv_document_four.setText(jsonobject
						 * .getString("DOCUMENT_NAME"));
						 * urlFour=jsonobject.getString("DOCUMENT_URL");
						 * btnDeleteFour.setVisibility(View.VISIBLE); }
						 * 
						 * // "DOCUMENT_URL" + // "STATUS }
						 */
					} else {
						btnUploadDocument.setVisibility(View.GONE);
						Toast.makeText(DocumentUploadActivity.this,
								obj.getString("msg"), Toast.LENGTH_LONG).show();
					}

				} catch (Exception e) {
				}
				Log.d("Result", result);
			}

		}
	}

	@Override
	public void onTaskComplete(String result) {
		// TODO Auto-generated method stub

	}

	@Override
	public void resultfromNetwork(String object, int id, int arg1, String arg2) {
		// TODO Auto-generated method stub
		switch (id) {
		case 1:
			parseData(object);
			/*
			 * try { JSONObject obj = new JSONObject(object);
			 * 
			 * if (obj.getInt("status") == 1) {
			 * 
			 * JSONArray data = obj.getJSONArray("data");
			 * 
			 * boolean flag=true; if(data.length()==4){ flag=false; }
			 * 
			 * if(data.length()==0){ btnUploadDocument.setVisibility(View.GONE);
			 * }
			 * 
			 * for (int i = 0; i < data.length(); i++) { if (i == 0) {
			 * JSONObject jsonobject = data.getJSONObject(i); String DOCUMENT_ID
			 * = jsonobject .getString("DOCUMENT_ID");
			 * tv_document_one.setText(jsonobject .getString("DOCUMENT_NAME"));
			 * 
			 * urlOne=jsonobject.getString("DOCUMENT_URL"); String STATUS =
			 * jsonobject .getString("STATUS"); if(STATUS.equals("1")){
			 * flag=false; btnUploadOne.setClickable(false);
			 * btnDeleteOne.setClickable(false);
			 * btnDeleteOne.setBackgroundResource(R.drawable.ic_deleted); }
			 * else{ flag=true; }
			 * 
			 * Editor editor = mPrefs.edit();
			 * editor.putString("DOCUMENT_ID_First", DOCUMENT_ID);
			 * editor.commit();
			 * 
			 * btnDeleteOne.setVisibility(View.VISIBLE); } else if (i == 1) {
			 * JSONObject jsonobject = data.getJSONObject(i); String DOCUMENT_ID
			 * = jsonobject .getString("DOCUMENT_ID");
			 * tv_document_two.setText(jsonobject .getString("DOCUMENT_NAME"));
			 * urlTwo=jsonobject.getString("DOCUMENT_URL"); String STATUS =
			 * jsonobject .getString("STATUS"); if(STATUS.equals("1")){
			 * flag=false; btnUploadTwo.setClickable(false);
			 * btnDeleteTwo.setClickable(false);
			 * btnDeleteTwo.setBackgroundResource(R.drawable.ic_deleted); }
			 * else{ flag=true; }
			 * 
			 * Editor editor = mPrefs.edit();
			 * editor.putString("DOCUMENT_ID_Second", DOCUMENT_ID);
			 * editor.commit();
			 * 
			 * btnDeleteTwo.setVisibility(View.VISIBLE); } else if (i == 2) {
			 * JSONObject jsonobject = data.getJSONObject(i); String DOCUMENT_ID
			 * = jsonobject .getString("DOCUMENT_ID");
			 * tv_document_three.setText(jsonobject
			 * .getString("DOCUMENT_NAME"));
			 * 
			 * urlThree=jsonobject.getString("DOCUMENT_URL"); String STATUS =
			 * jsonobject .getString("STATUS"); if(STATUS.equals("1")){
			 * flag=false; btnUploadThree.setClickable(false);
			 * btnDeleteThree.setClickable(false);
			 * btnDeleteThree.setBackgroundResource(R.drawable.ic_deleted); }
			 * else{ flag=true; }
			 * 
			 * Editor editor = mPrefs.edit();
			 * editor.putString("DOCUMENT_ID_Third", DOCUMENT_ID);
			 * editor.commit();
			 * 
			 * btnDeleteThree.setVisibility(View.VISIBLE); } else if (i == 3) {
			 * JSONObject jsonobject = data.getJSONObject(i); String DOCUMENT_ID
			 * = jsonobject .getString("DOCUMENT_ID");
			 * tv_document_four.setText(jsonobject .getString("DOCUMENT_NAME"));
			 * urlFour=jsonobject.getString("DOCUMENT_URL"); String STATUS =
			 * jsonobject .getString("STATUS"); if(STATUS.equals("1")){
			 * flag=false; btnUploadFour.setClickable(false);
			 * btnDeleteFour.setClickable(false);
			 * btnDeleteFour.setBackgroundResource(R.drawable.ic_deleted); }
			 * else{ flag=true; }
			 * 
			 * Editor editor = mPrefs.edit();
			 * editor.putString("DOCUMENT_ID_Fourth", DOCUMENT_ID);
			 * editor.commit();
			 * 
			 * btnDeleteFour.setVisibility(View.VISIBLE); }
			 * 
			 * // "DOCUMENT_URL" + // "STATUS
			 * 
			 * if(data.length()==4){ if(flag){
			 * btnUploadDocument.setVisibility(View.VISIBLE); } else {
			 * btnUploadDocument.setVisibility(View.GONE); } } } } else {
			 * btnUploadDocument.setVisibility(View.GONE);
			 * Toast.makeText(DocumentUploadActivity.this, obj.getString("msg"),
			 * Toast.LENGTH_LONG).show(); }
			 * 
			 * } catch (Exception e) { }
			 */
			break;
		case 2:
			try {
				JSONObject obj = new JSONObject(object);

				if (obj.getInt("status") == 1) {
					btnDeleteOne.setVisibility(View.GONE);
					btnDeleteTwo.setVisibility(View.GONE);
					btnDeleteThree.setVisibility(View.GONE);
					btnDeleteFour.setVisibility(View.GONE);

					btnUploadOne.setClickable(true);
					btnUploadTwo.setClickable(true);
					btnUploadThree.setClickable(true);
					btnUploadFour.setClickable(true);

					tv_document_one.setText("");
					tv_document_two.setText("");
					tv_document_three.setText("");
					tv_document_four.setText("");

					/*Toast.makeText(DocumentUploadActivity.this,
							obj.getString("msg"), Toast.LENGTH_LONG).show();*/

					parseData(object);

					/*
					 * JSONArray data = obj.getJSONArray("data");
					 * Log.d("Data deletd", data + "");
					 * 
					 * if(data.length()==0){
					 * btnUploadDocument.setVisibility(View.GONE); }
					 * 
					 * for (int i = 0; i < data.length(); i++) { if (i == 0) {
					 * JSONObject jsonobject = data.getJSONObject(i); String
					 * DOCUMENT_ID = jsonobject .getString("DOCUMENT_ID");
					 * tv_document_one.setText(jsonobject
					 * .getString("DOCUMENT_NAME"));
					 * 
					 * urlOne=jsonobject.getString("DOCUMENT_URL"); Editor
					 * editor = mPrefs.edit();
					 * editor.putString("DOCUMENT_ID_First", DOCUMENT_ID);
					 * editor.commit();
					 * 
					 * btnDeleteOne.setVisibility(View.VISIBLE); } else if (i ==
					 * 1) { JSONObject jsonobject = data.getJSONObject(i);
					 * String DOCUMENT_ID = jsonobject
					 * .getString("DOCUMENT_ID");
					 * tv_document_two.setText(jsonobject
					 * .getString("DOCUMENT_NAME"));
					 * urlTwo=jsonobject.getString("DOCUMENT_URL"); Editor
					 * editor = mPrefs.edit();
					 * editor.putString("DOCUMENT_ID_Second", DOCUMENT_ID);
					 * editor.commit();
					 * 
					 * btnDeleteTwo.setVisibility(View.VISIBLE); } else if (i ==
					 * 2) { JSONObject jsonobject = data.getJSONObject(i);
					 * String DOCUMENT_ID = jsonobject
					 * .getString("DOCUMENT_ID");
					 * tv_document_three.setText(jsonobject
					 * .getString("DOCUMENT_NAME"));
					 * urlThree=jsonobject.getString("DOCUMENT_URL"); Editor
					 * editor = mPrefs.edit();
					 * editor.putString("DOCUMENT_ID_Third", DOCUMENT_ID);
					 * editor.commit();
					 * 
					 * btnDeleteThree.setVisibility(View.VISIBLE); } else if (i
					 * == 3) { JSONObject jsonobject = data.getJSONObject(i);
					 * String DOCUMENT_ID = jsonobject
					 * .getString("DOCUMENT_ID");
					 * tv_document_four.setText(jsonobject
					 * .getString("DOCUMENT_NAME"));
					 * urlFour=jsonobject.getString("DOCUMENT_URL"); Editor
					 * editor = mPrefs.edit();
					 * editor.putString("DOCUMENT_ID_Fourth", DOCUMENT_ID);
					 * editor.commit();
					 * 
					 * btnDeleteFour.setVisibility(View.VISIBLE); }
					 * 
					 * // "DOCUMENT_URL" + // "STATUS } } else {
					 * 
					 * btnUploadDocument.setVisibility(View.GONE);
					 * Toast.makeText(DocumentUploadActivity.this,
					 * obj.getString("msg"), Toast.LENGTH_LONG).show();
					 */
				}

			} catch (Exception e) {
			}
			break;
		case 3:
			parseData(object);
			/*
			 * try { JSONObject obj = new JSONObject(object);
			 * 
			 * if (obj.getInt("status") == 1) {
			 * 
			 * JSONArray data = obj.getJSONArray("data"); boolean flag=true;
			 * if(data.length()==4){ flag=false; }
			 * 
			 * if(data.length()==0){ btnUploadDocument.setVisibility(View.GONE);
			 * } for (int i = 0; i < data.length(); i++) {
			 * 
			 * //JSONObject jsonobject = data.getJSONObject(i);
			 * 
			 * 
			 * if (i == 0) { JSONObject jsonobject = data.getJSONObject(i);
			 * String DOCUMENT_ID = jsonobject .getString("DOCUMENT_ID");
			 * tv_document_one.setText(jsonobject .getString("DOCUMENT_NAME"));
			 * urlOne=jsonobject.getString("DOCUMENT_URL"); 
			 * String STATUS =
			 * jsonobject .getString("STATUS"); if(STATUS.equals("1")){
			 * flag=false; btnUploadOne.setClickable(false);
			 * btnDeleteOne.setClickable(false);
			 * btnDeleteOne.setBackgroundResource(R.drawable.ic_deleted); }
			 * else{ btnUploadOne.setClickable(true);
			 * btnDeleteOne.setClickable(true);
			 * btnDeleteOne.setBackgroundResource(R.drawable.ic_delete);
			 * flag=true; }
			 * 
			 * Editor editor = mPrefs.edit();
			 * editor.putString("DOCUMENT_ID_First", DOCUMENT_ID);
			 * editor.commit();
			 * 
			 * btnDeleteOne.setVisibility(View.VISIBLE); } else if (i == 1) {
			 * JSONObject jsonobject = data.getJSONObject(i); String DOCUMENT_ID
			 * = jsonobject .getString("DOCUMENT_ID");
			 * tv_document_two.setText(jsonobject .getString("DOCUMENT_NAME"));
			 * 
			 * urlTwo=jsonobject.getString("DOCUMENT_URL");
			 * 
			 * String STATUS = jsonobject .getString("STATUS");
			 * if(STATUS.equals("1")){ flag=false;
			 * btnUploadTwo.setClickable(false);
			 * btnDeleteTwo.setClickable(false);
			 * btnDeleteTwo.setBackgroundResource(R.drawable.ic_deleted); }
			 * else{ btnUploadTwo.setClickable(true);
			 * btnDeleteTwo.setClickable(true);
			 * btnDeleteTwo.setBackgroundResource(R.drawable.ic_delete);
			 * flag=true; }
			 * 
			 * Editor editor = mPrefs.edit();
			 * editor.putString("DOCUMENT_ID_Second", DOCUMENT_ID);
			 * editor.commit();
			 * 
			 * btnDeleteTwo.setVisibility(View.VISIBLE); } else if (i == 2) {
			 * JSONObject jsonobject = data.getJSONObject(i); String DOCUMENT_ID
			 * = jsonobject .getString("DOCUMENT_ID");
			 * tv_document_three.setText(jsonobject
			 * .getString("DOCUMENT_NAME"));
			 * 
			 * urlThree=jsonobject.getString("DOCUMENT_URL"); String STATUS =
			 * jsonobject .getString("STATUS"); if(STATUS.equals("1")){
			 * flag=false; btnUploadThree.setClickable(false);
			 * btnDeleteThree.setClickable(false);
			 * btnDeleteThree.setBackgroundResource(R.drawable.ic_deleted); }
			 * else{ btnUploadThree.setClickable(true);
			 * btnDeleteThree.setClickable(true);
			 * btnDeleteThree.setBackgroundResource(R.drawable.ic_delete);
			 * flag=true; }
			 * 
			 * Editor editor = mPrefs.edit();
			 * editor.putString("DOCUMENT_ID_Third", DOCUMENT_ID);
			 * editor.commit();
			 * 
			 * btnDeleteThree.setVisibility(View.VISIBLE); } else if (i == 3) {
			 * JSONObject jsonobject = data.getJSONObject(i); String DOCUMENT_ID
			 * = jsonobject .getString("DOCUMENT_ID");
			 * tv_document_four.setText(jsonobject .getString("DOCUMENT_NAME"));
			 * urlFour=jsonobject.getString("DOCUMENT_URL");
			 * 
			 * String STATUS = jsonobject .getString("STATUS");
			 * if(STATUS.equals("1")){ flag=false;
			 * btnUploadFour.setClickable(false);
			 * btnDeleteFour.setClickable(false);
			 * btnDeleteFour.setBackgroundResource(R.drawable.ic_deleted); }
			 * else{ btnUploadFour.setClickable(true);
			 * btnDeleteFour.setClickable(true);
			 * btnDeleteFour.setBackgroundResource(R.drawable.ic_delete);
			 * flag=true; }
			 * 
			 * Editor editor = mPrefs.edit();
			 * editor.putString("DOCUMENT_ID_Fourth", DOCUMENT_ID);
			 * editor.commit();
			 * 
			 * btnDeleteFour.setVisibility(View.VISIBLE); }
			 * 
			 * }
			 * 
			 * 
			 * if(data.length()==4){ if(flag){
			 * btnUploadDocument.setVisibility(View.VISIBLE); } else {
			 * btnUploadDocument.setVisibility(View.GONE); } } } else{
			 * btnUploadDocument.setVisibility(View.GONE); } }catch (Exception
			 * e) { // TODO: handle exception }
			 */

			break;

		}

	}

	/*
	 * private void askForPermission(String permission, Integer requestCode) {
	 * if (ContextCompat.checkSelfPermission(DocumentUploadActivity.this,
	 * permission) != PackageManager.PERMISSION_GRANTED) {
	 * 
	 * // Should we show an explanation? if
	 * (ActivityCompat.shouldShowRequestPermissionRationale
	 * (DocumentUploadActivity.this, permission)) {
	 * 
	 * //This is called if user has denied the permission before //In this case
	 * I am just asking the permission again
	 * ActivityCompat.requestPermissions(DocumentUploadActivity.this, new
	 * String[]{permission}, requestCode);
	 * 
	 * } else {
	 * 
	 * ActivityCompat.requestPermissions(DocumentUploadActivity.this, new
	 * String[]{permission}, requestCode); } } else { // Toast.makeText(this, ""
	 * + permission + " is already granted.", Toast.LENGTH_SHORT).show(); } }
	 */

	private void parseData(String object) {
		try {
			JSONObject obj = new JSONObject(object);

			if (obj.getInt("status") == 1) {

				JSONArray data = obj.getJSONArray("data");
				boolean flag = false;
				if (data.length() == 4) {
					flag = false;
				}

				if (obj.getString("msg").trim().length() > 0)
					Toast.makeText(DocumentUploadActivity.this,
							obj.getString("msg"), Toast.LENGTH_LONG).show();

				if (data.length() == 0) {
					btnUploadDocument.setVisibility(View.GONE);
				}
				for (int i = 0; i < data.length(); i++) {

					// JSONObject jsonobject = data.getJSONObject(i);

					if (i == 0) {
						JSONObject jsonobject = data.getJSONObject(i);
						String DOCUMENT_ID = jsonobject
								.getString("DOCUMENT_ID");
						tv_document_one.setText(jsonobject
								.getString("DOCUMENT_NAME"));
						urlOne = jsonobject.getString("DOCUMENT_URL");
						String STATUS = jsonobject.getString("STATUS");
						if (STATUS.equals("1")) {
							if (!flag)
								flag = false;
							btnUploadOne.setClickable(false);
							btnDeleteOne.setClickable(false);
							btnDeleteOne
									.setBackgroundResource(R.drawable.ic_deleted);
						} else {
							btnUploadOne.setClickable(true);
							btnDeleteOne.setClickable(true);
							btnDeleteOne
									.setBackgroundResource(R.drawable.ic_delete);
							flag = true;
						}

						Editor editor = mPrefs.edit();
						editor.putString("DOCUMENT_ID_First", DOCUMENT_ID);
						editor.commit();

						btnDeleteOne.setVisibility(View.VISIBLE);
					} else if (i == 1) {
						JSONObject jsonobject = data.getJSONObject(i);
						String DOCUMENT_ID = jsonobject
								.getString("DOCUMENT_ID");
						tv_document_two.setText(jsonobject
								.getString("DOCUMENT_NAME"));

						urlTwo = jsonobject.getString("DOCUMENT_URL");

						String STATUS = jsonobject.getString("STATUS");
						if (STATUS.equals("1")) {
							if (!flag)
								flag = false;
							btnUploadTwo.setClickable(false);
							btnDeleteTwo.setClickable(false);
							btnDeleteTwo
									.setBackgroundResource(R.drawable.ic_deleted);
						} else {
							btnUploadTwo.setClickable(true);
							btnDeleteTwo.setClickable(true);
							btnDeleteTwo
									.setBackgroundResource(R.drawable.ic_delete);

							flag = true;
						}

						Editor editor = mPrefs.edit();
						editor.putString("DOCUMENT_ID_Second", DOCUMENT_ID);
						editor.commit();

						btnDeleteTwo.setVisibility(View.VISIBLE);
					} else if (i == 2) {
						JSONObject jsonobject = data.getJSONObject(i);
						String DOCUMENT_ID = jsonobject
								.getString("DOCUMENT_ID");
						tv_document_three.setText(jsonobject
								.getString("DOCUMENT_NAME"));

						urlThree = jsonobject.getString("DOCUMENT_URL");
						String STATUS = jsonobject.getString("STATUS");
						if (STATUS.equals("1")) {
							if (!flag)
								flag = false;
							btnUploadThree.setClickable(false);
							btnDeleteThree.setClickable(false);
							btnDeleteThree
									.setBackgroundResource(R.drawable.ic_deleted);
						} else {
							btnUploadThree.setClickable(true);
							btnDeleteThree.setClickable(true);
							btnDeleteThree
									.setBackgroundResource(R.drawable.ic_delete);
							flag = true;
						}

						Editor editor = mPrefs.edit();
						editor.putString("DOCUMENT_ID_Third", DOCUMENT_ID);
						editor.commit();

						btnDeleteThree.setVisibility(View.VISIBLE);
					} else if (i == 3) {
						JSONObject jsonobject = data.getJSONObject(i);
						String DOCUMENT_ID = jsonobject
								.getString("DOCUMENT_ID");
						tv_document_four.setText(jsonobject
								.getString("DOCUMENT_NAME"));
						urlFour = jsonobject.getString("DOCUMENT_URL");

						String STATUS = jsonobject.getString("STATUS");
						if (STATUS.equals("1")) {
							if (!flag)
								flag = false;
							btnUploadFour.setClickable(false);
							btnDeleteFour.setClickable(false);
							btnDeleteFour
									.setBackgroundResource(R.drawable.ic_deleted);
						} else {
							btnUploadFour.setClickable(true);
							btnDeleteFour.setClickable(true);
							btnDeleteFour
									.setBackgroundResource(R.drawable.ic_delete);
							flag = true;
						}

						Editor editor = mPrefs.edit();
						editor.putString("DOCUMENT_ID_Fourth", DOCUMENT_ID);
						editor.commit();

						btnDeleteFour.setVisibility(View.VISIBLE);
					}

				}

				if (data.length() == 4) {
					if (flag) {
						btnUploadDocument.setVisibility(View.VISIBLE);
					} else {
						btnUploadDocument.setVisibility(View.GONE);
					}
				}
			} else {
				btnUploadDocument.setVisibility(View.GONE);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

}
