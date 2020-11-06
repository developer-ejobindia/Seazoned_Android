package com.seazoned.view.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.seazoned.R;
import com.seazoned.app.AppData;
import com.seazoned.databinding.ActivityBookingStepTwoBinding;
import com.seazoned.databinding.ServiceFrequencyRowBinding;
import com.seazoned.service.util.Util;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by root on 15/3/18.
 */

public class ServiceFrequencyAdapter extends RecyclerView.Adapter<ServiceFrequencyAdapter.BindingHolder> {
    private Context mContext;
    ActivityBookingStepTwoBinding binding;
    ArrayList<HashMap<String,String>> arrayList;
    int lastIndex=-1;
    private double price=0.0;

    public ServiceFrequencyAdapter(Context mContext, ActivityBookingStepTwoBinding binding,ArrayList<HashMap<String,String>> arrayList) {
        this.mContext = mContext;
        this.binding = binding;
        this.arrayList=arrayList;
        lastIndex =this.arrayList.size() - 1;
    }

    @Override
    public BindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BindingHolder((ServiceFrequencyRowBinding) DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.service_frequency_row, parent, false));
    }

    @Override
    public void onBindViewHolder(final BindingHolder holder, final int position) {
        HashMap<String, String> hashMap = arrayList.get(position);
        holder.binding.tvTitle.setText(hashMap.get("title"));
        holder.binding.tvDays.setText(hashMap.get("days"));
        try {
            price = Double.parseDouble(AppData.sTotalPrice) - Double.parseDouble(hashMap.get("price"));
            double tax = Double.parseDouble(AppData.sPercentageRate);
            holder.binding.tvPrice.setText("$ " + Util.getDecimalTwoPoint(price+((price*tax)/100)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (position == arrayList.size() - 1) {
            //holder.binding.tvPerService.setVisibility(View.GONE);
            holder.binding.tvPerService.setVisibility(View.VISIBLE);
            holder.binding.tvPerService.setText("");
        } else {
            holder.binding.tvPerService.setVisibility(View.VISIBLE);
        }
        if (lastIndex == position) {
            double tax=Double.parseDouble(AppData.sPercentageRate);
            binding.tvGrandTotal.setText("$ "+Util.getDecimalTwoPoint(price+((price*tax)/100)));
            binding.tvServicePrice.setText("$ "+Util.getDecimalTwoPoint(price));
            if (lastIndex == arrayList.size() - 1)
            {
                binding.tvServiceType.setText("Single");
            }
            else
            {
                binding.tvServiceType.setText("Recurring");
            }

            holder.binding.lnMain.setBackgroundColor(mContext.getResources().getColor(R.color.colorFlatRed));
            holder.binding.tvTitle.setTextColor(mContext.getResources().getColor(R.color.colorWhite));
            holder.binding.tvPrice.setTextColor(mContext.getResources().getColor(R.color.colorWhite));
            holder.binding.tvDays.setTextColor(mContext.getResources().getColor(R.color.colorWhite));
            holder.binding.tvPerService.setTextColor(mContext.getResources().getColor(R.color.colorWhite));
        } else {
            holder.binding.lnMain.setBackgroundColor(mContext.getResources().getColor(R.color.colorDeactivate));
            holder.binding.tvTitle.setTextColor(mContext.getResources().getColor(R.color.colorLabel));
            holder.binding.tvPrice.setTextColor(mContext.getResources().getColor(R.color.colorFlatRed));
            holder.binding.tvDays.setTextColor(mContext.getResources().getColor(R.color.colorText));
            holder.binding.tvPerService.setTextColor(mContext.getResources().getColor(R.color.colorLabel));
        }

        holder.binding.lnMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lastIndex = position;
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class BindingHolder extends RecyclerView.ViewHolder {
        ServiceFrequencyRowBinding binding;

        public BindingHolder(ServiceFrequencyRowBinding itemView) {
            super(itemView.frameLayout);
            this.binding = itemView;
        }
    }

    public HashMap<String, String> getServiceData() {
        if (lastIndex >= 0)
            return arrayList.get(lastIndex);
        else
            return null;
    }
    public String getPrice(){
        price = Double.parseDouble(AppData.sTotalPrice) - Double.parseDouble(arrayList.get(lastIndex).get("price"));
        double tax = Double.parseDouble(AppData.sPercentageRate);
        return "$ " + Util.getDecimalTwoPoint(price+((price*tax)/100));
    }
}
