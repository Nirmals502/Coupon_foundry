package com.couponfoundry.Model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Offer_list {
    public ArrayList<Datum> offers = new ArrayList<>();

    public class Datum {
        @SerializedName("offer_id")
        public String offer_id;
        @SerializedName("program")
        public String program;
        @SerializedName("expiry_date")
        public String expiry_date;
        @SerializedName("categories")
        public String categories;
        @SerializedName("keywords")
        public String keywords;
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
}
