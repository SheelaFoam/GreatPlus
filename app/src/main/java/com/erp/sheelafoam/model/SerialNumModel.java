package com.erp.sheelafoam.model;

/**
 * Created by E5956 on 4/30/2018.
 */

public class SerialNumModel {
    boolean isChecked = false;
    String sNo;

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getsNo() {
        return sNo;
    }

    public void setsNo(String sNo) {
        this.sNo = sNo;
    }
}
