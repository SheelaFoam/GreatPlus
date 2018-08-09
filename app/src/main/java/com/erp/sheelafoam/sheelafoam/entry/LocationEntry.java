package com.erp.sheelafoam.sheelafoam.entry;

public class LocationEntry {
	
	

   private String id;
   private String userId;
   private String lat;
   private String lng;
   private String dcrDate;
   private String dcrTime;
   private String address;
   
   public LocationEntry(String lat, String lng, String time, String date)
   {
	   this.lat=lat;
	   this.lng=lng;
	   this.dcrTime=time;
	   this.dcrDate=date;
   }
   
   public LocationEntry()
   {
	   
   }
   
   
   public int hashCode(){
       //System.out.println("In hashcode");
       int hashcode = 0;
       hashcode = lat.hashCode();
       
       hashcode += lng.hashCode();
       return hashcode;
   }
   
   //http://www.java2novice.com/java-collections-and-util/hashset/duplicate/
   public boolean equals(Object obj){
       System.out.println("In equals");
       if (obj instanceof LocationEntry) {
           LocationEntry pp = (LocationEntry) obj;
           return (pp.getLat().equals(this.lat) && pp.lng == this.lng);
       } else {
           return false;
       }
   }
   
   
public String getId() {
	return id;
}
public void setId(String id) {
	this.id = id;
}
public String getUserId() {
	return userId;
}
public void setUserId(String userId) {
	this.userId = userId;
}
public String getLat() {
	return lat;
}
public void setLat(String lat) {
	this.lat = lat;
}
public String getLng() {
	return lng;
}
public void setLng(String lng) {
	this.lng = lng;
}
public String getDcrDate() {
	return dcrDate;
}
public void setDcrDate(String dcrDate) {
	this.dcrDate = dcrDate;
}
public String getDcrTime() {
	return dcrTime;
}
public void setDcrTime(String dcrTime) {
	this.dcrTime = dcrTime;
}
public String getAddress() {
	return address;
}
public void setAddress(String address) {
	this.address = address;
}
   
   
   

}
