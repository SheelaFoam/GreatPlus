package com.erp.sheelafoam.BE;

import java.util.ArrayList;

/**
 * Created by priyanka.sharma on 11/22/2016.
 */

public class PasswordBE {
    private int status;
    private String auth_type, error, msg;
    private ArrayList<PasswordUserInfoBE> user_info;

    public ArrayList<PasswordUserInfoBE> getUser_info() {
        return user_info;
    }

    public void setUser_info(ArrayList<PasswordUserInfoBE> user_info) {
        this.user_info = user_info;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getAuth_type() {
        return auth_type;
    }

    public void setAuth_type(String auth_type) {
        this.auth_type = auth_type;
    }

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
}
