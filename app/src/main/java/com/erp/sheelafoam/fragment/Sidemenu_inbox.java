package com.erp.sheelafoam.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.erp.sheelafoam.HomeScreen;
import com.erp.sheelafoam.R;
import com.erp.sheelafoam.UserLogAPI;
import com.erp.sheelafoam.constant.Constant;
import com.erp.sheelafoam.utils.Util;

/**
 * Created by priyanka.sharma on 12/2/2016.
 */

public class Sidemenu_inbox extends Fragment {
    WebView web_view;
    private String UsrID, Token;
    String url;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_webview
                , container, false);
        new UserLogAPI("Inbox Page", getActivity());
        init(view);

        return view;
    }

    private void getSharedPreferenceValues() {
        UsrID = Util.getSharedPrefrenceValue(getActivity(), Constant.Sp_UserID);
        Token = Util.getSharedPrefrenceValue(getActivity(), Constant.Sp_UserToken);

        ((HomeScreen) getActivity()).txt_title.setText("Inbox");
        ((HomeScreen) getActivity()).profile_image.setVisibility(View.GONE);
        ((HomeScreen) getActivity()).txt_title.setVisibility(View.VISIBLE);
    }

    public class myWebClient extends WebViewClient {
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

    private void init(View v) {
        web_view = (WebView) v.findViewById(R.id.web_view);

        WebSettings wbset=web_view.getSettings();
        wbset.setJavaScriptEnabled(true);
        web_view.setWebViewClient(new myWebClient());

        getSharedPreferenceValues();
        url = Constant.WS_URL + "inbox.login.php?uid=" + UsrID + "&token=" + Token;
        Log.e("Inbox URL", "" + url);
        web_view.loadUrl(url);


    }
}
