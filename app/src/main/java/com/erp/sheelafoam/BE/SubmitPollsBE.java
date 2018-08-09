package com.erp.sheelafoam.BE;

/**
 * Created by pranav.mittal on 12/2/2016.
 */

public class SubmitPollsBE {
    private String error,msg;
    private int status;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
