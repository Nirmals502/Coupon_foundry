package com.couponfoundry.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.couponfoundry.Model.Post_logout;
import com.couponfoundry.Model.Response_view_offer;
import com.couponfoundry.R;
import com.couponfoundry.rest.APIClient;
import com.couponfoundry.rest.APIInterface;
import com.couponfoundry.rest.Activity_log;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Setting_screen extends AppCompatActivity {
    Button Btn_editlocation;
    Button Btn_logout;
    APIInterface apiInterface;
    ImageView Img_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_screen);
        Btn_editlocation=(Button)findViewById(R.id.button3);
        Btn_logout=(Button)findViewById(R.id.button4);
        Img_back=(ImageView)findViewById(R.id.imageView5) ;
        apiInterface = APIClient.getClient(Setting_screen.this).create(APIInterface.class);
        Btn_editlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Setting_screen.this,
                        MapsActivity.class);
                startActivity(i);
                finish();
            }
        });
        Img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Setting_screen.this,
                        Home_screen.class);
                startActivity(i);
                finish();
            }
        });
        Btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Setting_screen.this);
                builder.setMessage("Are you sure you want to Logout ?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //System.exit(0);
                                String androidId = Settings.Secure.getString(getContentResolver(),
                                        Settings.Secure.ANDROID_ID);
                                Post_logout Logout = new Post_logout("logout", androidId);
                                Call<Response_view_offer> call1 = apiInterface.Logout(Logout);
//                        avi.show();
//                        Rlv_avi.setVisibility(View.VISIBLE);
//                        avi.dispatchWindowFocusChanged(true);

                                call1.enqueue(new Callback<Response_view_offer>() {
                                    @Override
                                    public void onResponse(Call<Response_view_offer> call, Response<Response_view_offer> response) {

                                        try {
//                                    Response_view_offer response_ = response.body();
//                                    avi.hide();
//                                    Rlv_avi.setVisibility(View.GONE);

                                            Activity_log activity_log = new Activity_log();
                                            activity_log.Activity_log(Setting_screen.this, "new", "logout");

                                            SharedPreferences preferences = getSharedPreferences("COUPON FOUNDRY", Context.MODE_PRIVATE);
                                            SharedPreferences.Editor editor = preferences.edit();
                                            editor.clear();
                                            editor.apply();
                                            SharedPreferences pref = getSharedPreferences("Coupon_foundry", 0);
                                            SharedPreferences.Editor editor2 = pref.edit();
                                            editor2.clear();
                                            editor2.apply();
                                            Intent i = new Intent(Setting_screen.this,
                                                    Welcome_screen.class);
                                            startActivity(i);
                                            finish();
                                        } catch (java.lang.NullPointerException e) {
                                            e.printStackTrace();

//                                    avi.hide();
//                                    Rlv_avi.setVisibility(View.GONE);

                                        }

                                    }

                                    @Override
                                    public void onFailure(Call<Response_view_offer> call, Throwable t) {
                                        call.cancel();
                                        Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_SHORT).show();
//                                avi.hide();
//                                Rlv_avi.setVisibility(View.GONE);

                                    }
                                });

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