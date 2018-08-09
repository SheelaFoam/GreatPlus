package com.erp.sheelafoam.BE;

import java.util.ArrayList;

/**
 * Created by priyanka.sharma on 12/15/2016.
 */

public class ApplyLeaveBE {
    private ArrayList<LeaveArrayBE> leave_type;
    private int status;

    public ArrayList<LeaveArrayBE> getLeave_type() {
        return leave_type;
    }

    public void setLeave_type(ArrayList<LeaveArrayBE> leave_type) {
        this.leave_type = leave_type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
