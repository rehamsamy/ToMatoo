package com.tomatoo.MenuActivities.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.tomatoo.Main.MainPageActivity;
import com.tomatoo.MyBase.MyBaseActivity;
import com.tomatoo.R;
import com.tomatoo.utils.PreferencesHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProfileActivity extends MyBaseActivity {

    @BindView(R.id.profile_user_img_id)
    ImageView profile_image;
    @BindView(R.id.profile_name_txtV_id)
    TextView userName_txtV;
    @BindView(R.id.profile_phone_txtV_id)
    TextView userPhone_txtV;
    @BindView(R.id.profile_email_txtV_id)
    TextView userEmail_txtV;
    @BindView(R.id.profile_edit_btn_id)
    ImageView edit_imgV;
    @BindView(R.id.address_val_txtV_id)
    TextView address_txtV;
    @BindView(R.id.address_eng_icon_imageV_id)
    TextView address_eng_icon_imageV;
    @BindView(R.id.address_arb_icon_imageV_id)
    TextView address_arb_icon_imageV;
    @BindView(R.id.payment_eng_icon_imageV_id)
    TextView payment_eng_icon_imageV;
    @BindView(R.id.payment_arb_icon_imageV_id)
    TextView payment_arb_icon_imageV;
    @BindView(R.id.password_eng_icon_imageV_id)
    TextView password_eng_icon_imageV;
    @BindView(R.id.password_arb_icon_imageV_id)
    TextView password_arb_icon_imageV;
    @BindView(R.id.payment_val_txtV_id)
    TextView payment_txtV;
    @BindView(R.id.password_val_txtV_id)
    TextView password_txtV;
    @BindView(R.id.profile_password_layout_id)
    RelativeLayout password_layout;
    @BindView(R.id.profile_address_layout_id)
    RelativeLayout address_layout;
    @BindView(R.id.profile_payment_layout_id)
    RelativeLayout paymentMethod_layout;

    private Toolbar mToolbar;
    private CardView card_one, card_two, card_three, card_four;

    ImageButton edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        userName_txtV.setText(MainPageActivity.userData.getName());
        userEmail_txtV.setText(MainPageActivity.userData.getEmail());
        userPhone_txtV.setText(MainPageActivity.userData.getPhone());
        address_txtV.setText(MainPageActivity.userData.getAddress());
        Glide.with(this)
                .load(MainPageActivity.userData.getImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL)   // cache both original & resized image
                .placeholder(R.drawable.profile_user_pic)
                .centerCrop()
                .crossFade()
                .into(profile_image);

        if (PreferencesHelper.getSomeStringValue(ProfileActivity.this).equals("ar")) {
            Log.i("lang: ", PreferencesHelper.getSomeStringValue(this));
            address_arb_icon_imageV.setVisibility(View.VISIBLE);
            payment_arb_icon_imageV.setVisibility(View.VISIBLE);
            password_arb_icon_imageV.setVisibility(View.VISIBLE);
            address_eng_icon_imageV.setVisibility(View.GONE);
            payment_eng_icon_imageV.setVisibility(View.GONE);
            password_eng_icon_imageV.setVisibility(View.GONE);
        } else {
            address_arb_icon_imageV.setVisibility(View.GONE);
            payment_arb_icon_imageV.setVisibility(View.GONE);
            password_arb_icon_imageV.setVisibility(View.GONE);
            address_eng_icon_imageV.setVisibility(View.VISIBLE);
            payment_eng_icon_imageV.setVisibility(View.VISIBLE);
            password_eng_icon_imageV.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.profile_edit_btn_id)
    void editProfile() {
        Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.profile_password_layout_id)
    void changePassword() {
        Intent intent = new Intent(ProfileActivity.this, ChangePasswordActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.profile_back_txtV_id)
    void goBack() {
        finish();
    }
}
