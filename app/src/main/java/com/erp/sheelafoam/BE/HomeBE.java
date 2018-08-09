package com.erp.sheelafoam.BE;

import java.util.ArrayList;

/**
 * Created by priyanka.sharma on 11/28/2016.
 */

public class HomeBE {
    ArrayList<AppointmentBE> appointment;
    ArrayList<DashboardSliderBE> slider;
    ArrayList<TaskBE> task;
    ArrayList<InboxBE> inbox;
    ArrayList<ProfileImageBE> profile_image;

    public ArrayList<ProfileImageBE> getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(ArrayList<ProfileImageBE> profile_image) {
        this.profile_image = profile_image;
    }

    public ArrayList<TaskBE> getTask() {
        return task;
    }

    public void setTask(ArrayList<TaskBE> task) {
        this.task = task;
    }

    public ArrayList<AppointmentBE> getAppointment() {
        return appointment;
    }

    public void setAppointment(ArrayList<AppointmentBE> appointment) {
        this.appointment = appointment;
    }

    public ArrayList<DashboardSliderBE> getSlider() {
        return slider;
    }

    public void setSlider(ArrayList<DashboardSliderBE> slider) {
        this.slider = slider;
    }

    public ArrayList<InboxBE> getInbox() {
        return inbox;
    }

    public void setInbox(ArrayList<InboxBE> inbox) {
        this.inbox = inbox;
    }
}
