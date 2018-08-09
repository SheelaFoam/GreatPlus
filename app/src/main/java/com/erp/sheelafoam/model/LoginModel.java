package com.erp.sheelafoam.model;

import com.erp.sheelafoam.BE.LoginBE;

import java.util.ArrayList;

/**
 * Created by priyanka.sharma on 11/22/2016.
 */

public class LoginModel {
    ArrayList<LoginBE> info;

    public ArrayList<LoginBE> getInfo() {
        return info;
    }

    public void setInfo(ArrayList<LoginBE> info) {
        this.info = info;
    }
}
