package com.erp.sheelafoam.BE;

import java.util.ArrayList;

/**
 * Created by priyanka.sharma on 12/12/2016.
 */

public class HeaderMenuChildBE {
    private String CHILD_MENU, LINK, ICON;
    ArrayList<HeaderMenuChildBE> SUB_MENU;

    public String getICON() {
        return ICON;
    }

    public void setICON(String ICON) {
        this.ICON = ICON;
    }

    public ArrayList<HeaderMenuChildBE> getSUB_MENU() {
        return SUB_MENU;
    }

    public void setSUB_MENU(ArrayList<HeaderMenuChildBE> SUB_MENU) {
        this.SUB_MENU = SUB_MENU;
    }

    public String getCHILD_MENU() {
        return CHILD_MENU;
    }

    public void setCHILD_MENU(String CHILD_MENU) {
        this.CHILD_MENU = CHILD_MENU;
    }

    public String getLINK() {
        return LINK;
    }

    public void setLINK(String LINK) {
        this.LINK = LINK;
    }
}
