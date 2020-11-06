package com.seazoned.view.fragment;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.seazoned.R;
import com.seazoned.databinding.SlidingDrawerBinding;
import com.seazoned.view.activity.DashBoardActivity;
import com.seazoned.view.activity.FaqActivity;
import com.seazoned.view.activity.LoginActivity;

/**
 * Created by root on 2/5/18.
 */

public class LeftDrawer extends Fragment implements View.OnClickListener {
    SlidingDrawerBinding binding;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding=DataBindingUtil.inflate(inflater,R.layout.sliding_drawer,container,false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        binding.tvFaq.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view==binding.tvFaq){
            getActivity().finish();
            startActivity(new Intent(getActivity(), FaqActivity.class));
            getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }
}
