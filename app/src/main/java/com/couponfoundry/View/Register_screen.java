package com.couponfoundry.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
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
import com.couponfoundry.rest.Update_token;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Register_screen extends AppCompatActivity {
    APIInterface apiInterface;
    @BindView(R.id.editText2)
    EditText Editext_phone;
    @BindView(R.id.editText_otp)
    EditText Editext_otp;
    @BindView(R.id.button)
    Button Btn_Register;
    String androidId = "";
    @BindView(R.id.Rlv_already_registered)
    RelativeLayout Rlv_Reregiter_;
    @BindView(R.id.Rlv_otp_)
    RelativeLayout Rlv_otp;
    @BindView(R.id.btn_confirm)
    Button Btn_confirm;
    @BindView(R.id.button_register)
    Button Btn_confirm_otp;
    @BindView(R.id.Butn_countrycode)
    Button Btn_Country_code;
    //    @BindView(R.id.avi)
//    AVLoadingIndicatorView avi;
//    @BindView(R.id.Rlv_avi)
//    RelativeLayout Rlv_avi;
    String Str_firebase_token = "";
    String mVerificationId = "";
    PhoneAuthProvider.ForceResendingToken mResendToken;
    private FirebaseAuth mAuth;
    String Device_exist = "no";
    String Str_country_code = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_screen);
        ButterKnife.bind(this);
        apiInterface = APIClient.getClient(this).create(APIInterface.class);
        Str_firebase_token = Fcm_token(this);
        mAuth = FirebaseAuth.getInstance();
        androidId = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);
        //    androidId = "76gg57645tt88787yyjhyh67";
        Post_isdeviceexist user = new Post_isdeviceexist("isdeviceexist", androidId);
        Call<Response_isdevice_exist> call1 = apiInterface.isdeviceexist(user);
