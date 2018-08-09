package com.erp.sheelafoam.model;

import com.erp.sheelafoam.BE.HomeSankalpBE;

import java.util.ArrayList;

/**
 * Created by priyanka.sharma on 12/2/2016.
 */

public class HomeSankalpModel {
    ArrayList<HomeSankalpBE> sankalp_story;

    public ArrayList<HomeSankalpBE> getSankalp_story() {
        return sankalp_story;
    }

    public void setSankalp_story(ArrayList<HomeSankalpBE> sankalp_story) {
        this.sankalp_story = sankalp_story;
    }
}
