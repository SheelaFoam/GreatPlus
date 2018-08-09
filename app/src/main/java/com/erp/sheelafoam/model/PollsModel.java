package com.erp.sheelafoam.model;

import com.erp.sheelafoam.BE.PollsBE;

import java.util.ArrayList;

/**
 * Created by pranav.mittal on 12/5/2016.
 */

public class PollsModel {

    ArrayList<PollsBE> info;

    public ArrayList<PollsBE> getInfo() {
        return info;
    }

    public void setInfo(ArrayList<PollsBE> info) {
        this.info = info;
    }
}
