package com.seazoned.view.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.seazoned.R;
import com.seazoned.databinding.RatingRowSearchDetailsBinding;
import com.seazoned.service.util.Util;
import com.seazoned.view.activity.SearchDeatils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by root on 12/4/18.
 */

public class SearchDetailsRatingAdapter extends RecyclerView.Adapter<SearchDetailsRatingAdapter.ViewHolder> {
    private  Context context;
    private ArrayList<HashMap<String,String>> mRatingList;


    public SearchDetailsRatingAdapter(Context context,ArrayList<HashMap<String,String>> mRatingList) {
        this.context=context;
        this.mRatingList=mRatingList;
    }

    @Override
    public SearchDetailsRatingAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RatingRowSearchDetailsBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.rating_row_search_details, parent, false);
        return new ViewHolder(binding);
    }


    @Override
    public void onBindViewHolder(SearchDetailsRatingAdapter.ViewHolder holder, final int position) {
        holder.binding.tvName.setText(mRatingList.get(position).get("first_name")+" "+mRatingList.get(position).get("last_name"));
        holder.binding.tvReview.setText(mRatingList.get(position).get("review"));
        holder.binding.ratingbar.setStarBorderWidth(1.0f);
        if (mRatingList.get(position).get("profile_image").equalsIgnoreCase("") ||
                mRatingList.get(position).get("profile_image").equalsIgnoreCase("null") ||
                mRatingList.get(position).get("profile_image").equalsIgnoreCase(null)
                ) {

            Picasso.with(context).load(R.drawable.icon_user).into(holder.binding.ivProfile);
        }
        else {
            holder.binding.ivProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Util.getPopUpWindow(context,mRatingList.get(position).get("profile_image"));
                }
            });
            Picasso.with(context).load(mRatingList.get(position).get("profile_image")).resize(150,150).error(R.drawable.icon_user).into(holder.binding.ivProfile);
        }
        try{
            float rating=Float.parseFloat(mRatingList.get(position).get("rating_value"));
            holder.binding.ratingbar.setRating(rating);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return mRatingList.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder{
        RatingRowSearchDetailsBinding binding;

        public ViewHolder(RatingRowSearchDetailsBinding itemView) {
            super(itemView.lnMain);
            this.binding=itemView;

        }
    }
}
