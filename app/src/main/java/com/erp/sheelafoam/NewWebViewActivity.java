package com.erp.sheelafoam;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.erp.sheelafoam.utils.CommonClass;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.erp.sheelafoam.R.id.coordinatorLayout;

/**
 * Created by dell on 15-Sep-17.
 */

public class NewWebViewActivity extends AppCompatActivity {
    private static final int REQUEST_TAKE_PHOTO = 100;
    ProgressBar progressBar;
    RelativeLayout mWebViewLoadingScreen, mWebViewErrorScreen, mWebViewContentScreen;
    Toolbar toolbar;
    CommonClass commonClass;
    Boolean isCamera = false;
    private WebView mWebView;
    private String sUrl, sTitle;
    private Boolean cameraIcon = false;
    private ProgressDialog dialog;
    private TextView tvCamera;
    private String mCurrentPhotoPath;
    private CoordinatorLayout coordinatorLayout;
    private ImageButton ib_backicon_new;
    private TextView tv_logoname, tv_emptyText;
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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview_new);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        commonClass = new CommonClass(this);
        ib_backicon_new = (ImageButton) findViewById(R.id.ib_backicon_new);
        tv_logoname = (TextView) findViewById(R.id.tv_logoname_new);
        tv_logoname.setText("Great Plus");
        getImage();
        Intent intent = getIntent();
        sUrl = intent.getExtras().getString("WebUrl");
        sTitle = intent.getExtras().getString("title");
        cameraIcon = intent.getExtras().getBoolean("camera");
        Log.d("webUrl", sUrl);
        if (cameraIcon) {
            isCamera = true;
            tvCamera.setVisibility(View.VISIBLE);
            tvCamera.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getImage();
                }
            });
        }
        if (!commonClass.checkInternetConnection(NewWebViewActivity.this)) {
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "No internet connection!", Snackbar.LENGTH_LONG)
                    .setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                        }
                    });
            // Changing message text color
            snackbar.setActionTextColor(Color.RED);
            // Changing action button text color
            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.YELLOW);
            snackbar.show();
        } else {
            dialog = new ProgressDialog(this);
            dialog.setMessage("Loading...");
            dialog.setCancelable(false);
            dialog.show();
            mWebView = (WebView) findViewById(R.id.webView);
            mWebView.getSettings().setBuiltInZoomControls(true);
            mWebView.getSettings().setJavaScriptEnabled(true);
            mWebView.getSettings().setGeolocationEnabled(true);
            mWebView.getSettings().setAppCacheEnabled(true);
            mWebView.setWebViewClient(new WebViewClient() {
                //If you will not use this method url links are opeen in new brower not in webview
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.setWebChromeClient(new WebChromeClient() {
                        @Override
                        public void onProgressChanged(WebView view, int newProgress) {
                        }/*{
                        progressBar.setVisibility(View.VISIBLE);
                        progressTvl.setVisibility(View.VISIBLE);
                        progressBar.setProgress(newProgress);
                        progressTvl.setText("");
                    }*/
                    });
                    view.loadUrl(url);
                    dialog.dismiss();
                    return true;
                }

                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    //   showLoadingScreen();
                }

                public void onPageFinished(WebView view, String url) {
                    dialog.dismiss();
                    //  showContentScreen();
                /*progressBar.setVisibility(View.GONE);
                progressTvl.setVisibility(View.GONE);*/
                }

                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {

                    //showErrorScreen();
                    Toast.makeText(getApplicationContext(), description, Toast.LENGTH_SHORT).show();
                }

            });

            mWebView.getSettings().setJavaScriptEnabled(true);
            mWebView.setDownloadListener(new DownloadListener() {
                @Override
                public void onDownloadStart(String s, String s1, String s2, String s3, long l) {
                    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(s));

                    request.allowScanningByMediaScanner();

                    request.setNotificationVisibility(
                            DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

                    //String fileName = URLUtil.guessFileName(sUrl,"application/csv","csv");
                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "mts_report.csv");

                    DownloadManager dManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                    dManager.enqueue(request);

//                Intent i = new Intent(Intent.ACTION_VIEW);
//                i.setData(Uri.parse(s));
//                startActivity(i);


                }
            });
            if (sUrl.contains(".pdf")) {
                mWebView.loadUrl("http://drive.google.com/viewerng/viewer?embedded=true&url=" + sUrl);
            } else {
                mWebView.loadUrl(sUrl);
            }
        }
        ib_backicon_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* if (mWebView.copyBackForwardList().getCurrentIndex() > 0) {
                    mWebView.goBack();
                } else {*/
                Intent goBack = new Intent(NewWebViewActivity.this, HomeScreen.class);
                startActivity(goBack);
                // }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (mWebView.copyBackForwardList().getCurrentIndex() > 0) {
            mWebView.goBack();
        } else {
            super.onBackPressed(); // finishes activity
        }
    }

    public void showLoadingScreen() {

        mWebViewErrorScreen.setVisibility(View.GONE);
        mWebViewContentScreen.setVisibility(View.GONE);
        mWebViewLoadingScreen.setVisibility(View.VISIBLE);
    }

    public void showErrorScreen() {

        mWebViewLoadingScreen.setVisibility(View.GONE);
        mWebViewContentScreen.setVisibility(View.GONE);
        mWebViewErrorScreen.setVisibility(View.VISIBLE);
    }

    public void showContentScreen() {

        mWebViewErrorScreen.setVisibility(View.GONE);
        mWebViewLoadingScreen.setVisibility(View.GONE);
        mWebViewContentScreen.setVisibility(View.VISIBLE);
    }

    private void getImage() {
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
            CommonClass.goToNextScreen(this, UploadActivity.class, imageUri.getPath());
            MediaScannerConnection.scanFile(this,
                    new String[]{imageUri.getPath()}, null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String path, Uri uri) {
                        }
                    });
        }

    }

}
