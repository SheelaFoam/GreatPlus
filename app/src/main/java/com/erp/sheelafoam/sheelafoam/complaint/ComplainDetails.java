package com.erp.sheelafoam.sheelafoam.complaint;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Parcelable;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.content.IntentCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.erp.sheelafoam.R;
import com.erp.sheelafoam.sheelafoam.activity.SplashActivity;
import com.erp.sheelafoam.sheelafoam.entry.ImageEntry;
import com.erp.sheelafoam.sheelafoam.utility.ConnectionDetector;
import com.erp.sheelafoam.sheelafoam.utility.CustomLoader;
import com.erp.sheelafoam.sheelafoam.utility.DBHelper;
import com.erp.sheelafoam.sheelafoam.utility.GlobalVariables;
import com.erp.sheelafoam.sheelafoam.utility.UploadFile;
import com.erp.sheelafoam.sheelafoam.xperia.activity.ZoomImageFr;
import com.erp.sheelafoam.sheelafoam.xperia.adapter.XperiaImageAdapter;
import com.erp.sheelafoam.sheelafoam.xperia.dataholders.ComplaintNumberHolder;
import com.erp.sheelafoam.sheelafoam.xperia.function.ComplaintDataBase;
import com.erp.sheelafoam.utils.FileUpload;

