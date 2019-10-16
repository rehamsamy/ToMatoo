package com.tomatoo.MenuActivities.Profile;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.tomatoo.Connection.ApiClient;
import com.tomatoo.Connection.ApiServiceInterface;
import com.tomatoo.Connection.NetworkAvailable;
import com.tomatoo.LogainAndRegistration.LoginActivity;
import com.tomatoo.LogainAndRegistration.RegistrationActivity;
import com.tomatoo.Main.MainPageActivity;
import com.tomatoo.Models.UserLoginModel;
import com.tomatoo.MyBase.MyBaseActivity;
import com.tomatoo.R;
import com.tomatoo.utils.DialogUtil;
import com.fourhcode.forhutils.FUtilsValidation;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends MyBaseActivity {


    @BindView(R.id.editProfile_user_img_id)
    ImageView user_ImageV;
    @BindView(R.id.editProfile_username_ed)
    EditText userName_ed;
    @BindView(R.id.editProfile_email_ed)
    EditText email_ed;
    @BindView(R.id.editProfile_phone_ed)
    EditText phone_ed;
    @BindView(R.id.editProfile_save_btn_id)
    Button save_btn;

    private Toolbar mToolbar;
    Uri image_uri;
    MultipartBody.Part body = null;
    private final String TAG = this.getClass().getSimpleName();
    private NetworkAvailable networkAvailable;
    private DialogUtil dialogUtil;

    String storage_permission[];
    private static final int image_pick_gallery_code = 330;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        ButterKnife.bind(this);

//        mToolbar = findViewById(R.id.main_page_toolbar);
//        setSupportActionBar(mToolbar);
//        getSupportActionBar().setTitle("       Profile - Edit data");
//        mToolbar.setNavigationIcon(R.drawable.back);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        networkAvailable = new NetworkAvailable(this);
        dialogUtil = new DialogUtil();
        //Storage Permission
        storage_permission = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};

        userName_ed.setText(MainPageActivity.userData.getName());
        email_ed.setText(MainPageActivity.userData.getEmail());
        phone_ed.setText(MainPageActivity.userData.getPhone());

        userName_ed.setInputType(0);
        userName_ed.setEnabled(false);
    }

    @OnClick(R.id.editProfile_save_btn_id)
    void saveData() {
        if (networkAvailable.isNetworkAvailable()) {
            if (!FUtilsValidation.isEmpty(userName_ed, getString(R.string.required))
                    && !FUtilsValidation.isEmpty(email_ed, getString(R.string.required))
                    && FUtilsValidation.isValidEmail(email_ed, getString(R.string.enter_valid_email))
                    && !FUtilsValidation.isEmpty(phone_ed, getString(R.string.required))) {

                Log.i("name: ", userName_ed.getText().toString());
                Log.i("email: ", email_ed.getText().toString());
                Log.i("phone: ", phone_ed.getText().toString());
                Log.i("id: ", MainPageActivity.userData.getId() + "");
                Log.i("token: ", MainPageActivity.userData.getToken());

                // Show Progress Dialog...
                final ProgressDialog dialog = dialogUtil.showProgressDialog(EditProfileActivity.this, getString(R.string.updating), false);

                RequestBody NamePart = RequestBody.create(MultipartBody.FORM, userName_ed.getText().toString().trim());
                ApiServiceInterface serviceInterface = ApiClient.getClient().create(ApiServiceInterface.class);
                Call<UserLoginModel> call = serviceInterface.updateProfile(NamePart, email_ed.getText().toString(), phone_ed.getText().toString(), MainPageActivity.userData.getToken(), MainPageActivity.userData.getId(), body);
                call.enqueue(new Callback<UserLoginModel>() {
                    @Override
                    public void onResponse(Call<UserLoginModel> call, Response<UserLoginModel> response) {
                        UserLoginModel userLoginModel = response.body();
                        dialog.dismiss();
                        if (userLoginModel.getStatus()) {
                            UserLoginModel.User user = userLoginModel.getUser();
                            MainPageActivity.userData.setName(user.getUsername());
                            MainPageActivity.userData.setEmail(user.getEmail());
                            MainPageActivity.userData.setPhone(user.getMobile());
                            if (body != null) {
                                MainPageActivity.userData.setImage(user.getUser_image());
                            }
                            Toast.makeText(EditProfileActivity.this, userLoginModel.getMessages(), Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(EditProfileActivity.this, userLoginModel.getMessages(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<UserLoginModel> call, Throwable t) {
                        dialog.dismiss();
                        t.printStackTrace();
                    }
                });
            }
        } else {
            Toast.makeText(this, getResources().getString(R.string.error_connection), Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.editProfile_back_txtV_id)
    void goBack() {
        finish();
    }

    @OnClick(R.id.editProfile_add_img_id)
    void addUserImage() {
        try {
            if (ActivityCompat.checkSelfPermission(EditProfileActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(EditProfileActivity.this, storage_permission, image_pick_gallery_code);
            } else {
                pickGallery();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                    user_ImageV.setImageBitmap(bitmap);
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
