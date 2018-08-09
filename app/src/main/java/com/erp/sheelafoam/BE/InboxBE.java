package com.erp.sheelafoam.BE;

import java.util.ArrayList;

/**
 * Created by priyanka.sharma on 11/28/2016.
 */

public class InboxBE {
    private String d,su;
    private ArrayList<InboxNameBE> e;

    public String getD() {
        return d;
    }

    public void setD(String d) {
        this.d = d;
    }

    public ArrayList<InboxNameBE> getE() {
        return e;
    }

    public void setE(ArrayList<InboxNameBE> e) {
        this.e = e;
    }

    public String getSu() {
        return su;
    }

    public void setSu(String su) {
        this.su = su;
    }
}
