package com.erp.sheelafoam.fragment;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.erp.sheelafoam.HomeScreen;
import com.erp.sheelafoam.Picasso;
import com.erp.sheelafoam.R;
import com.erp.sheelafoam.UserLogAPI;
import com.erp.sheelafoam.constant.Constant;
import com.erp.sheelafoam.model.ProfilePicModel;
import com.erp.sheelafoam.utils.AlertDialogManager;
import com.erp.sheelafoam.utils.ConnectionDetector;
import com.erp.sheelafoam.utils.MultipartUtility;
import com.erp.sheelafoam.utils.Util;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by priyanka.sharma on 12/12/2016.
 */

public class UpdateProfileFragment extends Fragment implements View.OnClickListener {
    ConnectionDetector cDetector;
    AlertDialogManager alert;
    CircularImageView profile_pic;
    CoordinatorLayout update_profile_crl;
    ImageView image_edit;
    TextView txt_change_password;
    static final Integer WRITE_EXST = 0x3;
    static final Integer READ_EXST = 0x4;
    Button btn_submit;
    EditText ed_name, ed_user_name, ed_email_id, ed_role_name, ed_mobile;
    String str_Name, str_username, strEmail, str_rolename, str_mobile, Profile_Image, op_grtplus_USerID, UserID, UserToken,
            UserAuthType;
    private final int REQUEST_CAMERA = 200, SELECT_FILE = 2;
    String selectedImagePath;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.update_profile_activity
                , container, false);

        askForPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, WRITE_EXST);
        askForPermission(Manifest.permission.READ_EXTERNAL_STORAGE, READ_EXST);
        askForPermission(Manifest.permission.CAMERA,REQUEST_CAMERA);

        new UserLogAPI("Update Profile Page", getActivity());

        init(view);

        return view;
    }

    private void init(View view) {
        cDetector = new ConnectionDetector(getActivity());
        alert = new AlertDialogManager();
        getSharedPreferenceValues();

        ((HomeScreen) getActivity()).txt_title.setText("Profile");
        ((HomeScreen) getActivity()).profile_image.setVisibility(View.GONE);
        ((HomeScreen) getActivity()).txt_title.setVisibility(View.VISIBLE);

        profile_pic = (CircularImageView) view.findViewById(R.id.profile_pic);
        image_edit = (ImageView) view.findViewById(R.id.image_edit);
        txt_change_password = (TextView) view.findViewById(R.id.txt_change_password);
        btn_submit = (Button) view.findViewById(R.id.btn_submit);
        ed_email_id = (EditText) view.findViewById(R.id.ed_email_id);
        ed_mobile = (EditText) view.findViewById(R.id.ed_mobile);
        ed_name = (EditText) view.findViewById(R.id.ed_name);
        ed_role_name = (EditText) view.findViewById(R.id.ed_role_name);
        ed_user_name = (EditText) view.findViewById(R.id.ed_user_name);
        update_profile_crl = (CoordinatorLayout) view.findViewById(R.id.update_profile_crl);

        if (UserAuthType.equals("OTP")) {
            txt_change_password.setVisibility(View.GONE);
        } else {
            txt_change_password.setVisibility(View.VISIBLE);
        }

        ed_user_name.setEnabled(false);
        ed_email_id.setEnabled(false);
        ed_mobile.setEnabled(false);
        ed_name.setEnabled(false);
        ed_role_name.setEnabled(false);

        btn_submit.setOnClickListener(this);
        image_edit.setOnClickListener(this);
        txt_change_password.setOnClickListener(this);

        Picasso.with(getActivity()).load(Profile_Image).into(profile_pic);
        ed_user_name.setText(str_username);
        ed_role_name.setText(str_rolename);
        ed_mobile.setText(str_mobile);
        ed_name.setText(str_Name);
        ed_email_id.setText(strEmail);
    }

    private void getSharedPreferenceValues() {
        Profile_Image = Util.getSharedPrefrenceValue(getActivity(), Constant.Sp_userProfileImage);
        str_rolename = Util.getSharedPrefrenceValue(getActivity(), Constant.Sp_op_user_role_name);
        str_mobile = Util.getSharedPrefrenceValue(getActivity(), Constant.Sp_User_Mobile);
        str_Name = Util.getSharedPrefrenceValue(getActivity(), Constant.Sp_DisplayName);
        str_username = Util.getSharedPrefrenceValue(getActivity(), Constant.Sp_USerName);
        strEmail = Util.getSharedPrefrenceValue(getActivity(), Constant.Sp_USerEmail);
        UserID = Util.getSharedPrefrenceValue(getActivity(), Constant.Sp_UserID);
        UserToken = Util.getSharedPrefrenceValue(getActivity(), Constant.Sp_UserToken);
        UserAuthType = Util.getSharedPrefrenceValue(getActivity(), Constant.Sp_AuthType);
        op_grtplus_USerID = Util.getSharedPrefrenceValue(getActivity(), Constant.Sp_GrtPlusUserID);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_edit:
                selectImage();
                break;
            case R.id.txt_change_password:
                if (UserAuthType.equals("EMAIL")) {
                    final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                    alertDialog.setMessage("Kindly change your password from your email.");
                    alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    alertDialog.show();
                } else {
                    ((HomeScreen) getActivity()).commonFragmentMethod(new ChangePassword(), null, "change password");
                }
                break;
            case R.id.btn_submit:
                if (!cDetector.isConnectingToInternet())
                    alert.showAlertDialog(getActivity(), "Network Connection", "Enable Your Internet Connection", false);
                else {
                    if (selectedImagePath == null) {
                        Util.showSnackbar(getActivity(), update_profile_crl, "Please select image first!!");//Sp_userProfileImage
                    } else {
                        new UploadImgClass().execute();
                    }
                }
                break;
        }
    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);
                } else if (items[item].equals("Choose from Library")) {
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    photoPickerIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    startActivityForResult(photoPickerIntent, SELECT_FILE);

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        switch (reqCode) {
            case (REQUEST_CAMERA):
                try {
                    Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                    File destination = new File(Environment.getExternalStorageDirectory(),
                            System.currentTimeMillis() + ".jpg");

                    selectedImagePath = destination.getAbsolutePath();

                    FileOutputStream fo;
                    try {
                        destination.createNewFile();
                        fo = new FileOutputStream(destination);
                        fo.write(bytes.toByteArray());
                        fo.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    profile_pic.setImageBitmap(thumbnail);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
                break;

            case (SELECT_FILE):
                if(data != null) {
                    Uri selectedImage = data.getData();
                    String tempPath = getRealPathFromURI(selectedImage);
                    File file2 = new File(tempPath);
                    long length2 = file2.length() / 1024;
                    Log.d("File Size", length2 + " kb");
                    selectedImagePath = tempPath;
                    File file = new File(selectedImagePath);
                    long length = file.length() / 1024;
                    Log.d("File Size", length + " kb");
                    String file_extn = selectedImagePath.substring(selectedImagePath.lastIndexOf(".") + 1);
                    try {
                        Bitmap bitmapImage = BitmapFactory.decodeFile(selectedImagePath);
                        int nh = (int) (bitmapImage.getHeight() * (512.0 / bitmapImage.getWidth()));
                        Bitmap bitmap = Bitmap.createScaledBitmap(bitmapImage, 512, nh, true);
                        profile_pic.setImageBitmap(bitmap);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    private String getRealPathFromURI(Uri contentURI) {
        Cursor cursor = getActivity().getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            String path = cursor.getString(idx);
            cursor.close();
            return path;
        }
    }

    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(getActivity(), permission) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), permission)) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{permission}, requestCode);
            } else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{permission}, requestCode);
            }
        } else {
            //  Toast.makeText(this, "" + permission + " is already granted.", Toast.LENGTH_SHORT).show();
        }
    }


    class UploadImgClass extends AsyncTask<String, Void, String> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(getActivity());
            dialog.setMessage("Please wait!!");
            //  dialog.setTitle(getString(R.string.progress_title));
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... urls) {
            String charset = "UTF-8";
            String uploadResponse = "";
            String requestURL = Constant.WS_URL + Constant.WS_UPDATE_PROFILE;
            try {
                MultipartUtility multipart = new MultipartUtility(requestURL, charset);
                if (selectedImagePath != null && !selectedImagePath.isEmpty()) {
                    multipart.addFilePart("user_image", new File(selectedImagePath));
                } else {
                    multipart.addFormField("user_image", "");
                }
                multipart.addFormField("mode", "upload_image");
                multipart.addFormField("op_greatplus_user_id", op_grtplus_USerID);
                multipart.addFormField("op_user_role_name", str_rolename);
                multipart.addFormField("auth_type", UserAuthType);
                multipart.addFormField("uid", UserID);
                multipart.addFormField("token", UserToken);
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
                Gson gson = new Gson();
                ProfilePicModel base = gson.fromJson(result, ProfilePicModel.class);

                if (base.getInfo().get(0).getStatus() == 1) {
                    Util.showSnackbar(getActivity(), update_profile_crl, base.getInfo().get(0).getMsg());//Sp_userProfileImage

                    Util.setSharedPrefrenceValue(getActivity(), Constant.PREFS_NAME, Constant.Sp_userProfileImage, base.getInfo().get(0).getOp_user_profile_image().replaceAll(" ", "%20"));

                } else {
                    Util.showSnackbar(getActivity(), update_profile_crl, base.getInfo().get(0).getMsg());
                }
            }

        }
    }

}
