package com.erp.sheelafoam.sheelafoam.xperia;

import java.util.List;

/**
 * Created by sudhirharit on 22/11/17.
 */

public class ComplaintListModel {
    String status;
    List<Complaint> data;
    String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Complaint> getData() {
        return data;
    }

    public void setData(List<Complaint> data) {
        this.data = data;
    }
}