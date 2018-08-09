package com.erp.sheelafoam.prefrences;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.Display;

import com.erp.sheelafoam.models.PendingGCOrderModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;


public class SheelaSharedPreference {
    private static final String DaysDifference = "DaysDifference";
    private static final String LastOrderStatus = "LastOrderStatus";
    private static final String LastProduct = "LastProduct";
    private static final String LastProductOrderRes= "LastProductOrderRes";
    @SuppressWarnings("unused")
    private static final String Key = "Key";
    private static SheelaSharedPreference appSharedPrefrence;
    int modevalueId;
    Context context;
    Gson gson;
    private SharedPreferences sharedPreferences;
    private Editor editor;

    @SuppressLint("CommitPrefEdits")
    public SheelaSharedPreference(Context context) {
        this.context = context;
        this.sharedPreferences = context.getSharedPreferences(
                "challenges_prefs", Context.MODE_PRIVATE);
        gson = new Gson();
        this.editor = sharedPreferences.edit();
    }

    public static SheelaSharedPreference getInstance(Context context) {
        if (appSharedPrefrence == null) {
            appSharedPrefrence = new SheelaSharedPreference(context);
        }
        return appSharedPrefrence;
    }

    public Editor getEditor() {
        return editor;
    }

    public void commitEditor() {
        editor.commit();
    }

    public void clearEditor() {
        editor.clear();
        editor.commit();
    }

    public int setModeValue(int modevalueId) {
        editor.putInt("modevalueId", modevalueId);
        editor.commit();
        return modevalueId;
    }

    public int getModeVlue() {
        return context.getSharedPreferences("challenges_prefs",
                Context.MODE_PRIVATE).getInt("modevalueId", 0);
    }

    public String setDeviceName(String deviceName) {
        editor.putString("deviceNameId", deviceName);
        editor.commit();
        return deviceName;
    }

    public String getDeviceName() {
        return context.getSharedPreferences("challenges_prefs",
                Context.MODE_PRIVATE).getString("deviceNameId", "");
    }

    public float setCarRate(Float carRate) {
        editor.putFloat("CarRateId", carRate);
        editor.commit();
        return carRate;
    }

    public float getCarRate() {
        return context.getSharedPreferences("challenges_prefs",
                Context.MODE_PRIVATE).getFloat("CarRateId", 0);
    }

    public float setBikeRate(Float bikeRate) {
        editor.putFloat("BikeRateId", bikeRate);
        editor.commit();
        return bikeRate;
    }

    public float getBikeRate() {
        return context.getSharedPreferences("challenges_prefs",
                Context.MODE_PRIVATE).getFloat("BikeRateId", 0);
    }

    public int setIntervalTime(int intervalTime) {
        editor.putInt("IntervalTimeId", intervalTime);
        editor.commit();
        return intervalTime;
    }

    public int getIntervalTime() {
        return context.getSharedPreferences("challenges_prefs",
                Context.MODE_PRIVATE).getInt("IntervalTimeId", 0);
    }

    public String setDefaulMode(String defaulMode) {
        editor.putString("DefaulModeId", defaulMode);
        editor.commit();
        return defaulMode;
    }

    public String getDefaulMode() {
        return context.getSharedPreferences("challenges_prefs",
                Context.MODE_PRIVATE).getString("DefaulModeId", "");
    }

    public int setModeID(int modeID) {
        editor.putInt("ModeIDId", modeID);
        editor.commit();
        return modeID;
    }

    public int getModeID() {
        return context.getSharedPreferences("challenges_prefs",
                Context.MODE_PRIVATE).getInt("ModeIDId", 0);
    }

    public String getPrevLat() {
        return context.getSharedPreferences("challenges_prefs",
                Context.MODE_PRIVATE).getString("PrevLatKey", "0.0");
    }

    public void setPrevLat(double lat) {
        String lattitude = String.valueOf(lat);
        editor.putString("PrevLatKey", lattitude);
        editor.commit();
    }

    public String getPrevLng() {
        return context.getSharedPreferences("challenges_prefs",
                Context.MODE_PRIVATE).getString("PrevLngKey", "");
    }

    public void setPrevLng(double lng) {
        String longi = String.valueOf(lng);
        editor.putString("PrevLngKey", longi);
        editor.commit();
    }

    public String getRouteId() {
        return context.getSharedPreferences("challenges_prefs",
                Context.MODE_PRIVATE).getString("RouteIdKey", "");
    }

    public void setRouteId(String TrackId) {
        editor.putString("RouteIdKey", TrackId);
        editor.commit();
    }

    public int getDestinationCount() {
        return context.getSharedPreferences("challenges_prefs",
                Context.MODE_PRIVATE).getInt("destCountKey", 0);
    }

    public void setDestinationCount(int destCount) {
        editor.putInt("destCountKey", destCount);
        editor.commit();
    }

    public String getLastRouteId() {
        return context.getSharedPreferences("challenges_prefs",
                Context.MODE_PRIVATE).getString("LastRouteIdKey", "");
    }

