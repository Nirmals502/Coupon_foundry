package com.couponfoundry.View;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.couponfoundry.Model.Offer_list;
import com.couponfoundry.Model.Post_isdeviceexist;
import com.couponfoundry.Model.Post_offer_list;
import com.couponfoundry.Model.Response_isdevice_exist;
import com.couponfoundry.R;
import com.couponfoundry.adapter.Coupon_list;
import com.couponfoundry.rest.APIClient;
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

public class View_coupon extends AppCompatActivity {
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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_coupon);
        ButterKnife.bind(this);
        Update_token update_token = new Update_token();
        update_token.Update_token(this);
        apiInterface = Api_client_with_member.getClient(this).create(APIInterface.class);
        Post_offer_list offer_list = new Post_offer_list("list", "xwmcoupon", "true");
        Call<Offer_list> call1 = apiInterface.Offer_list(offer_list);
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
                    Coupon_list adapter = new Coupon_list(View_coupon.this, offerlist);
                    Lv_list.setAdapter(adapter);
                    Activity_log activity_log = new Activity_log();
                    activity_log.Activity_log(View_coupon.this, "new", "search");
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
    void Home(){
        Intent i = new Intent(View_coupon.this,
                Home_screen.class);
        startActivity(i);
        finish();
    }
}