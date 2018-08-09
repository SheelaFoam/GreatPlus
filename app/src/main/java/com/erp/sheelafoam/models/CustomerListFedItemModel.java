package com.erp.sheelafoam.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CustomerListFedItemModel {
    @SerializedName("ageingOfMattress")
    @Expose
    private String ageingOfMattress;
    @SerializedName("currentMattressBrand")
    @Expose
    private String currentMattressBrand;
    @SerializedName("dateOfVisit")
    @Expose
    private String dateOfVisit;
    @SerializedName("dob")
    @Expose
    private String dob;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("firstName")
    @Expose
    private String firstName;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("havePurchasedItem")
    @Expose
    private String havePurchasedItem;
    @SerializedName("howYouHeard")
    @Expose
    private String howYouHeard;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("interestedIn")
    @Expose
    private String interestedIn;
    @SerializedName("lastName")
    @Expose
    private String lastName;
    @SerializedName("mobile")
    @Expose
    private String mobile;
    @SerializedName("purposeOfVisit")
    @Expose
    private String purposeOfVisit;
    @SerializedName("storeId")
    @Expose
    private String storeId;
    @SerializedName("storeName")
    @Expose
    private String storeName;

    public String getAgeingOfMattress() {
        return ageingOfMattress;
    }

    public void setAgeingOfMattress(String ageingOfMattress) {
        this.ageingOfMattress = ageingOfMattress;
    }

    public String getCurrentMattressBrand() {
        return currentMattressBrand;
    }

    public void setCurrentMattressBrand(String currentMattressBrand) {
        this.currentMattressBrand = currentMattressBrand;
    }

    public String getDateOfVisit() {
        return dateOfVisit;
    }

    public void setDateOfVisit(String dateOfVisit) {
        this.dateOfVisit = dateOfVisit;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getHavePurchasedItem() {
        return havePurchasedItem;
    }

    public void setHavePurchasedItem(String havePurchasedItem) {
        this.havePurchasedItem = havePurchasedItem;
    }

    public String getHowYouHeard() {
        return howYouHeard;
    }

    public void setHowYouHeard(String howYouHeard) {
        this.howYouHeard = howYouHeard;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInterestedIn() {
        return interestedIn;
    }

    public void setInterestedIn(String interestedIn) {
        this.interestedIn = interestedIn;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPurposeOfVisit() {
        return purposeOfVisit;
    }

    public void setPurposeOfVisit(String purposeOfVisit) {
        this.purposeOfVisit = purposeOfVisit;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

}
