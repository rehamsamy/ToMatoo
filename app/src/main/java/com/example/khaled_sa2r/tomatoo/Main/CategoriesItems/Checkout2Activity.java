package com.example.khaled_sa2r.tomatoo.Main.CategoriesItems;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.khaled_sa2r.tomatoo.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Checkout2Activity extends AppCompatActivity {

    @BindView(R.id.payment_method_arrow_img) ImageView paymentMenthodArrow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout2);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.payment_method_arrow_img)
    void setPaymentMenthodArrow(){
        startActivity(new Intent(getApplicationContext(),PaymentMethodActivity.class));
    }
}
