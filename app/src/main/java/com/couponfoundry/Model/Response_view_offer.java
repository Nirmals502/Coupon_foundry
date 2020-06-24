package com.couponfoundry.Model;

import com.google.gson.annotations.SerializedName;

public class Response_view_offer {
    @SerializedName("offer_id")
    public String offer_id;
    @SerializedName("program")
    public String program;
    @SerializedName("offer_type")
    public String offer_type;
    @SerializedName("expiry_date")
    public String expiry_date;
    @SerializedName("keywords")
    public String keywords;
    @SerializedName("categories")
    public String categories;
    @SerializedName("retailer_id")
    public String retailer_id;
    @SerializedName("retailer")
    public String retailer;
    @SerializedName("logo")
    public String logo;
    @SerializedName("banner_title")
    public String banner_title;
    @SerializedName("banner_subtitle")
    public String banner_subtitle;
    @SerializedName("product_information")
    public String product_information;
}
