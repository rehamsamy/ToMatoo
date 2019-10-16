package com.tomatoo.MenuActivities.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.tomatoo.MyBase.MyBaseActivity;
import com.tomatoo.R;

public class WalletActivity extends MyBaseActivity {
    private Toolbar mToolbar;
    private Button recharge_wallet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);

        mToolbar = (Toolbar) findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("              My Wallet");
        mToolbar.setNavigationIcon(R.drawable.back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recharge_wallet = (Button) findViewById(R.id.recharge_wallet);

        recharge_wallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WalletActivity.this,RechargeWalletActivity.class);
                startActivity(intent);
            }
        });
    }
}
