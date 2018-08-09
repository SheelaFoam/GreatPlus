package com.erp.sheelafoam.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ShoowRoomModel {
    @SerializedName("employeeId")
    @Expose
    private String employeeId;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("imageDate")
    @Expose
    private String imageDate;
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getImageDate() {
        return imageDate;
    }

    public void setImageDate(String imageDate) {
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
    }}