//        avi.show();
//        Rlv_avi.setVisibility(View.VISIBLE);
        Str_country_code = GetCountryZipCode();
        Str_country_code = "+" + Str_country_code;
        Btn_Country_code.setText(Str_country_code);
        //Toast.makeText(Register_screen.this,Str_country_code,Toast.LENGTH_LONG).show();


        call1.enqueue(new Callback<Response_isdevice_exist>() {
            @Override
            public void onResponse(Call<Response_isdevice_exist> call, Response<Response_isdevice_exist> response) {

                try {
                    Response_isdevice_exist response_ = response.body();

                    if (response.isSuccessful()) {
                        String status = response_.status;
                        if (status.contentEquals("success")) {
                            Device_exist = "yes";
                            Rlv_Reregiter_.setVisibility(View.VISIBLE);
                            Rlv_otp.setVisibility(View.GONE);
//                        avi.hide();
//                        Rlv_avi.setVisibility(View.GONE);

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


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                } catch (java.lang.NullPointerException e) {
                    e.printStackTrace();

                    //Toast.makeText(Register_screen.this, "Device does not exist", Toast.LENGTH_LONG).show();
//                    avi.hide();
//                    Rlv_avi.setVisibility(View.GONE);
                }

            }

            @Override
            public void onFailure(Call<Response_isdevice_exist> call, Throwable t) {
                call.cancel();
                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_SHORT).show();


            }
        });

    }

    @OnClick(R.id.button)
    void Register() {
        if (Editext_phone.getText().toString().contentEquals("")) {
            Shake_animation shkanim = new Shake_animation();
            Editext_phone.startAnimation(shkanim.shakeError());
            return;
        } else if (Editext_phone.getText().toString().length() < 5) {
            Shake_animation shkanim = new Shake_animation();
            Editext_phone.startAnimation(shkanim.shakeError());
            Editext_phone.setError("Enter valid cell");
            return;
        }
//        register_device Register;
//        Register = new register_device("reregister", androidId, Editext_phone.getText().toString(), Str_firebase_token);
//
//        // Register = new register_device("reregister", androidId, Editext_phone.getText().toString(), Str_firebase_token);
//        Call<Response_isdevice_exist> call1 = apiInterface.Register(Register);
//        call1.enqueue(new Callback<Response_isdevice_exist>() {
//            @Override
//            public void onResponse(Call<Response_isdevice_exist> call, Response<Response_isdevice_exist> response) {
//
//                try {
//                    Response_isdevice_exist user1 = response.body();
//                    String success = user1.status;
////                                        avi.hide();
////                                        Rlv_avi.setVisibility(View.GONE);
//                    if (success.contentEquals("success")) {
//                        String str_member = user1.member;
//                        //Toast.makeText(Register_screen.this, "Registered successfully with member...." + str_member, Toast.LENGTH_LONG).show();
//                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(Register_screen.this);
//                        SharedPreferences.Editor editor = prefs.edit();
//                        editor.putString("member", str_member);
//                        editor.apply();
//                        Activity_log activity_log = new Activity_log();
//                        activity_log.Activity_log(Register_screen.this, "new", "register");
//                        Intent i = new Intent(Register_screen.this,
//                                Home_screen.class);
//                        startActivity(i);
//                        finish();
//                    }
//
//
//                } catch (java.lang.NullPointerException e) {
//                    e.printStackTrace();
////                                        avi.hide();
////                                        Rlv_avi.setVisibility(View.GONE);
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<Response_isdevice_exist> call, Throwable t) {
////                                    avi.hide();
////                                    Rlv_avi.setVisibility(View.GONE);
//                call.cancel();
//                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_SHORT).show();
//
//
//            }
//        });



//        avi.show();
//        Rlv_avi.setVisibility(View.VISIBLE);
       // sendVerificationCode(Editext_phone.getText().toString());

        register_device Register;
        if (Device_exist.contentEquals("yes")) {
            Register = new register_device("reregister", androidId, Editext_phone.getText().toString(), Str_firebase_token);

        } else {
            Register = new register_device("register", androidId, Editext_phone.getText().toString(), Str_firebase_token);
        }
        // Register = new register_device("reregister", androidId, Editext_phone.getText().toString(), Str_firebase_token);
        Call<Response_isdevice_exist> call1 = apiInterface.Register(Register);
        call1.enqueue(new Callback<Response_isdevice_exist>() {
            @Override
            public void onResponse(Call<Response_isdevice_exist> call, Response<Response_isdevice_exist> response) {

                try {
                    Response_isdevice_exist user1 = response.body();
                    String success = user1.status;
//                                        avi.hide();
//                                        Rlv_avi.setVisibility(View.GONE);
                    if (success.contentEquals("success")) {
                        String str_member = user1.member;
                        Toast.makeText(Register_screen.this, "Registered successfully", Toast.LENGTH_LONG).show();
                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(Register_screen.this);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("member", str_member);
                        editor.apply();
                        Activity_log activity_log = new Activity_log();
                        activity_log.Activity_log(Register_screen.this, "new", "register");
                        Intent i = new Intent(Register_screen.this,
                                Home_screen.class);
                        startActivity(i);
                        finish();
                    }


                } catch (java.lang.NullPointerException e) {
                    e.printStackTrace();
//                                        avi.hide();
//                                        Rlv_avi.setVisibility(View.GONE);
                }

            }

            @Override
            public void onFailure(Call<Response_isdevice_exist> call, Throwable t) {
//                                    avi.hide();
//                                    Rlv_avi.setVisibility(View.GONE);
                call.cancel();
                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_SHORT).show();


            }
        });
    }

    @OnClick(R.id.button_register)
    void Button_confirm_otp() {
        if (Editext_otp.getText().toString().contentEquals("")) {
            Shake_animation shkanim = new Shake_animation();
            Editext_phone.startAnimation(shkanim.shakeError());
            return;
        }
        //Editext_otp.setText(code);
//        register_device Register;
//        Register = new register_device("register", androidId, Editext_phone.getText().toString(), Str_firebase_token);
//
//        // Register = new register_device("reregister", androidId, Editext_phone.getText().toString(), Str_firebase_token);
//        Call<Response_isdevice_exist> call1 = apiInterface.Register(Register);
//        call1.enqueue(new Callback<Response_isdevice_exist>() {
//            @Override
//            public void onResponse(Call<Response_isdevice_exist> call, Response<Response_isdevice_exist> response) {
//
//                try {
//                    Response_isdevice_exist user1 = response.body();
//                    String success = user1.status;
////                                        avi.hide();
////                                        Rlv_avi.setVisibility(View.GONE);
//                    if (success.contentEquals("success")) {
//                        String str_member = user1.member;
//                        //Toast.makeText(Register_screen.this, "Registered successfully with member...." + str_member, Toast.LENGTH_LONG).show();
//                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(Register_screen.this);
//                        SharedPreferences.Editor editor = prefs.edit();
//                        editor.putString("member", str_member);
//                        editor.apply();
//                        Activity_log activity_log = new Activity_log();
//                        activity_log.Activity_log(Register_screen.this, "new", "register");
//                        Intent i = new Intent(Register_screen.this,
//                                Home_screen.class);
//                        startActivity(i);
//                        finish();
//                    }
//
//
//                } catch (java.lang.NullPointerException e) {
//                    e.printStackTrace();
////                                        avi.hide();
////                                        Rlv_avi.setVisibility(View.GONE);
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<Response_isdevice_exist> call, Throwable t) {
////                                    avi.hide();
////                                    Rlv_avi.setVisibility(View.GONE);
//                call.cancel();
//                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_SHORT).show();
//
//
//            }
//        });

//                //verifying the code
         //  verifyVerificationCode(Editext_otp.getText().toString());

        register_device Register;
        if (Device_exist.contentEquals("yes")) {
            Register = new register_device("reregister", androidId, Editext_phone.getText().toString(), Str_firebase_token);

        } else {
            Register = new register_device("register", androidId, Editext_phone.getText().toString(), Str_firebase_token);
        }
        // Register = new register_device("reregister", androidId, Editext_phone.getText().toString(), Str_firebase_token);
        Call<Response_isdevice_exist> call1 = apiInterface.Register(Register);
        call1.enqueue(new Callback<Response_isdevice_exist>() {
            @Override
            public void onResponse(Call<Response_isdevice_exist> call, Response<Response_isdevice_exist> response) {

                try {
                    Response_isdevice_exist user1 = response.body();
                    String success = user1.status;
//                                        avi.hide();
//                                        Rlv_avi.setVisibility(View.GONE);
                    if (success.contentEquals("success")) {
                        String str_member = user1.member;
                        Toast.makeText(Register_screen.this, "Registered successfully", Toast.LENGTH_LONG).show();
                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(Register_screen.this);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("member", str_member);
                        editor.apply();
                        Activity_log activity_log = new Activity_log();
                        activity_log.Activity_log(Register_screen.this, "new", "register");
                        Intent i = new Intent(Register_screen.this,
                                Home_screen.class);
                        startActivity(i);
                        finish();
                    }


                } catch (java.lang.NullPointerException e) {
                    e.printStackTrace();
//                                        avi.hide();
//                                        Rlv_avi.setVisibility(View.GONE);
                }

            }

            @Override
            public void onFailure(Call<Response_isdevice_exist> call, Throwable t) {
//                                    avi.hide();
//                                    Rlv_avi.setVisibility(View.GONE);
                call.cancel();
                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_SHORT).show();


            }
        });
    }

    @OnClick(R.id.btn_confirm)
    void Reregister() {
        if (Editext_phone.getText().toString().contentEquals("")) {
            Shake_animation shkanim = new Shake_animation();
            Editext_phone.startAnimation(shkanim.shakeError());
            return;
        } else if (Editext_phone.getText().toString().length() < 5) {
            Shake_animation shkanim = new Shake_animation();
            Editext_phone.startAnimation(shkanim.shakeError());
            Editext_phone.setError("Enter valid cell");
            return;
        }
//        avi.show();
//        Rlv_avi.setVisibility(View.VISIBLE);
       // sendVerificationCode(Editext_phone.getText().toString());
        register_device Register = new register_device("reregister", androidId, Editext_phone.getText().toString(), Str_firebase_token);
        Call<Response_isdevice_exist> call1 = apiInterface.Register(Register);
        call1.enqueue(new Callback<Response_isdevice_exist>() {
            @Override
            public void onResponse(Call<Response_isdevice_exist> call, Response<Response_isdevice_exist> response) {

                try {
                    Response_isdevice_exist user1 = response.body();
                    String success = user1.status;
//                    avi.hide();
//                    Rlv_avi.setVisibility(View.GONE);
                    if (success.contentEquals("success")) {
                        String str_member = user1.member;
                        Toast.makeText(Register_screen.this, "Registered successfully", Toast.LENGTH_LONG).show();
                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(Register_screen.this);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("member", str_member);
                        editor.apply();
                        Activity_log activity_log = new Activity_log();
                        activity_log.Activity_log(Register_screen.this, "new", "reregister");
                        Intent i = new Intent(Register_screen.this,
                                Home_screen.class);
                        startActivity(i);
                        finish();
                    }


                } catch (java.lang.NullPointerException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<Response_isdevice_exist> call, Throwable t) {
                call.cancel();
                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_SHORT).show();


            }
        });
    }

    public String Fcm_token(Context ctx) {
        SharedPreferences prefs = getSharedPreferences("COUPON FOUNDRYY", Context.MODE_PRIVATE);
        String Str_tocken = prefs.getString("Firebase_token", "");
        return Str_tocken;
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

    private void sendVerificationCode(String mobile) {
        Rlv_otp.setVisibility(View.VISIBLE);
        // Editext_otp.setText(code);
        Rlv_Reregiter_.setVisibility(View.GONE);
        Btn_Register.setEnabled(false);
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                Str_country_code + mobile,
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
                // Editext_otp.setText(code);
                Rlv_Reregiter_.setVisibility(View.GONE);
//                avi.hide();
//                Rlv_avi.setVisibility(View.GONE);
//                //verifying the code
                // verifyVerificationCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
//            avi.hide();
//            Rlv_avi.setVisibility(View.GONE);
            Toast.makeText(Register_screen.this, e.getMessage(), Toast.LENGTH_LONG).show();
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

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(Register_screen.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //verification successful we will start the profile activity
//                            avi.show();
//                            Rlv_avi.setVisibility(View.VISIBLE);
                            register_device Register;
                            if (Device_exist.contentEquals("yes")) {
                                Register = new register_device("reregister", androidId, Editext_phone.getText().toString(), Str_firebase_token);

                            } else {
                                Register = new register_device("register", androidId, Editext_phone.getText().toString(), Str_firebase_token);
                            }
                            // Register = new register_device("reregister", androidId, Editext_phone.getText().toString(), Str_firebase_token);
                            Call<Response_isdevice_exist> call1 = apiInterface.Register(Register);
                            call1.enqueue(new Callback<Response_isdevice_exist>() {
                                @Override
                                public void onResponse(Call<Response_isdevice_exist> call, Response<Response_isdevice_exist> response) {

                                    try {
                                        Response_isdevice_exist user1 = response.body();
                                        String success = user1.status;
//                                        avi.hide();
//                                        Rlv_avi.setVisibility(View.GONE);
                                        if (success.contentEquals("success")) {
                                            String str_member = user1.member;
                                            Toast.makeText(Register_screen.this, "Registered successfully", Toast.LENGTH_LONG).show();
                                            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(Register_screen.this);
                                            SharedPreferences.Editor editor = prefs.edit();
                                            editor.putString("member", str_member);
                                            editor.apply();
                                            Activity_log activity_log = new Activity_log();
                                            activity_log.Activity_log(Register_screen.this, "new", "register");
                                            Intent i = new Intent(Register_screen.this,
                                                    Home_screen.class);
                                            startActivity(i);
                                            finish();
                                        }


                                    } catch (java.lang.NullPointerException e) {
                                        e.printStackTrace();
//                                        avi.hide();
//                                        Rlv_avi.setVisibility(View.GONE);
                                    }

                                }

                                @Override
                                public void onFailure(Call<Response_isdevice_exist> call, Throwable t) {
//                                    avi.hide();
//                                    Rlv_avi.setVisibility(View.GONE);
                                    call.cancel();
                                    Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_SHORT).show();


                                }
                            });

                        } else {

                            //verification unsuccessful.. display an error message

                            String message = "Somthing is wrong, we will fix it soon...";

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                message = "Invalid code entered...";
                            }

//                            Snackbar snackbar = Snackbar.make(findViewById(R.id.parent), message, Snackbar.LENGTH_LONG);
//                            snackbar.setAction("Dismiss", new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//
//                                }
//                            });
//                            snackbar.show();
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
//                            avi.hide();
//                            Rlv_avi.setVisibility(View.GONE);
                        }
                    }
                });
    }

    public String Getcountry_code() {
        TelephonyManager telephoneManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        String countryCode = telephoneManager.getNetworkCountryIso();
        ;
        return countryCode;
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
}


