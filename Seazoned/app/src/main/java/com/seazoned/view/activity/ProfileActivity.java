package com.seazoned.view.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.seazoned.R;
import com.seazoned.app.AppData;
import com.seazoned.databinding.ActivityProfileBinding;
import com.seazoned.model.User;
import com.seazoned.service.api.Api;
import com.seazoned.service.parser.GetDataParser;
import com.seazoned.service.preference.SharedPreferenceHelper;
import com.seazoned.service.util.Util;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityProfileBinding mBinding;
    private String profileImage = "";
    private String fullName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_profile);
        Util.setPadding(this, mBinding.mainLayout);
        mBinding.lnViewProfile.setOnClickListener(this);
        mBinding.lnAddressBook.setOnClickListener(this);
        mBinding.lnPaymentInfo.setOnClickListener(this);
        mBinding.lnLogOut.setOnClickListener(this);
        mBinding.lnBookingHistory.setOnClickListener(this);
        mBinding.lnFavourite.setOnClickListener(this);
        mBinding.lnMenu.setOnClickListener(this);

        mBinding.lnNotificationBell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivity.this,Notification.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserInfo();
        getNotiStatus();
    }

    private void getUserInfo() {
        new GetDataParser(ProfileActivity.this, Api.sUserInfo, true, new GetDataParser.OnGetResponseListner() {
            public String mStatus;
            public String mSuccess;

            @Override
            public void onGetResponse(JSONObject response) {
                if (response != null) {
                    try {
                        mSuccess = response.optString("success");
                        mStatus = response.optString("msg");
                        if (mSuccess.equalsIgnoreCase("1")) {
                            JSONObject data = response.getJSONObject("data");


                            fullName = data.optString("first_name").trim() + " " + data.optString("last_name").trim();
                            mBinding.tvName.setText(fullName);
                            profileImage = data.optString("profile_image");
                            if (!profileImage.equalsIgnoreCase("") && !profileImage.equalsIgnoreCase("null") && profileImage != null) {
                                mBinding.ivProfile.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Util.getPopUpWindow(ProfileActivity.this,profileImage);
                                    }
                                });
                                Picasso.with(ProfileActivity.this).load(profileImage).error(R.mipmap.temp).resize(200, 200).into(mBinding.ivProfile);
                            } else {
                                Picasso.with(ProfileActivity.this).load(R.mipmap.temp).into(mBinding.ivProfile);
                            }


                        } else {
                            Toast.makeText(ProfileActivity.this, "" + mStatus, Toast.LENGTH_SHORT).show();
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
        if (view == mBinding.lnViewProfile) {
            finish();
            startActivity(new Intent(ProfileActivity.this, ViewProfileActivity.class));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
        else if (view == mBinding.lnMenu) {
            mBinding.drawer.openDrawer(Gravity.LEFT);
        }
        else if (view == mBinding.lnFavourite) {
            finish();
            startActivity(new Intent(ProfileActivity.this, FavouriteActivity.class));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        } else if (view == mBinding.lnAddressBook) {
            finish();
            startActivity(new Intent(ProfileActivity.this, AddressBookActivity.class)
                    .putExtra("profile_image", profileImage)
                    .putExtra("full_name", fullName)
            );
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        } else if (view == mBinding.lnLogOut) {
            AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
            builder.setCancelable(true);
            builder.setMessage(getResources().getString(R.string.logout));
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    SharedPreferenceHelper helper = SharedPreferenceHelper.getInstance(ProfileActivity.this);
                    if (helper.clearData()) {
                        try {
                            Profile p = Profile.getCurrentProfile();
                            if (p != null) {
                                LoginManager.getInstance().logOut();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        finish();
                        startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    }
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.show();

        } else if (view == mBinding.lnPaymentInfo) {
            finish();
            startActivity(new Intent(ProfileActivity.this, PaymentInfoActivity.class));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
        else if (view==mBinding.lnBookingHistory){
            finish();
            startActivity(new Intent(ProfileActivity.this, BookingHistoryActivity.class));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        finish();
        startActivity(new Intent(ProfileActivity.this, DashBoardActivity.class));
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private void getNotiStatus() {

        new GetDataParser(ProfileActivity.this, Api.sGetNotificationStatus, true, new GetDataParser.OnGetResponseListner() {
            @Override
            public void onGetResponse(JSONObject response) {

                if (response != null) {
                    try {

                        String success = response.optString("success");
                        String notification_count = response.optString("notification_count");

                        if (success.equalsIgnoreCase("1")) {

                            if (notification_count.length() > 0) {
                                mBinding.ivNotiDot.setVisibility(View.VISIBLE);
                            } else {
                                mBinding.ivNotiDot.setVisibility(View.GONE);
                            }

                        } else {
                            mBinding.ivNotiDot.setVisibility(View.GONE);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
