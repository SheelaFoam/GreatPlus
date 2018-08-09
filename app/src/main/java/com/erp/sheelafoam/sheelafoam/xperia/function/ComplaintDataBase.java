package com.erp.sheelafoam.sheelafoam.xperia.function;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.erp.sheelafoam.sheelafoam.xperia.dataholders.FailedComplaintHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Md Farhan Raja on 9/15/2016.
 */
public class ComplaintDataBase extends SQLiteOpenHelper {

	public static final int DbVersion = 1;
	public static final String DbName = "agentcomplaint.db";
	
	public static final String CreateMatTableFinal = "create table MatressFinal (CId text,UId text,Imgname text,Remark text,TryUpload text,Uploaded text)";
	

	public ComplaintDataBase(Context context) {
		super(context, DbName, null, DbVersion);
	}


	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CreateMatTableFinal);
	}

	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}


	public int saveMatDetails(String CId, String UId, List<String> imgs, String Remark, String Status)
	{
		deleteRow(CId, UId);
		try
		{
			if(imgs.size()>0)
			{
			    for(int i=0;i<imgs.size();i++)
			    {
			    	SQLiteDatabase db=this.getWritableDatabase();
					ContentValues cont = new ContentValues();
					cont.put("CId", CId);
					cont.put("UId", UId);
					cont.put("Imgname", imgs.get(i).toString());
					cont.put("Remark", Remark);
					cont.put("TryUpload", "N");
					cont.put("Uploaded", Status);
					db.insert("MatressFinal", null, cont);
			    }
			}else
			{			  
			    	SQLiteDatabase db=this.getWritableDatabase();
					ContentValues cont = new ContentValues();
					cont.put("CId", CId);
					cont.put("UId", UId);
					cont.put("Imgname", "");
					cont.put("Remark", Remark);
					cont.put("TryUpload", "N");
					cont.put("Uploaded", Status);
					db.insert("MatressFinal", null, cont);			   
			}

			return 1;
		}
		catch(SQLiteException e){
		return 2;}
	}

	
	public void deleteRow(String CId, String UId)
	{
		try
		{
	SQLiteDatabase db = this.getWritableDatabase();//TryUpload='N' AND Uploaded='N' AND
	db.execSQL("DELETE FROM MatressFinal WHERE CId='" + CId + "' AND " + "UId='" + UId + "'");
	db.close();}catch(SQLException e){}
	}
	

	public Cursor getMatDetails(String CompId, String UserId) {
		try {
			SQLiteDatabase db = this.getReadableDatabase();
			String query = "select Imgname,Remark,Uploaded from MatressFinal where TryUpload='N' AND Uploaded='N' AND CId=" + "'" + CompId + "'" + " AND UId=" + "'" + UserId + "'";
			Cursor cursor = db.rawQuery(query, null);
			return cursor;
		} catch (SQLiteException e) {
			return null;
		}
	}
	
	
	public Cursor getFailedMatImages(String CompId, String UserId) {
		try {
			SQLiteDatabase db = this.getReadableDatabase();
			String query = "select Imgname,Remark,Uploaded from MatressFinal where TryUpload='Y' AND Uploaded='N' AND CId=" + "'" + CompId + "'" + " AND UId=" + "'" + UserId + "'";
			Cursor cursor = db.rawQuery(query, null);
			return cursor;
		} catch (SQLiteException e) {
			return null;
		}
	}
	
	
	public ArrayList<FailedComplaintHolder> getFailedComplaintsId1(String UserId) {
		try {
			ArrayList<FailedComplaintHolder> Ids=new ArrayList<FailedComplaintHolder>();
						
			SQLiteDatabase db = this.getReadableDatabase();
			String query = "select Distinct CId from MatressFinal where TryUpload='Y' AND Uploaded='N' AND UId=" + "'" + UserId + "'";
			Cursor cursor = db.rawQuery(query, null);
			
			if (cursor.moveToFirst()) {
				do {
					FailedComplaintHolder fc=new FailedComplaintHolder();
	                fc.setComId(cursor.getString(0));
	                String queryc = "select CId from MatressFinal where TryUpload='Y' AND Uploaded='N' AND CId=" + "'" + cursor.getString(0) + "' AND UId=" + "'" + UserId + "'";
	    			Cursor cursorc = db.rawQuery(queryc, null);
	                fc.setTotalImages(cursorc.getCount()+"");
	                Ids.add(fc);	                
				} while (cursor.moveToNext());
			} 						
			return Ids;
			
		} catch (SQLiteException e) {
			ArrayList<FailedComplaintHolder> Ids=new ArrayList<FailedComplaintHolder>();
			return Ids;
		}
	}

	public int updateMatDetailsTry(String CId, String UId, List<String> imgs)
	{		
		try
		{
			if(imgs.size()>0)
			{
			    for(int i=0;i<imgs.size();i++)
			    {
			    	SQLiteDatabase db=this.getWritableDatabase();
			    	ContentValues cont = new ContentValues();
					cont.put("TryUpload", "Y");
					db.update("MatressFinal", cont, "CId" + "=" + "'" + CId + "'" + " AND " + "UId" + "=" + "'" + UId + "'" + " AND " + "Imgname" + "=" + "'" + imgs.get(i).toString() + "'", null);
			    }
			}else
			{}
			return 1;
		}
		catch(SQLiteException e){
		return 2;}
	}

	public int updateMatDetailsSuccess(String CId, String UId, String imgs)
	{		
		Log.e("Error", "Deleted");
		try
		{
		 SQLiteDatabase db=this.getWritableDatabase();
		 db.delete("MatressFinal", "CId" + "=" + "'" + CId + "'" + " AND " + "UId" + "=" + "'" + UId + "'" + " AND " + "Imgname" + "=" + "'" + imgs + "'", null);
		 Log.e("Success", "Deleted");
		 return 1;
		}
		catch(SQLiteException e){
			Log.e("Error", "Deleted");
		return 2;}
	}
	
}
