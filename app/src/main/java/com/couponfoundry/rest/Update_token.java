package com.couponfoundry.rest;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.couponfoundry.Model.Post_to_get_token;
import com.couponfoundry.Model.response_token;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class Update_token {
    APIInterface apiInterface;

    public void Update_token(Context context) {
        String Sha512 = get_SHA_512_SecurePassword("secret:0nRj$Zb$+UL=+apikey:b$4wk09jAQs*");
        String Str = Sha512;
        apiInterface = Api_client_get_token.getClient(context).create(APIInterface.class);

        Post_to_get_token user = new Post_to_get_token("authenticate", "z/9n}0YoMDl5", Str);
        Call<response_token> call1 = apiInterface.Get_token(user);
        call1.enqueue(new Callback<response_token>() {
            @Override
            public void onResponse(Call<response_token> call, Response<response_token> response) {
                try {
                    response_token user1 = response.body();
                    String Token = user1.token;
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("token", Token);
                    editor.apply();
                    System.out.println("token......" + Token);


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

}
