package com.erp.sheelafoam.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SalesModel implements Cloneable, Serializable {
    int itemCount = 0;
    @SerializedName("DLR_SALES_REP_NAME")
    @Expose
    private String dLRSALESREPNAME;
    @SerializedName("PRODUCT_SPECIFICATION")
    @Expose
    private String pRODUCTSPECIFICATION;
    @SerializedName("LENGTH")
    @Expose
    private String lENGTH;
    @SerializedName("BREDTH")
    @Expose
    private String bREDTH;
    @SerializedName("THICK")
    @Expose
    private String tHICK;
    @SerializedName("COUNT(1)")
    @Expose
    private String cOUNT1;

    public String getDLRSALESREPNAME() {
        return dLRSALESREPNAME;
    }

    public void setDLRSALESREPNAME(String dLRSALESREPNAME) {
        this.dLRSALESREPNAME = dLRSALESREPNAME;
    }

    public String getPRODUCTSPECIFICATION() {
        return pRODUCTSPECIFICATION;
    }

    public void setPRODUCTSPECIFICATION(String pRODUCTSPECIFICATION) {
        this.pRODUCTSPECIFICATION = pRODUCTSPECIFICATION;
    }

    public String getLENGTH() {
        return lENGTH;
    }

    public void setLENGTH(String lENGTH) {
        this.lENGTH = lENGTH;
    }

    public String getBREDTH() {
        return bREDTH;
    }

    public void setBREDTH(String bREDTH) {
        this.bREDTH = bREDTH;
    }

    public String getTHICK() {
        return tHICK;
    }

    public void setTHICK(String tHICK) {
        this.tHICK = tHICK;
    }

    public String getCOUNT1() {
        return cOUNT1;
    }

    public void setCOUNT1(String cOUNT1) {
        this.cOUNT1 = cOUNT1;
    }

    public int getItemCount() {
        return itemCount;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }

    @Override
    public SalesModel clone() {
        SalesModel salesModel = null;
        try {
            salesModel = (SalesModel) super.clone();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return salesModel;
    }
}

