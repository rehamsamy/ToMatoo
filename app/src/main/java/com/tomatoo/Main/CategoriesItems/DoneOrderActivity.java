package com.tomatoo.Main.CategoriesItems;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.tomatoo.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class DoneOrderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_done_order);
        ButterKnife.bind(this);

    }


    @OnClick(R.id.back_to_shop_btn)
    public void back_ToShop() {
        finish();
    }
}
