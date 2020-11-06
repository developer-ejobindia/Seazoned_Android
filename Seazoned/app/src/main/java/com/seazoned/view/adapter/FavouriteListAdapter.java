package com.seazoned.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.seazoned.R;
import com.seazoned.databinding.FavouriteListRowBinding;
import com.seazoned.databinding.ProviderRowBinding;
import com.seazoned.model.ServiceProviderModel;
import com.seazoned.service.api.Api;
import com.seazoned.service.parser.PostDataParser;
import com.seazoned.view.activity.FavouriteActivity;
import com.seazoned.view.activity.SearchDeatils;
import com.seazoned.view.activity.ServiceProviderActivity;
import com.seazoned.view.activity.ViewLandScraperProfileActivity;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Souvik on 19-02-2018.
 */

public class FavouriteListAdapter extends RecyclerView.Adapter<FavouriteListAdapter.ViewHolder> {
    private Context context;
    private ArrayList<HashMap<String,String>> favouriteList;

    public FavouriteListAdapter(Context context, ArrayList<HashMap<String,String>> favouriteList) {
        this.context = context;
        this.favouriteList = favouriteList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        FavouriteListRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.favourite_list_row, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        HashMap<String,String> hashMap=favouriteList.get(position);
        final String fetureImage=hashMap.get("feature_image");

        if (!fetureImage.equalsIgnoreCase("") && !fetureImage.equalsIgnoreCase("null") && fetureImage != null) {
            Picasso.with(context).load(fetureImage).error(R.mipmap.feature_image).into(holder.binding.ivFeatureImage);
        } else {
            Picasso.with(context).load(R.mipmap.feature_image).into(holder.binding.ivFeatureImage);
        }
        holder.binding.tvTitle.setText(hashMap.get("name"));
        holder.binding.tvAddress.setText(hashMap.get("address"));
        holder.binding.tvrating.setText(hashMap.get("rating"));
        holder.binding.tvUserCount.setText(hashMap.get("usercount"));
        holder.binding.tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String,String> params=new HashMap<>();
                params.put("landscaper_id", favouriteList.get(position).get("lanscaper_id"));
                params.put("status", "1");
                removeFavourite(Api.sRemoveFavorite,params);
            }
        });

        holder.binding.lnproviderrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, ViewLandScraperProfileActivity.class).putExtra("lanscaper_id",favouriteList.get(position).get("lanscaper_id")));
                ((Activity) context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

    }

    @Override
    public int getItemCount() {
        return favouriteList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private FavouriteListRowBinding binding;

        public ViewHolder(FavouriteListRowBinding binding) {
            super(binding.lnproviderrow);
            this.binding = binding;
        }
    }

    private void removeFavourite(String url, HashMap<String, String> params) {
        new PostDataParser(context, url, params, false, new PostDataParser.OnGetResponseListner() {
            @Override
            public void onGetResponse(JSONObject response) {
                String success, msg;
                if (response != null) {
                    try {
                        msg = response.optString("msg");
                        success = response.optString("success");
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                        if (success.equalsIgnoreCase("1")) {
                            ((FavouriteActivity)context).getFavouriteList();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        });
    }
}
