package com.tomatoo.PasswordReset;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.tomatoo.Connection.ApiClient;
import com.tomatoo.Connection.ApiServiceInterface;
import com.tomatoo.Connection.NetworkAvailable;
import com.tomatoo.Main.MainPageActivity;
import com.tomatoo.Models.SendCodeModel;
import com.tomatoo.MyBase.MyBaseActivity;
import com.tomatoo.R;
import com.tomatoo.utils.DialogUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerificationCodeActivity extends MyBaseActivity {

    private TextInputEditText entercode;
    private Button verify;
    private Toolbar mToolbar;
    private NetworkAvailable networkAvailable;
    private DialogUtil dialogUtil;
    private String code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification_code);


        networkAvailable = new NetworkAvailable(this);
        dialogUtil = new DialogUtil();

        mToolbar = (Toolbar) findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("    Verification Code");
        mToolbar.setNavigationIcon(R.drawable.back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (getIntent().hasExtra("code")) {
            code = getIntent().getStringExtra("code");
            entercode.setText(code);
        }

        entercode = findViewById(R.id.entercode);
        verify = findViewById(R.id.verify);

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (networkAvailable.isNetworkAvailable())
                    CheckCode();
                else
                    Toast.makeText(activity, getString(R.string.error_connection), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void CheckCode() {
        if (TextUtils.isEmpty(entercode.getText().toString())) {
            entercode.setError(getString(R.string.required));
            entercode.requestFocus();
            return;
        }

        final ProgressDialog dialog = dialogUtil.showProgressDialog(VerificationCodeActivity.this, getString(R.string.sending), false);
        // Check code
        ApiServiceInterface apiServiceInterface = ApiClient.getClient().create(ApiServiceInterface.class);
        Call<SendCodeModel> call = apiServiceInterface.sendCode(entercode.getText().toString());
        call.enqueue(new Callback<SendCodeModel>() {
            @Override
            public void onResponse(Call<SendCodeModel> call, Response<SendCodeModel> response) {
                dialog.dismiss();
                if (response.body().getStatus()) {
                    Toast.makeText(VerificationCodeActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(VerificationCodeActivity.this, ResetPasswordActivity.class);
                    intent.putExtra("user_id", response.body().getUser_id());
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(VerificationCodeActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SendCodeModel> call, Throwable t) {
                t.printStackTrace();
                dialog.dismiss();
            }
        });
    }
}
