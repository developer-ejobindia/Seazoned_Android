package com.seazoned.view.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.seazoned.R;
import com.seazoned.databinding.AerationSearchRowBinding;
import com.seazoned.databinding.LawnTreatmentSearchRowBinding;
import com.seazoned.databinding.LeafRemovalSearchRowBinding;
import com.seazoned.databinding.MowingEdgingSearchRowBinding;
import com.seazoned.databinding.PoolcleaingUpkeepSearchRowBinding;
import com.seazoned.databinding.SearchFilterRowBinding;
import com.seazoned.databinding.SnowRemovalSearchRowBinding;
import com.seazoned.databinding.SprinklerWinterizingSearchRowBinding;
import com.seazoned.model.ServiceListModel;

import java.util.ArrayList;

/**
 * Created by root on 23/2/18.
 */

public class SearchFilterAdapter extends RecyclerView.Adapter<SearchFilterAdapter.ViewHolder> {

    private Context context;
    private ArrayList<ServiceListModel> mServiceList;
    private int row_index=-1;
    LinearLayout linearLayout;

    public SearchFilterAdapter(Context context, ArrayList<ServiceListModel> mServiceList, LinearLayout linearLayout) {
        this.context=context;
        this.mServiceList=mServiceList;
        this.linearLayout=linearLayout;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        SearchFilterRowBinding binding= DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),R.layout.search_filter_row,parent,false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final ServiceListModel setGet=mServiceList.get(position);
        final String service_id=mServiceList.get(position).getId();
        holder.binding.tvSearchRow.setText(setGet.getServiceName());

        holder.binding.tvSearchRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                row_index=position;

                if(service_id.equalsIgnoreCase("1"))
                    setMowingEdging();

                else
                    if(service_id.equalsIgnoreCase("2"))
                    leafRemoval();


                else
                    if(service_id.equalsIgnoreCase("3"))
                    lawnTreatment();

                else
                    if(service_id.equalsIgnoreCase("4"))
                    setAeration();

                else
                    if(service_id.equalsIgnoreCase("5"))
                    sprinklerWinterzing();


                else
                    if(service_id.equalsIgnoreCase("6"))
                    poolCleaning();


                else
                    if(service_id.equalsIgnoreCase("7"))
                    snowRemoval();


                notifyDataSetChanged();

            }

        });
        if(row_index==position){
            holder.binding.tvSearchRow.setTextColor(context.getResources().getColor(R.color.colorFlatRed));
        }
        else
        {
            holder.binding.tvSearchRow.setTextColor(context.getResources().getColor(R.color.colorText));
        }



    }

    private void sprinklerWinterzing() {
        linearLayout.removeAllViews();
        SprinklerWinterizingSearchRowBinding binding=DataBindingUtil.inflate(LayoutInflater.from(context),R.layout.sprinkler_winterizing_search_row,linearLayout,true);
    }

    private void snowRemoval() {
        linearLayout.removeAllViews();
        SnowRemovalSearchRowBinding binding=DataBindingUtil.inflate(LayoutInflater.from(context),R.layout.snow_removal_search_row,linearLayout,true);
    }

    private void poolCleaning() {
        linearLayout.removeAllViews();
        PoolcleaingUpkeepSearchRowBinding binding=DataBindingUtil.inflate(LayoutInflater.from(context),R.layout.poolcleaing_upkeep_search_row,linearLayout,true);
    }

    private void leafRemoval() {
        linearLayout.removeAllViews();
        LeafRemovalSearchRowBinding binding=DataBindingUtil.inflate(LayoutInflater.from(context),R.layout.leaf_removal_search_row,linearLayout,true);
    }

    private void lawnTreatment() {
        linearLayout.removeAllViews();
        LawnTreatmentSearchRowBinding binding=DataBindingUtil.inflate(LayoutInflater.from(context),R.layout.lawn_treatment_search_row,linearLayout,true);
    }

    private void setAeration() {
        linearLayout.removeAllViews();
        AerationSearchRowBinding binding=DataBindingUtil.inflate(LayoutInflater.from(context),R.layout.aeration_search_row,linearLayout,true);
    }

    private void setMowingEdging() {
        linearLayout.removeAllViews();
        MowingEdgingSearchRowBinding binding=DataBindingUtil.inflate(LayoutInflater.from(context),R.layout.mowing_edging_search_row,linearLayout,true);
    }


    @Override
    public int getItemCount() {
        return mServiceList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        private SearchFilterRowBinding binding;

        public ViewHolder(SearchFilterRowBinding binding) {
            super(binding.lnsearchrow);
            this.binding = binding;
        }
    }
}
