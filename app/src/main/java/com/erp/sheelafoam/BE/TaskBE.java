package com.erp.sheelafoam.BE;

/**
 * Created by priyanka.sharma on 11/28/2016.
 */

public class TaskBE {
    private String displayname, TASK_DATETIME, TASK_TITLE, TASK_DESC, AUTHORIZE_YN, TASK_ID;

    public String getTASK_ID() {
        return TASK_ID;
    }

    public void setTASK_ID(String TASK_ID) {
        this.TASK_ID = TASK_ID;
    }

    public String getDisplayname() {
        return displayname;
    }

    public void setDisplayname(String displayname) {
        this.displayname = displayname;
    }

    public String getTASK_DATETIME() {
        return TASK_DATETIME;
    }

    public void setTASK_DATETIME(String TASK_DATETIME) {
        this.TASK_DATETIME = TASK_DATETIME;
    }

    public String getTASK_TITLE() {
        return TASK_TITLE;
    }

    public void setTASK_TITLE(String TASK_TITLE) {
        this.TASK_TITLE = TASK_TITLE;
    }

    public String getTASK_DESC() {
        return TASK_DESC;
    }

    public void setTASK_DESC(String TASK_DESC) {
        this.TASK_DESC = TASK_DESC;
    }

    public String getAUTHORIZE_YN() {
        return AUTHORIZE_YN;
    }

    public void setAUTHORIZE_YN(String AUTHORIZE_YN) {
        this.AUTHORIZE_YN = AUTHORIZE_YN;
    }
}
