package com.example.khaled_sa2r.tomatoo.Main.CategoriesItems;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.khaled_sa2r.tomatoo.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PaymentMethodActivity extends AppCompatActivity {

    @BindView(R.id.wallet_add_img) ImageView walletAddImg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_method);
        ButterKnife.bind(this);
    }


    @OnClick(R.id.wallet_add_img)
    void setWalletAddImg(){
        startActivity(new Intent(getApplicationContext(),MyWalletActivity.class));
    }
}
