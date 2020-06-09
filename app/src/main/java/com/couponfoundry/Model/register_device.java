package com.couponfoundry.Model;

import com.google.gson.annotations.SerializedName;

public class register_device {
    @SerializedName("action")
    public String action;
    @SerializedName("device")
    public String device;
    @SerializedName("mobile")
    public String mobile;
    @SerializedName("fcm")
    public String fcm;

    public register_device(String action, String device,String mobile,String fcm) {
        this.action = action;
        this.device = device;
        this.mobile = mobile;
        this.fcm = fcm;
    }
}
