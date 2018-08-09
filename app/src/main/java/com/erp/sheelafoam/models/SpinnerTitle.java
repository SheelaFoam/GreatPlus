package com.erp.sheelafoam.models;

public class SpinnerTitle {
    public SpinnerTitle(String ID,
                      String PropertyName) {
        this.ID = ID;
        this.PropertyName = PropertyName;
    }

    public String getPropertyName() {
        return PropertyName;
    }

    public void setPropertyName(String propertyName) {
        PropertyName = propertyName;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    String ID;
    String PropertyName;
}
