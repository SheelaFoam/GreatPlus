package com.erp.sheelafoam.model;

import com.erp.sheelafoam.BE.QuickAccessMenuBE;

import java.util.ArrayList;

/**
 * Created by dell on 03-Aug-17.
 */

public class QuickAccessModel {
    ArrayList<QuickAccessMenuBE> quick_access;

    public ArrayList<QuickAccessMenuBE> getQuick_access() {
        return quick_access;
    }

    public void setQuick_access(ArrayList<QuickAccessMenuBE> quick_access) {
        this.quick_access = quick_access;
    }
}
