package com.erp.sheelafoam.BE;

/**
 * Created by priyanka.sharma on 12/13/2016.
 */

public class ProfilePicBE {
    private String op_user_profile_image, msg;
    private int status;

    public String getOp_user_profile_image() {
        return op_user_profile_image;
    }

    public void setOp_user_profile_image(String op_user_profile_image) {
        this.op_user_profile_image = op_user_profile_image;
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
