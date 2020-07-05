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
import android.provider.Settings;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.couponfoundry.Model.Offer_list;
import com.couponfoundry.Model.Post_logout;
import com.couponfoundry.Model.Post_notification;
import com.couponfoundry.Model.Post_offer_list;
import com.couponfoundry.Model.Response_view_offer;
import com.couponfoundry.R;
import com.couponfoundry.adapter.Coupon_list;
import com.couponfoundry.rest.APIInterface;
import com.couponfoundry.rest.Activity_log;
import com.couponfoundry.rest.Api_client_with_member;
import com.couponfoundry.rest.Update_token;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class View_my_saved_coupon extends AppCompatActivity {
    APIInterface apiInterface;
    @BindView(R.id.list)
    ListView Lv_list;
    @BindView(R.id.imageView_home)
    ImageView Btn_home;
    @BindView(R.id.imageView4)
    ImageView Btn_setting;
    @BindView(R.id.Rlv_avi)
    RelativeLayout Rlv_avi;
    @BindView(R.id.avi)
    AVLoadingIndicatorView avi;
    int int_size = 0;


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_my_saved_coupon);
        ButterKnife.bind(this);
        Update_token update_token = new Update_token();
        update_token.Update_token(this);
        apiInterface = Api_client_with_member.getClient(this).create(APIInterface.class);
        Post_offer_list offer_list = new Post_offer_list("list", "xwmcoupon");
        Call<Offer_list> call1 = apiInterface.View_Save_offer(offer_list);
        avi.show();
        Rlv_avi.setVisibility(View.VISIBLE);
        avi.dispatchWindowFocusChanged(true);

        call1.enqueue(new Callback<Offer_list>() {
            @Override
            public void onResponse(Call<Offer_list> call, Response<Offer_list> response) {

                try {
                    Offer_list response_ = response.body();
                    avi.hide();
                    Rlv_avi.setVisibility(View.GONE);
                    ArrayList<Offer_list.Datum> offerlist = response_.offers;
                    Coupon_list adapter = new Coupon_list(View_my_saved_coupon.this, offerlist, "OPEN");
                    Lv_list.setAdapter(adapter);

                    int_size = offerlist.size();
                    String Str_size = String.valueOf(int_size);
                    if (Str_size.contentEquals("0")) {
                        return;
                    }
                    Post_notification Push_notification = new Post_notification("send", "Offers are Available", "You have " + Str_size + " Saved Offers");
                    Call<Response_view_offer> call_push = apiInterface.Notification(Push_notification);
                    avi.show();
                    Rlv_avi.setVisibility(View.VISIBLE);
                    avi.dispatchWindowFocusChanged(true);

                    call_push.enqueue(new Callback<Response_view_offer>() {
                        @Override
                        public void onResponse(Call<Response_view_offer> call, Response<Response_view_offer> response) {

                            try {
                                Response_view_offer response_ = response.body();
                                avi.hide();
                                Rlv_avi.setVisibility(View.GONE);

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
                    Activity_log activity_log = new Activity_log();
                    activity_log.Activity_log(View_my_saved_coupon.this, "new", "myoffers");
                } catch (java.lang.NullPointerException e) {
                    e.printStackTrace();

                    avi.hide();
                    Rlv_avi.setVisibility(View.GONE);

                }

            }

            @Override
            public void onFailure(Call<Offer_list> call, Throwable t) {
                call.cancel();
                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_SHORT).show();
                avi.hide();
                Rlv_avi.setVisibility(View.GONE);

            }
        });
    }

    @OnClick(R.id.imageView_home)
    void Home() {
        Intent i = new Intent(View_my_saved_coupon.this,
                Home_screen.class);
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
                                    activity_log.Activity_log(View_my_saved_coupon.this, "new", "logout");

                                    SharedPreferences preferences = getSharedPreferences("COUPON FOUNDRY", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.clear();
                                    editor.apply();
                                    Intent i = new Intent(View_my_saved_coupon.this,
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