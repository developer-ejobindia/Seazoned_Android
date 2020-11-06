package com.seazoned.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.seazoned.R;
import com.seazoned.app.AppData;
import com.seazoned.model.ChatModel;
import com.seazoned.service.util.Util;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by root on 16/10/17.
 */

public class ConversationDetailsAdapter extends RecyclerView.Adapter<ConversationDetailsAdapter.ViewHolder> {
    Context mContext;
    ArrayList<ChatModel> arrayList = new ArrayList<>();
    String profileImage;

    public ConversationDetailsAdapter(Context mContext,String profileImage) {
        this.mContext = mContext;
        this.profileImage=profileImage;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.chatrow,parent,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        ChatModel model=arrayList.get(position);
        String timeStamp= Util.changeAnyDateFormatTimeStamp(model.getDate(),"dd-MM-yyyy HH:mm:ss","MMM-dd-yyyy hh:mm a");
        if (model.getSenderid().equalsIgnoreCase(AppData.sUserId)){
            holder.mLnRight.setVisibility(View.VISIBLE);
            holder.mLnLeft.setVisibility(View.GONE);
            if (timeStamp!="")
            {
                holder.tvRightTime.setVisibility(View.VISIBLE);
                holder.tvRightTime.setText(timeStamp);
            }
            holder.mTvRight.setText(model.getText());
        }
        else{
            /*if (profileImage.equalsIgnoreCase("")||profileImage.equalsIgnoreCase("null")){
                Picasso.with(mContext).load(R.drawable.icon_user).into(holder.ivProfile);
            }
            else {
                Picasso.with(mContext).load(profileImage).placeholder(R.drawable.icon_user).error(R.drawable.icon_user).into(holder.ivProfile);
            }*/
            holder.mLnRight.setVisibility(View.GONE);
            holder.mLnLeft.setVisibility(View.VISIBLE);
            if (timeStamp!="")
            {
                holder.tvLeftTime.setVisibility(View.VISIBLE);
                holder.tvLeftTime.setText(timeStamp);
            }
            holder.mTvLeft.setText(model.getText());
        }

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final LinearLayout mLnLeft;
        private final LinearLayout mLnRight;
        private final TextView mTvLeft;
        private final TextView mTvRight;
        private final ImageView ivProfile;
        private final TextView tvRightTime;
        private final TextView tvLeftTime;

        public ViewHolder(View itemView) {
            super(itemView);
            mLnLeft=(LinearLayout)itemView.findViewById(R.id.ln_left);
            mTvLeft=(TextView)itemView.findViewById(R.id.tv_left);
            mLnRight=(LinearLayout)itemView.findViewById(R.id.ln_right);
            mTvRight=(TextView)itemView.findViewById(R.id.tv_right);
            tvRightTime=(TextView)itemView.findViewById(R.id.tvRightTime);
            tvLeftTime=(TextView)itemView.findViewById(R.id.tvLeftTime);
            ivProfile=(ImageView) itemView.findViewById(R.id.ivProfile);
        }
    }

    public void addRow(ChatModel chatRow) {
        arrayList.add(chatRow);
    }

    public void removeRow(int index) {
        try {
            arrayList.remove(index);
            notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void removeAll(){
        arrayList.clear();
    }
}
