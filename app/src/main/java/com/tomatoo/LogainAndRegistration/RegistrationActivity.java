package com.tomatoo.LogainAndRegistration;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tomatoo.Connection.ApiClient;
import com.tomatoo.Connection.ApiServiceInterface;
import com.tomatoo.Connection.NetworkAvailable;
import com.tomatoo.Location.LocationActivity;
import com.tomatoo.Models.RegisterationModel;
import com.tomatoo.Models.UserModel;
import com.tomatoo.MyBase.MyBaseActivity;
import com.tomatoo.R;
import com.tomatoo.utils.DialogUtil;
import com.fourhcode.forhutils.FUtilsValidation;
import com.google.gson.Gson;
import com.tomatoo.Connection.NetworkAvailable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationActivity extends MyBaseActivity {
    private Toolbar mToolbar;
    private TextView signin;
    private Button signup;
    private ImageView add_userImage, user_imageV;
    private TextInputEditText username, email, phone, pass, confirm_pass;
    @BindView(R.id.signup_btn_id)
    Button signUp_btn;

    String storage_permission[];
    private static final int storage_request_code = 300;
    private static final int image_pick_gallery_code = 500;
    private MultipartBody.Part body = null;
    private NetworkAvailable networkAvailable;
    private UserModel userModel;
    SharedPreferences.Editor user_data_edito;
    private DialogUtil dialogUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        ButterKnife.bind(this);

        mToolbar = (Toolbar) findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Sign Up");
        mToolbar.setNavigationIcon(R.drawable.back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        networkAvailable = new NetworkAvailable(this);
        dialogUtil = new DialogUtil();

        InitializeFields();
        //Storage Permission
        storage_permission = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.enter_from_right, R.anim.exit_out_left);
            }
        });

        add_userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (ActivityCompat.checkSelfPermission(RegistrationActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(RegistrationActivity.this, storage_permission, image_pick_gallery_code);
                    } else {
                        pickGallery();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @OnClick(R.id.signup_btn_id)
    void userSignUp() {
        if (networkAvailable.isNetworkAvailable()) {
            if (!FUtilsValidation.isEmpty(username, getString(R.string.required))
                    && !FUtilsValidation.isEmpty(email, getString(R.string.required))
                    && FUtilsValidation.isValidEmail(email, getString(R.string.enter_valid_email))
                    && !FUtilsValidation.isEmpty(phone, getString(R.string.required))
                    && !FUtilsValidation.isEmpty(pass, getString(R.string.required))
                    && !FUtilsValidation.isEmpty(confirm_pass, getString(R.string.required))
                    && FUtilsValidation.isPasswordEqual(pass, confirm_pass, getString(R.string.pass_not_equal))) {

                if (pass.getText().toString().length() < 6) {
                    pass.setError(getResources().getString(R.string.pass_is_weak));
                    pass.requestFocus();
                    return;
                }

                RequestBody NamePart = RequestBody.create(MultipartBody.FORM, username.getText().toString().trim());

                // Show Progress Dialog
                final ProgressDialog dialog = dialogUtil.showProgressDialog(RegistrationActivity.this, getString(R.string.signing), false);

                signin.setEnabled(false);
                ApiServiceInterface serviceInterface = ApiClient.getClient().create(ApiServiceInterface.class);
                Call<RegisterationModel> call = serviceInterface.Register(NamePart, email.getText().toString(), phone.getText().toString(), pass.getText().toString(), confirm_pass.getText().toString(), "user", body);
                call.enqueue(new Callback<RegisterationModel>() {
                    @Override
                    public void onResponse(Call<RegisterationModel> call, Response<RegisterationModel> response) {
                        userModel = new UserModel();
                        if (response.body().getStatus()) {
                            RegisterationModel.User userData = response.body().getUser();
                            userModel.setId(userData.getId());
                            userModel.setName(userData.getUsername());
                            userModel.setEmail(userData.getEmail());
                            userModel.setPhone(userData.getMobile());
                            userModel.setImage(userData.getUser_image());
                            userModel.setToken(response.body().getToken());

                            // Convert User Data to Gon OBJECT ...
                            Gson gson = new Gson();
                            String user_data = gson.toJson(userModel);
                            user_data_edito = getSharedPreferences(LoginActivity.MY_PREFS_NAME, MODE_PRIVATE).edit();
                            user_data_edito.putString("user_data", user_data);
                            user_data_edito.commit();
                            user_data_edito.apply();

                            dialog.dismiss();
                            Intent intent = new Intent(RegistrationActivity.this, LocationActivity.class);
                            intent.putExtra("user_data", userModel);
                            startActivity(intent);
                            finish();
                        } else {
                            signUp_btn.setEnabled(true);
                            Toast.makeText(RegistrationActivity.this, response.body().getMessages().getUsername() + "\n" +
                                    response.body().getMessages().getEmail() + "\n" + response.body().getMessages().getMobile(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<RegisterationModel> call, Throwable t) {
                        signUp_btn.setEnabled(true);
                        t.printStackTrace();
                        dialog.dismiss();
                    }
                });
            } else {
                Toast.makeText(this, getResources().getString(R.string.error_connection), Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void AllowUserToSignUp() {
        String susername = username.getText().toString();
        String semail = email.getText().toString();
        String sphone = phone.getText().toString();
        String spassword = pass.getText().toString();
        String sconfrim_pass = confirm_pass.getText().toString();

        if (TextUtils.isEmpty(susername) || TextUtils.isEmpty(semail) || TextUtils.isEmpty(sphone) || TextUtils.isEmpty(spassword) || TextUtils.isEmpty(sconfrim_pass)) {
            Toast.makeText(this, "Please fill your data...", Toast.LENGTH_SHORT).show();
            username.setError("User Name is Required");
            email.setError("Email is Required");
            phone.setError("Phone is Required");
            pass.setError("Password is Required");
            confirm_pass.setError("Confirm password is Required");
        } else {
            // Allow User to Sign Up

            Intent intent = new Intent(RegistrationActivity.this, LocationActivity.class);
            startActivity(intent);
        }
    }

    private void InitializeFields() {
        signin = findViewById(R.id.signIn_txtV_id);
//        signup = findViewById(R.id.signup_btn_id);
        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        pass = findViewById(R.id.password);
        confirm_pass = findViewById(R.id.confirm_password);
        add_userImage = findViewById(R.id.add_img_id);
        user_imageV = findViewById(R.id.register_user_img_id);
    }

    private void pickGallery() {
        Intent gallery_intent = new Intent(Intent.ACTION_PICK);
        gallery_intent.setType("image/*");
        startActivityForResult(gallery_intent, image_pick_gallery_code);
    }

    // Handle permission result...
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case image_pick_gallery_code:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    pickGallery();
                else
                    Toast.makeText(this, "permission denied", Toast.LENGTH_SHORT).show();

                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == image_pick_gallery_code) {
                try {
                    android.net.Uri selectedImage = data.getData();
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                    user_imageV.setImageBitmap(bitmap);
                    InputStream is = getContentResolver().openInputStream(data.getData());
                    createMultiPartFile(getBytes(is));

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private byte[] getBytes(InputStream is) throws IOException {
        ByteArrayOutputStream byteBuff = new ByteArrayOutputStream();
        int buffSize = 1024;
        byte[] buff = new byte[buffSize];

        int len = 0;
        while ((len = is.read(buff)) != -1) {
            byteBuff.write(buff, 0, len);
        }
        return byteBuff.toByteArray();
    }

    private void createMultiPartFile(byte[] imageBytes) {
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), imageBytes);
        body = MultipartBody.Part.createFormData("image", "image.jpg", requestFile);
    }

}
