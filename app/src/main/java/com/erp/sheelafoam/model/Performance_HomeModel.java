package com.erp.sheelafoam.model;

import com.erp.sheelafoam.BE.Performance_homeBE;

import java.util.ArrayList;

/**
 * Created by priyanka.sharma on 12/5/2016.
 */

public class Performance_HomeModel {
    ArrayList<Performance_homeBE> my_performance;

    public ArrayList<Performance_homeBE> getMy_performance() {
        return my_performance;
    }

    public void setMy_performance(ArrayList<Performance_homeBE> my_performance) {
        this.my_performance = my_performance;
    }
}
