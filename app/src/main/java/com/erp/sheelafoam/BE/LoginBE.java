package com.erp.sheelafoam.BE;

/**
 * Created by priyanka.sharma on 11/22/2016.
 */

public class LoginBE {
    private  int status;
    private String  area, email_login, op_otp,msg;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getEmail_login() {
        return email_login;
    }

    public void setEmail_login(String email_login) {
        this.email_login = email_login;
    }

    public String getOp_otp() {
        return op_otp;
    }

    public void setOp_otp(String op_otp) {
        this.op_otp = op_otp;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
