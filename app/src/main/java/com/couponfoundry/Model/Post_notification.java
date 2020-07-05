package com.couponfoundry.Model;

import com.google.gson.annotations.SerializedName;

public class Post_notification {
    @SerializedName("action")
    public String action;
    @SerializedName("title")
    public String title;
    @SerializedName("text")
    public String text;


    public Post_notification(String action, String title, String text) {
        this.action = action;
        this.title = title;
        this.text = text;
    }
}
