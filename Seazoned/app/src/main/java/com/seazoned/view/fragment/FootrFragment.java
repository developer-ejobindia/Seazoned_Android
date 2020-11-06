package com.seazoned.view.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.graphics.PorterDuff;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.seazoned.R;
import com.seazoned.databinding.FooterFragmentBinding;
import com.seazoned.service.RedDotService;
import com.seazoned.service.preference.SharedPreferenceHelper;
import com.seazoned.view.activity.ChatListActivity;
import com.seazoned.view.activity.ContactUs;
import com.seazoned.view.activity.DashBoardActivity;
import com.seazoned.view.activity.FaqActivity;
import com.seazoned.view.activity.ProfileActivity;

import java.util.Date;

/**
 * Created by root on 31/1/18.
 */

public class FootrFragment extends Fragment implements View.OnClickListener {
    FooterFragmentBinding mBinding;
    Context mContext;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.footer_fragment, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContext = getActivity();
        mBinding.lnProfile.setOnClickListener(this);
        mBinding.lnHome.setOnClickListener(this);
        mBinding.lnContactUs.setOnClickListener(this);
        mBinding.lnMessage.setOnClickListener(this);
        mBinding.lnFaq.setOnClickListener(this);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(
                mMessageReceiver, new IntentFilter("RedDotStatus"));
        getActivity().startService(new Intent(getActivity(), RedDotService.class));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().stopService(new Intent(getActivity(), RedDotService.class));
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            boolean status = intent.getExtras().getBoolean("status");
            if (status) {
                if (mContext.getClass().getSimpleName().equalsIgnoreCase("ChatListActivity")) {
                    if (((RecyclerView) ((Activity) mContext).findViewById(R.id.rcvChatList)).getAdapter() != null) {
                        ((RecyclerView) ((Activity) mContext).findViewById(R.id.rcvChatList)).getAdapter().notifyDataSetChanged();
                    }
                }
                mBinding.ivMsgDot.setVisibility(View.VISIBLE);
            } else {
                mBinding.ivMsgDot.setVisibility(View.GONE);
            }

            //tvStatus.setText(message);
            // Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    };
    public void showRedDot(boolean status){
        if (status)
        getActivity().findViewById(R.id.ivMsgDot).setVisibility(View.VISIBLE);
        else
            getActivity().findViewById(R.id.ivMsgDot).setVisibility(View.GONE);

    }

