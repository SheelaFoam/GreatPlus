package com.erp.sheelafoam.utils;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.erp.sheelafoam.sheelafoam.complaint.ComplainDetails;
import com.erp.sheelafoam.sheelafoam.utility.UploadFile;

import java.util.List;

/**
 * Created by dell on 10/01/2016.
 */

public class FileUpload extends Thread {

	public static List<String> imgList;
	List<String> imgList_server;
	Activity act;
	public static String remark = "";
	Context context;
	ComplainDetails complainDetails;
	String complaintId;
	String genuineType;
	
	public FileUpload(String complaintId, List<String> list, String remark,String genuineType, Context context, ComplainDetails complainDetails) {
		Log.e("uploading11111", "i am in image uploading11111" + list.size());
		imgList = list;
		this.remark = remark;
		this.complainDetails=complainDetails;
		this.context=context;
		this.complaintId = complaintId;
		this.genuineType = genuineType;
	}

	public void run() {

		if (imgList.size() > 0) 
		{
			UploadFile.upload(imgList, remark,genuineType,context,complainDetails, complaintId);
			while (!UploadFile.isUploadComplete) {
			}
		} else 
		{
			UploadFile.upload(imgList, remark,genuineType,context,complainDetails, complaintId);
		}

		if (UploadFile.isUploadComplete) {
			Log.e("successfully22222", "file upload successfully22222");
		}
	}

}
