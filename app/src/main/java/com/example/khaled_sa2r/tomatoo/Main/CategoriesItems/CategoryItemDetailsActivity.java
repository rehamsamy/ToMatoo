package com.example.khaled_sa2r.tomatoo.Main.CategoriesItems;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.khaled_sa2r.tomatoo.Main.CategoriesTabs.CategoriesActivity;
import com.example.khaled_sa2r.tomatoo.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CategoryItemDetailsActivity extends AppCompatActivity {

    private static final String TAG =CategoryItemDetailsActivity.class.getSimpleName() ;
    @BindView(R.id.back_img) ImageView backImg;
    @BindView(R.id.nav_filter_img) ImageView navFilterImg;
    @BindView(R.id.item_name_toolbar) TextView itemNameToolbar;
    @BindView(R.id.item_image) ImageView itemImage;
    @BindView(R.id.item_name_txtV) TextView itemNameTxt;
    @BindView(R.id.item_type_txtV) TextView itemTypeTxt;
    @BindView(R.id.item_price_txtV) TextView itemPriceTxt;
    @BindView(R.id.item_description_txtV) TextView itemDescriptionTxt;
    @BindView(R.id.quantity_value_txtV) TextView itemQuantityValueTxt;
    @BindView(R.id.plus_img) ImageView plusImage;
    @BindView(R.id.minus_img) ImageView minusImage;
    @BindView(R.id.total_price_value_txtV) TextView totalPriceValueTxt;
    @BindView(R.id.add_to_cart_btn)
    Button addToCartBtn;
    int quant;
    int pricePerEachItem;


    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_item_details);
        ButterKnife.bind(this);
        pricePerEachItem= Integer.parseInt(itemPriceTxt.getText().toString());
        totalPriceValueTxt.setText("3$");

    }


    @OnClick(R.id.back_img)
    void onBack(View view){
          startActivity(new Intent(getApplicationContext(),CategoriesActivity.class));
    }

    @OnClick(R.id.plus_img)
    void plus(View view){
        quant= Integer.parseInt(itemQuantityValueTxt.getText().toString());
        int sum=quant+1;
        Log.v(TAG,"qqq"+quant+"       "+sum);
       itemQuantityValueTxt.setText(String.valueOf(sum));
       totalPriceValueTxt.setText(String.valueOf(pricePerEachItem*sum)+"$");
    }

    @OnClick(R.id.minus_img)
    void minus(View view) {
        quant = Integer.parseInt(itemQuantityValueTxt.getText().toString());
        if (quant > 0) {
            int sum = quant - 1;
            Log.v(TAG, "qqq" + sum + "       " + sum);
            itemQuantityValueTxt.setText(String.valueOf(sum));
            totalPriceValueTxt.setText(String.valueOf(sum*pricePerEachItem)+"$");
        }
    }
    @OnClick(R.id.add_to_cart_btn)
    void click(){
        startActivity(new Intent(getApplicationContext(),ShoppingCartActivity.class));
    }
}
