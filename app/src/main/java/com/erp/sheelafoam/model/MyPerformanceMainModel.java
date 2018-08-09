package com.erp.sheelafoam.model;

import com.erp.sheelafoam.BE.MyPerformanceBE;

import java.util.ArrayList;

/**
 * Created by priyanka.sharma on 12/1/2016.
 */

public class MyPerformanceMainModel {
    ArrayList<MyPerformanceBE> info;

    public ArrayList<MyPerformanceBE> getInfo() {
        return info;
    }

    public void setInfo(ArrayList<MyPerformanceBE> info) {
        this.info = info;
    }
}
