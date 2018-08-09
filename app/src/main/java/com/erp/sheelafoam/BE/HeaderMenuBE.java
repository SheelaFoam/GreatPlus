package com.erp.sheelafoam.BE;

import java.util.ArrayList;

/**
 * Created by priyanka.sharma on 11/29/2016.
 */

public class HeaderMenuBE {
    private String CHILD_MENU, CHILD_MENU_VALUE, PARENT_MENU, ICON, CHILD_MENU_TYPE, CHILD_MENU_TYPE1,
            CHILD_MENU_VALUE1;
    ArrayList<HeaderMenuChildBE> SUB_MENU;

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

    public String getCHILD_MENU_VALUE() {
        return CHILD_MENU_VALUE;
    }

    public void setCHILD_MENU_VALUE(String CHILD_MENU_VALUE) {
        this.CHILD_MENU_VALUE = CHILD_MENU_VALUE;
    }

    public String getPARENT_MENU() {
        return PARENT_MENU;
    }

    public void setPARENT_MENU(String PARENT_MENU) {
        this.PARENT_MENU = PARENT_MENU;
    }

    public String getICON() {
        return ICON;
    }

    public void setICON(String ICON) {
        this.ICON = ICON;
    }

    public String getCHILD_MENU_TYPE() {
        return CHILD_MENU_TYPE;
    }

    public void setCHILD_MENU_TYPE(String CHILD_MENU_TYPE) {
        this.CHILD_MENU_TYPE = CHILD_MENU_TYPE;
    }

    public String getCHILD_MENU_TYPE1() {
        return CHILD_MENU_TYPE1;
    }

    public void setCHILD_MENU_TYPE1(String CHILD_MENU_TYPE1) {
        this.CHILD_MENU_TYPE1 = CHILD_MENU_TYPE1;
    }

    public String getCHILD_MENU_VALUE1() {
        return CHILD_MENU_VALUE1;
    }

    public void setCHILD_MENU_VALUE1(String CHILD_MENU_VALUE1) {
        this.CHILD_MENU_VALUE1 = CHILD_MENU_VALUE1;
    }
}