    @Override
    public void onResume() {
        super.onResume();
        int myActiveVectorColor = ContextCompat.getColor(getActivity(), R.color.colorActiveNavText);
        int myInActiveVectorColor = ContextCompat.getColor(getActivity(), R.color.colorNavText);

        if (getActivity().getClass().getSimpleName().equalsIgnoreCase("ProfileActivity") ||
                getActivity().getClass().getSimpleName().equalsIgnoreCase("ViewProfileActivity") ||
                getActivity().getClass().getSimpleName().equalsIgnoreCase("EditProfileActivity") ||
                getActivity().getClass().getSimpleName().equalsIgnoreCase("AddressBookActivity") ||
                getActivity().getClass().getSimpleName().equalsIgnoreCase("PaymentInfoActivity") ||
                getActivity().getClass().getSimpleName().equalsIgnoreCase("BookingHistoryActivity") ||
                getActivity().getClass().getSimpleName().equalsIgnoreCase("FavouriteActivity")
                ) {
            mBinding.ivProfile.getDrawable().setColorFilter(myActiveVectorColor, PorterDuff.Mode.SRC_IN);
            mBinding.tvProfile.setTextColor(myActiveVectorColor);
            if (getActivity().getClass().getSimpleName().equalsIgnoreCase("ProfileActivity"))
                mBinding.lnProfile.setOnClickListener(null);

            //in active
            mBinding.ivMessage.getDrawable().setColorFilter(myInActiveVectorColor, PorterDuff.Mode.SRC_IN);
            mBinding.tvMessage.setTextColor(myInActiveVectorColor);

            mBinding.ivContact.getDrawable().setColorFilter(myInActiveVectorColor, PorterDuff.Mode.SRC_IN);
            mBinding.tvContact.setTextColor(myInActiveVectorColor);

            mBinding.ivFaq.getDrawable().setColorFilter(myInActiveVectorColor, PorterDuff.Mode.SRC_IN);
            mBinding.tvFaq.setTextColor(myInActiveVectorColor);


        } else if (getActivity().getClass().getSimpleName().equalsIgnoreCase("DashBoardActivity")) {

            //in active
            mBinding.ivProfile.getDrawable().setColorFilter(myInActiveVectorColor, PorterDuff.Mode.SRC_IN);
            mBinding.tvProfile.setTextColor(myInActiveVectorColor);

            mBinding.ivContact.getDrawable().setColorFilter(myInActiveVectorColor, PorterDuff.Mode.SRC_IN);
            mBinding.tvContact.setTextColor(myInActiveVectorColor);

            mBinding.ivMessage.getDrawable().setColorFilter(myInActiveVectorColor, PorterDuff.Mode.SRC_IN);
            mBinding.tvMessage.setTextColor(myInActiveVectorColor);

            mBinding.ivFaq.getDrawable().setColorFilter(myInActiveVectorColor, PorterDuff.Mode.SRC_IN);
            mBinding.tvFaq.setTextColor(myInActiveVectorColor);

            mBinding.lnHome.setOnClickListener(null);
        } else if (getActivity().getClass().getSimpleName().equalsIgnoreCase("ChatListActivity")) {

            //active
            mBinding.ivMessage.getDrawable().setColorFilter(myActiveVectorColor, PorterDuff.Mode.SRC_IN);
            mBinding.tvMessage.setTextColor(myActiveVectorColor);

            //inactive
            mBinding.ivProfile.getDrawable().setColorFilter(myInActiveVectorColor, PorterDuff.Mode.SRC_IN);
            mBinding.tvProfile.setTextColor(myInActiveVectorColor);

            mBinding.ivContact.getDrawable().setColorFilter(myInActiveVectorColor, PorterDuff.Mode.SRC_IN);
            mBinding.tvContact.setTextColor(myInActiveVectorColor);

            mBinding.ivFaq.getDrawable().setColorFilter(myInActiveVectorColor, PorterDuff.Mode.SRC_IN);
            mBinding.tvFaq.setTextColor(myInActiveVectorColor);

            mBinding.lnMessage.setOnClickListener(null);
        } else if (getActivity().getClass().getSimpleName().equalsIgnoreCase("ContactUs")) {

            //in active
            mBinding.ivProfile.getDrawable().setColorFilter(myInActiveVectorColor, PorterDuff.Mode.SRC_IN);
            mBinding.tvProfile.setTextColor(myInActiveVectorColor);

            mBinding.ivContact.getDrawable().setColorFilter(myInActiveVectorColor, PorterDuff.Mode.SRC_IN);
            mBinding.tvContact.setTextColor(myInActiveVectorColor);

            mBinding.ivFaq.getDrawable().setColorFilter(myInActiveVectorColor, PorterDuff.Mode.SRC_IN);
            mBinding.tvFaq.setTextColor(myInActiveVectorColor);

            //active
            mBinding.ivContact.getDrawable().setColorFilter(myActiveVectorColor, PorterDuff.Mode.SRC_IN);
            mBinding.tvContact.setTextColor(myActiveVectorColor);

            mBinding.lnContactUs.setOnClickListener(null);
        } else if (getActivity().getClass().getSimpleName().equalsIgnoreCase("FaqActivity")) {

            //in active
            mBinding.ivContact.getDrawable().setColorFilter(myInActiveVectorColor, PorterDuff.Mode.SRC_IN);
            mBinding.tvContact.setTextColor(myInActiveVectorColor);

            mBinding.ivContact.getDrawable().setColorFilter(myInActiveVectorColor, PorterDuff.Mode.SRC_IN);
            mBinding.tvContact.setTextColor(myInActiveVectorColor);

            mBinding.ivFaq.getDrawable().setColorFilter(myInActiveVectorColor, PorterDuff.Mode.SRC_IN);
            mBinding.tvFaq.setTextColor(myInActiveVectorColor);

            //active
            mBinding.ivFaq.getDrawable().setColorFilter(myActiveVectorColor, PorterDuff.Mode.SRC_IN);
            mBinding.tvFaq.setTextColor(myActiveVectorColor);

            mBinding.lnFaq.setOnClickListener(null);
        }
        SharedPreferenceHelper helper=SharedPreferenceHelper.getInstance(getActivity());
        if (helper.getUserLoginType().equalsIgnoreCase("guestLogin")){
           /* mBinding.lnMessage.setOnClickListener(null);
            mBinding.lnProfile.setOnClickListener(null);*/
           mBinding.lnMessage.setClickable(false);
           mBinding.lnProfile.setClickable(false);
        }
        else {
            mBinding.lnMessage.setClickable(true);
            mBinding.lnProfile.setClickable(true);
        }


    }

    @Override
    public void onClick(View view) {
        if (view == mBinding.lnProfile) {
            getActivity().finish();
            startActivity(new Intent(getActivity(), ProfileActivity.class));
            getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        } else if (view == mBinding.lnHome) {
            getActivity().finish();
            startActivity(new Intent(getActivity(), DashBoardActivity.class));
            getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        } else if (view == mBinding.lnContactUs) {
            getActivity().finish();
            startActivity(new Intent(getActivity(), ContactUs.class));
            getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        } else if (view == mBinding.lnMessage) {
            getActivity().finish();
            startActivity(new Intent(getActivity(), ChatListActivity.class));
            getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        } else if (view == mBinding.lnFaq) {
            getActivity().finish();
            startActivity(new Intent(getActivity(), FaqActivity.class));
            getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }

}
