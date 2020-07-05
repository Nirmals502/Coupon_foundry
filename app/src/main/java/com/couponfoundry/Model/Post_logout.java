package com.couponfoundry.Model;

import com.google.gson.annotations.SerializedName;

public class Post_logout {
    @SerializedName("action")
    public String action;
    @SerializedName("device")
    public String device;


    public Post_logout(String action, String device) {
        this.action = action;
        this.device = device;

    }
}
