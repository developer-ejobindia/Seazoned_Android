package com.seazoned.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.seazoned.R;
import com.seazoned.service.util.PixelUtil;

import java.util.ArrayList;
import java.util.HashMap;


public class MowingAcrAdapter extends ArrayAdapter<HashMap<String, String>> {

    private Context context;
    private ArrayList<HashMap<String, String>> arrayList;

    public MowingAcrAdapter(Context context, int resource,
                            ArrayList<HashMap<String, String>> arrayList) {
        super(context, resource, arrayList);
        this.context = context;
        this.arrayList = arrayList;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // return super.getView(position, convertView, parent);

        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(R.layout.spinner_item, parent, false);
        }
        TextView mTvItem = (TextView) row.findViewById(R.id.tv_main);
        mTvItem.setTextColor(ContextCompat.getColor(context, R.color.colorText));
        mTvItem.setText(arrayList.get(position).get("service_field_value"));


        return row;

    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(R.layout.spinner_item, parent, false);
        }
        TextView mTvItem = (TextView) row.findViewById(R.id.tv_main);
        /*if (position<= mCountryList.size()-2)
        mTvItem.setBackgroundResource(R.drawable.spinner_item_divider);
        else*/
        mTvItem.setBackgroundColor(Color.parseColor("#FFFFFF"));        //mTvItem.setBackgroundColor(Color.parseColor("#1e2028"));
        //mTvItem.setTextColor(Color.parseColor("#FFFFFF"));
        mTvItem.setPadding(PixelUtil.dpToPx(context, 10), PixelUtil.dpToPx(context, 15), PixelUtil.dpToPx(context, 10), PixelUtil.dpToPx(context, 15));
        mTvItem.setTextColor(ContextCompat.getColor(context, R.color.colorText));
        mTvItem.setText(arrayList.get(position).get("service_field_value"));
        return row;
    }

}
