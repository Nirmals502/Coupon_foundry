package com.couponfoundry.Model;

import com.google.gson.annotations.SerializedName;

public class Post_view_offer {
    @SerializedName("action")
    public String action;
    @SerializedName("program")
    public String program;
    @SerializedName("offer_id")
    public String offer_id;


    public Post_view_offer(String action, String program, String offer_id) {
        this.action = action;
        this.program = program;
        this.offer_id = offer_id;
    }
}
