package com.erp.sheelafoam.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ShowRoomModelOne {
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("imageList")
    @Expose
    private List<ShoowRoomModel> imageList = null;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<ShoowRoomModel> getImageList() {
        return imageList;
    }

    public void setImageList(List<ShoowRoomModel> imageList) {
        this.imageList = imageList;
    }
}
