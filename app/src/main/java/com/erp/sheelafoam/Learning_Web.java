package com.erp.sheelafoam;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;

/**
 * Created by priyanka.sharma on 11/29/2016.
 */

public class Learning_Web extends Activity {
    WebView web_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        init();
    }

    private void init() {
        Intent in = getIntent();
        String WEbURL = in.getStringExtra("WebUrl");

        web_view = (WebView) findViewById(R.id.web_view);
        web_view.loadUrl(WEbURL);

    }
}
