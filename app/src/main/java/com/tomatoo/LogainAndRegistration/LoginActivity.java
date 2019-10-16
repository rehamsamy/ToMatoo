package com.tomatoo.LogainAndRegistration;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TextInputEditText;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tomatoo.Connection.ApiClient;
import com.tomatoo.Connection.ApiServiceInterface;
import com.tomatoo.Connection.NetworkAvailable;
import com.tomatoo.Location.LocationActivity;
import com.tomatoo.Main.MainPageActivity;
import com.tomatoo.Models.UserLoginModel;
import com.tomatoo.Models.UserModel;
import com.tomatoo.MyBase.MyBaseActivity;
import com.tomatoo.PasswordReset.ForgetPasswordActivity;
import com.tomatoo.R;
import com.tomatoo.utils.DialogUtil;
import com.fourhcode.forhutils.FUtilsValidation;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends MyBaseActivity {
    private EditText email, password;
    private Button signin, continue_without_login;
    private TextView forget_password, signup;

    // put data to shared preferences ...
    SharedPreferences.Editor user_data_edito;
    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;
    public static final String MY_PREFS_NAME = "all_user_data";
    public static final String MY_PREFS_login = "login_data";

    private String TAG = this.getClass().getName();
    private NetworkAvailable networkAvailable;
    private UserModel userModel;
    private DialogUtil dialogUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        networkAvailable = new NetworkAvailable(this);
        dialogUtil = new DialogUtil();

        InitializeFields();

        //   get data from shared preferences ...
        sharedPreferences = getSharedPreferences(MY_PREFS_login, MODE_PRIVATE);
        email.setText(sharedPreferences.getString("email", ""));//"No name defined" is the default value.
        password.setText(sharedPreferences.getString("password", "")); //0 is the default value.

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
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

    private void AllowUserToLogin() {
        String email_login = email.getText().toString();
        String password_login = password.getText().toString();
        if (!FUtilsValidation.isEmpty(email, getString(R.string.required))
                && FUtilsValidation.isValidEmail(email, getString(R.string.enter_valid_email))
                && !FUtilsValidation.isEmpty(password, getString(R.string.required))) {


            if (TextUtils.isEmpty(password_login) || password.length() < 6) {
                password.setError("Required 6 letters at least");
                password.requestFocus();
                return;
            }

            String token = "123456";
//        String token = FirebaseInstanceId.getInstance().getToken();

            final ProgressDialog dialog = dialogUtil.showProgressDialog(LoginActivity.this, getString(R.string.logining), false);
            //Allow User to Sign In
            ApiServiceInterface serviceInterface = ApiClient.getClient().create(ApiServiceInterface.class);
            Call<UserLoginModel> call = serviceInterface.userLogin(email.getText().toString(), password.getText().toString());
            call.enqueue(new Callback<UserLoginModel>() {
                @Override
                public void onResponse(Call<UserLoginModel> call, Response<UserLoginModel> response) {
                    userModel = new UserModel();
                    UserLoginModel userLoginModel = response.body();
                    if (userLoginModel.getStatus()) {
                        UserLoginModel.User user = userLoginModel.getUser();
                        userModel.setId(user.getId());
                        userModel.setName(user.getUsername());
                        userModel.setEmail(user.getEmail());
                        userModel.setPhone(user.getMobile());
                        userModel.setImage(user.getUser_image());
                        userModel.setToken(userLoginModel.getToken());

                        // Convert User Data to Gon OBJECT ...
                        Gson gson = new Gson();
                        String user_data = gson.toJson(userModel);
                        user_data_edito = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                        user_data_edito.putString("user_data", user_data);
                        user_data_edito.commit();
                        user_data_edito.apply();

                        dialog.dismiss();
                        Intent intent = new Intent(LoginActivity.this, MainPageActivity.class);
                        intent.putExtra("user_data", userModel);
                        startActivity(intent);
                        finish();
                    } else {
                        dialog.dismiss();
                        Toast.makeText(LoginActivity.this, userLoginModel.getMessages(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<UserLoginModel> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        }
    }

    private void InitializeFields() {
        email = findViewById(R.id.email_ed_id);
        password = findViewById(R.id.pass_ed_id);
        signin = findViewById(R.id.login_btn_id);
        continue_without_login = findViewById(R.id.continue_without_login);
        forget_password = findViewById(R.id.forget_pass_txtV_id);
        signup = findViewById(R.id.signUp_txtV_id);
    }

    @Override
    protected void onPause() {
        super.onPause();
        editor = sharedPreferences.edit();
        editor.putString("email", email.getText().toString());
        editor.putString("password", password.getText().toString());
        editor.commit();
        editor.apply();
    }
}
