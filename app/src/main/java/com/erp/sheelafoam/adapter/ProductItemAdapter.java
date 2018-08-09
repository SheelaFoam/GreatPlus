package com.erp.sheelafoam.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.erp.sheelafoam.R;
import com.erp.sheelafoam.models.SalesModel;
import com.erp.sheelafoam.sheelafoam.exchangeschame.ProductMainActivity;

import java.util.List;

public class ProductItemAdapter extends RecyclerView.Adapter<ProductItemAdapter.ProductItemHolder> {

    private List<SalesModel> list;
    private ProductMainActivity productMainActivity;

    public ProductItemAdapter(ProductMainActivity productMainActivity, List<SalesModel> list) {
        this.list = list;
        this.productMainActivity = productMainActivity;
    }

    @Override
    public ProductItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_list, parent, false);
        return new ProductItemHolder(view);
    }


    @Override
    public void onBindViewHolder(ProductItemHolder holder, int position) {
        final SalesModel model = getItem(position);
        holder.item_name.setText(model.getBREDTH());
        holder.item_short_desc.setText(model.getLENGTH());
        holder.item_price.setText(model.getCOUNT1());
        holder.item_count.setText("" + model.getItemCount());
        holder.remove_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(model.getItemCount()>0){
                    model.setItemCount(model.getItemCount() - 1);
                    productMainActivity.updateUI(model, true);
                    notifyDataSetChanged();
                }

            }
        });
        holder.add_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                model.setItemCount(model.getItemCount() + 1);
                productMainActivity.updateUI(model, false);
                notifyDataSetChanged();
            }
        });
    }

    private SalesModel getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ProductItemHolder extends RecyclerView.ViewHolder {
        TextView item_name, item_short_desc, item_price, remove_item, item_count, add_item;

        public ProductItemHolder(View itemView) {
            super(itemView);
            item_name = (TextView) itemView.findViewById(R.id.item_name);
            item_short_desc = (TextView) itemView.findViewById(R.id.item_short_desc);
            item_price = (TextView) itemView.findViewById(R.id.item_price);
            remove_item = (TextView) itemView.findViewById(R.id.remove_item);
            item_count = (TextView) itemView.findViewById(R.id.item_count);
            add_item = (TextView) itemView.findViewById(R.id.add_item);
        }
    }
}
