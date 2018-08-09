package com.erp.sheelafoam.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.erp.sheelafoam.HomeScreen;
import com.erp.sheelafoam.R;
import com.erp.sheelafoam.UserLogAPI;
import com.erp.sheelafoam.constant.Constant;
import com.erp.sheelafoam.utils.Util;

/**
 * Created by priyanka.sharma on 12/2/2016.
 */

public class Sidemenu_DocumentBox extends Fragment {
    WebView web_view;
    private String UsrID, Token;
    String url;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_webview
                , container, false);
        new UserLogAPI("Document Page", getActivity());
        init(view);

        return view;
    }

    private void getSharedPreferenceValues() {
        UsrID = Util.getSharedPrefrenceValue(getActivity(), Constant.Sp_UserID);
        Token = Util.getSharedPrefrenceValue(getActivity(), Constant.Sp_UserToken);
        ((HomeScreen) getActivity()).txt_title.setText("Document box");
        ((HomeScreen) getActivity()).profile_image.setVisibility(View.GONE);
        ((HomeScreen) getActivity()).txt_title.setVisibility(View.VISIBLE);

    }


    private void init(View v) {
        web_view = (WebView) v.findViewById(R.id.web_view);
        getSharedPreferenceValues();
        url = Constant.WS_URL + "document-box-login.php?uid=" + UsrID + "&token=" + Token;
        web_view.loadUrl(url);

    }
}