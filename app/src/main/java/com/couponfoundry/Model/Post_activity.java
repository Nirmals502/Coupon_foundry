package com.couponfoundry.Model;

import com.google.gson.annotations.SerializedName;

public class Post_activity {
    @SerializedName("action")
    public String action;
    @SerializedName("activity")
    public String activity;


    public Post_activity(String action, String activity) {
        this.action = action;
        this.activity = activity;

    }
}
