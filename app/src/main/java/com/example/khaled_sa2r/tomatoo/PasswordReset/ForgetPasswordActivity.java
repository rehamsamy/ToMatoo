package com.example.khaled_sa2r.tomatoo.PasswordReset;

import android.app.ActionBar;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.khaled_sa2r.tomatoo.MyBase.MyBaseActivity;
import com.example.khaled_sa2r.tomatoo.R;

public class ForgetPasswordActivity extends MyBaseActivity {
    private Toolbar mToolbar;
    private TextInputEditText email;
    private Button send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);


        mToolbar = (Toolbar) findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("       Forget Password");
        mToolbar.setNavigationIcon(R.drawable.back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);





        email = findViewById(R.id.email);
        send = findViewById(R.id.send);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendVerificationCodeToEmail();
            }
        });

    }

    private void SendVerificationCodeToEmail() {
        String email_login = email.getText().toString();
        if (TextUtils.isEmpty(email_login)) {
            Toast.makeText(this, "Please enter email...", Toast.LENGTH_SHORT).show();
            email.setError("Email is Required");
        }
        else
        {
            // Send Verification Code

            Intent intent = new Intent(ForgetPasswordActivity.this,VerificationCodeActivity.class);
            startActivity(intent);
        }
    }
}
