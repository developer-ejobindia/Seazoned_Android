package com.seazoned.view.activity;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.seazoned.R;
import com.seazoned.databinding.ActivityEditCardBinding;
import com.seazoned.service.api.Api;
import com.seazoned.service.parser.PostDataParser;
import com.seazoned.service.util.Util;

import org.json.JSONObject;

import java.util.HashMap;

public class EditCard extends AppCompatActivity implements View.OnClickListener {

    ActivityEditCardBinding mBinding;
    private int count = 0;
    private int expcount = 0;
    private HashMap<String, String> hashMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_edit_card);
        hashMap = (HashMap<String, String>) getIntent().getSerializableExtra("hashMapKey");
        mBinding.lnBack.setOnClickListener(this);
        mBinding.tvUpdateCard.setOnClickListener(this);
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

        setData();
    }

    private void setData() {

        mBinding.etCardHolderName.setText(hashMap.get("name"));
        mBinding.etCardNumber.setText(hashMap.get("card_no"));
        mBinding.etExpiryDate.setText(hashMap.get("month") + "/" + hashMap.get("year"));

    }

    @Override
    public void onClick(View view) {
        if (view == mBinding.lnBack) {
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        } else if (view == mBinding.tvUpdateCard) {
            String year = "";
            String month = "";
            String cardHolderName = mBinding.etCardHolderName.getText().toString().trim();
            String cardNumber = mBinding.etCardNumber.getText().toString().trim();
            String expiryDate = mBinding.etExpiryDate.getText().toString().trim();
            if (TextUtils.isEmpty(cardHolderName) || TextUtils.isEmpty(cardNumber) ||
                    TextUtils.isEmpty(expiryDate)) {
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

            HashMap<String, String> params = new HashMap<>();
            params.put("card_holder_name", cardHolderName);
            params.put("card_no", cardNumber);
            params.put("month", month);
            params.put("year", year);
            params.put("card_id", hashMap.get("card_id"));
            updateCard(params);
        }
    }

    private void updateCard(HashMap<String, String> params) {

        new PostDataParser(EditCard.this, Api.sEditCard, params, true, new PostDataParser.OnGetResponseListner() {
            @Override
            public void onGetResponse(JSONObject response) {

                if (response != null) {
                    try {
                        String success = response.optString("success");
                        String msg = response.optString("msg");
                        Toast.makeText(EditCard.this, msg, Toast.LENGTH_SHORT).show();
                        if (success.equalsIgnoreCase("1")) {
                            Util.closeKeyBoard(EditCard.this);
                            finish();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
