package com.erp.sheelafoam.passbook.model;


import com.erp.sheelafoam.passbook.fragment.BaseFragment;

public class FragmentWithTitle {

    public BaseFragment fragment;
    public String title;
    public FragmentWithTitle(BaseFragment fragment, String title) {
        this.fragment = fragment;
        this.title = title;
    }

}
