package com.seazoned.view.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.google.android.gms.maps.StreetViewPanoramaView;
import com.seazoned.R;
import com.seazoned.databinding.ActivityBookingHistoryBinding;
import com.seazoned.model.BookingHistoryModel;
import com.seazoned.service.api.Api;
import com.seazoned.service.parser.GetDataParser;
import com.seazoned.service.util.Util;
import com.seazoned.view.adapter.BookingHistoryListAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class BookingHistoryActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityBookingHistoryBinding mBinding;
    private BookingHistoryListAdapter mHistoryListAdapter;
    private ArrayList<HashMap<String, String>> mHistoryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_booking_history);
        Util.setPadding(this, mBinding.mainLayout);
        mBinding.lnBack.setOnClickListener(this);
        mBinding.rcvHistoryList.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    public void onClick(View view) {
        if (view == mBinding.lnBack) {
            finish();
            startActivity(new Intent(BookingHistoryActivity.this, ProfileActivity.class));
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getBookingHistoryDetails();

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        mBinding.lnBack.performClick();
    }

    private void getBookingHistoryDetails() {
        new GetDataParser(BookingHistoryActivity.this, Api.sBookingHistory, true, new GetDataParser.OnGetResponseListner() {
            public String success;

            @Override
            public void onGetResponse(JSONObject response) {
                if (response != null) {
                    try {
                        success = response.optString("success");
                        if (success.equalsIgnoreCase("1")) {
                            mHistoryList = new ArrayList<>();
                            JSONArray data = response.optJSONArray("data");
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject jsonObject = data.optJSONObject(i);

                                HashMap<String, String> hashMap = new HashMap<>();

                                hashMap.put("bookingId", jsonObject.optString("order_id").trim());
                                hashMap.put("customer_id", jsonObject.optString("customer_id").trim());
                                hashMap.put("landscaper_id", jsonObject.optString("landscaper_id"));
                                hashMap.put("order_no", jsonObject.optString("order_no").trim());
                                hashMap.put("status", jsonObject.optString("status").trim());
                                hashMap.put("is_completed", jsonObject.optString("is_completed").trim());
                                hashMap.put("profile_image", jsonObject.optString("profile_image").trim());
                                hashMap.put("first_name", jsonObject.optString("first_name").trim());
                                hashMap.put("last_name", jsonObject.optString("last_name").trim());
                                hashMap.put("service_name", jsonObject.optString("service_name").trim());
                                hashMap.put("status_name", jsonObject.optString("status_name").trim());
                                hashMap.put("favourite_status", jsonObject.optString("favourite_status").trim());
                                hashMap.put("landscaper_name", jsonObject.optString("landscaper_name").trim());
                                hashMap.put("transaction_id", jsonObject.optString("transaction_id").trim());
                                hashMap.put("booking_time", jsonObject.optString("booking_time").trim());


                                //service image list
                                /*ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
                                BookingHistoryModel historyModel = new BookingHistoryModel(hashMap, arrayList);*/
                                mHistoryList.add(hashMap);
                            }
                            if (mHistoryList.size() > 0) {
                                mBinding.tvAlert.setVisibility(View.GONE);
                                mBinding.rcvHistoryList.setVisibility(View.VISIBLE);
                                mBinding.rcvHistoryList.setAdapter(new BookingHistoryListAdapter(BookingHistoryActivity.this, mHistoryList));
                            } else {
                                mBinding.tvAlert.setVisibility(View.VISIBLE);
                                mBinding.rcvHistoryList.setVisibility(View.GONE);
                            }
                        } else {
                            mBinding.tvAlert.setVisibility(View.VISIBLE);
                            mBinding.rcvHistoryList.setVisibility(View.GONE);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
