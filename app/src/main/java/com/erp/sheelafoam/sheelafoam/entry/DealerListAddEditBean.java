package com.erp.sheelafoam.sheelafoam.entry;

import java.io.Serializable;

public class DealerListAddEditBean implements Serializable {
	
	private String dealerId;
	private String dealerName;
	private String dealerZone;
	
	
	
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
	public String getDealerZone() {
		return dealerZone;
	}
	public void setDealerZone(String dealerZone) {
		this.dealerZone = dealerZone;
	}
	
	
	
	
	

}
