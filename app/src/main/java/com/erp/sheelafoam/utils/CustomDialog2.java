package com.erp.sheelafoam.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;

public class CustomDialog2 extends Dialog {
    public CustomDialog2(Context context, int layoutId) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(layoutId);
    }

}
