package com.erp.sheelafoam.model;

import com.erp.sheelafoam.BE.DashboardSliderBE;

import java.util.ArrayList;

/**
 * Created by priyanka.sharma on 11/28/2016.
 */

public class DashboardSliderModel {
    public ArrayList<DashboardSliderBE> slider;

    public ArrayList<DashboardSliderBE> getSlider() {
        return slider;
    }

    public void setSlider(ArrayList<DashboardSliderBE> slider) {
        this.slider = slider;
    }
}
