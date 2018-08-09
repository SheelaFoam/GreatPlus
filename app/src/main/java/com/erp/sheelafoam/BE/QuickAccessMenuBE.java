package com.erp.sheelafoam.BE;

/**
 * Created by dell on 03-Aug-17.
 */

public class QuickAccessMenuBE {
    private String CHILD_MENU, ICON, LINK;

    public String getLINK() {
        return LINK;
    }

    public void setLINK(String LINK) {
        this.LINK = LINK;
    }

    public String getCHILD_MENU() {
        return CHILD_MENU;
    }

    public void setCHILD_MENU(String CHILD_MENU) {
        this.CHILD_MENU = CHILD_MENU;
    }

    public String getICON() {
        return ICON;
    }

    public void setICON(String ICON) {
        this.ICON = ICON;
    }
}
