package com.seazoned.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.seazoned.R;
import com.seazoned.databinding.AddressRowBinding;
import com.seazoned.view.activity.AddEditAddressActivity;
import com.seazoned.view.activity.BookingStepTwo;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by root on 15/3/18.
 */

public class AddressListAdapter extends RecyclerView.Adapter<AddressListAdapter.BindingHolder> {
    ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
    private Context mContext;
    private int lastIndex = 0;

    public AddressListAdapter(Context mContext) {
        this.mContext = mContext;
    }


    @Override
    public BindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        AddressRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.address_row, parent, false);
        return new BindingHolder(binding);
    }

    @Override
    public void onBindViewHolder(final BindingHolder holder, final int position) {

        if (((Activity) mContext).getClass().getSimpleName().equalsIgnoreCase("BookingStepTwo")) {
            holder.binding.rBtnSelect.setVisibility(View.VISIBLE);
        } else if (((Activity) mContext).getClass().getSimpleName().equalsIgnoreCase("AddressBookActivity")) {
            holder.binding.rBtnSelect.setVisibility(View.GONE);
        }

        holder.binding.rBtnSelect.setChecked(lastIndex == position);

        holder.binding.rBtnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lastIndex = position;
                notifyDataSetChanged();
            }
        });
        final HashMap<String, String> hashMap = arrayList.get(position);
        holder.binding.tvName.setText(hashMap.get("name"));
        holder.binding.tvaddress.setText(hashMap.get("address"));
        holder.binding.tvPhone.setText(hashMap.get("phoneNo"));
        holder.binding.tvEmail.setText(hashMap.get("email"));
        holder.binding.tvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mContext.startActivity(new Intent(mContext, AddEditAddressActivity.class)
                        .putExtra("mode", "edit")
                        .putExtra("addressId", hashMap.get("addressId"))
                        .putExtra("name", hashMap.get("name"))
                        .putExtra("email", hashMap.get("email"))
                        .putExtra("phoneNo", hashMap.get("phoneNo"))
                        .putExtra("address", hashMap.get("address"))
                        .putExtra("city", hashMap.get("city"))
                        .putExtra("state", hashMap.get("state"))
                        .putExtra("country", hashMap.get("country"))
                );
                ((Activity) mContext).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class BindingHolder extends RecyclerView.ViewHolder {
        AddressRowBinding binding;

        public BindingHolder(AddressRowBinding itemView) {
            super(itemView.llMain);
            this.binding = itemView;
        }
    }

    public void addRow(HashMap<String, String> hashMap) {
        arrayList.add(hashMap);
    }

    public void clearData() {
        arrayList.clear();
        notifyDataSetChanged();
    }

    public String getAddressId() {
        if (arrayList.size()>0)
            return arrayList.get(lastIndex).get("addressId");
        else
            return "";
    }
}
