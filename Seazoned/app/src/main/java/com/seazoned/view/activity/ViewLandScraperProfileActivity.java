package com.seazoned.view.activity;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;

import com.seazoned.R;
import com.seazoned.app.AppData;
import com.seazoned.databinding.ActivityViewLandScraperProfileBinding;
import com.seazoned.databinding.ServiceHoursRowBinding;
import com.seazoned.service.api.Api;
import com.seazoned.service.parser.PostDataParser;
import com.seazoned.service.util.Util;
import com.seazoned.view.adapter.RcvImageOneAdapter;
import com.seazoned.view.adapter.SearchDetailsRatingAdapter;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ViewLandScraperProfileActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityViewLandScraperProfileBinding mBinding;
    String landscaperId;
    private String serviceId;
    private String addedServiceId;
    private ArrayList<HashMap<String, String>> mRatingList;
    private String favorite_status = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_view_land_scraper_profile);
        Util.setPadding(this, mBinding.mainLayout);
        mBinding.lnBack.setOnClickListener(this);
        mBinding.ivFavorite.setOnClickListener(this);
        mBinding.rcvRatingList.setLayoutManager(new LinearLayoutManager(this));

        mBinding.rcvRatingList.setNestedScrollingEnabled(false);

        landscaperId = getIntent().getExtras().getString("lanscaper_id");
    }

    @Override
    protected void onResume() {
        super.onResume();
        HashMap<String, String> params = new HashMap<>();
        params.put("landscaper_id", landscaperId);
        getSearchDetails(params);
    }


    @Override
    public void onClick(View view) {
        if (view == mBinding.lnBack) {
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        } else if (view == mBinding.ivFavorite) {
            if (favorite_status.equalsIgnoreCase("0")) {
                HashMap<String, String> params = new HashMap<>();
                params.put("landscaper_id", landscaperId);
                params.put("status", "1");
                editHeartFavorite(Api.sAddFavorite, params);

            } else if (favorite_status.equalsIgnoreCase("1")) {
                HashMap<String, String> params = new HashMap<>();
                params.put("landscaper_id", landscaperId);
                params.put("status", "0");
                editHeartFavorite(Api.sRemoveFavorite, params);

            }
        }
    }

    private void getSearchDetails(HashMap<String, String> params) {

        new PostDataParser(ViewLandScraperProfileActivity.this, Api.sSearchDetails, params, true, new PostDataParser.OnGetResponseListner() {
            @Override
            public void onGetResponse(JSONObject response) {
                String msg, success;
                if (response != null) {
                    try {
                        msg = response.optString("msg");
                        success = response.optString("success");
                        if (success.equalsIgnoreCase("1")) {


                            JSONObject data = response.optJSONObject("data");
                            String total_rating = data.optString("total_rating");
                            String total_review = data.optString("total_review");
                            String overall_rating = data.optString("overall_rating");
                            mBinding.ratingsAndReviewsId.setText("(" + total_rating + " Ratings and " + total_review + " Reviews" + ")");
                            mBinding.overallRating.setText(overall_rating);
                            mBinding.overallRatingAgain.setText(overall_rating);


                            JSONObject all_rating = data.optJSONObject("all_rating");
                            int fifth_rating = all_rating.optInt("5");
                            int forth_rating = all_rating.optInt("4");
                            int third_rating = all_rating.optInt("3");
                            int second_rating = all_rating.optInt("2");
                            int first_rating = all_rating.optInt("1");

                            mBinding.progressBar5.setMax(Integer.parseInt(total_rating));
                            mBinding.progressBar5.setProgress(fifth_rating);

                            mBinding.progressBar4.setMax(Integer.parseInt(total_rating));
                            mBinding.progressBar4.setProgress(forth_rating);

                            mBinding.progressBar3.setMax(Integer.parseInt(total_rating));
                            mBinding.progressBar3.setProgress(third_rating);

                            mBinding.progressBar2.setMax(Integer.parseInt(total_rating));
                            mBinding.progressBar2.setProgress(second_rating);

                            mBinding.progressBar1.setMax(Integer.parseInt(total_rating));
                            mBinding.progressBar1.setProgress(first_rating);

                            JSONArray imageArray = data.optJSONArray("landscaper_service_images");
                            if (imageArray.length() > 0) {
                                //commented out by me kamalika

                               /* ArrayList<String> imageList = new ArrayList<>();
                                mBinding.imgDetails.setVisibility(View.GONE);
                                mBinding.vpImage.setVisibility(View.VISIBLE);
                                for (int j = 0; j < imageArray.length(); j++) {
                                    imageList.add(imageArray.optJSONObject(j).getString("service_image"));
                                }
                                setImage(imageList);*/

                                //for recycler view

                                final ArrayList<String> imageList = new ArrayList<>();
                                for (int i = 0; i < imageArray.length(); i++) {
                                    imageList.add(imageArray.optJSONObject(i).getString("service_image"));
                                }
                                RcvImageOneAdapter image1Adapter;

                                mBinding.rcvImageSearch.setVisibility(View.VISIBLE);
                                mBinding.rcvImageSearch.setLayoutManager(new GridLayoutManager(ViewLandScraperProfileActivity.this, 2));
                                mBinding.rcvImageSearch.setNestedScrollingEnabled(false);
                                mBinding.rcvImageSearch.setAdapter(image1Adapter = new RcvImageOneAdapter(ViewLandScraperProfileActivity.this, imageList));
                                mBinding.tvNoDataFoundSearch.setVisibility(View.GONE);
                                image1Adapter.setOnItemClickListner(new RcvImageOneAdapter.OnItemClickListner() {
                                    @Override
                                    public void onItemClick(int position) {
                                        Util.getPopUpWindow(ViewLandScraperProfileActivity.this, imageList.get(position));

                                    }
                                });
                            } else {

                                mBinding.rcvImageSearch.setVisibility(View.GONE);
                                mBinding.tvNoDataFoundSearch.setVisibility(View.VISIBLE);
                            }













                            /*else
                        {
                            mBinding.imgDetails.setVisibility(View.VISIBLE);
                            mBinding.vpImage.setVisibility(View.GONE);
                        }*/


                            JSONArray serviceTime = data.optJSONArray("service_time");
                            mBinding.lnServiceHours.removeAllViews();
                            for (int i = 0; i < serviceTime.length(); i++) {
                                JSONObject jsonObject1 = serviceTime.optJSONObject(i);
                                String service_day = jsonObject1.optString("service_day");
                                String start_time = jsonObject1.optString("start_time");
                                String end_time = jsonObject1.optString("end_time");
                                ServiceHoursRowBinding binding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.service_hours_row, mBinding.lnServiceHours, true);
                                binding.tvDay.setText(service_day);

                                //binding.tvTime.setText(Util.changeAnyDateFormat(start_time,"hh:mm:ss","hh:mm a") + " - " +Util.changeAnyDateFormat(end_time,"hh:mm:ss","hh:mm a"));
                                binding.tvTime.setText(setTimeAmPm(start_time) + " - " + setTimeAmPm(end_time));
                            }

                            JSONArray landscapperInfo = data.optJSONArray("landscapper_info");
                            for (int i = 0; i < landscapperInfo.length(); i++) {
                                JSONObject jsonObject2 = landscapperInfo.optJSONObject(i);
                                serviceId = jsonObject2.optString("service_id");
                                addedServiceId = jsonObject2.optString("id");
                                String location = jsonObject2.optString("location");
                                String profile_image = jsonObject2.optString("profile_image");
                                String description = jsonObject2.optString("description");
                                favorite_status = jsonObject2.optString("favorite_status");
                                String name = jsonObject2.optString("name");
                                String user_id = jsonObject2.optString("user_id");
                                final String feature_image = jsonObject2.optString("feature_image");
                                mBinding.gardenAssistanceTeamId.setText(name);
                                mBinding.descriptionId.setText(description);
                                if (favorite_status.equalsIgnoreCase("0")) {
                                    mBinding.ivFavorite.setImageResource(R.drawable.ic_favourite);
                                } else if (favorite_status.equalsIgnoreCase("1")) {
                                    mBinding.ivFavorite.setImageResource(R.drawable.ic_heart);
                                }


                                if (location.equalsIgnoreCase("null") ||
                                        location.equalsIgnoreCase(null) ||
                                        location.equalsIgnoreCase("")) {
                                    mBinding.addressId.setText("---");
                                } else {
                                    mBinding.addressId.setText(location);
                                }

                                if (!TextUtils.isEmpty(profile_image)) {
                                    Picasso.with(ViewLandScraperProfileActivity.this).load(profile_image).error(R.drawable.icon_user).resize(200, 200).into(mBinding.ivProfileId);
                                } else {
                                    Picasso.with(ViewLandScraperProfileActivity.this).load(R.drawable.icon_user).resize(200, 200).into(mBinding.ivProfileId);
                                }
                                if (!TextUtils.isEmpty(feature_image)) {
                                    mBinding.ivFeatureImage.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Util.getPopUpWindow(ViewLandScraperProfileActivity.this, feature_image);
                                        }
                                    });
                                    Picasso.with(ViewLandScraperProfileActivity.this).load(feature_image).error(R.mipmap.featurepic).into(mBinding.ivFeatureImage);
                                } else {
                                    Picasso.with(ViewLandScraperProfileActivity.this).load(R.mipmap.featurepic).into(mBinding.ivFeatureImage);
                                }
                            }

                            mRatingList = new ArrayList<>();
                            JSONArray serviceRatingDetails = data.optJSONArray("service_rating_details");
                            for (int i = 0; i < serviceRatingDetails.length(); i++) {
                                JSONObject jsonObject4 = serviceRatingDetails.optJSONObject(i);
                                String rating_value = jsonObject4.optString("rating_value");
                                String review = jsonObject4.optString("review");
                                String first_name = jsonObject4.optString("first_name");
                                String last_name = jsonObject4.optString("last_name");
                                String profile_image = jsonObject4.optString("profile_image");

                                HashMap<String, String> hashMap = new HashMap<>();
                                hashMap.put("rating_value", rating_value);
                                hashMap.put("review", review);
                                hashMap.put("first_name", first_name);
                                hashMap.put("last_name", last_name);
                                hashMap.put("profile_image", profile_image);
                                mRatingList.add(hashMap);
                            }
                            mBinding.rcvRatingList.setAdapter(new SearchDetailsRatingAdapter(ViewLandScraperProfileActivity.this, mRatingList));

                            JSONArray servicePrices = data.optJSONArray("service_prices");
                            AppData.sRecurringServiceList = new ArrayList<>();
                            if (servicePrices.length() > 0) {
                                for (int i = 0; i < servicePrices.length(); i++) {
                                    JSONObject jsonObject3 = servicePrices.optJSONObject(i);
                                    String landscaper_id = jsonObject3.optString("landscaper_id");
                                    String discount_price = jsonObject3.optString("discount_price");
                                    String service_frequency = jsonObject3.optString("service_frequency");
                                    String service_price_id = jsonObject3.optString("id");
                                    HashMap<String, String> hashMap = new HashMap<>();
                                    if (i == servicePrices.length() - 1)
                                        hashMap.put("title", "Single Service");
                                    else
                                        hashMap.put("title", "Recurring Service");
                                    hashMap.put("days", service_frequency);
                                    hashMap.put("price", discount_price);
                                    hashMap.put("service_price_id", service_price_id);
                                    AppData.sRecurringServiceList.add(hashMap);
                                }

                            } else {
                        /*HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("title", "Single Service");
                        hashMap.put("days", "Just Once");
                        hashMap.put("price", "0.0");
                        hashMap.put("service_price_id", "");
                        AppData.sRecurringServiceList.add(hashMap);*/
                            }

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        });

    }

    private void editHeartFavorite(String url, HashMap<String, String> params) {
        new PostDataParser(ViewLandScraperProfileActivity.this, url, params, false, new PostDataParser.OnGetResponseListner() {
            @Override
            public void onGetResponse(JSONObject response) {
                String success, msg;
                if (response != null) {
                    try {
                        msg = response.optString("msg");
                        success = response.optString("success");
                        if (success.equalsIgnoreCase("1")) {
                            onResume();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        });
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        mBinding.lnBack.performClick();
    }

    private String setTimeAmPm(String time) {
        int hour = 0;
        int minute = 0;
        //String time = tvHideTime.getText().toString().trim();
        if (!time.equalsIgnoreCase("")) {
            if (time.contains(":")) {
                try {
                    hour = Integer.parseInt(time.split(":")[0]);
                    minute = Integer.parseInt(time.split(":")[1]);
                    String showTime = "";
                    String showMinute = "";
                    if (minute < 10) {
                        showMinute = "0" + minute;
                    } else {
                        showMinute = "" + minute;
                    }
                    //AM
                    if (hour == 0) {
                        showTime = "12:" + showMinute + " am";
                    } else if (hour != 0 && hour < 12) {
                        if (hour < 10)
                            showTime = "0" + hour + ":" + showMinute + " am";
                        else
                            showTime = hour + ":" + showMinute + " am";
                    }
                    //PM
                    else if (hour == 12) {
                        showTime = hour + ":" + showMinute + " pm";
                    } else if (hour > 12) {
                        hour = hour - 12;
                        if (hour < 10)
                            showTime = "0" + hour + ":" + showMinute + " pm";
                        else
                            showTime = hour + ":" + showMinute + " pm";
                    }
                    return showTime;
                    //tvShowTime.setText(showTime);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
        return time;
    }
}
