package com.erp.sheelafoam.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by E5956 on 4/26/2018.
 */

public class UserModel {
    @SerializedName("p_user_id")
    @Expose
    private String pUserId;
    @SerializedName("p_bundle_number")
    @Expose
    private String pBundleNumber;
    @SerializedName("serial_number")
    @Expose
    private ArrayList<String> serialNumber = null;
    @SerializedName("op_message")
    @Expose
    private String opMessage;

    public String getPUserId() {
        return pUserId;
    }

    public void setPUserId(String pUserId) {
        this.pUserId = pUserId;
    }

    public String getPBundleNumber() {
        return pBundleNumber;
    }

    public void setPBundleNumber(String pBundleNumber) {
        this.pBundleNumber = pBundleNumber;
    }

    public ArrayList<String> getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(ArrayList<String> serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getOpMessage() {
        return opMessage;
    }

    public void setOpMessage(String opMessage) {
        this.opMessage = opMessage;
    }

}
