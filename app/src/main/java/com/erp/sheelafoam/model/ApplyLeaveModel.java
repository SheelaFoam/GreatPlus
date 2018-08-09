package com.erp.sheelafoam.model;

import com.erp.sheelafoam.BE.ApplyLeaveBE;

import java.util.ArrayList;

/**
 * Created by priyanka.sharma on 12/15/2016.
 */

public class ApplyLeaveModel {
    private ArrayList<ApplyLeaveBE> info;

    public ArrayList<ApplyLeaveBE> getInfo() {
        return info;
    }

    public void setInfo(ArrayList<ApplyLeaveBE> info) {
        this.info = info;
    }
}
