package com.erp.sheelafoam.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by E5956 on 4/24/2018.
 */

public class GuaranteeLogModel {
    @SerializedName("0")
    @Expose
    private String _0;
    @SerializedName("SERIAL_NUMBER")
    @Expose
    private String sERIALNUMBER;
    @SerializedName("1")
    @Expose
    private Object _1;
    @SerializedName("SERIAL_NUMBER1")
    @Expose
    private Object sERIALNUMBER1;
    @SerializedName("2")
    @Expose
    private String _2;
    @SerializedName("DATETIME")
    @Expose
    private String dATETIME;
    @SerializedName("3")
    @Expose
    private String _3;
    @SerializedName("RECEIVE_MESSAGE")
    @Expose
    private String rECEIVEMESSAGE;
    @SerializedName("4")
    @Expose
    private String _4;
    @SerializedName("SENT_MESSAGE")
    @Expose
    private String sENTMESSAGE;
    @SerializedName("5")
    @Expose
    private String _5;
    @SerializedName("GIFT_MESSAGE")
    @Expose
    private String gIFTMESSAGE;

    public String get0() {
        return _0;
    }

    public void set0(String _0) {
        this._0 = _0;
    }

    public String getSERIALNUMBER() {
        return sERIALNUMBER;
    }

    public void setSERIALNUMBER(String sERIALNUMBER) {
        this.sERIALNUMBER = sERIALNUMBER;
    }

    public Object get1() {
        return _1;
    }

    public void set1(Object _1) {
        this._1 = _1;
    }

    public Object getSERIALNUMBER1() {
        return sERIALNUMBER1;
    }

    public void setSERIALNUMBER1(Object sERIALNUMBER1) {
        this.sERIALNUMBER1 = sERIALNUMBER1;
    }

    public String get2() {
        return _2;
    }

    public void set2(String _2) {
        this._2 = _2;
    }

    public String getDATETIME() {
        return dATETIME;
    }

    public void setDATETIME(String dATETIME) {
        this.dATETIME = dATETIME;
    }

    public String get3() {
        return _3;
    }

    public void set3(String _3) {
        this._3 = _3;
    }

    public String getRECEIVEMESSAGE() {
        return rECEIVEMESSAGE;
    }

    public void setRECEIVEMESSAGE(String rECEIVEMESSAGE) {
        this.rECEIVEMESSAGE = rECEIVEMESSAGE;
    }

    public String get4() {
        return _4;
    }

    public void set4(String _4) {
        this._4 = _4;
    }

    public String getSENTMESSAGE() {
        return sENTMESSAGE;
    }

    public void setSENTMESSAGE(String sENTMESSAGE) {
        this.sENTMESSAGE = sENTMESSAGE;
    }

    public String get5() {
        return _5;
    }

    public void set5(String _5) {
        this._5 = _5;
    }

    public String getGIFTMESSAGE() {
        return gIFTMESSAGE;
    }

    public void setGIFTMESSAGE(String gIFTMESSAGE) {
        this.gIFTMESSAGE = gIFTMESSAGE;
    }

}