package com.tomatoo.Main.CategoriesItems;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.tomatoo.MyBase.MyBaseActivity;
import com.tomatoo.PasswordReset.VerifyCodeAfterRegistrateActivity;
import com.tomatoo.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class DeliveryAddressActivity extends MyBaseActivity implements OnMapReadyCallback {

    private Toolbar mToolbar;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    public static final int MY_PERMISSIONS_REQUEST_CODE_LOCATION = 25 ;
    private GoogleMap mMap;
    private TextInputEditText country_et,city_et,area_et,building_et;
    private Button set_location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_address);

        InitializeFields();

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
    }

    private void SetUserLocation() {
        String scountry_et = country_et.getText().toString();
        String scity_et = city_et.getText().toString();
        String sarea_et = area_et.getText().toString();
        String sbuilding_et = building_et.getText().toString();

        if (TextUtils.isEmpty(scountry_et)||TextUtils.isEmpty(scity_et)||TextUtils.isEmpty(sarea_et)||TextUtils.isEmpty(sbuilding_et)) {
            Toast.makeText(this, "Please enter your address details...", Toast.LENGTH_SHORT).show();
            country_et.setError("Country is Required");
            city_et.setError("City is Required");
            area_et.setError("Area is Required");
            building_et.setError("Building is Required");
        }
        else
        {
            // Set User Location
            Intent intent = new Intent(DeliveryAddressActivity.this, VerifyCodeAfterRegistrateActivity.class);
            startActivity(intent);
        }
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
        ShowConfirmationDialog("Reminding","Please Check your GPS if not active","open GPS","I'm already opened it",null,null);
        mMap = googleMap;
        mMap.setOnMyLocationButtonClickListener(onMyLocationButtonClickListener);
        enableMyLocationIfPermitted();

        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setMinZoomPreference(11);
        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {

                if ( flag2== 0)
                {
                    mMap.clear();
                    Geocoder geocoder = new Geocoder(DeliveryAddressActivity.this, Locale.getDefault());
                    // to write current location on mark
                    // to convert lat , lang to name
                    List<Address> addresses = null;


                    try {
                        addresses = geocoder.getFromLocation(location.getLongitude(), location.getLatitude(), 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                    String cityName = addresses.get(0).getAddressLine(0);
                    mMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())).title(cityName));
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
