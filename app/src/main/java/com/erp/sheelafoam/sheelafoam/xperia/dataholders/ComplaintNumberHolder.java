package com.erp.sheelafoam.sheelafoam.xperia.dataholders;

public class ComplaintNumberHolder 
{
  static String ComplaintNumber;
  static int IsResume,ListPosition,isSuccess=0;
  

public static int getIsSuccess() {
	return isSuccess;
}

public static void setIsSuccess(int isSuccess) {
	ComplaintNumberHolder.isSuccess = isSuccess;
}

public static int getItemPosition() {
	return ListPosition;
}

public static void setItemPosition(int itemPosition) {
	ListPosition = itemPosition;
}

public static int getIsResume() {
	return IsResume;
}

public static void setIsResume(int isResume) {
	IsResume = isResume;
}

public static String getComplaintNumber() {
	return ComplaintNumber;
}

public static void setComplaintNumber(String complaintNumber) {
	ComplaintNumber = complaintNumber;
}



}
