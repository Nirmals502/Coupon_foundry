package com.couponfoundry.View;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.couponfoundry.R;
import com.couponfoundry.Sharedpreference.PrefManager;

public class Splash extends AppCompatActivity {
    private static int SPLASH_SCREEN_TIME_OUT = 2000;
    private PrefManager prefManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        prefManager = new PrefManager(this);
        if (prefManager.isFirstTimeLaunch()) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent i = new Intent(Splash.this,
                            MainActivity.class);
                    startActivity(i);

                    finish();

                }
            }, SPLASH_SCREEN_TIME_OUT);
        }else{
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent i = new Intent(Splash.this,
                            Home_screen.class);
                    startActivity(i);

                    finish();

                }
            }, SPLASH_SCREEN_TIME_OUT);
        }

    }
}

