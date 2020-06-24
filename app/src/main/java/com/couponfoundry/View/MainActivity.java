package com.couponfoundry.View;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;
import android.widget.Toast;

import com.couponfoundry.Firebase.Constants;
import com.couponfoundry.Firebase.MyNotificationManager;
import com.couponfoundry.Model.Post_isdeviceexist;
import com.couponfoundry.Model.Post_to_get_token;
import com.couponfoundry.Model.Response_isdevice_exist;
import com.couponfoundry.Model.response_token;
import com.couponfoundry.R;
import com.couponfoundry.rest.APIClient;
import com.couponfoundry.rest.APIInterface;
import com.couponfoundry.rest.Api_client_get_token;
import com.couponfoundry.rest.Update_token;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@RequiresApi(api = Build.VERSION_CODES.KITKAT)

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.Btn_Register)
    Button Btn_register;
    APIInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Update_token update_token = new Update_token();
        update_token.Update_token(this);

    }



    @OnClick(R.id.Btn_Register)
    void Register() {
        Intent i = new Intent(MainActivity.this,
                Register_screen.class);
        startActivity(i);
        finish();

    }

    @OnClick(R.id.btn_Login)
    void Login() {
        Intent i = new Intent(MainActivity.this,
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
}
