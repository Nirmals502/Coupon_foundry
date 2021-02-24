package com.couponfoundry.View;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.couponfoundry.Model.Offer_list;
import com.couponfoundry.Model.Post_logout;
import com.couponfoundry.Model.Post_notification;
import com.couponfoundry.Model.Post_offer_list;
import com.couponfoundry.Model.Response_view_offer;
import com.couponfoundry.R;
import com.couponfoundry.Sharedpreference.PrefManager;
import com.couponfoundry.adapter.Coupon_list;
import com.couponfoundry.rest.APIClient;
import com.couponfoundry.rest.APIInterface;
import com.couponfoundry.rest.Activity_log;
import com.couponfoundry.rest.Api_client_get_token;
import com.couponfoundry.rest.Update_token;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Home_screen extends AppCompatActivity {
    private static final String TAG = Welcome_screen.class.getSimpleName();

    @BindView(R.id.btn_coupons)
    Button Btn_coupons;
    @BindView(R.id.btn_wallet)
    Button Btn_wallet;
    @BindView(R.id.imageView4)
    ImageView Img_logout;
    private PrefManager prefManager;
    APIInterface apiInterface;
    private String mLastUpdateTime;
    //    @BindView(R.id.Rlv_avi)
//    RelativeLayout Rlv_avi;
//    @BindView(R.id.avi)
//    AVLoadingIndicatorView avi;
// location updates interval - 1min
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 60000;

    // fastest updates interval - 5 sec
    // location updates will be received if another app is requesting the locations
    // than your app can handle
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 5000;

    private static final int REQUEST_CHECK_SETTINGS = 100;


    // bunch of location related apis
    private FusedLocationProviderClient mFusedLocationClient;
    private SettingsClient mSettingsClient;
    private LocationRequest mLocationRequest;
    private LocationSettingsRequest mLocationSettingsRequest;
    private LocationCallback mLocationCallback;
    private Location mCurrentLocation;

    // boolean flag to toggle the ui
    private Boolean mRequestingLocationUpdates;


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        ButterKnife.bind(this);
        prefManager = new PrefManager(Home_screen.this);
        prefManager.setFirstTimeLaunch(false);
        apiInterface = APIClient.getClient(Home_screen.this).create(APIInterface.class);

        new AsyncTaskRunner().execute();
        init();

    }

    @OnClick(R.id.btn_coupons)
    void Coupon_list() {
        Intent i = new Intent(Home_screen.this,
                View_coupon.class);
        startActivity(i);
        finish();
    }

    @OnClick(R.id.btn_wallet)
    void View_saved_coupons() {
        Intent i = new Intent(Home_screen.this,
                View_my_saved_coupon.class);
        startActivity(i);
        finish();
    }

    @OnClick(R.id.imageView4)
    void Logout() {

//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setMessage("Are you sure you want to Logout ?")
//                .setCancelable(false)
//                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        //System.exit(0);
//                        String androidId = Settings.Secure.getString(getContentResolver(),
//                                Settings.Secure.ANDROID_ID);
//                        Post_logout Logout = new Post_logout("logout", androidId);
//                        Call<Response_view_offer> call1 = apiInterface.Logout(Logout);
////                        avi.show();
////                        Rlv_avi.setVisibility(View.VISIBLE);
////                        avi.dispatchWindowFocusChanged(true);
//
//                        call1.enqueue(new Callback<Response_view_offer>() {
//                            @Override
//                            public void onResponse(Call<Response_view_offer> call, Response<Response_view_offer> response) {
//
//                                try {
////                                    Response_view_offer response_ = response.body();
////                                    avi.hide();
////                                    Rlv_avi.setVisibility(View.GONE);
//
//                                    Activity_log activity_log = new Activity_log();
//                                    activity_log.Activity_log(Home_screen.this, "new", "logout");
//
//                                    SharedPreferences preferences = getSharedPreferences("COUPON FOUNDRY", Context.MODE_PRIVATE);
//                                    SharedPreferences.Editor editor = preferences.edit();
//                                    editor.clear();
//                                    editor.apply();
//                                    Intent i = new Intent(Home_screen.this,
//                                            Welcome_screen.class);
//                                    startActivity(i);
//                                    finish();
//                                } catch (java.lang.NullPointerException e) {
//                                    e.printStackTrace();
//
////                                    avi.hide();
////                                    Rlv_avi.setVisibility(View.GONE);
//
//                                }
//
//                            }
//
//                            @Override
//                            public void onFailure(Call<Response_view_offer> call, Throwable t) {
//                                call.cancel();
//                                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_SHORT).show();
////                                avi.hide();
////                                Rlv_avi.setVisibility(View.GONE);
//
//                            }
//                        });
//
//                    }
//                })
//                .setNegativeButton("No", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        //dialog.cancel();
//                        Intent i = new Intent(Home_screen.this,
//                                MapsActivity.class);
//                        startActivity(i);
//                        finish();
//                    }
//                });
//        AlertDialog alert = builder.create();
//        alert.show();
        Intent i = new Intent(Home_screen.this,
                Setting_screen.class);
        startActivity(i);
        finish();

    }


    private class AsyncTaskRunner extends AsyncTask<String, String, String> {


        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected String doInBackground(String... params) {
            Update_token update_token = new Update_token();
            update_token.Update_token(Home_screen.this);


            return null;
        }


        @Override
        protected void onPostExecute(String result) {
            // execution of result of Long time consuming operation
            Activity_log activity_log = new Activity_log();
            activity_log.Activity_log(Home_screen.this, "new", "home");
        }


    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //System.exit(0);
                        finishAffinity();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    }

    private void init() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mSettingsClient = LocationServices.getSettingsClient(this);

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                // location is received
                mCurrentLocation = locationResult.getLastLocation();
                mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());

                updateLocationUI();
            }
        };

        mRequestingLocationUpdates = false;

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();

        Dexter.withActivity(this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        mRequestingLocationUpdates = true;
                        startLocationUpdates();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        if (response.isPermanentlyDenied()) {
                            // open device settings when the permission is
                            // denied permanently
                            // openSettings();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    private void updateLocationUI() {
        if (mCurrentLocation != null) {
//            txtLocationResult.setText(
//                    "Lat: " + mCurrentLocation.getLatitude() + ", " +
//                            "Lng: " + mCurrentLocation.getLongitude()

            String Strlat = String.valueOf(mCurrentLocation.getLatitude());
            String Strlng = String.valueOf(mCurrentLocation.getLongitude());

            String Country_name = getAddress(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
            System.out.println("Loaction.........................." + mCurrentLocation.getLatitude() + " " + mCurrentLocation.getLongitude() + Country_name);
            //mCurrentLocation.
            SharedPreferences prefs = getApplicationContext().getSharedPreferences("Coupon_foundry", 0);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("Lat", Strlat);
            editor.putString("Lng", Strlng);
            editor.putString("country_name", Country_name);
            editor.apply();

        }
    }

    private void startLocationUpdates() {
        mSettingsClient
                .checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        Log.i(TAG, "All location settings are satisfied.");

                        //    Toast.makeText(getApplicationContext(), "Started location updates!", Toast.LENGTH_SHORT).show();

                        //noinspection MissingPermission
                        mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                                mLocationCallback, Looper.myLooper());
                        if (mCurrentLocation != null) {
//
                            System.out.println("Loaction.........................." + mCurrentLocation.getLatitude() + " " + mCurrentLocation.getLongitude());
                        }

                        //Toast.makeText(Welcome_screen.this, (int) mCurrentLocation.getLatitude(),Toast.LENGTH_LONG).show();
                        // giving a blink animation on TextView


                        // updateLocationUI();
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        int statusCode = ((ApiException) e).getStatusCode();
                        switch (statusCode) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                Log.i(TAG, "Location settings are not satisfied. Attempting to upgrade " +
                                        "location settings ");
                                try {
                                    // Show the dialog by calling startResolutionForResult(), and check the
                                    // result in onActivityResult().
                                    ResolvableApiException rae = (ResolvableApiException) e;
                                    rae.startResolutionForResult(Home_screen.this, REQUEST_CHECK_SETTINGS);
                                } catch (IntentSender.SendIntentException sie) {
                                    Log.i(TAG, "PendingIntent unable to execute request.");
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                String errorMessage = "Location settings are inadequate, and cannot be " +
                                        "fixed here. Fix in Settings.";
                                Log.e(TAG, errorMessage);

                                Toast.makeText(Home_screen.this, errorMessage, Toast.LENGTH_LONG).show();
                        }

                        //updateLocationUI();
                    }
                });

    }

    private String getAddress(double latitude, double longitude) {
        StringBuilder result = new StringBuilder();
        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses.size() > 0) {
                Address address = addresses.get(0);
                // result.append(address.getLocality()).append("\n");
                result.append(address.getCountryName());
            }
        } catch (IOException e) {
            Log.e("tag", e.getMessage());
        }
        return result.toString();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isApplicationBroughtToBackground()) {
            //Toast.makeText(Home_screen.this, "background", Toast.LENGTH_LONG).show();
            SharedPreferences prefs = getApplicationContext().getSharedPreferences("Coupon_foundry", 0);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("Latnew", "");
            editor.putString("Lngnew", "");


            editor.apply();
        } else {
            //Toast.makeText(Home_screen.this, "Foreground", Toast.LENGTH_LONG).show();
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