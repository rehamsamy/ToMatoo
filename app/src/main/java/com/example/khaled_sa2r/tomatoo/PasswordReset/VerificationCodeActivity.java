package com.example.khaled_sa2r.tomatoo.PasswordReset;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.khaled_sa2r.tomatoo.Main.MainPageActivity;
import com.example.khaled_sa2r.tomatoo.MyBase.MyBaseActivity;
import com.example.khaled_sa2r.tomatoo.R;

public class VerificationCodeActivity extends MyBaseActivity {

    private TextInputEditText entercode;
    private Button verify;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification_code);


        mToolbar = (Toolbar) findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("    Verification Code");
        mToolbar.setNavigationIcon(R.drawable.back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        entercode = findViewById(R.id.entercode);
        verify = findViewById(R.id.verify);

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckCode();
            }
        });
    }

    private void CheckCode() {
        String code = entercode.getText().toString();
        if (TextUtils.isEmpty(code)||code.length()<6)
        {
            Toast.makeText(this, "Please enter your verification code...", Toast.LENGTH_SHORT).show();
            entercode.setError("Required 6 letters");
        }
    else
        {
            // Check code

            Intent intent = new Intent(VerificationCodeActivity.this,ResetPasswordActivity.class);
            startActivity(intent);
        }
}
}
