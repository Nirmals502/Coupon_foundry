package com.couponfoundry.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.List;
import java.util.concurrent.TimeUnit;

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
//    @BindView(R.id.Rlv_avi)
//    RelativeLayout Rlv_avi;
//    @BindView(R.id.avi)
//    AVLoadingIndicatorView avi;
    @BindView(R.id.Rlv_not_registered)
    RelativeLayout Rlv_notregisted;
    @BindView(R.id.btn_confirm)
    Button Register;
    @BindView(R.id.Butn_countrycode)
    Button Btn_Country_code;
    String Str_country_code = "";
    String mVerificationId = "";
    @BindView(R.id.Rlv_otp_)
    RelativeLayout Rlv_otp;

    @BindView(R.id.editText_otp)
    EditText Editext_otp;
    PhoneAuthProvider.ForceResendingToken mResendToken;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        ButterKnife.bind(this);
        apiInterface = APIClient.getClient(this).create(APIInterface.class);
        mAuth = FirebaseAuth.getInstance();

        androidId = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);
        Str_country_code = GetCountryZipCode();
        Str_country_code = "+" + Str_country_code;
        Btn_Country_code.setText(Str_country_code);
    }

    @OnClick(R.id.button)
    void Login() {

        if (Editext_phone.getText().toString().contentEquals("")) {
            Shake_animation shkanim = new Shake_animation();
            Editext_phone.startAnimation(shkanim.shakeError());
            return;
        }
        Post_isdeviceexist user = new Post_isdeviceexist("login", androidId);
        Call<Response_isdevice_exist> call1 = apiInterface.isdeviceexist(user);
//                            avi.show();
//                            Rlv_avi.setVisibility(View.VISIBLE);
//                            avi.dispatchWindowFocusChanged(true);

        call1.enqueue(new Callback<Response_isdevice_exist>() {
            @Override
            public void onResponse(Call<Response_isdevice_exist> call, Response<Response_isdevice_exist> response) {

                try {
                    Response_isdevice_exist response_ = response.body();
                    String status = response_.status;
//                                        avi.hide();
//                                        Rlv_avi.setVisibility(View.GONE);
                    if (status.contentEquals("success")) {
                        Toast.makeText(Login_screen.this, "Logged in successfully", Toast.LENGTH_LONG).show();
                        String str_member = response_.member;
                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(Login_screen.this);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("member", str_member);
                        editor.apply();
                        Activity_log activity_log = new Activity_log();
                        activity_log.Activity_log(Login_screen.this, "new", "login");
                        Intent i = new Intent(Login_screen.this,
                                Home_screen.class);
                        startActivity(i);
                        finish();
                    } else {
                        Rlv_notregisted.setVisibility(View.VISIBLE);
                    }


                } catch (java.lang.NullPointerException e) {
                    e.printStackTrace();
                    Toast.makeText(Login_screen.this, "Device does not exist", Toast.LENGTH_LONG).show();
//                                        avi.hide();
//                                        Rlv_avi.setVisibility(View.GONE);
                    Rlv_notregisted.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onFailure(Call<Response_isdevice_exist> call, Throwable t) {
                call.cancel();
                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_SHORT).show();


            }
        });

      //  sendVerificationCode(Editext_phone.getText().toString());
    }

    @OnClick(R.id.btn_confirm)
    void Register() {
        Intent i = new Intent(Login_screen.this,
                Register_screen.class);
        startActivity(i);
        finish();
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
    public String GetCountryZipCode() {
        String CountryID = "";
        String CountryZipCode = "";

        TelephonyManager manager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        //getNetworkCountryIso
        CountryID = manager.getSimCountryIso().toUpperCase();
        String[] rl = this.getResources().getStringArray(R.array.CountryCodes);
        for (int i = 0; i < rl.length; i++) {
            String[] g = rl[i].split(",");
            if (g[1].trim().equals(CountryID.trim())) {
                CountryZipCode = g[0];
                break;
            }
        }
        return CountryZipCode;
    }

    private void sendVerificationCode(String mobile) {
        //Btn_login.setEnabled(false);
        Rlv_otp.setVisibility(View.VISIBLE);
        Btn_login.setEnabled(true);
        Btn_login.setEnabled(false);
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                Str_country_code+mobile,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallbacks);
    }


    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            //Getting the code sent by SMS
            String code = phoneAuthCredential.getSmsCode();

            //sometime the code is not detected automatically
            //in this case the code will be null
            //so user has to manually enter the code
            if (code != null) {
                Rlv_otp.setVisibility(View.VISIBLE);
                Btn_login.setEnabled(true);
                //verifyVerificationCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
//            avi.hide();
//            Rlv_avi.setVisibility(View.GONE);
            Toast.makeText(Login_screen.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            mVerificationId = s;
            mResendToken = forceResendingToken;
        }
    };

    private void verifyVerificationCode(String code) {
        //creating the credential
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);

        //signing the user
        signInWithPhoneAuthCredential(credential);
    }

    @OnClick(R.id.button_register)
    void Reregister() {
        if (Editext_otp.getText().toString().contentEquals("")) {
            Shake_animation shkanim = new Shake_animation();
            Editext_phone.startAnimation(shkanim.shakeError());
            return;
        }
        verifyVerificationCode(Editext_otp.getText().toString());
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(Login_screen.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

//                            avi.show();
//                            Rlv_avi.setVisibility(View.VISIBLE);
                            Post_isdeviceexist user = new Post_isdeviceexist("login", androidId);
                            Call<Response_isdevice_exist> call1 = apiInterface.isdeviceexist(user);
//                            avi.show();
//                            Rlv_avi.setVisibility(View.VISIBLE);
//                            avi.dispatchWindowFocusChanged(true);

                            call1.enqueue(new Callback<Response_isdevice_exist>() {
                                @Override
                                public void onResponse(Call<Response_isdevice_exist> call, Response<Response_isdevice_exist> response) {

                                    try {
                                        Response_isdevice_exist response_ = response.body();
                                        String status = response_.status;
//                                        avi.hide();
//                                        Rlv_avi.setVisibility(View.GONE);
                                        if (status.contentEquals("success")) {
                                            Toast.makeText(Login_screen.this, "Logined successfully", Toast.LENGTH_LONG).show();
                                            String str_member = response_.member;
                                            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(Login_screen.this);
                                            SharedPreferences.Editor editor = prefs.edit();
                                            editor.putString("member", str_member);
                                            editor.apply();
                                            Activity_log activity_log = new Activity_log();
                                            activity_log.Activity_log(Login_screen.this, "new", "login");
                                            Intent i = new Intent(Login_screen.this,
                                                    Home_screen.class);
                                            startActivity(i);
                                            finish();
                                        } else {
                                            Rlv_notregisted.setVisibility(View.VISIBLE);
                                        }


                                    } catch (java.lang.NullPointerException e) {
                                        e.printStackTrace();
                                        Toast.makeText(Login_screen.this, "Device does not exist", Toast.LENGTH_LONG).show();
//                                        avi.hide();
//                                        Rlv_avi.setVisibility(View.GONE);
                                        Rlv_notregisted.setVisibility(View.VISIBLE);
                                    }

                                }

                                @Override
                                public void onFailure(Call<Response_isdevice_exist> call, Throwable t) {
                                    call.cancel();
                                    Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_SHORT).show();


                                }
                            });
                            //verification successful we will start the profile activity


                        } else {

                            //verification unsuccessful.. display an error message

                            String message = "Somthing is wrong, we will fix it soon...";

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                message = "Invalid code entered...";
                            }
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

//                            Snackbar snackbar = Snackbar.make(findViewById(R.id.parent), message, Snackbar.LENGTH_LONG);
//                            snackbar.setAction("Dismiss", new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//
//                                }
//                            });
//                            snackbar.show();
//                            avi.hide();
//                            Rlv_avi.setVisibility(View.GONE);
                        }
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