package com.erp.sheelafoam.model;

import com.erp.sheelafoam.BE.AppointmentBE;

import java.util.ArrayList;

/**
 * Created by priyanka.sharma on 11/28/2016.
 */

public class AppointmentModel {
    ArrayList<AppointmentBE> appointment;
    ArrayList<AppointmentBE> info;

    public ArrayList<AppointmentBE> getAppointment() {
        return appointment;
    }

    public void setAppointment(ArrayList<AppointmentBE> appointment) {
        this.appointment = appointment;
    }

    public ArrayList<AppointmentBE> getInfo() {
        return info;
    }

    public void setInfo(ArrayList<AppointmentBE> info) {
        this.info = info;
    }
}
