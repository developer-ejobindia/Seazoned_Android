package com.seazoned.view.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.google.android.gms.maps.model.LatLng;
import com.seazoned.R;
import com.seazoned.databinding.BookingHistoryListRowBinding;
import com.seazoned.model.BookingHistoryModel;
import com.seazoned.service.api.Api;
import com.seazoned.service.parser.PostDataParser;
import com.seazoned.service.util.Util;
import com.seazoned.view.activity.SearchDeatils;
import com.seazoned.view.activity.ServiceHistoryDetailsActivity;
import com.seazoned.view.activity.ViewProfileActivity;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class BookingHistoryListAdapter extends RecyclerView.Adapter<BookingHistoryListAdapter.BindingHolder> {
    Context mContext;
    ArrayList<HashMap<String, String>> mHistoryList;

    public BookingHistoryListAdapter(Context mContext, ArrayList<HashMap<String, String>> mHistoryList) {
        this.mContext = mContext;
        this.mHistoryList = mHistoryList;
    }

    @Override
    public BindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BookingHistoryListRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.booking_history_list_row, parent, false);
        return new BindingHolder(binding);
    }

    @Override
    public void onBindViewHolder(final BindingHolder holder, final int position) {
        final HashMap<String, String> hashMap = mHistoryList.get(position);
        holder.binding.tvOrderNo.setText(hashMap.get("order_no"));
        holder.binding.tvServiceName.setText(hashMap.get("service_name"));
        holder.binding.tvLandscaperName.setText(hashMap.get("landscaper_name"));
        //holder.binding.tvWorkingStatus.setText(hashMap.get("status_name"));
        holder.binding.tvTime.setText(Util.changeAnyDateFormat(hashMap.get("booking_time"), "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd hh:mm a"));

        if (hashMap.get("profile_image").equalsIgnoreCase("") ||
                hashMap.get("profile_image").equalsIgnoreCase("null") ||
                hashMap.get("profile_image").equalsIgnoreCase(null)) {
            Picasso.with(mContext).load(R.drawable.ic_user).into(holder.binding.ivLandscaperPic);
        } else {
            holder.binding.ivLandscaperPic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Util.getPopUpWindow(mContext, hashMap.get("profile_image"));
                }
            });
            Picasso.with(mContext).load(hashMap.get("profile_image")).error(R.drawable.ic_user).resize(150, 150).into(holder.binding.ivLandscaperPic);
        }

        String status = hashMap.get("status");
        String is_completed = hashMap.get("is_completed");
        holder.binding.tvPaymentStatus.setVisibility(View.VISIBLE);
        if (status.equalsIgnoreCase("-1")){
            holder.binding.tvPaymentStatus.setVisibility(View.GONE);
            Drawable img = mContext.getResources().getDrawable(R.drawable.ic_dot_red);
            holder.binding.tvWorkingStatus.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
            holder.binding.tvWorkingStatus.setText("Service request rejected");
            holder.binding.tvWorkingStatus.setTextColor(mContext.getResources().getColor(R.color.colorWarning));
        }
        if (status.equalsIgnoreCase("0") &&
                is_completed.equalsIgnoreCase("0")) {
            holder.binding.tvPaymentStatus.setVisibility(View.GONE);
            Drawable img = mContext.getResources().getDrawable(R.drawable.ic_dot);
            holder.binding.tvWorkingStatus.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
            holder.binding.tvWorkingStatus.setText("Service request sent");
            holder.binding.tvWorkingStatus.setTextColor(mContext.getResources().getColor(R.color.colorWarning));
        } else if (status.equalsIgnoreCase("1") &&
                is_completed.equalsIgnoreCase("0")) {
            //if (hashMap.get("transaction_id").equalsIgnoreCase("")||hashMap.get("transaction_id").equalsIgnoreCase("null")||hashMap.get("transaction_id").equalsIgnoreCase(null)) {
            Drawable img1 = mContext.getResources().getDrawable(R.drawable.ic_dot_green);
            holder.binding.tvWorkingStatus.setCompoundDrawablesWithIntrinsicBounds(img1, null, null, null);
            holder.binding.tvWorkingStatus.setText("Request accepted");
            holder.binding.tvWorkingStatus.setTextColor(mContext.getResources().getColor(R.color.colorWarning));

            Drawable img = mContext.getResources().getDrawable(R.drawable.ic_pending);
            holder.binding.tvPaymentStatus.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
            holder.binding.tvPaymentStatus.setText("Make payment to start work");
            holder.binding.tvPaymentStatus.setTextColor(mContext.getResources().getColor(R.color.colorWarning));
        } else if (status.equalsIgnoreCase("2") &&
                is_completed.equalsIgnoreCase("1")) {
            Drawable img = mContext.getResources().getDrawable(R.drawable.ic_dot);
            holder.binding.tvPaymentStatus.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
            holder.binding.tvPaymentStatus.setText("Payment is in Escrow");
            holder.binding.tvPaymentStatus.setTextColor(mContext.getResources().getColor(R.color.colorWarning));


            Drawable img1 = mContext.getResources().getDrawable(R.drawable.ic_dot);
            holder.binding.tvWorkingStatus.setCompoundDrawablesWithIntrinsicBounds(img1, null, null, null);
            holder.binding.tvWorkingStatus.setText("Work in progress");
            holder.binding.tvWorkingStatus.setTextColor(mContext.getResources().getColor(R.color.colorWarning));
        } else if (status.equalsIgnoreCase("3") &&
                is_completed.equalsIgnoreCase("1")) {
            Drawable img = mContext.getResources().getDrawable(R.drawable.ic_dot);
            holder.binding.tvPaymentStatus.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
            holder.binding.tvPaymentStatus.setText("Payment is in Escrow");
            holder.binding.tvPaymentStatus.setTextColor(mContext.getResources().getColor(R.color.colorWarning));

            Drawable img1 = mContext.getResources().getDrawable(R.drawable.ic_dot_green);
            holder.binding.tvWorkingStatus.setCompoundDrawablesWithIntrinsicBounds(img1, null, null, null);
            holder.binding.tvWorkingStatus.setText("Job has been completed by Landscaper, waiting for your confirmation");
            holder.binding.tvWorkingStatus.setTextColor(mContext.getResources().getColor(R.color.colorActiveNavText));
        } else if (status.equalsIgnoreCase("3") &&
                is_completed.equalsIgnoreCase("2")) {
            Drawable img = mContext.getResources().getDrawable(R.drawable.ic_success);
            holder.binding.tvPaymentStatus.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
            holder.binding.tvPaymentStatus.setText("Payment success");
            holder.binding.tvPaymentStatus.setTextColor(mContext.getResources().getColor(R.color.colorActiveNavText));
            //}

            Drawable img1 = mContext.getResources().getDrawable(R.drawable.ic_dot_green);
            holder.binding.tvWorkingStatus.setCompoundDrawablesWithIntrinsicBounds(img1, null, null, null);
            holder.binding.tvWorkingStatus.setText("Job complete");
            holder.binding.tvWorkingStatus.setTextColor(mContext.getResources().getColor(R.color.colorActiveNavText));
        }

