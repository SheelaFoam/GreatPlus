package com.erp.sheelafoam.sheelafoam.xperia.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.widget.LinearLayout;

import com.erp.sheelafoam.R;
import com.erp.sheelafoam.touch.TouchImageView;

import java.io.File;

public class ZoomImageFr extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.zoom_image_view);
		LinearLayout ll = (LinearLayout) findViewById(R.id.imageViewAdd);
		TouchImageView img = new TouchImageView(getApplicationContext());
		Intent i = getIntent();
		File f;
		Bitmap b;
		try {
			f = new File(Environment.getExternalStorageDirectory() + "/Pictures/SF/" + i.getStringExtra("ImgName")+".jpg");
			b = BitmapFactory.decodeFile(f.getAbsolutePath());
			img.setImageBitmap(b);
		} catch (Exception e) {
		}

		ll.addView(img);
	}
}
