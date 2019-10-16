package com.tomatoo.Main.CategoriesItems;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.tomatoo.Connection.NetworkAvailable;
import com.tomatoo.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PaymentMethodActivity extends AppCompatActivity {

    @BindView(R.id.wallet_add_img)
    ImageView walletAddImg;

    private final String TAG = this.getClass().getSimpleName();
    private int paymentMethod_state = 1;
    private NetworkAvailable networkAvailable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_method);
        ButterKnife.bind(this);
    }


    @OnClick(R.id.wallet_add_img)
    void setWalletAddImg() {
        startActivity(new Intent(getApplicationContext(), MyWalletActivity.class));
    }

    public void onRadioPaymentClicked(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.wallet_radio_btn:
                paymentMethod_state = 1;
//                cardLayoutInfo.setEnabled(false);
//                cardLayoutInfo.setVisibility(View.INVISIBLE);
                break;
            case R.id.cash_on_delivery_radio_btn:
                paymentMethod_state = 2;
//                cardLayoutInfo.setEnabled(false);
//                cardLayoutInfo.setVisibility(View.INVISIBLE);
                break;
            case R.id.online_credit_radio_btn:
                paymentMethod_state = 3;
//                cardLayoutInfo.setEnabled(false);
//                cardLayoutInfo.setVisibility(View.INVISIBLE);
                break;
        }
    }
}
