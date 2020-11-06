package com.seazoned.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.seazoned.R;

import com.seazoned.app.AppData;
import com.seazoned.databinding.ServiceRowBinding;
import com.seazoned.model.ServiceListModel;
import com.seazoned.view.activity.ServiceProviderActivity;


import java.util.ArrayList;

/**
 * Created by Souvik on 19-02-2018.
 */

public class ServiceListAdapter extends RecyclerView.Adapter<ServiceListAdapter.ViewHolder>{
 private Context context;
  private ArrayList<ServiceListModel> mServiceList;

    public ServiceListAdapter(Context context, ArrayList<ServiceListModel> mServiceList) {
        this.context = context;
        this.mServiceList = mServiceList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ServiceRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.service_row, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
              holder.binding.tvServiceName.setText(mServiceList.get(position).getServiceName());
              String sServiceId=mServiceList.get(position).getId();
                if(sServiceId.equals("1")){
               holder.binding.ivServiceLogo.setImageResource(R.drawable.ic_mowing_edging);
                }
                if(sServiceId.equals("2")){
                    holder.binding.ivServiceLogo.setImageResource(R.drawable.ic_leaf_removal);
                }
                if(sServiceId.equals("3")){
                    holder.binding.ivServiceLogo.setImageResource(R.drawable.ic_lawn_treatment);
                } if(sServiceId.equals("4")){
                holder.binding.ivServiceLogo.setImageResource(R.drawable.aeration);
                    } if(sServiceId.equals("5")){
                holder.binding.ivServiceLogo.setImageResource(R.drawable.ic_sprinkler);
                    } if(sServiceId.equals("6")){
                holder.binding.ivServiceLogo.setImageResource(R.drawable.ic_pool_cleaning);
                    } if(sServiceId.equals("7")){
                holder.binding.ivServiceLogo.setImageResource(R.drawable.ic_snow_removal);
                }

                holder.binding.lnServiceItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AppData.sServiceId=mServiceList.get(position).getId();
                        //((Activity)context).finish();
                        Intent intent = new Intent(view.getContext(), ServiceProviderActivity.class);
                        AppData.sServiceName=mServiceList.get(position).getServiceName();
                        intent.putExtra("ServiceName",mServiceList.get(position).getServiceName());
                        view.getContext().startActivity(intent);
                        ((Activity)context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                    }
                });

    }

    @Override
    public int getItemCount() {
        return mServiceList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ServiceRowBinding binding;

        public ViewHolder(ServiceRowBinding binding) {
            super(binding.lnservicerow);
            this.binding = binding;
        }
    }
}
