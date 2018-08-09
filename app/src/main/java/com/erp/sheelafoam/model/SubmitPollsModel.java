package com.erp.sheelafoam.model;

import com.erp.sheelafoam.BE.SubmitPollsBE;

import java.util.ArrayList;

/**
 * Created by pranav.mittal on 12/2/2016.
 */

public class SubmitPollsModel {
    private ArrayList<SubmitPollsBE> info;

    public ArrayList<SubmitPollsBE> getInfo() {
        return info;
    }

    public void setInfo(ArrayList<SubmitPollsBE> info) {
        this.info = info;
    }
}
