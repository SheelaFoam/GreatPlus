package com.erp.sheelafoam.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.erp.sheelafoam.R;
import com.erp.sheelafoam.models.MTSModel;
import com.erp.sheelafoam.models.PerformanceNewModels;
import com.erp.sheelafoam.models.ShoowRoomModel;
import com.erp.sheelafoam.utils.CustomTypefaceTextView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PerformanceNewAdpter extends BaseAdapter {
    private Context context;
    private List<PerformanceNewModels> array;
    private LayoutInflater inflater;
    Typeface tf;

    public PerformanceNewAdpter(Context context, List<PerformanceNewModels> array) {
        this.context = context;
        this.array = array;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return array.size();
    }

    @Override
    public PerformanceNewModels getItem(int i) {
        return array.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = inflater.inflate(R.layout.performance_model_item, parent, false);
        TextView tv_img_name = (TextView) convertView.findViewById(R.id.tv_img_name);
        PerformanceNewModels model = array.get(i);
        tv_img_name.setText(getItem(i).getName());
        ImageView grid_image = (ImageView) convertView.findViewById(R.id.iv_img_url);
        ViewGroup.LayoutParams layoutParams = grid_image.getLayoutParams();
        layoutParams.height = 200; //this is in pixels
        grid_image.setLayoutParams(layoutParams);
        if (!TextUtils.isEmpty(model.getImage())) {
            String encodedUrl = model.getImage().replaceAll("\\s+", "%20");
            System.out.println("imgurl" + encodedUrl);
            Picasso.with(context).load(encodedUrl).into(grid_image);
        }
        return convertView;
    }
}
