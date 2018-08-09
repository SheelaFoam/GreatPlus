package com.erp.sheelafoam.utils;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.erp.sheelafoam.R;

import org.acra.ACRA;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;

@ReportsCrashes(formKey = "", mailTo = "appspportsleepwell@gmail.com", mode = ReportingInteractionMode.TOAST, resToastText = R.string.crash_report)
public class ACRAActivity extends MultiDexApplication {
    /**
     * This is the application class of PsplClaim used to initialize variables
     * used throughout the app
     *
     * @author PSPL
     */
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
    @Override
    public void onCreate() {
        super.onCreate();
        ACRA.init(this);
        //MultiDex.install(this);

    }

}
