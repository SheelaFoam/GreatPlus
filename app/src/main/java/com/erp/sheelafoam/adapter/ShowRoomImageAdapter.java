package com.erp.sheelafoam.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.erp.sheelafoam.R;
import com.erp.sheelafoam.models.ShoowRoomModel;
import com.erp.sheelafoam.utils.CustomTypefaceTextView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ShowRoomImageAdapter extends BaseAdapter {
    Typeface tf;
    private Context context;
    private List<ShoowRoomModel> array;
    private LayoutInflater inflater;

    public ShowRoomImageAdapter(Context context, List<ShoowRoomModel> array) {
        this.context = context;
        this.array = array;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return array.size();
    }

    @Override
    public ShoowRoomModel getItem(int i) {
        return array.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = inflater.inflate(R.layout.shoowroom_img_item, parent, false);
        ShoowRoomModel model = array.get(i);
        ImageView grid_image = (ImageView) convertView.findViewById(R.id.iv_galey_img);
        ViewGroup.LayoutParams layoutParams = grid_image.getLayoutParams();
        layoutParams.height = 200; //this is in pixels
        grid_image.setLayoutParams(layoutParams);
        if (!TextUtils.isEmpty(model.getImageUrl())) {
            String encodedUrl = model.getImageUrl().replaceAll("\\s+", "%20");
            System.out.println("imgurl" + encodedUrl);
            Picasso.with(context).load(encodedUrl).into(grid_image);
        }
        return convertView;
    }
}