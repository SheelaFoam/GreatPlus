package com.erp.sheelafoam.sheelafoam.xperia.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.erp.sheelafoam.R;
import com.erp.sheelafoam.sheelafoam.xperia.PerfectMatchAdapter;

public class PerfectMatch extends AppCompatActivity {


    TextView textview_back;
    ListView perfectMatchListView;
    android.support.v7.widget.Toolbar toolbar;
    PerfectMatchAdapter perfectMatchAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfect_match);

        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.app_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        textview_back = (TextView) findViewById(R.id.app_toolbar_title);
        textview_back.setText("Perfect Match");

        perfectMatchListView = (ListView) findViewById(R.id.perfect_match_list);
        perfectMatchAdapter = new PerfectMatchAdapter(this, null);
        perfectMatchListView.setAdapter(perfectMatchAdapter);

    }
}
