package com.tomatoo.Main.CategoriesItems;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import com.tomatoo.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CheckoutActivity extends AppCompatActivity {

    @BindView(R.id.side_arrow_img) ImageView  deliveryAddressArrow;
    @BindView(R.id.confirm_data_btn) Button confirmDataBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.side_arrow_img)
    void click(){
        startActivity(new Intent(getApplicationContext(),DeliveryAddressActivity.class));
    }

    @OnClick(R.id.confirm_data_btn)
    void confirmData(){
        startActivity(new Intent(getApplicationContext(), Checkout2Activity.class));
    }
}
