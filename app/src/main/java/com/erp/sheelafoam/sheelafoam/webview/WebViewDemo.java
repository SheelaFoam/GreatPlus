package com.erp.sheelafoam.sheelafoam.webview;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.erp.sheelafoam.R;
import com.erp.sheelafoam.sheelafoam.complaint.ComplainDetails;
import com.erp.sheelafoam.sheelafoam.utility.GlobalVariables;

/**
 * Demonstrates how to embed a WebView in your activity. Also demonstrates how
 * to have javascript in the WebView call into the activity, and how the activity
 * can invoke javascript.
 * <p>
 * In this example, clicking on the android in the WebView will result in a call into
 * the activities code in {@link DemoJavaScriptInterface#clickOnAndroid(String)}. This code
 * will turn around and invoke javascript using the {@link android.webkit.WebView#loadUrl(String)}
 * method.
 * <p>
 * Obviously all of this could have been accomplished without calling into the activity
 * and then back into javascript, but this code is intended to show how to set up the
 * code paths for this sort of communication.
 *
 */
public class WebViewDemo extends Activity implements OnClickListener {

    private static final String LOG_TAG = "WebViewDemo";
    private SharedPreferences mPrefs;

    private WebView mWebView;
    private String webPageText= null;
    private Handler mHandler = new Handler();
    private String mobileNum = null;
    private String user_role = "";

    //UI Objects
    
    ProgressBar progressBar;
    
    TextView textview_back,textview_user_id;
    
    ImageButton btn_logout;
    
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_web_view_demo);
        mPrefs = getSharedPreferences("Location", Context.MODE_PRIVATE);
        user_role=mPrefs.getString("op_user_type", "").toLowerCase();
        initialize();
        setOnclickListner();
        mWebView = (WebView) findViewById(R.id.webview);
        WebSettings webSettings = mWebView.getSettings();
       
        webSettings.setSaveFormData(false);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        mWebView.setWebChromeClient(new MyWebChromeClient());
        mWebView.setWebViewClient(new myWebClient());
        mWebView.addJavascriptInterface(new DemoJavaScriptInterface(), "demo");
        //mWebView.getSettings().setLoadWithOverviewMode(true);
       // mobileNum = getIntent().getStringExtra("op_user_name");
       mobileNum= mPrefs.getString("op_user_mobile", "");
       Log.e("mobileNum", mobileNum);
       // mobileNum="SG0764";
       
        String url="http://125.19.46.252/ws/ComplaintList.php?MOBILE="+mobileNum;
        Log.e("URL", url);
        
        //mobileNum ="9428007792";
        //mWebView.setInitialScale(110);
        //mWebView.getSettings().setUseWideViewPort(true);
       // mWebView.loadUrl("http://125.19.46.252/ws/tabletest.php?MOBILE="+mobileNum);
        //mWebView.loadUrl("http://125.19.46.252/ws/tabletest.php?MOBILE=9417251047");
        //mWebView.loadUrl("http://125.19.46.252/ws/tabletest.php?MOBILE=9428007792");
        //mWebView.loadUrl("file:///android_asset/tabletest.html");
       // new LoadWebPageASYNC().execute();
       // loadWebView();
        
       // mWebView.loadUrl("http://125.19.46.252/ws/ComplaintList.php?MOBILE="+mobileNum);
        mWebView.loadUrl("http://125.19.46.252/ws/ComplaintListWithHeaders.php?MOBILE="+mobileNum);
        

    }


    private void loadWebView()
    {
        try {
            while (webPageText == null) {
                Thread.sleep(3000);
            }
        }
        catch(Exception ex)
        {

        }

        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadData(webPageText,"text/html","utf-8");
    }
    final class DemoJavaScriptInterface {

        DemoJavaScriptInterface() {
        }

        /**
         * This is not called on the UI thread. Post a runnable to invoke
         * loadUrl on the UI thread.
         */
        @JavascriptInterface
        public void clickOnAndroid(String str) {
            final String complainId = str;
            mHandler.post(new Runnable() {
                public void run() {
                    Intent in = new Intent(getApplicationContext(),ComplainDetails.class);
                    in.putExtra("complainid",complainId);
                    in.putExtra("mobile",mobileNum);
                    
                    startActivity(in);
                    //mWebView.loadUrl("javascript:wave()");
                }
            });

        }
    }

    /**
     * Provides a hook for calling "alert" from javascript. Useful for
     * debugging your javascript.
     */
    final class MyWebChromeClient extends WebChromeClient {
        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            Log.d(LOG_TAG, message);
            result.confirm();
            return true;
        }
        
        
    }
    
    //http://www.technotalkative.com/android-load-webview-with-progressbar/
    
    public class myWebClient extends WebViewClient
    {
    	@Override
    	public void onPageStarted(WebView view, String url, Bitmap favicon) {
    		// TODO Auto-generated method stub
    		super.onPageStarted(view, url, favicon);
    	}
    	
    	@Override
    	public boolean shouldOverrideUrlLoading(WebView view, String url) {
    		// TODO Auto-generated method stub
    		
    		view.loadUrl(url);
    		return true;
    		
    	}
    	
    	@Override
    	public void onPageFinished(WebView view, String url) {
    		// TODO Auto-generated method stub
    		super.onPageFinished(view, url);
    		
    		progressBar.setVisibility(View.GONE);
    	}
    }

/**
 * Initialize UI object
 * **/
    
    public void initialize()
    {
    	textview_back=(TextView)findViewById(R.id.textview_back);
    	textview_user_id=(TextView)findViewById(R.id.textview_user_id);
    	 progressBar = (ProgressBar) findViewById(R.id.progressBar);
        
         btn_logout=(ImageButton)findViewById(R.id.btn_logout);
         
         textview_user_id.setText(mobileNum);
         
         textview_back.setText("greatplus"+" "+user_role+" "+"app");
 		
 		
 		SpannableString spannable_string=new SpannableString(textview_back.getText().toString().trim());
 		spannable_string.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.color_great)), 0, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
 		spannable_string.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.color_plus)), 5, 10, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
 		
 		textview_back.setText(spannable_string);
    	
    }
    
    public void setOnclickListner()
    {
    	btn_logout.setOnClickListener(this);
    }

// callback method for track 
@Override
public void onClick(View v) {
	// TODO Auto-generated method stub
	
	switch(v.getId())
	{
	   case R.id.btn_logout:
		   
		   GlobalVariables.logout(this);
		   
		   break;
		   
		   default:
			   
			   break;
	}
	
}



@Override
public void onBackPressed() {
	// TODO Auto-generated method stub
	//super.onBackPressed();
	if(user_role.equals("EMPLOYEE"))
	{
		finish();
	}
	else
	{
	showDialog();
	}
}


/**
 * Method: This medoth use to create alert dialog for exit from app when user press back button.
 * **/

public void showDialog() {
	
	AlertDialog.Builder builder = new AlertDialog.Builder(
			WebViewDemo.this);

	builder.setTitle("SheelaFoam");

	builder.setMessage("Are you sure want to exit from app");
	builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

		@Override
		public void onClick(DialogInterface dialog, int which)
		{
			// TODO Auto-generated method stub

			finish();
			dialog.dismiss();
		}
	});
	
	builder.setNegativeButton("Cancel",
			new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub

					dialog.dismiss();
				}
			});
	builder.show();
}

}