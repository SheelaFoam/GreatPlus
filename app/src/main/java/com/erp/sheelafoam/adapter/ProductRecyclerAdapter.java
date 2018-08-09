package com.erp.sheelafoam.adapter;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.erp.sheelafoam.R;
import com.erp.sheelafoam.models.ProductCategoryModel;
import com.erp.sheelafoam.sheelafoam.exchangeschame.ProductMainActivity;

import java.util.List;

public class ProductRecyclerAdapter extends RecyclerView.Adapter<ProductRecyclerAdapter.ProductDataHolder> {
    private static final int TITLE = 0;
    private static final int DATA = 1;
    private List<ProductCategoryModel> list;
    private ProductMainActivity productMainActivity;

    public ProductRecyclerAdapter(ProductMainActivity productMainActivity, List<ProductCategoryModel> list) {
        this.list = list;
        this.productMainActivity = productMainActivity;
    }


    @Override
    public ProductDataHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_category, parent, false);
        return new ProductDataHolder(view);
    }

    @Override
    public void onBindViewHolder(final ProductDataHolder holder, int position) {
        final ProductCategoryModel model = getItem(position);
        holder.tvTitleCategory.setText(model.getTitle());
        holder.recyclerView.setAdapter(new ProductItemAdapter(productMainActivity, model.getSalesModels()));
        holder.recyclerView.setVisibility(model.isShown() ? View.VISIBLE : View.GONE);
        holder.tvTitleCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (model.isShown()) {
                    model.setShown(false);
                    holder.recyclerView.setVisibility(View.GONE);
                } else {
                    model.setShown(true);
                    holder.recyclerView.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    private ProductCategoryModel getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class ProductDataHolder extends RecyclerView.ViewHolder {
        TextView tvTitleCategory;
        RecyclerView recyclerView;

        public ProductDataHolder(View itemView) {
            super(itemView);
            tvTitleCategory = (TextView) itemView.findViewById(R.id.tvTitleCategory);
            recyclerView = (RecyclerView) itemView.findViewById(R.id.recyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
            recyclerView.setNestedScrollingEnabled(false);
        }
    }
}
