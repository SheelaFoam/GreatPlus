package com.erp.sheelafoam.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by E6036 on 5/1/2018.
 */

public class RepresentativeName {

    @SerializedName("SALES_REP_NAME")
    @Expose
    private String sALESREPNAME;

    public String getSALESREPNAME() {
        return sALESREPNAME;
    }

    public void setSALESREPNAME(String sALESREPNAME) {
        this.sALESREPNAME = sALESREPNAME;
    }


}
