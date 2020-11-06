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


public class CountryAdapter extends ArrayAdapter<HashMap<String, String>> {

    private Context context;
    private ArrayList<HashMap<String, String>> mCountryList;

    public CountryAdapter(Context context, int resource,
                          ArrayList<HashMap<String, String>> mCourseList) {
        super(context, resource, mCourseList);
        this.context = context;
        this.mCountryList = mCourseList;
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
        mTvItem.setText(mCountryList.get(position).get("country_name"));


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
        mTvItem.setText(mCountryList.get(position).get("country_name"));
        return row;
    }

}
