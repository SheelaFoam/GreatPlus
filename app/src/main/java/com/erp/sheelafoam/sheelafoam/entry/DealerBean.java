package com.erp.sheelafoam.sheelafoam.entry;

import java.io.Serializable;

public class DealerBean implements Serializable {
	
	private String dealerId;
	private String dealerName;
	private String dealerZone;
	private String dealer_category;
	private String dealer_area;
	private String dealer_city;
	private String dealer_state;
	
	public String getDealerZone() {
		return dealerZone;
	}
	public void setDealerZone(String dealerZone) {
		this.dealerZone = dealerZone;
	}
	
	public String getDealerId() {
		return dealerId;
	}
	public void setDealerId(String dealerId) {
		this.dealerId = dealerId;
	}
	public String getDealerName() {
		return dealerName;
	}
	public void setDealerName(String dealerName) {
		this.dealerName = dealerName;
	}
	public String getDealer_category() {
		return dealer_category;
	}
	public void setDealer_category(String dealer_category) {
		this.dealer_category = dealer_category;
	}
	public String getDealer_area() {
		return dealer_area;
	}
	public void setDealer_area(String dealer_area) {
		this.dealer_area = dealer_area;
	}
	public String getDealer_city() {
		return dealer_city;
	}
	public void setDealer_city(String dealer_city) {
		this.dealer_city = dealer_city;
	}
	public String getDealer_state() {
		return dealer_state;
	}
	public void setDealer_state(String dealer_state) {
		this.dealer_state = dealer_state;
	}
	
	
	
	

}
