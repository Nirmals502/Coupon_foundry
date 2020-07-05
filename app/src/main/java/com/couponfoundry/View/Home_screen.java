package com.couponfoundry.View;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
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
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Home_screen extends AppCompatActivity {
    @BindView(R.id.btn_coupons)
    Button Btn_coupons;
    @BindView(R.id.btn_wallet)
    Button Btn_wallet;
    @BindView(R.id.imageView4)
    ImageView Img_logout;
    private PrefManager prefManager;
    APIInterface apiInterface;
    @BindView(R.id.Rlv_avi)
    RelativeLayout Rlv_avi;
    @BindView(R.id.avi)
    AVLoadingIndicatorView avi;

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

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to Logout ?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //System.exit(0);
                        String androidId = Settings.Secure.getString(getContentResolver(),
                                Settings.Secure.ANDROID_ID);
                        Post_logout Logout = new Post_logout("logout", androidId);
                        Call<Response_view_offer> call1 = apiInterface.Logout(Logout);
                        avi.show();
                        Rlv_avi.setVisibility(View.VISIBLE);
                        avi.dispatchWindowFocusChanged(true);

                        call1.enqueue(new Callback<Response_view_offer>() {
                            @Override
                            public void onResponse(Call<Response_view_offer> call, Response<Response_view_offer> response) {

                                try {
                                    Response_view_offer response_ = response.body();
                                    avi.hide();
                                    Rlv_avi.setVisibility(View.GONE);

                                    Activity_log activity_log = new Activity_log();
                                    activity_log.Activity_log(Home_screen.this, "new", "logout");

                                    SharedPreferences preferences =getSharedPreferences("COUPON FOUNDRY",Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.clear();
                                    editor.apply();
                                    Intent i = new Intent(Home_screen.this,
                                            Login_screen.class);
                                    startActivity(i);
                                    finish();
                                } catch (java.lang.NullPointerException e) {
                                    e.printStackTrace();

                                    avi.hide();
                                    Rlv_avi.setVisibility(View.GONE);

                                }

                            }

                            @Override
                            public void onFailure(Call<Response_view_offer> call, Throwable t) {
                                call.cancel();
                                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_SHORT).show();
                                avi.hide();
                                Rlv_avi.setVisibility(View.GONE);

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
}