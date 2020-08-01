package com.couponfoundry.View;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.couponfoundry.Model.Post_logout;
import com.couponfoundry.Model.Post_view_offer;
import com.couponfoundry.Model.Response_view_offer;
import com.couponfoundry.R;
import com.couponfoundry.rest.APIInterface;
import com.couponfoundry.rest.Activity_log;
import com.couponfoundry.rest.Activity_log_view_offer;
import com.couponfoundry.rest.Api_client_with_member;
import com.couponfoundry.rest.Update_token;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Redeem_screen extends AppCompatActivity {
    @BindView(R.id.Txt_program)
    TextView Txt_tittle;
    @BindView(R.id.txt_retailer)
    TextView Txt_subtittle;
    @BindView(R.id.Txt_expiry_date)
    TextView Txt_information;
    @BindView(R.id.txt_expiry_date)
    TextView Txt_expiry;
    //    @BindView(R.id.button_save)
//    Button Btn_save;
    @BindView(R.id.logo)
    ImageView img_logo;
    @BindView(R.id.imageView_home)
    ImageView img_home;
    @BindView(R.id.imageView4)
    ImageView img_setting;
    APIInterface apiInterface;
    //    @BindView(R.id.avi2)
//    AVLoadingIndicatorView avi;
//    @BindView(R.id.Rlv_avi)
//    RelativeLayout Rlv_avi;
    @BindView(R.id.Rlv_reset)
    RelativeLayout Rlv_Reset;
    @BindView(R.id.button_save2)
    Button Btn_cancel;
    @BindView(R.id.button_save3)
    Button Btn_close;
    String offer_id = "";


    @BindView(R.id.Img_phone_cart)
    ImageView Img_phone_cart;

    @BindView(R.id.LnrL__)
    LinearLayout Rlv_container;


    @BindView(R.id.instore)
    ImageView Img_instore;

    @BindView(R.id.phone_on)
    ImageView Img_phone_on;

    @BindView(R.id.print_)
    ImageView Img_print_;
    Response_view_offer response_ = null;
    @BindView(R.id.imageView6)
    ImageView img_call;
    @BindView(R.id.txt_program2)
    TextView txt_affliate_data;

    @BindView(R.id.spc1)
    Space space1;
    @BindView(R.id.spc2)
    Space space2;
    @BindView(R.id.spc3)
    Space space3;
    @BindView(R.id.spc4)
    Space space4;
    @BindView(R.id.spc5)
    Space space5;

    String Str_call = "";
    String Str_link = "";
    String Str_online = "";
    String Banner_tittle;
    @BindView(R.id.textView3)
    TextView Tittle;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redeem_screen);
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
        Img_phone_cart.setImageResource(R.drawable.mobile_on);
        Img_instore.setImageResource(R.drawable.instore_off);
        Img_phone_on.setImageResource(R.drawable.phone_off);
        Img_print_.setImageResource(R.drawable.print_off);
        Update_token update_token = new Update_token();
        update_token.Update_token(this);
        apiInterface = Api_client_with_member.getClient(this).create(APIInterface.class);
        Post_view_offer offer_list = new Post_view_offer("view", "xwmcoupon", offer_id);
        Call<Response_view_offer> call1 = apiInterface.Redeem_offer_view(offer_list);

        img_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callPhoneNumber();
            }
        });
        Txt_tittle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Str_link.contentEquals("")) {
                    Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(Str_link));
                    startActivity(intent);
                }
            }
        });

