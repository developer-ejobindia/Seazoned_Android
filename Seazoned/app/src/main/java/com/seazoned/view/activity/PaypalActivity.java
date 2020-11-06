package com.seazoned.view.activity;

import android.content.Intent;
import android.database.DatabaseUtils;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.seazoned.R;
import com.seazoned.app.AppData;
import com.seazoned.databinding.ActivityPaypalBinding;
import com.seazoned.service.api.Api;
import com.seazoned.service.parser.PostDataParser;
import com.seazoned.service.util.Util;

import org.json.JSONObject;

import java.util.HashMap;

public class PaypalActivity extends AppCompatActivity {
    ActivityPaypalBinding mBinding;
    private String paykey = "";
    private String landscaper_amount;
    private String admin_amount;
    private String order_no;
    private String order_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_paypal);
        paykey = getIntent().getExtras().getString("paykey");
        landscaper_amount = getIntent().getExtras().getString("landscaper_amount");
        admin_amount = getIntent().getExtras().getString("admin_amount");
        order_no = getIntent().getExtras().getString("order_no");
        order_id = getIntent().getExtras().getString("order_id");

        //String url = "https://www.sandbox.paypal.com/cgi-bin/webscr?cmd=_ap-payment&paykey=" + paykey;
        String url = "https://www.paypal.com/cgi-bin/webscr?cmd=_ap-payment&paykey=" + paykey;
        mBinding.wvPaypal.getSettings().setJavaScriptEnabled(true);
        mBinding.wvPaypal.setWebViewClient(new WebViewClient());
        mBinding.wvPaypal.loadUrl(url);

        mBinding.wvPaypal.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.e("Loading url...", url);
                view.loadUrl(url);

                String loadWebUrl = view.getUrl();

                Log.e("loadWebUrl", "" + loadWebUrl);

                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                Log.e("Finished url...", url);

                String webUrl = view.getUrl();

                Log.e("webUrl", "" + webUrl);


                if (webUrl.contains(Api.sTransactionApproved)) {

                    Log.e("Getting Success Request", "Test");
                    saveTransactionHistory();

                } else if (webUrl.contains(Api.sTransactionDeclined)) {
                    Log.e("Getting Success Request", "Test");
                    //saveTransactionHistory();
                    finish();
                }


            }

            @Override
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {

                Log.e("Error in url...", description);
                Log.e("Error in failingUrl...", failingUrl);

            }

        });
    }

    private void saveTransactionHistory() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("payKey", paykey);
        hashMap.put("landscaper_amount", landscaper_amount);
        hashMap.put("admin_amount", admin_amount);
        hashMap.put("order_no", order_no);
        hashMap.put("order_id", order_id);
        hashMap.put("payment_time", Util.getSystemDateTime());


        new PostDataParser(PaypalActivity.this, Api.sPayUsingPaypal, hashMap, true, new PostDataParser.OnGetResponseListner() {
            public String success;

            @Override
            public void onGetResponse(JSONObject response) {
                if (response != null) {
                    try {
                        success = response.optString("success");
                        if (success.equalsIgnoreCase("1")) {
                            finish();
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });


    }
}
