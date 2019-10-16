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
import com.tomatoo.LogainAndRegistration.LoginActivity;
import com.tomatoo.Main.MainPageActivity;
import com.tomatoo.Models.SetNewPasswordModel;
import com.tomatoo.MyBase.MyBaseActivity;
import com.tomatoo.R;
import com.tomatoo.utils.DialogUtil;
import com.fourhcode.forhutils.FUtilsValidation;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResetPasswordActivity extends MyBaseActivity {
    private Toolbar mToolbar;
    private TextInputEditText newpassword, confirm_new_password;
    private Button reset_and_login;

    // Vars...
    private String user_id;
    private NetworkAvailable networkAvailable;
    private DialogUtil dialogUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        mToolbar = (Toolbar) findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Change Password");
        mToolbar.setNavigationIcon(R.drawable.back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (getIntent().hasExtra("user_id")) {
            user_id = getIntent().getStringExtra("user_id");
        }

        networkAvailable = new NetworkAvailable(this);
        dialogUtil = new DialogUtil();

        newpassword = findViewById(R.id.newpassword);
        confirm_new_password = findViewById(R.id.confirm_new_password);
        reset_and_login = findViewById(R.id.reset_and_login);

        reset_and_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (networkAvailable.isNetworkAvailable())
                    ResetPassword();
                else
                    Toast.makeText(activity, getString(R.string.error_connection), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void ResetPassword() {

        String password = newpassword.getText().toString();
        String confirm_pass = confirm_new_password.getText().toString();

        if (!FUtilsValidation.isEmpty(newpassword, getString(R.string.required))
                && !FUtilsValidation.isEmpty(confirm_new_password, getString(R.string.required))
                && FUtilsValidation.isPasswordEqual(newpassword, confirm_new_password, getString(R.string.pass_not_equal))
        ) {

            if (TextUtils.isEmpty(confirm_pass) || password.length() < 6) {
                newpassword.setError("Required 6 letters at least");
                newpassword.requestFocus();
                return;
            }

            final ProgressDialog dialog = dialogUtil.showProgressDialog(ResetPasswordActivity.this, getString(R.string.sending), false);

            ApiServiceInterface serviceInterface = ApiClient.getClient().create(ApiServiceInterface.class);
            Call<SetNewPasswordModel> call = serviceInterface.setNewPass(newpassword.getText().toString(), confirm_new_password.getText().toString(), user_id);
            call.enqueue(new Callback<SetNewPasswordModel>() {
                @Override
                public void onResponse(Call<SetNewPasswordModel> call, Response<SetNewPasswordModel> response) {
                    dialog.dismiss();
                    if (response.body().getStatus()) {
                        Toast.makeText(ResetPasswordActivity.this, response.body().getMessages(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ResetPasswordActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(ResetPasswordActivity.this, response.body().getMessages(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<SetNewPasswordModel> call, Throwable t) {
                    t.printStackTrace();
                    dialog.dismiss();
                }
            });
        }
    }
}
