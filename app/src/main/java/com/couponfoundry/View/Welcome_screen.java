package com.couponfoundry.View;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.couponfoundry.R;
import com.couponfoundry.rest.APIInterface;
import com.couponfoundry.rest.Update_token;
import com.google.firebase.iid.FirebaseInstanceId;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@RequiresApi(api = Build.VERSION_CODES.KITKAT)

public class Welcome_screen extends AppCompatActivity {
    @BindView(R.id.button2)
    Button Btn_register;
    APIInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wlecome_screen);
        ButterKnife.bind(this);
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
}
