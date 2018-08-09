package com.erp.sheelafoam.model;

import com.erp.sheelafoam.BE.LatestEventsBE;

import java.util.ArrayList;

/**
 * Created by priyanka.sharma on 12/2/2016.
 */

public class LAtestEventsModel {
    ArrayList<LatestEventsBE> info;

    public ArrayList<LatestEventsBE> getInfo() {
        return info;
    }

    public void setInfo(ArrayList<LatestEventsBE> info) {
        this.info = info;
    }
}
