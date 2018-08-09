package com.erp.sheelafoam.model;

import com.erp.sheelafoam.BE.TaskBE;

import java.util.ArrayList;

/**
 * Created by priyanka.sharma on 12/1/2016.
 */

public class TaskModel {
    ArrayList<TaskBE> info;

    public ArrayList<TaskBE> getInfo() {
        return info;
    }

    public void setInfo(ArrayList<TaskBE> info) {
        this.info = info;
    }
}
