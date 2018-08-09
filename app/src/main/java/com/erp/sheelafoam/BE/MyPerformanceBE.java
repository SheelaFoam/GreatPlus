package com.erp.sheelafoam.BE;

import java.util.ArrayList;

/**
 * Created by priyanka.sharma on 12/1/2016.
 */

public class MyPerformanceBE {
    private String KPI, ACH_DETAIL, WEIGHTAGE, UOM,TARGET;
   private ArrayList<MyPerformanceChildBE> performace_detail;

    public ArrayList<MyPerformanceChildBE> getPerformace_detail() {
        return performace_detail;
    }

    public String getTARGET() {
        return TARGET;
    }

    public void setTARGET(String TARGET) {
        this.TARGET = TARGET;
    }

    public void setPerformace_detail(ArrayList<MyPerformanceChildBE> performace_detail) {
        this.performace_detail = performace_detail;
    }

    public String getKPI() {
        return KPI;
    }

    public void setKPI(String KPI) {
        this.KPI = KPI;
    }

    public String getACH_DETAIL() {
        return ACH_DETAIL;
    }

    public void setACH_DETAIL(String ACH_DETAIL) {
        this.ACH_DETAIL = ACH_DETAIL;
    }

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
}
