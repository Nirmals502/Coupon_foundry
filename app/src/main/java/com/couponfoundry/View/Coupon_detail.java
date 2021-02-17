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
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.couponfoundry.Model.Offer_list;
import com.couponfoundry.Model.Post_logout;
import com.couponfoundry.Model.Post_offer_list;
import com.couponfoundry.Model.Post_view_offer;
import com.couponfoundry.Model.Response_view_offer;
import com.couponfoundry.R;
import com.couponfoundry.adapter.Coupon_list;
import com.couponfoundry.rest.APIInterface;
import com.couponfoundry.rest.Activity_log;
import com.couponfoundry.rest.Activity_log_view_offer;
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

public class Coupon_detail extends AppCompatActivity {
    @BindView(R.id.txt_Tittle2)
    TextView Txt_tittle;
    @BindView(R.id.txt_retailer)
    TextView Txt_subtittle;
    @BindView(R.id.Txt_expiry_date)
    TextView Txt_information;
    @BindView(R.id.txt_expiry_date)
    TextView Txt_expiry;
    @BindView(R.id.button_save)
    Button Btn_save;
    @BindView(R.id.logo)
    ImageView img_logo;
    @BindView(R.id.imageView_home)
    ImageView img_home;
    @BindView(R.id.imageView4)
    ImageView img_setting;
    APIInterface apiInterface;
    @BindView(R.id.textView3)
    TextView Tittle;
    @BindView(R.id.txt_program)
    TextView txt_affliate_data;
    @BindView(R.id.imageView5)
    ImageView IImg_back;
    //    @BindView(R.id.avi2)
//    AVLoadingIndicatorView avi;
//    @BindView(R.id.Rlv_avi)
//    RelativeLayout Rlv_avi;
    String offer_id = "";
    String Banner_tittle;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon_detail);
        ButterKnife.bind(this);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                offer_id = null;
                Banner_tittle = null;
            } else {
                offer_id = extras.getString("offerid");
                Banner_tittle = extras.getString("Banner_tittle");
            }
        } else {
            offer_id = (String) savedInstanceState.getSerializable("offerid");
            Banner_tittle = (String) savedInstanceState.getSerializable("Banner_tittle");
        }
        Tittle.setText(Banner_tittle);
        Update_token update_token = new Update_token();
        update_token.Update_token(this);
        apiInterface = Api_client_with_member.getClient(this).create(APIInterface.class);
        Post_view_offer offer_list = new Post_view_offer("view", "xwmcoupon", offer_id);
        Call<Response_view_offer> call1 = apiInterface.View_offer(offer_list);
//        avi.show();
//        Rlv_avi.setVisibility(View.VISIBLE);
//        avi.dispatchWindowFocusChanged(true);

        call1.enqueue(new Callback<Response_view_offer>() {
            @Override
            public void onResponse(Call<Response_view_offer> call, Response<Response_view_offer> response) {

                try {
                    Response_view_offer response_ = response.body();
//                    avi.hide();
//                    Rlv_avi.setVisibility(View.GONE);
                    Txt_tittle.setText(response_.banner_header);
                    Txt_subtittle.setText(response_.banner_subtitle);
                    Txt_information.setText(response_.product_information);
                    Txt_expiry.setText("Expires on " + response_.expiry_date);
                    txt_affliate_data.setText(response_.affiliate_data);
                    if(response_.affiliate_data.contentEquals("")){
                        txt_affliate_data.setVisibility(View.GONE);
                    }
                    String img_logo_ = response_.banner_image;
                    byte[] imageByteArray = Base64.decode(img_logo_, Base64.DEFAULT);

                    Glide.with(Coupon_detail.this)
                            .load(imageByteArray)
                            //.placeholder(R.drawable.coupon)
                            .into(img_logo);
                    Activity_log_view_offer activity_log = new Activity_log_view_offer();
                    activity_log.Activity_log_view_offer(Coupon_detail.this, "new", "viewnewoffer", offer_id);

                } catch (java.lang.NullPointerException e) {
                    e.printStackTrace();

//                    avi.hide();
//                    Rlv_avi.setVisibility(View.GONE);

                }

            }

            @Override
            public void onFailure(Call<Response_view_offer> call, Throwable t) {
                call.cancel();
                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_SHORT).show();
//                avi.hide();
//                Rlv_avi.setVisibility(View.GONE);

            }
        });
    }

    @OnClick(R.id.imageView5)
    void Back() {
//        Intent i = new Intent(Coupon_detail.this,
//                View_coupon.class);
//        startActivity(i);
        finish();
    }

    @OnClick(R.id.button_save)
    void Save() {
        Post_view_offer offer_list = new Post_view_offer("save", "xwmcoupon", offer_id);
        Call<Response_view_offer> call1 = apiInterface.Save_offer(offer_list);
//        avi.show();
//        Rlv_avi.setVisibility(View.VISIBLE);

        call1.enqueue(new Callback<Response_view_offer>() {
            @Override
            public void onResponse(Call<Response_view_offer> call, Response<Response_view_offer> response) {

                try {
                    Response_view_offer response_ = response.body();
//                    avi.hide();
//                    Rlv_avi.setVisibility(View.GONE);
                    Toast.makeText(Coupon_detail.this, response_.status, Toast.LENGTH_SHORT).show();
//                    Intent i = new Intent(Coupon_detail.this,
//                            View_coupon.class);
//                    startActivity(i);
                    finish();
                } catch (java.lang.NullPointerException e) {
                    e.printStackTrace();

//                    avi.hide();
//                    Rlv_avi.setVisibility(View.GONE);

                }

            }

            @Override
            public void onFailure(Call<Response_view_offer> call, Throwable t) {
                call.cancel();
                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_SHORT).show();
//                avi.hide();
//                Rlv_avi.setVisibility(View.GONE);

            }
        });
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
//                        avi.show();
//                        Rlv_avi.setVisibility(View.VISIBLE);
//                        avi.dispatchWindowFocusChanged(true);

                        call1.enqueue(new Callback<Response_view_offer>() {
                            @Override
                            public void onResponse(Call<Response_view_offer> call, Response<Response_view_offer> response) {

                                try {
                                    Response_view_offer response_ = response.body();
//                                    avi.hide();
//                                    Rlv_avi.setVisibility(View.GONE);

                                    Activity_log activity_log = new Activity_log();
                                    activity_log.Activity_log(Coupon_detail.this, "new", "logout");

                                    SharedPreferences preferences = getSharedPreferences("COUPON FOUNDRY", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.clear();
                                    editor.apply();
                                    Intent i = new Intent(Coupon_detail.this,
                                            Login_screen.class);
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

    @OnClick(R.id.imageView_home)
    void Home() {
        Intent i = new Intent(Coupon_detail.this,
                Home_screen.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(Coupon_detail.this,
                View_coupon.class);
        startActivity(i);
        finish();

    }
}