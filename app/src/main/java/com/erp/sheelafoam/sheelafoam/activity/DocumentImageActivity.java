package com.erp.sheelafoam.sheelafoam.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.ImageView;


import com.erp.sheelafoam.R;

import java.io.InputStream;

public class DocumentImageActivity extends Activity {

	ImageView ivImage;
	  ProgressDialog progressDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_document_image);
		
		ivImage=(ImageView) findViewById(R.id.iv_image);
		progressDialog=new ProgressDialog(DocumentImageActivity.this);
		
		new DownloadImageTask(ivImage)
        .execute(getIntent().getStringExtra("IMAGEURL"));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.document_image, menu);
		return true;
	}
	
	
	private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
		  ImageView bmImage;
		

		  public DownloadImageTask(ImageView bmImage) {
		      this.bmImage = bmImage;
		      progressDialog.setMessage("Loading Document");
		      progressDialog.show();
		  }

		  protected Bitmap doInBackground(String... urls) {
		      String urldisplay = urls[0];
		      Bitmap mIcon11 = null;
		      try {
		        InputStream in = new java.net.URL(urldisplay).openStream();
		        mIcon11 = BitmapFactory.decodeStream(in);
		      } catch (Exception e) {
		          Log.e("Error", e.getMessage());
		          e.printStackTrace();
		      }
		      return mIcon11;
		  }

		  protected void onPostExecute(Bitmap result) {
			  progressDialog.dismiss();
		      bmImage.setImageBitmap(result);
		  }
		}

}
