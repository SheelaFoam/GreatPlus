package com.erp.sheelafoam.sheelafoam.exchangeschame.model;

import java.io.Serializable;

public class ConsumerItemModel implements Serializable{
    public String getProductNameValue() {
        return productNameValue;
    }

    public void setProductNameValue(String productNameValue) {
        this.productNameValue = productNameValue;
    }

    public String getLengthValue() {
        return lengthValue;
    }

    public void setLengthValue(String lengthValue) {
        this.lengthValue = lengthValue;
    }

    public String getBreathValue() {
        return breathValue;
    }

    public void setBreathValue(String breathValue) {
        this.breathValue = breathValue;
    }

    public String getThickensValue() {
        return thickensValue;
    }

    public void setThickensValue(String thickensValue) {
        this.thickensValue = thickensValue;
    }

    public String getQtyValue() {
        return qtyValue;
    }

    public void setQtyValue(String qtyValue) {
        this.qtyValue = qtyValue;
    }

    public String getTotalAmountValue() {
        return totalAmountValue;
    }

    public void setTotalAmountValue(String totalAmountValue) {
        this.totalAmountValue = totalAmountValue;
    }

    String productNameValue;
    String lengthValue;
    String breathValue;
    String thickensValue;
    String qtyValue;
    String totalAmountValue;

}
