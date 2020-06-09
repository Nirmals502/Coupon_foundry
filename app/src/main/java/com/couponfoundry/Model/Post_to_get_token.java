package com.couponfoundry.Model;

import com.google.gson.annotations.SerializedName;

public class Post_to_get_token {
    @SerializedName("action")
    public String action;
    @SerializedName("client")
    public String client;
    @SerializedName("key")
    public String key;

    public Post_to_get_token(String action, String client, String key) {
        this.action = action;
        this.client = client;
        this.key = key;

    }
}
