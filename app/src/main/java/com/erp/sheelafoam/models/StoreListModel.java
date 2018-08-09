package com.erp.sheelafoam.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by twigz on 12/3/18.
 */

public class StoreListModel {

    @SerializedName("data")
    List<EmpDetails> data;

    public List<EmpDetails> getData() {
        return data;
    }

    public void setData(List<EmpDetails> data) {
        this.data = data;
    }
}
