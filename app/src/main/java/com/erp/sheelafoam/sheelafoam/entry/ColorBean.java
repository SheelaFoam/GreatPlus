package com.erp.sheelafoam.sheelafoam.entry;

import java.io.Serializable;

public class ColorBean implements Serializable {
	
	private String id;
	private String color;
	private String product_display_name;
	
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public String getProduct_display_name() {
		return product_display_name;
	}
	public void setProduct_display_name(String product_display_name) {
		this.product_display_name = product_display_name;
	}
	
	
	
	
	
	
	

}
