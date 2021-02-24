package com.couponfoundry.View;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.couponfoundry.Firebase.Constants;
import com.couponfoundry.Firebase.MyNotificationManager;
import com.couponfoundry.Model.Offer_list;
import com.couponfoundry.Model.Post_logout;
import com.couponfoundry.Model.Post_notification;
import com.couponfoundry.Model.Post_offer_list;
import com.couponfoundry.Model.Post_to_get_token;
import com.couponfoundry.Model.Response_view_offer;
import com.couponfoundry.Model.response_token;
import com.couponfoundry.R;
import com.couponfoundry.adapter.Coupon_list;
import com.couponfoundry.rest.APIInterface;
import com.couponfoundry.rest.Activity_log;
import com.couponfoundry.rest.Api_client_with_member;
import com.couponfoundry.rest.Update_token;
import com.wang.avi.AVLoadingIndicatorView;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

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
    @BindView(R.id.button2)
    Button Img_back;
    //    @BindView(R.id.Rlv_avi)
//    RelativeLayout Rlv_avi;
//    @BindView(R.id.avi)
//    AVLoadingIndicatorView avi;
    int int_size = 0;
    ArrayList<Offer_list.Datum> offerlist;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_my_saved_coupon);
        ButterKnife.bind(this);
//        Update_token update_token = new Update_token();
//        update_token.Update_token(this);
        apiInterface = Api_client_with_member.getClient(this).create(APIInterface.class);
        Post_offer_list offer_list = new Post_offer_list("list", "xwmcoupon","true");
        Call<Offer_list> call1 = apiInterface.View_Save_offer(offer_list);
//        avi.show();
//        Rlv_avi.setVisibility(View.VISIBLE);
//        avi.dispatchWindowFocusChanged(true);

        call1.enqueue(new Callback<Offer_list>() {
            @Override
            public void onResponse(Call<Offer_list> call, Response<Offer_list> response) {
                if (response.isSuccessful()) {
                    try {
                        Offer_list response_ = response.body();
//                    avi.hide();
//                    Rlv_avi.setVisibility(View.GONE);
                        offerlist = response_.offers;
                        Coupon_list adapter = new Coupon_list(View_my_saved_coupon.this, offerlist, "OPEN");
                        Lv_list.setAdapter(adapter);

                        int_size = offerlist.size();
                        String Str_size = String.valueOf(int_size);
                        if (Str_size.contentEquals("0")) {
                            return;
                        }

                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                            NotificationManager mNotificationManager =
                                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                            int importance = NotificationManager.IMPORTANCE_HIGH;
                            NotificationChannel mChannel = new NotificationChannel(Constants.CHANNEL_ID, Constants.CHANNEL_NAME, importance);
                            mChannel.setDescription(Constants.CHANNEL_DESCRIPTION);
                            mChannel.enableLights(true);
                            mChannel.setLightColor(Color.RED);
                            mChannel.enableVibration(true);
                            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                            mNotificationManager.createNotificationChannel(mChannel);
                        }
                        MyNotificationManager.getInstance(View_my_saved_coupon.this).displayNotification("Offers are Available", "You have " + Str_size + " Saved Offers");
//                        Post_notification Push_notification = new Post_notification("send", "Offers are Available", "You have " + Str_size + " Saved Offers");
//                        Call<Response_view_offer> call_push = apiInterface.Notification(Push_notification);
////                    avi.show();
////                    Rlv_avi.setVisibility(View.VISIBLE);
////                    avi.dispatchWindowFocusChanged(true);
//
//                        call_push.enqueue(new Callback<Response_view_offer>() {
//                            @Override
//                            public void onResponse(Call<Response_view_offer> call, Response<Response_view_offer> response) {
//
//                                try {
//                                    Response_view_offer response_ = response.body();
////                                avi.hide();
////                                Rlv_avi.setVisibility(View.GONE);
//
//                                } catch (java.lang.NullPointerException e) {
//                                    e.printStackTrace();
//
////                                avi.hide();
////                                Rlv_avi.setVisibility(View.GONE);
//
//                                }
//
//                            }
//
//                            @Override
//                            public void onFailure(Call<Response_view_offer> call, Throwable t) {
//                                call.cancel();
//                                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_SHORT).show();
////                            avi.hide();
////                            Rlv_avi.setVisibility(View.GONE);
//
//                            }
//                        });
                        Activity_log activity_log = new Activity_log();
                        activity_log.Activity_log(View_my_saved_coupon.this, "new", "myoffers");
                    } catch (java.lang.NullPointerException e) {
                        e.printStackTrace();

//                    avi.hide();
//                    Rlv_avi.setVisibility(View.GONE);

                    }
                }else{
                    String Sha512 = get_SHA_512_SecurePassword("secret:0nRj$Zb$+UL=+apikey:b$4wk09jAQs*");
                    String Str = Sha512;
                    Post_to_get_token user = new Post_to_get_token("authenticate", "z/9n}0YoMDl5", Str);
                    Call<response_token> call1 = apiInterface.Get_token(user);
                    call1.enqueue(new Callback<response_token>() {
                        @Override
                        public void onResponse(Call<response_token> call, Response<response_token> response) {
                            try {
                                response_token user1 = response.body();
                                String Token = user1.token;
                                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(View_my_saved_coupon.this);
                                SharedPreferences.Editor editor = prefs.edit();
                                editor.putString("token", Token);
                                editor.apply();
                                System.out.println("token......" + Token);

                                finish();
                                overridePendingTransition(0, 0);
                                startActivity(getIntent());
                                overridePendingTransition(0, 0);

                            } catch (java.lang.NullPointerException e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onFailure(Call<response_token> call, Throwable t) {
                            call.cancel();


                        }
                    });
                }

            }

            @Override
            public void onFailure(Call<Offer_list> call, Throwable t) {
                call.cancel();
                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_SHORT).show();
//                avi.hide();
//                Rlv_avi.setVisibility(View.GONE);

            }
        });

        Lv_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(View_my_saved_coupon.this,
                        Redeem_screen.class);
                i.putExtra("offerid", offerlist.get(position).offer_id);
                i.putExtra("Banner_tittle", offerlist.get(position).retailer);
                startActivity(i);
                //finish();
            }
        });
    }

    @OnClick(R.id.button2)
    void Home() {
        Intent i = new Intent(View_my_saved_coupon.this,
                Home_screen.class);
        startActivity(i);
        finish();
    }
    @OnClick(R.id.imageView5)
    void Back() {
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
        Intent i = new Intent(View_my_saved_coupon.this,
                Home_screen.class);
        startActivity(i);
        finish();

    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public String get_SHA_512_SecurePassword(String passwordToHash) {
        try {
            // getInstance() method is called with algorithm SHA-512
            MessageDigest md = MessageDigest.getInstance("SHA-512");

            // digest() method is called
            // to calculate message digest of the input string
            // returned as array of byte
            byte[] messageDigest = md.digest(passwordToHash.getBytes());

            // Convert byte array into signum representation
            BigInteger no = new BigInteger(1, messageDigest);

            // Convert message digest into hex value
            String hashtext = no.toString(16);

            // Add preceding 0s to make it 32 bit
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }

            // return the HashText
            return hashtext;
        }

        // For specifying wrong message digest algorithms
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

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