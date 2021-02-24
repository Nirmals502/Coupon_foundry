package com.couponfoundry.View;

import androidx.fragment.app.FragmentActivity;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.couponfoundry.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Marker marker;
    EditText Edttxt_lat, Edttxtlon;
    String Str_lat = "";
    String Str_lng = "";
    ImageView Img_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapView);

        Edttxt_lat = (EditText) findViewById(R.id.editTextlat);
        Edttxtlon = (EditText) findViewById(R.id.edttxtlon);
        Img_back = (ImageView) findViewById(R.id.imageView5);
        SharedPreferences pref = MapsActivity.this.getSharedPreferences("Coupon_foundry", 0); // 0 - for private mode
        //SharedPreferences.Editor editor = pref.edit();
        Str_lat = pref.getString("Latnew", "novalue");
        // Str_lng = pref.getString("Lngnew", "");
        if (Str_lat.contentEquals("novalue")) {
            Str_lat = pref.getString("Lat", "");
            Str_lng = pref.getString("Lng", "");
        } else {
            Str_lat = pref.getString("Latnew", "");
            Str_lng = pref.getString("Lngnew", "");
        }


        Edttxt_lat.setText(Str_lat);
        Edttxtlon.setText(Str_lng);
        Img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MapsActivity.this,
                        Home_screen.class);
                startActivity(i);
                finish();
            }
        });
        Edttxt_lat.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                SharedPreferences prefs = getApplicationContext().getSharedPreferences("Coupon_foundry", 0);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("Latnew", Edttxt_lat.getText().toString());
//                editor.putString("Lngnew", String.valueOf(latLng.longitude));
//                editor.putString("country_name", address.getCountryName());

                editor.apply();
            }
        });
        Edttxtlon.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                SharedPreferences prefs = getApplicationContext().getSharedPreferences("Coupon_foundry", 0);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("Lngnew", Edttxtlon.getText().toString());
//                editor.putString("Lngnew", String.valueOf(latLng.longitude));
//                editor.putString("country_name", address.getCountryName());

                editor.apply();
            }
        });
        mapFragment.getMapAsync(this);
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
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //Marker marker = new Marker();
        try {
            double dlat = Double.parseDouble(Str_lat);
            double dlng = Double.parseDouble(Str_lng);
            // Add a marker in Sydney and move the camera
            LatLng sydney = new LatLng(dlat, dlng);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        //mMap.setMapStyle()
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
            Geocoder geocoder = new Geocoder(MapsActivity.this, Locale.getDefault());
            try {
                android.location.Address address = geocoder.getFromLocation(dlat, dlng, 1).get(0);
                marker = mMap.addMarker(new MarkerOptions().position(sydney).draggable(false).title(address.getAdminArea()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            // mMap.addMarker(new MarkerOptions().position(sydney).draggable(true).title(""));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(dlat, dlng), 4.0f));
            // Enable the zoom controls for the map
            mMap.getUiSettings().setZoomControlsEnabled(true);
        } catch (java.lang.IllegalArgumentException e) {
            e.printStackTrace();
          //  Toast.makeText(MapsActivity.this,"Invalid Location",Toast.LENGTH_LONG).show();
            SharedPreferences pref = MapsActivity.this.getSharedPreferences("Coupon_foundry", 0); // 0 - for private mode
            Str_lat = pref.getString("Lat", "");
            Str_lng = pref.getString("Lng", "");
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("Latnew", Str_lat);
            editor.putString("Lngnew", Str_lng);
           // editor.putString("country_name", address.getCountryName());

            editor.apply();
            finish();
            startActivity(getIntent());
        }

//        googleMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
//
//
//            @Override
//            public void onMarkerDragStart(Marker marker) {
//            }
//
//            @Override
//            public void onMarkerDrag(Marker marker) {
//
//            }
//
//            @Override
//            public void onMarkerDragEnd(Marker marker) {
//                LatLng latLng = marker.getPosition();
//                Geocoder geocoder = new Geocoder(MapsActivity.this, Locale.getDefault());
//                try {
//                    android.location.Address address = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1).get(0);
//                    //mMap.clear();
//                    //mMap.addMarker(new MarkerOptions().position(sydney).draggable(true).title(address.getAdminArea()));
//                    mMap.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
//                    // mMap.
//                    //  mMap.addMarker(new MarkerOptions().position(latLng).draggable(true).title(address.getAdminArea()));
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                try {
                    marker.setPosition(latLng);
                    Geocoder geocoder = new Geocoder(MapsActivity.this, Locale.getDefault());
                    try {
                        android.location.Address address = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1).get(0);
                        marker.setTitle(address.getAdminArea());
                        Edttxt_lat.setText(String.valueOf(latLng.latitude));
                        Edttxtlon.setText(String.valueOf(latLng.longitude));
                        SharedPreferences prefs = getApplicationContext().getSharedPreferences("Coupon_foundry", 0);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("Latnew", String.valueOf(latLng.latitude));
                        editor.putString("Lngnew", String.valueOf(latLng.longitude));
                        editor.putString("country_name", address.getCountryName());

                        editor.apply();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                } catch (java.lang.NullPointerException e) {
                    e.printStackTrace();
                }
            }
        });

    }
    @Override
    protected void onPause() {
        super.onPause();
        if (isApplicationBroughtToBackground()) {

            SharedPreferences prefs = getApplicationContext().getSharedPreferences("Coupon_foundry", 0);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("Latnew", "");
            editor.putString("Lngnew", "");


            editor.apply();
        } else {

        }
    }

    private boolean isApplicationBroughtToBackground() {
        ActivityManager am = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(this.getPackageName())) {
                return true;
            }

        }
        return false;
    }
}