package com.tomatoo.Location;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tomatoo.Connection.ApiClient;
import com.tomatoo.Connection.ApiServiceInterface;
import com.tomatoo.Main.MainPageActivity;
import com.tomatoo.Models.UserModel;
import com.tomatoo.MyBase.MyBaseActivity;
import com.tomatoo.PasswordReset.VerificationCodeActivity;
import com.tomatoo.PasswordReset.VerifyCodeAfterRegistrateActivity;
import com.tomatoo.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.tomatoo.Location.LocationActivity.longitude;

public class SetLocationActivity extends MyBaseActivity implements OnMapReadyCallback {
    private Toolbar mToolbar;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    public static final int MY_PERMISSIONS_REQUEST_CODE_LOCATION = 25;
    private GoogleMap mMap;
    private TextInputEditText country_et, city_et, area_et, building_et;
    private Button set_location;
    Geocoder geocoder;
    private String building_no, floor_no, apartment_no, villa_no, other = "";
    String address, city, state, country, postalCode, knownName = "";
    private double lat, lng;

    @BindView(R.id.location_example)
    TextView currentLocation_txtV;
    @BindView(R.id.setLocation_progress_id)
    ProgressBar progressBar;
    public static UserModel userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_location);
        ButterKnife.bind(this);

        InitializeFields();

        if (getIntent().hasExtra("user_data")) {
            userData = getIntent().getExtras().getParcelable("user_data");
        }

        mToolbar = (Toolbar) findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("            Set Location");
        mToolbar.setNavigationIcon(R.drawable.back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        set_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetUserLocation();
            }
        });
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        geocoder = new Geocoder(this, Locale.getDefault());
    }

    private void SetUserLocation() {
        String scountry_et = country_et.getText().toString();
        String scity_et = city_et.getText().toString();
        String sarea_et = area_et.getText().toString();
        String sbuilding_et = building_et.getText().toString();

        if (TextUtils.isEmpty(scountry_et) || TextUtils.isEmpty(scity_et) || TextUtils.isEmpty(sarea_et) || TextUtils.isEmpty(sbuilding_et)) {
            Toast.makeText(this, "Please enter your address details...", Toast.LENGTH_SHORT).show();
            country_et.setError("Country is Required");
            city_et.setError("City is Required");
            area_et.setError("Area is Required");
            building_et.setError("Building is Required");
            return;
        }

        // Show Progress Dialog
        progressBar.setVisibility(View.VISIBLE);
        ApiServiceInterface serviceInterface = ApiClient.getClient().create(ApiServiceInterface.class);
        Call<UpdateAddressResponse> call = serviceInterface.userAddress(country, state, address, building_no, floor_no, apartment_no, villa_no, other, lat, lng, userData.getId(), userData.getToken());
        call.enqueue(new Callback<UpdateAddressResponse>() {
            @Override
            public void onResponse(Call<UpdateAddressResponse> call, Response<UpdateAddressResponse> response) {
                progressBar.setVisibility(View.GONE);
                if (response.body().getStatus()) {
                    Toast.makeText(SetLocationActivity.this, getString(R.string.get_location_success), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SetLocationActivity.this, MainPageActivity.class);
                    intent.putExtra("user_data", userData);
                    intent.putExtra("user_lat", lat);
                    intent.putExtra("user_lng", lng);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(SetLocationActivity.this, response.body().getMessages(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UpdateAddressResponse> call, Throwable t) {
                t.printStackTrace();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void InitializeFields() {
        set_location = (Button) findViewById(R.id.set_location);
        country_et = (TextInputEditText) findViewById(R.id.country_et);
        city_et = (TextInputEditText) findViewById(R.id.city_et);
        area_et = (TextInputEditText) findViewById(R.id.area_et);
        building_et = (TextInputEditText) findViewById(R.id.building_et);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    int flag2 = 0;   // to don't recover to the defult location

    @Override
    public void onMapReady(GoogleMap googleMap) {
//        ShowConfirmationDialog("Reminding", "Please Check your GPS if not active", "open GPS", "I'm already opened it", null, null);
        mMap = googleMap;
        mMap.setOnMyLocationButtonClickListener(onMyLocationButtonClickListener);
        enableMyLocationIfPermitted();
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setMinZoomPreference(11);
        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {

                lat = location.getLatitude();
                lng = location.getLongitude();
                if (flag2 == 0) {
                    mMap.clear();
                    List<Address> addresses = null;
                    try {
                        addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                        address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                        String stateName = addresses.get(0).getFeatureName();
                        String city = addresses.get(0).getLocality();
                        state = addresses.get(0).getAdminArea();
                        country = addresses.get(0).getCountryName();
                        postalCode = addresses.get(0).getPostalCode();
                        knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL

                        Log.i("address: ", address);
                        Log.i("state: ", state);
                        Log.i("country: ", country);
                        Log.e("cityName: ", knownName);
                        currentLocation_txtV.setText(address);
                        country_et.setText(country);
                        city_et.setText(city);
                        area_et.setText(state);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    mMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())).title(address));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude())));
                    flag2 = 1;
                }
            }
        });
    }

    private void enableMyLocationIfPermitted() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else if (mMap != null) {
            mMap.setMyLocationEnabled(true);
        }
    }

    private GoogleMap.OnMyLocationButtonClickListener onMyLocationButtonClickListener =
            new GoogleMap.OnMyLocationButtonClickListener() {
                @Override
                public boolean onMyLocationButtonClick() {
                    mMap.setMinZoomPreference(15);
                    return false;
                }
            };
}
