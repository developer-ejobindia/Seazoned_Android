package com.seazoned.view.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebViewClient;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.seazoned.R;
import com.seazoned.databinding.ActivityEditProfileBinding;
import com.seazoned.databinding.ActivityFaqBinding;
import com.seazoned.service.api.Api;
import com.seazoned.service.parser.GetDataParser;
import com.seazoned.service.parser.PostDataParser;
import com.seazoned.service.preference.SharedPreferenceHelper;
import com.seazoned.service.util.Util;
import com.seazoned.view.adapter.FaqAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class FaqActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityFaqBinding mBinding;
    ArrayList<HashMap<String,String>> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_faq);
        mBinding.faqWebView.getSettings().setJavaScriptEnabled(true);
        //mBinding.faqWebView.setWebViewClient(new WebViewClient());
        mBinding.faqWebView.loadUrl("http://www.seazoned.com/customer-FAQ");
        /*mBinding.lnBack.setOnClickListener(this);
        RecyclerView.LayoutManager manager=new LinearLayoutManager(this);
        mBinding.rcvFaqList.setLayoutManager(manager);
        getFaqInfo();*/

    }

   /* private void getFaqInfo() {
        HashMap<String,String> params=new HashMap<>();
        params.put("profile_id","2");
        new PostDataParser(FaqActivity.this, Api.sFaq,params, true, true, new PostDataParser.OnGetResponseListner() {
            public String success;

            @Override
            public void onGetResponse(JSONObject response) {
                try{
                    if (response!=null){
                        success=response.optString("success");
                        if (success.equalsIgnoreCase("1")){
                            arrayList=new ArrayList<>();
                            //JSONObject data=response.optJSONObject("data");
                            JSONArray faq=response.optJSONArray("data");
                            for (int i=0;i<faq.length();i++){
                                JSONObject jsonObject=faq.optJSONObject(i);
                                String id=jsonObject.optString("id");
                                String questions=jsonObject.optString("questions");
                                String answers=jsonObject.optString("answers");
                                HashMap<String,String> hashMap=new HashMap<>();
                                hashMap.put("id",id);
                                hashMap.put("questions",questions);
                                hashMap.put("answers",answers);
                                hashMap.put("visibility","0");

                                arrayList.add(hashMap);
                            }
                            if (faq.length() > 0) {
                                mBinding.rcvFaqList.setVisibility(View.VISIBLE);
                                mBinding.tvAlert.setVisibility(View.GONE);
                                mBinding.rcvFaqList.setAdapter(new FaqAdapter(FaqActivity.this, arrayList));
                            }
                            else {
                                mBinding.rcvFaqList.setVisibility(View.GONE);
                                mBinding.tvAlert.setVisibility(View.VISIBLE);
                            }
                        }
                        else {
                            mBinding.rcvFaqList.setVisibility(View.GONE);
                            mBinding.tvAlert.setVisibility(View.VISIBLE);
                        }

                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }*/

    @Override
    public void onClick(View view) {
        /*if (view==mBinding.lnBack){
            finish();
            startActivity(new Intent(FaqActivity.this,DashBoardActivity.class));
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }*/

    }


    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        finish();
        startActivity(new Intent(FaqActivity.this,DashBoardActivity.class));
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
