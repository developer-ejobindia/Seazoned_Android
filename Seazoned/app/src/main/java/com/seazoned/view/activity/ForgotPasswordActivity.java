package com.seazoned.view.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.seazoned.R;
import com.seazoned.app.AppData;
import com.seazoned.databinding.ActivityForgotPasswordBinding;
import com.seazoned.service.api.Api;
import com.seazoned.service.parser.PostDataParser;

import org.json.JSONObject;

import java.util.HashMap;

public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityForgotPasswordBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_forgot_password);

        mBinding.tvSubmit.setOnClickListener(this);
        mBinding.tvCancel.setOnClickListener(this);
        mBinding.lnBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if (v == mBinding.tvSubmit) {
            String mail = mBinding.etEmail.getText().toString();
            if(TextUtils.isEmpty(mail)){

                Toast.makeText(this, "Enter email", Toast.LENGTH_SHORT).show();
                return;
            }
            forgotPass(mail);

        } else if (v == mBinding.tvCancel) {

            finish();
        }
        else if(v==mBinding.lnBack){
            finish();
        }

    }

    private void forgotPass(final String mail) {

        HashMap<String, String> params = new HashMap<>();
        params.put("email", mail);
        params.put("profile_id", "2");
        new PostDataParser(ForgotPasswordActivity.this, Api.sForgotPass, params, true, new PostDataParser.OnGetResponseListner() {
            @Override
            public void onGetResponse(JSONObject response) {

                if (response != null) {


                    try {

                        String success, msg;
                        success = response.optString("success");
                        msg = response.optString("msg");
                        if (success.equalsIgnoreCase("1")) {

                            finish();
                            startActivity(new Intent(ForgotPasswordActivity.this, OTPActivity.class).putExtra("email",mail));
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        }
                        Toast.makeText(ForgotPasswordActivity.this, "" + msg, Toast.LENGTH_SHORT).show();

                    } catch (Exception e) {

                        e.printStackTrace();
                    }
                }

            }
        });

    }
}
