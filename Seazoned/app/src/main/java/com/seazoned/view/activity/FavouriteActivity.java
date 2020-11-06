package com.seazoned.view.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;

import com.google.android.gms.maps.StreetViewPanoramaView;
import com.seazoned.R;
import com.seazoned.databinding.ActivityFavouriteBinding;
import com.seazoned.service.api.Api;
import com.seazoned.service.parser.GetDataParser;
import com.seazoned.service.util.Util;
import com.seazoned.view.adapter.FavouriteListAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class FavouriteActivity extends AppCompatActivity implements View.OnClickListener {
ActivityFavouriteBinding mBinding;
ArrayList<HashMap<String,String>> favouriteList=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding= DataBindingUtil.setContentView(this,R.layout.activity_favourite);
        Util.setPadding(this,mBinding.mainLayout);
        mBinding.lnBack.setOnClickListener(this);
        GridLayoutManager manager = new GridLayoutManager(this, 2);
        mBinding.rcvFavouriteList.setLayoutManager(manager);
        mBinding.rcvFavouriteList.setNestedScrollingEnabled(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getFavouriteList();
    }

    @Override
    public void onBackPressed() {
        mBinding.lnBack.performClick();
    }

    public void getFavouriteList() {
        new GetDataParser(FavouriteActivity.this, Api.sFavouriteList, true, new GetDataParser.OnGetResponseListner() {
            public String mStatus;
            public String mSuccess;

            @Override
            public void onGetResponse(JSONObject response) {
                if (response!=null){
                    try{
                        mSuccess = response.optString("success");
                        mStatus = response.optString("msg");
                        if(mSuccess.equalsIgnoreCase("1")){
                            JSONArray data=response.optJSONArray("data");
                            favouriteList=new ArrayList<>();
                            for (int i=0;i<data.length();i++){
                                JSONObject jsonObject=data.optJSONObject(i);
                                HashMap<String,String> hashMap=new HashMap<>();
                                hashMap.put("lanscaper_id",jsonObject.optString("lanscaper_id"));
                                hashMap.put("service_id",jsonObject.optString("service_id"));
                                hashMap.put("name",jsonObject.optString("name"));
                                hashMap.put("description",jsonObject.optString("description"));
                                hashMap.put("feature_image",jsonObject.optString("feature_image"));
                                hashMap.put("address",jsonObject.optString("address"));
                                hashMap.put("rating",jsonObject.optString("rating"));
                                hashMap.put("usercount",jsonObject.optString("usercount"));
                                hashMap.put("favorite_status",jsonObject.optString("favorite_status"));
                                favouriteList.add(hashMap);
                            }
                            mBinding.rcvFavouriteList.setVisibility(View.VISIBLE);
                            mBinding.tvAlert.setVisibility(View.GONE);
                            mBinding.rcvFavouriteList.setAdapter(new FavouriteListAdapter(FavouriteActivity.this,favouriteList));
                        }
                        else {
                            mBinding.rcvFavouriteList.setVisibility(View.GONE);
                            mBinding.tvAlert.setVisibility(View.VISIBLE);
                        }

                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view==mBinding.lnBack){
            finish();
            startActivity(new Intent(FavouriteActivity.this, ProfileActivity.class));
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }
    }
}