//        avi.show();
//        Rlv_avi.setVisibility(View.VISIBLE);
//        avi.dispatchWindowFocusChanged(true);

        call1.enqueue(new Callback<Response_view_offer>() {
            @Override
            public void onResponse(Call<Response_view_offer> call, Response<Response_view_offer> response) {
                if (response.isSuccessful()) {
                    try {
                        response_ = response.body();
                        Log.e("Success", new Gson().toJson(response.body()));
//                    avi.hide();
//                    Rlv_avi.setVisibility(View.GONE);
                        img_call.setVisibility(View.GONE);
                        Img_phone_cart.setImageResource(R.drawable.mobile_on);
                        Img_instore.setImageResource(R.drawable.instore_off);
                        Img_phone_on.setImageResource(R.drawable.phone_off);
                        Img_print_.setImageResource(R.drawable.print_off);

                        Txt_tittle.setText(response_.banner_header);
                        Txt_subtittle.setText(response_.banner_subtitle);
                        Txt_information.setText(response_.product_information);
                        Txt_expiry.setText("Expires on " + response_.expiry_date);
                        String img_logo_ = response_.banner_image;
                        String Str_barcode = response_.barcode;
                        String Str_phone = response_.phone;
                        //Btn_save.setVisibility(View.VISIBLE);
                        txt_affliate_data.setText(response_.affiliate_data);
                        if (response_.affiliate_data.contentEquals("")) {
                            txt_affliate_data.setVisibility(View.GONE);
                        }


                        String Str_mobile = response_.mobile;
                        Str_online = response_.online;
                        String Str_phonee = response_.phone;
                        String Str_print = response_.print;
//                        @BindView(R.id.spc1)
//                        Space space1;
//                        @BindView(R.id.spc2)
//                        Space space2;
//                        @BindView(R.id.spc3)
//                        Space space3;
//                        @BindView(R.id.spc4)
//                        Space space4;
//                        @BindView(R.id.spc5)
//                        Space space5;
//                        if (data.get(position).mobile.contentEquals("true")) {
//                            viewHolder.img_mobile.setImageResource(R.drawable.mobile_on);
//                        } else {
//                            viewHolder.img_mobile.setImageResource(R.drawable.mobile_off);
//                        }
//                        if (data.get(position).online.contentEquals("true")) {
//                            viewHolder.img_cart.setImageResource(R.drawable.instore_on);
//                        } else {
//                            viewHolder.img_cart.setImageResource(R.drawable.instore_off);
//                        }
//                        if (data.get(position).phone.contentEquals("true")) {
//                            viewHolder.imgphone.setImageResource(R.drawable.phone_on);
//                        } else {
//                            viewHolder.imgphone.setImageResource(R.drawable.phone_off);
//                        }
//                        if (data.get(position).print.contentEquals("true")) {
//                            viewHolder.img_print.setImageResource(R.drawable.print_on);
//                        } else {
//                            viewHolder.img_print.setImageResource(R.drawable.print_off);
//                        }


                        if (Str_mobile.contentEquals("true")) {
                            Img_phone_cart.setImageResource(R.drawable.mobile_on);
                            // space1.setVisibility(View.GONE);
                        } else {
                            Img_phone_cart.setImageResource(R.drawable.mobile_off);
                            Img_phone_cart.setEnabled(false);
                        }
                        if (Str_online.contentEquals("true")) {
                            Img_instore.setImageResource(R.drawable.instore_on);

                            //space2.setVisibility(View.GONE);
                        } else {
                            Img_instore.setImageResource(R.drawable.instore_off);
                            Img_instore.setEnabled(false);
                        }
                        if (Str_phonee.contentEquals("true")) {
                            Img_phone_on.setImageResource(R.drawable.phone_on);
                            //space3.setVisibility(View.GONE);
                        } else {
                            Img_phone_on.setImageResource(R.drawable.phone_off);
                            Img_phone_on.setEnabled(false);
                        }
                        if (Str_print.contentEquals("true")) {
                            Img_print_.setImageResource(R.drawable.print_on);
                            //space4.setVisibility(View.GONE);
                        } else {
                            Img_print_.setImageResource(R.drawable.print_off);
                            Img_print_.setEnabled(false);
                        }
                        Rlv_container.setVisibility(View.VISIBLE);
                        // Toast.makeText(Redeem_screen.this,Str_phone,Toast.LENGTH_LONG).show();
                        byte[] imageByteArray = Base64.decode(Str_barcode, Base64.DEFAULT);

                        Glide.with(Redeem_screen.this)
                                .load(imageByteArray)
                                .placeholder(R.drawable.coupon)
                                .into(img_logo);
                        Activity_log_view_offer activity_log = new Activity_log_view_offer();
                        activity_log.Activity_log_view_offer(Redeem_screen.this, "new", "viewmyoffer", offer_id);

                    } catch (java.lang.NullPointerException e) {
                        e.printStackTrace();

//                    avi.hide();
//                    Rlv_avi.setVisibility(View.GONE);

                    }

                } else {
                    JsonParser parser = new JsonParser();
                    JsonElement mJson = null;


                    response.errorBody();

                    String error = "";
                    try {
                        BufferedReader ereader = new BufferedReader(new InputStreamReader(
                                response.errorBody().byteStream()));
                        String eline = null;
                        while ((eline = ereader.readLine()) != null) {
                            error += eline + "";
                        }
                        ereader.close();
                    } catch (Exception e) {
                        error += e.getMessage();
                    }
                    Log.e("Error", error);

                    try {
                        JSONObject reader = new JSONObject(error);
                        String message = reader.getString("errormsg");

                        Toast.makeText(Redeem_screen.this, message, Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(Redeem_screen.this,
                                View_my_saved_coupon.class);
                        startActivity(i);
                        finish();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
//                        mJson = parser.parse(response.errorBody().string());
//                        Gson gson = new Gson();
//                        APIError message = gson.fromJson(response.errorBody().charStream(), APIError.class);
                    //Toast.makeText(Redeem_screen.this, "" + response.errorBody().string(), Toast.LENGTH_SHORT).show();
                    // MyError errorResponse = gson.fromJson(mJson, MyError.class);
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


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 101) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callPhoneNumber();
            }
        }
    }

    public void callPhoneNumber() {
        try {
            if (Build.VERSION.SDK_INT > 22) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(Redeem_screen.this, new String[]{Manifest.permission.CALL_PHONE}, 101);
                    return;
                }

                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + Str_call));
                startActivity(callIntent);

            } else {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + Str_call));
                startActivity(callIntent);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    //    @BindView(R.id.Img_phone_cart)
