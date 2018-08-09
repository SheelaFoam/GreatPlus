package com.erp.sheelafoam.models;

import java.util.List;

public class ProductCategoryModel {
    private String title;
    private boolean isShown;
    private List<SalesModel> salesModels;

    public boolean isShown() {
        return isShown;
    }

    public void setShown(boolean shown) {
        isShown = shown;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<SalesModel> getSalesModels() {
        return salesModels;
    }

    public void setSalesModels(List<SalesModel> salesModels) {
        this.salesModels = salesModels;
    }

}
