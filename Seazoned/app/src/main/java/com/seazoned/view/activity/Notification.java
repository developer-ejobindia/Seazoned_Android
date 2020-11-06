package com.seazoned.view.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.seazoned.R;
import com.seazoned.app.AppData;
import com.seazoned.databinding.ActivityNotificationBinding;
import com.seazoned.service.api.Api;
import com.seazoned.service.parser.GetDataParser;
import com.seazoned.service.preference.SharedPreferenceHelper;
import com.seazoned.view.adapter.NotificationListAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Notification extends AppCompatActivity implements View.OnClickListener {

    ActivityNotificationBinding mBinding;
    ArrayList<HashMap<String, String>> mNotificationList;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(Notification.this, R.layout.activity_notification);
        SharedPreferences sp = getSharedPreferences("Noti", MODE_PRIVATE);
        type = sp.getString("type", "");
        sp.edit().clear().apply();

        SharedPreferenceHelper helper = SharedPreferenceHelper.getInstance(getApplicationContext());
        String userId = helper.getUserId();
        String userToken = helper.getUserToken();
        if (!userId.equalsIgnoreCase("") && !userToken.equalsIgnoreCase("")) {
            AppData.sToken = userToken;
            AppData.sUserId = userId;

        }
        mBinding.rcvNotificationList.setLayoutManager(new LinearLayoutManager(this));
        mBinding.rcvNotificationList.setNestedScrollingEnabled(false);
        mBinding.lnBack.setOnClickListener(this);

        getNotificationList();
    }

    private void getNotificationList() {

        new GetDataParser(Notification.this, Api.sNotificationList, true, new GetDataParser.OnGetResponseListner() {
            @Override
            public void onGetResponse(JSONObject response) {
                String msg, success;
                if (response != null) {
                    try {
                        msg = response.optString("msg");
                        success = response.optString("success");
                        if (success.equalsIgnoreCase("1")) {


                            JSONObject data = response.optJSONObject("data");
                            mNotificationList = new ArrayList<HashMap<String, String>>();

                            JSONArray notificationListUser = data.optJSONArray("notification_list_user");
                            for (int i = 0; i < notificationListUser.length(); i++) {

                                JSONObject jsonObject = notificationListUser.optJSONObject(i);

                                String status = jsonObject.optString("status");
                                String name = jsonObject.optString("name");
                                String landscaper_name = jsonObject.optString("landscaper_name");
                                String profile_image = jsonObject.optString("profile_image");
                                String timestamp = jsonObject.optString("timestamp");

                                HashMap<String, String> hashMap = new HashMap<>();

                                hashMap.put("Status", status);
                                hashMap.put("Name", name);
                                hashMap.put("LandscaperName", landscaper_name);
                                hashMap.put("ProfileImage", profile_image);
                                hashMap.put("timestamp", timestamp);
                                hashMap.put("bookingId", jsonObject.optString("order_id"));
                                hashMap.put("landscaper_id", jsonObject.optString("landscaper_id"));

                                mNotificationList.add(hashMap);

                            }
                            if (mNotificationList.size() > 0) {
                                mBinding.tvAlert.setVisibility(View.GONE);
                                mBinding.rcvNotificationList.setVisibility(View.VISIBLE);
                                mBinding.rcvNotificationList.setAdapter(new NotificationListAdapter(Notification.this, mNotificationList));
                            } else {
                                mBinding.tvAlert.setVisibility(View.VISIBLE);
                                mBinding.rcvNotificationList.setVisibility(View.GONE);
                            }

                        } else {
                            mBinding.tvAlert.setVisibility(View.VISIBLE);
                            mBinding.rcvNotificationList.setVisibility(View.GONE);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }


            }
        });


    }

    @Override
    public void onClick(View view) {
        if (view == mBinding.lnBack) {


            if (type.equalsIgnoreCase("")) {
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            } else {
                finish();
                startActivity(new Intent(Notification.this, DashBoardActivity.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }


        }
    }

    @Override
    public void onBackPressed() {
        mBinding.lnBack.performClick();
    }
}