import org.apache.commons.io.FilenameUtils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import nl.changer.polypicker.ImagePickerActivity;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class ComplainDetails extends Activity implements OnClickListener {

	// http://stackoverflow.com/questions/9396805/create-soap-request-using-ksoap-android

	private Uri fileUri; // file url to store image
	private static final String IMAGE_DIRECTORY_NAME = "SF";
	private final int CAMERA_PIC_ACTION = 100;
	private final int GALLERY_PIC_ACTION = 200;
	private static int count = 1,countImg=1,docOrimage=0;
	private String complainNum = "",Opt="";

	private static final int CAMERA_CONSTANT = 100;

	//public static final String DATA_SAVED_BROADCAST = "net.simplifiedcoding.datasaved";

	List<String> imgList = new ArrayList<String>();

	ArrayList<ImageEntry> arrayList_image = new ArrayList<ImageEntry>();
	ArrayList<ImageEntry> arrayList_Docimage = new ArrayList<ImageEntry>();

	ArrayList<String> DocImageName = new ArrayList<String>();
	ArrayList<String> MatImageName = new ArrayList<String>();

	String remarks = "", dbremarks = "";

	ComplaintDataBase coDataBase;

	int heightOfScreen;
	int widthOfScreen;
	int baseHeight;
	int baseWidth;

	private static final int PERMISSION_REQUEST_CODE = 1;
	GridView gridview,gridDoc;
	Button addImageBtn, addDocbutton;
	Button submitBtn, btnSave;
	ImageButton btn_logout;
	EditText edittext_remark;
	String imgName;
	TextView textview_complaintID, textview_app_title;
	RelativeLayout rlayout_remark, rlayout_bottom;

	String mobileNum;
	DBHelper dbConn = null;
	String imgStr = "", remark = "",imgStr1 = "";
	String user_role = "";
	Bitmap bM;
	ConnectionDetector con;
	ProgressDialog dialog;
	String imageName;
	int position;
	BroadcastReceiver broadcastReceiver;
	CheckBox genuineCb, inGenuineCb;
	ProgressDialog dialog_loadImage;
	String strResult1 = null;
	CustomLoader loader;
	Handler handler;
	private SharedPreferences mPrefs;
	public static ComplainDetails complaintDetails;

	public static ComplainDetails getInstance() {
		if (complaintDetails == null) {
			complaintDetails = new ComplainDetails();
		}
		return complaintDetails;
	}

	public static ComplainDetails getInstanceNew() {

		complaintDetails = new ComplainDetails();

		return complaintDetails;
	}

	public ComplainDetails() {
		complaintDetails = this;
	}

	boolean checkboxFlag = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_complain_details);
		dbConn = DBHelper.getDBHelper(this);
		handler = new Handler();
		mPrefs = getSharedPreferences("Location", Context.MODE_PRIVATE);

		user_role = "Agent";
		ComplaintNumberHolder.setIsResume(2);
		complainNum = getIntent().getStringExtra("complainid");
		mobileNum = getIntent().getStringExtra("mobile");
		Opt = getIntent().getStringExtra("opt");
		coDataBase=new ComplaintDataBase(getApplicationContext());
		DocImageName.clear();
		MatImageName.clear();

		if (imgList.size() > 0) {
			imgList.clear();
		}
		if (arrayList_image.size() > 0) {
			arrayList_image.clear();
		}

		//success("9810702659","58742","MAT_Image132244***MAT_Image132244***DOC_Image132244***DOC_Image132244rt***DOC_Image132244jrt","testing");

		initializeUiControls();

		checkCameraPermission();
		btn_logout.setOnClickListener(this);
		getSppanable();
		con = new ConnectionDetector(this);

		genuineCb = (CheckBox) findViewById(R.id.genuine);
		inGenuineCb = (CheckBox) findViewById(R.id.in_genuine);

		genuineCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
				if(b){
					genuineCb.setChecked(true);
					checkboxFlag = true;
					inGenuineCb.setChecked(false);
				}else {
					checkboxFlag = false;
					genuineCb.setChecked(false);
				}
			}
		});


		inGenuineCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
				if(b){
					checkboxFlag = true;
					inGenuineCb.setChecked(true);
					genuineCb.setChecked(false);
				}else {
					checkboxFlag = false;
					inGenuineCb.setChecked(false);
				}
			}
		});
		Log.e("mobile number", mobileNum);
		textview_complaintID.setText("Complaint Id :" + " " + complainNum);

		DisplayMetrics dm = new DisplayMetrics();
		try {
			getWindowManager().getDefaultDisplay().getMetrics(dm);
		} catch (Exception e) {
			e.printStackTrace();
		}

		heightOfScreen = dm.heightPixels;
		widthOfScreen = dm.widthPixels;


		addImageBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				if (checkPermission()) {

					selectImageFromPhone(1);
				} else {

					requestPermission();
				}


			}
		});
		submitBtn = (Button) findViewById(R.id.btn_submit);
		btnSave = (Button) findViewById(R.id.btn_save);
		btnSave.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v)
			{

				if(checkboxFlag) {
					if (imgList.size() > 0 || edittext_remark.getText().toString().trim().length() > 0) {
						remarks = edittext_remark.getText().toString();
						int status = coDataBase.saveMatDetails(complainNum, mobileNum, imgList, remarks, "N");
						if (status == 1) {
							Toast.makeText(getApplicationContext(), "Details saved successfully", Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(getApplicationContext(), "Details not saved. Error occurred", Toast.LENGTH_SHORT).show();
						}
					} else {
						Toast.makeText(getApplicationContext(), "Nothing for save", Toast.LENGTH_SHORT).show();
					}
					ComplaintNumberHolder.setIsSuccess(0);
				}else {
					Toast.makeText(ComplainDetails.this, "Please select Genuine or In-Genuine", Toast.LENGTH_SHORT).show();
				}
			}
		});

		submitBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				if(checkboxFlag) {
					showAlert();
				}else {
					Toast.makeText(ComplainDetails.this, "Please select Genuine or In-Genuine", Toast.LENGTH_SHORT).show();
				}

				//Toast.makeText(getApplicationContext(), imgList.size()+"", 500).show();
			}
		});

		addDocbutton = (Button) findViewById(R.id.addDoc);
		addDocbutton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				selectImageFromPhone(2);
			}
		});

	}

	AlertDialog alertDialog;
	private void showAlert(){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Are you sure ?")
				.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// FIRE ZE MISSILES!
//						if (isNetworkAvailable()) {
							//Toast.makeText(ComplainDetails.this, "Internet Available", Toast.LENGTH_SHORT).show();
							onSubmitClick();
							//mSetTextView.setText("Internet is here");
//						}else {

//							Toast.makeText(ComplainDetails.this, "Internet not Available", Toast.LENGTH_SHORT).show();
//						}
					}
				})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// User cancelled the dialog
						alertDialog.dismiss();
					}
				});
		// Create the AlertDialog object and return it
		alertDialog = builder.create();
		alertDialog.show();
	}

	// To check availability of internet
	private boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager
				= (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}

	/*@Override
	protected void onStop()
	{
		unregisterReceiver( broadcastReceiver);
		super.onStop();
	}*/

	// Initialize UI controls.

	public void initializeUiControls() {
		gridview = (GridView) findViewById(R.id.listView1);
		gridDoc = (GridView) findViewById(R.id.docImageList);

		gridview.setOnItemClickListener(new OnItemClickListener()

		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
//				Intent i = new Intent(getApplicationContext(), ZoomImageFr.class);
//				i.putExtra("ImgName", MatImageName.get(position));

//				startActivityForResult(i,1);

				Uri uri = getFile(MatImageName.get(position));
				if(uri!=null) {
					imageName = MatImageName.get(position);
					ComplainDetails.this.position = position;
					Intent editIntent = new Intent();
					editIntent.setAction(Intent.ACTION_EDIT);
					editIntent.setDataAndType(uri, "image/*");
					editIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
					startActivityForResult(Intent.createChooser(editIntent, null),1);
				}
				// Toast.makeText(getApplicationContext(), position+"",
				// 1000).show();
			}
		});

		gridDoc.setOnItemClickListener(new OnItemClickListener()

		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getApplicationContext(), ZoomImageFr.class);
				i.putExtra("ImgName", DocImageName.get(position));
				i.putExtra("position",position);
				startActivity(i);


				// Toast.makeText(getApplicationContext(), position+"",
				// 1000).show();
			}
		});

		gridDoc.setLongClickable(true);
		gridDoc.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
			{
				deleteImageFromList(2, position);
				return true;
			}
		});

		gridview.setLongClickable(true);
		gridview.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
			{
				deleteImageFromList(1, position);
				return true;
			}
		});



		textview_complaintID = (TextView) findViewById(R.id.textview_complaintID);
		textview_app_title = (TextView) findViewById(R.id.textview_back);
		edittext_remark = (EditText) findViewById(R.id.edittext_remark);
		addImageBtn = (Button) findViewById(R.id.btn_image);
		btn_logout = (ImageButton) findViewById(R.id.btn_logout);

		if(Opt.equalsIgnoreCase("1"))
		{
			getImages();
		}
		else
		{
			getFailedImages();
		}


		// rlayout_remark = (RelativeLayout) findViewById(R.id.rlayout_remark);
		rlayout_bottom = (RelativeLayout) findViewById(R.id.rlayout_bottom);

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


	private Uri getFile(String nameFile){
		File f = new File(Environment.getExternalStorageDirectory() + "/Pictures/SF/" + nameFile+".jpg");
		if(f.exists()){


			if (Build.VERSION.SDK_INT > 23) { //use this if Lollipop_Mr1 (API 23) or above
				return FileProvider.getUriForFile(getApplicationContext(), getPackageName()+".fileprovider", f);
			} else {
				return Uri.fromFile(f);
			}
//			return Uri.fromFile(f);
		}
		return null;
	}
	//Check Camera Permission and storage permission

	public void checkCameraPermission() {
		if (ActivityCompat.checkSelfPermission(ComplainDetails.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
			if (ActivityCompat.shouldShowRequestPermissionRationale(ComplainDetails.this, Manifest.permission.CAMERA)) {
				//Show Information about why you need the permission
				ActivityCompat.requestPermissions(ComplainDetails.this, new String[]{Manifest.permission.CAMERA}, CAMERA_CONSTANT);

			} else {
				//just request the permission
				ActivityCompat.requestPermissions(ComplainDetails.this, new String[]{Manifest.permission.CAMERA}, CAMERA_CONSTANT);
			}




		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (requestCode == CAMERA_CONSTANT) {
			if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


			} else {
				if (ActivityCompat.shouldShowRequestPermissionRationale(ComplainDetails.this, Manifest.permission.CAMERA)) {
					ActivityCompat.requestPermissions(ComplainDetails.this, new String[]{Manifest.permission.CAMERA}, CAMERA_CONSTANT);
				}
				else {
//					SharedPreferences.Editor editor = mPrefs.edit();
//					editor.putBoolean("deny", true);
//					editor.commit();


					Toast.makeText(getBaseContext(),"Enable Camera permission in Settings > Apps > GreatPlus > Permissions",Toast.LENGTH_LONG).show();
				}
			}
		}

		if(requestCode == PERMISSION_REQUEST_CODE ){

			if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

				//Snackbar.make(view,"Permission Granted, Now you can access location data.",Snackbar.LENGTH_LONG).show();
				selectImageFromPhone(1);

			} else {

				//Snackbar.make(view,"Permission Denied, You cannot access location data.",Snackbar.LENGTH_LONG).show();
				Toast.makeText(ComplainDetails.this, "Permission Denied, You cannot access location data.", Toast.LENGTH_SHORT).show();

			}
		}

	}

	public void setBitmap(Bitmap bitmap) {
		// TODO Auto-generated method stub

		bM = bitmap;

	}


	public Bitmap getBitMap() {
		return bM;
	}


	private void onSubmitClick() {
		UploadFile.strMobileNo = mobileNum;
		UploadFile.strCMPID = complainNum;

		remarks=edittext_remark.getText().toString();
		int statussave =coDataBase.saveMatDetails(complainNum, mobileNum,imgList,remarks,"N");

		int statusupdate=coDataBase.updateMatDetailsTry(complainNum, mobileNum,imgList);

		remark = edittext_remark.getText().toString().trim();
//		if (con.isConnectingToInternet()) {
			//imgList.removeAll(getServerImageList(strResult1));

			if (remark.length() > 0 && imgList.size() > 0) {
				new AsyncTask_UploadImages().execute();
			} else if (remark.length() == 0) {

				alertDialogWithTwoButtonForRemark();
			} else {

				alertDialogWithTwoButtonForImage();
			}

//		} else {
//			GlobalVariables.defaultOneButtonDialog(ComplainDetails.this, GlobalVariables.CONNECTION_ERROR);
//		}

	}


	String genuineType;
	class AsyncTask_UploadImages extends AsyncTask<Void, Void, String> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

			dialog = new ProgressDialog(ComplainDetails.this);
			dialog.setMessage("Please wait........");
			dialog.setCancelable(false);
			dialog.show();
		}

		@Override
		protected String doInBackground(Void... params) {

			if(genuineCb.isChecked()){
				genuineType = "genuine";
			}else {
				genuineType = "ingenuine";
			}
			new FileUpload(complainNum, imgList,remark, genuineType,getApplicationContext(),ComplainDetails.this).start();
			while (!UploadFile.isUploadComplete) {

			}

			return null;
		}

		@Override
		protected void onPostExecute(String aVoid) {

			super.onPostExecute(aVoid);
			if (null != dialog && dialog.isShowing()) {
				dialog.cancel();

				imgStr = UploadFile.imgStr;
				UploadFile.isUploadComplete = false;

				Toast.makeText(ComplainDetails.this, "Images Uploaded Successfully", Toast.LENGTH_LONG).show();
				imgList.clear();
				//UploadFile.intCountSuccess=0;
				UploadFile.intCountSuccess=0;
				UploadFile.intCountFailed=0;
				UploadFile.imgStr="";
				//Toast.makeText(getApplicationContext(), imgList.size()+"", 500).show();
				ComplaintNumberHolder.setIsSuccess(1);
				goBack();
			}else {

			}
		}
	}


	private void goBack() {
		finish();

	}


	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		ComplaintNumberHolder.setIsSuccess(0);
	}

	XperiaImageAdapter bMap;
	private void addImageInList() {
		bMap=new XperiaImageAdapter(MatImageName, ComplainDetails.this);
		gridview.setAdapter(bMap);
	}


	XperiaImageAdapter bMap2;
	private void addImageInListDoc() {
		bMap2 = new XperiaImageAdapter(DocImageName, ComplainDetails.this);
		gridDoc.setAdapter(bMap2);
	}


	public void refreshDocImage()
	{
		bMap2.notifyDataSetChanged();
		gridDoc.setAdapter(bMap2);
	}


	public void refreshMatImage()
	{
		bMap.notifyDataSetChanged();
//		gridview.setAdapter(bMap);
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}


	private void captureImage() {
		if (count < 5) {
//			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

//			if(Build.VERSION.SDK_INT>24) {
//				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//
//				if (intent.resolveActivity(getPackageManager()) != null) {
////            fileTemp = ImageUtils.getOutputMediaFile();
//					ContentValues values = new ContentValues(1);
//					values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg");
//					fileUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
////            if (fileTemp != null) {
////            fileUri = Uri.fromFile(fileTemp);
//
////                fileUri = getOutputMediaFileUri(0);
//
//					intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
//					intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
//					startActivityForResult(intent, CAMERA_PIC_ACTION);
////
//				} else {
//					Toast.makeText(this, getString(R.string.error_no_camera), Toast.LENGTH_LONG).show();
//				}
//			}
//			else {
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

			fileUri = getOutputMediaFileUri(0);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
//			// start the image capture Intent
			startActivityForResult(intent, CAMERA_PIC_ACTION);

//			}
//
//
//            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
//			// start the image capture Intent
//			startActivityForResult(intent, CAMERA_PIC_ACTION);
		}
	}


	public Uri getOutputMediaFileUri(int type) {

		if (Build.VERSION.SDK_INT > 23) { //use this if Lollipop_Mr1 (API 23) or above
			return FileProvider.getUriForFile(getApplicationContext(), "com.erp.sheelafoam.fileprovider", getOutputMediaFile(docOrimage));
		} else {
			return Uri.fromFile(getOutputMediaFile(docOrimage));
		}
//		return Uri.fromFile(getOutputMediaFile(docOrimage));
	}


	private File getOutputMediaFile(int type) {

		// External sdcard location
		File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
				IMAGE_DIRECTORY_NAME);

		// Create the storage directory if it does not exist
		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {
				Log.e(IMAGE_DIRECTORY_NAME, "Oops! Failed create " + IMAGE_DIRECTORY_NAME + " directory");
				return null;
			}
		}

		// Create a media file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
		File mediaFile=null;
		if (type == 1) {
			if (null != complainNum){
				/*
				 * imgName = "IMG_"+complainNum+"_"+count; else
				 */
				imgName = "MAT_IMG_" + timeStamp + "_" + countImg;

				mediaFile = new File(mediaStorageDir.getPath() + File.separator + imgName + ".jpg");}
		}
		else if (type == 2) {
			if (null != complainNum){
				/*
				 * imgName = "IMG_"+complainNum+"_"+count; else
				 */
				imgName = "DOC_IMG_" + timeStamp + "_" + countImg;

				mediaFile = new File(mediaStorageDir.getPath() + File.separator + imgName + ".jpg");}
		}

		return mediaFile;
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == CAMERA_PIC_ACTION) {
			if (resultCode == RESULT_OK) {
				// successfully captured the image
				// display it in image view

				imgList.add(imgName);

				countImg++;
				if(docOrimage==1)
				{
					MatImageName.add(imgName);
					refreshMatImage();
				}

				if(docOrimage==2)
				{
					DocImageName.add(imgName);
					refreshDocImage();
				}

			} else if (resultCode == RESULT_CANCELED) {
				// user cancelled Image capture
				Toast.makeText(getApplicationContext(), "User cancelled image capture", Toast.LENGTH_SHORT).show();
			} else {
				// failed to capture image
				Toast.makeText(getApplicationContext(), "Sorry! Failed to capture image", Toast.LENGTH_SHORT).show();
			}
		}

		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == GALLERY_PIC_ACTION) {
				Parcelable[] parcelableUris = data.getParcelableArrayExtra(ImagePickerActivity.EXTRA_IMAGE_URIS);

				if (parcelableUris == null) {
					return;
				}

				// Java doesn't allow array casting, this is a little hack
				Uri[] uris = new Uri[parcelableUris.length];
				System.arraycopy(parcelableUris, 0, uris, 0, parcelableUris.length);
				if (uris != null) {
					for (Uri uri : uris) {
						Log.e("Image Path ", uri.getPath() + "\n" + uri +"");
						saveImages(uri);
					}
					if(docOrimage==1)
					{
						refreshMatImage();
					}

					if(docOrimage==2)
					{
						refreshDocImage();
					}
				}
			}
		}

		if (requestCode == 1) {
			File file = null;
			if(resultCode == Activity.RESULT_OK){
//				String result=data.getStringExtra("counter");
//				int position = data.getIntExtra("position",0);
				Log.d("TAG", "onActivityResult: "+data);
				Uri uri = data.getData();
//				String ImageName = data.getStringExtra("ImageName");
				//MatImageName.set(position,MatImageName.get(position)+""+result);

				try {
					// Save the image to the SD card.

					File folder = new File(Environment.getExternalStorageDirectory() + "/Pictures/SF/" + new Date().getTime()+".jpg");

					if (!folder.exists()) {
						folder.mkdir();
						//folder.mkdirs();  //For creating multiple directories
					}

					//File file = new File(Environment.getExternalStorageDirectory()+"/DrawTextOnImg/tempImg.png");
					file = new File(Environment.getExternalStorageDirectory() + "/Pictures/SF/" + new Date().getTime()+".jpg");
					copyFileStream(file,uri,this);
					// Android equipment Gallery application will only at boot time scanning system folder
					// The simulation of a media loading broadcast, for the preservation of images can be viewed in Gallery

            /*Intent intent = new Intent();
            intent.setAction(Intent.ACTION_MEDIA_MOUNTED);
            intent.setData(Uri.fromFile(Environment.getExternalStorageDirectory()));
            sendBroadcast(intent);
*/

				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
//				MatImageName.remove(this.imageName);
//				Log.d("TAG", "onActivityResult: "+this.imageName);
//				Log.d("TAG", "onActivityResult: "+this.imageName);
//				MatImageName.add(this.imageName);
				MatImageName.remove(this.imageName);
				if(file!=null&&file.exists()) {
					MatImageName.add(file.getName().split(".jpg")[0]);
//					MatImageName.set(position,file.getName().split(".jpg")[0]);
					Log.d("TAG", "onActivityResult: "+file.getName().split(".jpg")[0]);
				}
				refreshMatImage();
			}
			if (resultCode == Activity.RESULT_CANCELED) {
				//Write your code if there's no result
			}
		}

	}


	public Bitmap getbitpam(String imgName) {
		File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
				IMAGE_DIRECTORY_NAME);
		String path = mediaStorageDir.getPath() + File.separator + imgName + ".jpg";

		Bitmap imgthumBitmap = null;
		try {
			final int THUMBNAIL_SIZE = 64;

			FileInputStream fis = new FileInputStream(path);
			imgthumBitmap = BitmapFactory.decodeStream(fis);

			imgthumBitmap = Bitmap.createScaledBitmap(imgthumBitmap, THUMBNAIL_SIZE, THUMBNAIL_SIZE, false);

			ByteArrayOutputStream bytearroutstream = new ByteArrayOutputStream();
			imgthumBitmap.compress(Bitmap.CompressFormat.JPEG, 150, bytearroutstream);

		} catch (Exception ex) {

		}
		return imgthumBitmap;
	}


	class Adapter extends ArrayAdapter<Bitmap> {
		List<Bitmap> items;
		ImageView image;

		public Adapter(Context c, int resources, ArrayList<Bitmap> list) {
			super(c, resources, list);
			this.items = list;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			if (convertView == null) {
				convertView = LayoutInflater.from(getContext()).inflate(R.layout.thumbnail, parent, false);
			}
			image = (ImageView) convertView.findViewById(R.id.images);
			image.setImageBitmap(items.get(position));
			return convertView;

		}
	}


	public void getSppanable() {

		SpannableString spannable_string = new SpannableString(textview_app_title.getText().toString().trim());
		spannable_string.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.color_great)), 0, 5,
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		spannable_string.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.color_plus)), 5, 10,
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

		textview_app_title.setText(spannable_string);
	}


	public void logout(Activity activity) {

		SharedPreferences prefs = activity.getSharedPreferences("Location", Context.MODE_PRIVATE);
		Editor editor = prefs.edit();

		editor.clear();
		editor.commit();

		removeAll();

		Intent intent = new Intent(activity, SplashActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		activity.startActivity(intent);
	}


	public void removeAll() {
		SQLiteDatabase db = dbConn.getWritableDatabase();
		db.delete(DBHelper.TABLE_COMPLAINT, null, null);

	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {
			case R.id.btn_logout:
				logout(this);
				break;

			default:
				break;
		}

	}


	public void alertDialogWithTwoButtonForImage() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ComplainDetails.this);

		// set title
		alertDialogBuilder.setTitle("Greate Plus");

		// set dialog message
		alertDialogBuilder.setMessage("Do you want to click images for this remarks").setCancelable(false)
				.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// if this button is clicked, close
						// current activity
						//captureImage();
						selectImageFromPhone(1);
					}
				}).setNegativeButton("No", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				// if this button is clicked, just close
				// the dialog box and do nothing
				dialog.cancel();
				new AsyncTask_UploadImages().execute();
			}
		});

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();
	}


	public void alertDialogWithTwoButtonForRemark() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ComplainDetails.this);

		// set title
		alertDialogBuilder.setTitle("Greate Plus");

		// set dialog message
		alertDialogBuilder.setMessage("Do you want to add remark for these images").setCancelable(false)
				.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// if this button is clicked, close
						// current activity
						// captureImage();
					}
				}).setNegativeButton("No", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				// if this button is clicked, just close
				// the dialog box and do nothing
				dialog.cancel();
				remark = "";
				new AsyncTask_UploadImages().execute();
			}
		});

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();
	}


	public void selectImageFromPhone(int btCode) {
		docOrimage=btCode;
		final Dialog dialog = new Dialog(ComplainDetails.this, R.style.MyDialog2);
		View view = LayoutInflater.from(ComplainDetails.this).inflate(R.layout.select_image_dialog, null);
		WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
		params.width = WindowManager.LayoutParams.MATCH_PARENT;
		params.height = WindowManager.LayoutParams.WRAP_CONTENT;
		dialog.setContentView(view);
		dialog.getWindow().setGravity(Gravity.BOTTOM);
		// dialog.getWindow().setWindowAnimations(R.style.DialogAnimations_SmileWindow);

		TextView cam = (TextView) view.findViewById(R.id.camra);
		TextView gal = (TextView) view.findViewById(R.id.galry);
		TextView can = (TextView) view.findViewById(R.id.cancl);
		cam.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				captureImage();
				// Toast.makeText(getApplicationContext(), "Camera",
				// 500).show();
				dialog.dismiss();
			}
		});

		gal.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ComplainDetails.this, ImagePickerActivity.class);
				intent.putExtra(ImagePickerActivity.EXTRA_IMAGE_URIS, 100);
				startActivityForResult(intent, GALLERY_PIC_ACTION);
				dialog.dismiss();
			}
		});

		can.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		dialog.show();
	}


	public void deleteImageFromList(final int btCode,final int position) {
		//docOrimage=btCode;
		final Dialog dialog = new Dialog(ComplainDetails.this, R.style.MyDialog2);
		View view = LayoutInflater.from(ComplainDetails.this).inflate(R.layout.image_delete_from_list, null);
		WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
		params.width = WindowManager.LayoutParams.MATCH_PARENT;
		params.height = WindowManager.LayoutParams.WRAP_CONTENT;
		dialog.setContentView(view);
		dialog.getWindow().setGravity(Gravity.BOTTOM);
		// dialog.getWindow().setWindowAnimations(R.style.DialogAnimations_SmileWindow);


		TextView gal = (TextView) view.findViewById(R.id.galry);
		TextView can = (TextView) view.findViewById(R.id.cancl);

		gal.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				if(btCode==1)
				{
					try
					{
						String removeditem=MatImageName.get(position);
						//arrayList_image.remove(position);
						MatImageName.remove(position);
						imgList.remove(removeditem);
					}catch(Exception e){}
					Toast.makeText(getApplicationContext(),"Image removed from matress list",
							Toast.LENGTH_SHORT).show();
					refreshMatImage();
				}else if(btCode==2)
				{
					try
					{
						String removeditem=DocImageName.get(position);
						//arrayList_Docimage.remove(position);
						DocImageName.remove(position);
						imgList.remove(removeditem);
					}catch(Exception e){}
					Toast.makeText(getApplicationContext(), "image removed from doc list",
							Toast.LENGTH_SHORT).show();
					refreshDocImage();
				}
				dialog.dismiss();
			}
		});

		can.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		dialog.show();
	}


	public String getFileName(Uri uri) {
		String result = null;
		if (uri.getScheme().equals("content")) {
			Cursor cursor = getContentResolver().query(uri, null, null, null, null);
			try {
				if (cursor != null && cursor.moveToFirst()) {
					result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
				}
			} finally {
				cursor.close();
			}
		}
		if (result == null) {
			result = uri.getPath();
			int cut = result.lastIndexOf('/');
			if (cut != -1) {
				result = result.substring(cut + 1);
			}
		}


		return result;
	}


	void getImages()
	{
		Cursor cursor=coDataBase.getMatDetails(complainNum, mobileNum);
		if (cursor.moveToFirst()) {
			do {
				String imgName=cursor.getString(0);
				dbremarks=cursor.getString(1);
				Log.e("Status ", cursor.getString(2)+"");
				try
				{
					if(imgName.substring(0, 3).equalsIgnoreCase("MAT"))
					{
						MatImageName.add(imgName);
						imgList.add(imgName);
					}
					else
					{
						DocImageName.add(imgName);
						imgList.add(imgName);
					}
				}catch(Exception e){}
			} while (cursor.moveToNext());
		} else {

		}
		addImageInList();
		addImageInListDoc();
		edittext_remark.setText(dbremarks);
		remark=dbremarks;
	}

	void getFailedImages()
	{
		Cursor cursor=coDataBase.getFailedMatImages(complainNum, mobileNum);
		if (cursor.moveToFirst()) {
			do {
				String imgName=cursor.getString(0);
				dbremarks=cursor.getString(1);
				Log.e("Status ", cursor.getString(2)+"");
				try
				{
					if(imgName.substring(0, 3).equalsIgnoreCase("MAT"))
					{
						MatImageName.add(imgName);
						imgList.add(imgName);
					}
					else
					{
						DocImageName.add(imgName);
						imgList.add(imgName);
					}
				}catch(Exception e){e.printStackTrace();}
			} while (cursor.moveToNext());
		} else {
			Log.d("","");
		}
		addImageInList();
		addImageInListDoc();
		edittext_remark.setText(dbremarks);
		remark=dbremarks;
	}


	void saveImages(Uri selectedImage)
	{
		try {

			File ff=new File(selectedImage.toString());

			File fl = new File(
					Environment.getExternalStorageDirectory() + "/Pictures/SF");
			if(!fl.exists()) {
				fl.mkdirs();
			}

			File toTemp = new File(
					Environment.getExternalStorageDirectory() + "/Pictures/SF/" + ff.getName());
			String toName="";
			if(docOrimage==1)
			{
				toName="MAT_"+FilenameUtils.removeExtension(toTemp.getName())+".jpg";
			}else if(docOrimage==2)
			{
				toName="DOC_"+FilenameUtils.removeExtension(toTemp.getName())+".jpg";
			}

			//String toName="DOC_"+FilenameUtils.removeExtension(toTemp.getName())+".jpg";

			File to = new File(
					Environment.getExternalStorageDirectory() + "/Pictures/SF/" + toName);

			Bitmap bitmap = BitmapFactory.decodeFile(ff.getAbsolutePath());
			try {

				ByteArrayOutputStream bytes = new ByteArrayOutputStream();
				bitmap.compress(Bitmap.CompressFormat.JPEG, 70, bytes);
				to.createNewFile();
				// write the bytes in file
				FileOutputStream fo = new FileOutputStream(to);
				fo.write(bytes.toByteArray());
				// remember close de FileOutput
				fo.close();

				String imgName=FilenameUtils.removeExtension(to.getName());

//
				if(docOrimage==1)
				{
					imgList.add(imgName);
					MatImageName.add(imgName);
					//refreshMatImage();
				}

				if(docOrimage==2)
				{
					imgList.add(imgName);
					DocImageName.add(imgName);
					//refreshDocImage();
				}


			} catch (Exception e) {
				Toast.makeText(getApplicationContext(), "error while loading images into memory", Toast.LENGTH_SHORT).show();
			}


		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), "error while loading images into memory", Toast.LENGTH_SHORT).show();
		}
	}

	public void copyFileStream(File dest, Uri uri, Context context) throws IOException {
		InputStream is = null;
		OutputStream os = null;
		try {
			is = context.getContentResolver().openInputStream(uri);
			os = new FileOutputStream(dest);
			byte[] buffer = new byte[1024];
			int length;


			if (is != null) {
				while ((length = is.read(buffer)) > 0) {
					os.write(buffer, 0, length);
				}
				is.close();
				os.close();
			}


		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public class upDateServerImages extends AsyncTask<String, String, String> {

		StringBuilder stringBuilder = new StringBuilder();
		String mob,comp,image,remark;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... params) {

			mob=params[0];
			comp=params[1];
			image=params[2];
			remark=params[3];

			try {
				URL url = new URL("http://125.19.46.252/ws/ComplaintVisitReport.php?MOBILE=" + mob + "&ID=" + comp + "&IMAGE=" + image + "&REMARK=" + remark + "");
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
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String s) {
			try {
				Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}


	public void success(String Mob, String Comp, List<String> imgList, String Rem)
	{
		for (String img : imgList) {
			if (imgStr1 == null || imgStr1 == "")
				imgStr1 = img;
			else
				imgStr1 = imgStr1 + "***" + img;
		}
		Toast.makeText(getApplicationContext(), Mob+Comp+imgStr1+Rem, Toast.LENGTH_SHORT).show();
		//new upDateServerImages().execute(Mob,Comp,Images,Rem);
	}

/*	private boolean checkWriteExternalPermission()
	{

		String permission = "android.permission.WRITE_EXTERNAL_STORAGE";
		int res = ComplainDetails.this.checkCallingOrSelfPermission(permission);
		return (res == PackageManager.PERMISSION_GRANTED);
	}*/

	private boolean checkPermission(){
		int result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
		if (result == PackageManager.PERMISSION_GRANTED){

			return true;

		} else {

			return false;

		}
	}
	private void requestPermission(){

		if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)){

			Toast.makeText(this,"GPS permission allows us to access location data.",Toast.LENGTH_LONG).show();
			ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},PERMISSION_REQUEST_CODE);

		} else {

			ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},PERMISSION_REQUEST_CODE);
		}
	}

/*	@Override
	public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
		switch (requestCode) {
			case PERMISSION_REQUEST_CODE:
				if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

					//Snackbar.make(view,"Permission Granted, Now you can access location data.",Snackbar.LENGTH_LONG).show();

				} else {

					///Snackbar.make(view,"Permission Denied, You cannot access location data.",Snackbar.LENGTH_LONG).show();

				}
				break;
		}
	}*/

}
