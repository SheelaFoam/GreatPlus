package com.erp.sheelafoam.model;

import com.erp.sheelafoam.BE.TelephoneBE;

import java.util.ArrayList;

/**
 * Created by pranav.mittal on 12/2/2016.
 */

public class TelephoneModel {
    private ArrayList<TelephoneBE> info;

    public ArrayList<TelephoneBE> getInfo() {
        return info;
    }

    public void setInfo(ArrayList<TelephoneBE> info) {
        this.info = info;
    }
}
