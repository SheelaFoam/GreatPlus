package com.erp.sheelafoam.model;

import com.erp.sheelafoam.BE.RquestInnerBE;

import java.util.ArrayList;

/**
 * Created by priyanka.sharma on 12/13/2016.
 */

public class RequestInnerMOdel {
    ArrayList<RquestInnerBE> request_center_inner_menu;

    public ArrayList<RquestInnerBE> getRequest_center_inner_menu() {
        return request_center_inner_menu;
    }

    public void setRequest_center_inner_menu(ArrayList<RquestInnerBE> request_center_inner_menu) {
        this.request_center_inner_menu = request_center_inner_menu;
    }
}
