package com.erp.sheelafoam.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by twigz on 12/3/18.
 */

public class EmpDetails {
    @SerializedName("eMP_NAME")
    String eMP_NAME;

    @SerializedName("rEPORING_OFFICER")
    String rEPORING_OFFICER;

    @SerializedName("rEVIEWING_OFFICER")
    String rEVIEWING_OFFICER;

    @SerializedName("storeList")
    List<StoreList> storeList;

    public String geteMP_NAME() {
        return eMP_NAME;
    }

    public void seteMP_NAME(String eMP_NAME) {
        this.eMP_NAME = eMP_NAME;
    }

    public String getrEPORING_OFFICER() {
        return rEPORING_OFFICER;
    }

    public void setrEPORING_OFFICER(String rEPORING_OFFICER) {
        this.rEPORING_OFFICER = rEPORING_OFFICER;
    }

    public String getrEVIEWING_OFFICER() {
        return rEVIEWING_OFFICER;
    }

    public void setrEVIEWING_OFFICER(String rEVIEWING_OFFICER) {
        this.rEVIEWING_OFFICER = rEVIEWING_OFFICER;
    }

    public List<StoreList> getStoreList() {
        return storeList;
    }

    public void setStoreList(List<StoreList> storeList) {
        this.storeList = storeList;
    }
}
