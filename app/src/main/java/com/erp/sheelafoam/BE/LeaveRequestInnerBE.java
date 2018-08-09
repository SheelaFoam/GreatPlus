package com.erp.sheelafoam.BE;

import java.util.ArrayList;

/**
 * Created by priyanka.sharma on 12/14/2016.
 */

public class LeaveRequestInnerBE {
    ArrayList<LeaveBE> Leave;

    public ArrayList<LeaveBE> getLeave() {

        return Leave;
    }

    public void setLeave(ArrayList<LeaveBE> leave) {
        Leave = leave;
    }
}
