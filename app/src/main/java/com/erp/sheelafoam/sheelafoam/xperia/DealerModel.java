package com.erp.sheelafoam.sheelafoam.xperia;

/**
 * Created by sudhirharit on 21/11/17.
 */

public class DealerModel {
    private String dealerName;
    private String dealerZone;
    private String dealerCateogry;
    private String dealerId;
    public  DealerModel(){

    }
    public DealerModel(String dealerName, String dealerZone, String dealerCateogry, String dealerId) {
        this.dealerName = dealerName;
        this.dealerZone = dealerZone;
        this.dealerCateogry = dealerCateogry;
        this.dealerId = dealerId;
    }

    public String getDealerName() {
        return dealerName;
    }

    public void setDealerName(String dealerName) {
        this.dealerName = dealerName;
    }

    public String getDealerZone() {
        return dealerZone;
    }

    public void setDealerZone(String dealerZone) {
        this.dealerZone = dealerZone;
    }

    public String getDealerCateogry() {
        return dealerCateogry;
    }

    public void setDealerCateogry(String dealerCateogry) {
        this.dealerCateogry = dealerCateogry;
    }

    public String getDealerId() {
        return dealerId;
    }

    public void setDealerId(String dealerId) {
        this.dealerId = dealerId;
    }
}
