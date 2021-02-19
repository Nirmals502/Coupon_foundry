package com.couponfoundry.View;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.couponfoundry.R;
import com.couponfoundry.rest.APIInterface;
import com.couponfoundry.rest.Update_token;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;


import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@RequiresApi(api = Build.VERSION_CODES.KITKAT)

public class Welcome_screen extends AppCompatActivity {
    private static final String TAG = Welcome_screen.class.getSimpleName();

    @BindView(R.id.button2)
    Button Btn_register;
    APIInterface apiInterface;
    private String mLastUpdateTime;

    // location updates interval - 10sec
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;

    // fastest updates interval - 5 sec
    // location updates will be received if another app is requesting the locations
    // than your app can handle
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 5000;

    private static final int REQUEST_CHECK_SETTINGS = 100;


    // bunch of location related apis

    // boolean flag to toggle the ui
    private Boolean mRequestingLocationUpdates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wlecome_screen);
        ButterKnife.bind(this);
        if (ContextCompat.checkSelfPermission(Welcome_screen.this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(Welcome_screen.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(Welcome_screen.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            } else {
                ActivityCompat.requestPermissions(Welcome_screen.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }

       // init();
        //  restoreValuesFromBundle(savedInstanceState);

        Update_token update_token = new Update_token();
        update_token.Update_token(this);
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(this, instanceIdResult -> {
            String newToken = instanceIdResult.getToken();
            Log.e("newToken", newToken);
            SharedPreferences prefs = getSharedPreferences("COUPON FOUNDRYY", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("Firebase_token", newToken);
            editor.apply();
        });
//        String token = FirebaseInstanceId.getInstance().getToken();
//        SharedPreferences prefs = getSharedPreferences("COUPON FOUNDRYY", Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = prefs.edit();
//        editor.putString("Firebase_token", token);
//        editor.apply();

//        FirebaseInstanceId.getInstance().getInstanceId()
//                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
//
//
//                    @Override
//                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
//                        if (!task.isSuccessful()) {
////To do//
//                            return;
//                        }
//
//// Get the Instance ID token//
//                        String token = task.getResult().getToken();
//                        //getSharedPreferences("_", MODE_PRIVATE).edit().putString("fcm_token", token).apply();
//                        SharedPreferences prefs = getSharedPreferences("COUPON FOUNDRYY", Context.MODE_PRIVATE);
//                        SharedPreferences.Editor editor = prefs.edit();
//                        editor.putString("Firebase_token", token);
//                        editor.apply();
//                     //   String msg = getString(R.string.fcm_token, token);
//                        Log.d("TAG", token);
//
//                    }
//                });


    }


    @OnClick(R.id.button2)
    void Register() {
        Intent i = new Intent(Welcome_screen.this,
                Register_screen.class);
        startActivity(i);
        finish();

    }

    @OnClick(R.id.button)
    void Login() {
        Intent i = new Intent(Welcome_screen.this,
                Login_screen.class);
        startActivity(i);
        finish();

    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
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

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(Welcome_screen.this,
                            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        //   Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();

                    }
                } else {
                    this.finishAffinity();
                    //  Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }

    }



    /**
     * Update the UI displaying the location data
     * and toggling the buttons
     */


        //Toast.makeText(Welcome_screen.this, (int) mCurrentLocation.getLatitude(),Toast.LENGTH_LONG).show();
        // giving a blink animation on TextView







    /**
     * Starting location updates
     * Check whether location settings are satisfied and then
     * location updates will be requested
     */


}
