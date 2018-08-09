package com.erp.sheelafoam.BE;

import java.util.ArrayList;

/**
 * Created by priyanka.sharma on 12/14/2016.
 */

public class RquestInnerBE {
    ArrayList<FacilityBE> Facilities;
    ArrayList<LeaveBE> Leave;
    ArrayList<OtherRequestBE> other_request;

    public ArrayList<OtherRequestBE> getOther_request() {
        return other_request;
    }

    public void setOther_request(ArrayList<OtherRequestBE> other_request) {
        this.other_request = other_request;
    }

    public ArrayList<FacilityBE> getFacilities() {
        return Facilities;
    }

    public void setFacilities(ArrayList<FacilityBE> facilities) {
        Facilities = facilities;
    }

    public ArrayList<LeaveBE> getLeave() {
        return Leave;
    }

    public void setLeave(ArrayList<LeaveBE> leave) {
        Leave = leave;
    }
}
