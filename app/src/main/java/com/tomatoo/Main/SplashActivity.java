package com.tomatoo.Main;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tomatoo.Location.SetLocationActivity;
import com.tomatoo.LogainAndRegistration.LoginActivity;
import com.tomatoo.Models.UserModel;
import com.tomatoo.MyBase.MyBaseActivity;
import com.tomatoo.R;
import com.tomatoo.utils.PreferencesHelper;

import java.util.Locale;

public class SplashActivity extends MyBaseActivity {
    Animation uptodown, downtoup;
    private TextView hello;
    private ImageView image;
    Typeface fontHello;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        hello = findViewById(R.id.hello);
        image = findViewById(R.id.image);

        fontHello = Typeface.createFromAsset(this.getAssets(), "fonts/Cherry.ttf");
        hello.setTypeface(fontHello);

        uptodown = AnimationUtils.loadAnimation(this, R.anim.uptodown);
        downtoup = AnimationUtils.loadAnimation(this, R.anim.downtoup);

        hello.setAnimation(uptodown);
        image.setAnimation(downtoup);

        setConfig(PreferencesHelper.getSomeStringValue(SplashActivity.this), SplashActivity.this);

        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences prefs = getSharedPreferences(LoginActivity.MY_PREFS_NAME, MODE_PRIVATE);
                String user_data = prefs.getString("user_data", "");

                if (user_data != null && !user_data.equals("")) {
                    // Retrive Gson Object from Shared Prefernces ....
                    Gson gson = new Gson();
                    UserModel userModel = gson.fromJson(user_data, UserModel.class);
                    Intent intent = new Intent(SplashActivity.this, MainPageActivity.class);
                    intent.putExtra("user_data", userModel);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.enter_from_right, R.anim.exit_out_left);
                } else {
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.enter_from_right, R.anim.exit_out_left);
                }
            }
        }, 4000);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }

    public void setConfig(String language, Context context) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
    }
}
