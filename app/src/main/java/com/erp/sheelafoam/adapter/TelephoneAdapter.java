package com.erp.sheelafoam.adapter;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.erp.sheelafoam.BE.TelephoneBE;
import com.erp.sheelafoam.Picasso;
import com.erp.sheelafoam.R;

import java.util.ArrayList;

/**
 * Created by pranav.mittal on 12/2/2016.
 */

public class TelephoneAdapter extends RecyclerView.Adapter<TelephoneAdapter.TelephoneHolder> {

    Context context;
    ArrayList<TelephoneBE> base;
    int size;

    public TelephoneAdapter(Context context, ArrayList<TelephoneBE> base, int size) {
        this.context = context;
        this.base = base;
        this.size = size;
    }

    @Override
    public TelephoneHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.telephone_row, parent, false);
        TelephoneHolder viewHolder = new TelephoneHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final TelephoneHolder holder, final int position) {
        if (base.get(position).getUSER_NAME().length() > 0) {
            holder.name.setVisibility(View.VISIBLE);
            holder.name.setText(base.get(position).getUSER_NAME());
        } else {
            holder.name.setVisibility(View.GONE);
        }

        if (base.get(position).getDESIGNATION().length() > 0) {
            holder.designation.setVisibility(View.VISIBLE);
            holder.designation.setText(base.get(position).getDESIGNATION());
        } else {
            holder.designation.setVisibility(View.GONE);
        }
        if (base.get(position).getDEPARTEMENT().length() > 0) {
            holder.department.setVisibility(View.VISIBLE);
            holder.department.setText(base.get(position).getDEPARTEMENT());
        } else {
            holder.department.setVisibility(View.GONE);
        }
        if (base.get(position).getMOBILE_NO_1().length() > 0) {
            holder.mobile_no.setVisibility(View.VISIBLE);
            holder.mobile_no.setText(base.get(position).getMOBILE_NO_1());
            holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent sIntent = new Intent(Intent.ACTION_CALL, Uri


                            .parse("tel:" + base.get(position).getMOBILE_NO_1()));


                    sIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    context.startActivity(sIntent);
                }
            });
        } else {
            holder.mobile_no.setVisibility(View.GONE);
        }
        if (base.get(position).getEMAIL_ID().length() > 0) {
            holder.email_id.setVisibility(View.VISIBLE);
            holder.email_id.setText(base.get(position).getEMAIL_ID());
        } else {
            holder.email_id.setVisibility(View.GONE);
        }


        Picasso.with(context)
                .load(base.get(position).getImage())
                .fit()
                .into(holder.telephone_user_img,
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
        int setSize;
        if (base.size() > size) {
            setSize = size;
        } else {
            setSize = base.size();
        }
        return setSize;
    }

    public class TelephoneHolder extends RecyclerView.ViewHolder {
        TextView name, designation, department, mobile_no, email_id;
        ImageView telephone_user_img;
        ProgressBar image_loading;
        LinearLayout linearLayout;

        public TelephoneHolder(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.name);
            designation = (TextView) itemView.findViewById(R.id.designation);
            department = (TextView) itemView.findViewById(R.id.department);
            mobile_no = (TextView) itemView.findViewById(R.id.mobile_no);
            email_id = (TextView) itemView.findViewById(R.id.email_id);
            image_loading = (ProgressBar) itemView.findViewById(R.id.image_loading);
            telephone_user_img = (ImageView) itemView.findViewById(R.id.telephone_user_img);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.layout_row);
        }
    }
}
