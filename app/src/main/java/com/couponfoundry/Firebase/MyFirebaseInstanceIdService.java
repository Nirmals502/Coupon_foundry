package com.couponfoundry.Firebase;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.couponfoundry.View.Login_screen;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by Belal on 12/8/2017.
 */

//the class extending FirebaseInstanceIdService
public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {


    //this method will be called
    //when the token is generated
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();

        //now we will have the token
        String token = FirebaseInstanceId.getInstance().getToken();
        SharedPreferences prefs =getSharedPreferences("COUPON FOUNDRYY", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("Firebase_token", token);
        editor.apply();

        //for now we are displaying the token in the log
        //copy it as this method is called only when the new token is generated
        //and usually new token is only generated when the app is reinstalled or the data is cleared
        Log.d("MyRefreshedToken....", token);
    }
}