package com.erp.sheelafoam.BE;

/**
 * Created by pranav.mittal on 12/2/2016.
 */

public class PollsOptionBE {
    private String name,id;
    private boolean checkValues;

    public boolean isCheckValues() {
        return checkValues;
    }

    public void setCheckValues(boolean checkValues) {
        this.checkValues = checkValues;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
