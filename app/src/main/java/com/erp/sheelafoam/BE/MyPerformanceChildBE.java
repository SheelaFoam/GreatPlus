package com.erp.sheelafoam.BE;

/**
 * Created by priyanka.sharma on 12/1/2016.
 */

public class MyPerformanceChildBE {
    private String  PRODUCT_SEGMENT, GROWTH;
    private Float LAST_YEAR_SALE;

    public Float getLAST_YEAR_SALE() {
        return LAST_YEAR_SALE;
    }

    public void setLAST_YEAR_SALE(Float LAST_YEAR_SALE) {
        this.LAST_YEAR_SALE = LAST_YEAR_SALE;
    }

    public String getPRODUCT_SEGMENT() {
        return PRODUCT_SEGMENT;
    }

    public void setPRODUCT_SEGMENT(String PRODUCT_SEGMENT) {
        this.PRODUCT_SEGMENT = PRODUCT_SEGMENT;
    }

    public String getGROWTH() {
        return GROWTH;
    }

    public void setGROWTH(String GROWTH) {
        this.GROWTH = GROWTH;
    }
}
