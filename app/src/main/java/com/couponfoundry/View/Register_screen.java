package com.couponfoundry.View;

import androidx.appcompat.app.AppCompatActivity;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_screen);
        ButterKnife.bind(this);
        apiInterface = APIClient.getClient(this).create(APIInterface.class);
        androidId = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);
        Post_isdeviceexist user = new Post_isdeviceexist("isdeviceexist", androidId);
        Call<Response_isdevice_exist> call1 = apiInterface.isdeviceexist(user);
        avi.show();
        Rlv_avi.setVisibility(View.VISIBLE);
        avi.dispatchWindowFocusChanged(true);

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
                    Toast.makeText(Register_screen.this, "Device does not exist", Toast.LENGTH_LONG).show();
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
        register_device Register = new register_device("register", "5674566665htyt67", Editext_phone.getText().toString(), "bk3RNwTe3H0:CI2k_HHwgIpo");
        Call<Response_isdevice_exist> call1 = apiInterface.Register(Register);
        call1.enqueue(new Callback<Response_isdevice_exist>() {
            @Override
            public void onResponse(Call<Response_isdevice_exist> call, Response<Response_isdevice_exist> response) {

                try {
                    Response_isdevice_exist user1 = response.body();
                    String success = user1.status;
                    if (success.contentEquals("success")) {
                        String str_member = user1.member;
                        Toast.makeText(Register_screen.this, "Registered successfully with member...." + str_member, Toast.LENGTH_LONG).show();
                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(Register_screen.this);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("member", str_member);
                        editor.apply();
                        Activity_log activity_log = new Activity_log();
                        activity_log.Activity_log(Register_screen.this, "new", "register");
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

    @OnClick(R.id.btn_confirm)
    void Reregister() {
        if (Editext_phone.getText().toString().contentEquals("")) {
            Shake_animation shkanim = new Shake_animation();
            Editext_phone.startAnimation(shkanim.shakeError());
            return;
        }
        register_device Register = new register_device("reregister", "5674566665htyt67", Editext_phone.getText().toString(), "bk3RNwTe3H0:CI2k_HHwgIpo");
        Call<Response_isdevice_exist> call1 = apiInterface.Register(Register);
        call1.enqueue(new Callback<Response_isdevice_exist>() {
            @Override
            public void onResponse(Call<Response_isdevice_exist> call, Response<Response_isdevice_exist> response) {

                try {
                    Response_isdevice_exist user1 = response.body();
                    String success = user1.status;
                    if (success.contentEquals("success")) {
                        String str_member = user1.member;
                        Toast.makeText(Register_screen.this, "Registered successfully with member...." + str_member, Toast.LENGTH_LONG).show();
                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(Register_screen.this);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("member", str_member);
                        editor.apply();
                        Activity_log activity_log = new Activity_log();
                        activity_log.Activity_log(Register_screen.this, "new", "reregister");
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
}