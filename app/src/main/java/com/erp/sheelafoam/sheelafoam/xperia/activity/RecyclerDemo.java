package com.erp.sheelafoam.sheelafoam.xperia.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

import com.erp.sheelafoam.R;
import com.erp.sheelafoam.sheelafoam.xperia.adapter.MyWheelAdapter;
import com.wx.wheelview.widget.WheelView;

import java.util.ArrayList;
import java.util.List;

public class RecyclerDemo extends Activity {
	WheelView wv;
	List<String> data = new ArrayList<String>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.recycler_demo);
		 for(int i=1;i<=51;i++)
         {
             data.add(i+"");
         }
        wv=(WheelView)findViewById(R.id.wheelview);
         WheelView.WheelViewStyle style = new WheelView.WheelViewStyle();
         style.selectedTextColor = Color.parseColor("#000000");
         style.textColor = Color.GRAY;
         style.textSize=15;
         style.holoBorderColor= Color.TRANSPARENT;
         style.selectedTextSize = 16;
         wv.setStyle(style);
         wv.setWheelSize(1);
         wv.setSkin(WheelView.Skin.Holo);
         wv.setWheelAdapter(new MyWheelAdapter(getApplicationContext()));
         wv.setWheelData(data);
         
         
      //wv.setSelection(select);
         wv.setOnWheelItemSelectedListener(new WheelView.OnWheelItemSelectedListener() {
             @Override
             public void onItemSelected(int i, Object o) {
                Toast.makeText(getApplicationContext(), i+"", Toast.LENGTH_SHORT).show();
             }
         });
	}

}
