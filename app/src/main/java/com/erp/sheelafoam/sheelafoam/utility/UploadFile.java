package com.erp.sheelafoam.sheelafoam.utility;

/**
 * Created by Xperia Technologies Pvt. Ltd. on 28/10/2016.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.erp.sheelafoam.constant.Constant;
import com.erp.sheelafoam.sheelafoam.complaint.ComplainDetails;
import com.erp.sheelafoam.sheelafoam.xperia.function.ComplaintDataBase;
import com.erp.sheelafoam.utils.FileUpload;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

//import org.apache.commons.io.FileUtils;

public class UploadFile {
	public static boolean isUploadComplete = false;
	public static int intCountSuccess = 0;
	public static int intCountFailed = 0;
	public static String strMsg = "";
	public static String strCMPID = "";
	public static String strMobileNo = "";
	public static String imgStr = "";
	static String query = "";
    public static Context con;
	public static List<String> listOfSuccessImg = new ArrayList<String>();
	public static List<String> listOfImg = null;
	static ComplaintDataBase coDataBase;
	static ComplainDetails complainDetails;
	static String complainId;
	static String userId;
	public static void upload(List<String> list, String remark,String genuineType, Context context, ComplainDetails complainDetails, String complai) {
		coDataBase=new ComplaintDataBase(context);
		con=context;
		complainDetails=complainDetails;
		intCountFailed = list.size();
		complainId = complai;
		listOfImg = list;
		SharedPreferences sharedPreferences = context.getSharedPreferences(Constant.PREFS_NAME, Context.MODE_PRIVATE);
		userId = sharedPreferences.getString(Constant.Sp_GrtPlusUserID, "");
		Log.e("listOfImg", listOfImg.toString());
		String server = "125.19.46.252";
		int port = 21;
		String user = "complaint";
		String pass = "redhat";
		FTPClient ftpClient = new FTPClient();
		try {
			if (listOfImg.size() > 0) {
				ftpClient.connect(server, port);
				ftpClient.login(user, pass);
				///////////////////////////////////
				System.out.print(ftpClient.getReplyString());
				// After connection attempt, you should check the reply code to
				// verify
				// success.
				int reply = ftpClient.getReplyCode();
				if (!FTPReply.isPositiveCompletion(reply)) {
					ftpClient.disconnect();
					System.out.println("FTP server refused connection.");
				}
				ftpClient.enterLocalPassiveMode();
				ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
				// APPROACH #1: uploads first file using an InputStream
				File directoryFile = new File("/sdcard/Pictures/SF");
				File[] subFiles = directoryFile.listFiles();
				File firstLocalFile = new File("/sdcard/Pictures/SF/");
				InputStream inputStream = null;
				for (String fileName : listOfImg) {
					for (File dataFile : subFiles) {
						////////////////////////////////////////////////
						if (!dataFile.getName().toLowerCase().equals("test")) {
							//uploadFile = dataFile;
							if (dataFile.getName().equals(fileName + ".jpg")) {
								
								Log.e("Image Name: ", fileName);
								String filePath = firstLocalFile + "/" + dataFile.getName();
								inputStream = new FileInputStream(filePath);
								// updated code. Update by Md Farhan Raja(Xperia Technologies Pvt. Ltd).
								OutputStream outStream = new FileOutputStream(
										firstLocalFile + "/Back_" + dataFile.getName());
								// Start -- Code to compress images before FTP
								// Upload
								Bitmap tempBmpForCompression = BitmapFactory.decodeStream(inputStream);
								inputStream.close();
								try {
									tempBmpForCompression.compress(Bitmap.CompressFormat.JPEG, 30, outStream);
								} catch (Exception ex) {
									Log.d("Error: ", ex.getMessage());
								}
								outStream.flush();
								outStream.close();
								inputStream = new FileInputStream(firstLocalFile + "/Back_" + dataFile.getName());
								// End -- Code to compress images before FTP
								// Upload
								// finish
								// inputStream = new FileInputStream(filePath);
								boolean testBool = ftpClient.storeFile(dataFile.getName(), inputStream);
								int mssg1 = ftpClient.getReplyCode();
								inputStream.close();
								File tempFile = new File("/sdcard/Pictures/SF/" + fileName + ".jpg");
								//tempFile.delete();
								intCountSuccess += 1;
								intCountFailed -= 1;
								listOfSuccessImg.add(fileName);
								//String s=coDataBase.updateMatDetails(strCMPID, strMobileNo, fileName, "Y");
								//Log.e("Update ", s);
							}
						}
					}
				}
			}
		} catch (Exception ex) {
			Log.d("Error: ", ex.getMessage());
		} finally {
			//complainDetails.success(strMobileNo,strCMPID,listOfSuccessImg, remark);
			if(listOfSuccessImg.size()>0) {
				UpdateDeatilsAfterFTPUpload(listOfSuccessImg, remark,genuineType, context, complainId, "");
				strMsg = "Upload Process Complete with " + intCountSuccess + " successful uploades.";
				if (intCountFailed > 0)
					strMsg += " " + intCountFailed + " Images could not be uploaded, because of connection error.";
				isUploadComplete = true;
			} else {

			}
			try {
				if (ftpClient.isConnected()) {
					ftpClient.logout();
					ftpClient.disconnect();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
			
		}
	}



	public static Void UpdateDeatilsAfterFTPUpload(List<String> imgList, String remark, String genuineType, Context context, String complainId,String status ) {
		HttpClient httpClient = new DefaultHttpClient();
		HttpContext localContext = new BasicHttpContext();

		for (String img : imgList) {
			if (imgStr == null || imgStr == "")
				imgStr = img;
			else
				imgStr = imgStr + "***" + img;
		}
		try {
			query = URLEncoder.encode(remark, "utf-8");
		} catch (Exception e) {

		}
		try {
			HttpGet httpGet = new HttpGet("http://125.19.46.252/ws/ComplaintVisitReport.php?MOBILE=" + strMobileNo
					+ "&ID=" + strCMPID + "&IMAGE=" + imgStr + "&REMARK=" + URLEncoder.encode(remark, "utf-8")+"&p_gen_ungen="+URLEncoder.encode(genuineType, "utf-8"));
			
			
			
			HttpResponse response =httpClient.execute(httpGet, localContext);
			InputStream is=null;
	        String responseString="";
	        try {
	            HttpEntity entity=response.getEntity();
	            is=entity.getContent();
	            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
	            StringBuilder sb = new StringBuilder();
	            String line = null;
	            while ((line = reader.readLine()) != null) {
	                sb.append(line + "\n");
	            }
	            is.close();
	            responseString=sb.toString();

	            if(!responseString.contains("Images Submitted")){
					ComplaintDataBase complaintDataBase = new ComplaintDataBase(context);
					complaintDataBase.saveMatDetails(userId, complainId, imgList, remark, status);
				}else {

				}
                //coDataBase.updateMatDetailsSuccess(strCMPID, strMobileNo, listOfSuccessImg);
	            
	        } catch (IOException e) {
	            e.printStackTrace();
	        }

			
			Log.e("Success Response",responseString.toString());
								
			String text = null;
			listOfSuccessImg.clear();
			listOfImg.clear();
			imgStr="";
		} catch (Exception e) {
			listOfSuccessImg.clear();
			listOfImg.clear();
			imgStr="";
			return null;
		}
		listOfSuccessImg.clear();
		listOfImg.clear();
		imgStr="";
		return null;
	}
		

	//Update by Md Farhan Raja(Xperia Technologies Pvt. Ltd).
}
