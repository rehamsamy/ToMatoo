package com.example.khaled_sa2r.tomatoo.Main.CategoriesItems;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import com.example.khaled_sa2r.tomatoo.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyWalletActivity extends AppCompatActivity {

    @BindView(R.id.recharger_btn) Button rechargeBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_wallet);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.recharger_btn)
    void setRechargeBtn(){
        startActivity(new Intent(getApplicationContext(),DoneOrderActivity.class));
    }
}
