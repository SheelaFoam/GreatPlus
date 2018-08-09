package com.erp.sheelafoam.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PerformanceModelData extends PerformanceModel {

    @SerializedName("data")
    @Expose
    private List<PerformanceData> data = null;
    @SerializedName("status")
    @Expose
    private String status;

    public List<PerformanceData> getData() {
        return data;
    }

    public void setData(List<PerformanceData> data) {
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
