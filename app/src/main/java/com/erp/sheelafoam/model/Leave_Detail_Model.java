package com.erp.sheelafoam.model;

import com.erp.sheelafoam.BE.LeaveDetailBE;

import java.util.ArrayList;

/**
 * Created by priyanka.sharma on 12/22/2016.
 */

public class Leave_Detail_Model {
    private ArrayList<LeaveDetailBE> leave_detail;

    public ArrayList<LeaveDetailBE> getLeave_detail() {
        return leave_detail;
    }

    public void setLeave_detail(ArrayList<LeaveDetailBE> leave_detail) {
        this.leave_detail = leave_detail;
    }
}
