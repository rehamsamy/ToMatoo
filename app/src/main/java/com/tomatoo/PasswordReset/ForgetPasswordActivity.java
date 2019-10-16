package com.tomatoo.PasswordReset;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.tomatoo.Connection.ApiClient;
import com.tomatoo.Connection.ApiServiceInterface;
import com.tomatoo.Connection.NetworkAvailable;
import com.tomatoo.LogainAndRegistration.LoginActivity;
import com.tomatoo.Models.ForgetPassModel;
import com.tomatoo.MyBase.MyBaseActivity;
import com.tomatoo.R;
import com.tomatoo.utils.DialogUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgetPasswordActivity extends MyBaseActivity {
    private Toolbar mToolbar;
    private TextInputEditText email;
    private Button send;
    private NetworkAvailable networkAvailable;
    private DialogUtil dialogUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        networkAvailable = new NetworkAvailable(this);
        dialogUtil = new DialogUtil();

        mToolbar = findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("       Forget Password");
        mToolbar.setNavigationIcon(R.drawable.back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        email = findViewById(R.id.email);
        send = findViewById(R.id.send);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (networkAvailable.isNetworkAvailable())
                    SendVerificationCodeToEmail();
                else
                    Toast.makeText(activity, getString(R.string.error_connection), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void SendVerificationCodeToEmail() {
        String email_login = email.getText().toString();
        if (TextUtils.isEmpty(email_login)) {
            email.setError("Email is Required");
            email.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email_login).matches()) {
            email.setError("enter valid email");
            email.requestFocus();
            return;
        }
        final ProgressDialog dialog = dialogUtil.showProgressDialog(ForgetPasswordActivity.this, getString(R.string.sending), false);
        // Send Verification Code
        ApiServiceInterface apiServiceInterface = ApiClient.getClient().create(ApiServiceInterface.class);
        Call<ForgetPassModel> call = apiServiceInterface.sendPhone(email.getText().toString());
        call.enqueue(new Callback<ForgetPassModel>() {
            @Override
            public void onResponse(Call<ForgetPassModel> call, Response<ForgetPassModel> response) {
                dialog.dismiss();
                if (response.body().getStatus()) {
                    Toast.makeText(ForgetPasswordActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(ForgetPasswordActivity.this, VerificationCodeActivity.class);
                    intent.putExtra("code", response.body().getMobileConfCode());
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(ForgetPasswordActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ForgetPassModel> call, Throwable t) {
                t.printStackTrace();
                dialog.dismiss();
            }
        });

    }
}
