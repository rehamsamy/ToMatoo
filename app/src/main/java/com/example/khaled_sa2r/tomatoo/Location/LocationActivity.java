package com.example.khaled_sa2r.tomatoo.Location;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.khaled_sa2r.tomatoo.MyBase.MyBaseActivity;
import com.example.khaled_sa2r.tomatoo.PasswordReset.VerificationCodeActivity;
import com.example.khaled_sa2r.tomatoo.PasswordReset.VerifyCodeAfterRegistrateActivity;
import com.example.khaled_sa2r.tomatoo.R;

public class LocationActivity extends MyBaseActivity {
    private Toolbar mToolbar;
    private TextView location_manually;
     private Button current_location;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        mToolbar = (Toolbar) findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("          Set Location");
        mToolbar.setNavigationIcon(R.drawable.back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        current_location = findViewById(R.id.current_location);
        location_manually = findViewById(R.id.location_manually);

        location_manually.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LocationActivity.this,SetLocationActivity.class);
                startActivity(intent);
            }
        });

        current_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetUserCurrentLocation();
                Intent intent = new Intent(LocationActivity.this, VerifyCodeAfterRegistrateActivity.class);
                startActivity(intent);
            }
        });
    }

    private void GetUserCurrentLocation()
    {

    }
}
