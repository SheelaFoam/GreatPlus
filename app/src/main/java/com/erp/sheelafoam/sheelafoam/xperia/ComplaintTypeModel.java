package com.erp.sheelafoam.sheelafoam.xperia;

/**
 * Created by sudhirharit on 20/11/17.
 */

public class ComplaintTypeModel {


    private String complaint_code;
    private String complaint_name;

    public String getComplaint_code() {
        return complaint_code;
    }

    public void setComplaint_code(String complaint_code) {
        this.complaint_code = complaint_code;
    }

    public String getComplaint_name() {
        return complaint_name;
    }

    public void setComplaint_name(String complaint_name) {
        this.complaint_name = complaint_name;
    }

    public ComplaintTypeModel(String complaint_code, String complaint_name) {
        this.complaint_code = complaint_code;
        this.complaint_name = complaint_name;
    }
}
