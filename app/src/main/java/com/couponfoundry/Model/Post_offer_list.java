package com.couponfoundry.Model;

import com.google.gson.annotations.SerializedName;

public class Post_offer_list {
    @SerializedName("action")
    public String action;
    @SerializedName("program")
    public String program;
    @SerializedName("geolocation")
    public String geolocation;


    public Post_offer_list(String action, String program, String geolocation) {
        this.action = action;
        this.program = program;
        this.program = program;
        this.geolocation = geolocation;

    }
}
