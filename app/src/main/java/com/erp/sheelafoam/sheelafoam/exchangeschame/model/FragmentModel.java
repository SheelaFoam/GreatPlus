package com.erp.sheelafoam.sheelafoam.exchangeschame.model;


import android.support.v4.app.Fragment;

public class FragmentModel {
    public Fragment getFragment() {
        return fragment;
    }

    public String getTitle() {
        return title;
    }

    private Fragment fragment;
    private String title;
    public FragmentModel(Fragment fragment, String title) {
        this.fragment = fragment;
        this.title = title;
    }


}
