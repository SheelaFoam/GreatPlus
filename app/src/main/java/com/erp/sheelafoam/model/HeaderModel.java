package com.erp.sheelafoam.model;

import com.erp.sheelafoam.BE.HeaderMenuBE;
import com.erp.sheelafoam.BE.ProfileImageBE;
import com.erp.sheelafoam.BE.QuickAccessMenuBE;

import java.util.ArrayList;

/**
 * Created by priyanka.sharma on 11/29/2016.
 */

public class HeaderModel  {
    ArrayList<HeaderMenuBE> header_menu;
    ArrayList<ProfileImageBE> profile_image;
    ArrayList<QuickAccessMenuBE> quick_access;

    public ArrayList<QuickAccessMenuBE> getQuick_access() {
        return quick_access;
    }

    public void setQuick_access(ArrayList<QuickAccessMenuBE> quick_access) {
        this.quick_access = quick_access;
    }


    public ArrayList<ProfileImageBE> getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(ArrayList<ProfileImageBE> profile_image) {
        this.profile_image = profile_image;
    }

    public ArrayList<HeaderMenuBE> getHeader_menu() {
        return header_menu;
    }

    public void setHeader_menu(ArrayList<HeaderMenuBE> header_menu) {
        this.header_menu = header_menu;
    }


}
