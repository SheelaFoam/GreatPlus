package com.erp.sheelafoam.model;

import com.erp.sheelafoam.BE.ChangePasswordBE;

import java.util.ArrayList;

/**
 * Created by priyanka.sharma on 12/13/2016.
 */

public class ChangePasswordModel {
    ArrayList<ChangePasswordBE> info;

    public ArrayList<ChangePasswordBE> getInfo() {
        return info;
    }

    public void setInfo(ArrayList<ChangePasswordBE> info) {
        this.info = info;
    }
}
