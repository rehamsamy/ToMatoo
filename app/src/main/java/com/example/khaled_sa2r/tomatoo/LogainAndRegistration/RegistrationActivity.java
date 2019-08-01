package com.example.khaled_sa2r.tomatoo.LogainAndRegistration;

import android.content.Intent;
import android.location.Location;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.khaled_sa2r.tomatoo.Location.LocationActivity;
import com.example.khaled_sa2r.tomatoo.MyBase.MyBaseActivity;
import com.example.khaled_sa2r.tomatoo.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegistrationActivity extends MyBaseActivity {
    private Toolbar mToolbar;
    private TextView signin;
    private Button signup;
    private CircleImageView image;
    private TextInputEditText username,email,phone,pass,confirm_pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mToolbar = (Toolbar) findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("             Sign Up");
        mToolbar.setNavigationIcon(R.drawable.back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        InitializeFields();

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegistrationActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AllowUserToSignUp();
            }
        });
    }

    private void AllowUserToSignUp()
    {
        String susername = username.getText().toString();
        String semail= email.getText().toString();
        String sphone= phone.getText().toString();
        String spassword= pass.getText().toString();
        String sconfrim_pass= confirm_pass.getText().toString();

        if (TextUtils.isEmpty(susername)||TextUtils.isEmpty(semail)||TextUtils.isEmpty(sphone)||TextUtils.isEmpty(spassword)||TextUtils.isEmpty(sconfrim_pass)) {
            Toast.makeText(this, "Please fill your data...", Toast.LENGTH_SHORT).show();
            username.setError("User Name is Required");
            email.setError("Email is Required");
            phone.setError("Phone is Required");
            pass.setError("Password is Required");
            confirm_pass.setError("Confirm password is Required");
        }
        else
        {
            // Allow User to Sign Up

            Intent intent = new Intent(RegistrationActivity.this, LocationActivity.class);
            startActivity(intent);
        }
    }

    private void InitializeFields() {
        signin = (TextView) findViewById(R.id.signin);
        signup = (Button) findViewById(R.id.signup);
        username = (TextInputEditText)findViewById(R.id.username);
        email = (TextInputEditText)findViewById(R.id.email);
        phone = (TextInputEditText)findViewById(R.id.phone);
        pass = (TextInputEditText)findViewById(R.id.password);
        confirm_pass = (TextInputEditText)findViewById(R.id.confirm_password);
        image = (CircleImageView) findViewById(R.id.image2);
    }
}
