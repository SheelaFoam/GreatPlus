package com.erp.sheelafoam.BE;

/**
 * Created by priyanka.sharma on 12/13/2016.
 */

public class ChangePasswordBE {
    private String msg, token;
    private int status;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
