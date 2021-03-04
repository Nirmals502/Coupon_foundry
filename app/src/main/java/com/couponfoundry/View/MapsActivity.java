package com.couponfoundry.View;

import androidx.annotation.NonNull;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.couponfoundry.R;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Marker marker;
    EditText Edttxt_lat, Edttxtlon;
    String Str_lat = "";
    String Str_lng = "";
    ImageView Img_back;
    Button Btn_search;
    PlacesClient placesClient;
    AutocompleteSupportFragment autocompleteFragment;

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
        Btn_search = (Button) findViewById(R.id.button_search);
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
        String apiKey = getString(R.string.api_key);
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), apiKey);
        }

        // Create a new Places client instance.
        placesClient = Places.createClient(this);
        autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        autocompleteFragment.setTypeFilter(TypeFilter.GEOCODE);

        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG));
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                // TODO: Get info about the selected place.

                try {
                    LatLng Lat_LNG = place.getLatLng();
                    String Adress = place.getAddress();

                    marker.setPosition(Lat_LNG);
                    marker.setTitle(Adress);
                    marker.showInfoWindow();
                    // mMap.moveCamera(CameraUpdateFactory.newLatLng(Lat_LNG));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Lat_LNG.latitude, Lat_LNG.longitude), 8.0f));
                    // Enable the zoom controls for the map
                    mMap.getUiSettings().setZoomControlsEnabled(true);
                    String Str_lat = String.valueOf(Lat_LNG.latitude);
                    String Str_lng = String.valueOf(Lat_LNG.longitude);
                    Edttxt_lat.setText(Str_lat);
                    Edttxtlon.setText(Str_lng);
                    Geocoder geocoder = new Geocoder(MapsActivity.this, Locale.getDefault());
                    android.location.Address address = geocoder.getFromLocation(Lat_LNG.latitude, Lat_LNG.longitude, 1).get(0);
                    String country = address.getCountryName();
                    SharedPreferences prefs = getApplicationContext().getSharedPreferences("Coupon_foundry", 0);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("Latnew", Edttxt_lat.getText().toString());
                    editor.putString("Lngnew", Edttxtlon.getText().toString());
                    editor.putString("country_name", country);

                    editor.apply();
                } catch (NullPointerException | IOException e) {
                    e.printStackTrace();
                }
                //Toast.makeText(getApplicationContext(), place.getName(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(@NonNull Status status) {
                // TODO: Handle the error.
                Toast.makeText(getApplicationContext(), status.toString(), Toast.LENGTH_SHORT).show();
            }
        });

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
//                SharedPreferences prefs = getApplicationContext().getSharedPreferences("Coupon_foundry", 0);
//                SharedPreferences.Editor editor = prefs.edit();
//                editor.putString("Latnew", Edttxt_lat.getText().toString());
////                editor.putString("Lngnew", String.valueOf(latLng.longitude));
////                editor.putString("country_name", address.getCountryName());
//
//                editor.apply();
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
//                SharedPreferences prefs = getApplicationContext().getSharedPreferences("Coupon_foundry", 0);
//                SharedPreferences.Editor editor = prefs.edit();
//                editor.putString("Lngnew", Edttxtlon.getText().toString());
////                editor.putString("Lngnew", String.valueOf(latLng.longitude));
////                editor.putString("country_name", address.getCountryName());
//
//                editor.apply();
            }
        });
        Btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Edttxt_lat.getText().toString().contentEquals("") || !Edttxtlon.getText().toString().contentEquals("")) {
//
                    try {
                        try {
                            double dlat = Double.parseDouble(Edttxt_lat.getText().toString());
                            double dlng = Double.parseDouble(Edttxtlon.getText().toString());
                            LatLng latlngg = new LatLng(dlat, dlng);
                            marker.setPosition(latlngg);
                            Geocoder geocoder = new Geocoder(MapsActivity.this, Locale.getDefault());
                            try {
                                try {
                                    android.location.Address address = geocoder.getFromLocation(latlngg.latitude, latlngg.longitude, 1).get(0);
                                    String addressstr = address.getAddressLine(0); //0 to obtain first possible address
                                    String city = address.getLocality();
                                    String state = address.getAdminArea();
                                    String country = address.getCountryName();
                                    String postalCode = address.getPostalCode();
                                    String title = addressstr + "-" + city + "-" + state + "-" + country + "-" + postalCode;
                                    marker.setTitle(title);
                                    marker.showInfoWindow();
                                    autocompleteFragment.setText(title);
                                    try {
                                        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                                        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                                    } catch (Exception e) {
                                        // TODO: handle exception
                                    }

                                    SharedPreferences prefs = getApplicationContext().getSharedPreferences("Coupon_foundry", 0);
                                    SharedPreferences.Editor editor = prefs.edit();
                                    editor.putString("Latnew", Edttxt_lat.getText().toString());
                                    editor.putString("Lngnew", Edttxtlon.getText().toString());
                                    editor.putString("country_name", address.getCountryName());

                                    editor.apply();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                // mMap.animateCamera(CameraUpdateFactory.newLatLng(latlngg));
                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latlngg.latitude, latlngg.longitude), 8.0f));
                            } catch (IndexOutOfBoundsException e) {
                                e.printStackTrace();
                            }
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                            Toast.makeText(MapsActivity.this, "Invalid LatLng", Toast.LENGTH_LONG).show();
                        }
                    } catch (java.lang.NullPointerException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(MapsActivity.this, "Lattitude and Longitude is invalid", Toast.LENGTH_LONG).show();
                }

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
                try {
                    android.location.Address address = geocoder.getFromLocation(dlat, dlng, 1).get(0);
                    marker = mMap.addMarker(new MarkerOptions().position(sydney).draggable(false).title(address.getAdminArea()));
                    String addressstr = address.getAddressLine(0); //0 to obtain first possible address
                    String city = address.getLocality();
                    String state = address.getAdminArea();
                    String country = address.getCountryName();
                    String postalCode = address.getPostalCode();
                    String title = addressstr + "-" + city + "-" + state + "-" + country + "-" + postalCode;
                    marker.setTitle(title);
                    marker.isInfoWindowShown();
                    autocompleteFragment.setText(title);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (java.lang.IndexOutOfBoundsException e) {
                e.printStackTrace();
            }
            // mMap.addMarker(new MarkerOptions().position(sydney).draggable(true).title(""));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(dlat, dlng), 8.0f));
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
                        try {
                            android.location.Address address = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1).get(0);
                            String addressstr = address.getAddressLine(0); //0 to obtain first possible address
                            String city = address.getLocality();
                            String state = address.getAdminArea();
                            String country = address.getCountryName();
                            String postalCode = address.getPostalCode();
                            String title = addressstr + "-" + city + "-" + state + "-" + country + "-" + postalCode;
                            marker.setTitle(title);
                            marker.isInfoWindowShown();
                            autocompleteFragment.setText(title);
                            // marker.setTitle(address.getAdminArea());
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
                    } catch (java.lang.IndexOutOfBoundsException e) {
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