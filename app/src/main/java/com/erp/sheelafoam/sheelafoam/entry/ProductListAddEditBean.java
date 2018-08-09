package com.erp.sheelafoam.sheelafoam.entry;

import java.io.Serializable;

public class ProductListAddEditBean implements Serializable {
	
	private String productId;
	private String productName;
	private String subProductName;
	private String productSpecification;
	private String color_applicable_yn;
	
	
	public String getSubProductName() {
		return subProductName;
	}
	public void setSubProductName(String subProductName) {
		this.subProductName = subProductName;
	}
	
	public String getProductSpecification() {
		return productSpecification;
	}
	public void setProductSpecification(String productSpecification) {
		this.productSpecification = productSpecification;
	}
	
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getColor_applicable_yn() {
		return color_applicable_yn;
	}
	public void setColor_applicable_yn(String color_applicable_yn) {
		this.color_applicable_yn = color_applicable_yn;
	}
	
	
	
	

}
