package com.example.khaled_sa2r.tomatoo.MenuActivities.Profile;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.khaled_sa2r.tomatoo.LogainAndRegistration.LoginActivity;
import com.example.khaled_sa2r.tomatoo.Main.MainPageActivity;
import com.example.khaled_sa2r.tomatoo.MyBase.MyBaseActivity;
import com.example.khaled_sa2r.tomatoo.R;

public class ChangePasswordActivity extends MyBaseActivity {
    private Toolbar mToolbar;
    private Button save;
    private TextInputEditText old_password,new_password,confirm_new_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        mToolbar = (Toolbar) findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("       Change Password");
        mToolbar.setNavigationIcon(R.drawable.back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        InitializeFields();

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ChangePassword();
            }
        });
    }

    private void ChangePassword() {

        String sold_password = old_password.getText().toString();
        String snew_password = new_password.getText().toString();
        String Sconfirm_new_password = confirm_new_password.getText().toString();

        if (TextUtils.isEmpty(sold_password)) {
            Toast.makeText(this, "Please enter old password...", Toast.LENGTH_SHORT).show();
            old_password.setError("Old Password is Required");
        }
        if (TextUtils.isEmpty(snew_password) || TextUtils.isEmpty(Sconfirm_new_password)) {
            Toast.makeText(this, "Please enter new password", Toast.LENGTH_SHORT).show();
            new_password.setError("New Password is Required");
        }
        if (snew_password == Sconfirm_new_password) {
            Toast.makeText(this, "Please check confirm password", Toast.LENGTH_SHORT).show();
            confirm_new_password.setError("Matching Passwords are Required");
        }
        else
        {
            // Change User's Password
//            Intent intent = new Intent(ChangePasswordActivity.this,MainPageActivity.class);
//            startActivity(intent);
        }
    }

    private void InitializeFields() {
        save = (Button) findViewById(R.id.save);
        old_password = (TextInputEditText) findViewById(R.id.old_password);
        new_password = (TextInputEditText) findViewById(R.id.new_password);
        confirm_new_password = (TextInputEditText) findViewById(R.id.confirm_new_password);
    }
}
