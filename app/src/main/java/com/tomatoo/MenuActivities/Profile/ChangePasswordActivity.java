package com.tomatoo.MenuActivities.Profile;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tomatoo.Connection.ApiClient;
import com.tomatoo.Connection.ApiServiceInterface;
import com.tomatoo.Connection.NetworkAvailable;
import com.tomatoo.LogainAndRegistration.LoginActivity;
import com.tomatoo.Main.MainPageActivity;
import com.tomatoo.Models.ChangePasswordModel;
import com.tomatoo.MyBase.MyBaseActivity;
import com.tomatoo.R;
import com.tomatoo.utils.DialogUtil;
import com.fourhcode.forhutils.FUtilsValidation;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivity extends MyBaseActivity {

    @BindView(R.id.old_password_ed_id)
    EditText oldPassword_ed;
    @BindView(R.id.new_password_ed_id)
    EditText new_password_ed;
    @BindView(R.id.confirm_new_password_ed_id)
    EditText confirm_new_password_ed;

    private NetworkAvailable networkAvailable;
    private DialogUtil dialogUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        ButterKnife.bind(this);

//        mToolbar = (Toolbar) findViewById(R.id.main_page_toolbar);
//        setSupportActionBar(mToolbar);
//        getSupportActionBar().setTitle(getString(R.string.change_pass));
//        mToolbar.setNavigationIcon(R.drawable.back);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        networkAvailable = new NetworkAvailable(this);
        dialogUtil = new DialogUtil();
    }

    @OnClick(R.id.changePass_save_btn)
    void changePassword() {
        if (networkAvailable.isNetworkAvailable()) {
            if (!FUtilsValidation.isEmpty(oldPassword_ed, getString(R.string.required))
                    && !FUtilsValidation.isEmpty(new_password_ed, getString(R.string.required))
                    && !FUtilsValidation.isEmpty(confirm_new_password_ed, getString(R.string.required))
                    && FUtilsValidation.isPasswordEqual(new_password_ed, confirm_new_password_ed, getString(R.string.pass_not_equal))) {

                if (oldPassword_ed.getText().toString().length() < 6) {
                    oldPassword_ed.setError(getResources().getString(R.string.pass_is_weak));
                    oldPassword_ed.requestFocus();
                    return;
                }
                if (new_password_ed.getText().toString().length() < 6) {
                    new_password_ed.setError(getResources().getString(R.string.pass_is_weak));
                    new_password_ed.requestFocus();
                    return;
                }
                //show DialogProgress
                final ProgressDialog dialog = dialogUtil.showProgressDialog(ChangePasswordActivity.this, getString(R.string.updating), false);
                Log.i("token: ", MainPageActivity.userData.getToken());
                Log.i("token: ", MainPageActivity.userData.getId() + "");
                ApiServiceInterface serviceInterface = ApiClient.getClient().create(ApiServiceInterface.class);
                Call<ChangePasswordModel> call = serviceInterface.changePassword(MainPageActivity.userData.getId(), oldPassword_ed.getText().toString(), new_password_ed.getText().toString(), confirm_new_password_ed.getText().toString(), MainPageActivity.userData.getToken());
                call.enqueue(new Callback<ChangePasswordModel>() {
                    @Override
                    public void onResponse(Call<ChangePasswordModel> call, Response<ChangePasswordModel> response) {
                        ChangePasswordModel changePasswordModel = response.body();
                        dialog.dismiss();
                        if (changePasswordModel.getStatus()) {
                            Log.i("status: ", changePasswordModel.getStatus() + "");
                            Toast.makeText(ChangePasswordActivity.this, changePasswordModel.getMessages(), Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(ChangePasswordActivity.this, changePasswordModel.getMessages(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ChangePasswordModel> call, Throwable t) {
                        t.printStackTrace();
                        dialog.dismiss();
                    }
                });
            }
        } else {
            Toast.makeText(this, getResources().getString(R.string.error_connection), Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.changePass_back_txtV_id)
    void goBack() {
        finish();
    }
}
