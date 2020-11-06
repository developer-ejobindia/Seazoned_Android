package com.seazoned.view.activity;

import android.databinding.DataBindingUtil;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.Toast;

import com.seazoned.R;
import com.seazoned.databinding.ActivitySearchFilterBinding;
import com.seazoned.model.ServiceListModel;
import com.seazoned.service.api.Api;
import com.seazoned.service.parser.GetDataParser;
import com.seazoned.service.util.Util;
import com.seazoned.view.adapter.SearchFilterAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class SearchFilter extends AppCompatActivity {


    private ArrayList<ServiceListModel> mSearchFilterList=null;

    ActivitySearchFilterBinding mBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*//setContentView(R.layout.activity_search_filter);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_search_filter);
        Util.setPadding(this,mBinding.mainLayout);

       LinearLayoutManager manager=new LinearLayoutManager(SearchFilter.this);
        mBinding.rvSearchitems.setLayoutManager(manager);
        getServiceList();*/


    }

/*
    private void getServiceList() {
        new GetDataParser(SearchFilter.this, Api.sServiceList, true, new GetDataParser.OnGetResponseListner() {
            public String mStatus;
            public String mSuccess;

            @Override
            public void onGetResponse(JSONObject response) {
                if (response != null) {
                    try {
                        mSuccess = response.optString("success");
                        mStatus = response.optString("msg");
                        if (mSuccess.equalsIgnoreCase("1")) {

                            mSearchFilterList=new ArrayList<>();
                            JSONArray jarrData=response.getJSONArray("data");
                            for(int i=0;i<jarrData.length();i++){
                                JSONObject jsonObject=jarrData.getJSONObject(i);
                                String sId=jsonObject.optString("id");

                                String sServiceName=jsonObject.optString("service_name");
                                String sDescription=jsonObject.optString("description");
                                String sLogoName=jsonObject.optString("logo_name");

                                ServiceListModel objServiceList=
                                        new ServiceListModel(sId,sServiceName,sDescription);
                                mSearchFilterList.add(objServiceList);

                            }

                           //mBinding.rvSearchitems.setAdapter(new ServiceListAdapter(SearchFilter.this,mSearchFilterList));
                           mBinding.rvSearchitems.setAdapter(new SearchFilterAdapter(SearchFilter.this,mSearchFilterList,mBinding.lnSerachResult));



                        } else {
                            Toast.makeText(SearchFilter.this, "" + mStatus, Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }
*/
}
