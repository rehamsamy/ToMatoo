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

public class ResetPasswordActivity extends MyBaseActivity {
    private Toolbar mToolbar;
    private TextInputEditText newpassword,confirm_new_password;
    private Button reset_and_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        mToolbar = (Toolbar) findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("     Change Password");
        mToolbar.setNavigationIcon(R.drawable.back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        newpassword = findViewById(R.id.newpassword);
        confirm_new_password = findViewById(R.id.confirm_new_password);
        reset_and_login = findViewById(R.id.reset_and_login);

        reset_and_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResetPassword();
            }
        });
    }

    private void ResetPassword()
    {

        String NewPassword = newpassword.getText().toString();
        String ConfirmNewPaswword = confirm_new_password.getText().toString();

        if (TextUtils.isEmpty(NewPassword)|| newpassword.length() < 8) {
            Toast.makeText(this, "Please enter a new password...", Toast.LENGTH_SHORT).show();
            newpassword.setError("Required 8 letters at least");
        }
        if (TextUtils.isEmpty(ConfirmNewPaswword) || confirm_new_password.length() < 8) {
            Toast.makeText(this, "Please re enter a new password...", Toast.LENGTH_SHORT).show();
            confirm_new_password.setError("Required 8 letters at least");
        }
        else
        {
                  // Reset password

            Intent intent = new Intent(ResetPasswordActivity.this, MainPageActivity.class);
            startActivity(intent);
        }
    }
}
