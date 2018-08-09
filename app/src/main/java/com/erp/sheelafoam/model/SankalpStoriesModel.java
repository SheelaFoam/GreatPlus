package com.erp.sheelafoam.model;

import com.erp.sheelafoam.BE.SankalpStoriesBE;

import java.util.ArrayList;

/**
 * Created by priyanka.sharma on 12/2/2016.
 */

public class SankalpStoriesModel {
    ArrayList<SankalpStoriesBE> info;

    public ArrayList<SankalpStoriesBE> getInfo() {
        return info;
    }

    public void setInfo(ArrayList<SankalpStoriesBE> info) {
        this.info = info;
    }
}
