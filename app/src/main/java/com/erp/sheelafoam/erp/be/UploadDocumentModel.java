package com.erp.sheelafoam.erp.be;

import java.util.ArrayList;

public class UploadDocumentModel {
	private String STATUS,msg;
	private ArrayList<UploadDocumentBE> data;
	public String getSTATUS() {
		return STATUS;
	}
	public void setSTATUS(String sTATUS) {
		STATUS = sTATUS;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public ArrayList<UploadDocumentBE> getData() {
		return data;
	}
	public void setData(ArrayList<UploadDocumentBE> data) {
		this.data = data;
	}
	
	

}
