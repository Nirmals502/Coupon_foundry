package com.couponfoundry.rest;

import com.couponfoundry.Model.Offer_list;
import com.couponfoundry.Model.Post_activity;
import com.couponfoundry.Model.Post_activity_view_offer;
import com.couponfoundry.Model.Post_isdeviceexist;
import com.couponfoundry.Model.Post_logout;
import com.couponfoundry.Model.Post_notification;
import com.couponfoundry.Model.Post_offer_list;
import com.couponfoundry.Model.Post_to_get_token;
import com.couponfoundry.Model.Post_view_offer;
import com.couponfoundry.Model.Response_isdevice_exist;
import com.couponfoundry.Model.Response_view_offer;
import com.couponfoundry.Model.register_device;
import com.couponfoundry.Model.response_token;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by nirmal on 09/01/17.
 */

public interface APIInterface {
    @Headers("Content-Type: application/json")
    @POST("/api/v1/members")
    Call<Response_isdevice_exist> isdeviceexist(@Body Post_isdeviceexist Post_continue);

    @Headers("Content-Type: application/json")
    @POST("/api/v1/members")
    Call<Response_isdevice_exist> Register(@Body register_device Post_continue);

    @Headers("Content-Type: application/json")
    @POST("/api/v1/token")
    Call<response_token> Get_token(@Body Post_to_get_token Post_continue);

    @Headers("Content-Type: application/json")
    @POST("/api/v1/activity")
    Call<Response_isdevice_exist> Activity(@Body Post_activity Activity);

    @Headers("Content-Type: application/json")
    @POST("/api/v1/activity")
    Call<Response_isdevice_exist> Activity_view_offer(@Body Post_activity_view_offer Activity);


    @Headers("Content-Type: application/json")
    @POST("/api/v1/offers")
    Call<Offer_list> Offer_list(@Body Post_offer_list Activity);

    @Headers("Content-Type: application/json")
    @POST("/api/v1/offers")
    Call<Response_view_offer> View_offer(@Body Post_view_offer View_offer);

    @Headers("Content-Type: application/json")
    @POST("/api/v1/myoffers")
    Call<Response_view_offer> Save_offer(@Body Post_view_offer Save_offer);

    @Headers("Content-Type: application/json")
    @POST("/api/v1/myoffers")
    Call<Offer_list> View_Save_offer(@Body Post_offer_list Save_offer);

    @Headers("Content-Type: application/json")
    @POST("/api/v1/myoffers")
    Call<Response_view_offer> Redeem_offer_view(@Body Post_view_offer View_offer);

    @Headers("Content-Type: application/json")
    @POST("/api/v1/notification")
    Call<Response_view_offer> Notification(@Body Post_notification View_offer);

    @Headers("Content-Type: application/json")
    @POST("/api/v1/members")
    Call<Response_view_offer> Logout(@Body Post_logout Post_logout);
}
