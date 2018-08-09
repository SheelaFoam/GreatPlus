package com.erp.sheelafoam.model;

import com.erp.sheelafoam.BE.CompanyPerformanceBE;
import com.erp.sheelafoam.BE.CompanyPerformanceHeadingBE;

import java.util.ArrayList;

/**
 * Created by priyanka.sharma on 11/29/2016.
 */

public class CompanyPerformanceModel {
    ArrayList<CompanyPerformanceBE> company_performance;
    ArrayList<CompanyPerformanceBE> info;
    ArrayList<CompanyPerformanceHeadingBE> company_performance_heading;

    public ArrayList<CompanyPerformanceBE> getInfo() {
        return info;
    }

    public void setInfo(ArrayList<CompanyPerformanceBE> info) {
        this.info = info;
    }

    public ArrayList<CompanyPerformanceBE> getCompany_performance() {
        return company_performance;
    }

    public void setCompany_performance(ArrayList<CompanyPerformanceBE> company_performance) {
        this.company_performance = company_performance;
    }

    public ArrayList<CompanyPerformanceHeadingBE> getCompany_performance_heading() {
        return company_performance_heading;
    }

    public void setCompany_performance_heading(ArrayList<CompanyPerformanceHeadingBE> company_performance_heading) {
        this.company_performance_heading = company_performance_heading;
    }
}
