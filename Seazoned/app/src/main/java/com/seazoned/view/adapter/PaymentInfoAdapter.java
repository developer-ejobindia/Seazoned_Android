package com.seazoned.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.seazoned.R;
import com.seazoned.app.AppData;
import com.seazoned.databinding.PaymentInfoRowBinding;
import com.seazoned.service.api.Api;
import com.seazoned.service.parser.PostDataParser;
import com.seazoned.view.activity.AddCard;
import com.seazoned.view.activity.EditCard;
import com.seazoned.view.activity.PaymentInfoActivity;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by root on 13/3/18.
 */

    public class PaymentInfoAdapter extends RecyclerView.Adapter<PaymentInfoAdapter.BindingHolder> {
    private Context mContext;
    ArrayList<HashMap<String,String>> mCardList;
    private int lastIndex = -1;
    EditText editText;

    public PaymentInfoAdapter(Context mContext, ArrayList<HashMap<String, String>> mCardList) {
        this.mContext = mContext;
        this.mCardList=mCardList;
    }

    @Override
    public BindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        PaymentInfoRowBinding binding= DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.payment_info_row,parent,false);
        return new BindingHolder(binding);
    }

    @Override
    public void onBindViewHolder(final BindingHolder holder, final int position) {
        final HashMap<String,String> hashMap=mCardList.get(position);
        if (hashMap.get("is_primary").equalsIgnoreCase("1")){
            lastIndex=position;
            editText=holder.binding.etCvv;
        }
        if (((Activity) mContext).getClass().getSimpleName().equalsIgnoreCase("ServiceHistoryDetailsActivity")) {
            holder.binding.rBtnSelect.setVisibility(View.VISIBLE);
            holder.binding.tvRemove.setVisibility(View.VISIBLE);
            holder.binding.ivDelete.setVisibility(View.GONE);
            holder.binding.ivEdit.setVisibility(View.GONE);


            if (lastIndex==position){
                holder.binding.etCvv.setVisibility(View.VISIBLE);
            }else{
                holder.binding.etCvv.setVisibility(View.GONE);
            }
        } else if (((Activity) mContext).getClass().getSimpleName().equalsIgnoreCase("PaymentInfoActivity")
                ||((Activity) mContext).getClass().getSimpleName().equalsIgnoreCase("BookingStepTwo")) {
            holder.binding.rBtnSelect.setVisibility(View.VISIBLE);//gone
            holder.binding.tvRemove.setVisibility(View.GONE);
            holder.binding.ivDelete.setVisibility(View.VISIBLE);

        }


       /* holder.binding.rBtnSelect.setChecked(lastIndex == position);
        if (lastIndex==position){
            holder.binding.etCvv.setVisibility(View.VISIBLE);
        }else{
            holder.binding.etCvv.setVisibility(View.GONE);
        }
        holder.binding.rBtnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lastIndex = position;
                editText=holder.binding.etCvv;
                notifyDataSetChanged();
            }
        });*/

        holder.binding.rBtnSelect.setChecked(lastIndex == position);
        holder.binding.rBtnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String,String> params=new HashMap<>();
                params.put("card_id",hashMap.get("card_id"));
                params.put("user_id", AppData.sUserId);
                new PostDataParser(mContext, Api.sSetPrimaryCard, params, true, new PostDataParser.OnGetResponseListner() {
                    @Override
                    public void onGetResponse(JSONObject response) {
                        if(response!=null)
                        try {
                            String success=response.optString("success");
                            String msg=response.optString("msg");
                            Toast.makeText(mContext, ""+msg, Toast.LENGTH_SHORT).show();
                            if (success.equalsIgnoreCase("1")){
                                lastIndex = position;
                                editText=holder.binding.etCvv;
                                notifyDataSetChanged();
                            }
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }


                    }
                });
            }
        });
        holder.binding.tvCardType.setText(hashMap.get("card_brand"));

        String s=hashMap.get("card_no");
        String r = s;
        if (s.length()>=13){
            String a=s.substring(0,2);
            String b=s.substring(s.length()-4,s.length());
            if (s.length()==16)
                r=a+"XX"+" XXXX XXXX "+b;
            if (s.length()==15)
                r=a+"XX"+" XXXXXX X"+b;
        }
        holder.binding.tvCardNumber.setText(r);

        holder.binding.tvExpiryDate.setText("Expiry Date: "+hashMap.get("month")+"/"+hashMap.get("year"));
        holder.binding.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String,String> params=new HashMap<>();
                params.put("card_id",hashMap.get("card_id"));
                deleteDetails(params,position);
            }
        });
        holder.binding.tvRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.binding.ivDelete.performClick();
            }
        });

        holder.binding.ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, EditCard.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("hashMapKey", hashMap);
                i.putExtras(bundle);
                mContext.startActivity(i);
               ((Activity) mContext).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

            }
        });

    }

    public HashMap<String,String> getCardData(){
        if (lastIndex!=-1)
        {
            HashMap<String,String> hashMap=mCardList.get(lastIndex);
            hashMap.put("cvv_no",editText.getText().toString());
            return hashMap;
        }
        else
            return null;
    }


    private  void deleteDetails(Map<String, String> params, final int position){
        new PostDataParser(mContext, Api.sDeleteCard, params, true, new PostDataParser.OnGetResponseListner() {
            public String msg;
            public String success;

            @Override
            public void onGetResponse(JSONObject response) {
                if(response!=null){
                    try{
                        success=response.optString("success");
                        msg=response.optString("msg");
                        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                        if (success.equalsIgnoreCase("1")){
                            mCardList.remove(position);
                            notifyDataSetChanged();
                        }
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        });

    }
    @Override
    public int getItemCount() {
        return mCardList.size();
    }



    public class BindingHolder extends RecyclerView.ViewHolder{
        PaymentInfoRowBinding binding;

        public BindingHolder(PaymentInfoRowBinding binding) {
            super(binding.lnMain);
            this.binding=binding;
        }
    }
}
