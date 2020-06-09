package com.couponfoundry.Model;

import com.google.gson.annotations.SerializedName;

public class Post_isdeviceexist {
    @SerializedName("action")
    public String action;
    @SerializedName("device")
    public String device;


    public Post_isdeviceexist(String action, String device) {
        this.action = action;
        this.device = device;

    }
}
