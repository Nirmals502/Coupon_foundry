package com.couponfoundry.View;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.couponfoundry.Model.Post_isdeviceexist;
import com.couponfoundry.Model.Response_isdevice_exist;
import com.couponfoundry.R;
import com.couponfoundry.rest.APIClient;
import com.couponfoundry.rest.APIInterface;
import com.couponfoundry.rest.Activity_log;
import com.wang.avi.AVLoadingIndicatorView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login_screen extends AppCompatActivity {
    APIInterface apiInterface;
    String androidId = "";
    @BindView(R.id.editText2)
    EditText Editext_phone;
    @BindView(R.id.button)
    Button Btn_login;
    @BindView(R.id.Rlv_avi)
    RelativeLayout Rlv_avi;
    @BindView(R.id.avi)
    AVLoadingIndicatorView avi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        ButterKnife.bind(this);
        apiInterface = APIClient.getClient(this).create(APIInterface.class);
        androidId = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);
    }

    @OnClick(R.id.button)
    void Login() {
        avi.show();
        Rlv_avi.setVisibility(View.VISIBLE);
        Post_isdeviceexist user = new Post_isdeviceexist("login", androidId);
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
                        avi.hide();
                        Rlv_avi.setVisibility(View.GONE);
                        String str_member = response_.member;
                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(Login_screen.this);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("member", str_member);
                        editor.apply();
                        Activity_log activity_log = new Activity_log();
                        activity_log.Activity_log(Login_screen.this, "new", "login");

                    }


                } catch (java.lang.NullPointerException e) {
                    e.printStackTrace();
                    Toast.makeText(Login_screen.this, "Device does not exist", Toast.LENGTH_LONG).show();
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
}