package com.example.khaled_sa2r.tomatoo.MenuActivities.Settings;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;

import com.example.khaled_sa2r.tomatoo.MyBase.MyBaseActivity;
import com.example.khaled_sa2r.tomatoo.R;

public class SettingActivity extends MyBaseActivity {
    private Toolbar mToolbar;
    private CardView card_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        mToolbar = (Toolbar) findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("                Setting");
        mToolbar.setNavigationIcon(R.drawable.back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        card_view = (CardView) findViewById(R.id.card_view);
    }
}
