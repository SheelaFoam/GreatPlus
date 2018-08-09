package com.erp.sheelafoam.model;

import com.erp.sheelafoam.BE.HomeBE;

import java.util.ArrayList;

/**
 * Created by priyanka.sharma on 11/28/2016.
 */

public class HomeModel {
    ArrayList<HomeBE> home;

    public ArrayList<HomeBE> getHome() {
        return home;
    }

    public void setHome(ArrayList<HomeBE> home) {
        this.home = home;
    }
}
