package com.seazoned.view.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.seazoned.R;
import com.seazoned.app.AppData;
import com.seazoned.databinding.ActivityViewProfileBinding;
import com.seazoned.model.User;
import com.seazoned.service.api.Api;
import com.seazoned.service.parser.GetDataParser;
import com.seazoned.service.preference.SharedPreferenceHelper;
import com.seazoned.service.util.Util;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

public class ViewProfileActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityViewProfileBinding mBinding;
    private String firstName = "";
    private String lastName = "";
    private String email = "";
    private String dateOfBirth = "";
    private String phoneNumber = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_view_profile);
        Util.setPadding(this, mBinding.mainLayout);
        mBinding.lnBack.setOnClickListener(this);
        mBinding.tvEditProfile.setOnClickListener(this);
        mBinding.tvProfilePic.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserInfo();

    }

    private void getUserInfo() {
        new GetDataParser(ViewProfileActivity.this, Api.sUserInfo, true, new GetDataParser.OnGetResponseListner() {
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

                            firstName = data.optString("first_name");
                            lastName = data.optString("last_name");

                            SharedPreferenceHelper helper=SharedPreferenceHelper.getInstance(ViewProfileActivity.this);
                            User user=new User(AppData.sUserId,AppData.sToken,firstName,lastName,helper.getUserLoginType());
                            helper.saveUserInfo(user);

                            email = data.optString("email");
                            dateOfBirth = data.optString("date_of_birth");
                            phoneNumber = data.optString("phone_number");
                            final String profileImage = data.optString("profile_image");
                            if (!profileImage.equalsIgnoreCase("") && !profileImage.equalsIgnoreCase("null") && profileImage != null) {
                                mBinding.ivProfilePic.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Util.getPopUpWindow(ViewProfileActivity.this,profileImage);
                                    }
                                });
                                Picasso.with(ViewProfileActivity.this).load(profileImage).error(R.mipmap.temp).resize(512,512).into(mBinding.ivProfilePic);
                            } else {
                                Picasso.with(ViewProfileActivity.this).load(R.mipmap.temp).into(mBinding.ivProfilePic);
                            }
                            mBinding.tvName.setText(firstName + " " + lastName);
                            mBinding.tvDateOfBirth.setText(Util.changeAnyDateFormat(dateOfBirth, "yyyy-MM-dd", "MMM-dd-yyyy"));
                            mBinding.tvNumber.setText(phoneNumber);
                            mBinding.tvEmail.setText(email);


                        } else {
                            Toast.makeText(ViewProfileActivity.this, "" + mStatus, Toast.LENGTH_SHORT).show();
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
            finish();
            startActivity(new Intent(ViewProfileActivity.this, ProfileActivity.class));
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        } else if (view == mBinding.tvEditProfile) {
            finish();
            startActivity(new Intent(ViewProfileActivity.this, EditProfileActivity.class)
                    .putExtra("fname", firstName)
                    .putExtra("lname", lastName)
                    .putExtra("dateofbirth", dateOfBirth)
                    .putExtra("phonenumber", phoneNumber));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

        }
        else if (view == mBinding.tvProfilePic) {
            //finish();
            startActivity(new Intent(ViewProfileActivity.this, ImageUploadActivity.class));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        mBinding.lnBack.performClick();
    }
}