//    ImageView Img_phone_cart;
//
//    @BindView(R.id.instore)
//    ImageView Img_instore;
//
//    @BindView(R.id.phone_on)
//    ImageView Img_phone_on;
//
//    @BindView(R.id.print_)
//    ImageView Img_print_;
    @OnClick(R.id.Img_phone_cart)
    void phone_cart() {
        if (response_ == null) {
            return;
        }
        Str_link = "";
        //   Btn_save.setVisibility(View.VISIBLE);
        img_call.setVisibility(View.GONE);
//        Img_phone_cart.setImageResource(R.drawable.mobile_on);
//        Img_instore.setImageResource(R.drawable.instore_off);
//        Img_phone_on.setImageResource(R.drawable.phone_off);
//        Img_print_.setImageResource(R.drawable.print_off);

        Txt_tittle.setText(response_.banner_header);
        Txt_subtittle.setText(response_.banner_subtitle);
        Txt_information.setText(response_.product_information);
        Txt_expiry.setText("Expires on " + response_.expiry_date);
        String img_logo_ = response_.banner_image;
        String Str_barcode = response_.barcode;
        String Str_phone = response_.phone;
        // Toast.makeText(Redeem_screen.this,Str_phone,Toast.LENGTH_LONG).show();
        byte[] imageByteArray = Base64.decode(Str_barcode, Base64.DEFAULT);

        Glide.with(Redeem_screen.this)
                .load(imageByteArray)

                .into(img_logo);

        if (Str_online.contentEquals("true")) {
            Post_view_offer offer_list = new Post_view_offer("burn", "xwmcoupon", offer_id);
            Call<Response_view_offer> call1 = apiInterface.Save_offer(offer_list);

            call1.enqueue(new Callback<Response_view_offer>() {
                @Override
                public void onResponse(Call<Response_view_offer> call, Response<Response_view_offer> response) {

                    try {
                        Response_view_offer response_ = response.body();

                        Activity_log_view_offer activity_log = new Activity_log_view_offer();
                        activity_log.Activity_log_view_offer(Redeem_screen.this, "new", "redeem", offer_id);
                        Toast.makeText(Redeem_screen.this, "Successfully redeemed", Toast.LENGTH_SHORT).show();
                        Img_phone_cart.setImageResource(R.drawable.mobile_off);
                        Img_instore.setImageResource(R.drawable.instore_off);
                        Img_phone_on.setImageResource(R.drawable.phone_off);
                        Img_print_.setImageResource(R.drawable.print_off);
                        Img_phone_cart.setEnabled(false);
                        // Rlv_Reset.setVisibility(View.VISIBLE);

                    } catch (java.lang.NullPointerException e) {
                        e.printStackTrace();
//
//

                    }

                }

                @Override
                public void onFailure(Call<Response_view_offer> call, Throwable t) {
                    call.cancel();
                    Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_SHORT).show();


                }
            });
        }
    }

    @OnClick(R.id.instore)
    void instore() {
        if (response_ == null) {
            return;
        }
        //Btn_save.setVisibility(View.INVISIBLE);
        img_call.setVisibility(View.GONE);
//        Img_phone_cart.setImageResource(R.drawable.mobile_off);
//        Img_instore.setImageResource(R.drawable.instore_on);
//        Img_phone_on.setImageResource(R.drawable.phone_off);
//        Img_print_.setImageResource(R.drawable.print_off);

        Txt_tittle.setText(response_.product_url);
        Txt_subtittle.setText(response_.promo_code);
        Txt_information.setText(response_.product_information);
        Txt_expiry.setText("Expires on " + response_.expiry_date);
        String img_logo_ = response_.banner_image;
        String Str_barcode = response_.barcode;
        String Str_phone = response_.phone;
        Str_link = response_.product_url;
        // Toast.makeText(Redeem_screen.this,Str_phone,Toast.LENGTH_LONG).show();
        byte[] imageByteArray = Base64.decode(img_logo_, Base64.DEFAULT);

        Glide.with(Redeem_screen.this)
                .load(imageByteArray)

                .into(img_logo);
    }

    @OnClick(R.id.phone_on)
    void phone_on() {
        if (response_ == null) {
            return;
        }
        // Btn_save.setVisibility(View.INVISIBLE);
        Str_link = "";
//        Img_phone_cart.setImageResource(R.drawable.mobile_off);
//        Img_instore.setImageResource(R.drawable.instore_off);
//        Img_phone_on.setImageResource(R.drawable.phone_on);
//        Img_print_.setImageResource(R.drawable.print_off);


        Txt_tittle.setText(response_.promo_code);
        Txt_subtittle.setText(response_.telephone);
        Txt_information.setText(response_.product_information);
        Txt_expiry.setText("Expires on " + response_.expiry_date);
        String img_logo_ = response_.banner_image;
        String Str_barcode = response_.barcode;
        String Str_phone = response_.telephone;
        Str_call = Str_phone;

        if (!Str_phone.contentEquals("")) {
            img_call.setVisibility(View.VISIBLE);
        }
        // Toast.makeText(Redeem_screen.this,Str_phone,Toast.LENGTH_LONG).show();
        byte[] imageByteArray = Base64.decode(img_logo_, Base64.DEFAULT);

        Glide.with(Redeem_screen.this)
                .load(imageByteArray)

                .into(img_logo);
    }

    @OnClick(R.id.print_)
    void print_() {
        if (response_ == null) {
            return;
        }
        //Btn_save.setVisibility(View.INVISIBLE);
        Str_link = "";
        img_call.setVisibility(View.GONE);
//        Img_phone_cart.setImageResource(R.drawable.mobile_off);
//        Img_instore.setImageResource(R.drawable.instore_off);
//        Img_phone_on.setImageResource(R.drawable.phone_off);
//        Img_print_.setImageResource(R.drawable.print_on);

        Txt_tittle.setText(response_.banner_header);
        Txt_subtittle.setText(response_.banner_subtitle);
        Txt_information.setText(response_.product_information);
        Txt_expiry.setText("Expires on " + response_.expiry_date);
        String img_logo_ = response_.logo;
        String Str_barcode = response_.barcode;
        String Str_phone = response_.phone;
        // Toast.makeText(Redeem_screen.this,Str_phone,Toast.LENGTH_LONG).show();
        byte[] imageByteArray = Base64.decode(Str_barcode, Base64.DEFAULT);

        Glide.with(Redeem_screen.this)
                .load(imageByteArray)

                .into(img_logo);
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
        Post_view_offer offer_list = new Post_view_offer("burn", "xwmcoupon", offer_id);
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
                    Activity_log_view_offer activity_log = new Activity_log_view_offer();
                    activity_log.Activity_log_view_offer(Redeem_screen.this, "new", "redeem", offer_id);
                    Toast.makeText(Redeem_screen.this, response_.status, Toast.LENGTH_SHORT).show();
                    Rlv_Reset.setVisibility(View.VISIBLE);
                    //  Btn_save.setVisibility(View.GONE);
//                    Intent i = new Intent(Coupon_detail.this,
//                            View_coupon.class);
//                    startActivity(i);
                    //  finish();
                } catch (java.lang.NullPointerException e) {
                    e.printStackTrace();
//
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
                                    activity_log.Activity_log(Redeem_screen.this, "new", "logout");

                                    SharedPreferences preferences = getSharedPreferences("COUPON FOUNDRY", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.clear();
                                    editor.apply();
                                    Intent i = new Intent(Redeem_screen.this,
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
        Intent i = new Intent(Redeem_screen.this,
                Home_screen.class);
        startActivity(i);
        finish();
    }

    @OnClick(R.id.button_save2)
    void Reset() {
        Post_view_offer offer_list = new Post_view_offer("reset", "xwmcoupon", offer_id);
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
                    Activity_log_view_offer activity_log = new Activity_log_view_offer();
                    activity_log.Activity_log_view_offer(Redeem_screen.this, "new", "unredeem", offer_id);
                    Toast.makeText(Redeem_screen.this, response_.status, Toast.LENGTH_SHORT).show();
                    Rlv_Reset.setVisibility(View.GONE);
                    //Btn_save.setVisibility(View.VISIBLE);
//                    Intent i = new Intent(Coupon_detail.this,
//                            View_coupon.class);
//                    startActivity(i);
                    //  finish();
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

    @OnClick(R.id.button_save3)
    void Close() {
        // finish();
        Intent i = new Intent(Redeem_screen.this,
                View_my_saved_coupon.class);

        startActivity(i);
        finish();
    }

    @Override
    public void onBackPressed() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setMessage("Are you sure you want to exit?")
//                .setCancelable(false)
//                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        finishAffinity();
//                    }
//                })
//                .setNegativeButton("No", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        dialog.cancel();
//                    }
//                });
//        AlertDialog alert = builder.create();
//        alert.show();
        finish();

    }

    public class APIError {
        private String errormsg;

        public String getMessage() {
            return errormsg;
        }
    }

    // {"errornum":2018,"errormsg":"Offer Has Expired"}
}
