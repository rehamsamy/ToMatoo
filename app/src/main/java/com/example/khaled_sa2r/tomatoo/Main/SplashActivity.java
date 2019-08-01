package com.example.khaled_sa2r.tomatoo.Main;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.khaled_sa2r.tomatoo.LogainAndRegistration.LoginActivity;
import com.example.khaled_sa2r.tomatoo.MyBase.MyBaseActivity;
import com.example.khaled_sa2r.tomatoo.R;

public class SplashActivity extends MyBaseActivity {
    Animation uptodown,downtoup;
    private TextView hello;
    private ImageView image;
    Typeface fontHello;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        hello = findViewById(R.id.hello);
        image = findViewById(R.id.image);

        fontHello = Typeface.createFromAsset(this.getAssets(), "fonts/Cherry.ttf");
        hello.setTypeface(fontHello);

        uptodown = AnimationUtils.loadAnimation(this,R.anim.uptodown);
        downtoup = AnimationUtils.loadAnimation(this,R.anim.downtoup);

        hello.setAnimation(uptodown);
        image.setAnimation(downtoup);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }, 4000);
    }
}