    public void setLastRouteId(String routeId) {
        editor.putString("LastRouteIdKey", routeId);
        editor.commit();
    }

    public float getMode1Dist() {
        return context.getSharedPreferences("challenges_prefs",
                Context.MODE_PRIVATE).getFloat("ModeOneDistKey", 0);
    }

    public void setMode1Dist(float mode1Dist) {
        editor.putFloat("ModeOneDistKey", mode1Dist);
        editor.commit();
    }

    public String getMode1Value() {
        return context.getSharedPreferences("challenges_prefs",
                Context.MODE_PRIVATE).getString("ModeOneValueKey", "");
    }

    public void setMode1Value(String mode1Value) {
        editor.putString("ModeOneValueKey", mode1Value);
        editor.commit();
    }

    public float getMode2Dist() {
        return context.getSharedPreferences("challenges_prefs",
                Context.MODE_PRIVATE).getFloat("ModeTwoDistKey", 0);
    }

    public void setMode2Dist(float mode2Dist) {
        editor.putFloat("ModeTwoDistKey", mode2Dist);
        editor.commit();
    }

    public String getMode2Value() {
        return context.getSharedPreferences("challenges_prefs",
                Context.MODE_PRIVATE).getString("ModeTwoValueKey", "");
    }

    public void setMode2Value(String mode2Value) {
        editor.putString("ModeTwoValueKey", mode2Value);
        editor.commit();
    }

    public float getMode3Dist() {
        return context.getSharedPreferences("challenges_prefs",
                Context.MODE_PRIVATE).getFloat("ModeThreeDistKey", 0);
    }

    public void setMode3Dist(float mode3Dist) {
        editor.putFloat("ModeThreeDistKey", mode3Dist);
        editor.commit();
    }

    public String getMode3Value() {
        return context.getSharedPreferences("challenges_prefs",
                Context.MODE_PRIVATE).getString("ModeThreeValueKey", "");
    }

    public void setMode3Value(String mode3Value) {
        editor.putString("ModeThreeValueKey", mode3Value);
        editor.commit();
    }

    public String setEmpCode(String empCode) {
        editor.putString("EmpCodeId", empCode);
        editor.commit();
        return empCode;
    }

    public String getEmpCode() {
        return context.getSharedPreferences("challenges_prefs",
                Context.MODE_PRIVATE).getString("EmpCodeId", "");
    }

    public String getMode1() {
        return context.getSharedPreferences("challenges_prefs",
                Context.MODE_PRIVATE).getString("FirstMode", "");
    }

    public void setMode1(String mode1) {
        editor.putString("FirstMode", mode1);
        editor.commit();
    }

    public String getMode2() {
        return context.getSharedPreferences("challenges_prefs",
                Context.MODE_PRIVATE).getString("SecondMode", "");
    }

    public void setMode2(String mode2) {
        editor.putString("SecondMode", mode2);
        editor.commit();
    }

    public String getMode3() {
        return context.getSharedPreferences("challenges_prefs",
                Context.MODE_PRIVATE).getString("ThirdMode", "");
    }

    public void setMode3(String mode3) {
        editor.putString("ThirdMode", mode3);
        editor.commit();
    }

    public String setCurrentDtVersion(String currentVersion) {
        editor.putString("CurrentDtVersionId", currentVersion);
        editor.commit();
        return currentVersion;
    }

    public String getCurrentDtVersion() {
        return context.getSharedPreferences("challenges_prefs",
                Context.MODE_PRIVATE).getString("CurrentDtVersionId", "");
    }

    public int setVersionHit(int versionHit) {
        editor.putInt("VersionHitId", versionHit);
        editor.commit();
        return versionHit;
    }

    public int getVersionHit() {
        return context.getSharedPreferences("challenges_prefs",
                Context.MODE_PRIVATE).getInt("VersionHitId", 0);
    }

    public long getDayLong() {
        return context.getSharedPreferences("challenges_prefs",
                Context.MODE_PRIVATE).getLong(DaysDifference, 0);
    }

    public void setDayLong(long longday) {
        editor.putLong(DaysDifference, longday);
        editor.commit();
    }

    public String setCurrentVersion(String currentVersion) {
        editor.putString("currentVersionKey", currentVersion);
        editor.commit();
        return currentVersion;
    }

    public String getCurrentVersion() {
        return context.getSharedPreferences("challenges_prefs",
                Context.MODE_PRIVATE).getString("currentVersionKey", "");
    }

    public String setSyncTime(String syncTime) {
        editor.putString("SyncTimeKey", syncTime);
        editor.commit();
        return syncTime;
    }

    public String getSyncTime() {
        return context.getSharedPreferences("challenges_prefs",
                Context.MODE_PRIVATE).getString("SyncTimeKey", "");
    }

    public String getStartStopState() {
        return context.getSharedPreferences("challenges_prefs",
                Context.MODE_PRIVATE).getString("StartStopStateKey", "");
    }

