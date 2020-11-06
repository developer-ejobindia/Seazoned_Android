package com.seazoned.view.fragment;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.seazoned.R;
import com.seazoned.databinding.FragmentCardListBinding;
import com.seazoned.service.api.Api;
import com.seazoned.service.parser.GetDataParser;
import com.seazoned.view.activity.PaymentInfoActivity;
import com.seazoned.view.adapter.PaymentInfoAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class CardListFragment extends Fragment {

    FragmentCardListBinding mBinding;
    ArrayList<HashMap<String, String>> mCardList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_card_list, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getActivity());
        mBinding.rcvCardList.setLayoutManager(manager);

        mBinding.rcvCardList.setNestedScrollingEnabled(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        getCardList();
    }

    private void getCardList() {
        new GetDataParser(getActivity(), Api.sCardList, true, new GetDataParser.OnGetResponseListner() {
            @Override
            public void onGetResponse(JSONObject response) {
                try {
                    String msg, success;
                    if (response != null) {
                        msg = response.optString("msg");
                        success = response.optString("success");
                        if (success.equalsIgnoreCase("1")) {
                            mCardList = new ArrayList<>();
                            JSONObject data = response.optJSONObject("data");
                            JSONArray payment_accounts = data.optJSONArray("payment_accounts");
                            for (int i = 0; i < payment_accounts.length(); i++) {
                                JSONObject jsonObject1 = payment_accounts.optJSONObject(i);

                                HashMap<String, String> hashMap = new HashMap<>();

                                hashMap.put("card_id", jsonObject1.optString("id"));
                                hashMap.put("card_brand", jsonObject1.optString("card_brand"));
                                hashMap.put("card_no", jsonObject1.optString("card_no"));
                                hashMap.put("month", jsonObject1.optString("month"));
                                hashMap.put("year", jsonObject1.optString("year"));
                                hashMap.put("cvv_no", jsonObject1.optString("cvv_no"));
                                hashMap.put("name", jsonObject1.optString("name"));
                                hashMap.put("is_primary", jsonObject1.optString("is_primary"));

                                mCardList.add(hashMap);
                            }
                            mBinding.rcvCardList.setAdapter(new PaymentInfoAdapter(getActivity(), mCardList));
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }
}
