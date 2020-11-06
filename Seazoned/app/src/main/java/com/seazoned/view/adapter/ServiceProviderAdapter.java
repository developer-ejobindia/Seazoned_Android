package com.seazoned.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.seazoned.R;
import com.seazoned.app.AppData;
import com.seazoned.databinding.ProviderRowBinding;
import com.seazoned.model.ServiceProviderModel;
import com.seazoned.service.api.Api;
import com.seazoned.service.parser.PostDataParser;
import com.seazoned.service.util.Util;
import com.seazoned.view.activity.SearchDeatils;
import com.seazoned.view.activity.ServiceProviderActivity;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Souvik on 19-02-2018.
 */

public class ServiceProviderAdapter extends RecyclerView.Adapter<ServiceProviderAdapter.ViewHolder> {
    private Context context;
    private ArrayList<ServiceProviderModel> mProviderList;

    public ServiceProviderAdapter(Context context, ArrayList<ServiceProviderModel> mProviderList) {
        this.context = context;
        this.mProviderList = mProviderList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ProviderRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.provider_row, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.binding.tvMinPrice.setText("$"+Util.getDecimalTwoPoint(Double.parseDouble(mProviderList.get(position).getMin_price())+
                (Double.parseDouble(AppData.sPercentageRate)*Double.parseDouble(mProviderList.get(position).getMin_price()))/100));
        holder.binding.tvTitle.setText(mProviderList.get(position).getName());
        //holder.binding.tvProviderLocation.setText(mProviderList.get(position).getProviderLocation());
        holder.binding.tvProviderLocation.setText(mProviderList.get(position).getCity()+", "+mProviderList.get(position).getState());
        holder.binding.tvrating.setText(mProviderList.get(position).getRatings());
        holder.binding.tvUserCount.setText(mProviderList.get(position).getUserCount());
        final String sprofileImage = mProviderList.get(position).getProfileImage();
        if (!sprofileImage.equalsIgnoreCase("") && !sprofileImage.equalsIgnoreCase("null") && sprofileImage != null) {

            Picasso.with(context).load(sprofileImage).error(R.mipmap.feature_image).into(holder.binding.ivProfileLogo);
        } else {
            Picasso.with(context).load(R.mipmap.feature_image).into(holder.binding.ivProfileLogo);
        }
        holder.binding.lnproviderrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //((Activity)context).finish();

                if (!TextUtils.isEmpty(mProviderList.get(position).getId())) {
                    context.startActivity(new Intent(context, SearchDeatils.class)
                            .putExtra("landscaper_id", mProviderList.get(position).getId())
                    );
                    ((Activity) context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }

            }
        });
        if (mProviderList.get(position).getFavouriteStatus().equalsIgnoreCase("1")) {
            holder.binding.ivHeart.setImageResource(R.drawable.ic_favourite_yellow);
        } else if (mProviderList.get(position).getFavouriteStatus().equalsIgnoreCase("0")) {
            holder.binding.ivHeart.setImageResource(R.drawable.ic_favourite);
        }
        holder.binding.ivHeart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mProviderList.get(position).getFavouriteStatus().equalsIgnoreCase("0")) {
                    HashMap<String, String> params = new HashMap<>();
                    params.put("landscaper_id", mProviderList.get(position).getId());
                    params.put("status", "1");
                    editHeartFavorite(Api.sAddFavorite, params);

                } else if (mProviderList.get(position).getFavouriteStatus().equalsIgnoreCase("1")) {
                    HashMap<String, String> params = new HashMap<>();
                    params.put("landscaper_id", mProviderList.get(position).getId());
                    params.put("status", "0");
                    editHeartFavorite(Api.sRemoveFavorite, params);

                }

            }


        });


    }

    @Override
    public int getItemCount() {
        return mProviderList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ProviderRowBinding binding;

        public ViewHolder(ProviderRowBinding binding) {
            super(binding.lnproviderrow);
            this.binding = binding;
        }
    }

    private void editHeartFavorite(String url, HashMap<String, String> params) {
        new PostDataParser(context, url, params, false, new PostDataParser.OnGetResponseListner() {
            @Override
            public void onGetResponse(JSONObject response) {
                String success, msg;
                if (response != null) {
                    try {
                        msg = response.optString("msg");
                        success = response.optString("success");
                        if (success.equalsIgnoreCase("1")) {
                            ((ServiceProviderActivity)context).getData();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        });
    }
}
