package com.couponfoundry.rest;

import android.content.Context;

import com.couponfoundry.Model.Post_activity;
import com.couponfoundry.Model.Post_activity_view_offer;
import com.couponfoundry.Model.Response_isdevice_exist;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Activity_log_view_offer {
    APIInterface apiInterface;

    public void Activity_log_view_offer(Context context, String action, String activity, String offer_id) {

        apiInterface = Api_client_with_member.getClient(context).create(APIInterface.class);

        Post_activity_view_offer activty = new Post_activity_view_offer(action, activity, offer_id);
        Call<Response_isdevice_exist> call1 = apiInterface.Activity_view_offer(activty);
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