    public void setStartStopState(String startStop) {
        editor.putString("StartStopStateKey", startStop);
        editor.commit();
    }

    public boolean isDayOut() {
        return context.getSharedPreferences("challenges_prefs",
                Context.MODE_PRIVATE).getBoolean("isDayOutKey", false);
    }

    public void setIsDayOut(boolean isDayOut) {
        editor.putBoolean("isDayOutKey", isDayOut);
        editor.commit();
    }

    public boolean isDayIn() {
        return context.getSharedPreferences("challenges_prefs",
                Context.MODE_PRIVATE).getBoolean("isDayInKey", false);
    }

    public void setIsDayIn(boolean isDayIn) {
        editor.putBoolean("isDayInKey", isDayIn);
        editor.commit();
    }

    public int getDraftStatus() {
        return context.getSharedPreferences("challenges_prefs",
                Context.MODE_PRIVATE).getInt("DraftStatusKey", 0);
    }

    public void setDraftStatus(int DraftStatus) {
        editor.putInt("DraftStatusKey", DraftStatus);
        editor.commit();
    }

    public boolean isTrackServiceRunning() {
        return context.getSharedPreferences("challenges_prefs",
                Context.MODE_PRIVATE).getBoolean("isTrackServiceRunningKey",
                false);
    }

    public void setTrackServiceRunning(boolean isTrackServiceRunning) {
        editor.putBoolean("isTrackServiceRunningKey", isTrackServiceRunning);
        editor.commit();
    }

    public String getTrackStartStopState() {
        return context.getSharedPreferences("challenges_prefs",
                Context.MODE_PRIVATE).getString("trackStartStop", "");
    }

    public void setTrackStartStopState(String startStop) {
        editor.putString("trackStartStop", startStop);
        editor.commit();
    }

    public String getDelearID() {
        return context.getSharedPreferences("challenges_prefs",
                Context.MODE_PRIVATE).getString("token", "");
    }

    public void setDelearID(String token) {
        editor.putString("token", token);
        editor.commit();
    }

    public String getTokenID() {
        return context.getSharedPreferences("challenges_prefs",
                Context.MODE_PRIVATE).getString("tokennew", "");
    }

    public void setTokenID(String tokennew) {
        editor.putString("tokennew", tokennew);
        editor.commit();
    }

    public String getUserNameFootFall() {
        return context.getSharedPreferences("challenges_prefs",
                Context.MODE_PRIVATE).getString("userNameFootFall", "");
    }

    public void setUserNameFootFall(String userNameFootFall) {
        editor.putString("userNameFootFall", userNameFootFall);
        editor.commit();
    }

    public String getChannelPartnerName() {
        return context.getSharedPreferences("challenges_prefs",
                Context.MODE_PRIVATE).getString("channelPartnerGroup", "");
    }

    public void setChannelPartnerName(String channelPartnerGroup) {
        editor.putString("channelPartnerGroup", channelPartnerGroup);
        editor.commit();
    }

    public String getEMPName() {
        return context.getSharedPreferences("challenges_prefs",
                Context.MODE_PRIVATE).getString("EMPName", "");
    }

    public void setEMPName(String EMPName) {
        editor.putString("EMPName", EMPName);
        editor.commit();
    }


    public String getLastOrderStatus() {
        String lastOrder = context.
                getSharedPreferences("challenges_prefs", Context.MODE_PRIVATE)
                .getString(LastOrderStatus, "");
        return lastOrder;
    }

    public void setLastOrderStatus(String uniqueKey) {
        editor.putString(LastOrderStatus, uniqueKey);
        editor.commit();
    }

    public JSONObject getLastProduct() {
        JSONObject lastProduct = null;
        try {
            String json = context.getSharedPreferences("challenges_prefs", Context.MODE_PRIVATE).getString(LastProduct, new JSONObject().toString());
            lastProduct = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return lastProduct;
    }

    public void setLastProduct(JSONObject _object) {
        editor.putString(LastProduct, _object.toString());
        editor.commit();
    }
    public JSONObject getProductOrderRes() {
        JSONObject lastProductRes = null;
        try {
            String json = context.getSharedPreferences("challenges_prefs", Context.MODE_PRIVATE).getString(LastProductOrderRes, new JSONObject().toString());
            lastProductRes = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return lastProductRes;
    }

    public void setProductOrderRes(JSONObject _object) {
        editor.putString(LastProductOrderRes, _object.toString());
        editor.commit();
    }
    private static final String KEY_LIST="KEY_LIST";
    public void saveArrayList(ArrayList<PendingGCOrderModel> list ){
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(KEY_LIST, json);
        editor.apply();
    }

    public ArrayList<PendingGCOrderModel> getArrayList(){
        Gson gson = new Gson();
        String json = context.getSharedPreferences("challenges_prefs",Context.MODE_PRIVATE).getString(KEY_LIST, "[]");
        Type type = new TypeToken<ArrayList<PendingGCOrderModel>>() {}.getType();
        return gson.fromJson(json, type);
    }


}
