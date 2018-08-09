package com.erp.sheelafoam;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;

/**
 * Created by priyanka.sharma on 11/29/2016.
 */

public class HeaderMenuDesc_Web extends Activity {
    WebView web_view;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        init();
    }

    private void init() {
        Intent in = getIntent();
        url = in.getStringExtra("WebUrl");
        Log.e("WebView URL=>", url);
        web_view = (WebView) findViewById(R.id.web_view);

        if (url.contains(".pdf")) {
            web_view.getSettings().setJavaScriptEnabled(true);
            web_view.loadUrl("http://drive.google.com/viewerng/viewer?embedded=true&url=" + url);
        } else {
            web_view.loadUrl(url);
        }

     /*   web_view = (WebView) findViewById(R.id.web_view);
        WebSettings wbset=web_view.getSettings();
        wbset.setJavaScriptEnabled(true);
        web_view.setWebViewClient(new myWebClient());

        web_view.loadUrl(url);*/
    }

  /*  public class myWebClient extends WebViewClient {
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
    }
*/
}
