package com.erp.sheelafoam.sheelafoam.exchangeschame.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.erp.sheelafoam.R;
import com.erp.sheelafoam.sheelafoam.exchangeschame.ProductInfoTabActivity;

public class ConsumerDetailFragment extends Fragment {
    private Button btnNext;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_consumer_details, container, false);
        btnNext = (Button) view.findViewById(R.id.btnNext);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ProductInfoTabActivity) getActivity()).viewPager.setCurrentItem(1);
            }
        });
        return view;
    }
}
