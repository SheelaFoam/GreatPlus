package com.erp.sheelafoam.model;

import com.erp.sheelafoam.BE.PasswordBE;

import java.util.ArrayList;

/**
 * Created by priyanka.sharma on 11/22/2016.
 */

public class PasswordModel {
    ArrayList<PasswordBE> info;

    public ArrayList<PasswordBE> getInfo() {
        return info;
    }

    public void setInfo(ArrayList<PasswordBE> info) {
        this.info = info;
    }
}
