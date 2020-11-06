package com.seazoned.view.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.seazoned.R;
import com.seazoned.databinding.ActivityAddressBookBinding;
import com.seazoned.databinding.AddressRowBinding;
import com.seazoned.service.api.Api;
import com.seazoned.service.parser.GetDataParser;
import com.seazoned.service.util.Util;
import com.seazoned.view.adapter.AddressListAdapter;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by root on 6/2/18.
 */

public class AddressBookActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivityAddressBookBinding mBinding;
    AddressListAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_address_book);
        Util.setPadding(this, mBinding.mainLayout);
        mBinding.lnBack.setOnClickListener(this);
        mBinding.tvAddAddress.setOnClickListener(this);
        mBinding.rcvAddressList.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new AddressListAdapter(this);
        mBinding.rcvAddressList.setAdapter(mAdapter);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            final String profileImage = bundle.getString("profile_image");
            String fullName = bundle.getString("full_name");
            mBinding.tvName.setText(fullName);
            if (!profileImage.equalsIgnoreCase("") && !profileImage.equalsIgnoreCase("null") && profileImage != null) {
                mBinding.ivProfilePic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Util.getPopUpWindow(AddressBookActivity.this,profileImage);
                    }
                });
                Picasso.with(AddressBookActivity.this).load(profileImage).error(R.mipmap.temp).resize(200, 200).into(mBinding.ivProfilePic);
            } else {
                Picasso.with(AddressBookActivity.this).load(R.mipmap.temp).into(mBinding.ivProfilePic);
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        getAddressDetails();

    }

    private void getAddressDetails() {

        new GetDataParser(AddressBookActivity.this, Api.sAddressList, true, new GetDataParser.OnGetResponseListner() {
            @Override
            public void onGetResponse(JSONObject response) {
                String msg, success;
                if (response != null) {
                    msg = response.optString("msg");
                    success = response.optString("success");
                    if (success.equalsIgnoreCase("1")) {
                        JSONObject data = response.optJSONObject("data");
                        JSONArray addresses = data.optJSONArray("addresses");
                        mAdapter.clearData();
                        for (int i = 0; i < addresses.length(); i++) {

                            JSONObject jsonObject1 = addresses.optJSONObject(i);
                            String id = jsonObject1.optString("id").trim();
                            String name = jsonObject1.optString("name").trim();
                            String address = jsonObject1.optString("address").trim();
                            String contact_number = jsonObject1.optString("contact_number").trim();
                            String email_address = jsonObject1.optString("email_address").trim();
                            String city = jsonObject1.optString("city").trim();
                            String state = jsonObject1.optString("state").trim();
                            String country = jsonObject1.optString("country").trim();

                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("addressId", id);
                            hashMap.put("name", name);
                            hashMap.put("address", address);
                            hashMap.put("phoneNo", contact_number);
                            hashMap.put("email", email_address);
                            hashMap.put("city", city);
                            hashMap.put("state", state);
                            hashMap.put("country", country);
                            mAdapter.addRow(hashMap);
                        }
                        mBinding.rcvAddressList.setVisibility(View.VISIBLE);
                        mBinding.tvAlert.setVisibility(View.GONE);
                        mAdapter.notifyDataSetChanged();
                    } else {
                        mBinding.rcvAddressList.setVisibility(View.GONE);
                        mBinding.tvAlert.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view == mBinding.lnBack) {
            finish();
            startActivity(new Intent(AddressBookActivity.this, ProfileActivity.class));
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        } else if (view == mBinding.tvAddAddress) {
            startActivity(new Intent(AddressBookActivity.this, AddEditAddressActivity.class)
                    .putExtra("mode", "add")
            );
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        mBinding.lnBack.performClick();
    }

}
