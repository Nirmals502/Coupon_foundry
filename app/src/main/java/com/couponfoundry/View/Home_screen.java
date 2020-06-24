package com.couponfoundry.View;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import com.couponfoundry.R;
import com.couponfoundry.Sharedpreference.PrefManager;
import com.couponfoundry.rest.Activity_log;
import com.couponfoundry.rest.Update_token;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Home_screen extends AppCompatActivity {
    @BindView(R.id.btn_coupons)
    Button Btn_coupons;
    @BindView(R.id.btn_wallet)
    Button Btn_wallet;
    private PrefManager prefManager;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        ButterKnife.bind(this);
        prefManager = new PrefManager(Home_screen.this);
        prefManager.setFirstTimeLaunch(false);
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