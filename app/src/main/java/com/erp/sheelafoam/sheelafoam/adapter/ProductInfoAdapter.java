package com.erp.sheelafoam.sheelafoam.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import com.erp.sheelafoam.R;

public class ProductInfoAdapter extends RecyclerView.Adapter<ProductInfoAdapter.ProductHolder> {

    public ProductInfoAdapter() {

    }

    @Override
    public ProductHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_info, parent, false);
        return new ProductHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductHolder holder, int position) {
        holder.spnThickness.setAdapter(new ProductMeasureAdapter(holder.spnBreadth.getContext(), "120 cm"));
        holder.spnLength.setAdapter(new ProductMeasureAdapter(holder.spnBreadth.getContext(), "90 cm"));
        holder.spnBreadth.setAdapter(new ProductMeasureAdapter(holder.spnBreadth.getContext(), "100 cm"));
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    class ProductHolder extends RecyclerView.ViewHolder {
        private Spinner spnThickness, spnLength, spnBreadth;

        public ProductHolder(View itemView) {
            super(itemView);
            spnThickness = (Spinner) itemView.findViewById(R.id.spnThickness);
            spnLength = (Spinner) itemView.findViewById(R.id.spnLength);
            spnBreadth = (Spinner) itemView.findViewById(R.id.spnBreadth);
        }
    }
}
