package com.erp.sheelafoam.model;

import com.erp.sheelafoam.BE.ApproveTaskBE;

import java.util.ArrayList;

/**
 * Created by priyanka.sharma on 12/14/2016.
 */

public class ApproveTaskModel {
    ArrayList<ApproveTaskBE> info;

    public ArrayList<ApproveTaskBE> getInfo() {
        return info;
    }

    public void setInfo(ArrayList<ApproveTaskBE> info) {
        this.info = info;
    }
}
