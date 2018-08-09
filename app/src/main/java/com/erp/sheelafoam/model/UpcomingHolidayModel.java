package com.erp.sheelafoam.model;

import com.erp.sheelafoam.BE.UpcomingHolidayBE;

import java.util.ArrayList;

/**
 * Created by priyanka.sharma on 12/2/2016.
 */

public class UpcomingHolidayModel {
    ArrayList<UpcomingHolidayBE> info;

    public ArrayList<UpcomingHolidayBE> getInfo() {
        return info;
    }

    public void setInfo(ArrayList<UpcomingHolidayBE> info) {
        this.info = info;
    }
}
