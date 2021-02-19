package com.couponfoundry.rest;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

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
    public static String Str_lat = "";
    public static String Str_lng = "";
    public static String Country_name = "";
    public static String City_name = "";
    private static LocationManager locationManager;
    private static String provider;
    private static My_location mylistener;
    private static Criteria criteria;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @SuppressLint("NewApi")

    public static Retrofit getClient(Context mContext) {
        String Str_token = Get_tocken(mContext);

        SharedPreferences pref = mContext.getSharedPreferences("Coupon_foundry", 0); // 0 - for private mode
        //SharedPreferences.Editor editor = pref.edit();

        Str_lat = pref.getString("Lat", "");
        Str_lng = pref.getString("Lng", "");
        Country_name = pref.getString("country_name", "");
        //GPSTracker gpsTracker = new GPSTracker(mContext);

//        Str_lat = String.valueOf(gpsTracker.latitude);
//        Str_lng = String.valueOf(gpsTracker.longitude);
//        Country_name = gpsTracker.getCountryName(mContext);
//
//
//        //City_name = gpsTracker.getAddressLine(mContext);
//        System.out.println("Location.............................." + Str_lat + ",-" + Str_lng+Country_name+",,,,"+City_name);

        // String Str_token = "3C44B48850AF799094F663E2F1603E6B67926D3767EA827200D2CCF9DCAA0B827EA11E9DE0D45B090FAAD6EF9A2C9BB779DC472302230E9ADC24499280CC0CD9";

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
                        .addHeader("country", Country_name)
                        .addHeader("language", "us-en")
                        .addHeader("location", Str_lat + "," + Str_lng)
                        //.addHeader("location", "34.0201613" + "," + "-118.691920")
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
