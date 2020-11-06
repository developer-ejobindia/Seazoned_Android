package com.seazoned.view.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.seazoned.R;
import com.seazoned.databinding.RatingRowBinding;
import com.seazoned.model.ServiceListModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by root on 28/3/18.
 */

public class RatingListAdapter extends RecyclerView.Adapter<RatingListAdapter.BindingHolder> {
    private Context context;
    private ArrayList<HashMap<String, String>> mRatingList;

    public RatingListAdapter(Context context, ArrayList<HashMap<String, String>> mRatingList) {
        this.context = context;
        this.mRatingList = mRatingList;
    }

    @Override
    public BindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RatingRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.rating_row, parent, false);
        return new BindingHolder(binding);
    }

    @Override
    public void onBindViewHolder(BindingHolder holder, int position) {
        holder.binding.tvName.setText(mRatingList.get(position).get("name"));
        holder.binding.tvDate.setText(mRatingList.get(position).get("date"));
        holder.binding.tvReview.setText(mRatingList.get(position).get("review"));
        holder.binding.ratingbar.setStarBorderWidth(1.0f);
        if (mRatingList.get(position).get("profile_picture").equalsIgnoreCase("") ||
                mRatingList.get(position).get("profile_picture").equalsIgnoreCase("null") ||
                mRatingList.get(position).get("profile_picture").equalsIgnoreCase(null)
                ) {
            Picasso.with(context).load(R.drawable.icon_user).into(holder.binding.ivProfile);
        }
        else {
            Picasso.with(context).load(mRatingList.get(position).get("profile_picture")).resize(150,150).error(R.drawable.icon_user).into(holder.binding.ivProfile);
        }
        try {
            float rating=Float.parseFloat(mRatingList.get(position).get("rating"));
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

    public class BindingHolder extends RecyclerView.ViewHolder {
        RatingRowBinding binding;

        public BindingHolder(RatingRowBinding itemView) {
            super(itemView.lnMain);
            this.binding = itemView;
        }
    }
}
