package com.erp.sheelafoam.model;

import com.erp.sheelafoam.BE.LearningRefDetailBE;

import java.util.ArrayList;

/**
 * Created by priyanka.sharma on 11/30/2016.
 */

public class LearningRefDetailModel {
    ArrayList<LearningRefDetailBE> info;
    String msg;
    int status;

    public ArrayList<LearningRefDetailBE> getInfo() {
        return info;
    }

    public void setInfo(ArrayList<LearningRefDetailBE> info) {
        this.info = info;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