//previous
        /*if (hashMap.get("status").equalsIgnoreCase("-1")) {
            holder.binding.tvPaymentStatus.setVisibility(View.GONE);
            Drawable img = mContext.getResources().getDrawable(R.drawable.ic_dot_red);
            holder.binding.tvWorkingStatus.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
            holder.binding.tvWorkingStatus.setText("Service Request Rejected");
            holder.binding.tvWorkingStatus.setTextColor(mContext.getResources().getColor(R.color.colorWarning));
        } else if (hashMap.get("status").equalsIgnoreCase("0")) {
            holder.binding.tvPaymentStatus.setVisibility(View.GONE);
            Drawable img = mContext.getResources().getDrawable(R.drawable.ic_dot);
            holder.binding.tvWorkingStatus.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
            holder.binding.tvWorkingStatus.setText("Service Request Sent");
            holder.binding.tvWorkingStatus.setTextColor(mContext.getResources().getColor(R.color.colorWarning));

        } else if (hashMap.get("status").equalsIgnoreCase("1")) {
            holder.binding.tvPaymentStatus.setVisibility(View.VISIBLE);
            //if (hashMap.get("transaction_id").equalsIgnoreCase("")||hashMap.get("transaction_id").equalsIgnoreCase("null")||hashMap.get("transaction_id").equalsIgnoreCase(null)) {
            Drawable img1 = mContext.getResources().getDrawable(R.drawable.ic_dot_green);
            holder.binding.tvWorkingStatus.setCompoundDrawablesWithIntrinsicBounds(img1, null, null, null);
            holder.binding.tvWorkingStatus.setText("Request Accepted");
            holder.binding.tvWorkingStatus.setTextColor(mContext.getResources().getColor(R.color.colorWarning));

            Drawable img = mContext.getResources().getDrawable(R.drawable.ic_pending);
            holder.binding.tvPaymentStatus.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
            holder.binding.tvPaymentStatus.setText("Make Payment to Start Work");
            holder.binding.tvPaymentStatus.setTextColor(mContext.getResources().getColor(R.color.colorWarning));

            //}
            *//*else {

                Drawable img1 = mContext.getResources().getDrawable(R.drawable.ic_dot);
                holder.binding.tvWorkingStatus.setCompoundDrawablesWithIntrinsicBounds(img1, null, null, null);
                holder.binding.tvWorkingStatus.setText("Work In Progress");
                holder.binding.tvWorkingStatus.setTextColor(mContext.getResources().getColor(R.color.colorWarning));

                Drawable img = mContext.getResources().getDrawable(R.drawable.ic_success);
                holder.binding.tvPaymentStatus.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
                holder.binding.tvPaymentStatus.setText("Payment Success");
                holder.binding.tvPaymentStatus.setTextColor(mContext.getResources().getColor(R.color.colorActiveNavText));

            }*//*


        } else if (hashMap.get("status").equalsIgnoreCase("2")) {
            holder.binding.tvPaymentStatus.setVisibility(View.VISIBLE);
            *//*if (hashMap.get("transaction_id").equalsIgnoreCase("")||hashMap.get("transaction_id").equalsIgnoreCase("null")||hashMap.get("transaction_id").equalsIgnoreCase(null)) {
                Drawable img = mContext.getResources().getDrawable(R.drawable.ic_pending);
                holder.binding.tvPaymentStatus.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
                holder.binding.tvPaymentStatus.setText("Make Payment to Start Work");
                holder.binding.tvPaymentStatus.setTextColor(mContext.getResources().getColor(R.color.colorWarning));
            }
            else {*//*
            Drawable img = mContext.getResources().getDrawable(R.drawable.ic_success);
            holder.binding.tvPaymentStatus.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
            holder.binding.tvPaymentStatus.setText("Payment Success");
            holder.binding.tvPaymentStatus.setTextColor(mContext.getResources().getColor(R.color.colorActiveNavText));
            //}

            Drawable img1 = mContext.getResources().getDrawable(R.drawable.ic_dot);
            holder.binding.tvWorkingStatus.setCompoundDrawablesWithIntrinsicBounds(img1, null, null, null);
            holder.binding.tvWorkingStatus.setText("Work In Progress");
            holder.binding.tvWorkingStatus.setTextColor(mContext.getResources().getColor(R.color.colorWarning));

        } else if (hashMap.get("status").equalsIgnoreCase("3")) {
            holder.binding.tvPaymentStatus.setVisibility(View.VISIBLE);
            *//*if (hashMap.get("transaction_id").equalsIgnoreCase("") || hashMap.get("transaction_id").equalsIgnoreCase("null") || hashMap.get("transaction_id").equalsIgnoreCase(null)) {
                Drawable img = mContext.getResources().getDrawable(R.drawable.ic_pending);
                holder.binding.tvPaymentStatus.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
                holder.binding.tvPaymentStatus.setText("Make Payment to Start Work");
                holder.binding.tvPaymentStatus.setTextColor(mContext.getResources().getColor(R.color.colorWarning));
            } else {*//*
            Drawable img = mContext.getResources().getDrawable(R.drawable.ic_success);
            holder.binding.tvPaymentStatus.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
            holder.binding.tvPaymentStatus.setText("Payment Success");
            holder.binding.tvPaymentStatus.setTextColor(mContext.getResources().getColor(R.color.colorActiveNavText));
            //}

            Drawable img1 = mContext.getResources().getDrawable(R.drawable.ic_dot_green);
            holder.binding.tvWorkingStatus.setCompoundDrawablesWithIntrinsicBounds(img1, null, null, null);
            holder.binding.tvWorkingStatus.setText("Job Complete");
            holder.binding.tvWorkingStatus.setTextColor(mContext.getResources().getColor(R.color.colorActiveNavText));
        }*///previous

        /*if (position % 2 == 0) {
            holder.binding.tvPaymentStatus.setText("Payment Success");
            holder.binding.tvPaymentStatus.setTextColor(mContext.getResources().getColor(R.color.colorActiveNavText));
            Drawable img = mContext.getResources().getDrawable(R.drawable.ic_success);
            holder.binding.tvPaymentStatus.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
        } else {
            holder.binding.tvPaymentStatus.setText("Pending Payment");
            holder.binding.tvPaymentStatus.setTextColor(mContext.getResources().getColor(R.color.colorWarning));
            Drawable img = mContext.getResources().getDrawable(R.drawable.ic_pending);
            holder.binding.tvPaymentStatus.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                *//*((Activity)mContext).finish();
                mContext.startActivity(new Intent(mContext,ServiceHistoryDetailsActivity.class));
                ((Activity)mContext).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);*//*
            }
        });*/


        holder.binding.lnHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String favourite_status = mHistoryList.get(position).get("favourite_status");
                if (favourite_status.equalsIgnoreCase("0")) {

                    View v = LayoutInflater.from(mContext).inflate(R.layout.add_favorite_job_dialog, null);
                    final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setView(v);
                    builder.setCancelable(false);
                    TextView tvCancel = v.findViewById(R.id.tvCancel);
                    TextView tvOK = v.findViewById(R.id.tvOk);

                    final AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                    tvCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            alertDialog.dismiss();
                            //((Activity)mContext).finish();
                            Intent i = new Intent(mContext, ServiceHistoryDetailsActivity.class);
                            i.putExtra("bookingId", hashMap.get("bookingId"));
                            i.putExtra("landscaper_id", hashMap.get("landscaper_id"));
                            mContext.startActivity(i);
                            ((Activity) mContext).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);


                        }
                    });
                    tvOK.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            alertDialog.dismiss();
                            HashMap<String, String> params = new HashMap<>();
                            params.put("landscaper_id", hashMap.get("landscaper_id"));
                            params.put("status", "1");
                            new PostDataParser(mContext, Api.sAddFavorite, params, false, new PostDataParser.OnGetResponseListner() {
                                @Override
                                public void onGetResponse(JSONObject response) {
                                    String success, msg;
                                    if (response != null) {
                                        try {
                                            msg = response.optString("msg");
                                            success = response.optString("success");
                                            if (success.equalsIgnoreCase("1")) {
                                                Intent i = new Intent(mContext, ServiceHistoryDetailsActivity.class);
                                                i.putExtra("bookingId", hashMap.get("bookingId"));
                                                i.putExtra("landscaper_id", hashMap.get("landscaper_id"));
                                                mContext.startActivity(i);
                                                ((Activity) mContext).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }

                                }
                            });

                        }
                    });
                } else if (favourite_status.equalsIgnoreCase("1")) {

                    Intent i = new Intent(mContext, ServiceHistoryDetailsActivity.class);
                    i.putExtra("bookingId", hashMap.get("bookingId"));
                    i.putExtra("landscaper_id", hashMap.get("landscaper_id"));
                    mContext.startActivity(i);
                    ((Activity) mContext).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                }


            }
        });

    }


    @Override
    public int getItemCount() {
        return mHistoryList.size();
    }

    public static class BindingHolder extends RecyclerView.ViewHolder {
        private BookingHistoryListRowBinding binding;

        public BindingHolder(BookingHistoryListRowBinding binding) {
            super(binding.lnHistory);
            this.binding = binding;
        }
    }


}
