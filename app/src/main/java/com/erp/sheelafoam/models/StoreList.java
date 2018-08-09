package com.erp.sheelafoam.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by twigz on 12/3/18.
 */

public class StoreList {
    @SerializedName("cHANNEL_PARTNER_GROUP")
    String cHANNEL_PARTNER_GROUP;

    @SerializedName("pARENT_CHANNEL_PARTNER_NAME")
    String pARENT_CHANNEL_PARTNER_NAME;

    public String getcHANNEL_PARTNER_GROUP() {
        return cHANNEL_PARTNER_GROUP;
    }

    public void setcHANNEL_PARTNER_GROUP(String cHANNEL_PARTNER_GROUP) {
        this.cHANNEL_PARTNER_GROUP = cHANNEL_PARTNER_GROUP;
    }

    public String getpARENT_CHANNEL_PARTNER_NAME() {
        return pARENT_CHANNEL_PARTNER_NAME;
    }

    public void setpARENT_CHANNEL_PARTNER_NAME(String pARENT_CHANNEL_PARTNER_NAME) {
        this.pARENT_CHANNEL_PARTNER_NAME = pARENT_CHANNEL_PARTNER_NAME;
    }
}
