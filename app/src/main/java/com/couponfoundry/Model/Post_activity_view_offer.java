package com.couponfoundry.Model;

import com.google.gson.annotations.SerializedName;

public class Post_activity_view_offer {
    @SerializedName("action")
    public String action;
    @SerializedName("activity")
    public String activity;
    @SerializedName("data")
    public String data;


    public Post_activity_view_offer(String action, String activity, String data) {
        this.action = action;
        this.activity = activity;
        this.data = data;

    }
}
