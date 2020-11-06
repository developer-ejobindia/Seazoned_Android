package com.seazoned.view.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.seazoned.R;
import com.seazoned.databinding.AddCardBinding;
import com.seazoned.service.api.Api;
import com.seazoned.service.parser.PostDataParser;
import com.seazoned.service.util.Util;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by root on 20/3/18.
 */

public class AddCard extends AppCompatActivity implements View.OnClickListener {
    AddCardBinding mBinding;
    private int count = 0;
    private int expcount = 0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.add_card);
        mBinding.lnBack.setOnClickListener(this);
        mBinding.tvAddCard.setOnClickListener(this);
        mBinding.etExpiryDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {



            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (expcount <= mBinding.etExpiryDate.getText().toString().length()
                        && (mBinding.etExpiryDate.getText().toString().length() == 2)) {
                    mBinding.etExpiryDate.setText(mBinding.etExpiryDate.getText().toString() + "/");
                    int pos = mBinding.etExpiryDate.getText().length();
                    mBinding.etExpiryDate.setSelection(pos);
                } else if (expcount >= mBinding.etExpiryDate.getText().toString().length()
                        && (mBinding.etExpiryDate.getText().toString().length() == 2)) {
                    mBinding.etExpiryDate.setText(mBinding.etExpiryDate.getText().toString().substring(0, mBinding.etExpiryDate.getText().toString().length() - 1));
                    int pos = mBinding.etExpiryDate.getText().length();
                    mBinding.etExpiryDate.setSelection(pos);
                }
                expcount = mBinding.etExpiryDate.getText().toString().length();
            }
        });

    }

    @Override
    public void onClick(View view) {
        if (view == mBinding.lnBack) {
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        } else if (view == mBinding.tvAddCard) {
            String year = "";
            String month = "";
            String cardHolderName = mBinding.etCardHolderName.getText().toString().trim();
            String cardNumber = mBinding.etCardNumber.getText().toString().trim();
            String expiryDate = mBinding.etExpiryDate.getText().toString().trim();
            String cvv = mBinding.etCvv.getText().toString().trim();
            if (TextUtils.isEmpty(cardHolderName) || TextUtils.isEmpty(cardNumber) ||
                    TextUtils.isEmpty(expiryDate)||TextUtils.isEmpty(cvv)) {
                Toast.makeText(this, "Required all fields.", Toast.LENGTH_SHORT).show();
                return;
            }
            if (expiryDate.contains("/")) {
                try {
                    String date[] = expiryDate.split("/");

                    month = date[0];
                    if (date.length > 1)
                        year = date[1];
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            if (year.equalsIgnoreCase("")) {
                Toast.makeText(this, "Invalid expiry date.", Toast.LENGTH_SHORT).show();
                return;
            }
            HashMap<String,String> params=new HashMap<>();
            params.put("card_holder_name",cardHolderName);
            params.put("card_no",cardNumber);
            params.put("month",month);
            params.put("year",year);
            params.put("cvv_no",cvv);
            saveCardData(params);

        }
    }

    private void saveCardData(HashMap<String, String> params) {
        new PostDataParser(AddCard.this, Api.sAddcard, params, true, new PostDataParser.OnGetResponseListner() {
            public String msg;
            public String success;

            @Override
            public void onGetResponse(JSONObject response) {

                if (response!=null){
                    try {
                        success = response.optString("success");
                        msg = response.optString("msg");
                        Toast.makeText(AddCard.this, msg, Toast.LENGTH_SHORT).show();
                        if (success.equalsIgnoreCase("1")){
                            Util.closeKeyBoard(AddCard.this);
                            finish();
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
    public void onBackPressed() {
        //super.onBackPressed();
        mBinding.lnBack.performClick();
    }
}
