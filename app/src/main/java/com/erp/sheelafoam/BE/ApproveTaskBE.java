package com.erp.sheelafoam.BE;

/**
 * Created by priyanka.sharma on 12/14/2016.
 */

public class ApproveTaskBE {
    private String displayname, TASK_DATETIME, TASK_TITLE, TASK_DESC;

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
}
