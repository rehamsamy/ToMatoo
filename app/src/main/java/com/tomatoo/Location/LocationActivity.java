package com.tomatoo.Location;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tomatoo.Connection.ApiClient;
import com.tomatoo.Connection.ApiServiceInterface;
import com.tomatoo.LogainAndRegistration.LoginActivity;
import com.tomatoo.LogainAndRegistration.RegistrationActivity;
import com.tomatoo.Main.MainPageActivity;
import com.tomatoo.Models.UserModel;
import com.tomatoo.MyBase.MyBaseActivity;
import com.tomatoo.R;
import com.tomatoo.utils.DialogUtil;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LocationActivity extends MyBaseActivity {
    @BindView(R.id.useCurrentLocation_btn_id)
    Button current_location_btn;
    @BindView(R.id.setLocation_progress_id)
    ProgressBar progressBar;

    private String building_no, floor_no, apartment_no, villa_no, other = "";
    String address, city, state, country, postalCode, knownName = "";
    Geocoder geocoder;
    List<Address> addresses;

    private GpsTracker gpsTracker;
    public static double latitude, longitude;
    public static UserModel userData;
    private final String TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        ButterKnife.bind(this);

        if (getIntent().hasExtra("user_data")) {
            userData = getIntent().getParcelableExtra("user_data");
            Log.i(TAG, userData.getId() + "");
            Log.i(TAG, userData.getToken() + "");
        }
        gpsTracker = new GpsTracker(this);
        geocoder = new Geocoder(this, Locale.getDefault());
        progressBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.bgreen), PorterDuff.Mode.MULTIPLY);
    }

    @OnClick(R.id.useCurrentLocation_btn_id)
    void getCurrentLocation() {

        if (!gpsTracker.canGetLocation()) {
            gpsTracker.showSettingsAlert();
        } else {
            Location location = gpsTracker.getLocation();
            latitude = location.getLatitude();
            longitude = location.getLongitude();
        }
        Log.i("Latt: ", latitude + "");
        Log.i("Lngg: ", longitude + "");
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String stateName = addresses.get(0).getFeatureName();
            state = addresses.get(0).getAdminArea();
            country = addresses.get(0).getCountryName();
            postalCode = addresses.get(0).getPostalCode();
            knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL

            Log.i("address: ", address);
            Log.i("state: ", state);
            Log.i("country: ", country);
            Log.e("cityName: ", knownName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Show Progress Dialog
        progressBar.setVisibility(View.VISIBLE);
        ApiServiceInterface serviceInterface = ApiClient.getClient().create(ApiServiceInterface.class);
        Call<UpdateAddressResponse> call = serviceInterface.userAddress(country, state, address, building_no, floor_no, apartment_no, villa_no, other, latitude, longitude, userData.getId(), userData.getToken());
        call.enqueue(new Callback<UpdateAddressResponse>() {
            @Override
            public void onResponse(Call<UpdateAddressResponse> call, Response<UpdateAddressResponse> response) {
                progressBar.setVisibility(View.GONE);
                if (response.body().getStatus()) {
                    Toast.makeText(LocationActivity.this, getString(R.string.get_location_success), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LocationActivity.this, MainPageActivity.class);
                    intent.putExtra("user_data", userData);
                    intent.putExtra("user_lat", latitude);
                    intent.putExtra("user_lng", longitude);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LocationActivity.this, response.body().getMessages(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UpdateAddressResponse> call, Throwable t) {
                t.printStackTrace();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    @OnClick(R.id.setLocationManually_txtV)
    void setLocationManually() {
        Intent intent = new Intent(LocationActivity.this, SetLocationActivity.class);
        intent.putExtra("user_data", userData);
        startActivity(intent);
        finish();
    }
}
