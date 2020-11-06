package com.seazoned.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.seazoned.R;
import com.seazoned.app.AppData;
import com.seazoned.databinding.SelectImageRowBinding;
import com.seazoned.databinding.ServiceFrequencyRowBinding;
import com.seazoned.service.util.Util;
import com.seazoned.view.activity.SelectImageActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by root on 15/3/18.
 */

public class SelectImageAdapter extends RecyclerView.Adapter<SelectImageAdapter.BindingHolder> {
    private Context mContext;

    public SelectImageAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public BindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BindingHolder((SelectImageRowBinding) DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.select_image_row, parent, false));
    }

    @Override
    public void onBindViewHolder(final BindingHolder holder, final int position) {
        if (position == AppData.sImageList.size()) {
            holder.binding.lnDelete.setVisibility(View.GONE);
            holder.binding.ivImage.setVisibility(View.GONE);
            holder.binding.lnAddImage.setVisibility(View.VISIBLE);
        } else {
            holder.binding.lnDelete.setVisibility(View.VISIBLE);
            holder.binding.ivImage.setImageBitmap(AppData.sImageList.get(position).getImageBitmap());
            holder.binding.ivImage.setVisibility(View.VISIBLE);
            holder.binding.lnAddImage.setVisibility(View.GONE);
            holder.binding.ivImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    View v = LayoutInflater.from(mContext).inflate(R.layout.image_view_row, null);
                    ImageView imageView = v.findViewById(R.id.ivImageRow);
                    ImageView ivCancel = v.findViewById(R.id.ivCancel);

                    final PopupWindow popupWindow = new PopupWindow(v, ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT, true);
                    imageView.setImageBitmap(AppData.sImageList.get(position).getImageBitmap());


                    popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
                    ivCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            popupWindow.dismiss();
                        }
                    });
                }
            });
        }
        holder.binding.lnAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mContext.startActivity(new Intent(mContext, SelectImageActivity.class));
                ((Activity) mContext).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        holder.binding.lnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AppData.sImageList!=null&&AppData.sImageList.size()>0){
                    AppData.sImageList.remove(position);
                    notifyDataSetChanged();

                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return AppData.sImageList.size() + 1;
    }

    public class BindingHolder extends RecyclerView.ViewHolder {
        SelectImageRowBinding binding;

        public BindingHolder(SelectImageRowBinding itemView) {
            super(itemView.frameLayout);
            this.binding = itemView;
        }
    }
}
