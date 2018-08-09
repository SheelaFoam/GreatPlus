package com.erp.sheelafoam.model;

import com.erp.sheelafoam.BE.HomeEventBE;

import java.util.ArrayList;

/**
 * Created by priyanka.sharma on 12/2/2016.
 */

public class HomeEventModel {
    ArrayList<HomeEventBE> event_list;

    public ArrayList<HomeEventBE> getEvent_list() {
        return event_list;
    }

    public void setEvent_list(ArrayList<HomeEventBE> event_list) {
        this.event_list = event_list;
    }
}
