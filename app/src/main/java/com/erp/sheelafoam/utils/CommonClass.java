package com.erp.sheelafoam.utils;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Parcelable;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;

/**
 * Created by admin on 19-02-2018.
 */

public class CommonClass {
    private static final String IMAGE_DIRECTORY_NAME = "SheelaCam";

    public void showToast(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static Uri getUri(Context context, int type) {
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME);
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
                        + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile = null;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        }

        if (mediaFile != null) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                return FileProvider.getUriForFile(context,
//                        BuildConfig.APPLICATION_ID + ".provider",
//                        mediaFile);
//            } else {
//                return Uri.fromFile(mediaFile);
//            }
        }
        return null;
    }


    public static void goToNextScreen(Activity activity, Class<?> destination, String url) {
        Intent intent = new Intent(activity, destination);
        intent.putExtra("LoadURL", url);
        activity.startActivity(intent);
    }


    private Context context;
    private ProgressDialog dialog;

    public CommonClass(Context context) {
        this.context = context;
    }

    public void showProgress(String msg, boolean isCancelable) {
        dialog = new ProgressDialog(context);
        dialog.setMessage(msg);
        dialog.setCancelable(isCancelable);
        dialog.show();
    }

    public void disMissProgress() {
        if (dialog != null && dialog.isShowing())
            dialog.dismiss();
    }

    public static boolean checkInternetConnection(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifiNetwork = cm
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiNetwork != null && wifiNetwork.isConnected()) {
            return true;
        }

        NetworkInfo mobileNetwork = cm
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (mobileNetwork != null && mobileNetwork.isConnected()) {
            return true;
        }

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected()) {
            return true;
        }
        return false;
    }

    public static Typeface getFontStyle_ABeeZee_Regular(Context context) {
        String fontPath = "fonts/ABeeZee_Regular.ttf";
        Typeface tf = Typeface.createFromAsset(context.getAssets(), fontPath);

        return tf;
    }
}
