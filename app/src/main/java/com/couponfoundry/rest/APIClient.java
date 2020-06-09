package com.couponfoundry.rest;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;

import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by anupamchugh on 05/01/17.
 */

public class APIClient {

    public static Retrofit retrofit = null;
    public static String Client = "z/9n}0YoMDl5";
    public static String Client_Secret = "0nRj$Zb$+UL=";


    public static Retrofit getClient(Context mContext) {
        String Str_token = Get_tocken(mContext);
        //String Str_token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX2lkIjoiMTQ0IiwiZGV2aWNlX2lkIjoiYTVjZjA1MDZhZmM4MzM1NiIsImFwcF9uYW1lIjoiQ292aWQtMTkiLCJ1c2VyIjp7IklEIjoxNDQsIkZJUlNUX05BTUUiOiJOaXJtYWwiLCJTVVJOQU1FIjoiU2luZ2giLCJHRU5ERVIiOiJGIiwiQ0VMTCI6Ijk5MTQ1NjQwNTgiLCJFTUFJTCI6bnVsbCwiUEhPVE8iOm51bGwsIlBBU1NXT1JEIjoiIiwiU1RBVFVTIjoyLCJMQVNUX1VQREFURURfT04iOiIyMDIwLTA0LTA2IiwiTEFTVF9VUERBVEVEX0JZX0lEIjpudWxsLCJMQVNUX1VQREFURURfQllfVEVYVCI6bnVsbCwiQ1JFQVRFRF9PTiI6IjIwMjAtMDQtMDYiLCJDUkVBVEVEX09OX0JZX0lEIjpudWxsLCJDUkVBVEVEX09OX0JZX1RFWFQiOm51bGwsIkRFRkFVTFRfV09SS1BMQUNFIjoiIiwiSldUX1RPS0VOIjpudWxsLCJOQVRJT05BTF9JRCI6IjE0MjMyMTMyIn0sInRpbWUiOiIyMDIwLTA0LTA3VDEwOjAyOjQ1LjY5MVoiLCJpYXQiOjE1ODYyNTM3NjV9.xAbYcR2l0Xb07BgcmMf_sNJR5seD2VRPyI-C5LeNNtQ";
        OkHttpClient.Builder oktHttpClient = new OkHttpClient.Builder();
        //String authToken = Credentials.basic(Client, Client_Secret);
        oktHttpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                Request request = original.newBuilder()
                        .addHeader("Authorization",
                                Str_token)
                        //.addHeader("Accept", "application/json, text/plain, */*")
                        .addHeader("channel", "Airos Group")
                        .addHeader("brand", "xWrist")
                        .addHeader("country", "us")
                        .addHeader("language", "us-en")
                        .addHeader("location", "+37.33240905,-122.03051211")
                        .addHeader("Content-Type", "application/json;charset=utf-8")
                        .method(original.method(), original.body())
                        .build();

                Response response = chain.proceed(chain.request());
                if (response.code() == 307) {
                    request = request.newBuilder()
                            .url(response.header("Location"))
                            .build();
                    response = chain.proceed(request);
                    return response;
                }
                return chain.proceed(request);
            }
        });
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        Gson gson = null;
        try {
            gson = new GsonBuilder().setLenient().create();
        } catch (IllegalStateException | JsonSyntaxException exception) {

        }


        retrofit = new Retrofit.Builder()
                .baseUrl("https://uat.couponfoundry.io")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(oktHttpClient.build())
                .build();


        return retrofit;
    }

    public static String Get_tocken(Context ctx) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences((ctx));
        String Str_tocken = prefs.getString("token", "");
        return Str_tocken;
    }
}
