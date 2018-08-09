package com.erp.sheelafoam.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.erp.sheelafoam.BE.DashboardSliderBE;
import com.erp.sheelafoam.Picasso;
import com.erp.sheelafoam.R;

import java.util.ArrayList;


/**
 * Created by priyanka.sharma on 11/18/2016.
 */

public class DashboardListingAdaper extends RecyclerView.Adapter<DashboardListingAdaper.EmployerJobsListedHolder> {

    Context context;
    private ArrayList<DashboardSliderBE> base;
    ImageLoader imageLoader;
    DisplayImageOptions options;
    private com.squareup.picasso.Picasso picasso;

    public DashboardListingAdaper(Context context, ArrayList<DashboardSliderBE> base) {
        this.context = context;
        this.base = base;
        imageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisc(true).resetViewBeforeLoading(true)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .build();
        ImageLoader.getInstance().init(config);
    }

    @Override
    public EmployerJobsListedHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.dashboard_listing_row, parent, false);
        EmployerJobsListedHolder viewHolder = new EmployerJobsListedHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final EmployerJobsListedHolder holder, int position) {
        holder.dashboard_txt.setText(base.get(position).getMessage());
        holder.dashboard_txt_heading.setText(base.get(position).getName());

        String dashboard_image = base.get(position).getSlider_image();
        dashboard_image = dashboard_image.replace(" ", "%20");
        Log.e("Slider Image", dashboard_image);

        Picasso.with(context)
                .load(dashboard_image)
                .into(holder.dashboard_image,
                        new com.squareup.picasso.Callback() {
                            @Override
                            public void onSuccess() {
                                holder.image_loading.setVisibility(View.GONE);
                            }

                            @Override
                            public void onError() {

                                holder.image_loading.setVisibility(View.VISIBLE);
                            }
                        });
    }


    @Override
    public int getItemCount() {
        return base.size();
    }

    public class EmployerJobsListedHolder extends RecyclerView.ViewHolder {
        ImageView dashboard_image;
        TextView dashboard_txt_heading, dashboard_txt;
        ProgressBar image_loading;

        public EmployerJobsListedHolder(View itemView) {
            super(itemView);
            dashboard_txt = (TextView) itemView.findViewById(R.id.dashboard_txt);
            dashboard_txt_heading = (TextView) itemView.findViewById(R.id.dashboard_txt_heading);
            image_loading = (ProgressBar) itemView.findViewById(R.id.image_loading);
            dashboard_image = (ImageView) itemView.findViewById(R.id.dashboard_image);
        }
    }
}