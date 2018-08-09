package com.erp.sheelafoam.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by E6036 on 5/3/2018.
 */

public class MTSModel {
    @SerializedName("CHANNEL_PARTNER_GROUP")
    @Expose
    private String cHANNELPARTNERGROUP;
    @SerializedName("LOCATION_CODE")
    @Expose
    private String lOCATIONCODE;
    @SerializedName("PRODUCT_DISPLAY_NAME")
    @Expose
    private String pRODUCTDISPLAYNAME;
    @SerializedName("LENGTH")
    @Expose
    private String lENGTH;
    @SerializedName("BREDTH")
    @Expose
    private String bREDTH;
    @SerializedName("THICK")
    @Expose
    private String tHICK;
    @SerializedName("COLOR")
    @Expose
    private String cOLOR;
    @SerializedName("CURR_STOCK")
    @Expose
    private String cURRSTOCK;
    @SerializedName("ROL_QUANTITY")
    @Expose
    private String rOLQUANTITY;

    public String getCHANNELPARTNERGROUP() {
        return cHANNELPARTNERGROUP;
    }

    public void setCHANNELPARTNERGROUP(String cHANNELPARTNERGROUP) {
        this.cHANNELPARTNERGROUP = cHANNELPARTNERGROUP;
    }

    public String getLOCATIONCODE() {
        return lOCATIONCODE;
    }

    public void setLOCATIONCODE(String lOCATIONCODE) {
        this.lOCATIONCODE = lOCATIONCODE;
    }

    public String getPRODUCTDISPLAYNAME() {
        return pRODUCTDISPLAYNAME;
    }

    public void setPRODUCTDISPLAYNAME(String pRODUCTDISPLAYNAME) {
        this.pRODUCTDISPLAYNAME = pRODUCTDISPLAYNAME;
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

    public String getCOLOR() {
        return cOLOR;
    }

    public void setCOLOR(String cOLOR) {
        this.cOLOR = cOLOR;
    }

    public String getCURRSTOCK() {
        return cURRSTOCK;
    }

    public void setCURRSTOCK(String cURRSTOCK) {
        this.cURRSTOCK = cURRSTOCK;
    }

    public String getROLQUANTITY() {
        return rOLQUANTITY;
    }

    public void setROLQUANTITY(String rOLQUANTITY) {
        this.rOLQUANTITY = rOLQUANTITY;
    }


}
