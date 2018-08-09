package com.erp.sheelafoam.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PendingGCOrderModel {
    @SerializedName("p_dealer_id")
    @Expose
    private String pDealerId;
    @SerializedName("p_thick")
    @Expose
    private String pThick;
    @SerializedName("p_customer_mobile")
    @Expose
    private String pCustomerMobile;
    @SerializedName("p_bredth")
    @Expose
    private String pBredth;
    @SerializedName("p_cash_reward")
    @Expose
    private String pCashReward;
    @SerializedName("p_product_display_name")
    @Expose
    private String pProductDisplayName;
    @SerializedName("p_user_id")
    @Expose
    private String pUserId;
    @SerializedName("p_product_mrp")
    @Expose
    private String pProductMrp;
    @SerializedName("p_customer_name")
    @Expose
    private String pCustomerName;
    @SerializedName("p_quantity")
    @Expose
    private String pQuantity;
    @SerializedName("p_advance_amt")
    @Expose
    private String pAdvanceAmt;
    @SerializedName("p_length")
    @Expose
    private String pLength;
    @SerializedName("p_request_id")
    @Expose
    private String pRequestId;
    @SerializedName("Order Number")
    @Expose
    private String orderNumber;

    public String getPDealerId() {
        return pDealerId;
    }

    public void setPDealerId(String pDealerId) {
        this.pDealerId = pDealerId;
    }

    public String getPThick() {
        return pThick;
    }

    public void setPThick(String pThick) {
        this.pThick = pThick;
    }

    public String getPCustomerMobile() {
        return pCustomerMobile;
    }

    public void setPCustomerMobile(String pCustomerMobile) {
        this.pCustomerMobile = pCustomerMobile;
    }

    public String getPBredth() {
        return pBredth;
    }

    public void setPBredth(String pBredth) {
        this.pBredth = pBredth;
    }

    public String getPCashReward() {
        return pCashReward;
    }

    public void setPCashReward(String pCashReward) {
        this.pCashReward = pCashReward;
    }

    public String getPProductDisplayName() {
        return pProductDisplayName;
    }

    public void setPProductDisplayName(String pProductDisplayName) {
        this.pProductDisplayName = pProductDisplayName;
    }

    public String getPUserId() {
        return pUserId;
    }

    public void setPUserId(String pUserId) {
        this.pUserId = pUserId;
    }

    public String getPProductMrp() {
        return pProductMrp;
    }

    public void setPProductMrp(String pProductMrp) {
        this.pProductMrp = pProductMrp;
    }

    public String getPCustomerName() {
        return pCustomerName;
    }

    public void setPCustomerName(String pCustomerName) {
        this.pCustomerName = pCustomerName;
    }

    public String getPQuantity() {
        return pQuantity;
    }

    public void setPQuantity(String pQuantity) {
        this.pQuantity = pQuantity;
    }

    public String getPAdvanceAmt() {
        return pAdvanceAmt;
    }

    public void setPAdvanceAmt(String pAdvanceAmt) {
        this.pAdvanceAmt = pAdvanceAmt;
    }

    public String getPLength() {
        return pLength;
    }

    public void setPLength(String pLength) {
        this.pLength = pLength;
    }

    public String getPRequestId() {
        return pRequestId;
    }

    public void setPRequestId(String pRequestId) {
        this.pRequestId = pRequestId;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    boolean flag;





}
