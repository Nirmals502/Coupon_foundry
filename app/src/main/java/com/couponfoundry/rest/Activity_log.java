package com.couponfoundry.rest;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.couponfoundry.Model.Post_activity;
import com.couponfoundry.Model.Post_to_get_token;
import com.couponfoundry.Model.Response_isdevice_exist;
import com.couponfoundry.Model.response_token;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Activity_log {
    APIInterface apiInterface;

    public void Activity_log(Context context, String action, String activity) {

        apiInterface = Api_client_with_member.getClient(context).create(APIInterface.class);

        Post_activity activty = new Post_activity(action, activity);
        Call<Response_isdevice_exist> call1 = apiInterface.Activity(activty);
        call1.enqueue(new Callback<Response_isdevice_exist>() {
            @Override
            public void onResponse(Call<Response_isdevice_exist> call, Response<Response_isdevice_exist> response) {
                try {
                    Response_isdevice_exist Res = response.body();
                    String status = Res.status;


                } catch (java.lang.NullPointerException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<Response_isdevice_exist> call, Throwable t) {
                call.cancel();


            }
        });
    }
}
