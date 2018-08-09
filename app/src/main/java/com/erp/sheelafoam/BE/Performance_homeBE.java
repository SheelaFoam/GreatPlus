package com.erp.sheelafoam.BE;

/**
 * Created by priyanka.sharma on 12/5/2016.
 */

public class Performance_homeBE {
    int ACH_DETAIL;
    String KPI, TARGET, WEIGHTAGE, UOM;

    public String getWEIGHTAGE() {
        return WEIGHTAGE;
    }

    public void setWEIGHTAGE(String WEIGHTAGE) {
        this.WEIGHTAGE = WEIGHTAGE;
    }

    public String getUOM() {
        return UOM;
    }

    public void setUOM(String UOM) {
        this.UOM = UOM;
    }

    public String getTARGET() {
        return TARGET;
    }

    public void setTARGET(String TARGET) {
        this.TARGET = TARGET;
    }

    public String getKPI() {
        return KPI;
    }

    public void setKPI(String KPI) {
        this.KPI = KPI;
    }

    public int getACH_DETAIL() {
        return ACH_DETAIL;
    }

    public void setACH_DETAIL(int ACH_DETAIL) {
        this.ACH_DETAIL = ACH_DETAIL;
    }
}
