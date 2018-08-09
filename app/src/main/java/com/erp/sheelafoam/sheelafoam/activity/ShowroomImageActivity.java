package com.erp.sheelafoam.sheelafoam.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.erp.sheelafoam.HomeScreen;
import com.erp.sheelafoam.R;
import com.erp.sheelafoam.UploadActivity;
import com.erp.sheelafoam.adapter.RadioFootFallAdapter;
import com.erp.sheelafoam.adapter.ShowRoomImageAdapter;
import com.erp.sheelafoam.adapter.ShowRoomListViewAdapter;
import com.erp.sheelafoam.constant.Constant;
import com.erp.sheelafoam.models.ShoowRoomModel;
import com.erp.sheelafoam.models.ShowRoomModelOne;
import com.erp.sheelafoam.prefrences.SheelaSharedPreference;
import com.erp.sheelafoam.utils.CommonClass;
import com.erp.sheelafoam.utils.CustomDialog;
import com.erp.sheelafoam.utils.CustomTypefaceTextView;
import com.erp.sheelafoam.utils.Util;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ShowroomImageActivity extends Activity implements View.OnClickListener {
    private static final int REQUEST_TAKE_PHOTO = 100;
    Context context;
    CustomDialog dialog;
    ArrayList<String> array = new ArrayList<>();
    Boolean isCamera = true;
    CommonClass commonClass;
    ShoowRoomModel model;
    ShowRoomModelOne modelOne;
    ArrayList<ShowRoomModelOne> list;
    private TextView tv_uploadImage_new, tv_logoname;
    private CustomTypefaceTextView tv_selectShowRoomEMPSpinner, tv_selectShowroomStoreSpinner;
    private ImageButton ib_backicon_new, iv_camera_new;
    private SheelaSharedPreference preference;
    private String mCurrentPhotoPath;
    private Button btn_getShowRoomImg;
    private ProgressDialog progress;
    private GridView gridview_feedbackList;
    private ShowRoomImageAdapter adapter;
    private ShowRoomListViewAdapter listViewAdapter;
    private ListView lv_dateList;
    private PermissionListener permissionListener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            if (isCamera) {
                openCamera();
            }

        }

        @Override
        public void onPermissionDenied(ArrayList<String> arrayList) {

        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.showroom_image_activity);
        commonClass = new CommonClass(this);
        preference = new SheelaSharedPreference(this);
        progress = new ProgressDialog(this);
        ib_backicon_new = (ImageButton) findViewById(R.id.ib_backicon_new);
        tv_logoname = (TextView) findViewById(R.id.tv_logoname_new);
        tv_logoname.setText("ShowRoom Image");
        iv_camera_new = (ImageButton) findViewById(R.id.iv_camera_new);
        iv_camera_new.setVisibility(View.VISIBLE);
        tv_selectShowRoomEMPSpinner = (CustomTypefaceTextView) findViewById(R.id.tv_selectShowRoomEMPSpinner);
        tv_selectShowroomStoreSpinner = (CustomTypefaceTextView) findViewById(R.id.tv_selectShowroomStoreSpinner);
        btn_getShowRoomImg = (Button) findViewById(R.id.btn_getShowRoomImg);
        //  gridview_feedbackList = (GridView) findViewById(R.id.gridview_feedbackList);
        lv_dateList = (ListView) findViewById(R.id.lv_dateList);
        tv_selectShowRoomEMPSpinner.setOnClickListener(this);
        tv_selectShowroomStoreSpinner.setOnClickListener(this);
        btn_getShowRoomImg.setOnClickListener(this);
        ib_backicon_new.setOnClickListener(this);
        iv_camera_new.setOnClickListener(this);
        try {
            progress = new ProgressDialog(this);
            progress.setMessage("Loading...");
            progress.setCancelable(false);
            progress.show();
            callGetService();
        } catch (Exception e) {

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_selectShowRoomEMPSpinner:
                openRadioDialogForEMP(this);
                break;
            case R.id.tv_selectShowroomStoreSpinner:
                openRadioDialogForStore(this);
                break;
            case R.id.iv_camera_new:
                getImage(this);
                break;
            case R.id.ib_backicon_new:
                Intent goBack = new Intent(ShowroomImageActivity.this, HomeScreen.class);
                startActivity(goBack);
                break;
            case R.id.btn_getShowRoomImg:
                try {
                    progress = new ProgressDialog(ShowroomImageActivity.this);
                    progress.setMessage("Please wait....");
                    progress.setCancelable(false);
                    progress.show();
                    callGetImgService();
                } catch (Exception e) {

                }
                break;
        }
    }

    private void getImage(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] _permission = {android.Manifest.permission.CAMERA,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
            if (TedPermission.isGranted(this, _permission)) {
                if (isCamera) {
                    openCamera();
                }

            } else {
                TedPermission.with((this))
                        .setPermissionListener(permissionListener)
                        .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                        .setPermissions(
                                android.Manifest.permission.CAMERA,
                                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                                android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .check();
            }
        } else if (isCamera) {
            openCamera();
        }
    }

    private void openCamera() {
        try {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException ex) {
                    return;
                }
                if (photoFile != null) {
                    Uri photoURI = Uri.fromFile(createImageFile());
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);

                }

            }
        } catch (Exception e) {

        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp;
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), "Camera");
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            Uri imageUri = Uri.parse(mCurrentPhotoPath);
            //  CommonClass.goToNextScreen(this, UploadActivity.class, imageUri.getPath());
            commonClass.goToNextScreen(ShowroomImageActivity.this, UploadActivity.class, imageUri.getPath());
            MediaScannerConnection.scanFile(this,
                    new String[]{imageUri.getPath()}, null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String path, Uri uri) {
                        }
                    });
        }

    }

    private void openRadioDialogForStore(Context context) {
        array.clear();
        array.add(preference.getUserNameFootFall());
        dialog = new CustomDialog(context, R.layout.dialog_footfall);
        dialog.setCancelable(true);
        CustomTypefaceTextView tv_subTile = (CustomTypefaceTextView) dialog.findViewById(R.id.tv_subTile_footfall);
        tv_subTile.setText("Select Store*");
        ListView lv = (ListView) dialog.findViewById(R.id.recycler_footfall);
        TextView btn_footfall_cancel = (TextView) dialog.findViewById(R.id.btn_footfall_cancel);
        TextView btn_footfall_ok = (TextView) dialog.findViewById(R.id.btn_footfall_ok);
        btn_footfall_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_selectShowroomStoreSpinner.setText(preference.getUserNameFootFall());
                dialog.dismiss();
            }
        });
        dialog.show();
        lv.setAdapter(new RadioFootFallAdapter(this, array));
        btn_footfall_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    private void openRadioDialogForEMP(Context context) {
        array.clear();
        array.add(preference.getEMPName());
        dialog = new CustomDialog(context, R.layout.dialog_footfall);
        dialog.setCancelable(true);
        CustomTypefaceTextView tv_subTile = (CustomTypefaceTextView) dialog.findViewById(R.id.tv_subTile_footfall);
        tv_subTile.setText("Select Employee *");
        ListView lv = (ListView) dialog.findViewById(R.id.recycler_footfall);
        TextView btn_footfall_cancel = (TextView) dialog.findViewById(R.id.btn_footfall_cancel);
        TextView btn_footfall_ok = (TextView) dialog.findViewById(R.id.btn_footfall_ok);
        btn_footfall_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_selectShowRoomEMPSpinner.setText(preference.getEMPName());
                dialog.dismiss();
            }
        });
        dialog.show();
        lv.setAdapter(new RadioFootFallAdapter(this, array));
        btn_footfall_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    private void callGetService() {
        String UserID = Util.getSharedPrefrenceValue(ShowroomImageActivity.this, Constant.Sp_UserID);
        String UserToken = Util.getSharedPrefrenceValue(ShowroomImageActivity.this, Constant.Sp_UserToken);
        System.out.println("NEWTOKEN" + UserToken + "," + UserID);
        String URL = " http://be.greatplus.com/sheelafoam/rest/employees/details/"+UserID+"/"+UserToken;
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, URL, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                System.out.println("getresponse" + jsonObject);
                progress.dismiss();
                try {
                    if (jsonObject.getBoolean("success")) {
                        jsonObject.getString("message");
                        Log.d("getresponse", jsonObject.getString("message").toString());
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = (JSONObject) jsonArray.get(i);
                            object.getString("eMP_NAME");
                            Log.d("getresponse", object.getString("eMP_NAME").toString());
                            preference.setEMPName(object.getString("eMP_NAME"));
                            tv_selectShowRoomEMPSpinner.setText(object.getString("eMP_NAME"));
                            JSONArray array = object.getJSONArray("storeList");
                            for (int j = 0; j < array.length(); j++) {
                                JSONObject _object = (JSONObject) array.get(j);
                                _object.getString("pARENT_CHANNEL_PARTNER_NAME");
                                preference.setUserNameFootFall(_object.getString("pARENT_CHANNEL_PARTNER_NAME"));
                                tv_selectShowroomStoreSpinner.setText(_object.getString("pARENT_CHANNEL_PARTNER_NAME"));
                                Log.d("getresponse", _object.getString("pARENT_CHANNEL_PARTNER_NAME").toString());
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(ShowroomImageActivity.this);
        requestQueue.add(objectRequest);
    }

    private void callGetImgService() {
        String storeName = preference.getUserNameFootFall();
        String newStoreName=storeName.replaceAll(" ", "%20");
        String UserID = Util.getSharedPrefrenceValue(ShowroomImageActivity.this, Constant.Sp_UserID);
        String UserToken = Util.getSharedPrefrenceValue(ShowroomImageActivity.this, Constant.Sp_UserToken);
        System.out.println("NEWTOKEN" + UserToken + "," + UserID + "," + newStoreName);
        String URL_IMG = "http://be.greatplus.com/sheelafoam/rest/services/getStoreImage/"+UserID+"/"+UserToken+"/"+newStoreName;
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, URL_IMG, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                System.out.println("imageResponse" + jsonObject);
                progress.dismiss();
                try {
                    if (jsonObject.getBoolean("success")) {
                        jsonObject.getString("message");
                        list = new ArrayList<>();
                        //anshya
                        List<ShowRoomModelOne> outerList = new ArrayList<>();
                        Log.d("massage", jsonObject.getString("message").toString());
                        JSONArray jsonArray = jsonObject.getJSONArray("data");//----All data-----
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = (JSONObject) jsonArray.get(i);
                            //anshya
                            ShowRoomModelOne outerModel = new ShowRoomModelOne();
                            outerModel.setDate(object.getString("date"));
                            List<ShoowRoomModel> innerList = new ArrayList<>();
                            object.getString("date");//---------date----------
                            Log.d("date", object.getString("date").toString());
                            JSONArray array = object.getJSONArray("imageList");//-----ImageList---------
                            //model = new ShoowRoomModel();
                            for (int j = 0; j < array.length(); j++) {
                                JSONObject _object = (JSONObject) array.get(j);
                                ShoowRoomModel innerModel = new ShoowRoomModel();
                                innerModel.setImageDate(_object.getString("imageDate"));
                                innerModel.setImageUrl(_object.getString("imageUrl"));
                                innerModel.setEmployeeId(_object.getString("employeeId"));
                                innerModel.setStoreId(_object.getString("storeId"));
                                innerModel.setId(_object.getInt("id"));
                                innerList.add(innerModel);
                                //model.setImageDate(_object.getString("imageDate"));
                                //  list.add(model);
                                _object.getString("imageUrl");
                                Log.d("ImageURL", _object.getString("imageUrl").toString());

                            }
                            outerModel.setImageList(innerList);
                            outerList.add(outerModel);
                        }
                       /* adapter = new ShowRoomImageAdapter(ShowroomImageActivity.this, list);
                        gridview_feedbackList.setAdapter(adapter);*/

                        listViewAdapter = new ShowRoomListViewAdapter(ShowroomImageActivity.this, outerList);
                        lv_dateList.setAdapter(listViewAdapter);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progress.dismiss();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(ShowroomImageActivity.this);
        requestQueue.add(objectRequest);
    }

}
