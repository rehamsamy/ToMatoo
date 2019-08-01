package com.example.khaled_sa2r.tomatoo.LogainAndRegistration;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.khaled_sa2r.tomatoo.Main.MainPageActivity;
import com.example.khaled_sa2r.tomatoo.MyBase.MyBaseActivity;
import com.example.khaled_sa2r.tomatoo.PasswordReset.ForgetPasswordActivity;
import com.example.khaled_sa2r.tomatoo.R;

public class LoginActivity extends MyBaseActivity {
    private TextInputEditText email,password;
    private Button signin,continue_without_login;
    private TextView forget_password,dont_have_an_account,signup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        InitializeFields();

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,RegistrationActivity.class);
                startActivity(intent);
            }
        });

        forget_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ForgetPasswordActivity.class);
                startActivity(intent);
            }
        });

        continue_without_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, MainPageActivity.class);
                startActivity(intent);
            }
        });

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AllowUserToLogin();
            }
        });

    }

    private void AllowUserToLogin()
    {
            String email_login = email.getText().toString();
            String password_login = password.getText().toString();

            if (TextUtils.isEmpty(email_login)) {
                Toast.makeText(this, "Please Enter  Email...", Toast.LENGTH_SHORT).show();
                email.setError("Email is Required");
            }
            if (TextUtils.isEmpty(password_login) || password.length() < 8) {
                Toast.makeText(this, "Required  8 letters at least...", Toast.LENGTH_SHORT).show();
                password.setError("Required 6 letters at least");
            }
            else
            {
                //Allow User to Sign In
                Intent intent = new Intent(LoginActivity.this,MainPageActivity.class);
                startActivity(intent);
            }
    }

        private void InitializeFields () {
            email = (TextInputEditText) findViewById(R.id.email);
            password = (TextInputEditText) findViewById(R.id.password);
            signin = (Button) findViewById(R.id.signin);
            continue_without_login = (Button) findViewById(R.id.continue_without_login);
            forget_password = (TextView) findViewById(R.id.forget_password);
            dont_have_an_account = (TextView) findViewById(R.id.dont_have_an_account);
            signup = (TextView) findViewById(R.id.signup);
        }
    }
