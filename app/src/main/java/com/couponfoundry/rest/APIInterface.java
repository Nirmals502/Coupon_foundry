package com.couponfoundry.rest;

import com.couponfoundry.Model.Post_activity;
import com.couponfoundry.Model.Post_isdeviceexist;
import com.couponfoundry.Model.Post_to_get_token;
import com.couponfoundry.Model.Response_isdevice_exist;
import com.couponfoundry.Model.register_device;
import com.couponfoundry.Model.response_token;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by nirmal on 09/01/17.
 */

public interface APIInterface {
    @POST("/api/v1/members")
    Call<Response_isdevice_exist> isdeviceexist(@Body Post_isdeviceexist Post_continue);
    @POST("/api/v1/members")
    Call<Response_isdevice_exist> Register(@Body register_device Post_continue);
    @POST("/api/v1/token")
    Call<response_token> Get_token(@Body Post_to_get_token Post_continue);
    @POST("/api/v1/activity")
    Call<Response_isdevice_exist> Activity(@Body Post_activity Activity);
}
