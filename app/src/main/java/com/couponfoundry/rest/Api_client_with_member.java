package com.couponfoundry.rest;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Api_client_with_member {
    public static Retrofit retrofit = null;
    public static String Client = "z/9n}0YoMDl5";
    public static String Client_Secret = "0nRj$Zb$+UL=";
    public static String Str_lat = "";
    public static String Str_lng = "";
    public static String Country_name = "";
    public static String City_name = "";


    public static Retrofit getClient(Context mContext) {
        String Str_token = Get_tocken(mContext);
        // String Str_token = "3C44B48850AF799094F663E2F1603E6B67926D3767EA827200D2CCF9DCAA0B827EA11E9DE0D45B090FAAD6EF9A2C9BB779DC472302230E9ADC24499280CC0CD9";

        String member = Get_member(mContext);
//        GPSTracker gpsTracker = new GPSTracker(mContext);
//
//        Str_lat = String.valueOf(gpsTracker.latitude);
//        Str_lng = String.valueOf(gpsTracker.longitude);
//        Country_name = gpsTracker.getCountryName(mContext);
//        City_name = gpsTracker.getAddressLine(mContext);
        SharedPreferences pref = mContext.getSharedPreferences("Coupon_foundry", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();

//        Str_lat = pref.getString("Lat", "");
//        Str_lng = pref.getString("Lng", "");
        Str_lat = pref.getString("Latnew", "novalue");
        // Str_lng = pref.getString("Lngnew", "");
        if (Str_lat.contentEquals("novalue") || Str_lat.contentEquals("")) {
            Str_lat = pref.getString("Lat", "");
            Str_lng = pref.getString("Lng", "");
            Country_name = pref.getString("country_name", "");
        } else {
            Str_lat = pref.getString("Latnew", "");
            Str_lng = pref.getString("Lngnew", "");
            Country_name = pref.getString("country_name", "");
        }


        // Country_name = pref.getString("country_name", "");
        //System.out.println("Location.............................." + Str_lat + ",-" + Str_lng + Country_name + ",,,,");
        System.out.println("");

        OkHttpClient.Builder oktHttpClient = new OkHttpClient.Builder();

        oktHttpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                Request request = original.newBuilder()
                        .addHeader("Authorization",
                                Str_token)
                        .addHeader("Accept", "application/json, text/plain, */*")

                        .addHeader("channel", "Airos Group")
                        .addHeader("brand", "xWrist")
//                        .addHeader("channel", "QA")
//                        .addHeader("brand", "QATest")
                        .addHeader("country", Country_name)
                        .addHeader("language", "us-en")
                        .addHeader("location", Str_lat + "," + Str_lng)
                        //.addHeader("location", "43.231142" + "," + "-79.702862")
                        //.addHeader("Member", "100000007")
                        .addHeader("Member", member)
                        .addHeader("Content-Type", "application/json;charset=utf-8")
                        .method(original.method(), original.body())
                        .build();
                //  Response response = request.;
                Response response = chain.proceed(chain.request());
                try {
                    if (response.code() == 307) {
                        request = request.newBuilder()
                                .url(response.header("Location"))
                                .build();
                        response = chain.proceed(request);

                        return response;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return chain.proceed(request);
                //  return response;
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

    public static String Get_member(Context ctx) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences((ctx));
        String member = prefs.getString("member", "");
        return member;
    }


}
