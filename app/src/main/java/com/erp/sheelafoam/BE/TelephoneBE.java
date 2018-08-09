package com.erp.sheelafoam.BE;

/**
 * Created by pranav.mittal on 12/2/2016.
 */

public class TelephoneBE {
    private String USER_NAME, DESIGNATION, DEPARTEMENT, MOBILE_NO_1, EMAIL_ID, image;

    public String getUSER_NAME() {
        return USER_NAME;
    }

    public void setUSER_NAME(String USER_NAME) {
        this.USER_NAME = USER_NAME;
    }

    public String getDESIGNATION() {
        return DESIGNATION;
    }

    public void setDESIGNATION(String DESIGNATION) {
        this.DESIGNATION = DESIGNATION;
    }

    public String getDEPARTEMENT() {
        return DEPARTEMENT;
    }

    public void setDEPARTEMENT(String DEPARTEMENT) {
        this.DEPARTEMENT = DEPARTEMENT;
    }

    public String getMOBILE_NO_1() {
        return MOBILE_NO_1;
    }

    public void setMOBILE_NO_1(String MOBILE_NO_1) {
        this.MOBILE_NO_1 = MOBILE_NO_1;
    }

    public String getEMAIL_ID() {
        return EMAIL_ID;
    }

    public void setEMAIL_ID(String EMAIL_ID) {
        this.EMAIL_ID = EMAIL_ID;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
