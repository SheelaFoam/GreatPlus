package com.erp.sheelafoam.models;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by admin on 26-02-2018.
 */

public class Data {

    @SerializedName("employeeId")
    @Expose
    private String employeeId;
    @SerializedName("imageDate")
    @Expose
    private Object imageDate;
    @SerializedName("imageUrl")
    @Expose
    private String imageUrl;
    @SerializedName("storeId")
    @Expose
    private String storeId;

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public Object getImageDate() {
        return imageDate;
    }

    public void setImageDate(Object imageDate) {
        this.imageDate = imageDate;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }
}
