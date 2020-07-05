package com.couponfoundry.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.couponfoundry.Animation.Shake_animation;
import com.couponfoundry.Model.Post_isdeviceexist;
import com.couponfoundry.Model.Response_isdevice_exist;
import com.couponfoundry.Model.register_device;
import com.couponfoundry.R;
import com.couponfoundry.rest.APIClient;
import com.couponfoundry.rest.APIInterface;
import com.couponfoundry.rest.Activity_log;
import com.couponfoundry.rest.Update_token;
import com.wang.avi.AVLoadingIndicatorView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Register_screen extends AppCompatActivity {
    APIInterface apiInterface;
    @BindView(R.id.editText2)
    EditText Editext_phone;
    @BindView(R.id.button)
    Button Btn_Register;
    String androidId = "";
    @BindView(R.id.Rlv_already_registered)
    RelativeLayout Rlv_Reregiter_;
    @BindView(R.id.Rlv_otp_)
    RelativeLayout Rlv_otp;
    @BindView(R.id.btn_confirm)
    Button Btn_confirm;
    @BindView(R.id.avi)
    AVLoadingIndicatorView avi;
    @BindView(R.id.Rlv_avi)
    RelativeLayout Rlv_avi;
    String Str_firebase_token="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_screen);
        ButterKnife.bind(this);
        apiInterface = APIClient.getClient(this).create(APIInterface.class);
        Str_firebase_token= Fcm_token(this);
        androidId = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);
      //  androidId = "76gg57645tt8yh67";
        Post_isdeviceexist user = new Post_isdeviceexist("isdeviceexist", androidId);
        Call<Response_isdevice_exist> call1 = apiInterface.isdeviceexist(user);
        avi.show();
        Rlv_avi.setVisibility(View.VISIBLE);

        call1.enqueue(new Callback<Response_isdevice_exist>() {
            @Override
            public void onResponse(Call<Response_isdevice_exist> call, Response<Response_isdevice_exist> response) {

                try {
                    Response_isdevice_exist response_ = response.body();
                    String status = response_.status;
                    if (status.contentEquals("success")) {
                        Rlv_Reregiter_.setVisibility(View.VISIBLE);
                        Rlv_otp.setVisibility(View.GONE);
                        avi.hide();
                        Rlv_avi.setVisibility(View.GONE);

                    }


                } catch (java.lang.NullPointerException e) {
                    e.printStackTrace();
                    //Toast.makeText(Register_screen.this, "Device does not exist", Toast.LENGTH_LONG).show();
                    avi.hide();
                    Rlv_avi.setVisibility(View.GONE);
                }

            }

            @Override
            public void onFailure(Call<Response_isdevice_exist> call, Throwable t) {
                call.cancel();
                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_SHORT).show();


            }
        });

    }

    @OnClick(R.id.button)
    void Register() {
        if (Editext_phone.getText().toString().contentEquals("")) {
            Shake_animation shkanim = new Shake_animation();
            Editext_phone.startAnimation(shkanim.shakeError());
            return;
        }
        avi.show();
        Rlv_avi.setVisibility(View.VISIBLE);

        register_device Register = new register_device("register", androidId, Editext_phone.getText().toString(), Str_firebase_token);
        Call<Response_isdevice_exist> call1 = apiInterface.Register(Register);
        call1.enqueue(new Callback<Response_isdevice_exist>() {
            @Override
            public void onResponse(Call<Response_isdevice_exist> call, Response<Response_isdevice_exist> response) {

                try {
                    Response_isdevice_exist user1 = response.body();
                    String success = user1.status;
                    avi.hide();
                    Rlv_avi.setVisibility(View.GONE);
                    if (success.contentEquals("success")) {
                        String str_member = user1.member;
                        Toast.makeText(Register_screen.this, "Registered successfully with member...." + str_member, Toast.LENGTH_LONG).show();
                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(Register_screen.this);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("member", str_member);
                        editor.apply();
                        Activity_log activity_log = new Activity_log();
                        activity_log.Activity_log(Register_screen.this, "new", "register");
                        Intent i = new Intent(Register_screen.this,
                                Home_screen.class);
                        startActivity(i);
                        finish();
                    }


                } catch (java.lang.NullPointerException e) {
                    e.printStackTrace();
                    avi.hide();
                    Rlv_avi.setVisibility(View.GONE);
                }

            }

            @Override
            public void onFailure(Call<Response_isdevice_exist> call, Throwable t) {
                call.cancel();
                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_SHORT).show();


            }
        });

    }

    @OnClick(R.id.btn_confirm)
    void Reregister() {
        if (Editext_phone.getText().toString().contentEquals("")) {
            Shake_animation shkanim = new Shake_animation();
            Editext_phone.startAnimation(shkanim.shakeError());
            return;
        }
        avi.show();
        Rlv_avi.setVisibility(View.VISIBLE);
        register_device Register = new register_device("reregister", androidId, Editext_phone.getText().toString(), Str_firebase_token);
        Call<Response_isdevice_exist> call1 = apiInterface.Register(Register);
        call1.enqueue(new Callback<Response_isdevice_exist>() {
            @Override
            public void onResponse(Call<Response_isdevice_exist> call, Response<Response_isdevice_exist> response) {

                try {
                    Response_isdevice_exist user1 = response.body();
                    String success = user1.status;
                    avi.hide();
                    Rlv_avi.setVisibility(View.GONE);
                    if (success.contentEquals("success")) {
                        String str_member = user1.member;
                        Toast.makeText(Register_screen.this, "Registered successfully with member...." + str_member, Toast.LENGTH_LONG).show();
                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(Register_screen.this);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("member", str_member);
                        editor.apply();
                        Activity_log activity_log = new Activity_log();
                        activity_log.Activity_log(Register_screen.this, "new", "reregister");
                        Intent i = new Intent(Register_screen.this,
                                Home_screen.class);
                        startActivity(i);
                        finish();
                    }


                } catch (java.lang.NullPointerException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<Response_isdevice_exist> call, Throwable t) {
                call.cancel();
                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_SHORT).show();


            }
        });
    }
    public String Fcm_token(Context ctx) {
        SharedPreferences prefs = getSharedPreferences("COUPON FOUNDRYY", Context.MODE_PRIVATE);
        String Str_tocken = prefs.getString("Firebase_token", "");
        return Str_tocken;
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
