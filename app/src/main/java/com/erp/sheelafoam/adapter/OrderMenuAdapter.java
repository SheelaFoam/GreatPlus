package com.erp.sheelafoam.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.erp.sheelafoam.BE.OrderMenuItems;
import com.erp.sheelafoam.R;
import com.erp.sheelafoam.sheelafoam.fragments.ProductOrderFragment;
import com.erp.sheelafoam.sheelafoam.fragments.ProductOrderView;
import com.erp.sheelafoam.sheelafoam.report.ReportOrderStatusFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by twigz on 22/2/18.
 */

public class OrderMenuAdapter extends RecyclerView.Adapter<OrderMenuAdapter.ViewHolder> {

    String title;
    private ArrayList<OrderMenuItems> orderMenu;
    private Context context;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    public OrderMenuAdapter(Context context, ArrayList<OrderMenuItems> orderMenu, FragmentManager fragmentManager) {
        this.context = context;
        this.orderMenu = orderMenu;
        this.fragmentManager = fragmentManager;
    }

    @Override
    public OrderMenuAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.menu_grid, viewGroup, false);

        return new ViewHolder(view);
    }

    public void commonFragmentMethod(Fragment fragment, Bundle data, String TAG) {


        //fragmentManager = ((Activity) context).getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        if (data != null)
            fragment.setArguments(data);

        fragmentTransaction.replace(R.id.order_fragment_container, fragment, TAG);
        fragmentTransaction.addToBackStack(TAG);
        if (fragmentTransaction != null)
            fragmentTransaction.commit();
    }

    @Override
    public void onBindViewHolder(OrderMenuAdapter.ViewHolder holder, final int position) {
        holder.tv_menu.setText(orderMenu.get(position).getTxt_menu());
        String asd = orderMenu.get(position).getTxt_img_url();
        Log.d("alok", asd);
        try {
            Picasso.with(context).load(asd).resize(60, 60).centerInside().into(holder.img_menu);
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title = orderMenu.get(position).getTxt_menu();
                Bundle bundle = new Bundle();
                bundle.putBoolean("order_menu", true);
                if (title.equalsIgnoreCase("Order Report")) {
                    commonFragmentMethod(new ReportOrderStatusFragment(), bundle, null);
                } else if (title.equalsIgnoreCase("place order")) {
                    commonFragmentMethod(new ProductOrderFragment(), bundle, null);

                } else if (title.equalsIgnoreCase("order status")) {
                    commonFragmentMethod(new ProductOrderView(), bundle, null);


                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderMenu.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;
        private TextView tv_menu;
        private ImageView img_menu;

        public ViewHolder(View view) {
            super(view);

            cardView = (CardView) view.findViewById(R.id.linear);
            tv_menu = (TextView) view.findViewById(R.id.tv_menu);
            img_menu = (ImageView) view.findViewById(R.id.img_menu);

        }
    }
}
