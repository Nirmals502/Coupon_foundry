package com.couponfoundry.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.couponfoundry.Model.Offer_list;
import com.couponfoundry.R;
import com.couponfoundry.View.Coupon_detail;
import com.couponfoundry.View.MainActivity;
import com.couponfoundry.View.Splash;

import java.util.ArrayList;

public class Coupon_list extends BaseAdapter {
    private Context context; //context
    private String[] items; //data source of the list adapter
    public ArrayList<Offer_list.Datum> data = new ArrayList<>();
    String Str_type = "";

    //public constructor
    public Coupon_list(Context context, ArrayList<Offer_list.Datum> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size(); //returns total of items in the list
    }

    @Override
    public Object getItem(int position) {
        return data.get(position); //returns list item at the specified position
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // inflate the layout for each list row
        ViewHolder__ viewHolder;
        final View result;
        if (convertView == null) {
            viewHolder = new ViewHolder__();
            convertView = LayoutInflater.from(context).
                    inflate(R.layout.coupon_list_xml, parent, false);

            viewHolder.Txt_program = (TextView) convertView.findViewById(R.id.Txt_program);
            viewHolder.Txt_retailor = (TextView) convertView.findViewById(R.id.txt_retailer);
            viewHolder.Txt_expriy = (TextView) convertView.findViewById(R.id.Txt_expiry_date);
            viewHolder.Btn_save = (Button) convertView.findViewById(R.id.button_save);
            viewHolder.Img_logo = (ImageView) convertView.findViewById(R.id.logo);

            result = convertView;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder__) convertView.getTag();
            result = convertView;

        }
        viewHolder.Txt_program.setText(data.get(position).program);
        viewHolder.Txt_retailor.setText(data.get(position).retailer);
        viewHolder.Txt_expriy.setText(data.get(position).expiry_date);
        viewHolder.Btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context,
                        Coupon_detail.class);
                i.putExtra("offerid", data.get(position).offer_id);
                context.startActivity(i);

            }
        });
        String img_logo = data.get(position).logo;
        byte[] imageByteArray = Base64.decode(img_logo, Base64.DEFAULT);

        Glide.with(context)
                .load(imageByteArray)
                .placeholder(R.drawable.coupon)
                .into(viewHolder.Img_logo);
        // returns the view for the current row
        return convertView;
    }
}

class ViewHolder__ {
    TextView Txt_program, Txt_retailor, Txt_expriy;
    Button Btn_save;
    ImageView Img_logo;
}

