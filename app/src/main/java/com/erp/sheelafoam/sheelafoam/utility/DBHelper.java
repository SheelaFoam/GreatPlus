package com.erp.sheelafoam.sheelafoam.utility;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by dell on 31/01/2016.
 */
public class DBHelper extends SQLiteOpenHelper {
    Context context = null;
    private static DBHelper dbConn =  null;
    public static final String TABLE_COMPLAINT = "complains";

	public static final String KEY_COMPLAINT = "complainNo";
	public static final String KEY_IMAGE = "imageString";
    


    private DBHelper(Context con)
    {
        super(con,Constants.DBNAME,null,1);
        context = con;
    }

   public static DBHelper getDBHelper(Context context)
    {
        if(dbConn == null)
            dbConn = new DBHelper(context);
        return dbConn;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table complains (complainNo text ,imageString text)"); 
        
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS contacts");
        onCreate(db);
    }



    public boolean updateComplainInfo(String complainNo, String imageString)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues content = new ContentValues();
        content.put("complainNo",complainNo);
        content.put("imageString",imageString);
        db.update("complains", content, "complainNo = ? ",new String[]{complainNo} );
        return true;
    }

    public boolean insertComplain(String complainNo, String imageString)
    {
	        String[] image_names=imageString.split("[*]+");
	    	
	    	for(int i=0;i<image_names.length;i++)
	    	{
	    		int count=checkImageInDb(complainNo, image_names[i]);
	        	Log.e("image is available in DB or not", "image is available in DB or not"+count);
	        	if(count==0)
	        	{
	        		Log.e("Insert image", "Insert image"+image_names[i]);
	        		 SQLiteDatabase db = this.getWritableDatabase();
	        	        ContentValues contentValues = new ContentValues();
	        	        contentValues.put("complainNo", complainNo);
	        	        contentValues.put("imageString", image_names[i]);
	        	        db.insert("complains", null, contentValues);

	        	}
	        	else
	        	{
	        		Log.e("update image", "update image");
	        		
	        		/* SQLiteDatabase db = this.getWritableDatabase();
	        	        ContentValues content = new ContentValues();
	        	        content.put("complainNo",complainNo);
	        	        content.put("imageString",imageString);
	        	        db.update("complains", content, "complainNo = ? ",new String[]{complainNo} );*/
	        	}
	    	}
    	
    	
    	
       /* Cursor cur = getData(complainNo);
        if(cur.getCount()!=0)
            updateComplainInfo(complainNo,imageString);
        SQLiteDatabase db = this.getWritableDatabase(); 
        ContentValues contentValues = new ContentValues();
        contentValues.put("complainNo", complainNo);
        contentValues.put("imageString", imageString);
        db.insert("complains", null, contentValues);*/
        
        
        return true;
    }
    
    //check image is already exist in db.
    
    public int checkImageInDb(String complainNo, String imageString)
    {
    	
    	
    	int count=0;
    	
    	/*String sql="select * from complains where complainNo="+"'"+complainNo+"'" +
    			" AND imageString="+"'"+imageString+"'";*/
    	
    	String sql = "select * from complains where "+ DBHelper.KEY_COMPLAINT+"='"+complainNo+"'"
				+ " AND " + DBHelper.KEY_IMAGE+"='"+imageString+"'";
    	
    	Log.e("sql for check image in DB", sql);
    	 SQLiteDatabase db = this.getWritableDatabase();
    	 Cursor cursor=db.rawQuery(sql, null);
    	 count=cursor.getCount();
    	return count;
    }
    
    

    public Cursor getData(String complainNo){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from complains where "+ DBHelper.KEY_COMPLAINT+"='"+complainNo+"'", null );
        return res;
    }
}
